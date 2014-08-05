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
 * <p> <b>Descripci�n:</b> clase de objetos que permiten la uni�n de dos
 * funciones de pertenencia en una nueva cuyos par�metros han sido
 * especificados por el usuarios.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 3.3
 * @see IXfspMFMerger
 *
 */
public class XfspCustomMFMerger
    implements IXfspMFMerger {

  //nueva funci�n de pertenencia que ser� igual a la uni�n de las dos originales
  private volatile ParamMemFunc mergedmf;

  //grado de similaridad que existe entre las funciones de pertenencia a
  //fusionar
  private double similarityDegree;

  //almac�n donde se guardan los posibles eventos producidos durante el proceso
  //de fusi�n de funciones de pertenencia
  private XfspStore store;

  /**
   * <p> <b>Descripci�n:</b> construye un objeto que fusiona funciones de
   * pertenencia.
   *
   */
  public XfspCustomMFMerger() {
    //establece a nulo la funci�n de pertenencia producto de la fusi�n
    mergedmf = null;
  }

  /**
   * <p> <b>Descripci�n:</b> une dos funciones de pertenencia en una nueva
   * cuyos par�metros son totalmente especificados por el usuario.
   * @param mf1 Primera funci�n de pertenencia que se debe fusionar.
   * @param mf2 Segunda funci�n de pertenencia.
   * @param universe Universo de las dos funciones de pertenencia originales.
   * @return Nueva funci�n de pertenencia definida por el usuario.
   *
   */
  public ParamMemFunc merge(ParamMemFunc mf1, ParamMemFunc mf2,
                            Universe universe) {
    //this.mf1 = mf1;
    //this.mf2 = mf2;
    //this.universe = universe;

    //argumentos que se le pasar�n al controlador para que cree una vista
    //adecuada para la elecci�n de la nueva funci�n de pertenencia
    Object[] args = new Object[4];
    //el primer argumento es la primera de las funciones de pertenencia a unir
    args[0] = mf1;
    //el segundo argumento es la segunda de las funciones de pertenencia a unir
    args[1] = mf2;
    //el tercer argumento es el grado de similaridad que existe entre ambas
    //funciones de pertenencia
    args[2] = new Double(similarityDegree);
    //el cuarto argumento es el propio objeto que permite la fusi�n de
    //funciones de pertenencia
    args[3] = this;
    //env�a un evento al almac�n pidiendo a alg�n controlador que muestre una
    //ventana de selecci�n de funciones de pertenencia por el usuario con los
    //argumentos indicados
    store.store(new XfspEvent("Merge", args));
    try {
      synchronized (this) {
        //mientras el usuario no haya elegido la nueva funci�n de pertenencia
        //sigue a la espera
        while (mergedmf == null) {
          wait();
        }
      }
    }
    catch (InterruptedException e) {
    }
    //si el usuario no ha seleccionado ninguna funci�n para la fusi�n de las dos
    //originales...
    if(mergedmf.getLabel() == null && mergedmf.getFunctionName() == null){
      //...devuelve null
      mergedmf = null;
    }
    //si el usuario ha introducido la funci�n que resulta de la fusi�n de las
    //dos similares
    else{
      //...establece el universo de la nueva funci�n de pertenencia...
      //...y el nombre
      mergedmf.set(mf1.getLabel(),universe);
    }
    //devuelve la nueva funci�n de pertenencia
    return mergedmf;
  }

  /**
   * <p> <b>Descripci�n:</b> establece la funci�n de pertenencia resultado de
   * la uni�n de las dos originales tal y como la ha definido el usuario.
   * @param mergedmf Funci�n de pertenencia definida por el usuario.
   *
   */
  public synchronized void setMergedMemFunction(ParamMemFunc mergedmf) {
    this.mergedmf = mergedmf;
    notify();
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el almac�n donde se almacenan los eventos
   * para comunicarse con los controladores.
   * @return Almac�n donde el objeto actual almacena los eventos para asi
   * comunicarse con un posible controlador.
   *
   */
  public XfspStore getStore() {
    return this.store;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el almac�n donde se almacenan los
   * eventos para comunicarse con los controladores.
   * @param store Nuevo almac�n de eventos que debe emplear el objeto actual
   * para almacenar los eventos.
   *
   */
  public void setStore(XfspStore store) {
    this.store = store;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la grado de similaridad de las funciones
   * que se quieren unir.
   * @return Grado de similaridad de las funcinoes de pertenencia que se
   * quieren unir.
   *
   */
  public double getSimilarityDegree() {
    return this.similarityDegree;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el grado de similaridad de las funciones
   * de pertenencia que se van a unir.
   * @param similarityDegree Grado de similaridad de las funciones de
   * pertenencia a unir.
   *
   */
  public void setSimilarityDegree(double similarityDegree) {
    this.similarityDegree = similarityDegree;
  }
}
