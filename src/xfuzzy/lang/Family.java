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
 * Clase que describe una familia de funciones de pertenencia
 * 
 * @author Francisco José Moreno Velo
 *
 */
public abstract class Family extends ParametricFunction implements Cloneable {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Nombre de la familia
	 */
	private String label;
	
	/**
	 * Tipo de variable a la que pertenece
	 */
	private Type type;
	
	/**
	 * Contador de enlaces (usos)
	 */
	private int link = 0;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public Family(String pkg, String function) {
		super(pkg, function);
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Compara la cadena con el nombre del objeto
	 */
	public boolean equals(String label) {
		return this.label.equals(label);
	}
	
	/**
	 * Obtiene el nombre del objeto
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
	 * Genera la descripcion XFL3 del objeto
	 */
	public String toXfl() {
		return " "+label+"[] "+getXflDescription();
	}
	
	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Type type) {
		try {
			Family clone = (Family) getClass().newInstance();
			clone.set(this.label,type);
			clone.set(get());
			return clone;
		}
		catch(Exception ex) { return null; }
	}
	
	/**
	 * Asigna el nombre de la etiqueta y el universo de discurso
	 */
	public void set(String label, Type type){
		this.label = label;
		this.type = type;
	}
	
	/**
	 * Obtiene el nombre de la familia de funciones de pertenencia
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * Asigna el nombre de la familia de funciones de pertenencia
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Obtiene el universo de discurso
	 */
	public Universe universe() {
		return this.type.getUniverse();
	}
	
	/**
	 * Obtiene el límite inferior del universo de discurso
	 */
	public double min() {
		return this.type.min();
	}
	
	/**
	 * Obtiene el límite superior del universo de discurso
	 */
	public double max() {
		return this.type.max();
	}
	
	/**
	 * Obtiene la separación entre los puntos del universo
	 */
	public double step() {
		return this.type.step();
	}
	
	/**
	 * Aplicación del modificador "mayor o igual" sobre la función
	 */
	public double greatereq(int index, double x) {
		double min = this.type.min();
		double step = this.type.step();
		double degree = 0;
		double mu;
		
		for(double y = min; y<=x ; y+=step) {
			mu = compute(index, y);
			if( mu>degree ) degree = mu;
		}
		return degree;
	}
	
	/**
	 * Aplicación del modificador "menor o igual" sobre la función
	 */
	public double smallereq(int index, double x) {
		double max = this.type.max();
		double step = this.type.step();
		double degree = 0;
		double mu;
		
		for(double y = max; y>=x ; y-=step) {
			mu = compute(index, y);
			if( mu>degree ) degree = mu;
		}
		return degree;
	}
	
	/**
	 * Obtiene el valor por defecto del centro de la función
	 */
	public double center(int index) {
		return 0;
	}
	
	/**
	 * Obtiene el valor por defecto de la base de la función
	 */
	public double basis(int index) {
		return 0;
	}
	
	/**
	 * Derivada del grado de pertenencia
	 */
	public double[] deriv_eq(int index, double x) throws XflException {
		throw new XflException(19);
	}
	
	/**
	 * Derivada del modificador "mayor o igual"
	 */
	public double[] deriv_greq(int index, double x) throws XflException {
		throw new XflException(19);
	}
	
	/**
	 * Derivada del modificador "menor o igual"
	 */
	public double[] deriv_smeq(int index, double x) throws XflException {
		throw new XflException(19);
	}
	
	/**
	 * Derivada del centro de la función
	 */
	public double[] deriv_center(int index) throws XflException {
		throw new XflException(19);
	}
	
	/**
	 * Derivada de la base de la función
	 */
	public double[] deriv_basis(int index) throws XflException {
		throw new XflException(19);
	}
	
	/**
	 * Obtiene el código Java de la modificación "mayor o igual"
	 */
	public String getGreqJavaCode() {
		return null;
	}
	
	/**
	 * Obtiene el código Java de la modificación "menor o igual"
	 */
	public String getSmeqJavaCode() {
		return null;
	}
	
	/**
	 * Obtiene el código Java de la propiedad "center"
	 */
	public String getCenterJavaCode() {
		return null;
	}
	
	/**
	 * Obtiene el código Java de la propiedad "basis"
	 */
	public String getBasisJavaCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C de la etiqueta lingüística
	 */
	public String getEqualCCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C de la modificación "mayor o igual"
	 */
	public String getGreqCCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C de la modificación "menor o igual"
	 */
	public String getSmeqCCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C de la propiedad "center"
	 */
	public String getCenterCCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C de la propiedad "basis"
	 */
	public String getBasisCCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C++ de la etiqueta lingüística
	 */
	public String getEqualCppCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C++ de la modificacion "mayor o igual"
	 */
	public String getGreqCppCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C++ de la modificacion "menor o igual"
	 */
	public String getSmeqCppCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C++ de la propiedad "center"
	 */
	public String getCenterCppCode() {
		return null;
	}
	
	/**
	 * Obtiene el código C++ de la propiedad "basis"
	 */
	public String getBasisCppCode() {
		return null;
	}
	
	//----------------------------------------------------------------------------//
	//                       MÉTODOS PÚBLICOS ABSTRACTOS                          //
	//----------------------------------------------------------------------------//
	
	/**
	 * Calcula el grado de pertenencia de la función
	 */
	public abstract double compute(int index, double x);
	
	/**
	 * Obtiene el número de miembros de la familia
	 */
	public abstract int members();
	
	/**
	 * Obtiene el código Java de la etiqueta lingüística
	 */
	public abstract String getEqualJavaCode();
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PRIVADOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Método que desplaza un conjunto de valores manteniendo su orden
	 */
	protected double[] sortedUpdate(double[] value,double[] desp,boolean[] enable){
		return sortedUpdate(value, desp, this.type.step(), enable);
	}
	
}
