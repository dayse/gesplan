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

package xfuzzy.xfsp.view;

import java.io.File;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import xfuzzy.lang.Rulebase;

import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.listener.XfspPruningActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripción:</b> ventana que permite introducir los parámetros
 * necesarios para poder llevar a cabo la simplificación de una base de reglas
 * mediante podado de las reglas que la componen.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see IXfspView
 * @see XfspPruningActionListener
 *
 */
public class XfspPruningView
    extends JFrame
    implements IXfspView {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603078L;

  //título de la vista
  private XLabel title;

  //panel que contendrá al título de la vista
  private JPanel titlePanel;

  //campo de texto que permite la elección del fichero con el patrón de entrada
  //necesario para realizar el podado de la base de reglas
  private XTextForm patternFile;

  //panel que contendrá el campo de texto para el fichero con el patrón de
  //entrada
  private JPanel patternFilePanel;

  //campo de texto que permite elegir el método de podado para la base de reglas
  private XTextForm prunningMethod;

  //campo de texto que permite asignar valores al parámetro necesario para el
  //método de podado de bases de reglas seleccionado
  private XTextForm parameter;

  //panel que contendrá el campo de texto para el método de podado y el campo de
  //texto para los parámetros necesarios
  private JPanel prunningPanel;

  //panel que contendra el panel con el campo de texto para el fichero con el
  //patrón de entrada y el panel que contiene los campos de texto para el método
  //de podado y para los parámetros necesarios
  private JPanel parametersPanel;

  //botones que permiten al usuario confirmar que los datos introducidos son
  //correctos o cancelar la introducción de parámetros
  private XCommandForm confirmForm;

  //panel que contendrá los botones de confirmación
  private JPanel confirmPanel;

  //referencia a la localización de la vista padre a esta (aquella que la
  //originó)
  private Point parentLocation;

  //métodos disponibles para el podado de bases de reglas
  private String[] methods = {
      "Threshold", "Best", "Worst"};

  //parámetros que hay introducir para cada uno de los métodos disponibles para
  //el podado de bases de reglas
  private String[] parameters = {
      "Threshold", "No rules", "No rules"};

  //índice que marca el método de podado que se debe utilizar para la
  //simplificación de la base de reglas
  private int index = -1;

  //almacena el fichero con el patrón de entrada para calcular los grados de
  //activación de las reglas y poder realizar el podado de la misma
  private File pattern = null;

  //objeto que se encarga de escuchar los eventos generados por el usuario al
  //pulsar los distintos botones de la vista
  private XfspPruningActionListener actionListener;

  //base de reglas que se debe simplificar mediante podado de sus reglas
  private Rulebase rulebase;

  //almacén donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por algún controlador
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea una ventana que sirve para configurar el
   * proceso de simplificación de bases de reglas por técnicas de podado.
   * @param rulebase Base de reglas que se quiere simplificar.
   * @param store Almacén donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por algún controlador.
   *
   */
  public XfspPruningView(Rulebase rulebase, XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfuzzy 3.0.0");
    //establece la base de reglas a simplificar
    this.rulebase = rulebase;
    //establece el almacén donde guardar los eventos
    this.store = store;
  }

  /**
   * <p> <b>Descripción:</b> construye todos los elementos de la interfaz
   * gráfica correspondiente a la simplificación de bases de reglas mediante
   * técnicas de podado.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    actionListener = new XfspPruningActionListener(this, this.store,
        this.rulebase);
    //construye el título de la ventana
    buildTitle();
    //construye los campos de texto precisos para seleccionar el método de
    //podado que se debe aplicar asi como para introducir los parámetros
    //necesarios para los mismo
    buildParameters();
    //crea los botones en la parte inferior de la ventana que permiten
    //confirmar los parámetros introduccidos y continuar con el proceso de
    //clustering o abortar la operación
    buildConfirmation();
    //obtiene el contendor de la ventana
    Container content = getContentPane();
    //añade el panel con el título de la ventana
    content.add(titlePanel, BorderLayout.NORTH);
    //añade el panel con los parámetros para el método de podado
    content.add(parametersPanel);
    //añade el panel con los botones de confirmación o rechazo en la parte
    //inferior de la ventana
    content.add(confirmPanel, BorderLayout.SOUTH);
    //establece la operación por defecto a realizar cuando se cierra la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //establece el objeto que debe atender los eventos de ventana
    addWindowListener(new XfspWindowListener(this));
    //establece la fuente para el texto que aparece en la ventana
    setFont(XConstants.font);
    //ajusta el tamaño de la ventana
    pack();
    //establece la localización en que se debe mostrar la ventana
    setLocation();
  }

  /**
   * <p> <b>Descripción:</b> crea el título de la ventana así como el panel que
   * lo contendrá.
   *
   */
  private void buildTitle() {
    //título de la ventana de introducción de parámetros para hacer el podado
    //de una base de reglas
    title = new XLabel("Pruning");
    //crea el panel situado en la parte superior en la opción de tipos...
    titlePanel = new JPanel(new GridLayout(1, 1));
    //...y le añade el título de la ventana
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripción:</b> crea el los campos de texto para la elección del
   * método de podado y los parámetros necesarios así como el panel que los
   * contiene.
   *
   */
  private void buildParameters() {
    //crea el campo de texto para la introducción del fichero con patrones de
    //entrada necesario para el método de podado
    patternFile = new XTextForm("Pattern File", actionListener);
    //establece como no editable el campo de texto anterior
    patternFile.setEditable(false);
    //establece el comando a ejecutar cuando se hace click sobre el campo de
    //texto anterior
    patternFile.setActionCommand("PatternFile");
    //crea el campo de texto para la elección del método de podado
    prunningMethod = new XTextForm("Prunning method", actionListener);
    //establece como no editable el campo de texto anterior
    prunningMethod.setEditable(false);
    //establece el comando a ejecutar cuando se hace click sobre el campo de
    //texto anterior
    prunningMethod.setActionCommand("Method");
    //crea el campo de texto para la introducción del parámetro necesario para
    //cada uno de los distintos métodos de podado
    parameter = new XTextForm("");
    //crea un vector de campos de texto...
    XTextForm[] tvector = new XTextForm[3];
    //...y le agrega los tres campos de texto anteriores
    tvector[0] = patternFile;
    tvector[1] = prunningMethod;
    tvector[2] = parameter;
    //ajusta la anchura de los tres campos de texto de la ventana
    XTextForm.setWidth(tvector);
    //crea el panel que contendrá el campo de texto para la selección del
    //fichero con patrones de entrada...
    patternFilePanel = new JPanel(new GridLayout(1, 1));
    //...y le añade dicho campo de texto
    patternFilePanel.add(patternFile);
    //crea el panel que contendrá el campo de texto para la selección del
    //método de podado y sus parámetros...
    prunningPanel = new JPanel(new GridLayout(1, 2));
    //...y le añade el campo de texto para la selección del método de podado...
    prunningPanel.add(prunningMethod);
    //...y el campo de texto para la introducción del parámetro necesario
    prunningPanel.add(parameter);
    //crea el panel que contendrá todos los campos de texto de la ventana...
    parametersPanel = new JPanel(new GridLayout(2, 1));
    //...y le añade los dos paneles creados anteriormente
    parametersPanel.add(patternFilePanel);
    parametersPanel.add(prunningPanel);
  }

  /**
   * <p> <b>Descripción:</b> crea los botones que permiten iniciar el
   * proceso de simplificación por <b>clustering</b> o cancelarlo y el
   * panel que contendrá dichos botones.
   *
   */
  private void buildConfirmation() {
    //vector con todas las cadenas de caracteres utilizadas para la confirmación
    //o cancelacion del proceso de clustering
    String[] confirmation = {
        "Ok", "Cancel"};
    //crea un conjunto de botones que permiten cancelar o seguir adelante con la
    //simplificación de tipos mediante clustering...
    confirmForm = new XCommandForm(confirmation, confirmation, actionListener);
    //...establece la anchura para el conjunto anterior...
    confirmForm.setCommandWidth(120);
    //...y lo fija
    confirmForm.block();
    //crea un panel que contendrá al conjunto de bontones de confirmación...
    confirmPanel = new JPanel();
    //...establece el layout para el panel...
    confirmPanel.setLayout(new GridLayout(1, 1));
    //...así como el tamaño predefinido...
    confirmPanel.setPreferredSize(new Dimension(400, 50));
    //...y añade el conjunto de botones de confirmación al panel
    confirmPanel.add(confirmForm);
  }

  /**
   * <p> <b>Descripción:</b> establece la posición en pantalla de la ventana en
   * función de la posición ocupada por su ventana padre.
   *
   */
  private void setLocation() {
    Point loc = parentLocation;
    loc.x += 40;
    loc.y += 200;
    setLocation(loc);
  }

  /**
   * <p> <b>Descripción:</b> establece la posició en pantalla de la ventana
   * padre de la actual.
   * @param loc Posición ocupada en la pantalla por la ventana padre del objeto
   * actual.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripción:</b> obtiene el fichero seleccionado con los valores de
   * entrada para el proceso de podado de bases de reglas.
   * @return Fichero seleccionado con los valores de entrada con los que se
   * activarán las reglas de la base de reglas a activar.
   *
   */
  public File getPatternFile() {
    return pattern;
  }

  /**
   * <p> <b>Descripción:</b> establece el fichero con los valores de entrada
   * para el proceso de podado de bases de reglas.
   * @param pattern Fichero con los valores de entrada con los que se activarán
   * las reglas de la base de reglas a activar.
   *
   */
  public void setPatternFile(File pattern) {
    this.pattern = pattern;
    patternFile.setText(pattern.getName());
  }

  /**
   * <p> <b>Descripción:</b> selecciona el siguiente método de podado de bases
   * de reglas y lo muestra en el campo de texto apropiado.
   *
   */
  public void selectNextMethod() {
    //incrementa el índice que apunta al método de podado seleccionado
    index = (index + 1) % methods.length;
    //establece el método de podado en el campo de texto apropiado
    prunningMethod.setText(methods[index]);
    //establece el nombre del parámetro necesario para el método de podado
    //seleccionado
    parameter.setLabel(parameters[index]);
  }

  /**
   * <p> <b>Descripción:</b> obtiene el método de podado seleccionado por el
   * usuario.
   * @return Método de podado seleccionado por el usuario para la
   * simplificación de la base de reglas.
   *
   */
  public String getSelectedMethod() {
    //cadena que contendrá el método elegido para el podado de la base de
    //reglas
    String method = null;
    //si el método a utilizar ha sido ya seleccionado...
    if (index > -1) {
      //...lo almacena en la cadena anterior...
      method = methods[index];
    }
    //...y lo devuelve
    return method;
  }

  /**
   * <p> <b>Descripción:</b> obtiene el valor para el parámetro necesario para
   * el método de podado seleccionado por el usuario.
   * @return Valor asignado por el usuario al parámetro necesario para el
   * método de podado seleccionado.
   *
   */
  public String getParameter() {
    return parameter.getText();
  }

  /**
   * <p> <b>Descripción:</b> cierra la ventana correspondiente a la
   * simplificación de tipos mediante técnicas de <i>clustering<\i>.
   *
   */
  public void close() {
    setVisible(false);
  }
}
