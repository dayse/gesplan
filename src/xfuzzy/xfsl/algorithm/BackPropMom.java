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

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//    ALGORITMO DE RETROPROPAGACION DE ERRORES CON MOMENTOS	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;

public class BackPropMom extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double rate;
 private double momentum;
 private DerivativeOption derivative;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public BackPropMom() {
  this.rate = -1;
  this.momentum = -1;
  this.derivative = new DerivativeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return BACKPROPMOM;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 2) throw new XflException(26);
  rate = super.test(param[0], POSITIVE);
  momentum = super.test(param[1], MOMENTUM);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  XfslAlgorithmParam[] pp = new XfslAlgorithmParam[2];
  pp[0] = new XfslAlgorithmParam(rate, POSITIVE, "Learning Rate");
  pp[1] = new XfslAlgorithmParam(momentum, MOMENTUM, "Momentum Factor");
  return pp;
 }

 //-------------------------------------------------------------//
 // Obtiene las opciones de configuracion del algoritmo         //
 //-------------------------------------------------------------//

 public XfslAlgorithmOption[] getOptions() {
  XfslAlgorithmOption[] opt = new XfslAlgorithmOption[1];
  opt[0] = derivative;
  return opt;
 }

 //-------------------------------------------------------------//
 // Ejecuta una iteracion del algoritmo                         //
 //-------------------------------------------------------------//

 public XfslEvaluation iteration(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef) throws XflException {

  XfslEvaluation prev = derivative.compute(spec,pattern,ef);
  Parameter[] param = spec.getAdjustable();
  for(int i=0; i<param.length; i++) {
   double desp = -rate*param[i].getDeriv()+momentum*param[i].getPrevDesp();
   param[i].setDesp(desp);
   param[i].setPrevDeriv(param[i].getDeriv());
   param[i].setDeriv(0);
  }

  spec.update();
  return ef.evaluate(spec,pattern,prev.error);
 }
}

