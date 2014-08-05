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
 * Clase que describe una variable lingüística
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Variable {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PÚBLICAS                              //
	//----------------------------------------------------------------------------//

	public final static int INPUT = 0;
	public final static int OUTPUT = 1;
	public final static int INNER = 2;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre de la variable
	 */
	private String name;
	
	/**
	 * Tipo de la variable
	 */
	private Type type;
	
	/**
	 * Valor de la variable
	 */
	private MemFunc value;
	
	/**
	 * Tipo de acceso de la variable (ENTRADA, SALIDA o INTERNA)
	 */
	private int access;
	
	/**
	 * Contador de usos
	 */
	private int link;
	
	/**
	 * Referencia a la base de reglas de la que toma el valor, para poder
	 * utilizar su conjunto de operadores
	 */
	private Rulebase mod;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 *  Construye una variable global del sistema
	 */
	public Variable(String name, int access) {
		this.name = name;
		this.type = null;
		this.mod = null;
		this.value = null;
		this.access = access;
		this.link = 0;
	}

	/**
	 * Construye una variable de un cierto tipo
	 */
	public Variable(String name, Type type, int access) {
		this.name = name;
		this.type = type; this.type.link();
		this.mod = null;
		this.value = null;
		this.access = access;
		this.link = 0;
	}

	/**
	 * Construye una variable de salida de una base de reglas
	 */
	public Variable(String name, Type type, Rulebase mod) {
		this.name = name;
		this.type = type; this.type.link();
		this.mod = mod;
		this.value = null;
		this.access = OUTPUT;
		this.link = 0;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos comunes de los objetos XFL3                                        //
	//----------------------------------------------------------------------------//

	/**
	 * Compara una cadena con el nombre del objeto
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}

	/**
	 * Obtiene el nombre del objeto
	 */
	public String toString() {
		return new String(this.name);
	}

	/**
	 * Incrementa el contador de enlaces (usos) de la variable
	 */
	public void link() {
		this.link++;
	}

	/**
	 * Decrementa el contador de enlaces (usos) de la variable
	 */
	public void unlink() {
		this.link--;
	}

	/**
	 * Verifica si la variable está siendo utilizada
	 */
	public boolean isLinked() {
		return (this.link>0);
	}

	/**
	 * Genera la descripcion XFL3 de la variable
	 */
	public String toXfl() {
		if(access != INNER && this.type != null) return this.type+" "+this.name;
		return "";
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a los campos                                             //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre de la variable
	 */
	public String getName() {
		return new String(this.name);
	}

	/**
	 * Asigna el nombre de la variable
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene el tipo de la variable
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Obtiene el valor de la variable
	 */
	public MemFunc getValue() {
		return this.value;
	}

	/**
	 * Elimina los enlaces para poder ser eliminada
	 */
	public void dispose() {
		if(this.type != null) type.unlink();
	}

	/**
	 * Busca una función de pertenencia
	 */
	public LinguisticLabel search(String mfname) {
		if(type == null) return null;
		return this.type.search(mfname);
	}

	/**
	 * Verifica si se trata de una variable de entrada
	 */
	public boolean isInput() {
		return (this.access == INPUT);
	}

	/**
	 * Verifica si se trata de una variable de salida
	 */
	public boolean isOutput() {
		return (this.access == OUTPUT);
	}

	/**
	 * Verifica si se trata de una variable interna
	 */
	public boolean isInner() {
		return (this.access == INNER);
	}

	/**
	 * Obtiene un punto del universo a partir de un valor relativo
	 */
	public double point(double x) {
		return this.type.getUniverse().point(x);
	}

	/**
	 * Obtiene el rango del universo de discurso
	 */
	public double range() {
		return this.type.getUniverse().range();
	}

	/**
	 * Obtiene el valor relativo de un punto del universo
	 */
	public double getRate(double x) {
		return this.type.getUniverse().getRate(x);
	}

	/**
	 * Asigna un valor difuso a la variable
	 */
	public void set(MemFunc mf) {
		this.value = mf;
	}

	/**
	 * Asigna un valor concreto a la variable
	 */
	public void set(double x) {
		ParamMemFunc singleton = new pkg.xfl.mfunc.singleton();
		singleton.set(x);
		this.value = singleton;
	}

	/**
	 * Añade una función implicada al valor agregado
	 */
	public void addConclusion(ImpliedMemFunc imf) {
		if(value == null) value = new AggregateMemFunc(type.getUniverse(), mod);
		AggregateMemFunc amf = (AggregateMemFunc) value;
		amf.add(imf);
	}

	/**
	 * Obtiene el valor concreto de la variable
	 */
	public double getCrispValue() throws XflException {
		if(!(this.value instanceof pkg.xfl.mfunc.singleton)) throw new XflException(18);
		ParamMemFunc singleton = (ParamMemFunc) this.value;
		double[] param = singleton.get();
		return param[0];
	}

	/**
	 *  Elimina el valor de la variable
	 */
	public void reset() {
		this.value = null;
	}

	/**
	 * Calcula la derivada del valor de la variable
	 */
	public void derivative(double derror) throws XflException {
		if(this.access != OUTPUT) return;
		AggregateMemFunc amf = (AggregateMemFunc) this.value;
		mod.operation.defuz.derivative(amf,derror);
	}
}

