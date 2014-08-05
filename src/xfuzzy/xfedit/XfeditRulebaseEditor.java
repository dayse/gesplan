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
import java.util.Vector;

/**
 * Ventana de edición de una base de reglas
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseEditor extends JDialog implements ActionListener,
ChangeListener, ListSelectionListener, ItemListener, KeyListener, MouseListener,
WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603028L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia al editor que realiza la llamada a XfeditRulebaseEditor
	 */
	private Xfedit xfeditor;
	
	/**
	 * Sistema difuso en edición
	 */
	private Specification spec;
	
	/**
	 * Objeto original que describe la base de reglas a editar
	 */
	private Rulebase original;
	
	/**
	 * Copia de trabajo sobre la que se realizan los cambios
	 * de edición de la base de reglas
	 */
	private Rulebase copy;
	
	/**
	 * Campo para introducir el nombre de la base de reglas
	 */
	private XTextForm nameform;
	
	/**
	 * Campo para seleccionar el conjunto de operadores a utilizar
	 * en la base de reglas
	 */
	private XComboBox opsetbox;
	
	/**
	 * Lista de variables de entrada de la base de reglas
	 */
	private XList inputlist;
	
	/**
	 * Lista de variables de salida de la base de reglas
	 */
	private XList outputlist;
	
	/**
	 * Menú desplegable asociado a la lista de variables de entrada
	 */
	private JPopupMenu inputpopup;
	
	/**
	 * Menú desplegable asociado a la lista de variables de entrada
	 */
	private JPopupMenu outputpopup;
	
	/**
	 * Panel con pestañas para las diferentes formas de edición de
	 * la base de reglas
	 */
	private JTabbedPane tabbed;
	
	/**
	 * Panel de edición seleccionado
	 */
	private JPanel selectedtab;
	
	/**
	 * Panel de edición en formato libre
	 */
	private XfeditRulebaseFreePanel freepanel;
	
	/**
	 * Panel de edición en formato tabular
	 */
	private XfeditRulebaseTablePanel tablepanel;
	
	/**
	 * Panel de edición en formato matricial
	 */
	private XfeditRulebaseMatrixPanel matrixpanel;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseEditor(Xfedit xfeditor, Rulebase rulebase) {
		super(xfeditor,"Xfedit",false);
		this.xfeditor = xfeditor;
		this.spec = xfeditor.getSpecification();
		this.original = rulebase;
		if(this.original != null) {
			this.original.setEditing(true);
			this.copy = (Rulebase) this.original.clone();
		}
		else this.copy = new Rulebase("");
		build();
		refreshAll();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera una ventana de edicion de una base de reglas
	 */
	public static void showRulebaseEditor(Xfedit xfeditor, Rulebase rb){
		XfeditRulebaseEditor editor = new XfeditRulebaseEditor(xfeditor,rb);
		editor.setVisible(true);
		editor.repaint();
	}

	/**
	 * Obtiene una referencia a la base de reglas editada
	 */
	public Rulebase getWorkingCopy() {
		return this.copy;
	}

	/**
	 * Obtiene la referencia al sistema difuso editado
	 */
	public Specification getSpecification() {
		return this.spec;
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
		else if(command.equals("NewInput")) newVariable(Variable.INPUT);
		else if(command.equals("EditInput")) editVariable(Variable.INPUT);
		else if(command.equals("RemoveInput")) removeVariable(Variable.INPUT);
		else if(command.equals("NewOutput")) newVariable(Variable.OUTPUT);
		else if(command.equals("EditOutput")) editVariable(Variable.OUTPUT);
		else if(command.equals("RemoveOutput")) removeVariable(Variable.OUTPUT);
	}

	/**
	 * Interfaz ChangeListener
	 */
	public void stateChanged(ChangeEvent e) {
		JPanel selected = (JPanel) tabbed.getSelectedComponent();
		if(selected == selectedtab) return;
		if(!getRules()) { tabbed.setSelectedComponent(selectedtab); return; }
		if(selected == freepanel) freepanel.refresh(copy);
		if(selected == tablepanel) tablepanel.refresh(copy);
		if(selected == matrixpanel) matrixpanel.refresh(copy);
		selectedtab = selected;
	}

	/**
	 * Interfaz ListSelectionListener
	 */
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == inputlist.getList() ) { 
			if(inputlist.isSelectionEmpty()) return;
			outputlist.clearSelection();
		} else {
			if(outputlist.isSelectionEmpty()) return;
			inputlist.clearSelection();
		}
		if(selectedtab == tablepanel) tablepanel.clearSelection();
		if(selectedtab == freepanel) freepanel.clearSelection();
	}

	/**
	 * Interfaz ItemListener
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() != ItemEvent.SELECTED) return;
		try { 
			Operatorset selection = (Operatorset) opsetbox.getSelectedItem();
			if(selection == null) return;
			copy.setOperatorset(selection);
		}
		catch(Exception ex) { copy.setOperatorset(new Operatorset()); }
	}

	/**
	 * Interfaz KeyListener. Acción al soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int kind = -1;
		if(e.getSource() == inputlist.getList() ) kind = Variable.INPUT;
		if(e.getSource() == outputlist.getList() ) kind = Variable.OUTPUT;

		if(e.getKeyCode() == KeyEvent.VK_DELETE)
		{ e.consume(); removeVariable(kind); }
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{ e.consume(); removeVariable(kind); }
		if(e.getKeyCode() == KeyEvent.VK_INSERT)
		{ e.consume(); newVariable(kind); }
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{ e.consume(); editVariable(kind); }
		if(e.getKeyCode() == 226)
		{ e.consume(); editVariable(kind); }
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

	/**
	 * Interfaz MouseListener. Acción al pulsar el ratón
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() != 2) return;
		int kind = -1;
		if(e.getSource() == inputlist.getList() ) kind = Variable.INPUT;
		if(e.getSource() == outputlist.getList() ) kind = Variable.OUTPUT;
		editVariable(kind);
	}

	/**
	 * Interfaz MouseListener. Acción al apretar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
		if(!e.isPopupTrigger()) return;
		if(e.getSource() == inputlist.getList()) {
			inputpopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
		if(e.getSource() == outputlist.getList() ) {
			outputpopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	/**
	 * Interfaz MouseListener. Acción al soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
		if(!e.isPopupTrigger()) return;
		if(e.getSource() == inputlist.getList()) {
			inputpopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
		if(e.getSource() == outputlist.getList() ) {
			outputpopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
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
	 * Interfaz WindowListener. Acción al finalizar el cierre
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
		XCommandForm form = new XCommandForm(lb,this);
		form.setCommandWidth(120);
		form.block();

		nameform = new XTextForm("Name");
		nameform.setWidth(130,130);
		nameform.setAlignment(JLabel.CENTER);
		opsetbox = new XComboBox("Operatorset");
		opsetbox.setWidth(130,130);
		opsetbox.addItemListener(this);
		inputlist = new XList("Input variables");
		inputlist.addListSelectionListener(this);
		inputlist.addKeyListener(this);
		inputlist.addMouseListener(this);
		outputlist = new XList("Output variables");
		outputlist.addListSelectionListener(this);
		outputlist.addKeyListener(this);
		outputlist.addMouseListener(this);

		JPanel leftbox = new JPanel();
		leftbox.setLayout(new BoxLayout(leftbox,BoxLayout.Y_AXIS));
		leftbox.add(nameform);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(opsetbox);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(inputlist);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(outputlist);
		Dimension pref = leftbox.getPreferredSize();
		Dimension maxd = leftbox.getMaximumSize();
		leftbox.setMaximumSize(new Dimension(pref.width, maxd.height));

		Box rightbox = new Box(BoxLayout.Y_AXIS);
		tabbed = new JTabbedPane();
		freepanel = new XfeditRulebaseFreePanel();
		tablepanel = new XfeditRulebaseTablePanel();
		matrixpanel = new XfeditRulebaseMatrixPanel();
		tabbed.addTab("Free form",freepanel);
		tabbed.addTab("Table form",tablepanel);
		tabbed.addTab("Matrix form",matrixpanel);
		tabbed.addChangeListener(this);
		selectedtab = freepanel;
		rightbox.add(tabbed);

		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(leftbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(rightbox);
		hbox.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Rulebase Edition"));
		content.add(Box.createVerticalStrut(5));
		content.add(hbox);
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setSize(new Dimension(910,500));
		setLocationRelativeTo(xfeditor);
		createPopups();
	}

	/**
	 * Crea los menús desplegables
	 */
	private void createPopups() {
		String ilabel[]= { "New Input Variable","Edit Input Variable", 
		"Remove Input Variable" };
		String icommand[] = { "NewInput", "EditInput", "RemoveInput" };
		inputpopup = new JPopupMenu();
		JMenuItem inputitem[] = new JMenuItem[icommand.length];
		for(int i=0; i<icommand.length; i++) {
			inputitem[i] = new JMenuItem(ilabel[i]);
			inputitem[i].setActionCommand(icommand[i]);
			inputitem[i].addActionListener(this);
			inputitem[i].setFont(XConstants.font);
			inputpopup.add(inputitem[i]);
		}

		String olabel[]= { "New Output Variable","Edit Output Variable", 
		"Remove Output Variable" };
		String ocommand[] = { "NewOutput", "EditOutput", "RemoveOutput" };
		outputpopup = new JPopupMenu();
		JMenuItem outputitem[] = new JMenuItem[ocommand.length];
		for(int i=0; i<ocommand.length; i++) {
			outputitem[i] = new JMenuItem(olabel[i]);
			outputitem[i].setActionCommand(ocommand[i]);
			outputitem[i].addActionListener(this);
			outputitem[i].setFont(XConstants.font);
			outputpopup.add(outputitem[i]);
		}
	}

	/**
	 * Lee el contenido del panel de edición de reglas
	 */
	private boolean getRules() {
		boolean rulesok = false;
		if(selectedtab == freepanel) rulesok = freepanel.getRules(copy);
		if(selectedtab == tablepanel) rulesok = tablepanel.getRules(copy);
		if(selectedtab == matrixpanel) rulesok = matrixpanel.getRules(copy);
		if(!rulesok) Toolkit.getDefaultToolkit().beep();
		return rulesok;
	}

	/**
	 * Actualiza el contenido de las diferentes áreas del editor
	 */
	private void refresh() {
		inputlist.setListData(copy.getInputs());
		outputlist.setListData(copy.getOutputs());
		if(selectedtab == freepanel) freepanel.refresh(copy);
		if(selectedtab == tablepanel) tablepanel.refresh(copy);
		if(selectedtab == matrixpanel) matrixpanel.refresh(copy);
		repaint();
	}

	/**
	 * Actualiza el contenido, incluyendo la lista de operadores
	 */
	private void refreshAll() {
		nameform.setText(copy.getName());
		Vector opsetlist = new Vector();
		opsetlist.add("default");
		Operatorset[] opsetdef = spec.getOperatorsets();
		for(int i=0; i<opsetdef.length; i++) opsetlist.add(opsetdef[i]);
		opsetbox.setList(opsetlist);
		Operatorset used = copy.getOperatorset();
		if(used == null || used.isDefault()) opsetbox.setSelectedIndex(0);
		else opsetbox.setSelectedItem(used);
		refresh();
	}

	/**
	 * Lee y verifica el nombre de la base de reglas
	 */
	private boolean getRulebaseName() {
		String name = nameform.getText();
		if(!XConstants.isIdentifier(name)) {
			nameform.setText("");
			XDialog.showMessage(nameform,"Invalid Name");
			return false;
		}
		Rulebase search = spec.searchRulebase(name);
		if(search != null && search != original) {
			nameform.setText("");
			XDialog.showMessage(nameform,"Invalid Name: Rulebase already exists");
			return false;
		}
		copy.setName(name);
		return true;
	}

	//----------------------------------------------------------------------------//
	// Métodos de edición de las variables                                        //
	//----------------------------------------------------------------------------//

	/**
	 * Crea una nueva variable
	 */
	private void newVariable(int kind) {
		if(!getRules()) return;
		XfeditVariableDialog editor = new XfeditVariableDialog(kind,this);
		editor.setVisible(true);
		editor.repaint();
		refresh();
	}

	/**
	 * Edita una variable existente
	 */
	private void editVariable(int kind) {
		if(!getRules()) return;
		Variable var;
		if(kind == Variable.INPUT) var = (Variable) inputlist.getSelectedValue();
		else var = (Variable) outputlist.getSelectedValue();
		if(var == null) return;
		XfeditVariableDialog editor = new XfeditVariableDialog(var,this);
		editor.setVisible(true);
		editor.repaint();
		refresh();
	}

	/**
	 * Elimina una variable existente
	 */
	private void removeVariable(int kind) {
		if(!getRules()) return;
		Variable var;
		if(kind == Variable.INPUT) var = (Variable) inputlist.getSelectedValue();
		else var = (Variable) outputlist.getSelectedValue();
		if(var == null) return;
		if(var.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove variable \""+var.getName()+"\".";
			msg[1] = "There are rules using this variable.";
			XDialog.showMessage(this,msg);
			return;
		}
		if(kind == Variable.INPUT) copy.removeInputVar(var);
		else copy.removeOutputVar(var);
		refresh();
	}

	//----------------------------------------------------------------------------//
	// Métodos que desarrollan las acciones                                       //
	//----------------------------------------------------------------------------//

	/**
	 * Aplica las modificaciónes introducidas en la edición
	 */
	private boolean tryApply() {
		if(!getRulebaseName()) return false;
		if(!getRules()) return false;

		if(original == null) spec.addRulebase(copy);
		else spec.exchangeRulebase(original,copy); 
		original = copy;
		copy = (Rulebase) original.clone();
		spec.setModified(true);
		return true;
	}

	/**
	 * Aplica los cambios y cierra la ventana
	 */
	private void ok() {
		if( !tryApply() ) return;
		if(original != null) original.setEditing(false);
		copy.dispose();
		xfeditor.refresh();
		setVisible(false);
	}

	/**
	 * Aplica los cambios y mantien abierta la ventana
	 */
	private void apply() {
		if(tryApply()) { refresh(); xfeditor.refresh(); }
	}

	/**
	 * Desecha los cambios no almacenados
	 */
	private void reload() {
		copy.dispose();
		if(original != null) copy = (Rulebase) original.clone();
		else copy = new Rulebase("");
		refreshAll();
	}

	/**
	 * Cierra la ventana sin almacenar los cambios
	 */
	private void cancel() {
		copy.dispose();
		if(original != null) original.setEditing(false);
		xfeditor.refresh();
		setVisible(false);
	}
}

