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



package xfuzzy.xfmt;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panel para representar las variables de entrada de un módulo difuso
 * 
 * @author Francisco JOsé Moreno Velo
 *
 */
public class XfmtInputPanel extends JPanel {

	//----------------------------------------------------------------------------//
	//                            CONSTANTES PRIVADAS                             //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603046L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Configuración gráfica para representar los ejes
	 */
	private Graphics2D gaxis;
	
	/**
	 * Configuración gráfica para representar las funciones de pertenencia
	 */
	private Graphics2D gmf;

	/**
	 * Configuración gráfica para representar la función de pertenencia seleccionada
	 */
	private Graphics2D gsel;

	/**
	 * Área donde situar la representación
	 */
	private int x0, x1, y0, y1;
	
	/**
	 * Mínimo del universo de discurso de la partición difusa
	 */
	private double min;
	
	/**
	 * Máximo del universo de discurso de la partición difusa
	 */
	private double max;	
	
	/**
	 * Partición difusa a representar
	 */
	private Type type;
	
	/**
	 * Función de pertenencia seleccionada
	 */
	private MemFunc value;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfmtInputPanel(int width,int height,Variable input) {
		super();
		Dimension prefsize = new Dimension(width,height);
		setPreferredSize(prefsize);
		setBackground(XConstants.textbackground);
		setBorder(BorderFactory.createLoweredBevelBorder());
		this.min = input.getType().getUniverse().min();
		this.max = input.getType().getUniverse().max();
		this.type = input.getType();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Asigna la función a representar
	 */
	public void setFunction(MemFunc value) {
		this.value = value;
	}
	
	/**
	 * Dibuja el panel
	 */
	public void paint(Graphics g) {
		super.paint(g);
		setGraphics(g);
		paintAxis();
		LinguisticLabel pmf[] = type.getAllMembershipFunctions();
		for(int i=0; i<pmf.length; i++) paintFunction(pmf[i],gmf);
		paintFunction(value,gsel);
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Calcula algunas constantes para la representación
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
	 * Dibuja una función de pertenencia
	 */
	private void paintFunction(MemFunc mf,Graphics2D gc) {
		if(mf == null) return;
		if(mf instanceof pkg.xfl.mfunc.singleton) {
			double val = ((ParamMemFunc) mf).get()[0];
			int pos = x0 + (int) ((val-min)*(x1-x0)/(max-min));
			gc.drawLine(pos, y0, pos, y1);
			return;
		}
		if((mf instanceof AggregateMemFunc) &&
				((AggregateMemFunc) mf).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) mf).getDiscreteValues();
			for(int i=0; i<val.length; i++){
				int xpos = x0 + (int) ((val[i][0]-min)*(x1-x0)/(max-min));
				int ypos = y0 + (int) (val[i][1]*(y1-y0));
				gc.drawLine(xpos, y0, xpos, ypos);
			}
			return;
		}
		double yval = mf.compute(min);
		int ypos = y0 + (int) (yval*(y1-y0));
		for(int i=1; i<=(x1-x0); i++) {
			double nval = mf.compute( min + (max-min)*i/(x1-x0) );
			int npos = y0 + (int) (nval*(y1-y0));
			gc.drawLine(x0+i-1, ypos, x0+i, npos);
			ypos = npos;
		}
	}
	
	/**
	 * Pinta los ejes de la representación
	 */
	private void paintAxis() {
		gaxis.drawLine(x0-1, y0+1, x0-1, y1);
		gaxis.drawLine(x0-1, y0+1, x1+1, y0+1);
		gaxis.drawLine(x1+1, y0+1, x1+1, y1);
	}
}

