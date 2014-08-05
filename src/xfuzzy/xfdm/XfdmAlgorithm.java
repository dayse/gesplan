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
//	    CONSTRUCCION AUTOMATICA DE UN SISTEMA		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;
import xfuzzy.xfsl.XfslPattern;

public abstract class XfdmAlgorithm {
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 protected Specification spec;
 protected XfdmConfig config;
 protected XfslPattern pattern;
 protected Operatorset opset;
 protected Type[] inputtype;
 protected Type[] outputtype;
 protected Rulebase rulebase;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Crea un algoritmo a partir de su identificador		//
 //-------------------------------------------------------------//

 public static XfdmAlgorithm create(String name, double[] param) {
  if(name.equals("Flat")) return new XfdmFlatSystem();
  else if(name.equals("WangMendel")) return new XfdmWangMendel();
  else if(name.equals("Nauck")) return new XfdmNauck(param);
  else if(name.equals("Senhadji")) return new XfdmSenhadji(param);
  else if(name.equals("IncGrid")) return new XfdmIncGrid(param);
  else if(name.equals("ICFA")) return new XfdmICFA(param);
  else if(name.equals("IncClustering")) return new XfdmIncClustering(param);
  else if(name.equals("HardCMeans"))
   return new XfdmFixedClustering(XfdmFixedClustering.HARD_CMEANS,param);
  else if(name.equals("CMeans"))
   return new XfdmFixedClustering(XfdmFixedClustering.CMEANS,param);
  else if(name.equals("GustafsonKessel"))
   return new XfdmFixedClustering(XfdmFixedClustering.GUSTAFSON_KESSEL,param);
  else if(name.equals("GathGeva"))
   return new XfdmFixedClustering(XfdmFixedClustering.GATH_GEVA,param);
  else return null;
 }

 //-------------------------------------------------------------//
 // Metodo que construye el sistema a partir de los datos	//
 //-------------------------------------------------------------//

 public abstract void compute(Specification spec, XfdmConfig config)
 throws XflException;

 /* Get the algorithm name. */
 public abstract String toString();

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public abstract String toCode();

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //	   Metodos que generan los objetos identificados	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Genera el conjunto de operadores de la base de reglas       //
 //-------------------------------------------------------------//

 protected Operatorset createOperatorSet() {
  if(config.systemstyle == null) return null;

  String prefix = config.systemstyle.rulebase+"_opset";
  String oname = ""+prefix;
  int osi=0;
  while(spec.searchOperatorset(oname) != null) { oname = prefix+osi; osi++; }

  Operatorset op = new Operatorset(oname);

  switch(config.systemstyle.conjunction) {
   case XfdmSystemStyle.MIN:
    op.set(new pkg.xfl.binary.min(), FuzzyOperator.AND);
    break;
   case XfdmSystemStyle.PROD:
    op.set(new pkg.xfl.binary.prod(), FuzzyOperator.AND);
    break;
  }

  switch(config.systemstyle.defuz) {
   case XfdmSystemStyle.FUZZYMEAN:
    op.set(new pkg.xfl.defuz.FuzzyMean(),FuzzyOperator.DEFUZMETHOD);
    break;
   case XfdmSystemStyle.WEIGHTED:
    op.set(new pkg.xfl.defuz.WeightedFuzzyMean(),FuzzyOperator.DEFUZMETHOD);
    break;
   case XfdmSystemStyle.TAKAGI:
    op.set(new pkg.xfl.defuz.TakagiSugeno(),FuzzyOperator.DEFUZMETHOD);
    break;
   case XfdmSystemStyle.CLASSIFICATION:
    op.set(new pkg.xfl.defuz.MaxLabel(),FuzzyOperator.DEFUZMETHOD);
    break;
  }

  return op;
 }

 //-------------------------------------------------------------//
 // Genera el conjunto de tipos de variables de entrada		//
 //-------------------------------------------------------------//

 protected Type[] createInputTypes() {
  Type itp[] = new Type[config.numinputs];
  for(int i=0; i<config.numinputs; i++) {
   String tname = "Tin"+i;
   int mfs = config.commonstyle.mfs;
   int style = config.commonstyle.style;
   Universe universe = null;
   if(config.commonstyle.isUniverseDefined()) {
    try { universe=new Universe(config.commonstyle.min,config.commonstyle.max);}
    catch(Exception ex) {}
   }

   if(config.inputstyle != null && config.inputstyle.length > i &&
      config.inputstyle[i] != null) {
    tname = "T"+config.inputstyle[i].name;
    mfs = config.inputstyle[i].mfs;
    style = config.inputstyle[i].style;
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
   createInputMemFuncs(itp[i],style,mfs);
  }
  return itp;
 }

 //-------------------------------------------------------------//
 // Genera el conjunto de tipos de variables de salida		//
 //-------------------------------------------------------------//

 protected Type[] createOutputTypes(int numomfs){
  Type otp[] = new Type[config.numoutputs];

  for(int i=0; i<config.numoutputs; i++) {
   String tname = "T"+config.systemstyle.outputname;
   if(config.numoutputs>1) tname += ""+i;
   otp[i] = new Type(tname,pattern.getUniverse(i,false));
   switch(config.systemstyle.defuz) {
    case XfdmSystemStyle.FUZZYMEAN:
     createSingletons(otp[i],numomfs);
     break;
    case XfdmSystemStyle.WEIGHTED:
     createBells(otp[i],numomfs);
     break;
    case XfdmSystemStyle.TAKAGI:
     createTakagi(otp[i],numomfs,1+inputtype.length);
     break;
    case XfdmSystemStyle.CLASSIFICATION:
     createClasses(otp[i],pattern.getValues(i));
     break;
   }
  }
  return otp;
 }

 //-------------------------------------------------------------//
 // Genera la base de reglas                                    //
 //-------------------------------------------------------------//

 protected Rulebase createEmptyRulebase() {
  Rulebase base = new Rulebase(""+config.systemstyle.rulebase);
  base.setOperatorset(opset);
  Variable ivar[] = new Variable[config.numinputs];
  Variable ovar[] = new Variable[config.numoutputs];
  for(int i=0; i<config.numinputs; i++) {
   String varname = "in"+i;
   if(config.inputstyle != null && config.inputstyle.length > i &&
      config.inputstyle[i] != null) {
    varname = ""+config.inputstyle[i].name;
   }
   ivar[i] = new Variable(varname,inputtype[i],Variable.INPUT);
   base.addInputVariable(ivar[i]);
  }
  for(int i=0; i<config.numoutputs; i++) {
   String name = config.systemstyle.outputname;
   if(config.numoutputs>1) name += ""+i;
   ovar[i] = new Variable(name,outputtype[i],base);
   base.addOutputVariable(ovar[i]);
  }
  return base;
 }

 //-------------------------------------------------------------//
 // Genera la estructura modular                                //
 //-------------------------------------------------------------//

 protected void createSystemStructure() {
  SystemModule system = spec.getSystemModule();
  RulebaseCall rmcall[] = system.getRulebaseCalls();
  for(int i=0; i<rmcall.length; i++) system.removeCall(rmcall[i]);
  Variable rmvar[] = system.getVariables();
  for(int i=0; i<rmvar.length; i++) system.removeVariable(rmvar[i]);
  for(int i=0; i<config.numinputs; i++) {
   String varname = "in"+i;
   if(config.inputstyle != null && config.inputstyle.length > i &&
      config.inputstyle[i] != null) {
    varname = ""+config.inputstyle[i].name;
   }
   system.addVariable( new Variable(varname,inputtype[i],Variable.INPUT) );
  }
  for(int i=0; i<config.numoutputs; i++) {
   String name = config.systemstyle.outputname;
   if(config.numoutputs>1) name += ""+i;
   system.addVariable( new Variable(name,outputtype[i],Variable.OUTPUT) );
  }
  system.addCall(rulebase,system.getInputs(),system.getOutputs());
 }

 //=============================================================//
 //			Metodos auxiliares			//
 //=============================================================//

 //-------------------------------------------------------------//
 // Calcula el numero de reglas del grid                        //
 //-------------------------------------------------------------//

 protected int computeGridSize(Type[] itp) {
  int number=1;
  for(int i=0; i<itp.length; i++) {
   number *= itp[i].getAllMembershipFunctions().length;
  }
  return number;
 }

 //-------------------------------------------------------------//
 // Obtiene el indice de la etiqueta mas activa			//
 //-------------------------------------------------------------//

 protected int getMFIndex(Type type, double x) {
  LinguisticLabel mf[] = type.getAllMembershipFunctions();
  double max = 0;
  int index = 0;
  for(int i=0; i<mf.length; i++) {
   double degree = mf[i].compute(x);
   if(degree>=max) { max = degree; index = i; }
  }
  return index;
 }

 //-------------------------------------------------------------//
 // Obtiene los indices de las etiquetas mas activas		//
 //-------------------------------------------------------------//

 protected int[] getMFIndexes(Type type, double x, double threshold) {
  LinguisticLabel mf[] = type.getAllMembershipFunctions();
  boolean active[] = new boolean[mf.length];
  
  int counter = 0;
  for(int i=0; i<mf.length; i++) {
   double degree = mf[i].compute(x);
   if(degree>=threshold) { active[i] = true; counter++; }
   else active[i] = false;
  }

  int index[] = new int[counter];
  for(int i=0,j=0; i<active.length; i++) if(active[i]) { index[j] = i; j++; }
  return index;
 }

 //-------------------------------------------------------------//
 // Calcula el grado de activacion de un antecedente		//
 //-------------------------------------------------------------//

 protected double computeActivationDegree(int index[], double value[]) {
  double degree = 1.0;
  for(int i=0; i<index.length; i++) {
   LinguisticLabel mf[] =  inputtype[i].getAllMembershipFunctions();
   double dom = mf[index[i]].compute(value[i]);
   if(config.systemstyle.conjunction == XfdmSystemStyle.MIN) {
    if(dom < degree) degree = dom;
   } else {
    degree *= dom;
   }
  }
  return degree;
 }

 //=============================================================//
 //                Metodos que generan un tipo                  //
 //=============================================================//

 //-------------------------------------------------------------//
 // Genera un conjunto de MFs predefinidas para una entrada	//
 //-------------------------------------------------------------//

 protected void createInputMemFuncs(Type type, int sel, int mfs) {
  switch(sel) {
   case XfdmInputStyle.FREE_TRIANGLES:
    createFreeTriangles(type,mfs);
    break;
   case XfdmInputStyle.FREE_SH_TRIANGLES:
    createFreeShoulderedTriangles(type,mfs);
    break;
   case XfdmInputStyle.FREE_GAUSSIANS:
    createFreeGaussians(type,mfs);
    break;
   case XfdmInputStyle.TRIANGULAR_FAMILY:
    createTriangularFamily(type,mfs);
    break;
   case XfdmInputStyle.SH_TRIANGULAR_FAMILY:
    createShoulderedTriangularFamily(type,mfs); 
    break;
   case XfdmInputStyle.BSPLINES_FAMILY:
    createBsplinesFamily(type,mfs);
    break;
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de triangulos equiespaciados               //
 //-------------------------------------------------------------//

 private void createFreeTriangles(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs-1);
  double param[] = new double[3];
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  param[0] = min - r;
  param[1] = min;
  param[2] = min + r;
  pmf[0] = new pkg.xfl.mfunc.triangle();
  pmf[0].set("mf0",u);
  try { pmf[0].set(param); } catch(XflException ex) {}

  for(int i=1; i<mfs-1; i++) {
   param[0] = min + (i-1)*r;
   param[1] = min + i*r;
   param[2] = min + (i+1)*r;
   pmf[i] = new pkg.xfl.mfunc.triangle();
   pmf[i].set("mf"+i,u);
   try { pmf[i].set(param); } catch(XflException ex) {}
  }

  param[0] = max - r;
  param[1] = max;
  param[2] = max + r;
  pmf[mfs-1] = new pkg.xfl.mfunc.triangle();
  pmf[mfs-1].set("mf"+(mfs-1),u);
  try { pmf[mfs-1].set(param); } catch(XflException ex) {}

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de triangulos y trapecios                  //
 //-------------------------------------------------------------//

 private void createFreeShoulderedTriangles(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs+1);
  double param4[] = new double[4];
  double param3[] = new double[3];
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  param4[0] = min-r;
  param4[1] = min;
  param4[2] = min+r;
  param4[3] = min+2*r;
  pmf[0] = new pkg.xfl.mfunc.trapezoid();
  pmf[0].set("mf0",u);
  try { pmf[0].set(param4); } catch(XflException ex) {}

  for(int i=1; i<mfs-1; i++) {
   param3[0] = min + i*r;
   param3[1] = min + (i+1)*r;
   param3[2] = min + (i+2)*r;
   pmf[i] = new pkg.xfl.mfunc.triangle();
   pmf[i].set("mf"+i,u);
   try { pmf[i].set(param3); } catch(XflException ex) {}
  }

  param4[0] = max-2*r;
  param4[1] = max-r;
  param4[2] = max;
  param4[3] = max+r;
  pmf[mfs-1] = new pkg.xfl.mfunc.trapezoid();
  pmf[mfs-1].set("mf"+(mfs-1), u);
  try { pmf[mfs-1].set(param4); } catch(XflException ex) {}

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de campanas equiespaciadas                 //
 //-------------------------------------------------------------//

 private void createFreeGaussians(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs-1);
  double param[] = new double[2];
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  param[0] = min;
  param[1] = 0.6*r;
  pmf[0] = new pkg.xfl.mfunc.bell();
  pmf[0].set("mf0", u);
  try { pmf[0].set(param); } catch(XflException ex) {}

  for(int i=1; i<mfs-1; i++) {
   param[0] = min + i*r;
   pmf[i] = new pkg.xfl.mfunc.bell();
   pmf[i].set("mf"+i, u);
   try { pmf[i].set(param); } catch(XflException ex) {}
  }

  param[0] = max;
  pmf[mfs-1] = new pkg.xfl.mfunc.bell();
  pmf[mfs-1].set("mf"+(mfs-1), u);
  try { pmf[mfs-1].set(param); } catch(XflException ex) {}

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de campanas y sigmoides                    //
 //-------------------------------------------------------------//

 /*
 private void createFreeShoulderedGaussians(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs+1);
  double param[] = new double[2];
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  param[0] = min + 1.5*r;
  param[1] = -0.17*r;
  pmf[0] = new pkg.xfl_mf_sigma();
  pmf[0].set("mf0", u);
  try { pmf[0].set(param); } catch(XflException ex) {}

  for(int i=1; i<mfs-1; i++) {
   param[0] = min + (i+1)*r;
   param[1] = 0.6*r;
   pmf[i] = new pkg.xfl_mf_bell();
   pmf[i].set("mf"+i, u);
   try { pmf[i].set(param); } catch(XflException ex) {}
  }

  param[0] = max - 1.5*r;
  param[1] = 0.17*r;
  pmf[mfs-1] = new pkg.xfl_mf_sigma();
  pmf[mfs-1].set("mf"+(mfs-1), u);
  try { pmf[mfs-1].set(param); } catch(XflException ex) {}

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }
*/
 //-------------------------------------------------------------//
 // Crea un conjunto de trapecios equiespaciados                //
 //-------------------------------------------------------------//

 /*
 private void createFreeTrapezoids(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(3*mfs-1);
  double param[] = new double[4];
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  param[0] = min - r;
  param[1] = min;
  param[2] = min + 2*r;
  param[3] = min + 3*r;
  pmf[0] = new pkg.xfl_mf_trapezoid();
  pmf[0].set("mf0", u);
  try { pmf[0].set(param); } catch(XflException ex) {}

  for(int i=1; i<mfs-1; i++) {
   param[0] = min + (3*i-1)*r;
   param[1] = min + 3*i*r;
   param[2] = min + (3*i+2)*r;
   param[3] = min + (3*i+3)*r;
   pmf[i] = new pkg.xfl_mf_trapezoid();
   pmf[i].set("mf"+i, u);
   try { pmf[i].set(param); } catch(XflException ex) {}
  }

  param[0] = max - 3*r;
  param[1] = max - 2*r;
  param[2] = max;
  param[3] = max + r;
  pmf[mfs-1] = new pkg.xfl_mf_trapezoid();
  pmf[mfs-1].set("mf"+(mfs-1), u);
  try { pmf[mfs-1].set(param); } catch(XflException ex) {}

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }
*/
 //-------------------------------------------------------------//
 // Crea una familia de triangulos equiespaciados               //
 //-------------------------------------------------------------//

 private void createTriangularFamily(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs-1);
  double param[] = new double[mfs-2];
  for(int i=0; i<param.length; i++) param[i] = min + (i+1)*r;

  Family fam = new pkg.xfl.family.triangular();
  fam.set("fam",type);
  try { fam.set(param); type.addFamily(fam); } catch(XflException ex) {}

  for(int i=0; i<mfs; i++) {
   FamiliarMemFunc fmf = new FamiliarMemFunc("mf"+i,fam,i);
   try { type.add(fmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea una familia de triangulos y trapecios			//
 //-------------------------------------------------------------//

 private void createShoulderedTriangularFamily(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs+1);
  double param[] = new double[mfs];
  for(int i=0; i<param.length; i++) param[i] = min + (i+1)*r;

  Family fam = new pkg.xfl.family.sh_triangular();
  fam.set("fam",type);
  try { fam.set(param); type.addFamily(fam); } catch(XflException ex) {}

  for(int i=0; i<mfs; i++) {
   FamiliarMemFunc fmf = new FamiliarMemFunc("mf"+i,fam,i);
   try { type.add(fmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea una familia de B-splines				//
 //-------------------------------------------------------------//

 private void createBsplinesFamily(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double r = (max-min)/(mfs-2);
  double param[] = new double[mfs-3];
  for(int i=0; i<param.length; i++) param[i] = min + (i+1)*r;

  Family fam = new pkg.xfl.family.spline();
  fam.set("fam",type);
  try { fam.set(param); type.addFamily(fam); } catch(XflException ex) {}

  for(int i=0; i<mfs; i++) {
   FamiliarMemFunc fmf = new FamiliarMemFunc("mf"+i,fam,i);
   try { type.add(fmf); } catch(XflException e) {}
  }
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de etiquetas (singularidades)		//
 //-------------------------------------------------------------//

 private void createClasses(Type type, double[] value) {
  Universe u = type.getUniverse();
  ParamMemFunc pmf[] = new ParamMemFunc[value.length];

  for(int i=0; i<value.length; i++) {
   pmf[i] = new pkg.xfl.mfunc.singleton();
   pmf[i].set("mf"+i, u);
   pmf[i].set(value[i]);
  }

  try { for(int i=0; i<pmf.length; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de campanas centradas                      //
 //-------------------------------------------------------------//

 private void createBells(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double param[] = new double[2];
  param[0] = (min + max)/2;
  param[1] = (max - min)/8;
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  for(int i=0; i<mfs; i++) {
   pmf[i] = new pkg.xfl.mfunc.bell();
   pmf[i].set("mf"+i, u);
   try { pmf[i].set(param); } catch(XflException ex) {}
  }

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de singularidades centradas                //
 //-------------------------------------------------------------//

 private void createSingletons(Type type, int mfs) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double min = u.min();
  double max = u.max();
  double middle = (min + max)/2 ;
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  for(int i=0; i<mfs; i++) {
   pmf[i] = new pkg.xfl.mfunc.singleton();
   pmf[i].set("mf"+i, u);
   pmf[i].set(middle);
  }

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

 //-------------------------------------------------------------//
 // Crea un conjunto de funciones de Takagi-Sugeno		//
 //-------------------------------------------------------------//

 private void createTakagi(Type type, int mfs, int np) {
  if(mfs == 0) return;
  Universe u = type.getUniverse();
  double param[] = new double[np];
  for(int i=0; i<np; i++) param[i] = 1.0;
  ParamMemFunc pmf[] = new ParamMemFunc[mfs];

  for(int i=0; i<mfs; i++) {
   pmf[i] = new pkg.xfl.mfunc.parametric();
   pmf[i].set("mf"+i, u);
   try { pmf[i].set(param); } catch(XflException ex) {}
  }

  try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
  catch(XflException e) {}
 }

}


