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

public class Conjugate extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double[] last_p;
 private int iter;
 private double tol;
 private int limit;
 private ConjugateMethodOption method;
 private DerivativeOption derivative;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public Conjugate() {
  this.iter = 0;
  this.tol = -1;
  this.limit = -1;
  this.method = new ConjugateMethodOption();
  this.derivative = new DerivativeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return CONJUGATE;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 2) throw new XflException(26);
  tol = test(param[0], REDUCE);
  limit = (int) test(param[1], INTEGER);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  XfslAlgorithmParam[] pp = new XfslAlgorithmParam[2];
  pp[0] = new XfslAlgorithmParam(tol, REDUCE, "Line-search Tolerance");
  pp[1] = new XfslAlgorithmParam(limit, INTEGER, "Search Iteration Limit");
  return pp;
 }

 //-------------------------------------------------------------//
 // Obtiene las opciones de configuracion del algoritmo         //
 //-------------------------------------------------------------//

 public XfslAlgorithmOption[] getOptions() {
  XfslAlgorithmOption[] opt = new XfslAlgorithmOption[2];
  opt[0] = method;
  opt[1] = derivative;
  return opt;
 }

 //-------------------------------------------------------------//
 // Ejecuta una iteracion del algoritmo                         //
 //-------------------------------------------------------------//

 public XfslEvaluation iteration(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef) throws XflException {
  XfslEvaluation prev = derivative.compute(spec,pattern,ef);
  OptimizingFunction function = new OptimizingFunction(spec,pattern,ef);
  Parameter[] param = spec.getAdjustable();
  double[] pt = new double[param.length];
  for(int i=0; i<param.length; i++) pt[i] = param[i].value;
  boolean reset = false;

  if(init) { init = false; iter = 0; reset = true;} else iter++; 
  if(iter == param.length) { reset = true; iter = 0; }

  double[] g = new double[param.length];
  for(int i=0; i<param.length; i++) g[i] = -param[i].getDeriv();
  double[] p;
  if(reset) p = g; else p = searchDirection(param,last_p);

  XfslEvaluation eval = function.linmin(p,prev,tol,limit);

  for(int i=0; i<param.length; i++) {
   param[i].setPrevDesp(param[i].value - pt[i]);
   param[i].setPrevDeriv(-g[i]);
   param[i].setDeriv(0);
  }

  last_p = p;
  return eval;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Calcula la direccion conjugada				//
 //-------------------------------------------------------------//

 private double[] searchDirection(Parameter[] param, double[] p) {
  switch(method.beta) {
   case ConjugateMethodOption.POLAK: return Polak_Ribiere(param,p);
   case ConjugateMethodOption.FLETCHER: return Fletcher_Reeves(param,p);
   case ConjugateMethodOption.HESTENES: return Hestenes_Stiefel(param,p);
   default: return One_step_secant(param);
  }
 }

 //-------------------------------------------------------------//
 // Direccion conjugada de Polak-Ribiere 			//
 //-------------------------------------------------------------//

 private double[] Polak_Ribiere(Parameter[] param, double[] p) {
  double[] d = new double[param.length];
  double num=0, den=0;
  for(int i=0; i<param.length; i++) {
   double g = param[i].getDeriv();
   double pg = param[i].getPrevDeriv();
   num += (g-pg)*g;
   den += pg*pg;
  }
  double B = num/den;
  for(int i=0; i<param.length; i++) d[i] = -param[i].getDeriv() + B*p[i];
  return d;
 }

 //-------------------------------------------------------------//
 // Direccion conjugada de Fletcher-Reeves 			//
 //-------------------------------------------------------------//

 private double[] Fletcher_Reeves(Parameter[] param, double[] p) {
  double[] d = new double[param.length];
  double num=0, den=0;
  for(int i=0; i<param.length; i++) {
   double g = param[i].getDeriv();
   double pg = param[i].getPrevDeriv();
   num += g*g;
   den += pg*pg;
  }
  double B = num/den;
  for(int i=0; i<param.length; i++) d[i] = -param[i].getDeriv() + B*p[i];
  return d;
 }

 //-------------------------------------------------------------//
 // Direccion conjugada de Hestenes-Stiefel 			//
 //-------------------------------------------------------------//

 private double[] Hestenes_Stiefel(Parameter[] param,double[] p) {
  double[] d = new double[param.length];
  double num=0, den=0;
  for(int i=0; i<param.length; i++) {
   double g = param[i].getDeriv();
   double y = g - param[i].getPrevDeriv();
   num += y*g;
   den += y*p[i];
  }
  double B = (den == 0? 0 : num/den);
  for(int i=0; i<param.length; i++) d[i] = -param[i].getDeriv() + B*p[i];
  return d;
 }

 //-------------------------------------------------------------//
 // Direccion conjugada del metodo One_step_secant		//
 //-------------------------------------------------------------//

 private double[] One_step_secant(Parameter[] param) {
  double[] d = new double[param.length];
  double yy=0, sy=0, sg=0, yg=0;
  for(int i=0; i<param.length; i++) {
   double g = param[i].getDeriv();
   double y = g - param[i].getPrevDeriv();
   double s = param[i].getPrevDesp();
   yy += y*y;
   sy += s*y;
   sg += s*g;
   yg += y*g;
  }
  double A = -(1 + yy/sy)*sg/sy + yg/sy;
  double B = sg/sy;
  for(int i=0; i<param.length; i++) {
   double g = param[i].getDeriv();
   double y = g - param[i].getPrevDeriv();
   double s = param[i].getPrevDesp();
   d[i] = -g + A*s + B*y;
  }
  return d;
 }
}

