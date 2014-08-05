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
 * Descripci�n de un par�metro en una funci�n param�trica
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class Parameter implements Cloneable {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Valor del par�metro
	 */
	public double value;
	
	/**
	 * Estimaci�n de la derivada de cada salida respecto al par�metro
	 */
	public double[] oderiv;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre del par�metro
	 */
	private String name;
	
	/**
	 * Indicador para saber si se debe ajustar en un proceso de aprendizaje
	 */
	private boolean adjustable = true;
	
	/**
	 * Copias de antiguos valores del par�metro
	 */
	private double[] copy = new double[0];
	
	/**
	 * Derivada del error respecto al par�metro
	 */
	private double deriv;
	
	/**
	 * Derivada del error respecto al par�metro en una iteraci�n previa
	 */
	private double prevderiv;
	
	/**
	 * Desplazamiento a realizar sobre el valor del par�metro
	 */
	private double desp;
	
	/**
	 * Desplazamiento realizado en una iteraci�n previa
	 */
	private double prevdesp;
	
	/**
	 * Campo para almacenar informaci�n auxiliar de diferentes algoritmos
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
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() { 
		try { return super.clone(); }
		catch (CloneNotSupportedException e) { return null; }
	}

	//----------------------------------------------------------------------------//
	// M�todos de asignaci�n                                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el valor del par�metro
	 */
	public void set(double value) {
		this.value = value;
	}

	/**
	 * Asigna el valor de la derivada del par�metro
	 */
	public void setDeriv(double deriv) {
		this.deriv = deriv;
	}

	/**
	 * Asigna el valor de la derivada previa del par�metro
	 */
	public void setPrevDeriv(double prevderiv) {
		this.prevderiv = prevderiv;
	}

	/**
	 * Asigna el valor del desplazamiento del par�metro
	 */
	public void setDesp(double desp) {
		this.desp = desp;
	}

	/**
	 * Asigna el valor del desplazamiento previo del par�metro
	 */
	public void setPrevDesp(double prevdesp) {
		this.prevdesp = prevdesp;
	}

	/**
	 * Asigna el valor del campo miscelaneo del par�metro
	 */
	public void setMisc(double misc) {
		this.misc = misc;
	}

	/**
	 * Asigna el valor del car�cter ajustable del par�metro
	 */
	public void setAdjustable(boolean enable) {
		this.adjustable = enable;
	}

	//----------------------------------------------------------------------------//
	// M�todos de obtenci�n de valores                                            //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el valor del nombre del par�metro
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Obtiene el valor del par�metro
	 */
	public double get() {
		return this.value;
	}

	/**
	 * Obtiene el valor de la derivada del par�metro
	 */
	public double getDeriv() {
		return this.deriv;
	}

	/**
	 * Obtiene el valor del desplazamiento del par�metro
	 */
	public double getDesp() {
		return this.desp;
	}

	/**
	 * Obtiene el valor de la derivada previa del par�metro
	 */
	public double getPrevDeriv() {
		return this.prevderiv;
	}

	/**
	 * Obtiene el valor del desplazamiento previo del par�metro
	 */
	public double getPrevDesp() {
		return this.prevdesp;
	}

	/**
	 * Obtiene el valor del campo miscelaneo del par�metro
	 */
	public double getMisc() {
		return this.misc;
	}

	/**
	 * Estudia si el par�metro es ajustable
	 */
	public boolean isAdjustable() {
		return this.adjustable;
	}

	//----------------------------------------------------------------------------//
	// M�todos de almacenamiento de la derivada                                   //
	//----------------------------------------------------------------------------//

	/**
	 * A�ade un valor a la derivada del par�metro
	 */
	public void addDeriv(double deriv) {
		this.deriv += deriv;
	}

	/**
	 * Almacena y reinicializa la derivada del par�metro
	 */
	public void forward() {
		prevderiv=deriv;
		deriv=0;
	}

	/**
	 * Almacena una copia del valor del par�metro
	 */
	public void backup(int i) {
		copy[i] = value;
	}

	/**
	 * Obtiene un valor almacenado del valor del par�metro
	 */
	public double restore(int i) {
		return copy[i];
	}

	/**
	 * A�ade un nuevo valor a los almacenados
	 */
	public void backup() {
		double aux[] = new double[copy.length+1];
		System.arraycopy(copy,0,aux,0,copy.length);
		aux[copy.length] = value;
		copy = aux;
	}

}
