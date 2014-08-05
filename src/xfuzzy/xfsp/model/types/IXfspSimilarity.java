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

/**
 * <p> <b>Descripción:</b> interfaz de los objetos que permiten calcular los
 * grados de similaridad de las funciones de pertenencia de un tipo.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.3
 * @see XfspModel
 * @see XfspTypeSimplifier
 *
 */
public interface IXfspSimilarity {

  /**
   * <p> <b>Descripción:</b> establece el universo del tipo para el que se
   * quiere calcular el grado de similaridad de sus funciones de pertenencia.
   * @param u Universo del tipo para el que se calcularán los grados de
   * similaridad de sus funciones de pertenencia.
   *
   */
  public void setUniverse(Universe u);

  /**
   * <p> <b>Descripción:</b> permite calcular el grado de similaridad entre dos
   * funciones de pertenencia de un tipo cuyo universo se ha establecido antes.
   * @param a Función de pertenencia de un tipo.
   * @param b Función de pertenencia del mismo tipo que la anterior.
   * @return Devuelve el grado de similaridad de las dos funciones de
   * pertenencia.
   *
   */
  public double similarity(ParamMemFunc a, ParamMemFunc b);
}
