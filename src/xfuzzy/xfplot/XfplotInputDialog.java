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



package xfuzzy.xfplot;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo para fijar los valores de las variables de entrada
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfplotInputDialog extends JDialog implements ActionListener {
	
	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603055L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Referencia a la aplicación
	 */
	private Xfplot xfplot;
	
	/**
	 * Campos para introducir los valores
	 */
	private XTextForm text[];
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public XfplotInputDialog(Xfplot xfplot) {
		super(xfplot,"Xfplot",true);
		this.xfplot = xfplot;
		
		Variable input[] = xfplot.getInputVariables();
		double value[] = xfplot.getInputValues();
		text = new XTextForm[input.length];
		for(int i=0; i<text.length; i++) {
			text[i] = new XTextForm(input[i].getName());
			text[i].setText(""+value[i]);
			text[i].setFieldWidth(200);
		}
		XTextForm.setWidth(text);
		
		String lb[] = { "Set", "Cancel" };
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(100);
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Fixed Values for Input Variables"));
		for(int i=0; i<text.length; i++) content.add(text[i]);
		content.add(form);
		
		Point loc = xfplot.getLocationOnScreen();
		loc.x += 40; loc.y += 200;
		this.setLocation(loc);
		pack();
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Set")) set();
		else if(command.equals("Cancel")) setVisible(false);
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Seleciona los valor iniciales de las variables
	 */
	private void set() {
		boolean good = true;
		double value[] = new double[text.length];
		for(int i=0; i<text.length; i++) {
			try { value[i] = Double.parseDouble(text[i].getText()); }
			catch (NumberFormatException e) { good = false; text[i].setText(""); }
		}
		if(good && xfplot.setInputValues(value)) setVisible(false);
		else Toolkit.getDefaultToolkit().beep(); 
	}
}

