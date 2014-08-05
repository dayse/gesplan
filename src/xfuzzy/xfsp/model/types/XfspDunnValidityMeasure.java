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
 * <p> <b>Descripci�n:</b> clase com�n a todos los m�todos de validaci�n de
 * clusters mediante los �ndices de Dunn (1974).
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.3
 * @see IXfspTypeSimplifier
 * @see IXfspValidityMeasure
 * @see XfspTypeSimplifierFactory
 *
 */
public abstract class XfspDunnValidityMeasure
    implements IXfspValidityMeasure {

  //conjunto de datos para los que se quiere conocer el n�mero �ptimo de
  //clusters en que se pueden agrupar
  private double[][] data;

  //pesos asignados a los distintos elementos que tiene cada dato
  private double[] weights;

  /**
   * <p> <b>Descripci�n:</b> crea un medidor del �ndice de valoraci�n de
   * clusters seg�n el m�todo de Dunn.
   * @param data Conjunto de datos que se quieren agrupar en clusters y del que
   * se va a calcular el n�mero �ptimo de ellos.
   * @param weights Pesos asignados a cada uno de los elementos que compone un
   * data de los que se van a estudiar (como hayan sido definidos previamente).
   *
   */
  public XfspDunnValidityMeasure(double[][] data, double[] weights) {
    //indica los datos con los que va a trabajar
    this.data = data;
    //referencia los pesos que debe tener cada elemento de cada dato
    this.weights = weights;
  }

  /**
   * <p> <b>Descripci�n:</b> obtiene el n�mero �ptimo de clusters en que puede
   * agruparse el conjunto de datos con que trabaja el objeto medidor de �ndices
   * de valoraci�n.
   * @return Devuelve el n�mero �ptimo de clusters en que agrupar los datos
   * seg�n el m�todo de Dunn.
   *
   */
  public int getNumClusters() {
    //en principio el n�mero de clusters es igual al n�mero de datos con que
    //se cuenta de partida (lo que es equivalente a no agrupar en clusters)
    int num = data.length;
    //crea un objeto que agrupa datos en clusters con tantos como datos hay...
    XfspCluster c = new XfspCluster(data, data.length, weights);
    //...y evalua dicho objeto
    double max = evaluation(c);
    //para todos los posibles valores de clusters desde 2 (valor m�nimo) al
    //n�mero de datos con que cuenta menos uno (valor m�ximo)...
    for (int i = 2; i < data.length - 1; i++) {
      //...crea un objetoq que agrupa los datos en clusters con tantos clusters
      //como est� considerando en la iteraci�n actual...
      c = new XfspCluster(data, i, weights);
      //...y eval�a dicha agrupaci�n en clusters
      double eval = evaluation(c);
      //si el valor de la evaluaci�n es mejor que el que ten�a hasta el
      //momento...
      if (eval > max) {
        //...actualiza el mejor valor encontrado hasta ahora...
        max = eval;
        //...y el mejor n�mero de clusters en que se pueden agrupar los datos
        num = i;
      }
    }
    //devuelve el mejor n�mero de clusters en que se pueden agrupar los datos
    //que se haya encontrado
    return num;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el resultado de la evaluaci�n de las
   * asignaciones de los datos a los distintos clusters que se han hecho.
   * @param c Cluster que se quiere evaluar.
   * @return Valoraci�n de las asignaciones de los datos de partida a los
   * distintos clusters.
   *
   */
  private double evaluation(XfspCluster c) {
    //inicializa el valor m�ximo al menor que puede tomar
    double max = 0;
    //inicializa el valor m�nimo al mayor valor que puede tomar
    double min = Double.MAX_VALUE;

    //para cada pareja de clusters...
    for (int i = 0; i < c.cluster.length; i++) {
      //...sin repetir los c�lculos para ninguna pareja...
      for (int j = i + 1; j < c.cluster.length; j++) {
        //..calcula la distancia m�nima entre dicha pareja de clusters
        double dist = getNumerator(c.data, c.assign, weights, i, j);
        //si dicha distancia es menor que el valor m�nimo encontrado hasta
        //ahora...
        if (dist < min) {
          //...actualiza dicho valor m�nimo a la �ltima distancia calculada
          min = dist;
        }
      }
    }
    //para cada uno de los clusters en que se agrupan los datos...
    for (int i = 0; i < c.cluster.length; i++) {
      //...obtiene el valor del denominador para dicho cluster
      double dist = getDenominator(c.data, c.assign, c.cluster, weights, i);
      //si dicho valor es mayor que le m�ximo calculado hasta el momento...
      if (dist > max) {
        //...establece el nuevo m�ximo
        max = dist;
      }
    }
    return min / max;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el valor del numerador para cualquiera de
   * los m�todos de validaci�n derivados de Dunn.
   * @param data Conjunto de datos agrupables en <i>clusters</i>.
   * @param assign Asignaci�n hecha de los datos a los distintos
   * <i>clusters</i>.
   * @param weights Pesos asignados a los par�metros de las funciones de
   * pertenencia.
   * @param c1 N�mero del primer <i>cluster</i>.
   * @param c2 N�mero del segundo <i>cluster</i>.
   * @return Valoraci�n del numerador de los m�todos de validaci�n derivados de
   * Dunn.
   *
   */
  abstract protected double getNumerator(double[][] data, int[] assign,
                                         double[] weights, int c1, int c2);

  /**
   * <p> <b>Descripci�n:</b> devuelve el valor del denominador para cualquiera
   * de los m�todos de validaci�n derivados de Dunn.
   * @param data Conjunto de datos agrupables en <i>clusters</i>.
   * @param assign Asignaci�n hecha de los datos a los distintos
   * <i>clusters</i>.
   * @param weights Pesos asignados a los par�metros de las funciones de
   * pertenencia.
   * @param c N�mero del <i>cluster</i>.
   * @return Valoraci�n del denominador de los m�todos de validaci�n derivados
   * de Dunn.
   *
   */
  abstract protected double getDenominator(double[][] data, int[] assign,
                                           double[][] cluster, double[] weights,
                                           int c);
}
