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
import xfuzzy.lang.Specification;
import xfuzzy.lang.SystemModule;

/**
 * <p> <b>Descripción:</b> clase de objetos que permiten el podado de bases de
 * reglas en función del grado de activación máximo de las reglas que la
 * componen.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see IXfspRulebaseSimplifier
 * @see XfspModel
 * @see XfspPrunning
 *
 */
public abstract class XfspPruneSimplifier
    implements IXfspRulebaseSimplifier {

  //patrón de entrada que permite evaluar el grado de activación de la base de
  //reglas
  protected double[][] pattern;

  /**
   * <p> <b>Descripción:</b> crea un simplificador de bases de reglas de
   * sistemas difusos por podado de las reglas.
   * @param pattern Patrón de datos para las entradas de las reglas.
   *
   */
  public XfspPruneSimplifier(double[][] pattern) {
    //establece los datos que se utilizarán para activar las entradas de la
    //base de reglas a podar
    this.pattern = pattern;
  }

  /**
   * <p> <b>Descripción:</b> simplifica una base de reglas de un sistem difuso
   * cuya especificación también es proporcionada por podado de algunas de sus
   * reglas.
   * @param rulebase Base de reglas que se quiere simplificar.
   * @param spec Especificación del sistema cuya base de reglas se va a
   * simplificar.
   *
   */
  public void simplify(Rulebase rulebase, Specification spec) {
    //calcula el grado de activación máximo de las reglas del sistema
    computeMaxActivation(spec);
    //poda la base de reglas elegida
    prune(rulebase);
  }

  /**
   * <p> <b>Descripción:</b> hace el podado de una base de reglas.
   * @param rulebase Base de reglas que se quiere podar.
   *
   */
  protected abstract void prune(Rulebase rulebase);

  /**
   * <p> <b>Descripción:</b> calcula el grado de activación máximo de las
   * reglas de un sistema difuso según un patrón de datos para las entradas.
   * @param spec Especificación del sistema cuyas entradas se van a estimular.
   *
   */
  protected void computeMaxActivation(Specification spec) {
    SystemModule system = spec.getSystemModule();
    //obtiene las bases de reglas del sistema
    Rulebase[] rulebases = spec.getRulebases();
    //para cada base de reglas del sistema...
    for (int i = 0; i < rulebases.length; i++) {
      //...pone a cero el grado de activación máximo de sus reglas
      rulebases[i].resetMaxActivation();
    }
    //para cada uno de los datos que servirán de entrada...
    for (int p = 0; p < pattern.length; p++) {
      //...calcula el grado de activación máximo de las reglas del sistema
      system.fuzzyInference(pattern[p]);
    }
  }
}
