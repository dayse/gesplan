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


package xfuzzy.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Clase que desarrolla un Combobox con etiqueta y lista desplegable
 * siguiendo la estética común al entorno
 * 
 * @author Francisco José Moreno Velo 
 *
 */
public class XComboBox extends Box {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 955056666030001L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Componente de la lista desplegable
	 */
	private JComboBox field;
	
	/**
	 * Componente de la etiqueta
	 */
	private JLabel label;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XComboBox(String title) {
		super(BoxLayout.X_AXIS);

		field = new JComboBox(new XComboBoxModel() );
		field.setBackground(XConstants.textbackground);
		field.setEditable(false);
		field.setFont(XConstants.textfont);
		Dimension prefsize = field.getPreferredSize();
		Dimension maxsize = field.getMaximumSize();
		field.setMaximumSize(new Dimension(maxsize.width,prefsize.height));

		label = new JLabel("  "+title+"  ");
		label.setAlignmentY(0.5f);
		label.setFont(XConstants.font);
		label.setForeground(Color.black);
		label.setBorder(BorderFactory.createRaisedBevelBorder());
		maxsize = label.getMaximumSize();
		label.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		label.setHorizontalAlignment(JLabel.CENTER);

		add(label);
		add(field);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el componente del menú desplegable
	 */
	public JComboBox getField() {
		return this.field;
	}

	/**
	 * Devuelve el objeto seleccionado
	 */
	public Object getSelectedItem() {
		return field.getSelectedItem();
	}

	/**
	 * Selecciona un determinado objeto de la lista
	 */
	public void setSelectedItem(Object item) {
		field.setSelectedItem(item);
	}

	/**
	 * Devuelve el índice del objeto seleccionado
	 */
	public int getSelectedIndex() {
		return field.getSelectedIndex();
	}

	/**
	 * Selecciona el índice de un determinado objeto de la lista
	 */
	public void setSelectedIndex(int index) {
		field.setSelectedIndex(index);
	}

	/**
	 * Asigna la lista de elementos seleccionables
	 */
	public void setList(Vector list) {
		field.removeAllItems();
		for(int i=0,size=list.size(); i<size; i++) field.addItem(list.elementAt(i));
	}

	/**
	 * Asigna la lista de elementos seleccionables
	 */
	public void setList(Object[] list) {
		field.removeAllItems();
		for(int i=0; i<list.length; i++) field.addItem(list[i]);
	}

	/**
	 * Asigna la clase que da forma a los elementos
	 */
	public void setRenderer(ListCellRenderer renderer) {
		field.setRenderer(renderer);
	}

	/**
	 * Añade un controlador a la lista
	 */
	public void addItemListener(ItemListener listener) {
		field.addItemListener(listener);
	}

	/**
	 * Asigna la anchura del componente
	 */
	public void setWidth(int lbwidth, int fdwidth) {
		Dimension minsize = this.label.getMinimumSize();
		Dimension prefsize = this.label.getPreferredSize();
		this.label.setMinimumSize(new Dimension(lbwidth,minsize.height));
		this.label.setPreferredSize(new Dimension(lbwidth,prefsize.height));
		minsize = this.field.getMinimumSize();
		prefsize = this.field.getPreferredSize();
		this.field.setMinimumSize(new Dimension(fdwidth,minsize.height));
		this.field.setPreferredSize(new Dimension(fdwidth,prefsize.height));
	}

	/**
	 * (Des)Habilita el componente
	 */
	public void setEnabled(boolean enable) {
		this.field.setEnabled(enable);
	}
}

