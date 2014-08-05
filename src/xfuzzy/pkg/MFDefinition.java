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

/**
 * Clase que contiene la definición de una función de pertenencia
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class MFDefinition extends Definition {

	//----------------------------------------------------------------------------//
	//                           MIEMBROS PRIVADOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Definición de la función en Java
	 */
	private String java_equal;
	
	/**
	 * Definición del modificador "mayor o igual" en Java
	 */
	private String java_greq;
	
	/**
	 * Definición del modificador "menor o igual" en Java
	 */
	private String java_smeq;
	
	/**
	 * Definición del centro las funciones en Java
	 */
	private String java_center;
	
	/**
	 * Definición de la base de las funciones en Java
	 */
	private String java_basis;
	
	/**
	 * Definición de la derivada de la función
	 */
	private String deriv_equal;
	
	/**
	 * Definición de la derivada del modificador "mayor o igual"
	 */
	private String deriv_greq;
	
	/**
	 * Definición de la derivada del modificador "menor o igual"
	 */
	private String deriv_smeq;
	
	/**
	 * Definición de la derivada del centro de las funciones
	 */
	private String deriv_center;
	
	/**
	 * Definición de la derivada de la base de las funciones
	 */
	private String deriv_basis;
	
	/**
	 * Definición de la función en C
	 */
	private String ansic_equal;
	
	/**
	 * Definición del modificador "mayor o igual" en C
	 */
	private String ansic_greq;
	
	/**
	 * Definición del modificador "menor o igual" en C
	 */
	private String ansic_smeq;
	
	/**
	 * Definición del centro de la función en C
	 */
	private String ansic_center;
	
	/**
	 * Definición de la base de la función en C
	 */
	private String ansic_basis;
	
	/**
	 * Definición de la función en C++
	 */
	private String cpp_equal;
	
	/**
	 * Definición del modificador "mayor o igual" en C++
	 */
	private String cpp_greq;
	
	/**
	 * Definición del modificador "menor o igual" en C++
	 */
	private String cpp_smeq;
	
	/**
	 * Definición del centro de la función en C++
	 */
	private String cpp_center;
	
	/**
	 * Definición de la base de la función en C++
	 */
	private String cpp_basis;


	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 * @param pkg Nombre del paquete al que pertenece
	 * @param name Nombre de la función
	 */
	public MFDefinition(String pkg, String name) {
		super(pkg, name);
	}

	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el tipo de definición
	 */
	public int getKind() { 
		return PackageDefinition.MFUNC;
	}

	/**
	 * Obtiene el código XFL3 de un bloque
	 */
	public String getCode(int kind) {
		switch(kind) {
		case REQUIREMENTS: return super.requires;
		case SOURCE: return super.other;
		case JAVA_EQUAL: return this.java_equal;
		case JAVA_GREQ: return this.java_greq;
		case JAVA_SMEQ: return this.java_smeq;
		case JAVA_CENTER: return this.java_center;
		case JAVA_BASIS: return this.java_basis;
		case DERIV_EQUAL: return this.deriv_equal;
		case DERIV_GREQ: return this.deriv_greq;
		case DERIV_SMEQ: return this.deriv_smeq;
		case DERIV_CENTER: return this.deriv_center;
		case DERIV_BASIS: return this.deriv_basis;
		case C_EQUAL: return this.ansic_equal;
		case C_GREQ: return this.ansic_greq;
		case C_SMEQ: return this.ansic_smeq;
		case C_CENTER: return this.ansic_center;
		case C_BASIS: return this.ansic_basis;
		case CPP_EQUAL: return this.cpp_equal;
		case CPP_GREQ: return this.cpp_greq;
		case CPP_SMEQ: return this.cpp_smeq;
		case CPP_CENTER: return this.cpp_center;
		case CPP_BASIS: return this.cpp_basis;
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
		case JAVA_EQUAL: this.java_equal = code; break;
		case JAVA_GREQ: this.java_greq = code; break;
		case JAVA_SMEQ: this.java_smeq = code; break;
		case JAVA_CENTER: this.java_center = code; break;
		case JAVA_BASIS: this.java_basis = code; break;
		case DERIV_EQUAL: this.deriv_equal = code; break;
		case DERIV_GREQ: this.deriv_greq = code; break;
		case DERIV_SMEQ: this.deriv_smeq = code; break;
		case DERIV_CENTER: this.deriv_center = code; break;
		case DERIV_BASIS: this.deriv_basis = code; break;
		case C_EQUAL: this.ansic_equal = code; break;
		case C_GREQ: this.ansic_greq = code; break;
		case C_SMEQ: this.ansic_smeq = code; break;
		case C_CENTER: this.ansic_center = code; break;
		case C_BASIS: this.ansic_basis = code; break;
		case CPP_EQUAL: this.cpp_equal = code; break;
		case CPP_GREQ: this.cpp_greq = code; break;
		case CPP_SMEQ: this.cpp_smeq = code; break;
		case CPP_CENTER: this.cpp_center = code; break;
		case CPP_BASIS: this.cpp_basis = code; break;
		case UPDATE: super.update = code; break;
		}
	}

	/**
	 * Obtiene la descripción XFL3 de la definición
	 */
	public String toPkg() {
		String code = "mf "+name+" {"+eol;
		code += super.aliasblock();
		code += super.paramblock();
		code += super.requiresblock();
		code += " java {"+eol;
		if(this.java_equal != null && this.java_equal.length()>0)
			code += "   equal {"+eol+this.java_equal+eol+"    }"+eol;
		if(this.java_greq != null && this.java_greq.length()>0)
			code += "   greatereq {"+eol+this.java_greq+eol+"    }"+eol;
		if(this.java_smeq != null && this.java_smeq.length()>0)
			code += "   smallereq {"+eol+this.java_smeq+eol+"    }"+eol;
		if(this.java_center != null && this.java_center.length()>0)
			code += "   center {"+eol+this.java_center+eol+"    }"+eol;
		if(this.java_basis != null && this.java_basis.length()>0)
			code += "   basis {"+eol+this.java_basis+eol+"    }"+eol;
		code += "  }"+eol;
		code += " derivative {"+eol;
		if(this.deriv_equal != null && this.deriv_equal.length()>0)
			code += "   equal {"+eol+this.deriv_equal+eol+"    }"+eol;
		if(this.deriv_greq != null && this.deriv_greq.length()>0)
			code += "   greatereq {"+eol+this.deriv_greq+eol+"    }"+eol;
		if(this.deriv_smeq != null && this.deriv_smeq.length()>0)
			code += "   smallereq {"+eol+this.deriv_smeq+eol+"    }"+eol;
		if(this.deriv_center != null && this.deriv_center.length()>0)
			code += "   center {"+eol+this.deriv_center+eol+"    }"+eol;
		if(this.deriv_basis != null && this.deriv_basis.length()>0)
			code += "   basis {"+eol+this.deriv_basis+eol+"    }"+eol;
		code += "  }"+eol;
		code += " ansi_c {"+eol;
		if(this.ansic_equal != null && this.ansic_equal.length()>0)
			code += "   equal {"+eol+this.ansic_equal+eol+"    }"+eol;
		if(this.ansic_greq != null && this.ansic_greq.length()>0)
			code += "   greatereq {"+eol+this.ansic_greq+eol+"    }"+eol;
		if(this.ansic_smeq != null && this.ansic_smeq.length()>0)
			code += "   smallereq {"+eol+this.ansic_smeq+eol+"    }"+eol;
		if(this.ansic_center != null && this.ansic_center.length()>0)
			code += "   center {"+eol+this.ansic_center+eol+"    }"+eol;
		if(this.ansic_basis != null && this.ansic_basis.length()>0)
			code += "   basis {"+eol+this.ansic_basis+eol+"    }"+eol;
		code += "  }"+eol;
		code += " cplusplus {"+eol;
		if(this.cpp_equal != null && this.cpp_equal.length()>0)
			code += "   equal {"+eol+this.cpp_equal+eol+"    }"+eol;
		if(this.cpp_greq != null && this.cpp_greq.length()>0)
			code += "   greatereq {"+eol+this.cpp_greq+eol+"    }"+eol;
		if(this.cpp_smeq != null && this.cpp_smeq.length()>0)
			code += "   smallereq {"+eol+this.cpp_smeq+eol+"    }"+eol;
		if(this.cpp_center != null && this.cpp_center.length()>0)
			code += "   center {"+eol+this.cpp_center+eol+"    }"+eol;
		if(this.cpp_basis != null && this.cpp_basis.length()>0)
			code += "   basis {"+eol+this.cpp_basis+eol+"    }"+eol;
		code += "  }"+eol;
		code += super.updateblock();
		code += super.otherblock();
		code += " }"+eol+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	//                            MÉTODOS PRIVADOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el código de la clase
	 */
	protected String class_code() {
		return "public class "+name+" extends ParamMemFunc {"+eol;
	}

	/**
	 * Genera el código de ejecución de la clase
	 */
	protected String compute_code() {
		String code = "";

		if(this.java_equal != null) {
			code += eol+" public double compute(double x) {"+eol;
			code += variable_code(this.java_equal);
			code += this.java_equal+eol;
			code += "  }"+eol;
		}

		if(this.java_greq != null) {
			code += eol+" public double greatereq(double x) {"+eol;
			code += variable_code(this.java_greq);
			code += this.java_greq+eol;
			code += "  }"+eol;
		}

		if(this.java_smeq != null) {
			code += eol+" public double smallereq(double x) {"+eol;
			code += variable_code(this.java_smeq);
			code += this.java_smeq+eol;
			code += "  }"+eol;
		}

		if(this.java_center != null) {
			code += eol+" public double center() {"+eol;
			code += variable_code(this.java_center);
			code += this.java_center+eol;
			code += "  }"+eol;
		}

		if(this.java_basis != null) {
			code += eol+" public double basis() {"+eol;
			code += variable_code(this.java_basis);
			code += this.java_basis+eol;
			code += "  }"+eol;
		}

		if(this.deriv_equal != null) {
			code += eol+" public double[] deriv_eq(double x) {"+eol;
			code += "   double[] deriv = new double[getNumberOfParameters()];"+eol;
			code += variable_code(this.deriv_equal);
			code += this.deriv_equal+eol;
			code += "   return deriv;"+eol;
			code += "  }"+eol;
		}

		if(this.deriv_greq != null) {
			code += eol+" public double[] deriv_greq(double x) {"+eol;
			code += "   double[] deriv = new double[getNumberOfParameters()];"+eol;
			code += variable_code(this.deriv_greq);
			code += this.deriv_greq+eol;
			code += "   return deriv;"+eol;
			code += "  }"+eol;
		}

		if(this.deriv_smeq != null) {
			code += eol+" public double[] deriv_smeq(double x) {"+eol;
			code += "   double[] deriv = new double[getNumberOfParameters()];"+eol;
			code += variable_code(this.deriv_smeq);
			code += this.deriv_smeq+eol;
			code += "   return deriv;"+eol;
			code += "  }"+eol;
		}

		if(this.deriv_center != null) {
			code += eol+" public double[] deriv_center() {"+eol;
			code += "   double[] deriv = new double[getNumberOfParameters()];"+eol;
			code += variable_code(this.deriv_center);
			code += this.deriv_center+eol;
			code += "   return deriv;"+eol;
			code += "  }"+eol;
		}

		if(this.deriv_basis != null) {
			code += eol+" public double[] deriv_basis() {"+eol;
			code += "   double[] deriv = new double[getNumberOfParameters()];"+eol;
			code += variable_code(this.deriv_basis);
			code += this.deriv_basis+eol;
			code += "   return deriv;"+eol;
			code += "  }"+eol;
		}

		return code;
	}

	/**
	 * Genera el código para los parámetros y las variables min	y max dentro de
	 * un método de la clase
	 */
	protected String variable_code(String source) {
		String code = "";
		if(source.indexOf("min")!=-1) code += "   double min = this.u.min();"+eol;
		if(source.indexOf("max")!=-1) code += "   double max = this.u.max();"+eol;
		if(source.indexOf("step")!=-1) code += "   double step = this.u.step();"+eol;
		for(int i=0; i<param.size(); i++)
			code += "   double "+param.elementAt(i)+" = singleparam["+i+"].value;"+eol;
		if(paramlist != null && paramlist.length() > 0)
			code += "   double[] "+paramlist+" = getParamListValues();"+eol;
		return code;
	}

	/**
	 * Genera el código de los métodos "get**JavaCode" de la clase
	 */
	protected String java_code() {
		String sampled[];

		String code = eol+" public String getEqualJavaCode() {"+eol;
		code += "   String eol = System.getProperty";
		code += "(\"line.separator\", \"\\n\");"+eol;
		code += "   String code = \"\";"+eol;

		sampled = super.sample(java_equal);
		for(int i=0; i<sampled.length; i++)
			if(sampled[i].trim().length() > 0)
				code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
		code += "   return code;"+eol;
		code += "  }"+eol+eol;

		if(this.java_greq != null && this.java_greq.length()>0) {
			code += " public String getGreqJavaCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(java_greq);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.java_smeq != null && this.java_smeq.length()>0) {
			code += " public String getSmeqJavaCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(java_smeq);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.java_center != null && this.java_center.length()>0) {
			code += " public String getCenterJavaCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(java_center);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.java_basis != null && this.java_basis.length()>0) {
			code += " public String getBasisJavaCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(java_basis);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		return code;
	}

	/**
	 * Genera el código de los métodos "get**CCode" de la clase
	 */
	protected String c_code() {
		String sampled[];
		String code = "";

		if(this.ansic_equal != null && this.ansic_equal.length()>0) {
			code += eol+" public String getEqualCCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(ansic_equal);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.ansic_greq != null && this.ansic_greq.length()>0) {
			code += " public String getGreqCCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(ansic_greq);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.ansic_smeq != null && this.ansic_smeq.length()>0) {
			code += " public String getSmeqCCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(ansic_smeq);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.ansic_center != null && this.ansic_center.length()>0) {
			code += " public String getCenterCCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(ansic_center);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.ansic_basis != null && this.ansic_basis.length()>0) {
			code += " public String getBasisCCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(ansic_basis);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		return code;
	}

	/**
	 * Genera el código de los métodos "get**CppCode" de la clase
	 */
	protected String cpp_code() {
		String sampled[];
		String code = "";

		if(this.cpp_equal != null && this.cpp_equal.length()>0) {
			code += eol+" public String getEqualCppCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(cpp_equal);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.cpp_greq != null && this.cpp_greq.length()>0) {
			code += " public String getGreqCppCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(cpp_greq);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.cpp_smeq != null && this.cpp_smeq.length()>0) {
			code += " public String getSmeqCppCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(cpp_smeq);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.cpp_center != null && this.cpp_center.length()>0) {
			code += " public String getCenterCppCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(cpp_center);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		if(this.cpp_basis != null && this.cpp_basis.length()>0) {
			code += " public String getBasisCppCode() {"+eol;
			code += "   String eol = System.getProperty";
			code += "(\"line.separator\", \"\\n\");"+eol;
			code += "   String code = \"\";"+eol;

			sampled = super.sample(cpp_basis);
			for(int i=0; i<sampled.length; i++)
				if(sampled[i].trim().length() > 0)
					code += "   code += \"   "+sampled[i]+"\"+eol;"+eol;
			code += "   return code;"+eol;
			code += "  }"+eol+eol;
		}

		return code;
	}
}
