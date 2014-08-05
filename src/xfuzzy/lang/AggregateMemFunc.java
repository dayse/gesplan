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
 * Funci�n de pertenencia formada por la agregaci�n de un conjunto
 * de conclusiones
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class AggregateMemFunc implements MemFunc {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de conclusiones a agregar
	 */
	public ImpliedMemFunc conc[];
	
	/**
	 * Valor de las entradas, para las funciones de tipo param�trico
	 * (Takagi-Sugeno)
	 */
	public double input[];

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Universo de discurso de la funci�n de pertenencia
	 */
	private Universe u;

	/**
	 * Base de reglas de la que procede la funci�n de pertenencia agregada
	 */
	private Rulebase mod;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 * @param universe Universo de discurso de la funci�n de pertenencia
	 * @param mod Base de reglas de la que procede la funci�n de pertenencia 
	 */
	public AggregateMemFunc(Universe universe, Rulebase mod) {
		this.u = universe;
		this.mod = mod;
		this.conc = new ImpliedMemFunc[0];
	}

 	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * A�ade una nueva funci�n a la agregaci�n
	 */
	public void add(ImpliedMemFunc imf) {
		ImpliedMemFunc amf[] = new ImpliedMemFunc[this.conc.length+1];
		System.arraycopy(this.conc,0,amf,0,this.conc.length);
		amf[this.conc.length] = imf;
		this.conc = amf;
	}

	/**
	 * Calcula el grado de pertenencia de la agregaci�n 
	 */
	public double compute(double x) {
		double degree = this.conc[0].compute(x);
		for(int i=1; i<this.conc.length; i++)
			degree = this.mod.operation.also.compute(degree, this.conc[i].compute(x));
		return degree;
	}

	/**
	 * Aplica el m�todo de concreci�n a la agregaci�n
	 */
	public double defuzzify() {
		return this.mod.operation.defuz.compute(this);
	}

	/**
	 * Obtiene el m�nimo del universo de discurso de la funci�n 
	 */
	public double min() {
		return this.u.min();
	}

	/**
	 * Obtiene el m�ximo del universo de discurso de la funci�n 
	 */
	public double max() {
		return this.u.max();
	}

	/**
	 * Obtiene la divisi�n del universo de discurso de la funci�n 
	 */
	public double step() {
		return this.u.step();
	}

	/**
	 * Analiza si se trata de una agregacion de singularidades 
	 */
	public boolean isDiscrete() {
		for(int i=0; i<conc.length; i++)
			if( !(conc[i].getMF() instanceof pkg.xfl.mfunc.singleton) ) return false;
		return true;
	} 

	/**
	 * Obtiene los valores de una agregaci�n de singularidades 
	 */
	public double[][] getDiscreteValues() {
		double[][] value = new double[conc.length][2];
		for(int i=0; i<conc.length; i++) {
			value[i][0] = conc[i].getParam()[0];
			value[i][1] = conc[i].getDegree();
		}
		return value;
	}

	/**
	 * Obtiene el grado de activaci�n de una singularidad 
	 */
	public double getActivationDegree(double label) {
		double degree = 0;
		for(int i=0; i<conc.length; i++)
			if(conc[i].center() == label)
				if(degree<conc[i].getDegree()) degree = conc[i].getDegree();
		return degree;
	}
}
