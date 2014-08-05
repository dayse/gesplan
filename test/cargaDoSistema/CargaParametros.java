 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.util.GregorianCalendar;

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
			parametroService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Inclusao dos valores default para os campos da tabela de parametros
	 * @throws AplicacaoException
	 */
	
	@Test//(groups="inclusao")
	public void incluirParametros() throws AplicacaoException{
		
		Parametros parametro = new Parametros();

		parametro.setInicPlanejamento(false);
		parametro.setMargemSeguranca(97.0);
		parametro.setNumIntervalosFixos(1);
		parametro.setPercentualDePerda(5);
		parametro.setDataEstqInic(new GregorianCalendar());
		
		parametroService.inclui(parametro);
	}

}
