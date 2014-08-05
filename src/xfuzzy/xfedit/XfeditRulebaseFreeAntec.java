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
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * Campo de texto que permite editar el antecedente de una regla
 * en la representación libre de la base de reglas
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseFreeAntec extends JTextField
implements TableCellEditor, ActionListener, MouseListener, KeyListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603029L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Panel de representación de la base de reglas en formato libre
	 */
	private XfeditRulebaseFreePanel panel;

	/**
	 * Objeto que describe el antecedente completo
	 */
	private XfeditRulebaseFreeRelation block;

	/**
	 * Objeto que describe la parte seleccionada del antecedente
	 */
	private XfeditRulebaseFreeRelation selected;

	/**
	 * Lista de controladores de edición
	 */
	private EventListenerList listenerList = new EventListenerList();

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseFreeAntec(Object panel, Relation rel) {
		super("");
		this.panel = (XfeditRulebaseFreePanel) panel;
		setEditable(false);
		setBackground(XConstants.textbackground);
		setFont(XConstants.textfont);
		addActionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		this.block = new XfeditRulebaseFreeRelation(rel,null);
		setText(block.toString());
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la representación del antecedente
	 */
	public String toString() {
		return block.toString();
	}

	/**
	 * Actualiza el contenido del componente
	 */
	public void actualize() {
		setText(block.toString());
		if(selected == null) return;
		selected.end = selected.begin+selected.width();
		select(selected.begin,selected.end);
	}

	/**
	 * Estudia si el antecedente se ha definido por completo
	 */
	public boolean isIncomplete() {
		return this.block.isIncomplete();
	}

	/**
	 * Obtiene la proposición que representa el antecedente
	 */
	public Relation getAntecedent() {
		return this.block.rel;
	}

	/**
	 * Asigna la variable de la proposición selecionada
	 */
	public void setVariable(Variable var) {
		if(selected==null || selected.rel==null) return;
		if(!(selected.rel instanceof SingleRelation)) return;
		selected.rel.setVariable(var);
		actualize();
	}

	/**
	 * Asigna la MF de la proposicion seleccionada
	 */
	public void setMembershipFunction(LinguisticLabel pmf) {
		if(selected==null || selected.rel==null) return;
		if(!(selected.rel instanceof SingleRelation)) return;
		selected.rel.setMembershipFunction(pmf);
		actualize();
	}

	/**
	 * Acción a realizar segun el botón pulsado
	 */
	public void action(int kind) {
		switch(kind) {
		case Relation.AND:
		case Relation.OR: premiseBinary(kind); return;
		case Relation.NOT:
		case Relation.MoL:
		case Relation.SLIGHTLY:
		case Relation.VERY: premiseUnary(kind); return;
		case Relation.IS:
		case Relation.SM_EQ:
		case Relation.SMALLER:
		case Relation.ISNOT:
		case Relation.GR_EQ:
		case Relation.APP_EQ:
		case Relation.VERY_EQ:
		case Relation.SL_EQ:
		case Relation.GREATER: premiseSingle(kind); return;
		case -1: premisePrune(); return;
		}
	}

	/**
	 * Interfaz TableCellEditor: Obtiene la descripción del objeto
	 */
	public Object getCellEditorValue() {
		return toString();
	}

	/**
	 * Interfaz TableCellEditor: Estudia si la celda es editable
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	/**
	 * Interfaz TableCellEditor: Estudia si la celda es seleccionable
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	/**
	 * Interfaz TableCellEditor: Detiene la edición
	 */
	public boolean stopCellEditing() {
		this.selected = null;
		actualize();
		fireEditingStopped();
		return true;
	}

	/**
	 * Interfaz TableCellEditor: Cancela la edición
	 */
	public void cancelCellEditing() {
		this.selected = null;
		actualize();
		fireEditingCanceled();
	}

	/**
	 * Interfaz TableCellEditor: Añade un controlador de edición
	 */
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}

	/**
	 * Interfaz TableCellEditor: Elimina un controlador de edición
	 */
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}

	/**
	 * Interfaz TableCellEditor: Componente que describe la celda
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		return this;
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		fireEditingStopped();
	}

	/**
	 * Interfaz KeyListener. Acción de soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SPACE) premiseUp();
		if(code == KeyEvent.VK_BACK_SPACE) premiseRemove();
		if(code == KeyEvent.VK_DELETE) premiseRemove();
		if(code == KeyEvent.VK_CUT) premiseRemove();
		e.consume();
	}

	/**
	 * Interfaz KeyListener. Acción de pulsar una tecla  
	 */
	public void keyPressed(KeyEvent e) {
		e.consume();
	}
	
	/**
	 * Interfaz KeyListener. Acción de teclear
	 */
	public void keyTyped(KeyEvent e) { 
		e.consume(); 
	}

	/**
	 * Interfaz MouseListener. Acción de pulsar un botón del ratón
	 */
	public void mouseClicked(MouseEvent e) { 
		premiseClick(); 
	}
	
	/**
	 * Interfaz MouseListener. Acción de apretar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acción de soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acción de entrar en el componente
	 */
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acción de salir del componente
	 */
	public void mouseExited(MouseEvent e) {
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Poda la proposición seleccionada	
	 */
	private void premisePrune() {
		if(selected == null || selected.rel == null) return;
		if(selected.rel.isBinary()) {
			XfeditRulebaseFreeRelation lblock = selected.left;
			XfeditRulebaseFreeRelation rblock = selected.right;
			if(lblock.rel != null && rblock.rel != null) return;
			Relation oldrel = selected.rel;
			XfeditRulebaseFreeRelation newblock = (lblock.rel != null? lblock : rblock);
			Relation newrel = newblock.rel;
			if(selected.parent != null) {
				Relation prel = selected.parent.rel;
				if(prel.getLeftRelation() == oldrel) prel.setLeftRelation(newrel);
				if(prel.getRightRelation() == oldrel) prel.setRightRelation(newrel);
			}
			selected.rel = newblock.rel;
			selected.left = newblock.left;
			selected.right = newblock.right;
		}
		if(selected.rel.isUnary()) {
			Relation oldrel = selected.rel;
			Relation newrel = selected.right.rel;
			if(selected.parent != null) {
				Relation prel = selected.parent.rel;
				if(prel.getLeftRelation() == oldrel) prel.setLeftRelation(newrel);
				if(prel.getRightRelation() == oldrel) prel.setRightRelation(newrel);
			}
			selected.rel = newrel;
			selected.left = selected.right.left;
			selected.right = selected.right.right;
		}
		actualize();
		premiseSetCombo();
	}

	/**
	 * Genera una proposición unaria de un cierto tipo
	 */
	private void premiseUnary(int kind) {
		if(selected == null) return;
		XfeditRulebaseFreeRelation nb = new XfeditRulebaseFreeRelation();
		nb.rel = selected.rel;
		nb.parent = selected;
		nb.left = selected.left;
		nb.right = selected.right;
		Relation rel = panel.createCompoundRelation(nb.rel,kind);
		selected.rel = rel;
		selected.left = null;
		selected.right = nb;
		if(selected.parent != null) {
			Relation prel = selected.parent.rel;
			if(prel.getLeftRelation() == nb.rel) prel.setLeftRelation(rel);
			if(prel.getRightRelation() == nb.rel) prel.setRightRelation(rel);
		}
		actualize();
		premiseSetCombo();
	}

	/**
	 * Genera una proposición binaria de un cierto tipo
	 */
	private void premiseBinary(int kind) {
		if(selected == null) return;
		XfeditRulebaseFreeRelation nb = new XfeditRulebaseFreeRelation();
		nb.rel = selected.rel;
		nb.parent = selected;
		nb.left = selected.left;
		nb.right = selected.right;
		Relation rel = panel.createCompoundRelation(nb.rel,kind);
		selected.rel = rel;
		selected.left = nb;
		selected.right = new XfeditRulebaseFreeRelation(null,selected);
		if(selected.parent != null) {
			Relation prel = selected.parent.rel;
			if(prel.getLeftRelation() == nb.rel) prel.setLeftRelation(rel);
			if(prel.getRightRelation() == nb.rel) prel.setRightRelation(rel);
		}
		actualize();
		premiseSetCombo();
	}


	/**
	 * Genera una proposición simple de un cierto tipo
	 */
	private void premiseSingle(int kind) {
		if(selected == null) return;
		if(selected.left != null || selected.right != null) return;
		Relation oldrel = selected.rel;
		Relation newrel = panel.createSingleRelation(selected.rel,kind);
		if(newrel == null) return;
		selected.rel = newrel;
		if(selected.parent != null) {
			Relation prel = selected.parent.rel;
			if(prel.getLeftRelation() == oldrel) prel.setLeftRelation(newrel);
			if(prel.getRightRelation() == oldrel) prel.setRightRelation(newrel);
		}
		actualize();
		premiseSetCombo();
	}

	/**
	 * Actualiza el contenido de los menús desplegables del panel de
	 * representación en formato libre
	 */
	private void premiseSetCombo() {
		if(selected == null || selected.rel == null || !selected.rel.isSingle()){
			panel.setComboBoxes(null,null,true);
		} else {
			Variable selvar = selected.rel.getVariable();
			LinguisticLabel selmf = selected.rel.getMembershipFunction();
			panel.setComboBoxes(selvar, selmf, true);
		}
	}

	/**
	 * Selecciona el padre de la proposición seleccionada
	 */
	private void premiseUp() {
		if(selected == null) return;
		if(selected.getParent() == null) return;
		selected = selected.getParent();
		select(selected.begin,selected.end);
		premiseSetCombo();
	}

	/**
	 * Elimina la proposición seleccionada
	 */
	private void premiseRemove() {
		if(selected == null) return;
		Relation oldrel = selected.rel;
		if(selected.parent != null) {
			Relation prel = selected.parent.rel;
			if(prel.getLeftRelation() == oldrel) prel.setLeftRelation(null);
			if(prel.getRightRelation() == oldrel) prel.setRightRelation(null);
		}
		oldrel.dispose();
		selected.rel = null;
		selected.left = null;
		selected.right = null;
		actualize();
		premiseSetCombo();
	}

	/**
	 * Obtiene la proposición seleccionada en funcion del cursor
	 */
	private void premiseClick() {
		selected = block.select(0,getCaretPosition());
		actualize();
		premiseSetCombo();
	}

	/**
	 * Dispara el evento de fin de edición
	 */
	private void fireEditingStopped() {
		ChangeEvent event = new ChangeEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for(int i = listeners.length-2; i>=0; i-=2)
			if(listeners[i]==CellEditorListener.class)
				((CellEditorListener)listeners[i+1]).editingStopped(event);
	}

	/**
	 * Dispara el evento de cancelación de edición
	 */
	private void fireEditingCanceled() {
		ChangeEvent event = new ChangeEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for(int i = listeners.length-2; i>=0; i-=2) 
			if(listeners[i]==CellEditorListener.class)
				((CellEditorListener)listeners[i+1]).editingCanceled(event);
	}

}

