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
//	GENERADOR DEL FICHERO "TP_spec_type.java"		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import xfuzzy.lang.*;
import java.io.*;

public class XfjSpecType {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private File dir;
 private String pkgname;
 private String classname;
 private String specname;
 private Type type;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS CONSTANTES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final String create(File dir, String pkgname, Type type, 
                                   String spec) {
  XfjSpecType creator = new XfjSpecType(dir,pkgname,type,spec);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjSpecType(File dir, String pkgname, Type type, String spec) {
  this.dir = dir;
  this.pkgname = pkgname;
  this.type = type;
  this.classname = "TP_"+spec+"_"+type.getName();
  this.specname = spec;
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
 // Genera el fichero "TP_spec_type.java"			//
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
   "// Description: Type \""+type.getName()+"\"                      //",
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
 // Genera el codigo de la clase "TP_spec_type"			//
 //-------------------------------------------------------------//

 private String getSource() {
  Type parent = type.getParent();
  Universe u = type.getUniverse();
  Family fam[] = type.getFamilies();
  ParamMemFunc mf[] = type.getParamMembershipFunctions();
  FamiliarMemFunc fmf[] = type.getFamiliarMembershipFunctions();

  String code = "";
  code += "public class "+classname;
  if(parent != null) code += " extends TP_"+specname+"_"+parent.getName();
  code += " {"+eol;

  if(parent == null) {
   code += " private double min = "+u.min()+";"+eol;
   code += " private double max = "+u.max()+";"+eol;
   code += " private double step = "+u.step()+";"+eol;
  }

  for(int i=0; i<fam.length; i++) {
   Parameter single[] = fam[i].getSingleParameters();
   code +=" double _pfs_"+fam[i]+"[] = { ";
   for(int j=0; j<single.length; j++) code += (j==0? "":",")+single[j].value;
   code += " };"+eol;

   Parameter list[] = fam[i].getParamList();
   int listlength = (list == null? 0 : list.length);
   code +=" double _pfl_"+fam[i]+"[] = { ";
   for(int j=0; j<listlength; j++) code += (j==0? "":",")+list[j].value;
   code += " };"+eol;
  }

  for(int i=0; i<mf.length; i++) {
   Parameter single[] = mf[i].getSingleParameters();
   code +=" double _ps_"+mf[i].getLabel()+"[] = { ";
   for(int j=0; j<single.length; j++) code += (j==0? "":",")+single[j].value;
   code += " };"+eol;

   Parameter list[] = mf[i].getParamList();
   int listlength = (list == null? 0 : list.length);
   code +=" double _pl_"+mf[i].getLabel()+"[] = { ";
   for(int j=0; j<listlength; j++) code += (j==0? "":",")+list[j].value;
   code += " };"+eol;
  }

  for(int i=0; i<fam.length; i++) {
   String pkgname = fam[i].getPackageName();
   String famname = fam[i].getFunctionName();
   code += " FAM_"+pkgname+"_"+famname+" "+fam[i];
   code += " = new FAM_"+pkgname+"_"+famname;
   code += "(min,max,step,_pfs_"+fam[i]+",_pfl_"+fam[i]+");"+eol;
  }

  for(int i=0; i<mf.length; i++) {
   code += " MF_"+mf[i].getPackageName()+"_"+mf[i].getFunctionName();
   code += " "+mf[i].getLabel();
   code += " = new MF_"+mf[i].getPackageName()+"_"+mf[i].getFunctionName();
   code += "(min,max,step,_ps_"+mf[i].getLabel();
   code += ",_pl_"+mf[i].getLabel()+");"+eol;
  }

  for(int i=0; i<fmf.length; i++) {
   code += " FamiliarMembershipFunction "+fmf[i].getLabel();
   code += " = new FamiliarMembershipFunction";
   code += "("+fmf[i].getFamily()+","+fmf[i].getIndex()+");"+eol;
  }

  code += "}"+eol+eol;

  return code;
 }

}
