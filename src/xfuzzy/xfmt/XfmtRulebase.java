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

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panel para monitorizar un módiulo difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfmtRulebase extends JDialog implements WindowListener {
	
	//----------------------------------------------------------------------------//
	//                            CONSTANTES PRIVADAS                             //
	//----------------------------------------------------------------------------//
	
	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603048L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia a la página principal de la aplicación
	 */
	private Xfmt xfmt;
	
	/**
	 * Módulo difuso a representar
	 */
	private RulebaseCall call;
	
	/**
	 * Paneles para representar las variables de salida
	 */
	private XfmtOutputPanel outputpanel[];
	
	/**
	 * Paneles para representar las variables de entrada
	 */
	private XfmtInputPanel inputpanel[];
	
	/**
	 * Campos de texto para mostrar las reglas
	 */
	private XTextForm rule[];
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfmtRulebase(Xfmt xfmt, RulebaseCall call){
		super(xfmt,"Xfmt",false);
		this.xfmt = xfmt;
		this.call = call;
		build(xfmt);
		refresh();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Devuelve la referencia al módulo difuso
	 */
	public RulebaseCall getCall() {
		return this.call;
	}
	
	/**
	 * Actualiza el contenido de la ventana
	 */
	public void refresh() {
		MemFunc result[] = call.getFuzzyValues();
		MemFunc trueresult[] = call.getTrueValues();
		MemFunc inputmf[] = call.getInputValues();
		
		for(int i=0; i<inputmf.length; i++) inputpanel[i].setFunction(inputmf[i]);
		
		for(int i=0; i<result.length; i++) {
			if(trueresult[i] instanceof pkg.xfl.mfunc.singleton) {
				double val = ((pkg.xfl.mfunc.singleton) trueresult[i]).get()[0];
				outputpanel[i].setFunction(result[i],val);
			}
			else outputpanel[i].setFunction(result[i]);
		}
		
		double degree[] = call.getDegree();
		for(int i=0; i<degree.length; i++) rule[i].setText(redondeo(degree[i]));
		repaint();
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
		xfmt.closeMonitor(this);
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
	private void build(Frame frame) {
		Box box = new Box(BoxLayout.X_AXIS);
		box.add(buildInputBox());
		box.add(Box.createHorizontalStrut(5));
		box.add(buildRuleBox());
		box.add(Box.createHorizontalStrut(5));
		box.add(buildOutputBox());
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Rulebase "+call.getRulebase().getName()));
		content.add(box);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setLocationRelativeTo(frame);
		
		int il = call.getRulebase().getInputs().length;
		int ol = call.getRulebase().getOutputs().length;
		int height = (il > ol? il*145 : ol*145);
		if(height < 320) height = 320;
		setSize(770,height);
	}
	
	/**
	 * Construye el área de variables de entrada
	 */
	private Box buildInputBox() {
		Box inputbox = new Box(BoxLayout.Y_AXIS);
		inputbox.add(new XLabel("Input values"));
		inputbox.add(Box.createVerticalStrut(5));
		
		Variable input[] = call.getRulebase().getInputs();
		inputpanel = new XfmtInputPanel[input.length];
		XLabel inputlabel[] = new XLabel[input.length];
		for(int i=0; i<input.length; i++) {
			inputpanel[i] = new XfmtInputPanel(250,80,input[i]);
			inputlabel[i] = new XLabel(input[i].getName());
			
			inputbox.add(inputpanel[i]);
			inputbox.add(Box.createVerticalStrut(5));
			inputbox.add(inputlabel[i]);
			inputbox.add(Box.createVerticalGlue());
		}
		return inputbox;
	}
	
	/**
	 * Construye el área que muestra las reglas
	 */
	private Box buildRuleBox() {
		rule = new XTextForm[call.getRulebase().getRules().length];
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		for(int i=0; i<rule.length; i++) {
			rule[i] = new XTextForm("Rule "+i);
			rule[i].setLabelWidth(100);
			rule[i].setFieldWidth(150);
			rule[i].setAlignment(JLabel.CENTER);
			rule[i].setEditable(false);
			panel.add(rule[i]);
		}
		
		Box rulebox = new Box(BoxLayout.Y_AXIS);
		rulebox.add(new XLabel("Rule activation degrees"));
		rulebox.add(Box.createVerticalStrut(5));
		rulebox.add(new JScrollPane(panel));
		return rulebox;
	}
	
	/**
	 * Construye el área de variables de salida	
	 */
	private Box buildOutputBox() {
		Box outputbox = new Box(BoxLayout.Y_AXIS);
		outputbox.add(new XLabel("Output values"));
		outputbox.add(Box.createVerticalGlue());
		outputbox.add(Box.createVerticalStrut(5));
		
		Variable output[] = call.getRulebase().getOutputs();
		outputpanel = new XfmtOutputPanel[output.length];
		XLabel outputlabel[] = new XLabel[output.length];
		for(int i=0; i<output.length; i++) {
			outputpanel[i] = new XfmtOutputPanel(250,80,output[i]);
			outputlabel[i] = new XLabel(output[i].getName());
			
			outputbox.add(outputpanel[i]);
			outputbox.add(Box.createVerticalStrut(5));
			outputbox.add(outputlabel[i]);
			outputbox.add(Box.createVerticalGlue());
		}
		return outputbox;
	}
	
	/**
	 * Devuelve la cadena que representa el número con 6 digitos
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
}
