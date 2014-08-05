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
 * Proposición difusa que puede ser simple, unaria o binaria
 * 
 * @author Francisco José Moreno Velo
 *
 */
public abstract class Relation {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PÚBLICAS                              //
	//----------------------------------------------------------------------------//

	public static final int NULL = 0;
	public static final int AND = 1;
	public static final int OR = 2;
	public static final int IS = 3;
	public static final int ISNOT = 4;
	public static final int GR_EQ = 5;
	public static final int SM_EQ = 6;
	public static final int GREATER = 7;
	public static final int SMALLER = 8;
	public static final int APP_EQ = 9;
	public static final int VERY_EQ = 10;
	public static final int SL_EQ = 11;
	public static final int NOT = 12;
	public static final int MoL = 13;
	public static final int SLIGHTLY = 14;
	public static final int VERY = 15;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Relation() {
	}

 	//----------------------------------------------------------------------------//
	//                       MÉTODOS PÚBLICOS ESTÁTICOS                           //
	//----------------------------------------------------------------------------//

	/**
	 * Crea una proposicion difusa de un cierto tipo
	 */
	public static final Relation create (int kind, Relation l, Relation r,
			Variable var, LinguisticLabel mf, Rulebase rb) {
		switch (kind) {
			case AND: 
			case OR: 
				return new BinaryRelation(kind,l,r,rb);
			case IS:
			case ISNOT:
			case SM_EQ:
			case GR_EQ:
			case SMALLER:
			case GREATER:
			case APP_EQ:
			case VERY_EQ:
			case SL_EQ: 
				return new SingleRelation(kind,var,mf,rb);
			case NOT: 
			case MoL:
			case SLIGHTLY: 
			case VERY: 
				return new UnaryRelation(kind,l,rb);
			default: 
				return null;
		}
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Verifica si es una proposición compuesta binaria	
	 */
	public boolean isBinary() {
		int kind = getKind();
		return (kind == AND || kind == OR);
	}

	/**
	 * Verifica si es una proposición compuesta unaria
	 */
	public boolean isUnary() {
		int kind = getKind();
		return (kind == NOT || kind == MoL || kind == SLIGHTLY || kind == VERY);
	}

	/**
	 * Verifica si es una proposición simple
	 */
	public boolean isSingle() {
		int kind = getKind();
		return (kind >= IS && kind <= SL_EQ);
	}

 	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ABSTRACTOS                         //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula el grado de activacion de la proposición
	 */
	public abstract double compute() throws XflException;

	/**
	 * Calcula la derivada de la proposición
	 */
	public abstract void derivative(double de) throws XflException;

	/**
	 * Verifica que la proposición sea ajustable
	 */
	public abstract boolean isAdjustable();

	/**
	 * Elimina los enlaces para poder eliminar la proposición
	 */
	public abstract void dispose();

	/**
	 * Sustituye una función de pertenencia por otra
	 */
	public abstract void exchange(LinguisticLabel oldmf, LinguisticLabel newmf);

	/**
	 * Sustituye una variable por otra
	 */
	public abstract void exchange(Variable oldvar, Variable newvar);

	/**
	 * Obtiene un duplicado del objeto
	 */
	public abstract Object clone(Rulebase rb);

	//----------------------------------------------------------------------------//
	// Métodos de edición de los campos                                           //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el tipo de proposición
	 */
	public abstract int getKind();

	/**
	 * Obtiene la referencia a la variable de la proposición
	 */
	public abstract Variable getVariable();

	/**
	 * Obtiene la referencia a la MF de la proposición
	 */
	public abstract LinguisticLabel getMembershipFunction();

	/**
	 * Obtiene la referencia a la componente izquierda
	 */
	public abstract Relation getLeftRelation();

	/**
	 * Obtiene la referencia a la componente derecha
	 */
	public abstract Relation getRightRelation();

	/**
	 * Asigna la referencia a la variable de la proposición
	 */
	public abstract void setVariable(Variable var);

	/**
	 * Asigna la referencia a la MF de la proposición
	 */
	public abstract void setMembershipFunction(LinguisticLabel mf);

	/**
	 * Asigna la referencia a la componente izquierda
	 */
	public abstract void setLeftRelation(Relation rel);

	/**
	 * Asigna la referencia a la componente derecha 
	 */
	public abstract void setRightRelation(Relation rel);

	//----------------------------------------------------------------------------//
	// Métodos que generan código                                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripcion XFL3 de la proposición
	 */
	public abstract String toXfl();

}
