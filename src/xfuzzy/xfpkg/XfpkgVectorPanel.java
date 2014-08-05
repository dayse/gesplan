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


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//CLASE DE XFPKG QUE EDITA LOS VECTORES: ALIAS, PARAM, DEFINED	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfpkg;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Clase de Xfpkg que edita los vectores: alias, param, defined
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfpkgVectorPanel extends JPanel implements ActionListener {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603051L;

	/**
	 * Número mínimo de campos a mostrar
	 */
	private int MINSIZE;
	
	/**
	 * Posición del campo que tiene la etiqueta "New"
	 */
	private int index;
	
	/**
	 * Componentes que referencian los campos editables
	 */
	private XTextForm text[];
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public XfpkgVectorPanel(int minsize) {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.MINSIZE = minsize;
		this.index = 0;
		this.text = new XTextForm[minsize];
		for(int i=0; i<text.length; i++) {
			text[i] = new XTextForm(" ", this);
			text[i].setLabelWidth(100);
			text[i].setFieldWidth(200);
			text[i].setAlignment(JButton.CENTER);
			text[i].setActionCommand(""+i);
			add(text[i]);
		}
		setLabel();
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Actualiza el contenido del panel con una lista de nombres
	 */
	public void setList(String[] list) {
		int size = list.length;
		index = size;
		ensureSize();
		setLabel();
		for(int i=0; i<size; i++) text[i].setText(list[i]);
		for(int i=size; i<text.length; i++) text[i].setText("");
	}
	
	/**
	 * Actualiza el contenido del panel con una lista de nombres
	 */
	public void set(Vector v) {
		int size = v.size();
		index = size;
		ensureSize();
		setLabel();
		for(int i=0; i<size; i++) text[i].setText((String) v.elementAt(i));
		for(int i=size; i<text.length; i++) text[i].setText("");
	}

	/**
	 * Obtiene la lista de nombres introducida en el panel
	 */
	public String[] getList() {
		String[] list = new String[index];
		for(int i=0; i<index; i++) list[i] = text[i].getText();
		return list;
	}

	/**
	 * Obtiene la lista de nombres introducida en el panel
	 */
	public Vector get() {
		Vector v = new Vector();
		for(int i=0; i<index; i++) v.addElement(text[i].getText());
		return v;
	}
	
	/**
	 * (Des)Habilita la edicion de los campos
	 */
	public void setEditable(boolean editable) {
		for(int i=0; i<index; i++) text[i].setEditable(editable);
		for(int i=0; i<text.length; i++) text[i].setLabelEnabled(editable);
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		int i = Integer.parseInt(command);
		if(i > index ) return; 
		if(i == index ) { 
			index++;
			ensureSize();
			setLabel();
		}
		else {
			for(int j=i+1; j<=index; j++) text[j-1].setText(text[j].getText());
			index--;
			ensureSize();
			setLabel();
		}
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Actualiza el texto de los botones del panel
	 */
	private void setLabel() {
		for(int i=0; i<index; i++) {
			text[i].setLabel("Remove");
			text[i].setEditable(true);
			text[i].setFieldEnabled(true);
		}
		for(int i=index; i<text.length; i++) {
			text[i].setLabel(" ");
			text[i].setFieldEnabled(false);
			text[i].setEditable(false);
		}
		text[index].setLabel("New");
	}

	/**
	 * Asegura un numero de campos suficiente para la lista
	 */
	private void ensureSize() {
		while(index < text.length-1 && text.length > MINSIZE) {
			remove(text[text.length-1]);
			XTextForm at[] = new XTextForm[text.length-1];
			System.arraycopy(text,0,at,0,text.length-1);
			text = at;
		}
		for(int i=index-text.length; i>=0; i--) {
			XTextForm nt = new XTextForm(" ",this);
			nt.setLabelWidth(100);
			nt.setFieldWidth(200);
			nt.setAlignment(JButton.CENTER);
			nt.setActionCommand(""+text.length);
			add(nt);
			XTextForm at[] = new XTextForm[text.length+1];
			System.arraycopy(text,0,at,0,text.length);
			at[text.length] = nt;
			text = at;
		}
	}
}
