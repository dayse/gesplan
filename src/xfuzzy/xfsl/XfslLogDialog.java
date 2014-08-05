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
//DIALOGO PARA DESCRIBIR UN FICHERO HISTORICO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
package xfuzzy.xfsl;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import xfuzzy.util.JFileChooserConfig;
import xfuzzy.util.XCommandForm;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

public class XfslLogDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603069L;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// MIEMBROS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	private Xfsl xfsl;

	private XfslLog logfile;

	private JRadioButton radio[];

	private XTextForm text;

	private File file;

	private boolean result = false;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// CONSTRUCTOR //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public XfslLogDialog(Xfsl xfsl, XfslLog logfile) {
		super(xfsl, "Xfsl", true);

		this.xfsl = xfsl;
		this.logfile = logfile;
		this.file = logfile.getFile();

		text = new XTextForm("File", this);
		text.setActionCommand("File");
		text.setEditable(false);
		if (file != null)
			text.setText(file.getAbsolutePath());

		String rname[] = { "Iteration", "Training Error", "Training RMSE",
				"Training MxAE", "Training Variation", "Test Error",
				"Test RMSE", "Test MxAE", "Test Variation" };

		radio = new JRadioButton[rname.length];
		for (int i = 0; i < rname.length; i++)
			radio[i] = new JRadioButton(rname[i]);

		boolean[] log = logfile.getSelection();
		if (log != null)
			for (int i = 0; i < radio.length && i < log.length; i++)
				radio[i].setSelected(log[i]);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout((radio.length + 1) / 2, 2));
		panel.add(radio[0]);
		panel.add(new JLabel(" "));
		panel.add(radio[1]);
		panel.add(radio[5]);
		panel.add(radio[2]);
		panel.add(radio[6]);
		panel.add(radio[3]);
		panel.add(radio[7]);
		panel.add(radio[4]);
		panel.add(radio[8]);
		Box panelbox = new Box(BoxLayout.X_AXIS);
		panelbox.add(Box.createHorizontalStrut(10));
		panelbox.add(Box.createHorizontalGlue());
		panelbox.add(panel);
		panelbox.add(Box.createHorizontalGlue());
		panelbox.add(Box.createHorizontalStrut(10));

		String lb[] = { "Set", "Unset", "Cancel" };
		XCommandForm form = new XCommandForm(lb, lb, this);
		form.setCommandWidth(100);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new XLabel("Log file"));
		content.add(text);
		content.add(new XLabel("Variables to log"));
		content.add(panelbox);
		content.add(form);

		Point loc = xfsl.getLocationOnScreen();
		loc.x += 40;
		loc.y += 200;
		this.setLocation(loc);
		pack();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PUBLICOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Informa si se ha selecionado el fichero o se ha cancelado //
	// -------------------------------------------------------------//

	public boolean isSelected() {
		return result;
	}

	// -------------------------------------------------------------//
	// Interfaz ActionListener //
	// -------------------------------------------------------------//

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Set"))
			set();
		if (command.equals("Unset"))
			unset();
		else if (command.equals("Cancel"))
			setVisible(false);
		else if (command.equals("File"))
			file();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// METODOS PRIVADOS //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	// -------------------------------------------------------------//
	// Selecciona el fichero y las variables a almacenar //
	// -------------------------------------------------------------//

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

	// -------------------------------------------------------------//
	// Deselecciona el fichero de log //
	// -------------------------------------------------------------//

	private void unset() {
		logfile.setFile(null);
		result = true;
		setVisible(false);
	}

	// -------------------------------------------------------------//
	// Selecciona el fichero de almacenamiento //
	// -------------------------------------------------------------//

	private void file() {
		File wdir = (file == null ? xfsl.getWorkingDirectory() : file);
		JFileChooser chooser = new JFileChooser(wdir);
		JFileChooserConfig.configure(chooser);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Log File");
		if (chooser.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION)
			return;
		file = chooser.getSelectedFile();
		text.setText(file.getAbsolutePath());
	}
}
