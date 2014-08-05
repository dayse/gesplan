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
import java.awt.event.*;

/**
 * Diálogo para crear o editar una variable lingüística
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditVariableDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603043L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia a la ventana de la herramienta Xfedit en caso de que la variable
	 * sea una variable global del sistema difuso
	 */
	private Xfedit xfedit;
	
	/**
	 * Referencia al editor de base de reglas en caso de que la variable
	 * pertenezca a una base de reglas
	 */
	private XfeditRulebaseEditor rbedit;
	
	/**
	 * Campo para editar el nombre de la variable
	 */
	private XTextForm varname;
	
	/**
	 * Menú desplegable para seleccionar el tipo de la variable
	 */
	private XComboBox vartype;
	
	/**
	 * Variable a editar
	 */
	private Variable var;
	
	/**
	 * Indicador del carácter de la variable: de entrada (Variable.INPUT)
	 * o de salida (Variable.OUTPUT)
	 */
	private int kind;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor para crear una nueva variable global
	 */
	public XfeditVariableDialog(int kind, Xfedit xfedit) {
		super(xfedit,"Xfedit", true);
		this.xfedit = xfedit;
		this.var = null;
		this.kind = kind;
		build();
		setLocationRelativeTo(xfedit);
		refresh();
	}

	/**
	 * Constructor para editar una variable global existente
	 */
	public XfeditVariableDialog(Variable var, Xfedit xfedit) {
		super(xfedit,"Xfedit", true);
		this.xfedit = xfedit;
		this.var = var;
		this.kind = -1;
		build();
		setLocationRelativeTo(xfedit);
		refresh();
	}

	/**
	 * Constructor para crear una nueva variable local
	 */
	public XfeditVariableDialog(int kind, XfeditRulebaseEditor rbedit) {
		super(rbedit,"Xfedit", true);
		this.rbedit = rbedit;
		this.var = null;
		this.kind = kind;
		build();
		setLocationRelativeTo(rbedit);
		refresh();
	}

	/**
	 * Constructor para editar una variable local existente
	 */
	public XfeditVariableDialog(Variable var, XfeditRulebaseEditor rbedit) {
		super(rbedit,"Xfedit", true);
		this.rbedit = rbedit;
		this.var = var;
		this.kind = -1;
		build();
		setLocationRelativeTo(rbedit);
		refresh();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("VariableSet")) { if(set()) setVisible(false); }
		else if(command.equals("VariableCancel")) setVisible(false);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el contenido del diálogo
	 */
	private void build() {
		String lb[] = {"Set","Cancel"};
		String command[] = { "VariableSet", "VariableCancel" };
		XCommandForm form = new XCommandForm(lb,command,this);
		form.setCommandWidth(120);
		form.block();

		varname = new XTextForm("Name");
		varname.setWidth(100,150);
		varname.setAlignment(JLabel.CENTER);
		vartype = new XComboBox("Type");
		vartype.setWidth(100,150);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Variable Properties"));
		content.add(Box.createVerticalStrut(5));
		content.add(varname);
		content.add(Box.createVerticalStrut(5));
		content.add(vartype);
		content.add(Box.createVerticalStrut(5));
		content.add(form);
		setFont(XConstants.font);
		pack();
	}

	/**
	 * Actualiza el contenido del menú desplegable
	 */
	private void refresh() {
		if(xfedit != null) vartype.setList(xfedit.getSpecification().getTypes());
		if(rbedit != null) vartype.setList(rbedit.getSpecification().getTypes());
		if(var == null) return;
		varname.setText(var.getName());
		vartype.setSelectedItem(var.getType());
		vartype.setEnabled(false);
	}

	/**
	 * Lee el contenido del diálogo
	 */
	private boolean set() {
		String name = varname.getText();
		if(!XConstants.isIdentifier(name)) {
			varname.setText("");
			XDialog.showMessage(varname,"Invalid Name");
			return false;
		}

		Variable search = null;
		if(xfedit != null) {
			search = xfedit.getSpecification().getSystemModule().searchVariable(name);
		} else {
			search = rbedit.getWorkingCopy().searchVariable(name);
		}

		if(search != null && search != var) {
			varname.setText("");
			XDialog.showMessage(varname,"Invalid Name: Variable already exists");
			return false;
		}

		if(var != null) { var.setName(name); return true; }
		Type type = (Type) vartype.getSelectedItem();
		if(type == null) {
			XDialog.showMessage(vartype,"Variable type not choosen");
			return false;
		}

		if(kind == Variable.INPUT) var = new Variable(name,type,Variable.INPUT);
		else if(xfedit != null) var = new Variable(name,type,Variable.OUTPUT);
		else if(rbedit != null) var = new Variable(name,type,rbedit.getWorkingCopy());

		if(xfedit!=null) xfedit.getSpecification().getSystemModule().addVariable(var);
		else if(kind == Variable.INPUT) rbedit.getWorkingCopy().addInputVariable(var);
		else rbedit.getWorkingCopy().addOutputVariable(var);

		return true;
	}

}

