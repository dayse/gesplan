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

package xfuzzy.lang;

import java.io.*;

/**
 * Clase que almacena un paquete de funciones
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XflPackage implements FilenameFilter {
	
	//----------------------------------------------------------------------------//
	//                           CONSTANTES PUBLICAS                              //
	//----------------------------------------------------------------------------//
	
	public static final int BINARY = 0;
	public static final int UNARY = 1;
	public static final int DEFUZ = 2;
	public static final int MFUNC = 3;
	public static final int FAMILY = 4;
	public static final int CRISP = 5;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre del paquete
	 */
	private String name;
	
	/**
	 * Lista de funciones binarias
	 */
	private String[] binary;
	
	/**
	 * Lista de funciones unarias
	 */
	private String[] unary;
	
	/**
	 * Lista de funciones no difusas
	 */
	private String[] crisp;
	
	/**
	 * Lista de métodos de concreción
	 */
	private String[] defuz;
	
	/**
	 * Lista de funciones de pertenencia
	 */
	private String[] mfunc;
	
	/**
	 * Lista de familias de funciones de pertenencia
	 */
	private String[] family;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 * @param file Direcotorio de almacenamiento del paquete
	 */
	public XflPackage(File file) {
		this.name = file.getName();
		this.binary = readDirectory(new File(file,"binary"));
		this.unary = readDirectory(new File(file,"unary"));
		this.crisp = readDirectory(new File(file,"crisp"));
		this.defuz = readDirectory(new File(file,"defuz"));
		this.mfunc = readDirectory(new File(file,"mfunc"));
		this.family = readDirectory(new File(file,"family"));
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
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
	 * Estudia si el paquete contiene una definición dada
	 */
	public boolean contains(String func, int kind) {
		String[] list = get(kind);
		for(int i=0; i<list.length; i++) {
			if(list[i].equals(func)) return true;
		}
		return false;
	}

	/**
	 * Obtiene una instancia de una definición
	 */
	public Object instantiate(String func, int kind) {
		String classname = "";
		switch(kind) {
			case BINARY: classname = "pkg."+name+".binary."+func; break;
			case UNARY: classname = "pkg."+name+".unary."+func; break;
			case CRISP: classname = "pkg."+name+".crisp."+func; break;
			case DEFUZ: classname = "pkg."+name+".defuz."+func; break;
			case MFUNC: classname = "pkg."+name+".mfunc."+func; break;
			case FAMILY: classname = "pkg."+name+".family."+func; break;
		}
		try { return Class.forName(classname).newInstance(); }
		catch (ClassNotFoundException e){ return null; }
		catch (IllegalAccessException e) { return null; }
		catch (InstantiationException e) { return null; }
	}

	/**
	 * Acepta los ficheros con extensión ".class" (binarios java)
	 * Desarrolla la interfaz FileFilter
	 * @param file
	 * @return
	 */
	public boolean accept(File file, String filename) {
		if(filename.endsWith(".class")) return true;
		return false;
	}

	/**
	 * Obtiene la lista de definiciones de un cierto tipo
	 */
	public String[] get(int kind) {
		switch(kind) {
			case BINARY: return this.binary;
			case UNARY: return this.unary;
			case DEFUZ: return this.defuz;
			case MFUNC: return this.mfunc;
			case FAMILY: return this.family;
			case CRISP: return this.crisp;
			default: return new String[0];
		}
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Extrea los nombres de las clases de un directorio
	 * @param dir
	 * @return
	 */
	private String[] readDirectory(File dir) {
		if(!dir.exists() || !dir.isDirectory()) return new String[0];
		String[] full = dir.list(this);
		String[] cut = new String[full.length];
		for(int i=0; i<cut.length; i++) {
			cut[i] = full[i].substring(0,full[i].length()-6);
		}
		return cut;
	}
}

