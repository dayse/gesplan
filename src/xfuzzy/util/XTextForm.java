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

/**
 * Clase que desarrolla tres tipos de componentes. El primero es una etiqueta
 * y un campo para introducir datos. El segundo tipo es un bot�n y un campo para
 * seleccionar una opci�n de una lista (pulsando el bot�n repetidas veces). El
 * tercer tipo es un bot�n con un campo para seleccionar una opci�n de una lista
 * y un campo secundario para introducir un dato asociado a la opci�n seleccionada.
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class XTextForm extends Box implements ActionListener {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603010L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Campo principal del componente
	 */
	private JTextField field;
	
	/**
	 * Campo secundario
	 */
	private JTextField field2;
	
	/**
	 * Etiqueta principal. Puede ser un bot�n en el caso de que exista
	 * un campo secundario
	 */
	private JComponent label;
	
	/**
	 * Etiqueta del campo secundario
	 */
	private JLabel label2;
	
	/**
	 * Marcador para indicar que el primer componente es una etiqueta y no un bot�n
	 */
	private boolean isLabel;
	
	/**
	 * Lista de opciones disponibles al pulsar el bot�n
	 */
	private String[] option;
	
	/**
	 * Nombres de los par�metros asociados a cada opci�n
	 */
	private String[] param;
	
	/**
	 * �ndice de la opci�n seleccionada
	 */
	private int selection;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor de una etiqueta y un campo
	 */
	public XTextForm(String label) {
		super(BoxLayout.X_AXIS);
		this.label = new JLabel(" "+label+" ");
		this.field = new JTextField();
		this.isLabel = true;
		dressLabel( (JLabel) this.label );
		dressField(this.field);
		add(this.label);
		add(this.field);
	}

	/**
	 * Constructor de un bot�n y un campo
	 */
	public XTextForm(String label, ActionListener action) {
		super(BoxLayout.X_AXIS);
		JButton lb = new JButton(" "+label+" ");
		if(action != null) lb.addActionListener(action);
		this.label = lb;
		this.field = new JTextField();
		this.isLabel = false;
		dressButton(lb);
		dressField(this.field);
		add(this.label);
		add(this.field);
	}

	/**
	 * Constructor de un bot�n, un campo, una etiqueta y un campo
	 */
	public XTextForm(String label, String[] option, String[] param) {
		super(BoxLayout.X_AXIS);
		this.option = option;
		this.param = param;
		this.selection = 0;
		JButton lb = new JButton(" "+label+" ");
		lb.addActionListener(this);
		this.label = lb;
		this.field = new JTextField();
		this.field.setEditable(false);
		dressButton( (JButton) this.label );
		dressField(this.field);
		add(this.label);
		add(this.field);
		if(param != null && param.length > 0) {
			this.label2 = new JLabel(" "+param[0]+" ");
			this.field2 = new JTextField();
			this.field2.setEditable(param[0].length() > 0);
			dressLabel(this.label2);
			dressField(this.field2);
			add(this.label2);
			add(this.field2);
		}
		this.isLabel = false;
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * A�ade un controlador para el bot�n
	 */
	public void addActionListener(ActionListener action) {
		if(isLabel) return;
		JButton lb = (JButton) this.label;
		lb.addActionListener(action);
	}

	/**
	 * Asigna el identificador del comando del bot�n
	 */
	public void setActionCommand(String command) {
		if(isLabel) return;
		JButton lb = (JButton) this.label;
		lb.setActionCommand(command);
	}

	/**
	 * A�ade un controlador de acci�n al campo
	 */
	public void addFieldActionListener(ActionListener action) {
		this.field.addActionListener(action);
	}

	/**
	 * Asigna el identificador de la acci�n de introducir datos
	 */
	public void setFieldActionCommand(String command) {
		this.field.setActionCommand(command);
	}

	/**
	 * A�ade un controlador de teclado al campo
	 */
	public void addFieldKeyListener(KeyListener action) {
		this.field.addKeyListener(action);
	}

	/**
	 * Solicita el enfoque para el campo
	 */
	public void requestFieldFocus() {
		if(this.field.isEditable()) this.field.requestFocus();
	}

	/**
	 * Asigna el texto del campo
	 */
	public void setText(String text) {
		this.field.setText(text);
	}

	/**
	 * Obtiene el texto del campo
	 */
	public String getText() {
		return this.field.getText();
	}

	/**
	 * Asigna el texto de la etiqueta
	 */
	public void setLabel(String text) {
		if(isLabel) { JLabel lb = (JLabel) this.label; lb.setText(" "+text+" "); }
		else { JButton lb = (JButton) this.label; lb.setText(" "+text+" "); }
	}

	/**
	 * Obtiene el texto de la etiqueta
	 */
	public String getLabel() {
		String text;
		if(isLabel) { JLabel lb = (JLabel) this.label; text = lb.getText(); }
		else { JButton lb = (JButton) this.label; text = lb.getText(); }
		return text.substring(1,text.length()-1);
	}

	/**
	 * (Des)Activa el campo para introducir datos
	 */
	public void setEditable(boolean editable) {
		this.field.setEditable(editable);
	}

	/**
	 * Verifica si el campo es editable
	 */
	public boolean isEditable() {
		return this.field.isEditable();
	}

	/**
	 * Asigna la alineaci�n de la etiqueta/boton
	 */
	public void setAlignment(int align) {
		if(isLabel) {
			JLabel lb = (JLabel) this.label;
			lb.setHorizontalAlignment(align);
		}
		else {
			JButton lb = (JButton) this.label;
			lb.setHorizontalAlignment(align);
		}
	}

	/**
	 * (Des)Habilita la etiqueta
	 */
	public void setLabelEnabled(boolean enabled) {
		this.label.setEnabled(enabled);
	}

	/**
	 * (Des)Habilita el campo
	 */
	public void setFieldEnabled(boolean enabled) {
		if(!enabled) this.field.setBackground(getBackground());
		else this.field.setBackground(XConstants.textbackground);
	}

	/**
	 * Asigna la anchura de la(s) etiqueta(s)
	 */
	public void setLabelWidth(int width) {
		Dimension minsize = this.label.getMinimumSize();
		Dimension prefsize = this.label.getPreferredSize();
		this.label.setMinimumSize(new Dimension(width,minsize.height));
		this.label.setPreferredSize(new Dimension(width,prefsize.height));
		if(this.label2 == null) return;
		minsize = this.label2.getMinimumSize();
		prefsize = this.label2.getPreferredSize();
		this.label2.setMinimumSize(new Dimension(width,minsize.height));
		this.label2.setPreferredSize(new Dimension(width,prefsize.height));
	}

	/**
	 * Asigna la anchura del campo
	 */
	public void setFieldWidth(int width) {
		Dimension prefsize = new Dimension( width, field.getPreferredSize().height);
		this.field.setPreferredSize(prefsize);
	}

	/**
	 * Asigna la anchura de la etiqueta y del campo
	 */
	public void setWidth(int lbwidth, int fdwidth) {
		setLabelWidth(lbwidth);
		setFieldWidth(fdwidth);
	}

	/**
	 * Obtiene la anchura de la etiqueta
	 */
	public int getLabelWidth() {
		return this.label.getPreferredSize().width;
	}

	/**
	 * Establece la selecci�n de un campo de opci�n
	 */
	public void setSelection(int selection) {
		this.selection = selection;
		field.setText(option[selection]);
		if(param == null) return;
		label2.setText(" "+param[selection]+" ");
		field2.setText("");
		field2.setEditable(param[selection].length() > 0);
	}

	/**
	 * Devuelve la selecci�n de una opci�n
	 */
	public int getSelection() {
		return this.selection;
	}

	/**
	 * Asigna el valor del par�metro de una opci�n
	 */
	public void setParameter(String parameter) {
		if(this.field2 == null) return;
		this.field2.setText(parameter);
	}

	/**
	 *  Obtiene el valor del par�metro de una opci�n
	 */
	public String getParameter() {
		if(this.field2 == null) return null;
		return this.field2.getText();
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		selection = (selection+1)%option.length;
		field.setText(option[selection]);
		if(param == null) return;
		label2.setText(" "+param[selection]+" ");
		field2.setText("");
		field2.setEditable(param[selection].length() > 0);
	}

	/**
	 * Establece un tama�o homog�neo para una lista de objetos
	 */
	public static void setWidth(XTextForm [] form) {
		int max = 0;
		for(int i=0; i<form.length; i++)
			if(form[i].getLabelWidth() > max) max = form[i].getLabelWidth();
		for(int i=0; i<form.length; i++) form[i].setLabelWidth(max);
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Aplica la configuraci�n com�n a las etiquetas
	 */
	private void dressLabel(JLabel label){
		label.setAlignmentY(0.5f);
		label.setFont(XConstants.font);
		label.setForeground(Color.black);
		label.setBorder(BorderFactory.createRaisedBevelBorder());
		Dimension maxsize = label.getMaximumSize();
		Dimension prefsize = label.getPreferredSize();
		label.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		label.setHorizontalAlignment(JLabel.LEFT);
	}

	/**
	 * Aplica la configuraci�n com�n a los botones
	 */
	private void dressButton(JButton label){
		label.setAlignmentY(0.5f);
		label.setFont(XConstants.font);
		label.setForeground(Color.black);
		label.setBorder(BorderFactory.createRaisedBevelBorder());
		Dimension maxsize = label.getMaximumSize();
		Dimension prefsize = label.getPreferredSize();
		label.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		label.setHorizontalAlignment(JButton.LEFT);
	}

	/**
	 * Aplica la configuraci�n com�n a los campos
	 */
	private void dressField(JTextField field){
		field.setAlignmentY(0.5f);
		field.setBorder(BorderFactory.createLoweredBevelBorder());
		field.setBackground(XConstants.textbackground);
		Dimension maxsize = field.getMaximumSize();
		Dimension prefsize = field.getPreferredSize();
		field.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		field.setPreferredSize(new Dimension(150,prefsize.height));
	}
}

