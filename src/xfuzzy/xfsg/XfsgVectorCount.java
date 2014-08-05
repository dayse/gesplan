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
 * Esta clase es un contador especial para recorrer la memoria de reglas
 * (similar a xfvhdl/XfvhdlVectorCount.java)
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgVectorCount {

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ATRIBUTOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	private int lengh;

	// Contiene para cada variable el número de veces que se puede restar
	private int vector[];

	private int vector2[];

	// private int max;
	private int initial[][];

	private int lengh_real;

	private boolean valid;

	private int var_restas[];

	// variable que indica si los parámetros de entrada no son correctos.
	private boolean error = false;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTOR DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	 * Constructor de XVectorCount
	 * 
	 * @param lengh
	 *            Longitud del vector
	 * @param valor
	 *            Valor máximo que tendrán las casillas del vector
	 * @param initial[]
	 *            Valores iniciales de las casillas del vector
	 */
	public XfsgVectorCount(int lengh, int[] valor, int initial[][]) {

		// imprime_vector(initial);

		for (int ii = 0; ii < initial.length && !error; ii++) {
			if (initial[ii][0] > initial[ii][1])
				error = true;
		}
		if (error) {
			new XfsgError(8, " ");
		} else {
			// verifica si todas las variables tienen una función de
			// pertenencia asociada
			boolean n = false;
			for (int ii = 0; ii < initial.length && !n; ii++) {
				if (initial[ii][0] == -1 || initial[ii][0] == -2
						|| (initial[ii][0] != initial[ii][1]))
					n = true;
			}

			// indica si el vector se puede restar o no
			if (!n)
				valid = false;
			else
				valid = true;

			lengh_real = lengh;

			int tmp = 0;
			for (int j = 0; j < initial.length; j++) {
				if (initial[j][0] == -1 || initial[j][0] == -2
						|| (initial[j][0] != initial[j][1])) {
					tmp++;
				}
			}

			var_restas = new int[tmp];

			int tmp2 = 0;
			for (int j = 0; j < initial.length; j++) {
				if (initial[j][0] == -1 || initial[j][0] == -2
						|| (initial[j][0] != initial[j][1])) {
					var_restas[tmp2] = j;
					tmp2++;
				}
			}

			this.lengh = tmp;

			vector = new int[this.lengh];
			vector2 = new int[this.lengh];

			this.initial = initial;
			tmp = 0;
			for (int i = 0; i < initial.length; i++) {
				if (initial[i][0] == -1) {
					vector[tmp] = valor[i] - 1;
					vector2[tmp] = valor[i] - 1;
					tmp++;
				} else if (initial[i][0] == -2) {
					if (initial[i][1] == valor[i] - 1) {
						vector[tmp] = valor[i] - 2;
						vector2[tmp] = valor[i] - 2;
					} else if (initial[i][1] == 0) {
						vector[tmp] = valor[i] - 2;
						vector2[tmp] = valor[i] - 1;
					} else {
						vector[tmp] = valor[i] - 1;
						vector2[tmp] = valor[i] - 1;
					}
					tmp++;
				} else if (initial[i][0] != initial[i][1]) {
					vector[tmp] = initial[i][1] - initial[i][0];
					vector2[tmp] = initial[i][1] - initial[i][0];
					tmp++;
				}

			}
		}
		// imprimevector(vector);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MÉTO_DOS PÚBLICOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	 * Método para obtener el valor de la posición pos del vector
	 * 
	 * @param pos
	 *            Posición del vector que deseamos conocer
	 * @return Valor que contiene el vector en la posición pos
	 */
	public int get(int pos) {
		int res = 0;
		int tmp = 0;
		int l = 0;

		if (initial[pos][1] != -1 && (initial[pos][0] == initial[pos][1])
				&& initial[pos][0] != -2)
			res = initial[pos][1];
		else {
			while (l < pos) {
				if (initial[l][0] == -1 || (initial[l][0] != initial[l][1])
						|| initial[l][0] == -2)
					tmp++;
				l++;
			}
			if (initial[pos][0] == -1)
				res = vector2[tmp] - vector[tmp];
			else if (initial[pos][0] == -2)
				res = vector2[tmp] - vector[tmp];
			else if (initial[pos][0] != initial[pos][1])
				res = initial[pos][1] - vector[tmp];
		}
		return res;
	}

	/**
	 * Método que resta un elemento en el vector
	 * 
	 * @return Devuelve un booleano indicando si se realizó la operación con
	 *         éxito o no
	 */
	public boolean sub() {
		boolean d = true;
		boolean dec = false;
		boolean cont = true;
		int actual = lengh - 1;

		if (!valid)
			return valid;

		if (vector[actual] > 0) {
			int var = var_restas[actual];
			vector[actual]--;
			if (initial[var][0] == -2) {
				// vemos si la etiqueta se puede usar en el caso del not
				if (initial[var][1] == vector2[actual] - vector[actual]) {
					if (vector[actual] > 0) {
						vector[actual]--;
						return true;
					} else
						return false;
				}
			} else
				return true;
		} else {
			while (!dec && actual >= 0) {

				if (actual > 0) {
					actual--;
				} else {
					cont = false;
					dec = true;
					d = false;
				}

				if (cont) {

					if (vector[actual] == 0) {

					} else {
						vector[actual]--;
						int var = var_restas[actual];
						// vector[actual]--;
						if (initial[var][0] == -2) {
							// vemos si la etiqueta se puede usar en el caso del
							// not
							if (initial[var][1] == vector2[actual]
									- vector[actual]) {
								if (vector[actual] > 0) {
									vector[actual]--;
									dec = true;
									reset(actual + 1);
								} else
									dec = false;
							} else {
								dec = true;
								reset(actual + 1);
							}
						} else {
							dec = true;
							reset(actual + 1);
						}
					}
				}
			}
		}

		return d;
	}

	/**
	 * Método para imprimir por pantalla el valor actual del vector Sólo tiene
	 * utilidad este método para la depuración de esta clase
	 */
	public void print() {
		String cadena = new String("[ ");

		for (int i = 0; i < lengh_real; i++) {
			cadena += get(i) + " ";
		}

		cadena += "]";
		System.out.println("El vector va por: " + cadena);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MÉTO_DOS PRIVADOS DE LA CLASE
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**
	 * Método que decrementa el vector en la posición pos
	 */
	/*
	 * private boolean sub(int pos) { boolean d = true;
	 * 
	 * if (vector[pos] > 0) vector[pos]--; else d = false;
	 * 
	 * return d; }
	 */

	/**
	 * Método que inicializa el vector en su posición pos
	 */

	public void reset(int pos) {
		for (int i = pos; i < lengh; i++) {
			int var = var_restas[pos];
			if (initial[var][0] == -2 && initial[var][1] == 0)
				vector[i] = vector2[i] - 1;
			else
				vector[i] = vector2[i];
		}
	}

	public boolean Error() {
		return error;
	}
}
