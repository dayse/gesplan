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
//		   CONDICIONES DE FINALIZACION			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

public class XfslEndCondition implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int EPOCH = 0;
 public static final int TRN_ERROR = 1;
 public static final int TRN_RMSE = 2;
 public static final int TRN_MXAE = 3;
 public static final int TRN_VAR = 4;
 public static final int TEST_ERROR = 5;
 public static final int TEST_RMSE = 6;
 public static final int TEST_MXAE = 7;
 public static final int TEST_VAR = 8;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private static final String limitname[] =
  { "epoch", "training_error", "training_RMSE", "training_MXAE",
    "training_variation", "test_error", "test_RMSE", "test_MXAE",
    "test_variation" };

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private double limit[] = new double[9];

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslEndCondition() {
  for(int i=0; i<limit.length; i++) limit[i] = -1;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Duplicado del objeto					//
 //-------------------------------------------------------------//

 public Object clone() {
  try { return super.clone(); }
  catch(CloneNotSupportedException e) { return null; }
 }

 //-------------------------------------------------------------//
 // Verifica que existe alguna condicion fijada			//
 //-------------------------------------------------------------//

 public boolean isOn() {
  for(int i=0; i<limit.length; i++) if(limit[i] != -1) return true;
  return false;
 }

 //-------------------------------------------------------------//
 // Verifica que se ha alcanzado la condicion de finalizacion	//
 //-------------------------------------------------------------//

 public boolean isOver(XfslStatus status) {
  if(status.status == XfslStatus.FINISHED) return true;
  if(status.epoch >= limit[0] && limit[0] != -1) return true;
  if(status.trn.error < limit[1] && limit[1] != -1) return true;
  if(status.trn.rmse < limit[2] && limit[2] != -1) return true;
  if(status.trn.mxae < limit[3] && limit[3] != -1) return true;
  //if(status.trn.var<limit[4] && status.trn.var>0 && limit[4]!=-1) return true;
  if(status.trn.var<limit[4] && status.trn.var>=0 && limit[4]!=-1) return true;
  if(!status.testing) return false;
  if(status.tst.error < limit[5] && limit[5] != -1) return true;
  if(status.tst.rmse < limit[6] && limit[6] != -1) return true;
  if(status.tst.mxae < limit[7] && limit[7] != -1) return true;
  //if(status.tst.var<limit[8] && status.tst.var>0 && limit[8]!=-1) return true;
  if(status.tst.var<limit[8] && status.tst.var>=0 && limit[8]!=-1) return true;
  return false;
 }

 //-------------------------------------------------------------//
 // Asigna una condicion de finalizacion			//
 //-------------------------------------------------------------//

 public void set(String name, double[] param) {
  for(int i=0; i<limitname.length; i++)
   if(limitname[i].equals(name)) limit[i] = param[0];
 }

 //-------------------------------------------------------------//
 // Asigna una condicion de finalizacion			//
 //-------------------------------------------------------------//

 public void setLimit(int index, double value) {
  limit[index] = value;
 }

 //-------------------------------------------------------------//
 // Obtiene el valor de una condicion de termino		//
 //-------------------------------------------------------------//

 public double getLimit(int index) {
  return limit[index];
 }

 //-------------------------------------------------------------//
 // Genera el codigo del fichero de configuracion		//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String src = "";
  if(limit[0] != -1) src += "xfsl_endcondition(epoch,"+(long)limit[0]+")"+eol;
  for(int i=1; i<limit.length; i++)
   if(limit[i] != -1)
     src += "xfsl_endcondition("+limitname[i]+","+limit[i]+")"+eol;
  return src;
 }
}

