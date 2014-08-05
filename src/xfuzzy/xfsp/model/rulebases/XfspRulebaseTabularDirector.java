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
 * <p> <b>Descripción:</b> dirige la construcción de una nueva base de reglas a
 * partir de un cubrimiento mínimo de los mintérminos de una base de reglas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.1
 * @see XfspModel
 * @see XfspTabularSimplifier
 * @see XfspRulebaseTabularBuilder
 *
 */
public class XfspRulebaseTabularDirector {

  //constructor de reglas a partir de los mintérminos asociados a una
  //determinada conclusión
  private XfspRulebaseTabularBuilder builder;

  //indica si se deben mantener las funciones de pertenencia situadas en los
  //extremos del universo de discurso de cada tipo
  private boolean maintainExtremeMF;

  /**
   * <p> <b>Descripción:</b> crea un constructor de bases de reglas a partir de
   * un cubrimiento mínimo de los mintérminos de una base de reglas que se
   * quiere simplificar.
   *
   */
  public XfspRulebaseTabularDirector() {
  }

  /**
   * <p> <b>Descripción:</b> establece si se deben mantener las funciones de
   * pertenencia situadas en los extremos del universo de discurso de cada tipo.
   * @param maintainExtremeMF Si vale cierto se mantienen las funciones de
   * pertenencia situadas en los extremos del universo de discurso de cada tipo
   * y si vale falso no lo hace.
   *
   */
  public void setMaintainExtremeMF(boolean maintainExtremeMF){
    //indica si se deben mantener las funciones de pertenencia que están en los
    //extremos del universo de discurso de las variables o, por el contrario, si
    //pueden ser eliminadas
    this.maintainExtremeMF = maintainExtremeMF;
  }

  /**
   * <p> <b>Descripción:</b> construye una base de reglas a partir de una base
   * de reglas original y del cubrimiento mínimo de los mintérminos asociados a
   * dicha base de reglas.
   * @param minimalCovering Cubrimiento mínimo de los mintérminos asociados a
   * la base de reglas original.
   * @param rulebase Base de reglas para la que se quiere construir otra
   * equivalente a partir del cubrimiento mínimo de sus mintérminos.
   *
   */
  public Rulebase build(Map minimalCovering, Rulebase rulebase) {
    //crea el constructor de reglas para los mintérminos asociados a una
    //determinada conclusión
    builder = new XfspRulebaseTabularBuilder(this.maintainExtremeMF,rulebase);
    //iterador que permite recorrer los mintérminos que forman el cubrimiento
    //mínimo asociado a una conclusión
    Iterator it = minimalCovering.keySet().iterator();
    //para los mintérminos de todas las conclusiones de la base de reglas
    //original...
    while (it.hasNext()) {
      //...obtiene una nueva conclusión...
      Conclusion conc = (Conclusion) it.next();
      //...los mintérminos que forman su  cubrimiento mínimo...
      Set minterms = (Set) minimalCovering.get(conc);
      //...y construye un conjunto de reglas para dicha conclusión a partir de
      //los mintérminos anteriores
      builder.buildRule(conc, minterms);
    }
    //devuelve la base de reglas que se ha construido tras tratar todas las
    //conclusiones de la base de reglas original
    return builder.getRulebase();
  }
}
