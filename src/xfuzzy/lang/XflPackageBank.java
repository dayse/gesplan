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
import java.util.Vector;

/**
 * Clase que almacena los paquetes de funciones disponibles
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XflPackageBank implements FilenameFilter {
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de paquetes de funciones
	 */
	private XflPackage[] list;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XflPackageBank() {
		String path = System.getProperty("xfuzzy.path");
		File pkgdir = new File(path+"/pkg");
		if(!pkgdir.exists() || !pkgdir.isDirectory()) {
			this.list = new XflPackage[0];
			return;
		}
		File[] pkg = pkgdir.listFiles(this);
		
		this.list = new XflPackage[pkg.length];
		for(int i=0; i<list.length; i++) {
			list[i] = new XflPackage(pkg[i]);
		}
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Estudia si el conjunto de paquetes contiene una definición dada
	 */
	public boolean contains(String pkg, String func, int kind) {
		XflPackage xflpkg = getXflPackage(pkg);
		if(xflpkg == null) return false;
		return xflpkg.contains(func,kind);
	}

	/**
	 * Obtiene una instancia de una definición
	 */
	public Object instantiate(String pkg, String func, int kind) {
		XflPackage xflpkg = getXflPackage(pkg);
		return xflpkg.instantiate(func,kind);
	}

	/**
	 * Acepta los ficheros que sean directorios
	 * Desarrolla la interfaz FilenameFilter
	 * @param file
	 * @return
	 */
	public boolean accept(File file, String filename) {
		if(file.isDirectory()) return true;
		return false;
	}

	/**
	 * Obtiene la lista de funciones disponibles de un cierto tipo
	 * @param kind
	 * @return
	 */
	public Vector getFunctionNames(int kind) {
		Vector vv = new Vector();
		for(int i=0; i<list.length; i++) {
			String pkgname = list[i].toString();
			String[] funcname = list[i].get(kind);
			for(int j=0; j<funcname.length; j++) vv.addElement(pkgname+"."+funcname[j]);
		}
		return vv;
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Busca un paquete a partir de su nombre
	 */
	private XflPackage getXflPackage(String pkg) {
		for(int i=0; i<list.length; i++) {
			if(list[i].equals(pkg)) return list[i];
		}
		return null;
	}

}

