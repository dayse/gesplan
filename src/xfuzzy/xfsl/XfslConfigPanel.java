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
//PANEL DE CONFIGURACION DEL APRENDIZAJE SUPERVISADO	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
package xfuzzy.xfsl;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class XfslConfigPanel extends Box implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603065L;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MIEMBROS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	private XTextForm text[] = new XTextForm[9];

	private XCommandForm form;

	private XfslConfig config;

	private Xfsl xfsl;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTOR //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public XfslConfigPanel(Xfsl xfsl) {
		super(BoxLayout.Y_AXIS);
		this.xfsl = xfsl;
		this.config = xfsl.getConfig();
		build();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PUBLICOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// (Des)Habilita los botones del panel //
	// -------------------------------------------------------------//

	public void setEnabled(boolean enable) {
		for (int i = 0; i < text.length; i++)
			text[i].setLabelEnabled(enable);
		form.setEnabled(0, enable);
		form.setEnabled(1, enable);
	}

	// -------------------------------------------------------------//
	// Actualiza la informacion mostrada en el panel //
	// -------------------------------------------------------------//

	public void set() {
		config.modified = true;
		if (config.trainingfile != null)
			text[0].setText(config.trainingfile.getAbsolutePath());
		else
			text[0].setText("");
		if (config.testfile != null)
			text[1].setText(config.testfile.getAbsolutePath());
		else
			text[1].setText("");
		if (config.logfile != null)
			text[2].setText(config.logfile.toString());
		else
			text[2].setText("");
		if (config.algorithm != null)
			text[3].setText(config.algorithm.getName());
		else
			text[3].setText("");
		if (config.preprocessing.isOn())
			text[4].setText("On");
		else
			text[4].setText("Off");
		if (config.endcondition.isOn())
			text[5].setText("On");
		else
			text[5].setText("No End");
		text[6].setText(config.errorfunction.getName());
		if (config.postprocessing.isOn())
			text[7].setText("On");
		else
			text[7].setText("Off");
		if (config.areSettingsOn())
			text[8].setText("On");
		else
			text[8].setText("Off");
		xfsl.setStatus();
	}

	// -------------------------------------------------------------//
	// Interfaz ActionListener //
	// -------------------------------------------------------------//

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("SaveConfig"))
			actionSaveConfig();
		else if (command.equals("LoadConfig"))
			actionLoadConfig();
		else if (command.equals("Settings"))
			actionSettings();
		else if (command.equals("EndCondition"))
			actionEndCondition();
		else if (command.equals("Postprocessing"))
			actionPostprocessing();
		else if (command.equals("Preprocessing"))
			actionPreprocessing();
		else if (command.equals("LogFile"))
			actionLogFile();
		else if (command.equals("TestFile"))
			actionTestFile();
		else if (command.equals("TrainingFile"))
			actionTrainingFile();
		else if (command.equals("ErrorFunction"))
			actionErrorFunction();
		else if (command.equals("Algorithm"))
			actionAlgorithm();
		else if (command.startsWith("ALG"))
			actionAlgorithm(command.substring(3));
		else if (command.startsWith("ERR"))
			actionErrorFunction(command.substring(3));
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Construye el panel //
	// -------------------------------------------------------------//

	private void build() {
		String lb[] = { "Training File", "Test File", "Log File", "Algorithm",
				"Preprocessing", "End Condition", "Error Function",
				"Postprocessing", "Settings" };
		String cm[] = { "TrainingFile", "TestFile", "LogFile", "Algorithm",
				"Preprocessing", "EndCondition", "ErrorFunction",
				"Postprocessing", "Settings" };
		for (int i = 0; i < text.length; i++) {
			text[i] = new XTextForm(lb[i], this);
			text[i].setActionCommand(cm[i]);
			text[i].setEditable(false);
		}
		XTextForm.setWidth(text);

		Box lbox = new Box(BoxLayout.Y_AXIS);
		for (int i = 3; i < 6; i++)
			lbox.add(text[i]);
		Box rbox = new Box(BoxLayout.Y_AXIS);
		for (int i = 6; i < 9; i++)
			rbox.add(text[i]);
		Box box = new Box(BoxLayout.X_AXIS);
		box.add(lbox);
		box.add(rbox);

		String label[] = { "Load Configuration", "Save Configuration" };
		String command[] = { "LoadConfig", "SaveConfig" };
		form = new XCommandForm(label, command, this);
		form.setCommandWidth(200);

		add(new XLabel("Configuration"));
		for (int i = 0; i < 3; i++)
			add(text[i]);
		add(box);
		add(form);
	}

	// -------------------------------------------------------------//
	// Accion asociada al fichero de entrenamiento //
	// -------------------------------------------------------------//

	private void actionTrainingFile() {
		File root = config.trainingfile;
		if (root == null)
			root = xfsl.getWorkingDirectory();
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy training files (.trn)", "trn");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Training File");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		config.trainingfile = chooser.getSelectedFile();
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada al fichero de test //
	// -------------------------------------------------------------//

	private void actionTestFile() {
		File root = config.testfile;
		if (root == null)
			root = xfsl.getWorkingDirectory();
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Test File");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		config.testfile = chooser.getSelectedFile();
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada al fichero de log //
	// -------------------------------------------------------------//

	private void actionLogFile() {
		XfslLog working = (XfslLog) config.logfile.clone();
		XfslLogDialog dialog = new XfslLogDialog(xfsl, working);
		dialog.setVisible(true);
		if (dialog.isSelected())
			config.logfile = working;
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada al fichero de preprocesado //
	// -------------------------------------------------------------//

	private void actionPreprocessing() {
		XfspProcess working = (XfspProcess) config.preprocessing.clone();
		XfspDialog dialog = new XfspDialog(xfsl, working);
		dialog.setVisible(true);
		if (dialog.isSelected())
			config.preprocessing = working;
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada al fichero de postprocesado //
	// -------------------------------------------------------------//

	private void actionPostprocessing() {
		XfspProcess working = (XfspProcess) config.postprocessing.clone();
		XfspDialog dialog = new XfspDialog(xfsl, working);
		dialog.setVisible(true);
		if (dialog.isSelected())
			config.postprocessing = working;
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada a la condicion de termino //
	// -------------------------------------------------------------//

	private void actionEndCondition() {
		XfslEndCondition working = (XfslEndCondition) config.endcondition
		.clone();
		XfslEndDialog dialog = new XfslEndDialog(xfsl, working);
		dialog.setVisible(true);
		if (dialog.isSelected())
			config.endcondition = working;
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada a la seleccion de parametros //
	// -------------------------------------------------------------//

	private void actionSettings() {
		XfslSettingsDialog dialog = new XfslSettingsDialog(xfsl);
		dialog.setVisible(true);
		set();
	}

	// -------------------------------------------------------------//
	// Accion asociada al menu de la funcion de error //
	// -------------------------------------------------------------//

	private void actionErrorFunction() {
		String errorfunctionname[] = { "Mean Square Error",
				"Weighted Mean Square Error", "Mean Absolute Error",
				"Weighted Mean Absolute Error", "Classification Error",
				"Advanced Classification Error", "Classification Square Error" };

		JPopupMenu menu = new JPopupMenu();
		SystemModule system = xfsl.getSpec().getSystemModule();
		boolean weights = (system != null && system.getOutputs().length > 1);
		for (int i = 0; i < errorfunctionname.length; i++)
			if ((i != XfslErrorFunction.W_MEAN_SQUARE_ERROR && i != XfslErrorFunction.W_MEAN_ABS_ERROR)
					|| weights) {
				JMenuItem item = new JMenuItem(errorfunctionname[i]);
				item.setFont(XConstants.font);
				item.addActionListener(this);
				item.setActionCommand("ERR" + i);
				menu.add(item);
			}
		menu.show(text[6], 0, text[6].getHeight());
	}

	// -------------------------------------------------------------//
	// Accion asociada a la funcion de error //
	// -------------------------------------------------------------//

	private void actionErrorFunction(String number) {
		int index = Integer.parseInt(number);
		int code = config.errorfunction.getCode();
		XfslErrorFunction working;
		if (index == code)
			working = (XfslErrorFunction) config.errorfunction.clone();
		else
			try {
				working = new XfslErrorFunction(index);
			} catch (XflException ex) {
				return;
			}

			if (working.isWeighted()) {
				Variable output[] = xfsl.getSpec().getSystemModule().getOutputs();
				XfslErrorDialog dialog = new XfslErrorDialog(xfsl, output);
				if (dialog.show(working.getWeights())) {
					try {
						working.setWeights(dialog.getWeights());
					} catch (Exception ex) {
					}
					config.errorfunction = working;
					set();
				}
			} else {
				config.errorfunction = working;
				set();
			}
	}

	// -------------------------------------------------------------//
	// Accion asociada al menu de algoritmo //
	// -------------------------------------------------------------//

	private void actionAlgorithm() {
		JPopupMenu menu = new JPopupMenu();
		JMenu[] fam = new JMenu[XfslAlgorithm.famname.length];
		for (int i = 0; i < fam.length; i++) {
			fam[i] = new JMenu(XfslAlgorithm.famname[i]);
			fam[i].setFont(XConstants.font);
			for (int j = 0; j < XfslAlgorithm.famcode[i].length; j++) {
				String label = "";
				try {
					label = XfslAlgorithm.getName(XfslAlgorithm.famcode[i][j]);
				} catch (Exception ex) {
				}
				JMenuItem item = new JMenuItem(label);
				item.setFont(XConstants.font);
				item.addActionListener(this);
				item.setActionCommand("ALG" + XfslAlgorithm.famcode[i][j]);
				fam[i].add(item);
			}
			menu.add(fam[i]);
		}
		menu.show(text[3], 0, text[3].getHeight());
	}

	// -------------------------------------------------------------//
	// Accion asociada a la seleccion de algoritmo //
	// -------------------------------------------------------------//

	private void actionAlgorithm(String number) {
		int index = Integer.parseInt(number);
		XfslAlgorithm working;
		if (config.algorithm != null && index == config.algorithm.getCode())
			working = (XfslAlgorithm) config.algorithm.clone();
		else
			try {
				working = XfslAlgorithm.create(index);
			} catch (XflException ex) {
				return;
			}

			XfslAlgorithmDialog dialog = new XfslAlgorithmDialog(xfsl, working);
			dialog.setVisible(true);
			if (dialog.isSelected()) {
				config.algorithm = working;
				set();
			}
	}

	// -------------------------------------------------------------//
	// Accion asociada a la carga del fichero de configuracion //
	// -------------------------------------------------------------//

	private void actionLoadConfig() {
		File wdir = xfsl.getWorkingDirectory();
		JFileChooser chooser = new JFileChooser(wdir);
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
		XfslConfig xfslc = xfcparser.parseXfslConfig(file);
		if (xfslc != null) {
			config = xfslc;
			xfsl.setConfig(config);
			set();
		} else {
			xfsl.log(xfcparser.resume());
			Toolkit.getDefaultToolkit().beep();
		}
	}

	// -------------------------------------------------------------//
	// Accion asociada a la grabacion del fichero de configuracion //
	// -------------------------------------------------------------//

	private void actionSaveConfig() {
		File wdir = xfsl.getWorkingDirectory();
		JFileChooser chooser = new JFileChooser(wdir);
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
}
