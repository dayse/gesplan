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
//	ALGORITMOS DE APROXIMACION ITERATIVA AL HESSIANO	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;

public class QuasiNewton extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double[][] lh;
 private double tol;
 private int limit;
 private QuasiNewtonMethodOption method;
 private DerivativeOption derivative;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public QuasiNewton() {
  this.tol = -1;
  this.limit = -1;
  this.method = new QuasiNewtonMethodOption();
  this.derivative = new DerivativeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return QUASI;
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
  double[][] h;

  if(init) { init=false; h=newHessian(param); }
  else h = (method.isBFGS()? BFGS(param) : DFP(param) );

  double[] g = new double[param.length];
  for(int i=0; i<param.length; i++) g[i] = -param[i].getDeriv();
  double[] p = product(h,g);

  XfslEvaluation eval = function.linmin(p,prev,tol,limit);

  for(int i=0; i<param.length; i++) {
   param[i].setPrevDesp(param[i].value - pt[i]);
   param[i].setPrevDeriv(-g[i]);
   param[i].setDeriv(0);
  }

  lh = h;
  return eval;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Inicializa el valor del hessiano				//
 //-------------------------------------------------------------//

 private double[][] newHessian(Parameter[] param) {
  double[][] h = new double[param.length][param.length];
  for(int i=0; i<param.length; i++) h[i][i] = 1.0;
  return h;
 }

 //-------------------------------------------------------------//
 // Multiplica el hessiano por el gradiente			//
 //-------------------------------------------------------------//

 private double[] product(double[][] h, double[] g) {
  double[] p = new double[h.length];
  for(int i=0; i<h.length; i++)
   for(int j=0; j<g.length; j++)
    p[i] += h[i][j]*g[j];
  return p;
 }

 //-------------------------------------------------------------//
 // Actualizacion del hessiano de Davidon-Fletcher-Powell	//
 //-------------------------------------------------------------//

 private double[][] DFP(Parameter[] param) {
  double[][] h = new double[param.length][param.length];
  double[] dx = new double[param.length];
  double[] dg = new double[param.length];
  for(int i=0; i<param.length; i++) {
   dx[i] = param[i].getPrevDesp();
   dg[i] = param[i].getDeriv() - param[i].getPrevDeriv();
  }
  double[] hg = product(lh,dg);
  double xg=0, ghg=0;
  for(int i=0; i<param.length; i++) xg += dx[i]*dg[i];
  for(int i=0; i<param.length; i++) ghg += dg[i]*hg[i];
  if(xg == 0 || ghg == 0) return newHessian(param);
  for(int i=0; i<param.length; i++)
   for(int j=0; j<param.length; j++)
    h[i][j] = lh[i][j] + dx[i]*dx[j]/xg - hg[i]*hg[j]/ghg;
  return h;
 }

 //-------------------------------------------------------------//
 // Actualizacion de Broyden-Fletcher-Goldfarb-Shanno		//
 //-------------------------------------------------------------//

 private double[][] BFGS(Parameter[] param) {
  double[] dx = new double[param.length];
  double[] dg = new double[param.length];
  double xg=0;
  for(int i=0; i<param.length; i++) {
   dx[i] = param[i].getPrevDesp();
   dg[i] = param[i].getDeriv() - param[i].getPrevDeriv();
   xg += dx[i]*dg[i];
  }
  if(xg == 0) return newHessian(param);

  double[] hg = product(lh,dg);
  double ghg=0;
  for(int i=0; i<param.length; i++) ghg += dg[i]*hg[i];

  double alpha = 1+ghg/xg;
  double[][] h = new double[param.length][param.length];
  for(int i=0; i<param.length; i++)
   for(int j=0; j<param.length; j++)
    h[i][j] = lh[i][j] + (alpha*dx[i]*dx[j] - dx[i]*hg[j] - dx[j]*hg[i])/xg;
  return h;
 }
}

