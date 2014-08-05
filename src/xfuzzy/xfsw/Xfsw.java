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

package xfuzzy.xfsw;

import xfuzzy.lang.*;
import java.io.*;

/**
 * Herramienta que desarrolla la síntesis software a C, C++ o Java
 * 
 * @author Francisco José Moreno Velo 
 *
 */
public class Xfsw {

	//----------------------------------------------------------------------------//
	//                            COSTANTES PÚBLICAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código para indicar el lenguaje C estandar
	 */
	public static int ANSI_C = 0;
	
	/**
	 * Código para indicar el lenguaje C++
	 */
	public static int C_PLUS_PLUS = 1;
	
	/**
	 * Código para indicar el lenguaje Java
	 */
	public static int JAVA = 2;
	
	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Carácter de fin de linea dependiente de la plataforma
	 */
	private static String eol = System.getProperty("line.separator", "\n");

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Mensaje a mostrar al terminar la ejecución
	 */
	private String msg;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfsw(Specification spec, File dir, int lang) {
		this.msg = "";
		File wdir = null; 
		if(dir == null) wdir = spec.getFile().getParentFile();
		else wdir = dir;
		
		if(lang == ANSI_C) this.msg = generateC(spec,wdir);
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Ejecución externa
	 */
	public static void main(String args[]) {
		String filename = "";
		int lang = ANSI_C;
		if(args.length == 1) filename = args[0];
		else if(args.length == 2) {
			if(args[0].equals("-ansic")) lang = ANSI_C;
			if(args[0].equals("-c++")) lang = C_PLUS_PLUS;
			if(args[0].equals("-java")) lang = JAVA;
			filename = args[1];
		}
		else {
			System.out.println("Usage: xfsw (-ansic | -c++ | -java) xflfile");
			System.exit(-1);
		}
		XflParser xflparser = new XflParser();
		Specification spec = xflparser.parse(filename);
		if(spec == null) { System.out.println(xflparser.resume()); System.exit(-1); }
		Xfsw compiler = new Xfsw(spec, null, lang);
		System.out.println(compiler.getMessage());
		System.exit(0);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Método que describe el resultado
	 */
	public String getMessage() {
		return this.msg;
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Genera la descripción en ANSI-C
	 */
	private String generateC(Specification spec, File wdir) {
		String msg = "";
		try {
			String msgh = XfswCHeader.create(spec,wdir);
			String msgc = XfswCCode.create(spec,wdir);
			msg += "C code generation describing the fuzzy system "+spec.getName();
			msg += " has been successfully finished."+eol;
			msg += "C description is included in files: "+eol;
			msg += "        \""+msgh+"\","+eol;
			msg += "        and \""+msgc+"\".";
		} catch(XflException ex) {
			msg += "Fatal error generating a C description.";
		}
		return msg;
	}
}
