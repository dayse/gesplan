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

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

import xfuzzy.xfsp.view.listener.XfspClusteringActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripción:</b> ventana que permite introducir parámetros numéricos
 * para aquellos procesos de simplificación por <i>clustering</i> que lo
 * requieran.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.3
 * @see IXfspView
 * @see XfspClusteringActionListener
 *
 */
public class XfspNumericParametersView
    extends JDialog
    implements IXfspView {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603076L;

  //título de la vista
  private XLabel titleLabel;

  //panel que contendrá al título de la vista
  private JPanel titlePanel;

  //campo donde el usuario podrá introducir el numero de clusters en que quiere
  //agrupar las funciones de pertenencia del tipo
  private XTextForm[] parameters;

  //panel que contendrá los campos para introducir los parámetros
  private JPanel parametersPanel;

  //panel que contendrá los campos de texto para la introducción de los pesos
  private JPanel weightsPanel;

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
  private XfspClusteringActionListener actionListener;

  //almacena los nombres de los parámetros que se pueden introducir en la vista
  private String[] parametersNames;

  //almacena los valores iniciales para los parámetros
  private double[] initialValues;

  //almacena el nombre de la vista
  private String name;

  //almacena el título de la vista
  private String title;

  //almacena el fichero con la imagen de la fórmula que debe aparecer en la
  //vista (si la hay)
  private File icon;

  //almacena cierto si los parámetros de la vista son enteros y falso si son
  //reales
  private boolean intParameters;

  /**
   * <p> <b>Descripción:</b> crea una ventana que sirve para introducir ç
   * parámetros de tipos numérico.
   *
   */
  public XfspNumericParametersView() {
    super();
  }

  /**
   * <p> <b>Descripción:</b> crea una vista adecuada para que el usuario pueda
   * introducir parámetros numéricos para aquellos procesos de simplificación
   * por <i>clustering</i> que lo requieran.
   * @param actionListener Objeto que se encargará de escuchar los eventos
   * provocados por el ususario al pulsar los botones que muestra la vista.
   *
   */
  public XfspNumericParametersView(XfspClusteringActionListener
                                   actionListener, String[] names,
                                   String name) {
    //establecemos el objeto pendiente de la escucha de los eventos generandos
    //por el usuario
    this.actionListener = actionListener;
    //establece los nombres de los parámetros que se pueden introducir en la
    //vista actual
    this.parametersNames = names;
    //establece el nombre de la vista actual
    this.name = name;
  }

  /**
   * <p> <b>Descripción:</b> construye la vista que permite la introducción de
   * parámetros numéricos.
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
    content.add(parametersPanel);
    //...y añade los botones de confirmación en la parte de abajo del contenedor
    content.add(confirmPanel, BorderLayout.SOUTH);
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
    titleLabel = new XLabel(this.title);
    //inicializa el panel situado en la parte superior que contendrá el titulo
    //de la vista
    titlePanel = new JPanel(new GridLayout(1, 1));
    //añade al panel el título de la vista
    titlePanel.add(titleLabel);
  }

  /**
   * <p> <b>Descripción:</b> crea los campos necesarios para que el usuario
   * pueda introducir los parámetros necesarios para llevar a cabo el método.
   *
   */
  private void buildParameters() {
    //panel que contendrá los campos de texto donde los usuarios podrán
    //introducir los parámetros
    parametersPanel = new JPanel();
    //crea un layout que disponga los elementos de forma vertical...
    BoxLayout layout = new BoxLayout(parametersPanel, BoxLayout.Y_AXIS);
    //...y establece dicho layout para el panel anterior
    parametersPanel.setLayout(layout);
    //crea un vector de campos de texto para todos los campos de texto que
    //sirven para introducir parámetros
    parameters = new XTextForm[parametersNames.length];
    //para todos los campos de texto dispontibles para introducir parámetros...
    for (int i = 0; i < parametersNames.length; i++) {
      //...establece el valor por defecto para dicho campo de texto...
      //....según se un valor entero...
      parameters[i] = new XTextForm(parametersNames[i]);
      if (!this.intParameters) {
        parameters[i].setText("" + initialValues[i]);
      }
      //...o un valor real
      else {
        parameters[i].setText("" + Math.round(initialValues[i]));
      }
    }
    //establece la anchura por defecto para todos los campos de texto
    XTextForm.setWidth(parameters);
    //crea el panel para los pesos
    weightsPanel = new JPanel(new GridLayout(parameters.length, 1));
    //si la vista tiene que mostrar alguna fórmula...
    if (icon != null) {
      Icon formula = new ImageIcon(icon.getAbsolutePath());
      //...crea un botón que contendrá el icono con la fórmula...
      JButton button = new JButton(formula);
      //...hace que el botón permacezca inactivo...
      button.setEnabled(false);
      //...y añade dicho botón al panel adecuado
      parametersPanel.add(button);
    }
    for (int i = 0; i < parameters.length; i++) {
      weightsPanel.add(parameters[i]);
    }
    parametersPanel.add(weightsPanel);
  }

  /**
   * <p> <b>Descripción:</b> crea el campo de confirmación de la vista.
   *
   */
  private void buildConfirmation() {
    //cadenas de caracteres que deben aparecer en los botones de confirmación
    //de la vista
    String[] confirmation = {
        "Ok", "Cancel"};
    //comandos que se ejecutarán cuando se pulse sobre los botones de
    //confirmación de la vista
    String[] commands = {
        this.name + "Ok", this.name + "Cancel"};
    //crea un nuevo campo de confirmación con las cadenas que deben mostrar
    //los bontones, los comandos que se deben ejecutar al pulsar sobre ellos y
    //el objeto encargado de escuchar los dichos eventos
    confirmForm = new XCommandForm(confirmation, commands, actionListener);
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
    //inicializa la localización a la misma que tuviera la ventana padre...
    Point loc = parentLocation;
    //...la desplaza 40 píxeles a la derecha...
    loc.x += 40;
    //...y 20 píxeles hacia abajo
    loc.y += 20;
    //llama al método setLocation de la superclase con la localización que debe
    //tener la ventana de esta vista
    setLocation(loc);
  }

  /**
   * <p> <b>Descripción:</b> permite establecer la posición de la ventana padre
   * de esta vista, es decir, aquella que la origió.
   * @param loc Localización de la ventana padre de la vista.
   *
   */
  public void setParentLocation(Point loc) {
    //establece la localización de la ventana padre
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripción:</b> establece el nombre de la vista actual.
   * @param name Nombre que tomará la vista actual.
   *
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <p> <b>Descripción:</b> establece el título de la vista.
   * @param title Título que mostrará la ventana de la vista actual.
   *
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * <p> <b>Descripción:</b> establece los valores por defecto para los
   * parámetros que permite introducir la vista.
   * @param initVal Valores por defecto que toman los parámetros que permite
   * introducir la vista.
   *
   */
  public void setInitialValues(double[] initVal) {
    this.initialValues = initVal;
  }

  /**
   * <p> <b>Descripción:</b> establece los nombres de los distintos parámetros
   * que permite introducir la vista.
   * @param paramNames Nombres de los parámetros que permite introducir la
   * vista.
   *
   */
  public void setParametersNames(String[] paramNames) {
    this.parametersNames = paramNames;
  }

  /**
   * <p> <b>Descripción:</b> establece el icono con la fórmula que debe mostrar
   * la vista actual.
   * @param icon Fichero con la imagen de la fórmula que debe aparecer en la
   * vista actual.
   *
   */
  public void setFormulaIcon(File icon) {
    this.icon = icon;
  }

  /**
   * <p> <b>Descripción:</b> establece el objeto encargado de atender los
   * eventos generados por la vista actual.
   * @param actionListener Objeto encargado de atender los eventos generados
   * por la vista actual.
   *
   */
  public void setActionListener(XfspClusteringActionListener actionListener) {
    this.actionListener = actionListener;
  }

  /**
   * <p> <b>Descripción:</b> permite saber si los parámetros que se pueden
   * introducir en la vista actual son enteros o reales.
   * @param intParameters Vale cierto si los parámetros que muestra la vista
   * actual son enteros y falso si son reales.
   *
   */
  public void setIntParameters(boolean intParameters) {
    this.intParameters = intParameters;
  }

  /**
   * <p> <b>Descripción:</b> obtiene el n-ésimo parámetro de la vista.
   * @return N-ésimo parámetro de los que permite introducir la vista.
   *
   */
  public String getNthParameter(int n) {
    String s = parameters[n].getText();
    return s;
  }

  /**
   * <p> <b>Descripción:</b> cierra la ventana de esta vista.
   *
   */
  public void close() {
    //establece a falso la visibilidad de la ventana
    setVisible(false);
  }
}
