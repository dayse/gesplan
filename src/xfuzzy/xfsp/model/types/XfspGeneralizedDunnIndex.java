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

package xfuzzy.xfsp.model.types;

/**
 * <p> <b>Descripción:</b> clase común a todos los métodos de validación de
 * clusters mediante los índices de Dunn generalizados.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see IXfspTypeSimplifier
 * @see IXfspValidityMeasure
 * @see XfspDunnValidityMeasure
 * @see XfspTypeSimplifierFactory
 *
 */
public abstract class XfspGeneralizedDunnIndex
    extends XfspDunnValidityMeasure {

  /**
   * <p> <b>Descripción:</b> crea un medidor del índice de valoración de
   * clusters según el método de generalizado de Dunn.
   * @param data Conjunto de datos que se quieren agrupar en clusters y del que
   * se va a calcular el número óptimo de ellos.
   * @param weights Pesos asignados a cada uno de los elementos que compone un
   * data de los que se van a estudiar (como hayan sido definidos previamente).
   *
   */
  public XfspGeneralizedDunnIndex(double[][] data, double[] weights) {
    super(data, weights);
  }

  /**
   * <p> <b>Descripción:</b> devuelve el valor del denominador para cualquiera
   * de los métodos de validación de Dunn generalizados.
   * @param data Conjunto de datos agrupables en <i>clusters</i>.
   * @param assign Asignación hecha de los datos a los distintos
   * <i>clusters</i>.
   * @param weights Pesos asignados a los parámetros de las funciones de
   * pertenencia.
   * @param c Número del <i>cluster</i>.
   * @return Valoración del denominador de los métodos de validación de Dunn
   * generalizados.
   *
   */
  protected double getDenominator(double[][] data, int[] assign,
                                  double[][] cluster, double[] weights, int c) {
    IXfspDistance d = new XfspEuclideanDistance(weights);
    double accumulative = 0;

    for (int i = 0; i < data.length; i++) {
      if (assign[i] == c) {
        accumulative += d.distance(data, cluster, i, c);
      }
    }
    int S = d.size(data, assign, c);
    return 2 * (accumulative / S);
  }
}
