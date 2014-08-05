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

/**
 * Clase que desarrolla un campo de texto con la estética común del entorno
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XTextField extends JTextField {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603009L;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor con el texto
	 */
	public XTextField(String text) {
		super();
		setText(text);
		setBackground(XConstants.textbackground);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	/**
	 * Constructor con el texto y la activación	
	 */
	public XTextField(String text, boolean editable) {
		this(text);
		setEditable(editable);
	}

	/**
	 * Constructor con el texto, la activación y la anchura
	 */
	public XTextField(String text, boolean editable, int width) {
		this(text);
		setEditable(editable);
		setPreferredWidth(width);
	}

	/**
	 * Constructor con el texto y la anchura
	 */
	public XTextField(String text, int width) {
		this(text);
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
	 * Fija la altura del campo	
	 */
	public void pack() {
		Dimension maxsize = getMaximumSize();
		Dimension prefsize = getPreferredSize();
		setMaximumSize(new Dimension(maxsize.width,prefsize.height));
	}
}

