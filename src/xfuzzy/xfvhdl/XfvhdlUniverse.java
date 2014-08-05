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
* Clase que gestiona el universo de discurso una vez que se discretizan 
* las funciones de pertenencia de las variables de entrada.
* 
* @author Lidia Delgado Carretero.
* 
*/
public class XfvhdlUniverse {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //	  ATRIBUTOS PRIVADOS DE LA CLASE 				            
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**Máximo del universo de discurso.*/
   private double max;
   
   /**Mínimo del universo de discurso.*/
   private double min;
   
   /**Cardinalidad.*/
   private int card;
   
   /**Lista de puntos del universo de discurso.*/
   private List points;

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				CONSTRUCTORES DE LA CLASE				 	   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//	
   /**
    * Constructor por defecto de XFvhdlUniverse.
    */
   public XfvhdlUniverse() {
   }

   /**
    * Constructor con parámetros de XFvhdlUniverse.
    */
   public XfvhdlUniverse(String v, double mx, double mn, int c) {
      max = mx;
      min = mn;
      card = c;
      points = new ArrayList();
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//	
   /**
    * Método que devuelve el máximo del universo de discurso.
    * @return max Double que contiene el valor máximo del  
    * universo de discurso.
    */
   public double getMax() {
      return max;
   }

   /**
    * Método que devuelve el mínimo del universo de discurso.
    * @return min Double que contiene el valor mínimo del 
    * universo de discurso.
    */
   public double getMin() {
      return min;
   }

   /**
    * Método que devuelve la cardinalidad del universo de discurso.
    * @return card Entero que contiene la cardinalidad del 
    * universo de discurso.
    */
   public int getCard() {
      return card;
   }

	/**
	 * Método que devuelve la longitud en puntos del universo de discurso.
	 * @return points Entero que contiene el nº de puntos del universo
	 * de discurso.
	 */
   public int getLength() {
      return points.size();
   }

   /**
    * Método que permite añadir un punto al universo de discurso.
    * @param p Es el punto que deseamos añadir al universo de discurso.
    */
   public void addXUniversePoint(XfvhdlUniversePoint p) {
      points.add(p);
   }

   /**
    * Método que devuelve un punto del universo de discurso.
    * @param i Indica qué punto deseamos obtener.
    * @return Punto que ocupa la posición i-ésima en el
    * universo de discurso.
    */
   public XfvhdlUniversePoint getXUniversePoint(int i) {
      return (XfvhdlUniversePoint) points.get(i);
   }

} // Fin de la clase
