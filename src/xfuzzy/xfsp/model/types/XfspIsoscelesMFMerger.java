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
 * pertenencia de tipo Isosceles.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see IXfspMFMerger
 *
 */
public class XfspIsoscelesMFMerger
    implements IXfspMFMerger {

  /**
   * <p> <b>Descripción:</b> crea un objeto que fusiona funciones de
   * pertenencia tipo Isosceles.
   *
   */
  public XfspIsoscelesMFMerger() {
  }

  /**
   * <p> <b>Descripción:</b> une dos funciones de pertenencia de tipo Isosceles
   * de un sistema difuso obteniendo una nueva función de pertenencia que sea
   * similar a ambas.
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
    double a1=0.0, a2=0.0, a3, b1=0.0, b2=0.0, b3;
    ParamMemFunc mergedmf = (ParamMemFunc) mf1.clone(universe);
    Parameter[] pm1 = mf1.getParameters();
    Parameter[] pm2 = mf2.getParameters();

    try {
      for (int i = 0; i < pm1.length; i++) {
        if (pm1[i].getName().equals("a")) {
          //param = (mf1.parameter[i].value + mf2.parameter[i].value) / 2;
          //dist = Math.abs(mf1.parameter[i].value - mf2.parameter[i].value);
          a1 = pm1[i].value;
          a2 = pm2[i].value;
        }
        else if (pm1[i].getName().equals("b")) {
          //param = Math.max(mf1.parameter[i].value+(dist/2), mf2.parameter[i].value+(dist/2));
          b1 = pm1[i].value;
          b2 = pm2[i].value;
        }
        //mergedparam[i] = param;
      }
      a3 = (Math.min(a1-b1, a2-b2)+Math.max(a1+b1, a2+b2))/2;
      b3 = (Math.max(a1+b1, a2+b2)-Math.min(a1-b1, a2-b2))/2;
      mergedparam[0]=a3;
      mergedparam[1]=b3;
      mergedmf.set(mergedparam);
    }
    catch (XflException e) {
    }
    return mergedmf;
  }
}
