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

import java.io.*;
import java.util.*;

/**
 * Clase gen�rica que describe una operaci�n difusa (funciones unarias,
 * funciones binarias, funciones de pertenencia, familias de funciones de 
 * pertenencia, m�todos de concreci�n o funciones no difusas)
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public abstract class Definition implements Cloneable {

	//----------------------------------------------------------------------------//
	//                          CONSTANTES P�BLICAS                               //
	//----------------------------------------------------------------------------//

	public static final int REQUIREMENTS = 0;
	public static final int SOURCE = 1;
	public static final int JAVA_EQUAL = 2;
	public static final int JAVA_GREQ = 3;
	public static final int JAVA_SMEQ = 4;
	public static final int JAVA_CENTER = 5;
	public static final int JAVA_BASIS = 6;
	public static final int DERIV_EQUAL = 7;
	public static final int DERIV_GREQ = 8;
	public static final int DERIV_SMEQ = 9;
	public static final int DERIV_CENTER = 10;
	public static final int DERIV_BASIS = 11;
	public static final int C_EQUAL = 12;
	public static final int C_GREQ = 13;
	public static final int C_SMEQ = 14;
	public static final int C_CENTER = 15;
	public static final int C_BASIS = 16;
	public static final int CPP_EQUAL = 17;
	public static final int CPP_GREQ = 18;
	public static final int CPP_SMEQ = 19;
	public static final int CPP_CENTER = 20;
	public static final int CPP_BASIS = 21;
	public static final int UPDATE = 22;
	public static final int MEMBERS = 23;
	public static final int INPUTS = 24;

	//----------------------------------------------------------------------------//
	//                           MIEMBROS PRIVADOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Constante que define el salto de l�nea en cada plataforma
	 */
	protected String eol = System.getProperty("line.separator", "\n");
	
	/**
	 * Nombre del paquete al que pertenece la definici�n
	 */
	protected String pkg;
	
	/**
	 * Nombre de la funci�n que describe la definici�n
	 */
	protected String name;
	
	/**
	 * Lista de nombres alternativos para la funci�n
	 */
	protected Vector alias;
	
	/**
	 * Lista de par�metros independientes de la funci�n
	 */
	protected Vector param;
	
	/**
	 * Nombre de la lista de par�metros
	 */
	protected String paramlist;
	
	/**
	 * Descripci�n de los requisitos de los par�metros
	 */
	protected String requires;
	
	/**
	 * Descripci�n del proceso de actualizaci�n de la funci�n
	 */
	protected String update;
	
	/**
	 * Otro c�digo a insertar directamente en la clase a generar
	 */
	protected String other;

	/**
	 * Clase compilada a partir de toda la informaci�n
	 */
	private Class defclass;
	
	/**
	 * Ventana de edici�n de la definici�n 
	 */
	private Object editor;

	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 * @param pkg Nombre del paquete al que pertenece
	 * @param name Nombre de la funci�n
	 */
	public Definition(String pkg, String name) {
		this.pkg = pkg;
		this.name = name;
		this.alias = new Vector();
		this.param = new Vector();
		this.defclass = getDefClass();
		this.editor = null;
	}

	//----------------------------------------------------------------------------//
	//                       M�TODOS P�BLICOS CONSTANTES                          //
	//----------------------------------------------------------------------------//

	/**
	 * Crea la definici�n de una funci�n binaria
	 */
	public static Definition createBinaryDefinition(String pkg, String name) {
		return new BinaryDefinition(pkg,name);
	}

	/**
	 * Crea la definici�n de una funci�n unaria
	 */
	public static Definition createUnaryDefinition(String pkg, String name) {
		return new UnaryDefinition(pkg,name);
	}

	/**
	 * Crea la definici�n de una funci�n de pertenencia
	 */
	public static Definition createMFDefinition(String pkg, String name) {
		return new MFDefinition(pkg,name);
	}

	/**
	 * Crea la definici�n de un m�todo de concreci�n
	 */
	public static Definition createDefuzDefinition(String pkg, String name) {
		return new DefuzDefinition(pkg,name);
	}

	/**
	 * Crea la definici�n de una familia de MFs
	 */
	public static Definition createFamilyDefinition(String pkg, String name) {
		return new FamilyDefinition(pkg,name);
	}

	/**
	 * Crea la definici�n de una funci�n no difusa
	 */
	public static Definition createCrispDefinition(String pkg, String name) {
		return new CrispDefinition(pkg,name);
	}

	//----------------------------------------------------------------------------//
	//                            M�TODOS P�BLICOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre de la funci�n definida
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Compara un nombre con los distintos identificadores de la funci�n definida
	 */
	public boolean equals(String name) {
		if(this.name.equals(name)) return true;
		for(int i=0, size=alias.size(); i<size; i++)
			if(alias.elementAt(i).equals(name)) return true;
		return false;
	}

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		try { return super.clone(); }
		catch(CloneNotSupportedException e) { return null; }
	}

	/**
	 * Asigna la referencia a la ventana de edici�n
	 */
	public void setEditor(Object editor) {
		this.editor = editor;
	}

	/**
	 * Verifica si la definici�n est� siendo editada
	 */
	public boolean isEditing() {
		return this.editor != null;
	}

	/**
	 * Elimina los ficheros creados por la definicion
	 */
	public void unlink() {
		String javafilename = classname()+".java";
		File path = new File( System.getProperty("xfuzzy.path") );
		File javafile = new File(path,javafilename);
		String classfilename = classname()+".class";
		File classfile = new File(path,classfilename);
		javafile.delete();
		classfile.delete();
	}

	/**
	 * Genera la descripci�n de la funci�n y la compila
	 */
	public boolean compile() {
		String filename = classname()+".java";
		File path = new File( System.getProperty("xfuzzy.path") );
		File file = new File(path,filename);
		byte buf[] = source().getBytes();
		String command;

		try {
			file.getParentFile().mkdirs();
			OutputStream f = new FileOutputStream(file);

			f.write(buf);
			f.close();
			if(File.separatorChar == '\\')
				command = "javac \""+file.getAbsolutePath()+"\"";
			else command = "javac "+file.getAbsolutePath();
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(command);
			p.waitFor();
		} catch (Exception e) {
			System.err.println(e.toString());
			return false;
		}

		this.defclass = getDefClass();
		if(this.defclass == null) return false;
		return true;
	}

	/**
	 * Obtiene una instancia de la clase de la definici�n
	 */
	public Object instantiate() {
		Object newobject;

		if(defclass == null) return null;
		try { newobject = this.defclass.newInstance(); }
		catch (IllegalAccessException e) { return null; }
		catch (InstantiationException e) { return null; }
		return newobject;
	}

	/**
	 * Obtiene el c�digo fuente de la clase de la definici�n
	 */
	public String source() {
		String code = headline_code();
		code += class_code();
		code += constructor_code();
		code += compute_code();
		code += test_code();
		code += update_code();
		code += defined_code();
		code += java_code();
		code += c_code();
		code += cpp_code();
		code += other_code();
		code += "}"+eol;
		return code;
	}

	/**
	 * Asigna el nombre de la funci�n
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Asigna los alias de la funci�n
	 */
	public void setAlias(Vector alias) {
		this.alias = alias;
	}

	/**
	 * Asigna los par�metros de la funci�n
	 */
	public void setParameters(Vector param) {
		this.param = param;
	}

	/**
	 * Asigna el identificador de la lista de par�metros
	 */
	public void setParamList(String paramlist) {
		this.paramlist = paramlist;
	}

	/**
	 * Asigna las funciones admitidas para un m�todo de concreci�n
	 */
	public void setDefinedFor(Vector v) {
	}

	/**
	 * Asigna el nombre del paquete al que pertenece la definici�n
	 */
	public void setPackageName(String name) {
		this.pkg = name;
	}

	/**
	 * Obtiene el nombre del paquete de la definici�n
	 */
	public String getPackageName() {
		return this.pkg;
	}

	/**
	 * Obtiene el nombre de la funci�nn definida
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Obtiene los nombres alternativos (alias) de la funci�n
	 */
	public Vector getAlias() {
		return this.alias;
	}

	/**
	 * Obtiene los par�metros de la funci�n definida
	 */
	public Vector getParameters() {
		return this.param;
	}

	/**
	 * Obtiene el nombre de la lista de par�metros (en su caso)
	 */
	public String getParamList() {
		return this.paramlist;
	}

	/**
	 * Obtiene las funciones admitidas para un metodo de concreci�N
	 */
	public Vector getDefinedFor() {
		return new Vector();
	}

	//----------------------------------------------------------------------------//
	//                       M�TODOS P�BLICOS ABSTRACTOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene las descripci�n XFL3 de la definici�n
	 */
	public abstract String toPkg();

	/**
	 * Obtiene el tipo de definici�n
	 */
	public abstract int getKind();

	/**
	 * Obtiene el c�digo XFL3 de un bloque
	 */
	public abstract String getCode(int kind);

	/**
	 * Asigna el c�digo XFL3 de un bloque
	 */
	public abstract void setCode(int kind, String code);

	//----------------------------------------------------------------------------//
	//                            M�TODOS PRIVADOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene un referencia a la clase compilada de la funci�n
	 */
	private Class getDefClass() {
		Class getclass;
		try { getclass = Class.forName("pkg."+pkg+"."+getKindString()+"."+name); }
		catch (ClassNotFoundException e){ return null; }
		return getclass;
	}

	/**
	 * Obtiene el tipo de definici�n en forma de String
	 * @return
	 */
	private String getKindString() {
		String kind="";
		switch(getKind()) {
		case PackageDefinition.BINARY: kind = "binary"; break;
		case PackageDefinition.UNARY:  kind = "unary"; break;
		case PackageDefinition.CRISP:  kind = "crisp"; break;
		case PackageDefinition.DEFUZ:  kind = "defuz"; break;
		case PackageDefinition.MFUNC:  kind = "mfunc"; break;
		case PackageDefinition.FAMILY: kind = "family"; break;
		}
		return kind;
	}

	/**
	 * Obtiene el nombre del fichero que describe la definici�n (sin extensi�n)
	 * @return
	 */
	private String classname() {
		String sep = File.separator;
		return "pkg"+sep+pkg+sep+getKindString()+sep+name;
	}

	/**
	 * Cambia algunos caracteres de una cadena
	 */
	protected String [] sample(String str) {
		int lines=1;
		if(str == null) return new String[0];
		for(int i=0; i<str.length(); i++) if(str.charAt(i) == '\n') lines++;
		String value[] = new String[lines];

		int line=0;
		value[0] = "";
		for(int i=0; i<str.length(); i++)
			switch(str.charAt(i)) {
			case '\b': value[line] += "\\b"; continue;
			case '\t': value[line] += "\\t"; continue;
			case '\f': value[line] += "\\f"; continue;
			case '\"': value[line] += "\\\""; continue;
			case '\'': value[line] += "\\\'"; continue;
			case '\\': value[line] += "\\\\"; continue;
			case '\n': line++; value[line] = ""; continue;
			case '\r': continue;
			case '\0': continue;
			default: value[line] += str.substring(i,i+1);
			}
		return value;
	}

	//----------------------------------------------------------------------------//
	// M�todos que generan el c�digo XFL3                                         //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la descripci�n XFL3 del bloque "alias"
	 */
	protected String aliasblock() {
		int size = this.alias.size();
		if(size==0) return "";
		String code = " alias "+this.alias.elementAt(0);
		for(int i=1; i<size; i++) code += ", "+this.alias.elementAt(i);
		code += ";"+eol;
		return code;
	}

	/**
	 * Obtiene la descripci�n XFL3 del bloque "parameter"
	 */
	protected String paramblock() {
		int size = this.param.size();
		boolean list = (paramlist != null && paramlist.length() >0);
		if(size==0 && !list) return "";
		String code = " parameter ";
		for(int i=0; i<size; i++) {
			if(i>0) code += ", ";
			code += ""+this.param.elementAt(i);
		}
		if(list) {
			if(size>0) code += ", ";
			code += paramlist+"[]";
		}
		code += ";"+eol;
		return code;
	}

	/**
	 * Obtiene la descripci�n XFL3 del bloque "requires"
	 */
	protected String requiresblock() {
		if(this.requires == null || this.requires.length()==0) return "";
		return " requires {"+eol+this.requires+eol+"  }"+eol;
	}

	/**
	 * Obtiene la descripci�n XFL3 del bloque "update"
	 */
	protected String updateblock() {
		if(this.update == null || this.update.length()==0) return "";
		return " update {"+eol+this.update+eol+"  }"+eol;
	}

	/**
	 * Obtiene la descripci�n XFL3 del bloque "source"
	 */
	protected String otherblock() {
		if(this.other == null || this.other.length()==0) return "";
		return " source {"+eol+this.other+eol+"  }"+eol;
	}

	//----------------------------------------------------------------------------//
	// M�todos que generan el c�digo de la clase                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el c�digo de cabecera	
	 */
	protected String headline_code() {
		String headline;
		headline  = "//+++++++++++++++++++++++++++++++++++++++++++++++++++++"+eol;
		headline += "// File automatically generated by Xfuzzy - DO NOT EDIT"+eol;
		headline += "//+++++++++++++++++++++++++++++++++++++++++++++++++++++"+eol;
		headline += eol+"package pkg."+pkg+"."+getKindString()+";"+eol+eol;
		headline += "import xfuzzy.lang.*;"+eol+eol;
		return headline;
	}

	/**
	 * Genera el c�digo del constructor
	 */
	protected String constructor_code() {
		String code = " public "+name+"() {"+eol;
		code += "   super(\""+pkg+"\",\""+name+"\");"+eol;
		code += "   Parameter single[] = new Parameter["+param.size()+"];"+eol;
		for(int i=0, size = param.size(); i<size; i++) {
			code += "   single["+i+"] = ";
			code += "new Parameter(\""+param.elementAt(i)+"\");"+eol;
		}
		code += "   setSingleParameters(single);"+eol;
		if(paramlist != null && paramlist.length() >0) {
			code += "   setParamListName(\""+paramlist+"\");"+eol;
			code += "   setParamList(new Parameter[0]);"+eol;
		} 
		code += "  }"+eol;
		return code;
	}

	/**
	 * Genera el c�digo del m�todo "test" 
	 */
	protected String test_code() {
		String code = eol+" public boolean test () {"+eol;
		if( this.requires == null ) code += "   return true;"+eol;
		else {
			code += variable_code(this.requires);
			code += "   return ("+this.requires+");"+eol;
		}
		code += "  }"+eol;
		return code;
	}

	/**
	 * Genera el c�digo de actualizaci�n de los par�metros
	 */
	protected String update_code() {
		String code = "";
		if(this.update != null) {
			code += eol;
			code += " public void update() {"+eol;
			code += "   if(!isAdjustable()) return;"+eol;
			code += "   double[] pos = get();"+eol;
			code += "   double[] desp = getDesp();"+eol;
			code += "   boolean[] adj = getAdjustable();"+eol;
			code += variable_code(this.update);
			code += this.update+eol;
			code += "   updateValues(pos);"+eol;
			code += "  }"+eol;
		}
		return code;
	}

	/**
	 * Genera el c�digo extra del bloque "source"
	 */
	protected String other_code() {
		if(this.other == null) return "";
		return this.other+eol;
	}

	/**
	 * Genera el c�digo del bloque "definedfor" 
	 */
	protected String defined_code() { return ""; }

	/**
	 * Genera el c�digo del m�todo "toC" 
	 */
	protected String c_code() {
		String ccode = eol+" public String getCCode()";
		ccode += " throws XflException {"+eol;
		ccode += "  throw new XflException();"+eol;
		ccode += " }"+eol;
		return ccode;
	}

	/**
	 * Genera el c�digo del m�todo "toCpp" 
	 */
	protected String cpp_code() {
		String ccode = eol+" public String getCppCode()";
		ccode += " throws XflException {"+eol;
		ccode += "  throw new XflException();"+eol;
		ccode += " }"+eol;
		return ccode;
	}
	
	/**
	 * Genera el c�digo para los par�metros dentro de un m�todo
	 */
	protected String variable_code(String source) {
		String code = "";
		for(int i=0; i<param.size(); i++)
			code += "   double "+param.elementAt(i)+" = singleparam["+i+"].value;"+eol;
		if(paramlist != null && paramlist.length() > 0)
			code += "   double[] "+paramlist+" = getParamListValues();"+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	//                       M�TODOS PRIVADOS ABSTRACTOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el c�digo de la clase
	 */
	protected abstract String class_code();

	/**
	 * Genera el c�digo del m�todo "compute"
	 */
	protected abstract String compute_code();

	/**
	 * Genera el c�digo del m�todo "toJava" 
	 */
	protected abstract String java_code();
}
