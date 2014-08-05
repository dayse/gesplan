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
import xfuzzy.lang.Type;

import xfuzzy.xfsp.controller.XfspStore;

/**
 * <p> <b>Descripción:</b> construye simplificadores de tipos de sistemas difusos
 * según la especificación dada.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see XfspModel
 * @see XfspTypeSimplifier
 *
 */
public class XfspTypeSimplifierFactory
    implements IXfspTypeSimplifierFactory {

  //instancia de la fábrica de simplificadores de tipos que hace posible que en
  //el sistema haya solamente una instancia como máximo en todo momento
  static private XfspTypeSimplifierFactory instance = null;

  /**
   * <p> <b>Descripción:</b> crea una fábrica de simplificadores de tipos de
   * sistemas difusos.
   *
   */
  private XfspTypeSimplifierFactory() {
  }

  /**
   * <p> <b>Descripción:</b> devuelve una fábrica de simplificadores de tipos de
   * sistemas difusos. Dicha fábrica sólo se crea la primera vez que se llama
   * a este método y el resto de las veces se devuelve la misma instancia.
   * @return Devuelve una fábrica de simplificadores de tipos.
   *
   */
  static public XfspTypeSimplifierFactory getInstance() {
    //si no se ha creado todavía ninguna fábrica...
    if (instance == null) {
      //...crea una nueva
      instance = new XfspTypeSimplifierFactory();
    }
    //devuelve la fábrica de simplificadores de tipos que hay creada
    return instance;
  }

  /**
   * <p> <b>Descripción:</b> devuelve un objeto que simplifica tipos de
   * sistemas difusos.
   * @param method Método de simplificación que se debe emplear
   * @param args Argumentos que se le deben pasar a los simplificadores de
   * tipos.
   * @return Objeto que simplifica tipos de sistemas difusos.
   *
   */
  public IXfspTypeSimplifier create(String method, Object[] args) {
    IXfspTypeSimplifier s = null;

    if (method.equals("Elementary")) {
      s = new XfspElementarySimplifier();
    }
    else if (method.equals("Clustering")) {
      String validityMeasure = (String) args[1];
      int numClusters;

      double[] weights = new double[args.length - 6];
      for (int i = 4; i < args.length - 2; i++) {
        Double weight = (Double) args[i];
        weights[i - 4] = weight.doubleValue();
      }
      Type type = (Type) args[3];
      double[][] data = setupClusters(type);

      if (validityMeasure.equals("Custom")) {
        numClusters = ( (Integer) args[2]).intValue();
      }
      else {
        IXfspValidityMeasure vm = null;
        if (validityMeasure.equals("Davies&Bouldin")) {
          int t = ( (Integer) args[args.length - 2]).intValue();
          int q = ( (Integer) args[args.length - 1]).intValue();

          vm = new XfspDaviesBouldinValidityMeasure(data, weights, t, q);
        }
        else if (validityMeasure.equals("SeparationIndex")) {
          vm = new XfspSeparationIndex(data, weights);
        }
        else if (validityMeasure.equals("GeneralizedDunnIndex33")) {
          vm = new XfspGeneralizedDunnIndex33(data, weights);
        }
        else if (validityMeasure.equals("GeneralizedDunnIndex63")) {
          vm = new XfspGeneralizedDunnIndex63(data, weights);
        }
        numClusters = vm.getNumClusters();
      }
      s = new XfspClustering(numClusters, weights);
    }
    //si hay que simplificar el tipo por similaridad de funciones de
    //pertenencia...
    else if (method.equals("Similarity")) {
      Double threshold = (Double) args[0];
      IXfspSimilarity sim = new XfspDuboisPradeSimilarity();

      s = new XfspSimilaritySimplifier(threshold.doubleValue(), sim);

      ((XfspSimilaritySimplifier)s).setStore((XfspStore)args[1]);
    }
    //devuelve el simplificador de tipos que ha creado según los requisitos no
    //funcionales especificados
    return s;
  }

  /**
   * <p> <b>Descripción:</b> iniciliza el conjunto de clusters en que se
   * agrupan las funciones de pertenencia de un tipo.
   * @param type Tipo que se quiere simplificar por <i>clustering</i> y para el
   * que se ajustan los <i>clusters</i>.
   *
   */
  private double[][] setupClusters(Type type) {
    ParamMemFunc[] mf = type.getParamMembershipFunctions();
    double[][] data = new double[mf.length][];
    for (int i = 0; i < mf.length; i++) {
      data[i] = mf[i].get();
    }
    return data;
  }
}
