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
//   ALGORITMO DE PODADO DE REGLAS Y FUNCIONES DE PERTENENCIA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;

public class XfspPruning implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int NONE = 0;
 public static final int PRUNE_THRESHOLD = 1;
 public static final int PRUNE_WORST = 2;
 public static final int PRUNE_EXCEPT = 3;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int kind;
 private double param;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfspPruning() {
  this.kind = NONE;
  this.param = -1;
 }

 //-------------------------------------------------------------//
 // Constructor con datos					//
 //-------------------------------------------------------------//

 public XfspPruning(int kind, double param) {
  this.kind = kind;
  this.param = param;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Duplicado del objeto					//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfspPruning(kind, param);
 }

 //-------------------------------------------------------------//
 // Obtiene el tipo de podado					//
 //-------------------------------------------------------------//

 public int getKind() {
  return this.kind;
 }

 //-------------------------------------------------------------//
 // Obtiene el parametro del podado				/
 //-------------------------------------------------------------//

 public double getParam() {
  return this.param;
 }

 //-------------------------------------------------------------//
 // Verifica que el proceso este configurado			//
 //-------------------------------------------------------------//

 public boolean isOn() {
  return (this.kind != NONE);
 }

 //-------------------------------------------------------------//
 // Configura el proceso desde una directiva			//
 //-------------------------------------------------------------//

 public void set(String name, double[] params) {
  if(name.equals("prune_threshold"))
   { kind = PRUNE_THRESHOLD; this.param = params[0]; }
  if(name.equals("prune_worst"))
   { kind = PRUNE_WORST; this.param = params[0]; }
  if(name.equals("prune_except"))
   { kind = PRUNE_EXCEPT; this.param = params[0]; }
 }

 //-------------------------------------------------------------//
 // Codigo de la directiva de configuracion			//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String src = "";
  if(kind == PRUNE_THRESHOLD)
    src = "(prune_threshold,"+param+")"+eol;
  if(kind == PRUNE_WORST)
    src = "(prune_worst,"+(int) param+")"+eol;
  if(kind == PRUNE_EXCEPT)
    src = "(prune_except,"+(int) param+")"+eol;
  return src;
 }

 //-------------------------------------------------------------//
 // Ejecucion del proceso de simplificacion			//
 //-------------------------------------------------------------//

 public String compute(Specification spec, double[][] pattern) {
  if(kind == NONE) return null;
  int rules = 0;
  setMaxActivation(spec, pattern);
  if(kind == PRUNE_THRESHOLD) rules = prune_threshold(spec,param);
  if(kind == PRUNE_WORST) rules = prune_worst(spec, (int) param);
  if(kind == PRUNE_EXCEPT) rules = prune_best(spec, (int) param);
  int mfs = prune_mfs(spec);
  return "Pruning: "+rules+" rules and "+mfs+" membership functions pruned.";
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Calcula el grado de activacion maxima de cada regla		//
 //-------------------------------------------------------------//

 private void setMaxActivation(Specification spec, double[][] pattern) {
  SystemModule system = spec.getSystemModule();
  Rulebase[] rulebase = spec.getRulebases();
  for(int i=0; i<rulebase.length; i++) rulebase[i].resetMaxActivation();
  for(int p=0; p<pattern.length; p++) system.fuzzyInference(pattern[p]);
 }

 //-------------------------------------------------------------//
 // Elimina todos las reglas que no alcanzan un umbral		//
 //-------------------------------------------------------------//

 private int prune_threshold(Specification spec, double threshold) {
  Rulebase[] rulebase = spec.getRulebases();
  int pruned = 0;
  for(int i=0; i<rulebase.length; i++) {
   Rule[] rule = rulebase[i].getRules();
   for(int j=0; j<rule.length; j++) 
    if(rule[j].getMaxActivation() < threshold )
     { rulebase[i].remove(rule[j]); pruned++; }
  }
  return pruned;
 }

 //-------------------------------------------------------------//
 // Elimina todas las reglas salvo las N mejores		//
 //-------------------------------------------------------------//

 private int prune_best(Specification spec, int best) {
  Rulebase[] rulebase = spec.getRulebases(); int count=0;
  for(int i=0; i<rulebase.length; i++) count+=rulebase[i].getRules().length;
  Rule[] all = new Rule[count];
  for(int i=0,k=0; i<rulebase.length; i++) {
   Rule[] rule = rulebase[i].getRules();
   for(int j=0; j<rule.length; j++) { all[k] = rule[j]; k++; }
  }
  for(int i=0; i<all.length; i++)
   for(int j=i+1; j<all.length; j++)
    if(all[i].getMaxActivation() < all[j].getMaxActivation()) {
     Rule aux = all[i];
     all[i] = all[j];
     all[j] = aux;
    }
  for(int i=best; i<all.length; i++)
   for(int j=0; j<rulebase.length; j++) rulebase[j].remove(all[i]);
  return all.length - best;
 }

 //-------------------------------------------------------------//
 // Elimina las N peores reglas					//
 //-------------------------------------------------------------//

 private int prune_worst(Specification spec, int worst) {
  Rulebase[] rulebase = spec.getRulebases(); int count=0;
  for(int i=0; i<rulebase.length; i++) count+=rulebase[i].getRules().length;
  Rule[] all = new Rule[count];
  for(int i=0,k=0; i<rulebase.length; i++) {
   Rule[] rule = rulebase[i].getRules();
   for(int j=0; j<rule.length; j++) { all[k] = rule[j]; k++; }
  }
  for(int i=0; i<all.length; i++)
   for(int j=i+1; j<all.length; j++) 
    if(all[i].getMaxActivation() < all[j].getMaxActivation()) {
     Rule aux = all[i];
     all[i] = all[j];
     all[j] = aux;
    }
  for(int i=all.length-worst; i<all.length; i++)
   for(int j=0; j<rulebase.length; j++) rulebase[j].remove(all[i]);
  return worst;
 }

 //-------------------------------------------------------------//
 // Elimina las funciones de pertenencia no utilizadas		//
 //-------------------------------------------------------------//

 private int prune_mfs(Specification spec) {
  int pruned = 0;
  Type[] type = spec.getTypes();
  for(int i=0; i<type.length; i++) pruned += type[i].prune();
  return pruned;
 }
}
