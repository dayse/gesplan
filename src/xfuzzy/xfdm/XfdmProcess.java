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


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//		HILO DEL PROCESO DE IDENTIFICACION		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.*;
import xfuzzy.lang.*;

import java.io.*;

public class XfdmProcess extends Thread {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private Xfdm xfdm;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 XfdmProcess(Xfdm xfdm) {
  super("xfdm thread 1");
  this.xfdm = xfdm;
  start();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public void run() {
  XfdmConfig config = xfdm.getConfig();
  Specification spec = xfdm.getSpecification();
  try { config.algorithm.compute(spec,config); }
  catch(Exception ex) { System.out.println("" + ex); return; }
  spec.save();
 }

 //-------------------------------------------------------------//
 // Ejecucion externa						//
 //-------------------------------------------------------------//

 public static void main(String args[]) {
  if (args.length < 1 || args.length > 2 ) {
      print_usage();
      System.exit(-1);
  } else {
      File path = new File(System.getProperty("user.dir"));
      File file = new File(path,args[0]);
      Specification spec = null;
      if (2 == args.length ) {
	  // Console mode
	  File cfgfile = new File(path,args[1]);
	  if(!cfgfile.exists()) {
	      System.out.println("Can't find file " + cfgfile.getAbsolutePath());
	  } else {
	      XfuzzyConfig xfcparser = new XfuzzyConfig();
	      XfdmConfig xfdmc = xfcparser.parseXfdmConfig(cfgfile);
	      if(xfdmc == null) {
		  System.out.println("Configuration parser: " + xfcparser.resume());
		  System.exit(-1);
	      }
	      if ( !file.exists() ) {
		  // Create .xfl file with the result from the mining process
		  try { 
		      file.createNewFile();
		  } catch (IOException e) {
		      System.out.println("Cannot create new XFL file: " + e);
		  }
	      }
	      spec = new Specification(file);
	      Xfdm miner = new Xfdm(null, spec);
	      miner.setConfig(xfdmc);
	      XfdmProcess proc = new XfdmProcess(miner);
	      try { 
		  proc.join();
	      } catch(InterruptedException e) {
		  System.out.println("Thread interrupted: " + e);
	      }
	      System.exit(0);
	  }
      } else { // 1 == args.length
	  // GUI mode
	  System.out.println("Starging Graphical User Interface...");
	  if(!file.exists()) {
	      spec = new Specification(file);
	  } else {
	      XflParser parser = new XflParser();
	      spec = parser.parse(file.getAbsolutePath());
	      if(spec == null) { System.out.println("XFL parser: " + parser.resume()); System.exit(-1); }
	  }
	  Xfdm mining = new Xfdm(null,spec);
	  mining.setVisible(true);
      }
  }
 }

 public static void print_usage() {
     System.out.println("\nConsole mode usage:\n\txfdm xfl_file configuration_file\nGUI usage:\n\txfdm xfl_file");
 }

}
