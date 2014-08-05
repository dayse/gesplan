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
 * Generador del fichero "xfuzzy.cpp"
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfcppXfuzzyCppCode {

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
	public XfcppXfuzzyCppCode(File dir) {
		this.dir = dir;
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el fichero "xfuzzy.cpp"
	 */
	public static final String create(File dir) {
		XfcppXfuzzyCppCode creator = new XfcppXfuzzyCppCode(dir);
		creator.createFile();
		return creator.getMessage();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Devuelve la descripcion del resultado de la compilacion
	 */
	public String getMessage() {
		File file = new File(dir,"xfuzzy.cpp");
		return file.getAbsolutePath();
	}

	/**
	 * Genera el fichero "xfuzzy.cpp"
	 */
	public void createFile() {
		File file = new File(dir,"xfuzzy.cpp");
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
		stream.println("// File:   xfuzzy.cpp                                   //");
		stream.println("//                                                      //");
		stream.println("// Author: Automatically generated by Xfuzzy            //");
		stream.println("//                                                      //");
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
	}

	/**
	 * Genera el contenido del fichero
	 */
	private void printSource(PrintStream stream) {
		stream.println("#include <string.h>");
		stream.println("#include \"xfuzzy.hpp\"");
		stream.println();
		createParamMembershipFunction(stream);
		createOutputMembershipFunction(stream);
	}

	/**
	 * Genera el código de la clase ParamMembershipFunction
	 */
	private void createParamMembershipFunction(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//       Membership function of an input variable       //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Constructor                                          //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("ParamMembershipFunction::ParamMembershipFunction");
		stream.println("(double min, double max, double step) {");
		stream.println(" this->min = min;");
		stream.println(" this->max = max;");
		stream.println(" this->step = step;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Default Center function                              //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double ParamMembershipFunction::center() { return 0; }");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Default Basis function                               //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double ParamMembershipFunction::basis() { return 0; }");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Default Smaller_or_equal function                    //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double ParamMembershipFunction::compute_smeq(double x) {");
		stream.println(" double degree=0, mu;");
		stream.print(" for(double y=max; y>=x ; y-=step) ");
		stream.println("if((mu = compute_eq(y))>degree) degree=mu;");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Default Greater_or_equal function                    //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double ParamMembershipFunction::compute_greq(double x) {");
		stream.println(" double degree=0, mu;");
		stream.print(" for(double y=min; y<=x ; y+=step) ");
		stream.println("if((mu = compute_eq(y))>degree) degree=mu;");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to detect singletons                        //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("int ParamMembershipFunction::isXflSingleton() {");
		stream.println(" int size = strlen(name);");
		stream.println(" if(size < 14) return 0;");
		stream.println(" if( !strcmp(&name[size-14],\"_xfl_singleton\") ) return 1;");
		stream.println(" return 0;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var == label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isEqual");
		stream.println("(MembershipFunction &mf) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.println("  { return compute_eq( ((FuzzySingleton &) mf).getValue()); }");
		stream.println(" double mu1,mu2,minmu,degree=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = compute_eq(omf.conc[i]->param(0));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = compute_eq(x);");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var >= label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isGreaterOrEqual");
		stream.println("(MembershipFunction &mf) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.println("  { return compute_greq( ((FuzzySingleton &) mf).getValue()); }");
		stream.println(" double mu1,mu2,minmu,degree=0,greq=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = compute_greq(omf.conc[i]->param(0));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = compute_eq(x);");
		stream.println("   if( mu2>greq ) greq = mu2;");
		stream.println("   if( mu1<greq ) minmu = mu1; else minmu = greq;");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var <= label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isSmallerOrEqual");
		stream.println("(MembershipFunction &mf) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.println("  { return compute_smeq( ((FuzzySingleton &) mf).getValue()); }");
		stream.println(" double mu1,mu2,minmu,degree=0,smeq=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = compute_smeq(omf.conc[i]->param(0));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=max; x>=min; x-=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = compute_eq(x);");
		stream.println("   if( mu2>smeq ) smeq = mu2;");
		stream.println("   if( mu1<smeq ) minmu = mu1; else minmu = smeq;");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var > label)        //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isGreater");
		stream.println("(MembershipFunction &mf, Operatorset &op) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.print("  { return op._not(compute_smeq( ");
		stream.println("((FuzzySingleton &) mf).getValue())); }");
		stream.println(" double mu1,mu2,minmu,gr,degree=0,smeq=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = op._not(compute_smeq(omf.conc[i]->param(0)));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=max; x>=min; x-=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = compute_eq(x);");
		stream.println("   if( mu2>smeq ) smeq = mu2;");
		stream.println("   gr = op._not(smeq);");
		stream.println("   minmu = ( mu1<gr ? mu1 : gr);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var < label)        //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isSmaller");
		stream.println("(MembershipFunction &mf, Operatorset &op) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.print("  { return op._not(compute_greq( ");
		stream.println("((FuzzySingleton &) mf).getValue())); }");
		stream.println(" double mu1,mu2,minmu,sm,degree=0,greq=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = op._not(compute_greq(omf.conc[i]->param(0)));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = compute_eq(x);");
		stream.println("   if( mu2>greq ) greq = mu2;");
		stream.println("   sm = op._not(greq);");
		stream.println("   minmu = ( mu1<sm ? mu1 : sm);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var != label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isNotEqual");
		stream.println("(MembershipFunction &mf, Operatorset &op) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.print("  { return op._not(compute_eq( ");
		stream.println("((FuzzySingleton &) mf).getValue())); }");
		stream.println(" double mu1,mu2,minmu,degree=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = op._not(compute_eq(omf.conc[i]->param(0)));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = op._not(compute_eq(x));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var ~= label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isApproxEqual");
		stream.println("(MembershipFunction &mf, Operatorset &op) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.print("  { return op._moreorless(compute_eq( ");
		stream.println("((FuzzySingleton &) mf).getValue())); }");
		stream.println(" double mu1,mu2,minmu,degree=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = op._moreorless(compute_eq(omf.conc[i]->param(0)));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = op._moreorless(compute_eq(x));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var += label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isVeryEqual");
		stream.println("(MembershipFunction &mf, Operatorset &op) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.print("  { return op._very(compute_eq( ");
		stream.println("((FuzzySingleton &) mf).getValue())); }");
		stream.println(" double mu1,mu2,minmu,degree=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = op._very(compute_eq(omf.conc[i]->param(0)));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = op._very(compute_eq(x));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute proposition (var %= label)       //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("double ParamMembershipFunction::isSlightlyEqual");
		stream.println("(MembershipFunction &mf, Operatorset &op) {");
		stream.println(" if( mf.getType() == MembershipFunction::CRISP )");
		stream.print("  { return op._slightly(compute_eq( ");
		stream.println("((FuzzySingleton &) mf).getValue())); }");
		stream.println(" double mu1,mu2,minmu,degree=0;");
		stream.println(" if( mf.getType() == MembershipFunction::INNER &&");
		stream.println("     ((OutputMembershipFunction &) mf).isDiscrete() ){");
		stream.print("  OutputMembershipFunction &omf = ");
		stream.println("(OutputMembershipFunction &) mf;");
		stream.println("  for(int i=0; i<omf.length; i++){");
		stream.println("   mu1 = omf.conc[i]->degree();");
		stream.println("   mu2 = op._slightly(compute_eq(omf.conc[i]->param(0)));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" else {");
		stream.println("  for(double x=min; x<=max; x+=step){");
		stream.println("   mu1 = mf.compute(x);");
		stream.println("   mu2 = op._slightly(compute_eq(x));");
		stream.println("   minmu = (mu1<mu2 ? mu1 : mu2);");
		stream.println("   if( degree<minmu ) degree = minmu;");
		stream.println("  }");
		stream.println(" }");
		stream.println(" return degree;");
		stream.println("}");
		stream.println();
	}

	/**
	 * Genera el código de la clase OutputMembershipFunction
	 */
	private void createOutputMembershipFunction(PrintStream stream) {
		stream.println("//======================================================//");
		stream.println("//       Membership function of an inner variable       //");
		stream.println("//======================================================//");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Constructor                                          //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.print("OutputMembershipFunction::OutputMembershipFunction");
		stream.println("(Operatorset *op,int length,int inputlength,double *input){");
		stream.println(" this->op = op;");
		stream.println(" this->length = length;");
		stream.println(" this->inputlength = inputlength;");
		stream.println(" this->input = input;");
		stream.println(" this->conc = new (RuleConclusion*)[length];");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to compute an aggregation of MFs            //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("double OutputMembershipFunction::compute(double x) {");
		stream.println(" double dom = op->_imp(conc[0]->degree(),conc[0]->compute(x));");
		stream.println(" for(int i=1; i<length; i++)");
		stream.print("   dom = op->_also(dom,op->_imp");
		stream.println("(conc[i]->degree(),conc[i]->compute(x)));");
		stream.println(" return dom;");
		stream.println("}");
		stream.println();
		stream.println("//------------------------------------------------------//");
		stream.println("// Function to detect an aggregation of singletons      //");
		stream.println("//------------------------------------------------------//");
		stream.println();
		stream.println("int OutputMembershipFunction::isDiscrete() {");
		stream.print(" for(int i=0; i<length; i++) ");
		stream.println("if(!conc[i]->isDiscrete()) return 0;");
		stream.println(" return 1;");
		stream.println("}");
		stream.println();
	}
}
