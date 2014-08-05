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
 * <p> <b>Descripci�n:</b> ventana que permite al usuario la introducci�n de
 * los par�metros adecuados para una nueva funci�n de pertenencia cuyo tipo ha
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
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603077L;

  //t�tulo de la vista
  private XLabel title;

  //panel que contendr� al t�tulo de la vista
  private JPanel titlePanel;

  //panel que contiene los campos de texto para la introducci�n de los
  //par�metros de una funci�n de pertenencia
  private JPanel parametersPanel;

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
  private XfspFunctionMergeActionListener actionListener;

  //funci�n de pertenencia cuyos par�metros van a ser introducidos mediante la
  //vista
  private ParamMemFunc mf;

  /**
   * <p> <b>Descripci�n:</b> crea una ventana que sirve para introducir los
   * par�metros que definen una funci�n de pertenencia param�trica.
   *
   */
  public XfspParamMemFunctionParametersView() {
    super();
  }

  /**
   * <p> <b>Descripci�n:</b> construye la ventana inicializando todos los
   * componentes que contiene.
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
    content.add(parametersPanel, BorderLayout.CENTER);
    //...y a�ade los botones de confirmaci�n en la parte de abajo del contenedor
    content.add(confirmForm, BorderLayout.SOUTH);
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
    title = new XLabel("Parameters for " + mf.getLabel());
    //inicializa el panel situado en la parte superior que contendr� el titulo
    //de la vista
    titlePanel = new JPanel(new GridLayout(1, 1));
    //a�ade al panel el t�tulo de la vista
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripci�n:</b> crea los campos necesarios para que el usuario
   * pueda introducir los par�metros que definen la funci�n de pertenencia.
   *
   */
  private void buildParameters() {
    //almacena los par�metros de la funci�n de pertenencia
    Parameter[] parameters = mf.getParameters();
    //crea nuevo panel en el que los elementos se dispondr�n en una sola columna
    parametersPanel = new JPanel(new GridLayout(parameters.length, 1));
    //iniciliza el vector de campos de texto para los par�metros de la funci�n
    XTextForm[] tvector = new XTextForm[parameters.length];
    //para cada uno de los par�metros de la funci�n...
    for (int i = 0; i < parameters.length; i++) {
      //...lo inicializa con el nombre del par�metro que permite definir en su
      //etiqueta...
      XTextForm auxTextForm = new XTextForm("Parameter " +
                                            parameters[i].getName() +
                                            " value");
      //...y lo a�ade al panel creado...
      parametersPanel.add(auxTextForm);
      //...y al vector de campos de texto
      tvector[i] = auxTextForm;
    }
    //establece la anchura �ptima para todos los campos de texto
    XTextForm.setWidth(tvector);
  }

  /**
   * <p> <b>Descripci�n:</b> crea el campo de confirmaci�n de la vista.
   *
   */
  private void buildConfirmation() {
    //cadenas de caracteres que deben aparecer en los botones de confirmaci�n
    //de la vista
    String[] confirmationText = {
        "Ok", "Refresh", "Cancel"};
    //comandos que se ejecutar�n cuando se pulse sobre los botones de
    //confirmaci�n de la vista
    String[] confirmationCommands = {
        "ParametersOk", "ParametersRefresh", "ParametersCancel"};
    //crea un nuevo campo de confirmaci�n con las cadenas que deben mostrar
    //los bontones, los comandos que se deben ejecutar al pulsar sobre ellos y
    //el objeto encargado de escuchar los dichos eventos
    confirmForm = new XCommandForm(confirmationText, confirmationCommands,
                                   actionListener);
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
    Point loc = parentLocation;
    loc.x += 20;
    loc.y += 100;
    setLocation(loc);
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la  funci�n de pertenencia cuyos
   * par�metros se pueden definir a trav�s de la vista.
   * @return Funci�n de pertenencia param�trica cuyos par�metros permite
   * establecer la vista.
   *
   */
  public ParamMemFunc getMF() {
    return mf;
  }

  /**
   * <p> <b>Descripci�n:</b> establece la funci�n de pertenencia cuyos
   * par�metros podr�n ser definidos mediante la vista.
   * @param mf Funci�n de pertenencia par�metrica para la que la vista
   * permitir� introducir sus par�metros.
   *
   */
  public void setMF(ParamMemFunc mf) {
    this.mf = mf;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el objeto encargado de atender los
   * eventos generados por la vista actual.
   * @param actionListener Objeto encargado de atender los eventos generados
   * por la vista actual.
   *
   */
  public void setActionListener(XfspFunctionMergeActionListener actionListener) {
    this.actionListener = actionListener;
  }

  /**
   * <p> <b>Descripci�n:</b> permite establecer la posici�n de la ventana padre
   * de esta vista, es decir, aquella que la origi�.
   * @param loc Localizaci�n de la ventana padre de la vista.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve los par�meros introducidos por el usuario
   * para la funci�n de pertenencia.
   * @return Par�metros introducidos en la vista para la funci�n de pertenencia
   * param�trica que se est� definiendo.
   *
   */
  public Parameter[] getParameters() {
    //par�metros de la funci�n de pertenencia
    Parameter[] parameters = mf.getParameters();
    //almacena los par�metros introducidos para la funci�n de pertenencia
    Parameter[] result = new Parameter[parameters.length];
    //almacena el campo de texto que se est� tratando en un momento dado
    XTextForm auxTextForm = null;
    //almacena el valor en forma de cadena contenido en el campo de texto que
    //se est� tratando
    String stringValue = null;
    try {
      //para cada uno de los par�metros de la funci�n de pertenencia...
      for (int i = 0; i < parameters.length; i++) {
        //...obtiene el par�metro actual...
        result[i] = (Parameter) parameters[i].clone();
        //...as� como el campo de texto que lo contiene
        auxTextForm = (XTextForm) parametersPanel.getComponent(i);
        //obtiene el valor en forma de cadena que almacena dicho campo de...
        //texto
        stringValue = auxTextForm.getText();
        //...y lo convierte a valor double
        double value = Double.parseDouble(stringValue);
        //establece el valor para el par�metro actual
        result[i].value = value;
      }
    }
    catch (NumberFormatException e) {
      result = null;
    }
    return result;
  }

  /**
   * <p> <b>Descripci�n:</b> cierra la ventana de esta vista.
   *
   */
  public void close() {
    setVisible(false);
  }
}
