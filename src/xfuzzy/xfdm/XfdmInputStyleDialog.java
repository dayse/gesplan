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
//    VENTANA DE CONFIGURACION DE LAS VARIABLES DE ENTRADA	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfdm;

import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class XfdmInputStyleDialog extends JDialog implements ActionListener,
ListSelectionListener, ChangeListener, WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603015L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 private Xfdm xfdm;
 private XfdmConfig config;
 private XTextForm text[];
 private XList varlist;
 private JToggleButton toggle[];
 private JRadioButton radio[];
 private JRadioButton universe[];
 private XfdmInputStyle style[];
 private XfdmInputStyle editing;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 
 public XfdmInputStyleDialog(Xfdm xfdm){
  super(xfdm,"Xfdm",true);
  this.xfdm = xfdm;
  this.config = xfdm.getConfig();
  build();
  getCopy();
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
  if(button == radio[0]) { actionSame(); return; }
  if(button == radio[1]) { actionSpecific(); return; }
  if(button == universe[0]) { actionRead(); return; }
  if(button == universe[1]) { actionFixed(); return; }
  if(button.isSelected())
   button.setBorder(BorderFactory.createLoweredBevelBorder());
  else button.setBorder(BorderFactory.createRaisedBevelBorder());
 }

 //-------------------------------------------------------------//
 // Interfaz ListSelecionListener				//
 //-------------------------------------------------------------//

 public void valueChanged(ListSelectionEvent e) {
  int index = varlist.getSelectedIndex();
  if(editing == style[index]) return;
  try { getStyle(); }
  catch(Exception ex) { varlist.setSelectedValue(editing); setStyle(); return; }
  editing = style[index];
  setStyle();
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

  text = new XTextForm[4];
  text[0] = new XTextForm("Variable name");
  text[1] = new XTextForm("Number of MFs");
  text[2] = new XTextForm("Minimum");
  text[3] = new XTextForm("Maximum");
  XTextForm.setWidth(text);

  universe = new JRadioButton[2];
  universe[0] = new JRadioButton("Read from patterns");
  universe[1] = new JRadioButton("Fixed");
  universe[0].addChangeListener(this);
  universe[1].addChangeListener(this);
  ButtonGroup ubg = new ButtonGroup();
  ubg.add(universe[0]);
  ubg.add(universe[1]);
  JPanel upanel = new JPanel();
  upanel.setLayout(new GridLayout(1,2));
  upanel.add(universe[0]);
  upanel.add(universe[1]);

  toggle = new JToggleButton[6];
  JPanel stbox = new JPanel();
  stbox.setLayout(new GridLayout(3,2));
  ButtonGroup stbg = new ButtonGroup();
  ImageIcon icon[] = {
   XfdmIcons.triangles,  XfdmIcons.triangles,
   XfdmIcons.sh_triangles,  XfdmIcons.sh_triangles,
   XfdmIcons.gaussians,  XfdmIcons.splines };

  String togglelabel[] = {
   "Free triangles", "Triangular family",
   "Free shouldered triangles", "Shouldered-triangular family",
   "Free gaussians", "B-splines family" };

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

  Box rbox = new Box(BoxLayout.Y_AXIS);
  rbox.add(text[0]);
  rbox.add(text[1]);
  rbox.add(new XLabel("Universe of discourse"));
  rbox.add(upanel);
  rbox.add(text[2]);
  rbox.add(text[3]);
  rbox.add(Box.createVerticalStrut(5));
  rbox.add(new XLabel("Style"));
  rbox.add(stbox);

  radio = new JRadioButton[2];
  radio[0] = new JRadioButton("same for all variables");
  radio[1] = new JRadioButton("individually for each variable");
  radio[0].addChangeListener(this);
  radio[1].addChangeListener(this);

  JPanel rpanel = new JPanel();
  rpanel.setLayout(new GridLayout(2,1));
  rpanel.add(radio[0]);
  rpanel.add(radio[1]);
  
  ButtonGroup rbg = new ButtonGroup();
  rbg.add(radio[0]);
  rbg.add(radio[1]);
  varlist = new XList("Input variables");
  varlist.setPreferredWidth(130);
  varlist.addListSelectionListener(this);

  Box lbox = new Box(BoxLayout.Y_AXIS);
  lbox.add(rpanel);
  lbox.add(Box.createVerticalStrut(5));
  lbox.add(varlist);

  Box box = new Box(BoxLayout.X_AXIS);
  box.add(Box.createHorizontalStrut(5));
  box.add(lbox);
  box.add(Box.createHorizontalStrut(5));
  box.add(rbox);
  box.add(Box.createHorizontalStrut(5));

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("Input Style"));
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
 // Genera la copia de trabajo					//
 //-------------------------------------------------------------//

 private void getCopy() {
  style = new XfdmInputStyle[config.numinputs+1];
  if(config.commonstyle == null) style[0] = new XfdmInputStyle();
  else style[0] = new XfdmInputStyle(config.commonstyle);
  if(config.inputstyle == null || config.inputstyle.length == 0) {
   for(int i=0; i<config.numinputs; i++) {
    style[i+1] = new XfdmInputStyle("i"+i);
   }
  } else {
   for(int i=0; i<config.numinputs; i++) {
    if(i<config.inputstyle.length) {
     style[i+1] = new XfdmInputStyle(config.inputstyle[i]);
    } else {
     style[i+1] = new XfdmInputStyle(style[0]);
     style[i+1].name = "i"+i;
    }
   }
  }

  varlist.setListData(style);
  if(config.inputstyle != null && config.inputstyle.length > 0) {
   radio[1].setSelected(true);
   varlist.setEnabled(true);
  } else {
   radio[0].setSelected(true);
   varlist.setEnabled(false);
  }
  this.editing = style[0];
  setStyle();
 }

 //-------------------------------------------------------------//
 // Actualiza los datos de estilo con la variable seleccionada 	//
 //-------------------------------------------------------------//

 private void setStyle() {
  text[0].setText(editing.name);
  text[1].setText(""+editing.mfs);
  toggle[editing.style].setSelected(true);
  if(this.editing == style[0]) text[0].setEditable(false);
  else text[0].setEditable(true);
  if(editing.isUniverseDefined()) {
   universe[1].setSelected(true);
   text[2].setEditable(true);
   text[3].setEditable(true);
   text[2].setText(""+editing.min);
   text[3].setText(""+editing.max);
  } else {
   universe[0].setSelected(true);
   text[2].setEditable(false);
   text[3].setEditable(false);
   text[2].setText("");
   text[3].setText("");
  }
  setConstraints();
 }

 //-------------------------------------------------------------//
 // Considera las restricciones que imponen los algoritmos	//
 //-------------------------------------------------------------//

 private void setConstraints() {
  if(config.algorithm == null) return;
  if(config.algorithm instanceof XfdmIncGrid) {
   text[1].setText("");
   text[1].setEditable(false);
   if(editing.style != XfdmInputStyle.FREE_TRIANGLES &&
      editing.style != XfdmInputStyle.TRIANGULAR_FAMILY) {
    editing.style = XfdmInputStyle.FREE_TRIANGLES;
   }
   toggle[editing.style].setSelected(true);
   toggle[XfdmInputStyle.FREE_SH_TRIANGLES].setEnabled(false);
   toggle[XfdmInputStyle.SH_TRIANGULAR_FAMILY].setEnabled(false);
   toggle[XfdmInputStyle.FREE_GAUSSIANS].setEnabled(false);
   toggle[XfdmInputStyle.BSPLINES_FAMILY].setEnabled(false);
  } 
  if(config.algorithm instanceof XfdmIncClustering ||
     config.algorithm instanceof XfdmFixedClustering) {
   text[1].setText("");
   text[1].setEditable(false);
   editing.style = XfdmInputStyle.FREE_GAUSSIANS;
   toggle[editing.style].setSelected(true);
   for(int i=0; i<toggle.length; i++) toggle[i].setEnabled(false);
  } 
 }

 //-------------------------------------------------------------//
 // Actualiza la variable seleccionada con los datos de estilo	//
 //-------------------------------------------------------------//

 private void getStyle() throws Exception {
  int mfs = 3;
  if(config.algorithm != null &&
     !(config.algorithm instanceof XfdmIncGrid) &&
     !(config.algorithm instanceof XfdmIncClustering) &&
     !(config.algorithm instanceof XfdmFixedClustering)) {
   mfs = getIntegerValue();
  }
  String id = getIdentifier();
  double min = 0;
  double max = 0;

  if(id == null) throw new Exception();
  if(mfs == -1) throw new Exception();
  if(universe[1].isSelected()) {
   try { min = Double.parseDouble(text[2].getText()); }
   catch(NumberFormatException ex) {
    text[2].setText("");
    XDialog.showMessage(text[2],"Not a numeric value");
    throw new Exception();
   }
   try { max = Double.parseDouble(text[3].getText()); }
   catch(NumberFormatException ex) {
    text[3].setText("");
    XDialog.showMessage(text[3],"Not a numeric value");
    throw new Exception();
   }
   if(min >= max) {
    XDialog.showMessage(text[2],"Not a valid universe");
    throw new Exception();
   }
  }

  editing.name = id;
  editing.mfs = mfs;
  editing.min = min;
  editing.max = max;

  for(int i=0;i<toggle.length;i++) if(toggle[i].isSelected()) editing.style = i;
 }

 //-------------------------------------------------------------//
 // Detecta posibles errores en el nombre de la variable	//
 //-------------------------------------------------------------//

 private String getIdentifier() {
  String id = text[0].getText().trim();
  if(!XConstants.isIdentifier(id)) {
   text[0].setText("");
   XDialog.showMessage(text[0],"Not a valid identifier");
   return null;
  }
  for(int i=0; i<style.length; i++) {
   if(style[i] != editing && style[i].name.equals(id)) {
    text[0].setText("");
    XDialog.showMessage(text[0],"Identifier already used");
    return null;
   }
  }
  return id;
 }

 //-------------------------------------------------------------//
 // Detecta posibles errores en el numero de MFs		//
 //-------------------------------------------------------------//

 private int getIntegerValue() {
  int val = -1;
  try { val = Integer.parseInt(text[1].getText()); }
  catch(NumberFormatException ex) {
   text[1].setText("");
   XDialog.showMessage(text[1],"Not a numeric value");
   return -1;
  }
  if(val <= 0) {
   text[1].setText("");
   XDialog.showMessage(text[1],"Not a valid value");
   return -1;
  }
  return val;
 }

 //=============================================================//
 //		Acciones de los botones de la ventana		//
 //=============================================================//

 //-------------------------------------------------------------//
 // Accion de cambiar el estilo a comun				//
 //-------------------------------------------------------------//

 private void actionSame() {
  if(editing != style[0]) { editing = style[0]; setStyle(); }
  varlist.setSelectedIndex(0);
  varlist.setEnabled(false);
 }

 //-------------------------------------------------------------//
 // Accion de cambiar el estilo a especifico			//
 //-------------------------------------------------------------//

 private void actionSpecific() {
  varlist.setEnabled(true);
 }

 //-------------------------------------------------------------//
 // Accion de cambiar el universo a automatico			//
 //-------------------------------------------------------------//

 private void actionRead() {
  text[2].setEditable(false);
  text[3].setEditable(false);
  text[2].setText("");
  text[3].setText("");
 }

 //-------------------------------------------------------------//
 // Accion de cambiar el universo a manual			//
 //-------------------------------------------------------------//

 private void actionFixed() {
  text[2].setEditable(true);
  text[3].setEditable(true);
  text[2].setText("0");
  text[3].setText("1");
 }

 //-------------------------------------------------------------//
 // Accion de almacenar los datos y cerrar la ventana		//
 //-------------------------------------------------------------//

 private void actionSet() {
  try { getStyle(); } catch(Exception ex) { return; }
  if(radio[0].isSelected()) {
   config.commonstyle = new XfdmInputStyle(style[0]);
   config.inputstyle = null;
  } else {
   config.inputstyle = new XfdmInputStyle[style.length-1];
   for(int i=1; i<style.length; i++)
    config.inputstyle[i-1] = new XfdmInputStyle(style[i]);
   config.commonstyle = new XfdmInputStyle(style[0]);
  }
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


