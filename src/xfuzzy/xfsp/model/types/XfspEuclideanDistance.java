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

import java.lang.Double;
import java.lang.Math;

/**
 * <p> <b>Descripción:</b> Implementación de los métodos que permiten calcular
 * distintos tipos de distacias entre arrays de elementos de tipo <double>.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.0
 * @see IXfspDistance
 *
 */
public class XfspEuclideanDistance
    implements IXfspDistance {

  //pesos asignados a las funciones de pertenencia de un tipo
  private double[] weights;

  /**
   * <p> <b>Descripción:</b> crea un objeto que permite calcular distintos
   * tipos de distancias de puntos y <i>clusters</i>
   * @param weights Pesos asignados a las funciones de pertenencia del tipo.
   *
   */
  public XfspEuclideanDistance(double[] weights) {
    this.weights = weights;
  }

  /**
   * <p> <b>Descripción:</b> calcula la distancia entre los vectores
   * <i>i-ésimo</i> y <i>j-ésimo</i> de data1 y data2 respectivamente.
   * @param data1 Array de vectores de elementos de tipo <i>double</i>.
   * @param data2 Array de vectores de elementos de tipo <i>double</i>.
   * @param i Posición que ocupa en <b>data1</b> un vector de elementos de tipo
   * <b>double</b>.
   * @param j Posición que ocupa en <b>data2</b> un vector de elementos de tipo
   * <b>double</b>.
   * @return Distancia entre el vector de <i>double</i> apuntado por <b>i</b> y
   * el apuntado por <b>j</b>.
   *
   */
  public double distance(double[][] data1, double[][] data2, int i, int j) {
    double dist = 0;

    for (int k = 0; k < data1[i].length; k++) {
      double diff = data1[i][k];
      diff -= data2[j][k];
      diff *= weights[k];
      dist += (diff * diff);
    }

    return dist;
  }

  /**
   * <p> <b>Descripción:</b> calcula el diámetro de un cluster, es decir, la
   * distancia máxima entre cualesquiera dos puntos pertenecientes a dicho
   * cluster.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qué cluster está asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-ésimo</i> de data está
   * asignado al cluster al que hace referencia el elemento <i>i-ésimo</i> de
   * <b>assign</b>.
   * @param cl Número del cluster cuyo diámtro queremos calcular.
   * @return Distancia máxima entre cualquier par de puntos pertenecientes al
   * cluster <b>cl</b>.
   *
   */
  public double diameter(double[][] data, int[] assign, int cl) {
    double max = 0;

    for (int i = 0; i < data.length; i++) {
      if (assign[i] != cl) {
        continue;
      }

      for (int j = i + 1; j < data.length; j++) {
        if (assign[j] != cl) {
          continue;
        }

        double dist = distance(data, data, i, j);

        if (dist > max) {
          max = dist;
        }
      }
    }

    return max;
  }

  /**
   * <p> <b>Descripción:</b> calcula la distancia mínima entre dos clusters, es
   * decir, la mínima de las distancias entre cualesquiera dos puntos que
   * pertenezcan cada uno de ellos a uno de los clusters.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qué cluster está asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-ésimo</i> de data está
   * asignado al cluster al que hace referencia el elemento <i>i-ésimo</i> de
   * <b>assign</b>.
   * @param cl1 Número de uno de los clusters.
   * @param cl2 Número del segundo de los clusters.
   * @return Distancia mínima entre un punto de <b>cl1</b> y otro de <b>cl2</b>.
   *
   */
  public double minDistance(double[][] data, int[] assign, int cl1, int cl2) {
    double min = Double.MAX_VALUE;

    for (int i = 0; i < data.length; i++) {
      if (assign[i] != cl1) {
        continue;
      }

      for (int j = 0; j < data.length; j++) {
        if (assign[j] != cl2) {
          continue;
        }

        double dist = distance(data, data, i, j);

        if (dist < min) {
          min = dist;
        }
      }
    }
    return min;
  }

  /**
   * <p> <b>Descripción:</b> calcula la distancia media entre los puntos
   * pertenecientes a dos clustersmínima entre dos clusters.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qué cluster está asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-ésimo</i> de data está
   * asignado al cluster al que hace referencia el elemento <i>i-ésimo</i> de
   * <b>assign</b>.
   * @param cl1 Número de uno de los clusters.
   * @param cl2 Número del segundo de los clusters.
   * @return Distancia media entre los puntos de <b>cl1</b> y los de <b>cl2</b>.
   *
   */
  public double averageDistance(double[][] data, int[] assign, int cl1, int cl2) {
    double accumulative = 0;

    for (int i = 0; i < data.length; i++) {
      if (assign[i] != cl1) {
        continue;
      }
      for (int j = 0; j < data.length; j++) {
        if (assign[j] != cl2) {
          continue;
        }
        double dist = distance(data, data, i, j);
        accumulative += dist;
      }
    }
    int S = size(data, assign, cl1);
    int T = size(data, assign, cl2);
    return accumulative / (S * T);
  }

  /**
   * <p> <b>Descripción:</b> calcula la distancia de Haussdorff entre dos
   * clusters.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qué cluster está asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-ésimo</i> de data está
   * asignado al cluster al que hace referencia el elemento <i>i-ésimo</i> de
   * <b>assign</b>.
   * @param cl1 Número de uno de los clusters.
   * @param cl2 Número del segundo de los clusters.
   * @return Distancia media entre los puntos de <b>cl1</b> y los de <b>cl2</b>.
   *
   */
  public double haussdorffDistance(double[][] data, int[] assign, int cl1,
                                   int cl2) {
    return Math.max(distancePrime(data, assign, cl1, cl2),
                    distancePrime(data, assign, cl2, cl1));
  }

  /**
   * <p> <b>Descripción:</b> calcula el tamaño alfa de un <i>cluster</i>.
   * @param data Array de vectores de elementos de tipo <i>double</i>, cada uno
   * de los cuales representa a un punto asignable a un cluster.
   * @param assign Indica a qué cluster está asignado cada elemento de
   * <b>data</b>, de tal forma que el elemento <i>i-ésimo</i> de data está
   * asignado al cluster al que hace referencia el elemento <i>i-ésimo</i> de
   * <b>assign</b>.
   * @param cluster Array con los centros de los clusters en que se agrupan los
   * datos.
   * @param i Número del <i>cluster</i> cuyo tamaño alfa se quiere calcular.
   * @param t Valor del parámetros t para calcular el tamaño alfa de
   * <i>cluster</i>.
   * @return Tamaño alfa de un <i>cluster</i>.
   *
   */
  public double alpha(double[][] data, int[] assign, double[][] cluster, int i,
                      int t) {
    double accumulative = 0;
    for (int k = 0; k < data.length; k++) {
      if (assign[k] == i) {
        accumulative += Math.pow(distance(data, cluster, k, i), t);
      }
    }
    int S = size(data, assign, i);

    return Math.pow( (accumulative / S), (double) 1 / t);
  }

  /**
   * <p> <b>Descripción:</b> Q-norma de Minkowski de un vector.
   * @param v Vector para el que se calculará la Q-norma de Minkowski.
   * @param q Parámetro para calcular la Q-norma de Minkowski.
   * @return Valor de la Q-norma de Minkowski del vector.
   *
   */
  public double minkowskiQNorm(double[] vector, int q) {
    double accumulative = 0;

    for (int i = 0; i < vector.length; i++) {
      accumulative += Math.pow(Math.abs(vector[i]), q);
    }
    return Math.pow(accumulative, 1 / q);
  }

  /**
   * <p> <b>Descripción:</b> calcula la diferencia entre dos vectores.
   * @param v1 Vector al que se le restará otro.
   * @param v2 Vector que se le restará el primero.
   * @return Diferencia entre los dos vectores.
   *
   */
  public double[] difference(double[] v1, double[] v2) {
    double[] difference = new double[v1.length];

    for (int i = 0; i < v1.length; i++) {
      difference[i] = v1[i] - v2[i];
    }
    return difference;
  }

  /**
   * <p> <b>Descripción:</b> calcula el tamaño de un <i>cluster</i> de datos.
   * @param data Vector de datos agrupados en <i>clusters</i>.
   * @param assign Vector con las asignaciones de los datos a los distintos
   * <i>clusterss</i>.
   * @param c Número del <i>cluster</i> cuyo tamaño se quiere calcular.
   * @return Número de elementos asignados al <i>cluster</i> deseado.
   *
   */
  public int size(double[][] data, int[] assign, int c) {
    int S = 0;

    for (int i = 0; i < data.length; i++) {
      if (assign[i] == c) {
        S++;
      }
    }
    return S;
  }

  /**
   * <p> <b>Descripción:</b> calcula la distancia prima entre dos
   * <i>clusters</i>.
   * @param data Vector de datos agrupados en <i>clusters</i>.
   * @param assign Vector con las asignaciones de los datos a los distintos
   * <i>clusterss</i>.
   * @param cl1 Número del primer <i>cluster</i>.
   * @param cl2 Número del segundo <i>cluster</i>.
   * @return Distancia prima entre los dos <i>clusters</i>.
   *
   */
  public double distancePrime(double[][] data, int[] assign, int cl1, int cl2) {
    double max = 0;

    for (int i = 0; i < data.length; i++) {
      if (assign[i] != cl1) {
        continue;
      }
      double min = Double.MAX_VALUE;
      for (int j = 0; j < data.length; j++) {
        if (assign[j] != cl2) {
          continue;
        }
        double dist = distance(data, data, i, j);
        if (dist < min) {
          min = dist;
        }
      }
      if (min > max) {
        max = min;
      }
    }
    return max;
  }
}
