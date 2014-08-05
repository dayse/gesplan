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
 * <p> <b>Descripción:</b> convierte bases de reglas bien a su forma expandida,
 * bien a su forma compacta. También actúa como fábrica de conversores
 * concretos.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.6
 * @see XfspModel
 * @see XfspCompactBuilder
 * @see XfspExpandBuilder
 *
 */
public abstract class XfspRuleConverter {

  /**
   * <p> <b>Descripción:</b> crea un conversor concreto de bases de reglas. El
   * tipo de conversor viene determinado por el argumento <i>type<\i>.
   * @param type Tipo de conversor que se va a crear. Los valores permitidos
   * son <i>Union<\i> o <i>Separation<\i>.
   * @param rb Base de reglas que debe transformar el conversor creado.
   *
   */
  public static XfspRuleConverter create(String type, Rulebase rb){
    //conversor que se creará
    XfspRuleConverter converter = null;
    //si el tipo de conversor a crear es de unión de reglas con mismos
    //consecuentes...
    if (type.equals("Union")) {
      //...crea el convesor de la clase XfspCompactBuilder
      converter = new XfspCompactBuilder(rb);
    }
    //si el tipo de conversor a crear es de separación de reglas con OR en su
    //premisa...
    else if (type.equals("Separation")) {
      //...crea el conversor de la clase XfspExpandBuilder
      converter = new XfspExpandBuilder(rb);
    }
    return converter;
  }

  /**
   * <p> <b>Descripción:</b> transforma una regla, bien para expandirla, bien
   * para compactarla.
   * @param rule Regla convertida a su forma expandida o compacta.
   *
   */
  public void buildRule(Rule rule) {
  }

  /**
   * <p> <b>Descripción:</b> obtiene la base de reglas que se ha ido creando
   * de forma incremental conforme se convertían las reglas que se le pasaban
   * al conversor.
   * @return Base de reglas que se ha creado a partir de las reglas que se han
   * ido convirtiendo.
   *
   */
  public abstract Rulebase getRulebase();
}
