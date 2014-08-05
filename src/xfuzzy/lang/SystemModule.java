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
 * Clase que describe la estructura global del sistema
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class SystemModule {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Variables definidas en el sistema
	 */
	private Variable var[];
	
	/**
	 * Lista de llamadas a bases de reglas o módulos no difusos que constituyen
	 * la estructura jerárquica del sistema
	 */
	private ModuleCall call[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public SystemModule() {
		this.var = new Variable[1];
		this.var[0] = new Variable("NULL",Variable.INNER);
		this.call = new ModuleCall[0];
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Sustitución de una base de reglas
	 */
	public void exchangeRulebase(Rulebase oldrb, Rulebase newrb) {
		for(int i=0; i<call.length; i++) if(call[i] instanceof RulebaseCall) {
			RulebaseCall rbcall = (RulebaseCall) call[i];
			rbcall.exchangeRulebase(oldrb, newrb);
		}
	}

	/**
	 * Sustitución de un bloque no difuso
	 */
	public void exchangeBlock(CrispBlock oldblock, CrispBlock newblock) {
		for(int i=0; i<call.length; i++) if(call[i] instanceof CrispBlockCall) {
			CrispBlockCall cbcall = (CrispBlockCall) call[i];
			cbcall.exchangeBlock(oldblock,newblock);
		}
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan código                                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripción XFL3 de la estructura global
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		Variable input[] = getInputs();
		Variable output[] = getOutputs();
		String code = eol+"system (";
		for(int i=0; i<input.length; i++) code += (i==0? "" : ", ")+input[i].toXfl();
		code += " : ";
		for(int i=0; i<output.length; i++) code += (i==0? "":", ")+output[i].toXfl();
		code += ") {"+eol;
		for(int i=0; i<call.length; i++) code += call[i].toXfl();
		code += " }"+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a las variables                                          //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la lista de variables globales de entrada
	 */
	public Variable[] getInputs() {
		int size = 0;
		for(int i=0; i<var.length; i++) if(var[i].isInput()) size++;
		Variable[] input = new Variable[size];
		for(int i=0,j=0; i<this.var.length; i++)
			if(var[i].isInput()) { input[j] = var[i]; j++; }
		return input;
	}

	/**
	 * Obtiene la lista de variables globales de salida
	 */
	public Variable[] getOutputs() {
		int size = 0;
		for(int i=0; i<var.length; i++) if(var[i].isOutput()) size++;
		Variable[] output = new Variable[size];
		for(int i=0,j=0; i<var.length; i++)
			if(var[i].isOutput()) { output[j] = var[i]; j++; }
		return output;
	}

	/**
	 * Obtiene la lista de variables globales internas
	 */
	public Variable[] getInners() {
		int size = 0;
		for(int i=0; i<var.length; i++) if(var[i].isInner()) size++;
		Variable[] inner = new Variable[size];
		for(int i=0,j=0; i<var.length; i++)
			if(var[i].isInner()) { inner[j] = var[i]; j++; }
		return inner;
	}

	/**
	 * Obtiene la lista completa de variables globales
	 */
	public Variable[] getVariables() {
		return this.var;
	}

	/**
	 * Añade una variable global
	 */
	public void addVariable(Variable newvar) {
		Variable aux[] = new Variable[var.length+1];
		System.arraycopy(var,0,aux,0,var.length);
		aux[var.length] = newvar;
		var = aux;
	}

	/**
	 * Elimina una variable global
	 */
	public void removeVariable(Variable rmvar) {
		int index = -1;
		for(int i=0; i<var.length; i++) if(var[i] == rmvar) index = i;
		if(index <= 0 || rmvar.isLinked() ) return;
		Variable[] aux = new Variable[var.length-1];
		System.arraycopy(var,0,aux,0,index);
		System.arraycopy(var,index+1,aux,index,aux.length-index);
		var = aux;
		rmvar.dispose();
	}

	/**
	 * Añade una variable interna
	 */
	public Variable addInnerVariable() {
		String newname = "i0";
		for(int i=0; searchVariable(newname)!=null; i++) newname="i"+i;
		Variable inner = new Variable(newname,Variable.INNER);
		addVariable(inner);
		return inner;
	}

	/**
	 * Busca una variable global
	 */
	public Variable searchVariable(String varname) {
		for(int i=0; i<var.length; i++) if( var[i].equals(varname) ) return var[i];
		return null;
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a las llamadas                                           //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la lista de llamadas a bases de reglas y bloques no difusos
	 */
	public ModuleCall[] getModuleCalls() {
		return call;
	}

	/**
	 * Obtiene la lista de llamadas a bases de reglas
	 */
	public RulebaseCall[] getRulebaseCalls() {
		int count=0;
		for(int i=0;i<call.length;i++) if(call[i] instanceof RulebaseCall) count++;
		RulebaseCall rbc[] = new RulebaseCall[count];
		for(int i=0,j=0;i<call.length;i++) if(call[i] instanceof RulebaseCall) {
			rbc[j] = (RulebaseCall) call[i];
			j++;
		}
		return rbc;
	}

	/**
	 * Añade una llamada a una base de reglas
	 */
	public void addCall(Rulebase ref, Variable ivar[], Variable ovar[]) {
		ModuleCall aux[] = new ModuleCall[call.length+1];
		System.arraycopy(call,0,aux,0,call.length);
		aux[call.length] = new RulebaseCall(ref,ivar,ovar);
		call = aux;
	}

	/**
	 * Añade una llamada a un modulo no difuso
	 */
	public void addCall(CrispBlock block, Variable ivar[], Variable ovar) {
		ModuleCall aux[] = new ModuleCall[call.length+1];
		System.arraycopy(call,0,aux,0,call.length);
		aux[call.length] = new CrispBlockCall(block,ivar,ovar);
		call = aux;
	}

	/**
	 * Elimina una llamada
	 */
	public void removeCall(ModuleCall rmcall) {
		int index = -1;
		for(int i=0; i<call.length; i++) if(call[i] == rmcall) index = i;
		if(index == -1) return;
		ModuleCall[] aux = new ModuleCall[call.length-1];
		System.arraycopy(call,0,aux,0,index);
		System.arraycopy(call,index+1,aux,index,aux.length-index);
		call = aux;
		rmcall.dispose();
	}

	/**
	 * Cambia el orden de dos llamadas a base de reglas
	 */
	public void exchangeCall(int i, int j) {
		ModuleCall aux = call[i];
		call[i] = call[j];
		call[j] = aux;
	}

	//----------------------------------------------------------------------------//
	// Métodos utilizados en el aprendizaje                                       //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula la derivada del proceso de inferencia del sistema
	 */
	public void derivative(double[] deriv) throws XflException {
		Variable[] outputvar = getOutputs();
		for(int i=call.length-1; i>=0; i--) {
			Variable[] calloutput = call[i].getOutputVariables();
			double[] callderiv = new double[calloutput.length];
			for(int j=0; j<calloutput.length; j++)
				for(int k=0; k<outputvar.length; k++)
					if(calloutput[j] == outputvar[k])
						callderiv[j] = deriv[k];
			call[i].derivative(callderiv);
		}
	}

	/**
	 * Calcula la derivada del proceso de inferencia del sistema
	 */
	public void derivative() throws XflException {
		for(int i=call.length-1; i>=0; i--) call[i].derivative();
	}

	//----------------------------------------------------------------------------//
	// Métodos de inferencia                                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Ejecuta una inferencia devolviendo datos concretos
	 */
	public double [] crispInference(double input[]) {
		Variable[] inputvar = getInputs();
		Variable[] outputvar = getOutputs();
		double output[] = new double[outputvar.length];

		for(int i=0; i<var.length; i++) var[i].reset();
		for(int i=0; i<input.length; i++) inputvar[i].set(input[i]);
		for(int i=0; i<call.length; i++) call[i].compute();
		for(int i=0; i<outputvar.length; i++)
			if(outputvar[i].getValue() instanceof pkg.xfl.mfunc.singleton)
				output[i] = ((pkg.xfl.mfunc.singleton) outputvar[i].getValue()).get()[0];
			else output[i] = ((AggregateMemFunc) outputvar[i].getValue()).defuzzify();
		return output;
	}

	/**
	 * Ejecuta una inferencia devolviendo datos concretos
	 */
	public double [] crispInference(MemFunc input[]) {
		Variable[] inputvar = getInputs();
		Variable[] outputvar = getOutputs();
		double output[] = new double[outputvar.length];

		for(int i=0; i<var.length; i++) var[i].reset();
		for(int i=0; i<input.length; i++) inputvar[i].set(input[i]);
		for(int i=0; i<call.length; i++) call[i].compute();
		for(int i=0; i<outputvar.length; i++)
			if(outputvar[i].getValue() instanceof pkg.xfl.mfunc.singleton)
				output[i] = ((pkg.xfl.mfunc.singleton) outputvar[i].getValue()).get()[0];
			else output[i] = ((AggregateMemFunc) outputvar[i].getValue()).defuzzify();
		return output;
	}

	/**
	 * Ejecuta una inferencia devolviendo datos difusos
	 */
	public MemFunc [] fuzzyInference(double input[]) {
		Variable[] inputvar = getInputs();
		Variable[] outputvar = getOutputs();
		MemFunc output[] = new MemFunc[outputvar.length];

		for(int i=0; i<var.length; i++) var[i].reset();
		for(int i=0; i<input.length; i++) inputvar[i].set(input[i]);
		for(int i=0; i<call.length; i++) call[i].compute();
		for(int i=0; i<outputvar.length; i++) output[i] = outputvar[i].getValue();
		return output;
	}

	/**
	 * Ejecuta una inferencia devolviendo datos difusos	
	 */
	public MemFunc [] fuzzyInference(MemFunc input[]) {
		Variable[] inputvar = getInputs();
		Variable[] outputvar = getOutputs();
		MemFunc output[] = new MemFunc[outputvar.length];

		for(int i=0; i<var.length; i++) var[i].reset();
		for(int i=0; i<input.length; i++) inputvar[i].set(input[i]);
		for(int i=0; i<call.length; i++) call[i].compute();
		for(int i=0; i<outputvar.length; i++) output[i] = outputvar[i].getValue();
		return output;
	}

	/**
	 * Obtiene los resultados difusos de las variables de salida
	 */
	public AggregateMemFunc[] getFuzzyValues() {
		Variable[] outputvar = getOutputs();
		AggregateMemFunc[] fuzzyvalue = new AggregateMemFunc[outputvar.length];
		for(int i=0; i<call.length; i++) {
			Variable[] calloutput = call[i].getOutputVariables();
			MemFunc[] callvalue = call[i].getFuzzyValues();
			for(int j=0; j<calloutput.length; j++)
				for(int k=0; k<outputvar.length; k++)
					if(calloutput[j] == outputvar[k])
						fuzzyvalue[k] = (AggregateMemFunc) callvalue[j];
		}
		return fuzzyvalue;
	}

}

