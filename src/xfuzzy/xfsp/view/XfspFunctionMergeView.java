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
 * <p> <b>Descripci�n:</b> ventana que permite la selecci�n por parte del
 * usuario de la funci�n de pertenencia que sustituir� a dos funciones de
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
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603074L;

  //t�tulo de la vista
  private XLabel title;

  //panel que contendr� al t�tulo de la vista
  private JPanel titlePanel;

  //campo de texto que mostrar� el grado de similaridad existente entre las dos
  //funciones de pertenencia a fusionar
  private XTextForm similarityValueTextForm;

  //campo de texto que mostrar� el nombre de la primera de las funciones de
  //pertenencia a fusionar
  private XTextForm firstFunctionTextForm;

  //campo de texto que motrar� el nombre de la segunda de las funciones de
  //pertenencia a fusionar
  private XTextForm secondFunctionTextForm;

  //titulo para la regi�n de la ventana dedicada a la nueva funci�n de
  //pertenencia
  private XLabel newFunctionTitle;

  //campo de texto que servir� para introducir el paquete donde se encuentra
  //la definici�n del tipo de la nueva funci�n de pertenencia
  private XTextForm packageTextForm;

  //campo de texto que permitir� elegir el tipo de la nueva funci�n de
  //pertenencia
  private XTextForm memberFunctionTextForm;

  //bot�n que permitir� introducir los par�metros adecuados para la nueva
  //funci�n de pertenencia
  private XButton parametersButton;

  //vector que contendr� todos los campos de texto de la ventana para poder
  //realizar un ajuste �ptimo de la anchura de los mismo
  private XTextForm[] tvector;

  //panel que contendr� todos elementos situados en la parte izquierda de la
  //ventana
  private JPanel leftPanel;

  //gr�fico que permitir� representar las funciones de pertenencia a fusionar
  //as� como la nueva funci�n de pertenencia
  private XfspGraphPanel graph;

  //panel que contendr� todos los elementos situados en la parte derecha de la
  //ventana
  private JPanel rightPanel;

  //panel que contendr� los paneles situados en el centro de la ventana (a la
  //izquierda y a la derecha)
  private JPanel centralPanel;

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

  //paquete seleccionado por el usuario
  private XflPackage loaded;

  //vector que almacenar� las funciones de pertenencia del tipo elegido por el
  //usuario
  private String[] mf;

  //�ndice que permitir� conocer qu� funci�n de pertenencia ha sido elegida de
  //entre las definidas en el paquete seleccionado por el usuario
  private int index = 0;

  //grado de similaridad entre las dos funciones de pertenencia a fusionar
  private double similarityDegree;

  //funciones de pertenencia similares que van a ser sustituidas por la que
  //indique el usuario
  private ParamMemFunc pmf1, pmf2;

  //par�metros de la funci�n definida por el usuario para sustituir a las dos
  //funciones de pertenencia similares
  private Parameter[] parameters;

  //almacena si los par�metros introducidos por el usuario son correctos
  private boolean validParameters;

  //almac�n donde almacenar los enventos que la vista quiera dirigir al modelo
  //para que puedan ser procesador por alg�n controlador
  private XfspStore store;

  /**
   * <p> <b>Descripci�n:</b> crea una ventana que sirve para elegir la funci�n
   * de pertenencia que sustituya a dos funciones de pertenencia similares pero
   * de tipos muy distintos.
   * @param pmf1 Primera de las funciones de pertenencia a sustituir.
   * @param pmf2 Segunda de las funciones de pertenencia a sustituir.
   * @param store Almac�n donde se deben guardar los eventos producidos por la
   * ventana para que sean procesados por alg�n controlador.
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
    //establece el almac�n donde guardar los eventos
    this.store = store;
    //por defecto, los par�metros introducidos no son correctos
    this.validParameters = false;
  }

  /**
   * <p> <b>Descripci�n:</b> construye la ventana inicializando todos los
   * componentes que contiene.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    //as� como por las que permiten la configuraci�n de los par�metros de la
    //nueva funci�n de pertenencia
    actionListener = new XfspFunctionMergeActionListener(this, store);
    //construye el t�tulo de la ventana
    buildTitle();
    //construye la parte de la ventana que permite la introducci�n de la nueva
    //funci�n de pertenencia y sus par�metros por parte del usuario
    buildParameters();
    //construye el gr�fico que muestra las dos funciones de pertenencia a
    //sustituir as� como la nueva funci�n que ha elegido el usuario
    buildGraph();
    //construye la parte inferior de la ventana que permite la confirmaci�n de
    //los par�metros introducidos por el usuario
    buildConfirmation();
    // panel central para la fusi�n de funciones de pertenencia
    centralPanel = new JPanel();
    //establece el layout del panel anterior...
    centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
    //...a�ade el panel para la introducci�n de par�metros...
    centralPanel.add(leftPanel);
    //...y el que contiene el gr�fico con las funciones de pertenencia
    centralPanel.add(rightPanel);
    //obtiene el contenedor de la ventana
    Container content = getContentPane();
    //a�ade el t�tulo en la parte superior...
    content.add(titlePanel, BorderLayout.NORTH);
    //...el panel con la informaci�n de las funciones de pertenencia y el
    //gr�fico que las muestra en la parte central...
    content.add(centralPanel, BorderLayout.CENTER);
    //...y los botones de confirmaci�n de los par�metros en la parte inferior
    content.add(confirmPanel, BorderLayout.SOUTH);
    //establece la acci�n por defecto cuando se cierra la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //a�ade el objeto que debe responder a los eventos de ventana
    addWindowListener(new XfspWindowListener(this));
    //establece la fuente que se debe mostrar
    setFont(XConstants.font);
    //ajusta el tama�o que debe tener la ventana
    pack();
    //establece la posici�n de la ventana en el monitor
    setLocation();
  }

  /**
   * <p> <b>Descripci�n:</b> crea todos los componentes necesarios para mostrar
   * el t�tulo de la ventana.
   *
   */
  private void buildTitle() {
    //t�tulo de la ventana
    title = new XLabel("Membership function merging");
    //panel que contendr� el t�tulo de la ventana
    titlePanel = new JPanel(new GridLayout(1, 1));
    //a�ade el t�tulo al panel anterior
    titlePanel.add(title);
  }

  /**
   * <p> <b>Descripci�n:</b> crea todos los componentes necesarios para que el
   * usuario pueda seleccionar la nueva funci�n de pertenencia y establecer los
   * par�metros de la misma.
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
    //encabezamiento de la parte que permite al usuario la selecci�n de la
    //nueva funci�n de pertenencia as� como establecer sus par�metros
    newFunctionTitle = new XLabel("New Function Parameters");
    //campo de texto que permite al usuario seleccionar el paquete en el que se
    //encuentra definida la nueva funci�n de pertenencia
    packageTextForm = new XTextForm("Package", actionListener);
    //hace que el campo de texto anterior no sea editable por el usuario
    packageTextForm.setEditable(false);
    //establece el comando para cuando el usuario hace click en el bot�n del
    //campo de texto anterior
    packageTextForm.setActionCommand("PackageSelection");
    //campo de texto que permite al usuario la selecci�n de la nueva funci�n de
    //pertenencia de entre las definidas en el paquete seleccionado
    //anteriormente
    memberFunctionTextForm = new XTextForm("Membership Function",
                                           actionListener);
    //hace que el campo de texto anterior no sea editable por el usuario
    memberFunctionTextForm.setEditable(false);
    //establece el comando para cuando el usuario hace click en el bot�n del
    //campo de texto anterior
    memberFunctionTextForm.setActionCommand("MemberFunctionSelection");
    //bot�n que, si es pulsado, permite la edici�n de los par�metros que
    //definen la funci�n de pertenencia seleccionada
    parametersButton = new XButton("Set Parameters", "SetParameters",
                                   actionListener);
    //vector que contendr� todos los campos de texto utilizados
    tvector = new XTextForm[5];
    //a�ade los campos de texto al vector anterior
    tvector[0] = similarityValueTextForm;
    tvector[1] = firstFunctionTextForm;
    tvector[2] = secondFunctionTextForm;
    tvector[3] = packageTextForm;
    tvector[4] = memberFunctionTextForm;
    //establece la anchura de todos los campos de texto
    XTextForm.setWidth(tvector);
    //panel que contendr� todos los elementos que permiten al usuario la
    //selecci�n de la nueva funci�n de pertenencia
    leftPanel = new JPanel();
    //establece el layout del panel anterior...
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    //...y a�ade todos los elementos creados anteriormente
    leftPanel.add(similarityValueTextForm);
    leftPanel.add(firstFunctionTextForm);
    leftPanel.add(secondFunctionTextForm);
    leftPanel.add(newFunctionTitle);
    leftPanel.add(packageTextForm);
    leftPanel.add(memberFunctionTextForm);
    leftPanel.add(parametersButton);
  }

  /**
   * <p> <b>Descripci�n:</b> crea todos los componentes necesarios para mostrar
   * al usuario las funciones de pertenencia que van a ser sustituidas as� como
   * la seleccionada por �l para sustituirlas.
   *
   */
  private void buildGraph() {
    //representaci�n gr�fica de las funciones de pertenencia que se van a
    //fusionar
    graph = new XfspGraphPanel(80);
    //tipo ficticio que contendr� las dos funciones de pertenencia que se deben
    //fusionar y que permitir� al panel gr�fico la representaci�n de dichas
    //funciones
    Type auxType = null;
    try {
      //iniciliza el tipo ficticio
      auxType = new Type("FictitiousType", pmf1.u);
      //a�ade la primera funci�n de pertenencia que se debe fusionar...
      auxType.add(pmf1);
      //...y, a continuaci�n, la segunda
      auxType.add(pmf2);
      //establece en el panel gr�fico el tipo ficticio creado como aqu�l que
      //debe representar en la ventana
      graph.setSelection(auxType);
      //dibuja el panel gr�fico
      graph.repaint();
    }
    catch (XflException ex) {
    }
    //panel situado arriba a la derecha para la simplificaci�n de tipos
    rightPanel = new JPanel(new GridLayout(1, 1));
    //establece las dimensiones que debe ocupar el panel con el gr�fico de las
    //funciones de pertenencia a fusionar en la ventana
    rightPanel.setPreferredSize(new Dimension(300, 130));   //300, 200));
    //a�ade el gr�fico de representaci�n de las funciones de pertenencia al
    //panel de la derecha de la ventana
    rightPanel.add(graph);
  }

  /**
   * <p> <b>Descripci�n:</b> crea todos los componentes que permiten al usuario
   * confirmar la selecci�n hecha y as� poder continuar con el algoritmo de
   * simplificaci�n por similaridad.
   *
   */
  private void buildConfirmation() {
    String[] confirmation = {
        "Ok", "Cancel"};
    //botones que permiten confirmar la selecci�n hecha por el usuario
    confirmForm = new XCommandForm(confirmation, confirmation, actionListener);
    confirmForm.setCommandWidth(120);
    confirmForm.block();
    //panel que contendr� el conjunto de botones anterior
    confirmPanel = new JPanel();
    //establece el layout del panel anterior...
    confirmPanel.setLayout(new GridLayout(1, 1));
    //...as� como sus dimensiones...
    confirmPanel.setPreferredSize(new Dimension(400, 50));
    //...y le a�ade el conjunto de bontones creado anteriormente
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
   * <p> <b>Descripci�n:</b> estable la localizaci�n de la ventana padre de
   * �sta.
   * @param loc Localizaci�n de la ventana padre de la actual.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripci�n:</b> estable el grado de similaridad encontrado para
   * las dos funciones que van a ser sustituidas por la seleccionada por el
   * usuario.
   * @param similarityDegree Grado de similaridad de las dos funciones de
   * pertenencia a las que sustituir� la seleccionada por el usuario.
   *
   */
  public void setSimilarityDegree(double similarityDegree) {
    this.similarityDegree = similarityDegree;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el paquete con la definici�n de las
   * funciones de pertenencia entre las que puede elegir el usuario.
   * @return Paquete seleccionado por el usuario en el que se encuentran
   * definidas las funciones de pertenencia entre las que podr� elegir a la
   * nueva funci�n.
   *
   */
  public XflPackage getPackage() {
    return loaded;
  }

  /**
   * <p> <b>Descripci�n:</b> estable el paquete con la definici�n de las
   * funciones de pertenencia entre las que puede elegir el usuario.
   * @param packageName Nombre del paquete seleccionado por el usuario con las
   * definiciones de funciones de pertenencia entre las que podr� elegir a la
   * nueva funci�n.
   *
   */
  public void setPackage(String packageName) {
    //establece el nombre del paquete en el campo de texto a tal efecto
    packageTextForm.setText(packageName);
    //invalida los par�metros introducidos hasta el momento por el usuario
    validParameters = false;
  }

  /**
   * <p> <b>Descripci�n:</b> estable el paquete con la definici�n de las
   * funciones de pertenencia entre las que puede elegir el usuario.
   * @param loaded Paquete seleccionado por el usuario en el que se encuentran
   * definidas las funciones de pertenencia entre las que podr� elegir a la
   * nueva funci�n.
   *
   */
  public void setPackage(XflPackage loaded) {
    //establece el paquete seleccionado por el usuario
    this.loaded = loaded;
    //establece el nombre de dicho paquete en el campo de texto a tal efecto
    packageTextForm.setText(loaded.toString());
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene los par�metros que ha introducido el
   * usuario para la funci�n de pertenencia que ha seleccionado.
   * @return Par�metros introducidos por el usuario para la funci�n de
   * pertenencia seleccionada, si los ha introducido, o <i>null</i> en caso de
   * no haberlo hecho a�n.
   *
   */
  public Parameter[] getParameters() {
    //valor que se devolver�
    Parameter[] result;
    //si el usuario a introducido par�metros v�lidos para la funci�n de
    //pertenencia que ha seleccionado...
    if (validParameters) {
      //...devolver� dichos par�metros
      result = this.parameters;
    }
    //...en caso de no haberlo hecho...
    else {
      //...devolver� null
      result = null;
    }
    return result;
  }

  /**
   * <p> <b>Descripci�n:</b> estable los par�metros para la funci�n de
   * pertenencia seleccionada por el usuario.
   * @param parameters Par�metros introducidos por el usuarios para la funci�n
   * de pertenencia que ha seleccionado.
   *
   */
  public void setParameters(Parameter[] parameters) {
    //establece los par�metros...
    this.parameters = parameters;
    //...e indica que ahora ya se dispone de par�metros v�lidos para la funci�n
    //de pertenencia seleccionada por el usuario
    validParameters = true;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el nombre de la funci�n de pertenencia
   * seleccionada por el usuario.
   * @return Nombre de la funci�n de pertenencia que ha seleccionado el usuario
   * para sustituir a las dos funciones originales.
   *
   */
  public String getNewMFName() {
    //obtiene el nombre de la funci�n de pertenencia seleccionada por el
    //usuario del campo de texto adecuado
    return memberFunctionTextForm.getText();
  }

  /**
   * <p> <b>Descripci�n:</b> estable el nombre de la funci�n de pertenencia
   * seleccionada por el usuario para sustituir a las dos funciones originales.
   * @param functionName Nombre de la funci�n de pertenencia seleccionada por
   * el usuario.
   *
   */
  public void setMemberFunction(String functionName) {
    //establece el nombre de la funci�n de pertenencia seleccionada por el
    //usuario en el campo de texto apropiado
    memberFunctionTextForm.setText(functionName);
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve las funciones de pertenencia entre las
   * que puede elegir el usuario para sustituir a las funciones originales.
   * @return Conjunto de funciones de pertenencia entre las que el usuario
   * puede elegir para sustituir a las originales.
   *
   */
  public String[] getFunctions() {
    return mf;
  }

  /**
   * <p> <b>Descripci�n:</b> establece las funciones de pertenencia entre las
   * que puede elegir el usuario para sustituir a las funciones originales.
   * @param memberFunctions Conjunto de funciones de pertenencia entre las que
   * el usuario puede elegir para sustituir a las originales.
   *
   */
  public void setFunctions(String[] memberFunctions) {
    mf = memberFunctions;
  }

  /**
   * <p> <b>Descripci�n:</b> selecciona la siguiente funci�n de pertenencia de
   * entre las que el usuario puede elegir para sustituir a las originales.
   *
   */
  public void selectNextFunction() {
    //incremente el �ndice que lleva la cuenta de la funci�n de pertenencia
    //seleccionada por el usuario hasta el momento
    index = (index + 1) % mf.length;
    //si se hab�an establecido par�metros para la funci�n de pertenencia
    //seleccionada anteriormente...
    if (validParameters) {
      //...se invalidan dichos par�metros...
      validParameters = false;
      //...y se refresca el gr�fico que muestra las funciones de pertencia
      refreshGraph();
    }
  }

  /**
   * <p> <b>Descripci�n:</b> refresca el gr�fico que muestra las funciones de
   * pertenencia a sustituir as� como la funci�n de pertenencia selecciondada
   * por el usuario para sustituirlas (si lo ha hecho).
   *
   */
  public void refreshGraph() {
    //si existen par�metros v�lidos para la funci�n de pertenencia seleccionada
    //por el usuario...
    if (validParameters) {
      //obtiene el paquete seleccionado por el usuario...
      XflPackage xflpackage = this.getPackage();
      //...y el nombre de la funci�n de pertenencia seleccionada por el usuario
      String functionName = this.getNewMFName();
      //crea una nueva funci�n de pertenencia del tipo seleccionado por el
      //usuario
      ParamMemFunc mf = (ParamMemFunc) xflpackage.instantiate(functionName,
          XflPackage.MFUNC);
      //establece los par�metros para la nueva funci�n de pertenencia como los
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
      //tipo ficticio que contendr� las dos funciones de pertenencia que se
      //deben fusionar as� como la nueva funci�n seleccionada por el usuario y
      //que permitir� al panel gr�fico la representaci�n de dichas funciones
      Type auxType = null;
      try {
        //iniciliza el tipo ficticio
        auxType = new Type("FictitiousType", pmf1.u);
        //a�ade la primera funci�n de pertenencia que se debe fusionar...
        auxType.add(pmf1);
        //...y, a continuaci�n, la segunda
        auxType.add(pmf2);
        //a�ade la funci�n de pertenencia seleccionada por el usuario
        auxType.add(mf);
        //establece en el panel gr�fico el tipo ficticio creado como aqu�l que
        //debe representar en la ventana
        graph.setSelection(auxType);
        //selecciona la nueva funci�n de pertenencia definida por el usuario
        //para que sea dibujada en color rojo en el panel que muestra las
        //funciones de pertenencia
        graph.setSelection(mf);
        //dibuja el panel gr�fico
        graph.repaint();
      }
      catch (XflException e) {
      }
    }
    //si no hay par�metros adecuados para el tipo seleccionado...
    else {
      //...hace lo mismo que si no hubiera ning�n tipo seleccionado
      buildGraph();
    }
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el �ndice que ocupa la funci�n de
   * pertenencia seleccionada entre las del paquete elegido.
   * @return �ndice ocupado por la funci�n de pertenencia seleccionada dentro
   * de las definidas en el tipo elegido por el usuario.
   *
   */
  public int getIndex() {
    return index;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la primera de las funciones de
   * pertenencia a fusionar.
   * @return Primera de las funciones de pertenencia que se fusionar�n.
   *
   */
  public ParamMemFunc getFirstMF(){
    return this.pmf1;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la segunda de las funciones de
   * pertenencia a fusionar.
   * @return Segunda de las funciones de pertenencia que se fusionar�n.
   *
   */
  public ParamMemFunc getSecondMF(){
    return this.pmf2;
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
