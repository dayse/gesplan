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

import xfuzzy.xfdm.*;
import xfuzzy.xfsl.*;

/**
 * Configuration of an xftsp run. Normally an object of this class
 * will be created and populated by the XftspConfigParser javaCC
 * parser.
 *
 * Sample config file:
 *
 * xftsp_series_name(estsp07)
 * xftsp_training_file("estsp07-training.txt")
 * xftsp_test_file("estsp07-test.txt")
 * xftsp_id_algorithm(WangMendel)
 * xftsp_opt_algorithm(Rprop, 0.1, 1.5, 0.5)
 * xftsp_option(tolerance,0)
 * xftsp_option(max_exploration,15)
 * xftsp_selection("selection_10")
 * xftsp_nrve("nrve_10")
 * 
 * @author fedemp
 **/
public class XftspConfig
{
    public String series_name;
    public File training_series_file, test_series_file, 
	selection_file, nrve_file;
    public int min_horiz, max_horiz, horiz_gap;
    // Implied: public int n_outputs; 1
    public XfdmInputStyle input_style;
    public XfdmSystemStyle system_style;
    public XfdmAlgorithm id_algorithm;
    public XfslAlgorithm opt_algorithm;

    public double [][] selection;
    public int [] num_vars_selected;
    public double [] nrve;

    // xftsp options
    public double tolerance;
    public int max_exploration, max_prediction_horizon;

    // Generate files with the training/test errors for the optimization
    // iterations.
    public boolean generate_optimization_logs;
    // Whether to keep the (selected) pattern files used for 
    // identification and optimization.
    public boolean keep_pattern_files;

    /**
     * Basic and only constructor.
     **/
    public void XftspConfig() {
	series_name = null;
	training_series_file = test_series_file = 
	    //training_file = test_file = 
	    selection_file = nrve_file = null;
	id_algorithm = null;
	opt_algorithm = null;
	tolerance = 0;
	max_exploration = 16;
	max_prediction_horizon = 10;
	generate_optimization_logs = false;
	keep_pattern_files = false;
    }

    /**
     * Get an XftspConfig object as text. @TODO
     **/
    public String toTxt() { 
	String eol = System.getProperty("line.separator", "\n");
	String result = "";

	if ( series_name != null ) {
	    result += "xftsp_series_name(\"" + series_name +"\")" + eol;
	}
	if ( training_series_file != null ) {
	    result += "xftsp_training_file(\"" + training_series_file.getAbsolutePath() +"\")" + eol;
	}
	if ( test_series_file != null ) {
	    result += "xftsp_test_file(\"" + test_series_file.getAbsolutePath() +"\")" + eol;
	}
	return result;
    }
}
