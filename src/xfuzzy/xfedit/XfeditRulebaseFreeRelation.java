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
//PROPOSICION PARA LA REPRESENTACION LIBRE DE LA BASE DE REGLAS//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfedit;

import xfuzzy.lang.*;

/**
 * Clase que encapsula la información necesaria para editar una relación
 * lógica en formato libre
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseFreeRelation {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Parte izquierda en caso de relación binaria
	 */
	public XfeditRulebaseFreeRelation left;
	
	/**
	 * Parte derecha en caso de relación binaria u operando en
	 * caso de relación unaria
	 */
	public XfeditRulebaseFreeRelation right;
	
	/**
	 * Relación superior (null para el antecedente completo)
	 */
	public XfeditRulebaseFreeRelation parent;
	
	/**
	 * Posición que ocupa en el texto del antecedente
	 */
	public int begin, end;
	
	/**
	 * Descripción XFL3 asociada al objeto
	 */
	public Relation rel;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor para representar una proposición incompleta (vacía)
	 */
	public XfeditRulebaseFreeRelation() {
	}

	/**
	 * Constructor para representar una proposición
	 * @param rel
	 * @param parent
	 */
	public XfeditRulebaseFreeRelation(Relation rel, Object parent) {
		this.rel = rel;
		this.parent = (XfeditRulebaseFreeRelation) parent;
		if(rel == null) return;
		if(rel instanceof BinaryRelation) {
			this.left = new XfeditRulebaseFreeRelation(rel.getLeftRelation(),this);
			this.right = new XfeditRulebaseFreeRelation(rel.getRightRelation(),this);
		}
		if(rel instanceof UnaryRelation) {
			this.right=new XfeditRulebaseFreeRelation(rel.getRightRelation(),this);
		}
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la anchura del texto de la proposición
	 */
	public int width() {
		return toString().length();
	}

	/**
	 * Selecciona la proposicion en funcion de la posicion del cursor
	 */
	public XfeditRulebaseFreeRelation select(int offset, int index) {
		int width = this.width();
		int lwidth = (left == null? 0: left.width() );
		int rwidth = (right == null? 0: right.width() );
		int parwidth = (parenthesis()? 2 :0);
		int rbase = width-rwidth-parwidth;
		begin = offset;
		end = offset+width;
		int loffset = offset+parwidth;
		int roffset = offset+rbase;
		if(index > width) return null;
		if(index < parwidth) return (left!=null? left.select(loffset,0): this);
		if(index < parwidth+lwidth) return left.select(loffset,index-parwidth);
		if(index < rbase) return this;
		int rindex = Math.min(index-rbase, rwidth);
		return (right != null? right.select(roffset,rindex) : this);
	}

	/**
	 * Obtiene la proposición padre de la actual
	 */
	public XfeditRulebaseFreeRelation getParent() {
		return parent;
	}

	/**
	 * Obtiene el tipo de proposición
	 */
	public int kind() {
		return (rel == null? 0: rel.getKind());
	}

	/**
	 * Estudia si la proposición se ha definido por completo
	 */
	public boolean isIncomplete() {
		if(rel == null) return true;
		if(rel.isBinary()) return left.isIncomplete() || right.isIncomplete();
		if(rel.isSingle())
			return(rel.getVariable()==null || rel.getMembershipFunction()==null);
		if(rel.isUnary()) return right.isIncomplete();
		return false;
	}

	/**
	 * Obtiene la representación de la proposición
	 */
	public String toString() {
		if(parenthesis()) return "( "+toCode()+" )";
		return toCode();
	}

	/**
	 * Obtiene la representación de la proposición
	 */
	public String toCode() {
		if(rel == null) return "?";
		if(rel.isSingle()) return rel.toXfl();
		else switch(rel.getKind()) {
			case Relation.AND: return left.toString()+" & "+right.toString();
			case Relation.OR: return left.toString()+" | "+right.toString();
			case Relation.NOT: return "!"+right.toString();
			case Relation.MoL: return "~"+right.toString();
			case Relation.SLIGHTLY: return "%"+right.toString();
			case Relation.VERY: return "+"+right.toString();
			default: return "";
		}
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Estudia si la representación necesita paréntesis
	 */
	private boolean parenthesis() {
		if(parent == null) return true;
		if(parent.rel.isUnary()) return true;
		if(parent.rel.getKind() == Relation.AND)
			return (rel!=null && rel.getKind()==Relation.OR);
		if(parent.rel.getKind() == Relation.OR)
			return (rel!=null && rel.getKind()==Relation.AND);
		return false;
	}
}

