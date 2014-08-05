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
import xfuzzy.lang.Specification;
import xfuzzy.lang.Type;
import xfuzzy.lang.Universe;

import xfuzzy.xfsp.controller.XfspStore;

/**
 * <p> <b>Descripción:</b> simplificador de tipos de sistemas difusos por el
 * método del cálculo del grado de similaridad de las funciones de pertenencia
 * de dicho sistema.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.7
 * @see IXfspTypeSimplifier
 * @see XfspTypeSimplifierFactory
 *
 */
public class XfspSimilaritySimplifier
    extends Thread
    implements IXfspTypeSimplifier {     //, Runnable {

  //umbral que debe superar el grado de similaridad entre dos funciones de
  //pertenencia de un tipo para que sean fusionadas dichas funciones en una sola
  private double threshold;

  //objeto que calcula el grado de similaridad de dos funciones de pertenencia
  //de un tipo
  private IXfspSimilarity s;

  //número de resultado de la funsión de dos similares
  //private String functionName;

  //almacén donde se guardan los eventos para que sean procesados por algún
  //controlador
  private XfspStore store;

  /**
   * <p> <b>Descripción:</b> crea un simplificador de un tipo por el cálculo
   * del grado de similaridad de las funciones de pertenencia de dicho tipo.
   * @param threshold Umbral que debe superar el grado de similaridad entre dos
   * funciones de pertenencia a fin de que sean fusionadas dichas funciones en
   * una sola.
   * @param s Objeto que permite el cálculo del grado de similaridad de dos
   * funciones de pertenencia.
   *
   */
  public XfspSimilaritySimplifier(double threshold, IXfspSimilarity s) {
    //estable el umbral que debe superar el grado de similaridad de dos
    //funciones de pertenencia para su fusión
    this.threshold = threshold;
    //registra el objeto que permitirá el cálculo del grado de similaridad de
    //dos funciones de pertenencia
    this.s = s;
    //establece el nombre de la función resultado de la fusión de dos similares
    //functionName = null;
    //establece el almacén donde guardar los eventos
    store = null;
  }

  /**
   * <p> <b>Descripción:</b> establece el almacén donde se guardan los eventos
   * para que sean procesados por algún controlador.
   * @param store Almacén donde se guardarán los eventos para que sean
   * procesados por algún procesador.
   *
   */
  public void setStore(XfspStore store) {
    this.store = store;
  }

  /**
   * <p> <b>Descripción:</b> simplifica un tipo por el cálculo de los grados de
   * similaridad de las funciones de pertenencia de dicho tipo y actualiza la
   * especificación de un sistema difuso para que refleje dicha simplificación.
   * @param type Tipo que debe ser simplificado.
   * @param spec Especificación de un sistema difuso que debe ser actualizada
   * para reflejar la simplificación hecha al tipo.
   *
   */
  public void simplify(Type type, Specification spec) {
    //booleano que vale cierto si el grado de similaridad más alto entre dos
    //funciones de pertenencia no supera el umbral establecido
    boolean end = false;
    //grado de similaridad entre dos funciones de pertenencia
    double similarity = 0;
    //almacena temporalmente el grado de similaridad de dos conjuntos difusos
    double aux;
    //índice que apunta a la primera de las funciones de pertenencia con mejor
    //grado de similaridad
    int u = -1;
    //índice que aputna a la segunda de las funciones de pertenencia con mejor
    //grado de similaridad
    int v = -1;
    //conjunto de funciones de pertenencia de tipo paramétrico del tipo a
    //simplificar
    ParamMemFunc[] mf = type.getParamMembershipFunctions();
    //universo del tipo a simplificar
    Universe universe = type.getUniverse();
    //establece el universo del tipo a simplificar en el objeto que calcula el
    //grado de similaridad entre dos funciones de pertenencia
    s.setUniverse(universe);
    //mientras queden funciones de pertenencia por recorrer y el grado de
    //similaridad más alto entre dos funciones de pertenencia supere el umbral
    //establecido
    while (!end && (mf.length > 0)) {
      //pone a cero el grado de similaridad que servirá de referencia en el
      //siguiente bucle
      similarity = 0;
      //para toda pareja de funciones de pertenencia del tipo que hay que
      //simplificar sin repetir ninguna pareja...
      for (int i = 0; i < (mf.length - 1); i++) {
        for (int j = i + 1; j < mf.length; j++) {
          //calcula el grado de similaridad entre la pareja de funciones de
          //pertenencia
          aux = s.similarity(mf[i], mf[j]);
          //si dicho grado de similaridad es más alto que el máximo encontrado
          //hasta ahora...
          if (aux > similarity) {
            //...actualiza el valor máximo encontrado...
            similarity = aux;
            //...y actualiza los índices que apuntan a las dos funciones de
            //pertenencia más similares encontradas
            u = i;
            v = j;
          }
        }
      }
      //si el grado de similaridad más alto encontrado para una pareja de
      //funciones de pertenencia supera el umbral establecido...
      if (similarity > threshold) {
        //...obtiene una fábrica de objetos que fusionan funciones similares
        IXfspMFMergerFactory mfmergerFactory = XfspMFMergerFactory.getInstance();
        //obtiene un objeto que fusiona funciones similares de la fábrica
        IXfspMFMerger mfm = mfmergerFactory.create(mf[u].getFunctionName(),
            mf[v].getFunctionName(), similarity, this.store);
        //fusiona las dos funciones de pertenencia similares en una nueva
        ParamMemFunc mergedmf = mfm.merge(mf[u], mf[v], universe);


        //si se ha encontrado una función de pertenencia resultado de la fusión
        //de las dos funciones similares...
        if(mergedmf != null){
          //...sustituye cada una de las funciones de pertenencia de la pareja
          //por la nueva función fruto de la fusión de ambas
          spec.exchange(mf[u], mergedmf);
          spec.exchange(mf[v], mergedmf);
          //...y reduce el conjunto de funciones de pertenencia que se deben
          //analizar en siguientes iteraciones
          mf = reduce(mf, u, v, mergedmf);
        }
        //en otro caso...
        else{
          //...concluye el proceso de simplificación
          end = true;
        }
      }
      //si el grado de similaridad más alto para una pareja no supera el
      //umbral...
      else {
        //...no hace nada más y sale del bucle
        end = true;
      }
    }
    //establece el conjunto de funciones de pertenencia del tipo que se quiere
    //simplificar
    type.setMembershipFunctions(mf);
  }

  /**
   * <p> <b>Descripción:</b> reduce el conjunto de funciones de pertenencia de
   * un tipo cuando se han fusionado dos de sus funciones de pertenencia
   * sustituyendo éstas por el resultado de dicha fusión.
   * @param mfs Conjunto de funciones de pertenencia original de un tipo.
   * @param i Índice de la primera de las funciones de pertenencia similares.
   * @param j Índice de la segunda de las funciones de pertenencia similares.
   * @param mf Resultado de la fusión de las dos funciones de pertenencia
   * similares.
   * @return Conjunto de funciones de pertenencia resultado de eliminar las dos
   * funciones similares y sustituirlas por la fusión de ambas.
   *
   */
  private ParamMemFunc[] reduce(ParamMemFunc[] mfs, int i, int j,
                                ParamMemFunc mf) {
    //crea un array de funciones de pertenencia paramétricas con un función
    //menos que el original
    ParamMemFunc[] res = new ParamMemFunc[mfs.length - 1];
    //copia todas las funciones del original en el nuevo array excepto las
    //dos funciones similares, para la que se sustituye la primera por el
    //resultado de la fusion y se elimina la segunda
    for (int k = 0; k < (mfs.length - 1); k++) {
      if (k < i) {
        res[k] = mfs[k];
      }
      else if (k == i) {
        res[k] = mf;
      }
      else if (k > i && k < j) {
        res[k] = mfs[k];
      }
      else if (k > i && k >= j) {
        res[k] = mfs[k + 1];
      }
    }
    return res;
  }
}
