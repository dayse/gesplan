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

package xfuzzy.xfsp.model;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import xfuzzy.Xfuzzy;
import xfuzzy.lang.Rulebase;
import xfuzzy.lang.Specification;
import xfuzzy.lang.Type;
import xfuzzy.util.JFileChooserConfig;
import xfuzzy.util.XDialog;
import xfuzzy.xfsp.Xfsp;
import xfuzzy.xfsp.XfspEvent;
import xfuzzy.xfsp.controller.XfspController;
import xfuzzy.xfsp.controller.XfspStore;
import xfuzzy.xfsp.model.rulebases.IXfspRulebaseSimplifier;
import xfuzzy.xfsp.model.rulebases.IXfspRulebaseSimplifierFactory;
import xfuzzy.xfsp.model.rulebases.XfspRulebaseSimplifierFactory;
import xfuzzy.xfsp.model.types.IXfspTypeSimplifier;
import xfuzzy.xfsp.model.types.IXfspTypeSimplifierFactory;
import xfuzzy.xfsp.model.types.XfspTypeSimplifierFactory;
import xfuzzy.xfsp.view.XfspView;

/**
 * <p> <b>Descripción:</b> Modelo del sistema de simplificación de sistemas
 * difusos que gestiona los distintos tipos de simplificación que se pueden
 * hacer. Es completamente independiente de la representación que se haga
 * mediante vistas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 4.1
 * @see XfspController
 * @see XfspView
 *
 */
public class XfspModel {

	//referencia al objeto de tipo Xfuzzy que solicitó la simplificación del
	//sistema
	private Xfuzzy xfuzzy;

	//especificación original del sistema que permite deshacer cambios realizados
	private Specification original;

	//especificación sobre la que trabajará el modelo para llevar a cabo los
	//procesos de simplificación
	private Specification workingcopy;

	//almacén donde enviar los eventos destinados a las vistas para su
	//procesamiento por los controladores
	private XfspStore store;

	//conjunto de controladores registrados por el modelo
	private List controller;

	/**
	 * <p> <b>Descripción:</b> crea una modelo a partir de un objeto de tipo
	 * <b>Xfuzzy</b> indicándole el almacén donde debe guardar los mensajes que
	 * quiera enviar a las vistas para que puedan ser procesados por un
	 * controlador.
	 * @param xfuzzy Solicitante de simplificación de tipos a través de la
	 * ventana principal de la aplicación.
	 * @param store Almacén donde hay que enviar los mensajes para que puedan ser
	 * procesados convenientemente por algún controlador.
	 *
	 */
	public XfspModel(Xfuzzy xfuzzy, Specification spec, XfspStore store) {
		//establece la ventana principal de la aplicación que sollicitó la
		//simplificación de un sistema difuso
		this.xfuzzy = xfuzzy;
		//establece la copia de trabajo para llevar a cabo los procesos de
		//simplificación
		this.workingcopy = xfuzzy.duplicate(spec);
		//hace una copia del sistema original
		this.original = spec; //xfuzzy.duplicate(spec);

		//con este constructor no se necesita parser, pues ya tiene la
		//especificación del sistema a simplificar
		//this.parser = null;

		//establece el almacén donde guardar los mensajes para que sean procesados
		//por los controladores
		this.store = store;
		//inicializa a lista de controladores registrados por el modelo
		this.controller = new ArrayList();
	}

	/**
	 * <p> <b>Descripción:</b> hace que el modelo registre un nuevo controlador
	 * para procesar los mensajes que quiera enviar a las vistas. En caso de que
	 * el controlador ya estuviera registrado por el modelo, este método no hace
	 * nada.
	 * @param controller Nuevo controlador que debe registrar el modelo.
	 *
	 */
	public void registerController(XfspController controller) {
		//inicializa un booleano a falso que nos indicará si ha encontrado el
		//controlador entre los registrados por el modelo
		boolean found = false;
		//índice que permitirá recorrer todos los controladores registrados por
		//el modelo hasta el momento
		int i = 0;
		//recorre todos los controladores registrados por el modelo hasta que
		//encuentre uno igual al que se quiere añadir o bien no quede ninguno por
		//comprobar
		while ((i < this.controller.size()) && (found == false)) {
			//si el controlador actual coincide con el que se quiere añadir a los
			//registrados por el modelo...
			if (controller == this.controller.get(i)) {
				//...sale dell bucle de búsqueda y no hace nada más
				found = true;
			}
			//si el controlador actual no es igual al que se quiere añadir...
			else {
				//...sigue buscando
				i++;
			}
		}
		//si no ha encontrador el controlador que se quiere añadir entre los que ya
		//estaban registrados por el modelo...
		if (found == false) {
			//...añade el controlador nuevo a los que debe registrar el modelo
			this.controller.add(controller);
		}
	}

	/**
	 * <p> <b>Descripción:</b> devuelve el objeto xfuzzy utilizado por el modelo
	 * para hacer las copias del sistema que está modificando.
	 * @return Devuelve un objeto de la clase Xfuzzy que está siendo utilizado
	 * por el modelo para la realización de copias de sistemas.
	 *
	 */
	public Xfuzzy getXfuzzy() {
		return this.xfuzzy;
	}

	/**
	 * <p> <b>Descripción:</b> establece un nuevo objeto xfuzzy para que el
	 * modelo pueda realizar las copias necesarias del sistema que está
	 * modifiando.
	 * @param xfuzzy Nuevo objeto de la clase Xfuzzy que será empleado por el
	 * modelo para la realización de copias de sistemas difusos.
	 *
	 */
	public void setXfuzzy(Xfuzzy xfuzzy) {
		this.xfuzzy = xfuzzy;
	}

	/**
	 * <p> <b>Descripción:</b> devuelve la copia de trabajo que está utilizando
	 * el modelo para la simplificación del sistema seleccionado por el usuario.
	 * @return Una copia de trabajo del sistema que se quiere simplificar.
	 *
	 */
	public Specification getWorkingcopy() {
		return this.workingcopy;
	}

	/**
	 * <p> <b>Descripción:</b> establece una nueva copia de trabajo para que el
	 * modelo pueda realizar su simplificación.
	 * @param spec Nueva copia de trabajo del sistema que se quiere simplificar
	 * para que sea manipulada por el modelo.
	 *
	 */
	public void setWorkingcopy(Specification spec) {
		this.workingcopy = spec;
	}

	/**
	 * <p> <b>Descripción:</b> devuelve el almacén de eventos que está utilizando
	 * el modelo.
	 * @return Almacén de eventos que está utilizando el modelo para que sean
	 * procesados por un controlador.
	 *
	 */
	public XfspStore getStore() {
		return this.store;
	}

	/**
	 * <p> <b>Descripción:</b> establece el almacén de eventos que debe utilizar
	 * el modelo.
	 * @param store Nuevo almacén de eventos que debe utilizar el modelo.
	 *
	 */
	public void setStore(XfspStore store) {
		this.store = store;
	}

	/**
	 * <p> <b>Descripción:</b> indica al modelo un controlador que ya no debe
	 * tener regitrado. Si el controlador indicado no estaba siendo registrado por
	 *  el modelo, este método no hace nada.
	 * @param controller Controlador que debe dejar de tener registrado el modelo.
	 *
	 */
	public void deleteController(XfspController controller) {
		//busca entre todos los controladores registrados por el modelo
		for (int i = 0; i < this.controller.size(); i++) {
			//si encuentra un controlador igual al indicado...
			if (controller == this.controller.get(i)) {
				//...lo elimina de la lista de controladores que tiene el modelo
				//registrados
				this.controller.remove(i);
			}
		}
	}

	/**
	 * <p> <b>Descripción:</b> permite obtener la especificación de un sistema
	 * difuso con la que está trabajando el modelo actualmente.
	 * @return Especificación con la que trabaja el modelo para lograr su
	 * simplificación.
	 *
	 */
	public Specification getSpecification() {
		//devuelve la especificación con la que le modelo está trabajando
		return workingcopy;
	}

	/**
	 * <p> <b>Descripción:</b> simplifica un tipo mediante el método indicado y
	 * tomando los argumentos establecidos por el usuario.
	 * @param type Tipo que se quiere simplificar de la espeficiación con la que
	 * trabaja el modelo.
	 * @param method Método de simplificación de tipos que se debe utilizar.
	 * @param args Argumentos que se deben utilizar para la simplificación del
	 * tipo.
	 *
	 */
	public void simplifyType(Type type, String method, Object[] args) {
		//número inicial de funciones de pertenencia que tiene el tipo antes de su
		//simplificación
		int initialMFs = type.getMembershipFunctions().length;
		//simplificador de tipos de un sistema difuso
		IXfspTypeSimplifier ts;
		//crea un fábrica de objetos simplificadores de tipos
		IXfspTypeSimplifierFactory f = XfspTypeSimplifierFactory.getInstance();
		//obtiene de la fábria un simplificador de tipos según el método deseado
		//y que utilice los argumentos indicados
		ts = f.create(method, args);
		//ordena al simplificador de tipos que simplifique el tipo elegido y
		//actualice la especificación con la que trabaja el modelo
		ts.simplify(type, this.workingcopy);
		//número de funciones de pertenencia que tiene el tipo después de su
		//simplificación
		int finalMFs = type.getMembershipFunctions().length;
		//crea los argumentos para el refresco de las vistas...
		Object[] refreshArgs = new Object[3];
		//...el primero de ellos son los tipos...
		refreshArgs[0] = this.workingcopy.getTypes();
		//...el segundo las bases de reglas...
		refreshArgs[1] = this.workingcopy.getRulebases();
		//...y el tercero el mensaje que se debe mostrar
		String[] msg = { "Process finished.",
				initialMFs + " membership functions reduced to " + finalMFs };
		refreshArgs[2] = msg;
		//crea un nuevo evento que indica que las vistas deben regenerar el gráfico
		//de simplificación de tipos...
		XfspEvent ev = new XfspEvent("Refresh", refreshArgs);
		//...y lo almacena para que algún controlador pueda procesarlo
		store.store(ev);
	}

	/**
	 * <p> <b>Descripción:</b> simplifica una base de reglas mediante el método
	 * indicado y tomando los argumentos establecidos por el usuario.
	 * @param rulebase Base de reglas que se quiere simplificar de la
	 * espeficiación con la que trabaja el modelo.
	 * @param method Método de simplificación de tipos que se debe utilizar.
	 * @param args Argumentos que se deben utilizar para la simplificación del
	 * tipo.
	 *
	 */
	public void simplifyRulebase(Rulebase rulebase, String method, Object[] args) {
		//número inicial de reglas que tiene la base de reglas antes de su
		//simplificación
		int initialRules = rulebase.getRules().length;
		//crea un fábrica de objetos simplificadores de bases de reglas
		IXfspRulebaseSimplifierFactory f = XfspRulebaseSimplifierFactory
		.getInstance();
		//obtiene de la fábria un simplificador de bases de reglas según el método
		//deseado y que utilice los argumentos indicados
		IXfspRulebaseSimplifier s = f.create(method, args);
		//ordena al simplificador de bases de reglas que simplifique la base de
		//reglas elegida y actualice la especificación con la que trabaja el modelo
		s.simplify(rulebase, this.workingcopy);
		//número de reglas que tiene la base de reglas después de su simplificación
		int finalRules = rulebase.getRules().length;
		//crea los argumentos para el refresco de las vistas...
		Object[] refreshArgs = new Object[3];
		//...el primero de ellos son los tipos...
		refreshArgs[0] = this.workingcopy.getTypes();
		//...el segundo las bases de reglas...
		refreshArgs[1] = this.workingcopy.getRulebases();
		//...y el tercero el mensaje que se debe mostrar
		String[] msg = {
				"Process finished.",
				initialRules + " rules transformed into " + finalRules
				+ " equivalent rules" };
		refreshArgs[2] = msg;
		//crea un nuevo evento que indica que las vistas deben regenerar el listado
		//reglas en la ventada de simplificación de bases de reglas...
		XfspEvent ev = new XfspEvent("Refresh", refreshArgs);
		//...y lo almacena para que algún controlador pueda procesarlo
		store.store(ev);
	}

	/**
	 * <p> <b>Descripción:</b> almacena el sistema con el que está trabajando el
	 * modelo de forma permanente en un fichero.
	 *
	 */
	public void save() {
		//obtiene el fichero correspondiente a la especificación original del
		//sistema
		File root = original.getFile();
		//si no se puede obtener dicho fichero...
		if (root == null) {
			//...obtiene el directorio de trabajo de la aplicación
			root = xfuzzy.getWorkingDirectory();
		}
		JFileChooser chooser = new JFileChooser(root);
		JFileChooserConfig.configure(chooser);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Xfuzzy system files (.xfl)", "xfl");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(original.getFile());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Save System As ...");
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();

		if (file.exists()) {
			String question[] = new String[2];
			question[0] = "File " + file.getName() + " already exists.";
			question[1] = "Do you want to overwrite this file?";
			if (!XDialog.showQuestion(null, question))
				return;
		}
		//guarda la copia de trabajo del sistema en el fichero seleccionado
		workingcopy.save_as(file);
		//carga el nuevo fichero en el sistema xfuzzy
		xfuzzy.load(file);
	}

	/**
	 * <p> <b>Descripción:</b> aplica al sistema original los cambios que se han
	 * realizado sobre el sistema en el que trabaja el modelo.
	 *
	 */
	public void apply() {
		//duplica la especificación sobre la que trabaja el sistema
		Specification dup = xfuzzy.duplicate(workingcopy); //spec);
		//establece en el modelo original el conjunto de operadores del sistema
		//duplicado...
		original.setOperatorsets(dup.getOperatorsets());
		//...el conjunto de tipos...
		original.setTypes(dup.getTypes());
		//...las bases de reglas...
		original.setRulebases(dup.getRulebases());
		//...y los módulos del sistema
		original.setSystemModule(dup.getSystemModule());
		//indica que el sistema original ha sido modificado
		original.setModified(true);
	}

	/**
	 * <p> <b>Descripción:</b> reestablece el sistema original sobre la copia de
	 * trabajo del modelo.
	 * @param location Localización en la pantalla que debe ocupar la vista tras
	 * reestablecer el sistema original.
	 *
	 */
	public void reload(Point location) {
		//crea un nuevo evento que solicita el cierre del sistema...
		XfspEvent ev = new XfspEvent("Close", null);
		//...y lo almacena en el almacén
		store.store(ev);
		//crea un nuevo sistema de simplificación...
		Xfsp newInstance = new Xfsp(this.xfuzzy, this.original, location);
		//...y lo muestra en pantalla
		newInstance.show();
	}

	/**
	 * <p> <b>Descripción:</b> termina la simplificación del sistema devolviendo
	 * el sistema con el que se estaba trabajando al estado que tenía el sistema
	 * original.
	 *
	 */
	public void close() {
		//cambia el conjunto de operadores de la especificación del sistema con el
		//que estaba trabajando el modelo por el del sistema original...
		workingcopy.setOperatorsets(original.getOperatorsets());
		//...el conjunto de tipos utilizados...
		workingcopy.setTypes(original.getTypes());
		//...el conjunto de bases de reglas...
		workingcopy.setRulebases(original.getRulebases());
		//...y el conjunto de módulos del sistema
		workingcopy.setSystemModule(original.getSystemModule());
	}
}
