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
 * Clase que describe un conjunto de operadores
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Operatorset implements Cloneable {
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Operador AND
	 */
	public Binary and;
	
	/**
	 * Operador OR
	 */
	public Binary or;
	
	/**
	 * Operador IMP
	 */
	public Binary imp;
	
	/**
	 * Operador ALSO
	 */
	public Binary also;
	
	/**
	 * Operador NOT
	 */
	public Unary not;
	
	/**
	 * Operador VERY
	 */
	public Unary very;
	
	/**
	 * Operador MORE_OR_LESS
	 */
	public Unary moreorless;
	
	/**
	 * Operador SLIGHTLY
	 */
	public Unary slightly;
	
	/**
	 * Método de concreción
	 */
	public DefuzMethod defuz;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Indica si el conjunto de operadores es el conjunto por defecto
	 */
	private boolean defaultDef;
	
	/**
	 * Nombre del conjunto de operadores
	 */
	private String name;
	
	/**
	 * Contador de enlaces (usos) del objeto
	 */
	private int link;
	
	/**
	 * Indica si el conjunto de operadores se está editando
	 */
	private boolean editing = false;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor por defecto
	 */
	public Operatorset() {
		this.defaultDef = true;
		this.name = "_default_";
		this.and = (Binary) getDefault(FuzzyOperator.AND);
		this.or = (Binary) getDefault(FuzzyOperator.OR);
		this.imp = (Binary) getDefault(FuzzyOperator.IMP);
		this.also = (Binary) getDefault(FuzzyOperator.ALSO);
		this.not = (Unary) getDefault(FuzzyOperator.NOT); 
		this.very = (Unary) getDefault(FuzzyOperator.VERY);
		this.moreorless = (Unary) getDefault(FuzzyOperator.MOREORLESS);
		this.slightly = (Unary) getDefault(FuzzyOperator.SLIGHTLY);
		this.defuz = (DefuzMethod) getDefault(FuzzyOperator.DEFUZMETHOD);
		this.link = 0;
	}
	
	/**
	 *  Constructor con nombre
	 */
	public Operatorset(String name) {
		this();
		this.defaultDef = false;
		this.name = name;
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Estudia si se trata del conjunto de operadores por defecto
	 */
	public boolean isDefault() {
		return this.defaultDef;
	}
	
	/**
	 * Compara una cadena con el nombre del conjunto de operadores
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}
	
	//-------------------------------------------------------------//
	// Obtiene el nombre del conjunto de operadores		//
	//-------------------------------------------------------------//
	
	public String getName() {
		return new String(this.name);
	}
	
	//-------------------------------------------------------------//
	// Asigna el nombre del conjunto de operadores			//
	//-------------------------------------------------------------//
	
	public void setName(String name) {
		this.name = name;
	}
	
	//-------------------------------------------------------------//
	// Obtiene un duplicado del objeto				//
	//-------------------------------------------------------------//
	
	public Object clone() {
		Operatorset clone = new Operatorset(this.name);
		clone.defaultDef = this.defaultDef;
		for(int kind=1; kind<10; kind++) clone.set(get(kind),kind);
		return clone;
	}
	
	//-------------------------------------------------------------//
	// Aumenta el contador de enlaces (usos) del objeto		//
	//-------------------------------------------------------------//
	
	public void link() {
		this.link++;
	}
	
	//-------------------------------------------------------------//
	// Decrementa el contador de enlaces (usos) del objeto		//
	//-------------------------------------------------------------//
	
	public void unlink() {
		this.link--;
	}
	
	//-------------------------------------------------------------//
	// Estudia si el objeto esta siendo utilizado			//
	//-------------------------------------------------------------//
	
	public boolean isLinked() {
		return (this.link>0);
	}
	
	//-------------------------------------------------------------//
	// Selecciona si se esta editando o no el objeto		//
	//-------------------------------------------------------------//
	
	public void setEditing(boolean editing) {
		this.editing = editing;
	}
	
	//-------------------------------------------------------------//
	// Verifica si se esta editando el objeto			//
	//-------------------------------------------------------------//
	
	public boolean isEditing() {
		return this.editing;
	}
	
	//-------------------------------------------------------------//
	// Asigna uno de los operadores del conjunto			//
	//-------------------------------------------------------------//
	
	public void set(FuzzyOperator fzop, int kind) {
		switch(kind) {
		case FuzzyOperator.AND: this.and = (Binary) fzop; break;
		case FuzzyOperator.OR: this.or = (Binary) fzop; break;
		case FuzzyOperator.IMP: this.imp = (Binary) fzop; break;
		case FuzzyOperator.ALSO: this.also = (Binary) fzop; break;
		case FuzzyOperator.NOT: this.not = (Unary) fzop; break;
		case FuzzyOperator.VERY: this.very = (Unary) fzop; break;
		case FuzzyOperator.MOREORLESS: this.moreorless = (Unary) fzop; break;
		case FuzzyOperator.SLIGHTLY: this.slightly = (Unary) fzop; break;
		case FuzzyOperator.DEFUZMETHOD: this.defuz = (DefuzMethod) fzop; break;
		}
	}
	
	//-------------------------------------------------------------//
	// Obtiene uno de los operadores del conjunto			//
	//-------------------------------------------------------------//
	
	public FuzzyOperator get(int kind) {
		FuzzyOperator fzop = null;
		switch(kind) {
		case FuzzyOperator.AND: fzop = this.and; break;
		case FuzzyOperator.OR: fzop = this.or; break;
		case FuzzyOperator.IMP: fzop = this.imp; break;
		case FuzzyOperator.ALSO: fzop = this.also; break;
		case FuzzyOperator.NOT: fzop = this.not; break;
		case FuzzyOperator.VERY: fzop = this.very; break;
		case FuzzyOperator.MOREORLESS: fzop = this.moreorless; break;
		case FuzzyOperator.SLIGHTLY: fzop = this.slightly; break;
		case FuzzyOperator.DEFUZMETHOD: fzop = this.defuz; break;
		}
		if(fzop == null) return null;
		return (FuzzyOperator) fzop.clone();
	}
	
	//-------------------------------------------------------------//
	// Obtiene la funcion por defecto de cada operador		//
	//-------------------------------------------------------------//
	
	public static FuzzyOperator getDefault(int kind) {
		FuzzyOperator fzop = null;
		switch(kind) {
		case FuzzyOperator.AND:
		case FuzzyOperator.IMP:
			fzop = new pkg.xfl.binary.min(); break;
		case FuzzyOperator.OR:
		case FuzzyOperator.ALSO:
			fzop = new pkg.xfl.binary.max(); break;
		case FuzzyOperator.NOT:
			fzop = new pkg.xfl.unary.not(); break;
		case FuzzyOperator.VERY:
			fzop = new pkg.xfl.unary.square(); break;
		case FuzzyOperator.MOREORLESS:
			fzop = new pkg.xfl.unary.sqrt(); break;
		case FuzzyOperator.SLIGHTLY:
			fzop = new pkg.xfl.unary.parabola(); break;
		case FuzzyOperator.DEFUZMETHOD:
			fzop = new pkg.xfl.defuz.CenterOfArea(); break;
		}
		if(fzop != null) fzop.setDefault(true);
		return fzop;
	}
	
	//-------------------------------------------------------------//
	// Obtiene el nombre del conjunto de operadores		//
	//-------------------------------------------------------------//
	
	public String toString() {
		return this.name.toString();
	}
	
	//-------------------------------------------------------------//
	// Genera la descripcion XFL3 del conjunto de operadores	//
	//-------------------------------------------------------------//
	
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		if(this.defaultDef) return "";
		String code = "operatorset "+this.name+" {"+eol;
		
		if(!this.and.isDefault()) code += "  and "+this.and.toXfl()+eol;
		if(!this.or.isDefault()) code += "  or "+this.or.toXfl()+eol;
		if(!this.imp.isDefault()) code += "  imp "+this.imp.toXfl()+eol;
		if(!this.also.isDefault()) code += "  also "+this.also.toXfl()+eol;
		if(!this.not.isDefault()) code += "  not "+this.not.toXfl()+eol;
		if(!this.very.isDefault()) code += "  strongly "+this.very.toXfl()+eol;
		if(!this.moreorless.isDefault())
			code += "  moreorless "+this.moreorless.toXfl()+eol;
		if(!this.slightly.isDefault())
			code += "  slightly "+this.slightly.toXfl()+eol;
		if(!this.defuz.isDefault()) code += "  defuz "+this.defuz.toXfl()+eol;
		
		code += " }"+eol+eol;
		return code;
	}
	
}

