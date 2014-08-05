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
* Clase que genera el fichero de script para la herramienta de síntesis 
* de Xilinx (Script.xst).
* 
* @author Lidia Delgado Carretero.
* 
*/
public class XfvhdlScriptXstFile {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método que crea la cadena que será escrita en el fichero Script.xst
   * @return Devuelve la cadena que será escrita en el fichero Script.xst
   */
   public String createScriptXstSource() {

      XfvhdlHeadFile head =
         new XfvhdlHeadFile(XfvhdlProperties.name_outputfiles,0);

      String code = head.getHeadScript();
      code += "\nset -tmpdir .\n"
         + "set -xsthdpdir "
         + XfvhdlProperties.outputDirectory
         + XfvhdlProperties.fileSeparator
         + XfvhdlProperties.name_outputfiles
         + "_synthesis\n"
         + "run\n"
         + "-ifmt VHDL\n"
         + "-ifn "
         + XfvhdlProperties.outputDirectory
         + XfvhdlProperties.fileSeparator
         + XfvhdlProperties.name_outputfiles
         + ".prj\n"
         + "-ent "+XfvhdlProperties.name_outputfiles+"\n"
         + "-ofmt NGC\n"
         + "-ofn "
         + XfvhdlProperties.outputDirectory
         + XfvhdlProperties.fileSeparator
         + XfvhdlProperties.name_outputfiles
         + "_synthesis\\"
         + XfvhdlProperties.name_outputfiles
         + ".ngc\n"
         + "-p "
         + XfvhdlProperties.partType
         + "\n"
         + "-keep_hierarchy Yes\n"
         + "-write_timing_constraints Yes\n";

      if (XfvhdlProperties.romExtract.equals((String) "Automatic"))
         code += "-rom_extract Yes\n";
      else if(XfvhdlProperties.romExtract.equals((String) "None"))
         code += "-rom_extract No\n";
      else if(XfvhdlProperties.romExtract.equals((String) "Block"))
    	  code += "-rom_extract Yes\n"+"-rom_style Block\n";
      else if(XfvhdlProperties.romExtract.equals((String) "Distributed"))
    	  code += "-rom_extract Yes\n"+"-rom_style Distributed\n";

      if (XfvhdlProperties.ramExtract.equals((String) "Automatic"))
    	  code += "-ram_extract Yes\n";
      else if(XfvhdlProperties.ramExtract.equals((String) "None"))
    	  code += "-ram_extract No\n";
      else if (XfvhdlProperties.ramExtract.equals((String) "Block"))
         code += "-ram_extract Yes\n"+"-ram_style Block\n";
      else if (XfvhdlProperties.ramExtract.equals((String) "Distributed"))
         code += "-ram_extract Yes\n"+"-ram_style Distributed\n";

      if (XfvhdlProperties.speedOptimization == true)
         code += "-opt_mode Speed\n";

      if (XfvhdlProperties.areaOptimization == true)
         code += "-opt_mode Area\n";

      if (XfvhdlProperties.mapEffort.equals((String) "HIGH_MAP_EFFORT"))
         code += "-opt_level 2\n";
      else
         code += "-opt_level 1\n";

      return code;
   }

} // Fin de la clase
