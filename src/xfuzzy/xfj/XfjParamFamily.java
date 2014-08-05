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
//	GENERADOR DEL FICHERO "FAM_pkg_family.java"		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import xfuzzy.lang.*;
import java.io.*;

public class XfjParamFamily {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private File dir;
 private String pkgname;
 private String classname;
 private Family fam;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS CONSTANTES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final String create(File dir, String pkgname, Family fam) {
  XfjParamFamily creator = new XfjParamFamily(dir,pkgname,fam);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjParamFamily(File dir, String pkgname, Family fam) {
  this.dir = dir;
  this.pkgname = pkgname;
  this.fam = fam;
  this.classname = "FAM_"+fam.getPackageName()+"_"+fam.getFunctionName();
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
 // Genera el fichero "FAM_pkg_family.java"			//
 //-------------------------------------------------------------//

 public void createFile() {
  File file = new File(dir,classname+".java");

  String heading[] = getHeading();
  String source = getSource();

  String code = "";
  for(int i=0; i<heading.length; i++) code += heading[i]+eol;
  code += getPackage()+eol+eol;
  code += source;

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
   "// Class:  "+classname+"                      //",
   "//                                                      //",
   "// Author: Automatically generated by Xfuzzy            //",
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
 // Genera el codigo de la clase "FAM_pkg_family"		//
 //-------------------------------------------------------------//

 private String getSource() {
  Parameter single[] = fam.getSingleParameters();
  String listname = fam.getParamListName();
  boolean haslist = (listname != null && listname.length() > 0);

  String code = "";
  code += "public class "+classname+" extends MembershipFunctionFamily {"+eol;
  for(int i=0; i<single.length; i++) {
   code += " double "+single[i].getName()+";"+eol;
  }
  if(haslist) code += " double "+listname+"[];"+eol;

  code += eol;
  code += " public "+classname+"(double min, double max, double step";
  code += ", double single[], double list[]) {"+eol;
  code += "  super.min = min;"+eol;
  code += "  super.max = max;"+eol;
  code += "  super.step = step;"+eol;
  for(int i=0; i<single.length; i++) {
   code += "  this."+single[i].getName()+" = single["+i+"];"+eol;
  }
  if(haslist) code += "  this."+listname+" = list;"+eol;
  code += " }"+eol;

  code += " public double param(int _i) {"+eol;
  if(single.length == 0 && haslist) {
   code += "  return "+listname+"[_i];"+eol;
  } else if(!haslist) {
   code += "  switch(_i) {"+eol;
   for(int i=0; i<single.length; i++) {
    code += "   case "+i+": return "+single[i].getName()+";"+eol;
   }
   code += "   default: return 0;"+eol;
   code += "  }"+eol;
  } else {
   code += "  if(_i>="+single.length+")";
   code += " return "+listname+"[_i-"+single.length+"];"+eol;
   code += "  switch(_i) {"+eol;
   for(int i=0; i<single.length; i++) {
    code += "   case "+i+": return "+single[i].getName()+";"+eol;
   }
   code += "   default: return 0;"+eol;
   code += "  }"+eol;
  }
  code += " }"+eol;
  code += eol;

  code += " public double isEqual(int i, double x) {"+eol;
  code += fam.getEqualJavaCode();
  code += " }"+eol;

  String greq = fam.getGreqJavaCode();
  if(greq != null && greq.length()>0) {
   code += " public double isGreaterOrEqual(int i, double x) {"+eol;
   code += greq;
   code += " }"+eol;
  }

  String smeq = fam.getSmeqJavaCode();
  if(smeq != null && smeq.length()>0) {
   code += " public double isSmallerOrEqual(int i, double x) {"+ eol;
   code += smeq;
   code += " }"+eol;
  }

  String center = fam.getCenterJavaCode();
  if(center != null && center.length()>0) {
   code += " public double center(int i) {"+eol;
   code += center;
   code += " }"+eol;
  }

  String basis = fam.getBasisJavaCode();
  if(basis != null && basis.length()>0) {
   code += " public double basis(int i) {"+eol;
   code += basis;
   code += " }"+eol;
  }

  code += "}"+eol+eol;

  return code;
 }
}
