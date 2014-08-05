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
//	   VENTANA DE SELECCION DE PARAMETROS A AJUSTAR		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class XfslSettingsDialog extends JDialog 
implements ActionListener, ListSelectionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603070L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private Xfsl xfsl;
 private JList list[] = new JList[4];
 private Type type[];
 private Type typesel;
 private ParametricFunction mfsel;
 private Vector vsett;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslSettingsDialog(Xfsl xfsl) {
  super(xfsl, "Xfsl", true);
  this.xfsl = xfsl;
  this.type = xfsl.getSpec().getTypes();
  build();
  data0();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Interfaz ListSelectionListener				//
 //-------------------------------------------------------------//

 public void valueChanged(ListSelectionEvent e) {
  if(e.getSource() == list[0]) data1();
  if(e.getSource() == list[1]) data2();
 }

 //-------------------------------------------------------------//
 // Interfaz ActionListener					//
 //-------------------------------------------------------------//

 public void actionPerformed(ActionEvent e) {
  String command = e.getActionCommand();
  if(command.equals("Enable")) actionEnable();
  else if(command.equals("Disable")) actionDisable();
  else if(command.equals("Delete")) actionDelete();
  else if(command.equals("Set")) actionSet();
  else if(command.equals("Unset")) actionUnset();
  else if(command.equals("Cancel")) setVisible(false);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Construye la ventana					//
 //-------------------------------------------------------------//

 private void build() {
  String llabel[] = { "Type", "Family/Membership Function", "Parameter" };
  Box hbox = new Box(BoxLayout.X_AXIS);
  for(int i=0; i<3; i++) {
   list[i] = new JList();
   list[i].setBackground(XConstants.textbackground);
   JScrollPane pane = new JScrollPane(list[i]);
   Dimension prefsize = pane.getPreferredSize();
   pane.setPreferredSize(new Dimension(150,prefsize.height));
   Box box = new Box(BoxLayout.Y_AXIS);
   box.add(new XLabel(llabel[i]));
   box.add(pane);
   hbox.add(box);
  }
  list[0].addListSelectionListener(this);
  list[1].addListSelectionListener(this);
  list[3] = new JList();
  list[3].setBackground(XConstants.textbackground);

  String label1[] = { "Enable", "Disable", "Delete" };
  String label2[] = { "Set", "Unset", "Cancel" };
  XCommandForm form1 = new XCommandForm(label1,label1,this);
  XCommandForm form2 = new XCommandForm(label2,label2,this);
  form1.setCommandWidth(140);
  form2.setCommandWidth(140);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel("Learning Settings"));
  content.add(hbox);
  content.add(new XLabel(" "));
  content.add(new JScrollPane(list[3]));
  content.add(form1);
  content.add(form2);

  Point loc = xfsl.getLocationOnScreen();
  loc.x += 40; loc.y += 200;
  setLocation(loc);
  pack();
 }

 //-------------------------------------------------------------//
 // Actualiza el contenido de la lista de tipos			//
 //-------------------------------------------------------------//

 private void data0() {
  Vector v = new Vector();
  v.add("ANY");
  for(int i=0; i<type.length; i++) v.add(type[i].getName());
  list[0].setListData(v);

  vsett = new Vector();
  XfslSetting[] setting = xfsl.getConfig().getSettings();
  for(int i=0; i<setting.length; i++) vsett.addElement(setting[i]);
  list[3].setListData(vsett);
 }

 //-------------------------------------------------------------//
 // Actualiza el contenido de la lista de MFs			//
 //-------------------------------------------------------------//

 private void data1() {
  String selected = (String) list[0].getSelectedValue();
  if(selected == null) return;
  if(selected.equals("ANY")) typesel = null;
  else try {
   int index;
   for(index=0; !type[index].equals(selected); index++);
   typesel = type[index];
  } catch(Exception ex) { typesel = null; }

  Vector v = new Vector();
  v.add("ANY");
  if(typesel != null) {
   Family[] fam = typesel.getFamilies();
   for(int j=0; j<fam.length; j++) v.add(fam[j].toString());
   ParamMemFunc mf[] = typesel.getParamMembershipFunctions();
   for(int j=0; j<mf.length; j++) v.add(mf[j].toString());
  }
  mfsel = null;
  list[1].setListData(v);
  list[2].setListData(new Vector());
 }

 //-------------------------------------------------------------//
 // Actualiza el contenido de la lista de parametros		//
 //-------------------------------------------------------------//

 private void data2() {
  String selected = (String) list[1].getSelectedValue();
  if(selected == null) return;
  if(selected.equals("ANY")) mfsel = null;
  else {
   Family[] fam = typesel.getFamilies();
   for(int i=0; i<fam.length; i++) if(fam[i].equals(selected)) mfsel = fam[i];
   ParamMemFunc mf[] = typesel.getParamMembershipFunctions();
   for(int i=0; i<mf.length; i++) if(mf[i].equals(selected)) mfsel = mf[i];
  }

  Vector v = new Vector();
  v.add("ANY");
  if(mfsel == null) {
   int max = 0;
   if(typesel == null) max = 4;
   else {
    Family[] fam= typesel.getFamilies();
    for(int i=0; i<fam.length; i++) max = Math.max(max,fam[i].get().length);
    ParamMemFunc mf[] = typesel.getParamMembershipFunctions();
    for(int i=0; i<mf.length; i++) max = Math.max(max,mf[i].get().length);
   }
   for(int i=1; i<=max; i++) v.add(""+i);
  }
  else for(int i=1, n=mfsel.get().length; i<=n; i++) v.add(""+i);
  list[2].setListData(v);
 }

 //-------------------------------------------------------------//
 // Accion de crear una seleccion de habilitacion		//
 //-------------------------------------------------------------//

 private void actionEnable() {
  String selected = (String) list[2].getSelectedValue();
  if(selected == null) return;
  int param;
  try { param = Integer.parseInt(selected); }
  catch(Exception ex) { param = XfslSetting.NOT_SELECTED; }
  vsett.addElement(new XfslSetting(typesel,mfsel,param,true));
  list[3].setListData(vsett);
 }

 //-------------------------------------------------------------//
 // Accion de crear una seleccion de deshabilitacion		//
 //-------------------------------------------------------------//

 private void actionDisable() {
  String selected = (String) list[2].getSelectedValue();
  if(selected == null) return;
  int param;
  try { param = Integer.parseInt(selected); }
  catch(Exception ex) { param = XfslSetting.NOT_SELECTED; }
  vsett.addElement(new XfslSetting(typesel,mfsel,param,false));
  list[3].setListData(vsett);
 }

 //-------------------------------------------------------------//
 // Accion de eliminar una seleccion				//
 //-------------------------------------------------------------//

 private void actionDelete() {
  XfslSetting selected = (XfslSetting) list[3].getSelectedValue();
  if(selected == null) return;
  vsett.removeElement(selected);
  list[3].setListData(vsett);
 }

 //-------------------------------------------------------------//
 // Accion de aceptar el conjunto de selecciones		//
 //-------------------------------------------------------------//

 private void actionSet() {
  Object[] obj = vsett.toArray();
  XfslSetting setting[] = new XfslSetting[obj.length];
  for(int i=0; i<obj.length; i++) setting[i] = (XfslSetting) obj[i];
  xfsl.getConfig().setSettings(setting);
  setVisible(false);
 }

 //-------------------------------------------------------------//
 // Accion de rechazar el conjunto de selecciones		//
 //-------------------------------------------------------------//

 private void actionUnset() {
  xfsl.getConfig().setSettings(new XfslSetting[0]);
  setVisible(false);
 }
}

