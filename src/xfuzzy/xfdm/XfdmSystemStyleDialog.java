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
//	  VENTANA DE CONFIGURACION DEL ESTILO DE SISTEMA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class XfdmSystemStyleDialog extends JDialog implements ActionListener,
ChangeListener, WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603018L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 private Xfdm xfdm;
 private XfdmConfig config;
 private XTextField name;
 private XTextField output;
 private JToggleButton toggle[];
 private JRadioButton operator[];
 private JRadioButton creation;
 private XfdmSystemStyle editing;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 public XfdmSystemStyleDialog(Xfdm xfdm){
  super(xfdm,"Xfdm",true);
  this.xfdm = xfdm;
  this.config = xfdm.getConfig();
  this.editing = new XfdmSystemStyle(this.config.systemstyle);
  build();
  setStyle();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Interfaz ActionListener					//
 //-------------------------------------------------------------//

 public void actionPerformed(ActionEvent e) {
  String command = e.getActionCommand();
  if(command.equals("Set")) actionSet();
  else if(command.equals("Cancel")) actionCancel();
 }

 //-------------------------------------------------------------//
 // Interfaz ChangeListener					//
 //-------------------------------------------------------------//

 public void stateChanged(ChangeEvent e) {
  AbstractButton button = (AbstractButton) e.getSource();
  if(button.isSelected())
   button.setBorder(BorderFactory.createLoweredBevelBorder());
  else button.setBorder(BorderFactory.createRaisedBevelBorder());
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


  toggle = new JToggleButton[4];
  JPanel stbox = new JPanel();
  stbox.setLayout(new GridLayout(2,2));
  ButtonGroup stbg = new ButtonGroup();
  ImageIcon icon[] = {
   XfdmIcons.singleton,  XfdmIcons.bell,
   XfdmIcons.takagi,  XfdmIcons.singletons };

  String togglelabel[] = {
   "Fuzzy Mean", " Weighted Fuzzy Mean ",
   "Takagi-Sugeno", "Classification" };

  for(int i=0; i<toggle.length; i++) {
   toggle[i] = new JToggleButton(togglelabel[i],icon[i]);
   toggle[i].setFont(XConstants.textfont);
   toggle[i].setVerticalTextPosition(JToggleButton.BOTTOM);
   toggle[i].setHorizontalTextPosition(JToggleButton.CENTER);
   toggle[i].setBorder(BorderFactory.createRaisedBevelBorder());
   toggle[i].setFocusPainted(false);
   toggle[i].setContentAreaFilled(false);
   toggle[i].addChangeListener(this);
   stbg.add(toggle[i]);
   stbox.add(toggle[i]);
  }

  name = new XTextField("");
  output = new XTextField("");

  operator = new JRadioButton[2];
  operator[0] = new JRadioButton("min");
  operator[1] = new JRadioButton("prod");

  Box opanel = new Box(BoxLayout.X_AXIS);
  opanel.add(Box.createHorizontalStrut(10));
  opanel.add(operator[0]);
  opanel.add(operator[1]);
  
  ButtonGroup rbg = new ButtonGroup();
  rbg.add(operator[0]);
  rbg.add(operator[1]);

  creation = new JRadioButton("Generate");
  Box cpanel = new Box(BoxLayout.X_AXIS);
  cpanel.add(Box.createHorizontalStrut(10));
  cpanel.add(creation);

  JPanel lpanel = new JPanel();
  lpanel.setLayout(new GridLayout(4,2));
  lpanel.add(new XLabel("Rule base name"));
  lpanel.add(name);
  lpanel.add(new XLabel("Output name"));
  lpanel.add(output);
  lpanel.add(new XLabel("Conjunction operator"));
  lpanel.add(opanel);
  lpanel.add(new XLabel("System structure"));
  lpanel.add(cpanel);

  Box lbox = new Box(BoxLayout.Y_AXIS);
  lbox.add(lpanel);
  lbox.add(Box.createVerticalGlue());

  Box box = new Box(BoxLayout.X_AXIS);
  box.add(Box.createHorizontalStrut(5));
  box.add(lbox);
  box.add(Box.createHorizontalStrut(5));
  box.add(stbox);
  box.add(Box.createHorizontalStrut(5));

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("System Style"));
  content.add(Box.createVerticalStrut(5));
  content.add(box);
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

 private void setStyle() {
  name.setText(editing.rulebase);
  output.setText(editing.outputname);
  toggle[editing.defuz].setSelected(true);
  operator[editing.conjunction].setSelected(true);
  creation.setSelected(editing.creation);
  toggle[editing.defuz].setSelected(true);
  setConstraints();
 }

 //-------------------------------------------------------------//
 // Considera las restricciones que imponen los algoritmos	//
 //-------------------------------------------------------------//

 private void setConstraints() {
  if(config.algorithm == null) return;
  if(config.algorithm instanceof XfdmNauck ||
     config.algorithm instanceof XfdmSenhadji) {
   editing.defuz = XfdmSystemStyle.CLASSIFICATION;
   toggle[editing.defuz].setSelected(true);
   for(int i=0; i<toggle.length; i++) toggle[i].setEnabled(false);
  }
  if(config.algorithm instanceof XfdmIncGrid) {
   if(editing.defuz != XfdmSystemStyle.FUZZYMEAN &&
      editing.defuz != XfdmSystemStyle.WEIGHTED) {
    editing.defuz=XfdmSystemStyle.FUZZYMEAN;
   }
   toggle[editing.defuz].setSelected(true);
   toggle[XfdmSystemStyle.CLASSIFICATION].setEnabled(false);
   toggle[XfdmSystemStyle.TAKAGI].setEnabled(false);
   editing.creation = true;
   creation.setSelected(editing.creation);
   creation.setEnabled(false);
  }
  if(config.algorithm instanceof XfdmIncClustering) {
   if(editing.defuz == XfdmSystemStyle.CLASSIFICATION) {
    editing.defuz=XfdmSystemStyle.FUZZYMEAN;
   }
   toggle[editing.defuz].setSelected(true);
   toggle[XfdmSystemStyle.CLASSIFICATION].setEnabled(false);
  }
  if(config.algorithm instanceof XfdmFixedClustering) {
   if(editing.defuz == XfdmSystemStyle.CLASSIFICATION) {
    editing.defuz=XfdmSystemStyle.FUZZYMEAN;
   }
   toggle[editing.defuz].setSelected(true);
   toggle[XfdmSystemStyle.CLASSIFICATION].setEnabled(false);
   editing.creation = true;
   creation.setSelected(editing.creation);
   creation.setEnabled(false);
  }
 }

 //-------------------------------------------------------------//
 // Actualiza la variable seleccionada con los datos de estilo	//
 //-------------------------------------------------------------//

 private void getStyle() throws Exception {
  String rbname = getIdentifier(name);
  String oname = getIdentifier(output);
  if(rbname == null || oname == null) throw new Exception();
  editing.rulebase = rbname;
  editing.outputname = oname;
  if(operator[0].isSelected()) editing.conjunction = 0;
  else editing.conjunction = 1;
  for(int i=0;i<toggle.length;i++) if(toggle[i].isSelected()) editing.defuz = i;
  editing.creation = creation.isSelected();
 }

 //-------------------------------------------------------------//
 // Detecta posibles errores en el nombre de la base de reglas	//
 //-------------------------------------------------------------//

 private String getIdentifier(XTextField field) {
  String id = field.getText().trim();
  if(!XConstants.isIdentifier(id)) {
   field.setText("");
   XDialog.showMessage(field,"Not a valid identifier");
   return null;
  }
  return id;
 }

 //=============================================================//
 //		Acciones de los botones de la ventana		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Accion de almacenar los datos y cerrar la ventana		//
 //-------------------------------------------------------------//

 private void actionSet() {
  try { getStyle(); } catch(Exception ex) { return; }
  config.systemstyle = new XfdmSystemStyle(editing);
  xfdm.refresh();
  setVisible(false); 
 }

 //-------------------------------------------------------------//
 // Accion de cerrar la ventana					//
 //-------------------------------------------------------------//

 private void actionCancel() {
  setVisible(false); 
 }

}


