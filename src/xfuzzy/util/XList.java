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
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Clase que desarrolla una lista con una etiqueta con el estilo común del
 * entorno
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XList extends Box {

	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603007L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Componente para mostrar la lista
	 */
	private JList list;
	
	/**
	 * Componente para mostrar la barra de scroll
	 */
	private JScrollPane scroll;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XList(String label) {
		super(BoxLayout.Y_AXIS);
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(XConstants.textbackground);
		scroll = new JScrollPane(list);
		add(new XLabel(label));
		add(scroll);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el componente que almacena la lista
	 */
	public JList getList() {
		return this.list;
	}

	/**
	 * Establece el contenido de la lista
	 */
	public void setListData(Object[] data) {
		list.setListData(data);
	}

	/**
	 * Establece el contenido de la lista
	 */
	public void setListData(Vector data) {
		list.setListData(data);
	}

	/**
	 * Establece la anchura preferida para el componente
	 */
	public void setPreferredWidth(int width) {
		Dimension dim = new Dimension(width,scroll.getPreferredSize().height);
		scroll.setPreferredSize(dim);
	}

	/**
	 * Establece la altura preferida para el componente
	 */
	public void setPreferredHeight(int height) {
		Dimension dim = new Dimension(scroll.getPreferredSize().width,height);
		scroll.setPreferredSize(dim);
	}

	/**
	 * Obtiene el elemento seleccionado en la lista
	 */
	public Object getSelectedValue() {
		return list.getSelectedValue();
	}

	/**
	 * Obtiene el índice del elemento seleccionado en la lista
	 */
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	/**
	 * Selecciona un elemento de la lista
	 */
	public void setSelectedValue(Object value) {
		list.setSelectedValue(value,true);
	}

	/**
	 * Selecciona el elemento de un índice la lista
	 */
	public void setSelectedIndex(int index) {
		list.setSelectedIndex(index);
	}

	/**
	 * Borra la selección de la lista
	 */
	public void clearSelection() {
		list.clearSelection();
	}

	/**
	 * Verifica si existen elementos seleccionados en la lista
	 */
	public boolean isSelectionEmpty() {
		return list.isSelectionEmpty();
	}

	/**
	 * Añade un controlador de selección
	 */
	public void addListSelectionListener(ListSelectionListener listener) {
		list.addListSelectionListener(listener);
	}

	/**
	 * Añade un controlador de eventos de ratón
	 */
	public void addMouseListener(MouseListener listener) {
		list.addMouseListener(listener);
	}

	/**
	 * Añade un controlador de eventos de teclado
	 */
	public void addKeyListener(KeyListener listener) {
		list.addKeyListener(listener);
	}

	/**
	 * Selecciona la clase que da forma a los elementos
	 */
	public void setCellRenderer(ListCellRenderer renderer) {
		list.setCellRenderer(renderer);
	}

	/**
	 * (Des)Habilita la lista
	 */
	public void setEnabled(boolean enabled) {
		list.setEnabled(enabled);
	}
}
