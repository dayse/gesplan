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



package xfuzzy.xfplot;

import xfuzzy.lang.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;

/**
 * Clase que desarrolla el panel de representación gráfica en 3
 * dimensiones
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Xfplot3DPanel extends JPanel {
	
	//----------------------------------------------------------------------------//
	//                            CONSTANTES PRIVADAS                             //
	//----------------------------------------------------------------------------//
	
	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603054L;
	
	/**
	 * Altura del panel
	 */
	private static final int HEIGHT = 500;
	
	/**
	 * Anchura del panel
	 */
	private static final int WIDTH = 700;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Referencia a la página principal de la aplicación
	 */
	private Xfplot xfplot;
	
	/**
	 * Anchura del panel
	 */
	private int width;
	
	/**
	 * Altura del panel
	 */
	private int height; 
	
	/**
	 * Coseno del ángulo de rotación horizontal
	 */
	private double cosalpha;
	
	/**
	 * Seno del ángulo de rotación horizontal
	 */
	private double sinalpha;
	
	/**
	 * Coseno del ángulo de rotación vertical
	 */
	private double cosbeta;
	
	/**
	 * Seno del ángulo de rotación vertical
	 */
	private double sinbeta;
	
	/**
	 * Ángulo de rotación horizontal
	 */
	private double alpha;
	
	/**
	 * Fuente de letra
	 */
	private FontMetrics fm;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfplot3DPanel(Xfplot xfplot) {
		super();
		this.xfplot = xfplot;
		setSize(new Dimension(WIDTH,HEIGHT));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.white);
		setHRotation(0);
		setVRotation(0.3);
		setBorder(BorderFactory.createLoweredBevelBorder());
		this.fm = getFontMetrics(getFont());
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Selecciona el ángulo de rotación horizontal
	 */
	public void setHRotation(double alpha) {
		this.alpha = alpha;
		this.cosalpha = Math.cos(Math.PI * alpha + Math.PI / 4);
		this.sinalpha = Math.sin(Math.PI * alpha + Math.PI / 4);
	}
	
	/**
	 * Selecciona el ángulo de rotación vertical
	 */
	public void setVRotation(double beta) {
		cosbeta = Math.cos(Math.PI * beta / 2);
		sinbeta = Math.sin(Math.PI * beta / 2);
	}
	
	/**
	 * Dibuja el panel con la representación gráfica.
	 * Este método sobreescribe al método paint() de JPanel.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		this.width = getSize().width;
		this.height = getSize().height;
		
		paintBasis(g);
		if(alpha<0) paintZAxis(g);
		if(xfplot.is3DComputed()) paintFunction(g);
		if(alpha>=0) paintZAxis(g);
	}
	
	/**
	 * Almacena la representación gráfica en un fichero externo
	 */
	public boolean saveImage(File file) {
		try {
			BufferedImage bi = (BufferedImage) createImage(width,height);
			Graphics g = getGraphics().create();
			g.drawImage(bi,0,0,this);
			repaint();
			
			FileOutputStream out = new FileOutputStream(file);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
			param.setQuality(1.0f,false);
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bi);
			out.close();
			repaint();
			return true;
		} catch (Exception ex) { return false; }
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Calcula la posición X en el plano de un punto del espacio
	 */
	private int getXPosition(double x, double y, double z) {
		double xpos = (x-0.5)*sinalpha + (y-0.5)*cosalpha;;
		xpos = xpos*0.424*width + width/2;
		return (int) Math.round(xpos);
	}
	
	/**
	 * Calcula la posición Y en el plano de un punto del espacio
	 */
	private int getYPosition(double x, double y, double z) {
		double ypos = (x-0.5)*cosalpha*sinbeta - (y-0.5)*sinalpha*sinbeta;
		ypos = ypos*0.424*width;
		ypos = (z-0.4)*cosbeta*0.3*height - ypos;
		ypos = height/2 - ypos;
		return (int) Math.round(ypos);
	}
	
	/**
	 * Dibuja los ejes de la representación gráfica
	 */
	private void paintBasis(Graphics g) {
		Variable xvar = xfplot.getXVariable();
		Variable yvar = xfplot.getYVariable();
		
		String xmin = ""+xvar.point(0.0);
		String xmax = ""+xvar.point(1.0);
		String ymin = ""+yvar.point(0.0);
		String ymax = ""+yvar.point(1.0);
		
		int xvarwidth = fm.stringWidth(xvar.toString());
		int xminwidth = fm.stringWidth(xmin);
		int xmaxwidth = fm.stringWidth(xmax);
		int yvarwidth = fm.stringWidth(yvar.toString());
		int yminwidth = fm.stringWidth(ymin);
		int ymaxwidth = fm.stringWidth(ymax);
		
		int ascent = fm.getAscent();
		
		int p0x = getXPosition(0,0,-0.2);
		int p0y = getYPosition(0,0,-0.2);
		int p1x = getXPosition(1,0,-0.2);
		int p1y = getYPosition(1,0,-0.2);
		int p2x = getXPosition(1,1,-0.2);
		int p2y = getYPosition(1,1,-0.2);
		int p3x = getXPosition(0,1,-0.2);
		int p3y = getYPosition(0,1,-0.2);
		
		g.drawLine(p0x, p0y, p1x, p1y);
		g.drawLine(p1x, p1y, p2x, p2y);
		g.drawLine(p2x, p2y, p3x, p3y);
		g.drawLine(p3x, p3y, p0x, p0y);
		
		int p4x = getXPosition(0,0,1);
		int p4y = getYPosition(0,0,1);
		int p5x = getXPosition(1,0,0);
		int p5y = getYPosition(1,0,0);
		int p6x = getXPosition(1,1,0);
		int p6y = getYPosition(1,1,0);
		int p7x = getXPosition(0,1,0);
		int p7y = getYPosition(0,1,0);
		
		g.drawLine(p0x, p0y, p4x, p4y);
		g.drawLine(p1x, p1y, p5x, p5y);
		g.drawLine(p2x, p2y, p6x, p6y);
		g.drawLine(p3x, p3y, p7x, p7y);
		
		double yy = (p0x < p1x ? -0.15 : 1.15);
		double yl = (p0x < p1x ? -0.3 : 1.3);
		
		int px0x = getXPosition(0, yy,-0.2);
		int px0y = getYPosition(0, yy,-0.2);
		int pxhx = getXPosition(0.5, yl,-0.2);
		int pxhy = getYPosition(0.5, yl,-0.2);
		int px1x = getXPosition(1, yy,-0.2);
		int px1y = getYPosition(1, yy,-0.2);
		
		g.drawString(""+xvar, pxhx-xvarwidth/2, pxhy+ascent/2);
		g.drawString(xmin, px0x-xminwidth/2, px0y+ascent/2);
		g.drawString(xmax, px1x-xmaxwidth/2, px1y+ascent/2);
		for(int i=0; i<=5; i++) {
			int pxl0x = getXPosition(i*0.2,0,-0.2);
			int pxl0y = getYPosition(i*0.2,0,-0.2);
			int pxl1x = getXPosition(i*0.2,1,-0.2);
			int pxl1y = getYPosition(i*0.2,1,-0.2);
			g.drawLine(pxl0x, pxl0y, pxl1x, pxl1y);
		}
		
		double xx = (p1x > p2x ? -0.15 : 1.15);
		double xl = (p1x > p2x ? -0.3 : 1.3);
		
		int py0x = getXPosition(xx,0,-0.2);
		int py0y = getYPosition(xx,0,-0.2);
		int pyhx = getXPosition(xl,0.5,-0.2);
		int pyhy = getYPosition(xl,0.5,-0.2);
		int py1x = getXPosition(xx,1,-0.2);
		int py1y = getYPosition(xx,1,-0.2);
		
		g.drawString(""+yvar, pyhx-yvarwidth/2, pyhy+ascent/2);
		g.drawString(ymin, py0x-yminwidth/2, py0y+ascent/2);
		g.drawString(ymax, py1x-ymaxwidth/2, py1y+ascent/2);
		for(int i=0; i<=5; i++) {
			int pyl0x = getXPosition(0,i*0.2,-0.2);
			int pyl0y = getYPosition(0,i*0.2,-0.2);
			int pyl1x = getXPosition(1,i*0.2,-0.2);
			int pyl1y = getYPosition(1,i*0.2,-0.2);
			g.drawLine(pyl0x, pyl0y, pyl1x, pyl1y);
		}
	}
	
	/**
	 * Dibuja el eje Z de la representación gráfica
	 */
	private void paintZAxis(Graphics g) {
		Variable zvar = xfplot.getZVariable();
		String zmin = ""+zvar.point(0.0);
		String zmax = ""+zvar.point(1.0);
		int zvarwidth = fm.stringWidth(zvar.toString());
		int zminwidth = fm.stringWidth(zmin);
		int zmaxwidth = fm.stringWidth(zmax);
		int ascent = fm.getAscent();
		
		int zbx = getXPosition(0,0,-0.2);
		int zby = getYPosition(0,0,-0.2);
		int z0x = getXPosition(0,0,0);
		int z0y = getYPosition(0,0,0);
		int z1x = getXPosition(0,0,1);
		int z1y = getYPosition(0,0,1);
		int zhx = getXPosition(0,0,0.5);
		int zhy = getYPosition(0,0,0.5);
		int zzx = getXPosition(1,1,0);
		
		g.drawLine(zbx, zby, z1x, z1y);
		int l = (z0x <= zzx? -3 : 3);
		for(int i=0; i<=5; i++) {
			int zx = getXPosition(0,0,i*0.2);
			int zy = getYPosition(0,0,i*0.2);
			g.drawLine(zx, zy, zx+l, zy);
		}
		
		if(z0x <= zzx) {
			g.drawString(zvar.toString(), zhx-20-zvarwidth, zhy+ascent/2);
			g.drawString(zmin, z0x-15-zminwidth, z0y+ascent/2);
			g.drawString(zmax, z1x-15-zmaxwidth, z1y+ascent/2);
		} else {
			g.drawString(zvar.toString(), zhx+20, zhy+ascent/2);
			g.drawString(zmin, z0x+15, z0y+ascent/2);
			g.drawString(zmax, z1x+15, z1y+ascent/2);
		}
	}
	
	/**
	 * Dibuja la superficie que representa la función
	 */
	private void paintFunction(Graphics g) {
		double[][] function = xfplot.getFunction();
		boolean xincrease, yincrease;
		int p0x = getXPosition(0,0,-0.2);
		int p1x = getXPosition(1,0,-0.2);
		int p2x = getXPosition(1,1,-0.2);
		if(p0x < p1x) yincrease = false; else yincrease = true;
		if(p1x < p2x) xincrease = false; else xincrease = true;
		
		Color gridcolor = xfplot.getGridColor();
		
		int x[] = new int[4];
		int y[] = new int[4];
		Color old = g.getColor();
		for(int j=0; j<function.length-1 ; j++)
			for(int i=0; i<function.length-1; i++) {
				int ii, jj;
				if(xincrease) ii = i; else ii = function.length-i-2;
				if(yincrease) jj = j; else jj = function.length-j-2;
				double y0 = jj*1.0/(function[0].length-1);
				double y1 = (jj+1)*1.0/(function[0].length-1);
				double x0 = ii*1.0/(function.length-1);
				double x1 = (ii+1)*1.0/(function.length-1);
				int f0x = getXPosition(x0,y0,function[ii][jj]);
				int f0y = getYPosition(x0,y0,function[ii][jj]);
				int f1x = getXPosition(x1,y0,function[ii+1][jj]);
				int f1y = getYPosition(x1,y0,function[ii+1][jj]);
				int f2x = getXPosition(x1,y1,function[ii+1][jj+1]);
				int f2y = getYPosition(x1,y1,function[ii+1][jj+1]);
				int f3x = getXPosition(x0,y1,function[ii][jj+1]);
				int f3y = getYPosition(x0,y1,function[ii][jj+1]);
				x[0]=f0x; x[1]=f1x; x[2]=f2x; x[3]=f3x;
				y[0]=f0y; y[1]=f1y; y[2]=f2y; y[3]=f3y;
				double zm = (function[ii][jj]+function[ii+1][jj]+function[ii][jj+1]+function[ii+1][jj+1])/4.0;
				g.setColor(xfplot.getColor(zm));
				g.fillPolygon(x,y,4);
				if(gridcolor != null) {
					g.setColor(gridcolor);
					g.drawPolygon(x,y,4);
				}
			}
		g.setColor(old);
	}
}
