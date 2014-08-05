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

import xfuzzy.lang.*;

import java.io.*;
import java.util.ArrayList;

/**
* Clase que genera el fichero complementario con el contenido de la memoria 
* de reglas.
* @author Lidia Delgado Carretero.
* 
*/
public class XfvhdlRulesMemComplementaryFile {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método que crea el fichero complementario con el contenido de la 
   * memoria de reglas
   */
   public void createCF(Specification spec, 
		   String nombre_i, ArrayList<Integer> bits_var, String[][] TakSugBinario, boolean calculateWeights, int No, int W,XfvhdlFLC flc)
   			throws IOException {

	   String valor="";
	   int regla_i=-1;
	   int l=0;
	   Rulebase[] base=spec.getRulebases();
	   for (int i=0;i<base.length;i++){
		   if(base[i].getName().equalsIgnoreCase(nombre_i))
			   regla_i=i;
	   }
	   
	   XfvhdlRulesMemData contenido =
         new XfvhdlRulesMemData(
        		 XfvhdlProperties.dir_regl,
				   No,
				   W,
				   spec,
				   nombre_i,
				   bits_var,calculateWeights);

	   String defuzificador=base[regla_i].getOperatorset().defuz.getFunctionName();
      // Genera un fichero con el contenido de la memoria de reglas

      String mem = new String();

      for (int i = 0; i < contenido.getLength(); i++) {
    	  valor="";
		   if (defuzificador=="TakagiSugeno"&&contenido.isActive(i)){
			   for(int m=0;m<3;m++){
				   valor+=TakSugBinario[l][m]+" ";
			   }
			   l++;
		   }else
			   valor=contenido.getValue(i);
		   
     
		   // Dependiendo del modo de defuzzificación se escribirá el 
		   // peso o no
		   if (contenido.isActive(i)
				   && flc.calculateWeights == false)
			   mem += " "
				   + contenido.getLine(i)
				   + " "
				   + valor
				   + "\n";
		   else if (
				   contenido.isActive(i)
				   && flc.calculateWeights == true)
			   mem += " "
				   + contenido.getLine(i)
				   + " "
				   + valor
				   + " "
				   + contenido.getWeight(i)
				   + "\n";
         
      }

      new XfvhdlCreateFile(
         flc.getname() + "rulesmem.dat",
         mem);
   }

} // Fin de la clase
