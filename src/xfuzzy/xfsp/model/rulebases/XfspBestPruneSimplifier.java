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

package xfuzzy.xfsp.model.rulebases;

import xfuzzy.lang.Rulebase;

/**
 * <p> <b>Descripción:</b> clase de objetos que permiten la simplificación de
 * bases de reglas mediante la reducción de éstas a las n mejores reglas, donde
 * se entiende por mejor aquella regla que tiene un mayor grado de activación.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see XfspModel
 * @see XfspPrunning
 * @see XfspNumberedPrunning
 * @see XfspWorstPrunning
 *
 */
public class XfspBestPruneSimplifier
    extends XfspNumberedPruning {

  //número de reglas que deberá conservar la base de reglas tras la
  //simplificación
  private int best;

  /**
   * <p> <b>Descripción:</b> crea un simplificador de bases de reglas de
   * sistemas difusos que conserva las reglas con más alto grado de activación
   * y elimina el resto.
   * @param pattern Patrón de datos para las entradas de las reglas.
   * @param best Número de reglas que deberá conservar la base de reglas tras
   * su simplificación.
   *
   */
  public XfspBestPruneSimplifier(double[][] pattern, int best) {
    //llama al constructor de la clase padre
    super(pattern);
    //almacena el número de funciones que debe conservar la base de reglas tras
    //la simplificación
    this.best = best;
  }

  /**
   * <p> <b>Descripción:</b> devuelve el índice que ocupa la última regla
   * seleccionada por el simplificador dentro de una lista de reglas
   * ordenadas por su grado de activación.
   * @return Índice ocupado por la última de las reglas de la base de reglas
   * que se conservará tras la simplificación si se ordenaran las reglas
   * según su grado de activación.
   *
   */
  protected int firstRuleIndex(Rulebase rb) {
    return best;
  }
}
