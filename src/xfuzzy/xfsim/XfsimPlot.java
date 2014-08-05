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


package xfuzzy.xfsim;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase que desarrolla un panel de representación gráfica de dos
 * variables de la simulación
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimPlot extends JPanel implements WindowListener {
	
	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603061L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	/**
	 * Variable a representar en el eje X
	 */
	private int xvar;
	
	/**
	 * Vaariable a representar en el eje Y
	 */
	private int yvar;
	
	/**
	 * Tipo de representación (con puntos, círculos, líneas, etc.)
	 */
	private int kind;
	
	/**
	 * Nombre de la variable a representar en el eje X
	 */
	private String xname;
	
	/**
	 * Nombre de la variable a representar en el eje Y
	 */
	private String yname;
	
	/**
	 * PValores límite de las variables
	 */
	private double xmin,xmax,ymin,ymax;
	
	/**
	 * Marco del panel
	 */
	private JDialog frame;
	
	/**
	 * Posición de los ejes
	 */
	private int x0,x1,y0,ym,y1;
	
	/**
	 * Componente gráfico
	 */
	private Graphics g;
	
	/**
	 * Descripción de la fuente
	 */
	private FontMetrics fm;
	
	/**
	 * Valores de la fuente
	 */
	private int ascent, fmheight;
	
	/**
	 * Valores históricos
	 */
	private double[] xhist, yhist;
	
	/**
	 * Valores previos para la representación con líneas
	 */
	private int xprev, yprev;
	
	/**
	 * Número de reescalados del eje X
	 */
	private int xresc;
	
	/**
	 * Índice de los datos a almacenar
	 */
	private int index;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public XfsimPlot(Xfsim xfsim) {
		this.xfsim = xfsim;
		this.xvar = -1;
		this.yvar = -1;
		setBackground(XConstants.textbackground);
	}
	
	/**
	 * Constructor
	 * @param xfsim
	 * @param xst
	 * @param yst
	 * @param kind
	 */
	public XfsimPlot(Xfsim xfsim, String xst, String yst, int kind) {
		this.xfsim = xfsim;
		this.kind = kind;
		this.xvar = -1;
		this.yvar = -1;
		setBackground(XConstants.textbackground);
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		
		if(xst.equals("_n"))
		{ this.xvar = 0; this.xname = "_n"; this.xmin = 0; this.xmax = 100; }
		if(xst.equals("_t"))
		{ this.xvar = 1; this.xname = "_t"; this.xmin = 0; this.xmax = 10; }
		for(int i=0; i<output.length; i++) if(xst.equals(output[i].getName())) {
			this.xvar = i+2;
			this.xname = output[i].getName();
			this.xmin = output[i].point(0.0);
			this.xmax = output[i].point(1.0);
		}
		for(int i=0; i<input.length; i++) if(xst.equals(input[i].getName())) {
			this.xvar = i+output.length+2;
			this.xname = input[i].getName();
			this.xmin = input[i].point(0.0);
			this.xmax = input[i].point(1.0);
		}
		
		for(int i=0; i<output.length; i++) if(yst.equals(output[i].getName())) {
			this.yvar = i;
			this.yname = output[i].getName();
			this.ymin = output[i].point(0.0);
			this.ymax = output[i].point(1.0);
		}
		for(int i=0; i<input.length; i++) if(yst.equals(input[i].getName())) {
			this.yvar = i+output.length;
			this.yname = input[i].getName();
			this.ymin = input[i].point(0.0);
			this.ymax = input[i].point(1.0);
		}
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 *  Devuelve el índice de la variable X
	 */
	public int getXvar() {
		return this.xvar;
	}
	
	/**
	 * Devuelve el índice de la variable Y
	 */
	public int getYvar() {
		return this.yvar;
	}
	
	/**
	 * Devuelve el tipo de representación
	 */
	public int getKind() {
		return this.kind;
	}
	
	/**
	 * Verifica que la configuración sea correcta
	 */
	public boolean isCorrect() {
		return (xvar >= 0 && yvar >= 0 && kind >= 0 && kind <= 5);
	}
	
	/**
	 * Configura la representación gráfica
	 */
	public void setVar(int xvar, int yvar, int kind) {
		this.xvar = xvar;
		this.yvar = yvar;
		this.kind = kind;
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		if(xvar == 0) { this.xname = "_n"; this.xmin = 0; this.xmax = 100; }
		else if(xvar == 1) { this.xname = "_t"; this.xmin = 0; this.xmax = 10; }
		else if(xvar < output.length+2) {
			this.xname = output[xvar-2].getName();
			this.xmin = output[xvar-2].point(0.0);
			this.xmax = output[xvar-2].point(1.0);
		} else {
			this.xname = input[xvar-output.length-2].getName();
			this.xmin = input[xvar-output.length-2].point(0.0);
			this.xmax = input[xvar-output.length-2].point(1.0);
		}
		if(yvar < output.length) {
			this.yname = output[yvar].getName();
			this.ymin = output[yvar].point(0.0);
			this.ymax = output[yvar].point(1.0);
		} else {
			this.yname = input[yvar-output.length].getName();
			this.ymin = input[yvar-output.length].point(0.0);
			this.ymax = input[yvar-output.length].point(1.0);
		}
	}
	
	/**
	 * Genera la ventana con la representación gráfica
	 */
	public void open() {
		this.xhist = new double[100];
		this.yhist = new double[100];
		this.index = -1;
		this.xresc = 0;
		
		frame = new JDialog((Frame) null,toString(),false);
		Container content = frame.getContentPane();
		content.add(this);
		
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setSize(600,450);
		frame.setVisible(true);
	}
	
	/**
	 * Dibuja un nuevo punto de la representación
	 */
	public void iter(double iter, double time, double[] fzst, double[] ptst) {
		this.index++;
		if(index == xhist.length) {
			double xaux[] = new double[2*xhist.length];
			System.arraycopy(xhist,0,xaux,0,xhist.length);
			this.xhist = xaux;
			double yaux[] = new double[2*yhist.length];
			System.arraycopy(yhist,0,yaux,0,yhist.length);
			this.yhist = yaux;
		}
		if(xvar == 0) xhist[index] = iter;
		else if(xvar == 1) xhist[index] = time/1000;
		else if(xvar<fzst.length+2) xhist[index] = fzst[xvar-2];
		else xhist[index] = ptst[xvar-2-fzst.length];
		if(yvar<fzst.length) yhist[index] = fzst[yvar];
		else yhist[index] = ptst[yvar-fzst.length];
		Rectangle r = paintStatus(index);
		if(r != null) repaint(r);
	}
	
	/**
	 * Cierra la ventana de representación gráfica
	 */
	public void close() {
		if(this.frame != null) this.frame.setVisible(false);
	}
	
	/**
	 * Pinta la gráfica completa
	 */
	public void paint(Graphics g) {
		super.paint(g);
		setConstants(g);
		paintAxis();
		paintHist();
	}
	
	/**
	 * Descripción que aparece en la lista de Xfsim
	 */
	public String toString() {
		return "plot("+xname+","+yname+","+kind+")";
	}
	
	/**
	 * Descripción que aparece en los ficheros de configuracion
	 */
	public String toCode() {
		return "xfsim_plot("+xname+","+yname+","+kind+")";
	}
	
	/**
	 * Interfaz WindowListener. Acción al abrir la ventana
	 */
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener. Acción al cerrar la ventana
	 */
	public void windowClosing(WindowEvent e) {
		if(xfsim.isSimulating()) return;
		this.frame.setVisible(false);
	}

	/**
	 * Interfaz WindowListener. Acción al finalizar el cierre
	 */
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener. Acción al minimizar la ventana
	 */
	public void windowIconified(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener. Acción al maximizar la ventana
	 */
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * Interfaz WindowListener. Acción al activar la ventana
	 */
	public void windowActivated(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al desactivar la ventana
	 */
	public void windowDeactivated(WindowEvent e) {
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Inicializa algunas constantes internas
	 */
	private void setConstants(Graphics g) {
		this.g = g;
		this.fm = getFontMetrics(getFont());
		this.ascent = fm.getAscent();
		this.fmheight = fm.getHeight();
		
		Dimension size = getSize();
		this.x0 = 100;
		this.x1 = size.width - 100;
		this.y0 = size.height - 50;
		this.y1 = 30;
		this.ym = (y0+y1)/2;
	}
	
	/**
	 * Pinta los ejes de la representación
	 */
	private void paintAxis() {
		g.drawRect(x0,y1,x1-x0,y0-y1);
		g.drawLine(x0,y1,x0-5,y1);
		g.drawLine(x0,ym,x0-5,ym);
		g.drawLine(x0,y0,x0-5,y0);
		g.drawLine(x1,y1,x1+5,y1);
		g.drawLine(x1,ym,x1+5,ym);
		g.drawLine(x1,y0,x1+5,y0);
		int xdiv = (xvar>1? 2 : 5);
		for(int i=0; i<=xdiv; i++) {
			int lpos = x0+(x1-x0)*i/xdiv;
			g.drawLine(lpos,y0,lpos,y0+5);
		}
		
		String xtitle = xname+(xvar == 1? " (s)": "");
		int yposx = (x0-fm.stringWidth(xtitle))/2;
		int yposy = (y1-fmheight)/2+ascent;
		int xposx = x1+(getSize().width-x1-fm.stringWidth(yname))/2;
		int xposy = y0+(getSize().height-y0)/2+ascent;
		String stymin = ""+ymin;
		String stymax = ""+ymax;
		String stymean = ""+((ymax+ymin)/2);
		g.drawString(xtitle,xposx,xposy);
		g.drawString(yname,yposx,yposy);
		g.drawString(stymax,x0-10-fm.stringWidth(stymax),y1+ascent/2);
		g.drawString(stymean,x0-10-fm.stringWidth(stymean),ym+ascent/2);
		g.drawString(stymin,x0-10-fm.stringWidth(stymin),y0+ascent/2);
		g.drawString(stymax,x1+10,y1+ascent/2);
		g.drawString(stymean,x1+10,ym+ascent/2);
		g.drawString(stymin,x1+10,y0+ascent/2);
		for(int i=0; i<=xdiv; i++) {
			String stval = ""+(xmin+(xmax-xmin)*i/xdiv);
			int stpos = x0+(x1-x0)*i/xdiv-fm.stringWidth(stval)/2;
			g.drawString(stval,stpos,y0+10+ascent);
		}
	}
	
	/**
	 * Pinta un punto de la representación
	 */
	private Rectangle paintStatus(int index) {
		double xvalue = xhist[index];
		double yvalue = yhist[index];
		if(xvalue > xmax && xvar <= 1) {
			xresc++;
			xmax = (xvar ==0? 100 : 10);
			for(int i=0; i<xresc ; i++) if(i%3 != 0) xmax*=2; else xmax=xmax*5/2;
			repaint();
			return null;
		}
		
		int xpos = x0 + (int) ((x1-x0)*(xvalue-xmin)/(xmax-xmin));
		int ypos = y0 + (int) ((y1-y0)*(yvalue-ymin)/(ymax-ymin));
		switch(kind) {
		case 0:
			if(index != 0) g.drawLine(xprev,yprev,xpos,ypos);
			Rectangle r = new Rectangle(xprev,yprev,xpos-xprev,ypos-yprev);
			xprev = xpos;
			yprev = ypos;
			return r;
		case 1:
			g.fillOval(xpos-2,ypos-2,4,4);
			return new Rectangle(xpos-2,ypos-2,4,4);
		case 2:
			g.drawRect(xpos-3,ypos-3,6,6);
			return new Rectangle(xpos-3,ypos-3,6,6);
		case 3:
			g.drawOval(xpos-3,ypos-3,6,6);
			return new Rectangle(xpos-3,ypos-3,6,6);
		case 4:
			g.drawOval(xpos-5,ypos-5,10,10);
			return new Rectangle(xpos-5,ypos-5,10,10);
		case 5:
			g.drawOval(xpos-10,ypos-10,20,20);
			return new Rectangle(xpos-10,ypos-10,20,20);
		default: return null;
		}
	}
	
	/**
	 * Pinta todos los puntos de la representación
	 */
	private void paintHist() {
		for(int i=0; i<index; i++) paintStatus(i);
	}
}
