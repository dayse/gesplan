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
//		ALGORITMOS DE CLUSTERING FIJO		 	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;
import xfuzzy.xfsl.*;

public class XfdmFixedClustering extends XfdmAlgorithm {

 // Whether to give verbose output/debug mode
 public static final boolean debug = false;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int HARD_CMEANS = 0;
 public static final int CMEANS = 1;
 public static final int GUSTAFSON_KESSEL = 2;
 public static final int GATH_GEVA = 3;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int clustering;
 private int num_clusters;
 private int num_iterations;
 private double fuzziness;
 private double epsilon;
 private boolean learning;

 private int width;
 private double point[][];
 private double cluster[][];
 private double degree[][];
 private double old_degree[][];

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfdmFixedClustering() {
  this.clustering = HARD_CMEANS;
  this.num_clusters = 10;
  this.num_iterations = 10;
  this.fuzziness = 2.0;
  this.epsilon = 0.01;
  this.learning = false;
 }

 //-------------------------------------------------------------//
 // Constructor desde la interfaz grafica			//
 //-------------------------------------------------------------//

 public XfdmFixedClustering(int algorithm, int clusters, int iterations,
                            double fuzziness, double epsilon, boolean lrn) {
  this.clustering = algorithm;
  this.num_clusters = clusters;
  this.num_iterations = iterations;
  this.fuzziness = fuzziness;
  this.epsilon = epsilon;
  this.learning = lrn;
 }

 //-------------------------------------------------------------//
 // Constructor desde el fichero de configuracion		//
 //-------------------------------------------------------------//

 public XfdmFixedClustering(int algorithm, double[] param) {
  this.clustering = algorithm;
  this.num_clusters = (int) param[0];
  this.num_iterations = (int) param[1];
  this.fuzziness = param[2];
  this.epsilon = param[3];
  this.learning = (((int) param[4]) == 1);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //		Metodos de acceso a la configuracion		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene el codigo del algoritmo de clustering elegido	//
 //-------------------------------------------------------------//

 public int getClustering() {
  return this.clustering;
 }

 //-------------------------------------------------------------//
 // Asigna el codigo del algoritmo de clustering		//
 //-------------------------------------------------------------//

 public void setClustering(int algorithm) {
  this.clustering = algorithm;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del numero de clusters elegido		//
 //-------------------------------------------------------------//

 public int getNumberOfClusters() {
  return this.num_clusters;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del numero de clusters			//
 //-------------------------------------------------------------//

 public void setNumberOfClusters(int num) {
  this.num_clusters = num;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del indice de borrosidad		 	//
 //-------------------------------------------------------------//

 public double getFuzziness() {
  return this.fuzziness;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del indice de borrosidad		 	//
 //-------------------------------------------------------------//

 public void setFuzziness(double fuzz) {
  this.fuzziness = fuzz;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del numero maximo de iteraciones		//
 //-------------------------------------------------------------//

 public int getNumberOfIterations() {
  return this.num_iterations;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del numero maximo de iteraciones		//
 //-------------------------------------------------------------//

 public void setNumberOfIterations(int num) {
  this.num_iterations = num;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del minimo grado de variacion	 	//
 //-------------------------------------------------------------//

 public double getEpsilon() {
  return this.epsilon;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del minimo grado de variacion	 	//
 //-------------------------------------------------------------//

 public void setEpsilon(double degree) {
  this.epsilon = degree;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor de la opcion de aprendizaje		//
 //-------------------------------------------------------------//

 public boolean getLearning() {
  return this.learning;
 }

 //-------------------------------------------------------------//
 // Asigna el valor de la opcion de aprendizaje		 	//
 //-------------------------------------------------------------//

 public void setLearning(boolean lrn) {
  this.learning = lrn;
 }

 //=============================================================//
 //		Metodos de desarrollo de XfdmAlgorithm		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene un duplicado del objeto				//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfdmFixedClustering(clustering, num_clusters, num_iterations,
				 fuzziness, epsilon, learning);
 }

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public String toString() {
  switch(clustering) {
   case HARD_CMEANS: return "Fixed Clustering (Hard-CMeans Algorithm)";
   case CMEANS: return "Fixed Clustering (CMeans Algorithm)";
   case GUSTAFSON_KESSEL:return "Fixed Clustering (Gustafson-Kessel Algorithm)";
   case GATH_GEVA: return "Fixed Clustering (Gath-Geva Algorithm)";
   default: return "";
  }
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  String code = "xfdm_algorithm(";
  switch(clustering) {
   case HARD_CMEANS: code += "HardCMeans,"; break;
   case CMEANS: code += "CMeans,"; break;
   case GUSTAFSON_KESSEL: code += "GustafsonKessel,"; break;
   case GATH_GEVA: code += "GathGeva,"; break;
  }
  code += " "+num_clusters+", "+num_iterations+", "+fuzziness+", "+epsilon;
  code += ", "+(learning? "1" : "0")+" )";
  return code;
 }

 //-------------------------------------------------------------//
 // Metodo que construye el sistema a partir de los datos	//
 //-------------------------------------------------------------//

 public void compute(Specification spec,XfdmConfig config) throws XflException {
  this.spec = spec;
  this.config = config;
  this.pattern = config.getPatterns();

  this.opset = createOperatorSet();
  spec.addOperatorset(opset);

  this.inputtype = createInputTypes();
  for(int i=0; i<inputtype.length; i++) spec.addType(inputtype[i]);
  this.outputtype = createOutputTypes();
  for(int i=0; i<outputtype.length; i++) spec.addType(outputtype[i]);
  this.rulebase = createEmptyRulebase();
  this.spec.addRulebase(rulebase);
  createSystemStructure();
  this.spec.setModified(true);
  this.pattern.setRanges(spec);

  this.width = this.inputtype.length + this.outputtype.length;
  this.point = createPointsFromPatterns();
  this.cluster = createClusters();
  this.degree = new double[point.length][cluster.length];
  this.old_degree = new double[point.length][cluster.length];

  clustering();

  createContent();

  if(learning) learning();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //		Metodos que desarrollan el algoritmo		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Aplica el algoritmo de clustering elegido			//
 //-------------------------------------------------------------//

 private void clustering() {
  switch(clustering) {
   case HARD_CMEANS:      HardCMeans(); break;
   case CMEANS:           CMeans(); break;
   case GUSTAFSON_KESSEL: GustafsonKessel(); break;
   case GATH_GEVA:        GathGeva(); break;
  }
 }

 //-------------------------------------------------------------//
 // Obtiene el conjunto normalizado de patrones			//
 //-------------------------------------------------------------//

 private double[][] createPointsFromPatterns() {
  int length = this.pattern.input.length;
  double point[][] = new double[length][width];
  for(int i=0; i<length; i++) for(int j=0; j<width; j++) {
   if(j<inputtype.length) {
    point[i][j] = pattern.input[i][j];
   } else {
    point[i][j] = pattern.output[i][j-inputtype.length];
   }
  }
  return point;
 }
 
 //-------------------------------------------------------------//
 // Obtiene una representacion inicial aleatoria de los clusters//
 //-------------------------------------------------------------//

 private double[][] createClusters() {
  int width = inputtype.length + outputtype.length;
  double cl[][] = new double[num_clusters][width];
  for(int j=0; j<inputtype.length; j++) {
   double min = inputtype[j].getUniverse().min();
   double max = inputtype[j].getUniverse().max();
   for(int i=0; i<num_clusters; i++) {
    cl[i][j] = min + Math.random()*(max-min);
   }
  }
  for(int j=0; j<outputtype.length; j++) {
   double min = outputtype[j].getUniverse().min();
   double max = outputtype[j].getUniverse().max();
   for(int i=0; i<num_clusters; i++) {
    cl[i][inputtype.length+j] = min + Math.random()*(max-min);
   }
  }
  return cl;
 }

 //-------------------------------------------------------------//
 // Intercambia old_cluster y cluster				//
 //-------------------------------------------------------------//

 private void setOldDegree() {
  double[][] aux;
  aux = old_degree;
  old_degree = degree;
  degree = aux;
 }

 //-------------------------------------------------------------//
 // Calcula la variacion de clusters entre dos iteraciones	//
 //-------------------------------------------------------------//

 private double computeVariation() {
  double max = 0;
  for(int i=0; i<degree.length; i++) for(int j=0; j<degree[i].length; j++) {
   double diff = degree[i][j] - old_degree[i][j];
   if(diff < 0) diff = -diff;
   if(diff > max) max = diff;
  }
  return max;
 }

 //-------------------------------------------------------------//
 // Calcula la distancia euclidea entre dos puntos (al cuadrado)//
 //-------------------------------------------------------------//

 private double distance(double[] x, double[] y) {
  double dist = 0;
  for(int i=0; i<x.length; i++) dist += (x[i]-y[i])*(x[i]-y[i]);
  return dist;
 }

 //=============================================================//
 //	 Metodos que desarrollan el algoritmo HardCMeans	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Algoritmo HardCMeans					//
 //-------------------------------------------------------------//

 private void HardCMeans() {
  double var = epsilon+1;
  for(int iter=0; iter<num_iterations && (epsilon<0 || var>epsilon); iter++) {
   setOldDegree();
   HardCMeansUpdateDegree();
   HardCMeansUpdateClusters();
   var = computeVariation();
  }
 }

 //-------------------------------------------------------------//
 // Actualiza el grado de pertenencia de los puntos a los	//
 // clusters							//
 //-------------------------------------------------------------//

 private void HardCMeansUpdateDegree() {
  for(int i=0; i<point.length; i++) {
   double min = distance(point[i],cluster[0]);
   int index = 0;
   for(int j=1; j<cluster.length; j++) {
    double dist = distance(point[i],cluster[j]);
    if(dist < min) { min = dist; index = j; }
   }
   for(int j=0; j<cluster.length; j++) {
    degree[i][j] = (j==index? 1.0 : 0.0);
   }
  }
 }

 //-------------------------------------------------------------//
 // Actualiza el valor de los clusters 				//
 //-------------------------------------------------------------//

 private void HardCMeansUpdateClusters() {
     for(int i=0; i<cluster.length; i++) {
	 for(int j=0; j<cluster[i].length; j++) cluster[i][j] = 0.0;
	 int sum = 0;
	 for(int k=0; k<degree.length; k++) if(degree[k][i] == 1.0) {
		 sum++;
		 for(int j=0; j<cluster[i].length; j++) cluster[i][j] += point[k][j];
	     } 
	 if(sum != 0) {
	     for(int j=0; j<cluster[i].length; j++) {
		 cluster[i][j] = cluster[i][j]/sum;
	     }  
	 } else {
	     /// Instead using 0 and center, we place the "empty" cluster at the last point
	     for(int j=0; j<cluster[i].length; j++) {
		 cluster[i][j] = point[point.length-1][j];
	     }  		
	 }
     }
 }

 //=============================================================//
 //	 Metodos que desarrollan el algoritmo CMeans		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Algoritmo CMeans						//
 //-------------------------------------------------------------//

 private void CMeans() {
  double var = epsilon+1;
  for(int iter=0; iter<num_iterations && (epsilon<0 || var>epsilon); iter++) {
   setOldDegree();
   CMeansUpdateDegree();
   updateClusters();
   var = computeVariation();
  }
 }

 //-------------------------------------------------------------//
 // Actualiza el grado de pertenencia de los puntos a los	//
 // clusters							//
 //-------------------------------------------------------------//

 private void CMeansUpdateDegree() {
  double dist[] = new double[cluster.length];
  for(int i=0; i<point.length; i++) {
   for(int j=0; j<cluster.length; j++) dist[j] = distance(point[i],cluster[j]);
   updateDegree(i,dist);
  }
 }

 //=============================================================//
 //  Metodos que desarrollan el algoritmo de Gustafson-Kessel	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Algoritmo Gustafson-Kessel					//
 //-------------------------------------------------------------//

 private void GustafsonKessel() {
  double[][][] covar = new double[cluster.length][width][width];
  double[][][] inverse = new double[cluster.length][width][width];
  double[] determ = new double[cluster.length];
  CMeansUpdateDegree();

  double var = epsilon+1;
  for(int iter=0; iter<num_iterations && (epsilon<0 || var>epsilon); iter++) {
   updateClusters();
   updateCovariance(covar);
   updateDeterminant(covar, determ);
   updateInverse(covar, inverse);
   setOldDegree();
   GustafsonKesselUpdateDegree(determ, inverse);
   var = computeVariation();
  }
 }

 //-------------------------------------------------------------//
 // Calcula la distancia segun el algoritmo de GustafsonKessel	//
 //-------------------------------------------------------------//

 private double distanceGK(double[] x,double[] y,double determ,double[][] inv) {
  double alpha = Math.pow(determ,1/width);
  double dist = 0;
  for(int i=0; i<width; i++) for(int j=0; j<width; j++) {
   dist += (x[i]-y[i])*inv[i][j]*(x[j]-y[j]);
  }
  return alpha*dist;
 }

 //-------------------------------------------------------------//
 // Actualiza el grado de pertenencia de los puntos a los	//
 // clusters							//
 //-------------------------------------------------------------//

 private void GustafsonKesselUpdateDegree(double[] determ,double[][][] inverse){
  double dist[] = new double[cluster.length];
   
  for(int i=0; i<point.length; i++) {
   for(int j=0; j<cluster.length; j++) {
    dist[j] = distanceGK(point[i],cluster[j],determ[j],inverse[j]);
   }
   updateDegree(i,dist);
  }
 }

 //=============================================================//
 //	Metodos que desarrollan el algoritmo de Gath-Geva	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Algoritmo Gath-Geva						//
 //-------------------------------------------------------------//

 private void GathGeva() {
  double[][][] covar = new double[cluster.length][width][width];
  double[][][] inverse = new double[cluster.length][width][width];
  double[] determ = new double[cluster.length];
  double[] rho = new double[cluster.length];
  CMeansUpdateDegree();

  double var = epsilon+1;
  for(int iter=0; iter<num_iterations && (epsilon<0 || var>epsilon); iter++) {
      if ( debug ) 
	  System.out.println("ITERACION "+iter);
      updateClusters();
      if ( debug )
	  debug("CLUSTERS",cluster);
      updateCovariance(covar);
      if ( debug ) 
	  debug("COVARIANZA",covar);
      updateDeterminant(covar, determ);
      if ( debug )
	  debug("DETERMINANTE",determ);
      updateInverse(covar, inverse);
      if ( debug )
	  debug("INVERSA",inverse);
      updateRho(rho);
      if ( debug )
	  debug("RHO",rho);
      setOldDegree();
      GathGevaUpdateDegree(rho, determ, inverse);
      if ( debug )
	  debug("PERTENENCIA",degree);
      var = computeVariation();
  }
 }

 //-------------------------------------------------------------//
 //-------------------------------------------------------------//

 private void debug(String matrix, double[][] cl) {
  System.out.println("");
  System.out.println(matrix);
  for(int i=0; i<cl.length; i++) {
   for(int j=0; j<cl[i].length; j++) System.out.print(""+cl[i][j]+"  ");
   System.out.println("");
  }
  System.out.println("");
 }

 private void debug(String vector, double[] v) {
  System.out.println(vector);
  for(int i=0; i<v.length; i++) System.out.println(""+v[i]+"  ");
  System.out.println("");
 }

 private void debug(String matrix, double[][][] cl) {
  System.out.println("");
  System.out.println(matrix);
  for(int i=0; i<cl.length; i++) {
   System.out.println("Matriz "+i);
   for(int j=0; j<width; j++) {
    for(int k=0; k<width; k++) System.out.print(""+cl[i][j][k]+"  ");
    System.out.println("");
   }
   System.out.println("");
  }
  System.out.println("");
 }

 //-------------------------------------------------------------//
 // Actualiza el vector Rho					//
 //-------------------------------------------------------------//

 private void updateRho(double[] rho) {
  for(int i=0; i<cluster.length; i++) {
   double sum = 0.0;
   for(int j=0; j<point.length; j++) sum += degree[j][i];
   rho[i] = sum/point.length;
  }
 }

 //-------------------------------------------------------------//
 // Calcula la distancia segun el algoritmo de GathGeva		//
 //-------------------------------------------------------------//

 private double distanceGG(double[] x,double[] y,double alpha,double[][] inv) {
  double dist = 0;
  for(int i=0; i<width; i++) for(int j=0; j<width; j++) {
   dist += (x[i]-y[i])*inv[i][j]*(x[j]-y[j]);
  }
  return alpha*Math.exp(dist/2);
 }

 //-------------------------------------------------------------//
 // Actualiza el grado de pertenencia de los puntos a los	//
 // clusters							//
 //-------------------------------------------------------------//

 private void GathGevaUpdateDegree(double[] rho,double[] determ,
                                   double[][][] inverse) {
  double dist[] = new double[cluster.length];
  double alpha[] = new double[cluster.length];
  for(int j=0; j<cluster.length; j++) alpha[j] = Math.sqrt(determ[j])/rho[j];
   
  for(int i=0; i<point.length; i++) {
   for(int j=0; j<cluster.length; j++) {
    dist[j] = distanceGG(point[i],cluster[j],alpha[j],inverse[j]);
   }
   updateDegree(i,dist);
  }
 }

 //=============================================================//
 //	    Metodos auxiliares de calculo de matrices		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Actualiza el valor de los clusters 				//
 //-------------------------------------------------------------//

 private void updateClusters() {
     for(int i=0; i<cluster.length; i++) {
	 for(int j=0; j<cluster[i].length; j++) cluster[i][j] = 0.0;
	 double sum = 0;
	 for(int k=0; k<degree.length; k++) {
	     double alpha = Math.pow(degree[k][i],fuzziness);
	     sum += alpha;
	     for(int j=0; j<cluster[i].length; j++) {
		 cluster[i][j] += alpha*point[k][j];
	     }
	 } 
	 if(sum != 0) {
	     for(int j=0; j<cluster[i].length; j++) {
		 cluster[i][j] = cluster[i][j]/sum;
	     }
	 } else {
	     /// Instead of centering at 0, we center the "empty" cluster at the last point
	     for(int j=0; j<cluster[i].length; j++) {
		 cluster[i][j] = point[point.length-1][j];
	     }  		
	 }
     } 
 }
     
 //-------------------------------------------------------------//
 // Calcula la covarianza					//
 //-------------------------------------------------------------//

 private void updateCovariance(double[][][] covar) {
  double alpha = 0.0;
  double denom[][] = new double[width][width];
  for(int i=0; i<cluster.length; i++) {
   double num = 0.0;
   for(int l=0;l<width; l++) for(int m=0;m<width; m++) {
    denom[l][m] = 0.0;
   }
   for(int j=0; j<point.length; j++) {
    alpha = Math.pow(degree[j][i],fuzziness);
    for(int l=0;l<width; l++) for(int m=0;m<width; m++) {
     double beta = (point[j][l] - cluster[i][l])*(point[j][m] - cluster[i][m]);
     denom[l][m] += alpha*beta;
    }
    num += alpha;
   }
   for(int l=0;l<width; l++) for(int m=0;m<width; m++) {
    covar[i][l][m] = denom[l][m]/num;
   }
  }
 }

 //-------------------------------------------------------------//
 // Calcula los determinantes de las matrices de covarianza	//
 //-------------------------------------------------------------//

 private void updateDeterminant(double[][][] covar, double[] determ) {
  for(int c=0; c<cluster.length; c++) {
   double[][] temp = new double[width][width];
   for(int i=0;i<width;i++) for(int j=0;j<width;j++) temp[i][j]=covar[c][i][j];

   for(int k=0; k<(temp.length-1); k++)
    for(int i=k+1; i<temp.length; i++)
     for(int j=k+1; j<temp.length; j++)
      temp[i][j]-=temp[i][k]*temp[k][j]/temp[k][k];
   double determinant = 1.0;
   for(int i=0; i<temp.length; i++) determinant*=temp[i][i];
   determ[c] = determinant;
  }
 }

 //-------------------------------------------------------------//
 // Calcula las matrices inversas de las covarianzas		//
 //-------------------------------------------------------------//

 private void updateInverse(double[][][] covar, double[][][] inverse) {
  for(int cl=0; cl<cluster.length; cl++) {
   double[][] temp = new double[width][width];
   for(int i=0;i<width;i++) for(int j=0;j<width;j++) temp[i][j]=covar[cl][i][j];

   double[][] b=new double[width][width];
   double[][] c=new double[width][width];
   for(int i=0; i<width; i++) for(int j=0;j<width;j++) { b[i][j]=0; c[i][j]=0;}
   for(int i=0; i<width; i++) b[i][i]=1.0;

   for(int k=0; k<width-1; k++) {
    for(int i=k+1; i<width; i++) {
     for(int s=0; s<width; s++) b[i][s]-=temp[i][k]*b[k][s]/temp[k][k];
     for(int j=k+1; j<width; j++) temp[i][j]-=temp[i][k]*temp[k][j]/temp[k][k];
    }
   }

   for(int s=0; s<width; s++) {
    c[width-1][s]=b[width-1][s]/temp[width-1][width-1];
    for(int i=width-2; i>=0; i--) {
     c[i][s]=b[i][s]/temp[i][i];
     for(int k=width-1; k>i; k--) c[i][s]-=temp[i][k]*c[k][s]/temp[i][i];
    }
   }

   for(int i=0;i<width;i++) for(int j=0;j<width;j++) inverse[cl][i][j]=c[i][j];
  }
 }
 
 //-------------------------------------------------------------//
 // Actualiza los grados de pertenencia de un punto teniendo en	//
 // cuenta la distancia a los clusters				//
 //-------------------------------------------------------------//

 private void updateDegree(int i, double[] dist) {
  double pow = 2/(fuzziness-1);
  boolean crisp = false;
  for(int j=0; j<cluster.length; j++) if(dist[j] == 0) crisp = true;
  if(crisp) {
   for(int j=0; j<cluster.length; j++) {
    if(dist[j] == 0) degree[i][j] = 1.0;
    else degree[i][j] = 0.0;
   }
   return;
  }

  for(int j=0; j<cluster.length; j++) {
   if(Double.isNaN(dist[j])) { degree[i][j] = 0; continue; }
   double sum = 0;
   for(int k=0; k<cluster.length; k++) {
    if(!Double.isNaN(dist[k])) sum += Math.pow((dist[j]/dist[k]), pow);
   }
   degree[i][j] = 1/sum;
  }

  int NaNcount = 0;
  double sum = 0;
  for(int j=0; j<cluster.length; j++) {
   if(Double.isNaN(degree[i][j])) NaNcount++;
   else sum += degree[i][j];
  }
  if(NaNcount != 0) for(int j=0; j<cluster.length; j++) {
   if(Double.isNaN(degree[i][j])) degree[i][j] = (1-sum)/NaNcount;
  }

 }

 //=============================================================//
 //	   Metodos que generan el contenido del sistema		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Genera el contenido del sistema a partir de los clusters	//
 //-------------------------------------------------------------//

 private void createContent() {
  for(int i=0; i<inputtype.length; i++) createBells(inputtype[i],i);
  for(int i=0; i<outputtype.length; i++) {
   switch(config.systemstyle.defuz) {
    case XfdmSystemStyle.FUZZYMEAN:
     createSingletons(outputtype[i],inputtype.length + i);
     break;
    case XfdmSystemStyle.WEIGHTED:
     createBells(outputtype[i],inputtype.length + i);
     break;
    case XfdmSystemStyle.TAKAGI:
     createParametric(outputtype[i],inputtype.length + i);
     break;
   }
  }
  createRules();
 }

 //-------------------------------------------------------------//
 // Genera las reglas correspondientes a los clusters		//
 //-------------------------------------------------------------//

 private void createRules() {
  Variable ivar[] = rulebase.getInputs();
  Variable ovar[] = rulebase.getOutputs();
  int is = Relation.IS;

  for(int i=0; i<cluster.length; i++) {
   LinguisticLabel pmf = ivar[0].getType().getAllMembershipFunctions()[i];
   Relation rel = Relation.create(is,null,null,ivar[0],pmf,rulebase);
   for(int j=1; j<ivar.length; j++) {
    pmf = ivar[j].getType().getAllMembershipFunctions()[i];
    Relation nrel = Relation.create(is,null,null,ivar[j],pmf,rulebase);
    rel = Relation.create(Relation.AND,rel,nrel,null,null,rulebase);
   }
   Rule rule = new Rule(rel);
   for(int j=0; j<ovar.length; j++) {
    pmf = ovar[j].getType().getAllMembershipFunctions()[i];
    rule.add(new Conclusion(ovar[j],pmf,rulebase));
   }
   rulebase.addRule(rule);
  }
 }

 //-------------------------------------------------------------//
 // Genera el conjunto de tipos de variables de entrada		//
 //-------------------------------------------------------------//

 protected Type[] createInputTypes() {
  Type itp[] = new Type[config.numinputs];
  for(int i=0; i<config.numinputs; i++) {
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

   if(universe == null) universe = pattern.getUniverse(i,true);
   itp[i] = new Type(tname,universe);
  }
  return itp;
 }

 //-------------------------------------------------------------//
 // Genera el conjunto de tipos de variables de salida          //
 //-------------------------------------------------------------//

 private Type[] createOutputTypes() {
  Type otp[] = new Type[config.numoutputs];

  for(int i=0; i<config.numoutputs; i++) {
   String tname = "T"+config.systemstyle.outputname;
   if(config.numoutputs>1) tname += ""+i;
   otp[i] = new Type(tname,pattern.getUniverse(i,false));
  }
  return otp;
 }

 //=============================================================//
 //	Metodos que generan las funciones de pertenencia	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Crea un conjunto de singularidades				//
 //-------------------------------------------------------------//

 private void createSingletons(Type type, int index) {
  Universe u = type.getUniverse();
  for(int cl=0; cl<cluster.length; cl++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.singleton();
   pmf.set("mf"+cl, u);
   pmf.set( cluster[cl][index] );
   try { type.add(pmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de funciones parametricas			//
 //-------------------------------------------------------------//

 private void createParametric(Type type, int index) {
  Universe u = type.getUniverse();
  double param[] = new double[inputtype.length+1];
  for(int i=0; i<cluster.length; i++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.parametric();
   pmf.set("mf"+i, u);
   param[0] = cluster[i][index];
   try { pmf.set(param); type.add(pmf); } catch(XflException ex) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de campanas				//
 //-------------------------------------------------------------//

 private void createBells(Type type, int index) {
  Universe u = type.getUniverse();
  double param[] = new double[2];
  for(int cl=0; cl<cluster.length; cl++) {
   param[0] = cluster[cl][index];
   param[1] = getWidthForMF(type,index,cl);
   ParamMemFunc pmf = new pkg.xfl.mfunc.bell();
   pmf.set("mf"+cl, u);
   try { pmf.set(param); type.add(pmf); } catch(XflException ex) {}
  }
 }

 //-------------------------------------------------------------//
 // Obtiene la anchura de una MF en funcion de los grados de	//
 // pertenencia de los puntos a cada cluster			//
 //-------------------------------------------------------------//

 private double getWidthForMF(Type type, int var, int cl) {
  double min = type.getUniverse().min();
  double max = type.getUniverse().max();
  double center = cluster[cl][var];

  double dist1 = max-min;
  boolean test1 = false;
  for(int i=0; i<point.length; i++) {
   double mu = degree[i][cl];
   double x = point[i][var];
   if(mu>0.3) continue;
   test1 = true;
   double d = (x>center? x-center : center-x);
   if(d < dist1) dist1 = d;
  }

  double dist2 = 0;
  boolean test2 = false;
  for(int i=0; i<point.length; i++) {
   double mu = degree[i][cl];
   double x = point[i][var];
   if(mu<0.3) continue;
   test2 = true;
   double d = (x>center? x-center : center-x);
   if(d > dist2) dist2 = d;
  }

  double dist = 0;
  if(test1 & test2) dist = (dist1+dist2)/2;
  else if(test1) dist = dist1;
  else if(test2) dist = dist2;
  if(dist==0) return (max-min)/100;
  return dist;
 }

 //-------------------------------------------------------------//
 // Ajusta las salidas del sistema con el algoritmo de          //
 // Marquardt-Levenberg                                         //
 //-------------------------------------------------------------//

 private void learning() {
  double param[] = { 0.1, 10.0, 0.1};
  int alg = XfslAlgorithm.MARQUARDT;
  int err = XfslErrorFunction.MEAN_SQUARE_ERROR;
  int end = XfslEndCondition.TRN_VAR;
  XfslConfig xfslconfig = new XfslConfig();
  try {
   xfslconfig.trainingfile = config.patternfile;
   xfslconfig.algorithm = XfslAlgorithm.create(alg,param);
   xfslconfig.errorfunction = new XfslErrorFunction(err);
   xfslconfig.endcondition.setLimit(end,0.001);
   xfslconfig.addSetting("ANY.ANY.ANY",false);
   for(int i=0; i<outputtype.length; i++) {
    xfslconfig.addSetting(outputtype[i].getName()+".ANY.ANY",true);
   }
   XfslThread lrnthread = new XfslThread(spec,xfslconfig);
   lrnthread.run();
  } catch (Exception ex) { }
 }

}

