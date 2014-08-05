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
//              ALGORITMO DE PARTICION INCREMENTAL              //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;
import xfuzzy.xfsl.*;

public class XfdmIncGrid extends XfdmAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int mfs_limit;
 private int rule_limit;
 private double rmse_limit;
 private boolean learning;

 private double input[][];
 private double output[][];
 private double last_rmse;
 private int worst_pattern;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfdmIncGrid() {
  this.mfs_limit = -1;
  this.rule_limit = -1;
  this.rmse_limit = -1;
  this.learning = false;
 }

 //-------------------------------------------------------------//
 // Constructor desde la interfaz grafica			//
 //-------------------------------------------------------------//

 public XfdmIncGrid(int mfs, int rule, double rmse, boolean learn) {
  this.mfs_limit = mfs;
  this.rule_limit = rule;
  this.rmse_limit = rmse;
  this.learning = learn;
 }

 //-------------------------------------------------------------//
 // Constructor desde el fichero de configuracion		//
 //-------------------------------------------------------------//

 public XfdmIncGrid(double[] param) {
  this.mfs_limit = (int) param[0];
  this.rule_limit = (int) param[1];
  this.rmse_limit = (double) param[2];
  this.learning = (((int) param[3]) == 1);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //		Metodos de acceso a la configuracion		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene el valor del maximo numero de MFs permitido		//
 //-------------------------------------------------------------//

 public int getMfsLimit() {
  return this.mfs_limit;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del maximo numero de MFs permitido		//
 //-------------------------------------------------------------//

 public void setMfsLimit(int limit) {
  this.mfs_limit = limit;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del maximo numero de reglas permitido	//
 //-------------------------------------------------------------//

 public int getRuleLimit() {
  return this.rule_limit;
 }

 //-------------------------------------------------------------//
 // Asigna el valor del maximo numero de reglas permitido	//
 //-------------------------------------------------------------//

 public void setRuleLimit(int limit) {
  this.rule_limit = limit;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor de la minima desviacion permitida	 	//
 //-------------------------------------------------------------//

 public double getRMSELimit() {
  return this.rmse_limit;
 }

 //-------------------------------------------------------------//
 // Asigna el valor de la minima desviacion permitida	 	//
 //-------------------------------------------------------------//

 public void setRMSELimit(double limit) {
  this.rmse_limit = limit;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor de la opcion de aprendizaje	 	//
 //-------------------------------------------------------------//

 public boolean isLearning() {
  return this.learning;
 }

 //-------------------------------------------------------------//
 // Asigna el valor de la opcion de aprendizaje		 	//
 //-------------------------------------------------------------//

 public void setLearning(boolean learn) {
  this.learning = learn;
 }

 //=============================================================//
 //		Metodos de desarrollo de XfdmAlgorithm		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene un duplicado del objeto				//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfdmIncGrid(mfs_limit,rule_limit,rmse_limit,learning);
 }

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public String toString() {
  return "Incremental Grid Partition";
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  String code = "xfdm_algorithm(IncGrid,";
  code += " "+mfs_limit+", "+rule_limit+", "+rmse_limit;
  code += (learning? ", 1" : ", 0")+" )";
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

  initInputPartition();
  initOutputPartition();
  createContent();
  if(learning) learning();
  computeError();
  while(!satisfyEndCondition()) {
   updatePartition();
   createContent();
   if(learning) learning();
   computeError();
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //	Funciones que generan el contenido del sistema		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Genera el contenido del sistema a partir de la particion	//
 //-------------------------------------------------------------//

 private void createContent() {
  for(int i=0; i<config.numinputs; i++) {
   int style = config.commonstyle.style;
   if(config.inputstyle != null && config.inputstyle.length > i &&
      config.inputstyle[i] != null) {
    style = config.inputstyle[i].style;
   }
   if(style == XfdmInputStyle.FREE_TRIANGLES) {
    createFreeTriangles(inputtype[i],input[i]);
   } else {
    createTriangularFamily(inputtype[i],input[i]);
   }
  }

  if(config.systemstyle.defuz == XfdmSystemStyle.FUZZYMEAN) {
   for(int i=0; i<outputtype.length; i++) {
    createSingletons(outputtype[i],output[i]);
   }
  } else {
   for(int i=0; i<outputtype.length; i++) createBells(outputtype[i],output[i]);
  }

  createRules();
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

 //-------------------------------------------------------------//
 // Genera el antecedente correspondiente a un punto de la	//
 // particion							//
 //-------------------------------------------------------------//

 private Relation createAntecedent(int[] index) {
  Variable ivar[] = rulebase.getInputs();
  int is = Relation.IS;

  LinguisticLabel pmf[] = new LinguisticLabel[index.length];
  for(int i=0; i<index.length; i++) {
   pmf[i] = ivar[i].getType().getAllMembershipFunctions()[index[i]];
  }

  Relation rel = Relation.create(is,null,null,ivar[0],pmf[0],rulebase);
  for(int j=1; j<index.length; j++) {
   Relation nrel = Relation.create(is,null,null,ivar[j],pmf[j],rulebase);
   rel = Relation.create(Relation.AND,rel,nrel,null,null,rulebase);
  }

  return rel;
 }

 //-------------------------------------------------------------//
 // Genera las reglas correspondientes a la particion		//
 //-------------------------------------------------------------//

 private void createRules() {
  rulebase.removeAllRules();
  int index[] = new int[input.length];
  Variable ovar[] = rulebase.getOutputs();
  int count = computeNumberOfRules();
  for(int i=0; i<count; i++) {
   Rule rule = new Rule(createAntecedent(index));
   for(int j=0; j<ovar.length; j++) {
    LinguisticLabel pmf = ovar[j].getType().getAllMembershipFunctions()[i];
    rule.add(new Conclusion(ovar[j],pmf,rulebase));
   }
   rulebase.addRule(rule);
   incrementIndex(index);
  }
 }

 //-------------------------------------------------------------//
 // Calcula el numero de reglas en funcion de la particion	//
 //-------------------------------------------------------------//

 private int computeNumberOfRules() {
  int number = 1;
  for(int i=0; i<input.length; i++) number = number*(input[i].length);
  return number;
 }

 //=============================================================//
 //	Funciones que generan las funciones de pertenencia	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Crea una familia de triangulos				//
 //-------------------------------------------------------------//

 private void createTriangularFamily(Type type, double[] partition) {
  type.removeAllLabels();
  type.removeAllFamilies();
  double param[] = new double[partition.length-2];
  for(int i=0; i<param.length; i++) param[i] = partition[i+1];

  Family fam = new pkg.xfl.family.triangular();
  fam.set("fam",type);
  try { fam.set(param); type.addFamily(fam); } catch(XflException ex) {}

  for(int i=0; i<fam.members(); i++) {
   FamiliarMemFunc fmf = new FamiliarMemFunc("mf"+i,fam,i);
   try { type.add(fmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de triangulos equiespaciados               //
 //-------------------------------------------------------------//

 private void createFreeTriangles(Type type, double[] partition) {
  type.removeAllLabels();
  type.removeAllFamilies();
  double param[] = new double[partition.length+2];
  param[0] = partition[0]-1;
  param[param.length-1] = partition[partition.length-1]+1;
  for(int i=0; i<partition.length; i++) param[i+1] = partition[i];

  Universe u = type.getUniverse();
  double pp[] = new double[3];

  for(int i=0; i<partition.length; i++) {
   pp[0] = param[i];
   pp[1] = param[i+1];
   pp[2] = param[i+2];
   ParamMemFunc pmf = new pkg.xfl.mfunc.triangle();
   pmf.set("mf"+i,u);
   try { pmf.set(pp); type.add(pmf); } catch(XflException ex) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de singularidades				//
 //-------------------------------------------------------------//

 private void createSingletons(Type type, double[] value) {
  type.removeAllLabels();
  Universe u = type.getUniverse();
  for(int i=0; i<value.length; i++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.singleton();
   pmf.set("mf"+i, u);
   pmf.set(value[i]);
   try { type.add(pmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de campanas				//
 //-------------------------------------------------------------//

 private void createBells(Type type, double[] value) {
  type.removeAllLabels();
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double param[] = new double[2];
  param[1] = (max - min)/10;
  for(int i=0; i<value.length; i++) {
   ParamMemFunc pmf = new pkg.xfl.mfunc.bell();
   pmf.set("mf"+i, u);
   param[0] = value[i];
   try { pmf.set(param); type.add(pmf); } catch(XflException ex) {}
  }
 }

 //=============================================================//
 //		Funciones que manejan las particiones		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Crea la particion inicial de las entradas (con min y max)	//
 //-------------------------------------------------------------//

 private void initInputPartition() {
  this.input = new double[inputtype.length][2];
  for(int i=0; i<input.length; i++) {
   input[i][0] = inputtype[i].getUniverse().min();
   input[i][1] = inputtype[i].getUniverse().max();
  }
 }

 //-------------------------------------------------------------//
 // Crea los valores de salida de la particion inicial		// 
 //-------------------------------------------------------------//

 private void initOutputPartition() {
  int points = computeNumberOfRules();
  this.output = new double[outputtype.length][points];
  int index[] = new int[input.length];
  for(int i=0; i<points; i++) {
   int pp = getPatternIndex( getPartitionFromIndexes(index) );
   for(int j=0; j<output.length; j++) output[j][i] = pattern.output[pp][j];
   incrementIndex(index);
  }
 }

 //-------------------------------------------------------------//
 // Incrementa un vector de indices sobre la particion		//
 //-------------------------------------------------------------//

 private void incrementIndex(int[] index) {
  boolean overhead = true;
  int i = index.length-1;
  while(overhead && i>=0) {
   index[i]++;
   if(index[i]>=input[i].length) { index[i] = 0; i--; }
   else overhead = false;
  }
 }

 //-------------------------------------------------------------//
 // Obtiene un punto de la particion a partir de los indices	//
 //-------------------------------------------------------------//

 private double[] getPartitionFromIndexes(int[] index) {
  double partition[] = new double[input.length];
  for(int i=0; i<input.length; i++) partition[i] = input[i][index[i]];
  return partition;
 }

 //=============================================================//
 //		Funciones de busqueda en los patrones		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Busca el patron mas cercano a un punto del espacio de	//
 // entrada							//
 //-------------------------------------------------------------//

 private int getPatternIndex(double[] input) {
  int index = 0;
  double dist = patternDistance(0,input);
  for(int i=1; i<pattern.input.length; i++) {
   double nd = patternDistance(i,input);
   if(nd<dist) { dist = nd; index = i; }
  }
  return index;
 }

 //-------------------------------------------------------------//
 // Calcula la distancia de un patron a un punto del espacio de	//
 // entrada							//
 //-------------------------------------------------------------//

 private double patternDistance(int index, double[] input) {
  double dist = 0;
  for(int i=0; i<input.length; i++) {
   dist+=(pattern.input[index][i]-input[i])*(pattern.input[index][i]-input[i]); 
  }
  return dist;
 }

 //=============================================================//
 //		Funciones de evaluacion del sistema		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Verifica si se han cumplido las condiciones de termino	//
 //-------------------------------------------------------------//

 private boolean satisfyEndCondition() {
  if(rule_limit > 0 && rulebase.getRules().length > rule_limit) return true;
  if(rmse_limit > 0 && last_rmse <= rmse_limit) return true;
  for(int i=0; i<input.length; i++) {
   if(mfs_limit > 0 && input[i].length > mfs_limit) return true;
  }
  if(worst_pattern < 0) return true;
  return false;
 }

 //-------------------------------------------------------------//
 // Calcula el error medio que comete el sistema y el patron en	//
 // el que la desviacion es mayor				//
 //-------------------------------------------------------------//

 private void computeError() {
  SystemModule system = spec.getSystemModule();
  double mxae=0;
  double mse=0;
  int index = -1;
  for(int p=0; p<pattern.input.length; p++) {
   double[] out = system.crispInference(pattern.input[p]);
   for(int i=0; i<out.length; i++) {
    double dev = (out[i]-pattern.output[p][i])/pattern.range[i];
    if(dev<0) dev = -dev;
    mse += dev*dev/out.length;
    if(dev>mxae) { mxae = dev; index = p; }
   }
  }
  this.last_rmse = Math.sqrt(mse/pattern.input.length);

  int list[] = new int[0];
  while(!testNewPoint(index)) {
   int nl[] = new int[list.length+1];
   System.arraycopy(list,0,nl,0,list.length);
   nl[list.length] = index;
   list = nl;

   mxae = 0;
   index = -1;
   for(int p=0; p<pattern.input.length; p++) {
    boolean excluded = false;
    for(int l=0; l<list.length; l++) if(list[l] == p) excluded = true;
    if(excluded) continue;

    double[] out = system.crispInference(pattern.input[p]);
    for(int i=0; i<out.length; i++) {
     double dev = (out[i]-pattern.output[p][i])/pattern.range[i];
     if(dev<0) dev = -dev;
     if(dev>mxae) { mxae = dev; index = p; }
    }
   }
  }
  this.worst_pattern = index;
 }

 //-------------------------------------------------------------//
 // Estudia el punto a incluir					//
 //-------------------------------------------------------------//

 private boolean testNewPoint(int pattern_index) {
  double newpoint[] = pattern.input[pattern_index];
  int position[] = new int[input.length];
  for(int i=0; i<input.length; i++) {
   double range = input[i][input[i].length-1] - input[i][0];
   int j;
   for(j=0; j<input[i].length; j++) if(input[i][j] > newpoint[i]) break;
   double prev = (j==0? input[i][0] : input[i][j-1]);
   double next = (j==input[i].length? input[i][j-1] : input[i][j]);
   boolean insert = true;
   if(newpoint[i]-prev < range/100) insert = false;
   if(next-newpoint[i] < range/100) insert = false;
   if(insert) position[i] = j; else position[i] = -1;
  }

  for(int i=0; i<position.length; i++) if(position[i] != -1) return true;
  return false;
 }

 //-------------------------------------------------------------//
 // Actualiza la particion					//
 //-------------------------------------------------------------//

 private void updatePartition() {
  if(worst_pattern < 0) return;
  double newpoint[] = pattern.input[worst_pattern];
  int position[] = new int[input.length];
  for(int i=0; i<input.length; i++) {
   double range = input[i][input[i].length-1] - input[i][0];
   int j;
   for(j=0; j<input[i].length; j++) if(input[i][j] > newpoint[i]) break;
   double prev = (j==0? input[i][0] : input[i][j-1]);
   double next = (j==input[i].length? input[i][j-1] : input[i][j]);
   boolean insert = true;
   if(newpoint[i]-prev < range/100) insert = false;
   if(next-newpoint[i] < range/100) insert = false;
   if(insert) {
    double ni[] = new double[input[i].length+1];
    System.arraycopy(input[i],0,ni,0,j);
    ni[j] = newpoint[i];
    System.arraycopy(input[i],j,ni,j+1,input[i].length-j);
    input[i] = ni;
    position[i] = j;
   } else position[i] = -1;
  }

  int old=0;
  int index[] = new int[input.length];
  int count = computeNumberOfRules();
  double no[][] = new double[outputtype.length][count];
  for(int i=0; i<count; i++) {
   boolean newvalue = false;
   for(int j=0; j<index.length; j++) if(index[j] == position[j]) newvalue=true;
   if(!newvalue) {
    for(int j=0; j<no.length; j++) no[j][i] = output[j][old];
    old++;
   } else {
    int pp = getPatternIndex( getPartitionFromIndexes(index) );
    for(int j=0; j<no.length; j++) no[j][i] = pattern.output[pp][j];
   }
   incrementIndex(index);
  }

  output = no;
 }

 //-------------------------------------------------------------//
 // Ajusta las salidas del sistema con el algoritmo de		//
 // Marquardt-Levenberg						//
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
  for(int i=0; i<outputtype.length; i++) {
   LinguisticLabel label[] = outputtype[i].getAllMembershipFunctions();
   for(int j=0; j<label.length; j++) {
    this.output[i][j] = label[j].get()[0];
   }
  }
 }
}

