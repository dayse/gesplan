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
 * Clase que desarrolla un botón con la estética común del entorno
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XButton extends JPanel implements ChangeListener {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603000L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Botón
	 */
	private JButton jbutton;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor con el texto del botón
	 */
	public XButton(String label) {
		super();
		setLayout(new GridLayout(1,1));
		jbutton = new JButton("  "+label+"  ");
		jbutton.setBorder(BorderFactory.createRaisedBevelBorder());
		jbutton.setHorizontalAlignment(JButton.CENTER);
		jbutton.setFont(XConstants.font);
		jbutton.setFocusPainted(false);
		jbutton.setContentAreaFilled(false);
		jbutton.addChangeListener(this);
		add(jbutton);
		pack();
	}

	/**
	 * Constructor con el texto y el controlador
	 */
	public XButton(String label, ActionListener action) {
		this(label);
		if(action != null) jbutton.addActionListener(action);
	}

	/**
	 * Constructor con el texto y la anchura
	 */
	public XButton(String label, int width) {
		this(label);
		setPreferredWidth(width);
	}

	/**
	 * Constructor con el texto, el controlador y la anchura
	 */
	public XButton(String label, ActionListener action, int width) {
		this(label);
		if(action != null) jbutton.addActionListener(action);
		setPreferredWidth(width);
	}

	/**
	 * Constructor con el texto, el comando y el controlador
	 */
	public XButton(String label, String command, ActionListener action) {
		this(label);
		jbutton.setActionCommand(command);
		if(action != null) jbutton.addActionListener(action);
	}

	/**
	 * Constructor con el texto, comando, controlador y anchura
	 */
	public XButton(String label, String command, ActionListener action, int width){
		this(label);
		jbutton.setActionCommand(command);
		if(action != null) jbutton.addActionListener(action);
		setPreferredWidth(width);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna la anchura mínima
	 */
	public void setMinimumWidth(int width) {
		Dimension minsize = new Dimension( width, getMinimumSize().height);
		setMinimumSize(minsize);
	}

	/**
	 * Asigna la anchura preferida
	 */
	public void setPreferredWidth(int width) {
		Dimension prefsize = new Dimension( width, getPreferredSize().height);
		setPreferredSize(prefsize);
	}

	/**
	 * Asigna la anchura máxima
	 */
	public void setMaximumWidth(int width) {
		Dimension maxsize = new Dimension( width, getMaximumSize().height);
		setMaximumSize(maxsize);
	}

	/**
	 * Bloquea la altura del botón
	 */
	public void pack() {
		Dimension maxsize = getMaximumSize();
		Dimension prefsize = getPreferredSize();
		setMaximumSize(new Dimension(maxsize.width,prefsize.height));
	}

	/**
	 * Asigna el texto del botón
	 */
	public void setText(String text) {
		this.jbutton.setText(text);
	}

	/**
	 * Obtiene el texto del botón
	 */
	public String getText() {
		return this.jbutton.getText();
	}

	/**
	 * (Des)Habilita el botón
	 */
	public void setEnabled(boolean b) {
		this.jbutton.setEnabled(b);
	}

	/**
	 * Asigna la alineacion del texto
	 */
	public void setAlignment(int ag) {
		jbutton.setHorizontalAlignment(ag);
	}

	/**
	 * Asigna la fuente de letra
	 */
	public void setLabelFont(Font font) {
		jbutton.setFont(font);
	}

	/**
	 * Asigna la descripción del botón
	 */
	public void setToolTipText(String tip) {
		jbutton.setToolTipText(tip);
	}

	/**
	 * Interfaz ChangeListener
	 */
	public void stateChanged(ChangeEvent e) {
		if(jbutton.isSelected())
			jbutton.setBorder(BorderFactory.createLoweredBevelBorder());
		else jbutton.setBorder(BorderFactory.createRaisedBevelBorder());
	}
}
