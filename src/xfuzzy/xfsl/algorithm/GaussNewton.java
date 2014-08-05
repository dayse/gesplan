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
//		    ALGORITMO DE GAUSS-NEWTON			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;

public class GaussNewton extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private DerivativeOption derivative;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public GaussNewton() {
  this.derivative = new DerivativeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return GAUSS;
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
  Parameter[] param = spec.getAdjustable();

  //+++++++++++++++++++++++++
  // crear jacobiano y error
  //+++++++++++++++++++++++++
  int N = pattern.input.length;
  int M = pattern.output[0].length;
  int P = param.length;
  double[][] jacobian = new double[M*N][P];
  double[] error = new double[M*N];

  //++++++++++++++++++++++++++++
  // calcular jacobiano y error
  //++++++++++++++++++++++++++++
  for(int i=0,n=0; i<N; i++) {
   double[] output = derivative.compute2(spec,pattern.input[i]);
   for(int p=0; p<P; p++)
    for(int o=0; o<M; o++)
     jacobian[n+o][p] = param[p].oderiv[o]/pattern.range[o];
   for(int o=0; o<M; o++)
    error[n+o] = (output[o] - pattern.output[n][o])/pattern.range[o];
   n+=M;
  }  

  //+++++++++++++++++++++++++++++++++
  // calcular el error total inicial
  //+++++++++++++++++++++++++++++++++
  double preverror = 0;
  for(int i=0; i<error.length; i++) preverror += error[i]*error[i];
  preverror /= error.length;

  //+++++++++++++++
  // actualizacion 
  //+++++++++++++++
  double[] desp = compute_desp(jacobian,error);
  for(int i=0; i<param.length; i++) param[i].setDesp(desp[i]);
  spec.update();
  return evaluate(spec,pattern,preverror);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Evalua el estado del sistema				//
 //-------------------------------------------------------------//

 private XfslEvaluation evaluate(Specification spec, XfslPattern pattern,
 double le) {
  SystemModule system = spec.getSystemModule();
  double mxae=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-pattern.output[p][i])/pattern.range[i];
    if(dev<0) dev = -dev;
    error += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
  }

  return new XfslEvaluation(error,error,mxae,le,pattern.input.length);
 }

 //-------------------------------------------------------------//
 // Calcula el desplazamiento de los parametros			//
 //-------------------------------------------------------------//

 private double[] compute_desp(double[][] jacobian, double[] error)
 throws XflException {
  int N = jacobian[0].length;
  double[][] hessian = new double[N][N];
  for(int i=0; i<N; i++) 
   for(int j=0; j<N; j++)
    for(int k=0; k<jacobian.length; k++)
     hessian[i][j] += jacobian[k][i]*jacobian[k][j];
  double[][] inverse = inverse(hessian);
  double[] gradient = new double[N];
  for(int i=0; i<N; i++) 
   for(int j=0; j<jacobian.length; j++)
    gradient[i] += jacobian[j][i]*error[j];

  double[] desp = new double[N];
  for(int i=0; i<N; i++)
   for(int j=0; j<N; j++)
    desp[i] -= inverse[i][j]*gradient[j];
  return desp;
 }

 //-------------------------------------------------------------//
 // Calcula la inversa de una matriz				//
 //-------------------------------------------------------------//

 private double[][] inverse(double[][] m) throws XflException {
  int N = m.length;
  double[][] y = new double[N][N];
  double[] col = new double[N];
  int[] indx = ludcmp(m);

  for(int j=0; j<N; j++) {
   for(int i=0; i<N; i++) col[i] = 0.0;
   col[j] = 1.0;
   lubksb(m,indx,col);
   for(int i=0; i<N; i++) y[i][j] = col[i];
  }
  return y;
 }

 //-------------------------------------------------------------//
 // Funcion auxiliar para el calculo de la inversa		//
 //-------------------------------------------------------------//

 private static void lubksb(double[][] a, int[] indx, double[] b) {
  int ii=-1;
  int N = a.length;
  for(int i=0; i<N; i++) {
   int ip=indx[i];
   double sum = b[ip];
   b[ip] = b[i];
   if(ii>=0) for(int j=ii; j<i;j++) sum -= a[i][j]*b[j];
   else if(sum != 0.0) ii=i;
   b[i] = sum;
  }
  for(int i=N-1; i>=0; i--) {
   double sum = b[i];
   for(int j=i+1; j<N; j++) sum -= a[i][j]*b[j];
   b[i] = sum/a[i][i];
  }
 }

 //-------------------------------------------------------------//
 // Funcion auxiliar para el calculo de la inversa		//
 //-------------------------------------------------------------//

 private static int[] ludcmp(double[][] a) throws XflException {
  int N = a.length;
  int[] indx = new int[N];
  double[] vv = new double[N];

  for(int i=0; i<N; i++) {
   double big = 0;
   for(int j=0; j<N; j++) if(Math.abs(a[i][j])>big) big = Math.abs(a[i][j]);
   if(big == 0.0) throw new XflException();
   vv[i] = 1/big;
  }
  for(int j=0; j<N; j++) {
   for(int i=0; i<j; i++) {
    double sum = a[i][j];
    for(int k=0; k<i; k++) sum -= a[i][k]*a[k][j];
    a[i][j] = sum;
   }
   double big = 0;
   int imax = 0;
   for(int i=j; i<N; i++) {
    double sum=a[i][j];
    for(int k=0; k<j; k++) sum -= a[i][k]*a[k][j];
    a[i][j] = sum;
    double dum = vv[i]*(sum>0?sum:-sum);
    if(dum>=big) { big = dum; imax = i; }
   }
   if(j!=imax) {
    for(int k=0; k<N; k++) {
     double dum=a[imax][k];
     a[imax][k] = a[j][k];
     a[j][k] = dum;
    }
    vv[imax] = vv[j];
   }
   indx[j] = imax;
   if(a[j][j] == 0.0) throw new XflException();
   for(int i=j+1; i<N; i++) a[i][j] /= a[j][j];
  }
  return indx;
 }
}
