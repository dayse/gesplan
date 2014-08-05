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
 * <p> <b>Descripción:</b> clase de objetos que atienden los eventos producidos
 * en las ventanas de introducción de parámetros para el método de
 * simplificación de tipos con técnicas de <i>clustering</i>.
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

  //ventana de selección de los distintos parámetros para poder llevar a cabo
  //el proceso de simplificación por clustering
  private XfspClusteringParametersView frame;

  //ventana que permite elegir los pesos asignados a los distintos parámetros
  //de las funciones de pertenencia
  private XfspNumericParametersView weightsFrame;

  //ventana que permite introducir valores numéricos para los distintos
  //parámetros de los algoritmos de validación de clusters
  private XfspNumericParametersView customFrame;

  //ventana que permite la introducción de los parámetros necesarios para
  //realizar la validación de clusters por el método de Davies & Bouldin
  //antiguo
  //private XfspClusteringDaviesBouldinParametersView dbFrame;
  //nuevo
  private XfspNumericParametersView dbFrame;

  //almacén donde se deben dejar los eventos del modelo y la vista del sistema
  //para ser procesados por el controlador
  private XfspStore store;

  //tipo que se quiere simplificar mediante clustering
  private Type type;

  /**
   * <p> <b>Descripción:</b> crea un objeto que responde a los distintos
   * eventos producidos por la ventana de introducción de parámetros para
   * el proceso de <b>clustering</b> así como por la ventana de introducción de
   * pesos y las ventanas de introducción de parámetros para los métodos de
   * validación de <b>clusters</b>.
   * @param frame Ventana que permite la elección de los parámetros asociados
   * a la simplificación de tipos mediante <b>clustering</b>.
   * @param store Almacén donde hay que enviar los mensajes para que puedan ser
   * procesados convenientemente por algún controlador.
   * @param type Tipo de un sistema difuso que debe ser simplificado mediante
   * <b>clustering</b>.
   *
   */
  public XfspClusteringActionListener(XfspClusteringParametersView frame,
                                      XfspStore store, Type type) {
    //llama al constructor del padre
    super(frame);
    //establece la ventana de parámetros para clustering que se escuchará
    this.frame = frame;
    //establece el almacén donde se deben dejar los eventos que se quieran procesar
    this.store = store;
    //establece el tipo que debe simplificar
    this.type = type;
  }

  /**
   * <p> <b>Descripción:</b> envía los parámetros introducidos en la vista
   * escuchada a un almacén para que sea procesado por algún controlador del
   * sistema.
   *
   */
  protected void sendParameters() {
    //almacena el número de parámetros de las funciones de pertenencia del tipo
    //a simplificar
    ParamMemFunc mf = (ParamMemFunc) type.getMembershipFunctions()[0];
    int numParameters = mf.getNumberOfParameters();
    //almacena los argumentos para realizar el proceso de clustering
    Object[] args = new Object[6 + numParameters];
    //almacena el número de clusters en que se deben agrupar las funciones de
    //pertenencia del tipo simplificado
    int num = frame.getNumberOfClusters();
    //almacena el valor del parámetro t para el método de validación de Davies &
    //Bouldin
    int t = frame.getT();
    //almacena el valor del parámetro q para el método de validación de Davies &
    //Bouldin
    int q = frame.getQ();
    //si no se ha seleccionado ningún método para el cálculo del número de
    //clusters...
    if (frame.getSelectedMethod() == null) {
      //...muestra un mensaje informativo en la pantalla pidiendo al usuario que
      //elija el método que desee de entre los disponibles
      String[] message = {
          "Incorrect argument for number of clusters method",
          "You must select a method to calculate the number of clusters"
      };
      XDialog.showMessage(this.frame, message);
    }
    //si el método para el cálculo del número de clusters en que se deben agrupar
    //las funciones de pertenencia ha sido elegido...
    else {
      //...comprueba que los parámetros que se introdujero para el método elegido
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
      //número de clusters en que se deben agrupar las funciones de pertenencia
      //del tipo simplificado
      Integer numClusters = new Integer(num);
      //almacena el método de validación de clusters que se debe emplear
      String measure = frame.getSelectedMethod();
      //establece los argumentos que se emplearán durante el proceso de
      //simplificación
      args[0] = type;
      args[1] = measure;
      args[2] = numClusters;
      args[3] = type;
      //vectro que almacenará los pesos asociados a los parámetros de las
      //funciones de pertenencia del tipo a simplificar
      double[] weights = frame.getWeights();
      //obtiene en formato Double los pesos asociados a todos los parámetros de
      //las funciones de pertenencia y las incorpora al vector que almacena los
      //argumentos utilizadas en el proceso de simplificación
      for (int i = 4; i < args.length - 2; i++) {
        args[i] = new Double(weights[i - 4]);
      }
      //almacena los argumentos específicos según el método de validación de
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
      //método de clustering y que contiene los argumentos que se deben
      //emplear...
      XfspEvent ev = new XfspEvent("Clustering", args);
      //...y lo almacena en un almacén
      store.store(ev);
      //cierra la ventana cuyos eventos se están atendiendo
      super.close();
    }
  }

  /**
   * <p> <b>Descripción:</b> acciones específicas para la vista destinada a la
   * introducción de los parámetros del método de <i>clustering<\i> que, en
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
      //elección de un número de clusters elegido por el usuario a su criterio
      actionCustom();
    }
    else if (command.equals("CustomOk")) {
      //acepta los parámetros introducidos por el usuario como número de
      //clusters en que se deben agrupar las funciones de pertenencia
      actionCustomOk();
    }
    else if (command.equals("CustomCancel")) {
      //rechaza los parámetros introducidos por el usuario como número de
      //clusters en que se deben agrupar las funciones de pertenencia y
      //establece el valor que hubiera anteriormente
      actionCustomCancel();
    }
    else if (command.equals("Davies&Bouldin")) {
      //elección de método de validación de Davies & Bouldin
      actionDaviesBouldin();
    }
    else if (command.equals("DaviesBouldinOk")) {
      //acepta los parámetros introducidos por el usuario para el método de
      //validación de clusters de Davies & Bouldin
      actionDaviesBouldinOk();
    }
    else if (command.equals("DaviesBouldinCancel")) {
      //rechaza los parámetros introducidos por el usuario para el método de
      //validación de clusters de Davies & Bouldin y reestablece los que
      //hubiera anteriormente
      actionDaviesBouldinCancel();
    }
    else if (command.equals("GeneralizedDunnIndex33")) {
      //elección de método de validación generalizados de Dunn
      actionGeneralizedDunnIndex33();
    }
    else if (command.equals("GeneralizedDunnIndex63")) {
      //elección de método de validación generalizados de Dunn
      actionGeneralizedDunnIndex63();
    }
    if (command.equals("NumberOfClusters")) {
      //elección del método por el que se obtendrá el número de clusters en que
      //hay que agrupar las funciones de pertenencia del tipo a simplificar
      actionNumberOfClusters();
    }
    else if (command.equals("SeparationIndex")) {
      //elección de método de validación basado en índices de separación de Dunn
      actionSeparationIndex();
    }
    else if (command.equals("Weights")) {
      //solicita la introducción de nuevos valores para los pesos asignados a
      //los parámetros de las funciones de pertenencia
      actionWeights();
    }
    else if (command.equals("WeightsOk")) {
      //acepta los pesos introducidos por el usuario en la ventana
      //correspondiente
      actionWeightsOk();
    }
    else if (command.equals("WeightsCancel")) {
      //rechaza los pesos introducidos por el usuario en la ventana
      //correspondiente y vuelve a tomar los pesos que había antes
      actionWeightsCancel();
    }
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario selecciona en
   * el menú correspondiente al número de <b>clusters</b> que elegirá
   * personalmente dicho número de <b>clusters</b>.
   *
   */
  private void actionCustom() {
    //etiqueta correspondiente al campo de texto para la introducción del
    //número de clusters elegido por el usuario
    String[] names = {
        "Number of clusters"};
    //valor inicial que tomará el campo de texto para la introducción del
    //número de clusters elegido por el usuario
    double[] initVal = {
        2.0};
    //crea una nueva ventana que permite la introducción de valores
    //estrictamente numéricos
    customFrame = new XfspNumericParametersView();
    //establece que se trata de un diálogo que no permite otras ventanas
    //activas al mismo tiempo
    customFrame.setModal(true);
    //establece el nombre de la ventana...
    customFrame.setName("Custom");
    //...le pone un título...
    customFrame.setTitle("Custom number of clusters");
    //...establece las etiquetas que deben mostrar los campos de texto de la
    //ventana...
    customFrame.setParametersNames(names);
    //...así como sus valores por defecto
    customFrame.setInitialValues(initVal);
    //indica que la ventana no presentará ninguna imagen correspondiente a
    //ninguna fórmula
    customFrame.setFormulaIcon(null);
    //establece el objeto que atenderá los eventos generados por la nueva
    //ventana como el objeto actual
    customFrame.setActionListener(this);
    //establece la localización de la nueva ventana
    customFrame.setParentLocation(frame.getLocation());
    //indica que los parámetros que se introducirá a través de la ventana
    //deberán de tipo entero
    customFrame.setIntParameters(true);
    //crea la ventana...
    customFrame.build();
    //...y la muestra en pantalla
    customFrame.setVisible(true);
    //establece en la ventana de parámetros del método de clustering que el
    //número de clusters en que agrupar las funciones de pertenencia se
    //determinará de forma manual por el usuario
    frame.setValidityMethod("Custom");
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario acepta el
   * número de <b>clusters</b> que ha introducido de forma manual en la
   * ventana correspondiente.
   *
   */
  private void actionCustomOk() {
    //obtiene en forma de cadena el primer parámetro de la ventana de
    //introducción de parámetros numéricos creada para la obtención del número
    //de clusters
    String numClustersString = customFrame.getNthParameter(0);
    //entero que contendrá el número de clusters elegido por el usuario
    int numClusters;
    try {
      //convierte la cadena del campo de texto correspondiente a un entero
      numClusters = Integer.parseInt(numClustersString);
      //establece en la ventana de parámetros del proceso de clustering el
      //número de clusters que ha elegido el usuario
      frame.setNumberOfClusters(numClusters);
      //cierra la ventana correspondiente a la introducción del número de
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
      //muestra un cuadro de diálogo con el mensaje anterior
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario rechaza el
   * número de <b>clusters</b> que ha introducido de forma manual en la
   * ventana correspondiente.
   *
   */
  private void actionCustomCancel() {
    //cierra la ventana correspondiente a la introducción del número de
    //clusters de manera manual por el usuario
    customFrame.close();
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario selecciona en
   * el menú correspondiente al número de <b>clusters</b> que elegirá el método
   * de validación de <i>clusters</i> de Davies & Bouldin.
   *
   */
  private void actionDaviesBouldin() {
    //etiquetas correspondiente a los campos de texto para la introducción de
    //los parámetros del método de validación de Davies & Bouldin
    String[] names = {
        "Paramter t", "Parameter q"};
    //valores iniciales que tomarán los campos de texto para la introducción de
    //los parámetros
    double[] initVal = {
        2.0, 2.0};
    //crea una nueva ventana que permite la introducción de valores
    //estrictamente numéricos
    dbFrame = new XfspNumericParametersView();
    //establece que se trata de un diálogo que no permite otras ventanas
    //activas al mismo tiempo
    dbFrame.setModal(true);
    //establece el nombre de la ventana...
    dbFrame.setName("DaviesBouldin");
    //...le pone un título...
    dbFrame.setTitle("Davies & Bouldin Validity Measure");
    //...establece las etiquetas que deben mostrar los campos de texto de la
    //ventana...
    dbFrame.setParametersNames(names);
    //...así como sus valores por defecto
    dbFrame.setInitialValues(initVal);
    //indica que la ventana no presentará ninguna imagen correspondiente a
    //ninguna fórmula
    dbFrame.setFormulaIcon(null);
    //establece el objeto que atenderá los eventos generados por la nueva
    //ventana como el objeto actual
    dbFrame.setActionListener(this);
    //establece la localización de la nueva ventana
    dbFrame.setParentLocation(frame.getLocation());
    //indica que los parámetros que se introducirán a través de la ventana
    //deberán de tipo entero
    dbFrame.setIntParameters(true);
    //crea la ventana...
    dbFrame.build();
    //...y la muestra en pantalla
    dbFrame.setVisible(true);
    //establece en la ventana de parámetros del método de clustering que el
    //número de clusters en que agrupar las funciones de pertenencia se
    //determinará por el método de validación de Davies & Bouldin
    frame.setValidityMethod("Davies&Bouldin");
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario acepta los
   * parámetros que ha introducido para el método de validación de Davies &
   * Bouldin en la ventana correspondiente.
   *
   */
  private void actionDaviesBouldinOk() {
    //obtiene en forma de cadena el primer parámetro de la ventana de
    //introducción de parámetros numéricos creada para la obtención del número
    //de clusters...
    String tString = dbFrame.getNthParameter(0);
    //...y el segundo
    String qString = dbFrame.getNthParameter(1);
    //almacena el parámetro t del método de validación de Davies & Bouldin
    int t;
    //almacena el parámetro t del método de validación de Davies & Bouldin
    int q;
    try {
      //convierte la cadena del campo de texto correspondiente a un entero
      t = Integer.parseInt(tString);
      q = Integer.parseInt(qString);
      //establece en la ventana de parámetros del proceso de clustering el
      //número de clusters que ha elegido el usuario
      frame.setT(t);
      frame.setQ(q);
      //cierra la ventana correspondiente a la introducción del número de
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
      //muestra un cuadro de diálogo con el mensaje anterior
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario rechaza los
   * parámetros que ha introducido para el método de validación de Davies &
   * Bouldin en la ventana correspondiente.
   *
   */
  private void actionDaviesBouldinCancel() {
    //cierra la ventana de introducción de parámetros para el método de
    //validación de Davies & Bouldin
    dbFrame.close();
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario pulsa sobre el
   * botón correspondiente al campo de texto para el número de <b>clusters</b>
   * en que se deben agrupar las funciones de pertenencia del tipo.
   *
   */
  private void actionNumberOfClusters() {
    //muestra en la ventana un menú con las distintas formas de calcular el
    //número de clusters óptimo para realizar la simplificación
    frame.showValidityMethods();
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario selecciona en
   * el menú correspondiente al número de <b>clusters</b> que éste se debe
   * calcular según el método de validación de <b>clusters</b> basado en el índice de
   * separación de Dunn.
   *
   */
  private void actionSeparationIndex() {
    //establece el método de validación correspondiente en la ventana de
    //introducción de parámetros para la simplificación por <b>clustering</b>.
    frame.setValidityMethod("SeparationIndex");
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario selecciona el
   * método de validación de <i>clusters</i> 33 generalizado a partir del
   * método de Dunn.
   *
   */
  private void actionGeneralizedDunnIndex33() {
    //establece el método de validación correspondiente en la ventana de
    //introducción de parámetros para la simplificación por <b>clustering</b>.
    frame.setValidityMethod("GeneralizedDunnIndex33");
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario selecciona el
   * método de validación de <i>clusters</i> 63 generalizado a partir del
   * método de Dunn.
   *
   */
  private void actionGeneralizedDunnIndex63() {
    //establece el método de validación correspondiente en la ventana de
    //introducción de parámetros para la simplificación por <b>clustering</b>.
    frame.setValidityMethod("GeneralizedDunnIndex63");
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario pulsa sobre el
   * botón correspondiente al campo de texto para los pesos que se deben
   * asignar a los parámetros de las funciones de pertenencia del tipo que se
   * quiere simplificar.
   *
   */
  private void actionWeights() {
    //array con los parámetros de las funciones de pertenencia del tipo que se
    //quiere simplificar
    Parameter[] parameters = frame.getParameters();
    //array con tantas cadenas como parámetros tengan las funciones de
    //pertenencia del tipo que se quiere simplificar
    String[] paramNames = new String[parameters.length];
    //inicializa cada cadena del array anterior con una breve descripción de a
    //qué parámetro corresponderá el peso que se va a introducir
    for (int i = 0; i < paramNames.length; i++) {
      paramNames[i] = "Weight for parameter " + parameters[i].getName();
    }
    //fichero cuya ruta es aquella donde se encuentra instalado el sistema
    //Xfuzzy
    File path = new File(System.getProperty("xfuzzy.path"));
    //fichero cuya ruta es la del gráfico con la fórmula de la distancia que se
    //emplea durante el proceso de clustering
    File icon = new File(path, "smp/gif/distance.gif");
    //crea una ventana de introducción de parámetros numéricos para los pesos
    weightsFrame = new XfspNumericParametersView();
    //establece que se trata de un diálogo que no permite otras ventanas
    //activas al mismo tiempo
    weightsFrame.setModal(true);
    //establece el nombre de la ventana
    weightsFrame.setName("Weights");
    //establece el título de la ventana
    weightsFrame.setTitle(
        "Weights for calculating membership function distances");
    //establece el nombre de los parámetros que se podrán introducir por la
    //ventana
    weightsFrame.setParametersNames(paramNames);
    //establece como valores iniciales para los campos de texto de los
    //parámetros de la ventana los pesos que ya estuvieran asignados a los
    //parámetros de las funciones de pertenencia del tipo a simplificar
    weightsFrame.setInitialValues(frame.getWeights());
    //establece el icono correspondiente a la fórmula de la distancia utilizada
    weightsFrame.setFormulaIcon(icon);
    //establece el objeto actual como el respondable de la atención de los
    //eventos generados en la nueva ventana
    weightsFrame.setActionListener(this);
    //establece la localización de la nueva ventana
    weightsFrame.setParentLocation(frame.getLocation());
    //construye la nueva ventana
    weightsFrame.build();
    //muestra la ventana de introducción de pesos para los parámetros de las
    //funciones de pertenencia
    weightsFrame.setVisible(true);
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario acepta los
   * pesos que ha introducido para los parámetros de las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  private void actionWeightsOk() {
    //array de double para los pesos que introduzca el usuario
    double[] weights = new double[frame.getWeights().length];
    //bandera que indicará si todos los pesos que introdujo el usuario eran
    //correctos
    boolean correctWeights = true;
    //cadena que contendrá una descripción de todos los pesos que haya
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
        //si el usuario dejó el campo de texto vacío...
        if (!weightString.equals("")) {
          //...crea un mensaje que indica que el campo de texto ha quedado en
          //blanco...
          String[] message = {
              "Incorrect weight for parameter " +
              frame.getParameters()[i].getName(),
              "You must enter a double number"
          };
          //...lo muestra en un cuadro de diálogo...
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
      //parámetros para clustering
      frame.setWeights(weights);
      //para cada uno de los pesos que haya introducido el usuario...
      for (int i = 0; i < weights.length; i++) {
        //...añade a la descripción de todos los pesos en forma de cadena la
        //del peso actual
        weightsString += "W(" + frame.getParameters()[i].getName() + ")=" +
            weights[i];
        //si aún no ha llegado al último peso...
        if (i < (weights.length - 1)) {
          //...añade el caracter punto y coma a la descripción de pesos en
          //forma de cadena
          weightsString += "; ";
        }
      }
      //establece en la ventana de parámetros para clustering la descripción
      //en forma de cadena de todos los pesos que ha introducido el usuario
      frame.setWeights(weightsString);
      //cierra la ventana que permite introducir pesos a los parámetros de las
      //funciones de pertenencia del tipo
      weightsFrame.close();
    }
  }

  /**
   * <p> <b>Descripción:</b> acción realizada cuando el usuario rechaza los
   * pesos que ha introducido para los parámetros de las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  private void actionWeightsCancel() {
    //cierra la ventana que permite introducir pesos a los parámetros de las
    //funciones de pertenencia del tipo
    weightsFrame.close();
  }
}
