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

import java.awt.*;

/**
 * Constantes comunes
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XConstants {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PÚBLICAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre de la versión del entorno
	 */
	public static final String version = "Xfuzzy 3.3 beta 1";

	/**
	 * Fuente de letra para las etiquetas y botones
	 */
	public static final Font font = new Font("Dialog",Font.BOLD,11);
	
	/**
	 * Fuente de letra para los campos
	 */
	public static final Font textfont = new Font("Dialog",Font.PLAIN,11);

	/**
	 * Color de fondo para los campos de texto
	 */
	public static final Color textbackground = new Color(255,250,205);
	
	/**
	 * Color de fondo para el elemento seleccionado en una lista
	 */
	public static final Color textselectedbg = new Color(204,204,255);
	
	/**
	 * Color de fondo para la representación del sistema
	 */
	public static final Color systembg = new Color(204,204,255);
	
	/**
	 * Color de fondo para las llamadas a bloques no difusos o bases de reglas
	 */
	public static final Color callbg = new Color(0,180,180);
	
	/**
	 * Color de fondo para la etiqueta de las llamadas
	 */
	public static final Color callnamebg = new Color(0,0,150);

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Verifica si una cadena corresponde a un identificador válido
	 */
	public static boolean isIdentifier(String name) {
		if(name.equals("")) return false;
		if(name.equals("operation")) return false;
		if(name.equals("type")) return false;
		if(name.equals("module")) return false;
		if(name.equals("extends")) return false;
		if(name.equals("using")) return false;
		if(name.equals("import")) return false;
		if(name.equals("if")) return false;
		char [] namec = name.toCharArray();
		if(!Character.isLetter(namec[0]) && namec[0] != '_') return false;
		for(int i=1; i<namec.length; i++)
			if(!Character.isLetterOrDigit(namec[i]) && namec[i] != '_') return false;
		return true;
	}
}
