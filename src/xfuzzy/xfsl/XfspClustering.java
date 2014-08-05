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
//    ALGORITMO DE AGRUPAMIENTO DE FUNCIONES DE PERTENENCIA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;
import java.util.Vector;

public class XfspClustering implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int NONE = 0;
 public static final int CLUST_BEST = 1;
 public static final int CLUST_SELECTED = 2;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int clustering;
 private int clusters;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			  CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfspClustering() {
  this.clustering = NONE;
  this.clusters = -1;
 }

 //-------------------------------------------------------------//
 // Constructor con datos					//
 //-------------------------------------------------------------//

 public XfspClustering(int kind, int num) {
  this.clustering = kind;
  this.clusters = num;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Duplicar el objeto						//
 //-------------------------------------------------------------//

 public Object clone() {
  return new XfspClustering(this.clustering, this.clusters);
 }

 //-------------------------------------------------------------//
 // Obtiene el tipo de agrupamiento				//
 //-------------------------------------------------------------//

 public int getKind() {
  return this.clustering;
 }

 //-------------------------------------------------------------//
 // Obtiene el numero de clusters				//
 //-------------------------------------------------------------//

 public int getParam() {
  return this.clusters;
 }

 //-------------------------------------------------------------//
 // Verificar si esta activo					//
 //-------------------------------------------------------------//

 public boolean isOn() {
  return (this.clustering != NONE);
 }

 //-------------------------------------------------------------//
 // Configura el proceso desde una directiva			//
 //-------------------------------------------------------------//

 public void set(String name, double[] param) {
  if(name.equals("output_clustering") && param.length == 0)
   { clustering = CLUST_BEST; clusters = -1; }
  if(name.equals("output_clustering") && param.length > 0)
   { clustering = CLUST_SELECTED; clusters = (int) param[0]; }
 }

 //-------------------------------------------------------------//
 // Codigo de la directiva de configuracion			//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String src = "";
  if(clustering == CLUST_SELECTED)
    src = "(output_clustering,"+clusters+")"+eol;
  if(clustering == CLUST_BEST)
    src = "(output_clustering)"+eol;
  return src;
 }

 //-------------------------------------------------------------//
 // Ejecucion del proceso de simplificacion			//
 //-------------------------------------------------------------//

 public String[] compute(Specification spec) {
  if(clustering == NONE) return new String[0];
  Vector message = new Vector();
  Vector reduced = new Vector();
  Rulebase[] rulebase = spec.getRulebases();
  for(int i=0; i<rulebase.length; i++) {
   Variable[] output = rulebase[i].getOutputs();
   for(int j=0; j<output.length; j++) {
    Type type = output[j].getType();
    if(reduced.contains(type) || !reducible(type)) continue;
    message.addElement( clustering(type,spec) );
    reduced.addElement(type);
   }
  }
  Object[] omsg = message.toArray();
  String msg[] = new String[omsg.length]; 
  for(int i=0; i<omsg.length; i++) msg[i] = (String) omsg[i];
  return msg;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Verifica que un tipo sea agrupable				//
 //-------------------------------------------------------------//

 private boolean reducible(Type type) {
  ParamMemFunc[] mf = type.getParamMembershipFunctions();
  if(mf.length == 0) return false;
  String mfclass = mf[0].getClass().getName();
  for(int i=0; i<mf.length; i++)
   if(!(mf[i].getClass().getName().equals(mfclass))) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Simplifica las funciones de pertenencia de un tipo		//
 //-------------------------------------------------------------//

 private String clustering(Type type, Specification spec) {
  ParamMemFunc[] mf = type.getParamMembershipFunctions();
  double[][] data = new double[mf.length][];
  for(int i=0; i<mf.length; i++) data[i] = mf[i].get();
  int M = mf.length;
  int C = (clustering == CLUST_BEST? num_cluster(data) : clusters);
  XfspCluster cl = new XfspCluster(data, C);
  simplifyType(type,cl,spec);
  String msg = "Clustering: Membership functions of type "+type.getName();
  msg += " reduced from "+M+" to "+C+".";
  return msg;
 }

 //-------------------------------------------------------------//
 // Calcula el numero de clusters optimo			//
 //-------------------------------------------------------------//

 private int num_cluster(double[][] data) {
  double max=0;
  int num=2;

  for(int i=2; i<data.length/2 ; i++) {
   XfspCluster cl = new XfspCluster(data, i);
   double eval = cl.evaluation();
   if(eval>max) { max = eval; num = i;}
  }
  return num;
 }

 //-------------------------------------------------------------//
 // Sustituye las funciones de un tipo por los clusters		//
 //-------------------------------------------------------------//

 private void simplifyType(Type type, XfspCluster cl, Specification spec) {
  ParamMemFunc[] pmf= new ParamMemFunc[cl.cluster.length];
  for(int i=0; i<cl.cluster.length; i++) {
   pmf[i] = createClusterMemFunc(type, cl, i);
  }
  ParamMemFunc[] omf = type.getParamMembershipFunctions();
  for(int j=0; j<omf.length; j++) spec.exchange(omf[j],pmf[ cl.assign[j] ]);
  type.setMembershipFunctions(pmf);
 }

 //-------------------------------------------------------------//
 // Genera una MF con los parametros del cluster		//
 //-------------------------------------------------------------//

 private ParamMemFunc createClusterMemFunc(Type type, XfspCluster cl, int i) {
  ParamMemFunc[] omf = type.getParamMembershipFunctions();
  int index = 0;
  for(int j = 0; j < omf.length; j++) if(cl.assign[j] == i) { index=j; break; }

  ParamMemFunc pmf = (ParamMemFunc) omf[index].clone(type.getUniverse());
  pmf.setLabel("mf"+i);
  double oldparam[] = pmf.get();
  boolean wrong = false;
  try { pmf.set(cl.cluster[i]); } catch(Exception ex) { wrong = true; }
  if(wrong || !pmf.test()) {
   try { pmf.set(oldparam); } catch(Exception ex) { }
   Parameter param[] = pmf.getParameters();
   for(int j=0; j<cl.cluster[i].length && j<param.length; j++) {
    param[j].setDesp(cl.cluster[i][j] - param[j].value);
   } 
   pmf.update();
  }
  return pmf;
 } 
 
}
