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

import java.io.*;

/**
 * Generador del fichero "xfuzzy.hpp"
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfcppXfuzzyHppCode {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Directorio en el que crear el fichero "xfuzzy.hpp"
	 */
	private File dir;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfcppXfuzzyHppCode(File dir) {
		this.dir = dir;
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el fichero "xfuzzy.hpp"
	 */
	public static final String create(File dir) {
		XfcppXfuzzyHppCode creator = new XfcppXfuzzyHppCode(dir);
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
		File file = new File(dir,"xfuzzy.hpp");
		return file.getAbsolutePath();
	}

	/**
	 * Genera el fichero "xfuzzy.hpp"
	 */
	public void createFile() {
		File file = new File(dir,"xfuzzy.hpp");
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
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el código de cabecera
	 */
	private void printHeading(PrintStream stream) {
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println("//                                                      //");
		stream.println("// File:   xfuzzy.hpp                                   //");
		stream.println("//                                                      //");
		stream.println("// Author: Automatically generated by Xfuzzy            //");
		stream.println("//                                                      //");
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
	}

	/**
	 * Genera el código de las estructuras
	 */
	private void printSource(PrintStream stream) {
		stream.println("#ifndef _XFUZZY_HPP");
		stream.println("#define _XFUZZY_HPP");
		createMembershipFunction(stream);
		createFuzzySingleton(stream);
		createFuzzyInferenceEngine(stream);
		createOperatorset(stream);
		createParamMembershipFunction(stream);
		createRuleConclusion(stream);
		createOutputMembershipFunction(stream);
		stream.println("#endif /* _XFUZZY_HPP */");
	}

	/**
	 * Genera la estructura de la clase MembershipFunction
	 */
	private void createMembershipFunction(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//           Abstract class of a fuzzy number           //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class MembershipFunction {");
		stream.println("public:");
		stream.println(" enum Type { GENERAL, CRISP, INNER };");
		stream.println(" virtual enum Type getType() { return GENERAL; }");
		stream.println(" virtual double getValue() { return 0; }");
		stream.println(" virtual double compute(double x) = 0;");
		stream.println(" virtual ~MembershipFunction() {}");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase FuzzySingleton
	 */
	private void createFuzzySingleton(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//               Class of a crisp number                //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class FuzzySingleton: public MembershipFunction {");
		stream.println("private:");
		stream.println(" double value;");
		stream.println();
		stream.println("public:");
		stream.println(" FuzzySingleton(double value) { this->value = value; }");
		stream.println(" virtual ~FuzzySingleton() {}");
		stream.println(" virtual double getValue() { return value; }");
		stream.println(" virtual enum Type getType() { return CRISP; }");
		stream.print(" virtual double compute(double x) ");
		stream.println("{ return (x==value? 1.0: 0.0);}");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase FuzzyInferenceEngine
	 */
	private void createFuzzyInferenceEngine(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//      Abstract class of a fuzzy inference engine      //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class FuzzyInferenceEngine {");
		stream.println("public:");
		stream.println(" virtual double* crispInference(double* input) = 0;");
		stream.print(" virtual double* ");
		stream.println("crispInference(MembershipFunction* &input) = 0;");
		stream.print(" virtual MembershipFunction** ");
		stream.println("fuzzyInference(double* input) = 0;");
		stream.print(" virtual MembershipFunction** ");
		stream.println("fuzzyInference(MembershipFunction* &input) = 0;");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase Operatorset
	 */
	private void createOperatorset(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//          Abstract class of an operator set           //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class OutputMembershipFunction;");
		stream.println();
		stream.println("class Operatorset {");
		stream.println("public:");
		stream.println(" virtual double _and(double a, double b) = 0;");
		stream.println(" virtual double _or(double a, double b) = 0;");
		stream.println(" virtual double _also(double a, double b) = 0;");
		stream.println(" virtual double _imp(double a, double b) = 0;");
		stream.println(" virtual double _not(double a) = 0;");
		stream.println(" virtual double _very(double a) = 0;");
		stream.println(" virtual double _moreorless(double a) = 0;");
		stream.println(" virtual double _slightly(double a) = 0;");
		stream.println(" virtual double _defuz(OutputMembershipFunction &mf) = 0;");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase ParamMembershipFunction
	 */
	private void createParamMembershipFunction(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//       Membership function of an input variable       //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class ParamMembershipFunction {");
		stream.println("public:");
		stream.println(" double min, max, step;");
		stream.println(" char *name;");
		stream.println();
		stream.println(" ParamMembershipFunction(double min, double max, double step);");
		stream.println(" ParamMembershipFunction() {};");
		stream.println(" virtual ~ParamMembershipFunction() {};");
		stream.println(" virtual double compute_eq(double x) = 0;");
		stream.println(" virtual double param(int i) = 0;");
		stream.println(" virtual double center();");
		stream.println(" virtual double basis();");
		stream.println(" virtual double compute_smeq(double x);");
		stream.println(" virtual double compute_greq(double x);");
		stream.println(" double isEqual(MembershipFunction &mf);");
		stream.println(" double isGreaterOrEqual(MembershipFunction &mf);");
		stream.println(" double isSmallerOrEqual(MembershipFunction &mf);");
		stream.println(" double isGreater(MembershipFunction &mf, Operatorset &op);");
		stream.println(" double isSmaller(MembershipFunction &mf, Operatorset &op);");
		stream.println(" double isNotEqual(MembershipFunction &mf, Operatorset &op);");
		stream.println(" double isApproxEqual(MembershipFunction &mf, Operatorset &op);");
		stream.println(" double isVeryEqual(MembershipFunction &mf, Operatorset &op);");
		stream.print(" double isSlightlyEqual");
		stream.println("(MembershipFunction &mf, Operatorset &op);");
		stream.println(" int isXflSingleton();");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase RuleConclusion
	 */
	private void createRuleConclusion(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//       Class for the conclusion of a fuzzy rule       //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class RuleConclusion {");
		stream.println("private:");
		stream.println(" double deg;");
		stream.println(" ParamMembershipFunction *mf;");
		stream.println("public:");
		stream.println(" RuleConclusion(double degree, ParamMembershipFunction *mf)");
		stream.println("  { this->deg = degree; this->mf = mf; }");
		stream.println(" ~RuleConclusion() { delete mf; }");
		stream.println(" double degree() { return deg; }");
		stream.println(" double compute(double x) { return mf->compute_eq(x); }");
		stream.println(" double center() { return mf->center(); }");
		stream.println(" double basis() { return mf->basis(); }");
		stream.println(" double param(int i) { return mf->param(i); }");
		stream.println(" double min() { return mf->min; }");
		stream.println(" double max() { return mf->max; }");
		stream.println(" double step() { return mf->step; }");
		stream.println(" int isDiscrete() { return mf->isXflSingleton(); }");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase OutputMembershipFunction
	 */
	private void createOutputMembershipFunction(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//      Membership function of an output variable       //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("class OutputMembershipFunction: public MembershipFunction {");
		stream.println("public:");
		stream.println(" int length;");
		stream.println(" int inputlength;");
		stream.println(" double *input;");
		stream.println(" RuleConclusion **conc;");
		stream.println("private:");
		stream.println(" Operatorset *op;");
		stream.println("public:");
		stream.println(" OutputMembershipFunction() {};");
		stream.print(" OutputMembershipFunction");
		stream.println("(Operatorset *op, int length, int inputlength, double *input);");
		stream.print(" virtual ~OutputMembershipFunction() ");
		stream.println("{ delete op; delete [] conc; };");
		stream.println(" virtual enum Type getType() { return INNER; }");
		stream.println(" double compute(double x);");
		stream.println(" double defuzzify() { return op->_defuz( (*this) ); }");
		stream.println(" double min() { return conc[0]->min(); }");
		stream.println(" double max() { return conc[0]->max(); }");
		stream.println(" double step() { return conc[0]->step(); }");
		stream.println(" int isDiscrete();");
		stream.println("};");
		stream.println();
	}
}
