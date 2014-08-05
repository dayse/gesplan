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
 * <p> <b>Descripci�n:</b> clase de los m�todos de validaci�n de clusters
 * mediante el �ndice de Dunn generalizado 33.
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
   * <p> <b>Descripci�n:</b> crea un medidor del �ndice de valoraci�n de
   * clusters seg�n el m�todo de generalizado de Dunn 33.
   * @param data Conjunto de datos que se quieren agrupar en clusters y del que
   * se va a calcular el n�mero �ptimo de ellos.
   * @param weights Pesos asignados a cada uno de los elementos que compone un
   * data de los que se van a estudiar (como hayan sido definidos previamente).
   *
   */
  public XfspGeneralizedDunnIndex33(double[][] data, double[] weights) {
    //llama al constructor del padre con los mismos par�metros que se le pasan
    super(data, weights);
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el valor del numerador para el m�todo de
   * validaci�n Dunn 33.
   * @param data Conjunto de datos agrupables en <i>clusters</i>.
   * @param assign Asignaci�n hecha de los datos a los distintos
   * <i>clusters</i>.
   * @param weights Pesos asignados a los par�metros de las funciones de
   * pertenencia.
   * @param c1 N�mero del primer <i>cluster</i>.
   * @param c2 N�mero del segundo <i>cluster</i>.
   * @return Valoraci�n del numerador del m�todo de validaci�n de Dunn 33.
   *
   */
  protected double getNumerator(double[][] data, int[] assign, double[] weights,
                                int c1, int c2) {
    IXfspDistance d = new XfspEuclideanDistance(weights);
    return d.averageDistance(data, assign, c1, c2);
  }
}
