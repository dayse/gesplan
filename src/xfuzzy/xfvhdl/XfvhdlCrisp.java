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

package xfuzzy.xfvhdl;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import xfuzzy.lang.CrispBlockCall;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

/**
 * Clase que implementa la interfaz XfvhdlBlock. Se utiliza para almacenar la
 * información relativa a un bloque crisp.
 * 
 * @author Lidia Delgado Carretero
 */
public class XfvhdlCrisp implements XfvhdlBlock {

	// ATRIBUTOS

	/**Nº bits salida del bloque crisp.*/ //Bitsize of output CrispBlock / 
	private int no;

	/**Nombre del bloque crisp.*/ // Name of crispblock 
	private String name;

	/**Atributo que indica si el usuario nos ha proporcionado todos los 
	 * datos relativos al bloque crisp.*/
	boolean todo_relleno;

	/**Nº de entradas.*/ 
	private int inputs;

	/**Nº de salidas.*/ 
	private int outputs;
	
	//Cadena que almacena el código VHDL del CrispBlock 
	String vhdl;
	
	/** Atributo que almacena la llamada al bloque Crisp.*/
	CrispBlockCall cbc;

	// CONSTRUCTOR
	
	/**Constructor de la clase.
	 * @param name Nombre del bloque crisp.
	 * @param cbc Llamada al bloque Crisp.*/
	public XfvhdlCrisp(String name, CrispBlockCall cbc) {
		this.name = name;
		no = 0;
		todo_relleno = false;
		this.cbc = cbc;
	}

	// MÉTODOS GET
	/**
	 * 
	 * @return  Nº bits entrada/salida del  bloque crisp
	 */ //Bitsize of input/output CrispBlock 
	public int getNo() {
		return no;
	}

	/**
	 * 
	 * @return Nombre del bloque crisp
	 */ //Name of crispblock 
	public String getname() {
		return name;
	}

	/**
	 * @return todo_relleno
	 */
	public boolean gettodo_relleno() {
		return todo_relleno;
	}

	/**
	 * @return el nº de entradas
	 */
	public int getinputs() {
		return inputs;
	}

	/**
	 * @return el nº de salidas
	 */
	public int getoutputs() {
		return outputs;
	}
	/**
	 * @return el codigo vhdl generado
	 */
	public String getVHDL(){
		return vhdl;
	}

	// MÉTODOS SET

	/**
	 * @param No  Nº bits entrada/salida del bloque crisp.
	 */ //Bitsize of input/output CrispBlock /
	public void setNo(int No) {
		this.no = No;
	}

	/**
	 * 
	 * @param name Nombre del bloque crisp.
	 */ //Name of crispblock /
	public void setname(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param todo_relleno Booleano que indica si está todo relleno.
	 */
	public void settodo_relleno(boolean todo_relleno) {
		this.todo_relleno = todo_relleno;
	}

	/**
	 * 
	 * @param inputs el nº de entradas
	 */
	public void setinputs(int inputs) {
		this.inputs = inputs;
	}

	/**
	 * 
	 * @param outputs el nº de salidas
	 */
	public void setoutputs(int outputs) {
		this.outputs = outputs;
	}

	// MÉTODOS DE LA CLASE

	public String toString() {
		return name;
	}

	/**
	 * @return Interfaz asociada al bloque crisp.*/
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

	/** Este método está diseñado para generar el código asociado
	 * al bloque crisp, aunque en este momento no genera nada,
	 * para ello, dentro de este método habría que modificar el atributo
	 * de esta clase llamado vhdl*/
	public void generaVHDL(){
		
	}
}
