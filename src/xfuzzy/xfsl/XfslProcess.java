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
//	    HILO DE CONTROL DEL PROCESO DE APRENDIZAJE		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.util.*;

public class XfslProcess extends Thread {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XfslPipe pipe;
 private Thread thread;
 private Xfsl xfsl;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			  CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslProcess(Xfsl xfsl) {
  super("xfsl thread 1");
  this.xfsl = xfsl;
  this.pipe = new XfslPipe();
  XfslThread slrn = new XfslThread(xfsl.getSpec(), xfsl.getConfig(), pipe);
  thread = new Thread(slrn,"xfsl thread 2");
  thread.start();
  start();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Detener la ejecucion					//
 //-------------------------------------------------------------//

 public void sendStop() {
  this.pipe.putCommand(XfslPipe.STOP);
 }

 //-------------------------------------------------------------//
 // Continuar la ejecucion					//
 //-------------------------------------------------------------//

 public void sendContinue() {
  this.pipe.putCommand(XfslPipe.CONTINUE);
 }

 //-------------------------------------------------------------//
 // Finalizar la ejecucion					//
 //-------------------------------------------------------------//

 public void sendFinish() {
  this.pipe.putCommand(XfslPipe.FINISH);
 }

 //-------------------------------------------------------------//
 // Ejecucion del proceso de aprendizaje			//
 //-------------------------------------------------------------//

 public void run() {
  XfslStatus status = this.pipe.get();
  xfsl.setStatus(status);
  while(status.status != XfslStatus.FINISHED) {
   status = this.pipe.get();
   xfsl.setStatus(status);
   String[] msg = this.pipe.getMessage();
   if(msg != null) XDialog.showMessage(null,msg); 
  }
  try { thread.join(); } catch(InterruptedException e) {}
 }
}
