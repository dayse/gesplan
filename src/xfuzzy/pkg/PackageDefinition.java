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
//			PAQUETE DE OPERACIONES			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.pkg;

import java.util.Vector;
import java.io.*;

public class PackageDefinition {
	
	//----------------------------------------------------------------------------//
	//                          CONSTANTES PÚBLICAS                               //
	//----------------------------------------------------------------------------//
	
	public static final int BINARY = 0;
	public static final int UNARY = 1;
	public static final int DEFUZ = 2;
	public static final int MFUNC = 3;
	public static final int FAMILY = 4;
	public static final int CRISP = 5;
	
	//----------------------------------------------------------------------------//
	//                           MIEMBROS PRIVADOS                                //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre del paquete
	 */
	private String name;
	
	/**
	 * Archivo en el que se describe el contenido del paquete
	 */
	private File file;
	
	/**
	 * Lista de definiciones de funciones binarias
	 */
	private Vector binary;
	
	/**
	 * Lista de definiciones de funciones unarias
	 */
	private Vector unary;
	
	/**
	 * Lista de definiciones de funciones de pertenencia
	 */
	private Vector mfunc;
	
	/**
	 * Lista de definiciones de métodos de concreción
	 */
	private Vector defuz;
	
	/**
	 * Lista de definiciones de familias de funciones de pertenencia
	 */
	private Vector family;
	
	/**
	 * Lista de definiciones de funciones no difusas
	 */
	private Vector crisp;
	
	/**
	 * Marcador que indica si existen modificaciones del paquete sin almacenar
	 */
	private boolean modified;
	
	/**
	 * Ventana de edición del paquete (null si no se está editando)
	 */
	private Object editor;
	
	/**
	 * Listado de errores en la compilación del paquete
	 */
	private String resume;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public PackageDefinition(File file) {
		String filename = file.getName();
		int index = filename.toLowerCase().indexOf(".pkg");
		if(index >0) this.name = filename.substring(0, index);
		else this.name = new String(filename);
		this.file = file;
		this.binary = new Vector();
		this.unary = new Vector();
		this.defuz = new Vector();
		this.mfunc = new Vector();
		this.family = new Vector();
		this.crisp = new Vector();
		this.modified = false;
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el nombre del paquete
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Compara una cadena con el nombre del paquete
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}
	
	/**
	 * Añade una definicion al paquete
	 */
	public void add(Definition def, int kind) {
		Vector vector = get(kind);
		if(vector != null) vector.addElement(def);
	}

	/**
	 * Elimina una definición del paquete
	 */
	public boolean remove(Definition def, int kind) {
		Vector vector = get(kind);
		if(vector == null) return false;
		return vector.removeElement(def);
	}

	/**
	 * Elimina una definición del paquete
	 */
	public boolean remove(String name, int kind) {
		Vector vector = get(kind);
		if(vector == null) return false;
		Definition def = search(vector,name);
		if(def == null) return false;
		return vector.removeElement(def);
	}

	/**
	 * Estudia si el paquete contiene una definición dada
	 */
	public boolean contains(String name, int kind) {
		Vector vector = get(kind);
		Definition def = search(vector,name);
		if(def == null) return false;
		return true;
	}

	/**
	 * Obtiene una instancia de una definición
	 */
	public Object instantiate(String name, int kind) {
		Vector vector = get(kind);
		Definition def = search(vector,name);
		if(def == null) return null;
		return def.instantiate();
	}

	/**
	 * Obtiene la lista de definiciones de un cierto tipo
	 */
	public Vector get(int kind) {
		switch(kind) {
		case BINARY: return this.binary;
		case UNARY: return this.unary;
		case DEFUZ: return this.defuz;
		case MFUNC: return this.mfunc;
		case FAMILY: return this.family;
		case CRISP: return this.crisp;
		default: return null;
		}
	}

	/**
	 * Busca una definición en una lista de definiciones
	 */
	public Definition search(Vector vector, String name) {
		if(vector == null) return null;
		for(int i=0, size = vector.size(); i<size; i++) {
			Definition def = (Definition) vector.elementAt(i);
			if(def.equals(name)) return def;
		}
		return null;
	}

	/**
	 * Obtiene el mensaje con los errores de compilación
	 */
	public String resume() {
		return this.resume;
	}

	/**
	 * Compila todas las definiciones del paquete
	 */
	public boolean compile() {
		Vector v = new Vector();
		for(int i=0, size=binary.size(); i<size; i++) {
			Definition def = (Definition) binary.elementAt(i);
			if(!def.compile()) v.addElement(def);
		}
		for(int i=0, size=unary.size(); i<size; i++) {
			Definition def = (Definition) unary.elementAt(i);
			if(!def.compile()) v.addElement(def);
		}
		for(int i=0, size=mfunc.size(); i<size; i++) {
			Definition def = (Definition) mfunc.elementAt(i);
			if(!def.compile()) v.addElement(def);
		}
		for(int i=0, size=defuz.size(); i<size; i++) {
			Definition def = (Definition) defuz.elementAt(i);
			if(!def.compile()) v.addElement(def);
		}
		for(int i=0, size=family.size(); i<size; i++) {
			Definition def = (Definition) family.elementAt(i);
			if(!def.compile()) v.addElement(def);
		}
		for(int i=0, size=crisp.size(); i<size; i++) {
			Definition def = (Definition) crisp.elementAt(i);
			if(!def.compile()) v.addElement(def);
		}
		
		String eol = System.getProperty("line.separator", "\n");
		resume = "";
		for(int i=0; i<v.size(); i++)
			resume += "Can't compile "+v.elementAt(i)+"."+eol;
		return (v.size()==0);
	}

	/**
	 * Elimina todos los ficheros creados al compilar
	 */
	public void unlink() {
		for(int i=0, size=binary.size(); i<size; i++)
			((Definition) binary.elementAt(i)).unlink();
		for(int i=0, size=unary.size(); i<size; i++)
			((Definition) unary.elementAt(i)).unlink();
		for(int i=0, size=defuz.size(); i<size; i++)
			((Definition) defuz.elementAt(i)).unlink();
		for(int i=0, size=mfunc.size(); i<size; i++)
			((Definition) mfunc.elementAt(i)).unlink();
		for(int i=0, size=family.size(); i<size; i++)
			((Definition) family.elementAt(i)).unlink();
		for(int i=0, size=crisp.size(); i<size; i++)
			((Definition) crisp.elementAt(i)).unlink();
		File path = new File(System.getProperty("xfuzzy.path"));
		File dir = new File(path,"pkg"+File.separator+this.name);
		File binarydir = new File(dir,"binary");
		File unarydir = new File(dir,"unary");
		File crispdir = new File(dir,"crisp");
		File defuzdir = new File(dir,"defuz");
		File mfuncdir = new File(dir,"mfunc");
		File familydir = new File(dir,"family");
		binarydir.delete();
		unarydir.delete();
		crispdir.delete();
		defuzdir.delete();
		mfuncdir.delete();
		familydir.delete();
		dir.delete();
	}
	
	/**
	 * Elimina el paquete y todos los archivos creados al compilar
	 */
	public void delete() {
		unlink();
		file.delete();
	}
	
	/**
	 * Obtiene la descripción XFL3 del paquete
	 */
	public String toPkg() {
		String code = "";
		for(int i=0, size=binary.size(); i<size; i++)
			code += ((Definition) binary.elementAt(i)).toPkg();
		for(int i=0, size=unary.size(); i<size; i++)
			code += ((Definition) unary.elementAt(i)).toPkg();
		for(int i=0, size=defuz.size(); i<size; i++)
			code += ((Definition) defuz.elementAt(i)).toPkg();
		for(int i=0, size=mfunc.size(); i<size; i++)
			code += ((Definition) mfunc.elementAt(i)).toPkg();
		for(int i=0, size=family.size(); i<size; i++)
			code += ((Definition) family.elementAt(i)).toPkg();
		for(int i=0, size=crisp.size(); i<size; i++)
			code += ((Definition) crisp.elementAt(i)).toPkg();
		return code;
	}

	/**
	 * Almacena la descripción en el fichero del paquete
	 */
	public boolean save() {
		if(this.file == null) return false;
		String code = toPkg();
		byte buf[] = code.getBytes();
		
		try {
			OutputStream stream = new FileOutputStream(this.file);
			stream.write(buf);
			stream.close();
		}
		catch (IOException e) { return false; }
		return true;
	}

	/**
	 * Almacena la descripción del paquete en un fichero dado
	 */
	public boolean save_as(File file) {
		String filename = file.getName();
		this.file = file;
		if(!save()) return false;
		rename(filename.substring(0, filename.indexOf(".")));
		compile();
		return true;
	}

	/**
	 * Asigna el valor al carácter de modificado del paquete
	 */
	public void setModified(boolean b) {
		this.modified = b;
	}
	
	/**
	 * Verifica si el paquete contiene cambios sin almacenar
	 */
	public boolean isModified() {
		return this.modified;
	}

	/**
	 * Asigna la referencia del editor del paquete
	 */
	public void setEditor(Object editor) {
		this.editor = editor;
	}

	/**
	 * Obtiene la referencia al editor del paquete
	 */
	public Object getEditor() {
		return this.editor;
	}

	/**
	 * Verifica si alguna definición está siendo editada
	 */
	public boolean isEditingDefinitions() {
		boolean editing = false;
		for(int i=0;i<binary.size(); i++) 
			if( ((Definition) binary.elementAt(i)).isEditing() ) editing = true;
		for(int i=0;i<unary.size(); i++) 
			if( ((Definition) unary.elementAt(i)).isEditing() ) editing = true;
		for(int i=0;i<mfunc.size(); i++) 
			if( ((Definition) mfunc.elementAt(i)).isEditing() ) editing = true;
		for(int i=0;i<defuz.size(); i++) 
			if( ((Definition) defuz.elementAt(i)).isEditing() ) editing = true;
		for(int i=0;i<family.size(); i++) 
			if( ((Definition) family.elementAt(i)).isEditing() ) editing = true;
		for(int i=0;i<crisp.size(); i++) 
			if( ((Definition) crisp.elementAt(i)).isEditing() ) editing = true;
		return editing;
	}

	/**
	 * Verifica si el paquete está siendo editado
	 */
	public boolean isEditing() {
		return (this.editor != null);
	}

	/**
	 * Verifica si el paquete es editable
	 */
	public boolean isEditable() {
		return !this.name.equals("xfl");
	}

	/**
	 * Obtiene el fichero de almacenamiento del paquete
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Modifica el nombre del paquete
	 */
	public void rename(String name) {
		this.name = name;
		for(int i=0; i<binary.size(); i++)
			((Definition) binary.elementAt(i)).setPackageName(name);
		for(int i=0; i<unary.size(); i++)
			((Definition) unary.elementAt(i)).setPackageName(name);
		for(int i=0; i<defuz.size(); i++)
			((Definition) defuz.elementAt(i)).setPackageName(name);
		for(int i=0; i<mfunc.size(); i++)
			((Definition) mfunc.elementAt(i)).setPackageName(name);
		for(int i=0; i<family.size(); i++)
			((Definition) family.elementAt(i)).setPackageName(name);
		for(int i=0; i<crisp.size(); i++)
			((Definition) crisp.elementAt(i)).setPackageName(name);
	}
}

