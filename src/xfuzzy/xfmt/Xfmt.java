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



package xfuzzy.xfmt;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.xfedit.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

/**
 * Clase que desarrolla la ventana principal de la aplicación "xfmt",
 * la herramienta para la monitorización de sistemas difusos
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Xfmt extends JFrame implements ActionListener, 
ChangeListener, WindowListener {

	//----------------------------------------------------------------------------//
	//                            CONSTANTES PRIVADAS                             //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603045L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Referencia a la ventana principal del entorno
	 */
	private Xfuzzy xfuzzy;
	
	/**
	 * Sistema difuso a monitorizar
	 */
	private Specification spec;
	
	/**
	 * Deslizadores para la selección de los valores de entrada
	 */
	private JSlider slider[];
	
	/**
	 * Campos de texto para introducir los valores de entrada
	 */
	private XTextForm inputform[];
	
	/**
	 * Campos de texto para indicar los valores de salida
	 */
	private XTextForm outputform[];
	
	/**
	 * Paneles de representación gráfica de las salidas
	 */
	private XfmtOutputPanel outputpanel[];
	
	/**
	 * Valores asignados a las variables de entrada
	 */
	private double inputvalue[];
	
	/**
	 * Lista de módulos difusos que se están monitorizando
	 */
	private Vector monitorizing;
	
	/**
	 * Lista de ventanas de monitorización abiertas
	 */
	private Vector monitors;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfmt(Xfuzzy xfuzzy, Specification spec) {
		super("Xfmt");
		this.xfuzzy = xfuzzy;
		this.spec = spec;
		this.monitorizing = new Vector();
		this.monitors = new Vector();
		build();
		refresh();
	}
	
 	//----------------------------------------------------------------------------//
	//                             EJECUCIÓN EXTERNA                              //
	//----------------------------------------------------------------------------//

	/**
	 * Ejecución externa de la herramienta "xfmt"
	 */
	public static void main(String args[]) {
		if(args.length != 1) { System.out.println("Usage: xfmt xflfile"); return; }
		File file = new File(args[0]);
		if(!file.exists())
		{ System.out.println("Can't find file "+file.getAbsolutePath()); return; }
		
		XflParser parser = new XflParser();
		Specification spec = parser.parse(file.getAbsolutePath());
		if(spec == null) { System.out.println(parser.resume()); return; }
		
		Xfmt monitor = new Xfmt(null,spec);
		monitor.setVisible(true);
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Monitoriza una base de reglas
	 */
	public void monitorize(RulebaseCall call) {
		if(monitorizing.contains(call)) return;
		monitorizing.add(call);
		XfmtRulebase monitor = new XfmtRulebase(this,call);
		monitor.setVisible(true);
		monitors.add(monitor);
	}
	
	/**
	 * Cierra la monitorización de una base de reglas
	 */
	public void closeMonitor(XfmtRulebase monitor) {
		monitors.remove(monitor);
		monitorizing.remove(monitor.getCall());
		monitor.setVisible(false);
		monitor.dispose();
	}
	
	/**
	 * Interfaz ActionListener, asociada a los campos de introducción
	 * de valores de entrada
	 */
	public void actionPerformed(ActionEvent e) {
		XTextForm changed = (XTextForm) ((JTextField) e.getSource()).getParent();
		for(int i=0; i<inputform.length; i++) if(inputform[i] == changed) {
			boolean valid = true;
			double value = 0;
			try { value = Double.parseDouble(inputform[i].getText()); }
			catch(Exception ex) { valid = false; }
			if(valid) {
				double rate = spec.getSystemModule().getInputs()[i].getRate(value);
				if(rate >= 0 && rate <= 1) slider[i].setValue((int) (rate*100));
				else valid = false;
			}
			if(valid) inputvalue[i] = value;
			else Toolkit.getDefaultToolkit().beep();
			inputform[i].setText(redondeo(inputvalue[i]));
			refresh();
		}
	}
	
	/**
	 * Interfaz ChangeListener, asociada a los deslizadores
	 */
	public void stateChanged(ChangeEvent e) {
		JSlider changed = (JSlider) e.getSource();
		for(int i=0; i<slider.length; i++) if(slider[i] == changed) {
			int value = slider[i].getValue();
			inputvalue[i] = spec.getSystemModule().getInputs()[i].point(value/100.0);
			inputform[i].setText(redondeo(inputvalue[i]));
			refresh();
		}
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
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Construye la ventana
	 */
	private void build() {
		Box body = new Box(BoxLayout.X_AXIS);
		body.add(Box.createHorizontalStrut(5));
		body.add(buildInputBox());
		body.add(Box.createHorizontalStrut(5));
		body.add(buildStructureBox());
		body.add(Box.createHorizontalStrut(5));
		body.add(buildOutputBox());
		body.add(Box.createHorizontalStrut(5));
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Monitor for specification "+spec.getName()));
		content.add(Box.createVerticalStrut(5));
		content.add(body);
		content.add(Box.createVerticalStrut(5));
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage( XfuzzyIcons.xfuzzy.getImage() );
		addWindowListener(this);
		pack();
		setLocation();
	}
	
	/**
	 * Construye el área de variables de entrada
	 */
	private Box buildInputBox() {
		Box inputbox = new Box(BoxLayout.Y_AXIS);
		inputbox.add(new XLabel("Input values"));
		inputbox.add(Box.createVerticalStrut(10));
		
		Variable input[] = spec.getSystemModule().getInputs();
		
		slider = new JSlider[input.length];
		inputform = new XTextForm[input.length];
		inputvalue = new double[input.length];
		
		for(int i=0; i<input.length; i++) {
			slider[i] = new JSlider(JSlider.HORIZONTAL,0,100,50);
			slider[i].addChangeListener(this);
			inputbox.add(slider[i]);
			inputbox.add(Box.createVerticalStrut(5));
			inputvalue[i] = input[i].point(0.5);
			inputform[i] = new XTextForm(input[i].getName());
			inputform[i].addFieldActionListener(this);
			inputform[i].setLabelWidth(100);
			inputform[i].setFieldWidth(100);
			inputform[i].setAlignment(JLabel.CENTER);
			inputform[i].setText(redondeo(inputvalue[i]));
			inputbox.add(inputform[i]);
			inputbox.add(Box.createVerticalGlue());
		}
		return inputbox;
	}
	
	/**
	 * Construye el área de variables de salida
	 */
	private Box buildOutputBox() {
		Box outputbox = new Box(BoxLayout.Y_AXIS);
		outputbox.add(new XLabel("Output values"));
		outputbox.add(Box.createVerticalStrut(5));
		
		Variable output[] = spec.getSystemModule().getOutputs();
		outputpanel = new XfmtOutputPanel[output.length];
		outputform = new XTextForm[output.length];
		for(int i=0; i<output.length; i++) {
			outputpanel[i] = new XfmtOutputPanel(250,80,output[i]);
			outputform[i] = new XTextForm(output[i].getName());
			outputform[i].setLabelWidth(100);
			outputform[i].setFieldWidth(150);
			outputform[i].setAlignment(JLabel.CENTER);
			outputform[i].setEditable(false);
			
			outputbox.add(outputpanel[i]);
			outputbox.add(Box.createVerticalStrut(5));
			outputbox.add(outputform[i]);
			outputbox.add(Box.createVerticalGlue());
		}
		return outputbox;
	}
	
	/**
	 * Construye el área de la estructura jerárquica del sistema
	 */
	private Box buildStructureBox() {
		XfeditStructure graph = new XfeditStructure(this,spec.getSystemModule());
		Box structbox = new Box(BoxLayout.Y_AXIS);
		structbox.add(new XLabel("System Structure"));
		structbox.add(new JScrollPane(graph));
		return structbox;
	}
	
	/**
	 * Coloca la ventana sobre la pantalla
	 */
	private void setLocation() {
		if(xfuzzy != null) {
			Point loc = xfuzzy.frame.getLocationOnScreen();
			loc.x += 40; loc.y += 200;
			setLocation(loc);
		} else {
			Dimension frame = getSize();
			Dimension screen = getToolkit().getScreenSize();
			setLocation((screen.width - frame.width)/2,(screen.height - frame.height)/2);
		}
	}
	
	/**
	 * Actualiza el contenido de la ventana
	 */
	private void refresh() {
		MemFunc result[] = spec.getSystemModule().fuzzyInference(inputvalue);
		for(int i=0; i<result.length; i++) {
			double val = 0;
			if(result[i] instanceof pkg.xfl.mfunc.singleton) 
				val = ((pkg.xfl.mfunc.singleton) result[i]).get()[0];
			else val = ((AggregateMemFunc) result[i]).defuzzify();
			outputform[i].setText(redondeo(val));
			outputpanel[i].setFunction(result[i],val);
		}
		
		for(int i=0; i<monitors.size(); i++)
			((XfmtRulebase) monitors.elementAt(i)).refresh();
		
		repaint();
	}
	
	/**
	 * Devuelve la cadena que representa el numero con 6 digitos
	 */
	private String redondeo(double dd) {
		String data = ""+dd;
		char[] cdat = data.toCharArray();
		int i;
		for(i=0; i<cdat.length; i++) if(cdat[i]>'0' && cdat[i]<='9') break;
		for(int j=0; j<6 && i<cdat.length; j++,i++)
			if(cdat[i]=='e' || cdat[i]=='E') break;
		StringBuffer buf = new StringBuffer(data.substring(0,i));
		int e = data.indexOf("E");
		if(e == -1) e = data.indexOf("e");
		if(e != -1) buf.append(data.substring(e));
		return buf.toString();
	}
	
	/**
	 * Cierra la ventana
	 */
	private void close() {
		if(xfuzzy == null) System.exit(0);
		else setVisible(false);
	}
}
