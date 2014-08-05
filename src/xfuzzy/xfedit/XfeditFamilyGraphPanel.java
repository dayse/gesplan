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


package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que muestra la representación gráfica de una familia de funciones
 * de pertenencia
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditFamilyGraphPanel extends JPanel {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603023L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Configuración gráfica para representar los ejes 
	 */
	private Graphics2D gaxis;
	
	/**
	 * Configuración gráfica para representar las funciones de la familia
	 */
	private Graphics2D gmf;
	
	/**
	 * Posición de los ejes de la representación
	 */
	private int x0, x1, y0, y1;
	
	/**
	 * Familia a representar
	 */
	private Family fam;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditFamilyGraphPanel(Family fam, int width) {
		super();
		this.fam = fam;
		Dimension prefsize = new Dimension(width,getPreferredSize().height);
		setPreferredSize(prefsize);
		setBackground(XConstants.textbackground);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna la familia a representar
	 */
	public void setFamily(Family fam) {
		this.fam = fam;
	}

	/**
	 * Pinta la representación gráfica
	 */
	public void paint(Graphics g) {
		super.paint(g);
		setGraphics(g);
		paintAxis();
		paintFunctions();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula algunas variables internas
	 */
	private void setGraphics(Graphics gc) {
		gaxis = (Graphics2D) gc.create(); gaxis.setColor(Color.black);
		gmf = (Graphics2D) gc.create(); gmf.setColor(Color.blue);
		Dimension size = getSize();
		x0 = size.width/8;
		x1 = size.width*7/8;
		y0 = size.height*7/8;
		y1 = size.height/8;
	}

	/**
	 * Pinta los ejes de la gráfica
	 */
	private void paintAxis() {
		int xm = (x0+x1)/2;
		int ym = (y0+y1+1)/2;
		gaxis.drawLine(x0-1, y0+1, x0-1, y1);
		gaxis.drawLine(x0-1, y0+1, x1+1, y0+1);
		gaxis.drawLine(x1+1, y0+1, x1+1, y1);
		gaxis.drawLine(x0-6, y1, x0-1, y1);
		gaxis.drawLine(x0-6, ym, x0-1, ym);
		gaxis.drawLine(x1+1, y1, x1+6, y1);
		gaxis.drawLine(x1+1, ym, x1+6, ym);
		gaxis.drawLine(x0-1, y0+6, x0-1, y0+1);
		gaxis.drawLine(xm, y0+6, xm, y0+1);
		gaxis.drawLine(x1+1, y0+6, x1+1, y0+1);
		gaxis.drawString("1.0", x0-30, y1+5);
		gaxis.drawString("1.0", x1+7, y1+5);
		gaxis.drawString("0.5", x0-30, ym+5);
		gaxis.drawString("0.5", x1+7, ym+5);
		gaxis.drawString("Min.", x0-10, y0+20);
		gaxis.drawString("Max.", x1-10, y0+20);
	}

	/**
	 * Pinta una función de pertenencia de un cierto color
	 */
	private void paintFunction(int index, Graphics2D gc) {
		double min = this.fam.min(); 
		double max = this.fam.max(); 
		double step = this.fam.step(); 
		double next = min + step;
		int xp = x0;
		int yp = (int) (fam.compute(index,min)*(y1-y0)) + y0;
		for(int xi=x0+1; xi<=x1; xi++) {
			double x = min + (xi-x0)*(max-min)/(x1-x0);
			while(x >= next) {
				int yi = (int) (fam.compute(index,next)*(y1-y0)) + y0;
				gc.drawLine(xp,yp,xi,yi);
				xp = xi;
				yp = yi;
				next += step;
			}
			int yi = (int) (fam.compute(index,x)*(y1-y0)) + y0;
			gc.drawLine(xp,yp,xi,yi);
			xp = xi;
			yp = yi;
		}
	}

	/**
	 * Pinta todas las funciones de pertenencia de la famila
	 */
	private void paintFunctions() {
		if(fam==null) return;
		int members = fam.members();
		for(int i=0; i<members; i++) paintFunction(i,gmf);
	}
}

