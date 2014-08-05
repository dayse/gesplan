 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

public class DigitosUtil {
	
	
	
	/*
	 * Este metodo esta sendo usado em TecidoAppService mais especificamente
	 * dentro do metodo recuperaListaPaginadaDeTecidosComListaDeTecModels()para
	 * impedir que mais de duas casas decimais sejam impressas apos o calculo para 
	 * o campo consumoPorLoteKg da classe TecModel, perceba que como o atributo esta
	 * marcado com @Transient nao havera uma coluna correspondente no banco de dados,
	 * pois o objetivo deste atributo é apenas para calclo.
	 * Este metodo reduz para duas casas decimais o valor de entrada.
	 */
	
	public static double fracionaParaDuasCasasDecimais(Double valor) {

		if (valor < Double.MAX_VALUE && valor < Double.POSITIVE_INFINITY) {

			String val = valor.toString() + "00";

			int posicao = val.indexOf(".");

			String valorFatorado = val.substring(0, posicao + 3);
			return Double.parseDouble(valorFatorado);
        
		}

		//Se o parametro de entrada for infinito ( caso haja uma divisao por zero ) sera retornado 0.0
		return 0.0;

	}
	
	

}
