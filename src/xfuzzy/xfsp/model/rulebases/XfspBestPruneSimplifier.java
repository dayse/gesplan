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
 * <p> <b>Descripci�n:</b> clase de objetos que permiten la simplificaci�n de
 * bases de reglas mediante la reducci�n de �stas a las n mejores reglas, donde
 * se entiende por mejor aquella regla que tiene un mayor grado de activaci�n.
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

  //n�mero de reglas que deber� conservar la base de reglas tras la
  //simplificaci�n
  private int best;

  /**
   * <p> <b>Descripci�n:</b> crea un simplificador de bases de reglas de
   * sistemas difusos que conserva las reglas con m�s alto grado de activaci�n
   * y elimina el resto.
   * @param pattern Patr�n de datos para las entradas de las reglas.
   * @param best N�mero de reglas que deber� conservar la base de reglas tras
   * su simplificaci�n.
   *
   */
  public XfspBestPruneSimplifier(double[][] pattern, int best) {
    //llama al constructor de la clase padre
    super(pattern);
    //almacena el n�mero de funciones que debe conservar la base de reglas tras
    //la simplificaci�n
    this.best = best;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el �ndice que ocupa la �ltima regla
   * seleccionada por el simplificador dentro de una lista de reglas
   * ordenadas por su grado de activaci�n.
   * @return �ndice ocupado por la �ltima de las reglas de la base de reglas
   * que se conservar� tras la simplificaci�n si se ordenaran las reglas
   * seg�n su grado de activaci�n.
   *
   */
  protected int firstRuleIndex(Rulebase rb) {
    return best;
  }
}
