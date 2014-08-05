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
 * <p> <b>Descripción:</b> clase que implementa el método de validación de
 * clusters mediante los índices de Davies & Bouldin.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see IXfspTypeSimplifier
 * @see IXfspValidityMeasure
 * @see XfspTypeSimplifierFactory
 *
 */
public class XfspDaviesBouldinValidityMeasure
    implements IXfspValidityMeasure {

  //array de datos para los que queremos saber cuál es el mejor número de
  //clusters en que se pueden agrupar
  private double[][] data;

  //pesos que se quiere dar a los elementos de que consta cada dato
  private double[] weights;

  //parámetro utilizado a la hora de calcular la Q-norma de Minkowski durante
  //la evaluación de los clusters
  private int q;

  //parámetro que se utiliza para calcular el valor de alfa de un determinado
  //cluster
  private int t;

  /**
   * <p> <b>Descripción:</b> crea un nuevo objeto de tipo
   * <b>XfspDaviesBouldinValidityMeasure</b> que permite evaluar el mejor
   * número de clusters en que agrupar un conjunto de datos basándose en el
   * método de validez de Davies & Bouldin (1979).
   * @param data Conjunto de datos de partida, cada uno de los cuales viene
   * representado como un array de <i>double</i>.
   * @param weights Pesos que se quieren otorgar a cada uno de los elementos
   * que compone un dato.
   * @param t Parámetro utilizado en el cálculo del valor alfa de un cluster
   * @para q Parámetro utilzado en el cálculo de la Q-Norma de Minkowski de un
   * vector.
   *
   */
  public XfspDaviesBouldinValidityMeasure(double[][] data, double[] weights,
                                          int t, int q) {
    this.data = data;
    this.weights = weights;
    this.t = t;
    this.q = q;
  }

  /**
   * <p> <b>Descripción:</b> calcula el número óptimo de clusters para agrupar
   * un conjunto de datos según el método de validez de Davies & Bouldin (1979).
   * @return Número de clusters más adecuado para agrupar un conjunto de datos
   * numéricos de tipo <i>double</i>.
   *
   */
  public int getNumClusters() {
    //como primera referencia consideraremos tantos clusters como datos tenemos
    int num = data.length;
    //creamos un objeto que permite representar clusters con los datos de
    //partida los pesos que queremos dar a cada uno de los elementos que forman
    //los datos de partida y un número inicial de clusters igual al número de
    //datos de partida de que disponemos...
    XfspCluster c = new XfspCluster(data, data.length, weights);
    //...y evaluamos la asignación anterior para tener una primera referencia
    double min = evaluation(c);
    //para todos los posibles números de clusters con que podemos agrupar los
    //datos (con un mínimo de dos) y sin tener en cuenta el caso de agrupar
    //los datos en tantos clusters como datos haya...
    for (int i = 2; i <= (data.length - 1); i++) {
      //...creamos un objeto que permite representar clusters con los mismos
      //datos y pesos que antes, pero con el número de cluster que estamos
      //considerando ahora...
      c = new XfspCluster(data, i, weights);
      //...y evaluamos dicha agrupación de datos en clusters
      double eval = evaluation(c);
      //si la nueva agrupación de datos en clusters mejora a la anterior...
      if (eval < min) {
        //...actualizamos tanto el número de clusters óptimo encontrado hasta
        //ahora como la evaluación óptima obtenida hasta ahora
        min = eval;
        num = i;
      }
    }
    //devolvemos el mejor valor para el número de cluster en que debemos agrupar
    //los datos
    return num;
  }

  /**
   * <p> <b>Descripción:</b> devuelve el resultado de la evaluación de las
   * asignaciones de los datos a los distintos clusters que se han hecho.
   * @param c Cluster que se quiere evaluar.
   * @return Valoración de las asignaciones de los datos de partida a los
   * distintos clusters.
   *
   */
  private double evaluation(XfspCluster c) {
    //número de clusters en que se han agrupado los datos
    int C = c.cluster.length;
    //creamos un objeto que nos permite calcular distancias entre datos y
    //clusters
    IXfspDistance d = new XfspEuclideanDistance(weights);
    //elemento aculumador que inicializamos a cero
    double accumulative = 0;
    //para cada pareja de clusters distintos...
    for (int i = 0; i < C; i++) {
      //...inicializamos el valor máximo al mínimo que puede tomar...
      double max = 0;
      for (int j = 0; j < C; j++) {
        //si se trata de dos clusters distintos...
        if (j != i) {
          //calculamos la diferencia entre los centros de dicha pareja de
          //clusters
          double[] diff = d.difference(c.cluster[i], c.cluster[j]);
          //calculamos el valor alfa correspondiente al cluster i-ésimo y al
          //cluster j-ésimo (ambos con parámetro t) y los sumamos...
          double aux = d.alpha(data, c.assign, c.cluster, i, t) +
              d.alpha(data, c.assign, c.cluster, j, t);
          //...y dividimos el resultado de dicha suma entre la Q-norma de
          //Minkowski de la diferencia entre los centros de los clusters
          aux /= d.minkowskiQNorm(diff, q);
          //si dicho valor supera al máximo que habíamos alcanzado hasta
          //ahora...
          if (aux > max) {
            //...actulizamos el máximo encontrado al nuevo valor...
            max = aux;
          }
        }
      }
      //...y vamos acumulando lso valores máximos en nuestro acumulador
      accumulative += max;
    }
    //el resultado de la función de evaluación es el cociente entre la suma de
    //los máximos y el numero de clusters en que hemos agrupado los datos
    return accumulative / C;
  }
}
