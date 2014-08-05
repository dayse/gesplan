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


package xfuzzy.xfsw;

import xfuzzy.lang.*;

import java.io.*;

/**
 * Generador del fichero de cabecera de la descripción C
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfswCHeader {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Sistema difuso a compilar
	 */
	private Specification spec;
	
	/**
	 * Directorio base para la creación del archivo
	 */
	private File dir;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfswCHeader(Specification spec, File dir) {
		this.spec = spec;
		this.dir = dir;
	}
	
	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el fichero de cabecera ".h" asociado al sistema difuso
	 */
	public static final String create(Specification spec,File dir) {
		XfswCHeader creator = new XfswCHeader(spec,dir);
		creator.createFile();
		return creator.getMessage();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Método que describe el resultado
	 */
	public String getMessage() {
		File file = new File(dir,spec.getName()+".h");
		return file.getAbsolutePath();
	}

	/**
	 * Método que general el fichero de cabecera "sistema.h"
	 */
	public void createFile() {
		File file = new File(dir,spec.getName()+".h");
		try {
			OutputStream ostream = new FileOutputStream(file);
			PrintStream stream = new PrintStream(ostream);
			writeHeading(stream);
			writeSource(stream);
			stream.close();
		}
		catch (IOException e) {}
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el codigo de cabecera	
	 */
	private void writeHeading(PrintStream stream) {
		
		stream.println("/*++++++++++++++++++++++++++++++++++++++++++++++++++++++*/");
		stream.println("/*                                                      */");
		stream.println(complete("/* File:  "+spec.getName()+".h"));
		stream.println("/*                                                      */");
		stream.println("/* Author: Automatically generated by Xfuzzy            */");
		stream.println("/*                                                      */");
		stream.println("/*++++++++++++++++++++++++++++++++++++++++++++++++++++++*/");
		stream.println();
	}

	/**
	 * Genera el contenido del fichero de cabecera
	 */
	private void writeSource(PrintStream stream) {
		String name = spec.getName();
		Variable[] input = spec.getSystemModule().getInputs();
		Variable[] output = spec.getSystemModule().getOutputs();

		stream.println("#ifndef XFUZZY");
		stream.println("#define XFUZZY");
		stream.println("/*------------------------------------------------------*/");
		stream.println("/* Fuzzy Number                                         */");
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.println(" typedef struct {");
		stream.println("   double (* equal)(double x);");
		stream.println("   double (* center)();");
		stream.println("   double (* basis)();");
		stream.println("   double (* param)(int i);");
		stream.println(" } Consequent;");
		stream.println();
		stream.println(" typedef struct {");
		stream.println("   double min;");
		stream.println("   double max;");
		stream.println("   double step;");	
		stream.println("   double (* imp)(double a, double b);");
		stream.println("   double (* also)(double a, double b);");
		stream.println("   int length;");
		stream.println("   double* degree;");
		stream.println("   int inputlength;");
		stream.println("   double* input;");
		stream.println("   Consequent* conc;");
		stream.println(" } FuzzyNumber;");
		stream.println();
		stream.println("#endif /* XFUZZY */");
		stream.println();
		stream.println();
		stream.println("#ifndef XFUZZY_"+name);
		stream.println("#define XFUZZY_"+name);
		stream.println("/*------------------------------------------------------*/");
		stream.println("/* Inference Engine                                     */");
		stream.println("/*------------------------------------------------------*/");
		stream.println();
		stream.print("void "+name+"InferenceEngine(");
		for(int i=0; i<input.length; i++) {
			if(i>0) stream.print(", ");
			stream.print("double "+input[i].getName());
		}
		for(int i=0; i<output.length; i++) {
			stream.print(", double* "+output[i].getName());
		}
		stream.println(");");
		stream.println();
		stream.println("#endif /* XFUZZY_"+name+" */");
	}

	/**
	 * Completa la cadena hasta 58 caracteres
	 */
	private String complete(String begining) {
		String line = begining;
		for(int i=begining.length(); i<56; i++) line += " ";
		line += "*/";
		return line;
	}
}
