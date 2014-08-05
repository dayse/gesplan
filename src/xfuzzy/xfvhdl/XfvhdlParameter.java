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

import java.io.File;

/**
* Clase que gestiona los parámetros que se pasan a la aplicación desde
* la línea de comandos
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlParameter {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //	  ATRIBUTOS PRIVADOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   
	/**Se almacenan los parámetros que se pasan por la línea 
	   de comandos.   */
   private String args[];

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				CONSTRUCTOR DE LA CLASE					 	   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//	
   /**
    * Constructor de XFvhdlParameter.
    * @param c Parámetros que se pasan por la línea de comandos.
    */
   public XfvhdlParameter(String c[]) {

      if (c.length == 0) {
         XfvhdlError err = new XfvhdlError(6);
         err.show();
         System.exit(-1);
      }

      args = new String[c.length];

      for (int i = 0; i < c.length; i++) {
         args[i] = new String(c[i]);
      }
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
   * Método que trata los parámetros que se pasan a la aplicación 
   * desde la línea de comandos.
   * @param args[] Array que contiene los parámetros.
   */
   public void loadParameter() {

      // Averigua el directorio desde el que se lanza la aplicación
      try {
         XfvhdlProperties.userDirectory =System.getenv("XFUZZY_WORK_DIRECTORY")!= null
			? System.getenv("XFUZZY_WORK_DIRECTORY")
					: System.getProperty("user.dir");
        	 
            //System.getProperty("user.dir", ".");
         XfvhdlProperties.fileSeparator =
            System.getProperty("file.separator", "\\");
      } catch (Exception e) {
      }

      // Determina si la ejecución es en consola o en una ventana
      if(findParameterBoolean("-g")==1){
    	  // En este primer caso le pasamos 1 parámetro .xfl,que es el que
          //se cargará en modo gráfico
          if (numParameter() == 2 && findParameterBoolean("-g") == 1){
             XfvhdlProperties.inWindow = true;
             XfvhdlProperties.ficheroXFL=XfvhdlProperties.userDirectory+XfvhdlProperties.fileSeparator+findParameter(1);
          
          }
          // Para este segundo caso hemos de pasarle ambos ficheros, el .xfl y el .xml
          //de manera que ambos se carguen en pantalla.
          else if(numParameter() == 3 && findParameterBoolean("-g") == 1){
        	  XfvhdlProperties.inWindow = true;
        	  XfvhdlProperties.ficheroXFL=XfvhdlProperties.userDirectory+XfvhdlProperties.fileSeparator+findParameter(1);
        	  XfvhdlProperties.fichero_config=XfvhdlProperties.userDirectory+XfvhdlProperties.fileSeparator+findParameter(2);
        	  XfvhdlProperties.cargarXML=true;
          }else{
        	  XfvhdlError err = new XfvhdlError(6);
              err.show();
              System.exit(-1);
          }
      }else{
    	  // En caso de ejecución por consola

          // Busca el nombre del fichero XFL
          XfvhdlProperties.ficheroXFL = XfvhdlProperties.userDirectory+XfvhdlProperties.fileSeparator+findParameter(0);
          XfvhdlProperties.fileDir = null;
          XfvhdlProperties.cargarXML=true;

          
          if(numParameter()==1){//Ejemplo:>> Xfvhdl spec.xfl
 	         XfvhdlProperties.fichero_config=XfvhdlProperties.ficheroXFL.substring(0, XfvhdlProperties.ficheroXFL.length()-4).concat(".xml");
          }else {//if(numParameter()==2){//Ejemplo:>> Xfvhdl spec.xfl config.xml
        	 if(findParameter(1).length()>4&&findParameter(1).substring(findParameter(1).length()-4, findParameter(1).length()).equals(".xml"))
        		 XfvhdlProperties.fichero_config=XfvhdlProperties.userDirectory+XfvhdlProperties.fileSeparator+findParameter(1);
        	 else
        		 XfvhdlProperties.fichero_config=XfvhdlProperties.ficheroXFL.substring(0, XfvhdlProperties.ficheroXFL.length()-4).concat(".xml");
             
          }
          File f=new File(XfvhdlProperties.fichero_config);
          if(!f.exists()){
         	 XfvhdlError err=new XfvhdlError(8, "The file "+XfvhdlProperties.fichero_config+" doesn't extist");
         	 err.show();
              System.exit(-1);
          }

          // Busca el directorio donde se encuentra la librería VHDL
          if (findParameter("-L") != null) {
             XfvhdlProperties.libraryDirectory = findParameter("-L");
          }


          // Averigua si hay que lanzar la herramienta de síntesis
          if (findParameterBoolean("-S") == 1) {
             XfvhdlProperties.synthesis = true;
          }

          // Averigua si hay que lanzar la herramienta de implementación 
          if (findParameterBoolean("-I") == 1) {
             XfvhdlProperties.implementation = true;
          }

       
      }
      
      
      
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //	MÉTO_DOS PRIVADOS DE LA CLASE							    
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
    * Método que busca si se ha pasado el parámetro c por la línea de 
    * comandos
    * @param c Parámetro que se está buscando
    * @return Devuelve el siguiente parámetro al que se busca, o null 
    * si no lo encuentra
    */
   private String findParameter(String c) {
      int l = args.length;
      int i = 0;
      boolean enc = false;

      while (i < l) {
         if (args[i].equalsIgnoreCase(c) && (i + 1) < l) {
            return (args[i + 1]);
         } else if (args[i].equalsIgnoreCase(c) && !((i + 1) < l)) {
            enc = true;
         }

         if (!((i + 1) < 1) && enc) {
            XfvhdlError err = new XfvhdlError();
            err.newWarning(7, args[i]);
         }

         i++;
      }

      return (null);
   }

   /**
    * Método que busca si se pasado el parámetro c por la línea de 
    * comandos
    * @param c Parámetro que se está buscando
    * @return Devuelve un ENTERO que es el siguiente parámetro al que 
    * se busca, o null si no lo encuentra
    */
   private int findParameterInt(String c) {
      int l = args.length;
      int i = 0;
      boolean enc = false;

      while (i < l) {
         if (args[i].equalsIgnoreCase(c) && (i + 1) < l) {
            return (Integer.parseInt(args[i + 1]));
         } else if (args[i].equalsIgnoreCase(c) && !((i + 1) < l)) {
            enc = true;
         }

         if (!((i + 1) < 1) && enc) {
            XfvhdlError err = new XfvhdlError();
            err.newWarning(7, args[i]);
         }

         i++;
      }
      return (-1);
   }

   /**
    * Método que busca si se ha pasado el parámetro c por la línea de 
    * comandos
    * @param c Parámetro que se está buscando
    * @return Devuelve 1 si encuentra el parametro y -1 si no lo 
    * encuentra
    */
   private int findParameterBoolean(String c) {
      int l = args.length;
      int i = 0;
      boolean enc = false;

      while (i < l) {
         if (args[i].equalsIgnoreCase(c)) {
            enc = true;
         }
         i++;
      }
      if (!enc)
         return (-1);
      else
         return (1);
   }

   /**
    * Método que devuelve el parámetro número p pasado por la 
    * línea de comandos
    * @param p Número del parámetro
    * @return Devuelve el parámetro número p, o null si no existe
    */
   private String findParameter(int p) {
      if (args.length < p) {
         new XfvhdlError(8);
         return (null);
      } else {
         return (args[p]);
      }
   }

   /**
    * Método que devuelve el número de parámetros pasados por la 
    * línea de comandos
    * @return Devuelve el número de parámetros
    */
   private int numParameter() {
      return args.length;
   }

   /**
    * Método que devuelve el prefijo del nombre de un fichero 
    * dada una ruta
    * @return Devuelve el prefijo del nombre del fichero
    */
   private String getPrefix(String in) {
      int c = 0;
      String out = new String();

      for (int i = 0; i < in.length(); i++) {
         if (in.charAt(i) == XfvhdlProperties.fileSeparator.charAt(0))
            c = i + 1;
      }

      out = in.substring(c);

      if (out.length() < 1) {
         out = XfvhdlProperties.OUTPUT_FILE_DEFAULT;
         XfvhdlError xerr = new XfvhdlError();
         xerr.newWarning(21);
      }

      return out;
   }

   /**
    * Método que devuelve el directorio dada una ruta
    * @return Devuelve el directorio
    */
   private String getDirectory(String in) {
      int c = 0;
      String out = new String();

      for (int i = 0; i < in.length(); i++) {
         if (in.charAt(i) == XfvhdlProperties.fileSeparator.charAt(0))
            c = i + 1;
      }

      out = in.substring(0, c);

      if (out.length() < 1) {
         out = XfvhdlProperties.OUTPUT_DIRECTORY_DEFAULT;
      }

      return out;
   }

} // Fin de la clase.
