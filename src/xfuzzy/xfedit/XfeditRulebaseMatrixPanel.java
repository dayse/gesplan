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


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//REPRESENTACION MATRICIAL DE LA BASE DE REGLAS		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;

public class XfeditRulebaseMatrixPanel extends JPanel {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603035L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Modelo de la tabla que representa la base de reglas en formato matricial
	 */
	private XfeditRulebaseMatrixModel model;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseMatrixPanel() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene las reglas y las almacena en la base de reglas
	 */
	public boolean getRules(Rulebase copy) {
		if(model == null) return true;
		else return model.getRules(copy);
	}

	/**
	 * Actualiza el contenido del panel
	 */
	public void refresh(Rulebase copy) {
		removeAll();
		Variable inputs[] = copy.getInputs();
		Variable outputs[] = copy.getOutputs();
		if(inputs.length != 2 || outputs.length != 1) {
			add(Box.createVerticalGlue());
			add(Box.createVerticalStrut(10));
			add(msglabel("Cannot represent this rulebase as a matrix"));
			add(Box.createVerticalStrut(10));
			add(msglabel("Matrix form needs two input variables"));
			add(msglabel("and one output variable"));
			add(Box.createVerticalStrut(10));
			add(Box.createVerticalGlue());
			return;
		}
		model = new XfeditRulebaseMatrixModel(copy);
		if(!model.isValid()) {
			add(Box.createVerticalGlue());
			add(Box.createVerticalStrut(10));
			add(msglabel("Cannot represent this rulebase as a matrix"));
			add(Box.createVerticalStrut(10));
			add(msglabel("Rules must include only AND conectives"));
			add(msglabel("and equality relations"));
			add(Box.createVerticalStrut(10));
			add(Box.createVerticalGlue());
			return;
		}

		JLabel label0 = new JLabel(inputs[0].toString());
		label0.setForeground(Color.black);
		label0.setFont(XConstants.font);

		JLabel label1 = new JLabel(inputs[1].toString());
		label1.setForeground(Color.black);
		label1.setFont(XConstants.font);

		JTable table = new JTable(model);
		table.setDefaultRenderer(Object.class,model);
		table.setDefaultEditor(Object.class,model.getEditor());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setFont(XConstants.font);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createHorizontalStrut(10));
		box.add(label0);
		box.add(Box.createHorizontalStrut(10));
		box.add(new JScrollPane(table));
		box.add(Box.createHorizontalStrut(10));

		add(Box.createVerticalGlue());
		add(Box.createVerticalStrut(10));
		add(label1);
		add(Box.createVerticalStrut(10));
		add(box);
		add(Box.createVerticalStrut(10));
		add(Box.createVerticalGlue());
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera una etiqueta con el mensaje especificado
	 */
	private Box msglabel(String msg) {
		JLabel label = new JLabel(msg);
		label.setFont(XConstants.font);
		label.setForeground(Color.red);
		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createHorizontalGlue());
		box.add(label);
		box.add(Box.createHorizontalGlue());
		return box;
	}
}

