 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utilitario
{	
	
	private static NumberFormat nf;
	
	static
	{	nf = NumberFormat.getNumberInstance(new Locale("pt","BR"));
		nf.setMaximumFractionDigits (2);	   
		nf.setMinimumFractionDigits (2);
	}
	
	//----------------------------------Metodos utilizados para ConversorDouble------------------------------------//
	
	public static Double strToDouble(String valor) throws Exception
	{     
		
		if(Pattern.compile("^([-]{0,1}[0-9]*)|([-]{0,1}[0-9]+[/.,]{1})|([-]{0,1}[0-9]*[/.,]{1}[0-9]+)$").matcher(valor).matches()){
		
			
			 /* O metodo indexOf retorna a posiçao de uma String
			  * se a String desejada nao for encontrada sera 
			  * retornado -1. 
			  * 
			  */
			int trocaVirgulaToPonto=0;
			if(valor.indexOf(",")!=-1){
				
				trocaVirgulaToPonto = valor.indexOf(",");
				valor = valor.substring(0,trocaVirgulaToPonto) + "." + valor.substring(trocaVirgulaToPonto+1,valor.length());
		   }

			return Double.parseDouble(valor);
		}		
		
		throw new Exception("Valor inválido"); 
	}
	
	public static String doubleToStr(Double valor)
	{	return String.valueOf(valor);
	}	
	
	public static String doubleToStrFormatado(Double valor)
	{	return nf.format(valor);
	}
	
		
} 
	
	
