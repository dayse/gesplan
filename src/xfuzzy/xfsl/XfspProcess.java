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
//		ALGORITMOS DE PRE Y POST PROCESADO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;

public class XfspProcess implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int PREPROCESSING = 0;
 public static final int POSTPROCESSING = 1;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int code;
 private XfspPruning pruning;
 private XfspClustering clustering;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfspProcess(int code) {
  this.code = code;
  this.pruning = new XfspPruning();
  this.clustering = new XfspClustering();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Duplicado del objeto					//
 //-------------------------------------------------------------//

 public Object clone() {
  XfspProcess dup = new XfspProcess(this.code);
  dup.setPruning((XfspPruning) this.pruning.clone());
  dup.setClustering((XfspClustering) this.clustering.clone());
  return dup;
 }

 //-------------------------------------------------------------//
 // Obtiene el tipo de proceso					//
 //-------------------------------------------------------------//

 public int getKind() {
  return this.code;
 }

 //-------------------------------------------------------------//
 // Obtiene el proceso de poda					//
 //-------------------------------------------------------------//

 public XfspPruning getPruning() {
  return this.pruning;
 }

 //-------------------------------------------------------------//
 // Actualiza el proceso de poda				//
 //-------------------------------------------------------------//

 public void setPruning(XfspPruning pruning) {
  this.pruning = pruning;
 }

 //-------------------------------------------------------------//
 // Obtiene el proceso de agrupamiento				//
 //-------------------------------------------------------------//

 public XfspClustering getClustering() {
  return this.clustering;
 }

 //-------------------------------------------------------------//
 // Actualiza el proceso de agrupamiento			//
 //-------------------------------------------------------------//

 public void setClustering(XfspClustering clustering) {
  this.clustering = clustering;
 }

 //-------------------------------------------------------------//
 // Verifica que el proceso este configurado			//
 //-------------------------------------------------------------//

 public boolean isOn() {
  return (this.pruning.isOn() || this.clustering.isOn());
 }

 //-------------------------------------------------------------//
 // Configura el proceso desde una directiva			//
 //-------------------------------------------------------------//

 public void set(String name, double[] param) {
  this.pruning.set(name,param);
  this.clustering.set(name,param);
 }

 //-------------------------------------------------------------//
 // Codigo de las directivas de configuracion			//
 //-------------------------------------------------------------//

 public String toCode() {
  String prefix = (code==PREPROCESSING? "xfsl_preprocessing":
                                        "xfsl_postprocessing");
  String src = "";
  String src_prune = pruning.toCode();
  String src_clust = clustering.toCode();
  if(src_prune.length() > 0) src += prefix+src_prune;
  if(src_clust.length() > 0) src += prefix+src_clust;
  return src;
 }

 //-------------------------------------------------------------//
 // Ejecucion del proceso de simplificacion			//
 //-------------------------------------------------------------//

 public String[] compute(Specification spec, double[][] pattern) {
  String prune = pruning.compute(spec,pattern);
  String[] clust = clustering.compute(spec);
  if(prune == null && clust.length == 0) return null;
  if(prune == null) return clust;
  String[] proc = new String[clust.length+1];
  proc[0] = prune;
  for(int i=0; i<clust.length; i++) proc[i+1] = clust[i];
  return proc;
 }
}
