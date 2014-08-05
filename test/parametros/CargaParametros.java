 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package parametros;

import modelo.Parametros;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.ParametrosAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaParametros {
	
	// Services
	private static ParametrosAppService parametroService;
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			parametroService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Inclusao dos valores default para os campos da tabela de parametros
	 * @throws AplicacaoException
	 */
	@Test
	public void incluirParametros() throws AplicacaoException{
		
		Parametros parametro = new Parametros();
		
		parametro.setInicPlanejamento(false);
		parametro.setMargemSeguranca(97.0);
		parametro.setNumIntervalosFixos(0);
		parametro.setPercentualDePerda(5);
		
		parametroService.inclui(parametro);
	}

}
