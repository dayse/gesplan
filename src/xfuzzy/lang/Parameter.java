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
 * Descripción de un parámetro en una función paramétrica
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Parameter implements Cloneable {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Valor del parámetro
	 */
	public double value;
	
	/**
	 * Estimación de la derivada de cada salida respecto al parámetro
	 */
	public double[] oderiv;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre del parámetro
	 */
	private String name;
	
	/**
	 * Indicador para saber si se debe ajustar en un proceso de aprendizaje
	 */
	private boolean adjustable = true;
	
	/**
	 * Copias de antiguos valores del parámetro
	 */
	private double[] copy = new double[0];
	
	/**
	 * Derivada del error respecto al parámetro
	 */
	private double deriv;
	
	/**
	 * Derivada del error respecto al parámetro en una iteración previa
	 */
	private double prevderiv;
	
	/**
	 * Desplazamiento a realizar sobre el valor del parámetro
	 */
	private double desp;
	
	/**
	 * Desplazamiento realizado en una iteración previa
	 */
	private double prevdesp;
	
	/**
	 * Campo para almacenar información auxiliar de diferentes algoritmos
	 * de ajuste
	 */
	private double misc;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Parameter(String name) {
		this.name = name;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() { 
		try { return super.clone(); }
		catch (CloneNotSupportedException e) { return null; }
	}

	//----------------------------------------------------------------------------//
	// Métodos de asignación                                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el valor del parámetro
	 */
	public void set(double value) {
		this.value = value;
	}

	/**
	 * Asigna el valor de la derivada del parámetro
	 */
	public void setDeriv(double deriv) {
		this.deriv = deriv;
	}

	/**
	 * Asigna el valor de la derivada previa del parámetro
	 */
	public void setPrevDeriv(double prevderiv) {
		this.prevderiv = prevderiv;
	}

	/**
	 * Asigna el valor del desplazamiento del parámetro
	 */
	public void setDesp(double desp) {
		this.desp = desp;
	}

	/**
	 * Asigna el valor del desplazamiento previo del parámetro
	 */
	public void setPrevDesp(double prevdesp) {
		this.prevdesp = prevdesp;
	}

	/**
	 * Asigna el valor del campo miscelaneo del parámetro
	 */
	public void setMisc(double misc) {
		this.misc = misc;
	}

	/**
	 * Asigna el valor del carácter ajustable del parámetro
	 */
	public void setAdjustable(boolean enable) {
		this.adjustable = enable;
	}

	//----------------------------------------------------------------------------//
	// Métodos de obtención de valores                                            //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el valor del nombre del parámetro
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Obtiene el valor del parámetro
	 */
	public double get() {
		return this.value;
	}

	/**
	 * Obtiene el valor de la derivada del parámetro
	 */
	public double getDeriv() {
		return this.deriv;
	}

	/**
	 * Obtiene el valor del desplazamiento del parámetro
	 */
	public double getDesp() {
		return this.desp;
	}

	/**
	 * Obtiene el valor de la derivada previa del parámetro
	 */
	public double getPrevDeriv() {
		return this.prevderiv;
	}

	/**
	 * Obtiene el valor del desplazamiento previo del parámetro
	 */
	public double getPrevDesp() {
		return this.prevdesp;
	}

	/**
	 * Obtiene el valor del campo miscelaneo del parámetro
	 */
	public double getMisc() {
		return this.misc;
	}

	/**
	 * Estudia si el parámetro es ajustable
	 */
	public boolean isAdjustable() {
		return this.adjustable;
	}

	//----------------------------------------------------------------------------//
	// Métodos de almacenamiento de la derivada                                   //
	//----------------------------------------------------------------------------//

	/**
	 * Añade un valor a la derivada del parámetro
	 */
	public void addDeriv(double deriv) {
		this.deriv += deriv;
	}

	/**
	 * Almacena y reinicializa la derivada del parámetro
	 */
	public void forward() {
		prevderiv=deriv;
		deriv=0;
	}

	/**
	 * Almacena una copia del valor del parámetro
	 */
	public void backup(int i) {
		copy[i] = value;
	}

	/**
	 * Obtiene un valor almacenado del valor del parámetro
	 */
	public double restore(int i) {
		return copy[i];
	}

	/**
	 * Añade un nuevo valor a los almacenados
	 */
	public void backup() {
		double aux[] = new double[copy.length+1];
		System.arraycopy(copy,0,aux,0,copy.length);
		aux[copy.length] = value;
		copy = aux;
	}

}
