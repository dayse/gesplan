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

package xfuzzy.xfsim;

import xfuzzy.*;
import xfuzzy.lang.*;
import java.io.*;
import java.util.*;

/**
 * Clase que contiene la configuración de un proceso de simulación
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimConfig extends ClassLoader {
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Fichero ".class" que describe el modelo de planta
	 */
	public File plantfile;
	
	/**
	 * Clase instanciada del modelo de planta
	 */
	public PlantModel plant;
	
	/**
	 * Valores iniciales de la planta
	 */
	public double[] init;
	
	/**
	 * Condiciones de finalización de la simulación
	 */
	public XfsimLimit limit;
	
	/**
	 * Lista de salidas de la simulación
	 */
	public Vector output;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfsimConfig(Xfsim xfsim) {
		this.xfsim = xfsim;
		this.limit = new XfsimLimit(xfsim);
		this.output = new Vector();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
		
	/**
	 * Verifica que la configuración está completa	
	 */
	public boolean isConfigured() {
		if(plant == null || !limit.isConfigured()) return false;
		for(int i=0; i<output.size(); i++) {
			try{ if( !((XfsimPlot) output.elementAt(i)).isCorrect()) return false; }
			catch(Exception e) {}
		}
		return true;
	}
	
	/**
	 * Selecciona el fichero que contiene el modelo de planta
	 */
	public void setPlantModel(File file) throws XflException {
		try {
			Class pclass = loadClass(file);
			plant = (PlantModel) pclass.newInstance();
			plantfile = file;
		} catch(Exception e) { throw new XflException(32); }
	}
	
	/**
	 * Genera una clase desde un fichero externo
	 */
	public Class loadClass(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			byte[] b = new byte[stream.available()];
			stream.read(b);
			stream.close();
			return defineClass(null,b,0,b.length);
		} catch (Exception ex) { return null; }
	}
	
	/**
	 * Añade una salida a un fichero histórico
	 */
	public void addLogFile(File file, Vector v) {
		output.add(new XfsimLog(xfsim,file,v) );
	}
	
	/**
	 * Añade una salida de representación gráfica
	 */
	public void addPlot(String xname, String yname, int kind) {
		output.add(new XfsimPlot(xfsim,xname,yname,kind) );
	}
	
	/**
	 * Almacena la configuración en un fichero externo
	 */
	public void save(File file) {
		String eol = System.getProperty("line.separator", "\n");
		String code = "";
		if(plantfile != null)
			code += "xfsim_plant(\""+plantfile.getAbsolutePath()+"\")"+eol;
		if(init != null && init.length != 0) {
			code += "xfsim_init(";
			for(int i=0;i<init.length; i++) code += (i==0? "":", ")+init[i];
			code += ")"+eol;
		}
		
		code += limit.toCode()+eol;
		
		for(int i=0; i<output.size(); i++) {
			try{ code += ((XfsimLog) output.elementAt(i)).toCode()+eol; }
			catch(Exception e) {}
			try{ code += ((XfsimPlot) output.elementAt(i)).toCode()+eol; }
			catch(Exception e) {}
		}
		
		try {
			FileOutputStream stream = new FileOutputStream(file); 
			stream.write(code.getBytes());
			stream.close(); 
		} catch(Exception ex) {}
	}
	
	/**
	 * Abre los elementos de salida del proceso de simulación
	 */
	public void open() {
		for(int i=0; i<output.size(); i++) {
			try{ ((XfsimLog) output.elementAt(i)).open(); } catch(Exception e) {}
			try{ ((XfsimPlot) output.elementAt(i)).open(); } catch(Exception e) {}
		}
	}
	
	/**
	 * Cierra los ficheros de log al finalizar la simulación
	 */
	public void end() {
		for(int i=0; i<output.size(); i++) {
			try{ ((XfsimLog) output.elementAt(i)).close(); } catch(Exception e) {}
		}
	}
	
	/**
	 * Cierra los ficheros de log y las representaciones
	 */
	public void close() {
		for(int i=0; i<output.size(); i++) {
			try{ ((XfsimLog) output.elementAt(i)).close(); } catch(Exception e) {}
			try{ ((XfsimPlot) output.elementAt(i)).close(); } catch(Exception e) {}
		}
	}
	
	/**
	 * Vuelca los resultados de una iteración en las salidas
	 */
	public void iter(double iter,double time,double[] fzst,double[] ptst) {
		for(int i=0; i<output.size(); i++) {
			try{ ((XfsimLog) output.elementAt(i)).iter(iter,time,fzst,ptst); }
			catch(Exception e) {}
			try{ ((XfsimPlot) output.elementAt(i)).iter(iter,time,fzst,ptst); }
			catch(Exception e) {}
		}
	}
}
