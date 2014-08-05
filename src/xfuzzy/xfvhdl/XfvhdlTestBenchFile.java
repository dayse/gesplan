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

import java.util.ArrayList;

import xfuzzy.lang.*;

/**
* Clase que genera el fichero VHDL con el testbench para chequear el 
* correcto funcionamiento del módulo de inferencia del controlador difuso 
* o para chechear el correcto funcionamiento del sistema jerárquico.
* 
* @author Lidia Delgado Carretero
* 
*/
public class XfvhdlTestBenchFile {
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			  ATRIBUTOS DE LA CLASE				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**Guarda las variables de entrada y salida del sistema jerárquico.*/
	private ArrayList<String> listaVblesSistema=new ArrayList<String>();
	
	/**Nº de entradas.*/
	private int inputs;
	
	//cadena que es distinta si se trata del test bench de la 
	//jerarquía o si se trata del test bench de un flc.
	private String Narray;
	
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
   //			  MÉTO_DOS PÚBLICOS DE LA CLASE				        
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

   /**
   * Método que crea la cadena que será escrita en el fichero de 
   * testbench.
   * @return Devuelve la cadena que será escrita en el fichero de 
   * testbench.
   */
   public String createTestBenchSource(Specification spec,XfvhdlFLC flc, ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp) {
	  int cycles_off;
	  XfvhdlHeadFile head;
	  String name;
	  int N=0, No=0;
	  String WORK="";
	  String entradasArray,generic1="",generic2="", 
	  		 port1,cadInputs="",cadOutputs="",signalouts="", s_output="";
	  
	      
	  if(flc==null){//Se trata del test bench de jerarquia
		  name=XfvhdlProperties.name_outputfiles;
		  inputs=spec.getSystemModule().getInputs().length;
		  for (int i=0;i<inputs;i++){
				listaVblesSistema.add(spec.getSystemModule().getInputs()[i].getName());
			}
		  for (int i=0;i<spec.getSystemModule().getOutputs().length;i++){
				listaVblesSistema.add(spec.getSystemModule().getOutputs()[i].getName());
			}
		  for(int i=0;i<listaVblesSistema.size();i++){
				if(i<inputs){
					N=(N>
						XfvhdlLeerXfl.maximoBitsEntradaDeSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)
						?
						N:
						XfvhdlLeerXfl.maximoBitsEntradaDeSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp));
					
					cadInputs+="\t\t"+listaVblesSistema.get(i)+"	: in std_logic_vector("+XfvhdlLeerXfl.maximoBitsEntradaDeSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)+" downto 1);		-- Input "+(i+1)+" signal.\n";
				}else{
					No=(No>
		  				XfvhdlLeerXfl.bitsSalidaSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)
		  				?
		  				N:
		  				XfvhdlLeerXfl.bitsSalidaSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp));
					
					cadOutputs+="\t\t"+listaVblesSistema.get(i)+" : out std_logic_vector("
						+ XfvhdlLeerXfl.bitsSalidaSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)
						+ " downto 1);	-- Output "+(i-inputs+1)+" signal.\n";
					signalouts+="\tsignal s_output"+(i-inputs+1)+" : std_logic_vector("
						+ XfvhdlLeerXfl.bitsSalidaSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)
						+ " downto 1);\n";
		  
				}
		  }
		  
		  head = new XfvhdlHeadFile(name, inputs);
		  cycles_off=5;
		  entradasArray=inputs+"";
		  Narray=N+"";
		  s_output="s_output1";
		  port1="		clk	: in std_logic;									-- Clock signal.\n" +
			"		reset : in std_logic;									-- Reset signal.\n" +
			cadInputs+
			cadOutputs+
			"		pipeline : out std_logic";
	  }else{
		  name=flc.getname();
		  inputs=flc.getinputs();
		  N=flc.getN();
		  No=flc.getNo();
		  WORK="use WORK."+name+"_Constants.all;\n\n";
		  entradasArray=name+"_inputs";
		  Narray=name+"_N";
		  s_output = "s_output";
		  generic1="\tgeneric(\n" 
		         + "\t\t"+name+"_pipe\t: integer;\n" 
		         + "\t\t"+name+"_bits\t: integer);\n";
		  generic2+= "\tgeneric map(\n"
		         + "\t\t"+name+"_pipe\t=> pipe_"+name+",\n" 
		         + "\t\t"+name+"_bits\t=> bits_"+name+")\n"
		         ;
		  port1="\t\tclk\t\t\t: in std_logic;\n"
		         + "\t\treset\t\t: in std_logic;\n" 
		         + entityInputPorts(inputs,Narray)
		         + "\t\tout1\t\t: out std_logic_vector("+name+"_No downto 1);\n" 
		         + "\t\tpipeline\t: out std_logic";
		  head = new XfvhdlHeadFile(name,inputs);
		  signalouts="\tsignal s_output\t\t: std_logic_vector("+name+"_No downto 1);\n";
		  if(flc.getSimplificado()){
	    	  cycles_off=2;
	      }else{//no simplificado
			  cycles_off=3;
		  }
//	      if (flc.getMFC_arithmetic())
//	    	  cycles_off=cycles_off+1;
	      
	  }
	  
      
      String code = head.getHeadTB();
      code
         += "\n"
         + "library IEEE;\n"
         + "use IEEE.std_logic_1164.all;\n"
         + "use IEEE.std_logic_arith.all;\n"
         + "use IEEE.std_logic_unsigned.all;\n"
         + "use IEEE.std_logic_textio.all;\n"
         + "use std.textio.all;\n\n"
         + WORK
         + "library XfuzzyLib;\n"
         + "use XfuzzyLib.Entities.all;\n\n\n"
         + "---------------------------------------------------------------------------\n"
         + "--                           Entity description                          --\n"
         + "---------------------------------------------------------------------------\n\n"
         + "entity "+name+"_tb is\n\n"
         + "\tgeneric ("+name+"_Ntb : integer := 5);\n\n"
         + "end "+name+"_tb;\n\n\n"
         + "---------------------------------------------------------------------------\n"
         + "--                       Architecture description                        --\n"
         + "---------------------------------------------------------------------------\n"
         + "\n"
         + "architecture FPGA of "+name+"_tb is\n\n"
         + "\ttype s_input_array is array(1 to " + entradasArray + ") of "
         + "std_logic_vector("+Narray+" downto 1);\n"
         + "\tsignal s_clk\t\t: std_logic;\n"
         + "\tsignal s_reset\t\t: std_logic;\n"
         + "\tsignal s_pipeline\t: std_logic;\n"
         + "\tsignal s_input\t\t: s_input_array;\n"
         + signalouts + "\n"
         + "\tcomponent "+name+"\n" 
         + generic1
         + "\tport(\n" 
         + port1 
         	+ ");\n" 
         + "\tend component;\n\n" 
         + "begin\n\n"
         // UUT
         + "\tUUT : "+name+"\t\t\t\t-- Fuzzy logic controller instance.\n\n"
         + generic2
         + "\tport map(\n" 
         + "\t\tclk\t\t\t=> s_clk,\n"
         + "\t\treset\t\t=> s_reset,\n"      
         + controllerSignals(inputs,flc, listaFlc, listaCrisp, spec)        
         + "\t\tpipeline\t=> s_pipeline);\n\n"
         // Clock
         + "\tClock : process\t\t\t\t\t-- Clock signal generator.\n\n"
         + "\tbegin\n"
         + "\t\ts_clk <= '0';\n"
         + "\t\twait for "+(inputs>1?25:20)+" ns;\n"
         + "\t\ts_clk <= '1';\n"
         + "\t\twait for "+(inputs>1?25:20)+" ns;\n"
         + "\tend process;\n\n"
         //Test
         + "\tTest : process\t\t\t\t\t-- Testbench process.\n\n"
         + "\t\tvariable cycles_pipe : integer := "+cycles_off+";\n"
         + "\t\tvariable value_out\t: integer := 0;\n"
         + "\t\tvariable cycles_off\t: integer := 0;\n"
         + "\t\tvariable lin\t\t: line;\n\n"        
         + "\t\tfile fil : text open write_mode is \"SIM.out\";\n\n" 
         + "\t\ttype value_in_array is array (1 to "+entradasArray+", 1 to cycles_pipe) of integer;\n"
         + "\t\ttype input_array is array (1 to 2) of integer;\n"
         + "\t\tvariable value_in\t: value_in_array;\n"
         + "\t\tvariable input\t\t: input_array;\n\n"
         //
         + "\tbegin\n"
         + (inputs>1? 
        	  "\t\tinput := (1,2);\n\n":"\t\tinput := (1,0);\n\n")
	     + "\t\tif (s_reset = 'U') then\n"
		 + "\t\t\ts_reset  <= '1';\n"
		 + "\t\t\twait for 90 ns;\n"
		 + "\t\t\ts_reset  <= '0';\n"
		 + resetInputSignals(inputs, N)
		 + "\t\telse\n"
		 + "\t\t\twait until rising_edge(s_pipeline);\n"
		 //
		 + "\t\t\t\tfor i in 1 to " +(inputs>1? "2":"1")+ " loop\n"
		 + "\t\t\t\t\tif (cycles_pipe > 1) then\n" 
		 + "\t\t\t\t\t\tfor j in 1 to (cycles_pipe-1) loop\n"
		 + "\t\t\t\t\t\t\tvalue_in(input(i),j) := value_in(input(i),j+1);\n"
		 + "\t\t\t\t\t\tend loop;\n"
		 + "\t\t\t\t\tend if;\n"
		 + "\t\t\t\t\tvalue_in(input(i),cycles_pipe) := conv_integer('0' & s_input(input(i)));\n"
	     + "\t\t\t\tend loop;\n\n"
	     //
		 + "\t\t\t\tvalue_out := conv_integer('0' & "+s_output+");\n\n"
		 //
		 + "\t\t\t\tif (cycles_off >= cycles_pipe-1) then\n"
		 + "\t\t\t\t\twrite(lin, value_in(input(1),1), right, 7);\n"
		 + (inputs>1?
			  "\t\t\t\t\twrite(lin, value_in(input(2),1), right, 7);\n":"")
		 + "\t\t\t\t\twrite(lin, value_out, right, 7);\n"
		 + "\t\t\t\t\twriteline(fil, lin);\n"
		 + (inputs>1?
			  "\t\t\t\t\tif (value_in(input(1),1) = 2**" + Narray + " - 1) then\n"
			+ "\t\t\t\t\t\twrite(lin, ' ');\n"
		    + "\t\t\t\t\t\twriteline(fil, lin);\n"
		    + "\t\t\t\t\tend if;\n"
		    :"")
		 + "\t\t\t\telse\n"
		 + "\t\t\t\t\tcycles_off := cycles_off + 1;\n"
		 + "\t\t\t\tend if;\n\n"
	     //
	     + "\t\t\tif (s_input(input(1)) < 2**"+Narray+" - 2**("+Narray+" - "+name+"_Ntb)) then\n"
	     + "\t\t\t\ts_input(input(1)) <= s_input(input(1)) + 2**("+Narray+" - "+name+"_Ntb);\n"
	     + "\t\t\tend if;\n\n"    
	     //
	     + "\t\t\tif (s_input(input(1)) = 2**"+Narray+" - 2**("+Narray+" - "+name+"_Ntb)) then\n"
	     + "\t\t\t\ts_input(input(1)) <= s_input(input(1)) + 2**("+Narray+" - "+name+"_Ntb) - 1;\n"
	     + "\t\t\tend if;\n\n"
	     //
	     + "\t\t\tif (s_input(input(1)) = 2**"+Narray+" - 1) then\n"
	     + "\t\t\t\ts_input(input(1)) <= (others => '0');\n"
	     + (inputs>1?
	    	 "\t\t\t\tif (s_input(input(2)) < 2**"+Narray+" - 2**("+Narray+" - "+name+"_Ntb)) then\n"
	       + "\t\t\t\t\ts_input(input(2)) <= s_input(input(2)) + 2**("+Narray+" - "+name+"_Ntb);\n"
	       + "\t\t\t\tend if;\n"
	       + "\t\t\t\tif (s_input(input(2)) = 2**"+Narray+" - 2**("+Narray+" - "+name+"_Ntb)) then\n"
	       + "\t\t\t\t\ts_input(input(2)) <= s_input(input(2)) + 2**("+Narray+" - "+name+"_Ntb) - 1;\n"
	       + "\t\t\t\tend if;\n"
		   :
			 ""
		   )
		 + "\t\t\tend if;\n\n"  
	     //
	     + (inputs>1?
	         "\t\t\tif ((value_in(input(1),1) = 2**"+Narray+" - 1)\n"
	       + "\t\t\tand (value_in(input(2),1) = 2**"+Narray+" - 1)) then\n"
	       + "\t\t\t\tfor i in 1 to 2 loop\n"
	       + "\t\t\t\t\ts_input(input(i)) <= (others => '0');\n"
	       + "\t\t\t\tend loop;\n"
	       :
	    	 "\t\t\tif (value_in(input(1),1) = 2**"+Narray+" - 1) then\n"
	       + "\t\t\t\ts_input(input(1)) <= (others => '0');\n"
	       )
	     + "\t\t\t\treport \"-- END OF SIMULATION --\";\n"
	     + "\t\t\t\twait;                             -- End of simulation.\n"
	     + "\t\t\tend if;\n"
	     + "\t\tend if;\n"
	     + "\tend process;\n\n"
	     //
		 + "end FPGA;\n";
	     
      return code;
   }
  
   /**@return Subcadena que describe las entradas en el bloque Entity.*/
   private String entityInputPorts(int n, String Narray){
	   String res="";
	   for(int i=0;i<n;i++){
		   res+="\t\tin"+(i+1)+"\t\t\t: in std_logic_vector("+Narray+" downto 1);\n";
	   }   
	   return res;   
   }
   
   /**@return Subcadena que describe las conexiones de las señales 
    * en la zona de Controller.*/
   private String controllerSignals(int n, XfvhdlFLC flc, ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp, Specification spec){
	   String res="";
	   if(flc==null){
		   for(int i=0;i<listaVblesSistema.size();i++){
			   if(i<inputs){
				   int max = Integer.valueOf(Narray);
				   int min = max-
				   		XfvhdlLeerXfl.maximoBitsEntradaDeSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)+1;
				   res+="\t\t" + listaVblesSistema.get(i) + "\t\t\t=> s_input(" + (i + 1) + ")("+max+" downto "+min+"),\n";
			   }else
				   res+="\t\t" + listaVblesSistema.get(i) + "\t\t\t=> s_output" + (i - inputs + 1) + ",\n";
		   }
	   }else{
		   for(int i=0;i<n;i++){
			   res+="\t\tin" + (i + 1) + "\t\t\t=> s_input(" + (i + 1) + "),\n";
		   }
		   res+="\t\tout1\t\t=> s_output,\n";
	   }
	   return res;
   }
  
   private String resetInputSignals(int n, int nb){
	   String res="";   
	   if(n==1){
		   res+= "\t\t\ts_input(input(1)) <= (others => '0');\n";
	   }else{
		   if(n>2){
			   for(int i=1;i<=n;i++){		   
				   res+="\t\t\ts_input("+i+") <= \"1";
				   for(int j=2;j<=nb;j++){
					   res+="0";
				   }
				   res+="\";\n";
			   }
		   }		   
		   res+= "\t\t\ts_input(input(1)) <= (others => '0');\n"
			   + "\t\t\ts_input(input(2)) <= (others => '0');\n";
	   }
	   return res;
   }
   
}
   