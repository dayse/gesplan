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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import xfuzzy.lang.CrispBlockCall;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

/**
 * Clase que implementa la interfaz XfsgBlock. Se utiliza para almacenar la
 * información relativa a un bloque crisp.
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgCrisp implements XfsgBlock {

	// ATRIBUTOS

	// Bitsize of output CrispBlock / Nº bits salida bloque crisp
	private int no;

	// Name of crispblock /Nombre del bloque crisp
	private String name;

	boolean todo_relleno;

	// Numero de entradas
	private int inputs;

	// Numero de salidas
	private int outputs;

	// Cadena que almacena la descripción del CrispBlock en formato txt
	String txt;

	// Cadena que almacena la descripción del CrispBlock en formato mdl
	String mdl;

	// Cadena que almacena la descripción del CrispBlock en formato m
	String m;

	// Llamada al bloque Crisp
	CrispBlockCall cbc;

	// CONSTRUCTOR

	public XfsgCrisp(String name, CrispBlockCall cbc) {
		this.name = name;
		no = 0;
		todo_relleno = false;
		this.cbc = cbc;
	}

	// MÉTODOS GET
	/**
	 * 
	 * @return Bitsize of input/output CrispBlock / Nº bits entrada/salida
	 *         bloque crisp
	 */
	public int getNo() {
		return no;
	}

	/**
	 * 
	 * @return Name of crispblock /Nombre del bloque crisp
	 */
	public String getname() {
		return name;
	}

	public boolean gettodo_relleno() {
		return todo_relleno;
	}

	/**
	 * 
	 * @return el numero de entradas
	 */
	public int getinputs() {
		return inputs;
	}

	/**
	 * 
	 * @return el numero de salidas
	 */
	public int getoutputs() {
		return outputs;
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

	// MÉTODOS SET

	/**
	 * 
	 * @param grad
	 *            Bitsize of input/output CrispBlock / Nº bits entrada/salida
	 *            bloque crisp
	 */
	public void setNo(int No) {
		this.no = No;
	}

	/**
	 * 
	 * @param name
	 *            Name of crispblock /Nombre del bloque crisp
	 */
	public void setname(String name) {
		this.name = name;
	}

	public void settodo_relleno(boolean todo_relleno) {
		this.todo_relleno = todo_relleno;
	}

	/**
	 * 
	 * @param inputs
	 *            el numero de entradas
	 */
	public void setinputs(int inputs) {
		this.inputs = inputs;
	}

	/**
	 * 
	 * @param outputs
	 *            el numero de salidas
	 */
	public void setoutputs(int outputs) {
		this.outputs = outputs;
	}

	// MÉTODOS DE LA CLASE

	public String toString() {
		return name;
	}

	public Box gui() {

		XTextForm numBits = new XTextForm("Bits for Output");

		numBits.setEditable(true);
		numBits.setFieldWidth(100);

		// Escribe los valores por defecto
		Integer intTmp = new Integer(no);
		numBits.setText(intTmp.toString());

		Box evolbox = new Box(BoxLayout.Y_AXIS);

		evolbox.add(Box.createVerticalStrut(10));

		evolbox.add(new XLabel("Variables for CrispBlock: " + name));
		// Pone en la ventana la sección "Bitsize information"
		evolbox.add(new XLabel("Bitsize information"));

		evolbox.add(numBits);

		evolbox.setVisible(true);
		return evolbox;
	}

	public void generaTxt() {
		// String txt="";
		XfsgArchitecturesSimulink as = new XfsgArchitecturesSimulink();
		String propiedades = cbc.getCrispBlock().getFunctionName();
		if (propiedades.equals("addN"))
			propiedades += cbc.getNumberOfInputs();
		// System.out.println(propiedades);
		txt = as.getDescriptionTxt(this, propiedades);
		// System.out.println(txt);

	}

	public void generaMdl() {
		XfsgArchitecturesSimulink as = new XfsgArchitecturesSimulink();
		String propiedades = cbc.getCrispBlock().getFunctionName();
		if (propiedades.equals("addN"))
			propiedades += cbc.getNumberOfInputs();
		mdl = as.getDescriptionMdl(this, propiedades);
	}

	public void generaM(JCheckBox include) {
	}

	public void generaM() {
		String code = "";
		code += "%" + "Crisp" + " " + name + "\n";
		code += name + ".No = " + no + ";\n";

		m = code;
	}
}
