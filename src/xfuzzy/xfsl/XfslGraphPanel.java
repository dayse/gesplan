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
//   PANEL DE REPRESENTACION DE LA EVOLUCION DEL APRENDIZAJE	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;

public class XfslGraphPanel extends JPanel {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603068L;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PRIVADAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private final static int HEIGHT = 300;
 private final static double log10 = Math.log(10.0);  

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private Graphics2D gc, gd;
 private Graphics2D gtrn_error, gtrn_rmse, gtrn_mxae;
 private Graphics2D gtst_error, gtst_rmse, gtst_mxae;
 private Dimension size;
 private int x0, y0, x1, y1;
 private int yresc = 3;
 private int xresc = 0;
 private int xscale = 20;
 private int pos = 0;
 private XfslStatus[] hist = new XfslStatus[130];
 private Xfsl xfsl;
 private boolean classif;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslGraphPanel(Xfsl xfsl) {
  super();
  this.xfsl = xfsl;
  Dimension prefsize = new Dimension(getPreferredSize().width,HEIGHT);
  setPreferredSize(prefsize);
  setBackground(XConstants.textbackground);
  setBorder(BorderFactory.createLoweredBevelBorder());
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Limpiar la representacion					//
 //-------------------------------------------------------------//

 public void reset() {
  yresc = 3;
  xresc = 0;
  xscale = 20;
  pos = 0;
  repaint();
 }

 //-------------------------------------------------------------//
 // Annadir un dato a la representacion				//
 //-------------------------------------------------------------//

 public void addStatus(XfslStatus status) {
  int step = xscale/20;
  if( (status.epoch%step) != 0) return;
  paintStatus(status);
  hist[pos] = status;
  pos++;
 }

 //-------------------------------------------------------------//
 // Pintar el panel						//
 //-------------------------------------------------------------//

 public void paint(Graphics g) {
  super.paint(g);
  setVars(g);
  paintLeyend();
  paintAxis();
  paintHist();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Calcula las variables internas				//
 //-------------------------------------------------------------//

 private void setVars(Graphics g){
  this.gc = (Graphics2D) g;
  this.gtrn_error = (Graphics2D) g.create(); gtrn_error.setColor(Color.blue);
  this.gtrn_rmse = (Graphics2D) g.create(); gtrn_rmse.setColor(Color.red);
  this.gtrn_mxae = (Graphics2D) g.create(); gtrn_mxae.setColor(Color.green);

  float dash[] = {4.0f,4.0f};
  this.gd = (Graphics2D) g.create();
  this.gd.setStroke(new BasicStroke(1.0f,2,0,10.0f,dash,0.0f));
  this.gtst_error=(Graphics2D) gd.create(); gtst_error.setColor(Color.blue);
  this.gtst_rmse =(Graphics2D) gd.create(); gtst_rmse.setColor(Color.red);
  this.gtst_mxae =(Graphics2D) gd.create(); gtst_mxae.setColor(Color.green);

  this.size = getSize();
  this.x0 = 170;
  this.y0 = size.height - 50;
  this.x1 = size.width - 110;
  this.y1 = 30;
 }

 //-------------------------------------------------------------//
 // Dibuja la leyenda de la representacion grafica		//
 //-------------------------------------------------------------//

 private void paintLeyend() {
  this.classif = xfsl.getConfig().errorfunction.isClassification();
  if(!classif) {
   gc.drawString("Training", 15, 40);
   gc.drawString("Error", 20, 60);
   gc.drawString("RMSE", 20, 80);
   gc.drawString("MxAE", 20, 100);
   gc.drawString("Test", 15, 140);
   gc.drawString("Error", 20, 160);
   gc.drawString("RMSE", 20, 180);
   gc.drawString("MxAE", 20, 200);
   gtrn_error.drawLine(60, 55, 100, 55);
   gtrn_rmse.drawLine(60, 75, 100, 75);
   gtrn_mxae.drawLine(60, 95, 100, 95);
   gtst_error.drawLine(60,155, 100,155);
   gtst_rmse.drawLine(60,175, 100,175);
   gtst_mxae.drawLine(60,195, 100,195);
  } else {
   gc.drawString("Training", 15, 40);
   gc.drawString("Error", 20, 60);
   gc.drawString("Missc.", 20, 80);
   gc.drawString("Test", 15, 140);
   gc.drawString("Error", 20, 160);
   gc.drawString("Missc.", 20, 180);
   gtrn_error.drawLine(60, 55, 100, 55);
   gtrn_rmse.drawLine(60, 75, 100, 75);
   gtst_error.drawLine(60,155, 100,155);
   gtst_rmse.drawLine(60,175, 100,175);
  }
 }

 //-------------------------------------------------------------//
 // Dibuja los ejes de la representacion grafica		//
 //-------------------------------------------------------------//

 private void paintAxis() {
  gc.drawString("Iteration", size.width-90, size.height-20);
  gc.drawLine(x0, y1, x0, y0+1);
  gc.drawLine(x0, y0+1, x1, y0+1);
  gc.drawLine(x1, y1, x1, y0+1);
  FontMetrics fm = getFontMetrics(getFont());

  for(int i=0 ; i<yresc ; i++) {
   int ypos = y1 + (y0-y1)*i/yresc;
   String label = "1.0"+(i==0?"":"E-"+i);
   int xpos = fm.stringWidth(label)+10;
   gc.drawLine(x0-4, ypos, x0, ypos);
   gc.drawString(label, x0-xpos, ypos+5);
   gd.drawLine(x0, ypos, x1, ypos);
  }
  for(int i=0 ; i<=5 ; i++) {
   int iter = i*xscale;
   int xpos = x0 + (x1-x0)*i/5;
   int spos = fm.stringWidth(""+iter)/2;
   gc.drawLine(xpos, y0+5, xpos, y0+1);
   gc.drawString(""+iter,xpos-spos, y0+21);
  }
 }

 //-------------------------------------------------------------//
 // Dibuja la evolucion historica				//
 //-------------------------------------------------------------//

 private void paintHist() {
  XfslGraphPoint p0, p1;
  if(pos == 0) return;
  p0 = createGraphPoint(hist[0]);
  for(int i=1; i<pos; i++) {
   p1 = createGraphPoint(hist[i]);
   paintPoints(p0,p1);
   p0 = p1;
  }
 }

 //-------------------------------------------------------------//
 // Dibuja un nuevo punto en la evolucion			//
 //-------------------------------------------------------------//

 private void paintStatus(XfslStatus status) {
  if(pos == 0) return;
  setVars(getGraphics());
  XfslGraphPoint p0 = createGraphPoint(hist[pos-1]);
  XfslGraphPoint p1 = createGraphPoint(status);
  if(verticalOutOfBounds(p0) || verticalOutOfBounds(p1) ) {
   yresc++;
   repaint();
  }
  else if(horizontalOutOfBounds(p1)) {
   rescale();
   repaint();
  }
  else paintPoints(p0,p1);
 }

 //-------------------------------------------------------------//
 // Dibuja un segmento en la evolucion				//
 //-------------------------------------------------------------//

 private void paintPoints(XfslGraphPoint p0, XfslGraphPoint p1) {
  if(p0.testing && p1.testing) {
   if(!classif) gtst_mxae.drawLine(p0.x, p0.ysm, p1.x, p1.ysm );
   gtst_rmse.drawLine(p0.x, p0.ysr, p1.x, p1.ysr );
   gtst_error.drawLine(p0.x,p0.yse, p1.x, p1.yse );
  }
  if(!classif) gtrn_mxae.drawLine(p0.x, p0.yrm, p1.x, p1.yrm );
  gtrn_rmse.drawLine(p0.x, p0.yrr, p1.x, p1.yrr );
  gtrn_error.drawLine(p0.x,p0.yre, p1.x, p1.yre );
 }

 //-------------------------------------------------------------//
 // Aumenta en un grado la escala temporal			//
 //-------------------------------------------------------------//

 private void rescale() {
  for(int i=0; 2*i<pos && 2*i<hist.length; i++) hist[i] = hist[2*i];
  pos = pos/2;
  xresc++;
  xscale=20;
  for(int i=0; i<xresc ; i++) if(i%3 != 0) xscale*=2; else xscale=xscale*5/2;
 }

 //-------------------------------------------------------------//
 // Construye un punto de la evolucion del aprendizaje		//
 //-------------------------------------------------------------//

 private XfslGraphPoint createGraphPoint(XfslStatus status) {
  XfslGraphPoint p = new XfslGraphPoint();
  p.x = (int) (x0 + (x1-x0)*status.epoch/(5*xscale) );
  p.yre = ypos(status.trn.error);
  p.yrr = ypos(status.trn.rmse);
  p.yrm = ypos(status.trn.mxae);
  p.testing = status.testing;
  if(p.testing) {
   p.yse = ypos(status.tst.error);
   p.ysr = ypos(status.tst.rmse);
   p.ysm = ypos(status.tst.mxae);
  }
  return p;
 }

 //-------------------------------------------------------------//
 // Calcula la posicion de un valor sobre la coordenada Y	//
 //-------------------------------------------------------------//

 private int ypos(double value) {
  if(value <= 1e-10) return y0;
  return (int) (y1 - (y0 - y1)*Math.log(value)/(yresc * log10) );
 }

 //-------------------------------------------------------------//
 // Detecta si un punto se sale de los limites de la grafica	//
 //-------------------------------------------------------------//

 private boolean verticalOutOfBounds(XfslGraphPoint p) {
  if(p.yre>y0 || p.yrr>y0 || p.yrm>y0) return true;
  if(p.testing && (p.yse>y0 || p.ysr>y0 || p.ysm>y0)) return true;
  return false;
 }

 //-------------------------------------------------------------//
 // Detecta si un punto se sale de los limites de la grafica	//
 //-------------------------------------------------------------//

 private boolean horizontalOutOfBounds(XfslGraphPoint p) {
  return (p.x>x1);
 }
}

