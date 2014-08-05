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
//     CONFIGURACION DEL ESTILO DE UNA VARIABLE DE ENTRADA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

public class XfdmInputStyle {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int FREE_TRIANGLES = 0;
 public static final int TRIANGULAR_FAMILY = 1;
 public static final int FREE_SH_TRIANGLES = 2;
 public static final int SH_TRIANGULAR_FAMILY = 3;
 public static final int FREE_GAUSSIANS = 4;
 public static final int BSPLINES_FAMILY = 5;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public String name;
 public int mfs;
 public int style;
 public double min;
 public double max;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfdmInputStyle() {
  this.name = "ANY";
  this.mfs = 3;
  this.style = 0;
  this.min = 0;
  this.max = 0;
 }

 //-------------------------------------------------------------//
 // Constructor con el nombre					//
 //-------------------------------------------------------------//

 public XfdmInputStyle(String name) {
  this.name = name;
  this.mfs = 3;
  this.style = 0;
  this.min = 0;
  this.max = 0;
 }

 //-------------------------------------------------------------//
 // Constructor completo					//
 //-------------------------------------------------------------//

 public XfdmInputStyle(String name, int mfs, int style) {
  this.name = name;
  this.mfs = mfs;
  this.style = style;
  this.min = 0;
  this.max = 0;
 }

 //-------------------------------------------------------------//
 // Constructor desde la configuracion				//
 //-------------------------------------------------------------//

 public XfdmInputStyle(String name, double param[]) {
  this.name = name;
  this.min = param[0];
  this.max = param[1];
  this.mfs = (int) param[2];
  this.style = (int) param[3];
 }

 //-------------------------------------------------------------//
 // Obtiene un duplicado					//
 //-------------------------------------------------------------//

 public XfdmInputStyle(XfdmInputStyle ist) {
  this.name = new String(ist.name);
  this.mfs = ist.mfs;
  this.style = ist.style;
  this.min = ist.min;
  this.max = ist.max;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene la identificacion del estilo (nombre de la variable)//  
 //-------------------------------------------------------------//

 public String toString() {
  return this.name;
 }

 //-------------------------------------------------------------//
 // Representacion en el fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  return "xfdm_input("+name+","+min+","+max+","+mfs+","+style+")";
 }

 //-------------------------------------------------------------//
 // Estudia si el universo se ha configurado o no		//
 //-------------------------------------------------------------//

 public boolean isUniverseDefined() {
  return !(min==0 && max==0);
 }

 //-------------------------------------------------------------//
 // Estudia si los parametros de configuracion son correctos	//
 //-------------------------------------------------------------//

 public static boolean paramTest(double[] param) {
  if(param.length != 4) return false;
  if(param[0] > param[1]) return false;
  if(param[0] == param[1] && param[0] != 0) return false;
  if(param[2] < 1) return false;
  if(param[3] < 0 || param[3] > 5) return false;
  return true;
 }
}
