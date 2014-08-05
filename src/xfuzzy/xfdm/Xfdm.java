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
//CONSTRUCCION AUTOMATICA DE UN SISTEMA		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Xfdm extends JFrame implements ActionListener, WindowListener {

	/**
	 * Codigo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603011L;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MIEMBROS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	private Xfuzzy xfuzzy;

	private Specification spec;

	private XfdmConfig config;

	private XTextForm text[];

	private XCommandForm form;

	private JPopupMenu algorithmmenu;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTOR //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public Xfdm(Xfuzzy xfuzzy, Specification spec) {
		super("Xfdm");
		this.xfuzzy = xfuzzy;
		this.spec = spec;
		this.config = new XfdmConfig();
		build();
		refresh();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PUBLICOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Actualiza la configuracion //
	// -------------------------------------------------------------//

	public void setConfig(XfdmConfig xfdmc) {
		this.config = xfdmc;
		refresh();
	}

	// -------------------------------------------------------------//
	// Obtiene la configuracion //
	// -------------------------------------------------------------//

	public XfdmConfig getConfig() {
		return this.config;
	}

	// -------------------------------------------------------------//
	// Obtiene la especificacion del sistema //
	// -------------------------------------------------------------//

	public Specification getSpecification() {
		return this.spec;
	}

	// -------------------------------------------------------------//
	// Actualiza los campos de la ventana //
	// -------------------------------------------------------------//

	public void refresh() {
		if (config.algorithm != null)
			text[5].setText(config.algorithm.toString());
		else
			text[5].setText("");

		if (config.patternfile != null)
			text[0].setText(config.patternfile.getAbsolutePath());
		else
			text[0].setText("");

		if (config.numinputs > 0)
			text[1].setText("" + config.numinputs);
		else
			text[1].setText("");

		if (config.numoutputs > 0)
			text[2].setText("" + config.numoutputs);
		else
			text[2].setText("");

		if (config.testInputStyle())
			text[3].setText("Configured");
		else
			text[3].setText("Unconfigured");

		if (config.systemstyle != null)
			text[4].setText("Configured");
		else
			text[4].setText("Unconfigured");

		form.setEnabled(2, config.isReadyToRun());
	}

	// -------------------------------------------------------------//
	// Finaliza externamente //
	// -------------------------------------------------------------//

	public void finish(String msg) {
		XDialog.showMessage(text[2], msg);
		actionClose();
	}

	// -------------------------------------------------------------//
	// Interfaz ActionListener //
	// -------------------------------------------------------------//

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Algorithm"))
			actionAlgorithm();
		else if (command.equals("Flat"))
			actionFlat();
		else if (command.equals("Wang"))
			actionWang();
		else if (command.equals("Nauck"))
			actionNauck();
		else if (command.equals("Senhadji"))
			actionSenhadji();
		else if (command.equals("IncGrid"))
			actionIncGrid();
		else if (command.equals("IncCluster"))
			actionIncCluster();
		else if (command.equals("FixedCluster"))
			actionFixedCluster();
		else if (command.equals("ICFA"))
			actionICFACluster();
		else if (command.equals("Pattern"))
			actionPattern();
		else if (command.equals("Inputs"))
			actionInputs();
		else if (command.equals("System"))
			actionSystem();
		else if (command.equals("Load"))
			actionLoad();
		else if (command.equals("Save"))
			actionSave();
		else if (command.equals("Create"))
			actionCreate();
		else if (command.equals("Close"))
			actionClose();
	}

	// -------------------------------------------------------------//
	// Interfaz WindowListener //
	// -------------------------------------------------------------//

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		actionClose();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// =============================================================//
	// Metodos de descripcion de la interfaz grafica //
	// =============================================================//

	// -------------------------------------------------------------//
	// Generacion de la ventana //
	// -------------------------------------------------------------//

	private void build() {
		algorithmmenu = buildAlgorithmMenu();

		String lb[] = { "Load Configuration", "Save Configuration",
				"Create System", "Close" };
		String cm[] = { "Load", "Save", "Create", "Close" };
		form = new XCommandForm(lb, cm, this);
		form.setCommandWidth(150);
		form.block();

		text = new XTextForm[6];
		text[0] = new XTextForm("Pattern file", this);
		text[0].setActionCommand("Pattern");
		text[0].setEditable(false);
		text[1] = new XTextForm("Number of inputs");
		text[2] = new XTextForm("Number of outputs");
		text[3] = new XTextForm("Input style", this);
		text[3].setActionCommand("Inputs");
		text[3].setEditable(false);
		text[4] = new XTextForm("System style", this);
		text[4].setActionCommand("System");
		text[4].setEditable(false);
		text[5] = new XTextForm("Algorithm", this);
		text[5].setActionCommand("Algorithm");
		text[5].setEditable(false);
		XTextForm.setWidth(text);

		Box lbox = new Box(BoxLayout.Y_AXIS);
		lbox.add(text[1]);
		lbox.add(text[3]);

		Box rbox = new Box(BoxLayout.Y_AXIS);
		rbox.add(text[2]);
		rbox.add(text[4]);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(lbox);
		box.add(rbox);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel("Data Mining"));
		content.add(Box.createVerticalStrut(5));
		content.add(text[5]);
		content.add(text[0]);
		content.add(box);
		content.add(Box.createVerticalStrut(5));
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		addWindowListener(this);
		pack();
		setLocation();
	}

	// -------------------------------------------------------------//
	// Menu de algoritmos //
	// -------------------------------------------------------------//

	private JPopupMenu buildAlgorithmMenu() {
		String label0[] = { "Flat system", "Wang & Mendel", "Nauck",
				"Senhadji", "Incremental Grid" };
		String command0[] = { "Flat", "Wang", "Nauck", "Senhadji", "IncGrid" };
		String label1[] = { "Incremental Clustering", "Fixed Clustering", "ICFA (Function Approximation)" };
		String command1[] = { "IncCluster", "FixedCluster", "ICFA" };

		JMenu[] fam = new JMenu[2];
		fam[0] = new JMenu("Structure-oriented algorithms");
		fam[0].setFont(XConstants.font);
		for (int i = 0; i < label0.length; i++) {
			JMenuItem item = new JMenuItem(label0[i]);
			item.setFont(XConstants.font);
			item.addActionListener(this);
			item.setActionCommand(command0[i]);
			fam[0].add(item);
		}

		fam[1] = new JMenu("Cluster-oriented algorithms");
		fam[1].setFont(XConstants.font);
		for (int i = 0; i < label1.length; i++) {
			JMenuItem item = new JMenuItem(label1[i]);
			item.setFont(XConstants.font);
			item.addActionListener(this);
			item.setActionCommand(command1[i]);
			fam[1].add(item);
		}

		JPopupMenu menu = new JPopupMenu();
		menu.add(fam[0]);
		menu.add(fam[1]);
		return menu;
	}

	// -------------------------------------------------------------//
	// Coloca la ventana en la pantalla //
	// -------------------------------------------------------------//

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

	// =============================================================//
	// Metodos de manejo de la configuracion //
	// =============================================================//

	// -------------------------------------------------------------//
	// Lee el valor de un campo de la ventana //
	// -------------------------------------------------------------//

	private int getValue(XTextForm textform) {
		int val = -1;
		try {
			val = Integer.parseInt(textform.getText());
		} catch (NumberFormatException ex) {
			textform.setText("");
			XDialog.showMessage(text[2], "Not a numeric value");
			return -1;
		}
		if (val <= 0) {
			textform.setText("");
			XDialog.showMessage(text[2], "Not a valid value");
			return -1;
		}
		return val;
	}

	// =============================================================//
	// Acciones de los botones de la ventana //
	// =============================================================//

	// -------------------------------------------------------------//
	// Accion del menu de algoritmos //
	// -------------------------------------------------------------//

	private void actionAlgorithm() {
		algorithmmenu.show(text[5], 0, text[5].getHeight());
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo Flat System //
	// -------------------------------------------------------------//

	private void actionFlat() {
		config.algorithm = new XfdmFlatSystem();
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo de Wang y Mendel //
	// -------------------------------------------------------------//

	private void actionWang() {
		config.algorithm = new XfdmWangMendel();
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo de Nauck //
	// -------------------------------------------------------------//

	private void actionNauck() {
		XfdmAlgorithm alg = XfdmNauckDialog.showDialog(this, config.algorithm);
		if (alg != null)
			config.algorithm = alg;
		if (config.systemstyle != null) {
			config.systemstyle.defuz = XfdmSystemStyle.CLASSIFICATION;
		}
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo de Senhadji //
	// -------------------------------------------------------------//

	private void actionSenhadji() {
		XfdmAlgorithm alg = XfdmSenhadjiDialog.showDialog(this,
				config.algorithm);
		if (alg != null)
			config.algorithm = alg;
		if (config.systemstyle != null) {
			config.systemstyle.defuz = XfdmSystemStyle.CLASSIFICATION;
		}
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo de Incremental Grid Partition //
	// -------------------------------------------------------------//

	private void actionIncGrid() {
		XfdmAlgorithm alg = XfdmIncGridDialog
		.showDialog(this, config.algorithm);
		if (alg != null)
			config.algorithm = alg;
		if (config.systemstyle != null
				&& config.systemstyle.defuz != XfdmSystemStyle.FUZZYMEAN
				&& config.systemstyle.defuz != XfdmSystemStyle.WEIGHTED) {
			config.systemstyle.defuz = XfdmSystemStyle.FUZZYMEAN;
		}
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo de Incremental Clustering //
	// -------------------------------------------------------------//

	private void actionIncCluster() {
		XfdmAlgorithm alg = XfdmIncClustDialog.showDialog(this,
				config.algorithm);
		if (alg != null)
			config.algorithm = alg;
		if (config.systemstyle != null
				&& config.systemstyle.defuz == XfdmSystemStyle.CLASSIFICATION) {
			config.systemstyle.defuz = XfdmSystemStyle.FUZZYMEAN;
		}
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion del algoritmo de Fixed Clustering //
	// -------------------------------------------------------------//

	private void actionFixedCluster() {
		XfdmAlgorithm alg = XfdmClusteringDialog.showDialog(this,
				config.algorithm);
		if (alg != null)
			config.algorithm = alg;
		if (config.systemstyle != null
				&& config.systemstyle.defuz == XfdmSystemStyle.CLASSIFICATION) {
			config.systemstyle.defuz = XfdmSystemStyle.FUZZYMEAN;
		}
		refresh();
	}

	private void actionICFACluster() {
	    XfdmICFA alg = XfdmICFAClustDialog.showDialog(this,
							       config.algorithm);
	    if (alg != null)
		config.algorithm = alg;
	    if (config.systemstyle != null
		&& config.systemstyle.defuz == XfdmSystemStyle.CLASSIFICATION) {
		config.systemstyle.defuz = XfdmSystemStyle.FUZZYMEAN;
	    }
	    config.numinputs = 0;
	    config.commonstyle = new XfdmInputStyle();
	    config.commonstyle.style = XfdmInputStyle.FREE_GAUSSIANS;
	    config.commonstyle.mfs = alg.getNumberOfClusters();
	    config.systemstyle = new XfdmSystemStyle();
	    config.systemstyle.creation = true;
	    refresh();
	}

	// -------------------------------------------------------------//
	// Accion de seleccion del fichero de patrones //
	// -------------------------------------------------------------//

	private void actionPattern() {
		File root = this.config.patternfile;
		if (root == null && xfuzzy != null)
			root = xfuzzy.getWorkingDirectory();
		if (root == null && xfuzzy == null)
			root = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Pattern file");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		config.patternfile = chooser.getSelectedFile();
		refresh();
	}

	// -------------------------------------------------------------//
	// Accion de seleccion del estilo de variables de entrada //
	// -------------------------------------------------------------//

	private void actionInputs() {
		int outputs = -1;
		try {
			outputs = Integer.parseInt(text[2].getText());
		} catch (NumberFormatException ex) {
		}
		if (outputs > 0)
			this.config.numoutputs = outputs;

		int inputs = getValue(text[1]);
		if (inputs < 0) {
			String msg = "Cannot edit input style. Select number of inputs first";
			XDialog.showMessage(text[1], msg);
			return;
		}
		this.config.numinputs = inputs;
		XfdmInputStyleDialog editor = new XfdmInputStyleDialog(this);
		editor.setVisible(true);
	}

	// -------------------------------------------------------------//
	// Accion de seleccion del estilo de sistema //
	// -------------------------------------------------------------//

	private void actionSystem() {
		int inputs = -1;
		try {
			inputs = Integer.parseInt(text[1].getText());
		} catch (NumberFormatException ex) {
		}
		if (inputs > 0)
			this.config.numinputs = inputs;

		int outputs = getValue(text[2]);
		if (outputs < 0) {
			String msg = "Cannot edit system style. Select number of outputs first";
			XDialog.showMessage(text[2], msg);
			return;
		}
		this.config.numoutputs = outputs;
		XfdmSystemStyleDialog editor = new XfdmSystemStyleDialog(this);
		editor.setVisible(true);
	}

	// -------------------------------------------------------------//
	// Accion de carga de la configuracion //
	// -------------------------------------------------------------//

	private void actionLoad() {
		File root = this.config.patternfile;
		if (root == null && xfuzzy != null)
			root = xfuzzy.getWorkingDirectory();
		if (root == null && xfuzzy == null)
			root = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy configuration files (.cfg)", "cfg");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load Configuration");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();
		XfuzzyConfig xfcparser = new XfuzzyConfig();
		XfdmConfig xfdmc = xfcparser.parseXfdmConfig(file);
		if (xfdmc != null) {
			this.config = xfdmc;
			refresh();
		} else {
			XDialog.showMessage(text[2], xfcparser.resume());
			Toolkit.getDefaultToolkit().beep();
		}
	}

	// -------------------------------------------------------------//
	// Accion de almacenamiento de la configuracion //
	// -------------------------------------------------------------//

	private void actionSave() {
		File root = this.config.patternfile;
		if (root == null && xfuzzy != null)
			root = xfuzzy.getWorkingDirectory();
		if (root == null && xfuzzy == null)
			root = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy configuration files (.cfg)", "cfg");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save Configuration");
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
		config.save(file);
	}

	// -------------------------------------------------------------//
	// Accion de creacion del sistema difuso //
	// -------------------------------------------------------------//

	private void actionCreate() {
		form.setLabel(2, "Creating");
		form.setEnabled(0, false);
		form.setEnabled(1, false);
		form.setEnabled(2, false);
		form.setEnabled(3, false);
		repaint();
		new XfdmProcess(this);
		finish("Knowledge acquisition successfully finished");
	}

	//-------------------------------------------------------------//
	// Accion de cerrar la ventana					//
	//-------------------------------------------------------------//

	private void actionClose() {
		setVisible(false);
		if (xfuzzy == null)
			System.exit(0);
	}

}
