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

import java.util.ArrayList;
import java.util.List;

import xfuzzy.lang.Conclusion;
import xfuzzy.lang.Relation;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;

/**
 * <p> <b>Descripción:</b> constructor de reglas contraídas equivalentes a una
 * dada.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see IXfspRulebaseSimplifier
 * @see XfspModel
 * @see XfspRuleConverter
 *
 */
public class XfspCompactBuilder
    extends XfspRuleConverter {

  //lista de reglas que tiene que ya procesado el constructor
  private List list;

  //nueva base de reglas expandida equivalente a la original
  private Rulebase newrb;

  /**
   * <p> <b>Descripción:</b> crea un constructor de reglas expandidas
   * equivalentes a una dada.
   * @param rulebase Base de reglas a la que pertenencen las reglas que se le
   * pasan al constructor mediante el métod buildRule.
   *
   */
  public XfspCompactBuilder(Rulebase rb) {
    //inicializa la lista de reglas que quedan por procesar
    list = new ArrayList();
    //inicializa la nueva base de reglas como el clon de la original
    //newrb = (Rulebase) rb.clone();



    newrb = new Rulebase(rb.getName());
    newrb.setOperatorset(rb.getOperatorset());
    for(int i=0;i<rb.getInputs().length;i++){
      newrb.addInputVariable(rb.getInputs()[i]);
    }
    for(int i=0;i<rb.getOutputs().length;i++){
      newrb.addOutputVariable(rb.getOutputs()[i]);
    }



  }

  /**
   * <p> <b>Descripción:</b> transforma la regla indicada en un conjunto de
   * reglas equivalentes en forma contraída.
   * @param rule Regla que se quiere transformar en otras equivalentes en forma
   * contraída para ser añadida a la base de reglas que está construyendo.
   *
   */
  public void buildRule(Rule rule) {
    //permite recorrer la lista de reglas
    int i = 0;
    //indica si la regla que se está procesando se tiene que insertar en la
    //lista de reglas
    boolean insert = false;
    //mientras queden reglas por procesar y todavía no se haya insertado la
    //regla en la lista
    while (i < list.size() && !insert) {
      //si la regla que se está procesando de la lista tiene la misma
      //conclusión que la regla que se quiere transformar...
      if (sameConclusions( (Rule) ( (List) list.get(i)).get(0), rule)) {
        //...se indica que puede insertar la nueva regla en la posición
        //i-ésima de la lista de reglas
        insert = true;
      }
      //en otro caso...
      else {
        //...se sigue buscando
        i++;
      }
    }
    //si se puede insertar la regla a transformar en la lista de reglas...
    if (insert) {
      //...se añade a la lista en la posición encontrada
      ( (List) list.get(i)).add(rule);
    }
    //en otro caso...
    else {
      //...crea una nueva lista...
      List newl = new ArrayList();
      //...le añade la regla a transformar...
      newl.add(rule);
      //...y añade dicha lista a la lista de reglas tratadas
      list.add(newl);
    }
  }

  /**
   * <p> <b>Descripción:</b> devuelve la base de reglas que se ha construido a
   * partir de la original mediante contracción de las reglas que la formaban.
   * @return Devuelve una base de reglas contraída que contiene reglas
   * equivalentes a las que se le han pasado por el método type buildRule.
   *
   */
  public Rulebase getRulebase() {
    //deja la nueva base de reglas vacía de toda regla que pudiera contener
    newrb.removeAllRules();
    //recorre la lista que agrupa reglas por consecuentes iguales...
    for (int i = 0; i < list.size(); i++) {
      //...y obtiene una regla (por ejemplo, la primera) para el consecuente
      //actual
      Rule aux = (Rule) ( (List) list.get(i)).get(0);
      //para todas las demás reglas que tienen el mismo consecuente...
      for (int j = 1; j < ( (List) list.get(i)).size(); j++) {
        //...crea una nueva premisa que consiste en la disyunción de la que ya
        //existía antes y la de la regla actual
        Relation premise = Relation.create(Relation.OR, aux.getPremise(),
                                           ( (Rule) ( (List) list.get(i)).get(j)).
                                           getPremise(), null, null, newrb);
        //obtiene la conclusión de la regla...
        Conclusion[] conc = aux.getConclusions();
        //...y crea una nueva regla con la premisa que acaba de crear...
        aux = new Rule(premise);
        //...y con las conclusiones que se obtuvieron antes
        for (int k = 0; k < conc.length; k++) {
          aux.add(conc[k]);
        }
      }
      //elimina todas las reglas que se acaban de procesar de la nueva base de
      //reglas...
      for (int j = 0; j < ( (List) list.get(i)).size(); j++) {
        newrb.remove( (Rule) ( (List) list.get(i)).get(j));
      }
      //...y le añade la nueva regla creada
      newrb.addRule(aux);
    }
    return newrb;
  }

  /**
   * <p> <b>Descripción:</b> comprueba las conclusiones de dos reglas son
   * iguales.
   * @param r1 Primera regla.
   * @param r2 Segunda regla.
   * @return Devuelve cierto si las conclusiones de las dos reglas son iguales
   * y falso en caso contrario.
   *
   */
  private boolean sameConclusions(Rule r1, Rule r2) {
    //resultado que se devolverá inicializado a cierto
    boolean result = true;
    //obtiene las conclusiones de la primera regla
    Conclusion[] c1 = r1.getConclusions();
    //obtiene las conclusiones de la segunda regla
    Conclusion[] c2 = r2.getConclusions();
    //si las reglas tienen un número distinto de conclusiones...
    if (c1.length != c2.length) {
      //...entonces tienen distintas conclusiones
      result = false;
    }
    //...en otro caso...
    else {
      //...comprueba que las dos reglas tienen las mismas conclusiones
      //recorre todas las conclusiones de la primera regla...
      for (int i = 0; i < c1.length && result; i++) {
        //...y busca una conclusión de la segunda regla igual a la de la primera
        boolean found = false;
        for (int j = 0; j < c2.length && !found; j++) {
          if (sameConclusion(c1[i], c2[j])) {
            found = true;
          }
        }
        //si no encontró ninguna conclusión de la segunda regla igual a la de
        //la primera...
        if (!found) {
          //...las dos reglas no tienen las mismas conclusiones
          result = false;
        }
      }
    }
    return result;
  }

  /**
   * <p> <b>Descripción:</b> comprueba si dos reglas son iguales.
   * @param c1 Primera conclusión.
   * @param c2 Segunda conclusión.
   * @return Devuelve cierto si las dos conclusiones son iguales y falso en
   * caso contrario.
   *
   */
  private boolean sameConclusion(Conclusion c1, Conclusion c2) {
    //resultado que se devolverá inicializado a cierto
    boolean result = true;
    //si la variable de la primera conclusión es distinta de la de la segunda...
    if (!c1.getVariable().getName().equals(c2.getVariable().getName())) {
      //...las conclusiones son distintas
      result = false;
    }
    //en otro caso...
    else {
      //...si la función de pertenencia de la primera conclusión es distinta de
      //la segunda...
      String lb2 = c2.getMembershipFunction().getLabel();
      if (!c1.getMembershipFunction().equals(lb2)) {
        //...las conclusiones son distintas
        result = false;
      }
    }
    return result;
  }
}
