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
//		ALGORITMO DE DESCENSO POR SIMPLEX		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;
import java.util.Random;

public class Simplex extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double pert;
 private double inc;
 private double dec;
 private double[][] simplex;
 private double[] simsum;
 private XfslEvaluation[] simeval;
 private int ilo, ihi, inhi;
 private double lasterror;
 private Random random;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public Simplex() {
  this.pert = -1;
  this.inc = -1;
  this.dec = -1;
  this.random = new Random();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return SIMPLEX;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 3) throw new XflException(26);
  pert = test(param[0], POSITIVE);
  inc = test(param[1], INCREASE);
  dec = test(param[2], DECREASE);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  XfslAlgorithmParam[] pp = new XfslAlgorithmParam[3];
  pp[0] = new XfslAlgorithmParam(pert, POSITIVE, "Perturbation");
  pp[1] = new XfslAlgorithmParam(inc, INCREASE, "Expansion Factor");
  pp[2] = new XfslAlgorithmParam(dec, DECREASE, "Contraction Factor");
  return pp;
 }

 //-------------------------------------------------------------//
 // Obtiene las opciones de configuracion del algoritmo         //
 //-------------------------------------------------------------//

 public XfslAlgorithmOption[] getOptions() {
  XfslAlgorithmOption[] opt = new XfslAlgorithmOption[0];
  return opt;
 }

 //-------------------------------------------------------------//
 // Ejecuta una iteracion del algoritmo                         //
 //-------------------------------------------------------------//

 public XfslEvaluation iteration(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef) throws XflException {
  Parameter[] param = spec.getAdjustable();
  if(init) {
   init = false;
   simplex = new double[param.length+1][param.length];
   simsum = new double[param.length];
   simeval = new XfslEvaluation[param.length+1];
   for(int j=0; j<param.length; j++) simplex[0][j] = param[j].value;
   for(int j=0; j<param.length; j++) simsum[j] += param[j].value;
   simeval[0] = ef.evaluate(spec,pattern,1.0);
   lasterror = simeval[0].error;
   for(int i=1; i<simplex.length; i++) {
    perturb(spec);
    for(int j=0; j<param.length; j++) simplex[i][j] = param[j].value;
    for(int j=0; j<param.length; j++) simsum[j] += param[j].value;
    simeval[i] = ef.evaluate(spec,pattern,lasterror);
    for(int j=0; j<param.length; j++) param[j].value = simplex[0][j];
   }
   getLimits();
   for(int j=0; j<param.length; j++) param[j].value = simplex[ilo][j];
   lasterror = simeval[ilo].error;
   return simeval[ilo];
  }

  XfslEvaluation simtry = amotry(spec,pattern,ef,param,ihi,-1.0);
  if(simtry.error <= simeval[ilo].error)
   simtry = amotry(spec,pattern,ef,param,ihi,inc);
  else if(simtry.error >= simeval[inhi].error) {
   XfslEvaluation simsave = simeval[ihi];
   simtry = amotry(spec,pattern,ef,param,ihi,dec);
   if(simtry.error >= simsave.error) {
    for(int i=0; i<simplex.length; i++) if(i != ilo) {
     for(int j=0; j<param.length; j++)
       param[j].setDesp(dec*(simplex[i][j] - simplex[ilo][j]));
     spec.update();
     simeval[i] = ef.evaluate(spec,pattern,1.0);
     for(int j=0; j<param.length; j++) simplex[i][j] = param[j].value;
     for(int j=0; j<param.length; j++) param[j].value = simplex[ilo][j];
    }
    for(int j=0; j<param.length; j++) {
     simsum[j] = 0;
     for(int i=0; i<simplex.length; i++) simsum[j] += simplex[i][j];
    }
   }
  }

  getLimits();
  for(int j=0; j<param.length; j++) param[j].value = simplex[ilo][j];
  simeval[ilo].var = (lasterror - simeval[ilo].error)/lasterror;
  lasterror = simeval[ilo].error;
  return simeval[ilo];
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Evalua una proyeccion de factor fac				//
 //-------------------------------------------------------------//

 private XfslEvaluation amotry(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef, Parameter[] param, int ihi, double fac) {
  double[] ptry = new double[ simplex[ihi].length ];
  double fac1 = (1.0 - fac)/simplex[ihi].length;
  double fac2 = fac1 - fac;
  for(int i=0; i<param.length; i++) {
   param[i].value = simplex[ihi][i];
   ptry[i] = simsum[i]*fac1 - simplex[ihi][i]*fac2; 
   param[i].setDesp(ptry[i] - simplex[ihi][i]);
  }
  spec.update();
  XfslEvaluation ytry = ef.evaluate(spec,pattern,1.0);
  if(ytry.error < simeval[ihi].error) {
   simeval[ihi] = ytry;
   for(int i=0; i<param.length; i++) {
    simsum[i] += param[i].value - simplex[ihi][i];
    simplex[ihi][i] = param[i].value;
   }
  }
  for(int i=0; i<param.length; i++) param[i].value = simplex[ilo][i];
  return ytry;
 }

 //-------------------------------------------------------------//
 // Actualiza los indices del mejor, peor y antepeor		//
 //-------------------------------------------------------------//

 private void getLimits() {
  ilo = 0;
  ihi = (simeval[0].error > simeval[1].error ? 0 : 1);
  inhi = (simeval[0].error > simeval[1].error ? 1 : 0);
  for(int i=0; i<simplex.length; i++) {
   if(simeval[i].error <= simeval[ilo].error) ilo = i;
   if(simeval[i].error > simeval[ihi].error) { inhi = ihi; ihi = i; }
   else if(simeval[i].error > simeval[inhi].error && i!=ihi) inhi = i;
  }
 }

 //-------------------------------------------------------------//
 // Realiza una perturbacion del sistema			//
 //-------------------------------------------------------------//

 private void perturb(Specification spec) {
  Type[] type = spec.getTypes();
  for(int i=0; i<type.length; i++) {
   if(!type[i].isAdjustable()) continue;

   Family fam[] = type[i].getFamilies();
   for(int j=0; j<fam.length; j++) {
    if(!fam[j].isAdjustable()) continue;
    Parameter[] pp = fam[j].getParameters();
    double[] val = fam[j].get();
    do {
     for(int k=0; k<pp.length; k++) {
      if(!pp[k].isAdjustable()) continue;
      pp[k].value = val[k] + pert*(random.nextDouble()-0.5);
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
      pp[k].value = val[k] + pert*(random.nextDouble()-0.5);
     }
    } while(!mf[j].test());
   }
  }
 }
}

