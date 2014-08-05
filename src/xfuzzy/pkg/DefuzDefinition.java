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


package xfuzzy.pkg;

import java.util.*;

/**
 * Clase que contiene la definición de un método de concreción
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class DefuzDefinition extends Definition {

	//----------------------------------------------------------------------------//
	//                           MIEMBROS PRIVADOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de funciones de pertenencia que admite el método
	 */
	private Vector defined = new Vector();
	
	/**
	 * Descripción de la función en Java
	 */
	private String compute;
	
	/**
	 * Descripción de la función en C
	 */
	private String c_compute;
	
	/**
	 * Descripción de la función en C++
	 */
	private String cpp_compute;
	
	/**
	 * Descripción de la derivada de la función 
	 */
	private String deriv;

	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 * @param pkg Nombre del paquete al que pertenece
	 * @param name Nombre de la función
	 */
	public DefuzDefinition(String pkg, String name) {
		super(pkg, name);
	}

	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el tipo de definición
	 */
	public int getKind() {
		return PackageDefinition.DEFUZ;
	}

	/**
	 * Obtiene el código XFL3 de un bloque
	 */
	public String getCode(int kind) {
		switch(kind) {
		case REQUIREMENTS: return super.requires;
		case SOURCE: return super.other;
		case JAVA_EQUAL: return this.compute;
		case C_EQUAL: return this.c_compute;
		case CPP_EQUAL: return this.cpp_compute;
		case DERIV_EQUAL: return this.deriv;
		case UPDATE: return super.update;
		default: return null;
		}
	}

	/**
	 * Asigna el código XFL3 de un bloque
	 */
	public void setCode(int kind, String code) {
		switch(kind) {
		case REQUIREMENTS: super.requires = code; break;
		case SOURCE: super.other = code; break;
		case JAVA_EQUAL: this.compute = code; break;
		case C_EQUAL: this.c_compute = code; break;
		case CPP_EQUAL: this.cpp_compute = code; break;
		case DERIV_EQUAL: this.deriv = code; break;
		case UPDATE: this.update = code; break;
		}
	}

	/**
	 * Obtiene la descripción XFL3 de la definición
	 */
	public String toPkg() {
		String code = "defuz "+name+" {"+eol;
		code += super.aliasblock();
		code += super.paramblock();
		code += super.requiresblock();
		if(this.compute != null && this.compute.length() >0) 
			code += " java {"+eol+this.compute+eol+"  }"+eol;
		if(this.c_compute != null && this.c_compute.length() >0) 
			code += " ansi_c {"+eol+this.c_compute+eol+"  }"+eol;
		if(this.cpp_compute != null && this.cpp_compute.length() >0)
			code += " cplusplus {"+eol+this.cpp_compute+eol+"  }"+eol;
		if(this.deriv != null && this.deriv.length() >0)
			code += " derivative {"+eol+this.deriv+eol+"  }"+eol;
		code += super.updateblock();
		code += super.otherblock();
		code += " }"+eol+eol;
		return code;
	}

	/**
	 * Asigna las funciones admitidas para el método
	 */
	public void setDefinedFor(Vector defined) {
		this.defined = defined;
	}

	/**
	 * Obtiene las funciones admitidas para el método
	 */
	public Vector getDefinedFor() { 
		return this.defined;
	}

	//----------------------------------------------------------------------------//
	//                            MÉTODOS PRIVADOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Genera el código de la clase
	 */
	protected String class_code() {
		return "public class "+name+" extends DefuzMethod {"+eol;
	}

	/**
	 * Genera el código del método "test"
	 */
	protected String defined_code() {
		String code = eol+" public boolean test(AggregateMemFunc mf) {"+eol;
		if(defined.size() == 0) code += "   return true;"+eol;
		else {
			code += "   for(int i=0; i<mf.conc.length; i++) {"+eol;
			code += "     LinguisticLabel pmf = mf.conc[i].getMF();"+eol;
			code += "     if(!(pmf instanceof ";
			code += toInstanceName(defined.elementAt(0))+")"+eol;
			for(int i=1; i<defined.size(); i++) {
				code += "       && !(pmf instanceof ";
				code += toInstanceName(defined.elementAt(i))+")"+eol;
			}
			code += "       ) return false;"+eol;
			code += "    }"+eol;
			code += "   return true;"+eol;
		}
		code += "  }"+eol;
		return code;
	}

	/**
	 * Genera el código de ejecución de la clase
	 */
	protected String compute_code() {
		if(this.compute == null) return "";
		String code = eol+" public double compute(AggregateMemFunc mf) {"+eol;
		code += universe_code(this.compute);
		code += variable_code(this.compute);
		code += this.compute+eol;
		code += "  }"+eol;
		return code;
	}

	/**
	 * Genera el código del método "getJavaCode" de la clase
	 */
	protected String java_code() {
		String code = eol+" public String getJavaCode() {"+eol;
		code += "   String eol = System.getProperty";
		code += "(\"line.separator\", \"\\n\");"+eol;
		code += "   String code = \"\";"+eol;

		String sampled[] = super.sample(compute);
		for(int i=0; i<sampled.length; i++)
			if(sampled[i].trim().length() > 0)
				code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
		code += "   return code;"+eol;
		code += "  }"+eol;
		return code;
	}

	/**
	 * Genera el código del método "getCCode" de la clase
	 */
	protected String c_code() {
		if(c_compute == null || c_compute.length() == 0 ) return super.c_code();
		String code = eol+" public String getCCode() {"+eol;
		code += "   String eol = System.getProperty";
		code += "(\"line.separator\", \"\\n\");"+eol;
		code += "   String code = \"\";"+eol;

		String sampled[] = super.sample(c_compute);
		for(int i=0; i<sampled.length; i++)
			if(sampled[i].trim().length() > 0)
				code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
		code += "   return code;"+eol;
		code += "  }"+eol;
		return code;
	}

	/**
	 * Genera el código del método "getCppCode" de la clase
	 */
	protected String cpp_code() {
		if(cpp_compute==null || cpp_compute.length()==0) return super.cpp_code();
		String code = eol+" public String getCppCode() {"+eol;
		code += "   String eol = System.getProperty";
		code += "(\"line.separator\", \"\\n\");"+eol;
		code += "   String code = \"\";"+eol;

		String sampled[] = super.sample(cpp_compute);
		for(int i=0; i<sampled.length; i++)
			if(sampled[i].trim().length() > 0)
				code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
		code += "   return code;"+eol;
		code += "  }"+eol;
		return code;
	}

	/**
	 * Nombre de la clase que referencia una MF
	 */
	private String toInstanceName(Object ob) {
		String name = (String) ob;
		int index = name.indexOf(".");
		if(index==-1) return "pkg."+super.pkg+".mfunc."+name;
		return "pkg."+name.substring(0,index)+".mfunc."+name.substring(index+1);
	}

	/**
	 * Genera el código para utilizar los valores del universo
	 */
	private String universe_code(String source) {
		String code = "";
		if(source.indexOf("min")!=-1) code += "   double min = mf.min();"+eol;
		if(source.indexOf("max")!=-1) code += "   double max = mf.max();"+eol;
		if(source.indexOf("step")!=-1) code += "   double step = mf.step();"+eol;
		return code;
	}
}
