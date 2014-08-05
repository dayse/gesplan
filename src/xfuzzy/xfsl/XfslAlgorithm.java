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
//		    ALGORITMOS DE APRENDIZAJE			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;
import xfuzzy.xfsl.algorithm.*;

public abstract class XfslAlgorithm implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Codigos de los algoritmos programados			//
 //-------------------------------------------------------------//

 public static final int BACKPROP = 0;
 public static final int BACKPROPMOM = 1;
 public static final int ADAPTLEARNRATE = 2;
 public static final int ADAPTSTEPSIZE = 3;
 public static final int MANHATTAN = 4;
 public static final int QUICKPROP = 5;
 public static final int RPROP = 6;
 public static final int MARQUARDT = 7;
 public static final int GAUSS = 8;
 public static final int STEEPEST = 9;
 public static final int CONJUGATE = 10;
 public static final int SCALED = 11;
 public static final int QUASI = 12;
 public static final int ANNEALING = 13;
 public static final int BLIND = 14;
 public static final int SIMPLEX = 15;
 public static final int POWELL = 16;

 //-------------------------------------------------------------//
 // Nombres de las familias de algoritmos			//
 //-------------------------------------------------------------//

 public static final String famname[] =
  {"Gradient Descent", "Conjugate Gradient", "Quasi-Newton", "No derivatives" };

 //-------------------------------------------------------------//
 // Codigos de los algoritmos de cada familia			//
 //-------------------------------------------------------------//

 public static final int famcode[][] =
  { { STEEPEST, BACKPROP, BACKPROPMOM, ADAPTLEARNRATE, ADAPTSTEPSIZE,
      QUICKPROP, MANHATTAN, RPROP },
    { CONJUGATE, SCALED },
    { QUASI, MARQUARDT, GAUSS },
    { SIMPLEX, POWELL, BLIND, ANNEALING } };

 //-------------------------------------------------------------//
 // Tipos de parametros (para su evaluacion)			//
 //-------------------------------------------------------------//

 public static final int POSITIVE = 0;
 public static final int DECREASE = 1;
 public static final int INCREASE = 2;
 public static final int MOMENTUM = 3;
 public static final int REDUCE = 4;
 public static final int INTEGER = 5;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 protected boolean init = true;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //		    METODOS ESTATICOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Genera un algoritmo utilizando su nobre y parametros	//
 //-------------------------------------------------------------//

 public static XfslAlgorithm create(String name,double[] param)
 throws XflException{
  XfslAlgorithm alg = create(parse(name));
  alg.setParameters(param);
  return alg;
 }

 //-------------------------------------------------------------//
 // Genera un algoritmo utilizando su codigo y parametros	//
 //-------------------------------------------------------------//

 public static XfslAlgorithm create(int code, double[] param)
 throws XflException {
  XfslAlgorithm alg = create(code);
  alg.setParameters(param);
  return alg;
 }

 //-------------------------------------------------------------//
 // Genera un algoritmo utilizando su codigo			//
 //-------------------------------------------------------------//

 public static XfslAlgorithm create(int code) throws XflException {
  switch(code) {
   case BACKPROP: return new BackProp();
   case BACKPROPMOM: return new BackPropMom();
   case ADAPTLEARNRATE: return new AdaptLearnRate();
   case ADAPTSTEPSIZE: return new AdaptStepSize();
   case MANHATTAN: return new Manhattan();
   case QUICKPROP: return new QuickProp();
   case RPROP: return new RProp();
   case MARQUARDT: return new Marquardt();
   case GAUSS: return new GaussNewton();
   case STEEPEST: return new SteepestDescent();
   case CONJUGATE: return new Conjugate();
   case SCALED: return new Scaled();
   case QUASI: return new QuasiNewton();
   case ANNEALING: return new Annealing();
   case BLIND: return new BlindSearch();
   case SIMPLEX: return new Simplex();
   case POWELL: return new Powell();
   default: throw new XflException(28);
  }
 }

 //-------------------------------------------------------------//
 // Identificador del algoritmo en el fichero de configuracion	//
 //-------------------------------------------------------------//

 public static String getParseName(int code) throws XflException {
  switch(code) {
   case BACKPROP: return "Backpropagation";
   case BACKPROPMOM: return "Backprop_with_momentum";
   case ADAPTLEARNRATE: return "Adaptive_learning_rate";
   case ADAPTSTEPSIZE: return "Adaptive_step_size";
   case MANHATTAN: return "Manhattan";
   case QUICKPROP: return "Quickprop";
   case RPROP: return "Rprop";
   case MARQUARDT: return "Marquardt";
   case GAUSS: return "Gauss_Newton";
   case STEEPEST: return "Steepest_descent";
   case CONJUGATE: return "Conjugate_gradient";
   case SCALED: return "Scaled_conjugate_gradient";
   case QUASI: return "QuasiNewton";
   case ANNEALING: return "Simulated_Annealing";
   case BLIND: return "Blind_search";
   case SIMPLEX: return "Simplex";
   case POWELL: return "Powell";
   default: throw new XflException(28);
  }
 }

 //-------------------------------------------------------------//
 // Identificador del algoritmo en la interfaz grafica		//
 //-------------------------------------------------------------//

 public static String getName(int code) throws XflException {
  switch(code) {
   case BACKPROP: return "Backpropagation";
   case BACKPROPMOM: return "Backprop with Momentum";
   case ADAPTLEARNRATE: return "Adaptive Learning Rate";
   case ADAPTSTEPSIZE: return "Adaptive Step Size";
   case MANHATTAN: return "Manhattan";
   case QUICKPROP: return "QuickProp";
   case RPROP: return "RProp";
   case MARQUARDT: return "Marquardt-Levenberg";
   case GAUSS: return "Gauss-Newton";
   case STEEPEST: return "Steepest Descent";
   case CONJUGATE: return "Conjugate Gradient";
   case SCALED: return "Scaled Conjugate Gradient";
   case QUASI: return "Quasi-Newton";
   case ANNEALING: return "Simulated Annealing";
   case BLIND: return "Blind Search";
   case SIMPLEX: return "Downhill Simplex";
   case POWELL: return "Powell's";
   default: throw new XflException(28);
  }
 }

 //-------------------------------------------------------------//
 // Verifica el valor de un parametro de un cierto tipo		//
 //-------------------------------------------------------------//

 public static final double test(double param, int tst) throws XflException {
  boolean good = true;
  switch(tst) {
   case POSITIVE: good = (param>0); break;
   case MOMENTUM: good = (param > 0 && param <= 1); break;
   case INCREASE: good = (param > 1); break;
   case DECREASE: good = (param > 0 && param < 1); break;
   case REDUCE:   good = (param > 0 && param <= 0.5); break;
  }
  if(!good) throw new XflException(26);
  return param;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //		METODOS ESTATICOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Parser de los identificadores de configuracion		//
 //-------------------------------------------------------------//

 private static int parse(String name) {
  if(name.equals("Backpropagation")) return BACKPROP;
  if(name.equals("Backprop_with_momentum")) return BACKPROPMOM;
  if(name.equals("Adaptive_learning_rate")) return ADAPTLEARNRATE;
  if(name.equals("Adaptive_step_size")) return ADAPTSTEPSIZE;
  if(name.equals("Manhattan")) return MANHATTAN;
  if(name.equals("Quickprop")) return QUICKPROP;
  if(name.equals("Rprop")) return RPROP;
  if(name.equals("Marquardt")) return MARQUARDT;
  if(name.equals("Gauss_Newton")) return GAUSS;
  if(name.equals("Steepest_descent")) return STEEPEST;
  if(name.equals("Conjugate_gradient")) return CONJUGATE;
  if(name.equals("Scaled_conjugate_gradient")) return SCALED;
  if(name.equals("QuasiNewton")) return QUASI;
  if(name.equals("Simulated_Annealing")) return ANNEALING;
  if(name.equals("Blind_search")) return BLIND;
  if(name.equals("Simplex")) return SIMPLEX;
  if(name.equals("Powell")) return POWELL;
  return -1;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Duplica el objeto						//
 //-------------------------------------------------------------//

 public Object clone() {
  try { return super.clone(); }
  catch(CloneNotSupportedException e) { return null; }
 }

 //-------------------------------------------------------------//
 // Marca la reinicializacion del algoritmo			//
 //-------------------------------------------------------------//

 public void reinit() {
  this.init = true;
 }

 //-------------------------------------------------------------//
 // Devuelve el identificador de la interfaz grafica		//
 //-------------------------------------------------------------//

 public String getName() {
  try { return getName( getCode() ); }
  catch(Exception ex) {}
  return "";
 }

 //-------------------------------------------------------------//
 // Descripcion del algoritmo en el fichero de configuracion	//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String name = "";
  try { name = getParseName( getCode() ); } catch(Exception ex) {}
  String src= "xfsl_algorithm("+name;
  XfslAlgorithmParam[] param = getParams();
  for(int i=0; i<param.length; i++) src += ", "+param[i].getValue();
  src += ")"+eol;
  XfslAlgorithmOption[] opt = getOptions();
  for(int i=0; i<opt.length; i++) src += opt[i].toCode()+eol;
  return src;
 }

 //-------------------------------------------------------------//
 // Selecciona el valor de una determinada opcion		//
 //-------------------------------------------------------------//

 public void setOption(String name,double[] param) throws XflException {
  XfslAlgorithmOption[] opt = getOptions();
  for(int i=0; i<opt.length; i++) if( opt[i].setOption(name,param) ) return;
  throw new XflException(29);
 }

 //-------------------------------------------------------------//
 // Devuelve el codigo de identificacion del algoritmo		//
 //-------------------------------------------------------------//

 public abstract int getCode();

 //-------------------------------------------------------------//
 // Actualiza los parametros de configuracion del algoritmo	//
 //-------------------------------------------------------------//

 public abstract void setParameters(double[] param) throws XflException;

 //-------------------------------------------------------------//
 // Obtiene los parametros de configuracion del algoritmo	//
 //-------------------------------------------------------------//

 public abstract XfslAlgorithmParam[] getParams();

 //-------------------------------------------------------------//
 // Obtiene las opciones de configuracion del algoritmo		//
 //-------------------------------------------------------------//

 public abstract XfslAlgorithmOption[] getOptions();

 //-------------------------------------------------------------//
 // Ejecuta una iteracion del algoritmo				//
 //-------------------------------------------------------------//

 public abstract XfslEvaluation iteration(Specification spec, XfslPattern p,
                                   XfslErrorFunction ef) throws XflException;

}
