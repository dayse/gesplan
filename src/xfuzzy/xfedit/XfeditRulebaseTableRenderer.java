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


package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * Clase que describe el aspecto de los elementos en la representación
 * tabular de las bases de reglas. Tiene como finalidad transformar
 * el valor seleccionado en el combobox en el valor mostrado en la celda
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseTableRenderer implements ListCellRenderer {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Modelo de la tabla
	 */
	private XfeditRulebaseTableModel model;
	
	/**
	 * Índice de la variable asociada a la celda
	 */
	private int i;
	
	/**
	 * Permite distinguir entre celdas de entrada y de salida
	 */
	private boolean inputcombo;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseTableRenderer(Object model, int i, boolean inputcombo) {
		this.model = (XfeditRulebaseTableModel) model;
		this.i = i;
		this.inputcombo = inputcombo;
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Interfaz ListCellRenderer
	 */
	public Component getListCellRendererComponent( JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		LinguisticLabel pmf = null;
		try { pmf = (LinguisticLabel) value; } catch(Exception ex) {}
		String label;
		if(index == -1 && pmf != null && inputcombo)
			label = model.getInput(i)+" == "+pmf;
		else if(index == -1 && pmf !=null && !inputcombo)
			label = model.getOutput(i)+" = "+pmf;
		else if(pmf == null) label = " ";
		else label = pmf.toString();
		JLabel renderer = new JLabel(label);
		renderer.setForeground(Color.black);
		if(isSelected) renderer.setBackground(XConstants.textselectedbg);
		else renderer.setBackground(XConstants.textbackground);
		renderer.setOpaque(true);
		renderer.setFont(XConstants.textfont);
		return renderer;
	}
}

