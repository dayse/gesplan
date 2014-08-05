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

package xfuzzy.xfsim;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Ventana principal de la aplicación "xfsim" de simulación de sistemas difusos.
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class Xfsim extends JFrame implements ActionListener, WindowListener,
MouseListener, KeyListener {

	// ----------------------------------------------------------------------------//
	// COSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603056L;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Ventana principal del entorno
	 */
	private Xfuzzy xfuzzy;

	/**
	 * Sistema difuso a simular
	 */
	private Specification spec;

	/**
	 * Hilo del proceso de simulación
	 */
	private XfsimProcess process;

	/**
	 * Configuración del proceso de simulación
	 */
	private XfsimConfig config;

	/**
	 * Estado de la simulación
	 */
	private int running = 0;

	/**
	 * Campos de texto para configurar el modelo de planta
	 */
	private XTextForm modelform[];

	/**
	 * Campos para mostrar la evolución de la simulación (iteraciones y tiempo)
	 */
	private XTextForm evolform[];

	/**
	 * Campos para mostrar la salida de la planta en cada instante
	 */
	private XTextForm plantform[];

	/**
	 * Campos para mostrar la salida del sistema difuso en cada instante
	 */
	private XTextForm fuzzyform[];

	/**
	 * Barra de botones
	 */
	private XCommandForm commandform;

	/**
	 * Lista de salidas a generar
	 */
	private XList outputlist;

	/**
	 * Menú contextual asociado a las salidas
	 */
	private JPopupMenu popup;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfsim(Xfuzzy xfuzzy, Specification spec) {
		super("Xfsim");
		this.xfuzzy = xfuzzy;
		this.spec = spec;
		this.config = new XfsimConfig(this);
		build();
		refreshConfig();
	}

	// ----------------------------------------------------------------------------//
	// EJECUCIÓN EXTERNA //
	// ----------------------------------------------------------------------------//

	/**
	 * Ejecución externa
	 */
	public static void main(String args[]) {
		if (args.length != 1 && args.length != 2) {
			System.out.println("Usage: xfsim xflfile [cfgfile]");
			return;
		}
		File file = new File(args[0]);
		if (!file.exists()) {
			System.out.println("Can't find file " + file.getAbsolutePath());
			System.exit(-1);
		}

		XflParser parser = new XflParser();
		Specification spec = parser.parse(file.getAbsolutePath());
		if (spec == null) {
			System.out.println(parser.resume());
			return;
		}

		Xfsim simulator = new Xfsim(null, spec);

		if (args.length == 2) {
			File cfgfile = new File(args[1]);
			if (!cfgfile.exists()) {
				System.out.println("Can't find file "
						+ cfgfile.getAbsolutePath());
			} else {
				XfuzzyConfig xfcparser = new XfuzzyConfig();
				if (!xfcparser.parseXfsimConfig(simulator, cfgfile))
					System.out.println(xfcparser.resume());
				simulator.refreshConfig();
			}
		}
		simulator.setVisible(true);
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Devuelve la referencia al sistema difuso a simular
	 */
	public Specification getSpecification() {
		return this.spec;
	}

	/**
	 * Devuelve la configuración del proceso de simulación
	 */
	public XfsimConfig getConfiguration() {
		return this.config;
	}

	/**
	 * Devuelve el estado de ejecución de la simulación
	 */
	public int getStatus() {
		return this.running;
	}

	/**
	 * Verifica si se está realizando un proceso de simulación
	 */
	public boolean isSimulating() {
		return (process != null && process.isAlive());
	}

	/**
	 * Coloca la ventana en la posición deseada sobre la pantalla
	 */
	public void setLocation() {
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

	/**
	 * Actualiza los valores de la zona de configuración
	 */
	public void refreshConfig() {
		if (config.plant != null)
			modelform[0].setText(config.plant.getClass().getName());
		else
			modelform[0].setText("");

		modelform[1].setText(config.limit.toString());

		if (config.init == null)
			modelform[2].setText("default");
		else
			modelform[2].setText("configured");

		outputlist.setListData(config.output);

		commandform.setEnabled(2, config.isConfigured());
		repaint();
	}

	/**
	 * Actualiza los valores de los campos de estado
	 */
	public void refreshStatus(double iter, double time, double[] fzst,
			double[] ptst) {
		evolform[0].setText(redondeo(iter));
		evolform[1].setText(redondeo(time) + " ms");
		for (int i = 0; i < fzst.length; i++)
			fuzzyform[i].setText(redondeo(fzst[i]));
		for (int i = 0; i < ptst.length; i++)
			plantform[i].setText(redondeo(ptst[i]));
	}

	/**
	 * Actualiza el estado de la barra de comandos
	 */
	public void setSensitive(int kind) {
		switch (kind) {
		case 0:
			commandform.setLabel(2, "Run");
			commandform.setEnabled(0, true);
			commandform.setEnabled(1, true);
			commandform.setEnabled(2, config.isConfigured());
			commandform.setEnabled(3, true);
			commandform.setEnabled(4, true);
			running = 0;
			return;
		case 1:
			commandform.setLabel(2, "Stop");
			commandform.setEnabled(0, false);
			commandform.setEnabled(1, false);
			commandform.setEnabled(3, false);
			commandform.setEnabled(4, false);
			running = 1;
			return;
		case 2:
			commandform.setLabel(2, "Continue");
			commandform.setEnabled(0, true);
			commandform.setEnabled(1, true);
			commandform.setEnabled(3, true);
			commandform.setEnabled(4, true);
			running = 2;
			return;
		}
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Load"))
			load();
		else if (command.equals("Save"))
			save();
		else if (command.equals("Run"))
			run();
		else if (command.equals("Reload"))
			reload();
		else if (command.equals("Close"))
			close();
		else if (command.equals("Plant"))
			plant();
		else if (command.equals("Init"))
			init();
		else if (command.equals("Limit"))
			limit();
		else if (command.equals("InsertLog"))
			insertLogfile();
		else if (command.equals("InsertPlot"))
			insertPlot();
		else if (command.equals("Edit"))
			editOutput();
		else if (command.equals("Remove"))
			removeOutput();
	}

	/**
	 * Interfaz KeyListener. Acción al soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_BACK_SPACE)
			removeOutput();
		if (code == KeyEvent.VK_DELETE)
			removeOutput();
		if (code == KeyEvent.VK_CUT)
			removeOutput();
		if (code == KeyEvent.VK_INSERT)
			insertOutput();
		if (code == KeyEvent.VK_ENTER)
			editOutput();
		if (code == 226)
			editOutput();
		e.consume();
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
		if (e.getClickCount() == 2)
			editOutput();
	}

	/**
	 * Interfaz MouseListener. Acción al apretar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		popup.show((Component) e.getSource(), e.getX(), e.getY());
	}

	/**
	 * Interfaz MouseListener. Acción al soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;
		popup.show((Component) e.getSource(), e.getX(), e.getY());
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
	 * Crea la ventana
	 */
	private void build() {
		String olabel[] = { "Insert new log file", "Insert new plot",
				"Edit log file/plot", "Remove log file/plot" };
		String ocommand[] = { "InsertLog", "InsertPlot", "Edit", "Remove" };
		popup = new JPopupMenu();
		JMenuItem item[] = new JMenuItem[ocommand.length];
		for (int i = 0; i < ocommand.length; i++) {
			item[i] = new JMenuItem(olabel[i]);
			item[i].setActionCommand(ocommand[i]);
			item[i].addActionListener(this);
			item[i].setFont(XConstants.font);
			popup.add(item[i]);
		}

		String modellb[] = { "Plant model", "Simulation limit",
		"Initial values" };
		String modelcm[] = { "Plant", "Limit", "Init" };

		modelform = new XTextForm[3];
		for (int i = 0; i < 3; i++) {
			modelform[i] = new XTextForm(modellb[i], this);
			modelform[i].setActionCommand(modelcm[i]);
			modelform[i].setEditable(false);
			modelform[i].setFieldWidth(200);
		}
		XTextForm.setWidth(modelform);

		outputlist = new XList("Simulation output");
		outputlist.addMouseListener(this);
		outputlist.addKeyListener(this);

		Box modelbox = new Box(BoxLayout.Y_AXIS);
		modelbox.add(new XLabel("Simulation model"));
		for (int i = 0; i < modelform.length; i++)
			modelbox.add(modelform[i]);
		modelbox.add(Box.createVerticalStrut(5));
		modelbox.add(outputlist);

		String evollb[] = { "Iteration (_n)", "Time (_t)" };
		evolform = new XTextForm[2];
		for (int i = 0; i < evolform.length; i++) {
			evolform[i] = new XTextForm(evollb[i]);
			evolform[i].setEditable(false);
			evolform[i].setFieldWidth(200);
		}

		Variable input[] = spec.getSystemModule().getInputs();
		plantform = new XTextForm[input.length];
		for (int i = 0; i < plantform.length; i++) {
			plantform[i] = new XTextForm(input[i].getName());
			plantform[i].setEditable(false);
			plantform[i].setFieldWidth(200);
		}

		Variable output[] = spec.getSystemModule().getOutputs();
		fuzzyform = new XTextForm[output.length];
		for (int i = 0; i < fuzzyform.length; i++) {
			fuzzyform[i] = new XTextForm(output[i].getName());
			fuzzyform[i].setEditable(false);
			fuzzyform[i].setFieldWidth(200);
		}

		int evollength = evolform.length + plantform.length + fuzzyform.length;
		XTextForm tform[] = new XTextForm[evollength];
		for (int i = 0; i < evolform.length; i++)
			tform[i] = evolform[i];
		for (int i = 0; i < plantform.length; i++)
			tform[evolform.length + i] = plantform[i];
		for (int i = 0; i < fuzzyform.length; i++)
			tform[evolform.length + plantform.length + i] = fuzzyform[i];
		XTextForm.setWidth(tform);

		Box evolbox = new Box(BoxLayout.Y_AXIS);
		evolbox.add(new XLabel("Evolution"));
		for (int i = 0; i < evolform.length; i++)
			evolbox.add(evolform[i]);
		evolbox.add(new XLabel("Plant state"));
		for (int i = 0; i < plantform.length; i++)
			evolbox.add(plantform[i]);
		evolbox.add(new XLabel("Fuzzy system output"));
		for (int i = 0; i < fuzzyform.length; i++)
			evolbox.add(fuzzyform[i]);
		evolbox.add(Box.createVerticalGlue());

		Box body = new Box(BoxLayout.X_AXIS);
		body.add(modelbox);
		body.add(Box.createHorizontalStrut(5));
		body.add(evolbox);

		String formlb[] = { "Load", "Save", "Run", "Reload", "Close" };
		commandform = new XCommandForm(formlb, formlb, this);
		commandform.setCommandWidth(120);
		commandform.block();

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content
		.add(new XLabel("Simulation for specification "
				+ spec.getName()));
		content.add(body);
		content.add(commandform);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		addWindowListener(this);
		pack();
		setLocation();
	}

	/**
	 * Devuelve la cadena que representa al número con 6 digitos
	 */
	private String redondeo(double dd) {
		String data = "" + dd;
		char[] cdat = data.toCharArray();
		int i;
		for (i = 0; i < cdat.length; i++)
			if (cdat[i] > '0' && cdat[i] <= '9')
				break;
		for (int j = 0; j < 6 && i < cdat.length; j++, i++)
			if (cdat[i] == 'e' || cdat[i] == 'E')
				break;
		StringBuffer buf = new StringBuffer(data.substring(0, i));
		int e = data.indexOf("E");
		if (e == -1)
			e = data.indexOf("e");
		if (e != -1)
			buf.append(data.substring(e));
		return buf.toString();
	}

	/**
	 * Selecciona la clase que desarrolla el modelo de planta
	 */
	private void plant() {
		File wdir = null;
		if (config.plantfile != null)
			wdir = config.plantfile;
		else if (xfuzzy != null)
			wdir = xfuzzy.getWorkingDirectory();
		else
			wdir = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Java binary files (.class)", "class");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select plant model");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();
		try {
			config.setPlantModel(file);
			refreshConfig();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	/**
	 * Selecciona los valores iniciales
	 */
	private void init() {
		XfsimInitDialog dialog = new XfsimInitDialog(this);
		dialog.setVisible(true);
		refreshConfig();
	}

	/**
	 * Selecciona las condiciones de termino
	 */
	private void limit() {
		XfsimLimitDialog dialog = new XfsimLimitDialog(this);
		dialog.setVisible(true);
		refreshConfig();
	}

	/**
	 * Añade una salida al proceso de simulación
	 */
	private void insertOutput() {
		XfsimOutputDialog dialog = new XfsimOutputDialog(this);
		dialog.setVisible(true);
	}

	/**
	 * Crea un nuevo fichero de almacenamiento histórico
	 */
	private void insertLogfile() {
		XfsimLog logfile = new XfsimLog(this);
		XfsimLogDialog dialog = new XfsimLogDialog(this, logfile);
		dialog.setVisible(true);
		if (dialog.getResult()) {
			config.output.add(logfile);
			refreshConfig();
		}
	}

	/**
	 * Crea una nueva representación gráfica
	 */
	private void insertPlot() {
		XfsimPlot plot = new XfsimPlot(this);
		XfsimPlotDialog dialog = new XfsimPlotDialog(this, plot);
		dialog.setVisible(true);
		if (dialog.getResult()) {
			config.output.add(plot);
			refreshConfig();
		}
	}

	/**
	 * Elimina una salida del proceso de simulación
	 */
	private void removeOutput() {
		try {
			XfsimLog logfile = (XfsimLog) outputlist.getSelectedValue();
			config.output.remove(logfile);
			refreshConfig();
		} catch (Exception ex) {
		}

		try {
			XfsimPlot plot = (XfsimPlot) outputlist.getSelectedValue();
			config.output.remove(plot);
			refreshConfig();
		} catch (Exception ex) {
		}
	}

	/**
	 * Edita una salida del proceso de simulación
	 */
	private void editOutput() {
		try {
			XfsimLog logfile = (XfsimLog) outputlist.getSelectedValue();
			XfsimLog tmp = new XfsimLog(this);
			tmp.setFile(logfile.getFile());
			tmp.setSelection(logfile.getSelection());
			XfsimLogDialog dialog = new XfsimLogDialog(this, tmp);
			dialog.setVisible(true);
			if (!dialog.getResult())
				return;
			logfile.setFile(tmp.getFile());
			logfile.setSelection(tmp.getSelection());
			refreshConfig();
		} catch (Exception ex) {
		}

		try {
			XfsimPlot plot = (XfsimPlot) outputlist.getSelectedValue();
			XfsimPlot tmp = new XfsimPlot(this);
			tmp.setVar(plot.getXvar(), plot.getYvar(), plot.getKind());
			XfsimPlotDialog dialog = new XfsimPlotDialog(this, tmp);
			dialog.setVisible(true);
			if (!dialog.getResult())
				return;
			plot.setVar(tmp.getXvar(), tmp.getYvar(), tmp.getKind());
			refreshConfig();
		} catch (Exception ex) {
		}
	}

	/**
	 * Carga la configuración desde un fichero externo
	 */
	private void load() {
		File wdir;
		if (xfuzzy != null)
			wdir = xfuzzy.getWorkingDirectory();
		else
			wdir = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy configuration files (.cfg)", "cfg");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load Configuration");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		XfsimConfig old = this.config;
		this.config = new XfsimConfig(this);
		File file = chooser.getSelectedFile();
		XfuzzyConfig xfcparser = new XfuzzyConfig();
		if (xfcparser.parseXfsimConfig(this, file))
			refreshConfig();
		else {
			this.config = old;
			if (xfuzzy != null)
				xfuzzy.log(xfcparser.resume());
			else
				System.out.println(xfcparser.resume());
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/**
	 * Guarda la configuración en un fichero externo
	 */
	private void save() {
		File wdir;
		if (xfuzzy != null)
			wdir = xfuzzy.getWorkingDirectory();
		else
			wdir = new File(System.getProperty("user.dir"));
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

	/**
	 * Ejecuta el proceso de simulacion
	 */
	private void run() {
		if (running == 0) {
			running = 1;
			setSensitive(1);
			config.close();
			if (process == null || !process.isAlive())
				process = new XfsimProcess(this);
		} else if (running == 2) {
			running = 1;
			setSensitive(1);
		} else {
			running = 2;
			setSensitive(2);
		}
	}

	/**
	 * Destruye el proceso de simulación e inicializa los campos
	 */
	private void reload() {
		this.running = 3;
		if (process != null && process.isAlive()) {
			try {
				process.join();
			} catch (InterruptedException ex) {
			}
		}
		this.process = null;
		this.config.close();
		for (int i = 0; i < evolform.length; i++)
			evolform[i].setText("");
		for (int i = 0; i < plantform.length; i++)
			plantform[i].setText("");
		for (int i = 0; i < fuzzyform.length; i++)
			fuzzyform[i].setText("");
		refreshConfig();
		setSensitive(0);
	}

	/**
	 * Cierra la ventana de simulación
	 */
	private void close() {
		if (process != null && process.isAlive()) {
			running = 3;
			try {
				process.join();
			} catch (InterruptedException ex) {
			}
		}
		this.config.close();
		if (xfuzzy == null)
			System.exit(0);
		else
			setVisible(false);
	}
}
