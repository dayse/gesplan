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
//VENTANA DEL APRENDIZAJE SUPERVISADO		//
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

public class Xfsl extends JFrame implements ActionListener, WindowListener {

	/**
	 * Codigo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603063L;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MIEMBROS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	private Specification original;
	private Specification workingcopy;
	private int status = XfslStatus.UNCONFIGURED;
	private Xfuzzy xfuzzy;
	private XfslConfig config;
	private XfslConfigPanel configpanel;
	private XfslStatusPanel statuspanel;
	private XfslGraphPanel graphpanel;
	private XCommandForm form;
	private XfslProcess process;
	private boolean stop = false;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTORES //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public Xfsl(Xfuzzy xfuzzy, Specification spec) {
		super("Xfsl");
		this.xfuzzy = xfuzzy;
		this.original = spec;
		this.workingcopy = xfuzzy.duplicate(spec);
		build();
	}

	public Xfsl(Specification spec) {
		super("Xfsl");
		this.xfuzzy = null;
		this.original = spec;
		this.workingcopy = spec;
		build();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PUBLICOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Obtiene la especificacion del sistema //
	// -------------------------------------------------------------//

	public Specification getSpec() {
		return this.workingcopy;
	}

	// -------------------------------------------------------------//
	// Obtiene la configuracion del aprendizaje //
	// -------------------------------------------------------------//

	public XfslConfig getConfig() {
		return this.config;
	}

	// -------------------------------------------------------------//
	// Actualiza la configuracion del aprendizaje //
	// -------------------------------------------------------------//

	public void setConfig(XfslConfig config) {
		this.config = config;
	}

	// -------------------------------------------------------------//
	// Actualiza el estado ante un cambio de configuracion //
	// -------------------------------------------------------------//

	public void setStatus() {
		if (this.status == XfslStatus.UNCONFIGURED
				&& this.config.isReadyToRun()) {
			this.status = XfslStatus.READY_TO_RUN;
			this.statuspanel.set(XfslStatus.READY_TO_RUN);
		}
		if (this.status == XfslStatus.READY_TO_RUN
				&& !this.config.isReadyToRun()) {
			this.status = XfslStatus.UNCONFIGURED;
			this.statuspanel.set(XfslStatus.UNCONFIGURED);
		}
		if (this.config.isReadyToRun())
			setSensitive(XfslStatus.READY_TO_RUN);
		if (!this.config.isReadyToRun())
			setSensitive(XfslStatus.UNCONFIGURED);
		this.statuspanel
		.setTitles(this.config.errorfunction.isClassification());
		repaint();
	}

	// -------------------------------------------------------------//
	// Directorio de trabajo //
	// -------------------------------------------------------------//

	public File getWorkingDirectory() {
		return xfuzzy.getWorkingDirectory();
	}

	// -------------------------------------------------------------//
	// Mostrar mensajes //
	// -------------------------------------------------------------//

	public void log(String msg) {
		xfuzzy.log(msg);
	}

	// -------------------------------------------------------------//
	// Interfaz ActionListener //
	// -------------------------------------------------------------//

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Close"))
			actionClose();
		else if (command.equals("Save"))
			actionSave();
		else if (command.equals("Apply"))
			actionApply();
		else if (command.equals("Reload"))
			actionReload();
		else if (command.equals("Run"))
			actionRun();
	}

	// -------------------------------------------------------------//
	// Interfaz WindowListener //
	// -------------------------------------------------------------//

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		actionClose();
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

	// -------------------------------------------------------------//
	// Accion asociada al boton Run //
	// -------------------------------------------------------------//

	private void actionRun() {
		if (process == null || !process.isAlive()) {
			graphpanel.reset();
			process = new XfslProcess(this);
			this.stop = true;
			return;
		}
		if (process != null && process.isAlive() && stop) {
			process.sendStop();
			setSensitive(XfslStatus.STOPPING);
			this.stop = false;
			return;
		}
		if (process != null && process.isAlive() && !stop) {
			process.sendContinue();
			setSensitive(XfslStatus.LEARNING);
			this.stop = true;
			return;
		}
	}

	// -------------------------------------------------------------//
	// Accion asociada al boton Reload //
	// -------------------------------------------------------------//

	private void actionReload() {
		if (process != null) {
			if (process.isAlive()) {
				process.sendFinish();
				try {
					process.join();
				} catch (InterruptedException ex) {
				}
			}
			process = null;
		}
		statuspanel.reset(config.isReadyToRun());
		graphpanel.reset();
		if (config.algorithm != null)
			config.algorithm.reinit();
		workingcopy = xfuzzy.duplicate(original);
	}

	// -------------------------------------------------------------//
	// Accion asociada al boton Apply //
	// -------------------------------------------------------------//

	private void actionApply() {
		if (status == XfslStatus.LEARNING)
			return;
		Specification dup = xfuzzy.duplicate(workingcopy);
		original.setOperatorsets(dup.getOperatorsets());
		original.setTypes(dup.getTypes());
		original.setRulebases(dup.getRulebases());
		original.setSystemModule(dup.getSystemModule());
		original.setModified(true);
	}

	// -------------------------------------------------------------//
	// Accion asociada al boton Save //
	// -------------------------------------------------------------//

	private void actionSave() {
		if (status == XfslStatus.LEARNING)
			return;
		File root = original.getFile();
		if (root == null)
			root = xfuzzy.getWorkingDirectory();
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy system files (.xfl)", "xfl");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(root);
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
		workingcopy.save_as(file);
		xfuzzy.load(file);
	}

	// -------------------------------------------------------------//
	// Accion asociada al boton Close //
	// -------------------------------------------------------------//

	private void actionClose() {
		if (process != null && process.isAlive()) {
			process.sendFinish();
			try {
				process.join();
			} catch (InterruptedException ex) {
			}
		}
		process = null;
		setVisible(false);
		if (xfuzzy == null)
			System.exit(0);
	}

	// -------------------------------------------------------------//
	// Construye la ventana principal //
	// -------------------------------------------------------------//

	private void build() {
		String label[] = { "Run", "Reload", "Apply", "Save", "Close" };

		this.form = new XCommandForm(label, label, this);
		this.form.setCommandWidth(120);
		this.form.block();
		this.config = new XfslConfig();
		this.configpanel = new XfslConfigPanel(this);
		this.statuspanel = new XfslStatusPanel();
		this.graphpanel = new XfslGraphPanel(this);

		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
		box.add(configpanel);
		box.add(statuspanel);
		Dimension maxsize = box.getMaximumSize();
		Dimension prefsize = box.getPreferredSize();
		box.setMaximumSize(new Dimension(maxsize.width, prefsize.height));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel("Supervised Learning for " + original));
		content.add(box);
		content.add(graphpanel);
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		addWindowListener(this);
		pack();
		setLocation();

		this.configpanel.set();
	}

	// -------------------------------------------------------------//
	// Actualiza el estado del aprendizaje //
	// -------------------------------------------------------------//

	public void setStatus(XfslStatus slstat) {
		if (this.status != slstat.status)
			setSensitive(slstat.status);
		this.status = slstat.status;
		this.statuspanel.set(slstat);
		this.graphpanel.addStatus(slstat);
	}

	// -------------------------------------------------------------//
	// (Des)Habilita los botones de la barra de comandos //
	// -------------------------------------------------------------//

	private void setSensitive(int status) {
		if (status == XfslStatus.UNCONFIGURED) {
			form.setLabel(0, "Run");
			form.setEnabled(0, false);
			form.setEnabled(1, true);
			form.setEnabled(2, true);
			form.setEnabled(3, true);
			form.setEnabled(4, true);
			configpanel.setEnabled(true);
			return;
		}
		if (status == XfslStatus.READY_TO_RUN) {
			form.setLabel(0, "Run");
			form.setEnabled(0, true);
			form.setEnabled(1, true);
			form.setEnabled(2, true);
			form.setEnabled(3, true);
			form.setEnabled(4, true);
			configpanel.setEnabled(true);
			return;
		}
		if (status == XfslStatus.LEARNING) {
			form.setLabel(0, "Stop");
			form.setEnabled(0, true);
			form.setEnabled(1, false);
			form.setEnabled(2, false);
			form.setEnabled(3, false);
			form.setEnabled(4, false);
			configpanel.setEnabled(false);
			return;
		}
		if (status == XfslStatus.STOPPING) {
			form.setLabel(0, "Stopping");
			form.setEnabled(0, false);
			form.setEnabled(1, false);
			form.setEnabled(2, false);
			form.setEnabled(3, false);
			form.setEnabled(4, false);
			configpanel.setEnabled(false);
			return;
		}
		if (status == XfslStatus.STOPPED) {
			form.setLabel(0, "Continue");
			form.setEnabled(0, true);
			form.setEnabled(1, true);
			form.setEnabled(2, true);
			form.setEnabled(3, true);
			form.setEnabled(4, true);
			configpanel.setEnabled(true);
			return;
		}
		if (status == XfslStatus.FINISHED) {
			form.setLabel(0, "Run");
			form.setEnabled(0, true);
			form.setEnabled(1, true);
			form.setEnabled(2, true);
			form.setEnabled(3, true);
			form.setEnabled(4, true);
			configpanel.setEnabled(true);
			return;
		}
	}
	
	/**
	 * Método que indica en qué localización de la pantalla hay que dibujar la
	 * ventana
	 */
	private void setLocation() {
		if (xfuzzy != null) {
			Point loc = xfuzzy.frame.getLocationOnScreen();
			loc.x += 95;
			loc.y += 45;
			setLocation(loc);
		} else {
			Dimension frame = getSize();
			Dimension screen = getToolkit().getScreenSize();
			setLocation((screen.width - frame.width) / 2,
					(screen.height - frame.height) / 2);
		}
	}

}
