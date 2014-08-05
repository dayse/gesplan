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

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XfslEndDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603066L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private static final int NOT_SELECTED = -1;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XfslEndCondition condition;
 private XTextForm text[] = new XTextForm[9];
 private boolean selected;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslEndDialog(JFrame frame, XfslEndCondition condition) { 
  super((Frame) frame, "Xfsl", true);
  this.condition = condition;
  this.selected = false;
  build(frame);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Verifica que se acepte la seleccion				//
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
  else if(command.equals("Unset")) unset();
  else if(command.equals("Cancel")) cancel();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Construye el dialogo de introduccion de datos		//
 //-------------------------------------------------------------//

 private void build(JFrame frame) {
  String label[] = {"Limit on Iterations",
                 "Limit on Training Error", "Limit on Training RMSE",
                 "Limit on Training MxAE", "Limit on Training Rel. Var.",
                 "Limit on Test Error", "Limit on Test RMSE",
                 "Limit on Test MxAE", "Limit on Test Rel. Var."};

  for(int i=0; i<text.length; i++) text[i] = new XTextForm(label[i]);
  XTextForm.setWidth(text);
  if(condition.getLimit(0) == NOT_SELECTED) text[0].setText("");
  else text[0].setText(""+ (long) condition.getLimit(0));
  for(int i=1; i<text.length; i++)
   if(condition.getLimit(i) == NOT_SELECTED) text[i].setText("");
   else text[i].setText(""+condition.getLimit(i));

  String lb[] = { "Set", "Unset", "Cancel" };
  XCommandForm form = new XCommandForm(lb,lb,this);
  form.setCommandWidth(100);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("End Conditions"));
  for(int i=0; i<text.length; i++) content.add(text[i]);
  content.add(form);

  Point loc = frame.getLocationOnScreen();
  loc.x += 40; loc.y += 200;
  setLocation(loc);
  pack();
 } 

 //-------------------------------------------------------------//
 // Obtiene los datos del dialogo				//
 //-------------------------------------------------------------//

 private boolean get() {
  boolean good = true;
  for(int i=0; i<9; i++) {
   if(text[i].getText().trim().length() > 0) {
    try {
     if(i==0) condition.setLimit(i, (double) Long.parseLong(text[i].getText()));
     else condition.setLimit(i, Double.parseDouble(text[i].getText()));
    }
    catch (NumberFormatException e) {
     good = false;
     condition.setLimit(i, NOT_SELECTED);
     text[i].setText("");
    }
   }
   else condition.setLimit(i, NOT_SELECTED);
  }
  return good;
 }

 //-------------------------------------------------------------//
 // Actualiza los datos con los valores de la ventana		//
 //-------------------------------------------------------------//

 private void set() {
  if(!get()) XDialog.showMessage(null,"Values introduced are not correct");
  else { selected = true; setVisible(false); }
 }

 //-------------------------------------------------------------//
 // Borra el contenido de las condiciones de finalizacion	//
 //-------------------------------------------------------------//

 private void unset() {
  for(int i=0; i<9; i++) condition.setLimit(i, NOT_SELECTED);
  selected = true;
  setVisible(false);
 }

 //-------------------------------------------------------------//
 // Cierra el dialogo sin provocar cambios en los datos		//
 //-------------------------------------------------------------//

 private void cancel() {
  selected = false;
  setVisible(false);
 }
}

