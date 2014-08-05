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
 * Clase que describe una llamada a un bloque no difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class CrispBlockCall implements ModuleCall {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Referencia al bloque al que se realiza la llamada
	 */
	private CrispBlock block;
	
	/**
	 * Referencia a las variables de entrada de la llamada
	 */
	private Variable inputvar[];
	
	/**
	 * Referencia a la variable de salida de la llamada
	 */
	private Variable outputvar;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
		
	/**
	 * Construye una llamada sin asignar las variables
	 */
	public CrispBlockCall(CrispBlock block) {
		this.block = block;
		this.block.link();
		this.inputvar = new Variable[block.inputs()];
		this.outputvar = null;
	}
		
	/**
	 * Construye una llamada asignando las variables
	 */
	public CrispBlockCall(CrispBlock block,Variable inputvar[],Variable outputvar){
		this.block = block;
		this.block.link();
		this.inputvar = inputvar;
		for(int i=0; i<inputvar.length; i++)
			if(inputvar[i] != null) inputvar[i].link();
		this.outputvar = outputvar;
		if(outputvar != null) outputvar.link();
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el nombre del objeto llamado
	 */
	public String getName() {
		return this.block.getName();
	}
	
	/**
	 * Genera la descripción XFL3 de la llamada
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		String code = "  "+block.getLabel()+"(";
		for(int i=0; i<inputvar.length; i++) {
			code += (i==0? "": ", ");
			code += (inputvar[i] == null? "null" : inputvar[i].getName());
		}
		code += " : "+(outputvar == null? "null" : outputvar.getName());
		code += ");"+eol;
		return code;
	}
	
	/**
	 * Obtiene la referencia al bloque no difuso
	 */
	public CrispBlock getCrispBlock() {
		return block;
	}
	
	/**
	 * Intercambia el bloque no difuso
	 */
	public void exchangeBlock(CrispBlock oldblock, CrispBlock newblock) {
		if(block != oldblock) return;
		oldblock.unlink();
		block = newblock;
		newblock.link();
	}
	
	/**
	 * Obtiene el número de variables de entrada
	 */
	public int getNumberOfInputs() {
		return this.block.inputs();
	}
	
	/**
	 * Obtiene el número de variables de salida
	 */
	public int getNumberOfOutputs() {
		return 1;
	}
	
	/**
	 * Obtiene las variables del sistema utilizadas como entradas
	 */
	public Variable[] getInputVariables() {
		return inputvar;
	}
	
	/**
	 * Obtiene la variable del sistema utilizada como salida
	 */
	public Variable getOutputVariable() {
		return outputvar;
	}
	
	/**
	 * Obtiene la variable del sistema utilizada como salida
	 */
	public Variable[] getOutputVariables() {
		Variable v[] = new Variable[1];
		v[0] = outputvar;
		return v;
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
	 *  Asigna una variable de entrada del sistema
	 */
	public void setInputVariable(int index, Variable var) {
		if(inputvar[index] != null) this.inputvar[index].unlink();
		this.inputvar[index] = var;
		if(inputvar[index] != null) this.inputvar[index].link();
	}
	
	/**
	 * Asigna la variable de salida del sistema
	 */
	public void setOutputVariable(Variable var) {
		if(outputvar != null) this.outputvar.unlink();
		this.outputvar = var;
		if(outputvar != null) this.outputvar.link();
	}
	
	/**
	 * Asigna la variable de salida del sistema
	 */
	public void setOutputVariable(int index, Variable var) {
		if(outputvar != null) this.outputvar.unlink();
		this.outputvar = var;
		if(outputvar != null) this.outputvar.link();
	}
	
	/**
	 * Calcula la ejecución del bloque
	 */
	public void compute() {
		try { 
			double input[] = new double[inputvar.length];
			for(int i=0; i<inputvar.length; i++) input[i] = inputvar[i].getCrispValue();
			outputvar.set( block.compute(input) );
		} catch(Exception ex) {}
	}
	
	/**
	 * Calcula la derivada
	 */
	public void derivative(double[] deriv) throws XflException {
		throw new XflException(19);
	}
		
	/**
	 * Calcula la derivada
	 */
	public void derivative() throws XflException {
		throw new XflException(19);
	}

	/**
	 * Elimina los enlaces para poder eliminar el objeto
	 */
	public void dispose() {
		for(int i=0; i<inputvar.length; i++)
			if(inputvar[i] != null) inputvar[i].unlink();
		if(outputvar != null) outputvar.unlink();
		block.unlink();
	}
		
	/**
	 * Obtiene los valores difusos de las salidas
	 */
	public MemFunc[] getFuzzyValues() {
		MemFunc mf[] = new MemFunc[1];
		mf[0] = outputvar.getValue();
		return mf;
	}
}

