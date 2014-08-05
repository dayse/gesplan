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
 * Interfaz que describe una etiqueta ling��stica. Esta interfaz la
 * desarrollan las clases ParametricMemFunc y FamiliarMemFunc.
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public interface LinguisticLabel extends MemFunc {
	
	//----------------------------------------------------------------------------//
	//                            M�TODOS P�BLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el nombre de la etiqueta ling��stica
	 */
	public String getLabel();
	
	/**
	 * Asigna el nombre de la etiqueta ling��stica
	 */
	public void setLabel(String label);
	
	/**
	 * Compara la cadena con la etiqueta ling��stica
	 */
	public boolean equals(String label);

	/**
	 * Obtiene el valor de la etiqueta ling��istica
	 */
	public String toString();
	
	/**
	 * Incrementa el contador de enlaces (usos) del objeto
	 */
	public void link();
	
	/**
	 * Decrementa el contador de enlaces (usos) del objeto
	 */
	public void unlink();
	
	/**
	 * Estudia si el objeto esta siendo utilizado
	 */
	public boolean isLinked();
	
	/**
	 * Genera la descripci�n XFL3 de la etiqueta ling��stica
	 */
	public String toXfl();
	
	/**
	 * Elimina los enlaces del objeto
	 */
	public void dispose();
	
	/**
	 * Calcula el grado de pertenencia de la funci�n
	 */
	public double compute(double x);
	
	/**
	 * Aplicaci�n del modificador "mayor o igual" sobre la funci�n
	 */
	public double greatereq(double x);
	
	/**
	 * Aplicaci�n del modificador "menor o igual" sobre la funci�n
	 */
	public double smallereq(double x);
	
	/**
	 * Obtiene el valor por defecto del centro de la funci�n
	 */
	public double center();
	
	/**
	 * Obtiene el valor por defecto de la base de la funci�n
	 */
	public double basis();
	
	/**
	 * Derivada del grado de pertenencia
	 */
	public double[] deriv_eq(double x) throws XflException;
	
	/**
	 * Derivada del modificador "mayor o igual"
	 */
	public double[] deriv_greq(double x) throws XflException;
	
	/**
	 * Derivada del modificador "menor o igual"
	 */
	public double[] deriv_smeq(double x) throws XflException;
	
	/**
	 * Derivada del centro de la funci�n
	 */
	public double[] deriv_center() throws XflException;
	
	/**
	 * Derivada de la base de la funci�n
	 */
	public double[] deriv_basis() throws XflException;
	
	/**
	 * Obtiene el l�mite inferior del universo de discurso
	 */
	public double min();
	
	/**
	 * Obtiene el l�mite superior del universo de discurso
	 */
	public double max();
	
	/**
	 * Obtiene la separaci�n entre los puntos del universo
	 */
	public double step();
	
	/**
	 * Verifica si la descripci�n de la funcion es correcta
	 */
	public boolean test();
	
	/**
	 * Verifica si alg�n par�metro de la funci�n es ajustable
	 */
	public boolean isAdjustable();
	
	/**
	 * Obtiene los valores de los par�metros de la funci�n
	 */
	public double[] get();
	
	/**
	 * A�ade un valor a la derivada de un par�metro
	 */
	public void addDeriv(int index, double deriv);
	
}
