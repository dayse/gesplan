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

import java.util.*;

/**
* Clase que genera una cabecera para los distintos ficheros VHDL que se 
* generan.
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlHeadFile {

   /**Sistema operativo.*/
   private String so;
   /**Prefijo del fichero en el que se genera la cabecera.*/
   private String name;
   /**N� de entradas.*/
   private int inputs;
   /**M�quina virtual.*/
   private String vmachine;
   /**Fecha en la que se genera el fichero.*/
   private String date;
   /**Usuario.*/
   private String user;
   /**Separador del sistema operativo.*/
   private String separator;
   
   private boolean init;

   /**Constructor de la clase.				       		    
   */
   public XfvhdlHeadFile(String name, int inputs) {
	   this.name=name;
	   this.inputs=inputs;
	   user="";
	   so="";
	   vmachine="";
	   separator="";
	   Date now = new Date();
	   date = now.toString();
	   if(init!=true){
	    	try {
	            so = System.getProperty("os.name", "not specified");
	            so += " ver. ";
	            so += System.getProperty("os.version", "not specified");
	            vmachine = System.getProperty("java.version", "not specified");
	            user = System.getProperty("user.name", "not specified");
	            separator = System.getProperty("file.separator", "\\");
	         } catch (Exception e) {
	         }
	         init = true;
	    }
			   
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  M�TO_DOS P�BLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
   * M�todo que crea la cabecera para los ficheros VHDL de test bench.
   * @return Devuelve la cabecera que ser� escrita en 
   * el fichero VHDL de test bench.
   */
   public String getHeadTB() {

      String head = new String();
      
      
      head="--\n" +
		"-- Automatically generated by Xfvhdl\n" +
		"--\n" +
		"-- output:: " + name + "_tb.vhd\n" +
		"-- source:: " + XfvhdlProperties.ficheroXFL + "\n" +
		"-- inputs:: " + inputs +
		"-- for " + user + ", " + date + "\n" +
		"-- in " + so + " with VM "+ vmachine + "\n" +
		"--\n" +
		"\n"
		;

      return head;
   }
   /**
    * M�todo que crea la cabecera para los ficheros script.
    * @return Devuelve la cabecera que ser� escrita en 
    * el fichero script.
    */
   public String getHeadScript() {

	      String head = new String();
	      String name_file="";
	      if(inputs==0)
	    	  name_file=name+"Script.xst";
	      else
	    	  name_file=name+".fst";
	      
	      head="#\n" +
			"# Automatically generated by Xfvhdl\n" +
			"#\n" +
			"# output:: "+name_file+"\n" +
			"# source:: "+XfvhdlProperties.ficheroXFL+"\n" +
			"# for "+user+", "+date+"\n" +
			"# in "+so+ " with VM "+ vmachine+ "\n" +
			"#\n" +
			"\n"
			;

	      return head;
	   }


} // Fin de la clase
