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
//         ALGORITMO DE NAUCK DE REGLAS MAS CORRECTAS		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;
import java.util.Vector;

public class XfdmNauck extends XfdmActiveRules {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int numclass;
 private boolean global;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //-------------------------------------------------------------//
 // Constructor completo					//
 //-------------------------------------------------------------//

 public XfdmNauck(int numclass, boolean global) {
  this.numclass = numclass;
  this.global = global;
 }

 //-------------------------------------------------------------//
 // Constructor con el numero de clases				//
 //-------------------------------------------------------------//

 public XfdmNauck(int numclass) {
  this.numclass = numclass;
  this.global = true;
 }

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfdmNauck() {
  this.numclass = 5;
  this.global = true;
 }

 //-------------------------------------------------------------//
 // Constructor desde el fichero de configuracion		//
 //-------------------------------------------------------------//

 public XfdmNauck(double param[]) {
  this.numclass = (int) param[0];
  this.global = (((int) param[1]) == 1);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene un duplicado del objeto				//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfdmNauck(numclass,global);
 }

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public String toString() {
  return "Nauck (Best-performance rule extraction)";
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  int gb = (global? 1 : 0);
  return "xfdm_algorithm(Nauck,"+numclass+","+gb+")";
 }

 //-------------------------------------------------------------//
 // Obtiene el numero de reglas a seleccionar			//
 //-------------------------------------------------------------//

 public int getNumberOfRules() {
  return numclass;
 }

 //-------------------------------------------------------------//
 // Verifica si es el numero de reglas total o por clase	//
 //-------------------------------------------------------------//

 public boolean isGlobal() {
  return this.global;
 }

 //-------------------------------------------------------------//
 // Asigna el numero de reglas a seleccionar			//
 //-------------------------------------------------------------//

 public void setNumberOfRules(int number) {
  this.numclass = number;
 }

 //-------------------------------------------------------------//
 // Asigna si es el numero de reglas total o por clase		//
 //-------------------------------------------------------------//

 public void setGlobal(boolean global) {
  this.global = global;
 }

 //-------------------------------------------------------------//
 // Selecciona las reglas mas correctas				//
 //-------------------------------------------------------------//

 public Vector pruneRules(Vector rules) {
  if(global) return pruneRulesGlobal(rules);
  else return pruneRulesLocal(rules);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Selecciona las N mejores reglas				//
 //-------------------------------------------------------------//

 private Vector pruneRulesGlobal(Vector rules) {
  int sort[][] = getSortedList(rules);
  Vector pruned = new Vector();
  for(int i=0;i<numclass;i++) pruned.add(rules.elementAt(sort[i][0]));
  return pruned;
 }

 //-------------------------------------------------------------//
 // Selecciona las N mejores reglas para cada clase		//
 //-------------------------------------------------------------//

 private Vector pruneRulesLocal(Vector rules) {
  int included[][] = new int[outputtype.length][];
  for(int i=0; i<outputtype.length; i++) {
   LinguisticLabel lb[] = outputtype[i].getAllMembershipFunctions();
   included[i] = new int[lb.length];
  }

  int sorted[][] = getSortedList(rules);

  Vector pruned = new Vector();
  for(int i=0; i<sorted.length; i++) {
   if(isFull(included)) break;
   if(!isFull(included,sorted[i])) {
    pruned.add(rules.elementAt(sorted[i][0]));
    increase(included,sorted[i]);
   }
  }
  return pruned;
 }

 //-------------------------------------------------------------//
 // Obtiene la lista de reglas ordenada por eficiencia		//
 //-------------------------------------------------------------//

 private int[][] getSortedList(Vector rules) {
  double perform[][] = new double[rules.size()][2];
  for(int i=0; i<perform.length; i++) {
   perform[i][0] = ((XfdmPseudoRule) rules.elementAt(i)).getPerformance();
   perform[i][1] = 1.0*i;
  }

  double aux0, aux1;
  for(int i=0; i<perform.length-1; i++) { 
   for(int j=i+1; j<perform.length; j++) {
    if(perform[i][0]<perform[j][0]) { 
     aux0 = perform[i][0]; aux1 = perform[i][1];
     perform[i][0] = perform[j][0]; perform[i][1] = perform[j][1];
     perform[j][0] = aux0; perform[j][1] = aux1;
    }
   }
  }

  int sorted[][] = new int[rules.size()][config.numoutputs+1];
  for(int i=0; i<sorted.length; i++) {
   sorted[i][0] = (int) perform[i][1];
   XfdmPseudoRule pseudo = (XfdmPseudoRule) rules.elementAt(sorted[i][0]);
   int clind[] = pseudo.getClassIndexes();
   for(int j=0; j<clind.length; j++) sorted[i][j+1] = clind[j];
  }
  return sorted;
 }

 //-------------------------------------------------------------//
 // Verifica si la cuenta de reglas por clases esta saturada	//
 //-------------------------------------------------------------//

 private boolean isFull(int counter[][]) {
  for(int i=0; i<counter.length; i++)
   for(int j=0; j<counter[i].length; j++)
    if(counter[i][j] <numclass) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Verifica si las clases indicadas estan saturadas		//
 //-------------------------------------------------------------//

 private boolean isFull(int counter[][], int index[]) {
  for(int i=0; i<counter.length; i++)
   if(counter[i][index[i+1]] <numclass) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Incrementa el contador de las clases indicadas		//
 //-------------------------------------------------------------//

 private void increase(int counter[][], int index[]) {
  for(int i=0; i<counter.length; i++) counter[i][index[i+1]]++;
 }
}


