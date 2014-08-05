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
 * <p> <b>Descripci�n:</b> ventana que permite introducir los par�metros
 * necesarios para poder llevar a cabo la simplificaci�n de un tipo mediante el
 * c�lulo de la media de similaridad entres sus funciones de pertenencia.
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
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603079L;

  //t�tulo de la vista
  private XLabel title;

  //panel que contendr� al t�tulo de la vista
  private JPanel titlePanel;

  //panel que contendr� la f�rmula de la medida de similaridad
  private JPanel similarityFormulaPanel;

  //panel que contendr� el panel con el t�tulo de la ventana y el panel con la
  //f�rmula de la medida de similaridad
  private JPanel similarityPanel;

  //etiqueta con el t�tulo para los par�metros
  private XLabel parametersTitle;

  //panel que contendr� el panel con el t�tulo para los par�metros
  private JPanel parametersTitlePanel;

  //campo de texto que permite introducir el umbral para el proceso de
  //simplificaci�n por similaridad
  private XTextForm threshold;

  //panel que contendr� el t�tulo de los par�metros y el campo de texto para la
  //introducci�n del umbral
  private JPanel parametersPanel;

  //botones que permiten al usuario confirmar que los datos introducidos son
  //correctos o cancelar la introducci�n de par�metros
  private XCommandForm confirmForm;

  //panel que contendr� los botones de confirmaci�n
  private JPanel confirmPanel;

  //referencia a la localizaci�n de la vista padre a esta (aquella que la
  //origin�)
  private Point parentLocation;

  //tipo que se debe simplificar mediante el uso de medidas de similaridad
  private Type type;

  //almac�n donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por alg�n controlador
  private XfspStore store;

  /**
   * <p> <b>Descripci�n:</b> crea una ventana que sirve para configurar el
   * proceso de simplificaci�n tipos por t�cnicas de similaridad.
   * @param type Tipo que se quiere simplificar.
   * @param store Almac�n donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por alg�n controlador.
   *
   */
  public XfspSimilarityParametersView(Type type, XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfuzzy 3.0.0");
    //establece el tipo a simplificar
    this.type = type;
    //establece el almac�n donde guardar los eventos
    this.store = store;
  }

  /**
   * <p> <b>Descripci�n:</b> construye todos los elementos de la interfaz
   * gr�fica correspondiente a la simplificaci�n de tipos mediante uso de
   * medidas de similaridad.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    new XfspSimilarityActionListener(this, this.store, this.type);
    //construye el t�tulo de la ventana
    buildTitle();
    //construye los campos de texto precisos para seleccionar el umbral que se
    //debe utilizar para la simplificaci�n por similaridad
    buildParameters();
    //crea los botones en la parte inferior de la ventana que permiten
    //confirmar los par�metros introduccidos y continuar con el proceso de
    //clustering o abortar la operaci�n
    buildConfirmation();
    //obtiene el contendor de la ventana
    Container content = getContentPane();
    //a�ade el panel con el t�tulo de la ventana y la f�rmula de la medida de
    //similaridad
    content.add(similarityPanel, BorderLayout.NORTH);
    //a�ade el panel con los par�metros para el c�lculo de la medida de
    //similaridad
    content.add(parametersPanel);
    //a�ade el panel con los botones de confirmaci�n o rechazo en la parte
    //inferior de la ventana
    content.add(confirmPanel, BorderLayout.SOUTH);
    //establece la operaci�n por defecto a realizar cuando se cierra la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //establece el objeto que debe atender los eventos de ventana
    addWindowListener(new XfspWindowListener(this));
    //establece la fuente para el texto que aparece en la ventana
    setFont(XConstants.font);
    //ajusta el tama�o de la ventana
    pack();
    //establece la localizaci�n en que se debe mostrar la ventana
    setLocation();
  }

  /**
   * <p> <b>Descripci�n:</b> crea el t�tulo de la ventana as� como el panel que
   * lo contendr�.
   *
   */
  private void buildTitle() {
    //t�tulo del panel para la introducci�n de par�metros
    title = new XLabel("Similarity");
    //crea el panel situado en la parte superior en la opci�n de tipos...
    titlePanel = new JPanel(new GridLayout(1, 1));
    //...y le a�ade el t�tulo de la ventana
    titlePanel.add(title);
    //establece la ruta donde se est� ejecutando el entorno
    File path = new File(System.getProperty("xfuzzy.path"));
    //esta l�nea puede tener que ser modificada en la versi�n definitiva para
    //poder ser usados los iconos correctamente
    //abre el fichero que contiene la imagen con la f�rmula de la medida de
    //similaridad empleada
    File icon = new File(path, "smp/gif/similarity.gif");
    //crea una imagen con la f�rmula de la similaridad
    Icon weightFormula = new ImageIcon(icon.getAbsolutePath());
    //crea un bot�n que contendr� el icono con la f�rmula de la similaridad
    JButton button = new JButton(weightFormula);
    //hace que el bot�n permacezca inactivo
    button.setEnabled(false);
    //crea un panel que contendr� la f�rmula de la similaridad
    similarityFormulaPanel = new JPanel(new GridLayout(1, 1));
    //a�ade el bot�n de la f�rmula al panel
    similarityFormulaPanel.add(button);
    //crea el panel situado en la parte superior de la ventana
    similarityPanel = new JPanel();
    //crea un layout que disponga los elementos de forma vertical...
    BoxLayout layout = new BoxLayout(similarityPanel, BoxLayout.Y_AXIS);
    //...y lo establece como layout del panel superior
    similarityPanel.setLayout(layout);
    //a�ade al panel el t�tulo de la ventana...
    similarityPanel.add(titlePanel);
    //...la f�rmula de la similaridad utilizada
    similarityPanel.add(similarityFormulaPanel);
  }

  /**
   * <p> <b>Descripci�n:</b> crea el t�tulo para los par�metros y el campo de
   * texto para la introducci�n del umbral.
   *
   */
  private void buildParameters() {
    //t�tulo de la secci�n para la introducci�n de par�metros
    parametersTitle = new XLabel("Parameters");
    //panel donde estar� ubicado el t�tulo para la introducci�n de par�metros
    parametersTitlePanel = new JPanel(new GridLayout(1, 1));
    //a�ade la etiqueta con el t�tulo al panel
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
    //crea el panel para los par�metros...
    parametersPanel = new JPanel(new GridLayout(2, 1));
    //...y le a�ade el panel con el t�tulo de los par�metros...
    parametersPanel.add(parametersTitlePanel);
    //...y el campo de texto donde introducir el umbral
    parametersPanel.add(threshold);
  }

  /**
   * <p> <b>Descripci�n:</b> crea los botones que permiten iniciar el
   * proceso de simplificaci�n por <b>clustering</b> o cancelarlo y el
   * panel que contendr� dichos botones.
   *
   */
  private void buildConfirmation() {
    //vector con todas las cadenas de caracteres utilizadas para la confirmaci�n
    //o cancelacion del proceso de clustering
    String[] confirmation = {
        "Ok", "Cancel"};
    //crea un conjunto de botones que permiten cancelar o seguir adelante con la
    //simplificaci�n de tipos mediante clustering...
    confirmForm = new XCommandForm(confirmation, confirmation,
                                   new XfspSimilarityActionListener(this, store,
        type));
    //...establece la anchura para el conjunto anterior...
    confirmForm.setCommandWidth(120);
    //...y lo fija
    confirmForm.block();
    //crea un panel que contendr� al conjunto de bontones de confirmaci�n...
    confirmPanel = new JPanel();
    //...establece el layout para el panel...
    confirmPanel.setLayout(new GridLayout(1, 1));
    //...as� como el tama�o predefinido...
    confirmPanel.setPreferredSize(new Dimension(400, 50));
    //...y a�ade el conjunto de botones de confirmaci�n al panel
    confirmPanel.add(confirmForm);
  }

  /**
   * <p> <b>Descripci�n:</b> establece la posici�n en pantalla de la ventana en
   * funci�n de la posici�n ocupada por su ventana padre.
   *
   */
  private void setLocation() {
    Point loc = parentLocation;
    loc.x += 40;
    loc.y += 200;
    setLocation(loc);
  }

  /**
   * <p> <b>Descripci�n:</b> establece la posici� en pantalla de la ventana
   * padre de la actual.
   * @param loc Posici�n ocupada en la pantalla por la ventana padre del objeto
   * actual.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el umbral introducido por el usuario para
   * el m�todo de simplificaci�n de tipos por similaridad.
   * @return Valor introducido por el usuario como umbral para el m�todo de
   * simplificaci�n de tipos por similaridad.
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
   * <p> <b>Descripci�n:</b> cierra la ventana correspondiente a la
   * simplificaci�n de tipos mediante t�cnicas de <i>clustering<\i>.
   *
   */
  public void close() {
    setVisible(false);
  }
}
