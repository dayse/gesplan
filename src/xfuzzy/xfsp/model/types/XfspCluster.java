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
 * <p> <b>Descripción:</b> representa los clusters utilizados por el modelo
 * para la simplificación de tipos mediante la técnica de <i>clustering</i>.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see XfspClustering
 *
 */
public class XfspCluster {

  //almacena las asignaciones de clusters hechas a cada una de las funciones de
  //pertenencia del tipo que se quiere simplificar
  public int[] assign;

  //almacena los datos relativos a los clusters encontrados
  public double[][] cluster;

  //almacena los pesos que se deben dar a cada uno de los parámetros de las
  //funciones de pertenencia
  public double[] weights;

  //almacena los parámetros de las funciones de pertenencia del tipo que se
  //quiere simplificar por clustering
  public double[][] data;

  /**
   * <p> <b>Descripción:</b> crea una objeto que representa un conjunto de C
   * clusters bajo los que agrupar los datos contenidos en <b>data</b>,
   * considerando que los pesos que hay que dar a los elementos que componen los
   * datos son los especificados en el array <b>weights</b>.
   * @param data Array de vectores de elementos de tipo <i>double</i> que serán
   * asignados a clusters.
   * @param C Número de clusters en que queremos agrupar los elementos de
   * <b>data</b>.
   * @param weights Pesos que queremos asignar a cada uno de los elementos de
   * los vectores contenidos en <b>data</b>.
   *
   */
  public XfspCluster(double[][] data, int C, double[] weights) {
    //nuevo array de datos que vamos a agrupar en clusters
    this.data = data;
    //inicialización del array que contendrá tantos centros de clusters como se
    //nos indique a través del atributo C
    this.cluster = new double[C][data[0].length];
    //pesos que se utilizarán en los cálculos de las distancias a la hora de
    //agrupar los datos en clusters
    this.weights = weights;
    //inicialización del array que informa de a qué cluster ha sido asignado
    //cada dato
    this.assign = new int[data.length];
    //para los C primeros datos, asignamos dichos datos al cluster correspondiente
    //a la posición que ocupa el dato en el array data
    for (int i = 0; i < C; i++) {
      assign[i] = i;
    }
    //el resto de los datos no se asigna a ningún cluster, lo que se representa
    //con -1...
    for (int i = C; i < assign.length; i++) {
      assign[i] = -1;
    }
    //...y lo vamos insertando uno a uno en el cluster que le corresponda
    for (int i = C; i < assign.length; i++) {
      insert(i);
    }
  }

  /**
   * <p> <b>Descripción:</b> agrupa las funciones de pertenencia del tipo en
   * clusters hasta obtener la agrupación óptima.
   *
   */
  public void apply() {
    do {
      setCluster();
    }
    while (reassign());
  }

  /**
   * <p> <b>Descripción:</b> inicializa el conjunto los <i>clusters</i>.
   *
   */
  private void setCluster() {
    //para todos los clusters en que se quieren agrupar las funciones de
    //pertenencia del tipo...
    for (int i = 0; i < cluster.length; i++) {
      int n = 0;
      //inicialmente no asigna ninguna función de pertenencia a ninguna
      //cluster...
      for (int k = 0; k < cluster[i].length; k++) {
        cluster[i][k] = 0;
      }

      for (int j = 0; j < data.length; j++) {
        if (assign[j] == i) {
          for (int k = 0; k < cluster[i].length; k++) {
            cluster[i][k] += data[j][k];
          }

          n++;
        }
      }

      if (n > 0) {
        for (int k = 0; k < cluster[i].length; k++) {
          cluster[i][k] /= n;
        }
      }
    }
  }

  /**
   * <p> <b>Descripción:</b> reasigna los datos a los <i>clusters</i>
   * disponibles.
   *
   */
  private boolean reassign() {
    IXfspDistance d = new XfspEuclideanDistance(weights);
    boolean change = false;

    for (int i = 0; i < data.length; i++) {
      int sel = 0;
      double min = d.distance(data, cluster, i, 0);

      for (int j = 1; j < cluster.length; j++) {
        double dist = d.distance(data, cluster, i, j);

        if (min > dist) {
          min = dist;
          sel = j;
        }
      }

      if (assign[i] != sel) {
        assign[i] = sel;
        change = true;
      }
    }

    return change;
  }

  /**
   * <p> <b>Descripción:</b> inserta un dato en un <i>clusters</i>.
   * @param n Posición ocupada por el dato a insertar en el array de datos.
   *
   */
  private void insert(int n) {
    IXfspDistance d = new XfspEuclideanDistance(weights);
    double dist_new;
    double dist_cl1;
    double min;
    int sel_new = 0;
    int sel_cl1 = 0;
    int sel_cl2 = 0;
    setCluster();
    min = Double.MAX_VALUE;

    for (int i = 0; i < cluster.length; i++) {
      double dist = d.distance(data, cluster, n, i);

      if (dist < min) {
        min = dist;
        sel_new = i;
      }
    }

    dist_new = min;
    min = Double.MAX_VALUE;

    for (int i = 0; i < cluster.length; i++) {
      for (int j = i + 1; j < cluster.length; j++) {
        double dist = d.distance(cluster, cluster, i, j);

        if (dist < min) {
          min = dist;
          sel_cl1 = i;
          sel_cl2 = j;
        }
      }
    }

    dist_cl1 = min;

    if (dist_new < dist_cl1) {
      assign[n] = sel_new;
    }
    else {
      for (int i = 0; i < n; i++) {
        if (assign[i] == sel_cl2) {
          assign[i] = sel_cl1;
        }

        assign[n] = sel_cl2;
      }
    }
  }
}
