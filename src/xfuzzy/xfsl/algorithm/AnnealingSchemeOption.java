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
//	  OPCION DEL ALGORITMO DE ENFRIAMIENTO SIMULADO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;

public class AnnealingSchemeOption implements XfslAlgorithmOption {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int LINEAL = 0;
 public static final int EXPONENTIAL = 1;
 public static final int CLASSICAL = 2;
 public static final int FAST = 3;
 public static final int ADAPTIVE = 4;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public int cooling;
 public int steps;
 public double decrease;
 public double asa;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XTextForm text;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public AnnealingSchemeOption() {
  this.cooling = CLASSICAL;
  this.steps = -1;
  this.decrease = -1;
  this.asa = -1;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Selecciona la opcion desde el fichero de configuracion      //
 //-------------------------------------------------------------//

 public boolean setOption(String name,double[] param) throws XflException {
  if(name.equals("linear_cooling")) {
   cooling = LINEAL;
   steps = (int) XfslAlgorithm.test(param[0], XfslAlgorithm.INTEGER);
   return true;
  }
  else if(name.equals("exponential_cooling")) {
   cooling = EXPONENTIAL;
   decrease = XfslAlgorithm.test(param[0], XfslAlgorithm.DECREASE);
   return true;
  }
  else if(name.equals("classical_cooling")) {
   cooling = CLASSICAL;
   return true;
  }
  else if(name.equals("fast_cooling")) {
   cooling = FAST;
   return true;
  }
  else if(name.equals("adaptive_cooling")) {
   cooling = ADAPTIVE;
   asa = XfslAlgorithm.test(param[0], XfslAlgorithm.POSITIVE);
   return true;
  }
  else return false;
 }

 //-------------------------------------------------------------//
 // Descripcion de la opcion en el fichero de configuracion     //
 //-------------------------------------------------------------//

 public String toCode() {
  if(cooling == LINEAL) return "xfsl_option(linear_cooling,"+steps+")";
  if(cooling == EXPONENTIAL)
   return "xfsl_option(exponential_cooling,"+decrease+")";
  if(cooling == CLASSICAL) return "xfsl_option(classical_cooling)";
  if(cooling == FAST) return "xfsl_option(fast_cooling)";
  if(cooling == ADAPTIVE) return "xfsl_option(adaptive_cooling,"+asa+")";
  return "";
 }

 //-------------------------------------------------------------//
 // Muestra un campo de seleccion grafica de la opcion          //
 //-------------------------------------------------------------//

 public XTextForm show() {
  String name[] =
   { "Linear Cooling","Exponential Cooling", "Classical Cooling",
     "Fast Cooling", "Adaptive Cooling"};
  String param[] =
   { "Number of Steps","Decrease Factor","", "", "Adaptive Factor"};
  this.text = new XTextForm("Cooling Scheme", name, param);
  this.text.setSelection(cooling);
  if(cooling == LINEAL && steps > 0) this.text.setParameter(""+steps);
  else if(cooling == EXPONENTIAL && decrease > 0)
       this.text.setParameter(""+decrease);
  if(cooling == ADAPTIVE && asa > 0) this.text.setParameter(""+asa);
  else this.text.setParameter("");
  return this.text;
 }

 //-------------------------------------------------------------//
 // Lee y verifica los valores introducidos en el campo         //
 //-------------------------------------------------------------//

 public boolean get() {
  boolean good = true;
  cooling = text.getSelection();
  if(cooling == LINEAL) {
   try { steps = Integer.parseInt(text.getParameter()); }
   catch(Exception ex) { good = false; }
   if(steps <= 0) good = false;
  }
  else if(cooling == EXPONENTIAL) {
   try { decrease = Double.parseDouble(text.getParameter()); }
   catch(Exception ex) { good = false; }
   if(decrease <= 0) good = false;
  }
  else if(cooling == ADAPTIVE) {
   try { asa = Double.parseDouble(text.getParameter()); }
   catch(Exception ex) { good = false; }
   if(asa <= 0) good = false;
  }
  if(!good) text.setParameter("");
  return good;
 }
}

