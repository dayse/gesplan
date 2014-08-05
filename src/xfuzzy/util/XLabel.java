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
 * Clase que desarrolla una etiqueta con la estética común del entorno
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XLabel extends JPanel {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603006L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de etiquetas que forman la etiqueta a mostrar
	 */
	private JLabel jlabel[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor con varias líneas
	 */
	public XLabel(String label[]) {
		super();
		setLayout(new GridLayout(label.length,1));
		jlabel = new JLabel[label.length];
		for(int i=0; i<label.length; i++) {
			jlabel[i] = new JLabel("  "+label[i]+"  ");
			jlabel[i].setHorizontalAlignment(JLabel.CENTER);
			jlabel[i].setForeground(Color.black);
			jlabel[i].setFont(XConstants.font);
			add(jlabel[i]);
		}
		setBorder(BorderFactory.createRaisedBevelBorder());
		Dimension maxsize = getMaximumSize();
		Dimension prefsize = getPreferredSize();
		setMaximumSize(new Dimension(maxsize.width,prefsize.height));
	}

	/**
	 * Constructor con una línea
	 */
	public XLabel(String label) {
		this(makeArray(label));
	}

	/**
	 * Constructor de una línea y anchura
	 */
	public XLabel(String label, int width) {
		this(label);
		Dimension minsize = getMinimumSize();
		Dimension prefsize = getPreferredSize();
		setMinimumSize(new Dimension(width,minsize.height));
		setPreferredSize(new Dimension(width,prefsize.height));
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el texto de la etiqueta
	 */
	public void setText(String text) {
		jlabel[0].setText(text);
	}

	/**
	 *  Selecciona la alineación de todas las líneas	
	 */
	public void setAlignment(int ag) {
		for(int i=0; i<jlabel.length; i++) jlabel[i].setHorizontalAlignment(ag);
	}

	/**
	 * (Des)Habilita la etiqueta
	 */
	public void setEnabled(boolean enable) {
		for(int i=0; i<jlabel.length; i++) jlabel[i].setEnabled(enable);
	}

	/**
	 * Selecciona la fuente de letra
	 */
	public void setLabelFont(Font font) {
		for(int i=0; i<jlabel.length; i++) jlabel[i].setFont(font);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Convierte un String en una lista de Strings
	 */
	private static String [] makeArray(String label) {
		String array[] = new String[1];
		array[0] = label;
		return array;
	}

}
