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
//			HERRAMIENTA DE SÍNTESIS: XFVHDL	 			    
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//


package xfuzzy.xfvhdl;

import xfuzzy.*;


import xfuzzy.lang.*;

import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;


/**
* Clase principal de la herramienta de síntesis Xfvhdl
* 
* @author Lidia Lavinia Delgado Carretero
* 
*/

public class Xfvhdl {
	//ATRIBUTOS DE LA CLASE
	/**
	*  Atributo que apunta al objeto Xfuzzy, siempre y cuando 
	*  se ejecute desde Xfuzzy y no desde consola. 				       
	*/
	public static Xfuzzy xfuzzy = null;
	
	/** Aquí se guarda el contenido del fichero con extensión .prj*/
	private XfvhdlPrjFile prjFile = new XfvhdlPrjFile();
	
	/** Atributo encargado de mostrar mensajes en la interfaz gráfica 
	 * de Xfuzzy*/
	XfvhdlMessage msg = new XfvhdlMessage(xfuzzy);
	
	/** Lista que almacena todos los módulos de inferencia del sistema 
	 * jerárquico.*/
	private ArrayList<XfvhdlFLC> listaFlc;
	
	/** Lista que almacena todos los módulos crisp del sistema 
	 * jerárquico.*/
	private ArrayList<XfvhdlCrisp> listaCrisp;
	
	/**
	*  Atributo que hace de bandera para cerrar la ventana de la 
	*  herramienta en el caso de que se produzca algún fallo que 
	*  requiera su cierre. 				       
	*/
	public static boolean cerrarWindow;
	 
	/**
	*  Método constructor de la clase. A partir de una especificación
	*  XFL y el árbol del sistema difuso, inicializa las variables estáticas
	*  definidas en XfvhdlProperties, y que sirven de variables globales 
	*  para todo el paquete. Es también aquí donde se crean la mayoría de 
	*  ficheros de salida y donde se llaman a las herramientas de síntesis 
	*  e implementación si es necesario. 
	*  @param spec	Especificación XFL
	*  @param top	Árbol del sistema difuso			 	   
	*/
	public Xfvhdl(Specification spec, DefaultMutableTreeNode top) throws IOException {
		listaCrisp=new ArrayList<XfvhdlCrisp>();
		listaFlc=new ArrayList<XfvhdlFLC>();
		   
	    // Dependiendo del método de defuzzificación se incluirá
	    // una etapa de salida simplificada
	    XfvhdlProperties.simplified_division = false;
	     
	    if (XfvhdlProperties.defuzzification_type.equalsIgnoreCase("MaxLabel")) {
	    	XfvhdlProperties.simplified_division = true;
	    }

	    if (XfvhdlProperties.defuzzification_type.equalsIgnoreCase("FuzzyMean")
	    		&& XfvhdlProperties.simplifiedComponents == true) {
	    	XfvhdlProperties.simplified_division = true;
	    }
	          
	    // Crea e inicializa un objeto error
	    XfvhdlError error = new XfvhdlError();
	    String state = new String("Done");

	    // Activa el log si se está ejecutando desde la ventana gráfica
	    if (XfvhdlProperties.inWindow == true)
	    	msg.setXfuzzy(xfuzzy);

      
	    //Codigo nuevo
	    String aux="";
	    XfvhdlFLC nodeflc=null;
	    int n = top.getChildCount();
	    cerrarWindow = false;
	    for (int i = 0; i < n && !cerrarWindow ; i++) {
	    	DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) top
				.getChildAt(i);
	    	aux = genera_texto_rama(dmtn, spec);
	    	if(aux.startsWith("error")){
	    		cerrarWindow = true;
	    		int numError= Integer.valueOf(aux.substring(6, 8));
	    		new XfvhdlError(numError, aux.substring(8) + ".");
	    	}
	    }
	    if(!cerrarWindow){
	    XfvhdlLeerXfl xfl = new XfvhdlLeerXfl(spec);
	    xfl.inicializarFLCs();
	    xfl.addToPrjCrispFunction(prjFile);
	    prjFile.addLibBlocks(listaFlc);
	    String jerarquia=xfl.generaJerarquia(listaFlc,listaCrisp);
	    String testBenchJerarquia="";
	    XfvhdlTestBenchFile tbj=new XfvhdlTestBenchFile();
	    if (jerarquia!=null){//Devuelve null cuando sólo hay un módulo de inferencia
	    	prjFile.addFile("vhdl work \""+XfvhdlProperties.name_outputfiles+".vhd\"");
	    	new XfvhdlCreateFile(
	    			XfvhdlProperties.name_outputfiles+".vhd",
	    			jerarquia);
	    	msg.addMessage(
	    			"\n>>>> Generating file:  "
	    			+ XfvhdlProperties.name_outputfiles
	    			+ ".vhd");
	      
	    	if (error.hasErrors())
	    		state = "With error";
		    msg.addMessage("     .........    " + state);
		    msg.show();
		      
		    //Ahora se genera el test bench del nivel jerárquico
		    testBenchJerarquia=tbj.createTestBenchSource(spec, null,listaFlc,listaCrisp);
		    new XfvhdlCreateFile(
		    		XfvhdlProperties.name_outputfiles+"_tb.vhd",
		    		testBenchJerarquia);
		    msg.addMessage(
		    		"\n>>>> Generating file:  "
		    		+ XfvhdlProperties.name_outputfiles
		    		+ "_tb.vhd");
			  
		    if (error.hasErrors())
		    	state = "With error";	
		    msg.addMessage("     .........    " + state);
		    msg.show();
	      	}
	      

	      

	    // ****************************************************************
	    // ************** Creando el fichero prj **************************
	    // ****************************************************************
	    // *** Este fichero se genera cuando la herramienta de síntesis 
	    // *** es Xilinx XST                                        
	    // ****************************************************************
	    if (XfvhdlProperties.synthesisTool.equals((String) "XILINX_XST")) {
	    	msg.addMessage(
	    			">>>> Generating file:  "
	    			+ XfvhdlProperties.name_outputfiles
	    			+ ".prj");
	         
	    	// Crea el fichero .prj.
	    	new XfvhdlCreateFile(
	    			XfvhdlProperties.name_outputfiles + ".prj",
	    			prjFile.getPrj());
	    	if (error.hasErrors())
	    		state = "With error";
	    	msg.addMessage("     .........    " + state);
	    	msg.show();
	      	}
	    // ************* !! Fichero .prj creado ¡¡ ************************	 

	    // ****************************************************************
	    // ************** Creando el fichero de script XST ****************
	    // ****************************************************************
	    // *** Este fichero se genera cuando la herramienta de síntesis 
	    // *** es Xilinx XST	                                        
	    // ****************************************************************
	    if (XfvhdlProperties.synthesisTool.equals((String) "XILINX_XST")) {
	    	msg.addMessage(
	    			">>>> Generating file:  "
	    			+ XfvhdlProperties.name_outputfiles
	    			+ "Script.xst");
	         
	    	XfvhdlScriptXstFile ScriptXstFile = new XfvhdlScriptXstFile();
	    	// Crea el fichero de script
	    	new XfvhdlCreateFile(
	    			XfvhdlProperties.name_outputfiles + "Script.xst",
	    			ScriptXstFile.createScriptXstSource());
	    	if (error.hasErrors())
	    		state = "With error";
	    	msg.addMessage("     .........    " + state);
	    	msg.show();
	    }
	    // ************* !! Fichero de script XST  creado ¡¡ **************	 
	    
	    // ****************************************************************
	    // ************** Creando el fichero de script FST ****************
	    // ****************************************************************
	    // *** Este fichero se genera cuando la herramienta de síntesis 
	    // *** es una de las herramientas de synopsys	                
	    // ****************************************************************
	    if (XfvhdlProperties.synthesisTool.equals((String) "FPGA_Express")
	    		|| XfvhdlProperties.synthesisTool.equals(
	    				(String) "FPGA_Compiler_2")) {
	    	msg.addMessage(
	    			">>>> Generating file:  "
	    			+ XfvhdlProperties.name_outputfiles
	    			+ "Script.fst");
	      
	    	XfvhdlScriptFile ScriptFile = new XfvhdlScriptFile();	
	    	// Crea el fichero de script
	    	new XfvhdlCreateFile(
	    			XfvhdlProperties.name_outputfiles + "Script.fst",
	    			ScriptFile.createScriptSource());
	    	if (error.hasErrors())
	    		state = "With error";
	    	msg.addMessage("     .........    " + state);
	    	msg.show();
	    }
    	// ************* !! Fichero de script FST creado ¡¡ **************
	    
	    //XfvhdlProperties.simplifiedComponents = false;
	    //XfvhdlProperties.complementaryFiles = false;
	
	    }
	}
	    
	 
	/**Método que genera los ficheros de extensión .vhd correspondientes 
	 * a los módulos de inferencia y a los módulos crisp, también genera 
	 * los ficheros de test bench de estos módulos.
	 * @param node Nodo padre a partir del cual recorremos el sistema jerárquico.
	 * @param spec Especificación XFL.*/ 
	 
	 
	public String genera_texto_rama(DefaultMutableTreeNode node, Specification spec) throws IOException {
		// Crea e inicializa un objeto error
		XfvhdlError error = new XfvhdlError();
	    String state = new String("Done");
		
	    // contiene la información a escribir en el archivo .vhd
		String code = "";
		String testBench="";
		
		String res;
		int n = node.getChildCount();
		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			Object o = dmtn.getUserObject();
			if (o instanceof XfvhdlFLC) {
				XfvhdlFLC f1 = (XfvhdlFLC) o;
				if(f1.compruebaPbAdecuado()==false)
					return "error" + " " + 39 + f1.getname();
			}
		}
		String nombre_i="";
		int regla_i=-1;
		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node
					.getChildAt(i);
			Object o = dmtn.getUserObject();
			if (o instanceof XfvhdlFLC) {
				
				regla_i++;
				XfvhdlFLC f1 = (XfvhdlFLC) o;
				nombre_i=f1.getname();
				// ****************************************************************
			    // ************ Creando el fichero vhd de cada flc ****************
			    // ****************************************************************
			    // *** Este fichero se genera siempre, independientemente de la 
			    // *** opción elegida para implementar la memoria	            
			    // ****************************************************************
				f1.generaVHDL(spec,nombre_i);
				code = f1.getVHDL();
				new XfvhdlCreateFile(
			    		  f1.getname()+".vhd",
			    		  code);
				msg.addMessage(
					         ">>>> Generating file:  "
					            + f1.getname()+".vhd");
				
				if (error.hasErrors())
		            state = "With error";
				msg.addMessage("     .........    " + state);
				msg.show();
			
				if (XfvhdlProperties.complementaryFiles == true) {
					             msg.addMessage(
					                ">>>> Generating rules memory's "
					                   + "complementary files ");
					             
					      f1.generaComplementarios(nombre_i);
					      if (error.hasErrors())
					                state = "With error";
					             msg.addMessage("     .........    " + state);
					             msg.show();
				}
				
				// ****************************************************************
			    // ************** Creando los testbench ****************************
			    // ****************************************************************
			    // *** Este fichero se genera siempre, independientemente de la 
			    // *** opción elegida para implementar la memoria	            
			    // ****************************************************************
				testBench=f1.getTestBench(spec);
				new XfvhdlCreateFile(
			    		  f1.getname()+"_tb.vhd",
			    		  testBench);
				
				msg.addMessage(
				         ">>>> Generating file:  "
							+ f1.getname()+"_tb.vhd");
				
				if (error.hasErrors())
		            state = "With error";
		         msg.addMessage("     .........    " + state);
		         msg.show();
				
				listaFlc.add(f1);

			} else if (o instanceof XfvhdlCrisp) {
				XfvhdlCrisp c1 = (XfvhdlCrisp) o;
				
				c1.generaVHDL();
				code += c1.getVHDL();
				listaCrisp.add(c1);
				
			}
			
		}
		res = code;
		XfvhdlError err = new XfvhdlError();
		// System.out.println(err.hasErrors());
		if (err.hasErrors() || err.hasWarnings()) {
			msg.addMessage(err.getMessages());
			msg.show();
			err.resetAll();
		}
		return res;
	}


	/**Este método es el método principal de la clase, aunque sólo es 
	 * llamado cuando lanzamos la aplicación desde un terminal de comandos.*/

	public static void main(String[] args) throws IOException {
	      // Trata los parámetros que se pasan desde la línea de comandos
	      XfvhdlParameter param = new XfvhdlParameter(args);
	      param.loadParameter();

	      // A partir de aquí depende si la aplicación se ejecuta desde la 
	      // consola o desde una ventana
	      if (XfvhdlProperties.inWindow == false) {
	          // EJECUTAMOS DESDE LA CONSOLA
	    	  // Lee el fichero XFL y le pasa el parser	   
	          XflParser xflparser = new XflParser();
	          XfvhdlMessage msg2 = new XfvhdlMessage(xfuzzy);
	          msg2.addMessage(">>>> File Syntax Analysis:");
	          msg2.show();
	          Specification spec = xflparser.parse(XfvhdlProperties.ficheroXFL);
	          DefaultMutableTreeNode top = new DefaultMutableTreeNode(spec.getName());
	          msg2.addMessage("        Correct File Syntax.\n");
	          msg2.show();
	          XfvhdlWindow window;
	          if(spec!=null){
		          window = new XfvhdlWindow(null,spec);
		          window.setVisible(false);
		          if(XfvhdlProperties.synthesis)
		        	  window.generateVHDLCodeAndSynthetize();
		          else if(XfvhdlProperties.implementation)
		        	  window.generateVHDLCodeAndImplement();
		          else
		        	  window.generateVHDLCode();
		          
	          }else{//Ha dado fallo al cargar la especificación
	    		  XfvhdlError err = new XfvhdlError(6);
	              err.show();
	              System.exit(-1);
		      }
	      } else {
	         // EJECUTAMOS DESDE UNA VENTANA
	    	  XflParser xflparser = new XflParser();
	    	  String ruta=XfvhdlProperties.ficheroXFL;
	    	  Specification sp = xflparser.parse(XfvhdlProperties.ficheroXFL);
	    	  if(sp!=null){
		         XfvhdlWindow window = new XfvhdlWindow(null,sp);
		         window.setVisible(true);}
	    	  else{//Ha dado fallo al cargar la especificación
	    		  XfvhdlError err = new XfvhdlError(6);
	              err.show();
	              System.exit(-1);
	         }
	      }
	   }

	/**Este método es llamado siempre que se lanza la herramienta, aunque si 
	 * es llamado cuando hemos lanzado la herramienta desde un terminal, 
	 * hay que leer los parámetros pasados por el usuario desde consola.*/
	public static void inConsoleExecution(Specification spec, DefaultMutableTreeNode top,String args[])
	throws IOException {

		// Aunque estamos dentro de la ejecución desde consola, en verdad,
		// este método se ejecuta siempre,la diferencia es que si antes hemos
		// mostrado la ventana gráfica, ahora hay que leer los parámetros
		// que se pasan desde ella
	      

		XfvhdlMessage msg2 = new XfvhdlMessage(xfuzzy);
		
		// Saca la información necesaria del conjunto de operadores,
		// tales como el método de defuzzificación y la operación AND.
		Operatorset[] opset = spec.getOperatorsets();
		String opAnd = opset[0].and.getFunctionName();
		String defuzMethod = opset[0].defuz.getFunctionName();
		if (opAnd.equalsIgnoreCase("min") || opAnd.equalsIgnoreCase("prod"))
			XfvhdlProperties.operationAnd = opAnd;
		else {
			XfvhdlError err = new XfvhdlError();
			err.newWarning(28);
		}
		XfvhdlProperties.defuzzification_type = defuzMethod;

		// Llama al constructor de la clase con la espeficicación del sistema
		new Xfvhdl(spec,top);
	      
		// Muestra por pantalla los errores que se han producido
		XfvhdlError error = new XfvhdlError();
		error.show();
	      
		if(cerrarWindow){
			//XfvhdlError err4 = new XfvhdlError(39,""+"");
			//err4.show();
			return;
		}
	      
		// Lanza la herramienta de síntesis -si procede-
		if (XfvhdlProperties.synthesis == true) {
			//No puede haber errores
			if (error.hasErrors()) {
				XfvhdlError err4 = new XfvhdlError(24);
				err4.show();
				return;
			}
			
			String command1 = new String();

			//Depende de la herramienta que se haya indicado
			if (XfvhdlProperties.synthesisTool.equals((String) "XILINX_XST")) {
				// Crea una cadena con la ruta del directorio que necesita
	            // crear, y luego lo crea
				String hola =
					new String(
							""
							+ XfvhdlProperties.outputDirectory
							+ XfvhdlProperties.fileSeparator
							+ XfvhdlProperties.name_outputfiles
							+ "_synthesis");
				File temp = new File(hola);
				temp.mkdirs();

				msg2.addMessage("\n\n>>>> Running Xilinx XST\n");
				command1 =
					"xst -ifn "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ "Script.xst -ofn "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ ".log";
				
			} else if (
					XfvhdlProperties.synthesisTool.equals(
							(String) "FPGA_Express")) {
				msg2.addMessage("\n\n>>>> Running Synopsys FPGA Express\n");
				command1 =
					"fe_shell -oem xilinx -f "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ "Script.fst";
			} else if (
					XfvhdlProperties.synthesisTool.equals(
							(String) "FPGA_Compiler_2")) {
				msg2.addMessage("\n\n>>>> Running Synopsys FPGA Compiler 2\n");
				command1 =
					"fc2_shell -f "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ "Script.fst | tee "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ ".log";
			} else {
				XfvhdlError err5 = new XfvhdlError(0);
	            err5.show();
	            return;
	         }

			//Muestra un mensaje de información
			msg2.show();

			// Ejecuta la herramienta externa, mostrando por pantalla
			// la salida de dicha herramienta
			try {
				String line;
	            msg2.addMessage("\n Executing...<" + command1 + ">");
	            msg2.show();
	            Process p = Runtime.getRuntime().exec(command1);
	            BufferedReader input =
	            	new BufferedReader(
	            			new InputStreamReader(p.getInputStream()));
	            while ((line = input.readLine()) != null) {
	            	// Escribe la salida por pantalla, pero ojo,
	            	// todo será escrito por la consola, por lo que
	            	// si la aplicación fue lanzada desde Xfuzzy, la
	            	// salida NO es mostrada en el log de éste, sino
	            	// en la consola
	            	System.out.println(line);
	            }
	            input.close();
			} catch (Exception err) {
				// Se ha producido un error en la ejecución
				XfvhdlError err5 = new XfvhdlError(25);
	            err5.show();
	            return;
			}

		}

		// Lanza la herramienta de implementación -si procede-
		if (XfvhdlProperties.implementation == true) {
			//No puede haber errores
			if (error.hasErrors()) {
				XfvhdlError err7 = new XfvhdlError(27);
	            err7.show();
	            return;
			}

			String command1 = new String();
			String command2 = new String();
			
			//Muestra un mensaje de información
			msg2.addMessage("\n\n>>>> Running Xilinx Xflow \n");
			msg2.show();

			//Depende de la herramienta que se haya indicado
			if (XfvhdlProperties.synthesisTool.equals((String) "XILINX_XST")) {
				command1 =
					"xflow -implement balanced.opt -wd "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ "_implement "
					+ XfvhdlProperties.outputDirectory
					+ "/"
					+ XfvhdlProperties.name_outputfiles
					+ "_synthesis/"
					+ XfvhdlProperties.name_outputfiles
					+ ".ngc";
			} else {
				command1 =
					"xflow -implement balanced.opt –wd "
					+ XfvhdlProperties.name_outputfiles
					+ "_implement "
					+ XfvhdlProperties.name_outputfiles
					+ "_synthesis/FLC.edf";
			}

			command2 =
				"xflow -config bitgen.opt -wd "
				+ XfvhdlProperties.outputDirectory
				+ XfvhdlProperties.fileSeparator
				+ XfvhdlProperties.name_outputfiles
				+ "_implement "
				+ XfvhdlProperties.outputDirectory
				+ XfvhdlProperties.fileSeparator
				+ XfvhdlProperties.name_outputfiles
				+ "_implement/"
				+ XfvhdlProperties.name_outputfiles
				+ ".ncd";

			// Ejecuta la herramienta externa, mostrando por pantalla
			// la salida de dicha herramienta         
			try {
				String line;
	            msg2.addMessage("\n Executing...<" + command1 + ">");
	            msg2.show();
	            Process p = Runtime.getRuntime().exec(command1);
	            BufferedReader input =
	            	new BufferedReader(
	            			new InputStreamReader(p.getInputStream()));
	            while ((line = input.readLine()) != null) {
	            	System.out.println(line);
	            }
	            input.close();
			} catch (Exception err) {
				XfvhdlError err5 = new XfvhdlError(25);
	            err5.show();
	            return;
			}

			try {
				String line;
	            msg2.addMessage("\n Executing...<" + command2 + ">");
	            msg2.show();
	            Process p = Runtime.getRuntime().exec(command2);
	            BufferedReader input =
	            	new BufferedReader(
	            			new InputStreamReader(p.getInputStream()));
	            while ((line = input.readLine()) != null) {
	            	System.out.println(line);
	            }
	            input.close();
			} catch (Exception err) {
				XfvhdlError err5 = new XfvhdlError(25);
	            err5.show();
	            return;
			}

		}

		// Termina la aplicación.  

	}

} // FIN DE LA CLASE Xfvhdl.
