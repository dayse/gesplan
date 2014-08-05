 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

public class Numeric {
	

	/**
	 * Dados dois numeros double, faz a divisao dos mesmos.
	 * Depois soma a variavel aproximacao (ex: 0.999 se quiser arredondamento ate 3 casas decimais)
	 * Retorna o proprio resultado da divisao, caso nao haja parte fracionaria. Caso contrario
	 * retorna o proximo inteiro maior(ou para o proximo inteiro mais negativo, caso seja negativo). 
	 * 
	 * Caso o numerador ou denominador sejam 0, ele retorna 0
	 * 
	 * Ex:
	 * Dadas a producao e o tamanho do lote, retorna o numero de lotes arredondando
	 *para o proximo inteiro no caso de haver decimais, ou considerando o proprio
 	 *resultado da divisao quando este for divisivel pelo tamanho do lote
	 * @param numerador
	 * @param denominador
	 * @param aproximacao
	 * @return
	 */
	public static int resultadoDivisaoInteira(double numerador, double denominador, double aproximacao){
		if(denominador == 0)
			return 0;
			
		double resp = (numerador / denominador);
		
		if(numerador != 0)
			if(resp < 0)
				resp -= aproximacao;
			else
				resp += aproximacao;
		
		
		return (int)(resp);
	}
	/**
	 * Dados dois numeros double, faz a divisao dos mesmos.
	 * Depois soma a variavel aproximacao (ex: 0.999 se quiser arredondamento ate 3 casas decimais)
	 * Retorna o proprio resultado da divisao, caso nao haja parte fracionaria. Caso contrario
	 * retorna o proximo inteiro maior(ou para o proximo inteiro mais negativo, caso seja negativo). 
	 * 
	 * Caso o numerador ou denominador sejam 0, ele retorna 0
	 * 
	 * 
	 * Utiliza o valor de aproximacao = 0.999
	 * 
	 * Ex:
	 * Dadas a producao e o tamanho do lote, retorna o numero de lotes arredondando
	 *para o proximo inteiro no caso de haver decimais, ou considerando o proprio
 	 *resultado da divisao quando este for divisivel pelo tamanho do lote
	 * @param numerador
	 * @param denominador
	 * @param aproximacao
	 * @return
	 */
	public static int resultadoDivisaoInteira(double numerador, double denominador){

		return resultadoDivisaoInteira(numerador, denominador, 0.999);
	}
	
	

	/**
	 * Dados dois numeros double, faz a multiplicacao dos mesmos.
	 * Depois soma a variavel aproximacao (ex: 0.999 se quiser arredondamento ate 3 casas decimais)
	 * Retorna o proprio resultado da multiplicacao, caso nao haja parte fracionaria. Caso contrario
	 * retorna o proximo inteiro maior(ou para o proximo inteiro mais negativo, caso seja negativo). 
	 * 
	 * Caso o x ou y sejam 0, ele retorna 0
	 * 
	 * Ex:
	 * Dadas a producaodiariaEmLotes e o NumDiasUteis, retorna a producao em lotes arredondando
     * para o proximo inteiro no caso de haver decimais, ou considerando o proprio
 	 * resultado da multiplicacao em caso contrario
	 * @param x
	 * @param y
	 * @param aproximacao
	 * @return
	 */
	public static int resultadoMultiplicacaoInteira(double x, double y, double aproximacao){
		if(x == 0 || y == 0)
			return 0;
			
		double resp = (x * y);
		

			if(resp < 0)
				resp -= aproximacao;
			else
				resp += aproximacao;
		
		
		return (int)(resp);
	}
	/**
	 * Dados dois numeros double, faz a multiplicacao dos mesmos.
	 * Depois soma a variavel aproximacao (ex: 0.999 se quiser arredondamento ate 3 casas decimais)
	 * Retorna o proprio resultado da multiplicacao, caso nao haja parte fracionaria. Caso contrario
	 * retorna o proximo inteiro maior(ou para o proximo inteiro mais negativo, caso seja negativo). 
	 * 
	 * Caso o x ou y sejam 0, ele retorna 0
	 * 
	 * Utiliza o valor de aproximacao = 0.999
	 * 
	 * Ex:
	 * Dadas a producaodiariaEmLotes e o NumDiasUteis, retorna a producao em lotes arredondando
     * para o proximo inteiro no caso de haver decimais, ou considerando o proprio
 	 * resultado da multiplicacao em caso contrario
	 * @param x
	 * @param y
	 * @param aproximacao
	 * @return
	 */
	public static int resultadoMultiplicacaoInteira(double x, double y){

		return resultadoMultiplicacaoInteira(x, y, 0.999);
	}
	
}
