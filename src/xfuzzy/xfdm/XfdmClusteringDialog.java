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
//	VENTANA DE CONFIGURACION DEL CLUSTERING FIJO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XfdmClusteringDialog extends JDialog implements ActionListener,
WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603012L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 private XfdmFixedClustering algorithm;
 private XTextField[] text;
 private JComboBox combo;
 private JRadioButton learning;
 private boolean conf;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 public XfdmClusteringDialog(Xfdm xfdm, XfdmAlgorithm alg){
  super(xfdm,"Xfdm",true);
  if(alg != null && alg instanceof XfdmFixedClustering) {
   this.algorithm = (XfdmFixedClustering) ((XfdmFixedClustering) alg).clone();
  } else {
   this.algorithm = new XfdmFixedClustering();
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

 public static XfdmFixedClustering showDialog(Xfdm xfdm,XfdmAlgorithm algorithm) {
  XfdmClusteringDialog dialog = new XfdmClusteringDialog(xfdm, algorithm);
  dialog.setVisible(true);
  return dialog.getAlgorithm();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el algoritmo					//
 //-------------------------------------------------------------//

 public XfdmFixedClustering getAlgorithm() {
  if(this.conf) return this.algorithm;
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

  combo = new JComboBox();
  combo.setBackground(XConstants.textbackground);
  combo.setEditable(false);
  combo.setFont(XConstants.textfont);
  combo.addItem("Hard C-Means");
  combo.addItem("Fuzzy C-Means");
  combo.addItem("Gustafson-Kessel");
  combo.addItem("Gath-Geva");

  text = new XTextField[4];
  text[0] = new XTextField("");
  text[1] = new XTextField("");
  text[2] = new XTextField("");
  text[3] = new XTextField("");

  learning = new JRadioButton("Activate");
  Box lrnbox = new Box(BoxLayout.X_AXIS);
  lrnbox.add(Box.createHorizontalStrut(20));
  lrnbox.add(learning);
  lrnbox.add(Box.createHorizontalGlue());

  JPanel panel = new JPanel();
  panel.setLayout(new GridLayout(6,2));
  panel.add(new XLabel("Clustering algorithm"));
  panel.add(combo);
  panel.add(new XLabel("Number of clusters"));
  panel.add(text[0]);
  panel.add(new XLabel("Limit on iterations"));
  panel.add(text[1]);
  panel.add(new XLabel("Fuzziness index"));
  panel.add(text[2]);
  panel.add(new XLabel("Limit on cluster variation"));
  panel.add(text[3]);
  panel.add(new XLabel("Learning option"));
  panel.add(lrnbox);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("Parameter selection for Fixed Clustering"));
  content.add(Box.createVerticalStrut(5));
  content.add(panel);
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
  combo.setSelectedIndex(algorithm.getClustering());
  text[0].setText(""+algorithm.getNumberOfClusters());
  text[1].setText(""+algorithm.getNumberOfIterations());
  text[2].setText(""+algorithm.getFuzziness());
  text[3].setText(""+algorithm.getEpsilon());
  learning.setSelected(algorithm.getLearning());
 }

 //-------------------------------------------------------------//
 // Detecta posibles errores en el nombre de la base de reglas	//
 //-------------------------------------------------------------//

 private boolean get() {
  boolean error = false;
  int num_cluster = -1;
  if(text[0].getText().trim().length() > 0) {
   try { num_cluster = Integer.parseInt(text[0].getText().trim()); }
   catch(Exception ex) { error = true; text[0].setText(""); }
  }
  if(num_cluster <= 0) error = true;

  int iteration = -1;
  if(text[1].getText().trim().length() > 0) {
   try { iteration = Integer.parseInt(text[1].getText().trim()); }
   catch(Exception ex) { error = true; text[1].setText(""); }
  }
  if(iteration <= 0) error = true;

  double fuzziness = -1;
  if(text[2].getText().trim().length() > 0) {
   try { fuzziness = Double.parseDouble(text[2].getText().trim()); }
   catch(Exception ex) { error = true; text[2].setText(""); }
  }
  if(fuzziness <= 1.0) error = true;

  double epsilon = -1;
  if(text[3].getText().trim().length() > 0) {
   try { epsilon = Double.parseDouble(text[3].getText().trim()); }
   catch(Exception ex) { error = true; text[3].setText(""); }
  }
  if(epsilon <= 0 || epsilon >= 1.0) error = true;

  if(error) {
   XDialog.showMessage(text[0],"Not a valid value");
   return false;
  }

  algorithm.setClustering(combo.getSelectedIndex());
  algorithm.setNumberOfClusters(num_cluster);
  algorithm.setNumberOfIterations(iteration);
  algorithm.setFuzziness(fuzziness);
  algorithm.setEpsilon(epsilon);
  algorithm.setLearning(learning.isSelected());
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


