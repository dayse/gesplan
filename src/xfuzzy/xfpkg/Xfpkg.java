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
//EDITOR DE PAQUETES DE OPERACIONES		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfpkg;

import xfuzzy.*;
import xfuzzy.pkg.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.EventObject;

/**
 * Clase que desarrolla la aplicación de edición gráfica de paquetes de
 * funciones
 * 
 * @author Francisco José Moreno Velo
 */
public class Xfpkg extends JFrame implements ActionListener, KeyListener,
MouseListener, ListSelectionListener, WindowListener {

	// ----------------------------------------------------------------------------//
	// CONSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603049L;

	private static final int BINARY = PackageDefinition.BINARY;

	private static final int UNARY = PackageDefinition.UNARY;

	private static final int MEMBERSHIP = PackageDefinition.MFUNC;

	private static final int DEFUZ = PackageDefinition.DEFUZ;

	private static final int FAMILY = PackageDefinition.FAMILY;

	private static final int CRISP = PackageDefinition.CRISP;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Campo para introducir el nombre del paquete
	 */
	private XTextForm pkgname;

	/**
	 * Menú principal de la aplicación
	 */
	private XMenu menu;

	/**
	 * Lista de funciones binarias
	 */
	private XList binlist;

	/**
	 * Lista de funciones unarias
	 */
	private XList unarylist;

	/**
	 * Lista de funciones de pertenencia
	 */
	private XList mflist;

	/**
	 * Lista de métodos de concreción
	 */
	private XList defuzlist;

	/**
	 * Lista de familias de funciones de pertenencia
	 */
	private XList familylist;

	/**
	 * Lista de funciones no difusas
	 */
	private XList crisplist;

	/**
	 * Panel desplegable asociado a las funciones binarias
	 */
	private JPopupMenu binarypopup;

	/**
	 * Panel desplegable asociado a las funciones unarias
	 */
	private JPopupMenu unarypopup;

	/**
	 * Panel desplegable asociado a las funciones de pertenencia
	 */
	private JPopupMenu mfpopup;

	/**
	 * Panel desplegable asociado a las familias de funciones de pertenencia
	 */
	private JPopupMenu familypopup;

	/**
	 * Panel desplegable asociado a las funciones no difusas
	 */
	private JPopupMenu crisppopup;

	/**
	 * Panel desplegable asociado a los métodos de concreción
	 */
	private JPopupMenu defuzpopup;

	/**
	 * Paquete de funciones editado
	 */
	private PackageDefinition pkg;

	/**
	 * Ventana principal del entorno Xfuzzy que realiza la llamada a la
	 * herramienta Xfpkg
	 */
	private Xfuzzy xfuzzy;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor utilizado desde Xfuzzy
	 */
	public Xfpkg(Xfuzzy xfuzzy, PackageDefinition pkg) {
		super("Xfpkg");
		this.xfuzzy = xfuzzy;
		this.pkg = pkg;
		this.pkg.setEditor(this);
		build();
		refresh();
	}

	/**
	 * Constructor utilizado desde la ejecución externa
	 */
	public Xfpkg(PackageDefinition pkg) {
		super("Xfpkg");
		this.xfuzzy = null;
		this.pkg = pkg;
		this.pkg.setEditor(this);
		build();
		refresh();
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS ESTÁTICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Abre una ventana de edición de un paquete de funciones
	 */
	public static void showPkgEditor(Xfuzzy xfuzzy, PackageDefinition pkg) {
		Xfpkg editor = new Xfpkg(xfuzzy, pkg);
		editor.setVisible(true);
	}

	/**
	 * Ejecución externa
	 */
	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: xfpkg pkgfile");
			return;
		}
		PkgParser parser = new PkgParser();
		parser.addPath(new File(System.getProperty("user.dir")));
		PackageDefinition pkg = parser.parse(new File(args[0]));
		if (pkg == null) {
			System.out.println(parser.resume());
			return;
		}
		Xfpkg editor = new Xfpkg(pkg);
		editor.setVisible(true);
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Obtiene la referencia al paquete en edición
	 */
	public PackageDefinition getPackage() {
		return this.pkg;
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
		else if (command.equals("Compile"))
			compile();
		else if (command.equals("Delete"))
			delete();
		else if (command.equals("Close"))
			exit();
		else if (command.equals("NewUnary"))
			newDefinition(UNARY);
		else if (command.equals("EditUnary"))
			editDefinition(UNARY);
		else if (command.equals("RemoveUnary"))
			removeDefinition(UNARY);
		else if (command.equals("NewBinary"))
			newDefinition(BINARY);
		else if (command.equals("EditBinary"))
			editDefinition(BINARY);
		else if (command.equals("RemoveBinary"))
			removeDefinition(BINARY);
		else if (command.equals("NewMF"))
			newDefinition(MEMBERSHIP);
		else if (command.equals("EditMF"))
			editDefinition(MEMBERSHIP);
		else if (command.equals("RemoveMF"))
			removeDefinition(MEMBERSHIP);
		else if (command.equals("NewFamily"))
			newDefinition(FAMILY);
		else if (command.equals("EditFamily"))
			editDefinition(FAMILY);
		else if (command.equals("RemoveFamily"))
			removeDefinition(FAMILY);
		else if (command.equals("NewCrisp"))
			newDefinition(CRISP);
		else if (command.equals("EditCrisp"))
			editDefinition(CRISP);
		else if (command.equals("RemoveCrisp"))
			removeDefinition(CRISP);
		else if (command.equals("NewDefuz"))
			newDefinition(DEFUZ);
		else if (command.equals("EditDefuz"))
			editDefinition(DEFUZ);
		else if (command.equals("RemoveDefuz"))
			removeDefinition(DEFUZ);
	}

	/**
	 * Interfaz ListSelectionListener
	 */
	public void valueChanged(ListSelectionEvent e) {
		int kind = getEventModule(e);
		listSelection(kind);
	}

	/**
	 * Interfaz KeyListener. Acción al soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int kind = getEventModule(e);
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE
				|| key == 127) {
			e.consume();
			removeDefinition(kind);
		}
		if (key == KeyEvent.VK_INSERT || key == 155) {
			e.consume();
			newDefinition(kind);
		}
		if (key == KeyEvent.VK_ENTER || key == 226) {
			e.consume();
			editDefinition(kind);
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
		int kind = getEventModule(e);
		editDefinition(kind);
	}

	/**
	 * Interfaz MouseListener. Acción al apretar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		int kind = getEventModule(e);
		showPopup(kind, e);
	}

	/**
	 * Interfaz MouseListener. Acción al soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		int kind = getEventModule(e);
		showPopup(kind, e);
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
		exit();
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
		if (e.getSource() == binlist.getList())
			kind = BINARY;
		if (e.getSource() == unarylist.getList())
			kind = UNARY;
		if (e.getSource() == mflist.getList())
			kind = MEMBERSHIP;
		if (e.getSource() == defuzlist.getList())
			kind = DEFUZ;
		if (e.getSource() == familylist.getList())
			kind = FAMILY;
		if (e.getSource() == crisplist.getList())
			kind = CRISP;
		return kind;
	}

	// ----------------------------------------------------------------------------//
	// Métodos para construir la interfaz gráfica //
	// ----------------------------------------------------------------------------//

	/**
	 * Genera el contenido de la ventana principal de Xfpkg
	 */
	private void build() {
		int width = 210;
		int height = 100;

		unarylist = new XList("Unary Functions");
		unarylist.setPreferredWidth(width);
		unarylist.setPreferredHeight(height);
		unarylist.addListSelectionListener(this);
		unarylist.addMouseListener(this);
		unarylist.addKeyListener(this);

		binlist = new XList("Binary Functions");
		binlist.setPreferredWidth(width);
		binlist.setPreferredHeight(height);
		binlist.addListSelectionListener(this);
		binlist.addMouseListener(this);
		binlist.addKeyListener(this);

		familylist = new XList("M. F. Families");
		familylist.setPreferredWidth(width);
		familylist.setPreferredHeight(height);
		familylist.addListSelectionListener(this);
		familylist.addMouseListener(this);
		familylist.addKeyListener(this);

		mflist = new XList("Membership Functions");
		mflist.setPreferredWidth(width);
		mflist.setPreferredHeight(height);
		mflist.addListSelectionListener(this);
		mflist.addMouseListener(this);
		mflist.addKeyListener(this);

		defuzlist = new XList("Defuzzification Methods");
		defuzlist.setPreferredWidth(width);
		defuzlist.setPreferredHeight(height);
		defuzlist.addListSelectionListener(this);
		defuzlist.addMouseListener(this);
		defuzlist.addKeyListener(this);

		crisplist = new XList("Crisp Functions");
		crisplist.setPreferredWidth(width);
		crisplist.setPreferredHeight(height);
		crisplist.addListSelectionListener(this);
		crisplist.addMouseListener(this);
		crisplist.addKeyListener(this);

		Box listbox1 = new Box(BoxLayout.Y_AXIS);
		listbox1.add(Box.createVerticalStrut(5));
		listbox1.add(unarylist);
		listbox1.add(Box.createVerticalStrut(5));
		listbox1.add(binlist);

		Box listbox2 = new Box(BoxLayout.Y_AXIS);
		listbox2.add(Box.createVerticalStrut(5));
		listbox2.add(familylist);
		listbox2.add(Box.createVerticalStrut(5));
		listbox2.add(mflist);

		Box listbox3 = new Box(BoxLayout.Y_AXIS);
		listbox3.add(Box.createVerticalStrut(5));
		listbox3.add(defuzlist);
		listbox3.add(Box.createVerticalStrut(5));
		listbox3.add(crisplist);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(listbox1);
		box.add(Box.createHorizontalStrut(5));
		box.add(listbox2);
		box.add(Box.createHorizontalStrut(5));
		box.add(listbox3);

		pkgname = new XTextForm("Name");
		pkgname.setAlignment(JLabel.CENTER);
		pkgname.setEditable(false);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(Box.createVerticalStrut(5));
		content.add(pkgname);
		content.add(Box.createVerticalStrut(5));
		content.add(box);
		content.add(Box.createVerticalStrut(5));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		addWindowListener(this);
		setJMenuBar(menubar());
		pack();
		setEditable(false);
		createPopups();
	}

	/**
	 * Construye la barra de menú de la herramienta
	 */
	private JMenuBar menubar() {
		String label[] = { "Save", "Save As", null, "Compile", null, "Delete",
				null, "Close edition" };
		String command[] = { "Save", "SaveAs", "Compile", "Delete", "Close" };

		menu = new XMenu("File", label, command, this);
		JMenuBar menubar = new JMenuBar();
		menubar.add(Box.createHorizontalStrut(5));
		menubar.add(menu);
		menubar.add(Box.createHorizontalStrut(5));
		menubar.add(Box.createGlue());
		return menubar;
	}

	/**
	 * Refresca el contenido de las listas
	 */
	private void refresh() {
		pkgname.setText(pkg.toString());
		binlist.setListData(pkg.get(PackageDefinition.BINARY));
		unarylist.setListData(pkg.get(PackageDefinition.UNARY));
		mflist.setListData(pkg.get(PackageDefinition.MFUNC));
		defuzlist.setListData(pkg.get(PackageDefinition.DEFUZ));
		familylist.setListData(pkg.get(PackageDefinition.FAMILY));
		crisplist.setListData(pkg.get(PackageDefinition.CRISP));
	}

	/**
	 * (Des)Habilita la edición del contenido de la definición
	 */
	private void setEditable(boolean editable) {
		boolean pkgeditable = pkg.isEditable();
		menu.setEnabled(!editable && pkgeditable, 0);
		menu.setEnabled(!editable && pkgeditable, 1);
		menu.setEnabled(!editable && pkgeditable, 2);
		menu.setEnabled(!editable && pkgeditable, 3);
		menu.setEnabled(!editable, 4);
		binlist.setEnabled(!editable);
		unarylist.setEnabled(!editable);
		mflist.setEnabled(!editable);
		defuzlist.setEnabled(!editable);
		familylist.setEnabled(!editable);
		crisplist.setEnabled(!editable);
	}

	// ----------------------------------------------------------------------------//
	// Métodos relacionados con menús desplegables //
	// ----------------------------------------------------------------------------//

	/**
	 * Genera los menús desplegables de las listas
	 */
	private void createPopups() {
		String ulabel[] = { "New unary function", "Edit unary function",
		"Remove unary function" };
		String blabel[] = { "New binary function", "Edit binary function",
		"Remove binary function" };
		String mlabel[] = { "New membership function",
				"Edit membership function", "Remove membership function" };
		String flabel[] = { "New family of membership functions",
				"Edit family of membership functions",
		"Remove family of membership functions" };
		String clabel[] = { "New crisp function", "Edit crisp function",
		"Remove crisp function" };
		String dlabel[] = { "New defuzzification method",
				"Edit defuzzification method", "Remove defuzzification method" };

		String ucommand[] = { "NewUnary", "EditUnary", "RemoveUnary" };
		String bcommand[] = { "NewBinary", "EditBinary", "RemoveBinary" };
		String mcommand[] = { "NewMF", "EditMF", "RemoveMF" };
		String fcommand[] = { "NewFamily", "EditFamily", "RemoveFamily" };
		String ccommand[] = { "NewCrisp", "EditCrisp", "RemoveCrisp" };
		String dcommand[] = { "NewDefuz", "EditDefuz", "RemoveDefuz" };

		unarypopup = createPopupMenu(ulabel, ucommand);
		binarypopup = createPopupMenu(blabel, bcommand);
		mfpopup = createPopupMenu(mlabel, mcommand);
		familypopup = createPopupMenu(flabel, fcommand);
		crisppopup = createPopupMenu(clabel, ccommand);
		defuzpopup = createPopupMenu(dlabel, dcommand);
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
		case BINARY:
			popup = binarypopup;
			break;
		case UNARY:
			popup = unarypopup;
			break;
		case MEMBERSHIP:
			popup = mfpopup;
			break;
		case FAMILY:
			popup = familypopup;
			break;
		case DEFUZ:
			popup = defuzpopup;
			break;
		case CRISP:
			popup = crisppopup;
			break;
		}
		if (popup != null)
			popup.show((Component) e.getSource(), e.getX(), e.getY());
	}

	// ----------------------------------------------------------------------------//
	// Acciones a realizar respecto a las listas //
	// ----------------------------------------------------------------------------//

	/**
	 * Elimina la seleccion de una lista al seleccionar otra
	 */
	private void listSelection(int list) {
		if (list == BINARY && binlist.isSelectionEmpty())
			return;
		if (list == UNARY && unarylist.isSelectionEmpty())
			return;
		if (list == MEMBERSHIP && mflist.isSelectionEmpty())
			return;
		if (list == DEFUZ && defuzlist.isSelectionEmpty())
			return;
		if (list == FAMILY && familylist.isSelectionEmpty())
			return;
		if (list == CRISP && crisplist.isSelectionEmpty())
			return;
		if (list != BINARY)
			binlist.clearSelection();
		if (list != UNARY)
			unarylist.clearSelection();
		if (list != MEMBERSHIP)
			mflist.clearSelection();
		if (list != DEFUZ)
			defuzlist.clearSelection();
		if (list != FAMILY)
			familylist.clearSelection();
		if (list != CRISP)
			crisplist.clearSelection();
	}

	/**
	 * Edita la definición seleccionada
	 */
	private void editDefinition(int list) {
		Definition definition = null;
		switch (list) {
		case BINARY:
			definition = (Definition) binlist.getSelectedValue();
			break;
		case UNARY:
			definition = (Definition) unarylist.getSelectedValue();
			break;
		case MEMBERSHIP:
			definition = (Definition) mflist.getSelectedValue();
			break;
		case DEFUZ:
			definition = (Definition) defuzlist.getSelectedValue();
			break;
		case FAMILY:
			definition = (Definition) familylist.getSelectedValue();
			break;
		case CRISP:
			definition = (Definition) crisplist.getSelectedValue();
			break;
		}
		if (definition == null || definition.isEditing())
			return;
		XfpkgDefinition.showDefinitionEditor(this, definition);
	}

	/**
	 * Elimina la definición seleccionada en una lista
	 */
	private void removeDefinition(int list) {
		if (!pkg.isEditable())
			return;
		Definition definition = null;
		switch (list) {
		case BINARY:
			definition = (Definition) binlist.getSelectedValue();
			break;
		case UNARY:
			definition = (Definition) unarylist.getSelectedValue();
			break;
		case MEMBERSHIP:
			definition = (Definition) mflist.getSelectedValue();
			break;
		case DEFUZ:
			definition = (Definition) defuzlist.getSelectedValue();
			break;
		case FAMILY:
			definition = (Definition) familylist.getSelectedValue();
			break;
		case CRISP:
			definition = (Definition) crisplist.getSelectedValue();
			break;
		}
		if (definition == null)
			return;
		definition.unlink();
		pkg.remove(definition, list);
		pkg.setModified(true);
		refresh();
	}

	/**
	 * Crea una nueva definición en una lista
	 */
	private void newDefinition(int list) {
		if (!pkg.isEditable())
			return;
		int i = 0;
		while (pkg.contains("noname_" + addZeroes(i), list))
			i++;
		String defname = "noname_" + addZeroes(i);
		Definition definition = createDefinition(list, defname);
		if (definition == null)
			return;
		pkg.add(definition, list);
		pkg.setModified(true);
		refresh();
		switch (list) {
		case BINARY:
			binlist.setSelectedValue(definition);
			break;
		case UNARY:
			unarylist.setSelectedValue(definition);
			break;
		case MEMBERSHIP:
			mflist.setSelectedValue(definition);
			break;
		case DEFUZ:
			defuzlist.setSelectedValue(definition);
			break;
		case FAMILY:
			familylist.setSelectedValue(definition);
			break;
		case CRISP:
			crisplist.setSelectedValue(definition);
			break;
		}
	}

	/**
	 * Obtiene una cadena que describe un entero con 3 caracteres
	 */
	private String addZeroes(int i) {
		if (i < 10)
			return "00" + i;
		if (i < 100)
			return "0" + i;
		return "" + i;
	}

	/**
	 * Crea una definición de un cierto tipo
	 */
	private Definition createDefinition(int list, String defname) {
		switch (list) {
		case BINARY:
			return Definition.createBinaryDefinition("" + pkg, defname);
		case UNARY:
			return Definition.createUnaryDefinition("" + pkg, defname);
		case MEMBERSHIP:
			return Definition.createMFDefinition("" + pkg, defname);
		case DEFUZ:
			return Definition.createDefuzDefinition("" + pkg, defname);
		case FAMILY:
			return Definition.createFamilyDefinition("" + pkg, defname);
		case CRISP:
			return Definition.createCrispDefinition("" + pkg, defname);
		default:
			return null;
		}
	}

	// ----------------------------------------------------------------------------//
	// Métodos que desarrollan las acciones del menú file //
	// ----------------------------------------------------------------------------//

	/**
	 * Almacena la descripción del paquete
	 */
	private void save() {
		if (pkg.save())
			pkg.setModified(false);
		else
			XDialog.showMessage(this, "Can't save package " + pkg + ".");
	}

	/**
	 * Almacena el paquete en un fichero seleccionado
	 */
	private void saveas() {
		JFileChooser chooser = new JFileChooser();
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy package files (.pkg)", "pkg");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(pkg.getFile());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save Package As ...");
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();
		String oldname = pkg.toString();
		String newname = file.getName().substring(0,
				file.getName().indexOf("."));
		if (oldname.equals("" + newname)) {
			save();
			return;
		}
		if (pkg.save_as(file)) {
			if (xfuzzy != null) {
				xfuzzy.log("Package " + oldname + " renamed to " + pkg + ".");
				xfuzzy.log("Package " + pkg + " saved as "
						+ file.getAbsolutePath() + ".");
			}
			pkg.setModified(false);
		} else
			XDialog.showMessage(this, "Can't save package " + pkg + ".");
		String resume = pkg.resume();
		if (resume != null && resume.length() > 0) {
			if (xfuzzy != null)
				xfuzzy.log(resume);
			else
				XDialog.showMessage(this, resume);
		}
		refresh();
		if (xfuzzy != null)
			xfuzzy.refreshList();
	}

	/**
	 * Elimina el paquete
	 */
	private void delete() {
		String msg = "Are you sure you want to delete package '" + pkg + "'?";
		if (!XDialog.showQuestion(this, msg))
			return;
		pkg.delete();
		setVisible(false);
		if (xfuzzy == null) {
			System.exit(0);
		} else {
			xfuzzy.removePackage(pkg);
		}
	}

	/**
	 * Compila todas las definiciones del paquete
	 */
	private void compile() {
		pkg.compile();
		String resume = pkg.resume();
		if (resume != null && resume.length() > 0) {
			if (xfuzzy != null)
				xfuzzy.log(resume);
			else
				XDialog.showMessage(this, resume);
		}
	}

	/**
	 * Sale de la aplicación
	 */
	private void exit() {
		if (pkg.isEditingDefinitions()) {
			String msg[] = new String[2];
			msg[0] = "Cannot close package edition";
			msg[1] = "Close editing windows first";
			XDialog.showMessage(null, msg);
			return;
		}
		if (pkg.isModified() && !saveChanges())
			return;
		pkg.setEditor(null);
		setVisible(false);
		if (xfuzzy == null)
			System.exit(0);
	}

	/**
	 * Almacena los cambios del paquete, si el usuario lo acepta
	 */
	private boolean saveChanges() {
		String msg[] = new String[2];
		msg[0] = "Package " + pkg + " has changed.";
		msg[1] = "Save before closing?";
		int answer = XDialog.showYNQuestion(this, msg);
		if (answer == XDialog.YES) {
			if (pkg.save()) {
				pkg.setModified(false);
				return true;
			} else {
				XDialog.showMessage(this, "Cannot save package " + pkg + ".");
				return false;
			}
		}
		if (answer == XDialog.NO)
			return true;
		return false;
	}
}
