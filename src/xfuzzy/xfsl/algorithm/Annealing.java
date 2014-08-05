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
//		ALGORITMO DE ENFRIAMIENTO SIMULADO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;
import java.util.Random;

public class Annealing extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double T0;
 private double E0;
 private int N;
 private AnnealingSchemeOption scheme;

 private double T;
 private double ratio;
 private int iter;
 private int n;
 private XfslEvaluation last;
 private Random random;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public Annealing() {
  this.T0 = -1;
  this.E0 = -1;
  this.N = -1;
  this.random = new Random();
  this.scheme = new AnnealingSchemeOption();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return ANNEALING;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 3) throw new XflException(26);
  T0 = test(param[0], POSITIVE);
  E0 = test(param[1], POSITIVE);
  N = (int) param[2]; if(N<=0) throw new XflException(26);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  XfslAlgorithmParam[] pp = new XfslAlgorithmParam[3];
  pp[0] = new XfslAlgorithmParam(T0, POSITIVE, "Initial Temperature");
  pp[1] = new XfslAlgorithmParam(E0, POSITIVE, "Initial Perturbation");
  pp[2] = new XfslAlgorithmParam(N, INTEGER, "Iter. per Temp.");
  return pp;
 }

 //-------------------------------------------------------------//
 // Obtiene las opciones de configuracion del algoritmo         //
 //-------------------------------------------------------------//

 public XfslAlgorithmOption[] getOptions() {
  XfslAlgorithmOption[] opt = new XfslAlgorithmOption[1];
  opt[0] = scheme;
  return opt;
 }

 //-------------------------------------------------------------//
 // Ejecuta una iteracion del algoritmo                         //
 //-------------------------------------------------------------//

 public XfslEvaluation iteration(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef) throws XflException {
  if(init) { 
   init = false;
   last = ef.evaluate(spec,pattern,1.0);
   iter = 0;
   n = 0;
   ratio = E0 / T0;
   T = T0;
  } 
  else n++;

  Parameter[] param = spec.getAdjustable();
  if(n==N) { n=0; iter++; cooling(param.length); }
  double[] p = new double[param.length];
  for(int i=0; i<param.length; i++) p[i] = param[i].value;
  perturbation(spec);
  XfslEvaluation eval = ef.evaluate(spec,pattern,last.error);
  boolean metropolis = false;
  double inc = eval.error - last.error;
  if(inc>0 && Math.exp(-inc/T)<random.nextDouble())
    for(int i=0; i<param.length; i++) param[i].value = p[i];
  else metropolis = true;

  if(!metropolis) {
   eval = new XfslEvaluation();
   eval.error = last.error;
   eval.rmse = last.rmse;
   eval.mxae = last.mxae;
   eval.var = 0;
  }

  last = eval;
  return eval;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Realiza una perturbacion de los parametros del sistema	//
 //-------------------------------------------------------------//

 private void perturbation(Specification spec) {
  Type[] type = spec.getTypes();
  for(int i=0; i<type.length; i++) {
   if(!type[i].isAdjustable()) continue;

   Family[] fam = type[i].getFamilies();
   for(int j=0; j<fam.length; j++) {
    if(!fam[j].isAdjustable()) continue;
    Parameter[] pp = fam[j].getParameters();
    double[] val = fam[j].get();
    do {
     for(int k=0; k<pp.length; k++) {
      if(!pp[k].isAdjustable()) continue;
      pp[k].value = val[k] + perturbation();
     }
    } while(!fam[j].test());
   }

   ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
   for(int j=0; j<mf.length; j++) {
    if(!mf[j].isAdjustable()) continue;
    Parameter[] pp = mf[j].getParameters();
    double[] val = mf[j].get();
    do {
     for(int k=0; k<pp.length; k++) {
      if(!pp[k].isAdjustable()) continue;
      pp[k].value = val[k] + perturbation();
     }
    } while(!mf[j].test());
   }
  }
 }

 //-------------------------------------------------------------//
 // Actualiza la temperatura siguiendo el esquema seleccionado	//
 //-------------------------------------------------------------//

 private void cooling(int D) {
  switch(scheme.cooling) {
   case AnnealingSchemeOption.LINEAL:
    if(iter<=scheme.steps) T=T0*(scheme.steps-iter)/scheme.steps;
    else T=0;
    break;
   case AnnealingSchemeOption.EXPONENTIAL:
    T = T*scheme.decrease;
    break;
   case AnnealingSchemeOption.CLASSICAL:
    T = T0/(1+Math.log(1+iter));
    break;
   case AnnealingSchemeOption.FAST:
    T = T0/(1+iter);
    break;
   case AnnealingSchemeOption.ADAPTIVE:
    T = T*Math.exp(scheme.asa*Math.pow(iter,1/D));
    break;
  } 
 }

 //-------------------------------------------------------------//
 // Calcula una perturbacion siguiendo el esquema seleccionado	//
 //-------------------------------------------------------------//

 private double perturbation() {
  switch(scheme.cooling) {
   case AnnealingSchemeOption.LINEAL:
    return ratio*T*(random.nextDouble()-0.5);
   case AnnealingSchemeOption.EXPONENTIAL:
    return ratio*T*(random.nextDouble()-0.5);
   case AnnealingSchemeOption.CLASSICAL:
    return ratio*T*random.nextGaussian();
   case AnnealingSchemeOption.FAST:
    return ratio*T*Math.tan(Math.PI*random.nextDouble());
   case AnnealingSchemeOption.ADAPTIVE:
    double u = random.nextDouble();
    double E = ratio*T;
    double e,s;
    if(u<0.5) { s = -1; e = 1-2*u; } else { s = 1; e = 2*u-1; }
    return s*E*(Math.pow(1+1/E,e)-1);
   default:
    return 0;
  }
 }
}

