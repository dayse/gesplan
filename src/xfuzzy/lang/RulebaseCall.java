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
 * Clase que describe una llamada a una base de reglas
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class RulebaseCall implements ModuleCall {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Base de reglas a la que se realiza la llamada
	 */
	private Rulebase base;
	
	/**
	 * Variables del sistema utilizadas como entrada de la llamada
	 */
	private Variable inputvar[];
	
	/**
	 * Variables del sistema utilizadas como salida de la llamada
	 */
	private Variable outputvar[];
	
	/**
	 * Grado de activación de cada regla tras un proceso de inferencia
	 */
	private double degree[];
	
	/**
	 * Valor difuso (sin aplicar la concreción) de las salidas tras un proceso 
	 * de inferencia
	 */
	private MemFunc outputvalue[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Construye una llamada sin asignar las variables
	 */
	public RulebaseCall(Rulebase base) {
		this.base = base;
		this.base.link();
		this.inputvar = new Variable[base.getInputs().length];
		this.outputvar = new Variable[base.getOutputs().length];
		this.outputvalue = new MemFunc[outputvar.length];
	}

	/**
	 * Construye una llamada asignando las variables
	 */
	public RulebaseCall(Rulebase base, Variable inputvar[], Variable outputvar[]) {
		this.base = base;
		this.base.link();
		this.inputvar = inputvar;
		this.outputvar = outputvar;
		this.outputvalue = new MemFunc[outputvar.length];
		for(int i=0; i<inputvar.length; i++) inputvar[i].link();
		for(int i=0; i<outputvar.length; i++) outputvar[i].link();
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos que describen la llamada                                           //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre del objeto llamado
	 */
	public String getName() {
		return this.base.getName();
	}

	/**
	 * Genera la descripción XFL3 de la llamada
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		String code = "  "+base.getName()+"(";
		for(int i=0; i<inputvar.length; i++) {
			code += (i==0? "": ", ");
			code += (inputvar[i] == null? "null" : inputvar[i].getName());
		}
		code += " : ";
		for(int i=0; i<outputvar.length; i++) {
			code += (i==0? "": ", ");
			code += (outputvar[i] == null? "null" : outputvar[i].getName());
		}
		code += ");"+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a la base de reglas                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la referencia a la base de reglas de la llamada
	 */
	public Rulebase getRulebase() {
		return base;
	}

	/**
	 * Intercambia la base de reglas
	 */
	public void exchangeRulebase(Rulebase oldrb, Rulebase newrb) {
		if(base != oldrb) return;
		oldrb.unlink();
		base = newrb;
		newrb.link();
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a las variables                                          //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el número de variables de entrada
	 */
	public int getNumberOfInputs() {
		return this.base.getInputs().length;
	}

	/**
	 * Obtiene el número de variables de salida
	 */
	public int getNumberOfOutputs() {
		return this.base.getOutputs().length;
	}

	/**
	 * Obtiene las variables del sistema utilizadas como entradas
	 */
	public Variable[] getInputVariables() {
		return inputvar;
	}

	/**
	 * Obtiene las variables del sistema utilizadas como salidas
	 */
	public Variable[] getOutputVariables() {
		return outputvar;
	}

	/**
	 * Obtiene la variable del sistema asociada a una variable de entrada de 
	 * la base de reglas de la llamada
	 */
	public Variable getCallInput(Variable inner) {
		Variable[] inn = base.getInputs();
		for(int i=0; i<inn.length; i++) if(inn[i] == inner) return inputvar[i];
		return null;
	}

	/**
	 * Obtiene la variable del sistema asociada a una variable de salida de 
	 * la base de reglas de la llamada
	 */
	public Variable getCallOutput(Variable inner) {
		Variable[] inn = base.getOutputs();
		for(int i=0; i<inn.length; i++) if(inn[i] == inner) return outputvar[i];
		return null;
	}

	/**
	 * Asigna la lista de variables de entrada del sistema
	 */
	public void setInputVariables(Variable[] var) {
		for(int i=0; i<inputvar.length; i++) {
			if(inputvar[i] != null) this.inputvar[i].unlink();
			this.inputvar[i] = var[i];
			if(inputvar[i] != null) this.inputvar[i].link();
		}
	}

	/**
	 * Asigna una variable de entrada del sistema
	 */
	public void setInputVariable(int index, Variable var) {
		if(inputvar[index] != null) this.inputvar[index].unlink();
		this.inputvar[index] = var;
		if(inputvar[index] != null) this.inputvar[index].link();
	}

	/**
	 * Asigna la lista de variables de salida del sistema
	 */
	public void setOutputVariables(Variable[] var) {
		for(int i=0; i<outputvar.length; i++) {
			if(outputvar[i] != null) this.outputvar[i].unlink();
			this.outputvar[i] = var[i];
			if(outputvar[i] != null) this.outputvar[i].link();
		}
	}

	/**
	 * Asigna una variable de salida del sistema
	 */
	public void setOutputVariable(int index, Variable var) {
		if(outputvar[index] != null) this.outputvar[index].unlink();
		this.outputvar[index] = var;
		if(outputvar[index] != null) this.outputvar[index].link();
	}

	/**
	 * Asigna la variable del sistema asociada a una entrada de la base de
	 * reglas de la llamada
	 */
	public void setInputVariable(Variable basevar, Variable sysvar) {
		Variable[] inn = base.getInputs();
		int index = -1;
		for(int i=0; i<inn.length; i++) if(inn[i] == basevar) index = i;
		if(index == -1) return;
		if(inputvar[index] != null) inputvar[index].unlink();
		inputvar[index] = sysvar;
		if(inputvar[index] != null) inputvar[index].link();
	}

	/**
	 * Asigna la variable del sistema asociada a una salida de la base de 
	 * reglas de la llamada
	 */
	public void setOutputVariable(Variable basevar, Variable sysvar) {
		Variable[] inn = base.getOutputs();
		int index = -1;
		for(int i=0; i<inn.length; i++) if(inn[i] == basevar) index = i;
		if(index == -1) return;
		if(outputvar[index] != null) outputvar[index].unlink();
		outputvar[index] = sysvar;
		if(outputvar[index] != null) outputvar[index].link();
	}

	//----------------------------------------------------------------------------//
	// Métodos de ejecución de la inferencia                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula la inferencia de la base de reglas
	 */
	public void compute() {
		this.degree = new double[base.getRules().length];
		base.compute(inputvar, outputvar, degree, outputvalue);
	}

	/**
	 * Calcula la derivada de la base de reglas
	 */
	public void derivative(double[] deriv) throws XflException {
		base.derivative(deriv);
	}

	/**
	 * Calcula la derivada de la base de reglas
	 */
	public void derivative() throws XflException {
		base.derivative();
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a datos internos                                         //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el grado de activación de las reglas
	 */
	public double[] getDegree() {
		return this.degree;
	}

	/**
	 * Obtiene los valores difusos de las salidas
	 */
	public MemFunc[] getFuzzyValues() {
		return this.outputvalue;
	}

	/**
	 * Obtiene los valores de las salidas (difusos o concretos)
	 */
	public MemFunc[] getTrueValues() {
		MemFunc value[] = new MemFunc[outputvar.length];
		for(int i=0; i<outputvar.length; i++) value[i] = outputvar[i].getValue();
		return value;
	}

	/**
	 * Obtiene los valores de las entradas
	 */
	public MemFunc[] getInputValues() {
		MemFunc value[] = new MemFunc[inputvar.length];
		for(int i=0; i<inputvar.length; i++) value[i] = inputvar[i].getValue();
		return value;
	}

	//----------------------------------------------------------------------------//
	// Métodos auxiliares                                                         //
	//----------------------------------------------------------------------------//

	/**
	 * Elimina los enlaces para poder eliminar el objeto
	 */
	public void dispose() {
		for(int i=0; i<inputvar.length; i++)
			if(inputvar[i] != null) inputvar[i].unlink();
		for(int i=0; i<outputvar.length; i++)
			if(outputvar[i] != null) outputvar[i].unlink();
		base.unlink();
	}

}

