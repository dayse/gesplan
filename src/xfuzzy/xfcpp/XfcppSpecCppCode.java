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


package xfuzzy.xfcpp;

import xfuzzy.lang.*;

import java.io.*;
import java.util.Vector;

/**
 * Generador del fichero "fuzzysystem.cpp"
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfcppSpecCppCode {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Sistema difuso a tratar
	 */
	private Specification spec;
	
	/**
	 * Directorio en el que se va a crear el archivo ".hpp"
	 */
	private File dir;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfcppSpecCppCode(Specification spec, File dir) {
		this.spec = spec;
		this.dir = dir;
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el archivo ".cpp" correspondiente a la especificación en el
	 * directorio indicado
	 */
	public static final String create(Specification spec, File dir)
	throws XflException {
		XfcppSpecCppCode creator = new XfcppSpecCppCode(spec, dir);
		creator.createFile();
		return creator.getMessage();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Devuelve la descripción del resultado de la compilación
	 */
	public String getMessage() {
		File file = new File(dir,spec.getName()+".cpp");
		return file.getAbsolutePath();
	}

	/**
	 * Genera el fichero ".cpp"
	 */
	public void createFile() throws XflException {
		File file = new File(dir,spec.getName()+".cpp");

		try {
			OutputStream os = new FileOutputStream(file);
			PrintStream stream = new PrintStream(os);
			printHeading(stream);
			printSource(stream);
			stream.close();
		}
		catch (IOException e) {}
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el código de cabecera del fichero
	 */
	private void printHeading(PrintStream stream) {
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println("//                                                      //");
		stream.println(complete("// File:   "+spec.getName()+".cpp"));
		stream.println("//                                                      //");
		stream.println("// Author: Automatically generated by Xfuzzy            //");
		stream.println("//                                                      //");
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
	}

	/**
	 * Genera el código de las clases del sistema
	 */
	private void printSource(PrintStream stream) throws XflException {
		String name = spec.getName();
		Operatorset[] opset = spec.getOperatorsets();
		Type[] type = spec.getTypes();
		Rulebase[] base = spec.getRulebases();
		CrispBlock[] crisp = spec.getCrispBlockSet().getBlocks();

		stream.println("#ifndef _"+name+"_INFERENCE_ENGINE_");
		stream.println("#define _"+name+"_INFERENCE_ENGINE_");
		stream.println();
		stream.println("#include <stdio.h>");
		stream.println("#include <math.h>");
		stream.println("#include \"xfuzzy.hpp\"");
		stream.println("#include \""+name+".hpp\"");
		stream.println();

		Vector famv = searchFamilyClasses();
		for(int i=0; i<famv.size(); i++) {
			printFamilyCppCode(stream,(Family) famv.elementAt(i));
		}

		Vector mfv = searchMFClasses();
		for(int i=0; i<mfv.size(); i++) {
			printMFCppCode(stream,(ParamMemFunc) mfv.elementAt(i));
		}

		for(int i=0; i<opset.length; i++) printOperatorSetCode(stream,opset[i]);
		for(int i=0; i<type.length; i++) printTypeCode(stream,type[i]);
		printSystemCode(stream);
		for(int i=0; i<base.length; i++) printRulebaseCode(stream,base[i]);
		for(int i=0; i<crisp.length; i++) printCrispBlockCode(stream,crisp[i]);
		stream.println("#endif /* _"+name+"_INFERENCE_ENGINE_ */");
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el codigo de las MF                                    //
	//----------------------------------------------------------------------------//

	/**
	 * Decripción de una función de pertenencia familiar
	 */
	private void printFamilyCppCode(PrintStream stream,Family fam) throws XflException {
		String spname = spec.getName();
		String pkgname = fam.getPackageName();
		String classname = "FMF_"+spname+"_"+pkgname+"_"+fam.getFunctionName();
		String equal = fam.getEqualCppCode();
		String listname = fam.getParamListName();
		Parameter[] single = fam.getSingleParameters();
		boolean haslist = (listname != null && listname.length() > 0);

		if(equal==null || equal.length()==0) throw new XflException();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  MembershipFunction "+classname));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Constructor                                         //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print(classname+"::"+classname+"(int i,double min,double max,double step");
		stream.println(",double *single,double *list,int length) :");
		stream.println("ParamMembershipFunction(min,max,step) {");
		stream.println(" this->name = \""+classname+"\";");
		stream.println(" this->i = i;");
		for(int i=0; i<single.length; i++) {
			stream.println(" this->"+single[i].getName()+" = single["+i+"];");
		}
		if(listname != null && listname.length() > 0) {
			stream.println(" this->length = length;");
			stream.println(" this->"+listname+" = new double[length];");
			stream.println(" for(int i=0; i<length; i++) this->"+listname+"[i] = list[i];");
		}
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Function to get a clone of the object               //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println(classname+" * "+classname+"::dup() {");
		stream.print(" double _param["+single.length+"] = {");
		for(int i=0; i<single.length; i++) {
			stream.print((i==0? "":",")+single[i].getName());
		}
		stream.println("};");
		if(haslist) {
			stream.println(" double *_list = this->"+listname+";");
			stream.println(" int _length = this->length;");
		} else {
			stream.println(" double *_list = NULL;");
			stream.println(" int _length = 0;");
		}
		stream.println(" return new "+classname+"(i,min,max,step,_param,_list,_length);");
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Function to get a parameter of the function         //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double "+classname+"::param(int _i) {");
		if(single.length == 0 && haslist) {
			stream.println(" return "+listname+"[_i];");
		} else if(!haslist) {
			stream.println(" switch(_i) {");
			for(int i=0; i<single.length; i++) {
				stream.println("  case "+i+": return "+single[i].getName()+";");
			}
			stream.println("  default: return 0;");
			stream.println(" }");
		} else {
			stream.print(" if(_i>="+single.length+")");
			stream.println(" return "+listname+"[_i-"+single.length+"];");
			stream.println(" switch(_i) {");
			for(int i=0; i<single.length; i++) {
				stream.println("  case "+i+": return "+single[i].getName()+";");
			}
			stream.println("  default: return 0;");
			stream.println(" }");
		}
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Function to compute the membership degree           //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double "+classname+"::compute_eq(double x) {");
		stream.println(equal);
		stream.println("}");
		stream.println();

		String greq = fam.getGreqCppCode();
		if(greq != null && greq.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Compute the Greater_or_equal membership degree      //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::compute_greq(double x) {");
			stream.println(greq);
			stream.println("}");
			stream.println();
		}

		String smeq = fam.getSmeqCppCode();
		if(smeq != null && smeq.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Compute the Smaller_or_equal membership degree      //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::compute_smeq(double x) {");
			stream.println(smeq);
			stream.println("}");
			stream.println();
		}

		String center = fam.getCenterCppCode();
		if(center != null && center.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Function to get the center of the MF                //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::center() {");
			stream.println(center);
			stream.println("}");
			stream.println();
		}

		String basis = fam.getBasisCppCode();
		if(basis != null && basis.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Function to get the basis of the MF                 //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::basis() {");
			stream.println(basis);
			stream.println("}");
			stream.println();
		}
	}

	/**
	 * Descripción de una función de pertenencia
	 */
	private void printMFCppCode(PrintStream stream,ParamMemFunc mf) throws XflException {
		String spname = spec.getName();
		String pkgname = mf.getPackageName();
		String classname = "MF_"+spname+"_"+pkgname+"_"+mf.getFunctionName();
		String equal = mf.getEqualCppCode();
		String listname = mf.getParamListName();
		Parameter[] single = mf.getSingleParameters();
		boolean haslist = (listname != null && listname.length() > 0);

		if(equal==null || equal.length()==0) throw new XflException();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  MembershipFunction "+classname));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Constructor                                         //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print(classname+"::"+classname+"(double min,double max,double step");
		stream.println(",double *single,double *list, int length) :");
		stream.println("ParamMembershipFunction(min,max,step) {");
		stream.println(" this->name = \""+classname+"\";");
		for(int i=0; i<single.length; i++) {
			stream.println(" this->"+single[i].getName()+" = single["+i+"];");
		}
		if(haslist) {
			stream.println(" this->length = length;");
			stream.println(" this->"+listname+" = new double[length];");
			stream.println(" for(int i=0; i<length; i++) this->list[i] = list[i];");
		} else {
		}
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Function to get a clone of the object               //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println(classname+" * "+classname+"::dup() {");
		stream.print(" double _param["+single.length+"] = {");
		for(int i=0; i<single.length; i++) {
			stream.print((i==0? "":",")+single[i].getName());
		}
		stream.println("};");
		if(haslist) {
			stream.println(" double *_list = this->"+listname+";");
			stream.println(" int _length = this->length;");
		} else {
			stream.println(" double *_list = NULL;");
			stream.println(" int _length = 0;");
		}
		stream.println(" return new "+classname+"(min,max,step,_param,_list,_length);");
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Function to get a parameter of the function         //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double "+classname+"::param(int _i) {");
		if(single.length == 0 && haslist) {
			stream.println(" return "+listname+"[_i];");
		} else if(!haslist) {
			stream.println(" switch(_i) {");
			for(int i=0; i<single.length; i++) {
				stream.println("  case "+i+": return "+single[i].getName()+";");
			}
			stream.println("  default: return 0;");
			stream.println(" }");
		} else {
			stream.print(" if(_i>="+single.length+")");
			stream.println(" return "+listname+"[_i-"+single.length+"];");
			stream.println(" switch(_i) {");
			for(int i=0; i<single.length; i++) {
				stream.println("  case "+i+": return "+single[i].getName()+";");
			}
			stream.println("  default: return 0;");
			stream.println(" }");
		}
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Function to compute the membership degree           //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double "+classname+"::compute_eq(double x) {");
		stream.println(equal);
		stream.println("}");
		stream.println();

		String greq = mf.getGreqCppCode();
		if(greq != null && greq.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Compute the Greater_or_equal membership degree      //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::compute_greq(double x) {");
			stream.println(greq);
			stream.println("}");
			stream.println();
		}

		String smeq = mf.getSmeqCppCode();
		if(smeq != null && smeq.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Compute the Smaller_or_equal membership degree      //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::compute_smeq(double x) {");
			stream.println(smeq);
			stream.println("}");
			stream.println();
		}

		String center = mf.getCenterCppCode();
		if(center != null && center.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Function to get the center of the MF                //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::center() {");
			stream.println(center);
			stream.println("}");
			stream.println();
		}

		String basis = mf.getBasisCppCode();
		if(basis != null && basis.length()>0) {
			stream.println("//------------------------------------------------------//");
			stream.println("//  Function to get the basis of the MF                 //");
			stream.println("//------------------------------------------------------//");
			stream.println();
			stream.println("double "+classname+"::basis() {");
			stream.println(basis);
			stream.println("}");
			stream.println();
		}
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan la estructura global                                   //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de la estructura global del sistema
	 */
	private void printSystemCode(PrintStream stream) {
		String spname = spec.getName();
		SystemModule system = spec.getSystemModule();
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();
		Variable inner[] = system.getInners();
		ModuleCall call[] = system.getModuleCalls();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println("//                   Inference Engine                   //");
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Inference from crisp numbers giving crisp numbers   //");
		stream.println("//------------------------------------------------------//");
		stream.println();

		stream.println("double* "+spname+"::crispInference(double *_input) {");
		for(int i=0; i<input.length; i++)
			stream.println(" FuzzySingleton "+input[i]+"(_input["+i+"]);");
			stream.print(" MembershipFunction ");
		for(int i=0; i<output.length; i++) stream.print((i!=0? ", *": "*")+output[i]);
		for(int i=1; i<inner.length; i++) stream.print(", *"+inner[i]);
		stream.println(";");

		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			printRulebaseCallCode(stream, (RulebaseCall) call[i]);
		} else printCrispBlockCallCode(stream, (CrispBlockCall) call[i]);

		stream.println(" double *_output = new double["+output.length+"];");
		for(int i=0; i<output.length; i++) {
			stream.print(" if("+output[i]+"->getType() == MembershipFunction::CRISP) ");
			stream.print("_output["+i+"] = ((FuzzySingleton *) ");
			stream.println(output[i]+")->getValue();");
			stream.print(" else _output["+i+"] = ((OutputMembershipFunction *) ");
			stream.println(output[i]+")->defuzzify();");
		}
		for(int i=0; i<output.length; i++) stream.println(" delete "+output[i]+";");
		for(int i=1; i<inner.length; i++) stream.println(" delete "+inner[i]+";");
		stream.println(" return _output;");
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Inference from fuzzy numbers giving crisp numbers   //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double* "+spname+"::crispInference");
		stream.println("(MembershipFunction* &_input) {");
		for(int i=0; i<input.length; i++)
			stream.println(" MembershipFunction & "+input[i]+" = _input["+i+"];");
			stream.print(" MembershipFunction ");
		for(int i=0; i<output.length; i++) stream.print((i!=0? ", *": "*")+output[i]);
		for(int i=1; i<inner.length; i++) stream.print(", *"+inner[i]);
		stream.println(";");

		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			printRulebaseCallCode(stream, (RulebaseCall) call[i]);
		} else printCrispBlockCallCode(stream, (CrispBlockCall) call[i]);

		stream.println(" double *_output = new double["+output.length+"];");
		for(int i=0; i<output.length; i++) {
			stream.print(" if("+output[i]+"->getType() == MembershipFunction::CRISP) ");
			stream.print("_output["+i+"] = ((FuzzySingleton *) ");
			stream.println(output[i]+")->getValue();");
			stream.print(" else _output["+i+"] = ((OutputMembershipFunction *) ");
			stream.println(output[i]+")->defuzzify();");
		}
		for(int i=0; i<output.length; i++) stream.println(" delete "+output[i]+";");
		for(int i=1; i<inner.length; i++) stream.println(" delete "+inner[i]+";");
		stream.println(" return _output;");
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Inference from crisp numbers giving fuzzy numbers   //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("MembershipFunction** "+spname+"::fuzzyInference");
		stream.println("(double *_input) {");
		for(int i=0; i<input.length; i++)
			stream.println(" FuzzySingleton "+input[i]+"(_input["+i+"]);");
			stream.print(" MembershipFunction ");
		for(int i=0; i<output.length; i++) stream.print((i!=0? ", *": "*")+output[i]);
		for(int i=1; i<inner.length; i++) stream.print(", *"+inner[i]);
		stream.println(";");

		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			printRulebaseCallCode(stream, (RulebaseCall) call[i]);
		} else printCrispBlockCallCode(stream, (CrispBlockCall) call[i]);

		for(int i=1; i<inner.length; i++) stream.println(" delete "+inner[i]+";");
		stream.print(" MembershipFunction **_output = ");
		stream.println("new (MembershipFunction *)["+output.length+"];");
		for(int i=0; i<output.length; i++)
			stream.println(" _output["+i+"] = "+output[i]+";");
		stream.println(" return _output;");
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Inference from fuzzy numbers giving fuzzy numbers   //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("MembershipFunction** "+spname+"::fuzzyInference");
		stream.println("(MembershipFunction* &_input) {");
		for(int i=0; i<input.length; i++)
			stream.println(" MembershipFunction & "+input[i]+" = _input["+i+"];");
			stream.print(" MembershipFunction ");
		for(int i=0; i<output.length; i++) stream.print((i!=0? ", *": "*")+output[i]);
		for(int i=1; i<inner.length; i++) stream.print(", *"+inner[i]);
		stream.println(";");

		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			printRulebaseCallCode(stream, (RulebaseCall) call[i]);
		} else printCrispBlockCallCode(stream, (CrispBlockCall) call[i]);

		for(int i=1; i<inner.length; i++) stream.println(" delete "+inner[i]+";");
		stream.print(" MembershipFunction **_output = ");
		stream.println("new (MembershipFunction *)["+output.length+"];");
		for(int i=0; i<output.length; i++)
			stream.println(" _output["+i+"] = "+output[i]+";");
		stream.println(" return _output;");
		stream.println("}");
		stream.println();

		stream.println("//------------------------------------------------------//");
		stream.println("//  Crisp Inference with single variables (not arrays)  //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("void "+spname+"::inference(");
		for(int i=0; i<input.length; i++) stream.print((i!=0?",":"")+" double _i_"+input[i]);
		for(int i=0; i<output.length; i++) stream.print( ", double *_o_"+output[i]);
		stream.println(" ) {");
		for(int i=0; i<input.length; i++)
			stream.println(" FuzzySingleton "+input[i]+"(_i_"+input[i]+");");
		stream.print(" MembershipFunction ");
		for(int i=0; i<output.length; i++) stream.print((i!=0? ", *": "*")+output[i]);
		for(int i=1; i<inner.length; i++) stream.print(", *"+inner[i]);
		stream.println(";");

		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			printRulebaseCallCode( stream,(RulebaseCall) call[i]);
		} else printCrispBlockCallCode( stream,(CrispBlockCall) call[i]);

		for(int i=0; i<output.length; i++) {
			stream.print(" if("+output[i]+"->getType() == MembershipFunction::CRISP) ");
			stream.print("(*_o_"+output[i]+") = ((FuzzySingleton *) ");
			stream.println(output[i]+")->getValue();");
			stream.print(" else (*_o_"+output[i]+") = ((OutputMembershipFunction *) ");
			stream.println(output[i]+")->defuzzify();");
		}
		for(int i=0; i<output.length; i++) stream.println(" delete "+output[i]+";");
		for(int i=1; i<inner.length; i++) stream.println(" delete "+inner[i]+";");
		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de una llamada a una base de reglas
	 */
	private void printRulebaseCallCode(PrintStream stream,RulebaseCall call) {
		Rulebase base = call.getRulebase();
		Variable[] inputvar = call.getInputVariables();
		Variable[] outputvar = call.getOutputVariables();

		stream.print(" RL_"+base+"(");
		for(int i=0; i < inputvar.length; i++) {
			if(inputvar[i].isInput()) stream.print((i==0? "" : ", ")+inputvar[i]);
			else stream.print((i==0? "*" : ", *")+inputvar[i]);
		}
		for(int i=0; i < outputvar.length; i++) stream.print(", &"+outputvar[i]);
		stream.println(");");
	}

	/**
	 * Descripción de una llamada a un bloque no difuso
	 */
	private void printCrispBlockCallCode(PrintStream stream,CrispBlockCall call) {
		Variable[] inputvar = call.getInputVariables();
		Variable outputvar = call.getOutputVariable();

		stream.print(" "+outputvar+" = new FuzzySingleton(");
		stream.print(" CB_"+call.getName()+"(");
		for(int i=0; i < inputvar.length; i++) {
			if(inputvar[i].isInput()) stream.print((i==0? "" : ", ")+inputvar[i]);
			else stream.print((i==0? "*" : ", *")+inputvar[i]);
		}
		stream.println(") );");
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan los conjuntos de operadores                            //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un conjunto de operadores
	 */
	private void printOperatorSetCode(PrintStream stream,Operatorset opset) throws XflException {
		String opname = "OP_"+spec.getName()+"_"+opset.getName();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  Operatorset "+opname));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator AND                     //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createBinaryCode(stream,opset.and, opname+"::_and");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator OR                      //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createBinaryCode(stream,opset.or, opname+"::_or");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator ALSO                    //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createBinaryCode(stream,opset.also, opname+"::_also");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator IMPLICATION             //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createBinaryCode(stream,opset.imp, opname+"::_imp");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator NOT                     //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createUnaryCode(stream,opset.not, opname+"::_not");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator STRONGLY                //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createUnaryCode(stream,opset.very, opname+"::_very");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator MORE OR LESS            //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createUnaryCode(stream,opset.moreorless, opname+"::_moreorless");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the operator SLIGHTLY                //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createUnaryCode(stream,opset.slightly, opname+"::_slightly");
		stream.println("//------------------------------------------------------//");
		stream.println("//  Description of the defuzzification method           //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		createDefuzCode(stream,opset.defuz, opname+"::_defuz");
	}

	/**
	 * Descripción de un operador binario
	 */
	private void createBinaryCode(PrintStream stream, Binary op, String name) throws XflException {
		String listname = op.getParamListName();
		Parameter single[] = op.getSingleParameters();
		Parameter list[] = op.getParamList();

		stream.println("double "+name+"(double a, double b) {");
		for(int i=0; i<single.length; i++) {
			stream.println(" double "+single[i].getName()+" = "+single[i].value+";");
		}
		if(listname != null && listname.length()>0) {
			stream.print("   double "+listname+"["+list.length+"] = {");
			for(int i=0; i<list.length; i++) 
				stream.print((i>0? ",":"")+list[i].value);
			stream.println("};");
		}

		stream.println(op.getCppCode());
		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de un operador unario
	 */
	private void createUnaryCode(PrintStream stream, Unary op, String name) throws XflException {
		String listname = op.getParamListName();
		Parameter single[] = op.getSingleParameters();
		Parameter list[] = op.getParamList();

		stream.println("double "+name+"(double a) {");
		for(int i=0; i<single.length; i++) {
			stream.println(" double "+single[i].getName()+" = "+single[i].value+";");
		}
		if(listname != null && listname.length()>0) {
			stream.print("   double "+listname+"["+list.length+"] = {");
			for(int i=0; i<list.length; i++)
				stream.print((i>0? ",":"")+list[i].value);
			stream.println("};");
		}

		stream.println(op.getCppCode());
		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de un método de concreción
	 */
	private void createDefuzCode(PrintStream stream, DefuzMethod op, String name)
	throws XflException {
		String listname = op.getParamListName();
		Parameter single[] = op.getSingleParameters();
		Parameter list[] = op.getParamList();

		String defuzcode = op.getCppCode();
		stream.println("double "+name+"(OutputMembershipFunction &mf) {");

		if(defuzcode.indexOf("min")!=-1) stream.println( " double min = mf.min();");
		if(defuzcode.indexOf("max")!=-1) stream.println(" double max = mf.max();");
		if(defuzcode.indexOf("step")!=-1) stream.println(" double step = mf.step();");

		for(int i=0; i<single.length; i++) {
			stream.println( " double "+single[i].getName()+" = "+single[i].value+";" );
		}
		if(listname != null && listname.length()>0) {
			stream.print("   double "+listname+"["+list.length+"] = {");
			for(int i=0; i<list.length; i++)
				stream.print( (i>0? ",":"")+list[i].value );
			stream.println( "};" );
		}

		stream.println(defuzcode);
		stream.println("}");
		stream.println();
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan los tipos de variables                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un tipo de variable linguistica
	 */
	private void printTypeCode(PrintStream stream,Type type) throws XflException {
		String specname = spec.getName();
		String typename = "TP_"+specname+"_"+type.getName();

		Universe u = type.getUniverse();
		Family[] fam = type.getFamilies();
		LinguisticLabel[] allmf = type.getAllMembershipFunctions();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  Type "+typename));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("//  Constructor                                         //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println(typename+"::"+typename+"() {");
		stream.println(" min = "+u.min()+";");
		stream.println(" max = "+u.max()+";");
		stream.println(" step = "+u.step()+";");

		for(int i=0; i<fam.length; i++) {
			Parameter single[] = fam[i].getSingleParameters();
			stream.print(" double _pfs_"+fam[i]+"[] = { ");
			for(int j=0; j<single.length; j++) stream.print((j==0? "":",")+single[j].value);
			stream.println(" };");

			Parameter list[] = fam[i].getParamList();
			int listlength = (list == null? 0 : list.length);
			stream.print(" double _pfl_"+fam[i]+"[] = { ");
			for(int j=0; j<listlength; j++) stream.print((j==0? "":",")+list[j].value);
			stream.println(" };");
		}

		for(int i=0; i<allmf.length; i++) if(allmf[i] instanceof ParamMemFunc) {
			ParamMemFunc pmf = (ParamMemFunc) allmf[i];
			Parameter single[] = pmf.getSingleParameters();
			stream.print(" double _ps_"+pmf.getLabel()+"[] = { ");
			for(int j=0; j<single.length; j++) stream.print((j==0? "":",")+single[j].value);
			stream.println(" };");

			Parameter list[] = pmf.getParamList();
			int listlength = (list == null? 0 : list.length);
			stream.print(" double _pl_"+pmf.getLabel()+"[] = { ");
			for(int j=0; j<listlength; j++) stream.print((j==0? "":",")+list[j].value);
			stream.println(" };");
		}

		for(int i=0; i<allmf.length; i++) {
			if(allmf[i] instanceof ParamMemFunc) {
				ParamMemFunc pmf = (ParamMemFunc) allmf[i];
				String pkgname = pmf.getPackageName();
				Parameter list[] = pmf.getParamList();
				int length = (list == null? 0 : list.length);
				String mfname = "MF_"+specname+"_"+pkgname+"_"+pmf.getFunctionName();
				stream.print(" "+pmf.getLabel()+" = "+mfname);
				stream.print("(min,max,step,");
				stream.println("_ps_"+pmf.getLabel()+",_pl_"+pmf.getLabel()+","+length+");");
			} else {
				FamiliarMemFunc fmf = (FamiliarMemFunc) allmf[i];
				Family fammf = fmf.getFamily();
				int index = fmf.getIndex();
				String pkgname = fammf.getPackageName();
				Parameter list[] = fammf.getParamList();
				int length = (list == null? 0 : list.length);
				String mfname = "FMF_"+specname+"_"+pkgname+"_"+fammf.getFunctionName();
				stream.print(" "+fmf.getLabel()+" = "+mfname+"("+index+",min,max,step");
				stream.println( ",_pfs_"+fammf+",_pfl_"+fammf+","+length+");");
			}
		}
		stream.println("}");
		stream.println();
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan las bases de reglas                                    //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de una base de reglas
	 */
	private void printRulebaseCode(PrintStream stream,Rulebase base) throws XflException {
		String spname = spec.getName();
		String rbname = "RL_"+base.getName();
		Variable[] inputvar = base.getInputs();
		Variable[] outputvar = base.getOutputs();
		Operatorset operation = base.getOperatorset();
		Rule[] rule = base.getRules();

		stream.println( "//------------------------------------------------------//" );
		stream.println( complete("//  Rulebase "+rbname) );
		stream.println( "//------------------------------------------------------//" );
		stream.println();
		stream.print( "void "+spname+"::"+rbname+"(" );
		for(int i=0; i<inputvar.length; i++)
			stream.print( (i==0? "": ", ")+"MembershipFunction &"+inputvar[i] );
		for(int i=0; i<outputvar.length; i++)
			stream.print( ", MembershipFunction ** _o_"+outputvar[i] );
		stream.println(  ") {" );
		stream.println(  " OP_"+spname+"_"+operation.getName()+" _op;" );
		stream.println(  " double _rl;" );
		for(int i=0; i<outputvar.length; i++)
			stream.println( " int _i_"+outputvar[i]+"=0;" );
		stream.println( " double *_input = new double["+inputvar.length+"];" );
		for(int i=0; i<inputvar.length; i++)
			stream.println( " _input["+i+"] = "+inputvar[i]+".getValue();" );
		for(int i=0; i<outputvar.length; i++) {
			stream.print( " OutputMembershipFunction *"+outputvar[i] );
			stream.print(  " = new OutputMembershipFunction(" );
			stream.print(  "new OP_"+spname+"_"+operation.getName()+"()," );
			stream.println(  base.computeOutputSize(i)+","+inputvar.length+",_input);");
		}
		for(int i=0; i<inputvar.length; i++)
			stream.println( " TP_"+spname+"_"+inputvar[i].getType()+" _t_"+inputvar[i]+";");
		for(int i=0; i<outputvar.length; i++)
			stream.println( " TP_"+spname+"_"+outputvar[i].getType()+" _t_"+outputvar[i]+";");

		for(int i=0; i<rule.length; i++) createRuleCode(stream, rule[i]);

		if(operation.defuz.isDefault())
			for(int i=0; i<outputvar.length; i++)
				stream.println( " *_o_"+outputvar[i]+" = "+outputvar[i]+";");
		else for(int i=0; i<outputvar.length; i++) {
			stream.print(  " *_o_"+outputvar[i] );
			stream.println(  " = new FuzzySingleton( (*"+outputvar[i]+").defuzzify());");
			stream.println( " delete "+outputvar[i]+";" );
		}
		stream.println(" delete _input;");
		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de una regla
	 */
	private void createRuleCode(PrintStream stream, Rule rule) throws XflException {
		double degree = rule.getDegree();
		Relation premise = rule.getPremise();
		Conclusion[] conc = rule.getConclusions();

		stream.print(" _rl = ");
		if(degree != 1.0) stream.print(degree+"*");
		stream.println(createRelationCode(premise)+";");
		for(int j=0; j<conc.length; j++) stream.println(createConclusionCode(conc[j]));
	}

	/**
	 * Descripción de la conclusión de una regla
	 */
	private String createConclusionCode(Conclusion conc) throws XflException {
		Variable var = conc.getVariable();
		LinguisticLabel mf = conc.getMembershipFunction();

		String code =  " (*"+var+").conc[_i_"+var+"] = new RuleConclusion(_rl, ";
		code += "_t_"+var+"."+mf+".dup());";
		code += " _i_"+var+"++;";
		return code;
	}

	/**
	 * Descripción de una proposicion
	 */
	private String createRelationCode(Relation rel) throws XflException {
		Variable var = rel.getVariable();
		LinguisticLabel mf = rel.getMembershipFunction();
		Relation l = rel.getLeftRelation();
		Relation r = rel.getRightRelation();

		switch(rel.getKind()) {
		case Relation.AND:
			return "_op._and("+createRelationCode(l)+","+createRelationCode(r)+")";
		case Relation.OR:
			return "_op._or("+createRelationCode(l)+","+createRelationCode(r)+")";
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
			return "_op._not("+createRelationCode(r)+")";
		case Relation.MoL:
			return "_op._moreorless("+createRelationCode(r)+")";
		case Relation.SLIGHTLY:
			return "_op.slightly("+createRelationCode(r)+")";
		case Relation.VERY:
			return "_op._very("+createRelationCode(r)+")";
		default: return "";
		}
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el código de un bloque no difuso                       //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un bloque no difuso
	 */
	private void printCrispBlockCode(PrintStream stream,CrispBlock crisp) throws XflException {
		String spname = spec.getName();
		String cbname = "CB_"+crisp.getName();
		int inputs = crisp.inputs();

		stream.println("//------------------------------------------------------//");
		stream.println(complete("//  CrsipBlock "+cbname));
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double "+spname+"::"+cbname+"(");
		for(int i=0; i<inputs; i++) stream.print((i==0?"":", ")+"MembershipFunction &_i"+i);
		stream.println(") {");

		Parameter singleparam[] = crisp.getSingleParameters();
		for(int i=0; i<singleparam.length; i++) {
			stream.print(" double "+singleparam[i].getName());
			stream.println(" = "+singleparam[i].value+";");
		}

		if(crisp.hasParamList()) {
			Parameter paramlist[] = crisp.getParamList();
			int listlength = (paramlist == null? 0 : paramlist.length);
			stream.print(" double "+crisp.getParamListName()+"["+listlength+"] = {");
			for(int i=0; i<listlength; i++)
				stream.print((i>0? ",":"")+paramlist[i].value);
			stream.println("};");
		}

		stream.println(" double x["+inputs+"];");
		stream.println();
		for(int i=0; i<inputs; i++) {
			stream.println(" if(_i"+i+".getType() == MembershipFunction::CRISP) {");
			stream.println("  x["+i+"] = ((FuzzySingleton &) _i"+i+").getValue();");
			stream.println(" } else {");
			stream.println("  x["+i+"] = ((OutputMembershipFunction &) _i"+i+").defuzzify();");
			stream.println(" }");
			stream.println();
		}

		stream.println(crisp.getCppCode());
		stream.println("}");
		stream.println();
	}

	//----------------------------------------------------------------------------//
	// Métodos auxiliares                                                         //
	//----------------------------------------------------------------------------//

	/**
	 * Busca las familias de funciones de pertenencia incluidas
	 */
	private Vector searchFamilyClasses() {
		Vector nmv = new Vector();
		Vector famv = new Vector();
		Type[] type = spec.getTypes();
		String spname = spec.getName();

		for(int i=0; i<type.length; i++) {
			Family[] fam = type[i].getFamilies();
			for(int j=0; j<fam.length; j++) {
				String pkgname = fam[j].getPackageName();
				String classname = "FMF_"+spname+"_"+pkgname+"_"+fam[j].getFunctionName();
				boolean included = false;
				for(int k=0; k<nmv.size(); k++)
					if(classname.equals( (String) nmv.elementAt(k) )) included = true;
				if(!included) {
					nmv.addElement(classname);
					famv.addElement(fam[j]);
				}
			}
		}
		return famv;
	}

	/**
	 * Busca las funciones de pertenencia incluidas
	 */
	private Vector searchMFClasses() {
		Vector nmv = new Vector();
		Vector mfv = new Vector();
		Type[] type = spec.getTypes();
		String spname = spec.getName();

		for(int i=0; i<type.length; i++) {
			ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
			for(int j=0; j<mf.length; j++) {
				String pkgname = mf[j].getPackageName();
				String classname = "MF_"+spname+"_"+pkgname+"_"+mf[j].getFunctionName();
				boolean included = false;
				for(int k=0; k<nmv.size(); k++)
					if(classname.equals( (String) nmv.elementAt(k) )) included = true;
				if(!included) {
					nmv.addElement(classname);
					mfv.addElement(mf[j]);
				}
			}
		}
		return mfv;
	}

	/**
	 * Completa la cadena hasta 58 caracteres
	 */
	private String complete(String begining) {
		String line = begining;
		for(int i=begining.length(); i<56; i++) line += " ";
		line += "//";
		return line;
	}
}
