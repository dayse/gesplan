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
 * Clase que describe una función de pertenencia familiar
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class FamiliarMemFunc implements LinguisticLabel {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/*
	 * Nombre de la etiqueta lingüística
	 */
	private String label;
	
	/**
	 * Referencia a la familia de la que forma parte
	 */
	private Family family;
	
	/**
	 * Índice de la función en la familia
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
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Compara la cadena con la etiqueta lingüística
	 */
	public boolean equals(String label) {
		return this.label.equals(label);
	}
	
	/**
	 * Obtiene el valor de la etiqueta lingüística
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
	 * Genera la descripción XFL3 de la etiqueta lingüística
	 */
	public String toXfl() {
		return "  "+label+" "+family+"["+index+"];";
	}
	
	/**
	 * Obtiene el nombre de la etiqueta lingüística
	 */
	public String getLabel() {
		return this.label;
	}
	/**
	 * Asigna el nombre de la etiqueta lingüística
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
	 * Obtiene el índice a la función de la familia
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Asigna el índice a la función de la familia
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
	 * Obtiene el límite inferior del universo de discurso
	 */
	public double min() {
		return this.family.min();
	}
	
	/**
	 * Obtiene el límite superior del universo de discurso
	 */
	public double max() {
		return this.family.max();
	}
	
	/**
	 * Obtiene la separación entre los puntos del universo
	 */
	public double step() {
		return this.family.step();
	}
	
	/**
	 * Calcula el grado de pertenencia de la función
	 */
	public double compute(double x) {
		return family.compute(index, x);
	}
	
	/**
	 * Aplicación del modificador "mayor o igual" sobre la función
	 */
	public double greatereq(double x) {
		return family.greatereq(index,x);
	}
	
	/**
	 * Aplicación del modificador "menor o igual" sobre la función
	 */
	public double smallereq(double x) {
		return family.smallereq(index,x);
	}
	
	/**
	 * Obtiene el valor del centro de la función
	 */
	public double center() {
		return family.center(index);
	}
	
	/**
	 * Obtiene el valor de la base de la función
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
	 * Derivada del centro de la función
	 */
	public double[] deriv_center() throws XflException {
		return family.deriv_center(index);
	}
	
	/**
	 * Derivada de la base de la función
	 */
	public double[] deriv_basis() throws XflException {
		return family.deriv_center(index);
	}
	
	/**
	 * Verifica si el índice es correcto
	 */
	public boolean test() {
		return (index>=0 && index<family.members());
	}
	
	/**
	 * Verifica si algún parámetro de la función es ajustable
	 */
	public boolean isAdjustable() {
		return family.isAdjustable();
	}
	
	/**
	 * Añade un valor a la derivada respecto a un parámetro
	 */
	public void addDeriv(int i,double deriv) {
		family.addDeriv(i,deriv);
	}
	
	/**
	 * Obtiene los valores de los parámetros de la función
	 */
	public double[] get() {
		return family.get();
	}
}
