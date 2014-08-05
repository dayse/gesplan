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
//REPRESENTACION GRAFICA DE LAS MFS DE UN TIPO DE VARIABLE	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panel de representaci�n gr�fica de las funciones de pertenencia
 * de un tipo de variable ling��stica
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class XfeditTypeGraphPanel extends JPanel {

	/**
	 * C�digo asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603042L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Configuraci�n gr�fica para dibujar los ejes de la representaci�n
	 */
	private Graphics2D gaxis;
	
	/**
	 * Configuraci�n gr�fica para dibujar las funciones de pertenencia 
	 * no seleccionadas
	 */
	private Graphics2D gmf;
	
	/**
	 * Configuraci�n gr�fica para dibujar la funci�n de pertenencia seleccionada
	 */
	private Graphics2D gsel;
	
	/**
	 * Posiciones de los extremos de la gr�fica
	 */
	private int x0, x1, y0, y1;
	
	/**
	 * Funci�n de pertenencia seleccionada
	 */
	private LinguisticLabel mfsel;
	
	/**
	 * Funci�n de pertenencia excluida
	 */
	private LinguisticLabel mfexcl;
	
	/**
	 * Tipo de variable a representar
	 */
	private Type type;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditTypeGraphPanel(Type type, int width) {
		super();
		this.type = type;
		Dimension prefsize = new Dimension(width,getPreferredSize().height);
		setPreferredSize(prefsize);
		setBackground(XConstants.textbackground);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS P�BLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el tipo a representar
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Asigna la funci�n de pertenencia seleccionada
	 */
	public void setSelection(LinguisticLabel mfsel) {
		this.mfsel = mfsel;
	}

	/**
	 * Asigna la funci�n de pertenencia excluida
	 */
	public void setExcluded(LinguisticLabel mfexcl) {
		this.mfexcl = mfexcl;
	}

	/**
	 * Pinta la representaci�n gr�fica
	 */
	public void paint(Graphics g) {
		super.paint(g);
		setGraphics(g);
		paintAxis();
		paintFunctions();
	}

	//----------------------------------------------------------------------------//
	//                             M�TODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula algunas variables internas
	 */
	private void setGraphics(Graphics gc) {
		gaxis = (Graphics2D) gc.create(); gaxis.setColor(Color.black);
		gmf = (Graphics2D) gc.create(); gmf.setColor(Color.blue);
		gsel = (Graphics2D) gc.create(); gsel.setColor(Color.red);
		Dimension size = getSize();
		x0 = size.width/8;
		x1 = size.width*7/8;
		y0 = size.height*7/8;
		y1 = size.height/8;
	}

	/**
	 * Pinta los ejes de la gr�fica
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
	 * Pinta una funci�n de pertenencia de un cierto color
	 */
	private void paintFunction(LinguisticLabel mf, Graphics2D gc) {
		double min = type.getUniverse().min(); 
		double max = type.getUniverse().max(); 
		double step = type.getUniverse().step(); 
		if(mf instanceof pkg.xfl.mfunc.singleton || mf instanceof pkg.xfsg.mfunc.singleton) {
			double value = mf.get()[0];
			int x = (int) ((value - min)*(x1-x0)/(max-min)) + x0;
			if(x>=x0 && x<=x1) gc.drawLine(x,y0,x,y1);
			return;
		}
		double next = min + step;
		int xp = x0;
		int yp = (int) (mf.compute(min)*(y1-y0)) + y0;
		for(int xi=x0+1; xi<=x1; xi++) {
			double x = min + (xi-x0)*(max-min)/(x1-x0);
			while(x >= next) {
				int yi = (int) (mf.compute(next)*(y1-y0)) + y0;
				gc.drawLine(xp,yp,xi,yi);
				xp = xi;
				yp = yi;
				next += step;
			}
			int yi = (int) (mf.compute(x)*(y1-y0)) + y0;
			gc.drawLine(xp,yp,xi,yi);
			xp = xi;
			yp = yi;
		}
	}

	/**
	 * Pinta todas las funciones de pertenencia de un tipo
	 */
	private void paintFunctions() {
		LinguisticLabel[] mf = type.getAllMembershipFunctions();
		for(int i=0; i<mf.length; i++) if(mf[i]!=mfexcl) paintFunction(mf[i],gmf);
		if(mfsel != null && mfsel.test() ) paintFunction(mfsel,gsel);
	}
}

