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
//   OPCION DE SELECCION DEL TIPO DE ESTIMACION DEL HESSIANO	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;

public class QuasiNewtonMethodOption implements XfslAlgorithmOption {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private static final int BFGS = 0;
 private static final int DFP = 1;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int equation;
 private XTextForm text;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public QuasiNewtonMethodOption() {
  this.equation = BFGS;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Selecciona la opcion desde el fichero de configuracion      //
 //-------------------------------------------------------------//

 public boolean setOption(String name,double[] param) throws XflException {
  if(name.equals("BFGS")) { equation = BFGS; return true; }
  else if(name.equals("DFP")) { equation = DFP; return true; }
  else return false;
 }

 //-------------------------------------------------------------//
 // Descripcion de la opcion en el fichero de configuracion     //
 //-------------------------------------------------------------//

 public String toCode() {
  if(equation == BFGS) return "xfsl_option(BFGS)";
  if(equation == DFP) return "xfsl_option(DFP)";
  return "";
 }

 //-------------------------------------------------------------//
 // Muestra un campo de seleccion grafica de la opcion          //
 //-------------------------------------------------------------//

 public XTextForm show() {
  String[] method =
   { "Broyden-Fletcher-Goldarfb-Shanno", "Davidon-Fletcher-Powell" };
  this.text = new XTextForm("Method", method, (String[]) null);
  this.text.setSelection(equation);
  return this.text;
 }

 //-------------------------------------------------------------//
 // Lee y verifica los valores introducidos en el campo         //
 //-------------------------------------------------------------//

 public boolean get() {
  equation = text.getSelection();
  return true;
 }

 //-------------------------------------------------------------//
 // Verifica si el metodo elegido es el BFGS			//
 //-------------------------------------------------------------//

 public boolean isBFGS() {
  return (equation == BFGS);
 }
}

