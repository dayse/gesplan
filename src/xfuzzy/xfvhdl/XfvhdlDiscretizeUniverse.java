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
//import xfuzzy.xfvhdl.XfvhdlError;

import java.util.*;

/**
* Clase que discretiza el universo de discurso de una variable
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlDiscretizeUniverse {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	/**Atributo que nos avisa si las entradas no están normalizadas 
	 * porque el solapamiento entre las funciones de pertenencia 
	 * es mayor que 1.*/
	boolean EntradasNoNormalizadasMayor1;
	
	/**Atributo que nos avisa si las entradas no están normalizadas 
	 * porque el solapamiento entre las funciones de pertenencia 
	 * es menor que 1.*/
	boolean EntradasNoNormalizadasMenor1;
	
	/**Constructor de la clase.*/
	public XfvhdlDiscretizeUniverse(){
		EntradasNoNormalizadasMayor1=false;
		EntradasNoNormalizadasMenor1=false;
	}
	
   /**
   * Método que discretiza el universo de discurso de una variable.
   * @return Devuelve el universo de discurso de la variable v 
   * discretizado.
   */
   public XfvhdlUniverse discretizeUniverse(Variable v, XfvhdlFLC flc, int entrada) {
      List mf_sorted = new ArrayList();
      ParamMemFunc mf1 = null;
      ParamMemFunc mf2 = null;
      FamiliarMemFunc fmf1=null;
      FamiliarMemFunc fmf2=null;
      double minMF1=5000, minFMF1=5000;
      double maxMF1=-5000, maxFMF1=-5000;
      int total;
      int actual = 0;
      double valor1;
      double valor2;
      List puntos = new ArrayList();
      double factor;
      double iR, iN;
      boolean familiar;
      int cardinalidad=8;
      EntradasNoNormalizadasMayor1=false;
      EntradasNoNormalizadasMenor1=false;
      if(flc.getN()!=0){
    	  cardinalidad=flc.getN();
      }
      // Crea un XUniverse con cardinalidad N (que es el número de bits 
      // con que se codifican las entradas)siempre y cuando este dato se
      // haya inicializado previamente.
      XfvhdlUniverse u =
         new XfvhdlUniverse(
            v.getName(),
            v.getType().getUniverse().max(),
            v.getType().getUniverse().min(),
            cardinalidad);

      // Crea un objeto XFvhdlInOrderParamMemFunc con las funciones 
      // de pertenencia ordenadas de la variable v
      XfvhdlInOrderParamMemFunc inOrderMf =
         new XfvhdlInOrderParamMemFunc(v);

      // Ordena las funciones de pertenencia de la variable v
      mf_sorted = inOrderMf.sort();

      // Calcula el número de funciones de pertenencia
      total = mf_sorted.size();

      if (mf_sorted.size()==0&&inOrderMf.getFamiliarMemFunc().length>0){
    	  familiar=true;
    	  total=inOrderMf.getFamiliarMemFunc().length;
      }else
    	  familiar=false;
      //Si la funciones de pertenencia de la emtrada es una familia de funciones,
      //entonces, la entrada está normalizada y no hay que comprobar más nada.
      if(familiar){
    	  EntradasNoNormalizadasMayor1=false;
    	  //return null;
      }
      // Comprueba el solapamiento 2 a 2
      if (total >= 2&&!familiar) {
         // Comprueba el solapamiento de todas las funciones de  
         // pertenencia menos las tres últimas
         for (int i = 0; i < total - 2; i++) {
            mf1 = (ParamMemFunc) mf_sorted.get(i);
            mf2 = (ParamMemFunc) mf_sorted.get(i + 2);
            
            
            if (inOrderMf.opMemFuncParameter(mf1, "max")
               > inOrderMf.opMemFuncParameter(mf2, "min")) {
               new XfvhdlError(4, v.getName());
        	   EntradasNoNormalizadasMayor1=true;
            }
         }
         // Comprueba el solapamiento en las tres últimas funciones 
         // de pertenencia
         mf1 = (ParamMemFunc) mf_sorted.get(total - 3);
         mf2 = (ParamMemFunc) mf_sorted.get(total - 1);

         if (inOrderMf.opMemFuncParameter(mf1, "max")
            > inOrderMf.opMemFuncParameter(mf2, "min")) {
            new XfvhdlError(4, v.getName());
            EntradasNoNormalizadasMayor1=true;
         }
      }

      // Coge las dos primeras funciones de pertenencia
      if (total == 0&&!familiar) { // no puede ser que no existan funciones
         new XfvhdlError(5, v.getName());
      }
      if(!familiar) {
    	  mf1 = (ParamMemFunc) mf_sorted.get(actual);
	  	  minMF1 = inOrderMf.opMemFuncParameter(mf1, "min");
	  	  maxMF1 = inOrderMf.opMemFuncParameter(mf1, "max");     
      } else {
    	  fmf1= (FamiliarMemFunc) inOrderMf.getFamiliarMemFunc()[actual];
     	  minFMF1 = minimoFamilia(actual, entrada, total, flc);
    	  maxFMF1 = maximoFamilia(actual, entrada, total, flc);
      }
      
      actual++;
      if (actual == total) {
         // si solo hay una funcion de pertenencia coge esa función 2 veces 
         actual--;
      }
      
      if(!familiar) {
    	  mf2 = (ParamMemFunc) mf_sorted.get(actual);
      } else {
    	  fmf2= (FamiliarMemFunc) inOrderMf.getFamiliarMemFunc()[actual];
      }  
 	  actual++;
 	  
      // calcula el factor necesario para mantener la relación entre los 
      // universos de discurso
      factor =
         (v.getType().getUniverse().max()
            - v.getType().getUniverse().min())
            / Math.pow((double) 2, (double) cardinalidad);

      // Hace un recorrido para cada uno de los puntos del universo de 
      // discurso
      long precision=1000000000;
      for (int i = 0;
         i < (int) Math.pow((double) 2, (double) cardinalidad);
         i++) {
    	 iR = ( i * factor) + v.getType().getUniverse().min();
    	 iN = ( i / Math.pow((double) 2, (double) cardinalidad));
    	 
    	 if(!familiar){
	         if (iR < minMF1) {
	            //si el punto es menor que el mínimo de mf1 añade un 0
	            u.addXUniversePoint(new XfvhdlUniversePoint(actual - 1, 0, 0));
	            puntos.add(new Double(0));
	         } else if (iR >= minMF1 && iR <= maxMF1) {
	            // si i está dentro de la función de pertenencia mf1
		        valor1 = mf1.compute((double) iR);
		        valor2 = mf2.compute((double) iR);
		        valor1=Math.round(valor1*precision)/1000000000.0;
		        valor2=Math.round(valor2*precision)/1000000000.0;
	            u.addXUniversePoint(new XfvhdlUniversePoint(actual - 1, valor1, valor2));
	            if((valor1+valor2)<1.0){
	            	EntradasNoNormalizadasMenor1=true;
	            }
	         } else {
	            if (actual < total) { //avanza una función de pertenencia           	
		        	mf1 = mf2;
		    		mf2 = (ParamMemFunc) mf_sorted.get(actual);
		    		minMF1 = inOrderMf.opMemFuncParameter(mf1, "min");
		    		maxMF1 = inOrderMf.opMemFuncParameter(mf1, "max");
		    		actual++;
	            }
		        valor1 = mf1.compute((double) iR);
		        valor2 = mf2.compute((double) iR);
		        valor1=Math.round(valor1*precision)/1000000000.0;
		        valor2=Math.round(valor2*precision)/1000000000.0;
	            u.addXUniversePoint(new XfvhdlUniversePoint(actual - 1, valor1, valor2));
	            if((valor1+valor2)<1.0){
	            	EntradasNoNormalizadasMenor1=true;
	            }
	         }
      	}else{
      		if (iN < minFMF1) {
	            //si el punto es menor que el mínimo de mf1 añade un 0
	            u.addXUniversePoint(new XfvhdlUniversePoint(actual - 1, 0, 0));
	            puntos.add(new Double(0));
	         } else if (
	            iN >= minFMF1 && iN <= maxFMF1) {
	            // si i está dentro de la función de pertenencia mf1
		        valor1 = fmf1.compute((double) iR);
		        valor2 = fmf2.compute((double) iR);
		        valor1=Math.round(valor1*precision)/1000000000.0;
		        valor2=Math.round(valor2*precision)/1000000000.0;
	            u.addXUniversePoint(new XfvhdlUniversePoint(actual - 1, valor1, valor2));
	         } else {
	            if (actual < total) { //avanza una función de pertenencia          	
		        	fmf1 = fmf2;
		    		fmf2 = (FamiliarMemFunc) inOrderMf.getFamiliarMemFunc()[actual];
		    		minFMF1 = minimoFamilia(actual-1, entrada, total, flc);
		    		maxFMF1 = maximoFamilia(actual-1, entrada, total, flc);
		    		actual++;
	            }
		        valor1 = fmf1.compute((double) iR);
		        valor2 = fmf2.compute((double) iR);
		        valor1=Math.round(valor1*precision)/1000000000.0;
		        valor2=Math.round(valor2*precision)/1000000000.0;
	            u.addXUniversePoint(new XfvhdlUniversePoint(actual - 1, valor1, valor2));
	         }
      	}
	     }
      return u;
   }

   /**@return EntradasNoNormalizadasMayor1*/
   public boolean getEntradasNoNormalizadasMayor1(){
	   return EntradasNoNormalizadasMayor1;
   }
   
   /**@return EntradasNoNormalizadasMenor1*/
   public boolean getEntradasNoNormalizadasMenor1(){
	   return EntradasNoNormalizadasMenor1;
   }
   
   /**@param actual Índice de la función de pertenencia actual.
    * @param entrada Índice de la entrada del flc actual.
    * @param total Número de funciones de pertenencia.
    * @param flc Módulo de inferencia.
    * @return Mínimo de la función de pertenencia.*/
   public double minimoFamilia(int actual, int entrada, int total, XfvhdlFLC flc){
	   double min;
	   if(actual==0)
	    	  min=0;
	   else if(actual==total)
	    	  min=flc.getmfcs().get(entrada).getPunto(actual-1);
	   else//Este es el caso general
	    	  min=flc.getmfcs().get(entrada).getPunto(actual-1);
	   return min;
   }
   
   /**@param actual Índice de la función de pertenencia actual.
    * @param entrada Índice de la entrada del flc actual.
    * @param total Número de funciones de pertenencia.
    * @param flc Módulo de inferencia.
    * @return Máximo de la función de pertenencia.*/
   public double maximoFamilia(int actual, int entrada, int total, XfvhdlFLC flc){
	   double max;
	   if(actual==0)
	    	  max=flc.getmfcs().get(entrada).getPunto(actual+1);
	   else if(actual==total)
	    	  max=1;
	   else//Este es el caso general
	    	  max=flc.getmfcs().get(entrada).getPunto(actual+1);
	      
	   return max;
   }
} // Fin de la clase
