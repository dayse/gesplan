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
//   TUBERIA DE COMUNICACION ENTRE EL APRENDIZAJE Y LA VENTANA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

public class XfslPipe {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int NOMSG=0;
 public static final int STOP=1;
 public static final int CONTINUE=2;
 public static final int FINISH=3;
 
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int command=NOMSG;
 private String msg[];
 private XfslStatus status;
 private boolean statusflag = false;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Lee el estado en la tuberia (Comunicacion hilo-ventana)	//
 //-------------------------------------------------------------//

 public synchronized XfslStatus get() {
  if(!statusflag) try { wait(); } catch(InterruptedException e) {}
  statusflag = false;
  notify();
  return status;
 }

 //-------------------------------------------------------------//
 // Escribe el estado en la tuberia (Comunicacion hilo-ventana)	//
 //-------------------------------------------------------------//

 public synchronized void put(XfslStatus status) {
  if(statusflag) try { wait(); } catch(InterruptedException e) {}
  this.status = status;
  statusflag = true;
  notify();
 }

 //-------------------------------------------------------------//
 // Lee el comando en la tuberia (Comunicacion ventana-hilo)	//
 //-------------------------------------------------------------//

 public synchronized int getCommand() {
  int last= this.command;
  this.command = NOMSG;
  return last;
 }

 //-------------------------------------------------------------//
 // Escribe el comando en la tuberia (Comunicacion ventana-hilo)//
 //-------------------------------------------------------------//

 public synchronized void putCommand(int command) {
  this.command = command;
 }

 //-------------------------------------------------------------//
 // Lee el mensaje en la tuberia (Comunicacion hilo-ventana)	//
 //-------------------------------------------------------------//

 public synchronized String[] getMessage() {
  String[] last = this.msg;
  this.msg = null;
  return last;
 }

 //-------------------------------------------------------------//
 // Escribe el mensaje en la tuberia (Comunicacion hilo-ventana)//
 //-------------------------------------------------------------//

 public synchronized void putMessage(String msg) {
  this.msg = new String[1];
  this.msg[0] = msg;
 }

 //-------------------------------------------------------------//
 // Escribe el mensaje en la tuberia (Comunicacion hilo-ventana)//
 //-------------------------------------------------------------//

 public synchronized void putMessage(String[] msg) {
  this.msg = msg;
 }
}
