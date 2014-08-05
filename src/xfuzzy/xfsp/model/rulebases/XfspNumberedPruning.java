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

import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;

/**
 * <p> <b>Descripción:</b> clase de objetos que permiten la simplificación de
 * bases de reglas mediante la conservación o eliminación de un número fijo de
 * reglas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see XfspModel
 * @see XfspPrunning
 * @see XfspBestPrunning
 * @see XfspWorstPrunning
 *
 */
public abstract class XfspNumberedPruning
    extends XfspPruneSimplifier {

  /**
   * <p> <b>Descripción:</b> crea un simplificador de bases de reglas de
   * sistemas difusos que conserva o elimina un número fijo de reglas.
   * @param pattern Patrón de datos para las entradas de las reglas.
   *
   */
  public XfspNumberedPruning(double[][] pattern) {
    //llama al constructor de la clase padre
    super(pattern);
  }

  /**
   * <p> <b>Descripción:</b> hace el podado de una base de reglas ya sea
   * eliminando o ya sea conservando un número fijo de reglas de la misma.
   * @param rulebase Base de reglas que se quiere podar.
   *
   */
  protected void prune(Rulebase rulebase){    //, Specification spec) {
    //obtiene las reglas de la base de reglas a simplificar
    Rule[] rules = rulebase.getRules();
    //implementa el algoritmo de ordenación de la burbuja para ordenar las
    //reglas de la base de regla según su grado de activación
    //para cada pareja de reglas distintas de la base de reglas...
    for (int i = 0; i < (rules.length - 1); i++) {
      for (int j = i + 1; j < rules.length; j++) {
        //si el grado de activación máximo de la regla más a la izquierda es
        //menor que el de la derecha...
        if (rules[i].getMaxActivation() < rules[j].getMaxActivation()) {
          //...intercambia sus posiciones
          Rule aux = rules[i];
          rules[i] = rules[j];
          rules[j] = aux;
        }
      }
    }
    //elimna las reglas con el grado de activación menor
    for (int i = firstRuleIndex(rulebase); i < rules.length; i++) {
      rulebase.remove(rules[i]);
    }
  }

  /**
   * <p> <b>Descripción:</b> devuelve el índice que ocuparía en una lista de
   * reglas ordenadas por su grado de activación la última regla que debe ser
   * conservada tras el podado.
   * @param rulebase Base de reglas que va a ser podada.
   *
   */
  protected abstract int firstRuleIndex(Rulebase rb);
}
