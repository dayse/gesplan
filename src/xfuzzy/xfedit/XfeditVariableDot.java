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
import java.awt.*;

/**
 * Componente asociado a una variable (un punto) en la representación
 * gráfica de la estructura jerárquica de un sistema difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditVariableDot {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PUBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Componente de llamada al que pertenece la variable. Null si se trata de una
	 * variable global
	 */
	public XfeditCallComponent call;
	
	/**
	 * Variable asociada al componente
	 */
	public Variable sysvar;
	
	/**
	 * Posición horizontal del componente
	 */
	public int x;
	
	/**
	 * Posición vertical del componente
	 */
	public int y;
	
	/**
	 * Canal asignado para las conexiones
	 */
	public int channel;
	
	/**
	 * Nivel de profundidad
	 */
	public int level;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Indice de la variable en el componente de llamada
	 */
	private int index;
	
	/**
	 * Indicador del caracter de la variable asociada (entrada o salida)
	 */
	private boolean input;
	
	/**
	 * Característica del tipo de letra
	 */
	private int descent;
	
	/**
	 * Tipo de letra para escribir el nombre
	 */
	private FontMetrics fm;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor de un punto asociado a una variable de una llamada
	 */
	public XfeditVariableDot(XfeditStructure structure, XfeditCallComponent call,
			int index, int level, boolean input) {
		this.fm = structure.getFontMetrics(structure.getFont());
		this.descent = fm.getDescent();

		this.call = call;
		this.index = index;
		this.input = input;
		this.sysvar = (input ? call.call.getInputVariables()[index] :
			call.call.getOutputVariables()[index]);
		this.level = level;
		if(call != null && !input)
		{ this.channel = structure.counter; structure.counter++; }
		if(call == null && input)
		{ this.channel = structure.counter; structure.counter++; }
	}

	/**
	 * Constructor de un punto asociado a una variable global
	 */
	public XfeditVariableDot(XfeditStructure structure, Variable sysvar,
			int level, boolean input) {
		this.fm = structure.getFontMetrics(structure.getFont());
		this.descent = fm.getDescent();

		this.call = null;
		this.index = -1;
		this.input = input;
		this.sysvar = sysvar;
		this.level = level;
		if(call != null && !input)
		{ this.channel = structure.counter; structure.counter++; }
		if(call == null && input)
		{ this.channel = structure.counter; structure.counter++; }
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 *  Verifica si el punto corresponde a una variable global
	 */
	public boolean isGlobal() {
		return (call == null);
	}

	/**
	 * Verifica si el punto corresponde a una base de reglas
	 */
	public boolean isInternal() {
		return (call != null);
	}

	/**
	 * Verifica si el punto no está enlazado
	 */
	public boolean isNull() {
		return (sysvar == null || sysvar.equals("NULL"));
	}

	/**
	 * Verifica si el punto corresponde a una entrada
	 */
	public boolean isInput() {
		return input;
	}

	/**
	 * Verifica si el punto corresponde a una salida
	 */
	public boolean isOutput() {
		return !input;
	}

	/**
	 * Verifica si el punto puede ser origen de un enlace
	 */
	public boolean isOrigin() {
		return (call == null && input) ||
		(call != null && !input);
	}

	/**
	 * Verifica si el punto puede ser destino de un enlace
	 */
	public boolean isDestination() {
		return (call == null && !input) ||
		(call != null && input);
	}

	/**
	 *  Selecciona la variable de sistema del punto
	 */
	public void setSystemVariable(Variable var) {
		if(this.call == null) return;
		if(input) this.call.call.setInputVariable(index, var);
		if(!input) this.call.call.setOutputVariable(index, var);
		this.sysvar = var;
	}

	/**
	 * Selecciona la posición del punto
	 */
	public void setPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Verifica que la posición corresponde al punto
	 */
	public boolean include(int px, int py) {
		return (px>x - 5 && px<x + 5 && py>y - 2 && py<y + 5);
	}

	/**
	 * Dibuja el punto con su identificador
	 */
	public void paintDot(Graphics g) {
		String label = "";
		if(call == null) label = sysvar.toString();
		else if(call.call instanceof RulebaseCall) {
			RulebaseCall rbcall = (RulebaseCall) call.call;
			if(input) label = rbcall.getRulebase().getInputs()[index].toString();
			else label = rbcall.getRulebase().getOutputs()[index].toString();
		}
		int sw = fm.stringWidth(label);
		if(input) { 
			g.drawString(label,x-sw-2,y-descent-4);
			g.fillRect(x-5,y-2,5,5);
		}
		else {
			g.drawString(label,x+2,y-descent-4);
			g.fillRect(x,y-2,5,5);
		}
	}
}
