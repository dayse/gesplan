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

import java.io.File;

import xfuzzy.lang.XflException;

import xfuzzy.xfsl.XfslPattern;

/**
 * <p> <b>Descripci�n:</b> f�brica de objetos que simplifican bases de reglas.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see IXfspRulebaseSimplifierFactory
 * @see IXfspRulebaseSimplifier
 * @see XfspModel
 * @see XfspRulebaseSimplifierDirector
 * @see XfspTabularSimplifier
 * @see XfspThresholdPruneSimplifier
 * @see XfspBestPruneSimplifier
 * @see XfspWorstPruneSimplifier
 *
 */
public class XfspRulebaseSimplifierFactory
    implements IXfspRulebaseSimplifierFactory {

  //instancia de la clase (�nica en el sistema) con la que se implementa el
  //patr�n Singleton
  static private XfspRulebaseSimplifierFactory instance = null;

  /**
   * <p> <b>Descripci�n:</b> contruye una f�brica de objetos que simplifican
   * bases de reglas. Este constructor es privado a fin de que s�lo los objetos
   * de la propia clase puedan llamarlo y los de otras clases hagan uso del
   * m�todo <i>getInstance<\i>.
   *
   */
  private XfspRulebaseSimplifierFactory() {
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve una instancia de la clase. Este m�todo es
   * la �nica forma de obtener f�bricas de simplificadores de reglas.
   *
   */
  public static XfspRulebaseSimplifierFactory getInstance() {
    //si no exist�a previamente una instancia de la clase...
    if (instance == null) {
      //...crea una nueva
      instance = new XfspRulebaseSimplifierFactory();
    }
    //devuelve una instancia de la clase
    return instance;
  }

  /**
   * <p> <b>Descripci�n:</b> crea un objeto simplificador de bases de reglas
   * del tipo indicado en el par�metro <i>method<\i> y al que se le pasan los
   * argumengos contenidos en <i>args<\i>.
   * @param method Tipo de simplificador de bases de reglas que se quiere
   * obtener de la f�brica. Los tipos permitidos son: <i>Union<\i>,
   * <i>Separation<\i>, <i>Tabular<\i>, <i>Threshold<\i>, <i>Best<\i> y
   * <i>Worst<\i>.
   * @param args Argumentos que hay que pasar al simplificador concreto que se
   * cree.
   *
   */
  public IXfspRulebaseSimplifier create(String method, Object[] args) {
    //objeto que permite simplificar bases de reglas
    IXfspRulebaseSimplifier s = null;
    //si hay que simplificar la base de reglas por uni�n de reglas con mismos
    //consecuentes o por separaci�n de reglas que tienen OR en su
    //antecedente...
    if (method.equals("Union") || method.equals("Separation")) {
      //...crea un director que aplicar� el patr�n builder para ir construyendo
      //la nueva base de reglas
      s = new XfspRulebaseSimplifierDirector(method);
    }
    else if (method.equals("Tabular")) {
      s = new XfspTabularSimplifier( ( (Boolean) args[1]).booleanValue());
    }
    //en otro caso...
    else {
      //obtiene el m�todo de podado que debe utilizar
      String pruningMethod = (String) args[1];
      //obtiene el fichero de entrenamiento
      File patternFile = (File) args[2];
      //obtiene el n�mero de entradas del sistema
      int numInputs = ( (Integer) args[3]).intValue();
      //obtiene el n�mero de salidas del sistema
      int numOutputs = ( (Integer) args[4]).intValue();
      try {
        //obtiene el fichero con los patrones de activaci�n de las entradas
        //para poder llevar a cabo el podado de la base de reglas
        XfslPattern pattern = new XfslPattern(patternFile, numInputs, numOutputs);
        //si hay que podar las reglas cuya m�xima activaci�n est�n por debajo
        //de un umbral...
        if (pruningMethod.equals("Threshold")) {
          //..obtiene el umbral por debajo del cual las reglas con menor valor
          //m�ximo de activaci�n deben ser podadas de la base de reglas...
          double threshold = ( (Double) args[5]).doubleValue();
          //...y crea el simplificador de bases de reglas correspondiente
          s = new XfspThresholdPruneSimplifier(pattern.input, threshold);
        }
        //si el podado debe consistir en dejar las n mejores o en quitar las n
        //peores reglas seg�n su m�xima activaci�n...
        else {
          //...obtiene el n�mero de reglas que deben quedar o se deben quitar
          //de la base de reglas despu�s del podado de la base de reglas
          int numRules = ( (Integer) args[5]).intValue();
          //si deben quedar tras el podado en la base de reglas s�lo las n
          //mejores...
          if (pruningMethod.equals("Best")) {
            //...la variable 'numRules' indica cu�ntas deben quedar
            s = new XfspBestPruneSimplifier(pattern.input, numRules);
          }
          //...y si debe quitar las n peores
          else if (pruningMethod.equals("Worst")) {
            //...la variable 'numRules' indica cu�ntas hay que eliminar
            s = new XfspWorstPruneSimplifier(pattern.input, numRules);
          }
        }
      }
      catch (XflException e) {
      }
    }
    //devuelve el simplificador de bases de reglas creado
    return s;
  }
}
