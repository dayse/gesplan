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


package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Editor de ficheros XFL3 en modo texto
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditFileEditor extends JDialog implements ActionListener,
WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603024L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia al editor que realiza la llamada a XfeditFileEditor
	 */
	private Xfedit xfeditor;
	
	/**
	 * Analizador sintáctico de ficheros XFL3
	 */
	private XflParser parser;
	
	/**
	 * Sistema difuso a editar
	 */
	private Specification spec;
	
	/**
	 * Área de texto para mostrar el contenido del fichero
	 */
	private JTextArea textarea;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditFileEditor(Xfedit xfeditor) {
		super(xfeditor,"Xfedit",false);
		this.xfeditor = xfeditor;
		this.parser = new XflParser();
		this.spec = xfeditor.getSpecification();
		this.spec.setFileEditing(true);

		textarea = new JTextArea(30,50);
		textarea.setBackground(XConstants.textbackground);
		textarea.setText(spec.toXfl());

		String lb[] = {"Ok","Apply","Reload","Cancel"};
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(100);
		form.block();

		String label = "XFL3 description of system "+spec.getName();

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel(label));
		content.add(new JScrollPane(textarea));
		content.add(form);

		setFont(XConstants.font);
		setLocationRelativeTo(xfeditor);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		pack();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Ok")) ok();
		else if(command.equals("Apply")) apply();
		else if(command.equals("Reload")) textarea.setText(spec.toXfl());
		else if(command.equals("Cancel")) cancel();
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
		cancel();
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

	/**
	 * Creación de un fichero temporal
	 */
	public File newTmpFile() {
		File w = new File(System.getProperty("user.dir"));
		int i=1;
		File tmp = new File(w,"tmp"+addZeroes(i)+".xfl");
		while( tmp.exists() ) { i++; tmp = new File(w,"tmp"+addZeroes(i)+".xfl"); }
		return tmp;
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Describe un entero con 3 digitos
	 */
	private String addZeroes(int i) {
		if(i<10) return "00"+i;
		if(i<100) return "0"+i;
		return ""+i;
	}

	/**
	 * Aplica los cambios a la especificación
	 */
	private boolean apply() {
		String content = textarea.getText();
		File tmp = newTmpFile();
		try {
			OutputStream stream = new FileOutputStream(tmp);
			stream.write(content.getBytes());
			stream.close();
		} catch (Exception e) { 
			xfeditor.log("Error writing XFL3 file. Changes not applied.");
			return false; 
		}

		Specification tmpspec = parser.parse(tmp.getAbsolutePath());
		if(tmpspec == null) { 
			xfeditor.log(parser.resume()+" Changes not applied.");
			tmp.delete();
			return false;
		}

		spec.setOperatorsets(tmpspec.getOperatorsets());
		spec.setTypes(tmpspec.getTypes());
		spec.setRulebases(tmpspec.getRulebases());
		spec.setSystemModule(tmpspec.getSystemModule());
		spec.setModified(true);
		xfeditor.log("Changes applied to specification "+spec.getName()+".");
		tmp.delete();
		xfeditor.refresh();
		return true;
	}

	/**
	 * Aplica los cambios a la aplicación y cierra la ventana
	 */
	private void ok() {
		if( apply() ) { spec.setFileEditing(false); setVisible(false); }
	}

	/**
	 * Cierra la ventana
	 */
	private void cancel() {
		spec.setFileEditing(false);
		setVisible(false);
	}
}
