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
//			COMPILADOR A JAVA			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import xfuzzy.lang.*;
import java.io.*;
import java.util.Vector;

public class Xfj {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private Specification spec;
 private File dir;
 private String pkgname;
 private String msg;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public Xfj(Specification spec, File dir, String pkgname) {
  this.spec = spec;
  this.pkgname = pkgname;
  if(dir==null) this.dir = spec.getFile().getParentFile();
  else this.dir = dir;
  create();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Ejecucion externa						//
 //-------------------------------------------------------------//

 public static void main(String args[]) {
  String pkgname = "";
  String filename = "";
  if(args.length == 3 && args[0].equals("-p"))
   { pkgname = args[1]; filename = args[2]; }
  else if(args.length == 1) filename = args[0];
  else {
   System.out.println("Usage: xfj [-p packagename] xflfile");
   System.exit(-1);
  }
  XflParser xflparser = new XflParser();
  Specification spec = xflparser.parse(filename);
  if(spec == null) { System.out.println(xflparser.resume()); System.exit(-1); }
  Xfj compiler = new Xfj(spec,null,pkgname);
  System.out.println(compiler.getMessage());
  System.exit(0);
 }

 //-------------------------------------------------------------//
 // Descripcion del resultado de la compilacion			//
 //-------------------------------------------------------------//

 public String getMessage() {
  return msg;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Genera los archivos de la descripcion Java del sistema	//
 //-------------------------------------------------------------//

 private void create() {
  String msg_fie = XfjFuzzyInferenceEngine.create(dir,pkgname);
  String msg_mf = XfjMembershipFunction.create(dir,pkgname);
  String msg_fs = XfjFuzzySingleton.create(dir,pkgname);
  String msg_fam = XfjFamily.create(dir,pkgname);
  String msg_imf = XfjInputMemFunc.create(dir,pkgname);
  String msg_omf = XfjOutputMemFunc.create(dir,pkgname);
  String msg_fmf = XfjFamiliarMemFunc.create(dir,pkgname);
  String msg_os = XfjAbstractOperatorSet.create(dir,pkgname);
  String msg_cc = XfjConclusion.create(dir,pkgname);
  String msg_pfm[] = createFamilyClasses();
  String msg_pmf[] = createMFClasses();
  String msg_op[] = createOPClasses();
  String msg_tp[] = createTPClasses();
  String msg_spec = XfjSpecClass.create(dir,pkgname,spec);

  msg = "";
  msg += "Java code generation describing the fuzzy system "+spec.getName();
  msg += " has been successfully finished."+eol;
  msg += "Java description is included in files: "+eol;
  msg += "        \""+msg_mf+"\","+eol;
  msg += "        \""+msg_fie+"\","+eol;
  msg += "        \""+msg_fs+"\","+eol;
  msg += "        \""+msg_fam+"\","+eol;
  msg += "        \""+msg_imf+"\","+eol;
  msg += "        \""+msg_omf+"\","+eol;
  msg += "        \""+msg_fmf+"\","+eol;
  msg += "        \""+msg_os+"\","+eol;
  msg += "        \""+msg_cc+"\","+eol;
  for(int i=0; i<msg_pfm.length; i++) msg += "        \""+msg_pfm[i]+"\","+eol;
  for(int i=0; i<msg_pmf.length; i++) msg += "        \""+msg_pmf[i]+"\","+eol;
  for(int i=0; i<msg_op.length; i++) msg += "        \""+msg_op[i]+"\","+eol;
  for(int i=0; i<msg_op.length; i++) msg += "        \""+msg_tp[i]+"\","+eol;
  msg += "        and \""+msg_spec+"\".";
 }

 //-------------------------------------------------------------//
 // Genera el codigo de las familias de funciones incluidas	//
 //-------------------------------------------------------------//

 private String[] createFamilyClasses() {
  Vector vector = searchFamilyClasses();
  String msg[] = new String[vector.size()];
  for(int i=0; i<msg.length; i++) {
   Family fam = (Family) vector.elementAt(i);
   msg[i] = XfjParamFamily.create(dir,pkgname,fam);
  }
  return msg;
 }

 //-------------------------------------------------------------//
 // Genera el codigo de las funciones de pertenencia incluidas	//
 //-------------------------------------------------------------//

 private String[] createMFClasses() {
  Vector vector = searchMFClasses();
  String msg[] = new String[vector.size()];
  for(int i=0; i<msg.length; i++) {
   ParamMemFunc mf = (ParamMemFunc) vector.elementAt(i);
   msg[i] = XfjParamMemFunc.create(dir,pkgname,mf);
  }
  return msg;
 }

 //-------------------------------------------------------------//
 // Genera el codigo de los conjuntos de operadores incluidos	//
 //-------------------------------------------------------------//

 private String[] createOPClasses() {
  String name = spec.getName();
  Operatorset[] opset = spec.getOperatorsets();
  String msg[] = new String[opset.length];

  for(int i=0;i<opset.length; i++) {
   msg[i] = XfjSpecOperatorSet.create(dir,pkgname,opset[i],name);
  }
  return msg;
 }

 //-------------------------------------------------------------//
 // Genera el codigo de los tipos de variables incluidos	//
 //-------------------------------------------------------------//

 private String[] createTPClasses() {
  String name = spec.getName();
  Type[] type = spec.getTypes();
  String msg[] = new String[type.length];

  for(int i=0;i<type.length; i++) {
   msg[i] = XfjSpecType.create(dir,pkgname,type[i],name);
  }
  return msg;
 }

 //-------------------------------------------------------------//
 // Genera la lista de funciones de pertenencia incluidas	//
 //-------------------------------------------------------------//

 private Vector searchMFClasses() {
  Vector nmv = new Vector();
  Vector mfv = new Vector();
  Type[] type = spec.getTypes();
  for(int i=0; i<type.length; i++) {
   ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
   for(int j=0; j<mf.length; j++) {
    String classname = "MF_"+mf[j].getPackageName()+"_"+mf[j].getFunctionName();
    boolean included = false;
    for(int k=0; k<nmv.size(); k++)
     if(classname.equals( (String) nmv.elementAt(k) )) included = true;
    if(!included) {
     mfv.addElement(mf[j]);
     nmv.addElement(classname);
    }
   }
  }
  return mfv;
 }

 //-------------------------------------------------------------//
 // Genera la lista de familias de funciones incluidas		//
 //-------------------------------------------------------------//

 private Vector searchFamilyClasses() {
  Vector nmv = new Vector();
  Vector famv = new Vector();
  Type[] type = spec.getTypes();
  for(int i=0; i<type.length; i++) {
   Family fam[] = type[i].getFamilies();
   for(int j=0; j<fam.length; j++) {
    String pkgname = fam[j].getPackageName();
    String classname ="FAM_"+pkgname+"_"+fam[j].getFunctionName();
    boolean included = false;
    for(int k=0; k<nmv.size(); k++)
     if(classname.equals( (String) nmv.elementAt(k) )) included = true;
    if(!included) {
     famv.addElement(fam[j]);
     nmv.addElement(classname);
    }
   }
  }
  return famv;
 }
}
