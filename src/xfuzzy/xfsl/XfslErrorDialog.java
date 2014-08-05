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
//   VENTANA DE SELECCION DE PESOS DE LA FUNCION DE ERROR	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class XfslErrorDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603067L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private boolean selected;
 private XTextForm text[];
 private double weight[];

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslErrorDialog(JFrame frame, Variable[] out) {
  super((Frame) frame, "Xfsl", true);

  text = new XTextForm[out.length];
  weight = new double[out.length];
  for(int i=0; i<out.length; i++) text[i] = new XTextForm(out[i].getName());
  XTextForm.setWidth(text);

  String label[] = { "Set", "Cancel" };
  XCommandForm form = new XCommandForm(label,label,this);
  form.setCommandWidth(100);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("Output Weights"));
  for(int i=0; i<text.length; i++) content.add(text[i]);
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
 // Muestra la ventana con los valores indicados		//
 //-------------------------------------------------------------//

 public boolean show(double[] value) {
  for(int i=0; i<text.length && i<value.length; i++)
   text[i].setText(""+value[i]);
  setVisible(true);
  return selected;
 }

 //-------------------------------------------------------------//
 // Obtiene los valores introducidos en los coampos		//
 //-------------------------------------------------------------//

 public double[] getWeights() {
  return weight;
 }

 //-------------------------------------------------------------//
 // Interfaz ActionListener					//
 //-------------------------------------------------------------//

 public void actionPerformed(ActionEvent e) {
  String command = e.getActionCommand();
  if(command.equals("Set")) set();
  else if(command.equals("Cancel")) cancel();
 }

 //-------------------------------------------------------------//
 // Lee los valores de los campos				//
 //-------------------------------------------------------------//

 private boolean get() {
  boolean good = true;
  for(int i=0; i<text.length; i++) {
   boolean test = true;
   try { weight[i] = Double.parseDouble(text[i].getText()); }
   catch(NumberFormatException nfe) { test = false; }
   if(test && weight[i] <0) test = false;
   if(!test) { weight[i] = -1; text[i].setText(""); good = false;}
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
