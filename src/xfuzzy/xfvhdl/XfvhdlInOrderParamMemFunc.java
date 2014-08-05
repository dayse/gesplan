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

import java.util.*;

/**
* Clase que gestiona la lista ordenada de funciones de pertenencia.
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlInOrderParamMemFunc {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			ATRIBUTOS PRIVADOS DE LA CLASE 				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**Lista de funciones de pertenencia ordenadas.*/
   private List mf_sorted;
   
   /**Lista de funciones de pertenencia sin falimias.*/
   private ParamMemFunc[] tmp;
   
   /**Lista de funciones de pertenencia con falimias.*/
   private FamiliarMemFunc[] tmpFamiliar=new FamiliarMemFunc[0];

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				  CONSTRUCTOR DE LA CLASE					   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
    * Constructor de XFvhdlInOrderParamMemFunc.
    */
   public XfvhdlInOrderParamMemFunc(Variable v) {
      this.mf_sorted = new ArrayList();
      if (v.getType().getAllMembershipFunctions().length!=v.getType().getParamMembershipFunctions().length)
    	  this.tmpFamiliar=v.getType().getFamiliarMembershipFunctions();
      this.tmp = v.getType().getParamMembershipFunctions();
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PUBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   /**
   * Método que devuelve la lista de funciones de pertenencia.
   * @return Lista que contiene las funciones de pertenencia.
   */
   public List getInOrderParamMemFunc() {
      return mf_sorted;
   }
   public FamiliarMemFunc[] getFamiliarMemFunc(){
	   return tmpFamiliar;
   }

   /**
   * Método que devuelve el número de funciones de pertenencia.
   * @return Número de funciones de pertenencia.
   */
   public int getSize() {
      return mf_sorted.size();
   }

   /**
   * Método que devuelve la función de pertenencia indexada por i.
   * @return Función de pertenencia i.
   */
   public ParamMemFunc getParamMemFunc(int i) {
      return (ParamMemFunc) mf_sorted.get(i);
   }

   /**
   * Método que devuelve la posición en la lista de una función de
   * pertenencia dada.
   * @param mf Función de pertenencia de la que queremos saber su posición.
   * @return Índice de la funcion de pertenencia dentro de la lista.
   */
   public int getIndex(ParamMemFunc mf) {
      int index = -1;
      boolean enc = false;
      ParamMemFunc aux;

      for (int i = 0; i < mf_sorted.size() && !enc; i++) {
         aux = (ParamMemFunc) mf_sorted.get(i);
         if (aux.equals(mf.getLabel())) {
            index = i;
            enc = true;
         }
      }

      return index;
   }

   /**
   * 
   * Método que ordena la lista de funciones de pertenencia.
   */
   public List sort() {
	      boolean enc = false;
	      if (tmp.length>0){
		      for (int i = 0; i < tmp.length; i++) {
		         if (mf_sorted.size() == 0) {
		            mf_sorted.add(tmp[i]);
		            enc = true;
		         }
	
		         for (int j = 1; j <= mf_sorted.size() && !enc; j++) {
		            double a, b;
		            a =
		               opMemFuncParameter(
		                  (ParamMemFunc) mf_sorted.get(j - 1),
		                  "min");
		            b = opMemFuncParameter(tmp[i], "min");
		            if (a > b) {
		               mf_sorted.add(j - 1, tmp[i]);
		               enc = true;
		            }
		         }
	
		         if (!enc) {
		            mf_sorted.add(tmp[i]);
		         }
	
		         enc = false;
		      }
	      }else{
	    	  //for (int i=0;i<tmpFamiliar.length;i++){
	    	//	  mf_sorted.add(tmpFamiliar[i]);
	    	  //}
	      }
	    	  
	      return mf_sorted;
	      
   }

   /**
   * Método que devuelve el valor del menor parámetro de una función 
   * de pertenencia si se le pasa "min" o el máximo si se le pasa "max".
   * @param p Función de pertenencia de la que queremos saber su valor 
   * máximo o mínimo.
   * @param op Cadena "max" si buscamos el valor máximo o "min" si 
   * buscamos el mínimo.
   * @return Valor buscado.
   */
   public double opMemFuncParameter(ParamMemFunc p, String op) {
      double param[] = p.get();
      double res;

      res = param[0];

      if (op.equalsIgnoreCase("min")) {
         for (int i = 1; i < param.length; i++) {
            if (param[i] < res)
               res = param[i];
         }
      } else if (op.equalsIgnoreCase("max")) {
         for (int i = 1; i < param.length; i++) {
            if (param[i] > res)
               res = param[i];
         }
      }

      return res;
   }

   /**
    * Método que devuelve el valor del menor parámetro de una de función 
    * de pertenencia de una familia si se le pasa "min" o el máximo si se le pasa "max".
    * @param p Función de pertenencia de la que queremos saber su valor 
    * máximo o mínimo.
    * @param op Cadena "max" si buscamos el valor máximo o "min" si 
    * buscamos el mínimo.
    * @return Valor buscado.
    */
   public double opMemFuncFamiliar(FamiliarMemFunc p, String op) {
	    double param[] = p.get();
	    double res;

	    res = param[0];

	    if (op.equalsIgnoreCase("min")) {
	       for (int i = 1; i < param.length; i++) {
	          if (param[i] < res)
	             res = param[i];
	       }
	    } else if (op.equalsIgnoreCase("max")) {
	       for (int i = 1; i < param.length; i++) {
	          if (param[i] > res)
	             res = param[i];
	       }
	    }

	    return res;
	 }

}


