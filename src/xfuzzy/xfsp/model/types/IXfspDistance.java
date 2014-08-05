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
 * <p> <b>Descripci�n:</b> Interfaz con m�todos que permiten calcular distintos
 * tipos de distacias entre arrays de elementos de tipo <double>.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.5
 * @see XfspCluster
 * @see xfuzzy.xfsp.model.type.XfspEuclideanDistance
 *
 */
public interface IXfspDistance {

  /**
   * <p> <b>Descripci�n:</b> calcula la distancia entre los vectores
   * <i>i-�simo</i> y <i>j-�simo</i> de data1 y data2 respectivamente.
   * @param data1 Array de vectores de elementos de tipo <i>double</i>.
   * @param data2 Array de vectores de elementos de tipo <i>double</i>.
   * @param i Posici�n que ocupa en <b>data1</b> un vector de elementos de tipo
   * <b>double</b>.
   * @param j Posici�n que ocupa en <b>data2</b> un vector de elementos de tipo
   * <b>double</b>.
   * @return Distancia entre el vector de <i>double</i> apuntado por <b>i</b> y
   * el apuntado por <b>j</b>.
   *
   */
  public double distance(double[][] data1, double[][] data2, int i, int j);

  /**
   * <p> <b>Descripci�n:</b> calcula el di�metro de un cluster, es decir, la
   * distancia m�xima entre cualesquiera dos puntos pertenecientes a dicho
   * cluster.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qu� cluster est� asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-�simo</i> de data est�
   * asignado al cluster al que hace referencia el elemento <i>i-�simo</i> de
   * <b>assign</b>.
   * @param cl N�mero del cluster cuyo di�mtro queremos calcular.
   * @return Distancia m�xima entre cualquier par de puntos pertenecientes al
   * cluster <b>cl</b>.
   *
   */
  public double diameter(double[][] data, int[] assign, int cl);

  /**
   * <p> <b>Descripci�n:</b> calcula la distancia m�nima entre dos clusters, es
   * decir, la m�nima de las distancias entre cualesquiera dos puntos que
   * pertenezcan cada uno de ellos a uno de los clusters.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qu� cluster est� asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-�simo</i> de data est�
   * asignado al cluster al que hace referencia el elemento <i>i-�simo</i> de
   * <b>assign</b>.
   * @param cl1 N�mero de uno de los clusters.
   * @param cl2 N�mero del segundo de los clusters.
   * @return Distancia m�nima entre un punto de <b>cl1</b> y otro de <b>cl2</b>.
   *
   */
  public double minDistance(double[][] data, int[] assign, int cl1, int cl2);

  /**
   * <p> <b>Descripci�n:</b> calcula la distancia media entre los puntos
   * pertenecientes a dos clustersm�nima entre dos clusters.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qu� cluster est� asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-�simo</i> de data est�
   * asignado al cluster al que hace referencia el elemento <i>i-�simo</i> de
   * <b>assign</b>.
   * @param cl1 N�mero de uno de los clusters.
   * @param cl2 N�mero del segundo de los clusters.
   * @return Distancia media entre los puntos de <b>cl1</b> y los de <b>cl2</b>.
   *
   */
  public double averageDistance(double[][] data, int[] assign, int cl1, int cl2);

  /**
   * <p> <b>Descripci�n:</b> calcula la distancia de Haussdorff entre dos
   * clusters.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qu� cluster est� asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-�simo</i> de data est�
   * asignado al cluster al que hace referencia el elemento <i>i-�simo</i> de
   * <b>assign</b>.
   * @param cl1 N�mero de uno de los clusters.
   * @param cl2 N�mero del segundo de los clusters.
   * @return Distancia media entre los puntos de <b>cl1</b> y los de <b>cl2</b>.
   *
   */
  public double haussdorffDistance(double[][] data, int[] assign, int cl1,
                                   int cl2);

  /**
   * <p> <b>Descripci�n:</b> calcula el tama�o alfa de un <i>cluster</i>.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qu� cluster est� asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-�simo</i> de data est�
   * asignado al cluster al que hace referencia el elemento <i>i-�simo</i> de
   * <b>assign</b>.
   * @param cluster Array con los centros de los clusters en que se agrupan los
   * datos.
   * @param i N�mero del <i>cluster</i> cuyo tama�o alfa se quiere calcular.
   * @param t Valor del par�metros t para calcular el tama�o alfa de
   * <i>cluster</i>.
   * @return Tama�o alfa de un <i>cluster</i>.
   *
   */
  public double alpha(double[][] data, int[] assign, double[][] cluster, int i,
                      int t);

  /**
   * <p> <b>Descripci�n:</b> Q-norma de Minkowski de un vector.
   * @param v Vector para el que se calcular� la Q-norma de Minkowski.
   * @param q Par�metro para calcular la Q-norma de Minkowski.
   * @return Valor de la Q-norma de Minkowski del vector.
   *
   */
  public double minkowskiQNorm(double[] v, int q);

  /**
   * <p> <b>Descripci�n:</b> calcula la diferencia entre dos vectores.
   * @param v1 Vector al que se le restar� otro.
   * @param v2 Vector que se le restar� el primero.
   * @return Diferencia entre los dos vectores.
   *
   */
  public double[] difference(double[] v1, double[] v2);

  /**
   * <p> <b>Descripci�n:</b> calcula el tama�o de un <i>cluster</i> de datos.
   * @param data Vector de datos agrupados en <i>clusters</i>.
   * @param assign Vector con las asignaciones de los datos a los distintos
   * <i>clusterss</i>.
   * @param c N�mero del <i>cluster</i> cuyo tama�o se quiere calcular.
   * @return N�mero de elementos asignados al <i>cluster</i> deseado.
   *
   */
  public int size(double[][] data, int[] assign, int c);
}
