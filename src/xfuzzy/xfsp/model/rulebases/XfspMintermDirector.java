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

import java.util.Map;

import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;

/**
 * <p> <b>Descripción:</b> construye un conjunto mintérminos a partir de una
 * base de reglas que la representa a fin de poder ser simplificada por un
 * método tabular.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.1
 * @see XfspModel
 * @see XfspTabularSimplifier
 * @see XfspMintermBuilder
 *
 */
public class XfspMintermDirector {

  /**
   * <p> <b>Descripción:</b> construye un map cuyas claves son las conclusiones
   * de la base de reglas tratada y cuyos valores asociados son los mintérminos
   * que representan las reglas con dicha conclusión.
   * @param rb Base de reglas que se quiere representar en forma de mintérminos.
   * @return Map cuyas claves son las conclusiones de la base de reglas y cuyos
   * valores asociados a dichas claves son los mintérminos que representan las
   * reglas que tienen dicha conclusión.
   *
   */
  public Map build(Rulebase rb) {
    //obtiene las reglas de la base de reglas tratada
    Rule[] rules = rb.getRules();
    //crea un constructor de mintérminos
    XfspMintermBuilder builder = new XfspMintermBuilder();
    //establece las variables de entrada de la base de reglas
    builder.setInputvars(rb.getInputs());
    //para todas las reglas de la base de reglas...
    for (int i = 0; i < rules.length; i++) {
      //...obtiene una nueva regla...
      Rule rule = rules[i];
      //...y contruye el conjunto de mintérminos que la representa
      builder.buildRule(rule);
    }
    //devuelve el map que representa la base de reglas tratada
    return builder.getMinterms();
  }
}
