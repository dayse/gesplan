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
//	  FUNCIONES QUE CALCULAN EL GRADIENTE CONJUGADO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl.algorithm;

import xfuzzy.xfsl.*;
import xfuzzy.lang.*;
import xfuzzy.util.*;

public class ConjugateMethodOption implements XfslAlgorithmOption {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int POLAK = 0;
 public static final int FLETCHER = 1;
 public static final int HESTENES = 2;
 public static final int ONESTEP = 3;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public int beta;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XTextForm text;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public ConjugateMethodOption() {
  this.beta = POLAK;
 }
  
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Selecciona la opcion desde el fichero de configuracion      //
 //-------------------------------------------------------------//

 public boolean setOption(String name,double[] param) throws XflException {
  if(name.equals("Polak_Ribiere")) { beta = POLAK; return true; }
  else if(name.equals("Fletcher_Reeves")) { beta = FLETCHER; return true; }
  else if(name.equals("Hestenes_Stiefel")) { beta = HESTENES; return true; }
  else if(name.equals("One_step_secant")) { beta = ONESTEP; return true; }
  else return false;
 }

 //-------------------------------------------------------------//
 // Descripcion de la opcion en el fichero de configuracion     //
 //-------------------------------------------------------------//

 public String toCode() {
  if(beta == POLAK) return "xfsl_option(Polak_Ribiere)";
  if(beta == FLETCHER) return "xfsl_option(Fletcher_Reeves)";
  if(beta == HESTENES) return "xfsl_option(Hestenes_Stiefel)";
  if(beta == ONESTEP) return "xfsl_option(One_step_secant)";
  return "";
 }

 //-------------------------------------------------------------//
 // Muestra un campo de seleccion grafica de la opcion          //
 //-------------------------------------------------------------//

 public XTextForm show() {
  String[] method =
    {"Polak-Ribiere","Fletcher-Reeves","Hestenes-Stiefel","One-step Secant"};
  this.text = new XTextForm("Method", method, (String[]) null );
  this.text.setSelection(beta);
  return this.text;
 }

 //-------------------------------------------------------------//
 // Lee y verifica los valores introducidos en el campo         //
 //-------------------------------------------------------------//

 public boolean get() {
  beta = text.getSelection();
  return true;
 }
}

