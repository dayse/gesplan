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

package xfuzzy.xfsg;

/**
 * Clase que implementa el método ‘create’ que se utiliza para crear objetos que
 * implementen la interfaz XfsgSTPrintRules
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgPrintRulesFactory {

	private int total_inputs = 0;

	public XfsgPrintRulesFactory(int total_inputs) {
		this.total_inputs = total_inputs;
	}

	public XfsgSTPrintRules create(XfsgRulesMemData contenido, String defuzzy,
			String name, int bitsn_fp, XfsgOutData outdata,
			XfsgInOrderMemFunc[] inOrderMfInput, int i) {

		if (total_inputs == 1)
			return new XfsgSTPrintRules1(contenido, defuzzy, name, i, bitsn_fp,
					outdata, inOrderMfInput, total_inputs);
		else if (total_inputs == 2)
			return new XfsgSTPrintRules2(contenido, defuzzy, name, i, bitsn_fp,
					outdata, inOrderMfInput, total_inputs);
		else if (total_inputs == 3)
			return new XfsgSTPrintRules3(contenido, defuzzy, name, i, bitsn_fp,
					outdata, inOrderMfInput, total_inputs);
		else if (total_inputs == 4)
			return new XfsgSTPrintRules4(contenido, defuzzy, name, i, bitsn_fp,
					outdata, inOrderMfInput, total_inputs);
		else
			return null;

	}

}
