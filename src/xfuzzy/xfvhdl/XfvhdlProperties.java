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

package xfuzzy.xfvhdl;

import java.util.ArrayList;

/**
* Clase que contiene todas las variables que se usan en la aplicaci�n
* 
* @author Lidia Delgado Carretero
*
*/
public final class XfvhdlProperties {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //		ATRIBUTOS P�BLICOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   //Valor por defecto
   //final static public int DONT_CARE = 32767;

   // Valores por defecto de los parametros obtenidos de la linea de 
   // comandos
   final static public String OUTPUT_FILE_DEFAULT = new String("FLC");
   final static public String OUTPUT_DIRECTORY_DEFAULT = new String("FLC");
   final static public String LIBRARY_DIRECTORY_DEFAULT =
	      new String("XfuzzyLib_src");
   final static public boolean SYNTHESIS_DEFAULT = false;
   final static public boolean IMPLEMENTATION_DEFAULT = false;
   final static public String SYNTHESIS_TOOL_DEFAULT =
      new String("XILINX_XST");
   final static public String MAP_EFFORT_DEFAULT =
      new String("LOW_MAP_EFFORT");
   final static public boolean SYNOPSYS_REPORT_DEFAULT = false;
   final static public boolean AREA_OPTIMIZATION_DEFAULT = false;
   final static public boolean SPEED_OPTIMIZATION_DEFAULT = false;
   final static public String PART_TYPE_DEFAULT =
   // new String("xc3s1000-ft256-4");
	  new String("xc3sd1800a-fg676-4");   
   final static public String TARGET_DEFAULT = new String("Spartan3");
   final static public boolean SIMPLIFIED_COMPONENTS_DEFAULT = true;
   final static public boolean COMPLEMENTARY_FILES_DEFAULT = false;
   final static public boolean INWINDOW_DEFAULT = false;
   // Datos para la generaci�n del reloj en el testbench
   final static public int PERIOD = 50; //tiempo en ns
   final static public int EDGE = 25; //tiempo en ns
   
 

   // Valores por defecto para el conjunto de operadores
   final static public String OPERATIONAND_DEFAULT = new String("min");
   final static public String DEFUZZIFICATION_TYPE_DEFAULT =
      new String("FuzzyMean");
   final static public boolean SIMPLIFIED_DIVISION_DEFAULT = false;
   
   

   // Variables con los parametros obtenidos de la linea de comandos
   
   /**Nos permite saber si se ha de cargar una configuraci�n en concreto 
    * cuando lanzamos la aplicaci�n desde l�nea de comando.*/
   public static boolean cargarXML=false;
   
   /**A trav�s de este atributo calculamos el pipe m�ximo de todos los m�dulos.*/
   public static int pipemax=0;
   
   /**A trav�s de este atributo calculamos el m�ximo n� de bits para representar 
    * el pipe, de todos los m�dulos.*/
   public static int bitsmax=0;

   /**Indica si el usuario quiere realizar la s�ntesis.*/
   public static boolean synthesis = SYNTHESIS_DEFAULT;
   
   /**Indica si el usuario quiere realizar la implementaci�n.*/
   public static boolean implementation = IMPLEMENTATION_DEFAULT;
   
   /**Nombre de la herramienta de s�ntesis elegida.*/
   public static String synthesisTool = new String(SYNTHESIS_TOOL_DEFAULT);
   
   /**Esfuerzo.*/
   public static String mapEffort = new String(MAP_EFFORT_DEFAULT);
   
   /**Indica si el usuario quere realizar la optimizaci�n del �rea.*/
   public static boolean areaOptimization = AREA_OPTIMIZATION_DEFAULT;
   
   /**Indica si el usuario quere realizar la optimizaci�n de la velocidad.*/
   public static boolean speedOptimization = SPEED_OPTIMIZATION_DEFAULT;
   
   /**Dispositivo.*/
   public static String partType = new String(	
		   					System.getenv("XFUZZY_PART_TYPE")!= null
		   					? System.getenv("XFUZZY_PART_TYPE") 
		   					: PART_TYPE_DEFAULT);
   /**Familia.*/
   public static String target = new String(
							System.getenv("XFUZZY_TARGET")!= null
							? System.getenv("XFUZZY_TARGET") 
							: TARGET_DEFAULT);
   
   /**Directorio de salida */
   public static String outputDirectory = new String(OUTPUT_DIRECTORY_DEFAULT);
   
   /**Directorio de usuario definido en el sistema.*/
   public static String userDirectory = new String(System.getProperty("user.dir", "."));
   
   /**Separador que depende del sistema operativo en el que se ejecute la aplicaci�n.*/
   public static String fileSeparator = new String(System.getProperty("file.separator", "\\"));  
   
   /**Librer�a.*/
   public static String libraryDirectory = new String(
							System.getenv("XFUZZY_VHDL_LIBRARY")!= null
							? System.getenv("XFUZZY_VHDL_LIBRARY") 
		                    : userDirectory + fileSeparator + LIBRARY_DIRECTORY_DEFAULT);
   
   /**Prefijo de los ficheros de salida.*/
   public static String name_outputfiles;
   
   /**Indica la elecci�n del usuario de si desea usar componentes simplificados 
    * en el caso de que sea posible.*/
   public static boolean simplifiedComponents = SIMPLIFIED_COMPONENTS_DEFAULT;
   
   /**Indica la elecci�n del usuario de si desea generar ficheros complementarios.*/
   public static boolean complementaryFiles = COMPLEMENTARY_FILES_DEFAULT;
   
   /**Indica si la generaci�n de c�digo se ha lanzado desde la ventana gr�fica o desde 
    * l�nea de comandos.*/
   public static boolean inWindow = INWINDOW_DEFAULT;
   
   /**Tipo de ROM que el usuario elige (ROM to be used).*/
   public static String romExtract = new String();
   
   /**Tipo de ROM que el usuario elige (RAM to be used).*/
   public static String ramExtract = new String();
   
   /**Lista que gestiona los bloques de biblioteca de XfuzzyLib.*/
   public static ArrayList<String> listaBloquesDeBiblioteca=new ArrayList<String>();

   // Variables calculadas a partir del fichero XFL
   /**Nombre del fichero de configuraci�n XFL.*/
   public static String ficheroXFL = new String();
   
   /**Directorio conde se guardan los ficheros.*/
   public static String fileDir;
   
   /**N� de filas de la memoria de reglas activa.*/
   public static int dir_regl;

   
   // Variables con los parametros obtenidos del fichero XFL
   /**Operaci�n para AND (min o prod).*/
   public static String operationAnd = OPERATIONAND_DEFAULT;
   
   /**Tipo de defuzzificador.*/
   public static String defuzzification_type =
      DEFUZZIFICATION_TYPE_DEFAULT;
   
   /**Indica si la divisi�n es simplificada.*/
   public static boolean simplified_division =
	      SIMPLIFIED_DIVISION_DEFAULT;
   
   /**Atributo que nos permite trabajar con el fichero de configuraci�n.*/
   public static String fichero_config=new String();
   
   /**Atributo que nos permite trabajar con los iconos del �rbol de 
    * representaci�n del sistema jer�rquico.*/
   public static boolean activar_boton_GMF=false;
   
   
} // Fin de la clase.
