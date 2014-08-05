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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import xfuzzy.lang.Type;

import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XDialog;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.listener.XfspSimilarityActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripción:</b> ventana que permite introducir los parámetros
 * necesarios para poder llevar a cabo la simplificación de un tipo mediante el
 * cálulo de la media de similaridad entres sus funciones de pertenencia.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.0
 * @see IXfspView
 * @see XfspSimilarityActionListener
 * @see XfspView
 *
 */
public class XfspSimilarityParametersView
    extends JFrame
    implements IXfspView {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603079L;

  //título de la vista
  private XLabel title;

  //panel que contendrá al título de la vista
  private JPanel titlePanel;

  //panel que contendrá la fórmula de la medida de similaridad
  private JPanel similarityFormulaPanel;

  //panel que contendrá el panel con el título de la ventana y el panel con la
  //fórmula de la medida de similaridad
  private JPanel similarityPanel;

  //etiqueta con el título para los parámetros
  private XLabel parametersTitle;

  //panel que contendrá el panel con el título para los parámetros
  private JPanel parametersTitlePanel;

  //campo de texto que permite introducir el umbral para el proceso de
  //simplificación por similaridad
  private XTextForm threshold;

  //panel que contendrá el título de los parámetros y el campo de texto para la
  //introducción del umbral
  private JPanel parametersPanel;

  //botones que permiten al usuario confirmar que los datos introducidos son
  //correctos o cancelar la introducción de parámetros
  private XCommandForm confirmForm;

  //panel que contendrá los botones de confirmación
  private JPanel confirmPanel;

  //referencia a la localización de la vista padre a esta (aquella que la
  //originó)
  private Point parentLocation;

  //tipo que se debe simplificar mediante el uso de medidas de similaridad
  private Type type;

  //almacén donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por algún controlador
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea una ventana que sirve para configurar el
   * proceso de simplificación tipos por técnicas de similaridad.
   * @param type Tipo que se quiere simplificar.
   * @param store Almacén donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por algún controlador.
   *
   */
  public XfspSimilarityParametersView(Type type, XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfuzzy 3.0.0");
    //establece el tipo a simplificar
    this.type = type;
    //establece el almacén donde guardar los eventos
    this.store = store;
  }

  /**
   * <p> <b>Descripción:</b> construye todos los elementos de la interfaz
   * gráfica correspondiente a la simplificación de tipos mediante uso de
   * medidas de similaridad.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    new XfspSimilarityActionListener(this, this.store, this.type);
    //construye el título de la ventana
    buildTitle();
    //construye los campos de texto precisos para seleccionar el umbral que se
    //debe utilizar para la simplificación por similaridad
    buildParameters();
    //crea los botones en la parte inferior de la ventana que permiten
    //confirmar los parámetros introduccidos y continuar con el proceso de
    //clustering o abortar la operación
    buildConfirmation();
    //obtiene el contendor de la ventana
    Container content = getContentPane();
    //añade el panel con el título de la ventana y la fórmula de la medida de
    //similaridad
    content.add(similarityPanel, BorderLayout.NORTH);
    //añade el panel con los parámetros para el cálculo de la medida de
    //similaridad
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
    //título del panel para la introducción de parámetros
    title = new XLabel("Similarity");
    //crea el panel situado en la parte superior en la opción de tipos...
    titlePanel = new JPanel(new GridLayout(1, 1));
    //...y le añade el título de la ventana
    titlePanel.add(title);
    //establece la ruta donde se está ejecutando el entorno
    File path = new File(System.getProperty("xfuzzy.path"));
    //esta línea puede tener que ser modificada en la versión definitiva para
    //poder ser usados los iconos correctamente
    //abre el fichero que contiene la imagen con la fórmula de la medida de
    //similaridad empleada
    File icon = new File(path, "smp/gif/similarity.gif");
    //crea una imagen con la fórmula de la similaridad
    Icon weightFormula = new ImageIcon(icon.getAbsolutePath());
    //crea un botón que contendrá el icono con la fórmula de la similaridad
    JButton button = new JButton(weightFormula);
    //hace que el botón permacezca inactivo
    button.setEnabled(false);
    //crea un panel que contendrá la fórmula de la similaridad
    similarityFormulaPanel = new JPanel(new GridLayout(1, 1));
    //añade el botón de la fórmula al panel
    similarityFormulaPanel.add(button);
    //crea el panel situado en la parte superior de la ventana
    similarityPanel = new JPanel();
    //crea un layout que disponga los elementos de forma vertical...
    BoxLayout layout = new BoxLayout(similarityPanel, BoxLayout.Y_AXIS);
    //...y lo establece como layout del panel superior
    similarityPanel.setLayout(layout);
    //añade al panel el título de la ventana...
    similarityPanel.add(titlePanel);
    //...la fórmula de la similaridad utilizada
    similarityPanel.add(similarityFormulaPanel);
  }

  /**
   * <p> <b>Descripción:</b> crea el título para los parámetros y el campo de
   * texto para la introducción del umbral.
   *
   */
  private void buildParameters() {
    //título de la sección para la introducción de parámetros
    parametersTitle = new XLabel("Parameters");
    //panel donde estará ubicado el título para la introducción de parámetros
    parametersTitlePanel = new JPanel(new GridLayout(1, 1));
    //añade la etiqueta con el título al panel
    parametersTitlePanel.add(parametersTitle);
    //crea el campo de texto para que el usuario introduzca el umbral a utilizar
    threshold = new XTextForm("Threshold");
    //establece el valor por defecto para el umbral
    threshold.setText("0.8");
    //crea un vector donde almacenar todos los campos de texto que deben
    //aparecer en la ventana
    XTextForm[] tvector = new XTextForm[1];
    //establece el umbral como el primer campo de texto de la ventana
    tvector[0] = threshold;
    //ajusta todos los campos de texto de la ventana
    XTextForm.setWidth(tvector);
    //crea el panel para los parámetros...
    parametersPanel = new JPanel(new GridLayout(2, 1));
    //...y le añade el panel con el título de los parámetros...
    parametersPanel.add(parametersTitlePanel);
    //...y el campo de texto donde introducir el umbral
    parametersPanel.add(threshold);
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
    confirmForm = new XCommandForm(confirmation, confirmation,
                                   new XfspSimilarityActionListener(this, store,
        type));
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
   * <p> <b>Descripción:</b> obtiene el umbral introducido por el usuario para
   * el método de simplificación de tipos por similaridad.
   * @return Valor introducido por el usuario como umbral para el método de
   * simplificación de tipos por similaridad.
   *
   */
  public double getThreshold() {
    //valor del umbral
    double ths = 0;
    //obtiene la cadena del campo de texto para el umbral
    String s = threshold.getText();
    try {
      //convierte a double dicha cadena de texto
      ths = Double.parseDouble(s);
    }
    //si no se pudo convertir la cadena...
    catch (NumberFormatException e) {
      //...muestra un mensaje de error por pantalla...
      String[] message = {
          "Incorrect argument for threshold",
          "You must enter a float number"
      };
      XDialog.showMessage(this, message);
      //...y devuelve como umbral 0
      ths = 0;
    }
    return ths;
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
