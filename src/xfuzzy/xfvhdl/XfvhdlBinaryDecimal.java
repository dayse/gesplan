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

import java.io.*;

/**
* Clase que convierte valores entre los formatos decimal y binario
* 
* @author Jos� Mar�a �vila Maireles
* 
*/
public class XfvhdlBinaryDecimal {

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //				M�TO_DOS P�BLICOS DE LA CLASE			        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /** 
   * M�todo que convierte una cadena de 0's y 1's (numero en binario) 
   * en un entero
   * @param s Cadena de 0's y 1's
   * @return Entero correspondiente
   */
   public int toDecimal(String s) throws IOException {
      int a = 0;
      int n = s.length();

      for (int i = 0; i < n; i++) {
         char c = s.charAt(i);
         a *= 2;
         a += toDecimal(c);
      }

      return a;
   }
   
   /** 
    * M�todo que convierte un double a un String de 0's y 1's (numero 
    * encomplemento a 2 binario) con una precisi�n de l bits, con pD bits para la parte decimal
    * @param n Entero que se quiere pasar a complemento a 2
    * @param l N�mero de bits totales
    * @param pD Numero de bits para la parte decimal
    * @return Cadena de 0's y 1's que representa al n�mero en complemento a 2
    */   
   public String toCA2(double n, int l, int pD) throws IOException{
	   //System.out.println("Double a ca2: "+n);
	   int entero=(int)(n*Math.pow(2, pD));
	   String ca2= toBinary(Math.abs(entero),l);
	   String ca2neg="";
	   String ceros = new String();
	   int decimal;
	   boolean primer_uno=false;

	   if(n<0){
		   for (int i = l-1; i >= 0; i--) {
		         char c = ca2.charAt(i);
		         if(primer_uno){
		        	 ca2neg=invierte(c)+ca2neg;
		         }else
		        	 ca2neg= c+ca2neg;
		         
		         if(c=='1'&&!primer_uno)
		        	 primer_uno=true;
		      }
		   ca2=ca2neg;
	   }
	   return ca2;
   }
   /** 
    * M�todo que invierte un caracter binario.
    * @param c 1 o 0
    * @return char 0 o 1
    */
   public char invierte(char c){
	   char res;
	   if (c=='1')
		   res='0';
	   else
		   res='1';
	   return res;
   }
   /** 
   * M�todo que convierte un entero a un String de 0's y 1's (numero en 
   * binario) con una precisi�n de l bits
   * @param n Entero que se quiere pasar a binario
   * @param l N�mero de bits
   * @return Cadena de 0's y 1's que representa al n�mero en binario
   */
   public String toBinary(int n, int l) {
      String binary = new String(Integer.toBinaryString(n));
      String ceros = new String();

      if (binary.length() < l) {
         for (int i = 0; i < l - binary.length(); i++) {
            ceros += "0";
         }

         binary = ceros + binary;
      } else if (binary.length() > l) {
         binary = binary.substring(binary.length() - l);
      }
      
      return binary;
   }

   /** 
   * M�todo que convierte un double a un String de 0's y 1's (numero en 
   * binario) con una precisi�n de l bits dentro del rango min-max
   * @param valor Double que se quiere pasar a binario
   * @param min Valor m�nimo del rango
   * @param max Valor m�ximo del rango
   * @param p N�mero de bits
   * @return Cadena de 0's y 1's que representa al n�mero en binario
   */
   public String toBinaryInRange(
      double valor,
      double min,
      double max,
      int p) {
	   
      double max_bin;
      int res;
      
      if (valor > max){  // Satura en max
      	valor = max;
      }
         
      max_bin = Math.pow((double) 2, (double) p) - 1;

      res = (int) ((max_bin * (valor - min)) / (max - min));

      String binary = new String(Integer.toBinaryString(res));
      String ceros = new String();

      if (binary.length() < p) {
         for (int i = 0; i < p - binary.length(); i++) {
            ceros += "0";
         }

         binary = ceros + binary;
      } else if (binary.length() > p) {
         binary = binary.substring(binary.length() - p);
      }

      return binary;
   }

   /** 
   * M�todo que genera un String con n guiones ("-") para la cl�usula others
   * @param n N�mero de guiones a generar
   * @return Cadena de guiones
   */
   public String toHyphen(int n) {
      String hyphens = new String();

      for (int i = 0; i < n; i++)
         hyphens += "-";

      return hyphens;
   }

   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  M�TO_DOS PRIVADOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * M�todo privado que pasa de un char (0 o 1) a un entero
   * @param c Car�cter a pasar a binario 
   */
   private int toDecimal(char c) throws IOException {
      switch (c) {
         case '0' :
            return 0;
         case '1' :
            return 1;
         default :
            throw new IOException("non digit: " + c);
      }
   }

} // Fin de la clase.
