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
import java.util.*;

/**
 * Clase que gestiona la lista ordenada de funciones de pertenencia 
 * (similar a xfvhdl/XfvhdlInOrderParamMemFunc)
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgInOrderMemFunc {

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ATRIBUTOS PRIVADOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	private List<LinguisticLabel> mf_sorted;

	private LinguisticLabel[] tmp;

	private Variable v;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTOR DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	 * Constructor de XfsgInOrderParamMemFunc
	 */
	public XfsgInOrderMemFunc(Variable v) {
		this.mf_sorted = new ArrayList<LinguisticLabel>();
		this.tmp = v.getType().getParamMembershipFunctions();
		if (tmp.length == 0)
			this.tmp = v.getType().getFamiliarMembershipFunctions();
		this.v = v;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MÉTO_DOS PUBLICOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	 * Método que devuelve la lista de funciones de pertenencia
	 * 
	 * @return Lista que contiene las funciones de pertenencia
	 */
	public List<LinguisticLabel> getInOrderMemFunc() {
		return mf_sorted;
	}

	/**
	 * Método que devuelve la variable de entrada
	 * 
	 * @return La variable de entrada
	 */
	public Variable getVariable() {
		return v;
	}

	/**
	 * Método que devuelve el número de funciones de pertenencia
	 * 
	 * @return Número de funciones de pertenencia
	 */
	public int getSize() {
		return mf_sorted.size();
	}

	/**
	 * Método que devuelve la función de pertenencia indexada por i
	 * 
	 * @return Función de pertenencia i
	 */

	public LinguisticLabel getMemFunc(int i) {
		return mf_sorted.get(i);
	}

	public LinguisticLabel getMemFunc2(int i) {
		return tmp[i];
	}

	/**
	 * Método que devuelve la posición en la lista de una función de pertenencia
	 * dada.
	 * 
	 * @param mf
	 *            Función de pertenencia de la que queremos saber su posición
	 * @return Índice de la funcion de pertenencia dentro de la lista
	 */
	public int getIndex(LinguisticLabel mf) {
		int index = -1;
		boolean enc = false;
		LinguisticLabel aux;

		for (int i = 0; i < mf_sorted.size() && !enc; i++) {
			aux = mf_sorted.get(i);
			if (aux.equals(mf.getLabel())) {
				index = i;
				enc = true;
			}
		}

		return index;
	}

	/**
	 * 
	 * Método que ordena la lista de funciones de pertenencia
	 */
	public List<LinguisticLabel> sort() {
		boolean enc = false;

		for (int i = 0; i < tmp.length; i++) {
			if (mf_sorted.size() == 0) {
				mf_sorted.add(tmp[i]);
				enc = true;
			}

			for (int j = 1; j <= mf_sorted.size() && !enc; j++) {
				double a, b;
				a = opMemFuncParameter(mf_sorted.get(j - 1), "min");
				b = opMemFuncParameter(tmp[i], "min");
				if (a > b) {
					mf_sorted.add(j - 1, tmp[i]);
					enc = true;
				}
			}

			if (!enc) {
				mf_sorted.add(tmp[i]);
			}

			enc = false;
		}

		return mf_sorted;
	}

	/**
	 * Método que devuelve el valor del menor parámetro de una función de
	 * pertenencia si se le pasa "min" o el máximo si se le pasa "max" cuando la
	 * función de pertenencia es libre. Si la función de pertenencia pertenece a
	 * una familia se devuelve el índice de dicha función dentro de la familia.
	 * 
	 * @param p
	 *            Función de pertenencia de la que queremos saber su valor
	 *            máximo o mínimo
	 * @param op
	 *            Cadena "max" si buscamos el valor máximo o "min" si buscamos
	 *            el mínimo
	 * @return Valor buscado
	 */
	public double opMemFuncParameter(LinguisticLabel p, String op) {

		double res;

		if (p instanceof ParamMemFunc) {
			double param[] = p.get();
			res = param[0];

			if (op.equalsIgnoreCase("min")) {
				for (int i = 1; i < param.length; i++) {
					if (param[i] < res)
						res = param[i];
				}
			} else if (op.equalsIgnoreCase("max")) {
				for (int i = 1; i < param.length; i++) {
					if (param[i] > res)
						res = param[i];
				}
			}
		} else {
			res = ((FamiliarMemFunc) p).getIndex();
		}

		return res;
	}
}
