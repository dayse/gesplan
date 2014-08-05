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
 * Clase que describe una proposición compuesta de un operador unario
 * aplicado sobre otra proposición
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class UnaryRelation extends Relation {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Tipo de proposición unaria
	 */
	private int kind;
	
	/**
	 * Referencia a la base de reglas para poder utilizar su conjunto de operadores
	 */
	private Rulebase rb;
	
	/**
	 * Proposición a la que se aplica la operación unaria
	 */
	private Relation r;
	
	/**
	 * Grado de activación de la proposición en un proceso de inferencia
	 */
	private double degree;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public UnaryRelation(int kind, Relation r, Rulebase rb) {
		this.kind = kind;
		this.rb = rb;
		this.r = r;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Rulebase rbclone) {
		Relation rclone = (Relation) this.r.clone(rbclone);
		return new UnaryRelation(kind,rclone,rbclone);
	}

	/**
	 * Verifica que la proposición sea ajustable
	 */
	public boolean isAdjustable() {
		return r.isAdjustable();
	}

	/**
	 * Elimina los enlaces para poder eliminar la proposición
	 */
	public void dispose() {
		if(this.r != null) this.r.dispose();
	}

	/**
	 * Sustituye una función de pertenencia por otra
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		this.r.exchange(oldmf,newmf);
	}

	/**
	 * Sustituye una variable por otra
	 */
	public void exchange(Variable oldvar, Variable newvar) {
		this.r.exchange(oldvar,newvar);
	}

	//----------------------------------------------------------------------------//
	// Métodos de edición de los campos                                           //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el tipo de proposicion
	 */
	public int getKind() {
		return kind;
	}

	/**
	 *  Obtiene la referencia a la variable de la proposición
	 */
	public Variable getVariable() {
		return null;
	}

	/**
	 * Asigna la referencia a la variable de la proposición
	 */
	public void setVariable(Variable var) {
	}

	/**
	 * Obtiene la referencia a la MF de la proposición
	 */
	public LinguisticLabel getMembershipFunction() {
		return null;
	}

	/**
	 * Asigna la referencia a la MF de la proposición
	 */
	public void setMembershipFunction(LinguisticLabel mf) {
	}

	/**
	 * Obtiene la referencia a la componente izquierda
	 */
	public Relation getLeftRelation() {
		return this.r;
	}

	/**
	 * Asigna la referencia a la componente izquierda
	 */
	public void setLeftRelation(Relation rel) {
		this.r = rel;
	}

	/**
	 * Obtiene la referencia a la componente derecha
	 */
	public Relation getRightRelation() {
		return this.r;
	}

	/**
	 * Asigna la referencia a la componente derecha
	 */
	public void setRightRelation(Relation rel) {
		this.r = rel;
	}

	//----------------------------------------------------------------------------//
	// Métodos de cálculo de la proposición                                       //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula el grado de activación de la proposición
	 */
	public double compute() throws XflException {
		degree = r.compute();
		switch(kind) {
			case NOT : return rb.operation.not.compute(degree);
			case MoL : return rb.operation.moreorless.compute(degree);
			case VERY : return rb.operation.very.compute(degree);
			case SLIGHTLY : rb.operation.slightly.compute(degree);
			default : return 0;
		}
	}

	/**
	 * Calcula la derivada de la proposición
	 */
	public void derivative(double de) throws XflException {
		double deriv = 0;
		switch(kind) {
			case NOT : deriv = rb.operation.not.derivative(degree); break;
			case MoL : deriv = rb.operation.moreorless.derivative(degree); break;
			case VERY : deriv = rb.operation.very.derivative(degree); break;
			case SLIGHTLY : deriv = rb.operation.slightly.derivative(degree); break;
		}
		if(deriv != 0) r.derivative(deriv*de);
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan código                                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripcion XFL3 de la proposicion
	 */
	public String toXfl() {
		switch(kind) {
			case NOT : return "!("+this.r.toXfl()+")";
			case MoL : return "~("+this.r.toXfl()+")";
			case VERY : return "+("+this.r.toXfl()+")";
			case SLIGHTLY : return "%("+this.r.toXfl()+")";
			default : return "";
		}
	}
}

