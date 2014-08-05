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
import java.util.Vector;
import java.util.EventObject;

/**
 * Dialogo de edición de un conjunto de operadores
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditOpsetEditor extends JDialog implements ActionListener,
ItemListener, WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603026L;

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombres de los operadores
	 */
	private static String oplabel[] = {
		"and", "or", "not", "also", "implication", "moreorless", "strongly",
		"slightly", "defuzzification"
	};

	/**
	 * Códigos de los operadores según FuzzyOperator
	 */
	private static int opcode[] = {
		FuzzyOperator.AND, FuzzyOperator.OR, FuzzyOperator.NOT, FuzzyOperator.ALSO,
		FuzzyOperator.IMP, FuzzyOperator.MOREORLESS, FuzzyOperator.VERY,
		FuzzyOperator.SLIGHTLY, FuzzyOperator.DEFUZMETHOD
	};

	/**
	 * Códigos de los operadores según XflPackage
	 */
	private static int fzcode[] = {
		XflPackage.BINARY, XflPackage.BINARY, XflPackage.UNARY, XflPackage.BINARY,
		XflPackage.BINARY, XflPackage.UNARY, XflPackage.UNARY, XflPackage.UNARY,
		XflPackage.DEFUZ
	};

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Editor de tipos que realiza la llamada a XfeditOpsetEditor
	 */
	private Xfedit xfeditor;
	
	/**
	 * Sistema difuso que se está editando
	 */
	private Specification spec;
	
	/**
	 * Objeto original que describe el conjunto de operadores a editar
	 */
	private Operatorset original;
	
	/**
	 * Copia de trabajo sobre la que se realizan los cambios
	 * de edición del conjunto de operadores
	 */
	private Operatorset copy;
	
	/**
	 * Campo para introducir el nombre del conjunto de operadores
	 */
	private XTextForm nameform;
	
	/**
	 * Lista de campos para introducir los valores de los operadores
	 */
	private XComboBox opcombo[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditOpsetEditor(Xfedit xfeditor, Operatorset opset) {
		super(xfeditor,"Xfedit",false);
		this.xfeditor = xfeditor;
		this.spec = xfeditor.getSpecification();
		this.original = opset;
		if(opset != null) {
			this.original.setEditing(true);
			this.copy = (Operatorset) opset.clone();
		}
		else this.copy = new Operatorset("");
		build();
		setName();
		setOperators();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Abre una ventana de edición de un conjunto de operadores
	 */
	public static void showOpsetEditor(Xfedit xfeditor, Operatorset opset) {
		XfeditOpsetEditor editor = new XfeditOpsetEditor(xfeditor,opset);
		editor.setVisible(true);
		editor.repaint();
	}

	/**
	 * Obtiene la referencia al conjunto de operadores editado
	 */
	public Operatorset getWorkingCopy() {
		return this.copy;
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Ok")) ok();
		else if(command.equals("Apply")) apply();
		else if(command.equals("Reload")) reload();
		else if(command.equals("Cancel")) cancel();
	}

	/**
	 * Interfaz ItemListener
	 */
	public void itemStateChanged(ItemEvent e) {
		int opindex = getComboBoxIndex(e);
		if(opcombo[opindex].getSelectedItem() == null) return;
		if(e.getStateChange() != ItemEvent.SELECTED) return;
		FuzzyOperator operator;

		String sel = (String) opcombo[opindex].getSelectedItem();

		if(sel.equals("default")) operator = Operatorset.getDefault(opcode[opindex]);
		else {
			int index = sel.indexOf('.');
			String pkgname = sel.substring(0,index);
			String funcname = sel.substring(index+1);
			operator = copy.get(opcode[opindex]);
			if(!pkgname.equals(operator.getPackageName()) ||
					!funcname.equals(operator.getFunctionName()) || 
					operator.isDefault() ) {
				XflPackageBank pkgbank = xfeditor.getXflPackageBank(); 
				operator = (FuzzyOperator) pkgbank.instantiate(pkgname,funcname,fzcode[opindex]);
			}
		}

		String plname = operator.getParamListName();
		if(operator.isDefault()) {
			copy.set(operator,opcode[opindex]);
		} else if( (plname==null || plname.length()==0) && operator.get().length==0) {
			copy.set(operator,opcode[opindex]);
		} else {
			XfeditOpsetParamDialog dialog;
			dialog = new XfeditOpsetParamDialog(xfeditor, operator, oplabel[opindex]);
			if(dialog.showDialog()) copy.set(operator,opcode[opindex]);
		}
		opcombo[opindex].setSelectedItem(null);
	}

	/**
	 * Interfaz WindowListener. Acción al abrir la ventana
	 */
	public void windowOpened(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al cerrar la ventana
	 */
	public void windowClosing(WindowEvent e) {
		cancel();
	}
	
	/**
	 * Interfaz WindowListener. Acción al terminar de cerrar la ventana
	 */
	public void windowClosed(WindowEvent e) {	
	}
	
	/**
	 * Interfaz WindowListener. Acción al minimizar la ventana
	 */
	public void windowIconified(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al maximizar la ventana
	 */
	public void windowDeiconified(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al activar la ventana
	 */
	public void windowActivated(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al desactivar la ventana
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el contenido de la ventana
	 */
	private void build() {
		String lb[] = {"Ok","Apply","Reload","Cancel"};
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(80);
		form.block();

		nameform = new XTextForm("Name");
		nameform.setLabelWidth(150);
		nameform.setFieldWidth(200);
		nameform.setAlignment(JLabel.CENTER);

		Box namebox = new Box(BoxLayout.X_AXIS);
		namebox.add(Box.createHorizontalStrut(5));
		namebox.add(nameform);
		namebox.add(Box.createHorizontalStrut(5));

		opcombo = new XComboBox[9];
		Box opbox = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<opcombo.length; i++) {
			opcombo[i] = new XComboBox(oplabel[i]);
			opcombo[i].setWidth(150,200);
			opcombo[i].setRenderer(new XfeditOpsetRenderer(this,opcode[i]));
			opcombo[i].addItemListener(this);
			opbox.add(opcombo[i]);
		}
		Box opsetbox = new Box(BoxLayout.X_AXIS);
		opsetbox.add(Box.createHorizontalStrut(5));
		opsetbox.add(opbox);
		opsetbox.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Operatorset Edition"));
		content.add(Box.createVerticalStrut(5));
		content.add(namebox);
		content.add(Box.createVerticalStrut(5));
		content.add(new XLabel("Selected operators"));
		content.add(Box.createVerticalStrut(5));
		content.add(opsetbox);
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setLocationRelativeTo(xfeditor);
		pack();
	}

	/**
	 * Muestra el nombre del conjunto en el campo de la ventana
	 */
	private void setName() {
		nameform.setText(copy.getName());
	}

	/**
	 * Asigna el contenido de los menús desplegables
	 */
	private void setOperators() {
		for(int i=0; i<opcombo.length; i++) opcombo[i].setList(available(i));
	}

	/**
	 * Obtiene las definiciones disponibles para cada operador
	 */
	private Vector available(int opindex) {
		Vector available = new Vector();
		available.addElement("default");

		Vector avlist = xfeditor.getXflPackageBank().getFunctionNames(fzcode[opindex]);
		available.addAll(avlist);
		return available;
	}


	/**
	 * Obtiene el indice del menú desplegable que genera el evento
	 */
	public int getComboBoxIndex(EventObject ev) {
		int index = -1;
		for(int i=0; i<opcombo.length; i++)
			if(ev.getSource() == opcombo[i].getField()) index = i;
		return index;
	}

	/**
	 * Acción del botón Apply
	 */
	private boolean apply() {
		String name = nameform.getText();
		if(!XConstants.isIdentifier(name)) {
			nameform.setText("");
			XDialog.showMessage(nameform,"Invalid Name");
			return false;
		}
		Operatorset search = spec.searchOperatorset(name);
		if(search != null && search != original) {
			nameform.setText("");
			XDialog.showMessage(nameform,"Invalid Name: Operatorset already exists");
			return false;
		}
		if(original != null) {
			original.setName(name);
			for(int k=1; k<10; k++) original.set(copy.get(k),k);
		}
		else {
			copy.setName(name);
			spec.addOperatorset(copy);
			original = copy;
			original.setEditing(true);
		}
		spec.setModified(true);
		copy = (Operatorset) original.clone();
		xfeditor.refresh();
		return true;
	}

	/**
	 * Acción del botón OK
	 */
	private void ok() {
		if(!apply()) return;
		original.setEditing(false);
		setVisible(false);
	}

	/**
	 * Acción del botón Reload
	 */
	private void reload() {
		if(original != null) copy = (Operatorset) original.clone();
		else copy = new Operatorset("");
		setName();
		setOperators();
	}

	/**
	 * Acción del botón Cancel
	 */
	private void cancel() {
		if(original != null) original.setEditing(false);
		xfeditor.refresh();
		setVisible(false);
	}
}
