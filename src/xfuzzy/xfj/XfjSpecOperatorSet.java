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
//	GENERADOR DEL FICHERO "OP_spec_opset.java"		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import xfuzzy.lang.*;
import java.io.*;

public class XfjSpecOperatorSet {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private File dir;
 private String pkgname;
 private String classname;
 private Operatorset opset;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS CONSTANTES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final String create(File dir, String pkgname, Operatorset opset, 
                                   String spec) {
  XfjSpecOperatorSet creator = new XfjSpecOperatorSet(dir,pkgname,opset,spec);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjSpecOperatorSet(File dir, String pkgname, Operatorset opset,
                           String spec) {
  this.dir = dir;
  this.pkgname = pkgname;
  this.opset = opset;
  this.classname = "OP_"+spec+"_"+opset.getName();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el nombre del fichero creado			//
 //-------------------------------------------------------------//

 private String getMessage() {
  File file = new File(dir,classname+".java");
  return file.getAbsolutePath();
 }

 //-------------------------------------------------------------//
 // Genera el fichero "OP_spec_opset.java"			//
 //-------------------------------------------------------------//

 public void createFile() {
  File file = new File(dir,classname+".java");

  String heading[] = getHeading();
  String source = getSource();

  String code = "";
  for(int i=0; i<heading.length; i++) code += heading[i]+eol;
  code += getPackage()+eol+eol;
  code += source+eol;

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
   "// Class:  "+classname+"                           //",
   "//                                                      //",
   "// Author: Automatically generated by Xfuzzy            //",
   "//                                                      //",
   "// Description: Operator set \""+opset.getName()+"\"             //",
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
 // Genera el codigo de la clase "OP_spec_opset"		//
 //-------------------------------------------------------------//

 private String getSource() {
  String code = "";
  code += "public class "+classname+" extends OperatorSet {"+eol;
  code += createBinaryCode(opset.and, "and");
  code += createBinaryCode(opset.or, "or");
  code += createBinaryCode(opset.also, "also");
  code += createBinaryCode(opset.imp, "imp");
  code += createUnaryCode(opset.not, "not");
  code += createUnaryCode(opset.very, "very");
  code += createUnaryCode(opset.moreorless, "moreorless");
  code += createUnaryCode(opset.slightly, "slightly");
  code += createDefuzCode(opset.defuz, "defuz");
  code += "}"+eol+eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de un operador binario				//
 //-------------------------------------------------------------//

 private String createBinaryCode(Binary op, String name) {
  String code = " public double "+name+"(double a, double b) {"+eol;

  Parameter singleparam[] = op.getSingleParameters();
  for(int i=0; i<singleparam.length; i++) {
   code += "  double "+singleparam[i].getName();
   code += " = "+singleparam[i].value+";"+eol;
  }

  if(op.hasParamList()) {
   Parameter paramlist[] = op.getParamList();
   int listlength = (paramlist == null? 0 : paramlist.length);
   code += "  double "+op.getParamListName()+"[] = {";
   for(int i=0; i<listlength; i++) 
    code += (i>0? ",":"")+paramlist[i].value;
   code += "};"+eol;
  }

  code += op.getJavaCode();
  code += " }"+eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de un operador unario				//
 //-------------------------------------------------------------//

 private String createUnaryCode(Unary op, String name) {
  String code = " public double "+name+"(double a) {"+eol;

  Parameter singleparam[] = op.getSingleParameters();
  for(int i=0; i<singleparam.length; i++) {
   code += "  double "+singleparam[i].getName();
   code += " = "+singleparam[i].value+";"+eol;
  }

  if(op.hasParamList()) {
   Parameter paramlist[] = op.getParamList();
   int listlength = (paramlist == null? 0 : paramlist.length);
   code += "  double "+op.getParamListName()+"[] = {";
   for(int i=0; i<listlength; i++) 
    code += (i>0? ",":"")+paramlist[i].value;
   code += "};"+eol;
  }

  code += op.getJavaCode();
  code += " }"+eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de un metodo de concrecion			//
 //-------------------------------------------------------------//

 private String createDefuzCode(DefuzMethod op, String name) {
  String defuzcode = op.getJavaCode();
  String code = " public double "+name+"(OutputMembershipFunction mf) {"+eol;

  if(defuzcode.indexOf("min")!=-1) code += "  double min = mf.min();"+eol;
  if(defuzcode.indexOf("max")!=-1) code += "  double max = mf.max();"+eol;
  if(defuzcode.indexOf("step")!=-1) code += "  double step = mf.step();"+eol;

  Parameter singleparam[] = op.getSingleParameters();
  for(int i=0; i<singleparam.length; i++) {
   code += "  double "+singleparam[i].getName();
   code += " = "+singleparam[i].value+";"+eol;
  }

  if(op.hasParamList()) {
   Parameter paramlist[] = op.getParamList();
   int listlength = (paramlist == null? 0 : paramlist.length);
   code += "  double "+op.getParamListName()+"[] = {";
   for(int i=0; i<listlength; i++) 
    code += (i>0? ",":"")+paramlist[i].value;
   code += "};"+eol;
  }

  code += defuzcode;
  code += " }"+eol;
  return code;
 }

}
