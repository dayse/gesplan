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
 * <p> <b>Descripci�n:</b> clase de objetos que permiten la simplificaci�n de
 * bases de reglas mediante la conservaci�n de �nicamente aquellas reglas cuyo
 * grado de activaci�n superen un cierto umbral.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.3
 * @see XfspModel
 * @see XfspPrunning
 *
 */
public class XfspThresholdPruneSimplifier
    extends XfspPruneSimplifier {

  //umbral que debe superar el grado de activaci�n de aquellas reglas que ser�n
  //conservadas tras el podado
  private double threshold;

  /**
   * <p> <b>Descripci�n:</b> crea un simplificador de bases de reglas de
   * sistemas difusos que conserva �nicamente aquellas reglas cuyo grado de
   * activaci�n m�xima supere un umbral.
   * @param pattern Patr�n de datos para las entradas de las reglas.
   * @param threshold Umbral que debe superar el grado de activaci�n m�ximo de
   * una regla de la base de reglas para ser conservada.
   *
   */
  public XfspThresholdPruneSimplifier(double[][] pattern, double threshold) {
    //llama al constructor de la superclase
    super(pattern);
    //establece el umbral que debe superar el grado de activaci�n m�ximo de una
    //regla para permanecer en la base de reglas que se podar�
    this.threshold = threshold;
  }

  /**
   * <p> <b>Descripci�n:</b> hace el podado de una base de reglas por
   * eliminaci�n de aquellas reglas cuya grado de activaci�n m�ximo no supere
   * un determinado umbral.
   * @param rulebase Base de reglas que se quiere podar.
   *
   */
  protected void prune(Rulebase rulebase) { //, Specification spec) {
    //obtiene las reglas de la base de reglas a podar
    Rule[] rules = rulebase.getRules();
    //para cada una de las reglas de la base de reglas...
    for (int i = 0; i < rules.length; i++) {
      //...si su grado de activaci�n m�ximo no supera el umbral fijado...
      if (rules[i].getMaxActivation() < threshold) {
        //...la regla es eliminada de la base de reglas
        rulebase.remove(rules[i]);
      }
    }
  }
}
