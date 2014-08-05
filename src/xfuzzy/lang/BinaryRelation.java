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
 * Clase que describe una relaci�n difusa binaria
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class BinaryRelation extends Relation {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Tipo de relaci�n: AND o OR
	 */
	private int kind;
	
	/**
	 * Base de reglas de la que tomar los operadores
	 */
	private Rulebase rb;
	
	/**
	 * Operando izquierdo de la relaci�n
	 */
	private Relation l;
	
	/**
	 * Operando derecho de la relaci�n
	 */
	private Relation r;
	
	/**
	 * Grado de activaci�n del operando izquierdo calculado previamente
	 */
	private double ldegree;
	
	/**
	 * Gradop de activaci�n del operando derecho calculado previamente
	 */
	private double rdegree;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public BinaryRelation(int kind, Relation l, Relation r, Rulebase rb) {
		this.kind = kind;
		this.rb = rb;
		this.l = l;
		this.r = r;
	}
	
	//----------------------------------------------------------------------------//
	//                            M�TODOS P�BLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Rulebase rbclone) {
		Relation lclone = (Relation) this.l.clone(rbclone);
		Relation rclone = (Relation) this.r.clone(rbclone);
		return new BinaryRelation(kind,lclone,rclone,rbclone);
	}
	
	/**
	 * Verifica que la proposici�n sea ajustable
	 */
	public boolean isAdjustable() {
		return (l.isAdjustable() || r.isAdjustable());
	}
	
	/**
	 * Elimina los enlaces para poder eliminar la proposici�n
	 */
	public void dispose() {
		if(this.l != null) this.l.dispose(); this.l = null;
		if(this.r != null) this.r.dispose(); this.r = null;
	}
	
	/**
	 * Sustituye una funci�n de pertenencia por otra
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		this.l.exchange(oldmf,newmf);
		this.r.exchange(oldmf,newmf);
	}
	
	/**
	 * Sustituye una variable por otra
	 */
	public void exchange(Variable oldvar, Variable newvar) {
		this.l.exchange(oldvar,newvar);
		this.r.exchange(oldvar,newvar);
	}
	
	/**
	 * Obtiene el tipo de proposici�n
	 */
	public int getKind() {
		return this.kind;
	}
	
	/**
	 * Obtiene la referencia a la variable de la proposici�n
	 */
	public Variable getVariable() {
		return null;
	}
	
	/**
	 * Asigna la referencia a la variable de la proposici�n
	 */
	public void setVariable(Variable var) {
	}
	
	/**
	 * Obtiene la referencia a la MF de la proposici�n
	 */
	public LinguisticLabel getMembershipFunction() {
		return null;
	}
	
	/**
	 * Asigna la referencia a la MF de la proposici�n
	 */
	public void setMembershipFunction(LinguisticLabel mf) {
	}
	
	/**
	 * Obtiene la referencia a la componente izquierda
	 */
	public Relation getLeftRelation() {
		return this.l;
	}
	
	/**
	 * Asigna la referencia a la componente izquierda
	 */
	public void setLeftRelation(Relation rel) {
		this.l = rel;
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
	
	/**
	 * Calcula el grado de activaci�n de la proposici�n
	 */
	public double compute() throws XflException {
		ldegree = l.compute();
		rdegree = r.compute();
		if(kind == AND) return rb.operation.and.compute(ldegree,rdegree);
		if(kind == OR) return rb.operation.or.compute(ldegree,rdegree);
		return 0;
	}
	
	/**
	 * Calcula la derivada de la proposici�n
	 */
	public void derivative(double de) throws XflException {
		double deriv[];
		if(kind == AND) deriv = rb.operation.and.derivative(ldegree,rdegree);
		else deriv = rb.operation.or.derivative(ldegree,rdegree);
		if(deriv[0] != 0) l.derivative(de*deriv[0]);
		if(deriv[1] != 0) r.derivative(de*deriv[1]);
	}
	
	/**
	 * Genera la descripci�n XFL3 de la proposici�n
	 */
	public String toXfl() {
		String code = "";
		if(parenthesis(this.l)) code += "("+this.l.toXfl()+")";
		else code += this.l.toXfl();
		if(kind == AND) code += " & ";
		else code += " | ";
		if(parenthesis(this.r)) code += "("+this.r.toXfl()+")";
		else code += this.r.toXfl();
		return code;
	}
	
	//----------------------------------------------------------------------------//
	//                            M�TODOS PRIVADOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Estudia si describir entre parentesis la proposici�n
	 */
	private boolean parenthesis(Relation rel) {
		if(rel.getKind() == kind) return false;
		if(rel instanceof SingleRelation) return false;
		return true;
	}
}

