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
//DIALOGO DEL COMPILADOR A JAVA			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import xfuzzy.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class XfjDialog extends JDialog implements ActionListener,
WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603044L;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MIEMBROS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	private Specification spec;

	private File dir;

	private String pkgname;

	private XTextForm text[];

	private String msg;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTORES //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Constructor utilizado en la ventana principal de Xfuzzy //
	// -------------------------------------------------------------//

	public XfjDialog(Xfuzzy xfuzzy, Specification spec) {
		super(xfuzzy.frame, "Xfj", true);
		this.spec = spec;
		this.dir = spec.getFile().getParentFile();

		String lb[] = { "Compile", "Cancel" };
		XCommandForm form = new XCommandForm(lb, lb, this);
		form.setCommandWidth(120);
		form.block();

		text = new XTextForm[2];
		text[0] = new XTextForm("Package");
		text[0].setLabelWidth(120);
		text[0].setFieldWidth(400);
		text[0].setAlignment(JLabel.CENTER);

		text[1] = new XTextForm("Target directory", this);
		text[1].setActionCommand("Browse");
		text[1].setLabelWidth(100);
		text[1].setFieldWidth(400);
		text[1].setAlignment(JLabel.CENTER);
		text[1].setEditable(false);
		text[1].setText(dir.getAbsolutePath());
		XTextForm.setWidth(text);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel("Java Compiler"));
		content.add(text[0]);
		content.add(text[1]);
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setLocationRelativeTo(xfuzzy.frame);
		pack();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PUBLICOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Descripcion del resultado de la compilacion //
	// -------------------------------------------------------------//

	public String getMessage() {
		return msg;
	}

	// -------------------------------------------------------------//
	// Interfaz ActionListener //
	// -------------------------------------------------------------//

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Browse"))
			browse();
		else if (command.equals("Compile"))
			generate();
		else if (command.equals("Cancel"))
			cancel();
	}

	// -------------------------------------------------------------//
	// Interfaz WindowListener //
	// -------------------------------------------------------------//

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		cancel();
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

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Genera el codigo java //
	// -------------------------------------------------------------//

	private void generate() {
		pkgname = text[0].getText();
		Xfj compiler = new Xfj(spec, dir, pkgname);
		this.msg = compiler.getMessage();
		setVisible(false);
	}

	// -------------------------------------------------------------//
	// Selecciona el directorio de destino //
	// -------------------------------------------------------------//

	private void browse() {
		JFileChooser chooser = new JFileChooser(dir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JFileChooserConfig.configure(chooser);
		chooser.setDialogTitle("Target Directory");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		dir = chooser.getSelectedFile();
		text[1].setText(dir.getAbsolutePath());
	}

	// -------------------------------------------------------------//
	// Cierra la ventana sin generar el codigo //
	// -------------------------------------------------------------//

	private void cancel() {
		setVisible(false);
	}
}
