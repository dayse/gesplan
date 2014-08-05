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
 * Clase que describe la conclusi�n de una regla difusa
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class Conclusion {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Variable de salida a la que se refiere la conclusi�n
	 */
	private Variable var;
	
	/**
	 * Funci�n de pertenencia de la variable de salida a la que se refiere
	 * la conclusi�n
	 */
	private LinguisticLabel mf;
	
	/**
	 * Base de reglas a la que pertenece la conclusi�n (necesaria para
	 * obtener los operadores)
	 */
	private Rulebase mod;
	
	/**
	 * Resultado de una ejecuci�n de la base de reglas
	 */
	private ImpliedMemFunc imf;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public Conclusion(Variable var, LinguisticLabel mf, Rulebase mod) {
		this.var = var;
		this.var.link();
		this.mf = mf;
		this.mf.link();
		this.mod = mod;
	}
	
	//----------------------------------------------------------------------------//
	//                            M�TODOS P�BLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Ejecuta la conclusi�n con un cierto grado de activaci�n
	 */
	public void compute(double degree) {
		this.imf = new ImpliedMemFunc(mf,mod,degree);
		this.var.addConclusion(this.imf);
	}
		
	/**
	 * Obtiene la derivada del grado de activaci�n
	 */
	public double getDegreeDeriv() {
		return this.imf.getDegreeDeriv();
	}
		
	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Rulebase rbclone) {
		return new Conclusion(this.var, this.mf, rbclone);
	}
	
	/**
	 * Elimina los enlaces del objeto
	 */
	public void dispose() {
		this.var.unlink();
		this.mf.unlink();
	}
	
	/**
	 * Obtiene la referencia a la variable de la conclusi�n
	 */
	public Variable getVariable() {
		return this.var;
	}
	
	/**
	 * Obtiene la referencia a la MF de la conclusi�n
	 */
	public LinguisticLabel getMembershipFunction() {
		return this.mf;
	}
	
	/**
	 * Asigna la variable de la conclusi�n
	 */
	public void setVariable(Variable var) {
		this.var = var;
	}
	
	/**
	 * Asigna la funci�n de pertenencia de la conclusi�n
	 */
	public void setMembershipFunction(LinguisticLabel mf) {
		this.mf = mf;
	}
	
	/**
	 * Intercambia dos funciones de pertenencia
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		if(this.mf == oldmf) {
			this.mf.unlink();
			this.mf = newmf;
			this.mf.link();
		}
	}
		
	/**
	 * Intercambia dos variables
	 */
	public void exchange(Variable oldvar, Variable newvar) {
		if(this.var == oldvar) {
			this.var.unlink();
			this.var = newvar;
			this.var.link();
		}
	}
		
	/**
	 * Descripci�n XFL3 del objeto
	 */
	public String toXfl() {
		return this.var.getName() + " = " + this.mf;
	}
	
}
