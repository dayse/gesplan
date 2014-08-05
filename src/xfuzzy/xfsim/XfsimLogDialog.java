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

import xfuzzy.XfuzzyConfig;
import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Diálogo para seleccionar el almacenamiento de los resultados de simulación en
 * un archivo histórico
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class XfsimLogDialog extends JDialog implements ActionListener {

	// ----------------------------------------------------------------------------//
	// COSTANTES PRIVADAS //
	// ----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603059L;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Descripción de la configuración de almacenamiento
	 */
	private XfsimLog logfile;

	/**
	 * Selectores de variables a almacenar
	 */
	private JRadioButton radio[];

	/**
	 * Campo para seleccionar el fichero
	 */
	private XTextForm text;

	/**
	 * Fichero de almacenamiento seleccionado
	 */
	private File file;

	/**
	 * Resultado del diálogo
	 */
	private boolean result = false;

	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfsimLogDialog(Xfsim xfsim, XfsimLog logfile) {
		super(xfsim, "Xfsim", true);

		this.logfile = logfile;
		this.file = logfile.getFile();

		text = new XTextForm("File", this);
		text.setActionCommand("File");
		text.setEditable(false);
		if (file != null)
			text.setText(file.getAbsolutePath());

		Variable input[] = xfsim.getSpecification().getSystemModule()
		.getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule()
		.getOutputs();
		int length = 2 + input.length + output.length;
		radio = new JRadioButton[length];
		radio[0] = new JRadioButton("Iteration");
		radio[1] = new JRadioButton("Time");
		for (int i = 0; i < output.length; i++)
			radio[i + 2] = new JRadioButton(output[i].getName());
		for (int i = 0; i < input.length; i++)
			radio[i + output.length + 2] = new JRadioButton(input[i].getName());

		boolean[] log = logfile.getSelection();
		if (log != null)
			for (int i = 0; i < radio.length && i < log.length; i++)
				radio[i].setSelected(log[i]);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout((radio.length + 1) / 2, 2));
		for (int i = 0; i < radio.length; i++)
			panel.add(radio[i]);
		Box panelbox = new Box(BoxLayout.X_AXIS);
		panelbox.add(Box.createHorizontalStrut(10));
		panelbox.add(Box.createHorizontalGlue());
		panelbox.add(panel);
		panelbox.add(Box.createHorizontalGlue());
		panelbox.add(Box.createHorizontalStrut(10));

		String lb[] = { "Set", "Cancel" };
		XCommandForm form = new XCommandForm(lb, lb, this);
		form.setCommandWidth(100);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel("Log file"));
		content.add(text);
		content.add(new XLabel("Variables to log"));
		content.add(panelbox);
		content.add(form);

		Point loc = xfsim.getLocationOnScreen();
		loc.x += 40;
		loc.y += 200;
		this.setLocation(loc);
		pack();
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Informa si se ha selecionado el fichero o se ha cancelado
	 */
	public boolean getResult() {
		return result;
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Set"))
			set();
		else if (command.equals("Cancel"))
			setVisible(false);
		else if (command.equals("File"))
			file();
	}

	// ----------------------------------------------------------------------------//
	// MÉTODOS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Selecciona el fichero y las variables a almacenar
	 */
	private void set() {
		if (file == null) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		logfile.setFile(file);
		boolean[] log = new boolean[radio.length];
		for (int i = 0; i < radio.length; i++)
			log[i] = radio[i].isSelected();
		logfile.setSelection(log);
		result = true;
		setVisible(false);
	}

	/**
	 * Selecciona el fichero de almacenamiento
	 */
	private void file() {
		File wdir = (file == null ? new File(System.getProperty("user.dir"))
		: file);
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Log files", "log", "txt");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Log to File");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		file = chooser.getSelectedFile();
		text.setText(file.getAbsolutePath());
	}
}
