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
//	  GRABACION DEL APRENDIZAJE EN UN FICHERO DE LOG	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import java.io.*;
import java.util.*;

public class XfslLog implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private File file;
 private boolean log[];
 private boolean classif;
 private FileOutputStream stream;
 private String eol = System.getProperty("line.separator", "\n");

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfslLog() {
  this.file = null;
  this.log =  new boolean[9];
  for(int i=0; i<log.length; i++) log[i] = true;
  this.classif = false;
  this.stream = null;
 }

 //-------------------------------------------------------------//
 // Constructor usado en la configuracion			//
 //-------------------------------------------------------------//

 public XfslLog(File file,Vector v) {
  this.file = file;
  this.log = new boolean[9];
  boolean base = (v.size() == 0);
  for(int i=0; i<log.length; i++) log[i] = base;
  for(int i=0; i<v.size(); i++) parse((String) v.elementAt(i));
  this.classif = false;
  this.stream = null;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Duplica el objeto						//
 //-------------------------------------------------------------//

 public Object clone() {
  XfslLog clone = new XfslLog();
  clone.setFile(this.file);
  clone.setSelection(this.log);
  clone.setClassif(this.classif);
  return clone;
 }

 //-------------------------------------------------------------//
 // Estudia si se ha seleccionado la opcion de log		//
 //-------------------------------------------------------------//

 public boolean isOn() {
  return (this.file != null);
 }

 //-------------------------------------------------------------//
 // Devuelve el fichero seleccionado para almacenar la historia	//
 //-------------------------------------------------------------//

 public File getFile() {
  return this.file;
 }

 //-------------------------------------------------------------//
 // Selecciona el fichero para almacenar la historia		//
 //-------------------------------------------------------------//

 public void setFile(File file) {
  if(file == null) this.file = null;
  else this.file = new File(file.getAbsolutePath());
 }

 //-------------------------------------------------------------//
 // Devuelve la seleccion de variables a almacenar		//
 //-------------------------------------------------------------//

 public boolean[] getSelection() {
  return this.log;
 }

 //-------------------------------------------------------------//
 // Establece la seleccion de variables a almacenar		//
 //-------------------------------------------------------------//

 public void setSelection(boolean[] sel) {
  for(int i=0; i<sel.length && i<log.length; i++) log[i] = sel[i];
 }

 //-------------------------------------------------------------//
 // Analiza si el almacenamiento corresponde a un clasificador	//
 //-------------------------------------------------------------//

 public boolean getClassif() {
  return this.classif;
 }

 //-------------------------------------------------------------//
 // Selecciona si es un almacenamiento de un clasificador	//
 //-------------------------------------------------------------//

 public void setClassif(boolean classif) {
  this.classif = classif;
 }

 //-------------------------------------------------------------//
 // Representacion que se muestra en la ventana			//
 //-------------------------------------------------------------//

 public String toString() {
  if(file == null) return "";
  String code = "";
  if(log[0]) code += ", iter";
  if(log[1]) code += ", trn_error";
  if(log[2] && !classif) code += ", trn_rmse";
  if(log[2] && classif) code += ", trn_rate";
  if(log[3] && !classif) code += ", trn_mxae";
  if(log[3] && classif) code += ", trn_count";
  if(log[4]) code += ", trn_var";
  if(log[5]) code += ", tst_error";
  if(log[6] && !classif) code += ", tst_rmse";
  if(log[6] && classif) code += ", tst_rate";
  if(log[7] && !classif) code += ", tst_mxae";
  if(log[7] && classif) code += ", tst_count";
  if(log[8]) code += ", tst_var";
  if(code.length()>0) code = code.substring(2);
  return file.getName()+" ("+code+")";
 }

 //-------------------------------------------------------------//
 // Representacion a almacenar en el fichero de configuracion	//
 //-------------------------------------------------------------//

 public String toCode() {
  if(file == null) return "";
  String code = "xfsl_log(\""+file.getAbsolutePath()+"\"";
  if(log[0]) code += ", iter";
  if(log[1]) code += ", trn_error";
  if(log[2] && !classif) code += ", trn_rmse";
  if(log[2] && classif) code += ", trn_rate";
  if(log[3] && !classif) code += ", trn_mxae";
  if(log[3] && classif) code += ", trn_count";
  if(log[4]) code += ", trn_var";
  if(log[5]) code += ", tst_error";
  if(log[6] && !classif) code += ", tst_rmse";
  if(log[6] && classif) code += ", tst_rate";
  if(log[7] && !classif) code += ", tst_mxae";
  if(log[7] && classif) code += ", tst_count";
  if(log[8]) code += ", tst_var";
  code += ")";
  return code;
 }

 //-------------------------------------------------------------//
 // Abre el fichero y escribe la cabecera			//
 //-------------------------------------------------------------//

 public void open() {
  if(file == null) return;
  try {
   stream = new FileOutputStream(file);
   String header = "# "+toString()+eol+eol;
   stream.write(header.getBytes());
  } catch(Exception ex) { System.out.println(""+ex); }
 }

 //-------------------------------------------------------------//
 // Cierra el fichero						//
 //-------------------------------------------------------------//

 public void close() {
  if(file == null) return;
  if(stream == null) return;
  try { stream.close(); stream = null; } catch(Exception ex) {}
 }

 //-------------------------------------------------------------//
 // Alacena el resultado de una iteracion del aprendizaje	//
 //-------------------------------------------------------------//

 public void write(XfslStatus status) {
  if(file == null) return;
  String line = "";
  if(log[0]) line += status.epoch+" ";
  if(log[1]) line += status.trn.error+" ";
  if(log[2]) line += status.trn.rmse+" ";
  if(log[3]) line += status.trn.mxae+" ";
  if(log[4]) line += status.trn.var+" ";
  if(log[5]) line += status.tst.error+" ";
  if(log[6]) line += status.tst.rmse+" ";
  if(log[7]) line += status.tst.mxae+" ";
  if(log[8]) line += status.tst.var+" ";
  line += eol;
  if(stream == null) open();
  try { stream.write(line.getBytes()); } catch(Exception ex) {}
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Selecciona una variable de estado a almacenar		//
 //-------------------------------------------------------------//

 private void parse(String var) {
  if(var.trim().equals("iter")) log[0] = true;
  if(var.trim().equals("trn_error")) log[1] = true;
  if(var.trim().equals("trn_rmse")) log[2] = true;
  if(var.trim().equals("trn_rate")) log[2] = true;
  if(var.trim().equals("trn_mxae")) log[3] = true;
  if(var.trim().equals("trn_count")) log[3] = true;
  if(var.trim().equals("trn_var")) log[4] = true;
  if(var.trim().equals("tst_error")) log[5] = true;
  if(var.trim().equals("tst_rmse")) log[6] = true;
  if(var.trim().equals("tst_rate")) log[6] = true;
  if(var.trim().equals("tst_mxae")) log[7] = true;
  if(var.trim().equals("tst_count")) log[7] = true;
  if(var.trim().equals("tst_var")) log[8] = true;
 }
}
