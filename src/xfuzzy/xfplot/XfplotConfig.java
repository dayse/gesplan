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

/**
 * Clase quealmacena la configuración de la herramienta xfplot
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfplotConfig {
	
	//----------------------------------------------------------------------------//
	//                            CONSTANTES PRIVADAS                             //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constante para indicar el tipo de representación en 3D 
	 */
	private static final int PLOT3D = 0;
	
	/**
	 * Constante para indicar el tipo de representación en 2D
	 */
	private static final int PLOT2D = 1;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia al sistema difuso a representar
	 */
	private Specification spec;
	
	/**
	 * Índice de la variable de entrada a representar en el eje X
	 */
	private int xindex;
	
	/**
	 * Índice de la variable de entrada a representar en el eje Y
	 */
	private int yindex;
	
	/**
	 * Índice de la variable de salida a representar en el eje Z
	 */
	private int zindex;
	
	/**
	 * Valores de las variables de entrada que no se van a representar
	 */
	private double[] inputvalue;
	
	/**
	 * Valor del desplazador horizontal
	 */
	private int hslide;
	
	/**
	 * Valor del desplazador vertical
	 */
	private int vslide;
	
	/**
	 * Número de puntos en los que se divide cada eje
	 */
	private int samples;
	
	/**
	 * Tipo de representación
	 */
	private int plotmode;
	
	/**
	 * Modelo de colores
	 */
	private int colormodel;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfplotConfig(Specification spec) {
		this.spec = spec;
		
		Variable inputvar[] = spec.getSystemModule().getInputs();
		
		this.inputvalue = new double[inputvar.length];
		for(int i=0; i<inputvar.length; i++) inputvalue[i] = inputvar[i].point(0.5);
		this.xindex = 0;
		this.yindex = 1;
		this.zindex = 0;
		this.samples = 40;
		this.colormodel = 0;
		this.plotmode = PLOT3D;
		this.hslide = 50;
		this.vslide = 30;
	}
	
 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene el índice de la variable asignada al eje X
	 */
	public int getXIndex() {
		return this.xindex;
	}
	
	/**
	 * Obtiene el índice de la variable asignada al eje Y
	 */
	public int getYIndex() {
		return this.yindex;
	}
	
	/**
	 * Obtiene el índice de la variable asignada al eje Z
	 */
	public int getZIndex() {
		return this.zindex;
	}
	
	/**
	 * Obtiene los valores de las variables de entrada
	 */
	public double[] getInputValues() {
		return this.inputvalue;
	}
	/**
	 * Obtiene el número de divisiones de cada eje
	 */
	public int getSamples() {
		return this.samples;
	}
	
	/**
	 * Obtiene el código del tipo de representación
	 */
	public int getPlotMode() {
		return this.plotmode;
	}
	
	/**
	 * Obtiene el código del modelo de colores
	 */
	public int getColorMode() {
		return this.colormodel;
	}
	
	/**
	 * Obtiene la rotación horizontal
	 */
	public int getHSlide() {
		return this.hslide;
	}
	
	/**
	 * Obtiene la rotación vertical
	 */
	public int getVSlide() {
		return this.vslide;
	}
	
	/**
	 * Obtiene los datos de la representación en 2D
	 */
	public double[] get2DFunction() {
		SystemModule system = spec.getSystemModule();
		Variable inputvar[] = system.getInputs();
		Variable outputvar[] = system.getOutputs();
		double zmin = outputvar[zindex].point(0.0);
		double zmax = outputvar[zindex].point(1.0);
		
		double input[] = new double[inputvalue.length];
		for(int i=0; i<inputvalue.length; i++) input[i] = inputvalue[i];
		
		double[] function = new double[samples];
		for(int i=0; i<function.length; i++) {
			input[xindex] = inputvar[xindex].point(i*1.0/(samples-1));
			double output = system.crispInference(input)[zindex];
			function[i] = (output - zmin)/(zmax - zmin);
		}
		return function;
	}
	
	/**
	 * Obtiene los datos de la representación en 3D
	 */
	public double[][] get3DFunction() {
		SystemModule system = spec.getSystemModule();
		Variable inputvar[] = system.getInputs();
		Variable outputvar[] = system.getOutputs();
		double zmin = outputvar[zindex].point(0.0);
		double zmax = outputvar[zindex].point(1.0);
		
		double input[] = new double[inputvalue.length];
		for(int i=0; i<inputvalue.length; i++) input[i] = inputvalue[i];
		
		double[][] function = new double[samples][samples];
		for(int i=0; i<samples; i++)
			for(int j=0; j<samples; j++) {
				input[xindex] = inputvar[xindex].point(i*1.0/(samples-1));
				input[yindex] = inputvar[yindex].point(j*1.0/(samples-1));
				double output = system.crispInference(input)[zindex];
				function[i][j] = (output - zmin)/(zmax - zmin);
			}
		return function;
	}
	
	/**
	 * Asigna el valor a las variables de entrada
	 */
	public void setInputValues(double[] value) throws Exception {
		if(value.length > inputvalue.length) throw new Exception();
		Variable inputvar[] = spec.getSystemModule().getInputs();
		for(int i=0; i<inputvar.length; i++) {
			double rate = inputvar[i].getRate(value[i]);
			if(rate < 0.0 || rate > 1.0) throw new Exception();
		}
		for(int i=0; i<inputvalue.length; i++) inputvalue[i] = value[i];
	}
	
	/**
	 * Asigna las variables a representar
	 */
	public void setVariables(String xvar, String yvar, String zvar)
	throws Exception {
		Variable inputvar[] = spec.getSystemModule().getInputs();
		Variable outputvar[] = spec.getSystemModule().getInputs();
		int xi = -1;
		int yi = -1;
		int zi = -1;
		for(int i=0; i<inputvar.length; i++) {
			if(inputvar[i].toString().equals(xvar)) xi = i;
			if(inputvar[i].toString().equals(yvar)) yi = i;
		}
		for(int i=0; i<outputvar.length; i++) {
			if(outputvar[i].toString().equals(zvar)) zi = i;
		}
		if(xi<0 || yi<0 || xi==yi || zi<0) throw new Exception();
		
		this.xindex = xi;
		this.yindex = yi;
		this.zindex = zi;
		this.plotmode = PLOT3D;
	}
	
	/**
	 * Asigna las variables a representar
	 */
	public void setVariables(String xvar, String zvar)
	throws Exception {
		Variable inputvar[] = spec.getSystemModule().getInputs();
		Variable outputvar[] = spec.getSystemModule().getInputs();
		int xi = -1;
		int zi = -1;
		for(int i=0; i<inputvar.length; i++) {
			if(inputvar[i].toString().equals(xvar)) xi = i;
		}
		for(int i=0; i<outputvar.length; i++) {
			if(outputvar[i].toString().equals(zvar)) zi = i;
		}
		if(xi<0 || zi<0) throw new Exception();
		
		this.xindex = xi;
		this.yindex = 0;
		this.zindex = zi;
		this.plotmode = PLOT2D;
	}
	
	/**
	 * Asigna los valores de la perspectiva
	 */
	public void setSlides(int horiz, int vert) throws Exception {
		if(vert<0 || vert>100 || horiz<0 || horiz>100) throw new Exception();
		this.hslide = horiz;
		this.vslide = vert;
	}
	
	/**
	 * Asigna el tipo de representación
	 */
	public void setPlotMode(int mode) throws Exception {
		if(mode != PLOT3D && mode != PLOT2D) throw new Exception();
		this.plotmode = mode;
	}
	
	/**
	 * Asigna el número de divisiones de cada eje
	 */
	public void setSamples(int number) throws Exception {
		if(number<0) throw new Exception();
		this.samples = number;
	}
	
	/**
	 * Asigna el modelo de color
	 */
	public void setColorMode(int code) throws Exception {
		if(code<0) throw new Exception();
		this.colormodel = code;
	}
	
	/**
	 * Selecciona la variable asignada al eje X
	 */
	public void setXIndex(int index) {
		this.xindex = index;
	}
	
	/**
	 * Selecciona la variable asignada al eje Y
	 */
	public void setYIndex(int index) {
		this.yindex = index;
	}
	
	/**
	 * Selecciona la variable asignada al eje Z
	 */
	public void setZIndex(int index) {
		this.zindex = index;
	}
	
	/**
	 * Devuelve la cabecera del fichero de datos
	 */
	public String getHeading() {
		String eol = System.getProperty("line.separator", "\n");
		Variable inputvar[] = spec.getSystemModule().getInputs();
		Variable outputvar[] = spec.getSystemModule().getInputs();
		Variable xvar = inputvar[xindex];
		Variable yvar = inputvar[yindex];
		Variable zvar = outputvar[zindex];
		
		String code = "// Data generated by Xfplot"+eol;
		code += "// System: "+spec.getName()+eol;
		code += "// X Axis: "+xvar.toString()+eol;
		code += "// Y Axis: "+yvar.toString()+eol;
		code += "// Z Axis: "+zvar.toString()+eol;
		code += "// Input values: "+eol;
		for(int i=0; i<inputvar.length; i++)
			code += "//  "+inputvar[i].toString()+" : "+inputvalue[i]+eol;
		code += eol+eol;
		return code;
	}
	
	/**
	 * Salva la configuración en un fichero externo
	 */
	public void save(File file) {
		Variable inputvar[] = spec.getSystemModule().getInputs();
		Variable outputvar[] = spec.getSystemModule().getInputs();
		String eol = System.getProperty("line.separator", "\n");
		
		String code;
		if(plotmode == PLOT3D) {
			code = "xfplot_graph3D("+inputvar[xindex]+", "+inputvar[yindex];
			code += ", "+outputvar[zindex]+")"+eol;
		} else {
			code = "xfplot_graph2D("+inputvar[xindex]+", "+outputvar[zindex]+")"+eol;
		}
		code += "xfplot_values("+inputvalue[0];
		for(int i=1; i<inputvalue.length; i++) code += ", "+inputvalue[i];
		code += ")"+eol;
		code += "xfplot_perspective("+hslide+", "+vslide+")"+eol;
		code += "xfplot_samples("+samples+")"+eol;
		code += "xfplot_colormode("+colormodel+")"+eol;
		
		try {
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(code.getBytes());
			stream.close();
		} catch(Exception ex) {}
	}
}
