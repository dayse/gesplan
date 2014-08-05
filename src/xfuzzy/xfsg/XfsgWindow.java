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

package xfuzzy.xfsg;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import xfuzzy.Xfuzzy;
import xfuzzy.XfuzzyIcons;
import xfuzzy.lang.Specification;
import xfuzzy.util.JFileChooserConfig;
import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XDialog;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

/**
 * Clase que implementa la interfaz gráfica de la herramienta Xfsg
 * 
 * @author Jesus Izquierdo Tena
 * @author Santiago Sánchez-Solano
 */

public class XfsgWindow extends JFrame implements TreeSelectionListener,
		ActionListener, WindowListener, MouseListener, KeyListener {

	// ----------------------------------------------------------------------------//
	// COSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	private JTree tree = null;

	private static boolean playWithLineStyle = false;

	private static String lineStyle = "Horizontal";

	private XTextForm filesInformation[];

	private JScrollPane right = null;

	private Specification spec = null;

	private JSplitPane splitPane = null;

	private Xfuzzy xfuzzy = null;

	private XfsgFLC flc = null;

	private Box boxflc = null;

	private XfsgCrisp crisp = null;

	private Box boxcrisp = null;

	XfsgMiRender mr = null;

	JFrame frame = null;

	JButton toolb = null;

	DefaultMutableTreeNode top = null;

	XCommandForm commandform = null;

	JProgressBar barra = null;

	Thread hilo = null;

	XfsgMessage msg;

	private JCheckBox include_confid;

	private JCheckBox gen_txtfile;

	private JCheckBox gen_simmodel;

	private JCheckBox simple;

	private JDialog infor;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	public XfsgWindow(Xfuzzy xfuzzy, Specification spec) {
		// super(new GridLayout(1,0));
		super("Xfsg");
		this.xfuzzy = xfuzzy;
		this.spec = spec;
		mr = new XfsgMiRender();
		msg = new XfsgMessage(xfuzzy);
		if (xfuzzy != null) {
			String aux = spec.getFile().getAbsolutePath();
			int n = aux.lastIndexOf("\\");
			int n2 = aux.lastIndexOf(".");
			XfsgProperties.outputDirectory = aux.substring(0, n);
			XfsgProperties.name_outputfiles = aux.substring(n + 1, n2);
			// System.out.println(XfsgProperties.outputDirectory);
			XfsgProperties.userDirectory = System.getProperty("user.dir", ".");
			XfsgProperties.fileSeparator = System.getProperty("file.separator",
					"\\");
			XfsgProperties.inWindow = true;
			XfsgProperties.ficheroXFL = spec.getFile().getAbsolutePath();
			// XfsgProperties.ficheroXFL = filesInformation[0].getText();
			// XfsgProperties.outputDirectory = filesInformation[2].getText();
			// XfsgProperties.name_outputfiles = filesInformation[1].getText();
		}
		build();
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Genera la ventana principal de Xfsg
	 */
	private void build() {

		// "Files and directory information"
		String filesInformationLabels[] = { "Input XFL file",
				"Name for Output files", "Output directory", };
		String filesInformationCommands[] = { "SelectXFLFile", "",
				"SelectOutputDirectory", };

		filesInformation = new XTextForm[3];
		filesInformation[0] = new XTextForm(filesInformationLabels[0], this);
		// filesInformation[0].setActionCommand(filesInformationCommands[0]);
		filesInformation[0].setEditable(false);
		filesInformation[0].setText(spec.getFile().getAbsolutePath());
		filesInformation[1] = new XTextForm(filesInformationLabels[1], this);
		filesInformation[1].setEditable(true);
		filesInformation[1].setText(XfsgProperties.name_outputfiles);
		filesInformation[2] = new XTextForm(filesInformationLabels[2], this);
		filesInformation[2].setActionCommand(filesInformationCommands[2]);
		filesInformation[2].setEditable(false);
		filesInformation[2].setText(XfsgProperties.outputDirectory);
		XTextForm.setWidth(filesInformation);

		Box filesbox = new Box(BoxLayout.Y_AXIS);
		filesbox.add(new XLabel("Files and directory information"));
		for (int i = 0; i < filesInformation.length; i++)
			filesbox.add(filesInformation[i]);
		filesbox.setPreferredSize(new Dimension(450, 80));
		filesbox.setMinimumSize(new Dimension(350, 80));

		// Información sobre el uso del Package xfsg
		JButton xfsg_bt;
		xfsg_bt = new JButton(XfsgIcons.Xfsg_logo);
		xfsg_bt.setActionCommand("Infor");
		xfsg_bt.setBorder((Border) new BevelBorder(BevelBorder.RAISED));
		xfsg_bt.addActionListener(this);
		xfsg_bt.setToolTipText("Xfsg");
		xfsg_bt.setEnabled(true);
		xfsg_bt.setAlignmentY(0.5f);

		JTextArea xfsg_txt = new JTextArea();
		xfsg_txt.setBackground(Color.LIGHT_GRAY);
		xfsg_txt
				.append("\n WARNING: to assure the implementability of the system, use  \n"
						+ " only the functions inside the xfsg package. Click on the XFSG  \n"
						+ " icon to see the functions included in this package.");

		Box xfsgbox = new Box(BoxLayout.X_AXIS);
		xfsgbox.add(Box.createHorizontalStrut(10));
		xfsgbox.add(xfsg_bt);
		xfsgbox.add(Box.createHorizontalStrut(10));
		xfsgbox.add(xfsg_txt);
		xfsgbox.add(Box.createHorizontalStrut(10));
		xfsgbox.setPreferredSize(new Dimension(450, 80));

		// Cabecera
		Box head = new Box(BoxLayout.X_AXIS);
		head.add(Box.createHorizontalStrut(5));
		head.add(filesbox);
		head.add(Box.createHorizontalStrut(5));
		head.add(xfsgbox);

		// Árbol del sistema difuso (systemtree).
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
		Box systemtree = new Box(BoxLayout.Y_AXIS);
		systemtree.add(new XLabel("System Tree"));
		systemtree.add(treeView);

		// Opciones de síntesis (options).
		Box options = new Box(BoxLayout.Y_AXIS);
		options.add(new XLabel("Global Options"));
		options.add(Box.createVerticalStrut(10));
		include_confid = new JCheckBox("Include Rules’ Confidence Factors");
		gen_txtfile = new JCheckBox("Generate txt file");
		gen_simmodel = new JCheckBox("Generate Simulink Model");
		simple = new JCheckBox("Use Simplified Components");
		options.add(include_confid);
		options.add(gen_txtfile);
		options.add(gen_simmodel);
		options.add(simple);
		options.add(Box.createVerticalStrut(10));

		// Parte izquierda de la ventana (left).
		Box left = new Box(BoxLayout.Y_AXIS);
		left.add(systemtree);
		left.add(options);
		left.setBorder(BorderFactory.createRaisedBevelBorder());
		left.setPreferredSize(new Dimension(250, 200));

		// Estructura del sistema difuso (right).
		XfsgStructure graph = new XfsgStructure(spec.getSystemModule());
		Box centerbox = new Box(BoxLayout.Y_AXIS);
		centerbox.add(new XLabel("System Structure"));
		centerbox.add(new JScrollPane(graph));
		right = new JScrollPane(centerbox);

		// splitPane.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);

		// Cuerpo de la ventana (body)
		Box body = new Box(BoxLayout.X_AXIS);
		body.add(splitPane);
		body.setPreferredSize(new Dimension(950, 550));

		// Barra de comandos (comandform)
		String formlb[] = { "Load Configuration", "Save Configuration",
				"Apply", "Close", "Generate Files" };
		commandform = new XCommandForm(formlb, formlb, this);
		commandform.setCommandWidth(150);
		commandform.block();
		commandform.setEnabled(1, false);
		commandform.setEnabled(4, false);

		// Barra de progreso (barra).
		barra = new JProgressBar();
		barra.setVisible(true);
		barra.setForeground(Color.green);

		// Ventana principal (content).
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel(
				"Build System Generator configuration files for "
						+ spec.getName()));
		content.add(Box.createVerticalStrut(5));
		content.add(head);
		content.add(Box.createVerticalStrut(10));
		content.add(body);
		content.add(Box.createVerticalStrut(5));
		content.add(commandform);
		content.add(barra);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setIconImage(XfuzzyIcons.xfuzzy.getImage());
		pack();
		setVisible(true);
		setLocation();
	}

	/**
	 * Muestra la ventana de selección del fichero .xfl de entrada
	 */
	private void selectXFLFile() {
		File wdir = new File(XfsgProperties.outputDirectory);
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
	 * Muestra la ventana de selección del directorio de salida
	 */
	private void selectOutputDirectory() {
		File wdir = new File(XfsgProperties.outputDirectory);
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Output Directory");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		filesInformation[2].setText(chooser.getSelectedFile().toString());
		XfsgProperties.outputDirectory = chooser.getSelectedFile().toString();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// FUNCIONES QUE DESARROLLAN LAS INTERFACES
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	 * Muestra las funciones incluidas en el Package xfgs
	 */
	private void mostrar_info() {
		infor = new JDialog();
		infor.setVisible(true);

		JTextArea textarea = new JTextArea(30, 50);
		textarea.setBackground(XConstants.textbackground);
		textarea
				.setText("UNARY FUNCTIONS\n"
						+ "	- Negation (C-norm)\n"
						+ "\n"
						+ "BINARY FUNCTIONS\n"
						+ "	- Minimum (T-norm)\n"
						+ "	- Product (T-norm)\n"
						+ "	- Maximum (S-norm)\n"
						+ "	- Sum (S-norm)\n"
						+ "\n"
						+ "MEMBERSHIP FUNCTIONS\n"
						+ "	- Singleton (non fuzzy) membership function\n"
						+ "	- A list of parameters (used in Takagi-Sugeno defuzzification)\n"
						+ "	- Trapezoidal membership function\n"
						+ "	- Triangular membership function\n"
						+ "	- Triangular membership function (an isosceles triangle)\n"
						+ "	- Gaussian membership function\n"
						+ "	- Rectangular (non fuzzy) membership function\n"
						+ "\n"
						+ "FAMILIES OF MEMBERSHIP FUNCTIONS\n"
						+ "	- Family of triangular MFs (overlapping degree = 2)\n"
						+ "\n"
						+ "DEFUZZIFICATION METHODS\n"
						+ "	- Fuzzy Mean (Simplified - Mamdani type)\n"
						+ "	- Weighted Fuzzy Mean (Simplified - Mamdani type)\n"
						+ "	- Most activated label (Classification method)\n"
						+ "	- Takagi-Sugeno (1st-order)\n"
						+ "	- Quality (Simplified - Mamdani type)\n"
						+ "	- Gamma-Quality (Simplified - Mamdani type)\n"
						+ "\n" + "CRISP FUNCTIONS\n"
						+ "	- Addition of N numbers\n"
						+ "	- Addition of two numbers\n"
						+ "	- Addition of two angles (in degrees)\n"
						+ "	- Addition of two angles (in radians)\n"
						+ "	- Difference between two numbers\n"
						+ "	- Difference between two angles (in degrees)\n"
						+ "	- Difference between two angles (in radians)\n"
						+ "	- Product of two numbers\n"
						+ "	- Division of two numbers\n"
						+ "	- Selection between N numbers\n");

		String lb[] = { "Ok" };
		XCommandForm form = new XCommandForm(lb, lb, this);
		form.setCommandWidth(100);
		form.block();

		Container content = infor.getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel("Functions included in the xfsg Package"));
		content.add(new JScrollPane(textarea));
		content.add(form);

		infor.setFont(XConstants.font);
		infor.setIconImage(XfuzzyIcons.xfuzzy.getImage());
		infor.pack();
	}

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode flc = null;
		DefaultMutableTreeNode crisp = null;

		category = new DefaultMutableTreeNode(new Rama("RuleBases"));

		top.add(category);

		XfsgLeerXfl xfl = new XfsgLeerXfl(spec);
		xfl.inicializarFLCs();

		ArrayList<XfsgFLC> arrayflcs = xfl.getlistaflc();

		for (int i = 0; i < arrayflcs.size(); i++) {
			flc = new DefaultMutableTreeNode(arrayflcs.get(i));
			category.add(flc);
		}

		xfl.inicializarCrispBlocks();
		ArrayList<XfsgCrisp> arraycrisp = xfl.getlistacrisp();
		if (arraycrisp.size() != 0) {
			category = new DefaultMutableTreeNode(new Rama("CrispBlocks"));
			top.add(category);
			for (int i = 0; i < arraycrisp.size(); i++) {
				crisp = new DefaultMutableTreeNode(arraycrisp.get(i));
				category.add(crisp);
			}
		}
		XfsgError err = new XfsgError();
		// System.out.println(err.hasErrors());
		if (err.hasErrors() || err.hasWarnings()) {
			msg.addMessage(err.getMessages());
			msg.show();
			err.resetAll();
		}
	}

	public void generate_file() {
		if (hilo == null) {
			hilo = new ThreadCarga();
			hilo.start();
		}
	}

	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {
		try {
			flc = null;
			crisp = null;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			if (node == null)
				return;

			Object nodeInfo = node.getUserObject();
			if (node.isLeaf()) {
				if (nodeInfo instanceof XfsgFLC) {
					flc = (XfsgFLC) nodeInfo;
					boxflc = flc.gui();
					right = new JScrollPane(boxflc);
					splitPane.setBottomComponent(right);
				} else if (nodeInfo instanceof XfsgCrisp) {
					crisp = (XfsgCrisp) nodeInfo;
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

						} else if (auxInfo instanceof XfsgFLC) {
							res = ((XfsgFLC) auxInfo).getname() + "\n";
							JLabel etiq3 = new JLabel(res, XfsgIcons.rulebase,
									SwingConstants.CENTER);
							etiq3.setVerticalTextPosition(SwingConstants.TOP);
							jp.add(etiq3);
						} else if (auxInfo instanceof XfsgCrisp) {
							res = ((XfsgCrisp) auxInfo).getname() + "\n";
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

	// @Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if (command.equals("Close"))
			close();
		else if (command.equals("SelectXFLFile"))
			selectXFLFile();
		else if (command.equals("SelectOutputDirectory"))
			selectOutputDirectory();
		else if (command.equals("Infor"))
			mostrar_info();
		else if (command.equals("Ok"))
			infor.setVisible(false);
		else if (command.equals("Apply"))
			Actualizar_valores();
		else if (command.equals("Load Configuration"))
			selectConfigFile();
		else if (command.equals("Save Configuration"))
			saveConfigFile();
		else if (command.equals("Generate Files"))
			generate_file();
	}

	private void close() {
		setVisible(false);
		XfsgProperties.activar_boton_GMF = false;
	}

	// @Override
	public void windowActivated(WindowEvent arg0) {
	}

	// @Override
	public void windowClosed(WindowEvent arg0) {
		close();
	}

	// @Override
	public void windowClosing(WindowEvent arg0) {
		close();
	}

	// @Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	// @Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	// @Override
	public void windowIconified(WindowEvent arg0) {
	}

	// @Override
	public void windowOpened(WindowEvent arg0) {
	}

	// @Override
	public void mouseClicked(MouseEvent arg0) {
	}

	// @Override
	public void mouseEntered(MouseEvent arg0) {
	}

	// @Override
	public void mouseExited(MouseEvent arg0) {
	}

	// @Override
	public void mousePressed(MouseEvent arg0) {
	}

	// @Override
	public void mouseReleased(MouseEvent arg0) {
	}

	// @Override
	public void keyPressed(KeyEvent arg0) {
	}

	// @Override
	public void keyReleased(KeyEvent arg0) {
	}

	// @Override
	public void keyTyped(KeyEvent arg0) {
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

	private int valida_string(String s) {
		int res;
		if (s.isEmpty())
			res = 0;
		else
			res = Integer.parseInt(s);
		return res;
	}

	// Función que actualiza los valores de los nodos de los arboles
	public void Actualizar_valores() {
		if (flc != null) {
			// String N_aux=
			int N = valida_string((((XTextForm) boxflc.getComponent(3))
					.getText()));
			int No = valida_string((((XTextForm) boxflc.getComponent(4))
					.getText()));
			int grad = valida_string((((XTextForm) boxflc.getComponent(5))
					.getText()));
			int P = valida_string((((XTextForm) boxflc.getComponent(6))
					.getText()));
			flc.setN(N);
			flc.setNo(No);
			flc.setgrad(grad);
			flc.setP(P);
			if (N != 0 & No != 0 & grad != 0 & P != 0) {
				flc.settodo_relleno(true);
			}
			if (N == 0 | No == 0 | grad == 0 | P == 0) {
				flc.settodo_relleno(false);
			}
			// flc=null;
		} else if (crisp != null) {
			int grad = Integer.parseInt(((XTextForm) boxcrisp.getComponent(3))
					.getText());
			crisp.setNo(grad);
			if (grad != 0) {
				crisp.settodo_relleno(true);
			}
			if (grad == 0)
				crisp.settodo_relleno(false);
			// crisp=null;
		}
		activarHijos(top);
		activarPadre(top);
		tree.repaint();
	}

	private void activarPadre(DefaultMutableTreeNode node) {
		int n = node.getChildCount();
		boolean cambiar = true;
		for (int i = 0; i < n & cambiar; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			Object o = dmtn.getUserObject();
			if (!((XfsgWindow.Rama) o).getCompleta())
				cambiar = false;
		}
		if (cambiar) {
			XfsgProperties.activar_boton_GMF = true;
			// toolb.setEnabled(true);
			commandform.setEnabled(4, true);
			commandform.setEnabled(1, true);
		} else {
			XfsgProperties.activar_boton_GMF = false;
			// toolb.setEnabled(false);
			commandform.setEnabled(4, false);
			commandform.setEnabled(1, false);
		}
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
			if (o instanceof XfsgFLC) {
				XfsgFLC f1 = (XfsgFLC) o;
				if (!f1.gettodo_relleno())
					cambiar = false;
			} else if (o instanceof XfsgCrisp) {
				XfsgCrisp c1 = (XfsgCrisp) o;
				if (!c1.gettodo_relleno())
					cambiar = false;
			}
		}
		if (cambiar)
			((XfsgWindow.Rama) nodeInfo).setCompleta(true);
		else
			((XfsgWindow.Rama) nodeInfo).setCompleta(false);
	}

	/**
	 * Muestra la ventana de selección del fichero de configuración
	 */
	private void selectConfigFile() {
		File wdir = new File(XfsgProperties.outputDirectory);

		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfsg configuration files (.xml)", "xml");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load Configuration");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;

		XfsgProperties.fichero_config = chooser.getSelectedFile().toString();
		XfsgLeerXML lector = new XfsgLeerXML(top, include_confid, gen_txtfile,
				gen_simmodel, simple);
		lector.leer_xml();
		activarHijos(top);
		activarPadre(top);
		tree.repaint();

		XfsgError err = new XfsgError();
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
			if (nodeInfo instanceof XfsgFLC) {
				flc = (XfsgFLC) nodeInfo;
				boxflc = flc.gui();
				right = new JScrollPane(boxflc);
				splitPane.setBottomComponent(right);
			} else if (nodeInfo instanceof XfsgCrisp) {
				crisp = (XfsgCrisp) nodeInfo;
				boxcrisp = crisp.gui();
				right = new JScrollPane(boxcrisp);
				splitPane.setBottomComponent(right);
			}
		}
	}

	/**
	 * Muestra la ventana de selección del fichero de configuración
	 */
	private void saveConfigFile() {
		File f = new File(XfsgProperties.outputDirectory + "\\"
				+ XfsgProperties.name_outputfiles + ".xml");
		JFileChooser chooser = new JFileChooser(f);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfsg configuration files (.xml)", "xml");
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

		XfsgLeerXML escritura = new XfsgLeerXML(top, include_confid,
				gen_txtfile, gen_simmodel, simple);
		String xml = escritura.escribir_xml();

		try {
			// String oldname = "config.xml";
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream stream = new PrintStream(fos);
			stream.print(xml);
			stream.close();
			// if(!oldname.equals(""+file.getName())) xfuzzy.log("Config
			// "+oldname+" renamed to "+file.getName()+".");
			xfuzzy.log("Config " + file.getName() + " saved as "
					+ file.getAbsolutePath() + ".");

		} catch (FileNotFoundException e) {
			xfuzzy.log("Can't save specification : " + file.getName() + ".");
			e.printStackTrace();
		}
	}

	public String[] genera_texto_rama(DefaultMutableTreeNode node) {
		// contiene la información a escribir en el archivo .m
		String code = "";
		// contiene la información a escribir en el archivo .txt
		String code2 = "";
		// contiene la información a escribir en el archivo .mdl
		String code3 = "";
		String[] res = new String[3];
		int n = node.getChildCount();
		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			Object o = dmtn.getUserObject();
			if (o instanceof XfsgFLC) {

				XfsgFLC f1 = (XfsgFLC) o;
				// El fichero .m se crea siempre
				if (i == 0) {
					code += "% --------------------------------------------------------------\n";
					code += "%           Rulebases\n";
					code += "% --------------------------------------------------------------\n";
				}
				f1.generaM(include_confid);
				code += f1.getM();

				if (gen_txtfile.isSelected()) {
					f1.generaTxt();
					code2 += f1.getTxt();
				}
				if (gen_simmodel.isSelected()) {
					f1.generaMdl();
					code3 += f1.getMdl();
				}
			} else if (o instanceof XfsgCrisp) {

				XfsgCrisp c1 = (XfsgCrisp) o;
				// El fichero .m se crea siempre
				if (i == 0) {
					code += "% --------------------------------------------------------------\n";
					code += "%           Crisp Blocks\n";
					code += "% --------------------------------------------------------------\n";

				}
				c1.generaM();
				code += c1.getM();

				if (gen_txtfile.isSelected()) {
					c1.generaTxt();
					code2 += c1.getTxt();
				}
				if (gen_simmodel.isSelected()) {
					c1.generaMdl();
					code3 += c1.getMdl();
				}
			}
		}

		res[0] = code;
		res[1] = code2;
		res[2] = code3;
		XfsgError err = new XfsgError();
		// System.out.println(err.hasErrors());
		if (err.hasErrors() || err.hasWarnings()) {
			msg.addMessage(err.getMessages());
			msg.show();
			err.resetAll();
		}
		return res;
	}

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

	// CLASE INTERNA

	class ThreadCarga extends Thread {
		public void run() {
			int min = 0;
			int max = 100;
			XfsgProperties.ficheroXFL = spec.getFile().getAbsolutePath();
			// XfsgProperties.ficheroXFL = filesInformation[0].getText();
			// XfsgProperties.outputDirectory = filesInformation[2].getText();
			XfsgProperties.name_outputfiles = filesInformation[1].getText();
			// String state="Done";
			XfsgMessage msg = new XfsgMessage(xfuzzy);
			msg.addMessage("  >>>> Generating files:  \n");
			msg.addMessage("	+  "
					+ XfsgProperties.name_outputfiles.replace(" ", "_")
					+ ".m\n");

			if (gen_txtfile.isSelected()) {
				msg.addMessage("	+  "
						+ XfsgProperties.name_outputfiles.replace(" ", "_")
						+ ".txt\n");
			}
			if (gen_simmodel.isSelected()) {
				msg.addMessage("	+  "
						+ XfsgProperties.name_outputfiles.replace(" ", "_")
						+ "_aux" + ".mdl\n");
			}
			msg.show();
			// calcula el número de llamadas a módulos (rulebase o crisp
			// block) que existen en el sistema.
			// new
			// XfsgArchitecturesSimulink(spec.getSystemModule().getModuleCalls().length);
			String code = "", code2 = "", code3 = "";
			try {
				barra.setValue(min);
				barra.setMinimum(min);
				barra.setMaximum(max);
				// Sustituye los espacios en blanco del nombre de la
				// especificación por "_"
				String newname = XfsgProperties.name_outputfiles.replace(" ",
						"_");
				// El fichero .m se crea siempre
				XfsgHeadFile head = new XfsgHeadFile(
						XfsgProperties.outputDirectory, newname + ".m",
						XfsgProperties.ficheroXFL);

				code = head.getMHead() + "\n";

				if (gen_txtfile.isSelected()) {
					XfsgHeadFile head2 = new XfsgHeadFile(
							XfsgProperties.outputDirectory, newname + ".txt",
							XfsgProperties.ficheroXFL);

					code2 = head2.getMHead() + "\n";
				}

				if (gen_simmodel.isSelected()) {
					XfsgHeadFile head3 = new XfsgHeadFile(
							XfsgProperties.outputDirectory, newname + "_aux"
									+ ".mdl", XfsgProperties.ficheroXFL);

					code3 = head3.getMHead() + "\n";

					code3 += "Model {\n"
							+ "	Name			  "
							+ newname
							+ "_mdl"
							+ "\n"
							+ "	System {\n"
							+ "		Name		    "
							+ newname
							+ "_mdl"
							+ "\n"
							+ "		Open		    on\n"
							+ "		ModelBrowserVisibility  off\n"
							+ "		ModelBrowserWidth	    200\n"
							+
							// " ScreenColor \"[1.000000, 0.980392,
							// 0.803922]\"\n"+
							"		PaperOrientation	    \"landscape\"\n"
							+ "		PaperPositionMode	    \"auto\"\n"
							+ "		PaperType		    \"A4\"\n"
							+ "		PaperUnits		    \"centimeters\"\n"
							+ "		TiledPaperMargins	    [0.500000, 0.500000, 0.500000, 0.500000]\n"
							+ "		TiledPageScale	    1\n"
							+ "		ShowPageBoundaries	    off\n"
							+ "		ZoomFactor		    \"100\"\n"
							+ "		ReportName		    \"simulink-default.rpt\"\n";
				}

				barra.setValue(25);

				if (simple.isSelected())
					XfsgProperties.use_components_simplified = true;
				else
					XfsgProperties.use_components_simplified = false;

				int n = top.getChildCount();
				for (int i = 0; i < n; i++) {
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) top
							.getChildAt(i);
					String[] aux = genera_texto_rama(dmtn);
					code += aux[0];
					code2 += aux[1];
					code3 += aux[2];
					barra.setValue(25 + (25 * (i + 1)));
				}

				// El fichero .m se crea siempre
				// Crea el fichero .m
				new XfsgCreateFile(newname + ".m", code);

				if (gen_txtfile.isSelected()) {
					// Crea el fichero .txt
					new XfsgCreateFile(newname + ".txt", code2);
				}

				if (gen_simmodel.isSelected()) {
					code3 += XfsgArchitecturesSimulink.generaConexiones(spec);
					code3 += "	}\n}\n";
					// Crea el fichero .mdl
					new XfsgCreateFile(newname + "_aux" + ".mdl", code3);
				}

				barra.setValue(90);
				sleep(400);
				barra.setValue(100);

				// barra.setValue(0);
				msg.addMessage("     .........    Files generated");
				msg.show();
				hilo = null;
			} catch (Exception e) {
				e.printStackTrace();
				msg.addMessage(" ........... Error");
				msg.show();
			}

		}
	}
}
