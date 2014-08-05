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

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

import xfuzzy.lang.Parameter;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Type;

import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.listener.XfspClusteringActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripci�n:</b> ventana que permite introducir los par�metros
 * necesarios para poder llevar a cabo la simplificaci�n de un tipo mediante la
 * agrupaci�n de sus fuciones de pertenencia en clusters.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 4.7
 * @see IXfspView
 * @see XfspClusteringActionListener
 *
 */
public class XfspClusteringParametersView
    extends JFrame
    implements IXfspView {

	/**
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603073L;

  //t�tulo de la vista
  private XLabel title;

  //panel que contendr� al t�tulo de la vista
  private JPanel titlePanel;

  //campo de texto que permite seleccionar la forma en que se determinar� el
  //n�mero de clusters en que se agrupar�n las funciones de pertenencia
  private XTextForm numClusters;

  //panel que contendr� el campo de texto anterior
  private JPanel clustersPanel;

  //vector de campos de texto que permite establecer un tama�o �ptimo para �stos
  private XTextForm[] formatTVector;

  //panel que contendr� los campos para introducir los par�metros
  private JPanel parametersPanel;

  //campo de texto que permite establecer los pesos para los par�mtros de las
  //funciones de pertenencia del tipo a simplificar
  private XTextForm weightsTextForm;

  //panel que contiene el campo de texto anterior
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

  //m�todos disponible para el c�lculo del �ndice de validez de clusters
  private String[] methods = {
      "Custom", "Davies&Bouldin", "SeparationIndex", "GeneralizedDunnIndex33",
      "GeneralizedDunnIndex63"};

  //almacena el n�mero de clusters en que hay que agrupar las funciones de
  //pertenencia si el usuario ha elegido el m�todo "Custom"
  private int numberOfClusters = -1;

  //almacena el valor del par�metro t para el m�todo de validaci�n de Davies &
  //Bouldin
  private int t = -1;

  //almacena el valor del par�metro q para el m�todo de validaci�n de Davies &
  //Bouldin
  private int q = -1;

  //almacena los valores asignados a los distintos par�metros del tipo a
  //simplificar
  private double[] weights;

  //tipo que se debe simplificar por medio de las t�cnicas de clustering
  private Type type;

  //almac�n donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por alg�n controlador
  private XfspStore store;

  /**
   * <p> <b>Descripci�n:</b> crea una ventana que sirve para configurar el
   * proceso de simplificaci�n de tipos por t�cnicas de <i>clustering<\i>.
   * @param type Tipo que se quiere simplificar.
   * @param store Almac�n donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por alg�n controlador.
   *
   */
  public XfspClusteringParametersView(Type type, XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfsp");
    //establece el tipo a simplificar
    this.type = type;
    //establece el almac�n donde guardar los eventos
    this.store = store;
    //inicializa a 1 los pesos aplicables a los par�metros de las funciones de
    //pertenencia del tipo a simplificar
    ParamMemFunc mf = (ParamMemFunc) type.getMembershipFunctions()[0];
    weights = new double[mf.getNumberOfParameters()];
    for (int i = 0; i < weights.length; i++) {
      weights[i] = 1.0;
    }
  }

  /**
   * <p> <b>Descripci�n:</b> construye todos los elementos de la interfaz
   * gr�fica correspondiente a la simplificaci�n de tipos mediante t�cnicas de
   * <i>clustering</i>.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    //as� como por las que permiten la configuraci�n de los pesos de las
    //funciones de pertenencia y de los m�todos de validaci�n de clusters
    actionListener = new XfspClusteringActionListener(this, this.store,
        this.type);
    //construye el t�tulo de la ventana
    buildTitle();
    //construye el campo de texto para la elecci�n del n�mero de clusters en
    //que agrupar las funciones de pertenencia
    buildNumberOfClusters();
    //crea el campo de texto para la introducci�n de los par�metros asignados a
    //los par�metros de las funciones de pertenencia del tipo a simplificar
    buildWeights();
    //crea los botones en la parte inferior de la ventana que permiten
    //confirmar los par�metros introduccidos y continuar con el proceso de
    //clustering o abortar la operaci�n
    buildConfirmation();
    //obtiene el contendor de la ventana
    Container content = getContentPane();
    //a�ade el panel con el t�tulo de la ventana asi como con el campo de texto
    //para elegir el n�mero de clusters en la parte superior de la ventana
    content.add(parametersPanel, BorderLayout.NORTH);
    //a�ade el panel con el campo de texto para la elecci�n de los pesos
    //asociados a los par�metros de las funciones de pertenencia del tipo en la
    //parte central de la ventana
    content.add(weightsPanel, BorderLayout.CENTER);
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
    title = new XLabel("Clustering");
    //crea y establece el layout del panel que contendr� el t�tulo
    titlePanel = new JPanel(new GridLayout(1, 1));
    //a�ade el t�tulo al panel
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripci�n:</b> crea el campo de texto para la elecci�n del n�mero
   * de <b>clusters</b> y el panel que lo contendr� y a�ade dicho panel y el
   * panel que contiene el t�tulo al panel superior de la ventana.
   *
   */
  private void buildNumberOfClusters() {
    //crea un campo de texto para indicar la forma en que se debe calcular el
    //n�mero de clusters que debe tener el tipo cuando sea simplificado...
    numClusters = new XTextForm("Number of clusters", actionListener);
    //...impide que se pueda escribir en el campo de texto...
    numClusters.setEditable(false);
    //...establece el comando a ejecutar cuando se haga click sobre el campo de
    //texto...
    numClusters.setActionCommand("NumberOfClusters");
    //...inicializa el vector de campos de texto...
    formatTVector = new XTextForm[2];
    //...y le a�ade el campo de texto que permite elegir el m�todo de validaci�n
    //de clusters
    formatTVector[0] = numClusters;
    //crea un panel que contendr� los elementos relativos al c�lculo del n�mero
    //de clusters y le asigna un layout que disponga los elementos en dos
    //columnas dentro e la misma fila
    clustersPanel = new JPanel(new GridLayout(1, 2));
    //a�ade al panel el campo de texto que permite elegir la forma de calcular
    //el n�mero de clusters...
    clustersPanel.add(numClusters);
    //...y el bot�n que permite establecer los par�metros de dicho m�todo
    //clustersPanel.add(setParameters);
    //crea un panel que contenga todos los elementos situados en la parte
    //superior de la ventana...
    parametersPanel = new JPanel(new GridLayout(2, 1));
    //...a�ade el panel que contiene el t�tulo de la ventana...
    parametersPanel.add(titlePanel);
    //...y el panel con los elementos relativos al c�lculo del n�mero de
    //clusters
    parametersPanel.add(clustersPanel);
  }

  /**
   * <p> <b>Descripci�n:</b> crea el campo de texto para la introducci�n de
   * los pesos asociados a los par�metros de las funciones de pertenencia y
   * el panel que lo contendr�.
   *
   */
  private void buildWeights() {
    //representaci�n de los pesos asociados a los par�metros de las funciones
    //de pertenencia en forma de cadena
    String weightsString = "";
    //inicializa dicha cadena para mostrar que los pesos de todos los
    //par�metros son 1.0 por defecto
    for (int i = 0; i < weights.length; i++) {
      weightsString += "W(" + this.getParameters()[i].getName() + ")=1.0";
      if (i < (weights.length - 1)) {
        weightsString += "; ";
      }
    }
    //crea el campo de texto para la introducci�n de los pesos asociados a los
    //par�metros de las funciones de pertenencia...
    weightsTextForm = new XTextForm("Weights", this.actionListener);
    //...lo establece como no editable...
    weightsTextForm.setEditable(false);
    //...establece como texto inicial la cadena con la representaci�n de los
    //pesos por defecto...
    weightsTextForm.setText(weightsString);
    //...e indica el comando asociado al evento que se generar� cuando sea
    //pulsado dicho campo de texto
    weightsTextForm.setActionCommand("Weights");
    //a�ade el campo de texto para los pesos al vector que contiene todos los
    //campos de texto de la ventana
    formatTVector[1] = weightsTextForm;
    //establece la anchura para todos los campos de texto de la ventana
    XTextForm.setWidth(formatTVector);
    //crea el panel con todos los elementos relativos a los pesos asociados al
    //c�lculo de la distancia
    weightsPanel = new JPanel();
    //crea un layout que disponga los elementos de forma vertical...
    BoxLayout layout = new BoxLayout(weightsPanel, BoxLayout.Y_AXIS);
    //...y lo establece como layout del panel para los pesos
    weightsPanel.setLayout(layout);
    //a�ade el campo de texto para la introducci�n de los pesos al panel
    //correspondiente
    weightsPanel.add(weightsTextForm);
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
                                   new XfspClusteringActionListener(this, store,
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
   * <p> <b>Descripci�n:</b> muestra en un men� situado debajo del campo de
   * texto correspondiente los m�todos de validaci�n de <i>clusters<\i>
   * disponibles en la herramienta de <i>clustering<\i>.
   *
   */
  public void showValidityMethods() {
    //iniciliza un men�
    JPopupMenu menu = new JPopupMenu();
    //para cada uno de los m�todos de validaci�n disponibles...
    for (int i = 0; i < methods.length; i++) {
      //...crea un elemento del con el m�todo actual...
      JMenuItem item = new JMenuItem(methods[i]);
      //...establece la fuente del elemento de men�...
      item.setFont(XConstants.font);
      //...a�ade el objeto que escucha los eventos de acci�n de dicho
      //elemento...
      item.addActionListener(this.actionListener);
      //...establece el comando que se ejecutar� cuando se seleccione el
      //elemento del men�...
      item.setActionCommand(methods[i]);
      //...y a�ade el elemento al men�
      menu.add(item);
    }
    //tras haber creado el men�, lo muestra justo debajo del campo de texto que
    //sirve para elegir el m�todo de validaci�n
    menu.show(numClusters, 0, numClusters.getHeight());
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el m�todo de validaci�n de <i>clusters<\i>
   * que se debe de emplear para la simplificaci�n del tipo.
   * @return M�todo de validaci�n de <i>clusters<\i> elegido por el usuario, si
   * lo ha hecho, o null en caso de no haber elegido ninguno a�n.
   *
   */
  public String getSelectedMethod() {
    //obtiene el m�todo de validaci�n de clusters del campo de texto
    //correspondiente
    String method = numClusters.getText();
    //si dicho campo de texto estaba vac�o...
    if (method.equals("")) {
      //...devuelve "null"
      method = null;
    }
    return method;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el m�todo de validaci�n de
   * <i>clusters<\i> seleccionado por el usuario.
   * @param method M�todo de validaci�n de <i>clusters<\i> elegido por el
   * usuario.
   *
   */
  public void setValidityMethod(String method) {
    numClusters.setText(method);
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
   * <p> <b>Descripci�n:</b> obtiene el n�mero de <i>clusters<\i> elegido por
   * el usuario para la simplificaci�n del tipo cuando se utiliza la opci�n
   * <i>Custom<\i>.
   * @return Devuelve el n�mero de <i>clusters<\i> elegido por el usuario para
   * la opci�n <i>Custom<\i> o -1 si no se ha elegido dicha opci�n.
   *
   */
  public int getNumberOfClusters() {
    return this.numberOfClusters;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el n�mero de <i>clusters<\i> elegido por
   * el usuario para la simplificaci�n del tipo cuando se utiliza la opci�n
   * <i>Custom<\i>.
   * @param numberOfClusters N�mero de <i>clusters<\i> elegido por el usuario
   * para la opci�n <i>Custom<\i>.
   *
   */
  public void setNumberOfClusters(int numberOfClusters) {
    this.numberOfClusters = numberOfClusters;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el valor del par�metro t para la
   * simplificaci�n del tipo utilizando el m�todo de validaci�n de Davies &
   * Bouldin.
   * @return Valor del par�metro t elegido por el usuario para el m�todo de
   * validaci�n de Davies & Bouldin o -1 si no se ha elegido dicho m�todo.
   *
   */
  public int getT() {
    return this.t;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el valor del par�metro t para la
   * simplificaci�n del tipo utilizando el m�todo de validaci�n de Davies &
   * Bouldin.
   * @param t Valor del par�metro t elegido por el usuario para el m�todo de
   * validaci�n de Davies & Bouldin.
   *
   */
  public void setT(int t) {
    this.t = t;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el valor del par�metro q para la
   * simplificaci�n del tipo utilizando el m�todo de validaci�n de Davies &
   * Bouldin.
   * @return Valor del par�metro q elegido por el usuario para el m�todo de
   * validaci�n de Davies & Bouldin o -1 si no se ha elegido dicho m�todo.
   *
   */
  public int getQ() {
    return this.q;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el valor del par�metro q para la
   * simplificaci�n del tipo utilizando el m�todo de validaci�n de Davies &
   * Bouldin.
   * @param q Valor del par�metro q elegido por el usuario para el m�todo de
   * validaci�n de Davies & Bouldin.
   *
   */
  public void setQ(int q) {
    this.q = q;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene los valores asignados a los pesos de los
   * distintos par�metros de las funciones de pertenencia del tipo que se va a
   * simplificar.
   * @return Valores de los pesos asignados a los par�metros de las funciones
   * de pertenencia del tipo a simplificar (por defecto, se les asigna un valor
   * de 1 a todos los pesos).
   *
   */
  public double[] getWeights() {
    return weights;
  }

  /**
   * <p> <b>Descripci�n:</b> establece los valores para los pesos de los
   * par�metros de las funciones de pertenencia del tipo a simplificar.
   * @param newWeights Pesos asignados a los par�metros de las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  public void setWeights(double[] newWeights) {
    this.weights = newWeights;
  }

  /**
   * <p> <b>Descripci�n:</b> establece la cadena que se debe mostrar en el
   * campo de texto destinado a los pesos de los par�metros de las funciones de
   * pertenencia del tipo a simplificar.
   * @param weights Cadena que debe mostrar el campo de texto para los pesos de
   * las funciones de pertenencia del tipo a simplificar.
   *
   */
  public void setWeights(String weights) {
    weightsTextForm.setText(weights);
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene los par�metros correspondientes a las
   * funciones de pertenencia del tipo a simplificar.
   * @return Par�metros correspondiente a las funciones de pertenencia del tipo
   * que se va a simplificar.
   *
   */
  public Parameter[] getParameters() {
    ParamMemFunc mf = (ParamMemFunc) type.getMembershipFunctions()[0];
    return mf.getParameters();
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
