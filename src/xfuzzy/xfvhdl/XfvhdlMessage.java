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

import xfuzzy.*;

/**
* Clase que gestiona los mensajes que se producen en el proceso de 
* generación y síntesis.
* 
* @author Lidia Delgado Carretero.
*
*/
public final class XfvhdlMessage {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //		  ATRIBUTOS PRIVADOS DE LA CLASE 				       
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**Mensajes que se producen. Actúa como un buuffer.*/
   private static String messages = new String();
   
   /**Contador del buffer de mensajes.*/
   private int mens = 0;
   
   /**Apunta al objeto Xfuzzy cuando la herramienta se lanza desde éste 
    * y es usado en esta clase para poder mostrar los mensajes en 
    * el lugar de la interfaz destinada a ello.*/
   private Xfuzzy xfuzzy = null;

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				CONSTRUCTORES DE LA CLASE					   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
   * Constructor de la clase XfvhdlMessage.
   */
   public XfvhdlMessage(Xfuzzy xfuzzy) {
      this.xfuzzy = xfuzzy;
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				  MÉTO_DOS PÚBLICOS DE LA CLASE				   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
   * Método público para añadir un mensaje.
   * @param m Mensaje que se ha producido.
   */
   public void addMessage(String m) {
      messages += m;
      mens++;
   }

   /**
   * Método que establece un xfuzzy para poder mostrar los mensajes en 
   * el log.
   */
   public void setXfuzzy(Xfuzzy xfuzzy) {
      this.xfuzzy = xfuzzy;
   }

   /**
   * Método que indica si existe algún mensaje.
   */
   public boolean hasMessages() {
      boolean e = false;
      if (mens > 0)
         e = true;

      return e;
   }

   /**
   * Método que muestra por pantalla todos los mensajes acumulados y 
   * vacía el contenedor de mensajes.
   */
   public void show() {
      if (xfuzzy == null) {
    	  messages += "\n";
          System.out.print(messages);
      }
      else
         xfuzzy.log(messages);

      messages = "";
      mens = 0;
   }

} // Fin de la clase.
