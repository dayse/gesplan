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
 * Interfaz que encapsula las llamadas a bases de reglas y módulos no difusos
 * 
 * @author Francisco José Moreno Velo
 *
 */
public interface ModuleCall {
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Genera la descripción XFL3 de la llamada
	 */
	public String toXfl();
	
	/**
	 * Obtiene el nombre del objeto llamado
	 */
	public String getName();
	
	/**
	 * Obtiene el número de variables de entrada
	 */
	public int getNumberOfInputs();
	
	/**
	 * Obtiene el número de variables de salida
	 */
	public int getNumberOfOutputs();
	
	/**
	 * Obtiene las variables de entrada de la llamada
	 */
	public Variable[] getInputVariables();
	
	/**
	 * Obtiene las variables de salida de la llamada
	 */
	public Variable[] getOutputVariables();
	
	/**
	 * Asigna una variable de entrada
	 */
	public void setInputVariable(int index, Variable var);
	
	/**
	 * Asigna una variable de salida
	 */
	public void setOutputVariable(int index, Variable var);
	
	/**
	 * Obtiene los valores difusos de las salidas
	 */
	public MemFunc[] getFuzzyValues();
	
	/**
	 * Ejecuta la llamada
	 */
	public void compute();
	
	/**
	 * Elimina los enlaces para poder eliminar el objeto
	 */
	public void dispose();
	
	/**
	 * Calcula la derivada
	 */
	public void derivative(double[] deriv) throws XflException;
	
	/**
	 * Calcula la derivada
	 */
	public void derivative() throws XflException;
	
}   

