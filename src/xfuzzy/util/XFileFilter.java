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


package xfuzzy.util;

import javax.swing.filechooser.*;
import java.io.File;

/**
 * Filtros de la ventana de selecci�n de ficheros
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class XFileFilter extends FileFilter {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES P�BLICAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Selecci�n de todos los ficheros
	 */
	public static final int ALLFILES = 0;
	
	/**
	 * Selecci�n de directorios
	 */
	public static final int NONEFILE = 1;
	
	/**
	 * Selecci�n configurable
	 */
	public static final int USERFILES = 2;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Tipo de filtro
	 */
	private int code;
	
	/**
	 * Terminaci�n de los ficheros a mostrar
	 */
	private String ending;
	
	/**
	 * Descripci�n del filtro
	 */
	private String description;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor de un filtro predise�ado
	 */
	public XFileFilter(int code) {
		this.code = code;
		this.ending = "";
		if(code == ALLFILES) this.description = "All files";
		else if(code == NONEFILE) this.description = "Directories";
		else this.description = "";
	}

	/**
	 * Constructor de un filtro creado por el usuario
	 * 
	 * @param ending
	 * @param description
	 */
	public XFileFilter(String ending, String description) {
		this.code = USERFILES;
		this.ending = ending;
		this.description = description;
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Estudia si el fichero es filtrado
	 */
	public boolean accept(File f) {
		if(code == ALLFILES) return true;
		if(code == NONEFILE) return false;
		if(f.getName().endsWith(ending)) return true;
		if(f.isDirectory()) return true;
		return false;
	}

	/**
	 * Descripci�n del filtro
	 */
	public String getDescription() {
		return description;
	}
}

