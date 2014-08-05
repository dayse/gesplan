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
* Clase que gestiona los ficheros que se van creando para obtener el 
* fichero de projecto .prj que usa la herramienta Xilinx XST
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlPrjFile {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //		ATRIBUTOS PRIVADOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**Lista privada en la que se almacenan los nombres de los 
    * ficheros que aparecerán en el fichero .prj*/
   private List lFiles;
   
   /**Cadena que se añade al principio del fichero.*/
   private String strbase;
   
   /**Cadena que se añade al final del fichero.*/
   private String strfinal;

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //		MÉTO_DOS PÚBLICOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /** Constructor de la clase. */
   XfvhdlPrjFile() {
      lFiles = new ArrayList();	  
               	  
      strbase = "vhdl XfuzzyLib \""
    	  	  + XfvhdlProperties.libraryDirectory
    	  	  + XfvhdlProperties.fileSeparator;
      
      strfinal=".vhd\"";;
            
      lFiles.add(strbase+"Entities"+strfinal);
   }
   
   /** Método público que añade un fichero al projecto.
    * @param file Nombre del fichero que se desea añadir.
    */
   public void addFile(String file) {
      lFiles.add(file);
   }
   
   /**Método que añade los bloques de librería necesarios en el fichero .prj.*/
   public void addLibBlocks(ArrayList<XfvhdlFLC> listFlc){
	   XfvhdlFLC flcAux;
	   //for(int i=0;i<listCrisp.size();i++){
		//   lFiles.add(strbase+listCrisp.get(i).getname()+strfinal);
	   //}
	   //En cada flc hemos de recorrer su atributo listaBloquesDeBiblioteca
	   for (int i=0;i<XfvhdlProperties.listaBloquesDeBiblioteca.size();i++){
		   
			   lFiles.add(strbase+XfvhdlProperties.listaBloquesDeBiblioteca.get(i)+strfinal);
	   }
	   for(int i=0;i<listFlc.size();i++){
		   lFiles.add("vhdl work \""+listFlc.get(i).getname()+strfinal);
	   }
   }

   /** Método público que genera una cadena con el contenido del 
    * archivo .prj
    * @return Cadena con el contenido del archivo .prj
    */
   public String getPrj() {
      String code = new String();

      for (int i = 0; i < lFiles.size(); i++) {
         code += (String) lFiles.get(i);
         code += "\n";
      }

      return code;
   }

} // Fin de la clase.
