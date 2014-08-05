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

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.EventObject;

/**
 * Herramienta de edición de un sistema difuso
 * 
 * @author Francisco José Moreno Velo
 */
public class Xfedit extends JFrame implements ActionListener, KeyListener,
MouseListener, ListSelectionListener, WindowListener {

	// ----------------------------------------------------------------------------//
	// COSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603019L;

	// ----------------------------------------------------------------------------//
	// COSTANTES PÚBLICAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Código de para identificar una variable de entrada
	 */
	public static final int INPUT = 0;

	/**
	 * Código de para identificar una variable de salida
	 */
	public static final int OUTPUT = 1;

	/**
	 * Código de para identificar una conjunto de operadores
	 */
	public static final int OPSET = 2;

	/**
	 * Código de para identificar un tipo lingüístico
	 */
	public static final int TYPE = 3;

	/**
	 * Código de para identificar una base de reglas
	 */

	public static final int RULEBASE = 4;

	/**
	 * Código de para identificar una módulo no difuso
	 */
	public static final int CRISP = 5;

	/**
	 * Código de para identificar la estructura jerárquica del sistema
	 */
	public static final int STRUCTURE = 6;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Ventana principal que realiza la llamada a la herramienta
	 */
	private Xfuzzy xfuzzy;

	/**
	 * Banco de paquetes de funciones disponibles
	 */
	private XflPackageBank pkgbank;

	/**
	 * Sistema difuso a editar
	 */
	private Specification spec;

	/**
	 * Lista de conjuntos de operadores
	 */
	private XList opsetlist;

	/**
	 * Lista de tipos lingüísticos
	 */
	private XList typelist;

	/**
	 * Lista de bases de reglas
	 */
	private XList baselist;

	/**
	 * Lista de módulos no difusos
	 */
	private XList crisplist;

	/**
	 * Lista de variables de entrada
	 */
	private XList inputlist;

	/**
	 * Lista de variables de salida
	 */
	private XList outputlist;

	/**
	 * Menú asociado a la lista de conjuntos de operadores
	 */
	private JPopupMenu opsetpopup;

	/**
	 * Menú asociado a la lista de tipos lingüísticos
	 */
	private JPopupMenu typepopup;

	/**
	 * Menú asociado a la lista de bases de reglas
	 */
	private JPopupMenu basepopup;

	/**
	 * Menú asociado a la lista de módulos no difusos
	 */
	private JPopupMenu crisppopup;

	/**
	 * Menú asociado a la lista de variables de entrada
	 */
	private JPopupMenu inputpopup;

	/**
	 * Menú asociado a la lista de variables de salida
	 */
	private JPopupMenu outputpopup;

	/**
	 * Campo para mostrar el nombre del sistema difuso
	 */
	private XTextForm nameform;

	/**
	 * Panel de edición de la estructura jerárquica del sistema
	 */
	private XfeditStructure graph;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfedit(Xfuzzy xfuzzy, Specification spec) {
		super("Xfedit");
		this.xfuzzy = xfuzzy;
		this.pkgbank = new XflPackageBank();
		this.spec = spec;
		this.spec.setEditor(this);
		build();
		refresh();
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS ESTÁTICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Ejecuta la herramienta Xfedit desde la ventana de Xfuzzy
	 */
	public static void showSystemEditor(Xfuzzy xfuzzy, Specification spec) {
		Xfedit editor = new Xfedit(xfuzzy, spec);
		editor.setVisible(true);
		editor.repaint();
	}

	/**
	 * Ejecución externa
	 */
	public static void main(String args[]) {
		Specification spec;
		if (args.length > 1) {
			System.out.println("Usage: xfedit xflfile");
			return;
		}
		File file = (args.length == 0 ? new File("noname.xfl") : new File(
				args[0]));

		XflParser parser = new XflParser();
		if (file.exists())
			spec = parser.parse(file.getAbsolutePath());
		else
			spec = new Specification(file);

		if (spec == null) {
			System.out.println(parser.resume());
			return;
		}
		Xfedit editor = new Xfedit(null, spec);
		editor.setVisible(true);
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Obtiene la referencia del sistema difuso editado
	 */
	public Specification getSpecification() {
		return this.spec;
	}

	/**
	 * Obtiene la lista de paquetes disponibles
	 */
	public XflPackageBank getXflPackageBank() {
		return pkgbank;
	}

	/**
	 * Actualiza el contenido de las listas de la ventana
	 */
	public void refresh() {
		nameform.setText(spec.getName());
		inputlist.setListData(spec.getSystemModule().getInputs());
		outputlist.setListData(spec.getSystemModule().getOutputs());
		opsetlist.setListData(spec.getOperatorsets());
		typelist.setListData(spec.getTypes());
		baselist.setListData(spec.getRulebases());
		crisplist.setListData(spec.getCrispBlockSet().getBlocks());
		graph.refresh();
	}

	/**
	 * Muestra un mensaje, ya sea en Xfuzzy o en un diálogo
	 */
	public void log(String msg) {
		if (xfuzzy != null)
			xfuzzy.log(msg);
		else
			XDialog.showMessage(this, msg);
	}

	/**
	 * Elimina la selección de una lista al activar otra
	 */
	public void listSelection(int list) {
		if (list == INPUT && inputlist.isSelectionEmpty())
			return;
		if (list == OUTPUT && outputlist.isSelectionEmpty())
			return;
		if (list == OPSET && opsetlist.isSelectionEmpty())
			return;
		if (list == TYPE && typelist.isSelectionEmpty())
			return;
		if (list == RULEBASE && baselist.isSelectionEmpty())
			return;
		if (list == CRISP && crisplist.isSelectionEmpty())
			return;
		if (list != OPSET)
			opsetlist.clearSelection();
		if (list != TYPE)
			typelist.clearSelection();
		if (list != RULEBASE)
			baselist.clearSelection();
		if (list != CRISP)
			crisplist.clearSelection();
		if (list != INPUT)
			inputlist.clearSelection();
		if (list != OUTPUT)
			outputlist.clearSelection();
		if (list != STRUCTURE)
			graph.clearSelection();
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Save"))
			save();
		else if (command.equals("SaveAs"))
			saveas();
		else if (command.equals("Edit"))
			editFile();
		else if (command.equals("Close"))
			close();
		else if (command.equals("NewIVar"))
			newModule(INPUT);
		else if (command.equals("EditIVar"))
			editModule(INPUT);
		else if (command.equals("DelIVar"))
			removeModule(INPUT);
		else if (command.equals("NewOVar"))
			newModule(OUTPUT);
		else if (command.equals("EditOVar"))
			editModule(OUTPUT);
		else if (command.equals("DelOVar"))
			removeModule(OUTPUT);
		else if (command.equals("NewOpset"))
			newModule(OPSET);
		else if (command.equals("EditOpset"))
			editModule(OPSET);
		else if (command.equals("DelOpset"))
			removeModule(OPSET);
		else if (command.equals("NewType"))
			newModule(TYPE);
		else if (command.equals("EditType"))
			editModule(TYPE);
		else if (command.equals("DelType"))
			removeModule(TYPE);
		else if (command.equals("NewBase"))
			newModule(RULEBASE);
		else if (command.equals("EditBase"))
			editModule(RULEBASE);
		else if (command.equals("DelBase"))
			removeModule(RULEBASE);
		else if (command.equals("NewCrisp"))
			newModule(CRISP);
		else if (command.equals("EditCrisp"))
			editModule(CRISP);
		else if (command.equals("DelCrisp"))
			removeModule(CRISP);
		else if (command.equals("NewCall"))
			graph.insertCall();
		else if (command.equals("DelCall"))
			graph.removeCall();
	}

	/**
	 * Interfaz KeyListener. Acción al soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int module = getEventModule(e);
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			e.consume();
			removeModule(module);
		}
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			e.consume();
			removeModule(module);
		}
		if (e.getKeyCode() == KeyEvent.VK_INSERT) {
			e.consume();
			newModule(module);
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			e.consume();
			editModule(module);
		}
		if (e.getKeyCode() == 226) {
			e.consume();
			editModule(module);
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

	/**
	 * Interfaz MouseListener. Acción al pulsar el ratón
	 */
	public void mouseClicked(MouseEvent e) {
		int module = getEventModule(e);
		if (e.getClickCount() == 2)
			editModule(module);
	}

	/**
	 * Interfaz MouseListener. Acción al apretar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		int module = getEventModule(e);
		showPopup(module, e);
	}

	/**
	 * Interfaz MouseListener. Acción al soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		int module = getEventModule(e);
		showPopup(module, e);
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
	 * Interfaz ListSelectionListener
	 */
	public void valueChanged(ListSelectionEvent e) {
		int module = getEventModule(e);
		listSelection(module);
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
		close();
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

	// ----------------------------------------------------------------------------//
	// MÉTODOS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Obtiene el código de la lista que genera el evento
	 */
	private int getEventModule(EventObject e) {
		int kind = -1;
		if (e.getSource() == inputlist.getList())
			kind = INPUT;
		if (e.getSource() == outputlist.getList())
			kind = OUTPUT;
		if (e.getSource() == opsetlist.getList())
			kind = OPSET;
		if (e.getSource() == typelist.getList())
			kind = TYPE;
		if (e.getSource() == baselist.getList())
			kind = RULEBASE;
		if (e.getSource() == crisplist.getList())
			kind = CRISP;
		return kind;
	}

	// ----------------------------------------------------------------------------//
	// Metodos que construyen la ventana //
	// ----------------------------------------------------------------------------//

	/**
	 * Genera el contenido de la ventana principal de Xfedit
	 */
	private void build() {
		nameform = new XTextForm("Name");
		nameform.setLabelWidth(100);
		nameform.setFieldWidth(400);
		nameform.setAlignment(JLabel.CENTER);
		nameform.setEditable(false);

		inputlist = new XList("Input Variables");
		inputlist.setPreferredWidth(130);
		inputlist.addListSelectionListener(this);
		inputlist.addMouseListener(this);
		inputlist.addKeyListener(this);

		outputlist = new XList("Output Variables");
		outputlist.setPreferredWidth(130);
		outputlist.addListSelectionListener(this);
		outputlist.addMouseListener(this);
		outputlist.addKeyListener(this);

		opsetlist = new XList("Operatorsets");
		opsetlist.setPreferredWidth(130);
		opsetlist.setPreferredHeight(100);
		opsetlist.addListSelectionListener(this);
		opsetlist.addMouseListener(this);
		opsetlist.addKeyListener(this);

		typelist = new XList("Types");
		typelist.setPreferredWidth(130);
		typelist.setPreferredHeight(100);
		typelist.addListSelectionListener(this);
		typelist.addMouseListener(this);
		typelist.addKeyListener(this);

		baselist = new XList("Rulebases");
		baselist.setPreferredWidth(130);
		baselist.setPreferredHeight(100);
		baselist.addListSelectionListener(this);
		baselist.addMouseListener(this);
		baselist.addKeyListener(this);

		crisplist = new XList("Crisp Blocks");
		crisplist.setPreferredWidth(130);
		crisplist.setPreferredHeight(100);
		crisplist.addListSelectionListener(this);
		crisplist.addMouseListener(this);
		crisplist.addKeyListener(this);

		Box namebox = new Box(BoxLayout.X_AXIS);
		namebox.add(Box.createHorizontalStrut(5));
		namebox.add(nameform);
		namebox.add(Box.createHorizontalStrut(5));

		Box leftbox = new Box(BoxLayout.Y_AXIS);
		leftbox.add(inputlist);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(outputlist);

		Box rightbox1 = new Box(BoxLayout.Y_AXIS);
		rightbox1.add(opsetlist);
		rightbox1.add(Box.createVerticalStrut(5));
		rightbox1.add(crisplist);

		Box rightbox2 = new Box(BoxLayout.Y_AXIS);
		rightbox2.add(typelist);
		rightbox2.add(Box.createVerticalStrut(5));
		rightbox2.add(baselist);

		graph = new XfeditStructure(this, spec.getSystemModule());
		Box centerbox = new Box(BoxLayout.Y_AXIS);
		centerbox.add(new XLabel("System Structure"));
		centerbox.add(new JScrollPane(graph));

		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(leftbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(centerbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(rightbox1);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(rightbox2);
		hbox.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(menubar());
		content.add(Box.createVerticalStrut(5));
		content.add(namebox);
		content.add(Box.createVerticalStrut(5));
		content.add(hbox);
		content.add(Box.createVerticalStrut(5));

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		pack();
		setLocation();
		createPopups();
	}

	/**
	 * Genera la barra de menú de la ventana principal
	 */
	private JMenuBar menubar() {
		String label[] = { "Save", "Save As", null, "Edit XFL3 File", null,
		"Close edition" };
		String command[] = { "Save", "SaveAs", "Edit", "Close" };

		String label2[] = { "New input variable", "Edit input variable",
				"Remove input variable", null, "New output variable",
				"Edit output variable", "Remove output variable", null,
				"New operator set", "Edit operator set", "Remove operator set",
				null, "New type", "Edit type", "Remove type", null,
				"New rulebase", "Edit rulebase", "Remove rulebase", null,
				"New crisp block", "Edit crisp block", "Remove crisp block",
				null, "New module call", "Remove module call" };

		String command2[] = { "NewIVar", "EditIVar", "DelIVar", "NewOVar",
				"EditOVar", "DelOVar", "NewOpset", "EditOpset", "DelOpset",
				"NewType", "EditType", "DelType", "NewBase", "EditBase",
				"DelBase", "NewCrisp", "EditCrisp", "DelCrisp", "NewCall",
		"DelCall" };

		XMenu menu = new XMenu("File", label, command, this);
		XMenu menu2 = new XMenu("Edit", label2, command2, this);

		JMenuBar menubar = new JMenuBar();
		menubar.add(Box.createHorizontalStrut(5));
		menubar.add(menu);
		menubar.add(Box.createHorizontalStrut(5));
		menubar.add(menu2);
		menubar.add(Box.createGlue());
		return menubar;
	}

	/**
	 * Sitúa la ventana de Xfedit sobre la pantalla
	 */
	private void setLocation() {
		if (xfuzzy != null) {
			Point loc = xfuzzy.frame.getLocationOnScreen();
			loc.x += 40;
			loc.y += 200;
			setLocation(loc);
		} else {
			Dimension frame = getSize();
			Dimension screen = getToolkit().getScreenSize();
			setLocation((screen.width - frame.width) / 2,
					(screen.height - frame.height) / 2);
		}
	}

	// ----------------------------------------------------------------------------//
	// Métodos que manejan los menús desplegables //
	// ----------------------------------------------------------------------------//

	/**
	 * Genera los menús desplegables de las listas
	 */
	private void createPopups() {
		String inputlabel[] = { "New input variable", "Edit input variable",
		"Remove input variable" };
		String outputlabel[] = { "New output variable", "Edit output variable",
		"Remove output variable" };
		String opsetlabel[] = { "New operator set", "Edit operator set",
		"Remove operator set" };
		String typelabel[] = { "New type", "Edit type", "Remove type" };
		String baselabel[] = { "New rulebase", "Edit rulebase",
		"Remove rulebase" };
		String crisplabel[] = { "New crisp block", "Edit crisp block",
		"Remove crisp block" };

		String inputcommand[] = { "NewIVar", "EditIVar", "DelIVar" };
		String outputcommand[] = { "NewOVar", "EditOVar", "DelOVar" };
		String opsetcommand[] = { "NewOpset", "EditOpset", "DelOpset" };
		String typecommand[] = { "NewType", "EditType", "DelType" };
		String basecommand[] = { "NewBase", "EditBase", "DelBase" };
		String crispcommand[] = { "NewCrisp", "EditCrisp", "DelCrisp" };

		inputpopup = createPopupMenu(inputlabel, inputcommand);
		outputpopup = createPopupMenu(outputlabel, outputcommand);
		opsetpopup = createPopupMenu(opsetlabel, opsetcommand);
		typepopup = createPopupMenu(typelabel, typecommand);
		basepopup = createPopupMenu(baselabel, basecommand);
		crisppopup = createPopupMenu(crisplabel, crispcommand);
	}

	/**
	 * Genera un menú desplegable
	 */
	private JPopupMenu createPopupMenu(String label[], String command[]) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem item[] = new JMenuItem[command.length];
		for (int i = 0; i < command.length; i++) {
			item[i] = new JMenuItem(label[i]);
			item[i].setActionCommand(command[i]);
			item[i].addActionListener(this);
			item[i].setFont(XConstants.font);
			popup.add(item[i]);
		}
		return popup;
	}

	/**
	 * Muestra un menú desplegable de un cierto tipo
	 */
	private void showPopup(int module, MouseEvent e) {
		JPopupMenu popup = null;
		switch (module) {
		case INPUT:
			popup = inputpopup;
			break;
		case OUTPUT:
			popup = outputpopup;
			break;
		case OPSET:
			popup = opsetpopup;
			break;
		case TYPE:
			popup = typepopup;
			break;
		case RULEBASE:
			popup = basepopup;
			break;
		case CRISP:
			popup = crisppopup;
			break;
		}
		if (popup != null)
			popup.show((Component) e.getSource(), e.getX(), e.getY());
	}

	// ----------------------------------------------------------------------------//
	// Métodos que crean un nuevo módulo //
	// ----------------------------------------------------------------------------//

	/**
	 * Crea una nueva variable de entrada global
	 */
	private void newInputVariable() {
		XfeditVariableDialog editor = new XfeditVariableDialog(Variable.INPUT,
				this);
		editor.setVisible(true);
		editor.repaint();
		refresh();
	}

	/**
	 * Crea una nueva variable de salida global
	 */
	private void newOutputVariable() {
		XfeditVariableDialog editor = new XfeditVariableDialog(Variable.OUTPUT,
				this);
		editor.setVisible(true);
		editor.repaint();
		refresh();
	}

	/**
	 * Crea un nuevo conjunto de operadores
	 */
	private void newOperatorset() {
		XfeditOpsetEditor editor = new XfeditOpsetEditor(this, null);
		editor.setVisible(true);
		editor.repaint();
	}

	/**
	 * Crea un nuevo tipo de variable lingüística
	 */
	private void newType() {
		XfeditTypeCreator.showTypeCreator(this);
	}

	/**
	 * Crea una nueva base de reglas
	 */
	private void newRulebase() {
		XfeditRulebaseEditor.showRulebaseEditor(this, null);
	}

	/**
	 * Crea un nuevo bloque no difuso
	 */
	private void newCrisp() {
		XfeditCrispEditor.showCrispEditor(this, null);
	}

	/**
	 * Crea un nuevo módulo de un cierto tipo
	 */
	private void newModule(int module) {
		switch (module) {
		case INPUT:
			newInputVariable();
			return;
		case OUTPUT:
			newOutputVariable();
			return;
		case OPSET:
			newOperatorset();
			return;
		case TYPE:
			newType();
			return;
		case RULEBASE:
			newRulebase();
			return;
		case CRISP:
			newCrisp();
			return;
		}
	}

	// ----------------------------------------------------------------------------//
	// Métodos que editan un módulo //
	// ----------------------------------------------------------------------------//

	/**
	 * Edita una variable de entrada global
	 */
	private void editInputVariable() {
		Variable var = (Variable) inputlist.getSelectedValue();
		if (var == null)
			return;
		XfeditVariableDialog editor = new XfeditVariableDialog(var, this);
		editor.setVisible(true);
		editor.repaint();
		refresh();
	}

	/**
	 * Edita una variable de salida global
	 */
	private void editOutputVariable() {
		Variable var = (Variable) outputlist.getSelectedValue();
		if (var == null)
			return;
		XfeditVariableDialog editor = new XfeditVariableDialog(var, this);
		editor.setVisible(true);
		editor.repaint();
		refresh();
	}

	/**
	 * Edita un conjunto de operadores
	 */
	private void editOperatorset() {
		Operatorset op = (Operatorset) opsetlist.getSelectedValue();
		if (op == null || op.isEditing()) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		XfeditOpsetEditor.showOpsetEditor(this, op);
	}

	/**
	 * Edita un tipo de variable lingüística
	 */
	private void editType() {
		Type selection = (Type) typelist.getSelectedValue();
		if (selection == null || selection.isEditing()) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		XfeditTypeEditor.showTypeEditor(this, selection);
	}

	/**
	 * Edita una base de reglas
	 */
	private void editRulebase() {
		Rulebase selection = (Rulebase) baselist.getSelectedValue();
		if (selection == null || selection.isEditing()) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		XfeditRulebaseEditor.showRulebaseEditor(this, selection);
	}

	/**
	 * Edita un bloque no difuso
	 */
	private void editCrisp() {
		CrispBlock selection = (CrispBlock) crisplist.getSelectedValue();
		if (selection == null || selection.isEditing()) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		XfeditCrispEditor.showCrispEditor(this, selection);
	}

	/**
	 * Edita un módulo de un cierto tipo
	 */
	private void editModule(int module) {
		switch (module) {
		case INPUT:
			editInputVariable();
			return;
		case OUTPUT:
			editOutputVariable();
			return;
		case OPSET:
			editOperatorset();
			return;
		case TYPE:
			editType();
			return;
		case RULEBASE:
			editRulebase();
			return;
		case CRISP:
			editCrisp();
			return;
		}
	}

	// ----------------------------------------------------------------------------//
	// Métodos que eliminan un módulo //
	// ----------------------------------------------------------------------------//

	/**
	 * Elimina una variable de entrada global
	 */
	private void removeInputVariable() {
		Variable var = (Variable) inputlist.getSelectedValue();
		if (var == null)
			return;
		if (var.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove variable \"" + var.getName() + "\".";
			msg[1] = "There are rules using this variable.";
			XDialog.showMessage(this, msg);
			return;
		}
		String confirm = "Please, confirm you want to remove";
		confirm += " variable \"" + var.getName() + "\".";
		if (!XDialog.showQuestion(this, confirm))
			return;
		spec.getSystemModule().removeVariable(var);
		refresh();
	}

	/**
	 * Elimina una variable de salida global
	 */
	private void removeOutputVariable() {
		Variable var = (Variable) outputlist.getSelectedValue();
		if (var == null)
			return;
		if (var.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove variable \"" + var.getName() + "\".";
			msg[1] = "System is using this variable.";
			XDialog.showMessage(this, msg);
			return;
		}
		String confirm = "Please, confirm you want to remove";
		confirm += " variable \"" + var.getName() + "\".";
		if (!XDialog.showQuestion(this, confirm))
			return;
		spec.getSystemModule().removeVariable(var);
		refresh();
	}

	/**
	 * Elimina un conjunto de operadores
	 */
	private void removeOperatorset() {
		Operatorset selection = (Operatorset) opsetlist.getSelectedValue();
		if (selection == null || selection.isEditing())
			return;
		if (selection.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove operator set \"" + selection.getName()
			+ "\".";
			msg[1] = "There are rulebases using this operator set.";
			XDialog.showMessage(this, msg);
			return;
		}
		String confirm = "Please, confirm you want to remove";
		confirm += " operator set \"" + selection.getName() + "\".";
		if (!XDialog.showQuestion(this, confirm))
			return;
		spec.removeOperatorset(selection);
		refresh();
	}

	/**
	 * Elimina un tipo de variable lingüística
	 */
	private void removeType() {
		Type selection = (Type) typelist.getSelectedValue();
		if (selection == null || selection.isEditing())
			return;
		if (selection.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove type \"" + selection.getName() + "\".";
			msg[1] = "System is using this type.";
			XDialog.showMessage(this, msg);
			return;
		}
		String confirm = "Please, confirm you want to remove";
		confirm += " type \"" + selection.getName() + "\".";
		if (!XDialog.showQuestion(this, confirm))
			return;
		spec.removeType(selection);
		refresh();
	}

	/**
	 * Elimina una base de reglas
	 */
	private void removeRulebase() {
		Rulebase selection = (Rulebase) baselist.getSelectedValue();
		if (selection == null || selection.isEditing())
			return;
		if (selection.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove rulebase \"" + selection.getName() + "\".";
			msg[1] = "System is using this rulebase.";
			XDialog.showMessage(this, msg);
			return;
		}
		String confirm = "Please, confirm you want to remove";
		confirm += " rulebase \"" + selection.getName() + "\".";
		if (!XDialog.showQuestion(this, confirm))
			return;
		spec.removeRulebase(selection);
		refresh();
	}

	/**
	 * Elimina un bloque no difuso
	 */
	private void removeCrisp() {
		CrispBlock selection = (CrispBlock) crisplist.getSelectedValue();
		if (selection == null || selection.isEditing())
			return;
		if (selection.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove crisp block \"" + selection.getName()
			+ "\".";
			msg[1] = "System is using this block.";
			XDialog.showMessage(this, msg);
			return;
		}
		String confirm = "Please, confirm you want to remove";
		confirm += " crisp block \"" + selection.getName() + "\".";
		if (!XDialog.showQuestion(this, confirm))
			return;
		spec.getCrispBlockSet().remove(selection);
		refresh();
	}

	/**
	 * Elimina un módulo de un cierto tipo
	 */
	private void removeModule(int module) {
		switch (module) {
		case INPUT:
			removeInputVariable();
			return;
		case OUTPUT:
			removeOutputVariable();
			return;
		case OPSET:
			removeOperatorset();
			return;
		case TYPE:
			removeType();
			return;
		case RULEBASE:
			removeRulebase();
			return;
		case CRISP:
			removeCrisp();
			return;
		}
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú File //
	// ----------------------------------------------------------------------------//

	/**
	 * Almacena la descripcion del sistema difuso
	 */
	private void save() {
		if (!spec.save())
			log("Can't save specification " + spec + ".");
		else {
			String msg = "Specification " + spec + " saved as ";
			msg += spec.getFile().getAbsolutePath() + ".";
			log(msg);
			spec.setModified(false);
		}
	}

	/**
	 * Almacena el sistema difuso en un fichero seleccionado
	 */
	private void saveas() {
		JFileChooser chooser = new JFileChooser();
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy system files (.xfl)", "xfl");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(spec.getFile());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save System As ...");
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();
		
		if (file.exists()) {
			String question[] = new String[2];
			question[0] = "File " + file.getName() + " already exists.";
			question[1] = "Do you want to overwrite this file?";
			if (!XDialog.showQuestion(this, question))
				return;
		}

		String oldname = spec.getName();
		if (spec.save_as(file)) {
			if (!oldname.equals("" + spec))
				log("System " + oldname + " renamed to " + spec + ".");
			String msg = "Specification " + spec + " saved as ";
			msg += spec.getFile().getAbsolutePath() + ".";
			log(msg);
			spec.setModified(false);
		} else
			log("Can't save specification " + spec + ".");
		refresh();
		if (xfuzzy != null)
			xfuzzy.refreshList();
	}

	/**
	 * Edita el contenido del fichero XFL3 del sistema difuso
	 */
	private void editFile() {
		XfeditFileEditor editor = new XfeditFileEditor(this);
		editor.setVisible(true);
		editor.repaint();
	}

	/**
	 * Sale de la herramienta
	 */
	private void close() {
		boolean editing = false;
		Operatorset[] opset = spec.getOperatorsets();
		Type[] type = spec.getTypes();
		Rulebase[] base = spec.getRulebases();
		if (spec.isFileEditing())
			editing = true;
		for (int i = 0; i < opset.length; i++)
			if (opset[i].isEditing())
				editing = true;
		for (int i = 0; i < type.length; i++)
			if (type[i].isEditing())
				editing = true;
		for (int i = 0; i < base.length; i++)
			if (base[i].isEditing())
				editing = true;
		if (editing) {
			String msg[] = new String[2];
			msg[0] = "Cannot close system edition";
			msg[1] = "Close editing windows first";
			XDialog.showMessage(null, msg);
			return;
		}
		spec.setEditor(null);
		setVisible(false);
		if (xfuzzy == null)
			System.exit(0);
	}
}
