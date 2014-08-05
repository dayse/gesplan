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
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import xfuzzy.util.XButton;
import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;

import xfuzzy.lang.Parameter;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Type;
import xfuzzy.lang.XflException;
import xfuzzy.lang.XflPackage;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.listener.XfspFunctionMergeActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripción:</b> ventana que permite la selección por parte del
 * usuario de la función de pertenencia que sustituirá a dos funciones de
 * pertenencia similares pero de tipos muy distintos.
 * <p>
 * @author Jorge Agudo Praena
 * @version 2.5
 * @see IXfspView
 *
 */
public class XfspFunctionMergeView
    extends JFrame
    implements IXfspView {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603074L;

  //título de la vista
  private XLabel title;

  //panel que contendrá al título de la vista
  private JPanel titlePanel;

  //campo de texto que mostrará el grado de similaridad existente entre las dos
  //funciones de pertenencia a fusionar
  private XTextForm similarityValueTextForm;

  //campo de texto que mostrará el nombre de la primera de las funciones de
  //pertenencia a fusionar
  private XTextForm firstFunctionTextForm;

  //campo de texto que motrará el nombre de la segunda de las funciones de
  //pertenencia a fusionar
  private XTextForm secondFunctionTextForm;

  //titulo para la región de la ventana dedicada a la nueva función de
  //pertenencia
  private XLabel newFunctionTitle;

  //campo de texto que servirá para introducir el paquete donde se encuentra
  //la definición del tipo de la nueva función de pertenencia
  private XTextForm packageTextForm;

  //campo de texto que permitirá elegir el tipo de la nueva función de
  //pertenencia
  private XTextForm memberFunctionTextForm;

  //botón que permitirá introducir los parámetros adecuados para la nueva
  //función de pertenencia
  private XButton parametersButton;

  //vector que contendrá todos los campos de texto de la ventana para poder
  //realizar un ajuste óptimo de la anchura de los mismo
  private XTextForm[] tvector;

  //panel que contendrá todos elementos situados en la parte izquierda de la
  //ventana
  private JPanel leftPanel;

  //gráfico que permitirá representar las funciones de pertenencia a fusionar
  //así como la nueva función de pertenencia
  private XfspGraphPanel graph;

  //panel que contendrá todos los elementos situados en la parte derecha de la
  //ventana
  private JPanel rightPanel;

  //panel que contendrá los paneles situados en el centro de la ventana (a la
  //izquierda y a la derecha)
  private JPanel centralPanel;

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
  private XfspFunctionMergeActionListener actionListener;

  //paquete seleccionado por el usuario
  private XflPackage loaded;

  //vector que almacenará las funciones de pertenencia del tipo elegido por el
  //usuario
  private String[] mf;

  //índice que permitirá conocer qué función de pertenencia ha sido elegida de
  //entre las definidas en el paquete seleccionado por el usuario
  private int index = 0;

  //grado de similaridad entre las dos funciones de pertenencia a fusionar
  private double similarityDegree;

  //funciones de pertenencia similares que van a ser sustituidas por la que
  //indique el usuario
  private ParamMemFunc pmf1, pmf2;

  //parámetros de la función definida por el usuario para sustituir a las dos
  //funciones de pertenencia similares
  private Parameter[] parameters;

  //almacena si los parámetros introducidos por el usuario son correctos
  private boolean validParameters;

  //almacén donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por algún controlador
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea una ventana que sirve para elegir la función
   * de pertenencia que sustituya a dos funciones de pertenencia similares pero
   * de tipos muy distintos.
   * @param pmf1 Primera de las funciones de pertenencia a sustituir.
   * @param pmf2 Segunda de las funciones de pertenencia a sustituir.
   * @param store Almacén donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por algún controlador.
   *
   */
  public XfspFunctionMergeView(ParamMemFunc pmf1, ParamMemFunc pmf2,
                               XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfsp");
    //establece la primera de las funciones de pertenencia a fusionar
    this.pmf1 = pmf1;
    //establece la segunda de las funciones de pertenencia a fusionar
    this.pmf2 = pmf2;
    //establece el almacén donde guardar los eventos
    this.store = store;
    //por defecto, los parámetros introducidos no son correctos
    this.validParameters = false;
  }

  /**
   * <p> <b>Descripción:</b> construye la ventana inicializando todos los
   * componentes que contiene.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    //así como por las que permiten la configuración de los parámetros de la
    //nueva función de pertenencia
    actionListener = new XfspFunctionMergeActionListener(this, store);
    //construye el título de la ventana
    buildTitle();
    //construye la parte de la ventana que permite la introducción de la nueva
    //función de pertenencia y sus parámetros por parte del usuario
    buildParameters();
    //construye el gráfico que muestra las dos funciones de pertenencia a
    //sustituir así como la nueva función que ha elegido el usuario
    buildGraph();
    //construye la parte inferior de la ventana que permite la confirmación de
    //los parámetros introducidos por el usuario
    buildConfirmation();
    // panel central para la fusión de funciones de pertenencia
    centralPanel = new JPanel();
    //establece el layout del panel anterior...
    centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
    //...añade el panel para la introducción de parámetros...
    centralPanel.add(leftPanel);
    //...y el que contiene el gráfico con las funciones de pertenencia
    centralPanel.add(rightPanel);
    //obtiene el contenedor de la ventana
    Container content = getContentPane();
    //añade el título en la parte superior...
    content.add(titlePanel, BorderLayout.NORTH);
    //...el panel con la información de las funciones de pertenencia y el
    //gráfico que las muestra en la parte central...
    content.add(centralPanel, BorderLayout.CENTER);
    //...y los botones de confirmación de los parámetros en la parte inferior
    content.add(confirmPanel, BorderLayout.SOUTH);
    //establece la acción por defecto cuando se cierra la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //añade el objeto que debe responder a los eventos de ventana
    addWindowListener(new XfspWindowListener(this));
    //establece la fuente que se debe mostrar
    setFont(XConstants.font);
    //ajusta el tamaño que debe tener la ventana
    pack();
    //establece la posición de la ventana en el monitor
    setLocation();
  }

  /**
   * <p> <b>Descripción:</b> crea todos los componentes necesarios para mostrar
   * el título de la ventana.
   *
   */
  private void buildTitle() {
    //título de la ventana
    title = new XLabel("Membership function merging");
    //panel que contendrá el título de la ventana
    titlePanel = new JPanel(new GridLayout(1, 1));
    //añade el título al panel anterior
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripción:</b> crea todos los componentes necesarios para que el
   * usuario pueda seleccionar la nueva función de pertenencia y establecer los
   * parámetros de la misma.
   *
   */
  private void buildParameters() {
    //campo de texto que muestra el grado de similaridad de las dos funciones
    //de pertenencia que se quieren unir
    similarityValueTextForm = new XTextForm("Similarity Degree");
    //establece el grado de pertenencia de las dos funciones a sustituir
    similarityValueTextForm.setText("" + similarityDegree);
    //hace que el campo de texto anterior no sea editable por el usuario
    similarityValueTextForm.setEditable(false);
    //campo de texto que muestra el nombre de la primera de las funciones a
    //sustituir
    firstFunctionTextForm = new XTextForm("First Function");
    //establece el nombre de la primera de las funciones a sustituir
    firstFunctionTextForm.setText(pmf1.getLabel());
    //hace que el campo de texto anterior no sea editable por el usuario
    firstFunctionTextForm.setEditable(false);
    //campo de texto que muestra el nombre de la segunda de las funciones a
    //sustituir
    secondFunctionTextForm = new XTextForm("Second Function");
    //establece el nombre de la segunda de las funciones a sustituir
    secondFunctionTextForm.setText(pmf2.getLabel());
    //hace que el campo de texto anterior no sea editable por el usuario
    secondFunctionTextForm.setEditable(false);
    //encabezamiento de la parte que permite al usuario la selección de la
    //nueva función de pertenencia así como establecer sus parámetros
    newFunctionTitle = new XLabel("New Function Parameters");
    //campo de texto que permite al usuario seleccionar el paquete en el que se
    //encuentra definida la nueva función de pertenencia
    packageTextForm = new XTextForm("Package", actionListener);
    //hace que el campo de texto anterior no sea editable por el usuario
    packageTextForm.setEditable(false);
    //establece el comando para cuando el usuario hace click en el botón del
    //campo de texto anterior
    packageTextForm.setActionCommand("PackageSelection");
    //campo de texto que permite al usuario la selección de la nueva función de
    //pertenencia de entre las definidas en el paquete seleccionado
    //anteriormente
    memberFunctionTextForm = new XTextForm("Membership Function",
                                           actionListener);
    //hace que el campo de texto anterior no sea editable por el usuario
    memberFunctionTextForm.setEditable(false);
    //establece el comando para cuando el usuario hace click en el botón del
    //campo de texto anterior
    memberFunctionTextForm.setActionCommand("MemberFunctionSelection");
    //botón que, si es pulsado, permite la edición de los parámetros que
    //definen la función de pertenencia seleccionada
    parametersButton = new XButton("Set Parameters", "SetParameters",
                                   actionListener);
    //vector que contendrá todos los campos de texto utilizados
    tvector = new XTextForm[5];
    //añade los campos de texto al vector anterior
    tvector[0] = similarityValueTextForm;
    tvector[1] = firstFunctionTextForm;
    tvector[2] = secondFunctionTextForm;
    tvector[3] = packageTextForm;
    tvector[4] = memberFunctionTextForm;
    //establece la anchura de todos los campos de texto
    XTextForm.setWidth(tvector);
    //panel que contendrá todos los elementos que permiten al usuario la
    //selección de la nueva función de pertenencia
    leftPanel = new JPanel();
    //establece el layout del panel anterior...
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    //...y añade todos los elementos creados anteriormente
    leftPanel.add(similarityValueTextForm);
    leftPanel.add(firstFunctionTextForm);
    leftPanel.add(secondFunctionTextForm);
    leftPanel.add(newFunctionTitle);
    leftPanel.add(packageTextForm);
    leftPanel.add(memberFunctionTextForm);
    leftPanel.add(parametersButton);
  }

  /**
   * <p> <b>Descripción:</b> crea todos los componentes necesarios para mostrar
   * al usuario las funciones de pertenencia que van a ser sustituidas así como
   * la seleccionada por él para sustituirlas.
   *
   */
  private void buildGraph() {
    //representación gráfica de las funciones de pertenencia que se van a
    //fusionar
    graph = new XfspGraphPanel(80);
    //tipo ficticio que contendrá las dos funciones de pertenencia que se deben
    //fusionar y que permitirá al panel gráfico la representación de dichas
    //funciones
    Type auxType = null;
    try {
      //iniciliza el tipo ficticio
      auxType = new Type("FictitiousType", pmf1.u);
      //añade la primera función de pertenencia que se debe fusionar...
      auxType.add(pmf1);
      //...y, a continuación, la segunda
      auxType.add(pmf2);
      //establece en el panel gráfico el tipo ficticio creado como aquél que
      //debe representar en la ventana
      graph.setSelection(auxType);
      //dibuja el panel gráfico
      graph.repaint();
    }
    catch (XflException ex) {
    }
    //panel situado arriba a la derecha para la simplificación de tipos
    rightPanel = new JPanel(new GridLayout(1, 1));
    //establece las dimensiones que debe ocupar el panel con el gráfico de las
    //funciones de pertenencia a fusionar en la ventana
    rightPanel.setPreferredSize(new Dimension(300, 130));   //300, 200));
    //añade el gráfico de representación de las funciones de pertenencia al
    //panel de la derecha de la ventana
    rightPanel.add(graph);
  }

  /**
   * <p> <b>Descripción:</b> crea todos los componentes que permiten al usuario
   * confirmar la selección hecha y así poder continuar con el algoritmo de
   * simplificación por similaridad.
   *
   */
  private void buildConfirmation() {
    String[] confirmation = {
        "Ok", "Cancel"};
    //botones que permiten confirmar la selección hecha por el usuario
    confirmForm = new XCommandForm(confirmation, confirmation, actionListener);
    confirmForm.setCommandWidth(120);
    confirmForm.block();
    //panel que contendrá el conjunto de botones anterior
    confirmPanel = new JPanel();
    //establece el layout del panel anterior...
    confirmPanel.setLayout(new GridLayout(1, 1));
    //...así como sus dimensiones...
    confirmPanel.setPreferredSize(new Dimension(400, 50));
    //...y le añade el conjunto de bontones creado anteriormente
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
   * <p> <b>Descripción:</b> estable la localización de la ventana padre de
   * ésta.
   * @param loc Localización de la ventana padre de la actual.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripción:</b> estable el grado de similaridad encontrado para
   * las dos funciones que van a ser sustituidas por la seleccionada por el
   * usuario.
   * @param similarityDegree Grado de similaridad de las dos funciones de
   * pertenencia a las que sustituirá la seleccionada por el usuario.
   *
   */
  public void setSimilarityDegree(double similarityDegree) {
    this.similarityDegree = similarityDegree;
  }

  /**
   * <p> <b>Descripción:</b> devuelve el paquete con la definición de las
   * funciones de pertenencia entre las que puede elegir el usuario.
   * @return Paquete seleccionado por el usuario en el que se encuentran
   * definidas las funciones de pertenencia entre las que podrá elegir a la
   * nueva función.
   *
   */
  public XflPackage getPackage() {
    return loaded;
  }

  /**
   * <p> <b>Descripción:</b> estable el paquete con la definición de las
   * funciones de pertenencia entre las que puede elegir el usuario.
   * @param packageName Nombre del paquete seleccionado por el usuario con las
   * definiciones de funciones de pertenencia entre las que podrá elegir a la
   * nueva función.
   *
   */
  public void setPackage(String packageName) {
    //establece el nombre del paquete en el campo de texto a tal efecto
    packageTextForm.setText(packageName);
    //invalida los parámetros introducidos hasta el momento por el usuario
    validParameters = false;
  }

  /**
   * <p> <b>Descripción:</b> estable el paquete con la definición de las
   * funciones de pertenencia entre las que puede elegir el usuario.
   * @param loaded Paquete seleccionado por el usuario en el que se encuentran
   * definidas las funciones de pertenencia entre las que podrá elegir a la
   * nueva función.
   *
   */
  public void setPackage(XflPackage loaded) {
    //establece el paquete seleccionado por el usuario
    this.loaded = loaded;
    //establece el nombre de dicho paquete en el campo de texto a tal efecto
    packageTextForm.setText(loaded.toString());
  }

  /**
   * <p> <b>Descripción:</b> obtiene los parámetros que ha introducido el
   * usuario para la función de pertenencia que ha seleccionado.
   * @return Parámetros introducidos por el usuario para la función de
   * pertenencia seleccionada, si los ha introducido, o <i>null</i> en caso de
   * no haberlo hecho aún.
   *
   */
  public Parameter[] getParameters() {
    //valor que se devolverá
    Parameter[] result;
    //si el usuario a introducido parámetros válidos para la función de
    //pertenencia que ha seleccionado...
    if (validParameters) {
      //...devolverá dichos parámetros
      result = this.parameters;
    }
    //...en caso de no haberlo hecho...
    else {
      //...devolverá null
      result = null;
    }
    return result;
  }

  /**
   * <p> <b>Descripción:</b> estable los parámetros para la función de
   * pertenencia seleccionada por el usuario.
   * @param parameters Parámetros introducidos por el usuarios para la función
   * de pertenencia que ha seleccionado.
   *
   */
  public void setParameters(Parameter[] parameters) {
    //establece los parámetros...
    this.parameters = parameters;
    //...e indica que ahora ya se dispone de parámetros válidos para la función
    //de pertenencia seleccionada por el usuario
    validParameters = true;
  }

  /**
   * <p> <b>Descripción:</b> devuelve el nombre de la función de pertenencia
   * seleccionada por el usuario.
   * @return Nombre de la función de pertenencia que ha seleccionado el usuario
   * para sustituir a las dos funciones originales.
   *
   */
  public String getNewMFName() {
    //obtiene el nombre de la función de pertenencia seleccionada por el
    //usuario del campo de texto adecuado
    return memberFunctionTextForm.getText();
  }

  /**
   * <p> <b>Descripción:</b> estable el nombre de la función de pertenencia
   * seleccionada por el usuario para sustituir a las dos funciones originales.
   * @param functionName Nombre de la función de pertenencia seleccionada por
   * el usuario.
   *
   */
  public void setMemberFunction(String functionName) {
    //establece el nombre de la función de pertenencia seleccionada por el
    //usuario en el campo de texto apropiado
    memberFunctionTextForm.setText(functionName);
  }

  /**
   * <p> <b>Descripción:</b> devuelve las funciones de pertenencia entre las
   * que puede elegir el usuario para sustituir a las funciones originales.
   * @return Conjunto de funciones de pertenencia entre las que el usuario
   * puede elegir para sustituir a las originales.
   *
   */
  public String[] getFunctions() {
    return mf;
  }

  /**
   * <p> <b>Descripción:</b> establece las funciones de pertenencia entre las
   * que puede elegir el usuario para sustituir a las funciones originales.
   * @param memberFunctions Conjunto de funciones de pertenencia entre las que
   * el usuario puede elegir para sustituir a las originales.
   *
   */
  public void setFunctions(String[] memberFunctions) {
    mf = memberFunctions;
  }

  /**
   * <p> <b>Descripción:</b> selecciona la siguiente función de pertenencia de
   * entre las que el usuario puede elegir para sustituir a las originales.
   *
   */
  public void selectNextFunction() {
    //incremente el índice que lleva la cuenta de la función de pertenencia
    //seleccionada por el usuario hasta el momento
    index = (index + 1) % mf.length;
    //si se habían establecido parámetros para la función de pertenencia
    //seleccionada anteriormente...
    if (validParameters) {
      //...se invalidan dichos parámetros...
      validParameters = false;
      //...y se refresca el gráfico que muestra las funciones de pertencia
      refreshGraph();
    }
  }

  /**
   * <p> <b>Descripción:</b> refresca el gráfico que muestra las funciones de
   * pertenencia a sustituir así como la función de pertenencia selecciondada
   * por el usuario para sustituirlas (si lo ha hecho).
   *
   */
  public void refreshGraph() {
    //si existen parámetros válidos para la función de pertenencia seleccionada
    //por el usuario...
    if (validParameters) {
      //obtiene el paquete seleccionado por el usuario...
      XflPackage xflpackage = this.getPackage();
      //...y el nombre de la función de pertenencia seleccionada por el usuario
      String functionName = this.getNewMFName();
      //crea una nueva función de pertenencia del tipo seleccionado por el
      //usuario
      ParamMemFunc mf = (ParamMemFunc) xflpackage.instantiate(functionName,
          XflPackage.MFUNC);
      //establece los parámetros para la nueva función de pertenencia como los
      //que ha indicado el usuario
      Parameter[] parameters = this.getParameters();
      double[] value = new double[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
       value[i] = parameters[i].value;
      }
      try {
       mf.set(value);
      }
      catch (XflException e) {
      }
      //tipo ficticio que contendrá las dos funciones de pertenencia que se
      //deben fusionar así como la nueva función seleccionada por el usuario y
      //que permitirá al panel gráfico la representación de dichas funciones
      Type auxType = null;
      try {
        //iniciliza el tipo ficticio
        auxType = new Type("FictitiousType", pmf1.u);
        //añade la primera función de pertenencia que se debe fusionar...
        auxType.add(pmf1);
        //...y, a continuación, la segunda
        auxType.add(pmf2);
        //añade la función de pertenencia seleccionada por el usuario
        auxType.add(mf);
        //establece en el panel gráfico el tipo ficticio creado como aquél que
        //debe representar en la ventana
        graph.setSelection(auxType);
        //selecciona la nueva función de pertenencia definida por el usuario
        //para que sea dibujada en color rojo en el panel que muestra las
        //funciones de pertenencia
        graph.setSelection(mf);
        //dibuja el panel gráfico
        graph.repaint();
      }
      catch (XflException e) {
      }
    }
    //si no hay parámetros adecuados para el tipo seleccionado...
    else {
      //...hace lo mismo que si no hubiera ningún tipo seleccionado
      buildGraph();
    }
  }

  /**
   * <p> <b>Descripción:</b> devuelve el índice que ocupa la función de
   * pertenencia seleccionada entre las del paquete elegido.
   * @return Índice ocupado por la función de pertenencia seleccionada dentro
   * de las definidas en el tipo elegido por el usuario.
   *
   */
  public int getIndex() {
    return index;
  }

  /**
   * <p> <b>Descripción:</b> devuelve la primera de las funciones de
   * pertenencia a fusionar.
   * @return Primera de las funciones de pertenencia que se fusionarán.
   *
   */
  public ParamMemFunc getFirstMF(){
    return this.pmf1;
  }

  /**
   * <p> <b>Descripción:</b> devuelve la segunda de las funciones de
   * pertenencia a fusionar.
   * @return Segunda de las funciones de pertenencia que se fusionarán.
   *
   */
  public ParamMemFunc getSecondMF(){
    return this.pmf2;
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
