package xfuzzy;

import java.io.File;

import xfuzzy.lang.AggregateMemFunc;
import xfuzzy.lang.MemFunc;
import xfuzzy.lang.Specification;
import xfuzzy.lang.XflParser;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import util.JPAUtil;

/**
 * Classe para testes dos metodos necessários ao funcionamento da biblioteca XFuzzy
 * @author dayse.arruda
 *
 */
public class TesteXfuzzy {
	public static final int NUM_VARIAVEIS_ENTRADA = 2;

	
	/**
	 * executa o mecanismo de inferencia para 8 pares de variáveis de entrada
	 * para testar a compatibilidade dos valores obtidos com aqueles obtidos na versão simplificada do 
	 * algoritmo fuzzy.
	 */
	@Test
	public void testePesado(){

			double vendas[] =
		{3900.00, 3900.00, 4500.00, 200.00, 5432.00, 1990.00, 5340.00, 7392.00};
			double varPercentuals [] =
		{50.00, 20.00, 60.00, 100.00, 80.00, 25.00, 10.00, 35.00};

		for(int i= 0; i < 8; i++){
			System.out.println("");
			System.out.println("===entrada====");
			System.out.println("=venda: "+vendas[i]+" =Percentual: "+ varPercentuals[i]);		
			testeBasicoXFuzzy(vendas[i], varPercentuals[i]);
			
		}
		
		
	}
	/**
	 * Calcula o valor da produção considerando como input um vetor com venda e varPercentual
	 * Executa o mecanismo de inferencia apenas uma vez
	 * 
	 * @param venda
	 * @param varPercentual
	 */
	public void testeBasicoXFuzzy(double venda, double varPercentual){
		double[] entrada = new double[NUM_VARIAVEIS_ENTRADA];
		entrada[0] = venda;
		entrada[1] = varPercentual;
		
		double [] saida = crisp(entrada);
		  
		for(int i = 0; i < saida.length; i++){
			System.out.println("saida :"+ saida[i]);
		}
	}
	
	//executa mecanismo de inferencia, para um dado vetor de entrada
	public double [] crisp(double [] inputValue){
		
		
		String pathXFL = "C:\\gesplan2010WorkComFuzzy\\xFuzzyGesplanConsole\\src\\xfl\\gesplan.xfl";
		File file = new File(pathXFL);
		XflParser parser = new XflParser();		
		Specification spec = parser.parse(file.getAbsolutePath());
		  
		MemFunc result[] = spec.getSystemModule().fuzzyInference(inputValue);

		double [] saida = new double[result.length];
		  
		  for(int i=0; i < saida.length; i++) {
		   double val = 0;
		   if(result[i] instanceof pkg.xfl.mfunc.singleton) {
			    val = ((pkg.xfl.mfunc.singleton) result[i]).get()[0];
			   
		   }
		   else{
			   //faz typecast de result para o tipo AggregateMemFunc
			   AggregateMemFunc amf = (AggregateMemFunc) result[i];
			   val = amf.defuzzify();
		   }
		   
		   saida[i] = val;
		  }	  
		  
		return saida;
	}
	
	
	
}
