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
//	  ALGORITMO DE SENHADJI DE REGLAS NO REDUNDANTES	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.lang.*;
import java.util.Vector;

public class XfdmSenhadji extends XfdmActiveRules {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int numrules;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 //-------------------------------------------------------------//
 // Constructor con el numero de clases				//
 //-------------------------------------------------------------//

 public XfdmSenhadji(int numrules) {
  this.numrules = numrules;
 }

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfdmSenhadji() {
  this.numrules = 5;
 }

 //-------------------------------------------------------------//
 // Constructor desde el fichero de configuracion		//
 //-------------------------------------------------------------//

 public XfdmSenhadji(double param[]) {
  this.numrules = (int) param[0];
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene un duplicado del objeto				//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfdmSenhadji(numrules);
 }

 //-------------------------------------------------------------//
 // Obtiene el nombre del algoritmo				//
 //-------------------------------------------------------------//

 public String toString() {
  return "Senhadji (Non-redundant rule extraction)";
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  return "xfdm_algorithm(Senhadji,"+numrules+")";
 }

 //-------------------------------------------------------------//
 // Obtiene el numero de reglas a seleccionar			//
 //-------------------------------------------------------------//

 public int getNumberOfRules() {
  return numrules;
 }

 //-------------------------------------------------------------//
 // Asigna el numero de reglas a seleccionar			//
 //-------------------------------------------------------------//

 public void setNumberOfRules(int number) {
  this.numrules = number;
 }

 //-------------------------------------------------------------//
 // Selecciona las reglas mas correctas				//
 //-------------------------------------------------------------//

 public Vector pruneRules(Vector rules) {
  int sort[] = getSortedList(rules);
  Vector selected = new Vector();
  Vector nonselected = new Vector();
  Vector redundant = new Vector();

  for(int i=0; i<numrules; i++) selected.add(rules.elementAt(sort[i]));
  for(int i=numrules; i<sort.length; i++) {
   XfdmPseudoRule pseudo = (XfdmPseudoRule) rules.elementAt(sort[i]);
   double perform = pseudo.getPerformance();
   if(perform < 0) break;
   nonselected.add(rules.elementAt(sort[i]));
  }
  boolean linkable[][][] = createLinkMatrix(true);
  boolean links[][][] = createLinkMatrix(false);
  boolean tentative[][][] = createLinkMatrix(false);
  updateLinkable(linkable,selected);

  XfdmPseudoRule rule2=searchRedundant(selected,redundant,linkable);
  while(rule2 != null && !nonselected.isEmpty()) {
   XfdmPseudoRule rule1 = getRedundant(selected,rule2,linkable);
   createTentativeLinks(rule1, rule2, links, tentative);
   boolean subst = false;
   while(!subst && !nonselected.isEmpty()) {
    XfdmPseudoRule rule3 = (XfdmPseudoRule) nonselected.elementAt(0);
    if(breakTentativeLinks(selected, rule3, tentative)) {
     nonselected.removeElement(rule3);
    } else {
     nonselected.removeElement(rule3);
     selected.addElement(rule3);
     redundant.addElement(rule2);
     updateLinks(links,tentative);
     updateLinkable(linkable,selected);
     subst = true;
    }
   }
   rule2=searchRedundant(selected,redundant,linkable);
  }

  removeRedundantRules(selected,redundant);
  return selected;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene la lista de reglas ordenada por eficiencia		//
 //-------------------------------------------------------------//

 private int[] getSortedList(Vector rules) {
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

  int sorted[] = new int[rules.size()];
  for(int i=0; i<sorted.length; i++) sorted[i] = (int) perform[i][1];
  return sorted;
 }

 //-------------------------------------------------------------//
 // Crea una matriz de enlazabilidad de las MF de un tipo	//
 //-------------------------------------------------------------//

 private boolean[][][] createLinkMatrix(boolean init) {
  boolean link[][][] = new boolean[inputtype.length][][];
  for(int i=0; i<link.length; i++) {
   LinguisticLabel label[] = inputtype[i].getAllMembershipFunctions(); 
   link[i] = new boolean[label.length][label.length];
   for(int j=0; j<label.length; j++) for(int k=0; k<label.length; k++) {
    link[i][j][k] = (j==k || init);
   }
  }
  return link;  
 }

 //-------------------------------------------------------------//
 // Actualiza la matriz de enlaces				//
 //-------------------------------------------------------------//

 private void updateLinks(boolean links[][][], boolean tentative[][][]) {
  for(int i=0; i<links.length; i++) {
   for(int j=0; j<links[i].length; j++) {
    for(int k=0; k<links[i][j].length; k++) {
     links[i][j][k] = tentative[i][j][k];
    }
   }
  }
 }

 //-------------------------------------------------------------//
 // Actualiza la matriz de funciones enlazables			//
 //-------------------------------------------------------------//

 private void updateLinkable(boolean linkable[][][], Vector rules) {
  int size = rules.size();
  for(int i=0; i<size; i++) for(int j=i+1; j<size; j++) {
   XfdmPseudoRule rule1 = (XfdmPseudoRule) rules.elementAt(i);
   XfdmPseudoRule rule2 = (XfdmPseudoRule) rules.elementAt(j);
   int input1[] = rule1.getAntecedent();
   int input2[] = rule2.getAntecedent();
   int column = differentColumn(input1,input2);
   if(column == -1 || hasSameOutputs(rule1,rule2)) continue;
   int min = (input1[column]<input2[column] ? input1[column] : input2[column]);
   int max = (input1[column]<input2[column] ? input2[column] : input1[column]);
   for(int k=0;k<=min; k++) for(int l=max; l<linkable[column].length; l++) {
    linkable[column][l][k] = false;
    linkable[column][k][l] = false;
   }
  }
 }

 //-------------------------------------------------------------//
 // Estudia si dos reglas tienen la misma salida		//
 //-------------------------------------------------------------//

 private boolean hasSameOutputs(XfdmPseudoRule rule1, XfdmPseudoRule rule2) {
  int output1[] = rule1.getClassIndexes();
  int output2[] = rule2.getClassIndexes();
  for(int i=0; i<output1.length; i++) if(output1[i] != output2[i]) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Devuelve el indice de la columna en que se diferencian	//
 // o -1 si se diferencian en mas de una columna		//
 //-------------------------------------------------------------//

 private int differentColumn(int list1[], int list2[]) {
  int column = -1;
  int count = 0;
  for(int i=0; i<list1.length; i++) {
   if(list1[i] != list2[i]) { column=i; count++; }
  }
  if(count > 1) return -1;
  return column;
 }

 //-------------------------------------------------------------//
 // Estudia si dos reglas son redundantes			//
 //-------------------------------------------------------------//

 private boolean isRedundant(XfdmPseudoRule rule1,
                             XfdmPseudoRule rule2,
                             boolean linkable[][][]) {
  if(!hasSameOutputs(rule1,rule2)) return false;

  int input1[] = rule1.getAntecedent();
  int input2[] = rule2.getAntecedent();
  for(int i=0; i<linkable.length; i++) {
   if(!linkable[i][input1[i]][input2[i]]) return false;
  }
  if(differentColumn(input1,input2)<0) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Estudia si una regla es redundante respecto a alguna de	//
 // mayor eficiencia						//
 //-------------------------------------------------------------//

 private XfdmPseudoRule getRedundant(Vector selected,
                                     XfdmPseudoRule rule,
                                     boolean linkable[][][]) {

  int size = selected.size();
  if(selected.contains(rule)) size = selected.indexOf(rule);
  for(int i=0; i<size; i++) {
   XfdmPseudoRule rule1 = (XfdmPseudoRule) selected.elementAt(i);
   if(isRedundant(rule1,rule,linkable)) return rule1;
  }
  return null;
 }

 //-------------------------------------------------------------//
 // Busca la regla redundante de menor eficiencia que no haya	//
 // sido sustituida						//
 //-------------------------------------------------------------//

 private XfdmPseudoRule searchRedundant(Vector selected, 
                                        Vector redundant,
                                        boolean linkable[][][]) {
  for(int i=selected.size()-1; i>=0; i--) {
   XfdmPseudoRule rule1 = (XfdmPseudoRule) selected.elementAt(i);
   if(redundant.contains(rule1)) continue;
   for(int j=i-1; j>=0; j--) {
    XfdmPseudoRule rule2 = (XfdmPseudoRule) selected.elementAt(j);
    if(isRedundant(rule2,rule1,linkable)) return rule1;
   }
  }
  return null;
 }

 //-------------------------------------------------------------//
 // Calcula los enlaces para sustituir rule2			//
 //-------------------------------------------------------------//

 private void createTentativeLinks(XfdmPseudoRule rule1,
                                   XfdmPseudoRule rule2,
                                   boolean links[][][],
                                   boolean tentative[][][]) {
  int input1[] = rule1.getAntecedent();
  int input2[] = rule2.getAntecedent();
  int min,max;
  for(int i=0; i<links.length; i++) {
   for(int j=0; j<links[i].length; j++) {
    for(int k=0; k<links[i][j].length; k++) {
     tentative[i][j][k] = links[i][j][k];
     if(input1[i]<=input2[i]) { min = input1[i]; max = input2[i]; }
     else { min = input2[i]; max = input1[i]; }
     if(j>=min && j<=max && k>=min && k<=max) tentative[i][j][k] = true;
    }
   }
  }
 }
  
 //-------------------------------------------------------------//
 // Estudia si una regla rompe los enlaces en que participa otra//
 //-------------------------------------------------------------//

 private boolean breakTentativeLinks(XfdmPseudoRule rule1,
                                     XfdmPseudoRule rule2,
                                     boolean tentative[][][]) {
  if(hasSameOutputs(rule1,rule2)) return false; 
  int input1[] = rule1.getAntecedent();
  int input2[] = rule2.getAntecedent();
  int column = differentColumn(input1,input2);
  if(column == -1) return false;
  return tentative[column][input1[column]][input2[column]];
 }

 //-------------------------------------------------------------//
 // Estudia si una regla rompe algun enlace			//
 //-------------------------------------------------------------//

 private boolean breakTentativeLinks(Vector selected,
                                     XfdmPseudoRule rule,
                                     boolean tentative[][][]) {
  int size = selected.size();
  for(int i=0; i<size; i++) {
   XfdmPseudoRule prule = (XfdmPseudoRule) selected.elementAt(i);
   if(breakTentativeLinks(prule,rule,tentative)) return true;
  } 
  return false;
 }

 //-------------------------------------------------------------//
 // Elimina de la seleccion las reglas redundantes		//
 //-------------------------------------------------------------//

 private void removeRedundantRules(Vector selected, Vector redundant) {
  int size = redundant.size();
  for(int i=0; i<size; i++) selected.removeElement(redundant.elementAt(i));
 } 
}


