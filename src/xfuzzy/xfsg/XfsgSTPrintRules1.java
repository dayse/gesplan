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

import java.io.IOException;

/**
 * Clase que implementa la interfaz XfsgSTPrintRules y su método ‘printRules’
 * 
 * @author Jesús Izquierdo Tena
 * @author Santiago Sánchez-Solano
 */

public class XfsgSTPrintRules1 implements XfsgSTPrintRules {

	XfsgBinaryDecimal converter = new XfsgBinaryDecimal();

	public String RB;

	public String grados;

	public String pesos;

	public String tak_sug_a;

	public String tak_sug_b;

	public String tak_sug_c;

	public XfsgRulesMemData contenido;

	public String defuzzy;

	public String name;

	public int i;

	public int bitsn_fp;

	public XfsgOutData outdata;

	public XfsgInOrderMemFunc[] inOrderMfInput;

	public int total_inputs;

	// booleano que indica si una base de reglas es completa o no
	public boolean complete;

	public XfsgSTPrintRules1(XfsgRulesMemData contenido, String defuzzy,
			String name, int i, int bitsn_fp, XfsgOutData outdata,
			XfsgInOrderMemFunc[] inOrderMfInput, int total_inputs) {
		this.contenido = contenido;
		this.defuzzy = defuzzy;
		RB = "";
		grados = "";
		pesos = "";
		tak_sug_a = "";
		tak_sug_b = "";
		tak_sug_c = "";
		this.name = name;
		this.i = i;
		this.bitsn_fp = bitsn_fp;
		this.outdata = outdata;
		this.inOrderMfInput = inOrderMfInput;
		this.total_inputs = total_inputs;
		complete = true;
	}

	public void printRules() throws IOException {
		int input1 = inOrderMfInput[0].getSize();
		// double y =Math.pow(2, bitsn_fp);
		RB = name + ".RB" + outdata.getnombre() + " = [\n";
		pesos = name + ".RBg" + outdata.getnombre() + " = [\n";
		if (i == 0)
			grados = name + ".Fi" + " = [\n";

		for (int i1 = 0; i1 < contenido.getLength(); i1++) {
			RB += contenido.getMF2(i1, 4 + i * 2) + "	";
			pesos += contenido.getPeso(i1, 3 + i * 2) + "	";
			if (i == 0) {
				Double grado = contenido.getGrado(i1, 2);
				if (i1 < input1 && grado.doubleValue() == 0.0)
					complete = false;
				grados += grado + "	";
			}
		}

		RB += "];\n";
		pesos += "];\n";
		if (i == 0)
			grados += "];\n";

	}

	public String getRB() {
		return RB;
	}

	public String getPesos() {
		return pesos;
	}

	public String getGrados() {
		return grados;
	}

	public boolean getComplete() {
		return complete;
	}
}
