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
//VENTANA GRÁFICA PARA Xfvhdl					
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
package xfuzzy.xfvhdl;

import xfuzzy.lang.*;

import xfuzzy.*;
import xfuzzy.util.*;
import xfuzzy.xfsg.XfsgIcons;
import xfuzzy.xfsg.XfsgStructure;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Clase que gestiona la ventana gráfica de la herramienta de síntesis de 
 * sistemas difusos Xfvhdl. Lo que hace es dibujar la ventana, 
 * esperar a que el usuario indique las opciones deseadas y permitirle a través 
 * de la botonera que muestra, generar código VHDL, o generarlo e implementarlo 
 * a la vez.
 * 
 * 
 */
public class XfvhdlWindow extends JFrame implements TreeSelectionListener, ActionListener,
WindowListener, MouseListener, KeyListener {


	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = -3243732153913695149L;

	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";
	
	/**Este atributo es el encargado de recoger la estructura que se muestra 
	 * en la parte izquierda de la interfaz gráfica.*/
	DefaultMutableTreeNode top = null;
	
	XfvhdlMessage msg;
	private JTree tree = null;
	XfvhdlMiRender mr = null;
	
	
	/**Atributo que recoge la información de la opción para generar 
	 * ficheros complementarios.*/
	private JCheckBox gen_compfile;
	
	/**Atributo que recoge la información de la opción para usar 
	 * componentes simplificados.*/
	private JCheckBox simple;
	
	/**Es el contenedor de la zona derecha superior de la interfaz.*/
	private JScrollPane right = null;
	
	/**Box que es el contenedor de la zona izquierda superior de 
	 * la interfaz.*/
	Box left;
	
	/**Contiene la parte izquierda superior y la derecha superior de 
	 * la interfaz.*/
	private JSplitPane splitPane = null;
	
	JProgressBar barra = null;
	
	/**Box que contiene el sistema de árbol a representar.*/
	Box systemtree;
	
	/**Atributo en el que nos apoyamos cuando cargamos un módulo de 
	 * inferencia en la zona derecha.*/
	private XfvhdlFLC flc = null;
	
	/**Atributo en el que nos apoyamos cuando cargamos un módulo crisp 
	 * en la zona derecha.*/
	private XfvhdlCrisp crisp = null;
	
	/**Contiene los controles que se cargarán en la zona derecha cuando 
	 * esta vaya a representar a un módulo de inferencia.*/
	private Box boxflc = null;
	
	/**Contiene los controles que se cargarán en la zona derecha cuando 
	 * esta vaya a representar a un módulo crisp.*/
	private Box boxcrisp = null;
	
	/** Lista que almacena todos los módulos de inferencia del sistema 
	 * jerárquico.*/
	ArrayList<XfvhdlFLC> arrayflcs;
	
	/** Lista que almacena todos los módulos crisp del sistema 
	 * jerárquico.*/
	ArrayList<XfvhdlCrisp> arraycrisp;
	
	//XComboBox memory[];
	JFrame frame = null;
	
	/**
	*  Atributo que apunta al objeto Xfuzzy, siempre y cuando 
	*  se ejecute desde Xfuzzy y no desde consola. 				       
	*/
	private Xfuzzy xfuzzy;
	
	/**Esfecificación XFL del sistema jerárquico.*/
	private Specification spec;

	/**Atributo que almacena y representa la información referente 
	 * a los nombres de ficheros de entrada, el prefijo de ciertos ficheros 
	 * de salida y al directorio de salida en el que queremos que se 
	 * generen los ficheros de salida. Esta sección es la que aparece en la 
	 * zona superior de la interfaz.*/
	private XTextForm filesInformation[];

	/**Gestiona los comandos asociados a la botonera inferior de la interfaz.*/
	private XCommandForm commandform;

	/**Gestiona la información referente a las opciones globales de ROM a usar 
	 * y de RAM a usar (automática, ninguna, de bloque o distribuida).*/
	private XComboBox otherInformation[];

	/**Gestiona la información referente a la opción global que permite 
	 * la elección de una u otra herramienta de síntesis.*/
	private XComboBox otherInformation2[];

	/**Gestiona la información referente a la opción global que permite 
	 * la elección del esfuerzo.(Alto o bajo).*/
	private XComboBox mapEffort[];

	/**Gestiona la información referente a la opción global que permite 
	 * la elección de la optimización.(En área, en velocidad, en ambas 
	 * o en ninguna).*/
	private XComboBox optimization[];

	//private JCheckBox simplifiedComponents;
	//private JCheckBox complementaryFiles;

	/**Gestiona la información referente a la opción global que permite 
	 * la elección del dispositivo para el que queremos realizar la 
	 * implementación.*/
	private XTextForm device;

	/**Gestiona la información referente a la opción global que permite 
	 * la elección de la familia para la que queremos sintetizar el 
	 * código VHDL.*/
	private XTextForm family;

	/**Atributo que indica si el usuario quiere realizar la síntesis.*/
	private int synth = 0;
	
	/**Atributo que indica si el usuario quiere implementar.*/
	private int impl = 0;

	JFrame j;

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			    CONSTRUCTORES			
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/** Constructor que recibe un objeto xfuzzy y una especificación XFL
	 * @param xfuzzy Objeto xfuzzy que ha llamado a Xfvhl4
	 * @param spec	 Especificación XFL del sistema difuso
	 */

	public XfvhdlWindow(Xfuzzy xfuzzy, Specification spec) {

		
		super("Xfvhdl");
		this.xfuzzy = xfuzzy;
		this.spec = spec;
		

		msg = new XfvhdlMessage(xfuzzy);
		mr = new XfvhdlMiRender();

		String aux = spec.getFile().getAbsolutePath();
		int n = aux.lastIndexOf("\\");
		int n2 = aux.lastIndexOf(".");
		XfvhdlProperties.outputDirectory = aux.substring(0, n);
		XfvhdlProperties.name_outputfiles = aux.substring(n + 1, n2);
		XfvhdlProperties.ficheroXFL = spec.getFile().getAbsolutePath();

		 build();
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//		FUNCIONES DE CREACION DE LA VENTANA		
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/** 
	 * Método que crea la ventana gráfica
	 */

	private void build() {
		// Define los botones y comandos asociados para la sección "Files 
		// and directories information"
		String filesInformationLabels[] = { "Input XFL file",
				"Name for Output files", "Output directory", };
		String filesInformationCommands[] = { "SelectXFLFile", "",
				"SelectOutputDirectory", };				
		
		filesInformation = new XTextForm[3];
		filesInformation[0] = new XTextForm(filesInformationLabels[0], this);
		filesInformation[0].setEditable(false);
		if (spec!=null)
			filesInformation[0].setText(spec.getFile().getAbsolutePath());
		filesInformation[1] = new XTextForm(filesInformationLabels[1], this);
		filesInformation[1].setEditable(true);
		filesInformation[1].setText(XfvhdlProperties.name_outputfiles);
		filesInformation[2] = new XTextForm(filesInformationLabels[2], this);
		filesInformation[2].setActionCommand(filesInformationCommands[2]);
		filesInformation[2].setEditable(false);
		filesInformation[2].setText(XfvhdlProperties.outputDirectory);
		XTextForm.setWidth(filesInformation);
				
		Box filesbox = new Box(BoxLayout.Y_AXIS);
		filesbox.add(new XLabel("Files and directory information"));
		for (int i = 0; i < filesInformation.length; i++)
			filesbox.add(filesInformation[i]);
		filesbox.setPreferredSize(new Dimension(450, 80));
		
		// Cabecera
		Box head = new Box(BoxLayout.X_AXIS);
		head.add(Box.createHorizontalStrut(5));
		head.add(filesbox);
		head.add(Box.createHorizontalStrut(5));
		
		// Árbol del sistema difuso (systemtree).
		if (spec!=null){
			top = new DefaultMutableTreeNode(spec.getName());
			createNodes(top);
			tree = new JTree(top);
			tree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.setCellRenderer(mr);
			tree.addTreeSelectionListener(this);
			if (playWithLineStyle) {
				System.out.println("line style = " + lineStyle);
				tree.putClientProperty("JTree.lineStyle", lineStyle);
			}
			tree.setBackground(XConstants.textbackground);
	
			JScrollPane treeView = new JScrollPane(tree);
			systemtree = new Box(BoxLayout.Y_AXIS);
			systemtree.add(new XLabel("System Tree"));
			systemtree.add(treeView);
		}else{
			JScrollPane treeView = new JScrollPane();
			systemtree = new Box(BoxLayout.Y_AXIS);
			systemtree.add(new XLabel("System Tree"));
			systemtree.add(treeView);
		}

		// Opciones de síntesis (options).
		Box options1 = new Box(BoxLayout.Y_AXIS);
		options1.setPreferredSize(new Dimension(270,100));
		options1.add(new XLabel("Global Options"));
		options1.add(Box.createVerticalStrut(25));
		
		gen_compfile = new JCheckBox("Generate complementary files");
		simple = new JCheckBox("Use Simplified Components", true);
		
		options1.add(gen_compfile);
		options1.add(simple);
		options1.add(Box.createVerticalStrut(10));
		options1.add(Box.createHorizontalStrut(10));
		
		
		Box options2 = new Box(BoxLayout.Y_AXIS);
		options2.setPreferredSize(new Dimension(350,100));
		options2.add(new XLabel("FPGA Implementation"));
		otherInformation= new XComboBox[2];
		otherInformation[0] = new XComboBox("RAM to be used");
		otherInformation[1] = new XComboBox("ROM to be used");
		Vector list1 = new Vector();
		list1.add("Automatic");
		list1.add("None");
		list1.add("Block");
		list1.add("Distributed");
		otherInformation[0].setList(list1);
		otherInformation[0].setSelectedIndex(0);
		otherInformation[1].setList(list1);
		otherInformation[1].setSelectedIndex(0);
		family = new XTextForm("FPGA Family               ");
		family.setText(XfvhdlProperties.target);
		device = new XTextForm("Device");
		device.setText(XfvhdlProperties.partType);
		for (int i = 0; i < otherInformation.length; i++)
			options2.add(otherInformation[i]);
		options2.add(family);
		options2.add(device);
		
		Box options3 = new Box(BoxLayout.Y_AXIS);
		options3.setPreferredSize(new Dimension(350,100));
		options3.add(new XLabel("CAD Tools Options"));
		otherInformation2 = new XComboBox[1];
		otherInformation2[0] = new XComboBox("Tool  ");
		Vector list2 = new Vector();
		list2.add("Xilinx XST");
		list2.add("Synopsys FPGA Express");
		list2.add("Synopsys FPGA Compiler 2");
		otherInformation2[0].setList(list2);
		otherInformation2[0].setSelectedIndex(0);
		optimization = new XComboBox[1];
		optimization[0] = new XComboBox("Optimization             ");
		Vector opti = new Vector();
		opti.add("Without optimization");
		opti.add("Area optimization");
		opti.add("Speed optimization");
		opti.add("Area and Speed optimizations");
		optimization[0].setList(opti);
		optimization[0].setSelectedIndex(0);
		mapEffort = new XComboBox[1];
		mapEffort[0] = new XComboBox("Effort");
		Vector meff = new Vector();
		meff.add("Low");
		meff.add("High");
		mapEffort[0].setList(meff);
		mapEffort[0].setSelectedIndex(0);
		for (int i = 0; i < otherInformation2.length; i++)
			options3.add(otherInformation2[i]);
		for (int i = 0; i < optimization.length; i++)
			options3.add(optimization[i]);
		for (int i = 0; i < mapEffort.length; i++)
			options3.add(mapEffort[i]);
		
		Box options =new Box(BoxLayout.X_AXIS);
		
		options.add(options1);
		options.add(options2);
		options.add(options3);
		((Box)(options.getComponent(0))).setAlignmentY((float)0.5);
		((Box)(options.getComponent(1))).setAlignmentY((float)0.48);
		((Box)(options.getComponent(2))).setAlignmentY((float)0.57);
		
		// Parte izquierda de la ventana (left).
		left = new Box(BoxLayout.Y_AXIS);
		left.add(systemtree);
		left.setBorder(BorderFactory.createRaisedBevelBorder());
		left.setMinimumSize(new Dimension(220, 100));
		
		// Estructura del sistema difuso (right).
		if(spec!=null){
			XfsgStructure graph = new XfsgStructure(spec.getSystemModule());
			Box centerbox = new Box(BoxLayout.Y_AXIS);
			centerbox.add(new XLabel("System Structure"));
			centerbox.add(new JScrollPane(graph));
			right = new JScrollPane(centerbox);
		}
		// splitPane.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);
		
		// Cuerpo de la ventana (body)
		Box body = new Box(BoxLayout.X_AXIS);
		body.add(splitPane);
		body.setPreferredSize(new Dimension(970, 450));

		
		// Barra de comandos (comandform)
		String formlb[] = { "Load Configuration", "Save Configuration",
				"Apply", "Generate VHDL code", "Generate and Implement",  "Close"};
		commandform = new XCommandForm(formlb, formlb, this);
		commandform.setCommandWidth(150);
		commandform.block();
		commandform.setEnabled(1, false);
		commandform.setEnabled(3, false);
		commandform.setEnabled(4, false);
		
		// Barra de progreso (barra).
		barra = new JProgressBar();
		barra.setVisible(XfvhdlProperties.inWindow);
		barra.setForeground(Color.green);

		// Ventana principal (content).
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		if (spec!=null)
			content.add(new XLabel(
				"Build System Generator configuration files for "
						+ spec.getName()));
		content.add(Box.createVerticalStrut(5));
		content.add(head);
		content.add(Box.createVerticalStrut(8));
		content.add(body);
		content.add(Box.createVerticalStrut(3));
		content.add(options);
		content.add(Box.createVerticalStrut(8));
		content.add(commandform);
		content.add(barra);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		pack();
		setVisible(XfvhdlProperties.inWindow);
		setLocation();

		// Si por consola hemos especificado un fichero de configuración
		//lo cargamos en pantalla.
		if(XfvhdlProperties.cargarXML){
			XfvhdlLeerXML lector = new XfvhdlLeerXML(top,
					gen_compfile, simple, otherInformation, family, device, 
					otherInformation2, optimization, mapEffort,filesInformation);
			lector.leer_xml();
			activarHijos(top);
			activarPadre(top);
			tree.repaint();

			XfvhdlError err = new XfvhdlError();
			if (err.hasErrors() || err.hasWarnings()) {
				msg.addMessage(err.getMessages());
				msg.show();
				err.resetAll();
			}

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			if (node == null)
				return;

			Object nodeInfo = node.getUserObject();
			if (node.isLeaf()) {
				if (nodeInfo instanceof XfvhdlFLC) {
					flc = (XfvhdlFLC) nodeInfo;
					boxflc = flc.gui();
					right = new JScrollPane(boxflc);
					splitPane.setBottomComponent(right);
				} else if (nodeInfo instanceof XfvhdlCrisp) {
					crisp = (XfvhdlCrisp) nodeInfo;
					boxcrisp = crisp.gui();
					right = new JScrollPane(boxcrisp);
					splitPane.setBottomComponent(right);
				}
			}
		}
			
	}

	/** 
	 * Método que indica en qué localización de la pantalla hay que 
	 * dibujar la ventana
	 */
	public void setLocation() {
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

	/**
	 * Método encargado de mostrar la ventana de selección del fichero .xfl
	 * de entrada
	 */
	private void selectXFLFile() {
		System.out.println("Entra en xfl");
		File wdir = new File(XfvhdlProperties.outputDirectory);
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy system files (.xfl)", "xfl");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load System");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		filesInformation[0].setText(chooser.getSelectedFile().toString());
		}
	
	/** 
	 * Método encargado de mostrar la ventana de selección del directorio 
	 * de salida
	 */
	private void selectOutputDirectory() {
		
		File wdir = new File (""+XfvhdlProperties.outputDirectory);
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Output Directory");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		filesInformation[2].setText(chooser.getSelectedFile().toString());
		XfvhdlProperties.outputDirectory = chooser.getSelectedFile().toString();
	}

	/** 
	 * Método encargado de generar el código VHDL y sintetizarlo. 
	 * Lo que hace es llamar al método generateVHDLCode 
	 * indicando que se desea sintetizar
	 * @see generateVHDLCode
	 */
	public void generateVHDLCodeAndSynthetize() {
		synth = 1;
		generateVHDLCode();
	}
	/** 
	 * Método encargado de generar el código VHDL, sintetizarlo e 
	 * implementarlo. Lo que hace es llamar al método generateVHDLCode 
	 * indicando que se desea sintetizar e implementar
	 * @see generateVHDLCode
	 */
	public void generateVHDLCodeAndImplement() {
		impl = 1;
		generateVHDLCode();
	}

	/** 
	 * Método encargado de generar el código VHDL. Lo que hace es llamar 
	 * a Xfvhdl indicándole todas las opciones que se han seleccionado en 
	 * la ventana gráfica.
	 */
	public void generateVHDLCode() {
		XfvhdlProperties.userDirectory = System.getProperty("user.dir", ".");
		XfvhdlProperties.fileSeparator = System.getProperty("file.separator",
		"\\");
		XfvhdlProperties.inWindow = true;
		XfvhdlProperties.ficheroXFL = filesInformation[0].getText();

		// tratamiento del directorio de salida
		XfvhdlProperties.outputDirectory = filesInformation[2].getText();
		XfvhdlProperties.name_outputfiles = filesInformation[1].getText();

		// tratamiento del directorio de la librería
		String libdefault = XfvhdlProperties.userDirectory
		+ XfvhdlProperties.fileSeparator
		+ XfvhdlProperties.libraryDirectory;
		
		// tratamiento de la implementacion
		if (otherInformation[0].getSelectedIndex() == 0)
			XfvhdlProperties.ramExtract = "None";
		else if (otherInformation[0].getSelectedIndex() == 1)
			XfvhdlProperties.ramExtract = "Automatic";
		else if (otherInformation[0].getSelectedIndex() == 2)
			XfvhdlProperties.ramExtract = "Block";
		else if (otherInformation[0].getSelectedIndex() == 3)
			XfvhdlProperties.ramExtract = "Distributed";

		if (otherInformation[1].getSelectedIndex() == 0)
			XfvhdlProperties.romExtract = "None";
		else if (otherInformation[1].getSelectedIndex() == 1)
			XfvhdlProperties.romExtract = "Automatic";
		else if (otherInformation[1].getSelectedIndex() == 2)
			XfvhdlProperties.romExtract = "Block";
		else if (otherInformation[1].getSelectedIndex() == 3)
			XfvhdlProperties.romExtract = "Distributed";
		
		//tratamiento de la herramienta de síntesis
		if (otherInformation2[0].getSelectedIndex() == 1)
			XfvhdlProperties.synthesisTool = "FPGA_Express";
		else if (otherInformation2[0].getSelectedIndex() == 2)
			XfvhdlProperties.synthesisTool = "FPGA_Compiler_2";
		else
			XfvhdlProperties.synthesisTool = "XILINX_XST";

		//tratamiento del map Effort
		if (mapEffort[0].getSelectedIndex() == 1)
			XfvhdlProperties.mapEffort = "HIGH_MAP_EFFORT";
		else
			XfvhdlProperties.mapEffort = "LOW_MAP_EFFORT";

		// tratamiento de la optimizacion
		if (optimization[0].getSelectedIndex() == 1)
			XfvhdlProperties.areaOptimization = true;
		else if (optimization[0].getSelectedIndex() == 2)
			XfvhdlProperties.speedOptimization = true;
		else if (optimization[0].getSelectedIndex() == 3) {
			XfvhdlProperties.areaOptimization = true;
			XfvhdlProperties.speedOptimization = true;
		}

		// tratamiento de componentes simplificados
		if (simple.isSelected())
			XfvhdlProperties.simplifiedComponents = true;
		else
			XfvhdlProperties.simplifiedComponents=false;

		// tratamiento de los ficheros complementarios
		if (gen_compfile.isSelected()) {
			XfvhdlProperties.complementaryFiles = true;
		}

		// tratamiento del dispositivo FPGA
		if (!device.getText().equals(XfvhdlProperties.partType)) {
			XfvhdlProperties.partType = new String(device.getText());
		}

		// tratamiento de la familia de FPGAs
		if (!family.getText().equals(XfvhdlProperties.target)) {
			XfvhdlProperties.target = new String(family.getText());
		}


		// Determina si hay que sintetizar o no
		if (synth == 1) {
			XfvhdlProperties.synthesis = true;
		}
		if(impl == 1){
			XfvhdlProperties.synthesis = true;
			XfvhdlProperties.implementation = true;}

		try {
			// Se asocia al xfuzzy que ha llamado a xfvhdl para 
			// que pueda mostrar los mensajes de log
			Xfvhdl.xfuzzy = xfuzzy;

			// Llama a Xfvhdl para que se ejecute en modo consola
			Xfvhdl.inConsoleExecution(spec, top, null);
			if(Xfvhdl.cerrarWindow)
				close();

			// Pone a cero los errores y los warnings
			XfvhdlError xerr = new XfvhdlError();
			xerr.resetAll();
			synth = 0;
			impl= 0 ;
		} catch (IOException e) {
			/**/System.err.println("Ejecutando Xfvhdl " + e);
		}
	}

	/** 
	 * Método encargado de cerrar la ventana gráfica.
	 */
	private void close() {
		XfvhdlProperties.activar_boton_GMF = false;
		if (xfuzzy == null)
			System.exit(0);
		else
			setVisible(false);
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//         FUNCIONES QUE DESARROLLAN LAS INTERFACES		
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("SelectXFLFile"))
			selectXFLFile();		
		
		else if (command.equals("SelectXFLFile"))
			selectXFLFile();
		else if (command.equals("SelectOutputDirectory"))
			selectOutputDirectory();
		else if (command.equals("Apply"))
			Actualizar_valores();
		else if (command.equals("Load Configuration"))
			selectConfigFile();
		else if (command.equals("Save Configuration"))
			saveConfigFile();
		else if (command.equals("Generate VHDL code"))
			generateVHDLCode();
		else if (command.equals("Generate and Implement"))
			generateVHDLCodeAndImplement();
		else if (command.equals("Close"))
			close();
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		close();
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
	
	//Métodos necesarios
	
	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode flc = null;
		DefaultMutableTreeNode crisp = null;

		category = new DefaultMutableTreeNode(new Rama("RuleBases"));
		top.add(category);
		XfvhdlLeerXfl xfl = new XfvhdlLeerXfl(spec);
		xfl.inicializarFLCs();
		arrayflcs = xfl.getlistaflc();
		for (int i = 0; i < arrayflcs.size(); i++) {
			flc = new DefaultMutableTreeNode(arrayflcs.get(i));
			category.add(flc);
		}

		xfl.inicializarCrispBlocks();
		arraycrisp = xfl.getlistacrisp();
		if (arraycrisp.size() != 0) {
			category = new DefaultMutableTreeNode(new Rama("CrispBlocks"));
			top.add(category);
			for (int i = 0; i < arraycrisp.size(); i++) {
				crisp = new DefaultMutableTreeNode(arraycrisp.get(i));
				category.add(crisp);
			}
		}
		XfvhdlError err = new XfvhdlError();
		if (err.hasErrors() || err.hasWarnings()) {
			msg.addMessage(err.getMessages());
			msg.show();
			err.resetAll();
		}
	}
	
	/**Sublase que gestiona la completitud de los datos introducidos en 
	 * cada hoja del árbol de manera que se pueda redibujar el icono 
	 * que aparece en cada noso del árbol.*/
	public class Rama {
		String nombre;

		boolean completa;

		public Rama(String nombre) {
			this.nombre = nombre;
			completa = false;
		}

		public String getNombre() {
			return nombre;
		}

		public boolean getCompleta() {
			return completa;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public void setCompleta(boolean completa) {
			this.completa = completa;
		}

		public String toString() {
			return nombre;
		}
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		try {
			flc = null;
			crisp = null;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			if (node == null)
				return;

			Object nodeInfo = node.getUserObject();
			if (node.isLeaf()) {
				if (nodeInfo instanceof XfvhdlFLC) {
					flc = (XfvhdlFLC) nodeInfo;
					boxflc = flc.gui();
					right = new JScrollPane(boxflc);
					splitPane.setBottomComponent(right);
					//Si las entradas tienen un solapamiento
					//mayor que 1 se cierra la ventana
					if(boxflc==null){
						//1º mostramos los errores
						XfvhdlError err = new XfvhdlError();
						if (err.hasErrors() || err.hasWarnings()) {
							msg.addMessage(err.getMessages());
							msg.show();
							err.resetAll();
						}
						//2º cerramos
						close();
					}
				} else if (nodeInfo instanceof XfvhdlCrisp) {
					crisp = (XfvhdlCrisp) nodeInfo;
					boxcrisp = crisp.gui();
					right = new JScrollPane(boxcrisp);
					splitPane.setBottomComponent(right);
				}
			} else {
				if (nodeInfo instanceof Rama) {
					Box box1 = new Box(BoxLayout.Y_AXIS);
					box1.add(Box.createVerticalStrut(100));
					XLabel xlabel = new XLabel("\nChoose one leaf\n");

					JPanel jp = new JPanel();
					jp.setLayout(new GridLayout(4, 3));

					String res = "";
					for (int i = 0; i < node.getChildCount(); i++) {
						DefaultMutableTreeNode aux = (DefaultMutableTreeNode) node
								.getChildAt(i);
						Object auxInfo = aux.getUserObject();

						if (auxInfo instanceof Rama) {
							res = ((Rama) auxInfo).getNombre() + "\n";
							JLabel etiq3 = new JLabel(res, XfsgIcons.folder,
									SwingConstants.CENTER);
							etiq3.setVerticalTextPosition(SwingConstants.TOP);
							jp.add(etiq3);

						} else if (auxInfo instanceof XfvhdlFLC) {
							res = ((XfvhdlFLC) auxInfo).getname() + "\n";
							JLabel etiq3 = new JLabel(res, XfsgIcons.rulebase,
									SwingConstants.CENTER);
							etiq3.setVerticalTextPosition(SwingConstants.TOP);
							jp.add(etiq3);
						} else if (auxInfo instanceof XfvhdlCrisp) {
							res = ((XfvhdlCrisp) auxInfo).getname() + "\n";
							JLabel etiq3 = new JLabel(res,
									XfsgIcons.crispblock, SwingConstants.CENTER);
							etiq3.setVerticalTextPosition(SwingConstants.TOP);
							jp.add(etiq3);
						}
					}
					box1.add(xlabel);
					box1.add(jp);
					right = new JScrollPane(box1);
				} else {
					// Dibujar el mapa
					XfsgStructure graph = new XfsgStructure(spec
							.getSystemModule());
					Box centerbox = new Box(BoxLayout.Y_AXIS);
					centerbox.add(new XLabel("System Structure"));
					centerbox.add(new JScrollPane(graph));

					right = new JScrollPane(centerbox);
				}
				// aux=new JScrollPane(box1);
				splitPane.setBottomComponent(right);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	private void saveConfigFile() {
		File f = new File(XfvhdlProperties.outputDirectory + "\\"
				+ XfvhdlProperties.name_outputfiles + ".xml");
		JFileChooser chooser = new JFileChooser(f);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfvhdl configuration files (.xml)", "xml");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(f);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save Configuration");
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

		XfvhdlLeerXML escritura = new XfvhdlLeerXML(top,
				gen_compfile, simple, otherInformation, family, device, 
				otherInformation2, optimization, mapEffort, filesInformation);
		String xml = escritura.escribir_xml();

		try {
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream stream = new PrintStream(fos);
			stream.print(xml);
			stream.close();
			xfuzzy.log("Config " + file.getName() + " saved as "
					+ file.getAbsolutePath() + ".");

		} catch (FileNotFoundException e) {
			xfuzzy.log("Can't save specification : " + file.getName() + ".");
			e.printStackTrace();
		}
	}

	private void selectConfigFile() {
		File wdir = new File(XfvhdlProperties.outputDirectory);

		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfvhdl configuration files (.xml)", "xml");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load Configuration");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;

		XfvhdlProperties.fichero_config = chooser.getSelectedFile().toString();
		XfvhdlLeerXML lector = new XfvhdlLeerXML(top,
				gen_compfile, simple, otherInformation, family, device, 
				otherInformation2, optimization, mapEffort,filesInformation);
		lector.leer_xml();
		activarHijos(top);
		activarPadre(top);
		tree.repaint();

		XfvhdlError err = new XfvhdlError();
		if (err.hasErrors() || err.hasWarnings()) {
			msg.addMessage(err.getMessages());
			msg.show();
			err.resetAll();
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null)
			return;

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			if (nodeInfo instanceof XfvhdlFLC) {
				flc = (XfvhdlFLC) nodeInfo;
				boxflc = flc.gui();
				right = new JScrollPane(boxflc);
				splitPane.setBottomComponent(right);
			} else if (nodeInfo instanceof XfvhdlCrisp) {
				crisp = (XfvhdlCrisp) nodeInfo;
				boxcrisp = crisp.gui();
				right = new JScrollPane(boxcrisp);
				splitPane.setBottomComponent(right);
			}
		}
	}
	
	public void Actualizar_valores() {
		if (flc != null) {
			int N = valida_string((((XTextForm) ((Box)((Box)boxflc.getComponent(0))
					.getComponent(0)).getComponent(2))
					.getText()));
			
			int No = valida_string((((XTextForm) ((Box)((Box)boxflc.getComponent(0))
					.getComponent(0)).getComponent(3))
					.getText()));
			int grad = valida_string((((XTextForm) ((Box)((Box)boxflc.getComponent(0))
					.getComponent(0)).getComponent(4))
					.getText()));
			int P = valida_string((((XTextForm) ((Box)((Box)boxflc.getComponent(0))
					.getComponent(0)).getComponent(5))
					.getText()));
			int W = valida_string((((XTextForm) ((Box)((Box)boxflc.getComponent(0))
					.getComponent(0)).getComponent(6))
					.getText()));
			boolean arith=((JRadioButton)((Box)((Box)((Box)((Box)boxflc.getComponent(0)).getComponent(1))
					.getComponent(1)).getComponent(1)).getComponent(0)).isSelected();
			String mem_MFC=null;
			for(int i=0;i<3;i++){
				if(((JRadioButton)((Box)((Box)((Box)((Box)boxflc.getComponent(0)).getComponent(1))
						.getComponent(1)).getComponent(2)).getComponent(i)).isSelected())
					mem_MFC=((JRadioButton)((Box)((Box)((Box)((Box)boxflc.getComponent(0)).getComponent(1))
							.getComponent(1)).getComponent(2)).getComponent(i)).getActionCommand();
				
			}
			if(mem_MFC==null)
				mem_MFC="ROM";
			
			String mem_RB=null;
			for(int i=0;i<3;i++){
				if(((JRadioButton)((Box)((Box)((Box)((Box)boxflc.getComponent(0)).getComponent(1))
						.getComponent(2)).getComponent(1)).getComponent(i)).isSelected())
					mem_RB=((JRadioButton)((Box)((Box)((Box)((Box)boxflc.getComponent(0)).getComponent(1))
							.getComponent(2)).getComponent(1)).getComponent(i)).getActionCommand();
				
			}
			if(mem_RB==null)
				mem_RB="ROM";
			
			flc.setN(N);
			flc.setNo(No);
			flc.setgrad(grad);
			flc.setP(P);
			flc.setW(W);
			flc.setMFC_arithmetic(arith);
			flc.setMFC_memory(mem_MFC);
			flc.setRB_memory(mem_RB);
			if (N != 0 & No != 0 & grad != 0 & W != 0 & (P != 0&arith||!arith)) {
				flc.settodo_relleno(true);
			}
			if (N == 0 | No == 0 | grad == 0 | W == 0 | (P == 0&arith)) {
				flc.settodo_relleno(false);
			}
		} else if (crisp != null) {
			int grad = Integer.parseInt(((XTextForm) boxcrisp.getComponent(3))
					.getText());
			crisp.setNo(grad);
			if (grad != 0) {
				crisp.settodo_relleno(true);
			}
			if (grad == 0)
				crisp.settodo_relleno(false);			
		}
		activarHijos(top);
		activarPadre(top);
		tree.repaint();
	}

	private void activarHijos(DefaultMutableTreeNode node) {
		int n = node.getChildCount();
		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			activar_hijos_aux(dmtn);
		}
	}
	
	private void activar_hijos_aux(DefaultMutableTreeNode node) {
		Object nodeInfo = node.getUserObject();
		int n = node.getChildCount();
		boolean cambiar = true;
		for (int i = 0; i < n & cambiar; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			Object o = dmtn.getUserObject();
			
			if (o instanceof XfvhdlFLC) {
				XfvhdlFLC f1 = (XfvhdlFLC) o;
				if (!f1.gettodo_relleno())
					cambiar = false;
			} else if (o instanceof XfvhdlCrisp) {
				XfvhdlCrisp c1 = (XfvhdlCrisp) o;
				if (!c1.gettodo_relleno())
					cambiar = false;
			}
		}

		if (cambiar)
			((XfvhdlWindow.Rama) nodeInfo).setCompleta(true);
		else
			((XfvhdlWindow.Rama) nodeInfo).setCompleta(false);
	}
	
	private void activarPadre(DefaultMutableTreeNode node) {
		int n = node.getChildCount();
		boolean cambiar = true;
		for (int i = 0; i < n & cambiar; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			Object o = dmtn.getUserObject();
			if (!((XfvhdlWindow.Rama) o).getCompleta())
				cambiar = false;
		}
		if (cambiar) {
			XfvhdlProperties.activar_boton_GMF = true;
			// toolb.setEnabled(true);
			commandform.setEnabled(4, true);
			commandform.setEnabled(3, true);
			commandform.setEnabled(1, true);
		} else {
			XfvhdlProperties.activar_boton_GMF = false;
			commandform.setEnabled(4, false);
			commandform.setEnabled(3, false);
			commandform.setEnabled(1, false);
		}
	}
	
	/*Si la entrada es nula devuelve 0 
	 * en caso contrario devuelve el entero
	 *  que representa la cadena de entrada*/
	private int valida_string(String s) {
		int res;
		if (s.isEmpty())
			res = 0;
		else
			res = Integer.parseInt(s);
		return res;
	}
	
}
