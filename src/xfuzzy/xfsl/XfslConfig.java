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
//	     CONFIGURACION DEL APRENDIZAJE SUPERVISADO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;
import java.io.*;

public class XfslConfig {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int NOT_SELECTED = -1;
 public static final int CANCEL = 0;
 public static final int UNSET = 1;
 public static final int SET = 2;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public File trainingfile;
 public File testfile;
 public File outputfile;
 public XfslLog logfile;
 public XfslAlgorithm algorithm;
 public XfslErrorFunction errorfunction;
 public XfspProcess preprocessing;
 public XfspProcess postprocessing;
 public XfslEndCondition endcondition;
 public boolean modified = false;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XfslSetting[] setting;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslConfig() {
  trainingfile = null;
  testfile = null;
  outputfile = null;
  logfile = new XfslLog();
  algorithm = null;
  try { errorfunction = new XfslErrorFunction(0); } catch(XflException e) {}
  preprocessing = new XfspProcess(XfspProcess.PREPROCESSING);
  postprocessing = new XfspProcess(XfspProcess.POSTPROCESSING);
  endcondition = new XfslEndCondition();
  setting = new XfslSetting[0];
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String src = "";
  if(trainingfile != null)
    src += "xfsl_training(\""+trainingfile.getAbsolutePath()+"\")"+eol;
  if(testfile != null)
    src += "xfsl_test(\""+testfile.getAbsolutePath()+"\")"+eol;
  if(logfile.isOn()) src += logfile.toCode()+eol;
  if(outputfile != null)
    src += "xfsl_output(\""+outputfile.getAbsolutePath()+"\")"+eol;
  if(algorithm != null) src += algorithm.toCode();
  src += errorfunction.toCode();
  src += preprocessing.toCode();
  src += postprocessing.toCode();
  src += endcondition.toCode();
  for(int i=0; i<setting.length; i++) src += setting[i].toCode() + eol;
  return src;
 }

 //-------------------------------------------------------------//
 // Almacenar la configuracion en un fichero			//
 //-------------------------------------------------------------//

 public boolean save(File file) {
  String code = toCode();
  byte buf[] = code.getBytes();

  try {
   OutputStream stream = new FileOutputStream(file);
   stream.write(buf);
   stream.close();
  }
  catch (IOException e) { return false; }
  return true;
 }

 //-------------------------------------------------------------//
 // Verifica que la configuracion puede ejecutarse		//
 //-------------------------------------------------------------//

 public boolean isReadyToRun() {
  return (algorithm != null && trainingfile != null);
 }

 //-------------------------------------------------------------//
 // Obtiene el conjunto de selecciones de parametros		//
 //-------------------------------------------------------------//

 public XfslSetting[] getSettings() {
  return this.setting;
 }

 //-------------------------------------------------------------//
 // Asigna el conjunto de selecciones de parametros		//
 //-------------------------------------------------------------//

 public void setSettings(XfslSetting[] stt) {
  this.setting = stt;
 }

 //-------------------------------------------------------------//
 // Annade una seleccion de parametros				//
 //-------------------------------------------------------------//

 public void addSetting(String def, boolean enable) {
  XfslSetting[] aux = new XfslSetting[setting.length+1];
  System.arraycopy(setting,0,aux,0,setting.length);
  aux[setting.length] = new XfslSetting(def, enable);
  setting = aux;
 }

 //-------------------------------------------------------------//
 // Verifica si existe alguna seleccion                         //
 //-------------------------------------------------------------//

 public boolean areSettingsOn() {
  return (setting.length>0);
 }

 //-------------------------------------------------------------//
 // Asigna las selecciones a los tipos de una especificacion    //
 //-------------------------------------------------------------//

 public void setParameterSettings(Type[] tp) {
  for(int i=0; i<setting.length; i++) setting[i].set(tp);
 }
}
