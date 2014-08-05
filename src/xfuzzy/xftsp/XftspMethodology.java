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
import java.util.*;
import java.text.*;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.xfdm.*;
import xfuzzy.xfsl.*;

/**
 * Implements a long-term time series prediction methodology. Requires
 * a nonparametric estimation of noise (or residual variance) and can
 * use any per-horizon variable selection.
 *
 * For more details on how it works, see these papers:
 *
 * F. Montesino, A. Lendasse, and A. Barriga, "Fuzzy Inference Based
 * Autoregressors for Time Series Prediction Using Nonparametric
 * Residual Variance Estimation," 17th IEEE International Conference
 * on Fuzzy Systems (FUZZ-IEEE'2008).
 *
 * F. Montesino-Pouzols, A. Lendasse, and A. Barriga, "xftsp: a Tool
 * for Time Series Prediction by Means of Fuzzy Inference Systems,"
 * 4th IEEE International Conference on Intelligent Systems (IS08).
 *
 * F. Montesino-Pouzols and A. Barriga, "Regressive Fuzzy Inference
 * Models with Clustering Identification: Application to the ESTSP08
 * Competition," 2nd European Symposium on Time Series Prediction
 * (ESTSP08).
 *
 * @author fedemp
 **/
public class XftspMethodology {

    private double optimization_error_decrease_threshold = 1.0E-09;
    private String step_subdir_basename = "xftsp-step-";

    private XftspConfig conf;
    private XftspLogger logger;

    /**
     * Basic and only constructor.
     **/
    public XftspMethodology() { }

    /**
     * Apply this methodology.
     * 
     * @return array of fuzzy regressor specifications.
     **/
    public Specification[] apply(XftspConfig conf, XftspLogger logger, XftspDialog window) throws XflException{
	this.logger = logger;
	// Check and prepare the configuration object
	if ( null == conf.id_algorithm || null == conf.opt_algorithm ) {
	    logger.logln("Fatal error: incomplete configuration");
	    System.exit(-1);
	}
	conf.selection = readMatrixFile(conf.selection_file);
	conf.nrve = readVectorFile(conf.nrve_file);
	if ( conf.nrve.length != conf.selection.length ) {
	    logger.logln("Error: the number of rows of the NRVE and selection files do not match");
	    System.exit(-1);
	}
	logger.logln("Series name: " + conf.series_name);
	logger.logln("Training series file: " + conf.training_series_file.getAbsolutePath());
	if ( null != conf.test_series_file ) {
	    logger.logln("Test series file: " + conf.training_series_file.getAbsolutePath());
	} else {
	    logger.logln("No test series file provided.");
	}
	logger.logln("NRVE file: " + conf.nrve_file + " " + conf.nrve.length);
	logger.logln("Selection file: " + conf.selection_file + " " + conf.selection.length + 
		     " " + conf.selection[0].length);

	return do_methodology(conf,window);
    }

    protected Specification[] do_methodology(XftspConfig conf, XftspDialog window) {
	// dialog is just ignored.
	this.conf = conf;
	if(null == conf) {
	    System.exit(-1);
	}
	conf.min_horiz = 1;
	conf.horiz_gap = 1;
	conf.max_horiz = conf.nrve.length;

	double[] predictions = new double[conf.max_horiz];
	conf.num_vars_selected = new int[conf.max_horiz - conf.min_horiz + 1];
	// Vector of final errors for every step
	double[][] final_step_errors = new double[10][conf.max_horiz];
	// Specification of fuzzy regressors
	Specification[] specs = new Specification[conf.max_horiz];

	File basedir = new File(System.getProperty("user.dir"));
	for ( int h=conf.min_horiz; h <= conf.max_horiz; h+= conf.horiz_gap ) {
	    if ( null != window )
		window.printMessage("Horizon " + h + "\n");

	    int num_inputs_before_selection = conf.selection[0].length;
	    double[] errors = null;
	    int complexity = 0;

	    // mkdir && "cd" for every prediction horizon
	    String subdirname = step_subdir_basename + h;
	    new File(subdirname).mkdir();
	    String current_subdir = subdirname + "/";
	    Specification s_id, s_opt;
	    boolean model_selection = false;
	    /* Results from the iterative identification+optimization process */
	    int[] step_mfs = new int[conf.max_exploration];
	    int[] step_rules = new int[500];
	    // Vector of errors for the current step (several complexities)
	    double[][] step_errors = new double[conf.max_exploration][10];
	    logger.logln("-> Step/horizon " + h);
	    String var_selection_string = getVarSelectionString(conf,h);
	    int num_vars_selected = conf.num_vars_selected[h-1];
	    logger.logln("Selected " + num_vars_selected + " variables: " + var_selection_string);

	    String selected_training_filename = current_subdir + conf.training_series_file.getName() + 
		"-" + num_vars_selected + "i1o-" + h + "step---" + var_selection_string;
	    logger.logln("Training pattern file (after selection): " + selected_training_filename);
	    File selected_training_file = new File(selected_training_filename);
	    generate_selected_pattern_file(conf.training_series_file, var_selection_string, h, 
					   selected_training_file);


	    String selected_test_filename = "";
	    File selected_test_file = null;
	    if ( null != conf.test_series_file ) {
		selected_test_filename = current_subdir + conf.test_series_file.getName() + "-" + 
		    num_vars_selected + "i1o-" + h + "step---" + var_selection_string;
		logger.logln("Test pattern file (after selection): " + selected_test_filename);
		selected_test_file = new File(selected_test_filename);
		generate_selected_pattern_file(conf.test_series_file, var_selection_string, h, 
					       selected_test_file);
	    }

	    // Iterative process: identification, optimization, complexity selection
	    do { 
		String id_alg = getShortAlgorithmID(conf.id_algorithm);
		String ofilename_base = conf.series_name + "-" + h + "sa" + "." + 
		    id_alg + "." + complexity;
		String ofilename = current_subdir + ofilename_base + ".xfl";
		File ofile = new File(ofilename);

		// 1st substage
		s_id = identification(conf, h, num_vars_selected, selected_training_file, ofile, complexity);

		step_mfs[complexity] = s_id.getSystemModule().getInputs()[0].getType().getMembershipFunctions().length;
		step_rules[complexity] = s_id.getRulebases()[0].getRules().length;

		String opt_ofilename = current_subdir + 
		    ofilename_base + "." + getShortAlgorithmID(conf.opt_algorithm) + 
		    ".opt.xfl"; 
		File opt_ofile = new File(opt_ofilename);
		File opt_logfile = new File(opt_ofilename + ".log");

		// 2nd substage
		s_opt = optimization(conf, h, s_id, selected_training_file, selected_test_file, 
				     opt_ofile, opt_logfile, complexity, step_errors[complexity]);
		specs[h-1] = s_opt;

		// 3rd substage
		model_selection = do_model_selection(conf, h, complexity, step_errors[complexity]);
		if ( !model_selection) {
			complexity++;
		} else {
		    for (int i = 0; i < step_errors[0].length; i++) {
			// Get the errors for the complexity selected
			final_step_errors[i][h-1] = step_errors[complexity][i];
		    }
		    copy_file(opt_ofile, new File(current_subdir + conf.series_name + "-" + h + "sa-regressor.xfl"));
		}

	    } while ( !model_selection ); 

	    if ( !conf.keep_pattern_files ) {
		if (selected_training_file.exists() && selected_training_file.canWrite()) {
		    selected_training_file.delete();
		}
		if (selected_test_file.exists() && selected_test_file.canWrite()) {
		    selected_test_file.delete();
		}
	    }

	    logger.logln("\n* Results: ");
	    logger.logln("MF      & rules & Trn. MSE \t& Test MSE \t& Trn. MxAE \t& Test MxAE \\\\\\hline\\hline");
	    for (int i = 0; i <= complexity; i ++) {
		NumberFormat formatter = new DecimalFormat("0.##########E00");
		logger.logln(step_mfs[i] + "\t& " + 
			     step_rules[i] + "\t& " + 
			     formatter.format(step_errors[i][1]) + "\t& " + 
			     formatter.format(step_errors[i][5]) + "\t& " +
			     formatter.format(step_errors[i][3]) + "\t& " +
			     formatter.format(step_errors[i][7]) + " \\\\\\hline");
	    }

	    /* Predict next values after the training set */
	    double[] trn_series = readVectorFile(conf.training_series_file);
	    String [] selected_strings = var_selection_string.split("-");
	    if ( num_vars_selected != selected_strings.length ) {
		logger.log("Error while analyzing selected variables: " + num_vars_selected + 
			   " - " + selected_strings.length);
		System.exit(-1);
	    }
	    // Note the reverse order assignment. In the XFL files, in[0] is the "older" input
	    double[] last_input_pattern = new double[selected_strings.length];
	    for (int k = 0; k < selected_strings.length ; k++) {
		int index = Integer.parseInt(selected_strings[k]);
		last_input_pattern[num_vars_selected - 1 - k ] = trn_series[trn_series.length - 1 - index + 1];
	    }

	    double prediction[] = new double[1];
	    SystemModule sm = s_opt.getSystemModule();
	    if ( null == sm ) {
		logger.logln("System module null!");
	    }
	    prediction = s_opt.getSystemModule().crispInference(last_input_pattern);
	    predictions[h-1] = prediction[0];
	    logger.logln("\nPrediction:  " + prediction[0] );
	    logger.logln("-----------------------------------------------------------------------------------------------\n");
	    generate_step_summary_vs_mf_files(current_subdir, complexity, 
					      step_mfs, step_rules, step_errors);
	}

	logger.logln("Predictions: [ ");
	for ( int i = (conf.min_horiz - 1); i < conf.max_horiz; i++ ) {
	    logger.log(predictions[i] + " ");
	}
	logger.logln("]");
	// Generate files with miscellaneous info about the systems generated
	generate_summary_file("trn_mse_steps", final_step_errors[1]);
	generate_summary_file("trn_mxae_steps", final_step_errors[3]);
	generate_summary_file("tst_mse_steps", final_step_errors[5]);
	generate_summary_file("tst_mxae_steps", final_step_errors[7]);
	write_vector_with_index_file("trn_prediction_steps", predictions);
	generate_summary_file("varn_trn_mse_steps",do_varn_errors(conf.training_series_file,final_step_errors[1]));
	generate_summary_file("varn_tst_mse_steps",do_varn_errors(conf.test_series_file,final_step_errors[5]));
	
	// return the regressor fuzzy systems
	return specs;
    }

    /**
     * Identification substage
     **/
    protected Specification identification(XftspConfig conf, int horizon, int num_vars_selected,
					File pattern, File ofile, int complexity) {
	Specification result = null;

	/* Set configuration for the identification process */
	XfdmConfig minerConfig = new XfdmConfig();
	minerConfig.patternfile = pattern;
	minerConfig.numinputs = num_vars_selected;
	minerConfig.numoutputs = 1;

	minerConfig.commonstyle = new XfdmInputStyle();
	minerConfig.commonstyle.name = "input";
	/* Scheme for increasing complexity */
	String id_alg = getShortAlgorithmID(conf.id_algorithm);
	if ( id_alg.equals("WangMendel") ){
	    minerConfig.algorithm = XfdmAlgorithm.create("WangMendel", null);
	    minerConfig.commonstyle.mfs = complexity + 2;
	} else if ( id_alg.equals("ICFA") ) {
	    minerConfig.commonstyle.mfs = complexity + 1;
	    double [] options = { complexity+1, 25, 2.0, 0.01, 1 };
	    minerConfig.algorithm = XfdmAlgorithm.create("ICFA", options);
	} else if ( id_alg.equals("SubClustering") ) {
	    minerConfig.commonstyle.mfs = complexity + 1;
	    double [] options = { complexity+1, 0.1 };
	    minerConfig.algorithm = XfdmAlgorithm.create("IncClustering", options);
	} else if ( id_alg.equals("HCM") ) {
	    minerConfig.commonstyle.mfs = complexity + 1;
	    // # of clusters, limit of iterations, fuzziness index, limit of cluster variation, learning-option
	    double [] options = { complexity+1, 20, 2.0, 0.01, 0 };
	    minerConfig.algorithm = XfdmAlgorithm.create("HardCMeans", options);
	} else if ( id_alg.equals("FCM") ) {
	    minerConfig.commonstyle.mfs = complexity + 1;
	    double [] options = { complexity+1, 20, 2.0, 0.01, 0 };
	    minerConfig.algorithm = XfdmAlgorithm.create("CMeans", options);
	} else if ( id_alg.equals("GustafsonKessel") ) {
	    minerConfig.commonstyle.mfs = complexity + 1;
	    double [] options = { complexity+1, 20, 2.0, 0.01, 0 };
	    minerConfig.algorithm = XfdmAlgorithm.create("GustafsonKessel", options);
	} else if ( id_alg.equals("GathGeva") ) {
	    minerConfig.commonstyle.mfs = complexity + 1;
	    double [] options = { complexity+1, 20, 2.0, 0.01, 0 };
	    minerConfig.algorithm = XfdmAlgorithm.create("GathGeva", options);
	} else {
	    minerConfig.commonstyle.mfs = complexity + 1;
	}

	minerConfig.commonstyle.style = XfdmInputStyle.FREE_GAUSSIANS;
	minerConfig.commonstyle.min = 0;
	minerConfig.commonstyle.max = 0;
	    
	minerConfig.systemstyle = new XfdmSystemStyle();
	minerConfig.systemstyle.rulebase = "global";
	minerConfig.systemstyle.outputname = "prediction";
	minerConfig.systemstyle.conjunction = XfdmSystemStyle.MIN;
	minerConfig.systemstyle.defuz = XfdmSystemStyle.FUZZYMEAN;
	minerConfig.systemstyle.creation = true;

	logger.logln("* Performing identification (with " + num_vars_selected + " inputs) " + 
		     "using " + conf.id_algorithm.toString());
	Specification spec = new Specification(ofile);
	Xfdm miner = new Xfdm(null,spec);
	try {
	    minerConfig.algorithm.compute(spec,minerConfig);
	    result = spec;
	} catch(Exception e) {
	    logger.logln("Exception in identification: " + e);
	}

	result.save_as(ofile);
	logger.logln("Identification finished, identified " + 
		     result.getRulebases()[0].getRules().length + " rules.");

	return result;
    }

    /**
     * Optimization substage
     **/
    protected Specification optimization(XftspConfig config, int horizon, Specification sId, 
				      File pattern_file, File test_file, 
				      File ofile, File logfile,
				      int complexity, double step_errors[]) {
	Specification result = null;

	XfslPattern training_pattern;
	XfslPattern test_pattern;

	/* Set configuration for the optimization process */
	conf.opt_algorithm.getName();
	XfslConfig optConfig = new XfslConfig();
	optConfig.trainingfile = pattern_file; 
	if (null != test_file) {
	    optConfig.testfile = test_file;
	}
	optConfig.outputfile = ofile;
	if ( conf.generate_optimization_logs ) {
	    optConfig.logfile = new XfslLog(logfile, new Vector()); 
	} else {
	    optConfig.logfile = new XfslLog(); 	    
	}
	optConfig.algorithm = conf.opt_algorithm;
	// Reinit required. Otherwise parameters from the previous run (step) may be kept.
	optConfig.algorithm.reinit();
	try { 
	    optConfig.errorfunction = new XfslErrorFunction(XfslErrorFunction.MEAN_SQUARE_ERROR);
	} catch(XflException e) {
	    logger.logln("XFL exception in optimization: " + e.toString());
	}
	optConfig.preprocessing = new XfspProcess(0);
	optConfig.postprocessing = new XfspProcess(1);
	optConfig.endcondition = new XfslEndCondition();
	optConfig.endcondition.setLimit(XfslEndCondition.TRN_VAR,optimization_error_decrease_threshold); 
	double nrve = conf.nrve[horizon - 1];
	optConfig.endcondition.setLimit(XfslEndCondition.TRN_ERROR, getNRVEAdjustment(conf,horizon)*nrve);


	int alg_code = optConfig.algorithm.getCode();
	switch ( alg_code ) {
	case XfslAlgorithm.RPROP: 
	    optConfig.endcondition.setLimit(XfslEndCondition.EPOCH,2000);
	    break;
	case XfslAlgorithm.SCALED: 
	    optConfig.endcondition.setLimit(XfslEndCondition.EPOCH,1000);
	    break;
	case XfslAlgorithm.ANNEALING: 
	case XfslAlgorithm.BLIND: 
	case XfslAlgorithm.POWELL: 
	    optConfig.endcondition.setLimit(XfslEndCondition.EPOCH,5000);
	    break;
	default: 
	    optConfig.endcondition.setLimit(XfslEndCondition.EPOCH,12000);
	}

	Rulebase rulebase = sId.getRulebases()[0];
	logger.logln("* Performing optimization (with " + rulebase.getInputs().length + " inputs and " +
		     rulebase.getRules().length +" rules) " + 
		     "using " + conf.opt_algorithm.getName());

	// do optimization
	XfslThread optThread = new XfslThread(sId, optConfig);
	try {
	    optThread.learning();
	} catch (XflException e) {
	    logger.logln("XFL exception in optimization: " + e.toString());
	}

	// Save specification and check.
	Specification threadSpec = optThread.getSpecification(); 
	XfslStatus final_status = optThread.getStatus();
	XfslEvaluation trn_final_errors = final_status.trn;
	XfslEvaluation tst_final_errors = final_status.tst;
	if ( XfslStatus.FINISHED == final_status.status ) {
	    step_errors[0] = final_status.epoch;
	    step_errors[1] = trn_final_errors.error;
	    step_errors[2] = trn_final_errors.rmse;
	    step_errors[3] = trn_final_errors.mxae;
	    step_errors[4] = trn_final_errors.var;

	    if (final_status.testing) {
		step_errors[5] = tst_final_errors.error;
		step_errors[6] = tst_final_errors.rmse;
		step_errors[7] = tst_final_errors.mxae;
		step_errors[8] = tst_final_errors.var;
	    }
	}
	threadSpec.save_as(ofile);
	result = threadSpec;
	if ( null == result ) {
	    logger.logln("Error, result from optimization is null!: " + result + 
			 " (file: " + ofile.getAbsolutePath() + ")");
	}
	logger.logln("Optimization finished");




	return result;
    }

    /**
     * Optimization substage
     *
     * Makes a decision on the basis of the nonparametric residual
     * variance estimation
     **/
    public boolean do_model_selection(XftspConfig conf, int horizon, int complexity, double[] errors) {
	boolean result = false;

	// The double array has the following elements:
	// (iter, trn_error, trn_rmse, trn_mxae, trn_var, tst_error, tst_rmse, tst_mxae, tst_var)
	double trn_mse = errors[1];
	double nrve = conf.nrve[horizon-1];
	double adj = getNRVEAdjustment(conf, horizon);
	double threshold = adj*nrve;
	NumberFormat formatter = new DecimalFormat("0.##########E00");
	logger.logln("Trn MSE: " + formatter.format(trn_mse) + ", Tst MSE: " + formatter.format(errors[5]) + 
		     " | Threshold: " + formatter.format(threshold)  +" (" + adj + " * " + formatter.format(nrve) + ")");
	if ( (trn_mse <= threshold) || (complexity + 1 >= conf.max_exploration) ) {
	    result = true;
	}

	return result;
    }

    /**
     * Residual variance estimation (Delta Test) based threshold.
     **/
    public double getNRVEAdjustment(XftspConfig conf, int h)
    {
	double result = 0;
	if ( 0 == conf.tolerance )
	    result = (1 + java.lang.Math.min(0.90,0.15*h));
	else
	    result = conf.tolerance;

	return result;
    }

    public String getShortAlgorithmID(XfdmAlgorithm alg) {
	String alg_substring = "";
	String name = alg.toString();
	if ( name.substring(0,13).equals("Wang & Mendel") ) {
	    alg_substring = "WangMendel";
	} else if ( name.substring(0,22).equals("Incremental Clustering") ) {
	    alg_substring = "SubClustering";
	} else if ( name.substring(0,35).equals("Fixed Clustering (CMeans Algorithm)") ) {
	    alg_substring = "FCM";
	} else if ( name.substring(0,38).equals("Fixed Clustering (Gath-Geva Algorithm)") ) {
	    alg_substring = "GathGeva";
	} else if ( name.substring(0,40).equals("Fixed Clustering (Hard-CMeans Algorithm)") ) {
	    alg_substring = "HCM";
	} else if ( name.substring(0,45).equals("Fixed Clustering (Gustafson-Kessel Algorithm)") ) {
	    alg_substring = "GustafsonKessel";
	} else if ( name.substring(0,53).equals("Improved Clustering for Function Approximation (ICFA)") ) {
	    alg_substring = "ICFA";
	} else {
	    alg_substring = "alg";
	}
	return alg_substring;
    }

    public String getShortAlgorithmID(XfslAlgorithm alg) {
	String alg_substring = "";
	String name = alg.getName();
	if ( name.length() >= 5 && name.substring(0,5).equals("RProp") ) {
	    alg_substring = "RProp";
	} else if ( name.length() >= 19 && name.substring(0,19).equals("Marquardt-Levenberg") ) {
	    alg_substring = "Levenberg";
	} else if ( name.length() >= 25 && name.substring(0,25).equals("Scaled Conjugate Gradient") ) {
	    alg_substring = "Scaled_Conjugate_Gradient";
	} else if ( name.length() >= 2 && name.substring(0,22).equals("Backprop with Momentum") ) {
	    alg_substring = "Backprop_momentum";
	} else if ( name.length() >= 22 && name.substring(0,22).equals("QuickProp") ) {
	    alg_substring = "QuickProp";
	} else if ( name.length() >= 19 && name.substring(0,19).equals("Simulated Annealing") ) {
	    alg_substring = "Simul_Annealing";
	} else if ( name.length() >= 12 && name.substring(0,12).equals("Blind Search") ) {
	    alg_substring = "Blind";
	} else if ( name.length() >= 6 && name.substring(0,6).equals("Powell") ) {
	    alg_substring = "Powell";
	} else {
	    alg_substring = "alg";
	}
	return alg_substring;
    }

    public String getVarSelectionString(XftspConfig conf, int h)
    {
	int num_vars = 0;
	String result = "";
	int number_inputs = conf.selection[h-1].length;
	for (int i =  (number_inputs - 1); i >= 0; i-- ) {
	    if ( 0 != conf.selection[h-1][i] ) {
		num_vars ++;
		if ( result.equals("") )  // first number
		    result += (number_inputs -i);
		else  // not first, add separator
		    result += "-" + (number_inputs - i);
	    }
	}
	conf.num_vars_selected[h-1] = num_vars;
	return result;
    }
    
    public void generate_selected_pattern_file(File series_file, String selection_string, 
					       int h, File selected_series_file) 
    {
	int n_outputs = 1;
	double[] series = readVectorFile(series_file);

	BufferedWriter writer = null;
	try {
	    writer = new BufferedWriter(new FileWriter(selected_series_file.getAbsolutePath()));
	} catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}
	String[] vars_selected = selection_string.split("-");
	int max_input_index = Integer.parseInt(vars_selected[(vars_selected.length -1)]);

	NumberFormat formatter = new DecimalFormat("0.##########E00");
	/* Loop over patterns (lines in the input pattern file) */
	for ( int i = max_input_index; i < (series.length - n_outputs - (h-1) + 1); i++ ) {
	    String output_line = "";
	    
	    /* Loop over inputs */
	    for (int j = (vars_selected.length - 1); j >= 0; j--) {
		int input_index = Integer.parseInt(vars_selected[j]);
		output_line += formatter.format(series[i - input_index]) + " "; 
	    }
	    /* Output */
	    output_line += formatter.format(series[i + (h - 1)]);
	    output_line.trim();
	    output_line += "\n";
	    try {
		writer.write(output_line);
	    } catch(IOException e) { 
		logger.logln("Error writing to file: " + e);
	    }
	}
	try {
		writer.flush();
		writer.close();
	    }  catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}
    }

    public double [] readVectorFile(File f)
    {
	double [] m = null;
	
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(f));
	    try {
		String l;
		do {
		    l = reader.readLine();
		} while (null != l && l.trim().equals(""));

		ArrayList list = new ArrayList();
		while ( null != l && !l.trim().equals("") ) {
		    list.add(l.trim());
		    l = reader.readLine();		
		}
		m = getRowFromStringList(list);
	    } catch (IOException e) {
		logger.logln("Error reading vector file: " + e);
	    }
	    reader.close();
	} catch(IOException e) {
	    logger.logln("I/O error accessing vector file: " + e);
	}
	
	return m;
    }

    public double [][] readMatrixFile(File f)
    {
	double [][] m = null;
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(f));
	    try {
		String l;
		do {
		    l = reader.readLine();
		} while (null != l && l.trim().equals(""));

		ArrayList list = new ArrayList();
		while ( null != l && !l.trim().equals("") ) {
		    list.add(l.trim());
		    l = reader.readLine();		
		}
		m = getMatrixFromStringList(list);
		reader.close();
	    } catch (IOException e) {
		logger.logln("Error reading matrix file: " + e);
	    } 
	} catch(IOException e) {
	    logger.logln("I/O error accessing matrix file: " + e);
	}
	return m;
    }

    private double [] getRowFromStringList(ArrayList list)
    {
	double [] r = null;
	int rows = list.size();
	if ( 0 < rows ) {
	    String current = null;
	    r = new double[rows];
	    try {
		for ( int i = 0; i < rows; i++ ) {
		    String l = (String)list.get(i);
		    r[i] = Double.parseDouble(l);
		}
	    } catch ( NumberFormatException e ) {
		logger.logln("Could not interpret " + current + " as a number");
	    }
	}
	return r;
    }

    private double [][] getMatrixFromStringList(ArrayList list)
    {
	double [][] m = null;
	int rows = list.size();
	if ( 0 < rows ) {
	    String current = null;
	    m = new double[rows][];
	    try {
		for ( int i = 0; i < rows; i++ ) {
		    String l = (String)list.get(i);
		    String[] doubleVector = l.split("\\s+");
		    int columns = doubleVector.length;
		    m[i] = new double[columns];
		    for ( int j = 0; j < columns; j++ ) {
			current = doubleVector[j];
			m[i][j] = Double.parseDouble(current);
		    }
		}

	    } catch ( NumberFormatException e ) {
		logger.logln("Could not interpret " + current + " as a number");
	    }
	}
	return m;
    }

    public double[] getLastLineFromLog(File opt_logfile)
    {
	double[] result = null;
	// last line
	String ll = null;
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(opt_logfile));
	    try  {
		String l = null;
		do {
		    if ( !reader.ready() ) {
			ll = l;
			l = null;
		    } else {
			 l = reader.readLine();
		    }
		} while (null != l);
 	    } catch (IOException e) {
		logger.logln("Error reading log file: " + e);
	    } 
	} catch(IOException e) {
	    logger.logln("I/O error accessing log file: " + e);
	}

	String[] strings = ll.split("\\s+");
	result = new double[strings.length];
	for ( int i = 0; i < strings.length; i++ ) {
	    result[i] = Double.parseDouble(strings[i]);
	}
	return result;
    }


    private double[] do_varn_errors(File series_file, double[] errors)
    {
	double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY, avg_accum = 0, avg = 0, range = 0;

	double[] series = readVectorFile(series_file);
	for ( int i = 0; i < series.length; i++) {
	    avg_accum += series[i];
	    if ( series[i] > max ) 
		max = series[i];
	    if ( series[i] < min ) 
		min = series[i];
	}
	avg = avg_accum / series.length;
	range = max - min;

	double var_accum = 0;
	for ( int i = 0; i < series.length; i++) {
	    double diff = series[i] - avg;
	    var_accum += diff * diff;
	}
	double var = var_accum/series.length;
	double [] varn_errors = new double[errors.length];
	for (int i = 0; i < varn_errors.length; i++) {
	    varn_errors[i] = errors[i]/var * (range*range);
	}

	return varn_errors;
    }

    public void generate_summary_file(String filename, double[] values)
    {
	BufferedWriter writer = null;
	try {
	    writer = new BufferedWriter(new FileWriter(filename));
	} catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}

	NumberFormat formatter = new DecimalFormat("0.##########E00");
	/* Loop over patterns (lines in the input pattern file) */
	for ( int i = 0; i < values.length; i++ ) {
	    try {
		writer.write(formatter.format(values[i]) + "\n");
	    } catch(IOException e) { 
		logger.logln("Error writing to file: " + e);
	    }
	}
	try {
	    writer.flush();
	    writer.close();
	}  catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}
    }

    public void generate_step_summary_vs_mf_files(String current_subdir, int complexity, 
						  int[] step_mfs, int[] step_rules, 
						  double[][] step_errors)
    {
	BufferedWriter writer_rules = null, writer_trn_mse = null, writer_tst_mse  = null;
	try {
	    writer_rules = new BufferedWriter(new FileWriter(current_subdir + "rules_vs_mf"));
	    writer_trn_mse = new BufferedWriter(new FileWriter(current_subdir + "trn_error_vs_mf"));
	    writer_tst_mse = new BufferedWriter(new FileWriter(current_subdir + "tst_error_vs_mf"));
	} catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}

	NumberFormat formatter = new DecimalFormat("0.##########E00");
	/* Loop over patterns (lines in the input pattern file) */
	for ( int i = 0; i <= complexity; i++ ) {
	    try {
		writer_rules.write(step_mfs[i] + " " + step_rules[i] + "\n");
		writer_trn_mse.write(step_mfs[i] + " " + 
				     formatter.format(step_errors[i][1]) + "\n");
		writer_tst_mse.write(step_mfs[i] + " " + 
				     formatter.format(step_errors[i][5]) + "\n");
	    } catch(IOException e) { 
		logger.logln("Error writing to file: " + e);
	    }
	}

	try {
	    writer_rules.flush();
	    writer_trn_mse.flush();
	    writer_tst_mse.flush();

	    writer_rules.close();
	    writer_trn_mse.close();
	    writer_tst_mse.close();
	}  catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}

    }

    public void write_vector_with_index_file(String filename, double[] values)
    {
	BufferedWriter writer = null;
	try {
	    writer = new BufferedWriter(new FileWriter(filename));
	} catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}

	NumberFormat formatter = new DecimalFormat("0.##########E00");
	/* Loop over patterns (lines in the input pattern file) */
	for ( int i = 0; i < values.length; i++ ) {
	    try {
		writer.write(i + " " + formatter.format(values[i]) + "\n");
	    } catch(IOException e) { 
		logger.logln("Error writing to file: " + e);
	    }
	}
	try {
	    writer.flush();
	    writer.close();
	}  catch(IOException e) { 
	    logger.logln("Error writing to file: " + e);
	}
    }

    private void copy_file(File src, File dst) {
	try {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);


	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	} catch(IOException e) {
	    logger.log("Error while copying file: " + e);
	}
    }
}