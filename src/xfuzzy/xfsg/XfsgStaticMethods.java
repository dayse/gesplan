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

import xfuzzy.lang.*;

/**
 * Clase que implementa dos métodos estáticos que pueden ser utilizados por las
 * otras clases que aparecen en el proyecto 
 * (similar a xfvhdl/XfvhdlStaticMethods.java)
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgStaticMethods {

	/**
	 * 
	 * @param x
	 * @return el ceil del log en base 2 de x.
	 */
	public static int ceillog2(int x) {
		return (int) Math.ceil(Math.log((double) x) / Math.log((double) 2));
	}

	/**
	 * 
	 * @param mc
	 * @return Devuelve el número máximo de funciones de pertenencia en un
	 *         ModuleCall con varias entradas. Se puede dar el caso de que las
	 *         entradas tengan diferente número de funciones de pertencia.
	 */
	public static int CalculaMaximon_fp(ModuleCall mc) {
		int res = 0;
		if (mc instanceof RulebaseCall) {
			Variable var[] = ((RulebaseCall) mc).getRulebase().getInputs();
			int n_fps[] = new int[var.length];
			for (int i = 0; i < var.length; i++) {
				n_fps[i] = var[i].getType().getAllMembershipFunctions().length;
			}
			res = n_fps[0];
			for (int i = 1; i < n_fps.length; i++) {
				if (n_fps[i] > res) {
					res = n_fps[i];
				}
			}
		}

		return res;
	}
}
