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
 * Clase que describe una función de pertenencia implicada
 * 
 * @author Francisco José Moreno Velo
 *
 */

public class ImpliedMemFunc implements MemFunc {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Base de reglas que produce la implicación
	 */
	private Rulebase mod;
	
	/**
	 * Etiqueta lingüística implicada
	 */
	private LinguisticLabel mf;
	
	/**
	 * Grado de activación de la regla
	 */
	private double degree;
	
	/**
	 * Derivada de la salida respecto al grado de activación de la regla
	 * siguiendo esta implicación
	 */
	private double derivative;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public ImpliedMemFunc(LinguisticLabel mf, Rulebase mod, double degree) {
		this.mf = mf;
		this.mod = mod;
		this.degree = degree;
		this.derivative = 0;
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Calcula el grado de pertenencia a la función
	 */
	public double compute(double x) {
		return this.mod.operation.imp.compute(this.degree, this.mf.compute(x));
	}
	
	/**
	 * Obtiene el grado de activación de la función implicada
	 */
	public double degree() {
		return this.degree;
	}
	
	/**
	 * Obtiene el valor del parámetro i-esimo de la función
	 */
	public double param(int i) {
		return (this.mf.get())[i];
	}
	
	/**
	 * Obtiene el valor del centro de la función implicada
	 */
	public double center() {
		return mf.center();
	}
	
	/**
	 * Obtiene el valor de la base de la función implicada
	 */
	public double basis() {
		return mf.basis();
	}
	
	/**
	 * Asigna el valor de la derivada del grado de activación
	 */
	public void setDegreeDeriv(double deriv) {
		this.derivative += deriv;
	}
	
	/**
	 * Asigna el valor de la derivada del parámetro i-esimo
	 */
	public void setParamDeriv(int index, double deriv) {
		this.mf.addDeriv(index,deriv);
	}
	
	/**
	 * Asigna el valor de la derivada del centro de la función
	 */
	public void setCenterDeriv(double deriv) {
		try { 
			double paramdev[] = mf.deriv_center();
			for(int i=0; i<paramdev.length; i++) mf.addDeriv(i,paramdev[i]*deriv);
		}
		catch(XflException ex) {}
	}
	
	/**
	 * Asigna el valor de la derivada de la base de la función
	 */
	public void setBasisDeriv(double deriv) {
		try { 
			double paramdev[] = mf.deriv_basis();
			for(int i=0; i<paramdev.length; i++) mf.addDeriv(i,paramdev[i]*deriv);
		}
		catch(XflException ex) {}
	}
	
	/**
	 * Obtiene el valor de los parámetros de la función
	 */
	public double[] getParam() {
		return this.mf.get();
	}
	
	/**
	 * Obtiene el valor del grado de activación de la función
	 */
	public double getDegree() {
		return this.degree;
	}
	
	/**
	 * Obtiene la derivada del grado de activación de la función
	 */
	public double getDegreeDeriv() {
		return this.derivative;
	}
	
	/**
	 * Obtiene la referencia de la función de pertenencia
	 */
	public LinguisticLabel getMF() {
		return this.mf;
	}
	
	/**
	 * Asigna el valor del grado de activación de la función
	 */
	public void setDegree(double degree) {
		this.degree = degree;
	}
	
	/**
	 * Asigna el valor de la derivada del grado de activación
	 */
	public void setDerivative(double deriv) {
		this.derivative = deriv;
	}
}
