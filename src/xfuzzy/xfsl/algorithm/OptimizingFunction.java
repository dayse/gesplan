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
//	 CLASE QUE PERMITE CALCULAR LA MINIMIZACION LINEAL	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.lang.*;
import xfuzzy.xfsl.*;

public class OptimizingFunction {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private static final double GOLD = 1.618034;
 private static final double GLIMIT = 100;
 private static final double TINY = 1.0e-20;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private Specification spec;
 private XfslPattern pattern;
 private XfslErrorFunction ef;

 private Parameter[] param;
 private double[] val;
 private double[] p;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public OptimizingFunction(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef) {
  this.spec = spec;
  this.pattern = pattern;
  this.ef = ef;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Optimizacion lineal en la direccion del vector p		//
 //-------------------------------------------------------------//

 public XfslEvaluation linmin(double[] p, XfslEvaluation prev, double tol,
 int limit) throws XflException {

  this.param = spec.getAdjustable();
  this.p = p;
  double[] brkpt = new double[3];
  XfslEvaluation[] brkev = new XfslEvaluation[3];

  this.val = new double[param.length];
  for(int i=0; i<param.length; i++) val[i] = param[i].value;

  minbracket(prev,brkpt,brkev);

  if(brkev[0].error == brkev[1].error && brkev[1].error == brkev[2].error) {
   for(int i=0; i<param.length; i++) param[i].value = val[i];
   XfslEvaluation ev = brkev[0];
   ev.var = 0;
   return ev;
  }

  XfslEvaluation eval = quad(brkpt,brkev,tol,limit);
  eval.var = (prev.error - eval.error)/prev.error;
  return eval;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Acota un intervalo que contenga un minimo local		//
 //-------------------------------------------------------------//

 private void minbracket(XfslEvaluation prev, double[] pos,
 XfslEvaluation[] ev) {
  double ax=0.0, bx=1.0;
  XfslEvaluation fa = prev;
  XfslEvaluation fb = evaluate(bx);
  if(fb.error == fa.error) { bx = -1.0; fb = evaluate(bx); }
  if(fb.error > fa.error) { ax=bx; bx=0; fa=fb; fb=prev; }
  double cx = bx+GOLD*(bx-ax);
  XfslEvaluation fc = evaluate(cx);
  XfslEvaluation fu = null;
  while(fb.error > fc.error) {
   double r = (bx-ax)*(fb.error-fc.error);
   double q = (bx-cx)*(fb.error-fa.error);
   double q_r = q-r;
   if(q_r>0 && q_r<TINY) q_r = TINY;
   if(q_r<0 && q_r>-TINY) q_r = -TINY;
   double u = bx - ((bx-cx)*q - (bx-ax)*r)/(2*q_r);
   double ulim = bx + GLIMIT*(cx-bx);
   if((bx-u)*(u-cx)>0) {
    fu = evaluate(u);
    if(fu.error < fc.error) {
     ax = bx; fa = fb;
     bx = u; fb = fu;
     break;
    } else if(fu.error > fb.error) {
     cx = u; fc = fu;
     break;
    }
    u = cx + GOLD*(cx-bx);
    fu = evaluate(u);
   } else if((cx-u)*(u-ulim) > 0) {
    fu = evaluate(u);
    if(fu.error < fc.error) {
     bx = cx; fb = fc;
     cx = u; fc = fu;
     u = cx + GOLD*(cx-bx);
     fu = evaluate(u);
    }
   } else if((u-ulim)*(ulim-cx) >= 0) {
    u = ulim;
    fu = evaluate(u);
   } else {
    u = cx + GOLD*(cx-bx);
    fu = evaluate(u);
   }
   ax = bx; fa = fb;
   bx = cx; fb = fc;
   cx = u; fc = fu;
  }

  pos[0] = ax; ev[0] = fa;
  pos[1] = bx; ev[1] = fb;
  pos[2] = cx; ev[2] = fc;
 }

 //-------------------------------------------------------------//
 // Interpolacion cuadratica para reducir el intervalo		//
 //-------------------------------------------------------------//

 private XfslEvaluation quad(double[] brkpt, XfslEvaluation[] brkev,
 double tol, int limit) {
  double u, x, v, alpha;
  XfslEvaluation fu, fx, fv, fa=null;
  int minpt = (brkpt[0]<=brkpt[2]? 0 : 2);
  int maxpt = (brkpt[0]<=brkpt[2]? 2 : 0);
  u = brkpt[minpt]; fu = brkev[minpt];
  v = brkpt[maxpt]; fv = brkev[maxpt];
  x = brkpt[1]; fx = brkev[1];

  for(int trials=0; trials<limit; trials++) {
   double xm = 0.5*(u+v);
   double tol1 = tol*(x>0? x : -x)+1.0e-10;
   double tol2 = 2.0*tol1;
   double dist = (x>xm? x-xm : xm-x);
   if(dist <= (tol2-0.5*(v-u))) break;

   double s = (v-x)*(v-x)*(fu.error-fx.error)-(u-x)*(u-x)*(fv.error-fx.error);
   double q = -2*( (u-x)*(fv.error-fx.error) - (v-x)*(fu.error-fx.error) );
   if(s==0 || q==0) alpha = x+(v-x)/10; else alpha = x + s/q;
   fa = evaluate(alpha);
   if(alpha < x && fa.error > fx.error) { u=alpha; fu=fa; }
   if(alpha < x && fa.error < fx.error) { v=x; fv=fx; x=alpha; fx=fa; }
   if(alpha > x && fa.error > fx.error) { v=alpha; fv=fa; }
   if(alpha > x && fa.error < fx.error) { u=x; fu=fx; x=alpha; fx=fa; }
  }
  if(fa != null && fa.error <= fx.error) return fa;
  for(int i=0; i<param.length; i++) param[i].value = val[i];
  for(int i=0; i<param.length; i++) param[i].setDesp(x*p[i]);
  spec.update();
  return fx;
 }

 //-------------------------------------------------------------//
 // Calcula el valor de la funcion desplazada en u*p[]		//
 //-------------------------------------------------------------//

 private XfslEvaluation evaluate(double u) {
  for(int i=0; i<param.length; i++) param[i].value = val[i];
  for(int i=0; i<param.length; i++) param[i].setDesp(u*p[i]);
  spec.update();
  return ef.evaluate(spec,pattern,1.0);
 }
}
