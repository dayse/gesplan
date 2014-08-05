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
 * Clase que describe un universo de discurso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Universe implements Cloneable {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Límite inferior del universo de discurso
	 */
	private double min;
	
	/**
	 * Límite superior del universo de discurso
	 */
	private double max;
	
	/**
	 * Número de puntos en los que se divide el universo de discurso
	 */
	private int card;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor por defecto
	 */
	public Universe() {
		this.min = 0;
		this.max = 1;
		this.card = 256;
	}

	/**
	 * Constructor con los límites del intervalo
	 */
	public Universe(double min, double max) throws XflException{
		if( min >= max ) throw new XflException(1);
		this.min = min;
		this.max = max;
		this.card = 256;
	}

	/**
	 * Constructor con los límites y la cardinalidad
	 */
	public Universe(double min, double max, int card) throws XflException{
		if( min >= max ) throw new XflException(1);
		if( card <=1 ) throw new XflException(2);
		this.min = min;
		this.max = max;
		this.card = card;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el valor del límite inferior del universo
	 */
	public double min() {
		return this.min;
	}

	/**
	 * Obtiene el valor del límite superior del universo
	 */
	public double max() {
		return this.max;
	}

	/**
	 * Obtiene el valor de la cardinalidad del universo
	 */
	public int card() {
		return this.card;
	}

	/**
	 * Asigna los valores de descripción del universo
	 */
	public void set(double min, double max, int card) throws XflException {
		if( min >= max ) throw new XflException(1);
		if( card <=1 ) throw new XflException(2);
		this.min = min;
		this.max = max;
		this.card = card;
	}

	/**
	 * Verifica si un dato se encuentra fuera del universo
	 */
	public boolean outside(double x) {
		return ( x<this.min || x>this.max );
	}

	/**
	 * Obtiene el tamaño del intervalo del universo
	 */
	public double range() {
		return (this.max - this.min);
	}

	/**
	 * Obtiene un punto a partir de un valor relativo
	 */
	public double point(double x) {
		return (1-x)*this.min + x*this.max;
	}

	/**
	 * Obtiene un valor relativo a partir de un punto
	 */
	public double getRate(double x) {
		return (x-min)/(max-min);
	}

	/**
	 * Obtiene la separación entre los puntos del universo
	 */
	public double step(){
		return (this.max - this.min)/(this.card - 1);
	}

	/**
	 * Obtiene la separacion entre los puntos del universo
	 */
	public double grain(){
		return (this.max - this.min)/(this.card - 1);
	}

	/**
	 * Obtiene el número de bits necesario para su representación
	 */
	public int bits(){
		double log2 = Math.log(this.card)/Math.log(2);
		return (int) Math.ceil( log2 );
	}

	/**
	 * Genera la representación XFL3 del universo
	 */
	public String toXfl() {
		return "["+min+","+max+";"+card+"]";
	}

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		try { return super.clone(); }
		catch (CloneNotSupportedException e) { return null; }
	}
}

