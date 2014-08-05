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

import xfuzzy.*;
import xfuzzy.lang.*;

/**
* Clase que genera el código de las memorias de antecedentes 
* del módulo de inferencia
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlAntecedentMemFiles {

   // El siguiente atributo se usa para mostrar los mensajes por pantalla
   //private XfvhdlMessage msg;
	/***/
	XfvhdlError error;

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			  CONSTRUCTOR DE LA CLASE				          
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/** Constructor de la clase
	 */
 	  XfvhdlAntecedentMemFiles() {
 	     error = new XfvhdlError();
 	  }

 	  /**
 	  * Método que crea la cadena que será escrita en la zona de 
 	  * antecedentes en memoria, siempre y cuando se sinteticen en ROM.
 	  * @param  spec Especificación XFL.
 	  * @param  flc  Módulo de inferencia.
 	  * @return Devuelve la cadena que será escrita en el fichero VHDL 
 	  * del módulo de inferencia, en la zona de antecedentes en memoria.
 	  */
 	  public String createMemorySourceROM(Specification spec, XfvhdlFLC flc) {

 		  Variable[] var = spec.getSystemModule().getInputs();
 		  String code="", alphasport="", elesport="";
      	
 		  XfvhdlProperties.dir_regl=0;
      
 		  for(int i=1;i<=flc.getmfcs().size();i++){
    	  
 			  XfvhdlUniverse u = new XfvhdlUniverse();
 			  XfvhdlDiscretizeUniverse disc = new XfvhdlDiscretizeUniverse();
 			  u = disc.discretizeUniverse(var[i-1], flc, (i-1));
          
 			  int bitswhen=(int)Math.ceil(Math.log(Integer.valueOf(flc.getmfcs().get(i-1).getn_fp()))/Math.log(2));
 			  flc.addBits_var(bitswhen);
 			  XfvhdlProperties.dir_regl+=bitswhen;
    	  
 			  alphasport="\t\talpha_1 : out std_logic_vector("+flc.getname()+"_grad downto 1);\n"
 			  + "\t\talpha_2 : out std_logic_vector("+flc.getname()+"_grad downto 1);\n";
 			  elesport="\t\tL_1 : out std_logic_vector("+flc.getname()+"_MFC"+i+"_bits downto 1);\n"
 			  + "\t\tL_2 : out std_logic_vector("+flc.getname()+"_MFC"+i+"_bits downto 1));\n";
          

 			  code+="---------------------------------------------------------------------------\n"
 				  + "--                           "+flc.getname()+"_AntecedentMem"+i+"                      --\n"
 				  + "---------------------------------------------------------------------------\n"
 				  + "\n"
 				  + "library IEEE;\n"
 				  + "use IEEE.std_logic_1164.all;\n"
 				  + "use IEEE.std_logic_arith.all;\n"
 				  + "use IEEE.std_logic_unsigned.all;\n"
 				  + "\n"
 				  + "use WORK."+flc.getname()+"_Constants.all;\n"
 				  + "\n"
 				  + "entity "+flc.getname()+"_AntecedentMem"+i+" is\n\n"
 				  + "\tport(\n"
 				  + "\t\tpipe\t: in std_logic;\n"
 				  + "\t\taddr\t: in std_logic_vector("+flc.getname()+"_N-1 downto 0);\n"
 				  + alphasport
 				  + elesport
 				  + "\n"
 				  + "end "+flc.getname()+"_AntecedentMem"+i+";\n"
 				  + "\n"
 				  + "\n"
 				  + "---------------------------------------------------------------------------\n"
 				  + "--                       Architecture description                        --\n"
 				  + "---------------------------------------------------------------------------\n"
 				  + "\n"
 				  + "architecture FPGA of " + flc.getname() + "_AntecedentMem" + i + " is\n\n"
 				  + "signal s_addr : std_logic_vector(" + flc.getname() + "_N-1 downto 0);\n"
 				  + "signal s_data : std_logic_vector(2*" + flc.getname() + "_grad+" + flc.getname() + "_MFC"+i+"_bits downto 1);\n\n"
 				  + "\tsubtype ROM_WORD is std_logic_vector(2*" + flc.getname() + "_grad+" + flc.getname() + "_MFC" + i + "_bits-1 downto 0);\n"
 				  + "\ttype ROM_TABLE is array (0 to 2**" + flc.getname() + "_N-1) of ROM_WORD;\n"
 				  + "\tconstant ROM : ROM_TABLE := ROM_TABLE'(\n";
 			  
 			  XfvhdlBinaryDecimal converter = new XfvhdlBinaryDecimal();
 			  String val1 = new String();
 			  String val2 = new String();
	
 			  for (int j = 0; j < u.getLength() - 1; j++) {
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
	
 				  code += "\t\tROM_WORD'(\""
 					  + converter.toBinary(
 							  (u.getXUniversePoint(j).getEtiq()) - 1,
 							  (int) Math.ceil(
 									  Math.log(Double.valueOf(flc.getmfcs().get(i-1).getn_fp()))
	   		 	              / Math.log((double) 2)))
	         	   + val1
	         	   + val2
	         	   + "\"),\n";
 			  }
	
 			  // La última línea es especial porque hay que ponerle un 
 			  // parentesis al final
		      val1 =
		         converter.toBinaryInRange(
		            u.getXUniversePoint(u.getLength() - 1).getVal1(),
		            0.0,
		            1.0,
		            flc.getgrad());
		      val2 =
		    	  converter.toBinaryInRange(
		    			  u.getXUniversePoint(u.getLength() - 1).getVal2(),
		    			  0.0,
		    			  1.0,
		    			  flc.getgrad());
		      code += "\t\tROM_WORD'(\""
		    	  + converter.toBinary(
		    			  (u.getXUniversePoint(u.getLength() - 1).getEtiq()) - 1,
		    			  (int) Math.ceil(
		    					  Math.log(Double.valueOf(flc.getmfcs().get(i-1).getn_fp()))
		    					  / Math.log((double) 2)))
		    					  + val1
		    					  + val2
		    					  + "\"));\n";
	
		      code += "begin\n\n"
		    	  + "\t\ts_addr  <= addr when rising_edge(pipe);\n"
		    	  + "\t\ts_data	<= ROM(conv_integer(s_addr));\n" 
		    	  + "\t\tL_1		<= s_data(2*"+flc.getname()+"_grad+"+flc.getname()+"_MFC"+i+"_bits downto (2*"+flc.getname()+"_grad)+1);\n"
		    	  + "\t\tL_2		<= s_data(2*"+flc.getname()+"_grad+"+flc.getname()+"_MFC"+i+"_bits downto (2*"+flc.getname()+"_grad)+1) + '1';\n"
		    	  + "\t\talpha_1	<= s_data(2*"+flc.getname()+"_grad downto "+flc.getname()+"_grad+1);\n"
		    	  + "\t\talpha_2	<= s_data("+flc.getname()+"_grad downto 1);\n"
		    	  + "end FPGA;\n\n";
 		  }
 		  return code;
 	  }

 	  /**
 	   * Método que crea la cadena que será escrita en la zona de 
 	   * antecedentes en memoria, siempre y cuando se sinteticen en 
 	   * bloques lógicos.	
 	   * @param  spec Especificación XFL.
 	   * @param  flc  Módulo de inferencia.
 	   * @return Devuelve la cadena que será escrita en el fichero VHDL 
 	   * del módulo de inferencia, en la zona de antecedentes en memoria.
 	   */
 	  public String createMemorySourceLB(Specification spec, XfvhdlFLC flc) {

 		  Variable[] var = spec.getSystemModule().getInputs();
 		  String code="", alphasport="", elesport="";
		  String others = "";
		    
	      XfvhdlProperties.dir_regl=0;
	      
	      for(int i=1;i<=flc.getmfcs().size();i++){
	    	  
	    	  XfvhdlUniverse u = new XfvhdlUniverse();
	          XfvhdlDiscretizeUniverse disc = new XfvhdlDiscretizeUniverse();
	          u = disc.discretizeUniverse(var[i-1], flc, (i-1));
	          
	    	  int bitswhen=(int)Math.ceil(Math.log(Integer.valueOf(flc.getmfcs().get(i-1).getn_fp()))/Math.log(2));
	    	  flc.addBits_var(bitswhen);
	    	  XfvhdlProperties.dir_regl+=bitswhen;
	    	  
	    	  others = "";
	    	  for(int j=0;j<(2*flc.getgrad()+bitswhen);j++){
					others+= "-";
	    	  }
	    	  
	    	  alphasport="\t\talpha_1 : out std_logic_vector("+flc.getname()+"_grad downto 1);\n"
	    	  	+ "\t\talpha_2 : out std_logic_vector("+flc.getname()+"_grad downto 1);\n";
	    	  elesport="\t\tL_1 : out std_logic_vector("+flc.getname()+"_MFC"+i+"_bits downto 1);\n"
	    	  	+ "\t\tL_2 : out std_logic_vector("+flc.getname()+"_MFC"+i+"_bits downto 1));\n";
	          

		      code+="---------------------------------------------------------------------------\n"
		         + "--                           "+flc.getname()+"_AntecedentMem"+i+"                      --\n"
		         + "---------------------------------------------------------------------------\n"
		         + "\n"
		         + "library IEEE;\n"
		         + "use IEEE.std_logic_1164.all;\n"
		         + "use IEEE.std_logic_arith.all;\n"
		         + "use IEEE.std_logic_unsigned.all;\n"
		         + "\n"
		         + "use WORK."+flc.getname()+"_Constants.all;\n"
		         + "\n"
		         + "entity "+flc.getname()+"_AntecedentMem"+i+" is\n\n"
		         + "\tport(\n" 
		         + "\t\tpipe\t: in std_logic;\n"
		         + "\t\taddr\t: in std_logic_vector("+flc.getname()+"_N-1 downto 0);\n"
		         + alphasport
		         + elesport
		         + "\n"
		         + "end "+flc.getname()+"_AntecedentMem"+i+";\n"
		         + "\n"
		         + "\n"
		         + "---------------------------------------------------------------------------\n"
		         + "--                       Architecture description                        --\n"
		         + "---------------------------------------------------------------------------\n"
		         + "\n"
		         + "architecture FPGA of "+flc.getname()+"_AntecedentMem"+i+" is\n"
				 + "	signal s_addr : std_logic_vector("+flc.getname()+"_N-1 downto 0);\n\n"
		         + "	signal s_data : std_logic_vector(2*"+flc.getname()+"_grad+"+flc.getname()+"_MFC"+i+"_bits downto 1);\n\n"
			     + "begin\n"
				 + "	s_addr <= addr when rising_edge(pipe);\n\n"	
				 + "	Read_mem : process(s_addr)\n"
				 + "	begin\n\n"
				 + "		case s_addr is\n";
		
		      XfvhdlBinaryDecimal converter = new XfvhdlBinaryDecimal();
		      String val1 = new String();
		      String val2 = new String();
		      String val  = new String();
		      
		      for (int j = 0; j < u.getLength(); j++) {
		         val  = converter.toBinary(j, flc.getN());
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
		
		         code +="			when \""
		        	  + val
		        	  + "\" => s_data <= \""
		        	  + converter.toBinary(
		        	    (u.getXUniversePoint(j).getEtiq()) - 1,
		                (int) Math.ceil(
		   		        Math.log(Double.valueOf(flc.getmfcs().get(i-1).getn_fp()))
		   		        / Math.log((double) 2)))
		              + val1
		              + val2
		              + "\";\n";
		      }
		
		      code +="			when others => s_data <= \""
		    	   + others + "\";\n"
		      	   + "		end case;\n"
		    	   + "	end process Read_mem;\n\n"
		           + "	L_1			<= s_data(2*"+flc.getname()+"_grad+"+flc.getname()+"_MFC"+i+"_bits downto (2*"+flc.getname()+"_grad)+1);\n"
		           + "	L_2			<= s_data(2*"+flc.getname()+"_grad+"+flc.getname()+"_MFC"+i+"_bits downto (2*"+flc.getname()+"_grad)+1) + '1';\n"
		           + "	alpha_1	<= s_data(2*"+flc.getname()+"_grad downto "+flc.getname()+"_grad+1);\n"
		           + "	alpha_2	<= s_data("+flc.getname()+"_grad downto 1);\n\n"
		           + "end FPGA;\n\n";
	      }
	      return code;
	   }

} // Fin de la clase
