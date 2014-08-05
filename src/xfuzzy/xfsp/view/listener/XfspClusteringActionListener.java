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

package xfuzzy.xfsp.view.listener;

import java.io.File;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xfuzzy.lang.Parameter;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Type;

import xfuzzy.util.XDialog;

import xfuzzy.xfsp.XfspEvent;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.XfspClusteringParametersView;
import xfuzzy.xfsp.view.XfspNumericParametersView;

/**
 * <p> <b>Descripci�n:</b> clase de objetos que atienden los eventos producidos
 * en las ventanas de introducci�n de par�metros para el m�todo de
 * simplificaci�n de tipos con t�cnicas de <i>clustering</i>.
 * <p>
 * @author Jorge Agudo Praena
 * @version 4.6
 * @see XfspClusteringParametersView
 * @see XfspNumericParametersView
 * @see XfspActionListener
 *
 */
public class XfspClusteringActionListener
    extends XfspActionListener
    implements ActionListener {

  //ventana de selecci�n de los distintos par�metros para poder llevar a cabo
  //el proceso de simplificaci�n por clustering
  private XfspClusteringParametersView frame;

  //ventana que permite elegir los pesos asignados a los distintos par�metros
  //de las funciones de pertenencia
  private XfspNumericParametersView weightsFrame;

  //ventana que permite introducir valores num�ricos para los distintos
  //par�metros de los algoritmos de validaci�n de clusters
  private XfspNumericParametersView customFrame;

  //ventana que permite la introducci�n de los par�metros necesarios para
  //realizar la validaci�n de clusters por el m�todo de Davies & Bouldin
  //antiguo
  //private XfspClusteringDaviesBouldinParametersView dbFrame;
  //nuevo
  private XfspNumericParametersView dbFrame;

  //almac�n donde se deben dejar los eventos del modelo y la vista del sistema
  //para ser procesados por el controlador
  private XfspStore store;

  //tipo que se quiere simplificar mediante clustering
  private Type type;

  /**
   * <p> <b>Descripci�n:</b> crea un objeto que responde a los distintos
   * eventos producidos por la ventana de introducci�n de par�metros para
   * el proceso de <b>clustering</b> as� como por la ventana de introducci�n de
   * pesos y las ventanas de introducci�n de par�metros para los m�todos de
   * validaci�n de <b>clusters</b>.
   * @param frame Ventana que permite la elecci�n de los par�metros asociados
   * a la simplificaci�n de tipos mediante <b>clustering</b>.
   * @param store Almac�n donde hay que enviar los mensajes para que puedan ser
   * procesados convenientemente por alg�n controlador.
   * @param type Tipo de un sistema difuso que debe ser simplificado mediante
   * <b>clustering</b>.
   *
   */
  public XfspClusteringActionListener(XfspClusteringParametersView frame,
                                      XfspStore store, Type type) {
    //llama al constructor del padre
    super(frame);
    //establece la ventana de par�metros para clustering que se escuchar�
    this.frame = frame;
    //establece el almac�n donde se deben dejar los eventos que se quieran procesar
    this.store = store;
    //establece el tipo que debe simplificar
    this.type = type;
  }

  /**
   * <p> <b>Descripci�n:</b> env�a los par�metros introducidos en la vista
   * escuchada a un almac�n para que sea procesado por alg�n controlador del
   * sistema.
   *
   */
  protected void sendParameters() {
    //almacena el n�mero de par�metros de las funciones de pertenencia del tipo
    //a simplificar
    ParamMemFunc mf = (ParamMemFunc) type.getMembershipFunctions()[0];
    int numParameters = mf.getNumberOfParameters();
    //almacena los argumentos para realizar el proceso de clustering
    Object[] args = new Object[6 + numParameters];
    //almacena el n�mero de clusters en que se deben agrupar las funciones de
    //pertenencia del tipo simplificado
    int num = frame.getNumberOfClusters();
    //almacena el valor del par�metro t para el m�todo de validaci�n de Davies &
    //Bouldin
    int t = frame.getT();
    //almacena el valor del par�metro q para el m�todo de validaci�n de Davies &
    //Bouldin
    int q = frame.getQ();
    //si no se ha seleccionado ning�n m�todo para el c�lculo del n�mero de
    //clusters...
    if (frame.getSelectedMethod() == null) {
      //...muestra un mensaje informativo en la pantalla pidiendo al usuario que
      //elija el m�todo que desee de entre los disponibles
      String[] message = {
          "Incorrect argument for number of clusters method",
          "You must select a method to calculate the number of clusters"
      };
      XDialog.showMessage(this.frame, message);
    }
    //si el m�todo para el c�lculo del n�mero de clusters en que se deben agrupar
    //las funciones de pertenencia ha sido elegido...
    else {
      //...comprueba que los par�metros que se introdujero para el m�todo elegido
      //son correctos
      if (frame.getSelectedMethod().equals("Custom") && num == -1) {
        String[] message = {
            "Incorrect argument for custom number of clusters",
            "You must enter the desired number of clusters"
        };
        XDialog.showMessage(this.frame, message);
      }
      else if (frame.getSelectedMethod().equals("Davies&Bouldin") &&
               (t == -1 || q == -1)) {
        String[] message = {
            "Incorrect arguments for Davies & Bouldin evaluation method",
            "You must enter the parameters t and q"
        };
        XDialog.showMessage(this.frame, message);
      }
      //n�mero de clusters en que se deben agrupar las funciones de pertenencia
      //del tipo simplificado
      Integer numClusters = new Integer(num);
      //almacena el m�todo de validaci�n de clusters que se debe emplear
      String measure = frame.getSelectedMethod();
      //establece los argumentos que se emplear�n durante el proceso de
      //simplificaci�n
      args[0] = type;
      args[1] = measure;
      args[2] = numClusters;
      args[3] = type;
      //vectro que almacenar� los pesos asociados a los par�metros de las
      //funciones de pertenencia del tipo a simplificar
      double[] weights = frame.getWeights();
      //obtiene en formato Double los pesos asociados a todos los par�metros de
      //las funciones de pertenencia y las incorpora al vector que almacena los
      //argumentos utilizadas en el proceso de simplificaci�n
      for (int i = 4; i < args.length - 2; i++) {
        args[i] = new Double(weights[i - 4]);
      }
      //almacena los argumentos espec�ficos seg�n el m�todo de validaci�n de
      //clusters seleccionado por el usuario
      if (measure.equals("Custom")) {
        args[args.length - 2] = new Integer(frame.getNumberOfClusters());
        args[args.length - 1] = new Integer(0);
      }
      else if (measure.equals("Davies&Bouldin")) {
        args[args.length - 2] = new Integer(2);
        args[args.length - 1] = new Integer(3);
      }
      else {
        args[args.length - 2] = new Integer(0);
        args[args.length - 1] = new Integer(0);
      }
      //crea un nuevo evento que indica que se debe simplificar utilizando el
      //m�todo de clustering y que contiene los argumentos que se deben
      //emplear...
      XfspEvent ev = new XfspEvent("Clustering", args);
      //...y lo almacena en un almac�n
      store.store(ev);
      //cierra la ventana cuyos eventos se est�n atendiendo
      super.close();
    }
  }

  /**
   * <p> <b>Descripci�n:</b> acciones espec�ficas para la vista destinada a la
   * introducci�n de los par�metros del m�todo de <i>clustering<\i> que, en
   * este caso, no hacen nada.
   * @param e Evento producido por la ventana escuchada.
   *
   */
  protected void specificActions(ActionEvent e) {
    //comando del evento que debe ser atendido
    String command = e.getActionCommand();
    //analiza los distintos comandos que admite el objeto y realiza las
    //acciones oportunas
    if (command.equals("Custom")) {
      //elecci�n de un n�mero de clusters elegido por el usuario a su criterio
      actionCustom();
    }
    else if (command.equals("CustomOk")) {
      //acepta los par�metros introducidos por el usuario como n�mero de
      //clusters en que se deben agrupar las funciones de pertenencia
      actionCustomOk();
    }
    else if (command.equals("CustomCancel")) {
      //rechaza los par�metros introducidos por el usuario como n�mero de
      //clusters en que se deben agrupar las funciones de pertenencia y
      //establece el valor que hubiera anteriormente
      actionCustomCancel();
    }
    else if (command.equals("Davies&Bouldin")) {
      //elecci�n de m�todo de validaci�n de Davies & Bouldin
      actionDaviesBouldin();
    }
    else if (command.equals("DaviesBouldinOk")) {
      //acepta los par�metros introducidos por el usuario para el m�todo de
      //validaci�n de clusters de Davies & Bouldin
      actionDaviesBouldinOk();
    }
    else if (command.equals("DaviesBouldinCancel")) {
      //rechaza los par�metros introducidos por el usuario para el m�todo de
      //validaci�n de clusters de Davies & Bouldin y reestablece los que
      //hubiera anteriormente
      actionDaviesBouldinCancel();
    }
    else if (command.equals("GeneralizedDunnIndex33")) {
      //elecci�n de m�todo de validaci�n generalizados de Dunn
      actionGeneralizedDunnIndex33();
    }
    else if (command.equals("GeneralizedDunnIndex63")) {
      //elecci�n de m�todo de validaci�n generalizados de Dunn
      actionGeneralizedDunnIndex63();
    }
    if (command.equals("NumberOfClusters")) {
      //elecci�n del m�todo por el que se obtendr� el n�mero de clusters en que
      //hay que agrupar las funciones de pertenencia del tipo a simplificar
      actionNumberOfClusters();
    }
    else if (command.equals("SeparationIndex")) {
      //elecci�n de m�todo de validaci�n basado en �ndices de separaci�n de Dunn
      actionSeparationIndex();
    }
    else if (command.equals("Weights")) {
      //solicita la introducci�n de nuevos valores para los pesos asignados a
      //los par�metros de las funciones de pertenencia
      actionWeights();
    }
    else if (command.equals("WeightsOk")) {
      //acepta los pesos introducidos por el usuario en la ventana
      //correspondiente
      actionWeightsOk();
    }
    else if (command.equals("WeightsCancel")) {
      //rechaza los pesos introducidos por el usuario en la ventana
      //correspondiente y vuelve a tomar los pesos que hab�a antes
      actionWeightsCancel();
    }
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario selecciona en
   * el men� correspondiente al n�mero de <b>clusters</b> que elegir�
   * personalmente dicho n�mero de <b>clusters</b>.
   *
   */
  private void actionCustom() {
    //etiqueta correspondiente al campo de texto para la introducci�n del
    //n�mero de clusters elegido por el usuario
    String[] names = {
        "Number of clusters"};
    //valor inicial que tomar� el campo de texto para la introducci�n del
    //n�mero de clusters elegido por el usuario
    double[] initVal = {
        2.0};
    //crea una nueva ventana que permite la introducci�n de valores
    //estrictamente num�ricos
    customFrame = new XfspNumericParametersView();
    //establece que se trata de un di�logo que no permite otras ventanas
    //activas al mismo tiempo
    customFrame.setModal(true);
    //establece el nombre de la ventana...
    customFrame.setName("Custom");
    //...le pone un t�tulo...
    customFrame.setTitle("Custom number of clusters");
    //...establece las etiquetas que deben mostrar los campos de texto de la
    //ventana...
    customFrame.setParametersNames(names);
    //...as� como sus valores por defecto
    customFrame.setInitialValues(initVal);
    //indica que la ventana no presentar� ninguna imagen correspondiente a
    //ninguna f�rmula
    customFrame.setFormulaIcon(null);
    //establece el objeto que atender� los eventos generados por la nueva
    //ventana como el objeto actual
    customFrame.setActionListener(this);
    //establece la localizaci�n de la nueva ventana
    customFrame.setParentLocation(frame.getLocation());
    //indica que los par�metros que se introducir� a trav�s de la ventana
    //deber�n de tipo entero
    customFrame.setIntParameters(true);
    //crea la ventana...
    customFrame.build();
    //...y la muestra en pantalla
    customFrame.setVisible(true);
    //establece en la ventana de par�metros del m�todo de clustering que el
    //n�mero de clusters en que agrupar las funciones de pertenencia se
    //determinar� de forma manual por el usuario
    frame.setValidityMethod("Custom");
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario acepta el
   * n�mero de <b>clusters</b> que ha introducido de forma manual en la
   * ventana correspondiente.
   *
   */
  private void actionCustomOk() {
    //obtiene en forma de cadena el primer par�metro de la ventana de
    //introducci�n de par�metros num�ricos creada para la obtenci�n del n�mero
    //de clusters
    String numClustersString = customFrame.getNthParameter(0);
    //entero que contendr� el n�mero de clusters elegido por el usuario
    int numClusters;
    try {
      //convierte la cadena del campo de texto correspondiente a un entero
      numClusters = Integer.parseInt(numClustersString);
      //establece en la ventana de par�metros del proceso de clustering el
      //n�mero de clusters que ha elegido el usuario
      frame.setNumberOfClusters(numClusters);
      //cierra la ventana correspondiente a la introducci�n del n�mero de
      //clusters de manera manual por el usuario
      customFrame.close();
    }
    catch (NumberFormatException e) {
      //mensaje que se muestra si la cadena introducida por el usuario no ha
      //podido ser convertida a entero de forma adecuada
      String[] message = {
          "Incorrect argument for custom number of clusters",
          "You must enter the desired number of clusters"
      };
      //muestra un cuadro de di�logo con el mensaje anterior
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario rechaza el
   * n�mero de <b>clusters</b> que ha introducido de forma manual en la
   * ventana correspondiente.
   *
   */
  private void actionCustomCancel() {
    //cierra la ventana correspondiente a la introducci�n del n�mero de
    //clusters de manera manual por el usuario
    customFrame.close();
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario selecciona en
   * el men� correspondiente al n�mero de <b>clusters</b> que elegir� el m�todo
   * de validaci�n de <i>clusters</i> de Davies & Bouldin.
   *
   */
  private void actionDaviesBouldin() {
    //etiquetas correspondiente a los campos de texto para la introducci�n de
    //los par�metros del m�todo de validaci�n de Davies & Bouldin
    String[] names = {
        "Paramter t", "Parameter q"};
    //valores iniciales que tomar�n los campos de texto para la introducci�n de
    //los par�metros
    double[] initVal = {
        2.0, 2.0};
    //crea una nueva ventana que permite la introducci�n de valores
    //estrictamente num�ricos
    dbFrame = new XfspNumericParametersView();
    //establece que se trata de un di�logo que no permite otras ventanas
    //activas al mismo tiempo
    dbFrame.setModal(true);
    //establece el nombre de la ventana...
    dbFrame.setName("DaviesBouldin");
    //...le pone un t�tulo...
    dbFrame.setTitle("Davies & Bouldin Validity Measure");
    //...establece las etiquetas que deben mostrar los campos de texto de la
    //ventana...
    dbFrame.setParametersNames(names);
    //...as� como sus valores por defecto
    dbFrame.setInitialValues(initVal);
    //indica que la ventana no presentar� ninguna imagen correspondiente a
    //ninguna f�rmula
    dbFrame.setFormulaIcon(null);
    //establece el objeto que atender� los eventos generados por la nueva
    //ventana como el objeto actual
    dbFrame.setActionListener(this);
    //establece la localizaci�n de la nueva ventana
    dbFrame.setParentLocation(frame.getLocation());
    //indica que los par�metros que se introducir�n a trav�s de la ventana
    //deber�n de tipo entero
    dbFrame.setIntParameters(true);
    //crea la ventana...
    dbFrame.build();
    //...y la muestra en pantalla
    dbFrame.setVisible(true);
    //establece en la ventana de par�metros del m�todo de clustering que el
    //n�mero de clusters en que agrupar las funciones de pertenencia se
    //determinar� por el m�todo de validaci�n de Davies & Bouldin
    frame.setValidityMethod("Davies&Bouldin");
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario acepta los
   * par�metros que ha introducido para el m�todo de validaci�n de Davies &
   * Bouldin en la ventana correspondiente.
   *
   */
  private void actionDaviesBouldinOk() {
    //obtiene en forma de cadena el primer par�metro de la ventana de
    //introducci�n de par�metros num�ricos creada para la obtenci�n del n�mero
    //de clusters...
    String tString = dbFrame.getNthParameter(0);
    //...y el segundo
    String qString = dbFrame.getNthParameter(1);
    //almacena el par�metro t del m�todo de validaci�n de Davies & Bouldin
    int t;
    //almacena el par�metro t del m�todo de validaci�n de Davies & Bouldin
    int q;
    try {
      //convierte la cadena del campo de texto correspondiente a un entero
      t = Integer.parseInt(tString);
      q = Integer.parseInt(qString);
      //establece en la ventana de par�metros del proceso de clustering el
      //n�mero de clusters que ha elegido el usuario
      frame.setT(t);
      frame.setQ(q);
      //cierra la ventana correspondiente a la introducci�n del n�mero de
      //clusters de manera manual por el usuario
      dbFrame.close();
    }
    catch (NumberFormatException e) {
      //mensaje que se muestra si la cadena introducida por el usuario no ha
      //podido ser convertida a entero de forma adecuada
      String[] message = {
          "Incorrect argument for Davies & Bouldin parameters",
          "You must enter an integer number"
      };
      //muestra un cuadro de di�logo con el mensaje anterior
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario rechaza los
   * par�metros que ha introducido para el m�todo de validaci�n de Davies &
   * Bouldin en la ventana correspondiente.
   *
   */
  private void actionDaviesBouldinCancel() {
    //cierra la ventana de introducci�n de par�metros para el m�todo de
    //validaci�n de Davies & Bouldin
    dbFrame.close();
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario pulsa sobre el
   * bot�n correspondiente al campo de texto para el n�mero de <b>clusters</b>
   * en que se deben agrupar las funciones de pertenencia del tipo.
   *
   */
  private void actionNumberOfClusters() {
    //muestra en la ventana un men� con las distintas formas de calcular el
    //n�mero de clusters �ptimo para realizar la simplificaci�n
    frame.showValidityMethods();
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario selecciona en
   * el men� correspondiente al n�mero de <b>clusters</b> que �ste se debe
   * calcular seg�n el m�todo de validaci�n de <b>clusters</b> basado en el �ndice de
   * separaci�n de Dunn.
   *
   */
  private void actionSeparationIndex() {
    //establece el m�todo de validaci�n correspondiente en la ventana de
    //introducci�n de par�metros para la simplificaci�n por <b>clustering</b>.
    frame.setValidityMethod("SeparationIndex");
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario selecciona el
   * m�todo de validaci�n de <i>clusters</i> 33 generalizado a partir del
   * m�todo de Dunn.
   *
   */
  private void actionGeneralizedDunnIndex33() {
    //establece el m�todo de validaci�n correspondiente en la ventana de
    //introducci�n de par�metros para la simplificaci�n por <b>clustering</b>.
    frame.setValidityMethod("GeneralizedDunnIndex33");
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario selecciona el
   * m�todo de validaci�n de <i>clusters</i> 63 generalizado a partir del
   * m�todo de Dunn.
   *
   */
  private void actionGeneralizedDunnIndex63() {
    //establece el m�todo de validaci�n correspondiente en la ventana de
    //introducci�n de par�metros para la simplificaci�n por <b>clustering</b>.
    frame.setValidityMethod("GeneralizedDunnIndex63");
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario pulsa sobre el
   * bot�n correspondiente al campo de texto para los pesos que se deben
   * asignar a los par�metros de las funciones de pertenencia del tipo que se
   * quiere simplificar.
   *
   */
  private void actionWeights() {
    //array con los par�metros de las funciones de pertenencia del tipo que se
    //quiere simplificar
    Parameter[] parameters = frame.getParameters();
    //array con tantas cadenas como par�metros tengan las funciones de
    //pertenencia del tipo que se quiere simplificar
    String[] paramNames = new String[parameters.length];
    //inicializa cada cadena del array anterior con una breve descripci�n de a
    //qu� par�metro corresponder� el peso que se va a introducir
    for (int i = 0; i < paramNames.length; i++) {
      paramNames[i] = "Weight for parameter " + parameters[i].getName();
    }
    //fichero cuya ruta es aquella donde se encuentra instalado el sistema
    //Xfuzzy
    File path = new File(System.getProperty("xfuzzy.path"));
    //fichero cuya ruta es la del gr�fico con la f�rmula de la distancia que se
    //emplea durante el proceso de clustering
    File icon = new File(path, "smp/gif/distance.gif");
    //crea una ventana de introducci�n de par�metros num�ricos para los pesos
    weightsFrame = new XfspNumericParametersView();
    //establece que se trata de un di�logo que no permite otras ventanas
    //activas al mismo tiempo
    weightsFrame.setModal(true);
    //establece el nombre de la ventana
    weightsFrame.setName("Weights");
    //establece el t�tulo de la ventana
    weightsFrame.setTitle(
        "Weights for calculating membership function distances");
    //establece el nombre de los par�metros que se podr�n introducir por la
    //ventana
    weightsFrame.setParametersNames(paramNames);
    //establece como valores iniciales para los campos de texto de los
    //par�metros de la ventana los pesos que ya estuvieran asignados a los
    //par�metros de las funciones de pertenencia del tipo a simplificar
    weightsFrame.setInitialValues(frame.getWeights());
    //establece el icono correspondiente a la f�rmula de la distancia utilizada
    weightsFrame.setFormulaIcon(icon);
    //establece el objeto actual como el respondable de la atenci�n de los
    //eventos generados en la nueva ventana
    weightsFrame.setActionListener(this);
    //establece la localizaci�n de la nueva ventana
    weightsFrame.setParentLocation(frame.getLocation());
    //construye la nueva ventana
    weightsFrame.build();
    //muestra la ventana de introducci�n de pesos para los par�metros de las
    //funciones de pertenencia
    weightsFrame.setVisible(true);
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario acepta los
   * pesos que ha introducido para los par�metros de las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  private void actionWeightsOk() {
    //array de double para los pesos que introduzca el usuario
    double[] weights = new double[frame.getWeights().length];
    //bandera que indicar� si todos los pesos que introdujo el usuario eran
    //correctos
    boolean correctWeights = true;
    //cadena que contendr� una descripci�n de todos los pesos que haya
    //introducido el usuario
    String weightsString = "";

    for (int i = 0; i < weights.length; i++) {
      //obtiene el siguiente peso introducido por el usuario en forma de cadena
      String weightString = weightsFrame.getNthParameter(i);
      //peso que ha introducido el usuario en forma de double
      double auxWeight = 0.0;
      try {
        //convierte el peso introducido por el usuario de string a double
        auxWeight = Double.parseDouble(weightString);
      }
      catch (NumberFormatException e) {
        //si el usuario dej� el campo de texto vac�o...
        if (!weightString.equals("")) {
          //...crea un mensaje que indica que el campo de texto ha quedado en
          //blanco...
          String[] message = {
              "Incorrect weight for parameter " +
              frame.getParameters()[i].getName(),
              "You must enter a double number"
          };
          //...lo muestra en un cuadro de di�logo...
          XDialog.showMessage(this.weightsFrame, message);
          //...e indica en una bandera que los pesos introducidos por el
          //usuario no son correctos
          correctWeights = false;
        }
      }
      //establece en el array de pesos el que ha introducido el usuario
      weights[i] = auxWeight;
    }
    //si todos los pesos que introdujo el usuario eran correctos...
    if (correctWeights) {
      //establece los pesos que haya introducido el usuario en la ventana de
      //par�metros para clustering
      frame.setWeights(weights);
      //para cada uno de los pesos que haya introducido el usuario...
      for (int i = 0; i < weights.length; i++) {
        //...a�ade a la descripci�n de todos los pesos en forma de cadena la
        //del peso actual
        weightsString += "W(" + frame.getParameters()[i].getName() + ")=" +
            weights[i];
        //si a�n no ha llegado al �ltimo peso...
        if (i < (weights.length - 1)) {
          //...a�ade el caracter punto y coma a la descripci�n de pesos en
          //forma de cadena
          weightsString += "; ";
        }
      }
      //establece en la ventana de par�metros para clustering la descripci�n
      //en forma de cadena de todos los pesos que ha introducido el usuario
      frame.setWeights(weightsString);
      //cierra la ventana que permite introducir pesos a los par�metros de las
      //funciones de pertenencia del tipo
      weightsFrame.close();
    }
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada cuando el usuario rechaza los
   * pesos que ha introducido para los par�metros de las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  private void actionWeightsCancel() {
    //cierra la ventana que permite introducir pesos a los par�metros de las
    //funciones de pertenencia del tipo
    weightsFrame.close();
  }
}
