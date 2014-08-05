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
//   CLASE QUE SELECCIONA LA FORMA DE CALCULO DE LA DERIVADA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.lang.*;
import xfuzzy.xfsl.*;
import xfuzzy.util.*;

public class DerivativeOption implements XfslAlgorithmOption {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private static final int COMPUTE = 0;
 private static final int ESTIMATE = 1;
 private static final int STOCHASTIC = 2;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int derivative;
 private double perturbation;
 private XTextForm text;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public DerivativeOption() {
  this.derivative = COMPUTE;
  this.perturbation = -1;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Selecciona la opcion desde el fichero de configuracion      //
 //-------------------------------------------------------------//

 public boolean setOption(String name,double[] param) throws XflException {
  if(name.equals("weight_perturbation")) {
   derivative = ESTIMATE;
   perturbation = XfslAlgorithm.test(param[0], XfslAlgorithm.POSITIVE);
   return true;
  }
  else if(name.equals("stochastic_perturbation")) {
   derivative = STOCHASTIC;
   perturbation = XfslAlgorithm.test(param[0], XfslAlgorithm.POSITIVE);
   return true;
  }
  else return false;
 }

 //-------------------------------------------------------------//
 // Descripcion de la opcion en el fichero de configuracion     //
 //-------------------------------------------------------------//

 public String toCode() {
  if(derivative == ESTIMATE)
   return "xfsl_option(weight_perturbation,"+perturbation+")";
  if(derivative == STOCHASTIC)
   return "xfsl_option(stochastic_perturbation,"+perturbation+")";
  return "";
 }

 //-------------------------------------------------------------//
 // Muestra un campo de seleccion grafica de la opcion          //
 //-------------------------------------------------------------//

 public XTextForm show() {
  String name[] = { "Compute","Estimate","Roughly estimate"};
  String param[] = { "", "Perturbation", "Perturbation" };
  this.text = new XTextForm("Derivatives", name, param);
  this.text.setSelection(derivative);
  if(derivative != COMPUTE && perturbation > 0)
   this.text.setParameter(""+perturbation);
  else this.text.setParameter("");
  return this.text;
 }

 //-------------------------------------------------------------//
 // Lee y verifica los valores introducidos en el campo         //
 //-------------------------------------------------------------//

 public boolean get() {
  boolean good = true;
  derivative = text.getSelection();
  if(derivative != COMPUTE) {
   try {
    double pp = Double.parseDouble(text.getParameter());
    perturbation = XfslAlgorithm.test(pp, XfslAlgorithm.POSITIVE);
   } catch(Exception ex) { good = false; text.setParameter(""); }
  }
  return good;
 }

 //-------------------------------------------------------------//
 // Calcula la derivada de la forma seleccionada		//
 //-------------------------------------------------------------//

 public XfslEvaluation compute(Specification spec, XfslPattern pt,
 XfslErrorFunction ef) throws XflException {
  switch(derivative) {
   case COMPUTE: return ef.compute_derivatives(spec,pt);
   case ESTIMATE: return ef.estimate_derivatives(spec,pt,perturbation);
   case STOCHASTIC: return ef.stochastic_derivatives(spec,pt,perturbation);
   default: return null;
  }
 }

 //-------------------------------------------------------------//
 // Calcula la derivada de las salidas (no del error)		//
 //-------------------------------------------------------------//

 public double[] compute2(Specification spec,double[] input) throws XflException {
  switch(derivative) {
   case COMPUTE: return spec.compute_derivatives(input);
   case ESTIMATE: return spec.estimate_derivatives(input,perturbation);
   case STOCHASTIC: return spec.stochastic_derivatives(input,perturbation);
   default: return null;
  }
 }
}
