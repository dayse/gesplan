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

package xfuzzy;

import xfuzzy.lang.*;
import xfuzzy.pkg.*;
import xfuzzy.xfpkg.Xfpkg;
import xfuzzy.xfedit.Xfedit;
import xfuzzy.xfdm.Xfdm;
import xfuzzy.xfsl.Xfsl;
import xfuzzy.xfsp.Xfsp;
import xfuzzy.xfsw.Xfsw;
import xfuzzy.xfmt.Xfmt;
import xfuzzy.xfplot.Xfplot;
import xfuzzy.xfsim.Xfsim;
import xfuzzy.xfcpp.Xfcpp;
import xfuzzy.xfvhdl.XfvhdlWindow;
import xfuzzy.xfj.XfjDialog;
import xfuzzy.xfsg.*;
import xfuzzy.util.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;

/**
 * Ventana principal de Xfuzzy 3.
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class Xfuzzy implements ActionListener, ListSelectionListener,
		KeyListener, MouseListener, WindowListener {

	// ----------------------------------------------------------------------------//
	// COSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Anchura de la ventana
	 */
	private static final int WIDTH = 750;

	/**
	 * Altura de la ventana
	 */
	private static final int HEIGHT = 750;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Marco de la ventana principal de la aplicación
	 */
	public JFrame frame;

	/**
	 * Componente de la lista de especificaciones
	 */
	private XList speclist;

	/**
	 * Componente de la lista de paquetes
	 */
	private XList pkglist;

	/**
	 * Componente de presentación de mensajes
	 */
	private JTextArea log;

	/**
	 * Menú principal
	 */
	private XMenu menu[];

	/**
	 * Menú desplegable para las especificaciones
	 */
	private JPopupMenu syspopup;

	/**
	 * Menú desplegable para los paquetes
	 */
	private JPopupMenu pkgpopup;

	/**
	 * Barra de botones
	 */
	private JButton toolb[];

	/**
	 * Carácter de fin de linea, dependiente de la plataforma
	 */
	private String eol = System.getProperty("line.separator", "\n");

	/**
	 * Fichero de almacenamiento de mensajes
	 */
	private PrintStream logfile = null;

	/**
	 * Directorio de trabajo
	 */
	private File workingdir = new File(
			System.getenv("XFUZZY_WORK_DIRECTORY")!= null
			? System.getenv("XFUZZY_WORK_DIRECTORY")
			: System.getProperty("user.dir"));

	/**
	 * Analizador de archivos de paquetes ".pkg"
	 */
	private PkgParser pkgparser = new PkgParser();

	/**
	 * Analizador de archivos de especificación ".xfl"
	 */
	private XflParser xflparser = new XflParser();

	/**
	 * Lista de paquetes almacenados en memoria
	 */
	private PackageDefinition[] pkg = new PackageDefinition[0];

	/**
	 * Lista de especificaciones almacenadas en memoria
	 */
	private Specification[] spec = new Specification[0];

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfuzzy() {
		boolean pkgPathFound = false, pkgLoaded = false;
		String xfuzzypath = System.getProperty("xfuzzy.path");
		this.frame = mainWindow();
		this.frame.setVisible(true);
		if (null != xfuzzypath) {
			File path = new File(xfuzzypath);
			if (null != path) {
				pkgPathFound = true;
				pkgparser.addPath(new File(path, "pkg"));
				pkgLoaded = loadPackage(new File(path, "pkg/xfl.pkg"));
				pkgLoaded &= loadPackage(new File(path, "pkg/xfsg.pkg"));
			} else {
				log("Warning, the \"xfuzzy.path\" environment variable does not point to a valid path.");
			}
		} else {
			log("Warning, the \"xfuzzy.path\" environment variable could not be found.");
		}

		if (!pkgPathFound || !pkgLoaded) {
			log("Standard XFL packages could not be loaded.");
			log("Check whether you are running Xfuzzy from a full installation with proper environment settings.");
		}
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Ejecución externa
	 */
	public static void main(String args[]) {
		Xfuzzy frame = new Xfuzzy();
		for (int i = 0; i < args.length; i++)
			frame.load(new File(args[i]));
	}

	/**
	 * Devuelve la lista de paquetes cargados
	 */
	public PackageDefinition[] getLoadedPackages() {
		return this.pkg;
	}

	/**
	 * Devuelve el directorio de trabajo
	 */
	public File getWorkingDirectory() {
		return this.workingdir;
	}

	/**
	 * Devuelve el analizador sintáctico de paquetes de funciones
	 */
	public PkgParser getPkgParser() {
		return this.pkgparser;
	}

	/**
	 * Devuelve el analizador sintáctico de especificaciones
	 */
	public XflParser getXflParser() {
		return this.xflparser;
	}

	/**
	 * Asigna el conjunto de paquetes de funciones cargados
	 */
	public void setLoadedPackages(PackageDefinition[] pkg) {
		this.pkg = pkg;
	}

	/**
	 * Genera un fichero temporal
	 */
	public File newTmpFile() {
		File w = workingdir;
		int i = 1;
		File tmp = new File(w, "tmp" + addZeroes(i) + ".xfl");
		while (tmp.exists()) {
			i++;
			tmp = new File(w, "tmp" + addZeroes(i) + ".xfl");
		}
		return tmp;
	}

	/**
	 * Genera un duplicado de una especificación
	 */
	public Specification duplicate(Specification spec) {
		File tmp = newTmpFile();
		File old = spec.getFile();
		spec.setFile(tmp);
		spec.save();
		spec.setFile(old);
		Specification dup = xflparser.parse(tmp.getAbsolutePath());
		if (dup == null)
			return null;
		tmp.delete();
		return dup;
	}

	/**
	 * Muestra un mensaje en el area de log
	 */
	public void log(String message) {
		String logmsg = message + "\n==\n";
		log.append(logmsg);
		if (this.logfile == null)
			return;
		this.logfile.println(message);
		this.logfile.println("==");
	}

	/**
	 * Actualiza el contenido de las listas de la ventana
	 */
	public void refreshList() {
		this.speclist.setListData(spec);
		this.pkglist.setListData(pkg);
	}

	/**
	 * Carga una especificación de un sistema
	 */
	public Specification load(File file) {
		for (int i = 0; i < this.spec.length; i++) {
			if (spec[i].getFile() == null)
				continue;
			String filename = spec[i].getFile().getAbsolutePath();
			if (filename.equals(file.getAbsolutePath()))
				return null;
		}

		Specification loaded = xflparser.parse(file.getAbsolutePath());
		if (loaded == null) {
			log(xflparser.resume());
			return null;
		}
		Specification[] aux = new Specification[spec.length + 1];
		System.arraycopy(spec, 0, aux, 0, spec.length);
		aux[spec.length] = loaded;
		this.spec = aux;
		this.speclist.setListData(spec);
		log("File " + file.getAbsolutePath() + " loaded as " + loaded.getName()
				+ ".");
		return loaded;
	}

	/**
	 * Carga un paquete de funciones
	 */
	public boolean loadPackage(File file) {
		boolean result = false;
		String filename = file.getName();
		String pkgname = filename.substring(0, filename.lastIndexOf(".pkg"));
		if (searchPackage(pkgname) != null) {
			XDialog.showMessage(frame, "Package " + pkgname
					+ " already exists.");
			return result;
		}
		PackageDefinition loaded = pkgparser.parse(file);
		if (loaded == null) {
			log(pkgparser.resume());
			return result;
		}

		PackageDefinition[] aux = new PackageDefinition[pkg.length + 1];
		System.arraycopy(pkg, 0, aux, 0, pkg.length);
		aux[pkg.length] = loaded;
		this.pkg = aux;

		pkglist.setListData(pkg);
		log("Package " + loaded + " loaded.");
		result = true;

		return result;
	}

	/**
	 * Elimina un paquete de la lista de paquetes cargados
	 * 
	 * @param removed
	 */
	public void removePackage(PackageDefinition removed) {
		int index = -1;
		for (int i = 0; i < pkg.length; i++)
			if (pkg[i] == removed)
				index = i;
		if (index < 0)
			return;

		PackageDefinition[] aux = new PackageDefinition[pkg.length - 1];
		if (index > 0)
			System.arraycopy(pkg, 0, aux, 0, index);
		if (index < aux.length)
			System.arraycopy(pkg, index + 1, aux, index, aux.length - index);
		this.pkg = aux;
		pkglist.setListData(pkg);
		log("Package " + removed + " removed.");
	}

	/**
	 * Interfaz ActionListener - Lanza las acciones de los items del menú
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("NewSystem"))
			newSystem();
		else if (command.equals("LoadSystem"))
			loadSystem();
		else if (command.equals("SaveSystem"))
			saveSystem();
		else if (command.equals("SaveSystemAs"))
			saveSystemAs();
		else if (command.equals("CloseSystem"))
			closeSystem();
		else if (command.equals("NewPackage"))
			newPackage();
		else if (command.equals("LoadPackage"))
			loadPackage();
		else if (command.equals("SavePackage"))
			savePackage();
		else if (command.equals("SavePackageAs"))
			savePackageAs();
		else if (command.equals("ClosePackage"))
			closePackage();
		else if (command.equals("Exit"))
			exit();
		else if (command.equals("Xfedit"))
			execXfedit();
		else if (command.equals("Xfpkg"))
			execXfpkg();
		else if (command.equals("Xfdm"))
			execXfdm();
		else if (command.equals("Xfsl"))
			execXfsl();
		else if (command.equals("Xfsp"))
			execXfsp();
		else if (command.equals("Xfplot"))
			execXfplot();
		else if (command.equals("Xfmt"))
			execXfmt();
		else if (command.equals("Xfsim"))
			execXfsim();
		else if (command.equals("Xfc"))
			execXfc();
		else if (command.equals("Xfcpp"))
			execXfcpp();
		else if (command.equals("Xfj"))
			execXfj();
		else if (command.equals("Xfvhdl"))
			execXfvhdl();
		else if (command.equals("Xfsg"))
			execXfsg();
		else if (command.equals("WorkingDir"))
			setWorkingDir();
		else if (command.equals("LogToFile"))
			setLogFile();
		else if (command.equals("StopLog"))
			stopLogging();
		else if (command.equals("ClearLog"))
			log.setText("");
		else if (command.equals("WindowsLAF"))
			changeLookAndFeel(0);
		else if (command.equals("MotifLAF"))
			changeLookAndFeel(1);
		else if (command.equals("MetalLAF"))
			changeLookAndFeel(2);
		else if (command.equals("BasicLAF"))
			changeLookAndFeel(3);
		else if (command.equals("Help"))
			help();
	}

	/**
	 * Interfaz ListSelectionListener - Acción asociada a la selección de una
	 * especificación o un paquete
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == speclist.getList())
			menuEnabled(1);
		if (e.getSource() == pkglist.getList())
			menuEnabled(2);
	}

	/**
	 * Interfaz KeyListener. DELETE permite cerrar una especificación o un
	 * paquete BACK_SPACE permite cerrar una especificación o un paquete INSERT
	 * permite crear una nueva especificación o paquete ENTER permite editar una
	 * especificación o paquete
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == speclist.getList()) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				e.consume();
				closeSystem();
			}
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				e.consume();
				closeSystem();
			}
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				e.consume();
				newSystem();
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				e.consume();
				execXfedit();
			}
			if (e.getKeyCode() == 226) {
				e.consume();
				execXfedit();
			}
		}
		if (e.getSource() == pkglist.getList()) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				e.consume();
				closePackage();
			}
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				e.consume();
				closePackage();
			}
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				e.consume();
				newPackage();
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				e.consume();
				execXfpkg();
			}
			if (e.getKeyCode() == 226) {
				e.consume();
				execXfpkg();
			}
		}
	}

	/**
	 * Interfaz KeyListener. No hace nada, las acciones de teclado las
	 * desarrolla keyReleased
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Interfaz KeyListener. No hace nada, las acciones de teclado las
	 * desarrolla keyReleased
	 */
	public void keyTyped(KeyEvent e) {
	}

	// -------------------------------------------------------------//
	// Interfaz MouseListener //
	// -------------------------------------------------------------//

	/**
	 * Interfaz MouseListener Un doble click permite editar una especificación o
	 * paquete
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() != 2)
			return;
		if (e.getSource() == speclist.getList())
			execXfedit();
		if (e.getSource() == pkglist.getList())
			execXfpkg();
	}

	/**
	 * Interfaz MouseListener Muestra el menú de acciones asociadas a
	 * especificaciones o paquetes
	 */
	public void mousePressed(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		if (e.getSource() == speclist.getList()) {
			syspopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
		if (e.getSource() == pkglist.getList()) {
			pkgpopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	/**
	 * Interfaz MouseListener Muestra el menú de acciones asociadas a
	 * especificaciones o paquetes
	 */
	public void mouseReleased(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		if (e.getSource() == speclist.getList()) {
			syspopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
		if (e.getSource() == pkglist.getList()) {
			pkgpopup.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	/**
	 * Interfaz MouseListener
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Interfaz MouseListener
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowClosing(WindowEvent e) {
		exit();
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowIconified(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Busca por su nombre un sistema en la lista
	 */
	private Specification searchSystem(String name) {
		for (int i = 0, size = this.spec.length; i < size; i++) {
			if (spec[i].equals(name))
				return spec[i];
		}
		return null;
	}

	/**
	 * Busca por su nombre un paquete de funciones de la lista
	 */
	private PackageDefinition searchPackage(String name) {
		for (int i = 0, size = this.pkg.length; i < size; i++) {
			if (pkg[i].equals(name))
				return pkg[i];
		}
		return null;
	}

	/**
	 * Cierra un sistema difuso, verificando que no esté siendo editado,
	 * preguntando si hay que salvar cambios, etc.
	 */
	private boolean close(Specification closing) {
		if (closing.isEditing()) {
			String msg[] = new String[2];
			msg[0] = "Cannot close specification " + closing.getName() + "."
					+ eol;
			msg[1] = "Close editing windows first.";
			XDialog.showMessage(frame, msg);
			Toolkit.getDefaultToolkit().beep();
			return false;
		}

		if (closing.isModified()) {
			String question[] = new String[2];
			question[0] = "Unsaved changes in specification "
					+ closing.getName() + ".";
			question[1] = "Save before closing?";
			Toolkit.getDefaultToolkit().beep();
			int answer = XDialog.showYNQuestion(frame, question);
			if (answer == XDialog.CANCEL)
				return false;
			if (answer == XDialog.YES && !closing.save())
				return false;
		}

		int index = -1;
		for (int i = 0; i < spec.length; i++)
			if (spec[i] == closing)
				index = i;
		if (index < 0)
			return false;

		Specification[] aux = new Specification[spec.length - 1];
		if (index > 0)
			System.arraycopy(spec, 0, aux, 0, index);
		if (index < aux.length)
			System.arraycopy(spec, index + 1, aux, index, aux.length - index);
		this.spec = aux;
		this.speclist.setListData(spec);
		return true;
	}

	/**
	 * Cierra un paquete de funciones, verificando que no esté siendo editado,
	 * preguntando si hay que salvar cambios, etc.
	 */
	private boolean close(PackageDefinition closing) {
		if (closing.isEditing()) {
			String msg[] = new String[2];
			msg[0] = "Cannot close package " + closing + ".";
			msg[1] = "Close editing window first.";
			Toolkit.getDefaultToolkit().beep();
			XDialog.showMessage(frame, msg);
			return false;
		}

		if (closing.isModified()) {
			String question[] = new String[2];
			question[0] = "Unsaved changes in package " + closing + ".";
			question[1] = "Save before closing?";
			Toolkit.getDefaultToolkit().beep();
			int answer = XDialog.showYNQuestion(frame, question);
			if (answer == XDialog.CANCEL)
				return false;
			if (answer == XDialog.YES && !closing.save())
				return false;
		}

		int index = -1;
		for (int i = 0; i < pkg.length; i++)
			if (pkg[i] == closing)
				index = i;
		if (index < 0)
			return false;

		PackageDefinition[] aux = new PackageDefinition[pkg.length - 1];
		if (index > 0)
			System.arraycopy(pkg, 0, aux, 0, index);
		if (index < aux.length)
			System.arraycopy(pkg, index + 1, aux, index, aux.length - index);
		this.pkg = aux;

		this.pkglist.setListData(pkg);
		return true;
	}

	/**
	 * (Des)Habilita los botones y acciones de los menús
	 */
	private void menuEnabled(int code) {
		if (code == 1 && speclist.isSelectionEmpty())
			return;
		if (code == 2 && pkglist.isSelectionEmpty())
			return;
		menu[0].setEnabled(true, 0);
		menu[0].setEnabled(true, 1);
		menu[0].setEnabled(true, 5);
		menu[0].setEnabled(true, 6);
		menu[0].setEnabled(true, 10);
		toolb[0].setEnabled(true);
		toolb[1].setEnabled(true);
		toolb[4].setEnabled(true);
		toolb[5].setEnabled(true);
		menu[5].setEnabled(logfile != null, 2);
		if (code == 0) {
			for (int i = 2; i < 5; i++)
				menu[0].setEnabled(false, i);
			for (int i = 7; i < 10; i++)
				menu[0].setEnabled(false, i);
			for (int i = 0; i < 1; i++)
				menu[1].setEnabled(false, i);
			for (int i = 0; i < 3; i++)
				menu[2].setEnabled(false, i);
			for (int i = 0; i < 3; i++)
				menu[3].setEnabled(false, i);
			for (int i = 0; i < 5; i++)
				menu[4].setEnabled(false, i);
			for (int i = 1; i < 2; i++)
				menu[1].setEnabled(false, i);
			toolb[2].setEnabled(false);
			toolb[3].setEnabled(false);
			toolb[6].setEnabled(false);
			toolb[7].setEnabled(false);
			for (int i = 8; i < toolb.length; i++)
				toolb[i].setEnabled(false);
			syspopup.getComponent(2).setEnabled(false);
			syspopup.getComponent(3).setEnabled(false);
			syspopup.getComponent(4).setEnabled(false);
			syspopup.getComponent(5).setEnabled(false);
			pkgpopup.getComponent(2).setEnabled(false);
			pkgpopup.getComponent(3).setEnabled(false);
			pkgpopup.getComponent(4).setEnabled(false);
			pkgpopup.getComponent(5).setEnabled(false);
			pkglist.clearSelection();
			speclist.clearSelection();
		}
		if (code == 1) {
			for (int i = 2; i < 5; i++)
				menu[0].setEnabled(true, i);
			for (int i = 0; i < 1; i++)
				menu[1].setEnabled(true, i);
			for (int i = 0; i < 3; i++)
				menu[2].setEnabled(true, i);
			for (int i = 0; i < 3; i++)
				menu[3].setEnabled(true, i);
			for (int i = 0; i < 5; i++)
				menu[4].setEnabled(true, i);
			for (int i = 7; i < 10; i++)
				menu[0].setEnabled(false, i);
			for (int i = 1; i < 2; i++)
				menu[1].setEnabled(false, i);
			toolb[2].setEnabled(true);
			toolb[3].setEnabled(true);
			toolb[6].setEnabled(false);
			toolb[7].setEnabled(false);
			for (int i = 8; i < toolb.length; i++)
				toolb[i].setEnabled(true);
			syspopup.getComponent(2).setEnabled(true);
			syspopup.getComponent(3).setEnabled(true);
			syspopup.getComponent(4).setEnabled(true);
			syspopup.getComponent(5).setEnabled(true);
			pkgpopup.getComponent(2).setEnabled(false);
			pkgpopup.getComponent(3).setEnabled(false);
			pkgpopup.getComponent(4).setEnabled(false);
			pkgpopup.getComponent(5).setEnabled(false);
			pkglist.clearSelection();
		}
		if (code == 2) {
			for (int i = 7; i < 10; i++)
				menu[0].setEnabled(true, i);
			for (int i = 1; i < 2; i++)
				menu[1].setEnabled(true, i);
			for (int i = 2; i < 5; i++)
				menu[0].setEnabled(false, i);
			for (int i = 0; i < 1; i++)
				menu[1].setEnabled(false, i);
			for (int i = 0; i < 3; i++)
				menu[2].setEnabled(false, i);
			for (int i = 0; i < 3; i++)
				menu[3].setEnabled(false, i);
			for (int i = 0; i < 6; i++)
				menu[4].setEnabled(false, i);
			toolb[2].setEnabled(false);
			toolb[3].setEnabled(false);
			toolb[6].setEnabled(true);
			toolb[7].setEnabled(true);
			for (int i = 8; i < toolb.length; i++)
				toolb[i].setEnabled(false);
			syspopup.getComponent(2).setEnabled(false);
			syspopup.getComponent(3).setEnabled(false);
			syspopup.getComponent(4).setEnabled(false);
			syspopup.getComponent(5).setEnabled(false);
			pkgpopup.getComponent(2).setEnabled(true);
			pkgpopup.getComponent(3).setEnabled(true);
			pkgpopup.getComponent(4).setEnabled(true);
			pkgpopup.getComponent(5).setEnabled(true);
			speclist.clearSelection();
		}
	}

	/**
	 * Devuelve el entero expresado con tres caracteres
	 */
	private String addZeroes(int i) {
		if (i < 10)
			return "00" + i;
		if (i < 100)
			return "0" + i;
		return "" + i;
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú "FILE" //
	// ----------------------------------------------------------------------------//

	/**
	 * Crea un nuevo sistema difuso y lo almacena en memoria
	 */
	private void newSystem() {
		int i = 0;
		while (searchSystem("noname_" + addZeroes(i)) != null)
			i++;
		File file = new File(workingdir, "noname_" + addZeroes(i) + ".xfl");
		Specification newspec = new Specification(file);
		Specification[] aux = new Specification[spec.length + 1];
		System.arraycopy(spec, 0, aux, 0, spec.length);
		aux[spec.length] = newspec;
		this.spec = aux;
		speclist.setListData(spec);
	}

	/**
	 * Carga un nuevo sistema difuso y lo almacena en memoria
	 */
	private void loadSystem() {
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy system files (.xfl)", "xfl");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load System");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		load(chooser.getSelectedFile());
	}

	/**
	 * Guarda un sistema difuso en su archivo
	 */
	private void saveSystem() {
		Specification selection = (Specification) this.speclist
				.getSelectedValue();
		if (selection == null)
			return;
		if (selection.save()) {
			String filepath = selection.getFile().getAbsolutePath();
			log("System " + selection + " saved as " + filepath + ".");
			selection.setModified(false);
		} else
			log("Can't save system " + selection + ".");
	}

	/**
	 * Guarda un sistema difuso en un archivo a seleccionar
	 */
	private void saveSystemAs() {
		Specification sel = (Specification) this.speclist.getSelectedValue();
		if (sel == null)
			return;
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy system files (.xfl)", "xfl");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(sel.getFile());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save System As ...");
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();

		if (file.exists()) {
			String question[] = new String[2];
			question[0] = "File " + file.getName() + " already exists.";
			question[1] = "Do you want to overwrite this file?";
			if (!XDialog.showQuestion(frame, question))
				return;
		}

		String oldname = sel.getName();
		if (sel.save_as(file)) {
			if (!oldname.equals("" + sel))
				log("System " + oldname + " renamed to " + sel + ".");
			log("System " + sel + " saved as " + file.getAbsolutePath() + ".");
			sel.setModified(false);
		} else
			log("Can't save specification " + sel + ".");
	}

	/**
	 * Cierra un sistema difuso
	 */
	private void closeSystem() {
		Specification sel = (Specification) this.speclist.getSelectedValue();
		if (sel == null)
			return;
		if (close(sel)) {
			log("System " + sel + " closed.");
			menuEnabled(0);
		}
	}

	/**
	 * Crea un nuevo paquete de funciones y lo almacena en memoria
	 */
	private void newPackage() {
		int i = 0;
		while (searchPackage("noname_" + addZeroes(i)) != null)
			i++;
		String name = "noname_" + addZeroes(i) + ".pkg";
		File file = new File(workingdir, name);
		PackageDefinition newpkg = new PackageDefinition(file);

		PackageDefinition[] aux = new PackageDefinition[pkg.length + 1];
		System.arraycopy(pkg, 0, aux, 0, pkg.length);
		aux[pkg.length] = newpkg;
		this.pkg = aux;

		pkglist.setListData(pkg);
	}

	/**
	 * Crea un nuevo paquete de funciones y lo almacena en memoria
	 */
	private void loadPackage() {
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy package files (.pkg)", "pkg");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load Package");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File pkgfile = chooser.getSelectedFile();
		String filename = pkgfile.getName();
		String pkgname = filename.substring(0, filename.lastIndexOf(".pkg"));
		if (searchPackage(pkgname) != null) {
			XDialog.showMessage(frame, "Package " + pkgname
					+ " already exists.");
			return;
		}
		PackageDefinition loaded = pkgparser.parse(pkgfile);
		if (loaded == null) {
			log(pkgparser.resume());
			return;
		}

		PackageDefinition[] aux = new PackageDefinition[pkg.length + 1];
		System.arraycopy(pkg, 0, aux, 0, pkg.length);
		aux[pkg.length] = loaded;
		this.pkg = aux;

		pkglist.setListData(pkg);
		log("Package " + loaded + " loaded.");
	}

	/**
	 * Guarda un paquete de funciones en su archivo
	 */
	private void savePackage() {
		PackageDefinition selection = (PackageDefinition) pkglist
				.getSelectedValue();
		if (selection == null)
			return;
		if (selection.save()) {
			String filepath = selection.getFile().getAbsolutePath();
			log("Package " + selection + " saved as " + filepath + ".");
			selection.setModified(false);
		} else
			log("Can't save package " + selection + ".");
	}

	/**
	 * Guarda un paquete de funciones en un archivo a seleccionar
	 */
	private void savePackageAs() {
		PackageDefinition sel = (PackageDefinition) this.pkglist
				.getSelectedValue();
		if (sel == null)
			return;
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy package files (.pkg)", "pkg");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(sel.getFile());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save Package As ...");
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();

		if (file.exists()) {
			String question[] = new String[2];
			question[0] = "File " + file.getName() + " already exists.";
			question[1] = "Do you want to overwrite this file?";
			if (!XDialog.showQuestion(frame, question))
				return;
		}

		String oldname = sel.toString();
		if (sel.save_as(file)) {
			if (!oldname.equals("" + sel))
				log("Package " + oldname + " renamed to " + sel + ".");
			log("Package " + sel + " saved as " + file.getAbsolutePath() + ".");
			sel.setModified(false);
		} else
			log("Can't save package " + sel + ".");
		pkglist.setListData(pkg);
	}

	/**
	 * Cierra un paquete de funciones
	 */
	private void closePackage() {
		PackageDefinition sel = (PackageDefinition) this.pkglist
				.getSelectedValue();
		if (sel == null || sel.isEditing())
			return;
		if (close(sel)) {
			log("Package " + sel + " closed.");
			menuEnabled(0);
		}
	}

	/**
	 * Método de salida del entorno
	 */
	private void exit() {
		for (int i = spec.length - 1; i >= 0; i--)
			close(spec[i]);
		for (int i = pkg.length - 1; i >= 0; i--)
			close(pkg[i]);
		if (spec.length == 0 && pkg.length == 0) {
			if (logfile != null)
				logfile.close();
			System.exit(0);
		}
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú "DESCRIPTION" //
	// ----------------------------------------------------------------------------//

	/**
	 * Aplica la herramienta "xfedit" al sistema seleccionado
	 */
	private void execXfedit() {
		Specification sel = (Specification) this.speclist.getSelectedValue();
		if (sel == null)
			return;
		if (sel.isEditing()) {
			Xfedit editor = (Xfedit) sel.getEditor();
			editor.setState(Frame.NORMAL);
			editor.toFront();
		} else
			Xfedit.showSystemEditor(this, sel);
	}

	/**
	 * Aplica la herramienta "xfpkg" al paquete seleccionado
	 */
	private void execXfpkg() {
		PackageDefinition selection = (PackageDefinition) pkglist
				.getSelectedValue();
		if (selection == null)
			return;
		if (selection.isEditing()) {
			Xfpkg editor = (Xfpkg) selection.getEditor();
			editor.setState(Frame.NORMAL);
			editor.toFront();
		} else
			Xfpkg.showPkgEditor(this, selection);
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú "TUNING" //
	// ----------------------------------------------------------------------------//

	/**
	 * Aplica la herramienta "xfdm" al sistema seleccionado
	 */
	private void execXfdm() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		Xfdm xfdm = new Xfdm(this, specsel);
		xfdm.setVisible(true);
	}

	/**
	 * Aplica la herramienta "xfsl" al sistema seleccionado
	 */
	private void execXfsl() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		Xfsl xfsl = new Xfsl(this, specsel);
		xfsl.setVisible(true);
	}

	/**
	 * Aplica la herramienta "xfsp" al sistema seleccionado
	 */
	private void execXfsp() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		Xfsp xfsp = new Xfsp(this, specsel);
		xfsp.show();
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú "VERIFICATION" //
	// ----------------------------------------------------------------------------//

	/**
	 * Aplica la herramienta "xfplot" al sistema seleccionado
	 */
	private void execXfplot() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		int numInputs = specsel.getSystemModule().getInputs().length;
		int numOutputs = specsel.getSystemModule().getOutputs().length;
		if (numInputs < 1 || numOutputs < 1) {
			log("Cannot plot system " + specsel + "." + eol
					+ "Invalid number of variables.");
			return;
		}
		Xfplot xfplot = new Xfplot(this, specsel);
		xfplot.setVisible(true);
	}

	/**
	 * Aplica la herramienta "xfmt" al sistema seleccionado
	 */
	private void execXfmt() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		Xfmt xfmt = new Xfmt(this, specsel);
		xfmt.setVisible(true);
	}

	/**
	 * Aplica la herramienta "xfsim" al sistema seleccionado
	 */
	private void execXfsim() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		Xfsim xfsim = new Xfsim(this, specsel);
		xfsim.setVisible(true);
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú "SYNTHESIS" //
	// ----------------------------------------------------------------------------//

	/**
	 * Aplica la herramienta "xfc" al sistema seleccionado
	 */
	private void execXfc() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Target directory");
		if (chooser.showDialog(frame, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		Xfsw compiler = new Xfsw(specsel, chooser.getSelectedFile(),
				Xfsw.ANSI_C);
		log(compiler.getMessage());
	}

	/**
	 * Aplica la herramienta "xfcpp" al sistema seleccionado
	 */
	private void execXfcpp() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Target directory");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		Xfcpp compiler = new Xfcpp(specsel, chooser.getSelectedFile());
		log(compiler.getMessage());
	}

	/**
	 * Aplica la herramienta "xfj" al sistema seleccionado
	 */
	private void execXfj() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		XfjDialog compiler = new XfjDialog(this, specsel);
		compiler.setVisible(true);
		if (compiler.getMessage() != null)
			log(compiler.getMessage());
	}

	/**
	 * Aplica la herramienta "Xfvhdl" al sistema seleccionado
	 */
	private void execXfvhdl() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		XfvhdlWindow compiler = new XfvhdlWindow(this, specsel);
		compiler.setVisible(true);
	}

	/**
	 * Aplica la herramienta "xfsg" al sistema seleccionado
	 */
	private void execXfsg() {
		Specification specsel = (Specification) this.speclist
				.getSelectedValue();
		if (specsel == null)
			return;
		XfsgWindow compiler = new XfsgWindow(this, specsel);
		compiler.setVisible(true);
		XfsgProperties.activar_boton_GMF = false;
	}

	// ----------------------------------------------------------------------------//
	// Acciones del menú "SET UP" //
	// ----------------------------------------------------------------------------//

	/**
	 * Selecciona el directorio de trabajo
	 */
	private void setWorkingDir() {
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Working Directory");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		workingdir = chooser.getSelectedFile();
		log("Working directory set to " + workingdir.getAbsolutePath() + ".");
	}

	/**
	 * Selecciona el archivo histórico
	 */
	private void setLogFile() {
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Log files", "log", "txt");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Log to File");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		File selected = chooser.getSelectedFile();
		try {
			FileOutputStream stream = new FileOutputStream(selected);
			logfile = new PrintStream(stream);
		} catch (IOException ex) {
			return;
		}
		log("Logging to file " + selected.getAbsolutePath() + ".");
		menu[4].setEnabled(true, 2);
	}

	/**
	 * Cierra el archivo histórico
	 */
	private void stopLogging() {
		if (logfile == null)
			return;
		logfile.close();
		logfile = null;
		menu[4].setEnabled(false, 2);
	}

	/**
	 * Selecciona el aspecto del entorno
	 */
	private void changeLookAndFeel(int index) {
		String name[] = { "Windows", "Motif", "Metal", "Basic" };
		String msg = "Look and Feel " + name[index]
				+ " not supported on this platform";
		String laf[] = { "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
				"com.sun.java.swing.plaf.motif.MotifLookAndFeel",
				"javax.swing.plaf.metal.MetalLookAndFeel",
				"javax.swing.plaf.basic.BasicLookAndFeel" };
		try {
			UIManager.setLookAndFeel(laf[index]);
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception e) {
			XDialog.showMessage(frame, msg);
		}
	}

	/**
	 * Muestra la ayuda del entorno
	 */
	private void help() {
		try {
			String command[] = new String[2];
			command[0] = System.getProperty("xfuzzy.html");
			command[1] = System.getProperty("xfuzzy.path");
			command[1] += File.separator + "doc" + File.separator
					+ "index.html";
			Runtime.getRuntime().exec(command);
		} catch (Exception ex) {
		}
	}

	// ----------------------------------------------------------------------------//
	// Métodos de creación de la ventana principal //
	// ----------------------------------------------------------------------------//

	/**
	 * Genera la barra de menús
	 */
	private JMenuBar menubar() {
		String title[] = { "File", "Design", "Tuning", "Verification",
				"Synthesis", "Set Up", "Look & Feel" };
		String label[][] = {
				{ "New System", "Load System", "Save System",
						"Save System As ...", "Close System", null,
						"New Package", "Load Package", "Save Package",
						"Save Package As ...", "Close Package", null, "Quit" },
				{ "Edit System", null, "Edit Package" },
				{ "Data Mining", "Supervised Learning", "Simplification" },
				{ "Graphic Representation", "Monitorization", "Simulation" },
				{ "To C", "To C++", "To Java", null, "To VHDL", "To Sysgen" },
				{ "Working directory", "Log to file", "Stop logging",
						"Clear log window" },
				{ "Windows L&F", "Motif L&F", "Metal L&F", "Basic L&F" }, };
		String command[][] = {
				{ "NewSystem", "LoadSystem", "SaveSystem", "SaveSystemAs",
						"CloseSystem", "NewPackage", "LoadPackage",
						"SavePackage", "SavePackageAs", "ClosePackage", "Exit" },
				{ "Xfedit", "Xfpkg" }, { "Xfdm", "Xfsl", "Xfsp" },
				{ "Xfplot", "Xfmt", "Xfsim" },
				{ "Xfc", "Xfcpp", "Xfj", "Xfvhdl", "Xfsg" },
				{ "WorkingDir", "LogToFile", "StopLog", "ClearLog" },
				{ "WindowsLAF", "MotifLAF", "MetalLAF", "BasicLAF" }, };

		menu = new XMenu[7];
		for (int i = 0; i < menu.length; i++)
			menu[i] = new XMenu(title[i], label[i], command[i], this);
		menu[5].addSeparator();
		menu[5].add(menu[6]);

		JMenuItem item = new JMenuItem("Help");
		item.setEnabled(true);
		item.setFont(XConstants.font);
		item.setActionCommand("Help");
		item.addActionListener(this);
		int width = item.getFontMetrics(XConstants.font).stringWidth(" Help ") + 20;
		Dimension maxsize = item.getMaximumSize();
		Dimension prefsize = item.getPreferredSize();
		item.setMaximumSize(new Dimension(width, maxsize.height));
		item.setPreferredSize(new Dimension(width, prefsize.height));

		JMenuBar menubar = new JMenuBar();
		menubar.add(Box.createHorizontalStrut(5));
		menubar.add(menu[0]);
		menubar.add(Box.createHorizontalStrut(10));
		menubar.add(menu[1]);
		menubar.add(Box.createHorizontalStrut(10));
		menubar.add(menu[2]);
		menubar.add(Box.createHorizontalStrut(10));
		menubar.add(menu[3]);
		menubar.add(Box.createHorizontalStrut(10));
		menubar.add(menu[4]);
		menubar.add(Box.createHorizontalStrut(10));
		menubar.add(menu[5]);
		menubar.add(Box.createHorizontalStrut(10));
		menubar.add(Box.createHorizontalGlue());
		menubar.add(item);
		menubar.add(Box.createHorizontalStrut(5));
		return menubar;
	}

	/**
	 * Genera la barra de iconos
	 */
	private JPanel toolbar() {
		String tip[] = { "New system", "Load system", "Save system",
				"Close system", "New package", "Load package", "Save package",
				"Close package", "Data mining", "Supervised learning",
				"Simplification", "Graphic representation", "Monitorization",
				"Simulation", "Compile to C", "Compile to C++",
				"Compile to Java", "Compile to VHDL", "Compile to Sysgen" 
				};
		String command[] = { "NewSystem", "LoadSystem", "SaveSystem",
				"CloseSystem", "NewPackage", "LoadPackage", "SavePackage",
				"ClosePackage", "Xfdm", "Xfsl", "Xfsp", "Xfplot", "Xfmt",
				"Xfsim", "Xfc", "Xfcpp", "Xfj", "Xfvhdl", "Xfsg" };

		ImageIcon icon[] = { XfuzzyIcons.xfuzzydoc, XfuzzyIcons.xfuzzyload,
				XfuzzyIcons.xfuzzysave, XfuzzyIcons.xfuzzyclose,
				XfuzzyIcons.pkg, XfuzzyIcons.pkgload, XfuzzyIcons.pkgsave,
				XfuzzyIcons.pkgclose, XfuzzyIcons.xfdm, XfuzzyIcons.xfsl,
				XfuzzyIcons.xfsp, XfuzzyIcons.xfplot, XfuzzyIcons.xfmt,
				XfuzzyIcons.xfsim, XfuzzyIcons.xfc, XfuzzyIcons.xfcpp,
				XfuzzyIcons.xfj, XfuzzyIcons.xfvhdl, XfuzzyIcons.xfsg 
				};

		ImageIcon disicon[] = { XfuzzyIcons.xfuzzydoc, XfuzzyIcons.xfuzzyload,
				XfuzzyIcons.xfuzzysave_d, XfuzzyIcons.xfuzzyclose_d,
				XfuzzyIcons.pkg, XfuzzyIcons.pkgload, XfuzzyIcons.pkgsave_d,
				XfuzzyIcons.pkgclose_d, XfuzzyIcons.xfdm_d, XfuzzyIcons.xfsl_d,
				XfuzzyIcons.xfsp_d, XfuzzyIcons.xfplot_d, XfuzzyIcons.xfmt_d,
				XfuzzyIcons.xfsim_d, XfuzzyIcons.xfc_d, XfuzzyIcons.xfcpp_d,
				XfuzzyIcons.xfj_d, XfuzzyIcons.xfvhdl_d, XfuzzyIcons.xfsg_d 
				};

		toolb = new JButton[icon.length];
		for (int i = 0; i < toolb.length; i++) {
			toolb[i] = new JButton(icon[i]);
			toolb[i].setBorder(new EmptyBorder(0, 0, 0, 0));
			toolb[i].setActionCommand(command[i]);
			toolb[i].addActionListener(this);
			toolb[i].setToolTipText(tip[i]);
			toolb[i].setDisabledIcon(disicon[i]);
			toolb[i].setEnabled(false);
			toolb[i].setAlignmentY(0.5f);
		}

		toolb[0].setEnabled(true);
		toolb[1].setEnabled(true);
		toolb[4].setEnabled(true);
		toolb[5].setEnabled(true);
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		for (int i = 0; i < toolb.length; i++) {
			if (i == 4 || i == 8 || i == 11 || i == 14)
				toolbar.addSeparator();
			toolbar.add(Box.createHorizontalStrut(3));
			toolbar.add(toolb[i]);
		}

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.add(toolbar);

		Dimension maxsize = panel.getMaximumSize();
		Dimension minsize = panel.getMinimumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width, minsize.height));
		panel.setPreferredSize(new Dimension(prefsize.width, minsize.height));

		return panel;
	}

	/**
	 * Genera el menú desplegable de la lista de especificaciones
	 */
	private JPopupMenu createSystemPopupMenu() {
		String label[] = { "New System", "Load System", "Edit System",
				"Save System", "Save System As ...", "Close System" };
		String command[] = { "NewSystem", "LoadSystem", "Xfedit", "SaveSystem",
				"SaveSystemAs", "CloseSystem" };
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
	 * Genera el menú desplegable de la lista de paquetes
	 */
	private JPopupMenu createPackagePopupMenu() {
		String label[] = { "New Package", "Load Package", "Edit Package",
				"Save Package", "Save Package As ...", "Close Package" };
		String command[] = { "NewPackage", "LoadPackage", "Xfpkg",
				"SavePackage", "SavePackageAs", "ClosePackage" };
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
	 * Genera la ventana principal
	 */
	private JFrame mainWindow() {
		JFrame mainwin = new JFrame(XConstants.version);
		mainwin.setSize(WIDTH, HEIGHT);

		speclist = new XList("Available Systems");
		speclist.addListSelectionListener(this);
		speclist.addMouseListener(this);
		speclist.addKeyListener(this);
		speclist.setPreferredWidth(3 * WIDTH / 4);

		pkglist = new XList("Available Packages");
		pkglist.addListSelectionListener(this);
		pkglist.addMouseListener(this);
		pkglist.addKeyListener(this);
		pkglist.setPreferredWidth(WIDTH / 4);

		log = new JTextArea(30, 50);
		log.setBackground(XConstants.textbackground);
		log.setEditable(false);

		Box list = new Box(BoxLayout.X_AXIS);
		list.add(speclist);
		list.add(pkglist);

		JSeparator sep1 = new JSeparator();
		Dimension maxsize = sep1.getMaximumSize();
		Dimension prefsize = sep1.getPreferredSize();
		sep1.setMaximumSize(new Dimension(maxsize.width, prefsize.height));
		JSeparator sep2 = new JSeparator();
		sep2.setMaximumSize(new Dimension(maxsize.width, prefsize.height));

		Box top = new Box(BoxLayout.Y_AXIS);
		top.add(sep1);
		top.add(toolbar());
		top.add(sep2);
		top.add(list);

		JScrollPane bottom = new JScrollPane(log);
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
		pane.setOneTouchExpandable(false);
		pane.setContinuousLayout(false);
		pane.setDividerLocation(HEIGHT / 5);

		Container content = mainwin.getContentPane();
		content.setLayout(new GridLayout(1, 1));
		content.add(pane);

		mainwin.setJMenuBar(menubar());
		mainwin.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainwin.addWindowListener(this);
		mainwin.setIconImage(XfuzzyIcons.xfuzzy.getImage());

		syspopup = createSystemPopupMenu();
		pkgpopup = createPackagePopupMenu();
		menuEnabled(0);
		return mainwin;
	}

}
