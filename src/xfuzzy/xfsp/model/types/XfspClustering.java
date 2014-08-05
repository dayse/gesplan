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

import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.Type;
import xfuzzy.lang.Specification;

/**
 * <p> <b>Descripción:</b> simplificador de tipos de sistemas difusos por el
 * método de <i>clustering</i> de las funciones de pertenencia del tipo en
 * cuestión.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see IXfspTypeSimplifier
 * @see XfspTypeSimplifierFactory
 *
 */
public class XfspClustering
    implements IXfspTypeSimplifier {

  //número de clusters en que se agrupan los datos (por defecto se iniciliza a
  //-1)
  private int clusters = -1;

  //pesos que se le debe asignar a cada uno de los elementos de cada dato
  private double[] weights;

  /**
   * <p> <b>Descripción:</b> crea un simplificador de tipos por clustering de
   * las funciones de pertenencia.
   * @param num Número de clusters en que se deben agrupar los datos.
   * @param weights Pesos que se debe asignar a cada uno de los elementos de
   * los datos
   *
   */
  public XfspClustering(int num, double[] weights) {
    //establece el número de clusters en que se deben agrupar los datos
    this.clusters = num;
    //establece los pesos que hay que asignar a los elementos de cada dato
    this.weights = weights;
  }

  /**
   * <p> <b>Descripción:</b> simplifica un tipo y actualiza la especificación
   * de un sistema difuso para que refleje la simplificación realizada.
   * @param type Tipo que debe ser simplificado.
   * @param spec Especificación de un sistema difuso que debe ser actualizada
   * tras la simplificación del tipo.
   *
   */
  public void simplify(Type type, Specification spec) {
    if (reducible(type)) {
      //conjunto de datos de las funciones de pertenencia del tipo a simplificar
      double[][] data = null;
      //inicializa los clusters del tipo
      data = setupClusters(type);
      //simplifica el tipo y actualiza la especificación
      simplifyType(type, spec, data, clusters);
    }
  }

  /**
   * <p> <b>Descripción:</b> determina si un tipo es reducible mediante
   * <i>clustering</i>.
   * @param type Tipo que se quiere simplificar.
   * @return Devuelve cierto si el tipo es reducible por <i>clustering</i> y
   * falso en caso contrario.
   *
   */
  private boolean reducible(Type type) {
    //por defecto el tipo es reducible...
    boolean res = true;
    //...y busca los casos en los que no lo sea
    //obtiene todas las funciones de pertenencia del tipo
    LinguisticLabel[] mf = type.getMembershipFunctions();
    //si el tipo no tiene funciones de pertenencia...
    if (mf.length == 0) {
      //...no es reducible
      res = false;
    }
    //si la primera fiuncion de pertenencia no es parametrica...
    else if ( !(mf[0] instanceof ParamMemFunc) ) {
      //...no es reducible
      res = false;
    }
    //en otro caso...
    else {
      //obtiene el nombre de la primera función de pertenencia del tipo...
      String mfclass = mf[0].getClass().getName();
      //...comprueba que todas las funciones de pertenencia del tipo son
      //iguales...
      for (int i = 0; i < mf.length; i++) {
        //...y si alguna no lo es...
        if (!mf[i].getClass().getName().equals(mfclass)) {
          //...entonces el tipo no es reducible
          res = false;
        }
      }
    }
    //devuelve el resultado de todas las comprobaciones anteriores
    return res;
  }

  /**
   * <p> <b>Descripción:</b> calcula el conjunto de datos iniciales para el
   * proceso de clustering.
   * @param type Tipo que se va a simplificar mediante clustering.
   * @return Conjunto de datos que se emplearán durante el proceso de
   * clustering.
   *
   */
  private double[][] setupClusters(Type type) {
    //obtiene todas las funciones de pertenencia del tipo que se va a
    //simplificar
    ParamMemFunc[] mf = type.getParamMembershipFunctions();
    //calcula el número de datos que maneja cada una de las funciones de
    //pertenencia
    double[][] data = new double[mf.length][];
    //para cada una de las funciones de pertenencia del tipo a simplificar...
    for (int i = 0; i < mf.length; i++) {
      //...obtiene el conjunto de datos y lo almacena en el elemento
      //correspondiente del array de datos
      data[i] = mf[i].get();
    }
    //devuelve un array con todos los datos de todas las funciones de
    //pertenencia del tipo a simplificar
    return data;
  }

  /**
   * <p> <b>Descripción:</b> simplifica un tipo y sustituye el tipo
   * simplificado por el nuevo en el sistema al que pertence.
   * @param type Tipo que se va a simplificar mediante clustering.
   * @param spec Especificacion del sistema al que pertenece el tipo que se va
   * a simplificar.
   * @param data
   * @param C Número de <i>clusters</i> en que se agruparán las funciones de
   * pertenencia del tipo a simplificar.
   *
   */
  private void simplifyType(Type type, Specification spec, double[][] data,
                            int C) {
    //obtiene las funciones de pertenencia del tipo
    ParamMemFunc[] omf = type.getParamMembershipFunctions();
    //obtiene la clase a que pertenecen las funciones de pertenencia del tipo
    Class mfclass = omf[0].getClass();
    //array de funciones de pertenencia para el tipo simplificado
    ParamMemFunc[] pmf = new ParamMemFunc[C];
    //crea un nuevo objeto simplificador de tipos por clustering para el
    //número de clusters en que se quieren agrupar las funciones de pertenencia
    //del tipo a simplificar
    XfspCluster c = new XfspCluster(data, C, weights);
    //aplica el proceso de clustering
    c.apply();
    //para todos los clusters en que se agruparán las funciones de pertenencia
    //del tipo...
    for (int i = 0; i < C; i++) {
      try {
        //...crea una nueva instancia de función de pertenencia del tipo
        //original...
        pmf[i] = (ParamMemFunc) mfclass.newInstance();
        //...le asigna un nombre...
        pmf[i].set("mf" + i, type.getUniverse());
        //...y le asigna los parámetros
        pmf[i].set(c.cluster[i]);
      }
      catch (Exception e) {
      }
    }
    //para todas las funciones de pertenencia del tipo original...
    for (int j = 0; j < omf.length; j++) {
      //...la sustituye por la nueva
      spec.exchange(omf[j], pmf[c.assign[j]]);
    }
    //establece el nuevo conjunto de funciones de pertencia para el tipo
    //simplificado
    type.setMembershipFunctions(pmf);
  }
}
