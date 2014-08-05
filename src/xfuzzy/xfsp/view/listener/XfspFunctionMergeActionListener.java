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

import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Parameter;
import xfuzzy.lang.XflException;
import xfuzzy.lang.XflPackage;
import xfuzzy.util.JFileChooserConfig;
import xfuzzy.util.XDialog;
import xfuzzy.xfsp.XfspEvent;
import xfuzzy.xfsp.controller.XfspStore;
import xfuzzy.xfsp.view.XfspFunctionMergeView;
import xfuzzy.xfsp.view.XfspParamMemFunctionParametersView;

/**
 * <p> <b>Descripción:</b> clase de objetos que atienden los eventos producidos
 * en las ventanas de seleccion de una nueva función de pertenencia para
 * sustituir a dos funciones de pertenencia similares pero de tipos distintos.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.7
 * @see XfspFunctionMergeView
 * @see XfspParamMemFunctionParametersView
 *
 */
public class XfspFunctionMergeActionListener extends XfspActionListener
implements ActionListener {

	//ventana de selección del paquete y función de pertenencia para la fusión de
	//dos funciones de pertencia de distinto tipo
	private XfspFunctionMergeView frame;

	//ventana de introducción de parámetros para la función de pertenencia
	//paramétrica seleccionada
	private XfspParamMemFunctionParametersView parametersView;

	//almacén donde se deben dejar los eventos del modelo y la vista del sistema
	//para ser procesados por el controlador
	private XfspStore store;

	/**
	 * <p> <b>Descripción:</b> crea un objeto que responde a los distintos
	 * eventos producidos por la ventana de selección de una nueva función de
	 * pertenencia para sustituir a dos funciones de pertenencia similares de
	 * tipos distintos.
	 * @param frame Ventana que permite la selección de una nueva función de
	 * pertenencia que sustituya a dos funciones similares de tipos distintos.
	 * @param store Almacén donde hay que enviar los mensajes para que puedan ser
	 * procesados convenientemente por algún controlador.
	 *
	 */
	public XfspFunctionMergeActionListener(XfspFunctionMergeView frame,
			XfspStore store) {
		//llama al constructor de la clase padre
		super(frame);
		//establece la ventana de parámetros para clustering que se escuchará
		this.frame = frame;
		//establece el almacén donde se deben dejar los eventos que se quieran
		//procesar
		this.store = store;
	}

	/**
	 * <p> <b>Descripción:</b> envía los parámetros introducidos en la vista
	 * escuchada a un almacén para que sea procesado por algún controlador del
	 * sistema.
	 *
	 */
	protected void sendParameters() {
		//obtiene el paquete seleccionado para buscar las definiciones de
		//funciones de pertenencia
		XflPackage xflpackage = frame.getPackage();
		//obtiene el nombre de la nueva función de pertenencia
		String functionName = frame.getNewMFName();
		//crea una instancia de la función de pertenencia elegida
		ParamMemFunc mf = (ParamMemFunc) xflpackage.instantiate(functionName,
				XflPackage.MFUNC);
		//obtiene los parámetros introducidos para la nueva función de pertenencia
		Parameter[] parameters = frame.getParameters();
		//si se han introducido los parámetros para la nueva función de
		//pertenencia...
		if (parameters != null) {
			//...obtiene los parámetros introducidos para dicha función de
			//pertenencia...
			double[] value = new double[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				value[i] = parameters[i].value;
			}
			try {
				mf.set(value);
			} catch (XflException ex) {
			}
			//...y los envía a un almacén para que sean procesados por algún
			//controlador
			Object[] args = new Object[1];
			args[0] = mf;
			XfspEvent ev = new XfspEvent("RestartMerging", args);
			store.store(ev);
			frame.close();
		}
		//si no se han introducido parámetros para la nueva función de
		//pertenencia...
		else {
			//...muestra un mensaje en pantalla que informa al usuario de que debe
			//introducir los parámetros adecuados para la función de pertenencia
			//elegida
			String[] message = { "Incorrect parameters for member function",
			"You must enter a valid parameters set for this function" };
			XDialog.showMessage(this.frame, message);
		}
	}

	/**
	 * <p> <b>Descripción:</b> acciones específicas para la vista destinada a la
	 * introducción de los parámetros para la fusión de dos funciones de
	 * pertenencia similares.
	 * @param e Evento producido por la ventana escuchada.
	 *
	 */
	protected void specificActions(ActionEvent e) {
		//comando del evento que debe ser atendido
		String command = e.getActionCommand();
		//analiza los distintos comandos que admite el objeto y realiza las
		//acciones oportunas
		if (command.equals("PackageSelection")) {
			//selección del paquete donde buscar funciones de pertenencia
			actionPackageSelection();
		} else if (command.equals("MemberFunctionSelection")) {
			//selección de la función de pertenencia
			actionMemberFunctionSelection();
		} else if (command.equals("SetParameters")) {
			//establecer parámetros para la función de pertenencia seleccionada
			actionSetParameters();
		}
		//si se confirman los parámetros introducidos para la nueva función de
		//pertenencia...
		else if (command.equals("ParametersOk")) {
			//...obtiene los parámetros
			Parameter[] parameters = parametersView.getParameters();
			//si dichos parámetros no son correctos...
			if (parameters == null) {
				//...muestra un mensaje en pantalla que informa al usuario de que debe
				//introducir parámetros correctos para la función de pertenencia elegida
				String[] message = {
						"Incorrect parameters for member function",
				"You must enter a valid parameters set for this function" };
				XDialog.showMessage(this.parametersView, message);
			}
			//en otro caso...
			else {
				//...establece los parámetros introducidos...
				frame.setParameters(parameters);
				//...y cierra la ventana de introducción de parámetros
				parametersView.close();
			}
		}
		//si se quiere mostrar la representación de la nueva función de pertenencia
		//en el panel apropiado...
		else if (command.equals("ParametersRefresh")) {
			//...obtiene los parámetros
			Parameter[] parameters = parametersView.getParameters();
			//si dichos parámetros no son correctos...
			if (parameters == null) {
				//...muestra un mensaje en pantalla que informa al usuario de que debe
				//introducir parámetros correctos para la función de pertenencia elegida
				String[] message = {
						"Incorrect parameters for member function",
				"You must enter a valid parameters set for this function" };
				XDialog.showMessage(this.parametersView, message);
			} else {
				//...establece los parámetros introducidos...
				frame.setParameters(parameters);
				//...y dibuja la nueva función de pertenencia
				frame.refreshGraph();
			}
		}
		//si se cancela la introducción de parámetros...
		else if (command.equals("ParametersCancel")) {
			//...cierra la ventana apropiada
			parametersView.close();
		}
	}

	/**
	 * <p> <b>Descripción:</b> acción realizada cuando se desea seleccionar un
	 * paquete con descripciones de funciones de pertenencia para poder ser
	 * utilizado.
	 *
	 */

	private void actionPackageSelection() {
		//obtiene el directorio de trabajo
		File workingdir = new File(System.getProperty("user.dir"));
		//crea un selector de ficheros...
		JFileChooser chooser = new JFileChooser(workingdir);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy package files (.pkg)", "pkg");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Load Package");
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File pkgfile = chooser.getSelectedFile();
		String pkgfilename = pkgfile.getName();
		String pkgname = pkgfilename.substring(0, pkgfilename.length() - 4);
		String path = System.getProperty("xfuzzy.path");
		File pkgdir = new File(path + "/pkg/" + pkgname);
		if (!pkgdir.exists() || !pkgdir.isDirectory())
			return;

		//obtiene el fichero seleccionado
		XflPackage loaded = new XflPackage(pkgdir);
		//establece el paquete seleccionado en la ventana
		frame.setPackage(loaded);
		//establece las funciones de pertenencia que contiene el paquete
		//seleccionado en la ventana
		frame.setFunctions(loaded.get(XflPackage.MFUNC));
		//establece la primera función de pertenencia definida en el paquete en la
		//ventana
		frame.setMemberFunction(frame.getFunctions()[frame.getIndex()]);
	}

	/**
	 * <p> <b>Descripción:</b> acción realizada cuando se selecciona una función
	 * de pertenencia de entre las disponibles.
	 *
	 */
	private void actionMemberFunctionSelection() {
		//selecciona la siguiente función de pertenencia de ente las disponibles
		frame.selectNextFunction();
		//establece la función de pertenencia seleccionada en la ventana
		frame.setMemberFunction(frame.getFunctions()[frame.getIndex()]);
	}

	/**
	 * <p> <b>Descripción:</b> acción realizada cuando se quiere establecer los
	 * parámetros de la función de pertenencia seleccionada.
	 *
	 */
	private void actionSetParameters() {
		//crea una ventana para la introducción de parámetros...
		parametersView = new XfspParamMemFunctionParametersView();
		//...hace que miestras la ventana esté abierta, no se pueda interactuar con
		//otras ventanas...
		parametersView.setModal(true);
		//...y establece el responsable de atender los eventos generados por dicha
		//ventana
		parametersView.setActionListener(this);
		//obtiene el paquete seleccionado
		XflPackage xflpackage = frame.getPackage();
		//si no se seleccionó ningún paquete...
		if (xflpackage == null) {
			//...muestra al usuario un mensaje por pantalla que le informa de que debe
			//elegir un paquete
			String[] message = { "No package selected",
			"You must select a package before assign member functions parameters" };
			XDialog.showMessage(this.parametersView, message);
		}
		//en otro caso...
		else {
			//...obtiene el nombre de la función de pertenencia seleccionada...
			String functionName = frame.getNewMFName();
			//...e instancia dicha función de pertenencia
			ParamMemFunc mf = (ParamMemFunc) xflpackage.instantiate(
					functionName, XflPackage.MFUNC);
			//si no se pudo instanciar la función de pertenencia seleccionada (por no
			//haber sido seleccionada)...
			if (mf == null) {
				//...muestra al usuario un mensaje por pantalla que le informa de que
				//debe seleccionar una función de pertenencia
				String[] message = { "No member function selected",
				"You must select a member function before assign its parameters" };
				XDialog.showMessage(this.parametersView, message);
			}
			//en otro caso...
			else {
				//...establece la función de pertenencia en la ventana de introducción
				//de parámetros
				parametersView.setMF(mf);
				//establece la localización de la ventana padre
				parametersView.setParentLocation(this.frame.getLocation());
				//construye la ventana de introducción de parámetros
				parametersView.build();
				//muestra la ventana que permite introducir los parámetros de la
				//función de pertenencia
				parametersView.setVisible(true);
			}
		}
	}

	/**
	 * <p> <b>Descripción:</b> cierra la ventana cuyos eventos se están atendiendo.
	 *
	 */
	protected void close() {
		//devuelve una función de pertenencia fácilmente reconocible al objeto del
		//modelo encargado de fusionar las dos funciones de perternencia
		//distintas...
		ParamMemFunc mf = frame.getFirstMF();
		ParamMemFunc clonedMF = (ParamMemFunc) mf.clone(mf.universe());
		clonedMF.setLabel(null);
		Object[] args = new Object[1];
		args[0] = clonedMF;
		XfspEvent ev = new XfspEvent("RestartMerging", args);
		store.store(ev);
		//...y cierra la ventana
		frame.close();
	}
}
