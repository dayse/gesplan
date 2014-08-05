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

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import xfuzzy.lang.Conclusion;
import xfuzzy.lang.Variable;

/**
 * <p> <b>Descripción:</b> representa un mintérmino o una implicante en el
 * algoritmo de simplificación tabular de bases de reglas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see XfspModel
 * @see XfspTabularSimplifier
 * @see XfspRulebaseTabularBuilder
 * @see XfspRulebaseTabularDirector
 *
 */
public class XfspMinterm
    implements Cloneable {

  //conclusión a la que está referido el mintérmino
  private Conclusion conclusion;

  //map cuyas claves son las variables del mintérmino y cuyos datos son las
  //funciones de pertenencia que pueden tomar dichas variables
  private Map variables;

  //conjunto de mintérminos que cubre el mintérmino o implicante
  private Set coveredMinterms;

  /**
   * <p> <b>Descripción:</b> crea un mintérmino inicial sin asignaciones para
   * sus variables ni conclusión.
   *
   */
  public XfspMinterm() {
    //inicializa el conjunto de mintérminos cubiertos con el actual
    coveredMinterms = new HashSet();
    coveredMinterms.add(this);
    //no realiza ninguna asignación a las variables...
    variables = new HashMap();
    //...ni establece la conclusión a la que está referido el mintérmino
    conclusion = null;
  }

  /**
   * <p> <b>Descripción:</b> crea un mintérmino o una implicante sin
   * asignaciones para sus variables ni conclusión.
   * @param isInitial Un valor cierto denota que se trata de un mintérmino
   * inicial y un valor falso indica que las variables pueden tener varias
   * asignaciones.
   *
   */
  public XfspMinterm(boolean isInitial) {
    //inicializa el conjunto de mintérminos cubiertos como conjunto vacío
    coveredMinterms = new HashSet();
    //no realiza asignaciones para las variables...
    variables = new HashMap();
    //...ni indica la conclusión a la que se refiere el mintérmino
    conclusion = null;
    //si se trata de un mintérmino inicial...
    if (isInitial) {
      //...añade el propio mintérmino como cubierto por sí mismo
      coveredMinterms.add(this);
    }
  }

  /**
   * <p> <b>Descripción:</b> realiza una copia de mintérmino actual.
   * @return Copia exacta del mintérmino actual.
   *
   */
  public Object clone() {
    //crea un nuevo mintermino
    XfspMinterm result = new XfspMinterm();
    //establece la misma conclusión que tiene el mintérmino actual
    result.setConclusion(this.getConclusion());
    //clona el map de variables y funciones de pertenencia del mintérmino actual
    result.setVariables((Map)((HashMap)this.getVariables()).clone());
    //clona el conjunto de mintérminos cubiertos por el mintérmino actual
    result.setCoveredMinterms((Set)((HashSet)this.getCoveredMinterms()).clone());
    //devuelve el clon creado
    return (Object) result;
  }

  /**
   * <p> <b>Descripción:</b> devuelve la conclusión a la que hace referencia el
   * mintérmino.
   * @return Conclusión derivada del mintérmino actual.
   *
   */
  public Conclusion getConclusion() {
    return conclusion;
  }

  /**
   * <p> <b>Descripción:</b> establece la conclusión del mintérmino.
   * @param conclusion Conclusión a la que hará referencia el mintérmino.
   *
   */
  public void setConclusion(Conclusion conclusion) {
    this.conclusion = conclusion;
  }

  /**
   * <p> <b>Descripción:</b> devuelve las asignaciones de funciones de
   * pertenencia a variables hechas en el mintérmino actual.
   * @param return Conjunto de pares (clave, valor) donde la clave es una
   * variable de entrada y el valor devuelto es el conjunto de funciones de
   * pertenencia que toma como valor dicha variable en el mintérmino actual.
   *
   */
  public Map getVariables() {
    return variables;
  }

  /**
   * <p> <b>Descripción:</b> establece las asignaciones de funciones de
   * pertenencia a las variables del mintérmino.
   * @param variables Conjunto de pares (clave, valor) donde la clave es una
   * variable de entrada y el valor devuelto es el conjunto de funciones de
   * pertenencia que toma como valor dicha variable en el mintérmino actual.
   *
   */
  public void setVariables(Map variables) {
    this.variables = variables;
  }

  /**
   * <p> <b>Descripción:</b> devuelve el conjunto de mintérminos que cubre el
   * mintérmino o implicante actual.
   * @return Conjunto de mintérminos cubiertos por el mintérmino o implicante
   * actual.
   *
   */
  public Set getCoveredMinterms() {
    return this.coveredMinterms;
  }

  /**
   * <p> <b>Descripción:</b> establece el conjunto de cubiertos por el
   * mintérimo o implicante actual.
   * @param coveredMinterms Conjunto de mintérminos cubiertos por el mintérmino
   * o implicante actual.
   *
   */
  public void setCoveredMinterms(Set coveredMinterms) {
    this.coveredMinterms = coveredMinterms;
  }

  /**
   * <p> <b>Descripción:</b> determina si un objeto es igual al mintérmino
   * actual.
   * @param obj Objeto que se quiere comparar con el mintérmino actual.
   * @return Devuelve cierto si el objeto comparado es idéntico al mintérmino
   * actual y falso en caso contrario.
   *
   */
  public boolean equals(Object obj) {
    //resultado de la comparación
    boolean result;
    //si el objeto comparado no es de la misma clase que el actual...
    if (! (obj instanceof XfspMinterm)) {
      //...devuelve falso
      result = false;
    }
    //en caso contrario...
    else {
      //convierte el objeto a comprar a la clase XfspMinterm
      XfspMinterm minterm = (XfspMinterm) obj;
      //compara las variables de la conclusión de ambos objetos
      result = this.getConclusion().getVariable().equals(minterm.getConclusion().
          getVariable().toString())
          &&
          this.getConclusion().getMembershipFunction().equals(minterm.
          getConclusion().
          getMembershipFunction().toString()
          );
      //compara el número de variables de ambos mintérminos
      result &= minterm.getVariables().size() == this.getVariables().size();
      //obtiene las variables del mintérmino que se quiere comparar
      Collection keys = minterm.getVariables().keySet();
      //iterador que permite recorrer todas las variables del mintérmino a
      //comparar
      Iterator it = keys.iterator();
      //para todas las variables del mintérmino a comparar...
      while (it.hasNext() && result) {
        //...comprueba que tiene asignadas las mismas funciones de pertenencia
        //en ambos mintérminos
        Variable var = (Variable) it.next();
        Set s1 = (Set) minterm.getVariables().get(var);
        Set s2 = (Set)this.getVariables().get(var);
        result = s1.equals(s2);
      }
    }
    //devuelve el resultado de la comparación
    return result;
  }

  /**
   * <p> <b>Descripción:</b> devuelve una representación del mintérmino o
   * implicante en formato String.
   * @return Cadena que representa el mintérmino o implicante.
   *
   */
  public String toString() {
    //representación que comienza con la conclusión a la que se refiere el
    //mintérmino o implicante
    String result = "[Conclusion: " + conclusion.toXfl();
    //obtiene una colección con las variables del mintérmino o implicante
    Collection col = variables.keySet();
    //iterador que permite recorrer todas las variables del mintérmino o
    //implicante
    Iterator it = col.iterator();
    //mientras queden variables por recorrer...
    while (it.hasNext()) {
      //...obtiene la siguiente variable...
      Variable var = (Variable) it.next();
      //...y añade a la representación la variable...
      result += " (" + var;
      Set s = (Set) variables.get(var);
      //...y las funciones de pertenencia que le son asignadas en el mintérmino
      //o implicante
      result += s.toString();
      result += ")";
    }
    result += "]";
    return result;
  }
}
