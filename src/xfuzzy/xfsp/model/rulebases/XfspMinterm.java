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
 * <p> <b>Descripci�n:</b> representa un mint�rmino o una implicante en el
 * algoritmo de simplificaci�n tabular de bases de reglas.
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

  //conclusi�n a la que est� referido el mint�rmino
  private Conclusion conclusion;

  //map cuyas claves son las variables del mint�rmino y cuyos datos son las
  //funciones de pertenencia que pueden tomar dichas variables
  private Map variables;

  //conjunto de mint�rminos que cubre el mint�rmino o implicante
  private Set coveredMinterms;

  /**
   * <p> <b>Descripci�n:</b> crea un mint�rmino inicial sin asignaciones para
   * sus variables ni conclusi�n.
   *
   */
  public XfspMinterm() {
    //inicializa el conjunto de mint�rminos cubiertos con el actual
    coveredMinterms = new HashSet();
    coveredMinterms.add(this);
    //no realiza ninguna asignaci�n a las variables...
    variables = new HashMap();
    //...ni establece la conclusi�n a la que est� referido el mint�rmino
    conclusion = null;
  }

  /**
   * <p> <b>Descripci�n:</b> crea un mint�rmino o una implicante sin
   * asignaciones para sus variables ni conclusi�n.
   * @param isInitial Un valor cierto denota que se trata de un mint�rmino
   * inicial y un valor falso indica que las variables pueden tener varias
   * asignaciones.
   *
   */
  public XfspMinterm(boolean isInitial) {
    //inicializa el conjunto de mint�rminos cubiertos como conjunto vac�o
    coveredMinterms = new HashSet();
    //no realiza asignaciones para las variables...
    variables = new HashMap();
    //...ni indica la conclusi�n a la que se refiere el mint�rmino
    conclusion = null;
    //si se trata de un mint�rmino inicial...
    if (isInitial) {
      //...a�ade el propio mint�rmino como cubierto por s� mismo
      coveredMinterms.add(this);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> realiza una copia de mint�rmino actual.
   * @return Copia exacta del mint�rmino actual.
   *
   */
  public Object clone() {
    //crea un nuevo mintermino
    XfspMinterm result = new XfspMinterm();
    //establece la misma conclusi�n que tiene el mint�rmino actual
    result.setConclusion(this.getConclusion());
    //clona el map de variables y funciones de pertenencia del mint�rmino actual
    result.setVariables((Map)((HashMap)this.getVariables()).clone());
    //clona el conjunto de mint�rminos cubiertos por el mint�rmino actual
    result.setCoveredMinterms((Set)((HashSet)this.getCoveredMinterms()).clone());
    //devuelve el clon creado
    return (Object) result;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la conclusi�n a la que hace referencia el
   * mint�rmino.
   * @return Conclusi�n derivada del mint�rmino actual.
   *
   */
  public Conclusion getConclusion() {
    return conclusion;
  }

  /**
   * <p> <b>Descripci�n:</b> establece la conclusi�n del mint�rmino.
   * @param conclusion Conclusi�n a la que har� referencia el mint�rmino.
   *
   */
  public void setConclusion(Conclusion conclusion) {
    this.conclusion = conclusion;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve las asignaciones de funciones de
   * pertenencia a variables hechas en el mint�rmino actual.
   * @param return Conjunto de pares (clave, valor) donde la clave es una
   * variable de entrada y el valor devuelto es el conjunto de funciones de
   * pertenencia que toma como valor dicha variable en el mint�rmino actual.
   *
   */
  public Map getVariables() {
    return variables;
  }

  /**
   * <p> <b>Descripci�n:</b> establece las asignaciones de funciones de
   * pertenencia a las variables del mint�rmino.
   * @param variables Conjunto de pares (clave, valor) donde la clave es una
   * variable de entrada y el valor devuelto es el conjunto de funciones de
   * pertenencia que toma como valor dicha variable en el mint�rmino actual.
   *
   */
  public void setVariables(Map variables) {
    this.variables = variables;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el conjunto de mint�rminos que cubre el
   * mint�rmino o implicante actual.
   * @return Conjunto de mint�rminos cubiertos por el mint�rmino o implicante
   * actual.
   *
   */
  public Set getCoveredMinterms() {
    return this.coveredMinterms;
  }

  /**
   * <p> <b>Descripci�n:</b> establece el conjunto de cubiertos por el
   * mint�rimo o implicante actual.
   * @param coveredMinterms Conjunto de mint�rminos cubiertos por el mint�rmino
   * o implicante actual.
   *
   */
  public void setCoveredMinterms(Set coveredMinterms) {
    this.coveredMinterms = coveredMinterms;
  }

  /**
   * <p> <b>Descripci�n:</b> determina si un objeto es igual al mint�rmino
   * actual.
   * @param obj Objeto que se quiere comparar con el mint�rmino actual.
   * @return Devuelve cierto si el objeto comparado es id�ntico al mint�rmino
   * actual y falso en caso contrario.
   *
   */
  public boolean equals(Object obj) {
    //resultado de la comparaci�n
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
      //compara las variables de la conclusi�n de ambos objetos
      result = this.getConclusion().getVariable().equals(minterm.getConclusion().
          getVariable().toString())
          &&
          this.getConclusion().getMembershipFunction().equals(minterm.
          getConclusion().
          getMembershipFunction().toString()
          );
      //compara el n�mero de variables de ambos mint�rminos
      result &= minterm.getVariables().size() == this.getVariables().size();
      //obtiene las variables del mint�rmino que se quiere comparar
      Collection keys = minterm.getVariables().keySet();
      //iterador que permite recorrer todas las variables del mint�rmino a
      //comparar
      Iterator it = keys.iterator();
      //para todas las variables del mint�rmino a comparar...
      while (it.hasNext() && result) {
        //...comprueba que tiene asignadas las mismas funciones de pertenencia
        //en ambos mint�rminos
        Variable var = (Variable) it.next();
        Set s1 = (Set) minterm.getVariables().get(var);
        Set s2 = (Set)this.getVariables().get(var);
        result = s1.equals(s2);
      }
    }
    //devuelve el resultado de la comparaci�n
    return result;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve una representaci�n del mint�rmino o
   * implicante en formato String.
   * @return Cadena que representa el mint�rmino o implicante.
   *
   */
  public String toString() {
    //representaci�n que comienza con la conclusi�n a la que se refiere el
    //mint�rmino o implicante
    String result = "[Conclusion: " + conclusion.toXfl();
    //obtiene una colecci�n con las variables del mint�rmino o implicante
    Collection col = variables.keySet();
    //iterador que permite recorrer todas las variables del mint�rmino o
    //implicante
    Iterator it = col.iterator();
    //mientras queden variables por recorrer...
    while (it.hasNext()) {
      //...obtiene la siguiente variable...
      Variable var = (Variable) it.next();
      //...y a�ade a la representaci�n la variable...
      result += " (" + var;
      Set s = (Set) variables.get(var);
      //...y las funciones de pertenencia que le son asignadas en el mint�rmino
      //o implicante
      result += s.toString();
      result += ")";
    }
    result += "]";
    return result;
  }
}
