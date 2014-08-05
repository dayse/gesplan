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
 * <p> <b>Descripci�n:</b> ventana que permite introducir los par�metros
 * necesarios para poder llevar a cabo la simplificaci�n de una base de reglas
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
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603078L;

  //t�tulo de la vista
  private XLabel title;

  //panel que contendr� al t�tulo de la vista
  private JPanel titlePanel;

  //campo de texto que permite la elecci�n del fichero con el patr�n de entrada
  //necesario para realizar el podado de la base de reglas
  private XTextForm patternFile;

  //panel que contendr� el campo de texto para el fichero con el patr�n de
  //entrada
  private JPanel patternFilePanel;

  //campo de texto que permite elegir el m�todo de podado para la base de reglas
  private XTextForm prunningMethod;

  //campo de texto que permite asignar valores al par�metro necesario para el
  //m�todo de podado de bases de reglas seleccionado
  private XTextForm parameter;

  //panel que contendr� el campo de texto para el m�todo de podado y el campo de
  //texto para los par�metros necesarios
  private JPanel prunningPanel;

  //panel que contendra el panel con el campo de texto para el fichero con el
  //patr�n de entrada y el panel que contiene los campos de texto para el m�todo
  //de podado y para los par�metros necesarios
  private JPanel parametersPanel;

  //botones que permiten al usuario confirmar que los datos introducidos son
  //correctos o cancelar la introducci�n de par�metros
  private XCommandForm confirmForm;

  //panel que contendr� los botones de confirmaci�n
  private JPanel confirmPanel;

  //referencia a la localizaci�n de la vista padre a esta (aquella que la
  //origin�)
  private Point parentLocation;

  //m�todos disponibles para el podado de bases de reglas
  private String[] methods = {
      "Threshold", "Best", "Worst"};

  //par�metros que hay introducir para cada uno de los m�todos disponibles para
  //el podado de bases de reglas
  private String[] parameters = {
      "Threshold", "No rules", "No rules"};

  //�ndice que marca el m�todo de podado que se debe utilizar para la
  //simplificaci�n de la base de reglas
  private int index = -1;

  //almacena el fichero con el patr�n de entrada para calcular los grados de
  //activaci�n de las reglas y poder realizar el podado de la misma
  private File pattern = null;

  //objeto que se encarga de escuchar los eventos generados por el usuario al
  //pulsar los distintos botones de la vista
  private XfspPruningActionListener actionListener;

  //base de reglas que se debe simplificar mediante podado de sus reglas
  private Rulebase rulebase;

  //almac�n donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por alg�n controlador
  private XfspStore store;

  /**
   * <p> <b>Descripci�n:</b> crea una ventana que sirve para configurar el
   * proceso de simplificaci�n de bases de reglas por t�cnicas de podado.
   * @param rulebase Base de reglas que se quiere simplificar.
   * @param store Almac�n donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por alg�n controlador.
   *
   */
  public XfspPruningView(Rulebase rulebase, XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfuzzy 3.0.0");
    //establece la base de reglas a simplificar
    this.rulebase = rulebase;
    //establece el almac�n donde guardar los eventos
    this.store = store;
  }

  /**
   * <p> <b>Descripci�n:</b> construye todos los elementos de la interfaz
   * gr�fica correspondiente a la simplificaci�n de bases de reglas mediante
   * t�cnicas de podado.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    actionListener = new XfspPruningActionListener(this, this.store,
        this.rulebase);
    //construye el t�tulo de la ventana
    buildTitle();
    //construye los campos de texto precisos para seleccionar el m�todo de
    //podado que se debe aplicar asi como para introducir los par�metros
    //necesarios para los mismo
    buildParameters();
    //crea los botones en la parte inferior de la ventana que permiten
    //confirmar los par�metros introduccidos y continuar con el proceso de
    //clustering o abortar la operaci�n
    buildConfirmation();
    //obtiene el contendor de la ventana
    Container content = getContentPane();
    //a�ade el panel con el t�tulo de la ventana
    content.add(titlePanel, BorderLayout.NORTH);
    //a�ade el panel con los par�metros para el m�todo de podado
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
    //t�tulo de la ventana de introducci�n de par�metros para hacer el podado
    //de una base de reglas
    title = new XLabel("Pruning");
    //crea el panel situado en la parte superior en la opci�n de tipos...
    titlePanel = new JPanel(new GridLayout(1, 1));
    //...y le a�ade el t�tulo de la ventana
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripci�n:</b> crea el los campos de texto para la elecci�n del
   * m�todo de podado y los par�metros necesarios as� como el panel que los
   * contiene.
   *
   */
  private void buildParameters() {
    //crea el campo de texto para la introducci�n del fichero con patrones de
    //entrada necesario para el m�todo de podado
    patternFile = new XTextForm("Pattern File", actionListener);
    //establece como no editable el campo de texto anterior
    patternFile.setEditable(false);
    //establece el comando a ejecutar cuando se hace click sobre el campo de
    //texto anterior
    patternFile.setActionCommand("PatternFile");
    //crea el campo de texto para la elecci�n del m�todo de podado
    prunningMethod = new XTextForm("Prunning method", actionListener);
    //establece como no editable el campo de texto anterior
    prunningMethod.setEditable(false);
    //establece el comando a ejecutar cuando se hace click sobre el campo de
    //texto anterior
    prunningMethod.setActionCommand("Method");
    //crea el campo de texto para la introducci�n del par�metro necesario para
    //cada uno de los distintos m�todos de podado
    parameter = new XTextForm("");
    //crea un vector de campos de texto...
    XTextForm[] tvector = new XTextForm[3];
    //...y le agrega los tres campos de texto anteriores
    tvector[0] = patternFile;
    tvector[1] = prunningMethod;
    tvector[2] = parameter;
    //ajusta la anchura de los tres campos de texto de la ventana
    XTextForm.setWidth(tvector);
    //crea el panel que contendr� el campo de texto para la selecci�n del
    //fichero con patrones de entrada...
    patternFilePanel = new JPanel(new GridLayout(1, 1));
    //...y le a�ade dicho campo de texto
    patternFilePanel.add(patternFile);
    //crea el panel que contendr� el campo de texto para la selecci�n del
    //m�todo de podado y sus par�metros...
    prunningPanel = new JPanel(new GridLayout(1, 2));
    //...y le a�ade el campo de texto para la selecci�n del m�todo de podado...
    prunningPanel.add(prunningMethod);
    //...y el campo de texto para la introducci�n del par�metro necesario
    prunningPanel.add(parameter);
    //crea el panel que contendr� todos los campos de texto de la ventana...
    parametersPanel = new JPanel(new GridLayout(2, 1));
    //...y le a�ade los dos paneles creados anteriormente
    parametersPanel.add(patternFilePanel);
    parametersPanel.add(prunningPanel);
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
    confirmForm = new XCommandForm(confirmation, confirmation, actionListener);
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
   * <p> <b>Descripci�n:</b> obtiene el fichero seleccionado con los valores de
   * entrada para el proceso de podado de bases de reglas.
   * @return Fichero seleccionado con los valores de entrada con los que se
   * activar�n las reglas de la base de reglas a activar.
   *
   */
  public File getPatternFile() {
    return pattern;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el fichero con los valores de entrada
   * para el proceso de podado de bases de reglas.
   * @param pattern Fichero con los valores de entrada con los que se activar�n
   * las reglas de la base de reglas a activar.
   *
   */
  public void setPatternFile(File pattern) {
    this.pattern = pattern;
    patternFile.setText(pattern.getName());
  }

  /**
   * <p> <b>Descripci�n:</b> selecciona el siguiente m�todo de podado de bases
   * de reglas y lo muestra en el campo de texto apropiado.
   *
   */
  public void selectNextMethod() {
    //incrementa el �ndice que apunta al m�todo de podado seleccionado
    index = (index + 1) % methods.length;
    //establece el m�todo de podado en el campo de texto apropiado
    prunningMethod.setText(methods[index]);
    //establece el nombre del par�metro necesario para el m�todo de podado
    //seleccionado
    parameter.setLabel(parameters[index]);
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el m�todo de podado seleccionado por el
   * usuario.
   * @return M�todo de podado seleccionado por el usuario para la
   * simplificaci�n de la base de reglas.
   *
   */
  public String getSelectedMethod() {
    //cadena que contendr� el m�todo elegido para el podado de la base de
    //reglas
    String method = null;
    //si el m�todo a utilizar ha sido ya seleccionado...
    if (index > -1) {
      //...lo almacena en la cadena anterior...
      method = methods[index];
    }
    //...y lo devuelve
    return method;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el valor para el par�metro necesario para
   * el m�todo de podado seleccionado por el usuario.
   * @return Valor asignado por el usuario al par�metro necesario para el
   * m�todo de podado seleccionado.
   *
   */
  public String getParameter() {
    return parameter.getText();
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
