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
//	  VENTANA DE CONFIGURACION DEL ALGORITMO DE NAUCK	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XfdmNauckDialog extends JDialog implements ActionListener,
WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603016L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 private XfdmNauck nauck;
 private XTextField number;
 private JRadioButton[] global;
 private boolean conf;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 public XfdmNauckDialog(Xfdm xfdm, XfdmAlgorithm algorithm){
  super(xfdm,"Xfdm",true);
  if(algorithm != null && algorithm instanceof XfdmNauck) {
   XfdmNauck alg = (XfdmNauck) algorithm;
   this.nauck = (XfdmNauck) alg.clone();
  } else {
   this.nauck = new XfdmNauck();
  }
  build();
  set();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS ESTATICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Muestra el dialogo						//
 //-------------------------------------------------------------//

 public static XfdmNauck showDialog(Xfdm xfdm, XfdmAlgorithm algorithm) {
  XfdmNauckDialog dialog = new XfdmNauckDialog(xfdm, algorithm);
  dialog.setVisible(true);
  return dialog.getAlgorithm();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el algoritmo					//
 //-------------------------------------------------------------//

 public XfdmNauck getAlgorithm() {
  if(this.conf) return this.nauck;
  return null;
 }

 //-------------------------------------------------------------//
 // Interfaz ActionListener					//
 //-------------------------------------------------------------//

 public void actionPerformed(ActionEvent e) {
  String command = e.getActionCommand();
  if(command.equals("Set")) actionSet();
  else if(command.equals("Cancel")) actionCancel();
 }

 //-------------------------------------------------------------//
 // Interfaz WindowListener					//
 //-------------------------------------------------------------//

 public void windowOpened(WindowEvent e) {}
 public void windowClosing(WindowEvent e) { actionCancel(); }
 public void windowClosed(WindowEvent e) {}
 public void windowIconified(WindowEvent e) {}
 public void windowDeiconified(WindowEvent e) {}
 public void windowActivated(WindowEvent e) {}
 public void windowDeactivated(WindowEvent e) {}

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //=============================================================//
 //	   Metodos de descripcion de la interfaz grafica	//
 //=============================================================//

 //-------------------------------------------------------------//
 // Generacion de la ventana					//
 //-------------------------------------------------------------//

 private void build() {
  String lb[] = {"Set", "Cancel"};
  XCommandForm form = new XCommandForm(lb,lb,this);
  form.setCommandWidth(150);
  form.block();

  number = new XTextField("");

  global = new JRadioButton[2];
  global[0] = new JRadioButton("Best rules");
  global[1] = new JRadioButton("Best per class");

  Box opanel = new Box(BoxLayout.X_AXIS);
  opanel.add(Box.createHorizontalStrut(10));
  opanel.add(global[0]);
  opanel.add(global[1]);
  
  ButtonGroup rbg = new ButtonGroup();
  rbg.add(global[0]);
  rbg.add(global[1]);

  JPanel lpanel = new JPanel();
  lpanel.setLayout(new GridLayout(2,2));
  lpanel.add(new XLabel("Number of rules"));
  lpanel.add(number);
  lpanel.add(new XLabel("Type of selection"));
  lpanel.add(opanel);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("Parameters selection for Nauck algorithm"));
  content.add(Box.createVerticalStrut(5));
  content.add(lpanel);
  content.add(Box.createVerticalStrut(5));
  content.add(form);

  setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
  addWindowListener(this);
  pack();
  setLocation();
 }

 //-------------------------------------------------------------//
 // Coloca la ventana en la pantalla				//
 //-------------------------------------------------------------//

 private void setLocation() {
  Dimension frame = getSize();
  Dimension screen = getToolkit().getScreenSize();
  setLocation((screen.width - frame.width)/2,(screen.height - frame.height)/2);
 }

 //-------------------------------------------------------------//
 // Actualiza los datos de estilo del sistema		 	//
 //-------------------------------------------------------------//

 private void set() {
  number.setText(""+nauck.getNumberOfRules());
  int index = (nauck.isGlobal() ? 0 : 1);
  global[index].setSelected(true);
 }

 //-------------------------------------------------------------//
 // Detecta posibles errores en el nombre de la base de reglas	//
 //-------------------------------------------------------------//

 private boolean get() {
  try { nauck.setNumberOfRules(Integer.parseInt(number.getText().trim())); }
  catch(Exception ex) {
   number.setText("");
   XDialog.showMessage(number,"Not a valid value");
   return false;
  }
  nauck.setGlobal(global[0].isSelected());
  return true;
 }

 //=============================================================//
 //		Acciones de los botones de la ventana		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Accion de almacenar los datos y cerrar la ventana		//
 //-------------------------------------------------------------//

 private void actionSet() {
  try { get(); } catch(Exception ex) { return; }
  this.conf = true;
  setVisible(false); 
 }

 //-------------------------------------------------------------//
 // Accion de cerrar la ventana					//
 //-------------------------------------------------------------//

 private void actionCancel() {
  this.conf = false;
  setVisible(false); 
 }

}


