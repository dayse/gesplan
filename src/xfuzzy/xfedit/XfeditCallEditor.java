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
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * Diálogo para crear una llamada a una base de reglas o a un módulo no difuso
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class XfeditCallEditor extends JDialog
implements ActionListener, MouseListener, ListSelectionListener, KeyListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603020L;

	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código para indicar que el módulo es una base de reglas
	 */
	private static final int RULEBASE = 1;
	
	/**
	 * Código para indicar que el módulo es no difuso
	 */
	private static final int CRISPBLOCK = 2;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de bases de reglas disponibles
	 */
	private XList rblist;
	
	/**
	 * Lista de módulos no difusos disponibles
	 */
	private XList cblist;
	
	/**
	 * Base de reglas seleccionada
	 */
	private Rulebase rulebase;
	
	/**
	 * Módulo no difuso seleccionado
	 */
	private CrispBlock crispblock;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditCallEditor(Xfedit xfedit) {
		super(xfedit,"Xfedit", true);

		String lb[] = {"Create","Cancel"};
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(120);
		form.block();

		rblist = new XList("Available rulebases");
		rblist.setPreferredWidth(100);
		rblist.addMouseListener(this);
		rblist.addListSelectionListener(this);
		rblist.addKeyListener(this);
		rblist.setListData(xfedit.getSpecification().getRulebases());

		cblist = new XList("Available crisp blocks");
		cblist.setPreferredWidth(100);
		cblist.addMouseListener(this);
		cblist.addListSelectionListener(this);
		cblist.addKeyListener(this);
		cblist.setListData(xfedit.getSpecification().getCrispBlockSet().getBlocks());

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createHorizontalStrut(5));
		box.add(rblist);
		box.add(Box.createHorizontalStrut(5));
		box.add(cblist);
		box.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(Box.createVerticalStrut(5));
		content.add(box);
		content.add(Box.createVerticalStrut(5));
		content.add(form);
		setFont(XConstants.font);
		setLocationRelativeTo(xfedit);
		pack();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Devuelve el objeto seleccionado
	 */
	public Object getSelection() {
		if(rulebase != null) return rulebase;
		if(crispblock != null) return crispblock;
		return null;
	}

	/**
	 * Interfaz MouseListener. Acción al pulsar el ratón
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) { if(set()) setVisible(false); }
	}
	
	/**
	 * Interfaz MouseListener. Acción al entrar en la ventana
	 */	
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acción al salir de la ventana
	 */
	public void mouseExited(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener. Acción al apretar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Interfaz MouseListener. Acción al soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Create")) { if(set()) setVisible(false); }
		else if(command.equals("Cancel")) setVisible(false);
	}

	/**
	 * Interfaz ListSelectionListener
	 */
	public void valueChanged(ListSelectionEvent e) {
		int list = getEventModule(e);
		listSelection(list);
	}

	/**
	 * Interfaz KeyListener. Acción al soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == 226) {
			e.consume();
			if(set()) setVisible(false);
		}
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

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Elimina la selección de una lista al activar otra
	 */
	private void listSelection(int list) {
		if(list == RULEBASE && rblist.isSelectionEmpty()) return;
		if(list == CRISPBLOCK && cblist.isSelectionEmpty()) return;
		if(list != RULEBASE) rblist.clearSelection();
		if(list != CRISPBLOCK) cblist.clearSelection();
	}

	/**
	 * Obtiene el código de la lista que genera el evento
	 */
	private int getEventModule(EventObject e) {
		int kind = -1;
		if(e.getSource() == rblist.getList() ) kind = RULEBASE;
		if(e.getSource() == cblist.getList() ) kind = CRISPBLOCK;
		return kind;
	}

	/**
	 * Verifica que se haya seleccionado un módulo
	 */
	private boolean set() {
		if(!rblist.isSelectionEmpty()) {
			rulebase = (Rulebase) rblist.getSelectedValue();
		} else if(!cblist.isSelectionEmpty()) {
			crispblock = (CrispBlock) cblist.getSelectedValue();
		}
		return (rulebase != null || crispblock != null);
	}
}
