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
//EDITOR DE LA ESTRUCTURA DE UN SISTEMA DIFUSO		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.xfmt.Xfmt;
import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panel de edición de la estructura de un sistema difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditStructure extends JTextArea
implements MouseListener, MouseMotionListener, KeyListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603039L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Descripción de la estructura a mostrar
	 */
	private SystemModule system;
	
	/**
	 * Menú desplegable asociado a los módulos de la estructura
	 */
	private JPopupMenu popup;
	
	/**
	 * Referencia a la aplicación Xfmt a la que pertenece el objeto
	 * (null si pertenece a una aplicación Xfedit)
	 */
	private Xfmt xfmt;
	
	/**
	 * Referencia a la aplicación Xfedit a la que pertenece el objeto
	 * (null si pertenece a una aplicación Xfmt)
	 */
	private Xfedit xfedit;
	
	/**
	 * Componentes de llamadas a módulos que forman parte de la estructura
	 */
	private XfeditCallComponent call[];

	/**
	 * Componente de llamada a módulo seleccionado
	 */
	private XfeditCallComponent selectedcall;
	
	/**
	 * Puntos (cuadraditos) que representan variables de entrada del sistema
	 */
	private XfeditVariableDot inputdot[];
	
	/**
	 * Puntos (cuadraditos) que representan variables de salida del sistema
	 */
	private XfeditVariableDot outputdot[];
	
	/**
	 * Puntos (cuadraditos) que representan variables de internas del sistema
	 */
	private XfeditVariableDot dot[];
	
	/**
	 * Punto seleccionado
	 */
	private XfeditVariableDot selecteddot;
	
	/**
	 * Fuente de letra para etiquetar las variables
	 */
	private FontMetrics fm;
	
	/**
	 * Niveles de encadenamiento de módulos en la estructura
	 */
	private int levels;
	
	/**
	 * Anchura de las diferentes zonas a representar: etiquetas
	 * de las variables de entrada, de salida, internas y 
	 * anchura de los componentes de llamada 
	 */
	private int inputwidth, outputwidth, callwidth, varwidth;
	
	/**
	 * Altura total de la representación
	 */
	private int totalheight;
	
	/**
	 * Posición en la que se pulsa el botón del ratón (para dibujar
	 * el arrastre)
	 */
	private int mousex, mousey;

	/**
	 * Contador de canales para las líneas de conexión
	 */
	public int counter;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor para la aplicación Xfedit
	 */
	public XfeditStructure(Xfedit xfedit, SystemModule system) {
		super();
		this.xfedit = xfedit;
		this.system = system;
		build();
	}

	/**
	 * Constructor para la aplicación Xfmt
	 * @param xfmt
	 * @param system
	 */
	public XfeditStructure(Xfmt xfmt, SystemModule system) {
		super();
		this.xfmt = xfmt;
		this.system = system;
		build();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula los componentes para representar la estructura
	 */
	public void setValues() {
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();
		ModuleCall rcall[] = system.getModuleCalls();

		this.fm = getFontMetrics(getFont());
		this.counter = 0;

		this.dot = new XfeditVariableDot[0];
		this.inputdot = new XfeditVariableDot[input.length];
		this.outputdot = new XfeditVariableDot[output.length];
		this.call = new XfeditCallComponent[rcall.length];
		for(int i=0; i<input.length; i++) {
			inputdot[i] = new XfeditVariableDot(this,input[i], 0, true);
			addDot(inputdot[i]);
		}
		for(int i=0; i<output.length; i++) {
			outputdot[i] = new XfeditVariableDot(this,output[i], 0, false);
			addDot(outputdot[i]);
		}
		for(int i=0; i<rcall.length; i++) {
			call[i] = new XfeditCallComponent(this,rcall[i]);
		}

		this.inputwidth = 0;
		this.outputwidth = 0;
		this.callwidth = 80;
		this.levels = 0;
		for(int i=0; i<input.length; i++)
			inputwidth = Math.max(inputwidth,fm.stringWidth(input[i].toString()));
		for(int i=0; i<output.length; i++)
			outputwidth = Math.max(outputwidth,fm.stringWidth(output[i].toString()));
		for(int i=0; i<rcall.length; i++) {
			int labelwidth = fm.stringWidth(rcall[i].getName())+10;
			callwidth = Math.max(callwidth,labelwidth);
		}
		for(int i=0; i<call.length; i++)
			if(call[i].level > levels) levels = call[i].level;
		this.inputwidth += 20;
		this.outputwidth += 20;
		this.levels ++;
		for(int i=0; i<outputdot.length; i++) outputdot[i].level = levels;
		this.varwidth = 30 + 3*counter;
		Dimension size = getSize();
		int width = (size.width>350 ? size.width : 350);
		int vwidth = (width-inputwidth-outputwidth-levels*callwidth)/(levels+1);
		if(varwidth < vwidth) varwidth = vwidth;
		int twidth = inputwidth+levels*callwidth+(levels+1)*varwidth+outputwidth;
		if(twidth < width) twidth = width;

		int theight = 70 + 3*counter;
		for(int lv=0; lv<levels; lv++) {
			int lvheight = 70 + 3*counter;
			for(int i=0; i<call.length; i++)
				if(call[i].level == lv) lvheight += call[i].height+20;
			if(lvheight > theight) theight = lvheight;
		}
		setPreferredSize(new Dimension(twidth,theight));
	}

	/**
	 * Sobreescribe el método que pinta el panel
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintBase(g);
	}

	/**
	 * Regenera el contenido del panel y vuelve a dibujarlo
	 */
	public void refresh() {
		setValues();
		revalidate();
		repaint();
	}

	/**
	 * Selecciona una llamada a un módulo
	 */
	public void setSelection(XfeditCallComponent scall) {
		if(selectedcall != null) selectedcall.setSelected(false);
		selectedcall = scall;
		if(scall != null) scall.setSelected(true);
		if(xfedit != null) xfedit.listSelection(Xfedit.STRUCTURE);
	}

	/**
	 * Deselecciona cualquier llamada a un módulo
	 */
	public void clearSelection() {
		if(selectedcall != null) selectedcall.setSelected(false);
		selectedcall = null;
		repaint();
	}

	/**
	 *  Inserta una nueva llamada a una base de reglas
	 */
	public void insertCall() {
		XfeditCallEditor dialog = new XfeditCallEditor(xfedit);
		dialog.setVisible(true);
		Object selection = dialog.getSelection();
		if(selection == null) return;
		if(selection instanceof Rulebase) {
			Rulebase rb = (Rulebase) selection;
			Variable nullvar = system.searchVariable("NULL");
			Variable sysinputvar[] = new Variable[rb.getInputs().length];
			Variable sysoutputvar[] = new Variable[rb.getOutputs().length];
			for(int i=0; i<sysinputvar.length; i++) sysinputvar[i] = nullvar;
			for(int i=0; i<sysoutputvar.length; i++) sysoutputvar[i] = nullvar;
			system.addCall(rb,sysinputvar,sysoutputvar);
			refresh();
		}
		else if(selection instanceof CrispBlock) { 
			CrispBlock cb = (CrispBlock) selection;  
			Variable nullvar = system.searchVariable("NULL");
			Variable sysinputvar[] = new Variable[cb.inputs()];
			for(int i=0; i<sysinputvar.length; i++) sysinputvar[i] = nullvar;
			Variable sysoutputvar = nullvar;
			system.addCall(cb,sysinputvar,sysoutputvar);
			refresh();
		}
	}

	/**
	 * Elimina la llamada seleccionada
	 */
	public void removeCall() {
		if(selectedcall == null) return;
		system.removeCall(selectedcall.call);
		selectedcall = null;
		refresh();
	}

	/**
	 * Busca el componente que representa a una llamada
	 */
	public XfeditCallComponent searchCallComponent(RulebaseCall rbc) {
		for(int i=0; i<call.length; i++) if(call[i].call == rbc) return call[i];
		return null;
	}

	/**
	 * Busca el punto correspondiente a una variable
	 */
	public XfeditVariableDot searchDot(Variable var) {
		if(var == null) return null;
		if(var.isInner()) for(int i=0; i<dot.length; i++)
		{ if(dot[i].sysvar == var && dot[i].isOrigin()) return dot[i]; }
		else for(int i=0; i<dot.length; i++)
		{ if(dot[i].sysvar == var && dot[i].isGlobal()) return dot[i]; }
		return null;
	}

	/**
	 * Busca el punto correspondiente a una variable
	 */
	public XfeditVariableDot searchOriginDot(Variable var) {
		if(var == null) return null;
		if(var.isInner() || var.isOutput()) for(int i=0; i<dot.length; i++) {
			if(dot[i].sysvar == var &&
					dot[i].isInternal() &&
					dot[i].isOutput()) return dot[i];
		}
		else for(int i=0; i<dot.length; i++)
		{ if(dot[i].sysvar == var && dot[i].isGlobal()) return dot[i]; }
		return null;
	}

	/**
	 * Añadir un punto (variable) a la gráfica
	 */
	public void addDot(XfeditVariableDot ndot) {
		XfeditVariableDot adot[] = new XfeditVariableDot[this.dot.length+1];
		System.arraycopy(this.dot,0,adot,0,this.dot.length);
		adot[this.dot.length] = ndot;
		this.dot = adot;
	}

	/**
	 * Interfaz KeyListener - Acción de soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		if(xfedit == null) return;
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_BACK_SPACE) { removeCall(); removeLink(); }
		if(code == KeyEvent.VK_DELETE) { removeCall(); removeLink(); }
		if(code == KeyEvent.VK_CUT) { removeCall(); removeLink(); }
		if(code == KeyEvent.VK_INSERT) insertCall();
		e.consume();
	}

	/**
	 * Interfaz KeyListener - Acción de presionar una tecla
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Interfaz KeyListener - Acción de pulsar una tecla
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Interfaz MouseListener - Acción de presionar el botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger()) {
			if(xfedit != null) popup.show((Component) e.getSource(), e.getX(), e.getY());
			return;
		}
		mousex = -1; mousey = -1;
		if(xfedit == null) return;
		setSelection(searchCall(e.getX(),e.getY()));
		selecteddot = searchDot(e.getX(),e.getY());
		repaint();
	}

	/**
	 * Interfaz MouseListener - Acción de sltar el botón del ratón
	 */
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger()) {
			if(xfedit != null) popup.show((Component) e.getSource(), e.getX(), e.getY());
			return;
		}
		mousex = -1; mousey = -1;
		if(xfedit != null) insertLink(selecteddot,searchDot(e.getX(),e.getY()));
		repaint();
	}

	/**
	 * Interfaz MouseListener - Acción de pulsar el botón del ratón
	 */
	public void mouseClicked(MouseEvent e) {
		if(xfmt == null) return;
		XfeditCallComponent call = searchCall(e.getX(),e.getY());
		if(call == null) return;
		if(call.call instanceof RulebaseCall) {
			xfmt.monitorize((RulebaseCall) call.call);
		}
	}

	/**
	 * Interfaz MouseListener - Acción de entrar el ratón en el componente
	 */
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener - Acción de salir el ratón del componente
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Interfaz MouseMotionListener - Acción de mover el ratón
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * Interfaz MouseMotionListener - Acción de arrastrar el ratón
	 */
	public void mouseDragged(MouseEvent e) {
		if(selecteddot == null) return;
		mousex = e.getX();
		mousey = e.getY();
		repaint();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el contenido del panel
	 */
	private void build() {
		setBackground(XConstants.textbackground);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setFont(XConstants.textfont);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setEditable(false);
		setValues();
		if(xfedit != null) createPopupMenu();
	}

	/**
	 * Genera el menú desplegable
	 */
	private void createPopupMenu() {
		String label[] = { "New module call","Remove module call" };
		String command[] = { "NewCall", "DelCall" };
		popup = new JPopupMenu();
		JMenuItem item[] = new JMenuItem[command.length];
		for(int i=0; i<command.length; i++) {
			item[i] = new JMenuItem(label[i]);
			item[i].setActionCommand(command[i]);
			item[i].addActionListener(xfedit);
			item[i].setFont(XConstants.font);
			popup.add(item[i]);
		}
	}

	/**
	 * Devuelve la llamada a módulo correspondiente a una determinada posición
	 */
	private XfeditCallComponent searchCall(int x, int y) {
		for(int i=0; i<call.length; i++) if(call[i].include(x,y)) return call[i];
		return null;
	}

	/**
	 * Vuelve a colocar los componentes de llamada
	 */
	private void reallocate() {
		XfeditCallComponent aux;
		for(int i=0; i<call.length; i++) call[i].clearPrevious();
		for(int i=0; i<call.length; i++) call[i].setPrevious();
		for(int i=0; i<call.length; i++)
			for(int j=i+1; j<call.length; j++) 
				if(call[i].isPrevious(call[j])) {
					aux=call[i]; call[i]=call[j]; call[j]=aux;
					system.exchangeCall(i,j);
					i--;
					break;
				}
	}

	/**
	 * Busca el punto (variable) correspondiente a una posición
	 */
	private XfeditVariableDot searchDot(int x, int y) {
		for(int i=0; i<dot.length; i++) if(dot[i].include(x,y)) return dot[i];
		return null;
	}

	/**
	 * Crea un enlace entre dos puntos
	 */
	private void insertLink(XfeditVariableDot a, XfeditVariableDot b) {
		if(a == null || b == null || a == b) return;

		XfeditVariableDot origdot = null;
		XfeditVariableDot destdot = null;
		if( a.isDestination() ) destdot = a;
		if( b.isDestination() ) destdot = b;
		if( a.isOrigin() ) origdot = a;
		if( b.isOrigin() ) origdot = b;
		if( origdot == null || destdot == null) return;
		if( origdot.isGlobal() && destdot.isGlobal() ) return;

		if(origdot.call != null && origdot.call == destdot.call) {
			XDialog.showMessage(this,"Cannot make loops");
			return;
		}

		if(origdot.call != null && origdot.call.isPrevious(destdot.call) ) {
			XDialog.showMessage(this,"Cannot make loops");
			return;
		}

		removeLink(destdot);
		if(origdot.isGlobal()) {
			destdot.setSystemVariable(origdot.sysvar);
		} else if(origdot.isNull() && destdot.isGlobal()) {
			origdot.setSystemVariable(destdot.sysvar);
		} else if(origdot.isNull()) {
			Variable inner = system.addInnerVariable();
			origdot.setSystemVariable(inner);
			destdot.setSystemVariable(inner);
		} else if(destdot.isGlobal()) {
			removeLink(origdot);
			origdot.setSystemVariable(destdot.sysvar);
		} else {
			destdot.setSystemVariable(origdot.sysvar);
		}

		reallocate();
		refresh();
	}

	/**
	 * Elimina el enlace seleccionado
	 */
	private void removeLink() {
		removeLink(selecteddot);
		refresh();
	}

	/**
	 * Elimina un enlace
	 */
	private void removeLink(XfeditVariableDot rmdot) {
		if(rmdot == null) return;
		Variable nullvar = system.searchVariable("NULL");

		if(rmdot.isInternal())
			if(rmdot.isDestination()) {
				rmdot.setSystemVariable(nullvar);
			} else {
				rmdot.setSystemVariable(nullvar);
				for(int i=0; i<dot.length; i++)
					if(dot[i] != rmdot && dot[i].isInternal() && dot[i].sysvar == rmdot.sysvar)
						dot[i].setSystemVariable(nullvar);
			}

		if(rmdot.isGlobal())
			if(rmdot.isOrigin()) {
				for(int i=0; i<dot.length; i++)
					if(dot[i] != rmdot && dot[i].isInternal() && dot[i].sysvar == rmdot.sysvar)
						dot[i].setSystemVariable(nullvar);
			} else {
				for(int i=0; i<dot.length; i++)
					if(dot[i] != rmdot && dot[i].sysvar == rmdot.sysvar)
						dot[i].setSystemVariable(nullvar);
			}
	}

	/**
	 * Pinta la estructura
	 */
	private void paintBase(Graphics g) {
		Dimension size = getSize();
		this.totalheight = size.height;
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();

		int ih = (size.height-35-3*counter)/(input.length+1);
		int oh = (size.height-35-3*counter)/(output.length+1);
		int sw = size.width-inputwidth-outputwidth;

		g.setColor(XConstants.systembg);
		g.fillRoundRect(inputwidth,10,sw,size.height-20,15,15);
		g.setColor(Color.black);
		g.drawRoundRect(inputwidth,10,sw,size.height-20,15,15);

		for(int i=0; i<inputdot.length; i++) {
			inputdot[i].setPoint(inputwidth,10+ih+ih*i);
			inputdot[i].paintDot(g);
		}

		for(int i=0; i<outputdot.length; i++) {
			outputdot[i].setPoint(size.width-outputwidth,10+oh+oh*i);
			outputdot[i].paintDot(g);
		}

		varwidth = (size.width-inputwidth-outputwidth-levels*callwidth)/(levels+1);
		for(int lv=0; lv<levels; lv++) {
			int lvcounter = 0, lvheight = 0;
			for(int i=0; i<call.length; i++) if(call[i].level == lv) {
				lvcounter++;
				lvheight += call[i].height;
			}
			int ch = (size.height-35-lvheight-3*counter)/(lvcounter+1);
			int cpos = inputwidth + varwidth + lv*(varwidth + callwidth);
			int ypos = 10 + ch;
			for(int i=0; i<call.length; i++) if(call[i].level == lv) {
				call[i].paintCall(g,cpos,ypos,callwidth);
				ypos += ch+call[i].height;
			}
		}
		for(int i=0; i<call.length; i++) call[i].paintLinks(g);

		paintDrag(g);
	}

	/**
	 * Pinta la línea discontínua si se está creando un enlace
	 */
	private void paintDrag(Graphics g) {
		if(selecteddot == null || mousex == -1 || mousey == -1) return;

		float dash[] = {4.0f,4.0f};
		Graphics2D gd = (Graphics2D) g.create();
		gd.setStroke(new BasicStroke(1.0f,2,0,10.0f,dash,0.0f));
		gd.setColor(Color.black);
		gd.drawLine(selecteddot.x, selecteddot.y, mousex, mousey);
	}

	/**
	 * Pinta un enlace
	 */
	public void paintLink(Graphics g, XfeditVariableDot orig,
			XfeditVariableDot dest) {
		if(orig == null || dest == null) return;
		if(orig.isNull()) return;
		if(dest.isNull()) return;

		Color channelcolor = Color.black;
		switch(orig.channel%4) {
		case 0: channelcolor = Color.black; break;
		case 1: channelcolor = Color.red; break;
		case 2: channelcolor = Color.blue; break;
		case 3: channelcolor = Color.green; break;
		}
		g.setColor(channelcolor);

		if(orig.level == dest.level) {
			int channel = orig.x + varwidth/2 + (orig.channel-counter/2)*3;
			g.drawLine(orig.x, orig.y, channel, orig.y);
			g.drawLine(channel, orig.y, channel, dest.y);
			g.drawLine(channel, dest.y, dest.x, dest.y);
			if(selecteddot != dest && selecteddot != orig) return;
			g.drawLine(orig.x, orig.y-1, channel+1, orig.y-1);
			g.drawLine(channel+1, orig.y-1, channel+1, dest.y-1);
			g.drawLine(channel+1, dest.y-1, dest.x, dest.y-1);
		}
		else {
			int channel1 = orig.x + varwidth/2 + (orig.channel-counter/2)*3;
			int channel2 = totalheight - 25 - 3*orig.channel;
			int channel3 = dest.x - varwidth/2 + (orig.channel-counter/2)*3;
			g.drawLine(orig.x, orig.y, channel1, orig.y);
			g.drawLine(channel1, orig.y, channel1, channel2);
			g.drawLine(channel1, channel2, channel3, channel2);
			g.drawLine(channel3, channel2, channel3, dest.y);
			g.drawLine(channel3, dest.y, dest.x, dest.y);
			if(selecteddot != dest && selecteddot != orig) return;
			channel1++; channel2--; channel3++;
			g.drawLine(orig.x, orig.y-1, channel1, orig.y-1);
			g.drawLine(channel1, orig.y-1, channel1, channel2);
			g.drawLine(channel1, channel2, channel3, channel2);
			g.drawLine(channel3, channel2, channel3, dest.y);
			g.drawLine(channel3, dest.y-1, dest.x, dest.y-1);
		}
		g.setColor(Color.black);
	}
}
