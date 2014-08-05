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


package xfuzzy.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase que desarrolla un diálogo para mostrar mensajes o pedir datos
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XDialog extends JDialog implements ActionListener, WindowListener {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603004L;

	/**
	 * Código para indicar una ventana que solicita un dato
	 */
	private static final int DIALOG = 0;
	
	/**
	 * Código para indicar una ventana que muestra un mensaje
	 */
	private static final int MESSAGE = 1;
	
	/**
	 * Código para mostrar una ventana que pide una confirmación
	 */
	private static final int QUESTION = 2;
	
	/**
	 * Código para mostrar una ventana que solicita una respuesta SI o NO
	 */
	private static final int YNQUESTION = 3;

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PÚBLICAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Constante que representa la respuesta "YES"
	 */
	public static final int YES = 2;
	
	/**
	 * Constante que representa la respuesta "NO"
	 */
	public static final int NO = 1;
	
	/**
	 * Constante que representa la respuesta "CANCEL"
	 */
	public static final int CANCEL = 0;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Código de la respuesta
	 */
	private int selected = 0;
	
	/**
	 * Campo para solicitar un dato
	 */
	private XTextField textfield;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor con varias líneas de texto
	 */
	public XDialog(Component parent, String label[], String text, int type) {
		super(getFrame(parent),XConstants.version,true);

		if(type == DIALOG) textfield = new XTextField(text, true, 200);
		String lbq[] = { "Ok", "Cancel" };
		String lbm[] = { "Dismiss" };
		String lbyn[] = { "Yes", "No", "Cancel" };

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(Box.createGlue());
		content.add(new XLabel(label));
		if(type == DIALOG) {
			content.add(Box.createGlue());
			content.add(textfield);
		}
		content.add(Box.createGlue());
		content.add(Box.createVerticalStrut(5));
		XCommandForm form = null;
		switch(type) {
		case DIALOG:
		case QUESTION: form = new XCommandForm(lbq,lbq,this); break;
		case MESSAGE: form = new XCommandForm(lbm,lbm,this); break;
		case YNQUESTION: form = new XCommandForm(lbyn,lbyn,this); break;
		}
		form.setCommandWidth(100);
		content.add(form);
		content.add(Box.createVerticalStrut(5));
		content.add(Box.createGlue());

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		pack();
		Dimension size = getSize();
		Dimension screen = getToolkit().getScreenSize();
		setLocation((screen.width - size.width)/2,(screen.height - size.height)/2);
	}

	/**
	 * Constructor con una única línea
	 */
	public XDialog(Component parent, String label, String text, int type) {
		this(parent, makeArray(label), text, type);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Devuelve el texto de la respuesta dada al diálogo
	 */
	public String getText() {
		if(textfield != null) return textfield.getText();
		return null;
	}

	/**
	 * Devuelve el código de la respuesta dada al diálogo
	 */
	public int getValue() { 
		return selected;
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("No")) actionNo();
		else if(command.equals("Yes")) actionYes();
		else if(command.equals("Ok")) actionYes();
		else if(command.equals("Cancel")) actionCancel();
		else if(command.equals("Dismiss")) actionCancel();
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
		actionCancel();
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
	//                         MÉTODOS PÚBLICOS ESTÁTICOS                         //
	//----------------------------------------------------------------------------//

	/**
	 * Muestra un diálogo solicitando un dato
	 */
	public static String showDialog(Component parent, String label, String text) {
		XDialog dialog = new XDialog(parent, label, text, DIALOG);
		dialog.setVisible(true);
		if(dialog.getValue() == YES) return dialog.getText();
		return null;
	}

	/**
	 * Muestra un diálogo solicitando un dato
	 */
	public static String showDialog(Component parent,String label[],String text) {
		XDialog dialog = new XDialog(parent, label, text, DIALOG);
		dialog.setVisible(true);
		if(dialog.getValue() == YES) return dialog.getText();
		return null;
	}

	/**
	 * Muestra un diálogo que presenta un mensaje
	 */
	public static void showMessage(Component parent, String label[]) {
		XDialog dialog = new XDialog(parent, label, null, MESSAGE);
		Toolkit.getDefaultToolkit().beep();
		dialog.setVisible(true);
	}

	/**
	 * Muestra un diálogo que presenta un mensaje
	 */
	public static void showMessage(Component parent, String label) {
		XDialog dialog = new XDialog(parent, label, null, MESSAGE);
		Toolkit.getDefaultToolkit().beep();
		dialog.setVisible(true);
	}

	/**
	 * Muestra un mensaje en espera de aceptación
	 */
	public static boolean showQuestion(Component parent, String label[]) {
		XDialog dialog = new XDialog(parent, label, null, QUESTION);
		Toolkit.getDefaultToolkit().beep();
		dialog.setVisible(true);
		if(dialog.getValue() == YES) return true;
		return false;
	}

	/**
	 * Muestra un mensaje en espera de aceptación
	 */
	public static boolean showQuestion(Component parent, String label) {
		XDialog dialog = new XDialog(parent, label, null, QUESTION);
		Toolkit.getDefaultToolkit().beep();
		dialog.setVisible(true);
		if(dialog.getValue() == YES) return true;
		return false;
	}

	/**
	 * Muestra un mensaje en espera de respuesta SI o NO
	 */
	public static int showYNQuestion(Component parent, String label[]) {
		XDialog dialog = new XDialog(parent, label, null, YNQUESTION);
		Toolkit.getDefaultToolkit().beep();
		dialog.setVisible(true);
		return dialog.getValue();
	}

	/**
	 * Muestra un mensaje en espera de respuesta SI o NO
	 */
	public static int showYNQuestion(Component parent, String label) {
		XDialog dialog = new XDialog(parent, label, null, YNQUESTION);
		Toolkit.getDefaultToolkit().beep();
		dialog.setVisible(true);
		return dialog.getValue();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Acción asociada al botón Yes
	 */
	private void actionYes() {
		selected = YES;
		setVisible(false);
	}

	/**
	 * Acción asociada al botón No
	 */
	private void actionNo() {
		selected = NO;
		setVisible(false);
	}

	/**
	 * Acción asociada al botón Cancel
	 */
	private void actionCancel() {
		selected = CANCEL;
		setVisible(false);
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PRIVADOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el marco correspondiente a un componente	
	 */
	private static Frame getFrame(Component parent) {
		if(parent instanceof Frame) return (Frame) parent;
		return (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
	}

	/**
	 * Construye una lista de cadena formada por una sola
	 */
	private static String [] makeArray(String label) {
		String array[] = new String[1];
		array[0] = "   "+label+"   ";
		return array;
	}

}
