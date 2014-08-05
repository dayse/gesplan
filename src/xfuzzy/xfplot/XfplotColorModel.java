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
//		COLORES DE LA REPRESENTACION SOLIDA		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfplot;

import java.awt.*;

/**
 * Clase que desarrolla los distintos modelos de colores de la representación
 * en 3D
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfplotColorModel {
	
	//----------------------------------------------------------------------------//
	//                            CONSTANTES PUBLICAS                             //
	//----------------------------------------------------------------------------//
	
	public static final int WHITE = 0;
	public static final int HUE_1 = 1;
	public static final int HUE_2 = 2;
	public static final int HUE_3 = 3;
	public static final int HUE_4 = 4;
	public static final int BRI_1 = 5;
	public static final int BRI_2 = 6;
	public static final int BRI_3 = 7;
	public static final int BRI_4 = 8;
	public static final int BRI_5 = 9;
	public static final int B_W = 10;
	public static final int COUNTER = 11;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Código del modelo
	 */
	private int model;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public XfplotColorModel(int model) {
		this.model = model;
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el código del modelo
	 */
	public int getModel() {
		return this.model;
	}
	
	/**
	 * Selecciona el código del modelo
	 */
	public void setModel(int model) {
		this.model = model;
	}
	
	/**
	 * Modifica el modelo seleccionado
	 */
	public void change() {
		this.model = (this.model+1)%COUNTER;
	}
	
	/**
	 * Devuelve el color asociado al valor selecionado
	 */
	public Color getColor(double value) {
		switch(model) {
		case WHITE:
			return Color.white;
		case HUE_1:
			return new Color(Color.HSBtoRGB((float) value, 1.0f, 1.0f));
		case HUE_2:
			return new Color(Color.HSBtoRGB((float) (value/2), 1.0f, 1.0f));
		case HUE_3:
			return new Color(Color.HSBtoRGB((float) (0.25+value/2),1.0f,1.0f));
		case HUE_4:
			return new Color(Color.HSBtoRGB((float) (0.5+value/2), 1.0f, 1.0f));
		case BRI_1:
			return new Color(Color.HSBtoRGB(0.3125f, 1.0f, (float) (0.25+value/2) ));
		case BRI_2:
			return new Color(Color.HSBtoRGB(0.0f, 1.0f, (float) (0.25+value/2) ));
		case BRI_3:
			return new Color(Color.HSBtoRGB(0.859f, 1.0f, (float) (0.25+value/2) ));
		case BRI_4:
			return new Color(Color.HSBtoRGB(0.625f, 1.0f, (float) (0.25+value/2) ));
		case BRI_5:
			return new Color(Color.HSBtoRGB(0.469f, 1.0f, (float) (0.25+value/2) ));
		case B_W:
			return new Color(Color.HSBtoRGB(0.0f, 0.0f, (float) (0.25+value/2) ));
		default: return Color.black;
		}
	}
	
	/**
	 * Devuelve el color asociado al grid
	 */
	public Color getGridColor() {
		switch(model) {
		case WHITE: return Color.red;
		case HUE_1: return Color.red;
		case HUE_2: return Color.red;
		case HUE_3: return Color.red;
		case HUE_4: return Color.red;
		case BRI_1: return Color.red;
		case BRI_2: return Color.red;
		case BRI_3: return Color.red;
		case BRI_4: return Color.red;
		case BRI_5: return Color.red;
		case B_W: return null;
		default: return null;
		}
	}
}
