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
 * Clase que describe un método de concreción
 * 
 * @author Francisco José Moreno Velo
 *
 */
public abstract class DefuzMethod extends FuzzyOperator {
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public DefuzMethod(String pkg, String function) {
		super(pkg,function);
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Ejecuta el método de concreción
	 */
	public abstract double compute(AggregateMemFunc mf);
	
	/**
	 * Verifica que la función agregada a concretar no incluye
	 * funciones de pertenencia de tipos no soportados
	 */
	public abstract boolean test(AggregateMemFunc mf);
	
	/**
	 * Calcula la derivada del método de concreción
	 */
	public void derivative(AggregateMemFunc mf, double de) throws XflException {
		throw new XflException(19);
	}
}
