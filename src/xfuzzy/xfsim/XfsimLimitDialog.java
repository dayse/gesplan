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


package xfuzzy.xfsim;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo para introducir los límites de un proceso de simulación
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimLimitDialog extends JDialog implements ActionListener {

	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603058L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	/**
	 * Campos para introducir los valores de los límites
	 */
	private XTextForm text[];
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfsimLimitDialog(Xfsim xfsim) {
		super(xfsim,"Xfsim",true);
		this.xfsim = xfsim;
		
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		int length = 2+2*input.length+2*output.length;
		text = new XTextForm[length];
		text[0] = new XTextForm("_n upper limit");
		text[1] = new XTextForm("_t upper limit");
		for(int i=0; i<output.length; i++) {
			text[2*i+2] = new XTextForm(output[i].getName()+" lower limit");
			text[2*i+2].setFieldWidth(200);
			text[2*i+3] = new XTextForm(output[i].getName()+" upper limit");
			text[2*i+3].setFieldWidth(200);
		}
		int base = 2+2*output.length;
		for(int i=0; i<input.length; i++) {
			text[2*i+base] = new XTextForm(input[i].getName()+" lower limit");
			text[2*i+base].setFieldWidth(200);
			text[2*i+base+1] = new XTextForm(input[i].getName()+" upper limit");
			text[2*i+base+1].setFieldWidth(200);
		}
		XTextForm.setWidth(text);
		
		XfsimConfig config = xfsim.getConfiguration();
		for(int i=0; i<text.length; i++) text[i].setText(config.limit.getValue(i));
		
		String lb[] = { "Set", "Unset", "Cancel" };
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(100);
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Simulation limits"));
		for(int i=0; i<text.length; i++) content.add(text[i]);
		content.add(form);
		
		Point loc = xfsim.getLocationOnScreen();
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
		else if(command.equals("Unset")) unset();
		else if(command.equals("Cancel")) setVisible(false);
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Selecciona las condiciones de parada de la simulación
	 */
	private void set() {
		boolean good = true;
		double value[] = new double[text.length];
		boolean flag[] = new boolean[text.length];
		for(int i=0; i<text.length; i++) {
			if(text[i].getText().trim().length() > 0)
				try { value[i] = Double.parseDouble(text[i].getText()); flag[i] = true; }
			catch (NumberFormatException e) { good = false; text[i].setText(""); }
			else flag[i] = false;
		}
		if(good) {
			xfsim.getConfiguration().limit.setLimits(flag, value);
			setVisible(false);
		}
		else Toolkit.getDefaultToolkit().beep();
	}
	
	/**
	 * Deselecciona las condiciones de parada de la simulación
	 */
	private void unset() {
		xfsim.getConfiguration().limit.clearLimits();
		setVisible(false);
	}
}

