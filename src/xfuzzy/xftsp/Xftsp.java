//--------------------------------------------------------------------------------//
//                               COPYRIGHT NOTICE                                 //
//--------------------------------------------------------------------------------//
// Copyright (c) 2012, Instituto de Microelectronica de Sevilla (IMSE-CNM)        //
//                                                                                //
// All rights reserved.                                                           //
//                                                                                //
// Redistribution and use in source and binary forms, with or without             //
// modification, are permitted provided that the following  conditions are met:   //
//                                                                                //
//     * Redistributions of source code must retain the above copyright notice,   //
//       this list of conditions and the following disclaimer.                    // 
//                                                                                //
//     * Redistributions in binary form must reproduce the above copyright        // 
//       notice, this list of conditions and the following disclaimer in the      //
//       documentation and/or other materials provided with the distribution.     //
//                                                                                //
//     * Neither the name of the IMSE-CNM nor the names of its contributors may   //
//       be used to endorse or promote products derived from this software        //
//       without specific prior written permission.                               //
//                                                                                //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"    //
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE      //
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE // 
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE  //
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL     //
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR     //
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER     //
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  //
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  //
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.           //
//--------------------------------------------------------------------------------//

package xfuzzy.xftsp;

import java.io.*;

import xfuzzy.lang.*;
import xfuzzy.util.*;

import xfuzzy.xfdm.*;
import xfuzzy.xfsl.*;
import xfuzzy.xftsp.cfgparser.*;

/**
 * Main class of the xftsp tool. It's got a main method and is
 * runnable.
 *
 * @author fedemp
 **/
public class Xftsp implements Runnable {
    private XftspConfig conf;
    private XftspDialog window;

    private String log_filename = "xftsp-run-results.log";

    public static final String copyrightMessage = "xftsp   Copyright (C) 2007,2008,2009 Federico Montesino Pouzols <fedemp@altern.org>\nThis program comes with ABSOLUTELY NO WARRANTY.\nThis is free software, and you are welcome to redistribute it\nunder certain conditions; see the license for details.";

    /**
     * Command line mode constructor
     **/   
    public Xftsp(XftspConfig conf) {
	this.conf = conf;
	this.window = null;
    }

    /**
     * GUI mode constructor
     **/   
    public Xftsp(XftspConfig conf, XftspDialog window) {
	this.conf = conf;
	this.window = window;
    }

    /**
     * Standalone execution
     **/
    public static void main(String args[]) throws Exception {
	// Up to 2 args: first is config file; second is "no-max-depth"   ( + -g for gui mode)
	boolean done = false;

	if ( args.length >= 1 ) {
	    if ( args[0].equals("-g") ) {
		if ( 2 == args.length ) {
		    do_gui_mode(args[0],args[1]);
		    done = true;
		}
	    } else {
		if ( args.length <= 2 ) {
		    do_command_line_mode(args[0], 
					 (args.length == 2)?  args[1] : null);
		    done = true;
		}
	    }
	} 

	if (!done) {
	    printUsage();
	    System.exit(-1);
	}

	System.exit(0);
    }

    /**
     * Runnable method
     **/
    public void run() {
	XftspLogger logger = new XftspLogger();
	XftspMethodology m = new XftspMethodology();
	try {
	    Specification [] specs = m.apply(conf, logger, window);
	} catch(XflException e) {
	    if (null != this.window) {
		this.window.printMessage(e.toString());
	    } else {
		System.out.println(e.toString());
	    }
	}
    }

    private static void do_command_line_mode(String config_filename, String depth) throws FileNotFoundException {
	XftspLogger logger = new XftspLogger();
	long initial_milliseconds = 0, final_milliseconds = 0;

	logger.logln("xftsp 0.31 [java flavor]");
	logger.logln("Read parameters: " + config_filename + " " + depth);
	try {
	    java.net.InetAddress local = java.net.InetAddress.getLocalHost();
	    System.out.println("Hostname: " + local.getHostName());
	}
	catch (java.net.UnknownHostException uhe) {
	    System.out.println("Couldn't get local hostname");
	}
	logger.logln("Running in: " + System.getProperty("user.dir"));
	java.util.Calendar c = java.util.Calendar.getInstance();
	initial_milliseconds = c.getTimeInMillis();
	logger.logln("Starting: " + c.getTime() + " = " + initial_milliseconds);

	File cfgfile = new File(config_filename);
	if (! cfgfile.exists() ) {
	    logger.logln("Configuration file: " + config_filename + ", not found.");
	    System.exit(-1);
	}
	XftspConfigParser parser = new XftspConfigParser(new FileInputStream(cfgfile));
	logger.log("\nParsing configuration file: " + config_filename + "... ");
	XftspConfig cfg = new XftspConfig();
	try {
	    cfg = parser.parse(cfgfile);
	} catch (Exception e) {
	    logger.logln("Parse exception: " + e.toString());
	}
	if ( null == cfg ) {
	    System.out.print(parser.resume());
	    System.exit(-1);
	}
	logger.logln("Parser finished successfully.");

	do_tsp(cfg, logger);

	final_milliseconds = java.util.Calendar.getInstance().getTimeInMillis();
	logger.logln("Finishing: " + c.getTime() + " = " + final_milliseconds);
	logger.logln("Total milliseconds ellapsed: " + (final_milliseconds-initial_milliseconds));
	logger.close();
    }

    private static void do_gui_mode(String config_filename, String depth) {
	XftspDialog window = new XftspDialog();
	XftspConfig conf = window.askConfig();

	do_tsp(conf, null);
    }

    /**
     * Creates and an XftspMethodology object and applies it on the
     * user provided configuration.
     **/
    public static boolean do_tsp(XftspConfig cfg, XftspLogger logger) {
	if ( null == logger ) {
	    logger = new XftspLogger();
	}

	logger.logln("\nApplying time series prediction methodology...");
	XftspMethodology m = new XftspMethodology();
	try {
	    Specification [] specs = m.apply(cfg,logger,null);
	} catch(XflException e) {
	    logger.logln(e.toString());
	    System.exit(-1);
	}
	return false;
    }

    private static void printUsage() { 
	System.out.println(copyrightMessage);
	System.out.println("Usage: \n" +
			   "\t(Console mode usage)\n" +
			   "xftsp cfg_file [max_depth*]\n" +
			   "\t(GUI usage)\n" +
			   "xftsp -g [file.xfl]\n");
    }
}