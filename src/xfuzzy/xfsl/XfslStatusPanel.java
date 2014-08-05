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
//	    PANEL DE MUESTRA DEL ESTADO DEL APRENDIZAJE		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.util.*;
import javax.swing.*;

public class XfslStatusPanel extends Box {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603071L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XTextForm text[] = new XTextForm[10];

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			 CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslStatusPanel() {
  super(BoxLayout.Y_AXIS);

  String label[] = { "Status", "Iteration  ", "Error", "RMSE", "MxAE",
     "Variation     ", "Error", "RMSE", "MxAE", "Variation     " };

  for(int i=0; i<text.length; i++) {
   text[i] = new XTextForm(label[i]);
   text[i].setEditable(false);
   text[i].setFieldWidth(100);
  }
  XTextForm.setWidth(text);

  Box trnbox = new Box(BoxLayout.Y_AXIS);
  trnbox.add(new XLabel("Training"));
  for(int i=2; i<6; i++) {
   trnbox.add(Box.createGlue());
   trnbox.add(text[i]);
  }

  Box tstbox = new Box(BoxLayout.Y_AXIS);
  tstbox.add(new XLabel("Test"));
  for(int i=6; i<10; i++) {
   tstbox.add(Box.createGlue());
   tstbox.add(text[i]);
  }

  Box box = new Box(BoxLayout.X_AXIS);
  box.add(trnbox);
  box.add(tstbox);

  add(new XLabel("Status"));
  add(Box.createGlue());
  add(text[0]);
  add(Box.createGlue());
  add(text[1]);
  add(Box.createGlue());
  add(box);
  text[0].setText("unconfigured");
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Actualiza el estado al cambiar la configuracion		//
 //-------------------------------------------------------------//

 public void set(int status) {
  if(status == XfslStatus.UNCONFIGURED) text[0].setText("unconfigured");
  if(status == XfslStatus.READY_TO_RUN) text[0].setText("ready to run");
 }

 //-------------------------------------------------------------//
 // Limpia el panel 						//
 //-------------------------------------------------------------//

 public void reset(boolean ready) {
  if(ready) text[0].setText("ready to run");
  else text[0].setText("unconfigured");
  for(int i=1; i<text.length; i++) text[i].setText("");
 }

 //-------------------------------------------------------------//
 // Modifica las etiquetas segun el tipo de sistema		//
 //-------------------------------------------------------------//

 public void setTitles(boolean classif) {
  if(classif) {
   text[3].setLabel("Missc. rate");
   text[4].setLabel("Missc. num.");
   text[7].setLabel("Missc. rate");
   text[8].setLabel("Missc. num.");
  } else {
   text[3].setLabel("RMSE");
   text[4].setLabel("MxAE");
   text[7].setLabel("RMSE");
   text[8].setLabel("MxAE");
  }
 }

 //-------------------------------------------------------------//
 // Actualiza los campos con los valores del estado		//
 //-------------------------------------------------------------//

 public void set(XfslStatus stat) {
  String stname="";
  switch(stat.status) {
   case XfslStatus.UNCONFIGURED: stname = "unconfigured"; break;
   case XfslStatus.READY_TO_RUN: stname = "ready to run"; break;
   case XfslStatus.LEARNING: stname = "learning"; break;
   case XfslStatus.STOPPED: stname = "stopped"; break;
   case XfslStatus.FINISHED: stname = "finished"; break;
  }
  text[0].setText(stname);
  text[1].setText(""+ stat.epoch);
  text[2].setText(redondeo(stat.trn.error));
  text[3].setText(redondeo(stat.trn.rmse));
  text[4].setText(redondeo(stat.trn.mxae));
  text[5].setText(redondeo(stat.trn.var));
  if(stat.testing) {
   text[6].setText(redondeo(stat.tst.error));
   text[7].setText(redondeo(stat.tst.rmse));
   text[8].setText(redondeo(stat.tst.mxae));
   text[9].setText(redondeo(stat.tst.var));
  }
  else for(int i=6; i<10; i++) text[i].setText("");
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Funcion de redondeo para mostrar un valor real		//
 //-------------------------------------------------------------//

 private String redondeo(double dd) {
  String data = ""+dd;
  char[] cdat = data.toCharArray();
  int i;
  for(i=0; i<cdat.length; i++) if(cdat[i]>'0' && cdat[i]<='9') break;
  for(int j=0; j<6 && i<cdat.length; j++,i++)
    if(cdat[i]=='e' || cdat[i]=='E') break;
  StringBuffer buf = new StringBuffer(data.substring(0,i));
  int e = data.indexOf("E");
  if(e == -1) e = data.indexOf("e");
  if(e != -1) buf.append(data.substring(e));
  return buf.toString();
 }
}
