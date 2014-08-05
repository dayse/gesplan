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


package xfuzzy.xfdm;

import xfuzzy.lang.*;

/**
 * Implements an identification scheme for fuzzy inference systems
 * based on the "Improved Clustering for Function Approximation"
 * algorithm, as described in [1] which extends [2].
 *
 * [1] Guill\'{e]n, A., Gonz\'{a}lez, J., Rojas, I., Pomares, H., Herrera, L.~J., Valenzuela, O., Prieto, A. (2007)
 *     Using fuzzy logic to improve a clustering technique for function approximation.
 *     Neurocomputing, 70(16-18):2853-2860 
 *
 * [2] Gonz\'{a}lez, J., Rojas, I., Pomares, H., Ortega, J., and Prieto, A. (2002). 
 *     A new Clustering Technique for Function Aproximation. 
 *     IEEE Transactions on Neural Networks, 13(1):132–142.
 *
 * @author fedemp
 **/
public class XfdmICFA extends XfdmAlgorithm {

    // Whether to give verbose output/debug mode
    public static final boolean debug = false;

    // Initialize widths using a fixed radius
    public static final int WIDTH_INIT_FIXED_RADIUS = 1;
    // Inititalize width using the k-nn rule with k=1 (vector distance)
    public static final int WIDTH_INIT_KNN = 2;
    // Inititalize width using the k-nn rule with k=1 (dimension-wise distance)
    public static final int WIDTH_INIT_KNN_PER_DIMENSION = 3;
    // Initialize widths inversely proportional to the distortion
    public static final int WIDTH_INIT_DISTORTION = 4;

    public static final int width_init_scheme = WIDTH_INIT_DISTORTION;

    private int num_clusters;
    private double fuzziness;
    private int num_iterations;
    private boolean migration;

    private double norm_patterns[][];

    // See [1]
    private double clusters[][];
    private double degree_u[][];
    private double weight_w[][];
    private double distances_kjw[][];
    private double estimated_outputs[];

    /* Input dimension + output dimension */
    private int width;

    private double per_center_distortion[];
    private double average_distortion_before;
    private double average_distortion_migrated;

    double[] average_weight_per_center;

    private double[][] clusters_previous;
    private double variation;
    private double epsilon;
    private double radius;

    /**
     * Default constructor.
     **/
    public XfdmICFA() {
	this.migration = true;
	this.num_clusters = 10;
	this.fuzziness = 2.0;
	this.num_iterations = 25;
	this.epsilon = 0.01;
	this.radius = 0.1;
    }

    /**
     * Constructor intended to be used from the GUI system.
     **/
    public XfdmICFA(int num_clusters, int num_iterations, double fuzziness, double espsilon, boolean migration) {
	this.migration = migration;
	this.num_clusters = num_clusters;
	this.fuzziness = fuzziness;
	this.num_iterations = num_iterations;
	this.epsilon = epsilon;
	this.radius = 0.1;
    }

    /**
     * Constructor intended to be used from the configuration file parser.
     **/
    public XfdmICFA(double[] param) {
	this.num_clusters = (int) param[0];
	this.num_iterations = (int) param[1];
	this.fuzziness = param[2];
	this.epsilon = (double) param[3];
	this.migration = (0.0 != param[4]);
	this.radius = 0.1;
    }

    public Object clone() {
	return new XfdmICFA(num_clusters, num_iterations, fuzziness, epsilon, migration);
    }

    private void initializeMatrices() {
	// See [1]
	this.degree_u = new double[norm_patterns.length][clusters.length];
	this.distances_kjw = new double[norm_patterns.length][clusters.length];
	this.weight_w = new double[norm_patterns.length][clusters.length];
	this.estimated_outputs = new double[clusters.length];
	this.per_center_distortion = new double[num_clusters];
	this.average_weight_per_center = new double[num_clusters];
    }

    public int getNumberOfClusters() {
	return this.num_clusters;
    }

    public void setNumberOfClusters(int num) {
	this.num_clusters = num;
    }

    public int getNumberOfIterations() {
	return this.num_iterations;
    }

    public void setNumberOfIterations(int n) {
	this.num_iterations = n;
    }

    public double getFuzziness() {
	return this.fuzziness;
    }

    public void setFuzziness(double fuzz) {
	this.fuzziness = fuzz;
    }

    public double getEpsilon() {
	return this.epsilon;
    }

    public void setEpsilon(double e) {
	this.epsilon = e;
    }

    public boolean getMigration() {
	return this.migration;
    }

    public void setMigration(boolean m) {
	this.migration = m;
    }    

    public String toString() {
	return "Improved Clustering for Function Approximation (ICFA)";
    }

    public String toCode() {
	String c = "xfdm_algorithm(ICFA, " + num_clusters + ", " + num_iterations + ", " + fuzziness + ")";
	return c;
    }

    public void compute(Specification spec, XfdmConfig cfg) throws XflException {
	this.spec = spec;
	this.config = cfg;
	this.pattern = config.getPatterns();

	this.opset = createOperatorSet();
	spec.addOperatorset(opset);

	this.inputtype = createInputTypes();
	for (int i = 0; i < inputtype.length; i++) 
	    spec.addType(inputtype[i]);
	this.outputtype = createOutputTypes();
	for (int i = 0; i < outputtype.length; i++) 
	    spec.addType(outputtype[i]);

	this.rulebase = createEmptyRulebase();
	this.spec.addRulebase(rulebase);

	createSystemStructure();
	this.spec.setModified(true);
	this.pattern.setRanges(spec);
	this.width = config.numinputs + config.numoutputs; // or this.inputtype.length + this.outputtype.length;
	this.norm_patterns = createNormPatterns();
	this.clusters = createInitialClusters();

	initializeMatrices();
	try {
	    do_ICFA_Clustering();
	} catch(Exception e) {
	    System.out.println("Exception in ICFA identification: " + e);
	}
	createContent();
    }

    public boolean do_ICFA_Clustering() throws XflException {

	ICFA_InitWeights();
	if (debug) {
	    System.out.println("Weigths initialized");
	    System.out.println("Starting ICFA Clustering with " + num_iterations + " iterations for " + num_clusters + " clusters.");
	}

	this.variation = Double.POSITIVE_INFINITY;
	for ( int i = 0; (i < num_iterations) && (this.variation > this.epsilon); i++) {
	    if ( debug )
		System.out.println("ICFA Iteration starts " + i + ": ");

	    // save current clusters for later check of variation
	    clusters_previous = clusters.clone();

	    ICFA_UpdateDegrees_U();
	    ICFA_Update_Clusters_EstimatedOutputs_Weights();
	    computeVariation();

	    if (debug) {
		for (int c = 0; c < clusters.length; c++ ) {
		    System.out.print("Cl: " + c + ": ");
		    for ( int j = 0; j < clusters[0].length; j++ )  {
			System.out.print(clusters[c][j] + " ");
		    }
		    System.out.println("");
		}
	    }

	    if ( num_clusters > 1 && migration ) {
		ICFA_Migration();
		
	    }
	    if (debug)
		System.out.println("* Variation at the end of iteration " + i + ": " + this.variation + " (epsilon: " +
				   this.epsilon + ")");
	}
	return true;
    }
    
    private void ICFA_Migration()
    {		
	// Among the centers that have a distortion above the average, select the one with the smallest distortion
	int index_smallest_above_average = -1;
	double smallest_above_average = Double.POSITIVE_INFINITY;
	int index_largest_above_average = -1;
	double largest_above_average = Double.NEGATIVE_INFINITY;

	average_distortion_before = 0;
	for ( int j = 0; j < num_clusters; j++ ) {
	    per_center_distortion[j] = 0;
	    for ( int k = 0; k < norm_patterns.length; k++ ) {
		per_center_distortion[j] += Math.pow( degree_u[k][j], this.fuzziness ) * distances_kjw[k][j]
		    * weight_w[k][j] * weight_w[k][j];
	    }
	    
	    average_distortion_before += per_center_distortion[j];
	}
	average_distortion_before /= num_clusters;

	for ( int j = 0; j < num_clusters; j++ ) {
	    if (per_center_distortion[j] > average_distortion_before ) {

		if (per_center_distortion[j] > largest_above_average ) {
		    largest_above_average = per_center_distortion[j];
		    index_largest_above_average = j;
		}

		if (per_center_distortion[j] < smallest_above_average ) {
		    smallest_above_average = per_center_distortion[j];
		    index_smallest_above_average = j;			
		}
	    }
	}

	if ( index_largest_above_average != index_smallest_above_average &&
	     index_smallest_above_average >= 0 &&
	     index_largest_above_average >= 0 ) {
	    
	    // Migrate the cluster with the smallest distortion among those with distortion above the average.
	    clusters[index_smallest_above_average] = clusters[index_largest_above_average];

	    double distortion_change = Math.abs(average_distortion_migrated-average_distortion_before) / 
		average_distortion_before;
	    if (debug) {
		System.out.println("Migration selected, migrating " + index_smallest_above_average + " to " + 
				   index_largest_above_average + " -  for " + 
				   per_center_distortion[index_smallest_above_average] + ", " + 
				   per_center_distortion[index_largest_above_average]);
		
		System.out.println("Average dist. before: " + average_distortion_before + 
				   ", after: " + average_distortion_migrated +
				   ". Distortion change: " + distortion_change );
	    }
	    
	    if ( average_distortion_before > average_distortion_migrated ) {
		if (debug) {
		    System.out.println("Accepting migration");	    
		    for (int c = 0; c < clusters.length; c++ ) {
			System.out.print("Cl " + c + ": ");
			for ( int j = 0; j < clusters[0].length; j++ )  {
			    System.out.print(clusters[c][j] + " ");
			}
			System.out.println("");
		    }
		}
		
	    } else {
		// revert migration
		clusters = clusters_previous;
		if (debug)
		    System.out.println("Rejecting migration");		    
	    }
	    
	} else {
	    if (debug)
		System.out.println("Ignoring absurd migration between " + index_smallest_above_average +
				   " and " + index_largest_above_average);
	    average_distortion_migrated = average_distortion_before;
	}
    }


    private void computeVariation() {
	// compute max. variation to later check whether the clusters have changed significantly:
	double max_var = Double.NEGATIVE_INFINITY;
	for ( int j = 0; j < num_clusters; j++ ) {
	    for ( int k = 0; k < width; k++ ) {			
		double diff = Math.abs((clusters[j][k] - clusters_previous[j][k])/clusters_previous[j][k]);
		if ( diff > max_var )
		    max_var = diff;
	    }
	}
	this.variation = max_var;
    }
    

    private void ICFA_InitWeights()
    {
	// Iterate over input patterns
	for ( int j = 0; j < norm_patterns.length; j++ ) 
	    // Iterate over clusters
	    for ( int k = 0; k < num_clusters; k++ ) 
		weight_w[j][k] = 1;
    }


    private void ICFA_UpdateDegrees_U() {
	double pow = 1/(this.fuzziness - 1);
	// First, calculate the point-centers distances 
	// Iterate over patterns
	for ( int j = 0; j < norm_patterns.length; j++ ) {
	    // Iterate over clusters
	    for ( int k = 0; k < num_clusters; k++ ) {
		double dist = 
		    distances_kjw[j][k] = distance(norm_patterns[j],clusters[k]); // * weight_w[j][k];
	    }
	}

	// Iterate over patterns
	for ( int k = 0; k < norm_patterns.length; k++ ) {
	    // Iterate over clusters
	    for ( int j = 0; j < num_clusters; j++ ) {
		double pow_distances_sum = 0;
		// iterate over clusters again
		for ( int i = 0; i < num_clusters; i++ ) {
		    double squared_weight_w = weight_w[k][j]*weight_w[k][j];
		    double weighted_distances_frac = (distances_kjw[k][j]*squared_weight_w) /
			(distances_kjw[k][i]*squared_weight_w);
		    pow_distances_sum += Math.pow( weighted_distances_frac , pow);

		    degree_u[k][j] = 1/pow_distances_sum;
		}
	    }
	}
    }

    private void ICFA_Update_Clusters_EstimatedOutputs_Weights() {
	// Iterate over clusters
	for ( int j = 0; j < num_clusters; j++ ) {	
	    double[] sum_nom_cl = new double[clusters[0].length];
	    double sum_den_cl = 0;
	    double sum_nom_o = 0;
	    double sum_den_o = 0;
	    // Iterate over patterns
	    for ( int k = 0; k < norm_patterns.length; k++ ) {
		double squared_weight_w = weight_w[k][j]*weight_w[k][j];
		double pow_u = Math.pow(degree_u[k][j], fuzziness);
		double coef_cl = pow_u * squared_weight_w;
		sum_nom_cl = sum_vectors( sum_nom_cl, multiply_vector(coef_cl, norm_patterns[k]) );
		sum_den_cl += coef_cl;
		double coef_o = pow_u * distances_kjw[k][j];
		sum_nom_o += coef_o * norm_patterns[k][width - 1];  // width == clusters[0].length
		sum_den_o += coef_o;
	    }

	    // update clusters
	    clusters[j] = divide_vector(sum_nom_cl, sum_den_cl);
	    // update estimated outputs
	    estimated_outputs[j] = sum_nom_o / sum_den_o;
	    
	    // update weights
	    // Iterate over patterns
	    for ( int k = 0; k < norm_patterns.length; k++ ) {
		// get square weights; norm_patterns[k][width -1] is the output value in the training patterns
		weight_w[k][j] = Math.abs( norm_patterns[k][width -1] - estimated_outputs[j] );
	    }
	}
	
    }
    
    /**
     * Square euclidean distance.
     **/
    private double distance(double[] x, double[] y) {
	double dist = 0;
	for(int i = 0; i < Math.min(x.length, y.length); i++) 
	    dist += (x[i]-y[i]) * (x[i]-y[i]);
	return dist;
    }

    private double[] sum_vectors(double[] x, double[] y)
    {
	double result[] = new double[Math.min(x.length, y.length)];
	for (int i = 0 ; i < Math.min(x.length, y.length); i++ )
	    result[i] = x[i] + y[i];
	return result;
    }

    private double[] sub_vectors(double[] x, double[] y)
    {
	double result[] = new double[Math.min(x.length, y.length)];
	for (int i = 0 ; i < Math.min(x.length, y.length); i++ )
	    result[i] = x[i] - y[i];
	return result;
    }

    private double[] multiply_vector(double x, double[] v)
    {
	double result[] = new double[v.length];
	for(int i = 0; i < v.length; i++) 
	    result[i] = x * v[i];
	return result;
    }

    private double[] divide_vector(double[] v, double x)
    {
	double result[] = new double[v.length];
	for(int i = 0; i < v.length; i++) 
	    result[i] = v[i] /  x;
	return result;
    }

    /**
     * Patterns are normalized in the [0,1] range.
     **/
    private double[][] createNormPatterns() {
	int len = this.pattern.input.length;
	double normPatterns[][] = new double[len][this.width];
	double[] mins = new double[this.width];
	double[] maxes = new double[this.width];
	
	// compute mins and maxes
	for(int j = 0; j < width; j++) {
	    if ( j < inputtype.length ) {
		mins[j] = inputtype[j].min();
		maxes[j] = inputtype[j].max();
	    } else {
		mins[j] = outputtype[j-inputtype.length].min();
		maxes[j] = outputtype[j-inputtype.length].max(); 
	    }
	}

	// normalize in the [0,1] range
	for(int i=0; i < len; i++) 
	    for(int j = 0; j < width; j++) {
		if( j < inputtype.length) {
		    normPatterns[i][j] = (pattern.input[i][j]-mins[j])/(maxes[j]-mins[j]);
		} else {
		    normPatterns[i][j] = (pattern.output[i][j-inputtype.length]-mins[j])/(maxes[j]-mins[j]);
		}
	    }
	return normPatterns;
    }

    /**
     * Create and initialize clusters.
     *
     * The initial centers for the inputs are distributed equally through the input domains.
     * The centers for the output are fixed to 1.
     **/
    private double[][] createInitialClusters() {
	double cl[][] = new double[num_clusters][width];
	for ( int j = 0; j < inputtype.length; j++) {
	    for(int i = 0; i < num_clusters; i++) {
		cl[i][j] = 0 + ((double)i+1)/((double)num_clusters+1);
	    }
	}
	for ( int j = 0; j < outputtype.length; j++) {
	    for ( int i = 0; i < num_clusters; i++ ) {
		cl[i][inputtype.length + j] = 1;
	    }
	}

	if (debug) {
	    System.out.println("INITIAL CLUSTERS: ");
	    for (int c = 0; c < num_clusters; c++ ) {
		System.out.print("  Cl " + c + ": ");
		for ( int j = 0; j < cl[0].length; j++ )  {
		    System.out.print( cl[c][j] + " ");
		}
		System.out.println("");
	    }	    
	}
	return cl;
    }

    /* The following methods are here for historic reasons:
     createRules, createInputTypes, createOutputTypes.  They should be
     moved up in the hierarchy to the XfdmAlgorithm class, as the same
     implementation is shared by all the supported clustering
     schemes. */

    /**
     * Fill in the system specification.
     **/
    private void createContent() {

	if ( WIDTH_INIT_DISTORTION == this.width_init_scheme ) {
	    for ( int j = 0; j < num_clusters; j++ ) {
		average_weight_per_center[j] = 0;
		for ( int k = 0; k < norm_patterns.length; k++ ) {
		    average_weight_per_center[j] += weight_w[k][j];
		}
		average_weight_per_center[j] /= norm_patterns.length;		
	    }
	}

	for(int i = 0; i < inputtype.length; i++) 
	    createBells(inputtype[i],i);
	for(int i=0; i<outputtype.length; i++) {
	    switch(config.systemstyle.defuz) {
	    case XfdmSystemStyle.FUZZYMEAN:
		createSingletons(outputtype[i],inputtype.length + i);
		break;
	    case XfdmSystemStyle.WEIGHTED:
		// TODO?
		//createBells(outputtype[i],inputtype.length + i);
		break;
	    case XfdmSystemStyle.TAKAGI:
		// TODO?
		//createParametric(outputtype[i],inputtype.length + i);
		break;
	    }
	}
	createRules();
    }

    private void createRules() {
	Variable ivar[] = rulebase.getInputs();
	Variable ovar[] = rulebase.getOutputs();
	int is = Relation.IS;

	for(int i = 0; i < clusters.length; i++) {
	    LinguisticLabel pmf = ivar[0].getType().getAllMembershipFunctions()[i];
	    Relation rel = Relation.create(is,null,null,ivar[0],pmf,rulebase);
	    for(int j = 1; j < ivar.length; j++) {
		pmf = ivar[j].getType().getAllMembershipFunctions()[i];
		Relation nrel = Relation.create(is,null,null,ivar[j],pmf,rulebase);
		rel = Relation.create(Relation.AND,rel,nrel,null,null,rulebase);
	    }
	    Rule rule = new Rule(rel);
	    for(int j = 0; j < ovar.length; j++) {
		pmf = ovar[j].getType().getAllMembershipFunctions()[i];
		rule.add(new Conclusion(ovar[j],pmf,rulebase));
	    }
	    rulebase.addRule(rule);
	}
    }

    protected Type[] createInputTypes() {
	Type itp[] = new Type[config.numinputs];
	for(int i = 0; i < config.numinputs; i++) {
	    String tname = "Tin"+i;
	    Universe universe = null;
	    if(config.commonstyle.isUniverseDefined()) {
		try { universe=new Universe(config.commonstyle.min,config.commonstyle.max);}
		catch(Exception ex) {}
	    }

	    if(config.inputstyle != null && config.inputstyle.length > i &&
	       config.inputstyle[i] != null) {
		tname = "T"+config.inputstyle[i].name;
		if(config.inputstyle[i].isUniverseDefined()) {
		    try {
			universe=new Universe(config.inputstyle[i].min,config.inputstyle[i].max);
		    } catch(Exception ex) {
		    }
		} else {
		    universe = null;
		}
	    }

	    if(universe == null) 
		universe = pattern.getUniverse(i,true);
	    itp[i] = new Type(tname,universe);
	}
	return itp;
    }

    private Type[] createOutputTypes() {
	Type otp[] = new Type[config.numoutputs];
	
	for(int i = 0; i < config.numoutputs; i++) {
	    String tname = "T" + config.systemstyle.outputname;
	    if(config.numoutputs > 1) 
		tname += ""+i;
	    otp[i] = new Type(tname,pattern.getUniverse(i,false));
	}
	return otp;
    }

    private void createSingletons(Type type, int index) {
	Universe u = type.getUniverse();
	double min = u.min();
	double max = u.max();
	for(int cl = 0; cl < clusters.length; cl++) {
	    ParamMemFunc pmf = new pkg.xfl.mfunc.singleton();
	    pmf.set("mf" + cl, u);
	    pmf.set( min + clusters[cl][index]*(max-min) );
	    try { 
		type.add(pmf); 
	    } catch(XflException e) {}
	}
    }
 
    private void createBells(Type type, int index) {
	Universe u = type.getUniverse();
	double min = u.min();
	double max = u.max();
	double param[] = new double[2];
	for(int i = 0; i < clusters.length; i++) {
	    ParamMemFunc pmf = new pkg.xfl.mfunc.bell();
	    pmf.set("mf"+i, u);
	    param[0] = min + clusters[i][index]*(max-min);

	    if ( WIDTH_INIT_FIXED_RADIUS == width_init_scheme ) {
		// 1st alternative: fix radius, as in subtractive clustering.  
		param[1] = radius*(max - min)/2;
	    } else if ( (WIDTH_INIT_KNN == width_init_scheme) || (WIDTH_INIT_KNN_PER_DIMENSION == width_init_scheme) ) {
		// 2nd alternative: k-NN: 1-nearest neighbor (using either overall or per-dimesion distance)
		// Find the nearest neighbor
		int neighbor_index = -1;
		double max_dist = Double.NEGATIVE_INFINITY;
		for (int j = 0; j < norm_patterns.length; j++ ) {
		    if ( distances_kjw[j][i] > max_dist) {
			max_dist = distances_kjw[j][i];
			neighbor_index = j;
		    }
		}
		if ( WIDTH_INIT_KNN == width_init_scheme ) { 
		    // 2. k-NN overall distance: this uses overall distance
		    param[1] = distances_kjw[neighbor_index][i] * (max-min)/2;
		} else { // if ( WIDTH_INIT_KNN_PER_DIMENSION == width_init_scheme ) { 
		    // 3. * k-NN per-dimension distance:  and this uses per-dimension distance
		    // 3 IS BETTER THAN 2 (and than 1), but WORSE THAN 4
		    double per_dimension_neighbor_distance = 
			Math.abs(norm_patterns[neighbor_index][index] - clusters[i][index]);
		    // square it, like in distances_kjw
		    per_dimension_neighbor_distance *= per_dimension_neighbor_distance;
		    param[1] = per_dimension_neighbor_distance * (max-min)/2;
		}
	    } else { //if ( WIDTH_INIT_DISTORTION == width_init_scheme ) {
		// 4th alternative: radius inversely proportional to the distortion.
		// Note the same widths are used for all the inputs of a certain rule
		param[1] = (max-min)/2/average_weight_per_center[i];
	    } // One more possibility, use the distortion: param[1] = (max-min)/2/per_center_distortion[i];

	    try { 
		pmf.set(param); 
		type.add(pmf); 
	    } catch(XflException ex) {}
	}
    }

}