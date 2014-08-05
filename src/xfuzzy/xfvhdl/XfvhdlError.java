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
* Clase que gestiona los errores que se producen el el proceso de 
* síntesis
* 
* @author Lidia Delgado Carretero
* 
*/
public final class XfvhdlError {

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//		  ATRIBUTOS PRIVADOS DE LA CLASE 				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**Mensaje de error accesible en toda la herramienta.*/
	private static String messages = new String();
   /**Mensaje interno de error.*/
   private String mensaje;
   /**Código de error.*/
   private int cod;
   /**Nº de errores.*/
   private static int errors = 0;
   /**Nº de warnings.*/
   private static int warnings = 0;
   /**Apunta a la zona de mensajes de la interfaz gráfica de Xfuzzy.*/
   private XfvhdlMessage msg = new XfvhdlMessage(Xfvhdl.xfuzzy);

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				CONSTRUCTORES DE LA CLASE					   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Constructor de la clase XfvhdlError.
   * @param c Código del error producido.
   */
   public XfvhdlError(int c) {
      error(c);
      messages += "\nERROR " + cod + ": " + mensaje;
      errors++;
   }

   /**
   * Constructor de la clase XfvhdlError.
   * @param c Código del error producido.
   * @param s Cadena a mostrar después del error.
   */
   public XfvhdlError(int c, String s) {
      error(c);
      messages += "\nERROR " + cod + ": " + mensaje + " " + s;
      errors++;
   }

   /**
   * Constructor por defecto de la clase XfvhdlError.
   */
   public XfvhdlError() {
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				  MÉTO_DOS PÚBLICOS DE LA CLASE				   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método para generar un warning.
   * @param c Código del warning producido.
   	*/
   public void newWarning(int c) {
      error(c);
      messages += "\nWARNING " + cod + ": " + mensaje;
      warnings++;
   }

   /**
   * @return Booleano que indica si existe algun error.
   */
   public boolean hasErrors() {
      boolean e = false;
      if (errors > 0)
         e = true;

      return e;
   }
   
   /**
	 * @return Booleano que indica si existe algun warning.
	 */
	public boolean hasWarnings() {
		boolean e = false;
		if (warnings > 0)
			e = true;

		return e;
	}

   /**
   * Método que inicializa los errores y los warnings.
   */
   public void resetAll() {
      messages = "";
      errors = 0;
      warnings = 0;
   }

   /**
   	* Método para generar un warning.
   	* @param c Código del warning producido.
   	* @param s Cadena a mostrar después del warning.
   	*/
   public void newWarning(int c, String s) {
      error(c);
      messages += "\nWARNING " + cod + ": " + mensaje + " " + s;
      warnings++;
   }

   /**
   * Método que muestra por pantalla o en el log todos los errores 
   * acumulados.
   */
   public void show() {
      String cad1 =
         new String(
            "\n\n------------------------------------"
               + "-------------\n\n    Finished with 0 errors "
               + "and 0 warnings.\n");
      String cad2 =
         new String(
            "\n\n--------------------------------------------"
               + "-----\n\n    Finished with "
               + errors
               + " errors and "
               + warnings
               + " warnings.\n"
               + messages
               + "\n");
      if (errors == 0 && warnings == 0)
         msg.addMessage(cad1);
      else
         msg.addMessage(cad2);

      msg.show();
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			MÉTO_DOS PRIVADOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**@return messages.*/
   public String getMessages() {
		return messages;
	}
   
   /**
   * Método privado que genera la cadena de identificación del error 
   * o del warning.
   * @param c Código del error o warning producido. 
   */
   private void error(int c) {
	   if (c == 2) {
         cod = c;
         mensaje = new String("Can't create output directory");
      } else if (c == 4) {
         cod = c;
         mensaje =
            new String(
               "The maximum overlapping degree must be two "
                  + "in variable");
      } else if (c == 5) {
         cod = c;
         mensaje =
            new String("There isn't any membership function in variable");
      } else if (c == 6) {
         cod = c;
         mensaje = new String("Bad call, usage:\n\n");
         mensaje += "For grafical mode use: \n"
            + "\t\txfvhdl -g <XFL3-file> [<XML-file>]\n"
            + "\nFor console mode use:\n"
            + "\t\txfvhdl <XFL3-file> [<XML-file>] [-L <directory>] [-S] [-I]\n\n"
            + "\t\t<XFL-file> : Source XFL 3 file.\n\n"
            + "\t\t<XML-file> : XML configuration file.\n\n"
            + "\t\t-L <directory> : Defines <directory> as the components library directory.\n"
            + "\t\t\t\tBy default, .\\" + XfvhdlProperties.LIBRARY_DIRECTORY_DEFAULT + " is used.\n"
            + "\t\t-S : Execute the synthesis tool once the VHDL code has been generated.\n"
            + "\t\t-I : Execute the synthesis tool once the VHDL code has been generated\n"
            + "\t\t\t\tand execute the Xilinx implementation tools.\n"
            ;
      } else if (c == 7) {
         cod = c;
         mensaje =
            new String(
               "Default value will be used because isn't "
                  + "complete the parameter");
      } else if (c == 8) {
         cod = c;
         mensaje = new String("Bad parameters");
      } else if (c == 9) {
          cod = c;
          mensaje = new String("It is not allowed rulebases with more " 
        		  		 + "than two inputs and Takagi-Sugeno as defuzzification method: ");
        
      } else if (c == 10) {
         cod = c;
         mensaje = new String("Error in rule: ");
      } else if (c == 13) {
         cod = c;
         mensaje = new String("Without consequent in rule");
      } else if (c == 14) {
          cod = c;
          mensaje = new String("It is not allowed rulebases with more than one output: ");
      
      } else if (c == 21) {
         cod = c;
         mensaje =
            new String(
               "No prefix file valid. By default "
                  + XfvhdlProperties.OUTPUT_FILE_DEFAULT
                  + ".");
      
      } else if (c == 24) {
         cod = c;
         mensaje =
            new String(
               "There are errors, so can´t execute any synthesis "
                  + "tool");
      } else if (c == 25) {
         cod = c;
         mensaje = new String("Exception in external tool");
      } else if (c == 27) {
         cod = c;
         mensaje =
            new String(
               "There are errors, so can´t execute any "
                  + "implementation tool");
      } else if (c == 28) {
         cod = c;
         mensaje =
            new String(
               "AND operation not valid. Will be used Minimum "
                  + "by default");
      } else if (c == 31) {
         cod = c;
         mensaje =
            new String("Families of Membership Functions not allowed");
      } else if (c==32) {
    	  cod = c;
    	  mensaje =
              new String("The xml file is not correctly defined");
      } else if (c == 33) {
    	  cod = c;
    	  mensaje = new String("Invalid name system: ");
      } else if (c == 34) {
    	  cod = c;
    	  mensaje = new String("Invalid name rulebase: ");
      } else if (c == 35) {
    	  cod = c;
    	  mensaje = new String("Invalid name crisp: ");
      } else if (c == 36) {
			cod = c;
			mensaje = new String("Exception in defuzzification method: ");
      } else if (c == 37) {
			cod = c;
			mensaje = new String(
					"Cannot calculate the weight of the rules. ");
      } else if (c == 39) {
			cod = c;
			mensaje = new String("The bitsize for membership function slope is too short, you must resize it or choose memory for the MFCs in ");
      }else {
         cod = 0;
         mensaje = new String("UNDEFINED ERROR");
      }
   }

} // Fin de la clase.
