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
 * Generador del fichero "fuzzysystem.hpp"
 * 
 * @author Francisco José Moreno Velo 
 *
 */
public class XfcppSpecHppCode {

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
	public XfcppSpecHppCode(Specification spec, File dir) {
		this.spec = spec;
		this.dir = dir;
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el archivo ".hpp" correspondiente a la especificación en el
	 * directorio indicado
	 */
	public static final String create(Specification spec, File dir)
	throws XflException {
		XfcppSpecHppCode creator = new XfcppSpecHppCode(spec, dir);
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
		File file = new File(dir,spec.getName()+".hpp");
		return file.getAbsolutePath();
	}

	/**
	 * Genera el fichero ".hpp"
	 */
	public void createFile() throws XflException {
		File file = new File(dir,spec.getName()+".hpp");

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
	 * Genera el código de cabecera del fichero
	 */
	private void printHeading(PrintStream stream) {
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println("//                                                      //");
		stream.println(complete("// File:   "+spec.getName()+".hpp"));
		stream.println("//                                                      //");
		stream.println("// Author: Automatically generated by Xfuzzy            //");
		stream.println("//                                                      //");
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
	}

	/**
	 * Genera las estructuras de las clases del sistema
	 */
	private void printSource(PrintStream stream) throws XflException {
		Operatorset[] opset = spec.getOperatorsets();
		Type[] type = spec.getTypes();
		String name = spec.getName();

		stream.println("#ifndef _"+name+"_INFERENCE_ENGINE_HPP");
		stream.println("#define _"+name+"_INFERENCE_ENGINE_HPP");
		stream.println();
		stream.println("#include \"xfuzzy.hpp\"");
		stream.println();

		Vector famv = searchFamilyClasses();
		for(int i=0; i<famv.size(); i++) {
			printFamilyHppCode(stream,(Family) famv.elementAt(i));
		}

		Vector mfv = searchMFClasses();
		for(int i=0; i<mfv.size(); i++) {
			printMFHppCode(stream,(ParamMemFunc) mfv.elementAt(i));
		}

		for(int i=0; i<opset.length; i++) printOperatorSetHppCode(stream,opset[i]);
		for(int i=0; i<type.length; i++) printTypeHppCode(stream,type[i]);
		printInferenceEngineHppCode(stream);
		
		stream.println("#endif /* _"+name+"_INFERENCE_ENGINE_HPP */");
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el fichero de cabecera                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Contenido de cabecera de las familias de funciones
	 */
	private void printFamilyHppCode(PrintStream stream, Family fam) throws XflException {
		String spname = spec.getName();
		String pkgname = fam.getPackageName();
		String classname = "FMF_"+spname+"_"+pkgname+"_"+fam.getFunctionName();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  MembershipFunction "+classname));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("class "+classname+": public ParamMembershipFunction {");
		stream.println("private:");
		stream.println(" int i;");
		Parameter[] single = fam.getSingleParameters();
		for(int i=0; i<single.length; i++) {
			stream.println(" double "+single[i].getName()+";");
		}
		String listname = fam.getParamListName();
		if(listname != null && listname.length() > 0) {
			stream.println(" double *"+listname+";");
			stream.println(" int length;");
		}
		stream.println();

		stream.println("public:");
		stream.println(" "+classname+"() {};");
		if(listname != null && listname.length() > 0) {
			stream.println(" virtual ~"+classname+"() { delete "+listname+"; };");
		} else {
			stream.println(" virtual ~"+classname+"() {};");
		}
		stream.print(" "+classname+"(int index,double min,double max,double step");
		stream.println(",double *single,double *list,int length);");
		stream.println(" "+classname+"*dup();");
		stream.println(" virtual double param(int _i);");
		stream.println(" virtual double compute_eq(double x);");
		String greq = fam.getGreqCppCode();
		if(greq != null && greq.length()>0)
			stream.println(" virtual double compute_greq(double x);");
		String smeq = fam.getSmeqCppCode();
		if(smeq != null && smeq.length()>0)
			stream.println(" virtual double compute_smeq(double x);");
		String center = fam.getCenterCppCode();
		if(center != null && center.length()>0)
			stream.println(" virtual double center();");
		String basis = fam.getBasisCppCode();
		if(basis != null && basis.length()>0)
			stream.println(" virtual double basis();");
		stream.println("};");
		stream.println();
	}

	/**
	 * Contenido de cabecera de las funciones de pertenencia
	 */
	private void printMFHppCode(PrintStream stream, ParamMemFunc mf) throws XflException {
		String spname = spec.getName();
		String pkgname = mf.getPackageName();
		String classname = "MF_"+spname+"_"+pkgname+"_"+mf.getFunctionName();
		
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  MembershipFunction "+classname));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("class "+classname+": public ParamMembershipFunction {");
		stream.println("private:");

		Parameter[] single = mf.getSingleParameters();
		for(int i=0; i<single.length; i++) {
			stream.println(" double "+single[i].getName()+";");
		}
		String listname = mf.getParamListName();
		if(listname != null && listname.length() > 0) {
			stream.println(" double *"+listname+";");
			stream.println(" int length;");
		}
		stream.println();

		stream.println("public:");
		stream.println(" "+classname+"() {};");
		if(listname != null && listname.length() > 0) {
			stream.println(" virtual ~"+classname+"() { delete "+listname+"; };");
		} else {
			stream.println(" virtual ~"+classname+"() {};");
		}
		stream.print(" "+classname+"(double min,double max,double step");
		stream.println(",double *single,double *list,int length);");
		stream.println(" "+classname+"*dup();");
		stream.println(" virtual double param(int _i);");
		stream.println(" virtual double compute_eq(double x);");
		String greq = mf.getGreqCppCode();
		if(greq != null && greq.length()>0)
			stream.println(" virtual double compute_greq(double x);");
		String smeq = mf.getSmeqCppCode();
		if(smeq != null && smeq.length()>0)
			stream.println(" virtual double compute_smeq(double x);");
		String center = mf.getCenterCppCode();
		if(center != null && center.length()>0)
			stream.println(" virtual double center();");
		String basis = mf.getBasisCppCode();
		if(basis != null && basis.length()>0)
			stream.println(" virtual double basis();");
		stream.println("};");
		stream.println();
	}

	/**
	 * Contenido de cabecera de los conjuntos de operadores
	 */
	private void printOperatorSetHppCode(PrintStream stream, Operatorset opset) throws XflException{
		String opname = "OP_"+spec.getName()+"_"+opset.getName();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  Operator set "+opname));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("class "+opname+": public Operatorset {");
		stream.println("public:");
		stream.println(" virtual ~"+opname+"() {};");
		stream.println(" virtual double _and(double a, double b);");
		stream.println(" virtual double _or(double a, double b);");
		stream.println(" virtual double _also(double a, double b);");
		stream.println(" virtual double _imp(double a, double b);");
		stream.println(" virtual double _not(double a);");
		stream.println(" virtual double _very(double a);");
		stream.println(" virtual double _moreorless(double a);");
		stream.println(" virtual double _slightly(double a);");
		stream.println(" virtual double _defuz(OutputMembershipFunction &mf);");
		stream.println("};");
		stream.println();
	}

	/**
	 * Contenido de cabecera de los tipos de variable
	 */
	private void printTypeHppCode(PrintStream stream, Type type) throws XflException {
		String specname = spec.getName();
		String typename = "TP_"+specname+"_"+type.getName();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  Type "+typename));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("class "+typename+" {");
		stream.println("private:");
		stream.println(" double min;");
		stream.println(" double max;");
		stream.println(" double step;");
		stream.println("public:");
		LinguisticLabel[] allmf = type.getAllMembershipFunctions();

		for(int i=0; i<allmf.length; i++) {
			if(allmf[i] instanceof ParamMemFunc) {
				ParamMemFunc pmf = (ParamMemFunc) allmf[i];
				String pkgname = pmf.getPackageName();
				String mfname = "MF_"+specname+"_"+pkgname+"_"+pmf.getFunctionName();
				stream.println(" "+mfname+" "+pmf.getLabel()+";");
			} else {
				FamiliarMemFunc fmf = (FamiliarMemFunc) allmf[i];
				Family fammf = fmf.getFamily();
				String pkgname = fammf.getPackageName();
				String mfname = "FMF_"+specname+"_"+pkgname+"_"+fammf.getFunctionName();
				stream.println(" "+mfname+" "+fmf.getLabel()+";");
			}

		}
		stream.println(" "+typename+"();");
		stream.println("};");
		stream.println();
	}

	/**
	 * Genera la estructura de la clase "sistema"
	 */
	private void printInferenceEngineHppCode(PrintStream stream) throws XflException {
		Rulebase[] base = spec.getRulebases();
		CrispBlock[] crisp = spec.getCrispBlockSet().getBlocks();
		Variable input[] = spec.getSystemModule().getInputs();
		Variable output[] = spec.getSystemModule().getOutputs();
		String name = spec.getName();

		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println(complete("//  Fuzzy Inference Engine "+name));
		stream.println("//++++++++++++++++++++++++++++++++++++++++++++++++++++++//");
		stream.println();
		stream.println("class "+name+": public FuzzyInferenceEngine {");
		stream.println("public:");
		stream.println(" "+name+"() {};");
		stream.println(" virtual ~"+name+"() {};");
		stream.println(" virtual double* crispInference(double* input);");
		stream.println(" virtual double* crispInference(MembershipFunction* &input);");
		stream.println(" virtual MembershipFunction** fuzzyInference(double* input);");
		stream.print(" virtual MembershipFunction** fuzzyInference");
		stream.println("(MembershipFunction* &input);");
		stream.print(" void inference(");
		for(int i=0; i<input.length; i++) stream.print((i!=0?",":"")+" double _i_"+input[i]);
		for(int i=0; i<output.length; i++) stream.print(", double *_o_"+output[i]);
		stream.println(" );");
		stream.println("private:");
		for(int i=0; i<base.length; i++) printRulebaseHppCode(stream,base[i]);
		for(int i=0; i<crisp.length; i++) printCrispBlockHppCode(stream,crisp[i]);
		stream.println("};");
		stream.println();
	}

	/**
	 * Contenido de cabecera de las bases de reglas
	 */
	private void printRulebaseHppCode(PrintStream stream, Rulebase base) throws XflException {
		String rbname = "RL_"+base.getName();
		Variable[] inputvar = base.getInputs();
		Variable[] outputvar = base.getOutputs();

		stream.print(" void "+rbname+"(");
		for(int i=0; i<inputvar.length; i++)
			stream.print((i==0? "": ", ")+"MembershipFunction &"+inputvar[i]);
		for(int i=0; i<outputvar.length; i++)
			stream.print(", MembershipFunction ** _o_"+outputvar[i]);
		stream.println(");");
	}

	/**
	 * Contenido de cabecera de los bloques no difusos
	 */
	private void printCrispBlockHppCode(PrintStream stream, CrispBlock crisp) throws XflException {

		stream.print(" double CB_"+crisp.getName()+"(");
		for(int i=0; i<crisp.inputs(); i++)
			stream.print((i==0? "": ", ")+"MembershipFunction &i"+i);
		stream.println(");");
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
