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



package xfuzzy.xfpkg;

import xfuzzy.lang.*;
import xfuzzy.pkg.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase que desarrolla la ventana de edición de la definición de una
 * función de un paquete
 * 
 * @author Francisco José MorenoVelo
 */
public class XfpkgDefinition extends JDialog
implements ActionListener, WindowListener {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603050L;
	
	/**
	 * Campo para introducir el nombre de la función
	 */
	private XTextForm defname;

	/**
	 * Panel de botones
	 */
	private XCommandForm commandform;
	
	/**
	 * Panel para introducir los nombres alternativos
	 */
	private XfpkgVectorPanel aliaspanel;
	
	/**
	 * Panel para introducir los nombres de los parámetros
	 */
	private XfpkgVectorPanel parampanel;
	
	/**
	 * Panel para introducir las funciones de pertenencias admitidas en un
	 * método de concreción
	 */
	private XfpkgVectorPanel definedpanel;
	
	/**
	 * Campo para introducir el nombre de la lista de parámetros
	 */
	private XTextForm paramlistname;
	
	/**
	 * Campo para introducir los requisitos de los parámetros
	 */
	private JTextArea requires_area;
	
	/**
	 * Campo para introducir el código de actualización de los parámetros
	 */
	private JTextArea update_area;
	
	/**
	 * Campo para introducir el código para calcular el número de entradas
	 * de una función no difusa
	 */
	private JTextArea inputs_area;
	
	/**
	 * Campo para introducir el código para calcular el número de miembros
	 * de una familia de funciones de pertenencia
	 */
	private JTextArea members_area;
	
	/**
	 * Campo para introducir el código Java que evalúa la función
	 */
	private JTextArea javaeq_area;
	
	/**
	 * Campo para introducir el código Java que evalúa el modificador "Mayor o igual"
	 */
	private JTextArea javagreq_area;
	
	/**
	 * Campo para introducir el código Java que evalúa el modificador "Menor o igual"
	 */
	private JTextArea javasmeq_area;
	
	/**
	 * Campo para introducir el código Java para calcular el centro de una función
	 */
	private JTextArea javacenter_area;
	
	/**
	 * Campo para introducir el código Java para calcular la base de una función
	 */
	private JTextArea javabasis_area;
	
	/**
	 * Campo para introducir el código C que evalúa la función
	 */
	private JTextArea ansiceq_area;
	
	/**
	 * Campo para introducir el código C que evalúa el modificador "Mayor o igual"
	 */
	private JTextArea ansicgreq_area;
	
	/**
	 * Campo para introducir el código C que evalúa el modificador "Menor o igual"
	 */
	private JTextArea ansicsmeq_area;
	
	/**
	 * Campo para introducir el código C para calcular el centro de una función
	 */
	private JTextArea ansiccenter_area;
	
	/**
	 * Campo para introducir el código C para calcular la base de una función
	 */
	private JTextArea ansicbasis_area;
	
	/**
	 * Campo para introducir el código C++ que evalúa la función
	 */
	private JTextArea cppeq_area;
	
	/**
	 * Campo para introducir el código C++ que evalúa el modificador "Mayor o igual"
	 */
	private JTextArea cppgreq_area;
	
	/**
	 * Campo para introducir el código C++ que evalúa el modificador "Menor o igual"
	 */
	private JTextArea cppsmeq_area;
	
	/**
	 * Campo para introducir el código C++ para calcular el centro de una función
	 */
	private JTextArea cppcenter_area;
	
	/**
	 * Campo para introducir el código C++ para calcular la base de una función
	 */
	private JTextArea cppbasis_area;
	
	/**
	 * Campo para introducir el código Java de la derivada de la función
	 */
	private JTextArea deriveq_area;
	
	/**
	 * Campo para introducir el código Java de la derivada del modificador "Mayor o igual"
	 */
	private JTextArea derivgreq_area;
	
	/**
	 * Campo para introducir el código Java de la derivada del modificador "Menor o igual"
	 */
	private JTextArea derivsmeq_area;
	
	/**
	 * Campo para introducir el código Java de la derivada del centro de una función
	 */
	private JTextArea derivcenter_area;
	
	/**
	 * Campo para introducir el código Jave de la derivada de la base de una función
	 */
	private JTextArea derivbasis_area;
	
	/**
	 * Campo para introducir código adicional en la clase
	 */
	private JTextArea source_area;
	
	/**
	 * Referencia al objeto Xfpkg del que depende la ventana
	 */
	private Xfpkg xfpkg;
	
	/**
	 * Definición que se está editando
	 */
	private Definition definition;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	public XfpkgDefinition(Xfpkg xfpkg, Definition definition) {
		super(xfpkg,"Xfpkg",false);
		this.xfpkg = xfpkg;
		this.definition = definition;
		this.definition.setEditor(this);
		build();
		setValues();
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Abre una ventana de edición de una definición
	 */
	static void showDefinitionEditor(Xfpkg xfpkg, Definition definition) {
		XfpkgDefinition editor = new XfpkgDefinition(xfpkg,definition);
		editor.setVisible(true);
		editor.repaint();
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Edit")) setEditable(true);
		else if(command.equals("Apply")) apply();
		else if(command.equals("Reload")) setValues();
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
	 * Interfaz WindowListener. Acción al terminar de cerrar la ventana
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

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	//----------------------------------------------------------------------------//
	// Métodos para construir la interfaz grafica                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Genera el contenido de la ventana
	 */
	private void build() {
		int kind = definition.getKind();
		
		JTabbedPane tabbed = new JTabbedPane();
		tabbed.addTab(" Alias ", buildAliasPanel());
		tabbed.addTab("Parameters", buildParamPanel());
		tabbed.addTab("Requirements",buildRequiresPanel(kind));
		if(kind == XflPackage.CRISP) tabbed.addTab("Inputs",buildInputsPanel());
		if(kind == XflPackage.FAMILY) tabbed.addTab("Members",buildMembersPanel());
		tabbed.addTab(" Java ", buildJavaPanel(kind));
		tabbed.addTab("  C  ", buildAnsiCPanel(kind));
		tabbed.addTab("  C++  ", buildCppPanel(kind));
		if(kind != XflPackage.CRISP && kind != XflPackage.DEFUZ)
			tabbed.addTab("Derivative", buildDerivPanel(kind));
		if(kind == XflPackage.FAMILY) tabbed.addTab("Update",buildUpdatePanel(kind));
		if(kind == XflPackage.MFUNC) tabbed.addTab("Update",buildUpdatePanel(kind));
		tabbed.addTab("Source", buildSourcePanel());
		
		defname = new XTextForm("Name");
		defname.setLabelWidth(100);
		defname.setFieldWidth(350);
		defname.setAlignment(JLabel.CENTER);
		defname.setEditable(false);
		
		String lb[] = {"Edit","Apply","Reload"};
		commandform = new XCommandForm(lb,lb,this);
		commandform.setCommandWidth(120);
		commandform.block();
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(defname);
		box.add(Box.createVerticalStrut(5));
		box.add(tabbed);
		box.add(Box.createVerticalStrut(5));
		box.add(commandform);
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(Box.createVerticalStrut(5));
		content.add(box);
		content.add(Box.createVerticalStrut(5));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setSize(new Dimension(800,400));
		setEditable(false);
		commandform.setEnabled(0,false);
	}
	
	//----------------------------------------------------------------------------//
	// Métodos de construcción de los paneles de los bloques                      //
	//----------------------------------------------------------------------------//
	
	/**
	 * Genera el panel del bloque "alias"
	 */
	private JPanel buildAliasPanel() {
		aliaspanel = new XfpkgVectorPanel(6);
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Alias",300));
		box.add(Box.createVerticalStrut(10));
		box.add(aliaspanel);
		box.add(Box.createVerticalStrut(20));
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}
	
	/**
	 * Genera el panel del bloque "parameter"
	 */
	private JPanel buildParamPanel() {
		parampanel = new XfpkgVectorPanel(6);
		paramlistname = new XTextForm("List name");
		paramlistname.setLabelWidth(100);
		paramlistname.setFieldWidth(200);
		paramlistname.setAlignment(JButton.CENTER);
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Parameters",300));
		box.add(Box.createVerticalStrut(10));
		box.add(parampanel);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("List of parameters",300));
		box.add(Box.createVerticalStrut(10));
		box.add(paramlistname);
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el panel del bloque "requires"
	 */
	private JPanel buildRequiresPanel(int kind) {
		String text = "";
		switch(kind) {
		case XflPackage.UNARY: text = "parameters"; break;
		case XflPackage.BINARY: text = "parameters"; break;
		case XflPackage.MFUNC: text = "parameters, min, max"; break;
		case XflPackage.FAMILY: text = "parameters, min, max"; break;
		case XflPackage.CRISP: text = "parameters"; break;
		case XflPackage.DEFUZ: text = "parameters, min, max, step"; break;
		}
		XTextForm varform = new XTextForm("Variables");
		varform.setLabelWidth(100);
		varform.setFieldWidth(200);
		varform.setAlignment(JButton.CENTER);
		varform.setEditable(false);
		varform.setText(text);
		
		if(kind == XflPackage.DEFUZ) {
			requires_area = new JTextArea(3,30);
			requires_area.setBackground(XConstants.textbackground);
			requires_area.setBorder(BorderFactory.createLoweredBevelBorder());
			definedpanel = new XfpkgVectorPanel(6);
		} else {
			requires_area = new JTextArea(8,30);
			requires_area.setBackground(XConstants.textbackground);
			requires_area.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Requirements",300));
		box.add(Box.createVerticalStrut(10));
		box.add(varform);
		box.add(Box.createVerticalStrut(10));
		box.add(new JScrollPane(requires_area));
		box.add(Box.createVerticalStrut(20));
		if(kind == XflPackage.DEFUZ) {
			box.add(createLabel("Defined for membership functions",300));
			box.add(Box.createVerticalStrut(10));
			box.add(definedpanel);
			box.add(Box.createVerticalStrut(20));
		}
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}
	
	/**
	 * Genera el panel del bloque "Inputs"
	 */
	private JPanel buildInputsPanel() {
		XTextForm varform = new XTextForm("Variables");
		varform.setLabelWidth(100);
		varform.setFieldWidth(200);
		varform.setAlignment(JButton.CENTER);
		varform.setEditable(false);
		varform.setText("parameters");
		
		inputs_area = new JTextArea(8,30);
		inputs_area.setBackground(XConstants.textbackground);
		inputs_area.setBorder(BorderFactory.createLoweredBevelBorder());
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Number of Input Variables",300));
		box.add(Box.createVerticalStrut(10));
		box.add(varform);
		box.add(Box.createVerticalStrut(10));
		box.add(new JScrollPane(inputs_area));
		box.add(Box.createVerticalStrut(20));
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el panel del bloque "Members"
	 */
	private JPanel buildMembersPanel() {
		XTextForm varform = new XTextForm("Variables");
		varform.setLabelWidth(100);
		varform.setFieldWidth(200);
		varform.setAlignment(JButton.CENTER);
		varform.setEditable(false);
		varform.setText("parameters");
		
		members_area = new JTextArea(8,30);
		members_area.setBackground(XConstants.textbackground);
		members_area.setBorder(BorderFactory.createLoweredBevelBorder());
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Number of Family Members",300));
		box.add(Box.createVerticalStrut(10));
		box.add(varform);
		box.add(Box.createVerticalStrut(10));
		box.add(new JScrollPane(members_area));
		box.add(Box.createVerticalStrut(20));
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el panel del bloque "update"
	 */
	private JPanel buildUpdatePanel(int kind) {
		XTextForm varform = new XTextForm("Variables");
		varform.setLabelWidth(100);
		varform.setFieldWidth(200);
		varform.setAlignment(JButton.CENTER);
		varform.setEditable(false);
		varform.setText("parameters, pos[], desp[], adj[], min, max, step");
		
		update_area = new JTextArea(8,30);
		update_area.setBackground(XConstants.textbackground);
		update_area.setBorder(BorderFactory.createLoweredBevelBorder());
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Parameters update",300));
		box.add(Box.createVerticalStrut(10));
		box.add(varform);
		box.add(Box.createVerticalStrut(10));
		box.add(new JScrollPane(update_area));
		box.add(Box.createVerticalStrut(20));
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el contenido del panel del bloque "java"	
	 */
	private JPanel buildJavaPanel(int kind) {
		Box box;
		
		if(kind == XflPackage.MFUNC || kind == XflPackage.FAMILY) {
			boolean fam = (kind == XflPackage.FAMILY);
			String functext = "parameters,"+(fam?" i,":"")+" x, min, max";
			String proptext = "parameters,"+(fam?" i,":"")+" min, max";
			
			javaeq_area = new JTextArea(8,30);
			javaeq_area.setBackground(XConstants.textbackground);
			javaeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			javagreq_area = new JTextArea(8,30);
			javagreq_area.setBackground(XConstants.textbackground);
			javagreq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			javasmeq_area = new JTextArea(8,30);
			javasmeq_area.setBackground(XConstants.textbackground);
			javasmeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			javacenter_area = new JTextArea(8,30);
			javacenter_area.setBackground(XConstants.textbackground);
			javacenter_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			javabasis_area = new JTextArea(8,30);
			javabasis_area.setBackground(XConstants.textbackground);
			javabasis_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			JTabbedPane tabbed = new JTabbedPane();
			tabbed.addTab(" Membership ", buildAreaPanel(javaeq_area, functext));
			tabbed.addTab("Greater or Equal", buildAreaPanel(javagreq_area, functext));
			tabbed.addTab("Smaller or Equal", buildAreaPanel(javasmeq_area, functext));
			tabbed.addTab(" Center ", buildAreaPanel(javacenter_area, proptext));
			tabbed.addTab(" Basis ", buildAreaPanel(javabasis_area, proptext));
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("Java description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(tabbed);
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		} else {
			String text = "";
			switch(kind) {
			case XflPackage.UNARY: text = "parameters, a"; break;
			case XflPackage.BINARY: text = "parameters, a, b"; break;
			case XflPackage.CRISP: text = "parameters, x"; break;
			case XflPackage.DEFUZ: text = "parameters, mf, min, max, step"; break;
			}
			XTextForm varform = new XTextForm("Variables");
			varform.setLabelWidth(100);
			varform.setFieldWidth(200);
			varform.setAlignment(JButton.CENTER);
			varform.setEditable(false);
			varform.setText(text);
			
			javaeq_area = new JTextArea(8,30);
			javaeq_area.setBackground(XConstants.textbackground);
			javaeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("Java description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(varform);
			box.add(Box.createVerticalStrut(10));
			box.add(new JScrollPane(javaeq_area));
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el contenido del panel del bloque "ansi_c"
	 */
	private JPanel buildAnsiCPanel(int kind) {
		Box box;
		
		if(kind == XflPackage.MFUNC || kind == XflPackage.FAMILY) {
			boolean fam = (kind == XflPackage.FAMILY);
			String functext = "parameters,"+(fam?" i,":"")+" x, min, max";
			String proptext = "parameters,"+(fam?" i,":"")+" min, max";
			
			ansiceq_area = new JTextArea(8,30);
			ansiceq_area.setBackground(XConstants.textbackground);
			ansiceq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			ansicgreq_area = new JTextArea(8,30);
			ansicgreq_area.setBackground(XConstants.textbackground);
			ansicgreq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			ansicsmeq_area = new JTextArea(8,30);
			ansicsmeq_area.setBackground(XConstants.textbackground);
			ansicsmeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			ansiccenter_area = new JTextArea(8,30);
			ansiccenter_area.setBackground(XConstants.textbackground);
			ansiccenter_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			ansicbasis_area = new JTextArea(8,30);
			ansicbasis_area.setBackground(XConstants.textbackground);
			ansicbasis_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			JTabbedPane tabbed = new JTabbedPane();
			tabbed.addTab(" Membership ", buildAreaPanel(ansiceq_area, functext));
			tabbed.addTab("Greater or Equal", buildAreaPanel(ansicgreq_area, functext));
			tabbed.addTab("Smaller or Equal", buildAreaPanel(ansicsmeq_area, functext));
			tabbed.addTab(" Center ", buildAreaPanel(ansiccenter_area, proptext));
			tabbed.addTab(" Basis ", buildAreaPanel(ansicbasis_area, proptext));
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("C description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(tabbed);
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		} else {
			String text = "";
			switch(kind) {
			case XflPackage.UNARY: text = "parameters, a"; break;
			case XflPackage.BINARY: text = "parameters, a, b"; break;
			case XflPackage.CRISP: text = "parameters, x"; break;
			case XflPackage.DEFUZ: text = "parameters, mf, min, max, step"; break;
			}
			XTextForm varform = new XTextForm("Variables");
			varform.setLabelWidth(100);
			varform.setFieldWidth(200);
			varform.setAlignment(JButton.CENTER);
			varform.setEditable(false);
			varform.setText(text);
			
			ansiceq_area = new JTextArea(8,30);
			ansiceq_area.setBackground(XConstants.textbackground);
			ansiceq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("C description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(varform);
			box.add(Box.createVerticalStrut(10));
			box.add(new JScrollPane(ansiceq_area));
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el contenido del panel del bloque "cplusplus"
	 */
	private JPanel buildCppPanel(int kind) {
		Box box;
		
		if(kind == XflPackage.MFUNC || kind == XflPackage.FAMILY) {
			boolean fam = (kind == XflPackage.FAMILY);
			String functext = "parameters,"+(fam?" i,":"")+" x, min, max";
			String proptext = "parameters,"+(fam?" i,":"")+" min, max";
			
			cppeq_area = new JTextArea(8,30);
			cppeq_area.setBackground(XConstants.textbackground);
			cppeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			cppgreq_area = new JTextArea(8,30);
			cppgreq_area.setBackground(XConstants.textbackground);
			cppgreq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			cppsmeq_area = new JTextArea(8,30);
			cppsmeq_area.setBackground(XConstants.textbackground);
			cppsmeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			cppcenter_area = new JTextArea(8,30);
			cppcenter_area.setBackground(XConstants.textbackground);
			cppcenter_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			cppbasis_area = new JTextArea(8,30);
			cppbasis_area.setBackground(XConstants.textbackground);
			cppbasis_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			JTabbedPane tabbed = new JTabbedPane();
			tabbed.addTab(" Membership ", buildAreaPanel(cppeq_area, functext));
			tabbed.addTab("Greater or Equal", buildAreaPanel(cppgreq_area, functext));
			tabbed.addTab("Smaller or Equal", buildAreaPanel(cppsmeq_area, functext));
			tabbed.addTab(" Center ", buildAreaPanel(cppcenter_area, proptext));
			tabbed.addTab(" Basis ", buildAreaPanel(cppbasis_area, proptext));
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("C++ description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(tabbed);
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		} else {
			String text = "";
			switch(kind) {
			case XflPackage.UNARY: text = "parameters, a"; break;
			case XflPackage.BINARY: text = "parameters, a, b"; break;
			case XflPackage.CRISP: text = "parameters, x"; break;
			case XflPackage.DEFUZ: text = "parameters, mf, min, max, step"; break;
			}
			XTextForm varform = new XTextForm("Variables");
			varform.setLabelWidth(100);
			varform.setFieldWidth(200);
			varform.setAlignment(JButton.CENTER);
			varform.setEditable(false);
			varform.setText(text);
			
			cppeq_area = new JTextArea(8,30);
			cppeq_area.setBackground(XConstants.textbackground);
			cppeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("C++ description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(varform);
			box.add(Box.createVerticalStrut(10));
			box.add(new JScrollPane(cppeq_area));
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el contenido del panel del bloque "derivative"
	 */
	private JPanel buildDerivPanel(int kind) {
		Box box;
		
		if(kind == XflPackage.MFUNC || kind == XflPackage.FAMILY) {
			boolean fam = (kind == XflPackage.FAMILY);
			String functext = "parameters, deriv[],"+(fam?" i,":"")+" x, min, max";
			String proptext = "parameters, deriv[],"+(fam?" i,":"")+" min, max";
			
			deriveq_area = new JTextArea(8,30);
			deriveq_area.setBackground(XConstants.textbackground);
			deriveq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			derivgreq_area = new JTextArea(8,30);
			derivgreq_area.setBackground(XConstants.textbackground);
			derivgreq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			derivsmeq_area = new JTextArea(8,30);
			derivsmeq_area.setBackground(XConstants.textbackground);
			derivsmeq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			derivcenter_area = new JTextArea(8,30);
			derivcenter_area.setBackground(XConstants.textbackground);
			derivcenter_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			derivbasis_area = new JTextArea(8,30);
			derivbasis_area.setBackground(XConstants.textbackground);
			derivbasis_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			JTabbedPane tabbed = new JTabbedPane();
			tabbed.addTab(" Membership ", buildAreaPanel(deriveq_area, functext));
			tabbed.addTab("Greater or Equal", buildAreaPanel(derivgreq_area, functext));
			tabbed.addTab("Smaller or Equal", buildAreaPanel(derivsmeq_area, functext));
			tabbed.addTab(" Center ", buildAreaPanel(derivcenter_area, proptext));
			tabbed.addTab(" Basis ", buildAreaPanel(derivbasis_area, proptext));
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("Derivative description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(tabbed);
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		} else {
			String text = "";
			switch(kind) {
			case XflPackage.UNARY: text = "parameters, deriv, a"; break;
			case XflPackage.BINARY: text = "parameters, deriv[], a, b"; break;
			}
			XTextForm varform = new XTextForm("Variables");
			varform.setLabelWidth(100);
			varform.setFieldWidth(200);
			varform.setAlignment(JButton.CENTER);
			varform.setEditable(false);
			varform.setText(text);
			
			deriveq_area = new JTextArea(8,30);
			deriveq_area.setBackground(XConstants.textbackground);
			deriveq_area.setBorder(BorderFactory.createLoweredBevelBorder());
			
			box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(20));
			box.add(createLabel("Derivative description",300));
			box.add(Box.createVerticalStrut(10));
			box.add(varform);
			box.add(Box.createVerticalStrut(10));
			box.add(new JScrollPane(deriveq_area));
			box.add(Box.createVerticalStrut(20));
			box.add(Box.createGlue());
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera el panel del bloque "source"
	 */
	private JPanel buildSourcePanel() {
		source_area = new JTextArea(8,30);
		source_area.setBackground(XConstants.textbackground);
		source_area.setBorder(BorderFactory.createLoweredBevelBorder());
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(20));
		box.add(createLabel("Additional code",300));
		box.add(Box.createVerticalStrut(10));
		box.add(new JScrollPane(source_area));
		box.add(Box.createVerticalStrut(20));
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		Dimension maxsize = panel.getMaximumSize();
		Dimension prefsize = panel.getPreferredSize();
		panel.setMaximumSize(new Dimension(maxsize.width,prefsize.height));
		return panel;
	}

	/**
	 * Genera un panel para un area de texto
	 */
	private JPanel buildAreaPanel(JTextArea area, String text) {
		XTextForm varform = new XTextForm("Variables");
		varform.setLabelWidth(100);
		varform.setFieldWidth(200);
		varform.setAlignment(JButton.CENTER);
		varform.setEditable(false);
		varform.setText(text);
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(10));
		box.add(varform);
		box.add(Box.createVerticalStrut(10));
		box.add(new JScrollPane(area));
		box.add(Box.createVerticalStrut(20));
		box.add(Box.createGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(25));
		panel.add(box);
		panel.add(Box.createHorizontalStrut(25));
		return panel;
	}
	
	//----------------------------------------------------------------------------//
	// Acciones a realizar respecto a las listas                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Actualiza el contenido de los paneles con la definición
	 */
	private void setValues() {
		String code;
		defname.setText(""+definition);
		
		aliaspanel.set(definition.getAlias());
		
		parampanel.set(definition.getParameters());
		code = definition.getParamList();
		paramlistname.setText((code == null? "" : code));
		
		int kind = definition.getKind();
		setValue(requires_area, Definition.REQUIREMENTS);
		setValue(javaeq_area, Definition.JAVA_EQUAL);
		setValue(ansiceq_area, Definition.C_EQUAL);
		setValue(cppeq_area, Definition.CPP_EQUAL);
		setValue(source_area, Definition.SOURCE);
		
		switch(kind) {
			case XflPackage.DEFUZ: 
				definedpanel.set(definition.getDefinedFor());
				break;
			case XflPackage.CRISP:
				setValue(inputs_area, Definition.INPUTS);
				break;
			case XflPackage.UNARY:
			case XflPackage.BINARY:
				setValue(deriveq_area, Definition.DERIV_EQUAL);
				break;
			case XflPackage.FAMILY:
				setValue(members_area, Definition.MEMBERS);
			case XflPackage.MFUNC:
				setValue(javagreq_area, Definition.JAVA_GREQ);
				setValue(javasmeq_area, Definition.JAVA_SMEQ);
				setValue(javacenter_area, Definition.JAVA_CENTER);
				setValue(javabasis_area, Definition.JAVA_BASIS);
				setValue(ansicgreq_area, Definition.C_GREQ);
				setValue(ansicsmeq_area, Definition.C_SMEQ);
				setValue(ansiccenter_area, Definition.C_CENTER);
				setValue(ansicbasis_area, Definition.C_BASIS);
				setValue(cppgreq_area, Definition.CPP_GREQ);
				setValue(cppsmeq_area, Definition.CPP_SMEQ);
				setValue(cppcenter_area, Definition.CPP_CENTER);
				setValue(cppbasis_area, Definition.CPP_BASIS);
				setValue(deriveq_area, Definition.DERIV_EQUAL);
				setValue(derivgreq_area, Definition.DERIV_GREQ);
				setValue(derivsmeq_area, Definition.DERIV_SMEQ);
				setValue(derivcenter_area, Definition.DERIV_CENTER);
				setValue(derivbasis_area, Definition.DERIV_BASIS);
				setValue(update_area, Definition.UPDATE);
				break;
		}
		
		setEditable(false);
	}
	
	/**
	 * Actualiza el contenido de un área de texto
	 */
	private void setValue(JTextArea area, int code) {
		String source = definition.getCode(code);
		area.setText((source == null? "" : source));
	}

	/**
	 * (Des)Habilita la edición del contenido de la definición
	 */
	private void setEditable(boolean editable) {
		boolean pkgeditable = xfpkg.getPackage().isEditable();
		defname.setEditable(editable && pkgeditable);
		aliaspanel.setEditable(editable && pkgeditable);
		parampanel.setEditable(editable && pkgeditable);
		paramlistname.setEditable(editable && pkgeditable);
		
		requires_area.setEditable(editable && pkgeditable);
		javaeq_area.setEditable(editable && pkgeditable);
		ansiceq_area.setEditable(editable && pkgeditable);
		cppeq_area.setEditable(editable && pkgeditable);
		source_area.setEditable(editable && pkgeditable);
		
		int kind = definition.getKind();
		switch(kind) {
			case XflPackage.DEFUZ:
				definedpanel.setEditable(editable && pkgeditable);
				break;
			case XflPackage.CRISP:
				inputs_area.setEditable(editable && pkgeditable);
				break;
			case XflPackage.UNARY:
			case XflPackage.BINARY:
				deriveq_area.setEditable(editable && pkgeditable);
				break;
			case XflPackage.FAMILY:
				members_area.setEditable(editable && pkgeditable);
			case XflPackage.MFUNC:
				javagreq_area.setEditable(editable && pkgeditable);
				javasmeq_area.setEditable(editable && pkgeditable);
				javacenter_area.setEditable(editable && pkgeditable);
				javabasis_area.setEditable(editable && pkgeditable);
				ansicgreq_area.setEditable(editable && pkgeditable);
				ansicsmeq_area.setEditable(editable && pkgeditable);
				ansiccenter_area.setEditable(editable && pkgeditable);
				ansicbasis_area.setEditable(editable && pkgeditable);
				cppgreq_area.setEditable(editable && pkgeditable);
				cppsmeq_area.setEditable(editable && pkgeditable);
				cppcenter_area.setEditable(editable && pkgeditable);
				cppbasis_area.setEditable(editable && pkgeditable);
				deriveq_area.setEditable(editable && pkgeditable);
				derivgreq_area.setEditable(editable && pkgeditable);
				derivsmeq_area.setEditable(editable && pkgeditable);
				derivcenter_area.setEditable(editable && pkgeditable);
				derivbasis_area.setEditable(editable && pkgeditable);
				update_area.setEditable(editable && pkgeditable);
				break;
		}
		
		commandform.setEnabled(0,!editable && pkgeditable);
		commandform.setEnabled(1,editable && pkgeditable);
		commandform.setEnabled(2,editable && pkgeditable);
	}
	
	/**
	 * Construye una definicion con el contenido de los paneles
	 */
	private Definition read(int kind) {
		String code;
		Definition workcopy = createDefinition();
		boolean good = true;
		code = defname.getText();
		if(!XConstants.isIdentifier(code)) {
			XDialog.showMessage(this,"Definition name is not an identifier.");
			good = false;
		}
		workcopy.setName(code);
		workcopy.setAlias(aliaspanel.get());
		workcopy.setParameters(parampanel.get());
		
		code = paramlistname.getText();
		if(code.trim().length() != 0) workcopy.setParamList(code);
		else workcopy.setParamList(null);
		
		
		readValue(workcopy,requires_area,Definition.REQUIREMENTS);
		readValue(workcopy,javaeq_area,Definition.JAVA_EQUAL);
		readValue(workcopy,ansiceq_area,Definition.C_EQUAL);
		readValue(workcopy,cppeq_area,Definition.CPP_EQUAL);
		readValue(workcopy,source_area,Definition.SOURCE);
		
		switch(kind) {
			case XflPackage.DEFUZ:
				workcopy.setDefinedFor(definedpanel.get());
				break;
			case XflPackage.CRISP:
				readValue(workcopy,inputs_area,Definition.INPUTS);
				break;
			case XflPackage.UNARY:
			case XflPackage.BINARY:
				readValue(workcopy,deriveq_area,Definition.DERIV_EQUAL);
				break;
			case XflPackage.FAMILY:
				readValue(workcopy,members_area,Definition.MEMBERS);
			case XflPackage.MFUNC:
				readValue(workcopy,javagreq_area,Definition.JAVA_GREQ);
				readValue(workcopy,javasmeq_area,Definition.JAVA_SMEQ);
				readValue(workcopy,javacenter_area,Definition.JAVA_CENTER);
				readValue(workcopy,javabasis_area,Definition.JAVA_BASIS);
				readValue(workcopy,ansicgreq_area,Definition.C_GREQ);
				readValue(workcopy,ansicsmeq_area,Definition.C_SMEQ);
				readValue(workcopy,ansiccenter_area,Definition.C_CENTER);
				readValue(workcopy,ansicbasis_area,Definition.C_BASIS);
				readValue(workcopy,cppgreq_area,Definition.CPP_GREQ);
				readValue(workcopy,cppsmeq_area,Definition.CPP_SMEQ);
				readValue(workcopy,cppcenter_area,Definition.CPP_CENTER);
				readValue(workcopy,cppbasis_area,Definition.CPP_BASIS);
				readValue(workcopy,deriveq_area,Definition.DERIV_EQUAL);
				readValue(workcopy,derivgreq_area,Definition.DERIV_GREQ);
				readValue(workcopy,derivsmeq_area,Definition.DERIV_SMEQ);
				readValue(workcopy,derivcenter_area,Definition.DERIV_CENTER);
				readValue(workcopy,derivbasis_area,Definition.DERIV_BASIS);
				readValue(workcopy,update_area,Definition.UPDATE);
				break;
		}
		
		if(good) return workcopy;
		return null;
	}
	
	/**
	 * Lee el contenido de un área de texto
	 */
	private void readValue(Definition workcopy,JTextArea area, int code) {
		String text = area.getText();
		if(text.trim().length() != 0) workcopy.setCode(code,text);
	}
	
	//----------------------------------------------------------------------------//
	// Métodos asociados a la barra de comandos                                   //
	//----------------------------------------------------------------------------//
	
	/**
	 * Aplica los cambios a una definición editada
	 */
	private boolean apply() {
		PackageDefinition pkg = xfpkg.getPackage();
		Definition workcopy = read(definition.getKind());
		if(workcopy == null) return false;
		if(!workcopy.compile()) {
			String msg = "Errors in the compilation of the definition.";
			XDialog.showMessage(this,msg);
			return false;
		}
		if(definition != null) {
			if(!definition.getName().equals(workcopy.getName())) definition.unlink();
			pkg.remove(definition, definition.getKind());
		}
		definition = workcopy;
		pkg.add(definition, definition.getKind());
		pkg.setModified(true);
		setEditable(false);
		return true;
	}
	
	/**
	 * Cierra la ventana de edición
	 */
	private void close() {
		definition.setEditor(null);
		setVisible(false);
	}
	
	//----------------------------------------------------------------------------//
	// Métodos auxiliares                                                         //
	//----------------------------------------------------------------------------//
	
	/**
	 * Crea una etiqueta con una cierta anchura
	 */
	private JLabel createLabel(String label, int width) {
		JLabel jlabel = new JLabel(label);
		jlabel.setAlignmentX(0.5f);
		jlabel.setForeground(Color.black);
		Dimension minsize = jlabel.getMinimumSize();
		Dimension prefsize = jlabel.getPreferredSize();
		jlabel.setMinimumSize(new Dimension(width,minsize.height));
		jlabel.setPreferredSize(new Dimension(width,prefsize.height));
		return jlabel;
	}
	
	/**
	 * Crea una definicion de un cierto tipo
	 */
	private Definition createDefinition() {
		int kind = definition.getKind();
		PackageDefinition pkg = xfpkg.getPackage();
		switch(kind) {
			case PackageDefinition.BINARY: return Definition.createBinaryDefinition(""+pkg,"");
			case PackageDefinition.UNARY: return Definition.createUnaryDefinition(""+pkg,"");
			case PackageDefinition.MFUNC: return Definition.createMFDefinition(""+pkg,"");
			case PackageDefinition.DEFUZ: return Definition.createDefuzDefinition(""+pkg,"");
			case PackageDefinition.FAMILY: return Definition.createFamilyDefinition(""+pkg,"");
			case PackageDefinition.CRISP: return Definition.createCrispDefinition(""+pkg,"");
			default: return null;
		}
	}
}
