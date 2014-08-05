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

package xfuzzy.xfsp;

import java.awt.Point;

import xfuzzy.Xfuzzy;

import xfuzzy.lang.Specification;

import xfuzzy.xfsp.controller.XfspController;
import xfuzzy.xfsp.controller.XfspStore;
import xfuzzy.xfsp.model.XfspModel;
import xfuzzy.xfsp.view.XfspView;

/**
 * <p> <b>Descripción:</b> Clase que permite la simplificación de sistemas
 * difusos.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.3
 * @see XfspEvent
 * @see XfspController
 * @see XfspModel
 * @see XfspStore
 * @see XfspView
 *
 */
public class Xfsp {
  private XfspModel model;
  private XfspController controller;
  private XfspView view;
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea una instancia del sistema de simplificación
   * de sistemas difusos.
   * @param xfuzzy Sistema principal desde donde se hace la llamada al sistema
   * de simplificación.
   * @param spec Especificación del sistema difuso a simplificar.
   *
   */
  public Xfsp(Xfuzzy xfuzzy, Specification spec) {

    Specification workingcopy;

    //parser = null;
    this.store = new XfspStore();
    this.model = new XfspModel(xfuzzy, spec, store);

    workingcopy = model.getWorkingcopy();

    this.controller = new XfspController();
    this.controller.init(model, store);

    // nuevo!!
    this.store.addObserver(controller);

    //TODO - quitar estos comentarios

    this.view = new XfspView(store);
    this.view.xfuzzy(true);
    this.view.setParentLocation(xfuzzy.frame.getLocationOnScreen());
    this.view.setTypes(workingcopy.getTypes()); //spec.getTypes());
    this.view.setRuleBases(workingcopy.getRulebases()); //spec.getRulebases());
    this.view.setName(spec.getName());
    this.view.build();
    this.controller.registerView(view);
  }



  /**
   * <p> <b>Descripción:</b> crea una instancia del sistema de simplificación
   * de sistemas difusos indicando la localización que debe ocupar la vista en
   * la pantalla.
   * @param xfuzzy Sistema principal desde donde se hace la llamada al sistema
   * de simplificación.
   * @param spec Especificación del sistema difuso a simplificar.
   * @param location Localización que ocupará en la pantalla la vista de la
   * instancia creada.
   *
   */
  public Xfsp(Xfuzzy xfuzzy, Specification spec, Point location) {

    Specification workingcopy;

    this.store = new XfspStore();
    this.model = new XfspModel(xfuzzy, spec, store);

    workingcopy = model.getWorkingcopy();

    this.controller = new XfspController();
    this.controller.init(model, store);

    this.store.addObserver(controller);

    this.view = new XfspView(store);
    this.view.xfuzzy(true);
    this.view.setLocation(location);
    this.view.setTypes(workingcopy.getTypes()); //spec.getTypes());
    this.view.setRuleBases(workingcopy.getRulebases()); //spec.getRulebases());
    this.view.setName(spec.getName());
    this.view.build();
    this.controller.registerView(view);
  }

  /**
   * <p> <b>Descripción:</b> muestra en pantalla la vista del sistema de
   * simplificación de sistemas difusos.
   *
   */
  public void show() {
    view.setVisible(true);
  }
}
