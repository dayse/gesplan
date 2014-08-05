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


package xfuzzy.xfplot;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

/**
 * Representación gráfica del sistema difuso
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class Xfplot extends JFrame implements ActionListener, ChangeListener,
WindowListener {

	// ----------------------------------------------------------------------------//
	// CONSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603052L;

	/**
	 * Código para indicar representaciones en 3D
	 */
	private static final int MODE_3D = 0;

	/**
	 * Código para indicar representaciones en 2D
	 */
	private static final int MODE_2D = 1;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Referencia a la ventana principal del entorno
	 */
	private Xfuzzy xfuzzy;

	/**
	 * Sistema difuso a representar
	 */
	private Specification spec;

	/**
	 * Valores a representar en 3D
	 */
	private double function3D[][];

	/**
	 * Indicador de que los valores en 3D se han calculado
	 */
	private boolean computed3D;

	/**
	 * Valores a representar en 2D
	 */
	private double function2D[];

	/**
	 * Modo de representación (2D o 3D)
	 */
	private int plotmode;

	/**
	 * Configuración de la aplicación
	 */
	private XfplotConfig config;

	/**
	 * Objeto que almacena el modelo de colores
	 */
	private XfplotColorModel colormodel;

	/**
	 * Objeto que desarrolla la representación 3D
	 */
	private Xfplot3DPanel graph3D;

	/**
	 * Objeto que desarrolla la representación 2D
	 */
	private Xfplot2DPanel graph2D;

	/**
	 * Desplazador horizontal
	 */
	private JSlider hslider;

	/**
	 * Desplazador vertical
	 */
	private JSlider vslider;

	/**
	 * Panel de la representación 3D
	 */
	private JPanel panel3D;

	/**
	 * Campo de texto para introducir el número de puntos por eje
	 */
	private JTextField samplestext;

	/**
	 * Selector de la variable X
	 */
	private JComboBox xcombo;

	/**
	 * Selector de la variable Y
	 */
	private JComboBox ycombo;

	/**
	 * Selector de la variable Z
	 */
	private JComboBox zcombo;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfplot(Xfuzzy xfuzzy, Specification spec) {
		super("Xfplot");
		this.xfuzzy = xfuzzy;
		this.spec = spec;
		this.computed3D = false;
		this.config = new XfplotConfig(spec);
		this.colormodel = new XfplotColorModel(0);

		int numinputs = spec.getSystemModule().getInputs().length;
		if (numinputs > 1) {
			this.plotmode = MODE_3D;
			try {
				config.setPlotMode(MODE_3D);
			} catch (Exception ex) {
			}
		} else {
			this.plotmode = MODE_2D;
			try {
				config.setPlotMode(MODE_2D);
			} catch (Exception ex) {
			}
		}

		build();
		setConfig();
		actualize();
	}

	// ----------------------------------------------------------------------------//
	// EJECUCIÓN EXTERNA //
	// ----------------------------------------------------------------------------//

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: xfplot xflfile");
			return;
		}
		File file = new File(args[0]);
		if (!file.exists()) {
			System.out.println("Can't find file " + file.getAbsolutePath());
			return;
		}

		XflParser parser = new XflParser();
		Specification spec = parser.parse(file.getAbsolutePath());
		if (spec == null) {
			System.out.println(parser.resume());
			return;
		}

		Xfplot plot = new Xfplot(null, spec);
		plot.setVisible(true);
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Devuelve la variable a representar en el eje X
	 */
	public Variable getXVariable() {
		Variable inputvar[] = spec.getSystemModule().getInputs();
		return inputvar[config.getXIndex()];
	}

	/**
	 * Devuelve la variable a representar en el eje Y
	 */
	public Variable getYVariable() {
		Variable inputvar[] = spec.getSystemModule().getInputs();
		return inputvar[config.getYIndex()];
	}

	/**
	 * Devuelve la variable a representar en el eje Z
	 */
	public Variable getZVariable() {
		Variable outputvar[] = spec.getSystemModule().getOutputs();
		return outputvar[config.getZIndex()];
	}

	/**
	 * Devuelve las variables de entrada
	 */
	public Variable[] getInputVariables() {
		return spec.getSystemModule().getInputs();
	}

	/**
	 * Devuelve el valor de las variables de entrada
	 */
	public double[] getInputValues() {
		return this.config.getInputValues();
	}

	/**
	 * Devuelve los valores para representar la funcion en 2D
	 */
	public double[] get2DFunction() {
		return this.function2D;
	}

	/**
	 * Devuelve los valores para representar la funcion en 3D
	 */
	public double[][] getFunction() {
		return this.function3D;
	}

	/**
	 * Verifica que los valores de la funcion se han calculado
	 */
	public boolean is3DComputed() {
		return computed3D;
	}

	/**
	 * Devuelve el color del grid (nulo si no debe pintarse)
	 */
	public Color getGridColor() {
		return this.colormodel.getGridColor();
	}

	/**
	 * Devuelve el color asociado a un cierto valor
	 */
	public Color getColor(double value) {
		return this.colormodel.getColor(value);
	}

	/**
	 * Asigna el valor de las variables de entrada
	 */
	public boolean setInputValues(double value[]) {
		try {
			this.config.setInputValues(value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Plot"))
			actualize();
		else if (command.equals("Actualize"))
			actualize();
		else if (command.equals("XAxis"))
			changeXAxis();
		else if (command.equals("YAxis"))
			changeYAxis();
		else if (command.equals("ZAxis"))
			changeZAxis();
		else if (command.equals("Samples"))
			actualize();
		else if (command.startsWith("CM"))
			changeColor(command);
		else if (command.equals("InputValues"))
			changeInputValues();
		else if (command.equals("Close"))
			close();
		else if (command.equals("SaveImage"))
			saveImage();
		else if (command.equals("SaveData"))
			saveData();
		else if (command.equals("SaveConfig"))
			saveConfig();
		else if (command.equals("LoadConfig"))
			loadConfig();
		else if (command.equals("2DPlot"))
			set2DPlotMode();
		else if (command.equals("3DPlot"))
			set3DPlotMode();
	}

	/**
	 * Interfaz ChangeListener
	 */
	public void stateChanged(ChangeEvent e) {
		int hangle = hslider.getValue();
		int vangle = vslider.getValue();
		try {
			config.setSlides(hangle, vangle);
		} catch (Exception ex) {
		}
		graph3D.setHRotation(hangle / 50.0 - 1.0);
		graph3D.setVRotation(vangle / 100.0);
		graph3D.repaint();
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
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(createPlotPanel());

		setJMenuBar(createMenubar());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		addWindowListener(this);
		pack();
		setLocation();
	}

	/**
	 * Crea la barra de menú
	 */
	private JMenuBar createMenubar() {
		Variable inputvar[] = spec.getSystemModule().getInputs();
		Variable outputvar[] = spec.getSystemModule().getOutputs();

		xcombo = createVariableList(inputvar, "XAxis");
		ycombo = createVariableList(inputvar, "YAxis");
		zcombo = createVariableList(outputvar, "ZAxis");

		samplestext = new JTextField("");
		samplestext.setBackground(XConstants.textbackground);
		samplestext.setBorder(BorderFactory.createLoweredBevelBorder());
		samplestext.addActionListener(this);
		samplestext.setActionCommand("Samples");

		Dimension prefsize = xcombo.getPreferredSize();
		Dimension samplesize = new Dimension(50, prefsize.height);
		samplestext.setPreferredSize(samplesize);
		samplestext.setMinimumSize(samplesize);
		samplestext.setMaximumSize(samplesize);

		String fileitem[] = { "Save data", null, "Actualize", null, "Close" };
		String filecomm[] = { "SaveData", "Actualize", "Close" };
		XMenu file = new XMenu("File", fileitem, filecomm, this);

		String plotitem[] = { "2-dimensional Plot", "3-dimensional Plot" };
		String plotcomm[] = { "2DPlot", "3DPlot" };
		XMenu plotmenu = new XMenu("Plot Mode", plotitem, plotcomm, this);
		if (inputvar.length < 2)
			plotmenu.setEnabled(false, 1);

		JMenu colormenu = createColorMenu();

		JMenuItem item1 = new JMenuItem("Input Values");
		item1.setFont(XConstants.font);
		item1.setActionCommand("InputValues");
		item1.addActionListener(this);
		JMenuItem item2 = new JMenuItem("Load Configuration");
		item2.setFont(XConstants.font);
		item2.setActionCommand("LoadConfig");
		item2.addActionListener(this);
		JMenuItem item3 = new JMenuItem("Save Configuration");
		item3.setFont(XConstants.font);
		item3.setActionCommand("SaveConfig");
		item3.addActionListener(this);

		JMenu conf = new JMenu("Configuration");
		conf.add(plotmenu);
		conf.add(colormenu);
		conf.add(item1);
		conf.addSeparator();
		conf.add(item2);
		conf.add(item3);

		JLabel xlabel = new JLabel("X Axis");
		JLabel ylabel = new JLabel("Y Axis");
		JLabel zlabel = new JLabel("Z Axis");
		JLabel slabel = new JLabel("Samples");
		xlabel.setForeground(Color.black);
		ylabel.setForeground(Color.black);
		zlabel.setForeground(Color.black);
		slabel.setForeground(Color.black);

		JMenuBar bar = new JMenuBar();
		bar.add(file);
		bar.add(Box.createHorizontalStrut(20));
		bar.add(conf);
		bar.add(Box.createHorizontalStrut(40));
		bar.add(Box.createHorizontalGlue());
		bar.add(xlabel);
		bar.add(Box.createHorizontalStrut(10));
		bar.add(xcombo);
		bar.add(Box.createHorizontalStrut(20));
		bar.add(ylabel);
		bar.add(Box.createHorizontalStrut(10));
		bar.add(ycombo);
		bar.add(Box.createHorizontalStrut(20));
		bar.add(zlabel);
		bar.add(Box.createHorizontalStrut(10));
		bar.add(zcombo);
		bar.add(Box.createHorizontalStrut(20));
		bar.add(slabel);
		bar.add(Box.createHorizontalStrut(10));
		bar.add(samplestext);
		bar.add(Box.createHorizontalStrut(20));
		return bar;
	}

	/**
	 * Crea del menú de modelos de colores
	 */
	private JMenu createColorMenu() {
		JMenu menu = new JMenu("Color Model");
		for (int i = 0; i < XfplotColorModel.COUNTER; i++) {
			JMenuItem item = new JMenuItem(new XfplotColorIcon(i));
			item.setActionCommand("CM" + i);
			item.addActionListener(this);
			menu.add(item);
		}
		return menu;
	}

	/**
	 * Crea una lista desplegable con las variables
	 */
	private JComboBox createVariableList(Variable[] var, String action) {
		JComboBox combo = new JComboBox();
		combo.setBackground(XConstants.textbackground);
		for (int i = 0; i < var.length; i++)
			combo.addItem(var[i]);
		combo.addActionListener(this);
		combo.setActionCommand(action);

		Dimension prefsize = combo.getPreferredSize();
		Dimension size = new Dimension(100, prefsize.height);
		combo.setPreferredSize(size);
		combo.setMinimumSize(size);
		combo.setMaximumSize(size);
		return combo;
	}

	/**
	 * Crea el panel de la representación gráfica
	 */
	private JPanel createPlotPanel() {
		this.graph2D = new Xfplot2DPanel(this);

		this.graph3D = new Xfplot3DPanel(this);
		this.hslider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		this.hslider.addChangeListener(this);
		this.vslider = new JSlider(JSlider.VERTICAL, 0, 100, 30);
		this.vslider.addChangeListener(this);
		this.vslider.setInverted(true);

		panel3D = new JPanel();
		panel3D.setLayout(new BorderLayout());
		panel3D.add(graph3D, BorderLayout.CENTER);
		panel3D.add(hslider, BorderLayout.SOUTH);
		panel3D.add(vslider, BorderLayout.EAST);

		if (plotmode == MODE_3D)
			return panel3D;
		return graph2D;
	}

	/**
	 * Coloca la ventana en la pantalla
	 */
	private void setLocation() {
		Dimension frame = getSize();
		Dimension screen = getToolkit().getScreenSize();
		setLocation((screen.width - frame.width) / 2,
				(screen.height - frame.height) / 2);
	}

	/**
	 * Actualiza los campos con los valores de la configuración
	 */
	private void setConfig() {
		xcombo.setSelectedIndex(config.getXIndex());
		if (plotmode == MODE_3D) {
			ycombo.setSelectedIndex(config.getYIndex());
		}
		zcombo.setSelectedIndex(config.getZIndex());
		samplestext.setText("" + config.getSamples());
		colormodel.setModel(config.getColorMode());
		graph3D.setHRotation(config.getHSlide() / 50.0 - 1.0);
		graph3D.setVRotation(config.getVSlide() / 100.0);
	}

	/**
	 * Acción asociada a la selección de la representación 2D
	 */
	private void set2DPlotMode() {
		if (plotmode == MODE_2D)
			return;
		plotmode = MODE_2D;
		try {
			config.setPlotMode(MODE_2D);
		} catch (Exception ex) {
		}
		Container content = getContentPane();
		content.remove(panel3D);
		content.add(graph2D);
		pack();
		actualize();
	}

	/**
	 * Acción asociada a la selección de la representación 3D
	 */
	private void set3DPlotMode() {
		if (plotmode == MODE_3D)
			return;
		plotmode = MODE_3D;
		try {
			config.setPlotMode(MODE_3D);
		} catch (Exception ex) {
		}
		Container content = getContentPane();
		content.remove(graph2D);
		content.add(panel3D);
		pack();
		actualize();
	}

	/**
	 * Acción asociada a la lista desplegable del eje X
	 */
	private void changeXAxis() {
		int index = xcombo.getSelectedIndex();
		int xindex = config.getXIndex();
		int yindex = config.getYIndex();
		if (index == xindex)
			return;
		config.setXIndex(index);
		if (yindex == index) {
			config.setYIndex(xindex);
			ycombo.setSelectedIndex(xindex);
		}
		actualize();
	}

	/**
	 * Acción asociada a la lista desplegable del eje Y
	 */
	private void changeYAxis() {
		int index = ycombo.getSelectedIndex();
		int xindex = config.getXIndex();
		int yindex = config.getYIndex();
		if (index == yindex)
			return;
		config.setYIndex(index);
		if (xindex == index) {
			config.setXIndex(yindex);
			xcombo.setSelectedIndex(yindex);
		}
		actualize();
	}

	/**
	 * Acción asociada a la lista desplegable del eje Z
	 */
	private void changeZAxis() {
		config.setZIndex(zcombo.getSelectedIndex());
		actualize();
	}

	/**
	 * Acción asociada al cambio de modelo de color
	 */
	private void changeColor(String action) {
		try {
			int code = Integer.parseInt(action.substring(2));
			config.setColorMode(code);
			colormodel.setModel(code);
			actualize();
		} catch (Exception ex) {
		}
	}

	/**
	 * Acción asociada a la edición de valores de entrada
	 */
	private void changeInputValues() {
		XfplotInputDialog dialog = new XfplotInputDialog(this);
		dialog.setVisible(true);
		actualize();
	}

	/**
	 * Actualiza la representación gráfica
	 */
	private void actualize() {
		if (plotmode == MODE_3D) {
			try {
				config.setSamples(Integer.parseInt(samplestext.getText()));
			} catch (Exception ex) {
				samplestext.setText("" + config.getSamples());
			}
			function3D = config.get3DFunction();
			computed3D = true;
			graph3D.repaint();
		} else if (plotmode == MODE_2D) {
			try {
				config.setSamples(Integer.parseInt(samplestext.getText()));
			} catch (Exception ex) {
				samplestext.setText("" + config.getSamples());
			}
			function2D = config.get2DFunction();
			graph2D.repaint();
		}
	}

	/**
	 * Almacena la imagen en un archivo de datos
	 */
	private void saveData() {
		File wdir;
		if (xfuzzy != null)
			wdir = xfuzzy.getWorkingDirectory();
		else
			wdir = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save Data");
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
		try {
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream stream = new PrintStream(fos);

			Variable xvar = getXVariable();
			Variable yvar = getYVariable();
			stream.println(config.getHeading());

			if (plotmode == MODE_3D) {
				int samples = function3D.length;
				for (int i = 0; i < samples; i++) {
					for (int j = 0; j < samples; j++) {
						stream.print("" + xvar.point(i * 1.0 / (samples - 1)));
						stream.print(" " + yvar.point(j * 1.0 / (samples - 1)));
						stream.println(" " + function3D[i][j]);
					}
					stream.println("");
				}
			} else {
				int samples = function2D.length;
				for (int i = 0; i < samples; i++) {
					stream.print("" + xvar.point(i * 1.0 / (samples - 1)));
					stream.println(" " + function2D[i]);
				}
			}

			stream.close();
		} catch (Exception ex) {
		}
	}

	/**
	 * Intenta grabar la imagen como un archivo JPEG
	 */
	private void saveImage() {
		File wdir;
		if (xfuzzy != null)
			wdir = xfuzzy.getWorkingDirectory();
		else
			wdir = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPEG mage files (.jpg)", "jpg");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save Image");
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
		graph3D.saveImage(file);
	}

	/**
	 * Almacena la configuración en un fichero externo
	 */
	private void saveConfig() {
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
	 * Recupera la configuración de un fichero externo
	 */
	private void loadConfig() {
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
		File file = chooser.getSelectedFile();
		XfuzzyConfig xfcparser = new XfuzzyConfig();
		if (!xfcparser.parseXfplotConfig(config, file)) {
			if (xfuzzy != null)
				xfuzzy.log(xfcparser.resume());
			else
				System.out.println(xfcparser.resume());
			Toolkit.getDefaultToolkit().beep();
		}
		setConfig();
		actualize();
	}

	/**
	 * Finaliza la aplicación
	 */
	private void close() {
		if (xfuzzy == null)
			System.exit(0);
		else
			setVisible(false);
	}
}
