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

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//REGLA				//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.lang;

public class Rule {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Antecedente de la regla
	 */
	private Relation premise;
	
	/**
	 * Lista de consecuentes de la regla
	 */
	private Conclusion conclusion[];
	
	/**
	 * Grado de certeza (verosimilitud) de la regla
	 */
	private double degree = 1;
	
	/**
	 * Máximo grado de activación alcanzado por la regla
	 */
	private double maxactivation = 0;
	
	/**
	 * Indicador de si la regla tiene componentes con parámetros a ajustar
	 */
	private boolean adjustable = true;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor basado en el antecedente
	 */
	public Rule(Relation premise) {
		this.premise = premise;
		this.conclusion = new Conclusion[0];
	}

	/**
	 * Constructor basado en el antecedente y el grado de certeza
	 * @param premise
	 * @param degree
	 */
	public Rule(Relation premise, double degree) {
		this.degree = degree;
		this.premise = premise;
		this.conclusion = new Conclusion[0];
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Rulebase rb) {
		Relation premiseclone = (Relation) premise.clone(rb);
		Rule clone = new Rule(premiseclone,this.degree);
		for(int i=0; i<conclusion.length; i++)
			clone.add( (Conclusion) conclusion[i].clone(rb) );
		return clone;
	}

	/**
	 * Elimina los enlaces de los miembros para borrar la regla
	 */
	public void dispose() {
		this.premise.dispose();
		for(int i=0; i<this.conclusion.length; i++) this.conclusion[i].dispose();
	}

	/**
	 * Obtiene el grado de certeza de la regla
	 */
	public double getDegree() {
		return this.degree;
	}

	/**
	 * Obtiene el antecedente de la regla
	 */
	public Relation getPremise() {
		return this.premise;
	}

	/**
	 * Obtiene la lista de consecuentes de la regla
	 */
	public Conclusion[] getConclusions() {
		return this.conclusion;
	}

	/**
	 * Intercambia dos funciones de pertenencia en la regla
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		premise.exchange(oldmf,newmf);
		for(int i=0; i<conclusion.length; i++) conclusion[i].exchange(oldmf,newmf);
	}

	/**
	 * Intercambia dos variables en la regla
	 */
	public void exchange(Variable oldvar, Variable newvar) {
		premise.exchange(oldvar,newvar);
		for(int i=0; i<conclusion.length; i++) conclusion[i].exchange(oldvar,newvar);
	}

	/**
	 * Añade un consecuente a la regla	
	 */
	public void add(Conclusion ncc) {
		Conclusion acc[] = new Conclusion[this.conclusion.length+1];
		System.arraycopy(this.conclusion,0,acc,0,this.conclusion.length);
		acc[this.conclusion.length] = ncc;
		this.conclusion = acc;
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan código                                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripción XFL3 de la regla
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		String code = "  ";
		if(degree != 1.0) code += "["+degree+"]";
		code += "if(";
		code += this.premise.toXfl();
		code += ") -> ";
		code += this.conclusion[0].toXfl();
		for(int i=1; i<this.conclusion.length; i++) 
			code += ", "+this.conclusion[i].toXfl();
		code += ";"+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	// Métodos de ejecución de la inferencia                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Ejecuta la inferencia de la regla
	 */
	public double compute() throws XflException {
		double activation = this.degree * this.premise.compute();
		if(activation > maxactivation ) maxactivation = activation;
		for(int i=0; i<this.conclusion.length; i++)
			this.conclusion[i].compute(activation);
		return activation;
	}

	/**
	 * Calcula la derivada de los miembros del antecedente
	 */
	public void derivative() throws XflException {
		if(!adjustable) return;
		double deriv = 0;
		for(int i=0; i<this.conclusion.length; i++)
			deriv += this.conclusion[i].getDegreeDeriv();
		deriv = deriv * this.degree;
		if(deriv != 0) this.premise.derivative(deriv);
	}

	/**
	 * Inicializa el grado de activación máximo
	 */
	public void resetMaxActivation() {
		this.maxactivation = 0;
	}

	/**
	 * Obtiene el grado de activación máximo
	 */
	public double getMaxActivation() {
		return this.maxactivation;
	}

	/**
	 * Analiza si la regla es ajustable
	 */
	public void setAdjustable() {
		this.adjustable = this.premise.isAdjustable();
	}

	/**
	 * Verifica si la regla es ajustable
	 */
	public boolean isAdjustable() {
		return this.adjustable;
	}
}
