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
 * <p> <b>Descripci�n:</b> ventana que permite introducir par�metros num�ricos
 * para aquellos procesos de simplificaci�n por <i>clustering</i> que lo
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
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603076L;

  //t�tulo de la vista
  private XLabel titleLabel;

  //panel que contendr� al t�tulo de la vista
  private JPanel titlePanel;

  //campo donde el usuario podr� introducir el numero de clusters en que quiere
  //agrupar las funciones de pertenencia del tipo
  private XTextForm[] parameters;

  //panel que contendr� los campos para introducir los par�metros
  private JPanel parametersPanel;

  //panel que contendr� los campos de texto para la introducci�n de los pesos
  private JPanel weightsPanel;

  //botones que permiten al usuario confirmar que los datos introducidos son
  //correctos o cancelar la introducci�n de par�metros
  private XCommandForm confirmForm;

  //panel que contendr� los botones de confirmaci�n
  private JPanel confirmPanel;

  //referencia a la localizaci�n de la vista padre a esta (aquella que la
  //origin�)
  private Point parentLocation;

  //objeto que se encarga de escuchar los eventos generados por el usuario al
  //pulsar los distintos botones de la vista
  private XfspClusteringActionListener actionListener;

  //almacena los nombres de los par�metros que se pueden introducir en la vista
  private String[] parametersNames;

  //almacena los valores iniciales para los par�metros
  private double[] initialValues;

  //almacena el nombre de la vista
  private String name;

  //almacena el t�tulo de la vista
  private String title;

  //almacena el fichero con la imagen de la f�rmula que debe aparecer en la
  //vista (si la hay)
  private File icon;

  //almacena cierto si los par�metros de la vista son enteros y falso si son
  //reales
  private boolean intParameters;

  /**
   * <p> <b>Descripci�n:</b> crea una ventana que sirve para introducir �
   * par�metros de tipos num�rico.
   *
   */
  public XfspNumericParametersView() {
    super();
  }

  /**
   * <p> <b>Descripci�n:</b> crea una vista adecuada para que el usuario pueda
   * introducir par�metros num�ricos para aquellos procesos de simplificaci�n
   * por <i>clustering</i> que lo requieran.
   * @param actionListener Objeto que se encargar� de escuchar los eventos
   * provocados por el ususario al pulsar los botones que muestra la vista.
   *
   */
  public XfspNumericParametersView(XfspClusteringActionListener
                                   actionListener, String[] names,
                                   String name) {
    //establecemos el objeto pendiente de la escucha de los eventos generandos
    //por el usuario
    this.actionListener = actionListener;
    //establece los nombres de los par�metros que se pueden introducir en la
    //vista actual
    this.parametersNames = names;
    //establece el nombre de la vista actual
    this.name = name;
  }

  /**
   * <p> <b>Descripci�n:</b> construye la vista que permite la introducci�n de
   * par�metros num�ricos.
   *
   */
  public void build() {
    //construye el t�tulo de la vista
    buildTitle();
    //construye la parte donde el usuario puede introducir los par�metros
    //necesarios
    buildParameters();
    //contruye los botones de confirmaci�n
    buildConfirmation();
    //obtiene el contenedor de la ventana correspondiente a la vista
    Container content = getContentPane();
    //a�ade en la parte de arriba de dicho contenedor el t�tulo de la vista...
    content.add(titlePanel, BorderLayout.NORTH);
    //...a�ade el panel donde se pueden introducir los par�metros en el
    //centro...
    content.add(parametersPanel);
    //...y a�ade los botones de confirmaci�n en la parte de abajo del contenedor
    content.add(confirmPanel, BorderLayout.SOUTH);
    //establece la operaci�n por defecto cuando se cierra la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //establece un nuevo objeto de tipo XfspWindowListener como el encargado de
    //escuchar los eventos de ventana generados por el usuario
    addWindowListener(new XfspWindowListener(this));
    //establece la fuente que debe utilizar la vista
    setFont(XConstants.font);
    //ajusta el tama�o que debe tener la ventana
    pack();
    //establece la posici�n que debe ocupar la ventana de la vista
    setLocation();
  }

  /**
   * <p> <b>Descripci�n:</b> crea el r�tulo con el t�tulo de la vista.
   *
   */
  private void buildTitle() {
    //t�tulo de la vista para la introducci�n de par�metros del m�todo Custom
    titleLabel = new XLabel(this.title);
    //inicializa el panel situado en la parte superior que contendr� el titulo
    //de la vista
    titlePanel = new JPanel(new GridLayout(1, 1));
    //a�ade al panel el t�tulo de la vista
    titlePanel.add(titleLabel);
  }

  /**
   * <p> <b>Descripci�n:</b> crea los campos necesarios para que el usuario
   * pueda introducir los par�metros necesarios para llevar a cabo el m�todo.
   *
   */
  private void buildParameters() {
    //panel que contendr� los campos de texto donde los usuarios podr�n
    //introducir los par�metros
    parametersPanel = new JPanel();
    //crea un layout que disponga los elementos de forma vertical...
    BoxLayout layout = new BoxLayout(parametersPanel, BoxLayout.Y_AXIS);
    //...y establece dicho layout para el panel anterior
    parametersPanel.setLayout(layout);
    //crea un vector de campos de texto para todos los campos de texto que
    //sirven para introducir par�metros
    parameters = new XTextForm[parametersNames.length];
    //para todos los campos de texto dispontibles para introducir par�metros...
    for (int i = 0; i < parametersNames.length; i++) {
      //...establece el valor por defecto para dicho campo de texto...
      //....seg�n se un valor entero...
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
    //si la vista tiene que mostrar alguna f�rmula...
    if (icon != null) {
      Icon formula = new ImageIcon(icon.getAbsolutePath());
      //...crea un bot�n que contendr� el icono con la f�rmula...
      JButton button = new JButton(formula);
      //...hace que el bot�n permacezca inactivo...
      button.setEnabled(false);
      //...y a�ade dicho bot�n al panel adecuado
      parametersPanel.add(button);
    }
    for (int i = 0; i < parameters.length; i++) {
      weightsPanel.add(parameters[i]);
    }
    parametersPanel.add(weightsPanel);
  }

  /**
   * <p> <b>Descripci�n:</b> crea el campo de confirmaci�n de la vista.
   *
   */
  private void buildConfirmation() {
    //cadenas de caracteres que deben aparecer en los botones de confirmaci�n
    //de la vista
    String[] confirmation = {
        "Ok", "Cancel"};
    //comandos que se ejecutar�n cuando se pulse sobre los botones de
    //confirmaci�n de la vista
    String[] commands = {
        this.name + "Ok", this.name + "Cancel"};
    //crea un nuevo campo de confirmaci�n con las cadenas que deben mostrar
    //los bontones, los comandos que se deben ejecutar al pulsar sobre ellos y
    //el objeto encargado de escuchar los dichos eventos
    confirmForm = new XCommandForm(confirmation, commands, actionListener);
    //establece el ancho del campo de confirmaci�n...
    confirmForm.setCommandWidth(120);
    //...y lo fija
    confirmForm.block();
    //inicializa el nuevo panel que contendr� al campo de confirmaci�n de la
    //vista...
    confirmPanel = new JPanel();
    //...establece el layout para el panel...
    confirmPanel.setLayout(new GridLayout(1, 1));
    //...as� como el tama�o predefinido...
    confirmPanel.setPreferredSize(new Dimension(400, 50));
    //...y a�ade el conjunto de botones de confirmaci�n al panel
    confirmPanel.add(confirmForm);
  }

  /**
   * <p> <b>Descripci�n:</b> establece la localizaci�n en pantalla de la
   * ventana.
   *
   */
  private void setLocation() {
    //inicializa la localizaci�n a la misma que tuviera la ventana padre...
    Point loc = parentLocation;
    //...la desplaza 40 p�xeles a la derecha...
    loc.x += 40;
    //...y 20 p�xeles hacia abajo
    loc.y += 20;
    //llama al m�todo setLocation de la superclase con la localizaci�n que debe
    //tener la ventana de esta vista
    setLocation(loc);
  }

  /**
   * <p> <b>Descripci�n:</b> permite establecer la posici�n de la ventana padre
   * de esta vista, es decir, aquella que la origi�.
   * @param loc Localizaci�n de la ventana padre de la vista.
   *
   */
  public void setParentLocation(Point loc) {
    //establece la localizaci�n de la ventana padre
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el nombre de la vista actual.
   * @param name Nombre que tomar� la vista actual.
   *
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el t�tulo de la vista.
   * @param title T�tulo que mostrar� la ventana de la vista actual.
   *
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * <p> <b>Descripci�n:</b> establece los valores por defecto para los
   * par�metros que permite introducir la vista.
   * @param initVal Valores por defecto que toman los par�metros que permite
   * introducir la vista.
   *
   */
  public void setInitialValues(double[] initVal) {
    this.initialValues = initVal;
  }

  /**
   * <p> <b>Descripci�n:</b> establece los nombres de los distintos par�metros
   * que permite introducir la vista.
   * @param paramNames Nombres de los par�metros que permite introducir la
   * vista.
   *
   */
  public void setParametersNames(String[] paramNames) {
    this.parametersNames = paramNames;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el icono con la f�rmula que debe mostrar
   * la vista actual.
   * @param icon Fichero con la imagen de la f�rmula que debe aparecer en la
   * vista actual.
   *
   */
  public void setFormulaIcon(File icon) {
    this.icon = icon;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el objeto encargado de atender los
   * eventos generados por la vista actual.
   * @param actionListener Objeto encargado de atender los eventos generados
   * por la vista actual.
   *
   */
  public void setActionListener(XfspClusteringActionListener actionListener) {
    this.actionListener = actionListener;
  }

  /**
   * <p> <b>Descripci�n:</b> permite saber si los par�metros que se pueden
   * introducir en la vista actual son enteros o reales.
   * @param intParameters Vale cierto si los par�metros que muestra la vista
   * actual son enteros y falso si son reales.
   *
   */
  public void setIntParameters(boolean intParameters) {
    this.intParameters = intParameters;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el n-�simo par�metro de la vista.
   * @return N-�simo par�metro de los que permite introducir la vista.
   *
   */
  public String getNthParameter(int n) {
    String s = parameters[n].getText();
    return s;
  }

  /**
   * <p> <b>Descripci�n:</b> cierra la ventana de esta vista.
   *
   */
  public void close() {
    //establece a falso la visibilidad de la ventana
    setVisible(false);
  }
}
