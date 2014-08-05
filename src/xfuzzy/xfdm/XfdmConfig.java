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
//	     CONFIGURACION DE LA IDENTIFICACION			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import java.io.*;
import xfuzzy.xfsl.XfslPattern;

public class XfdmConfig {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public File patternfile;
 public int numinputs;
 public int numoutputs;
 public XfdmInputStyle commonstyle;
 public XfdmInputStyle[] inputstyle;
 public XfdmSystemStyle systemstyle;
 public XfdmAlgorithm algorithm;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfdmConfig() {
  patternfile = null;
  numinputs = -1;
  numoutputs = -1;
  commonstyle = null;
  inputstyle = null;
  systemstyle = null;
  algorithm = null;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //  Metodos relacionados con la representacion en un fichero	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String src = "";
  if(patternfile != null)
    src += "xfdm_pattern(\""+patternfile.getAbsolutePath()+"\")"+eol;
  if(numinputs >= 0) src += "xfdm_inputs("+numinputs+")"+eol;
  if(numoutputs >= 0) src += "xfdm_outputs("+numoutputs+")"+eol;
  if(commonstyle != null) src += commonstyle.toCode()+eol;
  if(inputstyle != null) for(int i=0; i<inputstyle.length; i++) {
   if(inputstyle[i] != null) src += inputstyle[i].toCode()+eol;
  }
  if(systemstyle != null) src += systemstyle.toCode()+eol;
  if(algorithm != null) src += algorithm.toCode()+eol;
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

 //=============================================================//
 //		Metodos de verificacion del contenido		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Verifica que el estilo de variables de entrada es correcto	//
 //-------------------------------------------------------------//

 public boolean testInputStyle() {
  if(numinputs<=0) return false;
  if(commonstyle != null) return true;
  if(inputstyle == null) return false;
  if(inputstyle.length != numinputs) return false;
  return true;
 }

 //-------------------------------------------------------------//
 // Verifica que el estilo del sistema es correcto		//
 //-------------------------------------------------------------//

 public boolean testSystemStyle() {
  if(numoutputs<=0) return false;
  return (systemstyle != null);
 }

 //-------------------------------------------------------------//
 // Verifica que la configuracion puede ejecutarse		//
 //-------------------------------------------------------------//

 public boolean isReadyToRun() {
  return (patternfile != null && testInputStyle() && 
          testSystemStyle() && algorithm != null);
 }

 //=============================================================//
 //		Metodos de ayuda a los algoritmos		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Obtiene un objeto con la lista de patrones			//
 //-------------------------------------------------------------//

 public XfslPattern getPatterns() {
  if(patternfile == null || numinputs<=0 || numoutputs<=0) return null;
  XfslPattern pattern = null;
  try { pattern =  new XfslPattern(patternfile,numinputs,numoutputs); }
  catch(Exception ex) {}
  return pattern;
 }

}
