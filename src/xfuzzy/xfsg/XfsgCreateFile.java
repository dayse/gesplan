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

package xfuzzy.xfsg;

import java.io.*;

/**
* Clase que genera un fichero a partir de una cadena
* (similar a xfvhdl/XfvhdlCreateFile.java)
* 
* @author Jesús Izquierdo Tena.
*/
public class XfsgCreateFile{
	

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//			  CONSTRUCTOR DE LA CLASE					 	   
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//	
	/**
	* Método construtor de la clase
	*/
	public XfsgCreateFile (String fileName, String code) {
		createFile(fileName,code);
	}
	

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//			  MÉTO_DOS PÚBLICOS DE LA CLASE				       
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	* Método que crea un fichero de nombre "file" con el contenido 
	* de "code"
	* @param file Nombre del fichero
	* @param code Contenido del fichero
	*/
	private void createFile(String fileName, String code) {
		byte buf[] = code.getBytes();

		// Crea el directorio de salida si no existe
		File temp = new File(XfsgProperties.outputDirectory);
		if (!temp.exists()){
			if(!temp.mkdirs()){
				new XfsgError(6, XfsgProperties.outputDirectory);
			}
		}
		/*
		// Guarda información del directorio de salida
		if(XfsgProperties.fileDir == null)
				XfsgProperties.fileDir=temp.getAbsolutePath();
		*/
		
		File file = new File(XfsgProperties.outputDirectory,fileName);	
		try {
			OutputStream stream = new FileOutputStream(file);
			stream.write(buf);
			stream.close();
		}
		catch (IOException e) {}
	}
	
} // Fin de la clase
