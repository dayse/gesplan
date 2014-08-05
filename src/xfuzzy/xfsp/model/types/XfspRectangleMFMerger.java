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
 * <p> <b>Descripción:</b> clase de objetos que fusionan dos funciones de
 * pertenencia de tipo Rectangle.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.6
 * @see IXfspMFMerger
 *
 */
public class XfspRectangleMFMerger
    implements IXfspMFMerger {

  /**
   * <p> <b>Descripción:</b> crea un objeto que fusiona funciones de
   * pertenencia tipo Rectangle.
   *
   */
  public XfspRectangleMFMerger() {
  }

  /**
   * <p> <b>Descripción:</b> une dos funciones de pertenencia de tipo Rectangle
   * e Isosceles de un sistema difuso obteniendo una nueva función de
   * pertenencia que sea similar a ambas.
   * @param mf1 Una función de pertenencia de un tipo de un sistema difuso.
   * @param mf2 Otra función de pertenencia de un tipo de un sistema difuso
   * (puede ser igual que la primera).
   * @param universe Universo de discurso común a las dos funciones de
   * pertenencia.
   * @return Nueva función de pertenencia correspondiente a la unión de las dos
   * funciones de pertenencia que se le pasan como parámetro.
   *
   */
  public ParamMemFunc merge(ParamMemFunc mf1, ParamMemFunc mf2,
                            Universe universe) {
    double[] param1 = mf1.get();
    double[] mergedparam = new double[param1.length];
    ParamMemFunc mergedmf = (ParamMemFunc) mf1.clone(universe);
    Parameter[] pm1 = mf1.getParameters();
    Parameter[] pm2 = mf2.getParameters();

    try {
      for (int i = 0; i < pm1.length; i++) {
        double param = 0;

        if (pm1[i].getName().equals("a")) {
          param = Math.min(pm1[i].value, pm2[i].value);
        }
        else if (pm1[i].getName().equals("b")) {
          param = Math.max(pm1[i].value, pm2[i].value);
        }
        mergedparam[i] = param;
      }
      mergedmf.set(mergedparam);
    }
    catch (XflException e) {
    }
    return mergedmf;
  }
}
