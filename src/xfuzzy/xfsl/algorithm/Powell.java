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
//	   ALGORITMO DE POWELL (DIRECCIONES CONJUGADAS)		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;

public class Powell extends XfslAlgorithm {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double tol;
 private int limit;
 private int iter;
 private double[][] dirset;
 private XfslEvaluation lasteval;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTORES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public Powell() {
  this.tol = -1;
  this.limit = -1;
  this.iter = 0;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo          //
 //-------------------------------------------------------------//

 public int getCode() {
  return POWELL;
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
   iter = 0;
   dirset = new double[param.length][param.length];
   for(int i=0; i<dirset.length; i++) dirset[i][i] = 1.0;
   lasteval = ef.evaluate(spec,pattern,1.0);
  }

  iter++;
  if(iter == param.length) {
   iter = 0;
   for(int i=0; i<dirset.length; i++) dirset[i][i] = 1.0;
   for(int i=0; i<param.length-1; i++)
    for(int j=i+1; j<param.length; j++) {
     dirset[i][j] = 0.0;
     dirset[j][i] = 0.0;
    }
  }
   
  OptimizingFunction function = new OptimizingFunction(spec,pattern,ef);
  double[] pt = new double[param.length];
  for(int i=0; i<param.length; i++) pt[i] = param[i].value;

  XfslEvaluation neweval = lasteval;
  for(int i=0; i<dirset.length; i++) 
    neweval = function.linmin(dirset[i],neweval,tol,limit);
  for(int i=0; i<param.length-1; i++)
   for(int j=0; j<param.length; j++)
    dirset[i][j] = dirset[i+1][j];
  for(int i=0; i<param.length; i++)
    dirset[param.length-1][i] = param[i].value - pt[i];

  neweval = function.linmin(dirset[param.length-1],neweval,tol,limit);
  neweval.var = (lasteval.error - neweval.error)/lasteval.error;
  lasteval = neweval;

  return lasteval;
 }
}

