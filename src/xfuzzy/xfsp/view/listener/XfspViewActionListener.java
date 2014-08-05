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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.Type;
import xfuzzy.lang.Relation;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;

import xfuzzy.util.XDialog;

import xfuzzy.xfsp.XfspEvent;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.XfspClusteringParametersView;
import xfuzzy.xfsp.view.XfspSimilarityParametersView;
import xfuzzy.xfsp.view.XfspPruningView;
import xfuzzy.xfsp.view.XfspView;

/**
 * <p> <b>Descripción:</b> clase de objetos que atienden los eventos producidos
 * en la vista principal del sistema.
 * <p>
 * @author Jorge Agudo Praena
 * @version 2.3
 * @see XfspView
 * @see XfspActionListener
 *
 */
public class XfspViewActionListener
    extends XfspActionListener
    implements ActionListener {

  //ventana para la introducción de los parámetros del método de similaridad
  private XfspView frame;

  //almacén donde se deben dejar los eventos del modelo y la vista del sistema
  //para ser procesados por el controlador
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea un objeto que responde a los distintos
   * eventos producidos por la ventana de introducción de parámetros para el
   * método de similaridad.
   * @param frame Ventana que permite la introducción de los parámetros para el
   * método de tipos mediante similaridad.
   * @param store Almacén donde hay que enviar los mensajes para que puedan ser
   * procesados convenientemente por algún controlador.
   *
   */
  public XfspViewActionListener(XfspView frame, XfspStore store) {
    //llama al constructor de la clase padre
    super(frame);
    //establece la ventana de parámetros para clustering que se escuchará
    this.frame = frame;
    //establece el almacén donde se deben dejar los eventos que se quieran
    //procesar
    this.store = store;
  }

  /**
   * <p> <b>Descripción:</b> envía los parámetros introducidos en la vista
   * escuchada a un almacén para que sea procesado por algún controlador del
   * sistema.
   *
   */
  protected void sendParameters() {
  }

  /**
   * <p> <b>Descripción:</b> acciones específicas para la vista destinada a la
   * introducción de los parámetros del método de similaridad que, en este
   * caso, no hay ninguna.
   * @param e Evento producido por la ventana escuchada.
   *
   */
  protected void specificActions(ActionEvent e) {
    //comando del evento que debe ser atendido
    String command = e.getActionCommand();
    //analiza los distintos comandos que admite el objeto y realiza las
    //acciones oportunas
    if (command.equals("Purge")) {
      actionPurge();
    }
    else if (command.equals("Clustering")) {
      actionClustering();
    }
    else if (command.equals("Similarity")) {
      actionSimilarity();
    }
    else if (command.equals("Pruning")) {
      actionPruning();
    }
    else if (command.equals("Compress")) {
      actionCompact();
    }
    else if (command.equals("Expand")) {
      actionExpand();
    }
    else if (command.equals("TabularSimplification")) {
      actionTabular();
    }
    else if (command.equals("Reload")) {
      actionReload();
    }
    else if (command.equals("Apply")) {
      actionApply();
    }
    else if (command.equals("Save")) {
      actionSave();
    }
    else if (command.equals("Close")) {
      actionClose();
    }
  }

  /**
   * <p> <b>Descripción:</b> devuelve el sistema al último estado almacenado.
   *
   */
  private void actionReload() {
    Object [] args = new Object[1];
    args[0] = frame.getLocation();
    XfspEvent ev = new XfspEvent("Reload", args);
    store.store(ev);
  }

  /**
   * <p> <b>Descripción:</b> aplica los cambios realizados al sistema.
   *
   */
  private void actionApply() {
    XfspEvent ev = new XfspEvent("Apply", null);
    store.store(ev);
  }

  /**
   * <p> <b>Descripción:</b> guarda el sistema en un archivo.
   *
   */
  private void actionSave() {
    frame.setVisible(false);

    XfspEvent ev = new XfspEvent("Save", null);
    store.store(ev);

    if (!frame.getXfuzzy()) {
      System.exit(0);
    }
  }

  /**
   * <p> <b>Descripción:</b> cierra la vista y el sistema.
   *
   */
  private void actionClose() {
    this.frame.close();
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de simplificación de bases de
   * reglas por podado.
   *
   */
  private void actionPurge() {
    Type type = (Type) frame.getTypesList().getSelectedValue();

    if (type != null) {
      Object[] args = new Object[1];

      args[0] = type;
      XfspEvent ev = new XfspEvent("Elementary", args);

      store.store(ev);
    }
    else if (type == null) {
      String[] message = {
          "No type selected",
          "You must select a type to perform this operation"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de simplificación de bases de
   * reglas mediante métodos tabulares.
   *
   */
  private void actionTabular() {
    Rulebase rulebase = (Rulebase) frame.getRuleBasesList().getSelectedValue();
    //comprueba que las reglas de la base de reglas seleccionada sólo contengan
    //operadores IS y AND en su premisa para poder llevar a cabo la
    //simplificación tabular
    if(checkRulebase(rulebase)){
      if (rulebase != null) {
        String[] s = {
            "", "Do you want to maintain extreme membership functions?", ""};
        Boolean maintainExtremeMF = null;
        int value = XDialog.showYNQuestion(this.frame, s);
        if (value == XDialog.YES) {
          maintainExtremeMF = new Boolean(true);
        }
        else if (value == XDialog.NO) {
          maintainExtremeMF = new Boolean(false);
        }
        if (value != XDialog.CANCEL) {
          Object[] args = new Object[2];

          args[0] = rulebase;
          args[1] = maintainExtremeMF;
          XfspEvent ev = new XfspEvent("Tabular", args);

          store.store(ev);
        }
      }
      else if (rulebase == null) {
        String[] message = {
            "No rulebase selected",
            "You must select a rulebase to perform this operation"
        };
        XDialog.showMessage(this.frame, message);
      }
    }
    //si la base de reglas seleccionada no es válida...
    else{
      //...muestra al usuario un mensaje por pantalla y no hace nada
      String[] message = {
          "Wrong rulebase selected",
          "Rules must include only AND conectives and equality relations"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de simplificación de tipos
   * mediante <i>clustering</i>.
   *
   */
  private void actionClustering() {
    Type type = (Type) frame.getTypesList().getSelectedValue();

    if (type != null) {
      boolean differentMemFuncTypes = false;
      LinguisticLabel mflist[] = type.getMembershipFunctions();
      for(int i = 0; i<mflist.length; i++) {
       if(!(mflist[i] instanceof ParamMemFunc)) {
        differentMemFuncTypes = true;
       }
      }
      for(int i=1; i < mflist.length && !differentMemFuncTypes; i++) {
	ParamMemFunc mf0 = (ParamMemFunc) mflist[0];      
	ParamMemFunc mf1 = (ParamMemFunc) mflist[i];      
        if (!mf0.getPackageName().equals(mf1.getPackageName()) ||
            !mf0.getFunctionName().equals(mf1.getFunctionName()) ||
	    mf0.getNumberOfParameters() != mf1.getNumberOfParameters() ) {
          differentMemFuncTypes = true;
        }
      }
      if (!differentMemFuncTypes) {
        XfspClusteringParametersView pv = new XfspClusteringParametersView(type,
            store);

        pv.setParentLocation(this.frame.getLocationOnScreen());
        pv.build();

        pv.setVisible(true);
      }
      else {
        String[] message = {
            "Wrong type selected",
            "You must select a type whose membership functions are of the same kind"
        };
        XDialog.showMessage(this.frame, message);
      }
    }
    else if (type == null) {
      String[] message = {
          "No type selected",
          "You must select a type to perform this operation"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de simplificación de tipos
   * mediante medidas de similitud.
   *
   */
  private void actionSimilarity() {
    Type type = (Type) frame.getTypesList().getSelectedValue();

    if (type != null) {
      XfspSimilarityParametersView pv = new XfspSimilarityParametersView(type,
          store);

      pv.setParentLocation(frame.getLocationOnScreen());
      pv.build();

      pv.setVisible(true);
    }
    else if (type == null) {
      String[] message = {
          "No type selected",
          "You must select a type to perform this operation"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de simplificación de tipos
   * mediante eliminación de tipos no utilizados en ninguna base de reglas.
   *
   */
  private void actionPruning() {
    Rulebase rulebase = (Rulebase) frame.getRuleBasesList().getSelectedValue();

    if (rulebase != null) {
      XfspPruningView pv = new XfspPruningView(rulebase, store);

      pv.setParentLocation(frame.getLocationOnScreen());
      pv.build();

      pv.setVisible(true);
    }
    else if (rulebase == null) {
      String[] message = {
          "No rulebase selected",
          "You must select a rulebase to perform this operation"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de compactación de bases de
   * reglas.
   *
   */
  private void actionCompact() {
    Rulebase rulebase = (Rulebase) frame.getRuleBasesList().getSelectedValue();

    if (rulebase != null) {
      Object[] args = new Object[1];

      args[0] = rulebase;
      XfspEvent ev = new XfspEvent("Union", args);

      store.store(ev);
    }
    else if (rulebase == null) {
      String[] message = {
          "No rulebase selected",
          "You must select a rulebase to perform this operation"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> inicia el proceso de expansión de bases de reglas.
   *
   */
  private void actionExpand() {
    Rulebase rulebase = (Rulebase) frame.getRuleBasesList().getSelectedValue();

    if (rulebase != null) {
      Object[] args = new Object[1];

      args[0] = rulebase;
      XfspEvent ev = new XfspEvent("Separation", args);

      store.store(ev);
    }
    else if (rulebase == null) {
      String[] message = {
          "No rulebase selected",
          "You must select a rulebase to perform this operation"
      };
      XDialog.showMessage(this.frame, message);
    }
  }

  /**
   * <p> <b>Descripción:</b> comprueba que todas las reglas de una base de
   * reglas sólo contengan operadores IS y AND en su premisa.
   * @param rulebase Base de reglas que se debe comprobar.
   * @return Devuelve cierto si las reglas de la base de regla sólo tienen
   * operadores binarios AND y operadores unarios IS en la premisa.
   *
   */
  private boolean checkRulebase(Rulebase rulebase){
    Rule [] rules = rulebase.getRules();
    boolean result = true;
    for(int i=0;i<rules.length && result;i++){
      result = checkRelation(rules[i].getPremise());
    }
    return result;
  }

  /**
   * <p> <b>Descripción:</b> comprueba que en una relación sólo aparezcan
   * operadores binarios AND y operadores unarios IS.
   * @param rel Relación que se debe comprobar.
   * @return Devuelve cierto si la relación contiene operadores binarios AND y
   * operadores unarios IS.
   *
   */
  private boolean checkRelation(Relation rel){
    boolean result = false;
    if(rel.getKind()==Relation.IS){
      result = true;
    }
    else if(rel.getKind()==Relation.AND){
      result = checkRelation(rel.getLeftRelation()) && checkRelation(rel.getRightRelation());
    }
    return result;
  }
}
