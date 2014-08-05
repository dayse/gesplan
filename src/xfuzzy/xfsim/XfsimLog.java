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
//	  GRABACION DE LA SIMULACION EN UN FICHERO DE LOG	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsim;

import xfuzzy.lang.*;
import java.io.*;
import java.util.*;

/**
 * Clase que permite almacenar los resultados de un proceso de
 * simulación en un fichero
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimLog {
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Fichero en el que se guarda la información
	 */
	private File file;
	
	/**
	 * Selección de datos a almacenar
	 */
	private boolean log[];
	
	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	/**
	 * Flujo de datos hacia el fichero
	 */
	private PrintStream stream;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public XfsimLog(Xfsim xfsim) {
		this.xfsim = xfsim;
	}
	
	/**
	 * Constructor
	 * @param xfsim
	 * @param file
	 * @param v
	 */
	public XfsimLog(Xfsim xfsim,File file,Vector v) {
		this.xfsim = xfsim;
		this.file = file;
		
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		this.log = new boolean[2+input.length+output.length];
		
		for(int i=0; i<v.size(); i++) {
			String var = (String) v.elementAt(i);
			if(var.equals("_n")) log[0] = true;
			if(var.equals("_t")) log[1] = true;
			for(int j=0; j<output.length; j++)
				if( var.equals(output[j].getName()) ) log[j+2] = true;
			for(int j=0; j<input.length; j++)
				if( var.equals(input[j].getName()) ) log[j+output.length+2] = true;
		}
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Devuelve el fichero seleccionado para almacenar la historia
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Selecciona el fichero para almacenar la historia
	 */
	public void setFile(File file) {
		this.file = new File(file.getAbsolutePath());
	}
	
	/**
	 * Devuelve la selección de variables a almacenar
	 */
	public boolean[] getSelection() {
		return this.log;
	}
	
	/**
	 * Establece la selección de variables a almacenar
	 */
	public void setSelection(boolean[] sel) {
		this.log = new boolean[sel.length];
		for(int i=0; i<sel.length; i++) log[i] = sel[i];
	}
	
	/**
	 * Mensaje que se muestra en la ventana principal de la aplicación
	 */
	public String toString() {
		String code = "file(\""+file.getName()+"\"";
		if(log[0]) code += ",_n";
		if(log[1]) code += ",_t";
		
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		for(int i=0; i<output.length; i++)
			if(log[i+2]) code += ","+output[i].getName();
		for(int i=0; i<input.length; i++)
			if(log[i+output.length+2]) code += ","+input[i].getName();
		code += ")";
		return code;
	}
	
	/**
	 * Descripción a almacenar en el fichero de configuración
	 */
	public String toCode() {
		String code = "xfsim_log(\""+file.getAbsolutePath()+"\"";
		if(log[0]) code += ",_n";
		if(log[1]) code += ",_t";
		
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		for(int i=0; i<output.length; i++)
			if(log[i+2]) code += ","+output[i].getName();
		for(int i=0; i<input.length; i++)
			if(log[i+output.length+2]) code += ","+input[i].getName();
		code += ")";
		return code;
	}
	
	/**
	 * Abre el fichero y escribe la cabecera
	 */
	public void open() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			stream = new PrintStream(fos);
			stream.println("# "+toString());
			stream.println();
		} catch(Exception ex) { System.out.println(""+ex); }
	}
	
	/**
	 *  Cierra el fichero
	 */
	public void close() {
		try { stream.close(); } catch(Exception ex) {}
	}
	
	/**
	 * Almacena el resultado de una iteración de la simulación
	 */
	public void iter(double iter, double time, double[] fzst, double[] ptst) {
		String line = "";
		if(log[0]) line += iter+" ";
		if(log[1]) line += time+" ";
		for(int i=0; i<fzst.length; i++) if(log[i+2]) line += fzst[i]+" ";
		for(int i=0; i<ptst.length; i++) if(log[i+2+fzst.length]) line+=ptst[i]+" ";
		try { stream.println(line); } catch(Exception ex) {}
	}
}
