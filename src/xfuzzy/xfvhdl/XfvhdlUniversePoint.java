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
* Clase que representa un punto discreto en el universo de discurso 
* discretizado.
* 
* @author Lidia Delgado Carretero.
* 
*/
public class XfvhdlUniversePoint {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //		ATRIBUTOS PRIVADOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**Etiqueta.*/
   private int etiq;
   
   /**Primer valor.*/
   private double val1;
   
   /**Segundo valor.*/
   private double val2;

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				  CONSTRUCTOR DE LA CLASE				 	   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//	
   /**
    * Constructor de XFvhdlUniversePoint.
    */
   public XfvhdlUniversePoint(int e1, double v1, double v2) {
      etiq = e1;
      val1 = v1;
      val2 = v2;
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
    * Método que devuelve la etiqueta.
    * @return Devuelve la etiqueta del punto del universo de discurso.
    */	
   public int getEtiq() {
      return etiq;
   }

   /**
    * Método que devuelve el primer valor del punto.
    * @return Primer valor del punto.
    */
   public double getVal1() {
      return val1;
   }

   /**
    * Método que devuelve el segundo valor del punto.
    * @return Segundo valor del punto.
    */
   public double getVal2() {
      return val2;
   }

   /**
    * Método para establecer la etiqueta del punto.
    * @param e1 Nuevo valor de la etiqueta.
    */
   public void setEtiq(int e1) {
      etiq = e1;
   }

   /**
    * Método para establecer el primer valor del punto.
    * @param v1 Nuevo valor1 del punto.
    */
   public void setVal1(double v1) {
      val1 = v1;
   }

   /**
    * Método para establecer el segundo valor del punto.
    * @param v2 Nuevo valor2 del punto.
    */
   public void setVal2(double v2) {
      val2 = v2;
   }

} // Fin de la clase
