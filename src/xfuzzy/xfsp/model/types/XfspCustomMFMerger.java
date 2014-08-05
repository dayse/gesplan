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

package xfuzzy.xfsp.model.types;

import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Universe;

import xfuzzy.xfsp.XfspEvent;

import xfuzzy.xfsp.controller.XfspStore;

/**
 * <p> <b>Descripción:</b> clase de objetos que permiten la unión de dos
 * funciones de pertenencia en una nueva cuyos parámetros han sido
 * especificados por el usuarios.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 3.3
 * @see IXfspMFMerger
 *
 */
public class XfspCustomMFMerger
    implements IXfspMFMerger {

  //nueva función de pertenencia que será igual a la unión de las dos originales
  private volatile ParamMemFunc mergedmf;

  //grado de similaridad que existe entre las funciones de pertenencia a
  //fusionar
  private double similarityDegree;

  //almacén donde se guardan los posibles eventos producidos durante el proceso
  //de fusión de funciones de pertenencia
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> construye un objeto que fusiona funciones de
   * pertenencia.
   *
   */
  public XfspCustomMFMerger() {
    //establece a nulo la función de pertenencia producto de la fusión
    mergedmf = null;
  }

  /**
   * <p> <b>Descripción:</b> une dos funciones de pertenencia en una nueva
   * cuyos parámetros son totalmente especificados por el usuario.
   * @param mf1 Primera función de pertenencia que se debe fusionar.
   * @param mf2 Segunda función de pertenencia.
   * @param universe Universo de las dos funciones de pertenencia originales.
   * @return Nueva función de pertenencia definida por el usuario.
   *
   */
  public ParamMemFunc merge(ParamMemFunc mf1, ParamMemFunc mf2,
                            Universe universe) {
    //this.mf1 = mf1;
    //this.mf2 = mf2;
    //this.universe = universe;

    //argumentos que se le pasarán al controlador para que cree una vista
    //adecuada para la elección de la nueva función de pertenencia
    Object[] args = new Object[4];
    //el primer argumento es la primera de las funciones de pertenencia a unir
    args[0] = mf1;
    //el segundo argumento es la segunda de las funciones de pertenencia a unir
    args[1] = mf2;
    //el tercer argumento es el grado de similaridad que existe entre ambas
    //funciones de pertenencia
    args[2] = new Double(similarityDegree);
    //el cuarto argumento es el propio objeto que permite la fusión de
    //funciones de pertenencia
    args[3] = this;
    //envía un evento al almacén pidiendo a algún controlador que muestre una
    //ventana de selección de funciones de pertenencia por el usuario con los
    //argumentos indicados
    store.store(new XfspEvent("Merge", args));
    try {
      synchronized (this) {
        //mientras el usuario no haya elegido la nueva función de pertenencia
        //sigue a la espera
        while (mergedmf == null) {
          wait();
        }
      }
    }
    catch (InterruptedException e) {
    }
    //si el usuario no ha seleccionado ninguna función para la fusión de las dos
    //originales...
    if(mergedmf.getLabel() == null && mergedmf.getFunctionName() == null){
      //...devuelve null
      mergedmf = null;
    }
    //si el usuario ha introducido la función que resulta de la fusión de las
    //dos similares
    else{
      //...establece el universo de la nueva función de pertenencia...
      //...y el nombre
      mergedmf.set(mf1.getLabel(),universe);
    }
    //devuelve la nueva función de pertenencia
    return mergedmf;
  }

  /**
   * <p> <b>Descripción:</b> establece la función de pertenencia resultado de
   * la unión de las dos originales tal y como la ha definido el usuario.
   * @param mergedmf Función de pertenencia definida por el usuario.
   *
   */
  public synchronized void setMergedMemFunction(ParamMemFunc mergedmf) {
    this.mergedmf = mergedmf;
    notify();
  }

  /**
   * <p> <b>Descripción:</b> devuelve el almacén donde se almacenan los eventos
   * para comunicarse con los controladores.
   * @return Almacén donde el objeto actual almacena los eventos para asi
   * comunicarse con un posible controlador.
   *
   */
  public XfspStore getStore() {
    return this.store;
  }

  /**
   * <p> <b>Descripción:</b> establece el almacén donde se almacenan los
   * eventos para comunicarse con los controladores.
   * @param store Nuevo almacén de eventos que debe emplear el objeto actual
   * para almacenar los eventos.
   *
   */
  public void setStore(XfspStore store) {
    this.store = store;
  }

  /**
   * <p> <b>Descripción:</b> devuelve la grado de similaridad de las funciones
   * que se quieren unir.
   * @return Grado de similaridad de las funcinoes de pertenencia que se
   * quieren unir.
   *
   */
  public double getSimilarityDegree() {
    return this.similarityDegree;
  }

  /**
   * <p> <b>Descripción:</b> establece el grado de similaridad de las funciones
   * de pertenencia que se van a unir.
   * @param similarityDegree Grado de similaridad de las funciones de
   * pertenencia a unir.
   *
   */
  public void setSimilarityDegree(double similarityDegree) {
    this.similarityDegree = similarityDegree;
  }
}
