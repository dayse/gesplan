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
//	GENERADOR DEL FICHERO "OutputMembershipFunction.java"	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import java.io.*;

public class XfjOutputMemFunc {

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
  XfjOutputMemFunc creator = new XfjOutputMemFunc(dir,pkgname);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjOutputMemFunc(File dir, String pkgname) {
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
  File file = new File(dir,"OutputMembershipFunction.java");
  return file.getAbsolutePath();
 }

 //-------------------------------------------------------------//
 // Genera el fichero "OutputMembershipFunction.java"		//
 //-------------------------------------------------------------//

 public void createFile() {
  File file = new File(dir,"OutputMembershipFunction.java");

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
   "// Class:  OutputMembershipFunction                     //",
   "//                                                      //",
   "// Author: Automatically generated by Xfuzzy            //",
   "//                                                      //",
   "// Description: Membership function of an output        //",
   "//              variable                                //",
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
 // Genera el codigo de la clase "OutputMembershipFunction"	//
 //-------------------------------------------------------------//

 private String[] getSource() {
  String source[] = {
"public class OutputMembershipFunction implements MembershipFunction {",
" public Conclusion[] conc;",
" public double[] input;",
" private OperatorSet op;",
"",
" public OutputMembershipFunction() {",
"  this.conc = new Conclusion[0];",
" }",
"",
" public void set(int size, OperatorSet op, double[] input) {",
"  this.input = input;",
"  this.op = op;",
"  this.conc = new Conclusion[size];",
" }",
"",
" public void set(int pos, double dg, InputMembershipFunction imf) {",
"  this.conc[pos] = new Conclusion(dg,imf,op);",
" }",
"",
" public double compute(double x) {",
"  double dom = conc[0].compute(x);",
"  for(int i=1; i<conc.length; i++) dom = op.also(dom,conc[i].compute(x));",
"  return dom;",
" }",
"",
" public double defuzzify() {",
"  return op.defuz(this);",
" }",
"",
" public double min() {",
"  return conc[0].min();",
" }",
"",
" public double max() {",
"  return conc[0].max();",
" }",
"",
" public double step() {",
"  return conc[0].step();",
" }",
"",
" public boolean isDiscrete() {",
"  for(int i=0; i<conc.length; i++) if(!conc[i].isSingleton()) return false;",
"  return true;",
" }",
" ",
" public double[][] getDiscreteValues() {",
"  double[][] value = new double[conc.length][2];",
"  for(int i=0; i<conc.length; i++) {",
"   value[i][0] = conc[i].param(0);",
"   value[i][1] = conc[i].degree();",
"  }",
"  return value;",
" }",
"",
"}" };

  return source;
 }
}
