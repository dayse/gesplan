 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.ModeloAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class TesteModelo {
  
	public ModeloAppService modeloService;
	
	@BeforeClass
	public void setupClass(){
		try {
			
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void listarModelos() {
		
		List<Modelo> modelos = null;
		
		try {
			modelos = modeloService.recuperaListaDeModelos();
		} catch (AplicacaoException e) {
		}
		
		System.out.println("MODELOS = " + modelos);
	}

}
