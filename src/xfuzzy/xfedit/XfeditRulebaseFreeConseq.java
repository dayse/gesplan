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
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * Campo de texto que permite editar el consecuente de una regla
 * en la representaci�n libre de la base de reglas
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class XfeditRulebaseFreeConseq extends JTextField
implements TableCellEditor, ActionListener, MouseListener, KeyListener {

	/**
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603030L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Panel de representaci�n de la base de reglas en formato libre
	 */
	private XfeditRulebaseFreePanel panel;
	
	/**
	 * Lista de conclusiones a mostrar
	 */
	private Conclusion conc[];
	
	/**
	 * Objeto seleccionado en el editor
	 */
	private Conclusion selected;
	
	/**
	 * Lista de controladores de edici�n
	 */
	private EventListenerList listenerList = new EventListenerList();

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseFreeConseq(Object panel, Conclusion[] conc) {
		super("");
		this.panel = (XfeditRulebaseFreePanel) panel;
		this.conc = conc;
		setEditable(false);
		setBorder(new LineBorder(Color.black, 1));
		setBackground(XConstants.textbackground);
		setFont(XConstants.textfont);
		addActionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		setText(toString());
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la descripci�n del consecuente de la regla
	 */
	public String toString() {
		if(conc == null || conc.length == 0) return " ? ";
		String code = conc[0].toXfl();
		for(int i=1; i<conc.length; i++) code += ", "+conc[i].toXfl();
		return code;
	}

	/**
	 * Actualiza el contenido del texto
	 */
	public void actualize() {
		if(conc == null || conc.length == 0) {
			selected = null;
			setText(" ? ");
			return;
		}
		String code = conc[0].toXfl();
		for(int i=1; i<conc.length; i++) code += ", "+conc[i].toXfl();
		setText(code);
		selectText();
	}

	/**
	 * Estudia si el consecuente esta vac�o
	 */
	public boolean isIncomplete() {
		return (conc==null || conc.length==0);
	}

	/**
	 * Obtiene la lista de consecuentes
	 */
	public Conclusion[] getConsequents() {
		return this.conc;
	}

	/**
	 * Modifica la variable de la conclusi�n seleccionada
	 */
	public void setVariable(Variable var) {
		if(selected == null) return;
		selected.setVariable(var);
		actualize();
	}

	/**
	 * Modifica la MF de la conclusi�n seleccionada
	 */
	public void setMembershipFunction(LinguisticLabel pmf) {
		if(selected == null) return;
		selected.setMembershipFunction(pmf);
		actualize();
	}

	/**
	 * Crea una nueva conclusi�n con el contenido de los comboboxes
	 */
	public void conclusionAdd() {
		Conclusion nc = panel.createConclusion();
		if(nc == null) return;
		selected = nc;
		Conclusion ac[] = new Conclusion[conc.length+1];
		System.arraycopy(conc,0,ac,0,conc.length);
		ac[conc.length] = selected;
		conc = ac;
		actualize();
		conclusionSetCombo();
	}

	/**
	 * Interfaz TableCellEditor: Obtiene el texto del editor
	 */
	public Object getCellEditorValue() {
		return toString();
	}

	/**
	 * Interfaz TableCellEditor: Estudia si es editable
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	/**
	 * Interfaz TableCellEditor: Estudia si un evento debe seleccionar la celda
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	/**
	 * Interfaz TableCellEditor: Detiene la edici�n
	 */
	public boolean stopCellEditing() {
		this.selected = null;
		actualize();
		fireEditingStopped();
		return true;
	}

	/**
	 * Interfaz TableCellEditor: Detiene la edici�n
	 */
	public void cancelCellEditing() {
		this.selected = null;
		actualize();
		fireEditingCanceled();
	}

	/**
	 * Interfaz TableCellEditor: A�ade un controlador de edici�n
	 */
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}

	/**
	 * Interfaz TableCellEditor: Elimina un controlador de edici�n
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
	 * Interfaz KeyListener. Acci�n de soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_BACK_SPACE) conclusionRemove();
		if(code == KeyEvent.VK_DELETE) conclusionRemove();
		if(code == KeyEvent.VK_CUT) conclusionRemove();
		e.consume();
	}
	
	/**
	 * Interfaz KeyListener. Acci�n de pulsar una tecla  
	 */
	public void keyPressed(KeyEvent e) { 
		e.consume();
	}
	
	/**
	 * Interfaz KeyListener. Acci�n de teclear
	 */
	public void keyTyped(KeyEvent e) {
		e.consume();
	}

	/**
	 * Interfaz MouseListener. Acci�n de pulsar un bot�n del rat�n
	 */
	public void mouseClicked(MouseEvent e) { 
		conclusionClick(); 
	}
	
	/**
	 * Interfaz MouseListener. Acci�n de apretar un bot�n del rat�n
	 */
	public void mousePressed(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acci�n de soltar un bot�n del rat�n
	 */
	public void mouseReleased(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acci�n de entrar en el componente
	 */
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acci�n de salir del componente
	 */
	public void mouseExited(MouseEvent e) {
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Actualiza el contenido de los menus desplegables
	 */
	private void conclusionSetCombo() {
		if(selected == null) panel.setComboBoxes(null,null,false);
		else {
			Variable selvar = selected.getVariable();
			LinguisticLabel selmf = selected.getMembershipFunction();
			panel.setComboBoxes(selvar, selmf, false);
		}
	}

	/**
	 * Obtiene la conclusi�n seleccionada en funci�n del �ndice
	 */
	private Conclusion conclusionSelect(int index) {
		if(conc == null || conc.length == 0) return null;
		String code = conc[0].toXfl();
		if(index <= code.length()+1) return conc[0]; 
		for(int i=1; i<conc.length; i++) {
			code += ", "+conc[i].toXfl();
			if(index <= code.length()+1) return conc[i];
		}
		return null;
	}

	/**
	 * Selecciona el texto de la conclusi�n seleccionada
	 */
	private void selectText() {
		if(selected == null) return;
		int begin = 0;
		String code = "";
		for(int i=0; i<conc.length; i++) {
			if(selected==conc[i]) select(begin, begin+conc[i].toXfl().length());
			else { code += (i==0? ", ": "")+conc[i].toXfl(); begin = code.length(); }
		}
	}

	/**
	 * Elimina la conclusi�n seleccionada
	 */
	private void conclusionRemove() {
		if(selected == null) return;
		int index = -1;
		for(int i=0; i<conc.length; i++) if(conc[i] == selected) index = i;
		if(index == -1) return;
		Conclusion ac[] = new Conclusion[conc.length-1];
		System.arraycopy(conc,0,ac,0,index);
		System.arraycopy(conc,index+1,ac,index, ac.length-index);
		conc = ac;
		selected = null;
		actualize();
		conclusionSetCombo();
	}

	/**
	 * Selecciona la conclusi�n de la posicion del cursor
	 */
	private void conclusionClick() {
		selected = conclusionSelect(getCaretPosition());
		selectText();
		conclusionSetCombo();
	}

	/**
	 * Dispara el evento de fin de edici�n
	 */
	private void fireEditingStopped() {
		ChangeEvent event = new ChangeEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for(int i = listeners.length-2; i>=0; i-=2)
			if(listeners[i]==CellEditorListener.class)
				((CellEditorListener)listeners[i+1]).editingStopped(event);
	}

	/**
	 * Dispara el evento de cancelaci�n de edici�n
	 */
	private void fireEditingCanceled() {
		ChangeEvent event = new ChangeEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for(int i = listeners.length-2; i>=0; i-=2)
			if(listeners[i]==CellEditorListener.class)
				((CellEditorListener)listeners[i+1]).editingCanceled(event);
	}
}

