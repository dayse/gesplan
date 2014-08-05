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
//		GENERADOR DEL FICHERO "spec.java"		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import xfuzzy.lang.*;
import java.io.*;

public class XfjSpecClass {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private File dir;
 private String pkgname;
 private Specification spec;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS CONSTANTES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final String create(File dir,String pkgname,Specification spec) {
  XfjSpecClass creator = new XfjSpecClass(dir,pkgname,spec);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjSpecClass(File dir, String pkgname, Specification spec) {
  this.dir = dir;
  this.pkgname = pkgname;
  this.spec = spec;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el nombre del fichero creado			//
 //-------------------------------------------------------------//

 private String getMessage() {
  File file = new File(dir,spec+".java");
  return file.getAbsolutePath();
 }

 //-------------------------------------------------------------//
 // Genera el fichero "spec.java"				//
 //-------------------------------------------------------------//

 public void createFile() {
  File file = new File(dir,spec+".java");

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
   "// Class:  "+spec+"                           //",
   "//                                                      //",
   "// Author: Automatically generated by Xfuzzy            //",
   "//                                                      //",
   "// Description: Fuzzy inference engine \""+spec+"\"       //",
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
 // Genera el codigo de la clase "spec"				//
 //-------------------------------------------------------------//

 private String getSource() {
  String name = spec.getName();
  CrispBlock[] crisp = spec.getCrispBlockSet().getBlocks();
  Rulebase[] base = spec.getRulebases();

  String code = "";
  code += "public class "+name+" implements FuzzyInferenceEngine {"+eol+eol;
  for(int i=0; i<base.length; i++) code += createRulebaseCode(base[i]);
  for(int i=0; i<crisp.length; i++) code += createCrispBlockCode(crisp[i]);
  code += createSystemModuleCode();
  code += "}"+eol;
  return code;
 }

 //=============================================================//
 //	    Metodos que describen los bloques no difusos	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Descripcion de un bloque no difuso				//
 //-------------------------------------------------------------//

 private String createCrispBlockCode(CrispBlock crisp) {
  String name = crisp.getName();
  int inputs = crisp.inputs();

  String code = "";
  code += " //+++++++++++++++++++++++++++++++++++++++++++++++++++++//"+eol;
  code += " //  Crisp Block CB_"+name+"  //"+eol;
  code += " //+++++++++++++++++++++++++++++++++++++++++++++++++++++//"+eol;
  code += eol;
  code += " private double CB_"+name+"(";
  for(int i=0; i<inputs; i++) code += (i==0? "":", ")+"MembershipFunction _i"+i;
  code += ") {"+eol;

  Parameter singleparam[] = crisp.getSingleParameters();
  for(int i=0; i<singleparam.length; i++) {
   code += "  double "+singleparam[i].getName();
   code += " = "+singleparam[i].value+";"+eol;
  }

  if(crisp.hasParamList()) {
   Parameter paramlist[] = crisp.getParamList();
   int listlength = (paramlist == null? 0 : paramlist.length);
   code += "  double "+crisp.getParamListName()+"[] = {";
   for(int i=0; i<listlength; i++)
    code += (i>0? ",":"")+paramlist[i].value;
   code += "};"+eol;
  }

  code += "  double x[] = new double["+inputs+"];"+eol;
  for(int i=0; i<inputs; i++) {
   code += "  if(_i"+i+" instanceof FuzzySingleton) {"+eol;
   code += "   x["+i+"] = ((FuzzySingleton) _i"+i+").getValue();"+eol;
   code += "  } else if(_i"+i+" instanceof OutputMembershipFunction) {"+eol;
   code +="   x["+i+"] = ((OutputMembershipFunction) _i"+i+").defuzzify();"+eol;
   code += "  } else { x["+i+"] = 0;"+eol;
   code += "  }"+eol;
   code += eol;
  }

  code += crisp.getJavaCode();
  code += " }"+eol+eol;
  return code;
 }

 //=============================================================//
 //	    Metodos que describen las bases de reglas		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Descripcion de una base de reglas				//
 //-------------------------------------------------------------//

 private String createRulebaseCode(Rulebase base) {
  String specname = spec.getName();
  String name = base.getName();
  Operatorset opset = base.getOperatorset();
  Variable inputvar[] = base.getInputs();
  Variable outputvar[] = base.getOutputs();
  Rule rule[] = base.getRules();

  String code = "";
  code += " //+++++++++++++++++++++++++++++++++++++++++++++++++++++//"+eol;
  code += " //  Rulebase RL_"+name+"  //"+eol;
  code += " //+++++++++++++++++++++++++++++++++++++++++++++++++++++//"+eol+eol;
  code += " private MembershipFunction[] RL_"+name+"(";
  for(int i=0; i<inputvar.length; i++)
     code += (i==0? "":", ")+"MembershipFunction "+inputvar[i];
  code += ") {"+eol;
  code += "  double _rl;"+eol;
  code += "  double _input[] = new double["+inputvar.length+"];"+eol;
  for(int i=0; i<inputvar.length; i++) {
     code += "  if("+inputvar[i]+" instanceof FuzzySingleton)"+eol;
     code += "   _input["+i+"] = ((FuzzySingleton) "+inputvar[i];
     code += ").getValue();"+eol;
    }
  code += "  OP_"+specname+"_"+opset.getName()+" _op";
  code += " = new OP_"+specname+"_"+opset.getName()+"();"+eol;
  for(int i=0; i<outputvar.length; i++) {
     code += "  OutputMembershipFunction "+outputvar[i];
     code += " = new OutputMembershipFunction();"+eol;
     code += "  "+outputvar[i]+".set(";
     code += base.computeOutputSize(i)+",_op,_input);"+eol;
    }
  for(int i=0; i<inputvar.length; i++) {
     code += "  TP_"+specname+"_"+inputvar[i].getType()+" _t_"+inputvar[i];
     code += " = new TP_"+specname+"_"+inputvar[i].getType()+"();"+eol;
    }
  for(int i=0; i<outputvar.length; i++) {
     code += "  TP_"+specname+"_"+outputvar[i].getType()+" _t_"+outputvar[i];
     code += " = new TP_"+specname+"_"+outputvar[i].getType()+"();"+eol;
    }
  for(int i=0; i<outputvar.length; i++)
     code += "  int _i_"+outputvar[i]+"=0;"+eol;

  for(int i=0; i<rule.length; i++) code += createRuleCode(rule[i]);

  code += "  MembershipFunction[] _output";
  code += " = new MembershipFunction["+outputvar.length+"];"+eol;
  if(opset.defuz.isDefault())
    for(int i=0; i<outputvar.length; i++)
     code += "  _output["+i+"] = "+outputvar[i]+";"+eol;
  else for(int i=0; i<outputvar.length; i++) {
     code += "  _output["+i+"]";
     code += " = new FuzzySingleton("+outputvar[i]+".defuzzify());"+eol;
    }
  code += "  return _output;"+eol;
  code += " }"+eol+eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de una regla					//
 //-------------------------------------------------------------//

 private String createRuleCode(Rule rule) {
  double degree = rule.getDegree();
  Relation premise = rule.getPremise();
  Conclusion conc[] = rule.getConclusions();

  String code = "  _rl = ";
  if(degree != 1.0) code += degree+"*";
  code += createRelationCode(premise)+";"+eol;
  for(int j=0; j<conc.length; j++) code += createConclusionCode(conc[j])+eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de la conclusion de una regla			//
 //-------------------------------------------------------------//

 private String createConclusionCode(Conclusion conc) {
  Variable var = conc.getVariable();
  LinguisticLabel mf = conc.getMembershipFunction();

  return "  "+var+".set(_i_"+var+",_rl, _t_"+var+"."+mf+"); _i_"+var+"++;";
 }

 //-------------------------------------------------------------//
 // Descripcion de una proposicion				//
 //-------------------------------------------------------------//

 private String createRelationCode(Relation rel) {
  Variable var = rel.getVariable();
  LinguisticLabel mf = rel.getMembershipFunction();
  Relation l = rel.getLeftRelation();
  Relation r = rel.getRightRelation();

  switch(rel.getKind()) {
   case Relation.AND:
    return "_op.and("+createRelationCode(l)+","+createRelationCode(r)+")";
   case Relation.OR:
    return "_op.or("+createRelationCode(l)+","+createRelationCode(r)+")";
   case Relation.IS:
    return "_t_"+var+"."+mf.getLabel()+".isEqual("+var+")";
   case Relation.ISNOT:
    return "_t_"+var+"."+mf.getLabel()+".isNotEqual("+var+", _op)";
   case Relation.GR_EQ:
    return "_t_"+var+"."+mf.getLabel()+".isGreaterOrEqual("+var+")";
   case Relation.SM_EQ:
    return "_t_"+var+"."+mf.getLabel()+".isSmallerOrEqual("+var+")";
   case Relation.GREATER:
    return "_t_"+var+"."+mf.getLabel()+".isGreater("+var+", _op)";
   case Relation.SMALLER:
    return "_t_"+var+"."+mf.getLabel()+".isSmaller("+var+", _op)";
   case Relation.APP_EQ:
    return "_t_"+var+"."+mf.getLabel()+".isApproxEqual("+var+", _op)";
   case Relation.VERY_EQ:
    return "_t_"+var+"."+mf.getLabel()+".isVeryEqual("+var+", _op)";
   case Relation.SL_EQ:
    return "_t_"+var+"."+mf.getLabel()+".isSlightlyEqual("+var+", _op)";
   case Relation.NOT:
    return "_op.not("+createRelationCode(r)+")";
   case Relation.MoL:
    return "_op.moreorless("+createRelationCode(r)+")";
   case Relation.SLIGHTLY:
    return "_op.slightly("+createRelationCode(r)+")";
   case Relation.VERY:
    return "_op.very("+createRelationCode(r)+")";
   default: return "";
  }
 }

 //=============================================================//
 //	  Metodos que describen el comportamiento global	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Descripcion de la estructura jerarquica del sistema		//
 //-------------------------------------------------------------//

 private String createSystemModuleCode() {
  String code = "";
  code += " //+++++++++++++++++++++++++++++++++++++++++++++++++++++//"+eol;
  code += " //               Fuzzy Inference Engine                //"+eol;
  code += " //+++++++++++++++++++++++++++++++++++++++++++++++++++++//"+eol+eol;
  code += createInferenceCode(true,true);
  code += createInferenceCode(false,true);
  code += createInferenceCode(true,false);
  code += createInferenceCode(false,false);
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de un metodo de inferencia			//
 //-------------------------------------------------------------//

 private String createInferenceCode(boolean crispInput, boolean crispOutput) {
  SystemModule system = spec.getSystemModule();
  Variable input[] = system.getInputs();
  Variable output[] = system.getOutputs();
  Variable inner[] = system.getInners();
  ModuleCall call[] = system.getModuleCalls();

  String inputtype = (crispInput? "double" : "MembershipFunction");
  String outputtype = (crispOutput? "double" : "MembershipFunction");
  String function = (crispOutput? "crispInference" : "fuzzyInference");
  String code = " public "+outputtype+"[] "+function;
  code += "("+inputtype+"[] _input) {"+eol;
  for(int i=0; i<input.length; i++) {
   code += "  MembershipFunction "+input[i];
   if(crispInput) code += " = new FuzzySingleton(_input["+i+"]);"+eol;
   else code += " = _input["+i+"];"+eol;
  }
  for(int i=0; i<output.length; i++)
   code += "  MembershipFunction "+output[i]+";"+eol;
  for(int i=1; i<inner.length; i++)
   code += "  MembershipFunction "+inner[i]+";"+eol;
  code += "  MembershipFunction[] _call;"+eol;

  for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
   code += createRulebaseCallCode( (RulebaseCall) call[i]);
  } else code += createCrispBlockCallCode( (CrispBlockCall) call[i]);

  code += "  "+outputtype+" _output[] =";
  code += " new "+outputtype+"["+output.length+"];"+eol;
  if(crispOutput) for(int i=0; i<output.length; i++) {
   code += "  if("+output[i]+" instanceof FuzzySingleton)"+eol;
   code += "   _output["+i+"] = ((FuzzySingleton) "+output[i]+").getValue();";
   code += eol;
   code += "  else _output["+i+"]";
   code += " = ((OutputMembershipFunction) "+output[i]+").defuzzify();"+eol;
  }
  else for(int i=0; i<output.length; i++) {
   code += "  _output["+i+"] = "+output[i]+";"+eol;
  }
  code += "  return _output;"+eol;
  code += " }"+eol+eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de una llamada a una base de reglas		//
 //-------------------------------------------------------------//

 private String createRulebaseCallCode(RulebaseCall call) {
  Rulebase base = call.getRulebase();
  Variable inputvar[] = call.getInputVariables();
  Variable outputvar[] = call.getOutputVariables();

  String code = "  _call = RL_"+base+"(";
  for(int j=0; j < inputvar.length; j++) code += (j==0? "":",")+inputvar[j];
  code += ");";
  for(int j=0; j < outputvar.length; j++)
    code += " "+outputvar[j]+"=_call["+j+"];";
  code += eol;
  return code;
 }

 //-------------------------------------------------------------//
 // Descripcion de una llamada a un bloque no difuso		//
 //-------------------------------------------------------------//

 private String createCrispBlockCallCode(CrispBlockCall call) {
  Variable inputvar[] = call.getInputVariables();
  Variable outputvar = call.getOutputVariable();

  String code = "  "+outputvar+" =  new FuzzySingleton(";
  code += "CB_"+call.getName()+"(";
  for(int j=0; j < inputvar.length; j++) code += (j==0? "":",")+inputvar[j];
  code += ")";
  code += ");"+eol;
  return code;
 }
}
