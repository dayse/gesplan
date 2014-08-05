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
//		   APRENDIZAJE SUPERVISADO			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.*;
import xfuzzy.lang.*;
import java.io.*;

public class XfslThread implements Runnable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XfslConfig config;
 private XfslStatus status;
 private XfslPipe pipe;
 private Specification spec;
 private XfslPattern training;
 private XfslPattern test;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			  CONSTRUCTORES				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor para ejecucion en modo comando			//
 //-------------------------------------------------------------//

 public XfslThread(Specification spec, XfslConfig config) {
  this.spec = spec;
  this.config = config;
  this.status = new XfslStatus();
  this.pipe = null;
 }

 //-------------------------------------------------------------//
 // Constructor para ejecucion en modo grafico			//
 //-------------------------------------------------------------//

 public XfslThread(Specification spec, XfslConfig config, XfslPipe pipe) {
  this.spec = spec;
  this.config = config;
  this.status = new XfslStatus();
  this.pipe = pipe;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			EJECUCION EXTERNA			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static void main(String args[]) throws Exception {
  if(args.length == 1) foreground(args[0]);
  else if(args.length == 2) background(args[0], args[1]);
  else {
   System.out.println("Usage: xfsl file.xfl [file.cfg]");
   System.exit(-1);
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Ejecucion del hilo						//
 //-------------------------------------------------------------//

 public void run() {
  try { learning(); }
  catch(XflException ex) {
   if(this.pipe != null) this.pipe.putMessage(ex.toString());
   else System.out.println(ex.toString());
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //		METODOS CONSTANTES PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Ejecucion externa sin modo grafico				//
 //-------------------------------------------------------------//

 private static void background(String arg0, String arg1) {
  XflParser xflparser = new XflParser();
  Specification spec = xflparser.parse(arg0);
  if(spec == null) {
   System.out.print(xflparser.resume());
   System.exit(-1);
  }
  XfuzzyConfig xfcparser = new XfuzzyConfig();
  XfslConfig config = xfcparser.parseXfslConfig(new File(arg1));
  if(config == null) {
   System.out.print(xfcparser.resume());
   System.exit(-1);
  }
  XfslThread xfsl = new XfslThread(spec,config);
  try { xfsl.learning(); }
  catch(XflException ex) {
   System.out.println(ex.toString());
   System.exit(-1);
  }
  if(config.outputfile != null) spec.save_as(config.outputfile);
  else spec.save_as(new File("xfsl_out.xfl"));
 }

 //-------------------------------------------------------------//
 // Ejecucion externa con modo grafico				//
 //-------------------------------------------------------------//

 private static void foreground(String arg0) {
  XflParser xflparser = new XflParser();
  Specification spec = xflparser.parse(arg0);
  if(spec == null) {
   System.out.print(xflparser.resume());
   System.exit(-1);
  }
  Xfsl learn = new Xfsl(spec);
  learn.setVisible(true);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Ejecucion completa del proceso de aprendizaje		//
 //-------------------------------------------------------------//

 public void learning() throws XflException {
  initialization();
  preprocessing();
  status.status = XfslStatus.LEARNING;
  analize();
  communication();
  while(!config.endcondition.isOver(status)) {
   iteration();
   communication();
  }
  postprocessing();
  analize();
  status.status = XfslStatus.FINISHED;
  communication();
 }

 //-------------------------------------------------------------//
 // Inicializa el proceso de aprendizaje			//
 //-------------------------------------------------------------//

 private void initialization() throws XflException {
  this.config.modified = false;
  this.config.setParameterSettings(this.spec.getTypes());
  this.spec.setAdjustable();
  if(this.config.testfile == null) this.status.testing = false;
  else this.status.testing = true;
  int numinputs = spec.getSystemModule().getInputs().length;
  int numoutputs = spec.getSystemModule().getOutputs().length;
  this.training= new XfslPattern(this.config.trainingfile,numinputs,numoutputs);
  if(this.config.testfile == null) this.test = null;
  else this.test = new XfslPattern(this.config.testfile,numinputs,numoutputs);
  this.config.errorfunction.normalizeWeights(numoutputs);
  this.training.setRanges(spec);
  if(this.test != null) this.test.setRanges(spec);
 }

 //-------------------------------------------------------------//
 // Procesos de simplificacion previos al ajuste		//
 //-------------------------------------------------------------//

 private void preprocessing() {
  String[] preproc = config.preprocessing.compute(spec,training.input);
  if(preproc == null) return;
  if(this.pipe != null) this.pipe.putMessage(preproc);
  else for(int i=0; i<preproc.length; i++) System.out.println(preproc[i]);
 }

 //-------------------------------------------------------------//
 // Procesos de simplificacion posteriores al ajuste		//
 //-------------------------------------------------------------//

 private void postprocessing() {
  String[] postproc = config.postprocessing.compute(spec,training.input);
  if(postproc == null) return;
  if(this.pipe != null) this.pipe.putMessage(postproc);
  else for(int i=0; i<postproc.length; i++) System.out.println(postproc[i]);
 }

 //-------------------------------------------------------------//
 // Iteracion del proceso de ajuste				//
 //-------------------------------------------------------------//

 private void iteration() {
  status.epoch++;
  try {
   status.trn=config.algorithm.iteration(spec,training,config.errorfunction);
   if(status.testing) 
    status.tst = config.errorfunction.evaluate(spec, test, status.tst.error);
  } catch(XflException ex) {
   status.epoch--;
   status.status = XfslStatus.FINISHED;
   if(this.pipe != null) this.pipe.putMessage(ex.toString());
   else System.out.println(ex.toString());
  }
 }

 //-------------------------------------------------------------//
 // Evaluacion del sistema					//
 //-------------------------------------------------------------//

 private void analize() {
  status.trn = config.errorfunction.evaluate(spec, training, status.trn.error);
  if(!status.testing) return;
  status.tst = config.errorfunction.evaluate(spec, test , status.tst.error);
 }

 //-------------------------------------------------------------//
 // Comunicacion del estado del sistema				//
 //-------------------------------------------------------------//

 private void communication() throws XflException {
  this.config.logfile.write(this.status);
  if(this.pipe == null) return;
  this.pipe.put( (XfslStatus) this.status.clone());
  int msg=this.pipe.getCommand();
  if(msg == XfslPipe.NOMSG) return;
  if(msg == XfslPipe.STOP) {
   int laststat = this.status.status;
   this.status.status = XfslStatus.STOPPED;
   this.pipe.put( (XfslStatus) this.status.clone());
   while(msg != XfslPipe.CONTINUE && msg != XfslPipe.FINISH) {
    try{ Thread.sleep(100); } catch(InterruptedException e) {}
    msg=this.pipe.getCommand();
   }
   if(msg == XfslPipe.FINISH) this.status.status = XfslStatus.FINISHED;
   if(msg == XfslPipe.CONTINUE) {
    this.status.status = laststat;
    if(this.config.modified) initialization();
   }
   this.pipe.put( (XfslStatus) this.status.clone());
  }
 }
 public Specification getSpecification() {
     return spec;
 }

 public XfslStatus getStatus() {
     return status;
 }
}
