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
//	 CLASE ABSTRACTA DE ALGORITMOS DE REGLAS ACTIVAS	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;
import java.util.Vector;

public abstract class XfdmActiveRules extends XfdmAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //		   METODOS PUBLICOS ABSTRACTOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public abstract String toString();

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public abstract String toCode();

 //-------------------------------------------------------------//
 // Selecciona las reglas entre las reglas activas		//
 //-------------------------------------------------------------//

 public abstract Vector pruneRules(Vector rules);

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Metodo que construye el sistema a partir de los datos	//
 //-------------------------------------------------------------//

 public void compute(Specification spec, XfdmConfig config)
 throws XflException {

  this.spec = spec;
  this.config = config;
  this.pattern = config.getPatterns();

  this.opset = createOperatorSet();
  spec.addOperatorset(opset);

  this.inputtype = createInputTypes();
  for(int i=0; i<inputtype.length; i++) spec.addType(inputtype[i]);

  this.outputtype = createOutputTypes(0);
  for(int i=0; i<outputtype.length; i++) spec.addType(outputtype[i]);

  this.rulebase  = createEmptyRulebase();
  fillRulebase(this.rulebase);
  spec.addRulebase(rulebase);

  if(config.systemstyle.creation) createSystemStructure();

  spec.setModified(true);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Genera la base de reglas con las reglas activas		//
 //-------------------------------------------------------------//

 private void fillRulebase(Rulebase base) {
  boolean classif = (config.systemstyle.defuz==XfdmSystemStyle.CLASSIFICATION);

  Vector activerules = createActiveRules(base,classif);

  Vector selectedrules = pruneRules(activerules);

  createRules(base,selectedrules);
 }

 //=============================================================//
 //		Metodos para obtener las reglas activas		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene la lista de (pseudo) reglas activas			//
 //-------------------------------------------------------------//

 private Vector createActiveRules(Rulebase base, boolean classif) {
  Variable ivar[] = base.getInputs();
  Variable ovar[] = base.getOutputs();
  Vector pseudorules = new Vector();
  double label[][] = null;
  int numclasses[] = null;

  if(classif) {
   label = new double[ovar.length][];
   numclasses = new int[ovar.length];
   for(int i=0; i<ovar.length; i++) {
    LinguisticLabel mf[] = ovar[i].getType().getAllMembershipFunctions();
    numclasses[i] = mf.length;
    label[i] = new double[mf.length];
    for(int j=0; j<mf.length; j++) label[i][j] = mf[j].get()[0];
   }
  }

  for(int p=0; p<pattern.input.length; p++) {
   int index[] = new int[ivar.length];
   for(int i=0; i<ivar.length; i++) {
    index[i] = getMFIndex(ivar[i].getType(),pattern.input[p][i]);
   }
   double doa = computeActivationDegree(index, pattern.input[p]);
   boolean exists = false;
   for(int i=0; i<pseudorules.size(); i++) {
    XfdmPseudoRule pseudo = (XfdmPseudoRule) pseudorules.elementAt(i);
    if(pseudo.test(index)) {
     if(!classif) pseudo.update(pattern.output[p],doa);
     exists = true;
     break;
    }
   }
   if(!exists) {
    if(classif) {
     XfdmPseudoRule pseudo = new XfdmPseudoRule(index,numclasses);
     pseudorules.addElement(pseudo);
    } else {
     XfdmPseudoRule pseudo = new XfdmPseudoRule(index,ovar.length);
     pseudo.update(pattern.output[p],doa);
     pseudorules.addElement(pseudo);
    } 
   }
  }

  if(classif) {
   for(int p=0; p<pattern.input.length; p++) {
    for(int i=0; i<pseudorules.size(); i++) {
     XfdmPseudoRule pseudo = (XfdmPseudoRule) pseudorules.elementAt(i);
     int index[] = pseudo.getAntecedent();
     double doa = computeActivationDegree(index, pattern.input[p]);
     pseudo.update( getLabelIndexes(pattern.output[p],label) ,doa);
    }
   }
  }

  return pseudorules;
 }

 //-------------------------------------------------------------//
 // Obtiene los indices correspondientes a las clases activas	//
 //-------------------------------------------------------------//

 private int[] getLabelIndexes(double[] pt, double[][] label) {
  int index[] = new int[label.length];
  for(int i=0; i<pt.length; i++)
   for(int j=0; j<label[i].length; j++)
    if(label[i][j] == pt[i]) index[i] = j;
  return index;
 }

 //=============================================================//
 //		Metodos para generar las reglas			//
 //=============================================================//

 //-------------------------------------------------------------//
 // Crea las reglas y las annade a la base de reglas		//
 //-------------------------------------------------------------//

 private void createRules(Rulebase base, Vector rules) {
  switch(config.systemstyle.defuz) {
   case XfdmSystemStyle.CLASSIFICATION:
    createClassificationRules(base,rules);
    break;
   case XfdmSystemStyle.FUZZYMEAN:
    createFuzzyMeanRules(base,rules);
    break;
   case XfdmSystemStyle.WEIGHTED:
    createWeightedFuzzyMeanRules(base,rules);
    break;
   case XfdmSystemStyle.TAKAGI:
    createTakagiSugenoRules(base,rules);
    break;
  }
 }

 //-------------------------------------------------------------//
 // Genera las reglas de tipo classificador			//
 //-------------------------------------------------------------//

 private void createClassificationRules(Rulebase base, Vector rules) {
  Variable ovar[] = base.getOutputs();

  for(int r=0; r<rules.size(); r++) {
   XfdmPseudoRule pseudo = (XfdmPseudoRule) rules.elementAt(r);
   Rule rule = new Rule(pseudo.createAntecedent(base));
   int clind[] = pseudo.getClassIndexes();
   for(int j=0; j<ovar.length; j++) {
    LinguisticLabel lb[] = ovar[j].getType().getAllMembershipFunctions();
    rule.add(new Conclusion(ovar[j],lb[clind[j]],base));
   }

   base.addRule(rule);
  }
 }

 //-------------------------------------------------------------//
 // Genera las reglas de tipo FuzzyMean				//
 //-------------------------------------------------------------//

 private void createFuzzyMeanRules(Rulebase base, Vector rules) {
  Variable ovar[] = base.getOutputs();

  for(int r=0; r<rules.size(); r++) {
   XfdmPseudoRule pseudo = (XfdmPseudoRule) rules.elementAt(r);
   Rule rule = new Rule(pseudo.createAntecedent(base));
   double center[] = pseudo.getCenter();
   for(int j=0; j<ovar.length; j++) {
    ParamMemFunc mf = new pkg.xfl.mfunc.singleton();
    mf.set("mf"+r, ovar[j].getType().getUniverse());
    mf.set(center[j]);
    try { ovar[j].getType().add(mf); } catch(Exception ex) {}
    rule.add(new Conclusion(ovar[j],mf,base));
   }

   base.addRule(rule);
  }
 }

 //-------------------------------------------------------------//
 // Genera las reglas de tipo WeightedFuzzyMean			//
 //-------------------------------------------------------------//

 private void createWeightedFuzzyMeanRules(Rulebase base, Vector rules) {
  Variable ovar[] = base.getOutputs();

  for(int r=0; r<rules.size(); r++) {
   XfdmPseudoRule pseudo = (XfdmPseudoRule) rules.elementAt(r);
   Rule rule = new Rule(pseudo.createAntecedent(base));
   double center[] = pseudo.getCenter();
   double param[] = new double[2];
   for(int j=0; j<ovar.length; j++) {
    Universe u = ovar[j].getType().getUniverse();
    ParamMemFunc mf = new pkg.xfl.mfunc.bell();
    mf.set("mf"+r, u);
    param[0] = center[j];
    param[1] = (u.max() - u.min())/8;
    try { mf.set(param); ovar[j].getType().add(mf); } catch(Exception ex) {}
    rule.add(new Conclusion(ovar[j],mf,base));
   }

   base.addRule(rule);
  }
 }

 //-------------------------------------------------------------//
 // Genera las reglas de tipo TakagiSugeno			//
 //-------------------------------------------------------------//

 private void createTakagiSugenoRules(Rulebase base, Vector rules) {
  Variable ovar[] = base.getOutputs();
  Variable ivar[] = base.getInputs();

  for(int r=0; r<rules.size(); r++) {
   XfdmPseudoRule pseudo = (XfdmPseudoRule) rules.elementAt(r);
   Rule rule = new Rule(pseudo.createAntecedent(base));
   double center[] = pseudo.getCenter();
   double param[] = new double[ivar.length+1];
   for(int j=0; j<ovar.length; j++) {
    ParamMemFunc mf = new pkg.xfl.mfunc.parametric();
    mf.set("mf"+r, ovar[j].getType().getUniverse());
    param[0] = center[j];
    try { mf.set(param); ovar[j].getType().add(mf); } catch(Exception ex) {}
    rule.add(new Conclusion(ovar[j],mf,base));
   }

   base.addRule(rule);
  }
 }
}


