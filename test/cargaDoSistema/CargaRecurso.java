 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.util.ArrayList;
import java.util.List;

import modelo.Recurso;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.RecursoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaRecurso {
  

	private static RecursoAppService recursoService;
	
	
	@BeforeClass
	public void setupClass(){
		
		try{
			
			
			recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
		}catch(Exception e){
			
			e.printStackTrace();
			
		 }
			
	  }
		
		@Test//(groups="inclusao")
		public void incluirRecursos() throws AplicacaoException{
			
			
			
		//------------------- RECURSO 1 ----------------------//
		Recurso recurso1 = new Recurso();
		recurso1.setCodRecurso("01");
		recurso1.setDescrRecurso("botao");
		recurso1.setUM("pc");
		recurso1.setCustoUnit(10);

		//------------------- RECURSO 2 ----------------------//
		Recurso recurso2 = new Recurso();
		recurso2.setCodRecurso("02");
		recurso2.setDescrRecurso("linha");
		recurso2.setUM("mt");
		recurso2.setCustoUnit(10);

		//------------------- RECURSO 3 ----------------------//
		Recurso recurso3 = new Recurso();
		recurso3.setCodRecurso("03");
		recurso3.setDescrRecurso("ziper");
		recurso3.setUM("pc");
		recurso3.setCustoUnit(10);
		
		//-------------- LISTA DE RECURSOS -------------------//
		List<Recurso> recursos = new ArrayList<Recurso>();
		
		recursos.add(recurso1);
		recursos.add(recurso2);
		recursos.add(recurso3);
		
		//-------------- INCLUIR RECURSOS --------------------//
		
		for (Recurso recurso : recursos) {
			
			recursoService.incluiComCriticas(recurso);
			
		}
		

	}
	
}
