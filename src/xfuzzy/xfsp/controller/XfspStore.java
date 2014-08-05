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
 * <p> <b>Descripci�n:</b> Almacena los eventos de entrada provenientes tanto
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
  //lista donde se almacenar�n los eventos de entrada
  private List store;

  /**
   * <p> <b>Descripci�n:</b> crea un nuevo almac�n de eventos que deben ser
   * atendidos por un controlador.
   *
   */
  public XfspStore() {
    //inicializa la lista de eventos de entrada a una lista vac�a
    store = new ArrayList();
  }

  /**
   * <p> <b>Descripci�n:</b> guarda un nuevo evento dentro del almac�n.
   * @param ev Evento que debe ser guardado en el almac�n.
   *
   */
  public synchronized void store(XfspEvent ev) {
    //guardamos el evento de entrada en la lista de eventos
//    store.add(ev);
    store.add(store.size(),ev);
    //indicamos que el estado del objeto observable (almac�n) ha cambiado
    setChanged();
    //noficiamos a todos los observadores del almac�n (controladores) el cambio
    notifyObservers(ev);
  }

  /**
   * <p> <b>Descripci�n:</b> extrae un evento de los que han sido guardados en
   * el almac�n. En caso de que no haya ning�n evento almacenado, devuelve un
   * evento de tipo <b>"null"</b> y con par�metros <b>null</b>.
   * @return Evento extra�do del almac�n.
   *
   */
  public synchronized XfspEvent extract() {
    //evento que ser� devuelto
    XfspEvent event = null;
    //si el almac�n no est� vac�o...
    if (!store.isEmpty()) {
      //...devolvemos el primer elemento de la lista de eventos almacenados
      event = (XfspEvent) store.remove(0);
    }
    //si el almac�n est� vac�o...
    else {
      //...devolvemos un envento de tipo "null" y con par�metros null
      event = new XfspEvent("null", null);
    }
    return event;
  }
}
