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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import xfuzzy.xfsp.XfspEvent;

/**
 * <p> <b>Descripción:</b> Almacena los eventos de entrada provenientes tanto
 * del modelo (XfspModel) como de las vistas (<b>XfspView</b>) hasta que el
 * controlador (<b>XfspController</b>) los traslade a su destino.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see XfspController
 *
 */
public class XfspStore
    extends Observable {
  //lista donde se almacenarán los eventos de entrada
  private List store;

  /**
   * <p> <b>Descripción:</b> crea un nuevo almacén de eventos que deben ser
   * atendidos por un controlador.
   *
   */
  public XfspStore() {
    //inicializa la lista de eventos de entrada a una lista vacía
    store = new ArrayList();
  }

  /**
   * <p> <b>Descripción:</b> guarda un nuevo evento dentro del almacén.
   * @param ev Evento que debe ser guardado en el almacén.
   *
   */
  public synchronized void store(XfspEvent ev) {
    //guardamos el evento de entrada en la lista de eventos
//    store.add(ev);
    store.add(store.size(),ev);
    //indicamos que el estado del objeto observable (almacén) ha cambiado
    setChanged();
    //noficiamos a todos los observadores del almacén (controladores) el cambio
    notifyObservers(ev);
  }

  /**
   * <p> <b>Descripción:</b> extrae un evento de los que han sido guardados en
   * el almacén. En caso de que no haya ningún evento almacenado, devuelve un
   * evento de tipo <b>"null"</b> y con parámetros <b>null</b>.
   * @return Evento extraído del almacén.
   *
   */
  public synchronized XfspEvent extract() {
    //evento que será devuelto
    XfspEvent event = null;
    //si el almacén no está vacío...
    if (!store.isEmpty()) {
      //...devolvemos el primer elemento de la lista de eventos almacenados
      event = (XfspEvent) store.remove(0);
    }
    //si el almacén está vacío...
    else {
      //...devolvemos un envento de tipo "null" y con parámetros null
      event = new XfspEvent("null", null);
    }
    return event;
  }
}
