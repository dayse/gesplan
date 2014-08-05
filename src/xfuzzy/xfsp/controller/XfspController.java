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

package xfuzzy.xfsp.controller;

import java.awt.Point;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Type;
import xfuzzy.lang.Rulebase;

import xfuzzy.xfsp.XfspEvent;

import xfuzzy.xfsp.model.XfspModel;
import xfuzzy.xfsp.model.types.XfspCustomMFMerger;

import xfuzzy.xfsp.view.XfspView;
import xfuzzy.xfsp.view.XfspFunctionMergeView;

/**
 * <p> <b>Descripción:</b> Clase que recibe eventos y coordina su destino
 * (vista, <b>XfspView</b>, y/o modelo, <b>XfspModel</b>).
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see XfspModel
 * @see XfspView
 *
 */
public class XfspController
    implements Observer, Runnable {

  //modelo al que está ligado el controlador
  private XfspModel model;

  //almacén donde se guardan los eventos de entrada hasta que son procesados
  //por el modelo
  private XfspStore store;

  //conjunto de vistas gestionadas por el controlador
  private List view;

  //evento de entrada que le llega al controlador
  private XfspEvent newEvent;

  private XfspCustomMFMerger customMerger;

  /**
   * <p> <b>Descripción:</b> crea un nuevo controlador que no gestiona ninguna
   * vista ni tiene asignado ningún almacén donde guardar los elementos de
   * entrada ni ha sido registrado por ningún modelo.
   *
   */
  public XfspController() {
    //hace que el conjunto de vistas gestionadas sea el vacío
    view = new ArrayList();
    //indica que no se está procesando ninguna petición de simplificación
  }

  /**
   * <p> <b>Descripción:</b> inicializa el controlador indicándolo el modelo
   * que lo ha registrado y el almacén donde debe buscar los eventos de
   * entrada.
   * @param model Modelo que ha registrado el controlador.
   * @param store Almacén donde se guardarán los eventos de entrada antes de
   * ser procesados por el controlador.
   *
   */
  public void init(XfspModel model, XfspStore store) {
    //indicamos el modelo que va a registrar al controlador actual
    this.model = model;
    //indicamos el almacén donde se guardarán los eventos de entrada
    this.store = store;
    //hacemos que el modelo registre al controlador
    model.registerController(this);
  }

  /**
   * <p> <b>Descripción:</b> indica al controlador que debe registrar y
   * gestionar una nueva vista. Si la vista indicada ya estaba siendo
   * gestionada por el controlador, no hace nada.
   * @param view Vista que pasará a formar parte de las gestionadas por el
   * controlador.
   *
   */
  public void registerView(XfspView view) {
    //inicializamos a falso un booleano que nos indique si hemos encontrado la
    //vista que queremos añadir entre las ya gestionadas por el controlador
    boolean found = false;
    //índice que nos servirá para recorrer el conjunto de vistas gestionadas por
    //el controlador
    int i = 0;
    //mientras nos queden vistas por examinar y no hayamos encontrado la vista
    //que queremos añadir entre las ya gestionadas por el controaldor...
    while ( (i < this.view.size()) && (found == false)) {
      //...examinamos si la vista actual coincide con la que queremos añadir...
      if (view == this.view.get(i)) {
        //...si es así, dejamos de buscar
        found = true;
      }
      else {
        //...si no, seguimos buscando
        i++;
      }
    }
    //si la vista que queremos añadir no esta siendo gestionada por el
    //controlador...
    if (found == false) {
      //...la añadimos a la lista de vistas que tiene que gestionar el
      //controlador
      this.view.add(view);
    }
  }

  /**
   * <p> <b>Descripción:</b> hace que el controlador deje de gestionar la vista
   * indicada. Caso de que dicha vista no estuviera siendo gestionada por el
   * controlador, este método no hace nada.
   * @param view Vista que debe dejar de ser gestionada por el controlador.
   *
   */
  public void deleteView(XfspView view) {
    //buscamos entre todas las vistas gestionadas por el controlador
    for (int i = 0; i < this.view.size(); i++) {
      //si la vista actual coincide con la indicada...
      if (view == this.view.get(i)) {
        //...la eliminamos de las listas gestionadas por el controlador
        view.remove(i);
      }
    }
  }

  /**
   * <p> <b>Descripción:</b> acción realizada por el controlador (observador)
   * cuando el almacén (observable) recibe un nuevo evento de entrada.
   * @param obs Almacén donde se almacenan los eventos de entrada y que está
   * siendo observado por el controlador.
   * @param ev Evento de entrada que ha sido guardado en el almacén.
   *
   */
  public void run() {
    //el controlador analiza de qué tipo de evento se trata
    //eventos que provienen de las vistas:
    //si el evento es una petición de volver a la especificación anterior...
    if (newEvent.getType().equals("Reload")) {
      //...llamamos al método "reload" del modelo
      model.reload((Point)newEvent.getArgs()[0]);
    }
    //si el evento es una petición de aplicación de los cambios realizados en
    //la especificación...
    else if (newEvent.getType().equals("Apply")) {
      //...llamamos al método "apply" del modelo
      model.apply();
    }
    //si el evento es una petición de guardar los cambios realizados a la
    //especificación...
    else if (newEvent.getType().equals("Save")) {
      //...llamamos al metodo "save" del modelo
      model.save();
    }
    //si el evento es una petición de terminar la simplificación de la
    //especificación...
    else if (newEvent.getType().equals("Close")) {
      //...llamamos al método "close" del modelo
      model.close();
      //...y cierra todas las vistas
      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setVisible(false);
      }
    }
    //si el evento es una petición de simplificación de tipos por eliminación
    //de aquellas funciones de pertenencia que no se usan en ninguna regla...
    else if (newEvent.getType().equals("Elementary")) {

      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setEnabled(false);
      }
      //...hacemos que el modelo simplifique el tipo indicado mediante la
      //eliminación de las funciones de pertenencia del tipo que no se usen en
      //ninguna regla
      model.simplifyType( (Type) newEvent.getArgs()[0], "Elementary",
                         newEvent.getArgs());
    }
    //si se solicita la simplificación de tipos de la especificación mediante
    //clustering...
    else if (newEvent.getType().equals("Clustering")) {


      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setEnabled(false);
      }
      //...hacemos que el modelo simplifique el tipo indicado mediante
      //clustering con los parámetros que haya introducido el usuario
      model.simplifyType( (Type) newEvent.getArgs()[0], "Clustering",
                         newEvent.getArgs());
    }
    //si se solicita la simplificación de tipos de la especificación mediante
    //el estudio de la similaridad de funciones de pertenencia...
    else if (newEvent.getType().equals("Similarity")) {

      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setEnabled(false);
      }
      //...hacemos que el modelo simplifique el tipo indicada con técnicas de
      //estudio de similaridad y utilizando los parámetros que haya introducido
      //el usuario


      Object[] args = new Object[newEvent.getArgs().length];
      for (int i = 1; i < newEvent.getArgs().length; i++) {
        args[i - 1] = newEvent.getArgs()[i];
      }
      args[args.length - 1] = store;

      model.simplifyType( (Type) newEvent.getArgs()[0], "Similarity",
                         args); //newEvent.getArgs());
    }
    //si se solicita la simplificación de bases de reglas mediante podado...
    else if (newEvent.getType().equals("Prunning")) {

      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setEnabled(false);
      }
      //...hacemos que el modelo simplifique la base de reglas seleccionada
      //mediante podado según los grados de activación
      model.simplifyRulebase( (Rulebase) newEvent.getArgs()[0], "Prunning",
                             newEvent.getArgs());
    }
    //si se solicita la unión de las reglas que tengan en mismo consecuente...
    else if (newEvent.getType().equals("Union")) {

      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setEnabled(false);
      }
      //...hacemos que el modelo una las reglas con iguales consecuentes
      //mediante la disyunción de las premisas de dichas reglas
      model.simplifyRulebase( (Rulebase) newEvent.getArgs()[0], "Union",
                             newEvent.getArgs());
    }
    //se se solicita la separación de reglas cuya premisa es un conjunto de
    //AND...
    else if (newEvent.getType().equals("Separation")) {
      for(int i=0;i<view.size();i++){
        ( (XfspView) view.get(i)).setEnabled(false);
      }




      //...hacemos que el modelo sustituya las reglas con AND en su premisa por
      //tantas reglas como  operandos tenga la premisa
      model.simplifyRulebase( (Rulebase) newEvent.getArgs()[0], "Separation",
                             newEvent.getArgs());
    }
    else if (newEvent.getType().equals("Tabular")) {
      for(int i=0;i<view.size();i++){
       ( (XfspView) view.get(i)).setEnabled(false);
     }
      model.simplifyRulebase( (Rulebase) newEvent.getArgs()[0], "Tabular", newEvent.getArgs());
    }
    //si se solicita la continuación del proceso de mezcla de funciones de
    //pertenencia...
    else if (newEvent.getType().equals("RestartMerging")) {
      //...hacemos que el modelo continúe el proceso de mezcla de funciones de
      //pertenencia durante la simplificación de tipos por similaridad con los
      //parámetros que haya indicado el usuario
      customMerger.setMergedMemFunction((ParamMemFunc) newEvent.getArgs()[0]);
    }
    //eventos que provienen del modelo que ha registrado al controlador:
    //si el modelo solicita que se actualice el gráfico de representación de
    //las funciones de pertenencia de los tipos...
    else if (newEvent.getType().equals("Refresh")) {

      Object [] args = newEvent.getArgs();


      //...indicamos a todas las vistas gestionadas por el modelo que deben
      //actualizar el gráfico de simplificación de tipos llamando al metodo
      //"refresh" de las vistas asi como el campo de texto donde se muestran
      //las bases de reglas
      try {



        for (int i = 0; i < view.size(); i++) {
        ( (XfspView) view.get(i)).setTypes( (Type[]) args[0]);
        ( (XfspView) view.get(i)).setRuleBases( (Rulebase[]) args[1]);
      }




        //para todas las vistas registradas por el controlador...
        for (int i = 0; i < view.size(); i++) {
          final XfspView currentView = (XfspView) view.get(i);
          Runnable refreshRun = new Runnable() {
            public void run() {
              currentView.refresh();
            }
          };
          SwingUtilities.invokeAndWait(refreshRun);
        }

        //recoge el mensaje que se debe mostrar al refrescar
        String [] msg = (String []) newEvent.getArgs()[2];

        for(int i=0;i<view.size();i++){
          ((XfspView)  view.get(i)).showMessage(msg);
        }

        for(int i=0;i<view.size();i++){
       ( (XfspView) view.get(i)).setEnabled(true);
       ( (XfspView) view.get(i)).toFront();
     }


      }
      catch (InterruptedException e) {
      }
      catch (InvocationTargetException e) {
      }
    }
    //esta parte no está todo clara y debe ser revisada
    else if (newEvent.getType().equals("Rebuild")) {
      Object [] args = newEvent.getArgs();
      for (int i = 0; i < view.size(); i++) {
        ( (XfspView) view.get(i)).setTypes( (Type[]) args[0]);
        ( (XfspView) view.get(i)).setRuleBases( (Rulebase[]) args[1]);
      }
          for (int i = 0; i < view.size(); i++) {
            XfspView currentView = new XfspView(this.store);
            currentView.xfuzzy(true);
           currentView.setParentLocation(((XfspView) view.get(i)).getParentLocation());
           currentView.setLocation(((XfspView) view.get(i)).getLocation());
           currentView.setTypes(((XfspView) view.get(i)).getTypes());
           currentView.setRuleBases(((XfspView) view.get(i)).getRuleBases());
           ((XfspView)view.remove(i)).close();
           view.add(i,currentView);
           currentView.build();
           currentView.setVisible(true);
      }

      for(int i=0;i<view.size();i++){
       ( (XfspView) view.get(i)).setEnabled(true);
       ( (XfspView) view.get(i)).toFront();
     }



    }
    //si el modelo solicita al usuario la función de pertenencia que debe
    //sustituir a dos funciones similares...
    else if (newEvent.getType().equals("Merge")) {
      //...crea la ventana para que el usuario elija la función que debe
      //reemplazar a las dos funciones similares
      this.customMerger = (XfspCustomMFMerger) newEvent.getArgs()[3];


      for (int i = 0; i < view.size(); i++) {
        XfspFunctionMergeView mergeView = new XfspFunctionMergeView( (
            ParamMemFunc) newEvent.getArgs()[0],
            (ParamMemFunc) newEvent.getArgs()[1], this.store);
        mergeView.setSimilarityDegree( ( (Double) newEvent.getArgs()[2]).
                                      doubleValue());
        mergeView.setParentLocation( ( (XfspView) view.get(0)).getLocation());
        mergeView.build();
        mergeView.setVisible(true);
      }
    }
  }

  /**
     * <p> <b>Descripción:</b> hace que el controlador deje de gestionar la vista
     * indicada. Caso de que dicha vista no estuviera siendo gestionada por el
     * controlador, este método no hace nada.
     * @param view Vista que debe dejar de ser gestionada por el controlador.
     *
     */
  public void update(Observable obs, Object ev) {
      //convertimos el evento de entrada a un objeto de tipo XfspEvent
      this.newEvent = (XfspEvent) ev;
      //convertimos el observable en un objeto de tipo XfspStore
      this.store = (XfspStore) obs;
      //creamos un nuevo hilo para el proceso...
      Thread newThread = new Thread(this);
      //...y lo lanzamos
      newThread.start();
  }
}
