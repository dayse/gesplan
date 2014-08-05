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


package xfuzzy.xfcpp;

import xfuzzy.lang.*;
import java.io.*;

/**
 * Herramienta que genera una descripción del sistema difuso en C++
 * 
 * @author Francisco José Moreno Velo 
 *
 */
public class Xfcpp {

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
	public Xfcpp(Specification spec, File dir) {
		File wdir = null;
		if(dir == null) wdir = spec.getFile().getParentFile();
		else wdir = dir;
		this.msg = "";
		try {
			String msgsc = XfcppSpecCppCode.create(spec,wdir);
			String msgsh = XfcppSpecHppCode.create(spec,wdir);
			String msgxh = XfcppXfuzzyHppCode.create(wdir);
			String msgxc = XfcppXfuzzyCppCode.create(wdir);
			msg += "C++ code generation describing the fuzzy system "+spec.getName();
			msg += " has been successfully finished."+eol;
			msg += "C++ description is included in files: "+eol;
			msg += "        \""+msgxh+"\","+eol;
			msg += "        \""+msgxc+"\","+eol;
			msg += "        \""+msgsh+"\","+eol;
			msg += "        and \""+msgsc+"\".";
		} catch(XflException ex) {
			msg += "Fatal error generating a C++ description.";
		}
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Ejecución externa
	 */
	public static void main(String args[]) {
		String filename = "";
		if(args.length == 1) filename = args[0];
		else {
			System.out.println("Usage: xfc xflfile");
			System.exit(-1);
		}
		XflParser xflparser = new XflParser();
		Specification spec = xflparser.parse(filename);
		if(spec == null) { System.out.println(xflparser.resume()); System.exit(-1); }
		Xfcpp compiler = new Xfcpp(spec,null);
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
		return msg;
	}

}
