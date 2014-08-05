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

/**
 * Excepciones del lenguaje XFL3
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XflException extends Exception{

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603100L;

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Mensajes de error
	 */
	final static private String msg[] = {
		/*  0 */ "Undefined exception",
		/*  1 */ "Universe limits not properly defined",
		/*  2 */ "Cardinality not properly defined",
		/*  3 */ "Operator not found",
		/*  4 */ "Cannot instantiate the selected operator",
		/*  5 */ "Selected name does not correspond to any fuzzy operator",
		/*  6 */ "Membership function not found",
		/*  7 */ "Cannot instantiate the selected membership function",
		/*  8 */ "Membership function already defined",
		/*  9 */ "Operatorset already defined",
		/* 10 */ "Operatorset not found",
		/* 11 */ "Type already defined",
		/* 12 */ "Type not found",
		/* 13 */ "Rulebase already defined",
		/* 14 */ "Rulebase not found",
		/* 15 */ "Variable already defined",
		/* 16 */ "Variable not found",
		/* 17 */ "Package not found",
		/* 18 */ "Variable not valued",
		/* 19 */ "Derivative not supported",
		/* 20 */ "Unknown rulebase",
		/* 21 */ "Wrong number of arguments",
		/* 22 */ "Invalid argument. Unvalued inner variable",
		/* 23 */ "Invalid argument. Variable cannot be modified",
		/* 24 */ "System module already defined",

		/* 25 */ "Error Function not properly defined",
		/* 26 */ "Learning Algorithm not properly defined",
		/* 27 */ "Option not supported",
		/* 28 */ "Algorithm not found",
		/* 29 */ "Option not found",
		/* 30 */ "Cannot set option before selecting learning algorithm",
		/* 31 */ "File not found",

		/* 32 */ "Cannot instantiate class",
		/* 33 */ "Errors reading pattern file",

		/* 34 */ "Wrong number of parameters in function definition",
		/* 35 */ "Parameter values not allowed",
		/* 36 */ "Cannot instantiate the selected membership function family",
		/* 37 */ "Family not found",
		/* 38 */ "Membership function family already defined",
		/* 39 */ "Crisp block set already defined",
		/* 40 */ "Cannot instantiate the selected crisp block",
		/* 41 */ "Crisp block already defined"
	};

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código del error
	 */
	private int code;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor de error indefinido
	 */
	public XflException() {
		this.code = 0;
	}

	/**
	 * Constructor con código de error
	 * 
	 * @param code
	 */
	public XflException(int code) {
		this.code = code;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Devuelve el mensaje de la excepción
	 */
	public String toString() {
		return msg[ this.code ];
	}

	/**
	 * Devuelve el mensaje de la excepción
	 */
	public String getMessage() {
		return msg[code];
	}

	/**
	 * Devuelve el mensaje correspondiente a un código
	 */
	public static String getMessage(int code) {
		return msg[code];
	}
}

