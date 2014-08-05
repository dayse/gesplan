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

package xfuzzy.xfsp.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import xfuzzy.lang.Rulebase;
import xfuzzy.util.JFileChooserConfig;
import xfuzzy.util.XDialog;
import xfuzzy.xfsp.XfspEvent;
import xfuzzy.xfsp.controller.XfspStore;
import xfuzzy.xfsp.view.XfspPruningView;

/**
 * <p> <b>Descripción:</b> clase de objetos que atienden los eventos producidos
 * en las ventanas de introducción de parámetros para el método de
 * simplificación de bases de reglas por podado.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.5
 * @see XfspPruningView
 * @see XfspActionListener
 *
 */
public class XfspPruningActionListener extends XfspActionListener implements
ActionListener {

	//ventana para la introducción de los parámetros del método de podado
	private XfspPruningView frame;

	//almacén donde se deben dejar los eventos del modelo y la vista del sistema
	//para ser procesados por el controlador
	private XfspStore store;

	//base de reglas a simplificar mediante podado
	private Rulebase rulebase;

	/**
	 * <p> <b>Descripción:</b> crea un objeto que responde a los distintos
	 * eventos producidos por la ventana de introducción de parámetros para el
	 * método de podado.
	 * @param frame Ventana que permite la introducción de los parámetros para el
	 * método de simplificación de bases de reglas mediante podado.
	 * @param store Almacén donde hay que enviar los mensajes para que puedan ser
	 * procesados convenientemente por algún controlador.
	 * @param rulebase Base de reglas a simplificar mediante podado.
	 *
	 */
	public XfspPruningActionListener(XfspPruningView frame, XfspStore store,
			Rulebase rulebase) {
		//llama al constructor de la clase padre
		super(frame);
		//establece la ventana de parámetros para clustering que se escuchará
		this.frame = frame;
		//establece el almacén donde se deben dejar los eventos que se quieran
		//procesar
		this.store = store;
		//establece la base de reglas a simplificar
		this.rulebase = rulebase;
	}

	/**
	 * <p> <b>Descripción:</b> envía los parámetros introducidos en la vista
	 * escuchada a un almacén para que sea procesado por algún controlador del
	 * sistema.
	 *
	 */
	protected void sendParameters() {
		//obtiene el método de podado de la vista...
		String method = frame.getSelectedMethod();
		//...y el fichero de entrada
		File patternFile = frame.getPatternFile();
		//si se pudieron obtener ambos parámetros...
		if (method != null && patternFile != null) {
			//...los agrupa junto a la base de reglas a simplificar
			Object[] args = new Object[6];
			args[0] = rulebase;
			args[1] = method;
			args[2] = patternFile;
			args[3] = new Integer(rulebase.getInputs().length);
			args[4] = new Integer(rulebase.getOutputs().length);

			boolean error = false;
			//si el método de podado es por umbral del grado de activación...
			if (method.equals("Threshold")) {
				//...obtiene dicho umbral en forma de cadena...
				String s = frame.getParameter();
				try {
					//...lo convierte a tipo double...
					double ths = Double.parseDouble(s);
					//...y lo agrupa junto con el resto de parámetros del método
					args[5] = new Double(ths);
				}
				//si no pudo ser convertido a double...
				catch (NumberFormatException e) {
					//...muestra un mensaje por pantalla al usuario informando del
					//error...
					String[] message = { "Incorrect argument for threshold",
					"You must enter a float number" };
					XDialog.showMessage(this.frame, message);
					//...y declara que hubo un error
					error = true;
				}
			}
			//en otro caso (mejor número de reglas o eliminación de las peores)...
			else {
				//...obtiene el parámetro en forma de cadena...
				String s = frame.getParameter();
				try {
					//...lo convierte a tipo int...
					int numRules = Integer.parseInt(s);
					//...y lo agrupa junto con el resto de parámetros del método
					args[5] = new Integer(numRules);
				}
				//si no pudo ser convertido a int...
				catch (NumberFormatException e) {
					//...muestra un mensaje por pantalla la usuario informando del
					//error...
					String[] message = { "Incorrect argument for threshold",
					"You must enter an integer number" };
					XDialog.showMessage(this.frame, message);
					//...y declara que hubo un error
					error = true;
				}
			}
			//si no hubo ningún error...
			if (!error) {
				//...envía un evento al almacén con los parámetros para que sea
				//procesado por algún controlador...
				XfspEvent ev = new XfspEvent("Prunning", args);
				store.store(ev);
				//...y cierra la vista escuchada
				super.close();
			}
		}
		//si no se eligió el fichero de entrada...
		else if (patternFile == null) {
			//...muestra un mensaje por pantalla la usuario informando del error
			String[] message = { "Incorrect argument for Training File",
			"You must enter a training file" };
			XDialog.showMessage(this.frame, message);
		}
		//si no se eligió el método de podado a utilizar...
		else if (method == null) {
			//...muestra un mensaje por pantalla la usuario informando del error
			String[] message = { "Incorrect argument for method",
			"You must enter a prunning method" };
			XDialog.showMessage(this.frame, message);
		}
	}

	/**
	 * <p> <b>Descripción:</b> acciones específicas para la vista destinada a la
	 * introducción de los parámetros del método de podado.
	 * @param e Evento producido por la ventana escuchada.
	 *
	 */
	protected void specificActions(ActionEvent e) {
		//comando del evento que debe ser atendido
		String command = e.getActionCommand();
		//analiza los distintos comandos que admite el objeto y realiza las
		//acciones oportunas
		if (command.equals("PatternFile")) {
			//selecciona el fichero con el patrón de entrada
			actionPatternFile();
		} else if (command.equals("Method")) {
			//selecciona el método de podado que se debe utilizar
			actionMethod();
		}
	}

	/**
	 * <p> <b>Descripción:</b> acción realizada cuando se desea seleccionar un
	 * fichero de entrada para el podado de una base de reglas.
	 *
	 */
	private void actionPatternFile() {
		//obtiene el directorio de trabajo
		File workingdir = new File(System.getProperty("user.dir"));
		//crea un selector de ficheros...
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy training files (.trn)", "trn");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Training File");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		//obtiene el fichero seleccionado
		File patternFile = chooser.getSelectedFile();
		//establece el fichero seleccionado en la ventana de parámetros para el
		//podado
		frame.setPatternFile(patternFile);
	}

	/**
	 * <p> <b>Descripción:</b> acción realizada cuando se desea seleccionar un
	 * método de podado de bases de reglas.
	 *
	 */
	private void actionMethod() {
		//selecciona el siguiente método de podado en la ventana de introducción de
		//parámetros
		frame.selectNextMethod();
	}
}
