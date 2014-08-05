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
//    VENTANA DE EDICION DE LOS ALGORITMOS DE APRENDIZAJE	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XfslAlgorithmDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603064L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XfslAlgorithm algorithm;
 private XfslAlgorithmParam param[];
 private XfslAlgorithmOption option[];
 private boolean selected;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslAlgorithmDialog(JFrame frame, XfslAlgorithm algorithm) {
  super((Frame) frame, "Xfsl", true);
  this.algorithm = algorithm;
  this.param = algorithm.getParams();
  this.option = algorithm.getOptions();
  XTextForm[] all = new XTextForm[param.length+option.length];
  for(int i=0; i<param.length; i++) all[i] = param[i].show();
  for(int i=0; i<option.length; i++) all[param.length+i] = option[i].show();
  XTextForm.setWidth(all);

  String label[] = { "Set", "Cancel" };
  XCommandForm form = new XCommandForm(label,label,this);
  form.setCommandWidth(100);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel(algorithm.getName()+" Algorithm"));
  for(int i=0; i<param.length; i++) content.add(all[i]);
  if(option.length > 0) content.add(new XLabel("Options"));
  for(int i=0; i<option.length; i++) content.add( all[param.length+i] );
  content.add(form);

  Point loc = frame.getLocationOnScreen();
  loc.x += 40; loc.y += 200;
  setLocation(loc);
  pack();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Verifica si los valores se han selecionado			//
 //-------------------------------------------------------------//

 public boolean isSelected() {
  return selected;
 }

 //-------------------------------------------------------------//
 // Interfaz ActionListener					//
 //-------------------------------------------------------------//

 public void actionPerformed(ActionEvent e) {
  String command = e.getActionCommand();
  if(command.equals("Set")) set();
  else if(command.equals("Cancel")) cancel();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Lee los valores de la ventana				//
 //-------------------------------------------------------------//

 private boolean get() {
  boolean good = true;
  for(int i=0; i<param.length; i++) if(!param[i].get()) good = false;
  for(int i=0; i<option.length; i++) if(!option[i].get()) good = false;
  if(good) {
   double[] pp = new double[param.length];
   for(int i=0; i<param.length; i++) pp[i] = param[i].getValue();
   try { algorithm.setParameters(pp); } catch(Exception ex) { good = false; }
  }
  return good;
 }

 //-------------------------------------------------------------//
 // Selecciona los valores de la ventana			//
 //-------------------------------------------------------------//

 private void set() {
  if(!get()) XDialog.showMessage(null,"Values introduced are not correct");
  else { selected = true; setVisible(false); }
 }

 //-------------------------------------------------------------//
 // Cierra la ventana sin seleccionar los valores		//
 //-------------------------------------------------------------//

 private void cancel() {
  selected = false;
  setVisible(false);
 }
}
