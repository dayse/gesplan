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
//		    ALGORITMO DE BUSQUEDA CIEGA			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;
import java.util.Random;

public class BlindSearch extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double pert;
 private XfslEvaluation last;
 private Random random;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public BlindSearch() {
  this.pert = -1;
  this.random = new Random();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return BLIND;
 }

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo     //
 //-------------------------------------------------------------//

 public void setParameters(double[] param) throws XflException {
  if(param.length != 1) throw new XflException(26);
  pert = test(param[0], POSITIVE);
 }

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo       //
 //-------------------------------------------------------------//

 public XfslAlgorithmParam[] getParams() {
  XfslAlgorithmParam[] pp = new XfslAlgorithmParam[1];
  pp[0] = new XfslAlgorithmParam(pert, POSITIVE, "Perturbation");
  return pp;
 }

 //-------------------------------------------------------------//
 // Obtiene las opciones de configuracion del algoritmo         //
 //-------------------------------------------------------------//

 public XfslAlgorithmOption[] getOptions() {
  return new XfslAlgorithmOption[0];
 }

 //-------------------------------------------------------------//
 // Ejecuta una iteracion del algoritmo                         //
 //-------------------------------------------------------------//

 public XfslEvaluation iteration(Specification spec, XfslPattern pattern,
 XfslErrorFunction ef) throws XflException {
  if(init) { init = false; last = ef.evaluate(spec,pattern,1.0); } 

  Parameter[] param = spec.getAdjustable();
  double[] p = new double[param.length];
  for(int i=0; i<param.length; i++) p[i] = param[i].value;

  XfslEvaluation eval=null;
  boolean found=false;
  for(int trial=0; trial<20; trial++) {
   perturb(spec);
   eval = ef.evaluate(spec,pattern,last.error);
   if(eval.error < last.error) { found=true; break; }
   for(int i=0; i<param.length; i++) param[i].value = p[i];
  }

  if(!found) {
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

