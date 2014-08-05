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
 * Clase que se utiliza para almacenar toda la información necesaria acerca de
 * una salida
 * 
 * @author Jesús Izquierdo Tena
 * @author Santiago Sánchez-Solano
 */

public class XfsgOutData {

	private String nombre;

	private String RB;

	private String[] labels;

	private double[] values;

	private double[][] values2;

	private String pesos;

	public XfsgOutData(int n, String defuzzy) {
		labels = new String[n];
		if (defuzzy.equals("FuzzyMean") || defuzzy.equals("MaxLabel"))
			values = new double[n];
		else if (defuzzy.equals("WeightedFuzzyMean")
				|| defuzzy.equals("Quality") || defuzzy.equals("GammaQuality"))
			values2 = new double[n][2];
		else if (defuzzy.equals("TakagiSugeno"))
			values2 = new double[n][3];
		nombre = "";
	}

	public String getnombre() {
		return nombre;
	}

	public String getRB() {
		return RB;
	}

	public String[] getlabels() {
		return labels;
	}

	public double[] getvalues() {
		return values;
	}

	public double[][] getvalues2() {
		return values2;
	}

	public String getpesos() {
		return pesos;
	}

	public void setRB(String RB) {
		this.RB = RB;
	}

	public void setlabels(String[] labels) {
		this.labels = labels;
	}

	public void setvalues(double[] values) {
		this.values = values;
	}

	public void setvalues2(double[][] values2) {
		this.values2 = values2;
	}

	public void setpesos(String pesos) {
		this.pesos = pesos;
	}

	public void setnombre(String nombre) {
		this.nombre = nombre;
	}

}
