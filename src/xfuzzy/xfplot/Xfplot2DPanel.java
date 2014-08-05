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

import javax.swing.*;
import java.awt.*;

/**
 * Clase que desarrolla el panel para la representación gráfica en 2
 * dimensiones
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Xfplot2DPanel extends JPanel {
	
	//----------------------------------------------------------------------------//
	//                            CONSTANTES PRIVADAS                             //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603053L;
	
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
	 * Referencia al apágina principal de la aplicación
	 */
	private Xfplot xfplot;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Xfplot2DPanel(Xfplot xfplot) {
		super();
		this.xfplot = xfplot;
		setSize(new Dimension(WIDTH,HEIGHT));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.white);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Pinta la representación gráfica. 
	 * Este método sobreescribe el método paint() de JPanel(). 
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintAxis(g);
		paintVariables(g);
		paintFunction(g);
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Dibuja los ejes de la representación gráfica
	 */
	private void paintAxis(Graphics g) {
		int width = getSize().width;
		int height = getSize().height;
		int xmin = width/8;
		int xmax = width*7/8;
		int ymin = height*7/8;
		int ymax = height/8;
		g.drawLine(xmin, ymin, xmin, ymax);
		g.drawLine(xmin, ymin, xmax, ymin);
		for(int i=0; i<6; i++) {
			int y = ymin + (ymax - ymin)*i/5;
			g.drawLine(xmin-3, y, xmin, y);
		}
		for(int i=0; i<6; i++) {
			int x = xmin + (xmax - xmin)*i/5;
			g.drawLine(x, ymin+3, x, ymin);
		}
	}
	
	/**
	 * Dibuja los nombres de las variables a representar
	 */
	private void paintVariables(Graphics g) {
		int width = getSize().width;
		int height = getSize().height;
		int xx = width * 9 / 10;
		int xy = height * 9 / 10;
		int yx = width / 10;
		int yy = height / 10;
		g.drawString(xfplot.getXVariable().toString(), xx, xy);
		g.drawString(xfplot.getZVariable().toString(), yx, yy);
	}
	
	/**
	 * Dibuja la función en tramos lineales
	 */
	private void paintFunction(Graphics g) {
		double function[] = xfplot.get2DFunction();
		if(function == null) return;
		int x0, y0, x1, y1;
		
		int width = getSize().width;
		int height = getSize().height;
		
		int xrange = width * 3 / 4;
		int yrange = height * 3 / 4;
		int xmin = width/8;
		int ymin = height*7/8;
		Color old = g.getColor();
		g.setColor(Color.red);
		
		x0 = xmin;
		y0 = ymin - (int) Math.round(function[0]*yrange);
		for(int i=1; i<function.length; i++) {
			x1 = xmin + i*xrange/(function.length-1);
			y1 = ymin - (int) Math.round(function[i]*yrange);
			g.drawLine(x0,y0,x1,y1);
			x0 = x1;
			y0 = y1;
		}
		g.setColor(old);
	}
}
