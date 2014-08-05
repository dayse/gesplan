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
//	ALGORITMO DE CLUSTERING INCREMENTAL (SUBSTRACTIVE)	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;

public class XfdmIncClustering extends XfdmAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int clusters_limit;
 private double radius;

 private double point[][];
 private double cluster[][];
 private double potential[];
 private double cl_potential[];

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfdmIncClustering() {
  this.clusters_limit = 10;
  this.radius = 0.1;
 }

 //-------------------------------------------------------------//
 // Constructor desde la interfaz grafica			//
 //-------------------------------------------------------------//

 public XfdmIncClustering(int limit, double radius) {
  this.clusters_limit = limit;
  this.radius = radius;
 }

 //-------------------------------------------------------------//
 // Constructor desde el fichero de configuracion		//
 //-------------------------------------------------------------//

 public XfdmIncClustering(double[] param) {
  this.clusters_limit = (int) param[0];
  this.radius = (double) param[1];
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //		Metodos de acceso a la configuracion		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene el valor del maximo numero de clusters permitido	//
 //-------------------------------------------------------------//

 public int getClustersLimit() {
  return this.clusters_limit;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del maximo numero de clusters permitido	//
 //-------------------------------------------------------------//

 public void setClustersLimit(int limit) {
  this.clusters_limit = limit;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del radio de vecindad		 	//
 //-------------------------------------------------------------//

 public double getRadius() {
  return this.radius;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del radio de vecindad		 	//
 //-------------------------------------------------------------//

 public void setRadius(double radius) {
  this.radius = radius;
 }

 //=============================================================//
 //		Metodos de desarrollo de XfdmAlgorithm		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene un duplicado del objeto				//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfdmIncClustering(clusters_limit,radius);
 }

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public String toString() {
  return "Incremental Clustering (Substractive clustering)";
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  String code = "xfdm_algorithm(IncClustering,";
  code += " "+clusters_limit+", "+radius+" )";
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

  this.point = createPointsFromPatterns();
  this.potential = createPotentials();
  this.cluster = new double[0][];
  this.cl_potential = new double[0];
  do {
   selectCluster();
   updatePotentials();
  } while(!satisfyEndCondition());

  createContent();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //		Metodos que desarrollan el algoritmo		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene el conjunto normalizado de patrones			//
 //-------------------------------------------------------------//

 private double[][] createPointsFromPatterns() {
  int length = this.pattern.input.length;
  int width = this.inputtype.length + this.outputtype.length;
  double point[][] = new double[length][width];
  double min[] = new double[width];
  double max[] = new double[width];
  for(int i=0; i<width; i++) {
   if(i<inputtype.length) {
    min[i] = inputtype[i].min();
    max[i] = inputtype[i].max();
   } else {
    min[i] = outputtype[i-inputtype.length].min();
    max[i] = outputtype[i-inputtype.length].max();
   }
  }

  for(int i=0; i<length; i++) for(int j=0; j<width; j++) {
   if(j<inputtype.length) {
    point[i][j] = (pattern.input[i][j]-min[j])/(max[j]-min[j]);
   } else {
    point[i][j] =(pattern.output[i][j-inputtype.length]-min[j])/(max[j]-min[j]);
   }
  }
  return point;
 }
 
 //-------------------------------------------------------------//
 // Genera los potenciales asociados a cada punto		//
 //-------------------------------------------------------------//

 private double[] createPotentials() {
  double potential[] = new double[point.length];
  double alpha = 4/(radius*radius);
  for(int i=0; i<point.length; i++) for(int j=0; j<point.length; j++) {
   if(i!=j) potential[i] += Math.exp(-alpha*distance(point[i],point[j]));
  }
  return potential;
 }

 //-------------------------------------------------------------//
 // Annade como cluster el punto de mayor potencial		//
 //-------------------------------------------------------------//

 private void selectCluster() {
  int index = -1;
  double max = 0;
  for(int i=0; i<potential.length; i++) {
   if(potential[i]>max) { max = potential[i]; index = i; }
  }
  if(index <0) return;

  double[][] ac = new double[cluster.length+1][];
  System.arraycopy(cluster,0,ac,0,cluster.length);
  ac[cluster.length] = point[index];
  cluster = ac;

  double[] acp = new double[cl_potential.length+1];
  System.arraycopy(cl_potential,0,acp,0,cl_potential.length);
  acp[cl_potential.length] = potential[index];
  cl_potential = acp;

  double[][] ap = new double[point.length-1][];
  System.arraycopy(point,0,ap,0,index);
  System.arraycopy(point,index+1,ap,index,point.length-index-1);
  point = ap;

  double[] app = new double[potential.length-1];
  System.arraycopy(potential,0,app,0,index);
  System.arraycopy(potential,index+1,app,index,potential.length - index -1);
  potential = app;
 }

 //-------------------------------------------------------------//
 // Actualiza los potenciales considerando el ultimo cluster	//
 //-------------------------------------------------------------//

 private void updatePotentials() {
  double[] lastcluster = cluster[cluster.length-1];
  double lastpotential = cl_potential[cluster.length-1];
  double rb = 1.25*radius;
  double beta = -4/(rb*rb);
  for(int i=0; i<point.length; i++) {
   potential[i] -= lastpotential*Math.exp(beta*distance(point[i],lastcluster)); 
  }
 }

 //-------------------------------------------------------------//
 // Verifica si se han cumplido las condiciones de termino	//
 //-------------------------------------------------------------//

 private boolean satisfyEndCondition() {
  if(cluster.length >= clusters_limit) return true;
  for(int i=0; i<potential.length; i++) {
   if(potential[i] > 0.15*cl_potential[0]) return false;
  }
  // Finished if not potential is found to be significant enough, 
  return true;
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
  double min = u.min();
  double max = u.max();
  for(int i=0; i<cluster.length; i++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.singleton();
   pmf.set("mf"+i, u);
   pmf.set( min + cluster[i][index]*(max-min) );
   try { type.add(pmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de funciones parametricas			//
 //-------------------------------------------------------------//

 private void createParametric(Type type, int index) {
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double param[] = new double[inputtype.length+1];
  for(int i=0; i<cluster.length; i++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.parametric();
   pmf.set("mf"+i, u);
   param[0] = min + cluster[i][index]*(max-min);
   try { pmf.set(param); type.add(pmf); } catch(XflException ex) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de campanas				//
 //-------------------------------------------------------------//

 private void createBells(Type type, int index) {
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double param[] = new double[2];
  param[1] = radius*(max - min)/2;
  for(int i=0; i<cluster.length; i++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.bell();
   pmf.set("mf"+i, u);
   param[0] = min + cluster[i][index]*(max-min);
   try { pmf.set(param); type.add(pmf); } catch(XflException ex) {}
  }
 }

}

