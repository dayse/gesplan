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
//		   EVALUACION DE UN SISTEMA			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

public class XfslEvaluation implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double error;
 public double rmse;
 public double mxae;
 public double var;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			  CONSTRUCTORES				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor por defecto					//
 //-------------------------------------------------------------//

 public XfslEvaluation() {
  this.error = 1;
  this.rmse = 1;
  this.mxae = 1;
  this.var = 1;
 }

 //-------------------------------------------------------------//
 // Constructor sin variacion del error				//
 //-------------------------------------------------------------//

 public XfslEvaluation(double error, double mse, double mxae, int num) {
  this.error = error/num;
  this.rmse = Math.sqrt(mse/num);
  this.mxae = mxae;
  this.var = 0;
 }

 //-------------------------------------------------------------//
 // Constructor con variacion del error				//
 //-------------------------------------------------------------//

 public XfslEvaluation(double error, double mse, double mxae, double lasterror,
 int num) {
  this.error = error/num;
  this.rmse = Math.sqrt(mse/num);
  this.mxae = mxae;
  this.var = (lasterror - this.error)/lasterror;
 }

 //-------------------------------------------------------------//
 // Constructor para error de clasificacion			//
 //-------------------------------------------------------------//

 public XfslEvaluation(double error, double misscr, double misscn) {
  this.error = error;
  this.rmse = misscr;
  this.mxae = misscn;
  this.var = 0;
 }

 //-------------------------------------------------------------//
 // Constructor para error de clasificacion con variacion	//
 //-------------------------------------------------------------//

 public XfslEvaluation(double error, double misscr, double misscn, double le) {
  this.error = error;
  this.rmse = misscr;
  this.mxae = misscn;
  this.var = (le-error)/le;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Descripcion para el fichero de log				//
 //-------------------------------------------------------------//

 public String toString() {
  return ""+this.error+" "+this.rmse+" "+this.mxae+" "+this.var;
 }

 //-------------------------------------------------------------//
 // Duplicado del objeto					//
 //-------------------------------------------------------------//

 public Object clone() {
  try { return super.clone(); }
  catch (CloneNotSupportedException e) { return null; }
 }
}
