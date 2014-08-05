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
import xfuzzy.lang.Specification;

/**
 * <p> <b>Descripción:</b> simplificador de bases de reglas que permite
 * transformar las en otras equivalentes por compactación o expansión de las
 * mismas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.0
 * @see XfspModel
 * @see IXfspRulebaseSimplifier
 *
 */
public class XfspRulebaseSimplifierDirector
    implements IXfspRulebaseSimplifier {

  //conversor de reglas a otras equivalentes
  private XfspRuleConverter conv;

  //nueva base de reglas equivalente a la original
  private Rulebase newrb;

  //tipo de conversión que se hará de la base de reglas original
  private String type;

  /**
   * <p> <b>Descripción:</b> crea un simplificador de bases de reglas que
   * realiza la conversión de una base de reglas a otra equivalente por los
   * métodos de compactación o de expansión.
   * @param type Método de conversión de bases de reglas que se empleará. Puede
   * ser compactación o expansión de la base de reglas original.
   *
   */
  public XfspRulebaseSimplifierDirector(String type) {
    //establece el tipo de conversión que se llevará a cabo
    this.type = type;
  }

  /**
   * <p> <b>Descripción:</b> simplifica una base de reglas de un sistem difuso
   * cuya especificación también es proporcionada.
   * @param rulebase Base de reglas que se quiere simplificar.
   * @param spec Especificación del sistema cuya base de reglas se va a
   * simplificar.
   *
   */
  public void simplify(Rulebase rulebase, Specification spec) {
    //construye la nueva base de reglas a partir de la original
    newrb = build(this.type, rulebase);
    //cambia en la especificación del sistema la base de reglas original por la
    //nueva
    spec.exchangeRulebase(rulebase, newrb);
    //elimina todas las reglas de la base de reglas original...
    rulebase.removeAllRules();
    //...y le añade las reglas de la base nueva
    for (int i = 0; i < newrb.getRules().length; i++) {
      rulebase.addRule(newrb.getRules()[i]);
    }
  }

  /**
   * <p> <b>Descripción:</b> construye una nueva base de reglas a partir de la
   * primera y equivalente a la misma.
   * @param type Tipo de base de reglas que se va a construir. Éste puede ser
   * una base de reglas compactada o expandida.
   * @param rulebase Base de reglas original a partir de la cual se construirá
   * otra equivalente.
   *
   */
  public Rulebase build(String type, Rulebase rulebase) {
    //clona la base de reglas original
    newrb = (Rulebase) rulebase.clone();
    //crea un objeto que convierte reglas en otras equivalentes según el tipo
    //indicado
    conv = XfspRuleConverter.create(type, newrb);
    //obtiene las reglas de la base de reglas
    Rule[] rules = newrb.getRules();
    //para cada una de las reglas de la base de reglas...
    for (int i = 0; i < rules.length; i++) {
      Rule rule = rules[i];
      //...se la pasa al conversor de reglas para que la transforme en otra
      //equivalente
      conv.buildRule(rule);
    }
    //obtiene la base de reglas equivalente a la original que ha construido el
    //conversor de bases de reglas
    newrb = conv.getRulebase();
    //devuelve la nueva base de reglas
    return newrb;
  }
}
