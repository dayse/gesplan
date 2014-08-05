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


package xfuzzy.xfsw;

import xfuzzy.lang.*;
import java.io.*;


/**
 * Generador del fichero de código de la descripción C
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfswCCode {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Sistema difuso a compilar
	 */
	private Specification spec;
	
	/**
	 * Directorio base para la creación del archivo
	 */
	private File dir;
	
	/**
	 * Clase que analiza los funciones utilizadas en el sistema
	 */
	private XfswDataUsage usage;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfswCCode(Specification spec, File dir) {
		this.spec = spec;
		this.dir = dir;
		this.usage = new XfswDataUsage(spec);
	}
	
	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el fichero C asociado al sistema difuso
	 */
	public static final String create(Specification spec,File dir)
	throws XflException {
		XfswCCode creator = new XfswCCode(spec,dir);
		creator.createFile();
		return creator.getMessage();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Método que describe el resultado
	 */
	public String getMessage() {
		File file = new File(dir,spec.getName()+".c");
		return file.getAbsolutePath();
	}

	/**
	 * Genera el fichero "sistema.c"
	 */
	public void createFile() throws XflException {
		File file = new File(dir,spec.getName()+".c");
		try {
			OutputStream ostream = new FileOutputStream(file);
			PrintStream stream = new PrintStream(ostream);
			writeHeading(stream);
			writeSource(stream);
			stream.close();
		}
		catch(IOException e) {}
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	//	Métodos que generan el contenido del fichero                              //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el código de cabecera	
	 */
	private void writeHeading(PrintStream stream) {
		stream.println("/*++++++++++++++++++++++++++++++++++++++++++++++++++++++*/");
		stream.println("/*                                                      */");
		stream.println(complete("/* File:  "+spec.getName()+".c"));
		stream.println("/*                                                      */");
		stream.println("/* Author: Automatically generated by Xfuzzy            */");
		stream.println("/*                                                      */");
		stream.println("/*++++++++++++++++++++++++++++++++++++++++++++++++++++++*/");
		stream.println();
	}

	/**
	 * Genera el contenido del fichero
	 */
	private void writeSource(PrintStream stream) throws XflException {
		String name = spec.getName();
		Operatorset[] opset = spec.getOperatorsets();
		Type[] type = spec.getTypes();
		Rulebase[] base = spec.getRulebases();
		CrispBlock[] crisp = spec.getCrispBlockSet().getBlocks();

		stream.println("#include <stdio.h>");
		stream.println("#include <math.h>");
		stream.println("#include \""+name+".h\"");
		stream.println();

		if(usage.isComputeUsed()) createComputeFunction(stream);
		
		Family[] famlist = usage.getFamilyList();
		for(int i=0; i<famlist.length; i++)	createFamilyCode(stream, famlist[i]);

		ParamMemFunc[] mflist = usage.getMFList();
		for(int i=0; i<mflist.length; i++) createMFCode(stream, mflist[i]);

		for(int i=0; i<opset.length; i++) createOperatorSetCode(stream,opset[i]);
		for(int i=0; i<type.length; i++) createTypeCode(stream,type[i]);
		for(int i=0; i<base.length; i++) createRulebaseCode(stream,base[i]);
		for(int i=0; i<crisp.length; i++) createCrispBlockCode(stream,crisp[i]);
		createSystemModuleCode(stream);
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el código de las familias                              //
	//----------------------------------------------------------------------------//

	private void createComputeFunction(PrintStream stream) {
		stream.println("/*======================================================*/");
		stream.println("/*  Common function to compute a fuzzy number           */");
		stream.println("/*======================================================*/");
		stream.println();
		stream.println("static double compute(FuzzyNumber fn,double x) {");
		stream.println(" int length = fn.length;");
		stream.println(" int i;");
		stream.println(" double imp = fn.imp(fn.degree[0],fn.conc[0].equal(x));");
		stream.println(" double mu = imp");
		stream.println();
		stream.println(" for(i=1; i<length; i++) {");
		stream.println("  imp = fn.imp(fn.degree[i],fn.conc[i].equal(x));");
		stream.println("  mu = fn.also(mu,imp);");
		stream.println(" }");
		stream.println(" return mu;");
		stream.println("}");
		stream.println();
	}
	
	//----------------------------------------------------------------------------//
	// Métodos que generan el código de las familias                              //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de una familia de funciones de pertenencia
	 */
	private void createFamilyCode(PrintStream stream, Family fam) throws XflException {
		String classname = usage.getFamilyName(fam);
		String equal = fam.getEqualCCode();
		if(equal==null || equal.length()==0) throw new XflException();

		stream.println("/*======================================================*/");
		stream.println(complete("/*  MembershipFunction "+classname));
		stream.println("/*======================================================*/");
		stream.println();
		stream.println("/*------------------------------------------------------*/");
		stream.println("/* Function to compute an equal relationship            */");
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.print("static double "+classname+"_equal(double x");
		createFamVariableCode(stream, fam);
		stream.println(") {");
		stream.print(equal);
		stream.println("}");
		stream.println();

		if(usage.isFamilyGreqUsed( classname )) {
			String greq = fam.getGreqCCode();
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to compute a greater_or_equal relationship  */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_greq(double x, ");
			createFamVariableCode(stream,fam);
			stream.println(") {");
			if(greq != null && greq.length()>0) {
				stream.print(greq);
			} else {
				stream.println("  double _y, _mu, _degree=0;");
				stream.println("  for(_y = min; _y <= x ; _y += step) {");
				stream.print("    _mu = "+classname+"_equal(_y,");
				createFamVariableCallCode(stream,fam);
				stream.println(");");
				stream.println("    if( _mu > _degree) _degree = _mu;");
				stream.println("  }");
				stream.println(" return _degree;");					
			}
			stream.println("}");
			stream.println();
		}
		
		if(usage.isFamilySmeqUsed( classname )) {
			String smeq = fam.getSmeqCCode();
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to compute a smaller_or_equal relationship  */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_smeq(double x, ");
			createFamVariableCode(stream,fam);
			stream.println(") {");
			if(smeq != null && smeq.length()>0) {
				stream.print(smeq);
			} else {
				stream.println("  double _y, _mu, _degree=0;");
				stream.println("  for(_y = max; _y >= x ; _y -= step) {");
				stream.print(  "    _mu = "+classname+"_equal(_y,");
				createFamVariableCallCode(stream,fam);
				stream.println(");");
				stream.println("    if( _mu > _degree) _degree = _mu;");
				stream.println("  }");
				stream.println(" return _degree;");				
			}
			stream.println("}");
			stream.println();
		}

		if(usage.isFamilyCenterUsed( classname )) {
			String center = fam.getCenterCCode();
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to compute the center of the MF             */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_center(");
			createFamVariableCode(stream,fam);
			stream.println(") {");
			if(center != null && center.length()>0) {
				stream.print(center);
			} else {
				stream.println(" return 0;");
			}
			stream.println("}");
			stream.println();
		}

		if(usage.isFamilyBasisUsed( classname )) {
			String basis = fam.getBasisCCode();
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to compute the basis of the MF              */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_basis(");
			createFamVariableCode(stream,fam);
			stream.println(") {");
			if(basis != null && basis.length()>0) {
				stream.print(basis);
			} else {
				stream.println(" return 0;");
			}
			stream.println("}");
			stream.println();
		}

		if(usage.isFamilyParamUsed( classname )) {
			Parameter[] single = fam.getSingleParameters();
			String listname = fam.getParamListName();		
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to get a parameter of the MF                */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_param(int _i, ");
			createFamVariableCode(stream,fam);
			stream.println(") {");
			for(int i=0; i<single.length; i++) {
				stream.println(" if(_i == "+i+") return "+single[i].getName()+";");
			}
			if(listname != null && listname.length()>0) {
				stream.println(" return "+listname+"[_i-"+single.length+"];");
			} else {
				stream.println(" return 0.0;");
			}
			stream.println("}");
			stream.println();
		}
	}

	/**
	 * Método auxiliar para generar las variables de la función
	 */
	private void createFamVariableCode(PrintStream stream, Family fam) {
		Parameter single[] = fam.getSingleParameters();
		String listname = fam.getParamListName();

		stream.print("int i, double min, double max, double step");
		for(int i=0; i<single.length; i++) {
			stream.print(", double "+single[i].getName());
		}
		if(listname != null && listname.length() > 0) {
			stream.print(", double *"+listname+", int length");
		}
	}

	/**
	 * Método auxiliar para generar las variables de la función
	 */
	private void createFamVariableCallCode(PrintStream stream, Family fam) {
		Parameter single[] = fam.getSingleParameters();
		String listname = fam.getParamListName();

		stream.print(" i, min, max, step");
		for(int i=0; i<single.length; i++) {
			stream.print(", "+single[i].getName());
		}
		if(listname != null && listname.length() > 0) {
			stream.print(", "+listname+", length");
		}
	}
	
	//----------------------------------------------------------------------------//
	// Métodos que generan el código de las M.F.                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un función de pertenencia	
	 */
	private void createMFCode(PrintStream stream,ParamMemFunc mf) throws XflException {
		String classname = usage.getMFname(mf);
		String equal = mf.getEqualCCode();
		if(equal==null || equal.length()==0) throw new XflException();

		stream.println("/*======================================================*/");
		stream.println(complete("/*  MembershipFunction "+classname));
		stream.println("/*======================================================*/");
		stream.println();
		stream.println("/*------------------------------------------------------*/");
		stream.println("/* Function to compute an equal relationship            */");
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.print("static double "+classname+"_equal(double x, ");
		createVariableCode(stream,mf);
		stream.println(") {");
		stream.println(equal);
		stream.println("}");
		stream.println();

		if(usage.isMFGreqUsed( classname )) {
			String greq = mf.getGreqCCode();
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to compute a greater_or_equal relationship  */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_greq(double x, ");
			createVariableCode(stream,mf);
			stream.println(") {");
			if(greq != null && greq.length()>0) {
				stream.println(greq);
			} else {
				stream.println("  double _y, _mu, _degree=0;");
				stream.println("  for(_y = min; _y <= x ; _y += step) {");
				stream.print("    _mu = "+classname+"_equal(_y,");
				createVariableCallCode(stream,mf);
				stream.println(");");
				stream.println("    if( _mu > _degree) _degree = _mu;");
				stream.println("  }");
				stream.println(" return _degree;");				
			}
			stream.println("}");
			stream.println();
		}

		if(usage.isMFSmeqUsed( classname )) {
			String smeq = mf.getSmeqCCode();
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to compute a smaller_or_equal relationship  */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_smeq(double x, ");
			createVariableCode(stream,mf);
			stream.println(") {");
			if(smeq != null && smeq.length()>0) {
				stream.println(smeq);
			} else {
				stream.println("  double _y, _mu, _degree=0;");
				stream.println("  for(_y = max; _y >= x ; _y -= step) {");
				stream.print(  "    _mu = "+classname+"_equal(_y,");
				createVariableCallCode(stream,mf);
				stream.println(");");
				stream.println("    if( _mu > _degree) _degree = _mu;");
				stream.println("  }");
				stream.println(" return _degree;");				
			}
			stream.println("}");
			stream.println();
		}

		if(usage.isMFParamUsed( classname )) {
			Parameter[] single = mf.getSingleParameters();
			String listname = mf.getParamListName();		
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Function to get a parameter of the MF                */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			stream.print("static double "+classname+"_param(int _i, ");
			createVariableCode(stream,mf);
			stream.println(") {");
			for(int i=0; i<single.length; i++) {
				stream.println(" if(_i == "+i+") return "+single[i].getName()+";");
			}
			if(listname != null && listname.length()>0) {
				stream.println(" return "+listname+"[_i-"+single.length+"];");
			} else {
				stream.println(" return 0.0;");
			}
			stream.println("}");
			stream.println();
		}
	}

	/**
	 * Método auxiliar para generar las variables de la función
	 */
	private void createVariableCode(PrintStream stream, ParamMemFunc mf) {
		Parameter single[] = mf.getSingleParameters();
		String listname = mf.getParamListName();

		stream.print("double min, double max, double step");
		for(int i=0; i<single.length; i++) {
			stream.print(", double "+single[i].getName());
		}
		if(listname != null && listname.length() > 0) {
			stream.print(", double *"+listname+", int length");
		}

	}
	
	/**
	 * Método auxiliar para generar las variables de la función
	 */
	private void createVariableCallCode(PrintStream stream, ParamMemFunc mf) {
		Parameter single[] = mf.getSingleParameters();
		String listname = mf.getParamListName();

		stream.print(" min, max, step");
		for(int i=0; i<single.length; i++) {
			stream.print(", "+single[i].getName());
		}
		if(listname != null && listname.length() > 0) {
			stream.print(", "+listname+", length");
		}

	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el código de los conjuntos de operadores               //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un conjunto de operadores
	 */
	private void createOperatorSetCode(PrintStream stream, Operatorset opset) throws XflException {

		boolean[] flags = usage.getOpsetUsages(opset);
		
		String name = opset.getName();

		stream.println("/*======================================================*/");
		stream.println(complete("/*  Operatorset OP_"+name));
		stream.println("/*======================================================*/");
		stream.println();
		
		if(flags[0]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator AND                      */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createBinaryCode(stream,opset.and, "OP_"+name+"_And");
		}
		
		if(flags[1]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator OR                       */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createBinaryCode(stream,opset.or, "OP_"+name+"_Or");
		}
		
		if(flags[2]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator NOT                      */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createUnaryCode(stream,opset.not, "OP_"+name+"_Not");
		}
		
		if(flags[3]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator MORE OR LESS             */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createUnaryCode(stream,opset.moreorless, "OP_"+name+"_MoreOrLess");
		}
		
		if(flags[4]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator SLIGHTLY                 */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createUnaryCode(stream,opset.slightly, "OP_"+name+"_Slightly");
		}
		
		if(flags[5]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator STRONGLY                 */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createUnaryCode(stream,opset.very, "OP_"+name+"_Very");
		}
		
		if(flags[6]) {
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator ALSO                     */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createBinaryCode(stream,opset.also, "OP_"+name+"_Also");
			stream.println("/*------------------------------------------------------*/");
			stream.println("/* Description of the operator IMPLICATION              */");
			stream.println("/*------------------------------------------------------*/");
			stream.println();
			createBinaryCode(stream,opset.imp, "OP_"+name+"_Imp");
		}
				
		stream.println("/*------------------------------------------------------*/");
		stream.println("/* Description of the defuzzification method            */");
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		createDefuzCode(stream,opset.defuz, "OP_"+name+"_Defuz");
		stream.println();
	}

	/**
	 * Descripcion de un operador binario
	 */
	private void createBinaryCode(PrintStream stream, Binary op, String name) throws XflException {
		stream.println("static double "+name+"(double a, double b) {");

		Parameter singleparam[] = op.getSingleParameters();
		for(int i=0; i<singleparam.length; i++) {
			stream.print("   double "+singleparam[i].getName());
			stream.println(" = "+singleparam[i].value+";");
		}

		String listname = op.getParamListName();
		if(listname != null && listname.length() > 0) {
			Parameter paramlist[] = op.getParamList();
			stream.print("   double "+listname+"["+paramlist.length+"] = {");
			for(int i=0; i<paramlist.length; i++) 
				stream.print((i>0? ",":"")+paramlist[i].value);
			stream.println("};");
		}

		stream.println(op.getCCode());
		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de un operador unario
	 */
	private void createUnaryCode(PrintStream stream, Unary op, String name) throws XflException {
		stream.println("static double "+name+"(double a) {");

		Parameter singleparam[] = op.getSingleParameters();
		for(int i=0; i<singleparam.length; i++) {
			stream.print("   double "+singleparam[i].getName());
			stream.println(" = "+singleparam[i].value+";");
		}

		String listname = op.getParamListName();
		if(listname != null && listname.length() > 0) {
			Parameter paramlist[] = op.getParamList();
			stream.print("   double "+listname+"["+paramlist.length+"] = {");
			for(int i=0; i<paramlist.length; i++) 
				stream.print((i>0? ",":"")+paramlist[i].value);
			stream.println("};");
		}

		stream.println(op.getCCode());
		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de un método de concreción
	 */
	private void createDefuzCode(PrintStream stream, DefuzMethod op, String name) {
		String defuzcode = op.getCCode();
		stream.println("static double "+name+"(FuzzyNumber mf) {");

		if(defuzcode.indexOf("min")!=-1)
			stream.println(" double min = mf.min;");
		if(defuzcode.indexOf("max")!=-1)
			stream.println(" double max = mf.max;");
		if(defuzcode.indexOf("step")!=-1)
			stream.println(" double step = mf.step;");

		Parameter singleparam[] = op.getSingleParameters();
		for(int i=0; i<singleparam.length; i++) {
			stream.print("   double "+singleparam[i].getName());
			stream.println(" = "+singleparam[i].value+";");
		}

		String listname = op.getParamListName();
		if(listname != null && listname.length() > 0) {
			Parameter paramlist[] = op.getParamList();
			stream.print("   double "+listname+"["+paramlist.length+"] = {");
			for(int i=0; i<paramlist.length; i++) 
				stream.print((i>0? ",":"")+paramlist[i].value);
			stream.println("};");
		}

		stream.println(defuzcode);
		stream.println("}");
		stream.println();
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el código de los tipos                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un tipo de variable lingüística
	 */
	private void createTypeCode(PrintStream stream, Type type) throws XflException {
		Family[] fam = type.getFamilies();
		LinguisticLabel[] allmf = type.getAllMembershipFunctions();

		stream.println("/*======================================================*/");
		stream.println(complete("/*  Type TP_"+type.getName()));
		stream.println("/*======================================================*/");
		stream.println();

		for(int i=0; i<fam.length; i++) {
			createTypeFamilyCode(stream,type,fam[i]);
		}

		for(int i=0; i<allmf.length; i++) {
			if(allmf[i] instanceof ParamMemFunc) {
				createTypePMFCode(stream, type, (ParamMemFunc) allmf[i]);
			} else {
				createTypeFMFCode(stream, type, (FamiliarMemFunc) allmf[i]);
			}
		}
	}

	/**
	 * Descripción de una familia pertenenciente a un tipo de variable
	 * @param stream
	 * @param type
	 * @param fam
	 */
	private void createTypeFamilyCode(PrintStream stream, Type type, Family fam) {
		String typename = type.getName();
		double min = type.min();
		double max = type.max();
		double step = type.step();
		boolean isUsedAsInput = usage.isUsedAsInput(type);
		boolean isUsedAsOutput = usage.isUsedAsOutput(type);
		
		String famname = usage.getFamilyName(fam);
		Parameter single[] = fam.getSingleParameters();
		Parameter list[] = fam.getParamList();
		String listname = fam.getParamListName();
		if(listname == null || listname.length() == 0) list = null;

		stream.println("/*------------------------------------------------------*/");
		stream.println(complete("/* Description of the family "+fam));
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.println("static double TP_"+typename+"_"+fam+"_equal(double x, int i){");
		createParamListCode(stream,list);
		stream.print("   return "+famname+"_equal(x,i,"+min+","+max+","+step);
		createParamCallCode(stream,single,list);
		stream.println(");");
		stream.println("}");
		stream.println();
		
		if(isUsedAsInput && usage.isFamilyGreqUsed(famname)) {
			stream.println("static double TP_"+typename+"_"+fam+"_greq(double x, int i){");
			createParamListCode(stream,list);
			stream.print("   return "+famname+"_greq(x,i,"+min+","+max+","+step);
			createParamCallCode(stream,single,list);
			stream.println(");");
			stream.println("}");
			stream.println();			
		}

		if(isUsedAsInput && usage.isFamilySmeqUsed(famname)) {
			stream.println("static double TP_"+typename+"_"+fam+"_smeq(double x, int i){");
			createParamListCode(stream,list);
			stream.print("   return "+famname+"_smeq(x,i,"+min+","+max+","+step);
			createParamCallCode(stream,single,list);
			stream.println(");");
			stream.println("}");
			stream.println();			
		}
		
		if(isUsedAsOutput && usage.isFamilyParamUsed(famname)) {
			stream.println("static double TP_"+typename+"_"+fam+"_param(int index, int i){");
			createParamListCode(stream,list);
			stream.print("   return "+famname+"_param(index,i,"+min+","+max+","+step);
			createParamCallCode(stream,single,list);
			stream.println(");");
			stream.println("}");
			stream.println();
		}
	}

	/**
	 * Descripción de las funciones asociadas a una función de pertenencia paramétrica
	 * @param stream
	 * @param type
	 * @param pmf
	 */
	private void createTypePMFCode(PrintStream stream, Type type, ParamMemFunc pmf) {
		String typename = type.getName();
		double min = type.min();
		double max = type.max();
		double step = type.step();
		boolean isUsedAsInput = usage.isUsedAsInput(type);
		boolean isUsedAsOutput = usage.isUsedAsOutput(type);
		
		String mfname = usage.getMFname(pmf);	
		String labelname = "TP_"+typename+"_"+pmf;
		Parameter single[] = pmf.getSingleParameters();
		Parameter list[] = pmf.getParamList();
		String listname = pmf.getParamListName();
		if(listname == null || listname.length() == 0) list = null;

		stream.println("/*------------------------------------------------------*/");
		stream.println(complete("/* Description of the label "+pmf));
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.println("static double "+labelname+"_equal(double x){");
		createParamListCode(stream,list);
		stream.print("   return "+mfname+"_equal(x,"+min+","+max+","+step);
		createParamCallCode(stream,single,list);
		stream.println(");");
		stream.println("}");
		stream.println();
		
		if(isUsedAsInput && usage.isMFGreqUsed(mfname)) {
			stream.println("static double "+labelname+"_greq(double x){");
			createParamListCode(stream,list);
			stream.print("   return "+mfname+"_greq(x,"+min+","+max+","+step);
			createParamCallCode(stream,single,list);
			stream.println(");");
			stream.println("}");
			stream.println();			
		}

		if(isUsedAsInput && usage.isMFSmeqUsed(mfname)) {
			stream.println("static double "+labelname+"_smeq(double x){");
			createParamListCode(stream,list);
			stream.print("   return "+mfname+"_smeq(x,"+min+","+max+","+step);
			createParamCallCode(stream,single,list);
			stream.println(");");
			stream.println("}");
			stream.println();			
		}
		
		if(isUsedAsOutput && usage.isMFCenterUsed(mfname)) {
			double center = pmf.center();
			stream.println("static double "+labelname+"_center(){");
			stream.println("   return "+center+";");
			stream.println("}");
			stream.println();			
		} 
		
		if(isUsedAsOutput && usage.isMFBasisUsed(mfname)) {
			double basis = pmf.basis();
			stream.println("static double "+labelname+"_basis(){");
			stream.println("   return "+basis+";");
			stream.println("}");
			stream.println();			
		} 

		if(isUsedAsOutput && usage.isMFParamUsed(mfname)) {
			stream.println("static double "+labelname+"_param(int index){");
			createParamListCode(stream,list);
			stream.print("   return "+mfname+"_param(index,"+min+","+max+","+step);
			createParamCallCode(stream,single,list);
			stream.println(");");
			stream.println("}");
			stream.println();
		}
	}

	/**
	 * Descripción de las funciones asociadas a una función de pertenencia familiar
	 * @param stream
	 * @param type
	 * @param fmf
	 */
	private void createTypeFMFCode(PrintStream stream, Type type, FamiliarMemFunc fmf) {
		String typename = type.getName();
		Family fam = fmf.getFamily();
		int index = fmf.getIndex();
		boolean isUsedAsInput = usage.isUsedAsInput(type);
		boolean isUsedAsOutput = usage.isUsedAsOutput(type);
		
		String famname = "TP_"+typename+"_"+fam;
		String labelname = "TP_"+typename+"_"+fmf;
		String classname = usage.getFamilyName(fam);

		stream.println("/*------------------------------------------------------*/");
		stream.println(complete("/* Description of the label "+fmf));
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.println("static double "+labelname+"_equal(double x){");
		stream.print("   return "+famname+"_equal(x,"+index+");");
		stream.println("}");
		stream.println();
		
		if(isUsedAsInput && usage.isFamilyGreqUsed(classname)) {
			stream.println("static double "+labelname+"_greq(double x){");
			stream.println("   return "+famname+"_greq(x,"+index+");");
			stream.println("}");
			stream.println();			
		} 

		if(isUsedAsInput && usage.isFamilySmeqUsed(classname)) {
			stream.println("static double "+labelname+"_smeq(double x){");
			stream.println("   return "+famname+"_smeq(x,"+index+");");
			stream.println("}");
			stream.println();			
		}
		
		if(isUsedAsOutput && usage.isFamilyCenterUsed(classname)) {
			double center = fmf.center();
			stream.println("static double "+labelname+"_center(){");
			stream.println("   return "+center+";");
			stream.println("}");
			stream.println();			
		} 
		
		if(isUsedAsOutput && usage.isFamilyBasisUsed(classname)) {
			double basis = fmf.basis();
			stream.println("static double "+labelname+"_basis(){");
			stream.println("   return "+basis+";");
			stream.println("}");
			stream.println();			
		} 
		
		if(isUsedAsOutput && usage.isFamilyParamUsed(classname)) {
			stream.println("static double "+labelname+"_param(int index){");
			stream.println("   return "+famname+"_param(index,"+index+");");
			stream.println("}");
			stream.println();
		}
	}
	
	/**
	 * Crea la declaración de la lista de parámetros
	 * @param stream
	 * @param list
	 */
	private void createParamListCode(PrintStream stream, Parameter[] list) {
		if(list == null) return;
		stream.print("   double list["+list.length+"] = {");
		for(int i=0; i<list.length; i++) stream.print((i==0? "":",")+list[i].value);
		stream.println("};");
	}
	
	/**
	 * Escribe los valores de los parámetros en una llamada
	 * @param stream
	 * @param single
	 * @param list
	 */
	private void createParamCallCode(PrintStream stream, Parameter[] single, Parameter[] list) {
		for(int i=0; i<single.length; i++) {
			stream.print(","+single[i].value);
		}
		if(list != null) {
			stream.print(",list,"+list.length);
		}
	}
	
	//----------------------------------------------------------------------------//
	// Métodos que generan el código de las bases de reglas                       //
	//----------------------------------------------------------------------------//

	/**
	 * Descripcion de una base de reglas
	 */
	private void createRulebaseCode(PrintStream stream, Rulebase base) throws XflException {
		String rbname = base.getName();
		Operatorset opset = base.getOperatorset();
		boolean center = usage.isCenterUsed(opset.defuz);
		boolean basis = usage.isBasisUsed(opset.defuz);
		boolean param = usage.isParamUsed(opset.defuz);
		boolean input = usage.isInputUsed(opset.defuz);
		String opsetname = "OP_"+opset.getName()+"_";
		Variable[] inputvar = base.getInputs();
		Variable[] outputvar = base.getOutputs();
		Rule[] rule = base.getRules();

		stream.println("/*======================================================*/");
		stream.println(complete("/*  Rulebase RL_"+rbname));
		stream.println("/*======================================================*/");
		stream.println();
		stream.print("static void RL_"+rbname+"(");
		for(int i=0; i<inputvar.length; i++)
			stream.print((i==0? "": ", ")+"double "+inputvar[i]);
		for(int i=0; i<outputvar.length; i++)
			stream.print(", double *"+outputvar[i]);
		stream.println(") {");
		stream.println(" double _rl;");
		stream.println();
		
		if(input) {
			stream.println(" double _input["+inputvar.length+"];");
			for(int i=0; i<inputvar.length; i++) {
				stream.println(" _input["+i+"] = "+inputvar[i]+";");
			}
			stream.println();
		}
		
		for(int i=0; i<outputvar.length; i++) {
			createRulebaseOutputInitialization(stream,base,outputvar[i]);
		}

		for(int i=0; i<inputvar.length; i++) {
			createRulebaseInputInitialization(stream,base,inputvar[i]);
		}
		
		for(int i=0; i<rule.length; i++) {
			createRuleCode(stream,rule[i],opsetname,center,basis,param);
		}

		for(int i=0; i<outputvar.length; i++) {
			stream.print(" *"+outputvar[i]+" = "+opsetname+"Defuz(");
			stream.println("_"+outputvar[i]+");");
		}
		
		stream.println("}");
		stream.println();
	}

	/**
	 * Código de inicialización de la salida de una base de reglas
	 * @param stream
	 * @param base
	 * @param output
	 */
	private void createRulebaseOutputInitialization(PrintStream stream, Rulebase base, Variable output){
		Operatorset opset = base.getOperatorset();
		boolean imp_also = usage.isComputeUsed(opset.defuz);
		boolean input = usage.isInputUsed(opset.defuz);
		String opsetname = "OP_"+opset.getName()+"_";
		int inputlength = base.getInputs().length;

		/* Contar el número de conclusiones */
		int outputcount = getNumberOfConsequents(base,output);
		double min = output.getType().min();
		double max = output.getType().max();
		double step = output.getType().step();
		stream.println(" double _"+output+"_degree["+outputcount+"];");
		stream.println(" Consequent _"+output+"_conc["+outputcount+"];");
		stream.println(" FuzzyNumber _"+output+";");
		stream.println(" _"+output+".min = "+min+";");
		stream.println(" _"+output+".max = "+max+";");
		stream.println(" _"+output+".step = "+step+";");
		if(imp_also) {
			stream.println(" _"+output+".imp = "+opsetname+"Imp;");
			stream.println(" _"+output+".also = "+opsetname+"Also;");
		}
		if(input) {
			stream.println(" _"+output+".inputlength = "+inputlength+";");
			stream.println(" _"+output+".input = _input;");
		}
		stream.println(" _"+output+".length = "+outputcount+";");
		stream.println(" _"+output+".degree = _"+output+"_degree;");
		stream.println(" _"+output+".conc = _"+output+"_conc;");			
		stream.println(" int _"+output+"_i = 0;");
		stream.println();
	}

	/**
	 * Cuenta el número de veces que una variable de salida aparece en el
	 * consecuente de una base de reglas
	 * @param base
	 * @param output
	 * @return
	 */
	private int getNumberOfConsequents(Rulebase base, Variable output) {
		Rule[] rule = base.getRules();
		int count =  0;
		for(int i=0; i<rule.length; i++) {
			Conclusion[] conc = rule[i].getConclusions();
			for(int j=0; j<conc.length; j++) {
				if(conc[j].getVariable() == output) count++;
			}
		}
		return count;
	}
	
	/**
	 * Genera el código de inicialización de una variable de entrada de una
	 * base de reglas.
	 * 
	 * @param stream
	 * @param base
	 * @param input
	 */
	private void createRulebaseInputInitialization(PrintStream stream, Rulebase base, Variable input) {
		LinguisticLabel[] label = input.getType().getAllMembershipFunctions();
		boolean[] lbGreqUsed = new boolean[label.length];
		boolean[] lbSmeqUsed = new boolean[label.length];
		boolean globalGreqUsed = false;
		boolean globalSmeqUsed = false;
		for(int i=0; i<label.length; i++) {
			if(label[i] instanceof ParamMemFunc) {
				String mfname = usage.getMFname( (ParamMemFunc) label[i]);
				if(usage.isMFGreqUsed(mfname)) { lbGreqUsed[i] = true; globalGreqUsed = true; }
				if(usage.isMFSmeqUsed(mfname)) { lbSmeqUsed[i] = true; globalSmeqUsed = true; }
			} else {
				FamiliarMemFunc fmf = (FamiliarMemFunc) label[i];
				String famname = usage.getFamilyName(fmf.getFamily());
				if(usage.isFamilyGreqUsed(famname)) { lbGreqUsed[i] = true; globalGreqUsed = true; }
				if(usage.isFamilySmeqUsed(famname)) { lbSmeqUsed[i] = true; globalSmeqUsed = true; }
			}
		}
		
		stream.println(" double _"+input+"_eq["+label.length+"];");
		if(globalGreqUsed) stream.println(" double _"+input+"_greq["+label.length+"];");
		if(globalSmeqUsed) stream.println(" double _"+input+"_smeq["+label.length+"];");
		
		for(int i=0; i<label.length; i++) {
			String lbname = "TP_"+input.getType().getName()+"_"+label[i].getLabel();
			stream.print(" _"+input+"_eq["+i+"] = ");
			stream.println(lbname+"_equal("+input+");");
			if(lbGreqUsed[i]) {
				stream.print(" _"+input+"_greq["+i+"] = ");
				stream.println(lbname+"_greq("+input+");");										
			}
			if(lbSmeqUsed[i]) {
				stream.print(" _"+input+"_smeq["+i+"] = ");
				stream.println(lbname+"_smeq("+input+");");										
			}
		}
		stream.println();
	}
	
	/**
	 * Descripción de una regla
	 */
	private void createRuleCode(PrintStream stream, Rule rule, String op, boolean center, boolean basis, boolean param) throws XflException {
		double degree = rule.getDegree();
		Relation premise = rule.getPremise();
		Conclusion[] conc = rule.getConclusions();

		stream.print(" _rl = ");
		stream.println(createRelationCode(premise, op)+";");
		if(degree != 1.0) stream.println(" _rl = "+degree+" * _rl;");
		for(int j=0; j<conc.length; j++) createConclusionCode(stream,conc[j],center,basis,param);
		stream.println();
	}

	/**
	 * Descripción de una conclusión de una regla
	 */
	private void createConclusionCode(PrintStream stream, Conclusion conc, boolean center, boolean basis, boolean param) 
	throws XflException {
		Variable var = conc.getVariable();
		LinguisticLabel mf = conc.getMembershipFunction();
		String mfname = "TP_"+var.getType().getName()+"_"+mf.getLabel()+"_";

		stream.println(" _"+var+"_degree[_"+var+"_i] = _rl;");
		stream.println(" _"+var+"_conc[_"+var+"_i].equal = "+mfname+"equal;");
		if(center) stream.println(" _"+var+"_conc[_"+var+"_i].center = "+mfname+"center;");
		if(basis) stream.println(" _"+var+"_conc[_"+var+"_i].basis = "+mfname+"basis;");
		if(param) stream.println(" _"+var+"_conc[_"+var+"_i].param = "+mfname+"param;");
		stream.println(" _"+var+"_i++;");
	}

	/**
	 * Descripción de una proposición de una regla
	 */
	private String createRelationCode(Relation rel, String op) throws XflException {
		Variable var = rel.getVariable();
		LinguisticLabel mf = rel.getMembershipFunction();
		Relation l = rel.getLeftRelation();
		Relation r = rel.getRightRelation();
		int index = 0;
		if(mf != null) {
			LinguisticLabel[] lb = var.getType().getAllMembershipFunctions();
			for(int i=0; i<lb.length; i++) if(lb[i] == mf) index = i;
		}
	
		switch(rel.getKind()) {
		case Relation.AND:
			return op+"And("+createRelationCode(l,op)+","+createRelationCode(r,op)+")";
		case Relation.OR:
			return op+"Or("+createRelationCode(l,op)+","+createRelationCode(r,op)+")";
		case Relation.IS:
			return "_"+var+"_eq["+index+"]";
		case Relation.ISNOT:
			return op+"Not( _"+var+"_eq["+index+"] )";
		case Relation.GR_EQ:
			return "_"+var+"_greq["+index+"]";
		case Relation.SM_EQ:
			return "_"+var+"_smeq["+index+"]";
		case Relation.GREATER:
			return op+"Not( _"+var+"_smeq["+index+"] )";
		case Relation.SMALLER:
			return op+"Not( _"+var+"_greq["+var+"] )";
		case Relation.APP_EQ:
			return op+"MoreOrLess( _"+var+"_eq["+index+"] )";
		case Relation.VERY_EQ:
			return op+"Very( _"+var+"_eq["+index+"] )";
		case Relation.SL_EQ:
			return op+"Slightly( _"+var+"_eq["+index+"] )";
		case Relation.NOT:
			return op+"Not("+createRelationCode(r,op)+")";
		case Relation.MoL:
			return op+"MoreOrLess("+createRelationCode(r,op)+")";
		case Relation.SLIGHTLY:
			return op+"Slightly("+createRelationCode(r,op)+")";
		case Relation.VERY:
			return op+"Very("+createRelationCode(r,op)+")";
		default: return "";
		}
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el código de un bloque no difuso                       //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de un bloque no difuso
	 */
	private void createCrispBlockCode(PrintStream stream, CrispBlock crisp) throws XflException {
		String name = crisp.getName();
		int inputs = crisp.inputs();

		stream.println("/*======================================================*/");
		stream.println(complete("/*  CrispBlock CB_"+name));
		stream.println("/*======================================================*/");
		stream.println();
		stream.print("static double CB_"+name+"(");
		for(int i=0; i<inputs; i++) stream.print((i==0? "":", ")+"double _i"+i);
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
		for(int i=0; i<inputs; i++) stream.println(" x["+i+"] = _i"+i+";");

		stream.println(crisp.getCCode());
		stream.println("}");
		stream.println();
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el código de ejecución del sistema                     //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de la estructura jerárquica del sistema
	 */
	private void createSystemModuleCode(PrintStream stream) throws XflException {
		SystemModule system = spec.getSystemModule();
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();
		Variable inner[] = system.getInners();
		ModuleCall call[] = system.getModuleCalls();
		String name = spec.getName()+"InferenceEngine";

		stream.println();
		stream.println("/*======================================================*/");
		stream.println("/*                   Inference Engine                   */");
		stream.println("/*======================================================*/");
		stream.println();
		stream.print("void "+name+"(");
		for(int i=0; i<input.length; i++)
			stream.print((i==0? "" : ", ")+"double "+input[i]);
		for(int i=0; i<output.length; i++)
			stream.print(", double *_d_"+output[i]);
		stream.println(") {");
		
		for(int i=0; i<inner.length; i++) {
			if(!inner[i].equals("NULL")) stream.println(" double "+inner[i]+";");
		}
		for(int i=0; i<output.length; i++) stream.println(" double "+output[i]+";");

		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			createRulebaseCallCode(stream, (RulebaseCall) call[i]);
		} else createCrispBlockCallCode( stream, (CrispBlockCall) call[i]);

		for(int i=0; i<output.length; i++) {
			stream.println(" *_d_"+output[i]+" = "+output[i]+";");
		}

		stream.println("}");
		stream.println();
	}

	/**
	 * Descripción de una llamada a una base de reglas
	 */
	private void createRulebaseCallCode(PrintStream stream, RulebaseCall call) throws XflException {
		Rulebase base = call.getRulebase();
		Variable[] inputvar = call.getInputVariables();
		Variable[] outputvar = call.getOutputVariables();

		stream.print(" RL_"+base+"(");
		for(int i=0; i < inputvar.length; i++) stream.print((i==0? "" : ", ")+inputvar[i]);
		for(int i=0; i < outputvar.length; i++) stream.print(", &"+outputvar[i]);
		stream.println(");");
	}

	/**
	 * Descripción de una llamada a un bloque no difuso
	 */
	private void createCrispBlockCallCode(PrintStream stream, CrispBlockCall call)
	throws XflException {
		Variable[] inputvar = call.getInputVariables();
		Variable outputvar = call.getOutputVariable();

		stream.print(" "+outputvar+" = CB_"+call.getName()+"(");
		for(int j=0; j < inputvar.length; j++) stream.print((j==0? "":",")+inputvar[j]);
		stream.println(");");
	}

	//----------------------------------------------------------------------------//
	// Métodos auxiliares                                                         //
	//----------------------------------------------------------------------------//

	/**
	 * Completa la cadena hasta 58 caracteres
	 */
	private String complete(String begining) {
		String line = begining;
		for(int i=begining.length(); i<56; i++) line += " ";
		line += "*/";
		return line;
	}
	

}
