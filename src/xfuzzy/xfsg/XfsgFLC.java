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

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;

import xfuzzy.lang.RulebaseCall;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

/**
 * Clase que implementa la interfaz XfsgBlock. Se utiliza para almacenar la
 * información relativa a una base de reglas.
 * 
 * @author Jesús Izquierdo Tena
 */

public class XfsgFLC implements XfsgBlock {

	private static final long serialVersionUID = -4981662902071901357L;

	// Nombre del FLC
	private String name = "";

	// Bitsize of input / Nº bits entrada
	private int N;

	// Bitsize of membership degree / Nº bits grado de pertenencia
	private int grad;

	// Bitsize of MF slope / Nº bits pendientes
	private int P;

	// Bitsize of output / Nº bits salida
	private int No;

	// Bits for defuzzification weight
	private int K;

	// Array de mfc
	private ArrayList<MFC> mfcs;

	// Rule Base/Base de reglas
	private ArrayList<XfsgOutData> RB;

	// Conectivo de antecedentes
	private String connective;

	private boolean todo_relleno;

	// Método de defuzzificación
	private String defuzzy;

	XTextForm[] numBitsInformation;

	// Número de entradas
	private int inputs;

	// Número de salidas
	private int outputs;

	// Grado de certeza de las reglas
	String grados;

	// Contiene la llamada a la base de reglas
	RulebaseCall rbc;

	// Cadena que almacena la descripción del FLC en formato txt
	String txt = "";

	// Cadena que almacena la descripción del FLC en formato mdl
	String mdl = "";

	// Cadena que almacena la descripción del FLC en formato m
	String m = "";

	// booleano que indica si la base de reglas es completa
	boolean complete;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	public XfsgFLC(String name, RulebaseCall rbc) {
		this.name = name;
		N = 0;
		grad = 0;
		P = 0;
		No = 0;
		K = 0;
		mfcs = new ArrayList<MFC>();
		RB = new ArrayList<XfsgOutData>();
		connective = "";
		grados = "";
		defuzzy = "";
		todo_relleno = false;
		inputs = 0;
		outputs = 0;
		this.rbc = rbc;
	}

	// MÉTODOS GET

	/**
	 * 
	 * @return Bitsize of input / Nº bits entrada
	 */
	public int getN() {

		return N;
	}

	/**
	 * 
	 * @return Bitsize of membership degree / Nº bits grado de pertenencia
	 */
	public int getgrad() {
		return grad;
	}

	/**
	 * 
	 * @return Bitsize of MF slope / Nº bits pendientes
	 */
	public int getP() {
		return P;
	}

	/**
	 * 
	 * @return Bitsize of output / Nº bits salida
	 */
	public int getNo() {
		return No;
	}

	/**
	 * 
	 * @return el nombre del FLC
	 */
	public String getname() {
		return name;
	}

	/**
	 * 
	 * @return Bits for defuzzification weight
	 */
	public int getK() {
		return K;
	}

	/**
	 * 
	 * @return el array de mfcs de un flc
	 */
	public ArrayList<MFC> getmfcs() {
		return mfcs;
	}

	/**
	 * 
	 * @return RuleBase /Base de reglas
	 */
	public ArrayList<XfsgOutData> getRB() {
		return RB;
	}

	/**
	 * 
	 * @return conectivo de antecedentes
	 */
	public String getconnective() {
		return connective;
	}

	public boolean gettodo_relleno() {
		return todo_relleno;
	}

	/**
	 * 
	 * @return método de defuzzificación
	 */
	public String getdeffuzy() {
		return defuzzy;
	}

	/**
	 * 
	 * @return el número de entradas
	 */
	public int getinputs() {
		return inputs;
	}

	/**
	 * 
	 * @return el número de salidas
	 */
	public int getoutputs() {
		return outputs;
	}

	public String getgrados() {
		return grados;
	}

	public String getTxt() {
		return txt;
	}

	public String getMdl() {
		return mdl;
	}

	public String getM() {
		return m;
	}

	/**
	 * 
	 * @return un booleano indicando si la base de reglas es completa o no.
	 */
	public boolean getcomplete() {
		return complete;
	}

	// MÉTODOS SET

	/**
	 * 
	 * @param N
	 *            Bitsize of input / Nº bits entrada
	 */
	public void setN(int N) {
		this.N = N;
	}

	/**
	 * 
	 * @param grad
	 *            Bitsize of membership degree / Nº bits grado de pertenencia
	 */
	public void setgrad(int grad) {
		this.grad = grad;
	}

	/**
	 * 
	 * @param P
	 *            Bitsize of MF slope / Nº bits pendientes
	 */
	public void setP(int P) {
		this.P = P;
	}

	/**
	 * 
	 * @param No
	 *            Bitsize of output / Nº bits salida
	 */
	public void setNo(int No) {
		this.No = No;
	}

	/**
	 * 
	 * @param name
	 *            Nombre del FLC
	 */
	public void setname(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param K
	 *            Bits for defuzzification weight
	 */
	public void setK(int K) {
		this.K = K;
	}

	/**
	 * 
	 * @param mfcs
	 *            array de mfc
	 */
	public void setmfcs(ArrayList<MFC> mfcs) {
		this.mfcs = mfcs;
	}

	/**
	 * 
	 * @param RB
	 *            RuleBase/Base de reglas
	 */
	public void setRB(ArrayList<XfsgOutData> RB) {
		this.RB = RB;
	}

	/**
	 * 
	 * @param connective
	 *            conectivo de antecedentes
	 */
	public void setconnective(String connective) {
		this.connective = connective;
	}

	public void settodo_relleno(boolean todo_relleno) {
		this.todo_relleno = todo_relleno;
	}

	/**
	 * 
	 * @param deffuzy
	 *            método de defuzzificación
	 */
	public void setdefuzzy(String deffuzy) {
		this.defuzzy = deffuzy;
	}

	/**
	 * 
	 * @param inputs
	 *            el número de entradas
	 */
	public void setinputs(int inputs) {
		this.inputs = inputs;
	}

	/**
	 * 
	 * @param outputs
	 *            el número de salidas
	 */
	public void setoutputs(int outputs) {
		this.outputs = outputs;
	}

	public void setgrados(String grados) {
		this.grados = grados;
	}

	public void setcomplete(boolean complete) {
		this.complete = complete;
	}

	// MÉTODOS PROPIOS DE LA CLASE

	public String toString() {
		return name;
	}

	public void crear_añadirMFC(String n_fp, String _a, String slopes) {
		MFC m = new MFC(n_fp, _a, slopes);
		mfcs.add(m);
	}

	public void añadirRB(XfsgOutData rb) {
		this.RB.add(rb);
	}

	public Box gui() {

		// Define los botones asociados para la sección "Bitsize
		// information"
		String numBitsInformationLabels[] = { "Bits for Input",
				"Bits for Output", "Bits for membership degree",
				"Bits for membership function slope " };

		// Crea la sección "Bitsize information"
		numBitsInformation = new XTextForm[4];
		for (int i = 0; i < numBitsInformation.length; i++) {
			numBitsInformation[i] = new XTextForm(numBitsInformationLabels[i]);
			numBitsInformation[i].setEditable(true);
			numBitsInformation[i].setFieldWidth(100);
		}
		XTextForm.setWidth(numBitsInformation);

		// Escribe los valores por defecto
		Integer intTmp = new Integer(N);
		numBitsInformation[0].setText(intTmp.toString());
		intTmp = new Integer(No);
		numBitsInformation[1].setText(intTmp.toString());
		intTmp = new Integer(grad);
		numBitsInformation[2].setText(intTmp.toString());
		intTmp = new Integer(P);
		numBitsInformation[3].setText(intTmp.toString());

		Box evolbox = new Box(BoxLayout.Y_AXIS);

		evolbox.add(Box.createVerticalStrut(10));

		evolbox.add(new XLabel("Variables for Rulebase: " + name));
		evolbox.add(new XLabel("Bitsize information"));
		for (int i = 0; i < numBitsInformation.length; i++)
			evolbox.add(numBitsInformation[i]);
		for (int k = 0; k < mfcs.size(); k++) {
			evolbox.add(Box.createVerticalStrut(5));
			evolbox.add(new XLabel("MFC" + (k + 1)));
			String MFCInformationLabels[] = { "N. Membership Functions",
					"Breakpoints", "Slopes" };

			// Crea la sección "MFCj Information"
			XTextForm[] MFCInformation = new XTextForm[3];
			for (int j = 0; j < MFCInformation.length; j++) {
				MFCInformation[j] = new XTextForm(MFCInformationLabels[j]);
				MFCInformation[j].setEditable(false);
				MFCInformation[j].setFieldWidth(100);
			}
			MFC mfc_aux = mfcs.get(k);
			MFCInformation[0].setText(mfc_aux.getn_fp());
			MFCInformation[1].setText(mfc_aux.get_a());
			MFCInformation[2].setText(mfc_aux.getslopes());

			XTextForm.setWidth(MFCInformation);
			for (int l = 0; l < MFCInformation.length; l++) {
				evolbox.add(MFCInformation[l]);
			}
		}

		// Crea la sección "Rulebase Information"
		for (int i = 0; i < RB.size(); i++) {
			evolbox.add(Box.createVerticalStrut(5));
			evolbox.add(new XLabel("RB" + (i + 1)));
			JTextArea jta = new JTextArea();
			jta.append(RB.get(i).getRB());
			jta.setEditable(false);
			jta.setBackground(XConstants.textbackground);
			evolbox.add(jta);
		}

		evolbox.setVisible(true);
		return evolbox;
	}

	public void generaTxt() {
		// String txt="";
		XfsgArchitecturesSimulink as = new XfsgArchitecturesSimulink();
		String propiedades = inputs + "-" + outputs + "-" + connective + "-"
				+ defuzzy;
		txt = as.getDescriptionTxt(this, propiedades);
		// System.out.println(txt);

	}

	public void generaMdl() {
		XfsgArchitecturesSimulink as = new XfsgArchitecturesSimulink();
		String propiedades = inputs + "-" + outputs + "-" + connective + "-"
				+ defuzzy;
		mdl = as.getDescriptionMdl(this, propiedades);
	}

	public void generaM(JCheckBox include) {
		// System.out.println("Metodo: "+defuzzy);
		String code = "";
		code += "\n"
				+ "%"
				+ "FLC"
				+ " "
				+ name
				+ "\n\n"

				+ name
				+ ".N = "
				+ N
				+ ";		% Bitsize of input / Nº bits entrada \n"

				+ name
				+ ".grad = "
				+ grad
				+ ";		% Bitsize of membership degree / Nº bits grado de pertenencia\n"

				+ name + ".P = " + P
				+ ";		% Bitsize of MF slope / Nº bits pendientes\n"

				+ name + ".No = " + No
				+ ";		% Bitsize of output / Nº bits salida\n";

		/* MFC */

		if (mfcs.size() == 1) {
			MFC mfcaux = mfcs.get(0);
			/* Escribe el número de funciones de pertenencia */
			code += "\n% MFC1\n" + name + ".n_fp = " + mfcaux.getn_fp() + ";\n";

			code += name + ".MFC_a = " + mfcaux.get_a();
			code += name + ".MFC_m = " + mfcaux.getslopes();
		} else {
			for (int i = 0; i < mfcs.size(); i++) {
				MFC mfcaux = mfcs.get(i);
				/* Escribe el número de funciones de pertenencia */
				code += "\n% MFC" + (i + 1) + "\n" + name + ".MFC" + (i + 1)
						+ ".n_fp = " + mfcaux.getn_fp() + ";\n";

				code += name + ".MFC" + (i + 1) + ".MFC_a = " + mfcaux.get_a();
				code += name + ".MFC" + (i + 1) + ".MFC_m = "
						+ mfcaux.getslopes();
			}
		}

		code += "\n\n";
		code += "% --------------------------------------------------------------\n";
		code += "% Dufuzzification Method: " + defuzzy + "\n";
		code += "% --------------------------------------------------------------\n";
		for (int i = 0; i < RB.size(); i++) {
			code += "\n% RB" + (i + 1) + "\n";

			if (defuzzy.equals("FuzzyMean") || defuzzy.equals("MaxLabel")) {
				double[] values = RB.get(i).getvalues();
				String[] labels = RB.get(i).getlabels();
				for (int j = 0; j < values.length; j++) {
					code += labels[j] + " = " + values[j] + ";\n";
				}
				code += "NR = 0;\n";
				code += "\n\n" + RB.get(i).getRB() + "\n\n";
			} else if (defuzzy.equals("WeightedFuzzyMean")
					|| defuzzy.equals("Quality")
					|| defuzzy.equals("GammaQuality")) {
				double[][] values2 = RB.get(i).getvalues2();
				String[] labels = RB.get(i).getlabels();
				for (int j = 0; j < values2.length; j++) {
					code += labels[j] + " = [" + values2[j][0] + "   "
							+ values2[j][1] + "];\n";
				}
				code += "NR = [0  0];\n";
				code += "\n\n" + RB.get(i).getRB() + "\n\n";
			} else if (defuzzy.equals("TakagiSugeno")) {
				code += "% (p[0] p[1] p[2])\n";
				double[][] values2 = RB.get(i).getvalues2();
				String[] labels = RB.get(i).getlabels();
				for (int j = 0; j < values2.length; j++) {
					code += labels[j] + " = [" + values2[j][0] + "   "
							+ values2[j][1] + "   " + values2[j][2] + "];\n";
				}
				code += "NR = [0  0  0];\n";
				code += "\n\n" + RB.get(i).getRB() + "\n\n";
			}
		}
		if (include.isSelected()) {
			code += "% Degree of certainty of the rules\n";
			code += grados + "\n\n";
		}

		m = code;
	}

	public void generaM() {
	}

	// CLASE PRIVADA QUE REPRESENTA A LOS MFCs

	private class MFC {

		public String n_fp;

		public String _a;

		public String slopes;

		public MFC(String n_fp, String _a, String slopes) {
			this.n_fp = n_fp;
			this._a = _a;
			this.slopes = slopes;
		}

		public String getn_fp() {
			return n_fp;
		}

		public String get_a() {
			return _a;
		}

		public String getslopes() {
			return slopes;
		}

		public void setn_fp(String aux) {
			this.n_fp = aux;
		}

		public void set_a(String aux) {
			this._a = aux;
		}

		public void setslopes(String aux) {
			this.slopes = aux;
		}

	}
}
