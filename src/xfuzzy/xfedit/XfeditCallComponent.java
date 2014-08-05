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
import java.util.Vector;

/**
 * Componente que dibuja una llamada a un módulo
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditCallComponent {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de la llamada a representar
	 */
	public ModuleCall call;
	
	/**
	 * Nivel de profundidad en el esquema jerárquico
	 */
	public int level;
	
	/**
	 * Altura del componente
	 */
	public int height;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Editor de la estructura jerárquica
	 */
	private XfeditStructure structure;
	
	/**
	 * Componentes que marcan los puntos de las variables de entrada a la llamada
	 */
	private XfeditVariableDot inputdot[];
	
	/**
	 * Componentes que marcan los puntos de las variables de salida de la llamada
	 */
	private XfeditVariableDot outputdot[];
	
	/**
	 * Llamadas a módulos que deben preceder a la llamada a representar
	 */
	private Vector previous;
	
	/**
	 * Marcador para indicar si el componente está seleccionado
	 */
	private boolean selected = false;
	
	/**
	 * Posición en la que está colocado el componente
	 */
	private int x, y;
	
	/**
	 * Anchura del componente
	 */
	private int width;
	
	/**
	 * Características de la fuente de letra
	 */
	private int ascent, descent;
	
	/**
	 * Fuente de letra a utilizar
	 */
	private FontMetrics fm;
	
	/**
	 * Icono a mostrar en el componente
	 */
	private Icon callicon;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditCallComponent(XfeditStructure structure, ModuleCall call) {
		this.fm = structure.getFontMetrics(structure.getFont());
		this.ascent = fm.getAscent();
		this.descent = fm.getDescent();
		if(call instanceof CrispBlockCall) this.callicon = XfeditIcons.crispblock;
		else this.callicon = XfeditIcons.rulebase;

		this.structure = structure;
		this.call = call;
		this.level = 0;
		this.previous = new Vector();

		Variable inputvar[] = call.getInputVariables();
		int inputlength = call.getNumberOfInputs();
		int outputlength = call.getNumberOfOutputs();
		this.inputdot = new XfeditVariableDot[inputlength];
		this.outputdot = new XfeditVariableDot[outputlength];

		for(int i=0; i<inputlength; i++) {
			inputdot[i] = new XfeditVariableDot(structure,this,i,0,true);
			structure.addDot(inputdot[i]);
			XfeditVariableDot ldot = structure.searchOriginDot(inputvar[i]);
			if(ldot != null && ldot.call != null && !inputvar[i].equals("NULL"))
				previous.addElement(ldot.call);
			if(ldot != null && ldot.level > level) level = ldot.level;
		}
		for(int i=0; i<inputlength; i++) inputdot[i].level = level;

		for(int i=0; i<outputlength; i++) {
			outputdot[i] = new XfeditVariableDot(structure,this,i,level+1,false);
			structure.addDot(outputdot[i]);
		}

		int hl = ascent+descent+5;
		int hv = hl+hl*(inputlength>outputlength? inputlength: outputlength);
		int hv2 = callicon.getIconHeight() + 2*hl;
		this.height = (hv>hv2 ? hv : hv2);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Estudia si la posición se encuentra dentro del componente
	 */
	public boolean include(int px, int py) {
		return (px>x && px<x+width && py>y && py<y+height);
	}

	/**
	 * Asigna el estado de selección del componente
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Limpia el vector de llamadas previas
	 */
	public void clearPrevious() {
		this.previous.removeAllElements();
	}

	/**
	 * Recalcula el vector de llamadas previas
	 */
	public void setPrevious() {
		this.previous.removeAllElements();
		for(int i=0; i<inputdot.length; i++) {
			if(inputdot[i].sysvar.equals("NULL")) continue;
			XfeditVariableDot ldot = structure.searchDot(inputdot[i].sysvar);
			if(ldot != null && ldot.call != null) previous.addElement(ldot.call);
		}
	}

	/**
	 * Verifica si un componente corresponde a una llamada previa
	 */
	public boolean isPrevious(XfeditCallComponent cc) {
		for(int i=0; i<previous.size(); i++) {
			XfeditCallComponent pr = (XfeditCallComponent) previous.elementAt(i);
			if(cc == pr || pr.isPrevious(cc)) return true;
		}
		return false;
	}

	/**
	 * Dibuja el componente
	 */
	public void paintCall(Graphics g, int xx, int yy, int w) {
		String label = call.getName();

		int cw = fm.stringWidth(label);
		int ih = height/(inputdot.length+1);
		int oh = height/(outputdot.length+1);
		this.x = xx;
		this.y = yy;
		this.width = w;

		for(int i=0; i<inputdot.length; i++) {
			int ypos = y+ih*(i+1);
			inputdot[i].setPoint(x,ypos);
			inputdot[i].paintDot(g);
		}

		for(int i=0; i<outputdot.length; i++) {
			int ypos = y+oh*(i+1);
			outputdot[i].setPoint(x+w,ypos);
			outputdot[i].paintDot(g);
		}

		int labelh = ascent+descent+10;
		if(selected) g.setColor(Color.red); else g.setColor(Color.blue);
		g.fillRect(x,y,width,labelh);
		g.setColor(XConstants.callbg);
		g.fillRect(x,y+labelh,width,height-labelh);
		g.setColor(Color.black);
		g.drawRect(x,y,width,labelh);
		g.drawRect(x,y+labelh,width,height-labelh);

		int iconw = callicon.getIconWidth();
		int iconh = callicon.getIconHeight();
		int labely = y+5+ascent;
		int icony = y+labelh+(height-labelh-iconh)/2;
		if(icony<labely+5) icony = labely+5;
		g.setColor(Color.white);
		g.drawString(label,x+(w-cw)/2,labely);
		g.setColor(Color.black);
		callicon.paintIcon(structure,g,x+(w-iconw)/2,icony);
	}

	/**
	 * Dibuja los enlaces del componente
	 */
	public void paintLinks(Graphics g) {
		for(int i=0; i<inputdot.length; i++) 
			structure.paintLink(g,structure.searchOriginDot(inputdot[i].sysvar),inputdot[i]);

		for(int i=0; i<outputdot.length; i++) 
			if(outputdot[i].sysvar != null && outputdot[i].sysvar.isOutput())
				structure.paintLink(g,outputdot[i],structure.searchDot(outputdot[i].sysvar));
	} 
}
