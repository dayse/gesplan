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
 * Clase que define una funci�n de pertenencia libre
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public abstract class ParamMemFunc extends ParametricFunction 
implements LinguisticLabel, Cloneable {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	
	public Universe u;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre de la etiqueta ling��stica
	 */
	private String label;
	
	/**
	 * N�mero de enlaces de la funci�n de pertenencia
	 */
	private int link = 0;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public ParamMemFunc(String pkg, String function) {
		super(pkg, function);
	}

 	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// M�todos comunes de los objetos XFL3                                        //
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
	 * Estudia si el objeto est� siendo utilizado
	 */
	public boolean isLinked() {
		return (this.link>0);
	}

	/**
	 * Genera la descripcion XFL3 de la etiqueta ling��stica
	 */
	public String toXfl() {
		return "  "+label+" "+getXflDescription();
	}

	//----------------------------------------------------------------------------//
	// M�todos de comunes de edici�n                                              //
	//----------------------------------------------------------------------------//

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
	 * Asigna el nombre de la etiqueta y el universo de discurso
	 */
	public void set(String label, Universe u){
		this.label = label;
		this.u = u;
	}

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Universe cu) {
		try {
			ParamMemFunc clone = (ParamMemFunc) getClass().newInstance();
			clone.set(this.label,cu);
			clone.set(get());
			return clone;
		}
		catch(Exception ex) { return null; }
	}

	/**
	 * Elimina los enlaces del objeto
	 */
	public void dispose() {
	}

	//----------------------------------------------------------------------------//
	// M�todos de acceso al universo de discurso                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el universo de discurso
	 */
	public Universe universe() {
		return this.u;
	}

	/**
	 * Obtiene el l�mite inferior del universo de discurso
	 */
	public double min() {
		return this.u.min();
	}

	/**
	 * Obtiene el l�mite superior del universo de discurso
	 */
	public double max() {
		return this.u.max();
	}

	/**
	 * Obtiene la separaci�n entre los puntos del universo
	 */
	public double step() {
		return this.u.step();
	}

	//----------------------------------------------------------------------------//
	// M�todos por defecto de calculo de la funci�n                               //
	//----------------------------------------------------------------------------//

	/**
	 * Aplicaci�n del modificador "mayor o igual" sobre la funci�n
	 */
	public double greatereq(double x) {
		double min = this.u.min();
		double step = this.u.step();
		double degree = 0;
		double mu;

		for(double y = min; y<=x ; y+=step) {
			mu = compute(y);
			if( mu>degree ) degree = mu;
		}
		return degree;
	}

	/**
	 * Aplicaci�n del modificador "menor o igual" sobre la funci�n
	 */
	public double smallereq(double x) {
		double max = this.u.max();
		double step = this.u.step();
		double degree = 0;
		double mu;

		for(double y = max; y>=x ; y-=step) {
			mu = compute(y);
			if( mu>degree ) degree = mu;
		}
		return degree;
	}

	/**
	 * Obtiene el valor por defecto del centro de la funci�n
	 */
	public double center() {
		return 0;
	}

	/**
	 * Obtiene el valor por defecto de la base de la funci�n
	 */
	public double basis() {
		return 0;
	}

	//----------------------------------------------------------------------------//
	// M�todos por defecto del c�lculo de la derivada                             //
	//----------------------------------------------------------------------------//

	/**
	 * Derivada del grado de pertenencia
	 */
	public double[] deriv_eq(double x) throws XflException {
		throw new XflException(19);
	}

	/**
	 * Derivada del modificador "mayor o igual"
	 */
	public double[] deriv_greq(double x) throws XflException {
		throw new XflException(19);
	}

	/**
	 * Derivada del modificador "menor o igual"
	 */
	public double[] deriv_smeq(double x) throws XflException {
		throw new XflException(19);
	}

	/**
	 * Derivada del centro de la funci�n
	 */
	public double[] deriv_center() throws XflException {
		throw new XflException(19);
	}

	/**
	 * Derivada de la base de la funci�n
	 */
	public double[] deriv_basis() throws XflException {
		throw new XflException(19);
	}

	//----------------------------------------------------------------------------//
	// M�todos que obtienen el c�digo de la funci�n                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el c�digo Java de la modificaci�n "mayor o igual"
	 */
	public String getGreqJavaCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo Java de la modificaci�n "menor o igual"
	 */
	public String getSmeqJavaCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo Java de la propiedad "center"
	 */
	public String getCenterJavaCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo Java de la propiedad "basis"
	 */
	public String getBasisJavaCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C de la etiqueta ling��stica
	 */
	public String getEqualCCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C de la modificaci�n "mayor o igual"
	 */
	public String getGreqCCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C de la modificaci�n "menor o igual"
	 */
	public String getSmeqCCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C de la propiedad "center"
	 */
	public String getCenterCCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C de la propiedad "basis"
	 */
	public String getBasisCCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C++ de la etiqueta ling��stica
	 */
	public String getEqualCppCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C++ de la modificaci�n "mayor o igual"
	 */
	public String getGreqCppCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C++ de la modificaci�n "menor o igual"
	 */
	public String getSmeqCppCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C++ de la propiedad "center"
	 */
	public String getCenterCppCode() {
		return null;
	}

	/**
	 * Obtiene el c�digo C++ de la propiedad "basis"
	 */
	public String getBasisCppCode() {
		return null;
	}

 	//----------------------------------------------------------------------------//
	//                        M�TODOS P�BLICOS ABSTRACTOS                         //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula el grado de pertenencia de la funci�n
	 */
	public abstract double compute(double x);

	/**
	 * Verifica que los valores de los par�metros sean correctos
	 */
	public abstract boolean test();

	/**
	 * Obtiene el c�digo Java de la etiqueta ling��stica
	 */
	public abstract String getEqualJavaCode();

 	//----------------------------------------------------------------------------//
	//                             M�TODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * M�todo que desplaza un conjunto de valores manteniendo su orden
	 */
	protected double[] sortedUpdate(double[] value,double[] desp,boolean[] enable){
		return sortedUpdate(value, desp, this.u.step(), enable);
	}
}
