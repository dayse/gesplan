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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import xfuzzy.lang.Conclusion;
import xfuzzy.lang.Rulebase;

/**
 * <p> <b>Descripci�n:</b> dirige la construcci�n de una nueva base de reglas a
 * partir de un cubrimiento m�nimo de los mint�rminos de una base de reglas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.1
 * @see XfspModel
 * @see XfspTabularSimplifier
 * @see XfspRulebaseTabularBuilder
 *
 */
public class XfspRulebaseTabularDirector {

  //constructor de reglas a partir de los mint�rminos asociados a una
  //determinada conclusi�n
  private XfspRulebaseTabularBuilder builder;

  //indica si se deben mantener las funciones de pertenencia situadas en los
  //extremos del universo de discurso de cada tipo
  private boolean maintainExtremeMF;

  /**
   * <p> <b>Descripci�n:</b> crea un constructor de bases de reglas a partir de
   * un cubrimiento m�nimo de los mint�rminos de una base de reglas que se
   * quiere simplificar.
   *
   */
  public XfspRulebaseTabularDirector() {
  }

  /**
   * <p> <b>Descripci�n:</b> establece si se deben mantener las funciones de
   * pertenencia situadas en los extremos del universo de discurso de cada tipo.
   * @param maintainExtremeMF Si vale cierto se mantienen las funciones de
   * pertenencia situadas en los extremos del universo de discurso de cada tipo
   * y si vale falso no lo hace.
   *
   */
  public void setMaintainExtremeMF(boolean maintainExtremeMF){
    //indica si se deben mantener las funciones de pertenencia que est�n en los
    //extremos del universo de discurso de las variables o, por el contrario, si
    //pueden ser eliminadas
    this.maintainExtremeMF = maintainExtremeMF;
  }

  /**
   * <p> <b>Descripci�n:</b> construye una base de reglas a partir de una base
   * de reglas original y del cubrimiento m�nimo de los mint�rminos asociados a
   * dicha base de reglas.
   * @param minimalCovering Cubrimiento m�nimo de los mint�rminos asociados a
   * la base de reglas original.
   * @param rulebase Base de reglas para la que se quiere construir otra
   * equivalente a partir del cubrimiento m�nimo de sus mint�rminos.
   *
   */
  public Rulebase build(Map minimalCovering, Rulebase rulebase) {
    //crea el constructor de reglas para los mint�rminos asociados a una
    //determinada conclusi�n
    builder = new XfspRulebaseTabularBuilder(this.maintainExtremeMF,rulebase);
    //iterador que permite recorrer los mint�rminos que forman el cubrimiento
    //m�nimo asociado a una conclusi�n
    Iterator it = minimalCovering.keySet().iterator();
    //para los mint�rminos de todas las conclusiones de la base de reglas
    //original...
    while (it.hasNext()) {
      //...obtiene una nueva conclusi�n...
      Conclusion conc = (Conclusion) it.next();
      //...los mint�rminos que forman su  cubrimiento m�nimo...
      Set minterms = (Set) minimalCovering.get(conc);
      //...y construye un conjunto de reglas para dicha conclusi�n a partir de
      //los mint�rminos anteriores
      builder.buildRule(conc, minterms);
    }
    //devuelve la base de reglas que se ha construido tras tratar todas las
    //conclusiones de la base de reglas original
    return builder.getRulebase();
  }
}
