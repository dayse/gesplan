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
//	   	ALGORITMO GRADIENTE CONJUGADO			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;

public class Scaled extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private static final double SIGMA = 1.0e-4;
 private static final double LAMBDA = 1.0e-6;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double lambda;
 private double lambdab;
 private double[] lp;
 private int iter;
 private DerivativeOption derivative;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public Scaled() {
  this.derivative = new DerivativeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return SCALED;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 0) throw new XflException(26);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  return new XfslAlgorithmParam[0];
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
  XfslEvaluation eval=null;
  XfslEvaluation prev = derivative.compute(spec,pattern,ef);
  Parameter[] param = spec.getAdjustable();
  double[] val = new double[param.length];
  double[] g = new double[param.length];
  double[] lg = new double[param.length];
  double[] s = new double[param.length];
  double[] p;
  boolean reset = false;

  if(init) { init=false; iter=0; reset=true; lambda=LAMBDA; lambdab=0;}
  else iter++; 
  if(iter == param.length) { reset = true; iter = 0; }

  for(int i=0; i<param.length; i++) {
   g[i] = -param[i].getDeriv();
   lg[i] = -param[i].getPrevDeriv();
  }

  if(reset) p = g; else p = searchDirection(g,lg,lp);
     
  double module=0;
  for(int i=0; i<p.length; i++) module += p[i]*p[i];
  double sigma = SIGMA/Math.sqrt(module);
  for(int i=0; i<param.length; i++) val[i] = param[i].value;
  for(int i=0; i<param.length; i++) param[i].setDesp(sigma*p[i]);
  spec.update();
  for(int i=0; i<param.length; i++) param[i].setDeriv(0);
  derivative.compute(spec,pattern,ef);
  for(int i=0; i<param.length; i++) s[i]=(param[i].getDeriv() + g[i])/sigma;
  double delta=0;
  for(int i=0; i<p.length; i++) delta += p[i]*s[i];

  boolean success = false;
  while(!success) {
   delta += (lambda - lambdab)*module;
   if(delta<=0) {
    lambdab = 2*(lambda - delta/module);
    delta = -delta + lambda*module;
    lambda = lambdab;
   }
   double mu = 0;
   for(int i=0; i<p.length; i++) mu += p[i]*g[i];
   double alpha = mu/delta;

   for(int i=0; i<param.length; i++) param[i].value = val[i];
   for(int i=0; i<param.length; i++) param[i].setDesp(alpha*p[i]);
   spec.update();
   eval = ef.evaluate(spec,pattern,prev.error);

   double Delta = 2*delta*(prev.error - eval.error)/(mu*mu);
   if(Delta<0) lambdab = lambda;
   else { lambdab = 0; success = true; }
   if(Delta>=0.75) lambda = 0.25*lambda;
   if(Delta<0.25) lambda += delta*(1-Delta)/module;
  }

  for(int i=0; i<param.length; i++) {
   param[i].setPrevDeriv(-g[i]);
   param[i].setDeriv(0);
  }

  lp = p;
  return eval;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Calcula la direccion de busqueda				//
 //-------------------------------------------------------------//

 private double[] searchDirection(double[] g, double[] lg, double[] lp) {
  double mod1=0,mod2=0,mod3=0;
  for(int i=0; i<g.length; i++)
   { mod1+=g[i]*g[i]; mod2+=g[i]*lg[i]; mod3+=lp[i]*lg[i]; }
  double beta = (mod1 - mod2)/mod3;
  double[] p = new double[lp.length];
  for(int i=0; i<p.length; i++) p[i] = g[i] + beta*lp[i];
  return p;
 }
}

