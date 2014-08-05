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

/**
* Clase que contiene todas las variables que se usan en la aplicación
* 
* @author Jesús Izquierdo Tena
*/
public final class XfsgProperties {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //		ATRIBUTOS PÚBLICOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   //Valor por defecto
   final static public int DONT_CARE = 32767;

   // Valores por defecto de los parámetros obtenidos de la línea de 
   // comandos
   final static public String PREFIX_OUTPUT_FILE_DEFAULT = new String("FLC");
   final static public int N_DEFAULT = 5;
   final static public int grad_DEFAULT = 6;
   final static public int P_DEFAULT = 6;
   final static public int K_DEFAULT = 6;
   final static public int Pb_DEFAULT = 5;
   final static public boolean CALC_ARITHMETIC_DEFAULT = false;
   //final static public boolean CALCULATE_WEIGHTS_DEFAULT = true;
   final static public boolean INWINDOW_DEFAULT = false;
   
   // Variables con los parámetros obtenidos de la línea de comandos 
   public static int N = N_DEFAULT;
   public static int grad = grad_DEFAULT;
   public static int P = P_DEFAULT;
   public static int K = K_DEFAULT;
   public static int Pb = Pb_DEFAULT;
   public static boolean calcArithmetic = CALC_ARITHMETIC_DEFAULT;
   public static String outputDirectory = new String();
   public static String userDirectory = new String(System.getProperty("user.dir", "."));
   public static String fileSeparator = new String(System.getProperty("file.separator", "\\"));
   public static String name_outputfiles;

   //public static boolean calculateWeights = CALCULATE_WEIGHTS_DEFAULT;
   public static boolean inWindow = INWINDOW_DEFAULT;

   public static String ficheroXFL = new String();
   public static String fichero_config=new String();
   public static boolean activar_boton_GMF=false;
   public static boolean use_components_simplified=false;
   
} // Fin de la clase.
