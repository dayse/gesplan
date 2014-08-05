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
//		ALGORITMO RESILIENT PROPAGATION			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;

public class RProp extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double update;
 private double increase;
 private double decrease;
 private DerivativeOption derivative;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public RProp() {
  this.update = -1;
  this.increase = -1;
  this.decrease = -1;
  this.derivative = new DerivativeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return RPROP;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 3) throw new XflException(26);
  update = super.test(param[0], POSITIVE);
  increase = super.test(param[1], INCREASE);
  decrease = super.test(param[2], DECREASE);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  XfslAlgorithmParam[] pp = new XfslAlgorithmParam[3];
  pp[0] = new XfslAlgorithmParam(update, POSITIVE, "Initial Update");
  pp[1] = new XfslAlgorithmParam(increase, INCREASE, "Increase Factor");
  pp[2] = new XfslAlgorithmParam(decrease, DECREASE, "Decrease Factor");
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
  XfslEvaluation eval = null;
  Parameter[] param = spec.getAdjustable();

  if(init) {
   for(int i=0; i<param.length; i++) param[i].setMisc(update);
   init = false;
  }

  XfslEvaluation prev = derivative.compute(spec,pattern,ef);

  boolean backtracking = true;
  double prev_eval_error = 0;
  while(backtracking) {
   double[] delta = new double[param.length];
   for(int i=0; i<param.length; i++) {
    double signo = param[i].getPrevDeriv() * param[i].getDeriv();
    if( signo >0 ) delta[i] = param[i].getMisc()*increase;
    else if( signo <0 ) delta[i] = param[i].getMisc()*decrease;
    else delta[i] = param[i].getMisc();
    if(param[i].getDeriv()>0) param[i].setDesp(-delta[i]);
    else if(param[i].getDeriv()<0) param[i].setDesp(delta[i]);
    else param[i].setDesp(0);
   }
   spec.update();

   eval = ef.evaluate(spec,pattern,prev.error);
   // The second check (prev_eval_error == eval.error) avoids infinite loops when 
   // conditions are ill-posed and backtracking is not able to decrease the error.
   if ( (eval.error < prev.error) || (prev_eval_error == eval.error) ) {
       // if (prev_eval_error == eval.error) 
       //   System.out.println("Stopped before infinite loop");
       backtracking = false;
       for(int i=0; i<param.length; i++) param[i].setMisc(delta[i]);
   } else {
       for(int i=0; i<param.length; i++) {
	   param[i].value -= param[i].getPrevDesp();
	   param[i].setMisc(param[i].getMisc()*decrease);
       }
   }   
   prev_eval_error = eval.error;
  }

  for(int i=0; i<param.length; i++) param[i].forward();
  return eval;
 }
}
