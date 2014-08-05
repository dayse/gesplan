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
 * Diálogo para configurar una representación gráfica
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimPlotDialog extends JDialog implements ActionListener {

	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603062L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Clase que contiene la confoguración de la representación
	 */
	private XfsimPlot plot;
	
	/**
	 * Selector de la variable X
	 */
	private XComboBox xcombo;
	
	/**
	 * Selector de la variable Y
	 */
	private XComboBox ycombo;
	
	/**
	 * Grupo de botones para que sólo uno pueda estar seleccionado
	 */
	private ButtonGroup group;
	
	/**
	 * Selector del tipo de representación
	 */
	private JRadioButton[] radio;
	
	/**
	 * Marcador del resultado del diálogo
	 */
	private boolean result = false;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public XfsimPlotDialog(Xfsim xfsim, XfsimPlot plot) {
		super(xfsim,"Xfsim",true);
		this.plot = plot;
		
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		String xlist[] = new String[2+input.length+output.length];
		xlist[0] = "_n";
		xlist[1] = "_t";
		for(int i=0; i<output.length; i++) xlist[i+2] = output[i].getName();
		for(int i=0; i<input.length;i++) xlist[i+2+output.length]=input[i].getName();
		String ylist[] = new String[input.length+output.length];
		for(int i=0; i<output.length; i++) ylist[i] = output[i].getName();
		for(int i=0; i<input.length;i++) ylist[i+output.length]=input[i].getName();
		
		xcombo = new XComboBox("X axis");
		xcombo.setList(xlist);
		if(plot.getXvar()>=0) xcombo.setSelectedIndex(plot.getXvar());
		ycombo = new XComboBox("Y axis");
		ycombo.setList(ylist);
		if(plot.getYvar()>=0) ycombo.setSelectedIndex(plot.getYvar());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout( 3,2 ));
		group = new ButtonGroup();
		String radiolabel[] =
		{ "Line","Dots","Squares","Small circles","Medium circles","Large circles"};
		radio = new JRadioButton[radiolabel.length];
		for(int i=0; i<radio.length; i++) {
			radio[i] = new JRadioButton(radiolabel[i]);
			group.add(radio[i]);
			panel.add(radio[i]);
		}
		radio[plot.getKind()].setSelected(true);
		
		String lb[] = { "Set", "Cancel" };
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(100);
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Graphical representation"));
		content.add(xcombo);
		content.add(ycombo);
		content.add(new XLabel("Graphical style"));
		content.add(panel);
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
	 * Informa si se ha seleccionado la gráfica o se ha cancelado
	 */
	public boolean getResult() {
		return result;
	}
	
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
	 * Selecciona las variables a representar y el tipo de gráfica
	 */
	private void set() {
		int kind = 0;
		for(int i=0; i<radio.length; i++) if(radio[i].isSelected()) kind = i;
		plot.setVar(xcombo.getSelectedIndex(),ycombo.getSelectedIndex(),kind);
		result = true;
		setVisible(false);
	}
}

