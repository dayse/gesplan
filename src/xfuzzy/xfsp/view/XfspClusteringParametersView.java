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
 * <p> <b>Descripción:</b> ventana que permite introducir los parámetros
 * necesarios para poder llevar a cabo la simplificación de un tipo mediante la
 * agrupación de sus fuciones de pertenencia en clusters.
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
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603073L;

  //título de la vista
  private XLabel title;

  //panel que contendrá al título de la vista
  private JPanel titlePanel;

  //campo de texto que permite seleccionar la forma en que se determinará el
  //número de clusters en que se agruparán las funciones de pertenencia
  private XTextForm numClusters;

  //panel que contendrá el campo de texto anterior
  private JPanel clustersPanel;

  //vector de campos de texto que permite establecer un tamaño óptimo para éstos
  private XTextForm[] formatTVector;

  //panel que contendrá los campos para introducir los parámetros
  private JPanel parametersPanel;

  //campo de texto que permite establecer los pesos para los parémtros de las
  //funciones de pertenencia del tipo a simplificar
  private XTextForm weightsTextForm;

  //panel que contiene el campo de texto anterior
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

  //métodos disponible para el cálculo del índice de validez de clusters
  private String[] methods = {
      "Custom", "Davies&Bouldin", "SeparationIndex", "GeneralizedDunnIndex33",
      "GeneralizedDunnIndex63"};

  //almacena el número de clusters en que hay que agrupar las funciones de
  //pertenencia si el usuario ha elegido el método "Custom"
  private int numberOfClusters = -1;

  //almacena el valor del parámetro t para el método de validación de Davies &
  //Bouldin
  private int t = -1;

  //almacena el valor del parámetro q para el método de validación de Davies &
  //Bouldin
  private int q = -1;

  //almacena los valores asignados a los distintos parámetros del tipo a
  //simplificar
  private double[] weights;

  //tipo que se debe simplificar por medio de las técnicas de clustering
  private Type type;

  //almacén donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por algún controlador
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea una ventana que sirve para configurar el
   * proceso de simplificación de tipos por técnicas de <i>clustering<\i>.
   * @param type Tipo que se quiere simplificar.
   * @param store Almacén donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por algún controlador.
   *
   */
  public XfspClusteringParametersView(Type type, XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfsp");
    //establece el tipo a simplificar
    this.type = type;
    //establece el almacén donde guardar los eventos
    this.store = store;
    //inicializa a 1 los pesos aplicables a los parámetros de las funciones de
    //pertenencia del tipo a simplificar
    ParamMemFunc mf = (ParamMemFunc) type.getMembershipFunctions()[0];
    weights = new double[mf.getNumberOfParameters()];
    for (int i = 0; i < weights.length; i++) {
      weights[i] = 1.0;
    }
  }

  /**
   * <p> <b>Descripción:</b> construye todos los elementos de la interfaz
   * gráfica correspondiente a la simplificación de tipos mediante técnicas de
   * <i>clustering</i>.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    //así como por las que permiten la configuración de los pesos de las
    //funciones de pertenencia y de los métodos de validación de clusters
    actionListener = new XfspClusteringActionListener(this, this.store,
        this.type);
    //construye el título de la ventana
    buildTitle();
    //construye el campo de texto para la elección del número de clusters en
    //que agrupar las funciones de pertenencia
    buildNumberOfClusters();
    //crea el campo de texto para la introducción de los parámetros asignados a
    //los parámetros de las funciones de pertenencia del tipo a simplificar
    buildWeights();
    //crea los botones en la parte inferior de la ventana que permiten
    //confirmar los parámetros introduccidos y continuar con el proceso de
    //clustering o abortar la operación
    buildConfirmation();
    //obtiene el contendor de la ventana
    Container content = getContentPane();
    //añade el panel con el título de la ventana asi como con el campo de texto
    //para elegir el número de clusters en la parte superior de la ventana
    content.add(parametersPanel, BorderLayout.NORTH);
    //añade el panel con el campo de texto para la elección de los pesos
    //asociados a los parámetros de las funciones de pertenencia del tipo en la
    //parte central de la ventana
    content.add(weightsPanel, BorderLayout.CENTER);
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
    title = new XLabel("Clustering");
    //crea y establece el layout del panel que contendrá el título
    titlePanel = new JPanel(new GridLayout(1, 1));
    //añade el título al panel
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripción:</b> crea el campo de texto para la elección del número
   * de <b>clusters</b> y el panel que lo contendrá y añade dicho panel y el
   * panel que contiene el título al panel superior de la ventana.
   *
   */
  private void buildNumberOfClusters() {
    //crea un campo de texto para indicar la forma en que se debe calcular el
    //número de clusters que debe tener el tipo cuando sea simplificado...
    numClusters = new XTextForm("Number of clusters", actionListener);
    //...impide que se pueda escribir en el campo de texto...
    numClusters.setEditable(false);
    //...establece el comando a ejecutar cuando se haga click sobre el campo de
    //texto...
    numClusters.setActionCommand("NumberOfClusters");
    //...inicializa el vector de campos de texto...
    formatTVector = new XTextForm[2];
    //...y le añade el campo de texto que permite elegir el método de validación
    //de clusters
    formatTVector[0] = numClusters;
    //crea un panel que contendrá los elementos relativos al cálculo del número
    //de clusters y le asigna un layout que disponga los elementos en dos
    //columnas dentro e la misma fila
    clustersPanel = new JPanel(new GridLayout(1, 2));
    //añade al panel el campo de texto que permite elegir la forma de calcular
    //el número de clusters...
    clustersPanel.add(numClusters);
    //...y el botón que permite establecer los parámetros de dicho método
    //clustersPanel.add(setParameters);
    //crea un panel que contenga todos los elementos situados en la parte
    //superior de la ventana...
    parametersPanel = new JPanel(new GridLayout(2, 1));
    //...añade el panel que contiene el título de la ventana...
    parametersPanel.add(titlePanel);
    //...y el panel con los elementos relativos al cálculo del número de
    //clusters
    parametersPanel.add(clustersPanel);
  }

  /**
   * <p> <b>Descripción:</b> crea el campo de texto para la introducción de
   * los pesos asociados a los parámetros de las funciones de pertenencia y
   * el panel que lo contendrá.
   *
   */
  private void buildWeights() {
    //representación de los pesos asociados a los parámetros de las funciones
    //de pertenencia en forma de cadena
    String weightsString = "";
    //inicializa dicha cadena para mostrar que los pesos de todos los
    //parámetros son 1.0 por defecto
    for (int i = 0; i < weights.length; i++) {
      weightsString += "W(" + this.getParameters()[i].getName() + ")=1.0";
      if (i < (weights.length - 1)) {
        weightsString += "; ";
      }
    }
    //crea el campo de texto para la introducción de los pesos asociados a los
    //parámetros de las funciones de pertenencia...
    weightsTextForm = new XTextForm("Weights", this.actionListener);
    //...lo establece como no editable...
    weightsTextForm.setEditable(false);
    //...establece como texto inicial la cadena con la representación de los
    //pesos por defecto...
    weightsTextForm.setText(weightsString);
    //...e indica el comando asociado al evento que se generará cuando sea
    //pulsado dicho campo de texto
    weightsTextForm.setActionCommand("Weights");
    //añade el campo de texto para los pesos al vector que contiene todos los
    //campos de texto de la ventana
    formatTVector[1] = weightsTextForm;
    //establece la anchura para todos los campos de texto de la ventana
    XTextForm.setWidth(formatTVector);
    //crea el panel con todos los elementos relativos a los pesos asociados al
    //cálculo de la distancia
    weightsPanel = new JPanel();
    //crea un layout que disponga los elementos de forma vertical...
    BoxLayout layout = new BoxLayout(weightsPanel, BoxLayout.Y_AXIS);
    //...y lo establece como layout del panel para los pesos
    weightsPanel.setLayout(layout);
    //añade el campo de texto para la introducción de los pesos al panel
    //correspondiente
    weightsPanel.add(weightsTextForm);
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
                                   new XfspClusteringActionListener(this, store,
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
   * <p> <b>Descripción:</b> muestra en un menú situado debajo del campo de
   * texto correspondiente los métodos de validación de <i>clusters<\i>
   * disponibles en la herramienta de <i>clustering<\i>.
   *
   */
  public void showValidityMethods() {
    //iniciliza un menú
    JPopupMenu menu = new JPopupMenu();
    //para cada uno de los métodos de validación disponibles...
    for (int i = 0; i < methods.length; i++) {
      //...crea un elemento del con el método actual...
      JMenuItem item = new JMenuItem(methods[i]);
      //...establece la fuente del elemento de menú...
      item.setFont(XConstants.font);
      //...añade el objeto que escucha los eventos de acción de dicho
      //elemento...
      item.addActionListener(this.actionListener);
      //...establece el comando que se ejecutará cuando se seleccione el
      //elemento del menú...
      item.setActionCommand(methods[i]);
      //...y añade el elemento al menú
      menu.add(item);
    }
    //tras haber creado el menú, lo muestra justo debajo del campo de texto que
    //sirve para elegir el método de validación
    menu.show(numClusters, 0, numClusters.getHeight());
  }

  /**
   * <p> <b>Descripción:</b> obtiene el método de validación de <i>clusters<\i>
   * que se debe de emplear para la simplificación del tipo.
   * @return Método de validación de <i>clusters<\i> elegido por el usuario, si
   * lo ha hecho, o null en caso de no haber elegido ninguno aún.
   *
   */
  public String getSelectedMethod() {
    //obtiene el método de validación de clusters del campo de texto
    //correspondiente
    String method = numClusters.getText();
    //si dicho campo de texto estaba vacío...
    if (method.equals("")) {
      //...devuelve "null"
      method = null;
    }
    return method;
  }

  /**
   * <p> <b>Descripción:</b> establece el método de validación de
   * <i>clusters<\i> seleccionado por el usuario.
   * @param method Método de validación de <i>clusters<\i> elegido por el
   * usuario.
   *
   */
  public void setValidityMethod(String method) {
    numClusters.setText(method);
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
   * <p> <b>Descripción:</b> obtiene el número de <i>clusters<\i> elegido por
   * el usuario para la simplificación del tipo cuando se utiliza la opción
   * <i>Custom<\i>.
   * @return Devuelve el número de <i>clusters<\i> elegido por el usuario para
   * la opción <i>Custom<\i> o -1 si no se ha elegido dicha opción.
   *
   */
  public int getNumberOfClusters() {
    return this.numberOfClusters;
  }

  /**
   * <p> <b>Descripción:</b> establece el número de <i>clusters<\i> elegido por
   * el usuario para la simplificación del tipo cuando se utiliza la opción
   * <i>Custom<\i>.
   * @param numberOfClusters Número de <i>clusters<\i> elegido por el usuario
   * para la opción <i>Custom<\i>.
   *
   */
  public void setNumberOfClusters(int numberOfClusters) {
    this.numberOfClusters = numberOfClusters;
  }

  /**
   * <p> <b>Descripción:</b> obtiene el valor del parámetro t para la
   * simplificación del tipo utilizando el método de validación de Davies &
   * Bouldin.
   * @return Valor del parámetro t elegido por el usuario para el método de
   * validación de Davies & Bouldin o -1 si no se ha elegido dicho método.
   *
   */
  public int getT() {
    return this.t;
  }

  /**
   * <p> <b>Descripción:</b> establece el valor del parámetro t para la
   * simplificación del tipo utilizando el método de validación de Davies &
   * Bouldin.
   * @param t Valor del parámetro t elegido por el usuario para el método de
   * validación de Davies & Bouldin.
   *
   */
  public void setT(int t) {
    this.t = t;
  }

  /**
   * <p> <b>Descripción:</b> obtiene el valor del parámetro q para la
   * simplificación del tipo utilizando el método de validación de Davies &
   * Bouldin.
   * @return Valor del parámetro q elegido por el usuario para el método de
   * validación de Davies & Bouldin o -1 si no se ha elegido dicho método.
   *
   */
  public int getQ() {
    return this.q;
  }

  /**
   * <p> <b>Descripción:</b> establece el valor del parámetro q para la
   * simplificación del tipo utilizando el método de validación de Davies &
   * Bouldin.
   * @param q Valor del parámetro q elegido por el usuario para el método de
   * validación de Davies & Bouldin.
   *
   */
  public void setQ(int q) {
    this.q = q;
  }

  /**
   * <p> <b>Descripción:</b> obtiene los valores asignados a los pesos de los
   * distintos parámetros de las funciones de pertenencia del tipo que se va a
   * simplificar.
   * @return Valores de los pesos asignados a los parámetros de las funciones
   * de pertenencia del tipo a simplificar (por defecto, se les asigna un valor
   * de 1 a todos los pesos).
   *
   */
  public double[] getWeights() {
    return weights;
  }

  /**
   * <p> <b>Descripción:</b> establece los valores para los pesos de los
   * parámetros de las funciones de pertenencia del tipo a simplificar.
   * @param newWeights Pesos asignados a los parámetros de las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  public void setWeights(double[] newWeights) {
    this.weights = newWeights;
  }

  /**
   * <p> <b>Descripción:</b> establece la cadena que se debe mostrar en el
   * campo de texto destinado a los pesos de los parámetros de las funciones de
   * pertenencia del tipo a simplificar.
   * @param weights Cadena que debe mostrar el campo de texto para los pesos de
   * las funciones de pertenencia del tipo a simplificar.
   *
   */
  public void setWeights(String weights) {
    weightsTextForm.setText(weights);
  }

  /**
   * <p> <b>Descripción:</b> obtiene los parámetros correspondientes a las
   * funciones de pertenencia del tipo a simplificar.
   * @return Parámetros correspondiente a las funciones de pertenencia del tipo
   * que se va a simplificar.
   *
   */
  public Parameter[] getParameters() {
    ParamMemFunc mf = (ParamMemFunc) type.getMembershipFunctions()[0];
    return mf.getParameters();
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
