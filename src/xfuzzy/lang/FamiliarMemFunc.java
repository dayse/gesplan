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
//              FUNCION DE PERTENENCIA FAMILIAR			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.lang;

/**
 * Clase que describe una funci�n de pertenencia familiar
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class FamiliarMemFunc implements LinguisticLabel {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/*
	 * Nombre de la etiqueta ling��stica
	 */
	private String label;
	
	/**
	 * Referencia a la familia de la que forma parte
	 */
	private Family family;
	
	/**
	 * �ndice de la funci�n en la familia
	 */
	private int index;
	
	/**
	 * Contador de referencias
	 */
	private int link = 0;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public FamiliarMemFunc(String label, Family family, int index) {
		this.label = label;
		this.family = family;
		this.index = index;
		this.family.link();
	}
	
	//----------------------------------------------------------------------------//
	//                            M�TODOS P�BLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Compara la cadena con la etiqueta ling��stica
	 */
	public boolean equals(String label) {
		return this.label.equals(label);
	}
	
	/**
	 * Obtiene el valor de la etiqueta ling��stica
	 */
	public String toString() {
		return this.label;
	}
	
	/**
	 * Incrementa el contador de enlaces (usos) del objeto
	 */
	public void link() {
		this.link++;
	}
	
	/**
	 * Decrementa el contador de enlaces (usos) del objeto
	 */
	public void unlink() {
		this.link--;
	}

	/**
	 * Estudia si el objeto esta siendo utilizado
	 */
	public boolean isLinked() {
		return (this.link>0);
	}
	
	/**
	 * Genera la descripci�n XFL3 de la etiqueta ling��stica
	 */
	public String toXfl() {
		return "  "+label+" "+family+"["+index+"];";
	}
	
	/**
	 * Obtiene el nombre de la etiqueta ling��stica
	 */
	public String getLabel() {
		return this.label;
	}
	/**
	 * Asigna el nombre de la etiqueta ling��stica
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Obtiene la referencia a la familia de funciones
	 */
	public Family getFamily() {
		return this.family;
	}

	/**
	 * Asigna la referencia a la familia de funciones
	 */
	public void setFamily(Family fam) {
		this.family.unlink();
		this.family = fam;
		this.family.link();
	}
	
	/**
	 * Obtiene el �ndice a la funci�n de la familia
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Asigna el �ndice a la funci�n de la familia
	 */
	public void setIndex(int ix) {
		this.index = ix;
	}
	
	/**
	 * Elimina los enlaces del objeto
	 */
	public void dispose() {
		this.family.unlink();
	}
	
	/**
	 * Obtiene el l�mite inferior del universo de discurso
	 */
	public double min() {
		return this.family.min();
	}
	
	/**
	 * Obtiene el l�mite superior del universo de discurso
	 */
	public double max() {
		return this.family.max();
	}
	
	/**
	 * Obtiene la separaci�n entre los puntos del universo
	 */
	public double step() {
		return this.family.step();
	}
	
	/**
	 * Calcula el grado de pertenencia de la funci�n
	 */
	public double compute(double x) {
		return family.compute(index, x);
	}
	
	/**
	 * Aplicaci�n del modificador "mayor o igual" sobre la funci�n
	 */
	public double greatereq(double x) {
		return family.greatereq(index,x);
	}
	
	/**
	 * Aplicaci�n del modificador "menor o igual" sobre la funci�n
	 */
	public double smallereq(double x) {
		return family.smallereq(index,x);
	}
	
	/**
	 * Obtiene el valor del centro de la funci�n
	 */
	public double center() {
		return family.center(index);
	}
	
	/**
	 * Obtiene el valor de la base de la funci�n
	 */
	public double basis() {
		return family.basis(index);
	}
	
	/**
	 * Derivada del grado de pertenencia
	 */
	public double[] deriv_eq(double x) throws XflException {
		return family.deriv_eq(index,x);
	}
	
	/**
	 * Derivada del modificador "mayor o igual"
	 */
	public double[] deriv_greq(double x) throws XflException {
		return family.deriv_greq(index,x);
	}
	
	/**
	 * Derivada del modificador "menor o igual"
	 */
	public double[] deriv_smeq(double x) throws XflException {
		return family.deriv_greq(index,x);
	}
	
	/**
	 * Derivada del centro de la funci�n
	 */
	public double[] deriv_center() throws XflException {
		return family.deriv_center(index);
	}
	
	/**
	 * Derivada de la base de la funci�n
	 */
	public double[] deriv_basis() throws XflException {
		return family.deriv_center(index);
	}
	
	/**
	 * Verifica si el �ndice es correcto
	 */
	public boolean test() {
		return (index>=0 && index<family.members());
	}
	
	/**
	 * Verifica si alg�n par�metro de la funci�n es ajustable
	 */
	public boolean isAdjustable() {
		return family.isAdjustable();
	}
	
	/**
	 * A�ade un valor a la derivada respecto a un par�metro
	 */
	public void addDeriv(int i,double deriv) {
		family.addDeriv(i,deriv);
	}
	
	/**
	 * Obtiene los valores de los par�metros de la funci�n
	 */
	public double[] get() {
		return family.get();
	}
}
