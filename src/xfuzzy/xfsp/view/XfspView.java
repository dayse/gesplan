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

import java.awt.event.MouseAdapter;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xfuzzy.XfuzzyIcons;
import xfuzzy.lang.Type;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;

import xfuzzy.util.XCommandForm;
import xfuzzy.util.XConstants;
import xfuzzy.util.XDialog;
import xfuzzy.util.XLabel;
import xfuzzy.util.XList;

import xfuzzy.xfsp.XfspEvent;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.listener.XfspViewActionListener;
import xfuzzy.xfsp.view.listener.XfspWindowListener;

/**
 * <p> <b>Descripci�n:</b> Vista del sistema de simplificaci�n de sistemas
 * difusos que permite visualizar el sistema y seleccionar los distintos
 * m�todos de simplificaci�n.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 5.3
 * @see XfspController
 * @see XfspModel
 *
 */
public class XfspView
    extends JFrame
    implements IXfspView {

	/**
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603080L;

  //panel principal de la herramienta
  private JTabbedPane mainPane;

  //paneles para la simplificaci�n de tipos
  private JPanel typesPanel;
  private JPanel typesTop;
  private JPanel typesLeft;
  private JPanel typesRightTop;
  private JPanel typesRightDown;
  private JPanel typesRight;
  private JPanel typesCentral;

  //elementos para los paneles de simplificaci�n de tipos
  private XLabel typesTitle;
  private XList typesList;
  private XfspGraphPanel graph;
  private XCommandForm typesAlgorithm;

  //distintos algoritmos que se pueden utilizar para simplificar los tipos
  String[] typesAlgorithms = {
      "Purge", "Clustering", "Similarity"};

  //tipos del sistema
  private Type[] types;

  //paneles para la simplificaci�n de bases de reglas
  private JPanel rulesPanel;
  private JPanel rulesTop;
  private JPanel rulesLeft;
  private JPanel rulesRightTop;
  private JPanel rulesRightDown;
  private JPanel rulesRight;
  private JPanel rulesCentral;

  //elementos para los paneles de simplificaci�n de bases de reglas
  private XLabel rulesTitle;
  private XList ruleBasesList;
  private JTextArea rulesList;
  private JScrollPane rulesListPane;
  private XCommandForm rulesAlgorithm;

  //distintos algoritmos que se pueden utilizar para simplificar las reglas
  String[] rulesAlgorithms = {
      "Pruning", "Compress", "Expand", "TabularSimplification"};

  //bases de reglas del sistema
  private Rulebase[] ruleBases;

  //reglas de una base de reglas en particular
  private Rule[] rules;

  //opciones que se pueden elegir tras la simplificaci�n
  private XCommandForm finalOptions;
  private String[] finalOptionsLabel = {
      "Reload", "Apply", "Save", "Close"};

  private Point viewLocation;

  //almacena si el sistema fue llamado desde Xfuzzy
  private boolean xfuzzy;

  //referencia a la localizaci�n de la vista padre a esta (aquella que la
  //origin�)
  private Point parentLocation;

  //objeto que se encarga de escuchar los eventos generados por el usuario al
  //pulsar los distintos botones de la vista
  private XfspViewActionListener actionListener;

  //nombre del sistema a simplificar
  private String name;

  //almac�n donde deben ser guardados los eventos generados por la vista
  private XfspStore store;

  /**
   * <p> <b>Descripci�n:</b> crea una vista del sistema y le indica el almac�n
   * donde deber� guardar los eventos generados para que sean procesados.
   * @param store Almac�n donde se deben guardar los eventos producidos por la
   * vista para que sean procesados por alg�n controlador.
   *
   */
  public XfspView(XfspStore store) {
    //llama al constructor de la clase padre
    super("Xfsp");
    //establece el almac�n donde guardar los eventos
    this.store = store;
    this.viewLocation = null;
  }

  /**
   * <p> <b>Descripci�n:</b> construye todos los elementos de la interfaz
   * gr�fica correspondiente a la vista del sistema.
   *
   */
  public void build() {
    //crea el objeto que debe atender los eventos generados por esta ventana
    //as� como por las que permiten la configuraci�n de los pesos de las
    //funciones de pertenencia y de los m�todos de validaci�n de clusters
    actionListener = new XfspViewActionListener(this, this.store);
    //construye la ventana que permite la simplificaci�n de tipos
    buildTypes();
    //construye la ventana que permite la simplificaic�n de bases de reglas
    buildRules();
    //panel principal para la simplificaci�n de sistemas
    mainPane = new JTabbedPane();
    //a�ade una pesta�a para la simplificaci�n de tipos...
    mainPane.addTab("Types", typesPanel);
    //...y otra para la de bases de reglas
    mainPane.addTab("Rules", rulesPanel);
    //crea un conjunto de botones para aplicar cambios, grabar el sistema
    //obtenido, revertir cambios o cerrar la ventana...
    this.finalOptions = new XCommandForm(finalOptionsLabel, finalOptionsLabel,
                                         this.actionListener);
    //...establece la anchura que debe ocupar este conjunto de botones...
    this.finalOptions.setCommandWidth(80);
    //...y lo fija
    this.finalOptions.block();
    //obtiene el contenedor de la ventana...
    Container content = getContentPane();
    //...a�ade el panel con informaci�n de tipos y bases de reglas en el
    //centro...
    content.add(mainPane, BorderLayout.CENTER);
    //...y el conjunto de botones en la parte inferior
    content.add(finalOptions, BorderLayout.SOUTH);
    //establece la acci�n por defecto al cerrar la ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    //establece el icono que se muestra en la ventana
	setIconImage(XfuzzyIcons.xfuzzy.getImage());
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
   * <p> <b>Descripci�n:</b> construye todos los elementos de la interfaz
   * correspodientes a la simplificaci�n de tipos.
   *
   */
  private void buildTypes() {
    //t�tulo del panel para la simplificaci�n de tipos
    typesTitle = new XLabel("System types simplification for " + this.name);
    //crea el panel situado en la parte superior en la opci�n de tipos
    typesTop = new JPanel(new GridLayout(1, 1));
    //a�ade el titulo al panel
    typesTop.add(typesTitle);
    // tipos del sistema
    TypesListListener typesListener = new TypesListListener();
    typesList = new XList("Types");
    typesList.setListData(types);
    typesList.addListSelectionListener(typesListener);
    typesList.addMouseListener(typesListener);
    //representaci�n gr�fica de los tipos del sistema
    graph = new XfspGraphPanel(80);
    //crea un conjunto de bontones que permitan seleccionar c�mo se quieren
    //simplificar los tipos...
    typesAlgorithm = new XCommandForm(typesAlgorithms, typesAlgorithms,
                                      this.actionListener);
    //...establece la anchura...
    typesAlgorithm.setCommandWidth(120);
    //...y lo fija
    typesAlgorithm.block();
    //crea el panel situado a la izquierda para la simplificaci�n de tipos...
    typesLeft = new JPanel(new GridLayout(1, 1));
    //...establece sus dimensiones por defecto...
    typesLeft.setPreferredSize(new Dimension(175, 350));
    //...y le a�ade la lista con los tipos del sistema
    typesLeft.add(typesList);
    //crea el panel situado arriba a la derecha para la simplificaci�n de
    //tipos...
    typesRightTop = new JPanel(new GridLayout(1, 1));
    //...establece el tama�o por defecto...
    typesRightTop.setPreferredSize(new Dimension(400, 300));
    //...y le a�ade el gr�fico con la representaci�n del tipo seleccionado
    typesRightTop.add(graph);
    //crea el panel situado abajo a la derecha para la simplificaci�n de
    //tipos...
    typesRightDown = new JPanel(new GridLayout(1, 1));
    //...establece el tama�o por defecto...
    typesRightDown.setPreferredSize(new Dimension(400, 50));
    //...y le a�ade el panel con los botones con los algoritmos para la
    //simplificaci�n de tipos
    typesRightDown.add(typesAlgorithm);
    //crea el panel situado a la derecha para la simplificaci�n de tipos...
    typesRight = new JPanel();
    //...establece un layout que disponga los elementos de forma vertical...
    typesRight.setLayout(new BoxLayout(typesRight, BoxLayout.Y_AXIS));
    //...a�ade el panel con la representaci�n gr�fica de los tipos...
    typesRight.add(typesRightTop);
    //...y el panel con los botones de los algoritmos para la simplificaci�n
    typesRight.add(typesRightDown);
    //crea el panel central para la simplificaci�n de tipos
    typesCentral = new JPanel();
    //...establece un layout que disponga los elementos de forma horizontal...
    typesCentral.setLayout(new BoxLayout(typesCentral, BoxLayout.X_AXIS));
    //...a�ade el panel de la izquierda (con la lista de tipos del sistema)...
    typesCentral.add(typesLeft);
    //...y el de la derecha (con la representaci�n de los tipos y los botones
    //de los distintos algoritmos)
    typesCentral.add(typesRight);
    //crea el panel principal para la simplificaci�n de tipos...
    typesPanel = new JPanel(new BorderLayout());
    //...a�ade en la parte superior el panel de arriba (con el t�tulo de la
    //ventana)...
    typesPanel.add(typesTop, BorderLayout.NORTH);
    //...y en el centro el panel central (con la lista de tipos, la
    //representaci�n gr�fica de los tipos y los botones con los distintos
    //algoritmos)
    typesPanel.add(typesCentral, BorderLayout.CENTER);
  }

  /**
   * <p> <b>Descripci�n:</b> construye todos los elementos de la interfaz
   * correspodientes a la simplificaci�n de bases de reglas.
   *
   */
  private void buildRules() {
    //t�tulo del panel para la simplificaci�n de reglas
    rulesTitle = new XLabel("System rules simplification for " + this.name);
    //crea el panel situado en la parte superior en la opci�n de reglas
    rulesTop = new JPanel(new GridLayout(1, 1));
    //a�ade el t�tulo al panel
    rulesTop.add(rulesTitle);
    //bases de reglas del sistema
    RuleBasesListListener ruleBasesListener = new RuleBasesListListener();
    ruleBasesList = new XList("Rule Bases");
    ruleBasesList.setListData(ruleBases);
    ruleBasesList.addListSelectionListener(ruleBasesListener);
    ruleBasesList.addMouseListener(ruleBasesListener);
    //campo de texto que muestras las reglas de la base de reglas seleccionada
    rulesList = new JTextArea(400, 300);
    rulesList.setBackground(XConstants.textbackground);
    rulesList.setEditable(false);
    rulesListPane = new JScrollPane(rulesList);
    //algoritmos de simplificaci�n de bases de reglas
    rulesAlgorithm = new XCommandForm(rulesAlgorithms, rulesAlgorithms,
                                      this.actionListener);
    rulesAlgorithm.setCommandWidth(120);
    rulesAlgorithm.block();
    // panel situado a la izquierda en la opci�n de reglas
    rulesLeft = new JPanel(new GridLayout(1, 1));
    rulesLeft.setPreferredSize(new Dimension(175, 350));
    rulesLeft.add(ruleBasesList);
    // panel situado arriba a la derecha en la opci�n de reglas
    rulesRightTop = new JPanel(new GridLayout(1, 1));
    rulesRightTop.setPreferredSize(new Dimension(400, 300));
    rulesRightTop.add(rulesListPane); //rulesTabPane);
    // panel situado abajo a la derecha para la simplificaci�n de tipos
    rulesRightDown = new JPanel(new GridLayout(1, 1));
    rulesRightDown.setPreferredSize(new Dimension(400, 50));
    rulesRightDown.add(rulesAlgorithm);
    // panel situado a la derecha en la opci�n de reglas
    rulesRight = new JPanel();
    rulesRight.setLayout(new BoxLayout(rulesRight, BoxLayout.Y_AXIS));
    rulesRight.add(rulesRightTop);
    rulesRight.add(rulesRightDown);
    // panel situado en el centro en la opci�n de reglas
    rulesCentral = new JPanel();
    rulesCentral.setLayout(new BoxLayout(rulesCentral, BoxLayout.X_AXIS));
    rulesCentral.add(rulesLeft);
    rulesCentral.add(rulesRight);
    // panel principal para la simplificaci�n de reglas
    rulesPanel = new JPanel(new BorderLayout());
    rulesPanel.add(rulesTop, BorderLayout.NORTH);
    rulesPanel.add(rulesCentral, BorderLayout.CENTER);
  }

  /**
   * <p> <b>Descripci�n:</b> establece la localizaci�n de la vista.
   *
   */
  private void setLocation() {
    if (viewLocation == null) {

      if (xfuzzy) {
        Point loc = parentLocation;
        loc.x += 95;
        loc.y += 45;
        super.setLocation(loc);
      }
      else {
        Dimension frame = getSize();
        Dimension screen = getToolkit().getScreenSize();
        setLocation( (screen.width - frame.width) / 2,
                    (screen.height - frame.height) / 2);
      }
    }
    else {
      super.setLocation(viewLocation);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> permite activar o desactivar la vista.
   * @param enabled Activa la ventana si vale <i>true</i> y la desactiva si
   * vale <i>false</i>.
   *
   */
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    for (int i = 0; i < typesAlgorithms.length; i++) {
      typesAlgorithm.setEnabled(i, enabled);
    }
    for (int i = 0; i < rulesAlgorithms.length; i++) {
      rulesAlgorithm.setEnabled(i, enabled);
    }
    for (int i = 0; i < finalOptionsLabel.length; i++) {
      finalOptions.setEnabled(i, enabled);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve los tipos del sistema a simplificar.
   * @return Tipos del sistema que se est� tratando.
   *
   */
  public Type[] getTypes() {
    return this.types;
  }

  /**
   * <p> <b>Descripci�n:</b> establece los tipos del sistema a simplificar.
   * @param types Tipos del sistema que se est� tratando.
   *
   */
  public void setTypes(Type[] types) {
    this.types = types;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve las bases de reglas del sistema a
   * simplificar.
   * @return Bases de reglas del sistema que se est� tratando.
   *
   */
  public Rulebase[] getRuleBases() {
    return this.ruleBases;
  }

  /**
   * <p> <b>Descripci�n:</b> establece las bases de reglas del sistema a
   * simplificar.
   * @param ruleBases Bases de reglas del sistema que se est� tratando.
   *
   */
  public void setRuleBases(Rulebase[] ruleBases) {
    this.ruleBases = ruleBases;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el nombre del sistema a simplificar.
   * @return Nombre del sistema que se est� tratando.
   *
   */
  public String getName() {
    return this.name;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el nombre del sistema a simplificar.
   * @param name Nombre del sistema que se est� tratando.
   *
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve si se llam� al sistema de simplificaci�n
   * desde el sistema principal <i>Xfuzzy</i>.
   * @return Devuelve cierto si se llam� al sistema de simplificaci�n desde
   * <i>Xfuzzy</i> y falso si no fue as�.
   *
   */
  public boolean getXfuzzy() {
    return this.xfuzzy;
  }

  /**
   * <p> <b>Descripci�n:</b> establece si se llam� al sistema de simplificaci�n
   * desde el sistema principal <i>Xfuzzy</i>.
   * @param xfuzzy Vale cierto si se llam� al sistema de simplificaci�n desde
   * <i>Xfuzzy</i> y falso si no fue as�.
   *
   */
  public void xfuzzy(boolean xfuzzy) {
    this.xfuzzy = xfuzzy;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la localizaci�n de la ventana padre de la
   * vista.
   * @return Localizaci�n de la ventana padre de la vista.
   *
   */
  public Point getParentLocation() {
    return parentLocation;
  }

  /**
   * <p> <b>Descripci�n:</b> establece la localizaci�n de la ventana padre de
   * la vista.
   * @param loc Localizaci�n de la ventana padre de la vista.
   *
   */
  public void setParentLocation(Point loc) {
    parentLocation = loc;
  }

  /**
   * <p> <b>Descripci�n:</b> establece la localizaci�n de la vista.
   * @param loc Localizaci�n de la vista.
   *
   */
  public void setLocation(Point loc) {
    this.viewLocation = loc;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la lista de los tipos del sistema a
   * simplificar.
   * @return Lista de tipos del sistema a simplificar.
   *
   */
  public XList getTypesList() {
    return this.typesList;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la lista de las bases de reglas del
   * sistema a simplificar.
   * @return Lista de bases de reglas del sistema a simplificar.
   *
   */
  public XList getRuleBasesList() {
    return this.ruleBasesList;
  }

  /**
   * <p> <b>Descripci�n:</b> selecciona un tipo de entre los disponibles en el
   * sistema a simplificar.
   *
   */
  private void selectType() {
    Type selected = null;

    try {
      selected = (Type) typesList.getSelectedValue();
      graph.setSelection(selected);
      graph.repaint();
    }
    catch (Exception ex) {
    }
  }

  /**
   * <p> <b>Descripci�n:</b> selecciona una base de reglas de entre las
   * disponibles en el sistema a simplificar.
   *
   */
  private void selectRuleBase() {
    Rulebase selected = null;
    String rulesString = "";

    try {
      selected = (Rulebase) ruleBasesList.getSelectedValue();
      rules = selected.getRules();

      for (int i = 0; i < rules.length; i++) {
        rulesString += rules[i].toXfl();
      }
      //cambia la antigua lista de reglas por la nueva
      String oldText = rulesList.getText();
      rulesList.replaceRange(rulesString, 0, oldText.length());
    }
    catch (Exception ex) {
    }
  }

  /**
   * <p> <b>Descripci�n:</b> muestra un mensaje al usuario por pantalla.
   * @param msg Mensaje que se debe mostrar al usuario.
   *
   */
  public void showMessage(String[] msg) {
    XDialog.showMessage(this, msg);
  }

  /**
   * <p> <b>Descripci�n:</b> regenera la vista.
   *
   */
  public void refresh() {
    graph.repaint();
    selectRuleBase();
  }

  /**
   * <p> <b>Descripci�n:</b> cierra la ventana correspondiente a la vista y
   * termina el proceso de simplificaci�n.
   *
   */
  public void close() {
    //env�a al almac�n un evento indicando que se debe cerrar el sistema
    XfspEvent ev = new XfspEvent("Close", null);
    store.store(ev);
    //si el sistema de simplificaci�n no fue ejecutado desde Xfuzzy...
    if (!this.getXfuzzy()) {
      //...termina el proceso completamente
      System.exit(0);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> clase interna que atiende los eventos provocados
   * por la lista de tipos del sistema mostrada en la vista.
   * @version 1.0
   * @see XfspView
   *
   */
  private class TypesListListener
      extends MouseAdapter
      implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      selectType();
    }
  }

  /**
   * <p> <b>Descripci�n:</b> clase interna que atiende los eventos provocados
   * por la lista de bases de reglas del sistema mostrada en la vista.
   * @version 1.0
   * @see XfspView
   *
   */
  private class RuleBasesListListener
      extends MouseAdapter
      implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      selectRuleBase();
    }
  }
}
