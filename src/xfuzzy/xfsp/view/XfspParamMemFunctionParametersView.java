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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import xfuzzy.lang.Parameter;
import xfuzzy.lang.ParamMemFunc;

import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

import xfuzzy.xfsp.view.listener.XfspFunctionMergeActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripción:</b> ventana que permite al usuario la introducción de
 * los parámetros adecuados para una nueva función de pertenencia cuyo tipo ha
 * sido ya seleccionado.
 * <p>
 * @author Jorge Agudo Praena
 * @version 3.2
 * @see IXfspView
 *
 */
public class XfspParamMemFunctionParametersView
    extends JDialog
    implements IXfspView {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603077L;

  //título de la vista
  private XLabel title;

  //panel que contendrá al título de la vista
  private JPanel titlePanel;

  //panel que contiene los campos de texto para la introducción de los
  //parámetros de una función de pertenencia
  private JPanel parametersPanel;

  //botones que permiten al usuario confirmar que los datos introducidos son
  //correctos o cancelar la introducción de parámetros
  private XCommandForm confirmForm;

  //panel que contendrá los botones de confirmación
  private JPanel confirmPanel;

  //referencia a la localización de la vista padre a esta (aquella que la
  //originó)
  private Point parentLocation;

  //objeto que se encarga de escuchar los eventos generados por el usuario al
  //pulsar los distintos botones de la vista
  private XfspFunctionMergeActionListener actionListener;

  //función de pertenencia cuyos parámetros van a ser introducidos mediante la
  //vista
  private ParamMemFunc mf;

  /**
   * <p> <b>Descripción:</b> crea una ventana que sirve para introducir los
   * parámetros que definen una función de pertenencia paramétrica.
   *
   */
  public XfspParamMemFunctionParametersView() {
    super();
  }

  /**
   * <p> <b>Descripción:</b> construye la ventana inicializando todos los
   * componentes que contiene.
   *
   */
  public void build() {
    //construye el título de la vista
    buildTitle();
    //construye la parte donde el usuario puede introducir los parámetros
    //necesarios
    buildParameters();
    //contruye los botones de confirmación
    buildConfirmation();
    //obtiene el contenedor de la ventana correspondiente a la vista
    Container content = getContentPane();
    //añade en la parte de arriba de dicho contenedor el título de la vista...
    content.add(titlePanel, BorderLayout.NORTH);
    //...añade el panel donde se pueden introducir los parámetros en el
    //centro...
    content.add(parametersPanel, BorderLayout.CENTER);
    //...y añade los botones de confirmación en la parte de abajo del contenedor
    content.add(confirmForm, BorderLayout.SOUTH);
    //establece la operación por defecto cuando se cierra la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //establece un nuevo objeto de tipo XfspWindowListener como el encargado de
    //escuchar los eventos de ventana generados por el usuario
    addWindowListener(new XfspWindowListener(this));
    //establece la fuente que debe utilizar la vista
    setFont(XConstants.font);
    //ajusta el tamaño que debe tener la ventana
    pack();
    //establece la posición que debe ocupar la ventana de la vista
    setLocation();
  }

  /**
   * <p> <b>Descripción:</b> crea el rótulo con el título de la vista.
   *
   */
  private void buildTitle() {
    //título de la vista para la introducción de parámetros del método Custom
    title = new XLabel("Parameters for " + mf.getLabel());
    //inicializa el panel situado en la parte superior que contendrá el titulo
    //de la vista
    titlePanel = new JPanel(new GridLayout(1, 1));
    //añade al panel el título de la vista
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripción:</b> crea los campos necesarios para que el usuario
   * pueda introducir los parámetros que definen la función de pertenencia.
   *
   */
  private void buildParameters() {
    //almacena los parámetros de la función de pertenencia
    Parameter[] parameters = mf.getParameters();
    //crea nuevo panel en el que los elementos se dispondrán en una sola columna
    parametersPanel = new JPanel(new GridLayout(parameters.length, 1));
    //iniciliza el vector de campos de texto para los parámetros de la función
    XTextForm[] tvector = new XTextForm[parameters.length];
    //para cada uno de los parámetros de la función...
    for (int i = 0; i < parameters.length; i++) {
      //...lo inicializa con el nombre del parámetro que permite definir en su
      //etiqueta...
      XTextForm auxTextForm = new XTextForm("Parameter " +
                                            parameters[i].getName() +
                                            " value");
      //...y lo añade al panel creado...
      parametersPanel.add(auxTextForm);
      //...y al vector de campos de texto
      tvector[i] = auxTextForm;
    }
    //establece la anchura óptima para todos los campos de texto
    XTextForm.setWidth(tvector);
  }

  /**
   * <p> <b>Descripción:</b> crea el campo de confirmación de la vista.
   *
   */
  private void buildConfirmation() {
    //cadenas de caracteres que deben aparecer en los botones de confirmación
    //de la vista
    String[] confirmationText = {
        "Ok", "Refresh", "Cancel"};
    //comandos que se ejecutarán cuando se pulse sobre los botones de
    //confirmación de la vista
    String[] confirmationCommands = {
        "ParametersOk", "ParametersRefresh", "ParametersCancel"};
    //crea un nuevo campo de confirmación con las cadenas que deben mostrar
    //los bontones, los comandos que se deben ejecutar al pulsar sobre ellos y
    //el objeto encargado de escuchar los dichos eventos
    confirmForm = new XCommandForm(confirmationText, confirmationCommands,
                                   actionListener);
    //establece el ancho del campo de confirmación...
    confirmForm.setCommandWidth(120);
    //...y lo fija
    confirmForm.block();
    //inicializa el nuevo panel que contendrá al campo de confirmación de la
    //vista...
    confirmPanel = new JPanel();
    //...establece el layout para el panel...
    confirmPanel.setLayout(new GridLayout(1, 1));
    //...así como el tamaño predefinido...
    confirmPanel.setPreferredSize(new Dimension(400, 50));
    //...y añade el conjunto de botones de confirmación al panel
    confirmPanel.add(confirmForm);
  }

  /**
   * <p> <b>Descripción:</b> establece la localización en pantalla de la
   * ventana.
   *
   */
  private void setLocation() {
    Point loc = parentLocation;
    loc.x += 20;
    loc.y += 100;
    setLocation(loc);
  }

  /**
   * <p> <b>Descripción:</b> devuelve la  función de pertenencia cuyos
   * parámetros se pueden definir a través de la vista.
   * @return Función de pertenencia paramétrica cuyos parámetros permite
   * establecer la vista.
   *
   */
  public ParamMemFunc getMF() {
    return mf;
  }

  /**
   * <p> <b>Descripción:</b> establece la función de pertenencia cuyos
   * parámetros podrán ser definidos mediante la vista.
   * @param mf Función de pertenencia parámetrica para la que la vista
   * permitirá introducir sus parámetros.
   *
   */
  public void setMF(ParamMemFunc mf) {
    this.mf = mf;
  }

  /**
   * <p> <b>Descripción:</b> establece el objeto encargado de atender los
   * eventos generados por la vista actual.
   * @param actionListener Objeto encargado de atender los eventos generados
   * por la vista actual.
   *
   */
  public void setActionListener(XfspFunctionMergeActionListener actionListener) {
    this.actionListener = actionListener;
  }

  /**
   * <p> <b>Descripción:</b> permite establecer la posición de la ventana padre
   * de esta vista, es decir, aquella que la origió.
   * @param loc Localización de la ventana padre de la vista.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripción:</b> devuelve los parámeros introducidos por el usuario
   * para la función de pertenencia.
   * @return Parámetros introducidos en la vista para la función de pertenencia
   * paramétrica que se está definiendo.
   *
   */
  public Parameter[] getParameters() {
    //parámetros de la función de pertenencia
    Parameter[] parameters = mf.getParameters();
    //almacena los parámetros introducidos para la función de pertenencia
    Parameter[] result = new Parameter[parameters.length];
    //almacena el campo de texto que se está tratando en un momento dado
    XTextForm auxTextForm = null;
    //almacena el valor en forma de cadena contenido en el campo de texto que
    //se está tratando
    String stringValue = null;
    try {
      //para cada uno de los parámetros de la función de pertenencia...
      for (int i = 0; i < parameters.length; i++) {
        //...obtiene el parámetro actual...
        result[i] = (Parameter) parameters[i].clone();
        //...así como el campo de texto que lo contiene
        auxTextForm = (XTextForm) parametersPanel.getComponent(i);
        //obtiene el valor en forma de cadena que almacena dicho campo de...
        //texto
        stringValue = auxTextForm.getText();
        //...y lo convierte a valor double
        double value = Double.parseDouble(stringValue);
        //establece el valor para el parámetro actual
        result[i].value = value;
      }
    }
    catch (NumberFormatException e) {
      result = null;
    }
    return result;
  }

  /**
   * <p> <b>Descripción:</b> cierra la ventana de esta vista.
   *
   */
  public void close() {
    setVisible(false);
  }
}
