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

import xfuzzy.lang.Parameter;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Universe;
import xfuzzy.lang.XflException;

/**
 * <p> <b>Descripción:</b> une dos funciones de pertenencia de tipo campana
 * obteniendo otra nueva también de tipo campana.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.2
 *
 */
public class XfspBellMFMerger
    implements IXfspMFMerger {
  public XfspBellMFMerger() {
  }

  /**
   * <p> <b>Descripción:</b> une dos funciones de pertenencia de tipo campana
   * y obtiene una nueva función de tipo campana.
   * @param mf1 Una función de pertenencia de tipo campana (<i>bell</i>).
   * @param mf2 Otra función de pertenencia de tipo campana.
   * @return Nueva función de pertenencia de tipo campana cuyo centro es
   * equidistante a los centros de las dos funciones originales y cuya anchura
   * es igual a la suma de la sexta parte de la diferencia en valor absoluto de
   * los centros de ambas funciones de pertenencia más la anchura máxima de las
   * dos funciones de pertenencia originales.
   *
   */
  public ParamMemFunc merge(ParamMemFunc mf1, ParamMemFunc mf2,
                            Universe universe) {
    //almacena la media (centro) de la primera función de pertenencia que se le
    //pasa como parámetro
    double mf1Mean = 0.0;
    //almacena la media (centro) de la segunda función de pertenencia que se le
    //pasa como parámetro
    double mf2Mean = 0.0;
    //vector de double que almacenará los parámetros para la nueva función de
    //pertenencia
    double[] mergedparam = new double[mf1.get().length]; //.length];
    //inicializa la nueva función de pertenencia como el clon de la primera que
    //pasa como parámetro
    ParamMemFunc mergedmf = (ParamMemFunc) mf1.clone(universe);
    Parameter[] pm1 = mf1.getParameters();
    Parameter[] pm2 = mf2.getParameters();
    try {
      //recorre todos los parámetros de las dos funciones de pertenencia
      //orginales
      for (int i = 0; i < mergedparam.length; i++) {
        //nuevo parámetro que será añadido a la nueva función de pertenencia
        double param = 0;
        //si se está tratando los centros de las campanas originales...
        if (pm1[i].getName().equals("a")) {
          //...el nuevo parámetro será igual a la media de los centros de las
          //funciones de pertenencia orginales
          param = (pm1[i].value + pm2[i].value) / 2;
          //se almacena la media de la primera de las funciones de
          //pertenencia...
          mf1Mean = pm1[i].value;
          //...y también de la segunda
          mf2Mean = pm2[i].value;
        }
        //...si se está tratando la desviación típica de las campanas
        //originales...
        else if (pm1[i].getName().equals("b")) {
          //...el nuevo parámetro es igual a la suma de las medias de las
          //campanas originales entre seis y el máximo de las desviaciones
          //típicas
          param = Math.abs( (mf1Mean - mf2Mean) / 6) +
              Math.max(pm1[i].value, pm2[i].value);
        }
        //añade el nuevo parámetro al vector de parámetros para la nueva
        //función de pertenencia
        mergedparam[i] = param;
      }
      //establece los parámetros de la nueva función de pertenencia
      mergedmf.set(mergedparam);
    }
    catch (XflException e) {
    }
    //devuelve la nueva función de pertenencia creada
    return mergedmf;
  }
}
