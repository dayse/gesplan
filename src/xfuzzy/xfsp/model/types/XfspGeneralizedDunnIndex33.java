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
 * <p> <b>Descripción:</b> clase de los métodos de validación de clusters
 * mediante el índice de Dunn generalizado 33.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see IXfspTypeSimplifier
 * @see IXfspValidityMeasure
 * @see XfspDunnValidityMeasure
 * @see XfspGeneralizedDunnIndex
 * @see XfspTypeSimplifierFactory
 *
 */
public class XfspGeneralizedDunnIndex33
    extends XfspGeneralizedDunnIndex {

  /**
   * <p> <b>Descripción:</b> crea un medidor del índice de valoración de
   * clusters según el método de generalizado de Dunn 33.
   * @param data Conjunto de datos que se quieren agrupar en clusters y del que
   * se va a calcular el número óptimo de ellos.
   * @param weights Pesos asignados a cada uno de los elementos que compone un
   * data de los que se van a estudiar (como hayan sido definidos previamente).
   *
   */
  public XfspGeneralizedDunnIndex33(double[][] data, double[] weights) {
    //llama al constructor del padre con los mismos parámetros que se le pasan
    super(data, weights);
  }

  /**
   * <p> <b>Descripción:</b> devuelve el valor del numerador para el método de
   * validación Dunn 33.
   * @param data Conjunto de datos agrupables en <i>clusters</i>.
   * @param assign Asignación hecha de los datos a los distintos
   * <i>clusters</i>.
   * @param weights Pesos asignados a los parámetros de las funciones de
   * pertenencia.
   * @param c1 Número del primer <i>cluster</i>.
   * @param c2 Número del segundo <i>cluster</i>.
   * @return Valoración del numerador del método de validación de Dunn 33.
   *
   */
  protected double getNumerator(double[][] data, int[] assign, double[] weights,
                                int c1, int c2) {
    IXfspDistance d = new XfspEuclideanDistance(weights);
    return d.averageDistance(data, assign, c1, c2);
  }
}
