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
//		    INFORMACION DE UNA REGLA			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;

public class XfdmPseudoRule {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int[] antecedent;
 private double[][] class_degree;
 private double[] center;
 private double degree;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor de una regla tipo controlador			//
 //-------------------------------------------------------------//

 public XfdmPseudoRule(int[] antecedent, int numvar) {
  this.antecedent = antecedent;
  this.center = new double[numvar];
  this.degree = 0;
  this.class_degree = null;
 }

 //-------------------------------------------------------------//
 // Constructor de una regla tipo clasificador			//
 //-------------------------------------------------------------//

 public XfdmPseudoRule(int[] antecedent, int[] numclass) {
  this.antecedent = antecedent;
  this.class_degree = new double[numclass.length][];
  for(int i=0; i<numclass.length; i++) {
   this.class_degree[i] = new double[numclass[i]];
  }
  this.center = null;
  this.degree = 0;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Verifica si la lista coincide con el antecedente de la regla//
 //-------------------------------------------------------------//

 public boolean test(int[] ant) {
  if(antecedent.length != ant.length) return false;
  for(int i=0; i<ant.length; i++) if(antecedent[i] != ant[i]) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Actualiza el valor del consecuente (controlador)		//
 //-------------------------------------------------------------//

 public void update(double nc[], double nd) {
  for(int i=0; i<center.length; i++) {
   center[i] = (degree*center[i] + nd*nc[i])/(degree+nd);
  }
  degree += nd;
 }

 //-------------------------------------------------------------//
 // Actualiza el valor del consecuente (clasificador)		//
 //-------------------------------------------------------------//

 public void update(int[] index, double degree) {
  for(int i=0; i<class_degree.length; i++) {
   this.class_degree[i][index[i]] += degree;
  }
 }

 //-------------------------------------------------------------//
 // Obtiene la lista de indices del antecedente			//
 //-------------------------------------------------------------//

 public int[] getAntecedent() {
  return antecedent;
 }

 //-------------------------------------------------------------//
 // Obtiene el indice de la clase con mayor grado de activacion	//
 //-------------------------------------------------------------//

 public int[] getClassIndexes() {
  double max[] = new double[class_degree.length];
  int index[] = new int[class_degree.length];
  for(int v=0; v<class_degree.length; v++) {
   for(int i=0; i<class_degree[v].length; i++) {
    if(class_degree[v][i]>=max[v]) { index[v] = i; max[v] = class_degree[v][i];}
   }
  }
  return index;
 }

 //-------------------------------------------------------------//
 // Obtiene la eficiencia de la regla				//
 //-------------------------------------------------------------//

 public double getPerformance() {
  int selected[] = getClassIndexes();
  double performance = 0;
  for(int v=0; v<class_degree.length; v++) {
   for(int i=0; i<class_degree[v].length; i++) {
    if(i==selected[v]) performance += class_degree[v][i];
    else performance -= class_degree[v][i];
   }
  }
  return performance/class_degree.length;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del centro de las MF de salida		//
 //-------------------------------------------------------------//

 public double[] getCenter() {
  return this.center;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor del grado de activacion acumulado		//
 //-------------------------------------------------------------//

 public double getDegree() {
  return this.degree;
 }

 //-------------------------------------------------------------//
 // Genera el antecedente de la regla				//
 //-------------------------------------------------------------//

 public Relation createAntecedent(Rulebase base) {
  Variable ivar[] = base.getInputs();
  int is = Relation.IS;
  int index[] = this.antecedent;

  LinguisticLabel pmf[][] = new LinguisticLabel[index.length][];
  for(int i=0; i<index.length; i++) {
   pmf[i] = ivar[i].getType().getAllMembershipFunctions();
  }

  Relation rel = Relation.create(is,null,null,ivar[0],pmf[0][index[0]],base);
  for(int j=1; j<index.length; j++) {
   Relation nrel = Relation.create(is,null,null,ivar[j],pmf[j][index[j]],base);
   rel = Relation.create(Relation.AND,rel,nrel,null,null,base);
  }

  return rel;
 }
}
