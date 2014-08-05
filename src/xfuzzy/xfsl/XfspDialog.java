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
//   VENTANA DE SELECCION DE LOS PROCESOS DE SIMPLIFICACION	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XfspDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603072L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private XfspProcess process;
 private XTextForm text[];
 private boolean selected;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfspDialog(JFrame frame, XfspProcess process) {
  super(frame, "Xfsl", true);
  this.process = process;
  this.selected = false;
  build(frame);
  pack();
  set();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Interfaz ActionListener					//
 //-------------------------------------------------------------//

 public void actionPerformed(ActionEvent e) {
  String command = e.getActionCommand();
  if(command.equals("Set")) {
   if(!get()) XDialog.showMessage(null,"Values introduced are not correct");
   else { selected = true; setVisible(false); }
  }
  else if(command.equals("Unset")) {
   this.process.setPruning(new XfspPruning());
   this.process.setClustering(new XfspClustering());
   selected = true;
   setVisible(false);
  }
  else if(command.equals("Cancel")) {
   selected = false;
   setVisible(false);
  }
 }

 //-------------------------------------------------------------//
 // Verifica que se ha seleccionado el proceso			//
 //-------------------------------------------------------------//

 public boolean isSelected() {
  return selected;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Generar el contenido de la ventana				//
 //-------------------------------------------------------------//

 private void build(JFrame frame) {
  String ptext[] = { "Off", "Prune all rules under threshold (R)",
                   "Prune worst (N) rules", "Prune all rules except best (N)" };
  String plabel[] = { "", "Threshold", "No. rules", "No. rules" };
  String ctext[] = { "Off", "Best no. clusters", "Selected no. clusters" };
  String clabel[] = { "", "", "No. clusters" };

  boolean preproc = (process.getKind() == XfspProcess.PREPROCESSING);
  String lb = (preproc? "Preprocessing" : "Postprocessing");
  text = new XTextForm[2];
  text[0] = new XTextForm("Pruning", ptext, plabel );
  text[1] = new XTextForm("Clustering   ", ctext, clabel);
  XTextForm.setWidth(text);

  String label[] = { "Set", "Unset", "Cancel" };
  XCommandForm form = new XCommandForm(label,label,this);
  form.setCommandWidth(100);

  Container content = getContentPane();
  content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
  content.add(new XLabel(lb));
  content.add(text[0]);
  content.add(text[1]);
  content.add(form);

  Point loc = frame.getLocationOnScreen();
  loc.x += 40; loc.y += 200;
  setLocation(loc);
 }

 //-------------------------------------------------------------//
 // Actualiza el contenido de los campos			//
 //-------------------------------------------------------------//

 private void set() {
  int pruning = process.getPruning().getKind();
  double prune_param = process.getPruning().getParam();

  text[0].setSelection( pruning );
  if( pruning == XfspPruning.NONE ) prune_param = -1;
  if(prune_param<=0) text[0].setParameter("");
  else if(pruning == XfspPruning.PRUNE_THRESHOLD)
   text[0].setParameter(""+prune_param);
  else text[0].setParameter(""+ (int) prune_param);

  int clustering = process.getClustering().getKind();
  int clusters = process.getClustering().getParam();

  text[1].setSelection( clustering );
  if(clustering != XfspClustering.CLUST_SELECTED) clusters = -1;
  if(clusters <= 0) text[1].setParameter("");
  else if(clustering == XfspClustering.CLUST_SELECTED)
    text[1].setParameter(""+clusters);
 }

 //-------------------------------------------------------------//
 // Actualiza la configuracion con los datos de los campos	//
 //-------------------------------------------------------------//

 private boolean get() {
  boolean good = true;
  boolean test;
  int pruning;
  double prune_param = -1;
  int clustering, clusters;

  pruning = text[0].getSelection();
  switch(pruning) {
   case XfspPruning.PRUNE_THRESHOLD:
    test = true;
    try { prune_param = Double.parseDouble(text[0].getParameter()); }
    catch(NumberFormatException nfe) { test = false; }
    if(prune_param <= 0 || prune_param >= 1) test = false;
    if(!test) { text[0].setParameter(""); good = false; }
    else process.setPruning(new XfspPruning(pruning,prune_param));
    break;
   case XfspPruning.PRUNE_WORST:
   case XfspPruning.PRUNE_EXCEPT:
    test = true;
    try { prune_param = Integer.parseInt(text[0].getParameter()); }
    catch(NumberFormatException nfe) { test = false; }
    if(prune_param <= 0) test = false;
    if(!test) { text[0].setParameter(""); good = false; }
    else process.setPruning(new XfspPruning(pruning,prune_param));
  }

  clustering = text[1].getSelection();
  clusters = -1;
  test = true;
  if(clustering == XfspClustering.CLUST_SELECTED) {
   try { clusters = Integer.parseInt(text[1].getParameter()); }
   catch(NumberFormatException nfe) { test = false; }
   if(clusters <= 0 ) test = false;
   if(!test) { text[1].setParameter(""); good = false; }
  }
  if(test) process.setClustering(new XfspClustering(clustering,clusters));

  return good;
 }
}
