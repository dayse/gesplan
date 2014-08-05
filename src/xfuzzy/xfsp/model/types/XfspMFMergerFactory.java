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

import xfuzzy.xfsp.controller.XfspStore;

/**
 * <p> <b>Descripción:</b> contruye objetos que fusionan funciones de
 * pertenencia similares.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see XfspModel
 * @see XfspTypeSimplifier
 *
 */
public class XfspMFMergerFactory
    implements IXfspMFMergerFactory {

  //instancia de la fábrica de objetos que fusionan funciones de pertenencia
  //que hace posible que en el sistema haya solamente una instancia como máximo
  //en todo momento
  static private XfspMFMergerFactory instance = null;

  /**
   * <p> <b>Descripción:</b> crea una fábrica de objetos que fusionan funciones
   * de pertenencia.
   *
   */
  private XfspMFMergerFactory() {
  }

  /**
   * <p> <b>Descripción:</b> devuelve una fábrica de objetos que fusionan
   * funciones de pertenencia similares. Dicha fábrica sólo se crea la primera
   * vez que se llama a este método y el resto de las veces se devuelve la
   * misma instancia.
   * @return Devuelve una fábrica de simplificadores de tipos.
   *
   */
  static public XfspMFMergerFactory getInstance() {
    //si no se ha creado todavía ninguna fábrica...
    if (instance == null) {
      //...crea una nueva
      instance = new XfspMFMergerFactory();
    }
    //devuelve la fábrica que hay creada
    return instance;
  }

  /**
   * <p> <b>Descripción:</b> devuelve un objeto que fusiona funciones de
   * pertenencia similares.
   * @param mf1Name Nombre de la primera de las funciones de pertenencia.
   * @param mf2Name Nombre de la segunda de las funciones de pertenencia.
   * @param similarityDegree Grado de similaridad entre las dos funciones.
   * @param store Almacén donde se almacenan los eventos.
   * @return Objeto que fusiona funciones de pertenencia similares.
   *
   */
  public IXfspMFMerger create(String mf1Name, String mf2Name,
                              double similarityDegree, XfspStore store) {
    IXfspMFMerger merger = null;

    if (mf1Name.equals("trapezoid") && mf2Name.equals("trapezoid")) {
      merger = new XfspTrapezoidMFMerger();
    }
    else if (mf1Name.equals("triangle") && mf2Name.equals("triangle")) {
      merger = new XfspTriangleMFMerger();
    }
    else if (mf1Name.equals("isosceles") && mf2Name.equals("isosceles")){
      merger = new XfspIsoscelesMFMerger();
    }
    else if  (mf1Name.equals("rectangle") && mf2Name.equals("rectangle")) {
      merger = new XfspRectangleMFMerger();
    }
    else if (mf1Name.equals("bell") && mf2Name.equals("bell")) {
      merger = new XfspBellMFMerger();
    }
    else {
      XfspCustomMFMerger customMerger = new XfspCustomMFMerger();
      customMerger.setStore(store);
      customMerger.setSimilarityDegree(similarityDegree);
      merger = customMerger;
    }
    return merger;
  }
}
