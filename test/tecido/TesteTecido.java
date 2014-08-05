 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package tecido;

import modelo.Tecido;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.TecidoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

import com.google.gson.Gson;

public class TesteTecido {
private static TecidoAppService tecidoService;
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void incluitecido() throws AplicacaoException{
		
		Tecido tecido = new Tecido();
		
		tecido.setCodTecido("1");
		tecido.setDescrTecido("tecido6");
		
		tecidoService.inclui(tecido);
		
	}
	
	@Test
	public void testeGrafico() throws AplicacaoException{
		Gson gson = new Gson();
		Tecido tecido = new Tecido();
		
		tecido.setCodTecido("1");
		tecido.setDescrTecido("tecido6");
		String json = gson.toJson(tecido, tecido.getClass());
		System.out.println(json);
	}
}
