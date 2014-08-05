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

/**
* Clase que genera los ficheros complementarios de las memorias de 
* antecedentes
* 
* @author Lidia Delgado Carretero
*
*/
public class XfvhdlAntecedentMemComplementaryFiles {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				     
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método que genera los ficheros complementarios de la memoria de 
   * antecedentes
   * @param spec Especificación XFL
   * @param flc	 Módulo de inferencia
   */
   public void createCF(Specification spec, XfvhdlFLC flc) {

      int actual = 0;
      Variable[] var = spec.getSystemModule().getInputs();
      int total = var.length;
      
      new XfvhdlError();

      // Para todas las variables (******incluidas las INNER*********)
      for(int i=1;i<=flc.getmfcs().size();i++){
         // Discretiza el universo de discurso para la variable actual
    	 XfvhdlUniverse u = new XfvhdlUniverse();
         XfvhdlDiscretizeUniverse disc = new XfvhdlDiscretizeUniverse();
         u = disc.discretizeUniverse(var[i-1], flc,i-1);

         // Genera un fichero con el contenido de la memoria de 
         // antecedentes para cada variable
         String valor = new String();

         for (int t = 0; t < u.getLength(); t++) {
            valor += "\n"
               + t
               + "   "
               + u.getXUniversePoint(t).getVal1()
               + "   "
               + u.getXUniversePoint(t).getVal2();
         }

         new XfvhdlCreateFile(
            flc.getname()
               + "valor_"
               + var[actual].getName()
               + ".dat",
            valor);

         // Genera un fichero para GnuPlot que representa el contenido 
         // de la memoria de antecedentes para cada variable
         new XfvhdlCreateFile(
            flc.getname()
               + "var_"
               + var[actual].getName()
               + ".plt",
            createGnuPlotFile(var[actual].getName(),flc.getname()));

         // Genera un fichero con el contenido de la memoria de 
         // antecedentes para cada variable (en binario)
         XfvhdlBinaryDecimal converter = new XfvhdlBinaryDecimal();
         String val1 = new String();
         String val2 = new String();
         String code = new String();

         for (int j = 0; j < u.getLength(); j++) {
            val1 =
               converter.toBinaryInRange(
                  u.getXUniversePoint(j).getVal1(),
                  0.0,
                  1.0,
                  flc.getgrad());
            val2 =
               converter.toBinaryInRange(
                  u.getXUniversePoint(j).getVal2(),
                  0.0,
                  1.0,
                  flc.getgrad());

            code += ""
            	+ converter.toBinary(
     	               (u.getXUniversePoint(j).getEtiq()) - 1,
    	               (int) Math.ceil(
    	   		            Math.log(Double.valueOf(flc.getmfcs().get(i-1).getn_fp()))
    	   		               / Math.log((double) 2)))
               + " "
               + val1
               + " "
               + val2
               + "\n";
         }

         new XfvhdlCreateFile(
            flc.getname()
               + "valor_"
               + var[actual].getName()
               + ".dat.bin",
            code);

         actual++;
      }
      
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PRIVADOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método que genera el fichero de comandos para GNUPlot
   * @return Devuelve la cadena que será escrita en el fichero .plt
   */
   private String createGnuPlotFile(String v, String flcname) {
      String code =
         "set terminal windows color \"Arial\" 10\n"
            + "set output\n"
            + "set noclip points\n"
            + "set clip one\n"
            + "set noclip two\n"
            + "set border\n"
            + "set boxwidth\n"
            + "set dummy x,y\n"
            + "set format x \"%g\"\n"
            + "set format y \"%g\"\n"
            + "set format z \"%g\"\n"
            + "set nogrid\n"
            + "set key\n"
            + "set nolabel\n"
            + "set noarrow\n"
            + "set nologscale\n"
            + "set offsets 0, 0, 0, 0\n"
            + "set nopolar\n"
            + "set angles radians\n"
            + "set noparametric\n"
            + "set view 60, 30, 1, 1\n"
            + "set samples 100, 100\n"
            + "set isosamples 10, 10\n"
            + "set surface\n"
            + "set nocontour\n"
            + "set clabel\n"
            + "set nohidden3d\n"
            + "set cntrparam order 4\n"
            + "set cntrparam linear\n"
            + "set cntrparam levels auto 5\n"
            + "set cntrparam points 5\n"
            + "set size 1,1\n"
            + "set data style points\n"
            + "set function style lines\n"
            + "set xzeroaxis\n"
            + "set yzeroaxis\n"
            + "set tics in\n"
            + "set ticslevel 0.5\n"
            + "set xtics\n"
            + "set ytics\n"
            + "set ztics\n"
            + "set title \"\" 0,0\n"
            + "set notime\n"
            + "set rrange [0 : 10]\n"
            + "set trange [-5 : 5]\n"
            + "set urange [-5 : 5]\n"
            + "set vrange [-5 : 5]\n"
            + "set xlabel \"\" 0,0\n"
            + "set xrange [0 : 31]\n"
            + "set ylabel \"\" 0,0\n"
            + "set yrange [0 : 1]\n"
            + "set zlabel \"\" 0,0\n"
            + "set zrange [-10 : 10]\n"
            + "set autoscale r\n"
            + "set autoscale t\n"
            + "set autoscale xy\n"
            + "set autoscale z\n"
            + "set zero 1e-08\n"
            + "plot '"
            + flcname
            + "valor_"
            + v
            + ".dat' title 'grado n','"
            + flcname
            + "valor_"
            + v
            + ".dat' using 1:3 title 'grado n+1'";

      return code;
   }

} // Fin de la clase
