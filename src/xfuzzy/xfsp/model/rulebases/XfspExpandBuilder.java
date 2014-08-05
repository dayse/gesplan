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
 * <p> <b>Descripci�n:</b> constructor de reglas expandidas equivalentes a una
 * dada.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see IXfspRulebaseSimplifier
 * @see XfspModel
 * @see XfspRuleConverter
 *
 */
public class XfspExpandBuilder
    extends XfspRuleConverter {

  //lista de reglas que tiene que procesar el constructor
  private List list;

  //nueva base de reglas expandida equivalente a la original
  private Rulebase newrb;

  /**
   * <p> <b>Descripci�n:</b> crea un constructor de reglas expandidas
   * equivalentes a una dada.
   * @param rulebase Base de reglas a la que pertenencen las reglas que se le
   * pasan al constructor mediante el m�tod buildRule.
   *
   */
  public XfspExpandBuilder(Rulebase rb) {
    //inicializa la lista de reglas que quedan por procesar
    list = new ArrayList();
    //inicializa la nueva base de reglas como el clon de la original


    newrb = new Rulebase(rb.getName());
    newrb.setOperatorset(rb.getOperatorset());
    for(int i=0;i<rb.getInputs().length;i++){
      newrb.addInputVariable(rb.getInputs()[i]);
    }
    for(int i=0;i<rb.getOutputs().length;i++){
      newrb.addOutputVariable(rb.getOutputs()[i]);
    }



    //newrb = (Rulebase) rb.clone();
  }

  /**
   * <p> <b>Descripci�n:</b> transforma la regla indicada en un conjunto de
   * reglas equivalentes en forma expandida.
   * @param rule Regla que se quiere transformar en otras equivalentes en forma
   * expandida para ser a�adida a la base de reglas que est� construyendo.
   *
   */
  public void buildRule(Rule rule) {
    //lista auxiliar donde se a�adir�n las reglas que quedan por procesar
    List laux = new ArrayList();
    //comienza a�adiendo la regla que hay que transformar
    laux.add(rule);
    //obtiene el �ndice de la primera regla de las lista de reglas por procesar
    //que contiene una operaci�n OR en la premisa
    int i = existsOR(laux);
    while (i != -1) {
      //elimina la regla encontrada que tiene una operaci�n OR en su permisa de
      //la lista auxiliar para pasar a ser procesada
      Rule aux = (Rule) laux.remove(i);
      //toma la premisa de dicha regla...
      Relation premise = aux.getPremise();
      //...el operando izquiero...
      Relation rel1 = (Relation) premise.getLeftRelation().clone(newrb);
      //...y el derecho
      Relation rel2 = (Relation) premise.getRightRelation().clone(newrb);
      //construye dos nuevas reglas cuyas premisas son el operando izquierdo...
      Rule aux1 = new Rule(rel1);
      //...y el derecho, respectivamente
      Rule aux2 = new Rule(rel2);
      //a�adiendo a dichas reglas las conclusiones de la regla que se est�
      //procesando
      Conclusion[] conc = rule.getConclusions();
      for (int j = 0; j < conc.length; j++) {
        aux1.add(conc[j]);
        aux2.add(conc[j]);
      }
      //a�ade las nuevas reglas a la lista de reglas que quedan por procesar...
      laux.add(aux1);
      laux.add(aux2);
      //...y busca otra regla que tenga una operaci�n OR en su premisa
      i = existsOR(laux);
    }
    for (int j = 0; j < laux.size(); j++) {
      list.add(laux.get(j));
    }
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la base de reglas que se ha construido a
   * partir de la original mediante expansi�n de las reglas que la formaban.
   * @return Devuelve una base de reglas expandida que contiene reglas
   * equivalentes a las que se le han pasado por el m�todo type buildRule.
   *
   */
  public Rulebase getRulebase() {
    //elimina todas las reglas...
    newrb.removeAllRules();
    //...y a�ade las que se han ido construyendo con el m�todo buildRule
    for (int i = 0; i < list.size(); i++) {
      newrb.addRule( (Rule) list.get(i));
    }
    //devuelve la nueva base de reglas expandida
    return newrb;
  }

  /**
   * <p> <b>Descripci�n:</b> busca la primera regla de una lista de reglas cuya
   * premisa sea una operaci�n OR.
   * @param l Lista de reglas donde se buscar�.
   * @return Devuelve el �ndice de la primera regla de la lista que tenga una
   * operaci�n OR en su premisa o -1 en caso de no encontrar ninguna.
   *
   */
  private int existsOR(List l) {
    //almacena si se ha encontrado una regla con una operaci�n OR en su premisa
    boolean found = false;
    //�ndice que permite recorrer la lista de reglas
    int i = 0;
    //mientras queden reglas por procesar y no se haya encontrado ninguna con
    //una OR en su premisa
    while (i < l.size() && !found) {
      //si la regla que se est� procesando tiene una operaci�n OR en la
      //premisa...
      if ( ( (Rule) l.get(i)).getPremise().getKind() == Relation.OR) {
        //...indica que se ha encontrado la regla que se buscaba
        found = true;
      }
      //en caso contrario...
      else {
        //...sigue buscando en la lista de reglas
        i++;
      }
    }
    //si no se ha encontrado ninguna regla con una operaci�n OR en su premisa...
    if (!found) {
      //...el �ndice que devuelve es -1
      i = -1;
    }
    return i;
  }
}
