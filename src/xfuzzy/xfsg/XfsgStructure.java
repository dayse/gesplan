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

package xfuzzy.xfsg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import xfuzzy.lang.ModuleCall;
import xfuzzy.lang.SystemModule;
import xfuzzy.lang.Variable;
import xfuzzy.util.XConstants;

/**
 * Clase que se utiliza para representar gráficamente el sistema difuso definido
 * en el lenguaje XFL3. Para realizar esta clase se han utilizado gran parte de
 * las funciones del módulo Xfedit.
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgStructure extends JTextArea {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603039L;

	// ----------------------------------------------------------------------------//
	// MIEMBROS PRIVADOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Descripción de la estructura a mostrar
	 */
	private SystemModule system;

	/**
	 * Componentes de llamadas a módulos que forman parte de la estructura
	 */
	private XfsgCallComponent call[];

	/**
	 * Puntos (cuadraditos) que representan variables de entrada del sistema
	 */
	private XfsgVariableDot inputdot[];

	/**
	 * Puntos (cuadraditos) que representan variables de salida del sistema
	 */
	private XfsgVariableDot outputdot[];

	/**
	 * Puntos (cuadraditos) que representan variables de internas del sistema
	 */
	private XfsgVariableDot dot[];

	/**
	 * Punto seleccionado
	 */
	private XfsgVariableDot selecteddot;

	/**
	 * Fuente de letra para etiquetar las variables
	 */
	private FontMetrics fm;

	/**
	 * Niveles de encadenamiento de módulos en la estructura
	 */
	private int levels;

	/**
	 * Anchura de las diferentes zonas a representar: etiquetas de las variables
	 * de entrada, de salida, internas y anchura de los componentes de llamada
	 */
	private int inputwidth, outputwidth, callwidth, varwidth;

	/**
	 * Altura total de la representación
	 */
	private int totalheight;

	/**
	 * Contador de canales para las líneas de conexión
	 */
	public int counter;

	// ----------------------------------------------------------------------------//
	// MÉTODOS PÚBLICOS //
	// ----------------------------------------------------------------------------//

	/**
	 * Constructor para la aplicación Xfedit
	 */
	public XfsgStructure(SystemModule system) {
		super();
		this.system = system;
		build();
	}

	/**
	 * Calcula los componentes para representar la estructura
	 */
	public void setValues() {
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();
		ModuleCall rcall[] = system.getModuleCalls();

		this.fm = getFontMetrics(getFont());
		this.counter = 0;

		this.dot = new XfsgVariableDot[0];
		this.inputdot = new XfsgVariableDot[input.length];
		this.outputdot = new XfsgVariableDot[output.length];
		this.call = new XfsgCallComponent[rcall.length];
		for (int i = 0; i < input.length; i++) {
			inputdot[i] = new XfsgVariableDot(this, input[i], 0, true);
			addDot(inputdot[i]);
		}
		for (int i = 0; i < output.length; i++) {
			outputdot[i] = new XfsgVariableDot(this, output[i], 0, false);
			addDot(outputdot[i]);
		}
		for (int i = 0; i < rcall.length; i++) {
			call[i] = new XfsgCallComponent(this, rcall[i]);
		}

		this.inputwidth = 0;
		this.outputwidth = 0;
		this.callwidth = 80;
		this.levels = 0;
		for (int i = 0; i < input.length; i++)
			inputwidth = Math.max(inputwidth, fm.stringWidth(input[i]
					.toString()));
		for (int i = 0; i < output.length; i++)
			outputwidth = Math.max(outputwidth, fm.stringWidth(output[i]
					.toString()));
		for (int i = 0; i < rcall.length; i++) {
			int labelwidth = fm.stringWidth(rcall[i].getName()) + 10;
			callwidth = Math.max(callwidth, labelwidth);
		}
		for (int i = 0; i < call.length; i++)
			if (call[i].level > levels)
				levels = call[i].level;
		this.inputwidth += 20;
		this.outputwidth += 20;
		this.levels++;
		for (int i = 0; i < outputdot.length; i++)
			outputdot[i].level = levels;
		this.varwidth = 30 + 3 * counter;
		Dimension size = getSize();
		int width = (size.width > 350 ? size.width : 350);
		int vwidth = (width - inputwidth - outputwidth - levels * callwidth)
				/ (levels + 1);
		if (varwidth < vwidth)
			varwidth = vwidth;
		int twidth = inputwidth + levels * callwidth + (levels + 1) * varwidth
				+ outputwidth;
		if (twidth < width)
			twidth = width;

		int theight = 70 + 3 * counter;
		for (int lv = 0; lv < levels; lv++) {
			int lvheight = 70 + 3 * counter;
			for (int i = 0; i < call.length; i++)
				if (call[i].level == lv)
					lvheight += call[i].height + 20;
			if (lvheight > theight)
				theight = lvheight;
		}
		setPreferredSize(new Dimension(twidth, theight));
	}

	/**
	 * Añadir un punto (variable) a la gráfica
	 */
	public void addDot(XfsgVariableDot ndot) {
		XfsgVariableDot adot[] = new XfsgVariableDot[this.dot.length + 1];
		System.arraycopy(this.dot, 0, adot, 0, this.dot.length);
		adot[this.dot.length] = ndot;
		this.dot = adot;
	}

	/**
	 * Busca el punto correspondiente a una variable
	 */
	public XfsgVariableDot searchOriginDot(Variable var) {
		if (var == null)
			return null;
		if (var.isInner() || var.isOutput())
			for (int i = 0; i < dot.length; i++) {
				if (dot[i].sysvar == var && dot[i].isInternal()
						&& dot[i].isOutput())
					return dot[i];
			}
		else
			for (int i = 0; i < dot.length; i++) {
				if (dot[i].sysvar == var && dot[i].isGlobal())
					return dot[i];
			}
		return null;
	}

	/**
	 * Genera el contenido del panel
	 */
	private void build() {
		setBackground(XConstants.textbackground);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setFont(XConstants.textfont);
		setEditable(false);
		setValues();
	}

	/**
	 * Sobreescribe el método que pinta el panel
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintBase(g);
	}

	/**
	 * Pinta la estructura
	 */
	private void paintBase(Graphics g) {
		Dimension size = getSize();
		this.totalheight = size.height;
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();

		int ih = (size.height - 35 - 3 * counter) / (input.length + 1);
		int oh = (size.height - 35 - 3 * counter) / (output.length + 1);
		int sw = size.width - inputwidth - outputwidth;

		g.setColor(XConstants.systembg);
		g.fillRoundRect(inputwidth, 10, sw, size.height - 20, 15, 15);
		g.setColor(Color.black);
		g.drawRoundRect(inputwidth, 10, sw, size.height - 20, 15, 15);

		for (int i = 0; i < inputdot.length; i++) {
			inputdot[i].setPoint(inputwidth, 10 + ih + ih * i);
			inputdot[i].paintDot(g);
		}

		for (int i = 0; i < outputdot.length; i++) {
			outputdot[i].setPoint(size.width - outputwidth, 10 + oh + oh * i);
			outputdot[i].paintDot(g);
		}

		varwidth = (size.width - inputwidth - outputwidth - levels * callwidth)
				/ (levels + 1);
		for (int lv = 0; lv < levels; lv++) {
			int lvcounter = 0, lvheight = 0;
			for (int i = 0; i < call.length; i++)
				if (call[i].level == lv) {
					lvcounter++;
					lvheight += call[i].height;
				}
			int ch = (size.height - 35 - lvheight - 3 * counter)
					/ (lvcounter + 1);
			int cpos = inputwidth + varwidth + lv * (varwidth + callwidth);
			int ypos = 10 + ch;
			for (int i = 0; i < call.length; i++)
				if (call[i].level == lv) {
					call[i].paintCall(g, cpos, ypos, callwidth);
					ypos += ch + call[i].height;
				}
		}
		for (int i = 0; i < call.length; i++)
			call[i].paintLinks(g);

	}

	/**
	 * Pinta un enlace
	 */
	public void paintLink(Graphics g, XfsgVariableDot orig, XfsgVariableDot dest) {
		if (orig == null || dest == null)
			return;
		if (orig.isNull())
			return;
		if (dest.isNull())
			return;

		String aux1 = "null", aux2 = "null";

		Color channelcolor = Color.black;
		switch (orig.channel % 4) {
		case 0:
			channelcolor = Color.black;
			break;
		case 1:
			channelcolor = Color.red;
			break;
		case 2:
			channelcolor = Color.blue;
			break;
		case 3:
			channelcolor = Color.green;
			break;
		}
		g.setColor(channelcolor);
		// System.out.println("origen: "+orig.sysvar.getName());
		// System.out.println("destino: "+dest.sysvar.getName());
		Collection<int[]> c = new LinkedList<int[]>();
		int[] puntos;
		if (orig.level == dest.level) {
			int channel = orig.x + varwidth / 2 + (orig.channel - counter / 2)
					* 3;
			g.drawLine(orig.x, orig.y, channel, orig.y);
			puntos = new int[4];
			puntos[0] = orig.x;
			puntos[1] = orig.y;
			puntos[2] = channel;
			puntos[3] = orig.y;
			c.add(puntos);
			g.drawLine(channel, orig.y, channel, dest.y);
			puntos = new int[4];
			puntos[0] = channel;
			puntos[1] = orig.y;
			puntos[2] = channel;
			puntos[3] = dest.y;
			c.add(puntos);
			g.drawLine(channel, dest.y, dest.x, dest.y);
			puntos = new int[4];
			puntos[0] = channel;
			puntos[1] = dest.y;
			puntos[2] = dest.x;
			puntos[3] = dest.y;
			c.add(puntos);
			if (orig.call != null)
				aux1 = orig.call.call.getName();
			if (dest.call != null)
				aux2 = dest.call.call.getName();
			// if(orig.call!=null && dest.call!=null)
			// System.out.println(orig.sysvar.getName()+orig.call.call.getName()+dest.call.call.getName());
			new XfsgArchitecturesSimulink(orig.sysvar.getName() + aux1 + aux2,
					c);
			// System.out.println(orig.sysvar.getName()+aux1+aux2);
			if (selecteddot != dest && selecteddot != orig)
				return;
			g.drawLine(orig.x, orig.y - 1, channel + 1, orig.y - 1);
			g.drawLine(channel + 1, orig.y - 1, channel + 1, dest.y - 1);
			g.drawLine(channel + 1, dest.y - 1, dest.x, dest.y - 1);
		} else {
			int channel1 = orig.x + varwidth / 2 + (orig.channel - counter / 2)
					* 3;
			int channel2 = totalheight - 25 - 3 * orig.channel;
			int channel3 = dest.x - varwidth / 2 + (orig.channel - counter / 2)
					* 3;
			g.drawLine(orig.x, orig.y, channel1, orig.y);
			puntos = new int[4];
			puntos[0] = orig.x;
			puntos[1] = orig.y;
			puntos[2] = channel1;
			puntos[3] = orig.y;
			c.add(puntos);
			g.drawLine(channel1, orig.y, channel1, channel2);
			puntos = new int[4];
			puntos[0] = channel1;
			puntos[1] = orig.y;
			puntos[2] = channel1;
			puntos[3] = channel2;
			c.add(puntos);
			g.drawLine(channel1, channel2, channel3, channel2);
			puntos = new int[4];
			puntos[0] = channel1;
			puntos[1] = channel2;
			puntos[2] = channel3;
			puntos[3] = channel2;
			c.add(puntos);
			g.drawLine(channel3, channel2, channel3, dest.y);
			puntos = new int[4];
			puntos[0] = channel3;
			puntos[1] = channel2;
			puntos[2] = channel3;
			puntos[3] = dest.y;
			c.add(puntos);
			g.drawLine(channel3, dest.y, dest.x, dest.y);
			puntos = new int[4];
			puntos[0] = channel3;
			puntos[1] = dest.y;
			puntos[2] = dest.x;
			puntos[3] = dest.y;
			c.add(puntos);
			// if(orig.call!=null && dest.call!=null)
			// System.out.println(orig.sysvar.getName()+orig.call.call.getName()+dest.call.call.getName());
			if (orig.call != null)
				aux1 = orig.call.call.getName();
			if (dest.call != null)
				aux2 = dest.call.call.getName();
			new XfsgArchitecturesSimulink(orig.sysvar.getName() + aux1 + aux2,
					c);
			// System.out.println(orig.sysvar.getName()+aux1+aux2);
			if (selecteddot != dest && selecteddot != orig)
				return;
			channel1++;
			channel2--;
			channel3++;
			g.drawLine(orig.x, orig.y - 1, channel1, orig.y - 1);
			g.drawLine(channel1, orig.y - 1, channel1, channel2);
			g.drawLine(channel1, channel2, channel3, channel2);
			g.drawLine(channel3, channel2, channel3, dest.y);
			g.drawLine(channel3, dest.y - 1, dest.x, dest.y - 1);
		}

		g.setColor(Color.black);
	}

	/**
	 * Busca el punto correspondiente a una variable
	 */
	public XfsgVariableDot searchDot(Variable var) {
		if (var == null)
			return null;
		if (var.isInner())
			for (int i = 0; i < dot.length; i++) {
				if (dot[i].sysvar == var && dot[i].isOrigin())
					return dot[i];
			}
		else
			for (int i = 0; i < dot.length; i++) {
				if (dot[i].sysvar == var && dot[i].isGlobal())
					return dot[i];
			}
		return null;
	}

}
