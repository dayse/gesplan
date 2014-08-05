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
//   ALGORITMO QUE GENERA UN GRID CON COMPORTAMIENTO PLANO 	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;

public class XfdmFlatSystem extends XfdmAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 public XfdmFlatSystem(){
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public String toString() {
  return "Flat System";
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  return "xfdm_algorithm(Flat)";
 }

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

  this.outputtype = createOutputTypes(computeGridSize(inputtype));
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
 // Genera la base de reglas en forma de grid			//
 //-------------------------------------------------------------//

 private Rulebase fillRulebase(Rulebase base) {
  Variable ivar[] = base.getInputs();
  Variable ovar[] = base.getOutputs();

  int numrules = computeGridSize(inputtype);
  int is = Relation.IS;
  int index[] = new int[config.numinputs];
  LinguisticLabel pmf[][] = new LinguisticLabel[config.numinputs][];
  for(int i=0; i<config.numinputs; i++) {
   pmf[i] = inputtype[i].getAllMembershipFunctions();
  }
  LinguisticLabel omf[][] = new LinguisticLabel[config.numoutputs][];
  for(int i=0; i<config.numoutputs; i++) {
   omf[i] = outputtype[i].getAllMembershipFunctions();
  }
  for(int i=0; i<numrules; i++) {
   Relation rel = Relation.create(is,null,null,ivar[0],pmf[0][index[0]],base);
   for(int j=1; j<config.numinputs; j++) {
    Relation nrel = Relation.create(is,null,null,ivar[j],pmf[j][index[j]],base);
    rel = Relation.create(Relation.AND,rel,nrel,null,null,base);
   }
   Rule rule = new Rule(rel);
   for(int j=0; j<config.numoutputs; j++) {
    if(config.systemstyle.defuz == XfdmSystemStyle.CLASSIFICATION) {
     rule.add(new Conclusion(ovar[j],omf[j][0],base));
    } else {
     rule.add(new Conclusion(ovar[j],omf[j][i],base));
    }
   }
   base.addRule(rule);

   for(int j=config.numinputs-1; j>=0; j--) {
    index[j]++;
    if(index[j] == pmf[j].length) index[j] = 0;
    else break;
   }
  }
  return base;
 }

}


