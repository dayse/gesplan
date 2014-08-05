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
import javax.swing.table.*;
import java.awt.event.*;

/**
 * Desarrolla una tabla con celdas especiales que permiten editar el antecedente
 * y el consecuente de las reglas en formato libre
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseFreeForm extends JTable implements KeyListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603031L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Modelo de edición de la tabla
	 */
	private XfeditRulebaseFreeModel model;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseFreeForm(XfeditRulebaseFreeModel model) {
		super(model);
		addKeyListener(this);
		this.model = model;
		setDefaultRenderer(XLabel.class,model);
		setDefaultRenderer(Object.class,model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnSelectionAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setFont(XConstants.font);
		getColumnModel().getColumn(0).setMinWidth(50);
		getColumnModel().getColumn(0).setMaxWidth(50);
		getColumnModel().getColumn(0).setPreferredWidth(50);
		getColumnModel().getColumn(2).setMinWidth(30);
		getColumnModel().getColumn(2).setMaxWidth(30);
		getColumnModel().getColumn(2).setPreferredWidth(30);
		getColumnModel().getColumn(4).setMinWidth(30);
		getColumnModel().getColumn(4).setMaxWidth(30);
		getColumnModel().getColumn(4).setPreferredWidth(30);
		getColumnModel().getColumn(1).setPreferredWidth(50);
		getColumnModel().getColumn(3).setPreferredWidth(280);
		getColumnModel().getColumn(5).setPreferredWidth(140);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el editor de una celda de la tabla
	 */
	public TableCellEditor getCellEditor(int row, int column) {
		TableCellEditor editor = model.getEditor(row,column);
		if(editor == null) editor = getDefaultEditor(getColumnClass(column));
		return editor;
	}

	/**
	 * Elimina una fila (regla) de la tabla	
	 */
	public void removeRow() {
		int row = getSelectedRow();
		if(row <0 || row == model.getRowCount()-1) return;
		String msg[] =
		{ "You are going to delete a rule from the rulebase.","Continue?" };
		if(!XDialog.showQuestion(this,msg)) return;
		model.removeRow(row);
	}

	/**
	 * Ejecuta la acción sobre la celda seleccionada
	 */
	public void tableAction(int kind) {
		int row = getSelectedRow();
		int column = getSelectedColumn();
		if(row < 0 || row == model.getRowCount()-1) return;
		if(column == 5 && kind == -2) model.modelAction(row,-2);
		if(column == 3 && kind > -2) model.modelAction(row,kind);
	}

	/**
	 * Ejecuta la acción sobre la celda seleccionada
	 */
	public void tableAction(Variable var, LinguisticLabel pmf) {
		int row = getSelectedRow();
		int column = getSelectedColumn();
		if(row < 0 || row == model.getRowCount()-1) return;
		if(column == 5) model.changeConclusion(row, var, pmf);
		if(column == 3) model.changePremise(row, var, pmf);
	}

	/**
	 * Interfaz KeyListener. Acción al soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_BACK_SPACE) removeRow();
		if(code == KeyEvent.VK_DELETE) removeRow();
		if(code == KeyEvent.VK_CUT) removeRow();
		e.consume();
	}
	
	/**
	 * Interfaz KeyListener. Acción al apretar una tecla
	 */
	public void keyPressed(KeyEvent e) {	
	}

	/**
	 * Interfaz KeyListener. Acción al teclear.
	 */
	public void keyTyped(KeyEvent e) { 
	}
}


