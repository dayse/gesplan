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

/**
* Clase que genera el fichero de script con extensión .fst.
* 
* @author Lidia Delgado Carretero.
* 
*/
public class XfvhdlScriptFile {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método que crea la cadena que será escrita en el fichero Script.fst
   * @return Devuelve la cadena que será escrita en el fichero Script.fst
   */
   public String createScriptSource() {

      XfvhdlHeadFile head =
         new XfvhdlHeadFile(XfvhdlProperties.name_outputfiles,1);

      String code = head.getHeadScript();

      // El contenido de este fichero no se genera, pero si que
      // se genera lo que es el fichero con su cabecera, por lo
      // que para generar este fichero completo, sólo hay que escribir
      // en esta clase lo que deseemos que el fichero contenga.

      code += "\n\n *** NOT COMPLETED ***";

      return code;
   }

} // Fin de la clase
