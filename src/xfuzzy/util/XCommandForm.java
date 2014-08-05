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

/**
 * Componente para mostrar un barra de botones con la estética común
 * del entorno
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XCommandForm extends JPanel implements ChangeListener {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603003L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de botones
	 */
	private JButton button[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor con los nombres de los botones
	 */
	public XCommandForm(String[] label) {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

		button = new JButton[label.length];

		Box form = new Box(BoxLayout.X_AXIS);
		form.add(Box.createGlue());
		form.add(Box.createHorizontalStrut(5));
		for(int i=0; i<label.length; i++) {
			button[i] = new JButton(label[i]);
			button[i].setBorder(BorderFactory.createRaisedBevelBorder());
			button[i].setHorizontalAlignment(JButton.CENTER);
			button[i].setFont(XConstants.font);
			button[i].setForeground(Color.black);
			button[i].setFocusPainted(false);
			button[i].setContentAreaFilled(false);
			button[i].addChangeListener(this);
			form.add(button[i]);
			form.add(Box.createGlue());
			form.add(Box.createHorizontalStrut(5));
		}

		add(Box.createVerticalStrut(5));
		add(form);
		add(Box.createVerticalStrut(5));
	}

	/**
	 * Constructor con los nombres y el controlador de los botones
	 */
	public XCommandForm(String[] label, ActionListener[] action) {
		this(label);
		for(int i=0; i<button.length; i++)
			if(action[i] != null) button[i].addActionListener(action[i]);
	}

	/**
	 * Constructor con los nombres/comandos y el controlador
	 */
	public XCommandForm(String[] label, ActionListener action) {
		this(label);
		for(int i=0; i<button.length; i++) if(action != null) {
			button[i].setActionCommand(label[i]);
			button[i].addActionListener(action);
		}
	}

	/**
	 * Constructor con los nombres, los comandos y el controlador
	 */
	public XCommandForm(String[] label, String[] command, ActionListener action) {
		this(label);
		for(int i=0; i<button.length; i++) if(action != null) {
			button[i].setActionCommand(command[i]);
			button[i].addActionListener(action);
		}
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Bloquea la altura de la barra de botones	
	 */
	public void block() {
		Dimension maxsize = getMaximumSize();
		Dimension prefsize = getPreferredSize();
		setMaximumSize(new Dimension(maxsize.width,prefsize.height));
	}

	/**
	 * Asigna el estado de habilitacion de un botón
	 */
	public void setEnabled(int i, boolean enable) {
		button[i].setEnabled(enable);
	}

	/**
	 * Asigna la anchura de un botón
	 */
	public void setCommandWidth(int width) {
		for(int i=0; i<button.length; i++) {
			Dimension prefsize = button[i].getPreferredSize();
			button[i].setPreferredSize(new Dimension( width, prefsize.height));
		}
	}

	/**
	 * Asigna el nombre de un botón
	 */
	public void setLabel(int i, String label) {
		button[i].setText(label);
	}

	/**
	 * Interfaz ChangeListener
	 */
	public void stateChanged(ChangeEvent e) {
		AbstractButton button = (AbstractButton) e.getSource();
		if(button.isSelected())
			button.setBorder(BorderFactory.createLoweredBevelBorder());
		else button.setBorder(BorderFactory.createRaisedBevelBorder());
	}
}

