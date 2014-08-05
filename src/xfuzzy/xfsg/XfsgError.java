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

/**
 * Clase que gestiona los errores que se producen (similar a
 * xfvhdl/XfvhdlError.java)
 * 
 * @author Jesús Izquierdo Tena
 */
public final class XfsgError {

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ATRIBUTOS PRIVADOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	private static String messages = new String();

	private String mensaje;

	private int cod;

	private static int errors = 0;

	private static int warnings = 0;

	// private XfsgMessage msg;// = new XfsgMessage(xfuzzy);

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTORES DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	 * Constructor de la clase XfsgError
	 * 
	 * @param c
	 *            Código del error producido
	 */
	public XfsgError(int c) {
		error(c);
		messages += "\nERROR " + cod + ": " + mensaje;
		errors++;
	}

	/**
	 * Constructor de la clase XfsgError
	 * 
	 * @param c
	 *            Código del error producido
	 * @param s
	 *            Cadena a mostrar después del error
	 */
	public XfsgError(int c, String s) {
		error(c);
		messages += "\nERROR " + cod + ": " + mensaje + " " + s;
		errors++;
	}

	/**
	 * Constructor por defecto de la clase XfsgError
	 */
	public XfsgError() {
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MÉTO_DOS PÚBLICOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public String getMessages() {
		return messages;
	}

	/**
	 * Método para generar un warning
	 * 
	 * @param c
	 *            Código del warning producido
	 */
	public void newWarning(int c) {
		error(c);
		messages += "\nWARNING " + cod + ": " + mensaje;
		warnings++;
	}

	/**
	 * Método que indica si existe algun error
	 */
	public boolean hasErrors() {
		boolean e = false;
		if (errors > 0)
			e = true;

		return e;
	}

	/**
	 * Método que indica si existe algun warning
	 */
	public boolean hasWarnings() {
		boolean e = false;
		if (warnings > 0)
			e = true;

		return e;
	}

	/**
	 * Método que inicializa los errores y los warnings
	 */
	public void resetAll() {
		messages = "";
		errors = 0;
		warnings = 0;
	}

	/**
	 * Método para generar un warning
	 * 
	 * @param c
	 *            Código del warning producido
	 * @param s
	 *            Cadena a mostrar después del warning
	 */
	public void newWarning(int c, String s) {
		error(c);
		messages += "\nWARNING " + cod + ": " + mensaje + " " + s;
		warnings++;
	}

	/**
	 * Método que muestra por pantalla o en el log todos los errores acumulados
	 */
	/*
	 * public void show() { String cad1 = new String(
	 * "\n\n------------------------------------" + "-------------\n\n Finished
	 * with 0 errors " + "and 0 warnings.\n"); String cad2 = new String(
	 * "\n\n--------------------------------------------" + "-----\n\n Finished
	 * with " + errors + " errors and " + warnings + " warnings.\n" + messages +
	 * "\n");
	 * 
	 * if (errors == 0 && warnings == 0) //msg.addMessage(cad1); else
	 * //msg.addMessage(cad2);
	 * 
	 * msg.show();
	 *  }
	 */
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MÉTO_DOS PRIVADOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	 * Método privado que genera la cadena de identificación del error o del
	 * warning
	 * 
	 * @param c
	 *            Código del error o warning producido
	 */
	private void error(int c) {
		if (c == 1) {
			cod = c;
			mensaje = new String("Invalid name system: ");
		} else if (c == 2) {
			cod = c;
			mensaje = new String("Invalid name rulebase: ");
		} else if (c == 3) {
			cod = c;
			mensaje = new String("Invalid name crisp: ");
		} else if (c == 4) {
			cod = c;
			mensaje = new String("Invalid rule: ");
		} else if (c == 5) {
			cod = c;
			mensaje = new String("Exception in defuzzification method: ");
		} else if (c == 6) {
			cod = c;
			mensaje = new String("Cannot create output directory: ");
		} else if (c == 7) {
			cod = c;
			mensaje = new String("Error in ParamMemFunc: ");
		} else if (c == 8) {
			cod = c;
			mensaje = new String("Invalid parameters: ");
		} else if (c == 9) {
			cod = c;
			mensaje = new String("The Rulebase is not complete: ");
		} else if (c == 10) {
			cod = c;
			mensaje = new String(
					"Cannot calculate the weight of the rules. ");
		} else if (c == 11) {
			cod = c;
			mensaje = new String(
					"There is not a simulink component for this rulebase. You must create it!!! Rulebase:  ");
		} else if (c == 12) {
			cod = c;
			mensaje = new String("Incorrect membership functions for input:  ");
		} else if (c == 13) {
			cod = c;
			mensaje = new String(
					"You cannot use a simplified component. Rulebase:  ");
		} else if (c == 14) {
			cod = c;
			mensaje = new String(
					"There is not a simulink component for this crisp block. You must create it!!! Crisp Block:  ");
		}
	}

} // Fin de la clase.
