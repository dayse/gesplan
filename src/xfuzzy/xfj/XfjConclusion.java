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
//	    GENERADOR DEL FICHERO "Conclusion.java"		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import java.io.*;

public class XfjConclusion {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private File dir;
 private String pkgname;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS CONSTANTES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final String create(File dir, String pkgname) {
  XfjConclusion creator = new XfjConclusion(dir,pkgname);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjConclusion(File dir, String pkgname) {
  this.dir = dir;
  this.pkgname = pkgname;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el nombre del fichero creado			//
 //-------------------------------------------------------------//

 private String getMessage() {
  File file = new File(dir,"Conclusion.java");
  return file.getAbsolutePath();
 }

 //-------------------------------------------------------------//
 // Genera el fichero "Conclusion.java"				//
 //-------------------------------------------------------------//

 public void createFile() {
  File file = new File(dir,"Conclusion.java");

  String heading[] = getHeading();
  String source[] = getSource();

  String code = "";
  for(int i=0; i<heading.length; i++) code += heading[i]+eol;
  code += getPackage()+eol+eol;
  for(int i=0; i<source.length; i++) code += source[i]+eol;

  byte[] buf = code.getBytes();
  try {
   OutputStream stream = new FileOutputStream(file);
   stream.write(buf);
   stream.close();
  }
  catch (IOException e) {}
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Genera el codigo de cabecera				//
 //-------------------------------------------------------------//

 private String[] getHeading() {
  String source[] = {
   "//++++++++++++++++++++++++++++++++++++++++++++++++++++++//",
   "//                                                      //",
   "// Class:  Conclusion                                   //",
   "//                                                      //",
   "// Author: Automatically generated by Xfuzzy            //",
   "//                                                      //",
   "// Description: Conclusion of a fuzzy rule              //",
   "//                                                      //",
   "//++++++++++++++++++++++++++++++++++++++++++++++++++++++//",
   "" };

  return source;
 }

 //-------------------------------------------------------------//
 // Genera el codigo del paquete				//
 //-------------------------------------------------------------//

 private String getPackage() {
  if(pkgname != null && pkgname.length()>0) return "package "+this.pkgname+";";
  return "";
 }

 //-------------------------------------------------------------//
 // Genera el codigo de la clase "Conclusion"			//
 //-------------------------------------------------------------//

 private String[] getSource() {
  String source[] = {
"public class Conclusion {",
" private double degree;",
" private InputMembershipFunction mf;",
" private OperatorSet op;",
"",
" public Conclusion(double degree, InputMembershipFunction mf, OperatorSet op) {",
"  this.op = op;",
"  this.degree = degree;",
"  this.mf = mf;",
" }",
"",
" public double degree() {",
"  return degree;",
" }",
"",
" public double compute(double x) {",
"  return op.imp(degree,mf.isEqual(x));",
" }",
"",
" public double center() {",
"  return mf.center();",
" }",
"",
" public double basis() {",
"  return mf.basis();",
" }",
"",
" public double param(int i) {",
"  return mf.param(i);",
" }",
"",
" public double min() {",
"  return mf.min;",
" }",
"",
" public double max() {",
"  return mf.max;",
" }",
"",
" public double step() {",
"  return mf.step;",
" }",
"",
" public boolean isSingleton() { ",
"  return mf.getClass().getName().endsWith(\"MF_xfl_singleton\");",
" }",
"}" };

  return source;
 }
}
