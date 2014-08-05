 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package recurso;

import java.util.List;

import modelo.Recurso;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.RecursoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class TesteRecurso {
	
	// Services
	private static RecursoAppService recursoAppService;
	
	// Componentes de Controle
	List<Recurso> recursos;
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			recursoAppService = FabricaDeAppService.getAppService(RecursoAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(dependsOnGroups="recuperador")
	public void listarRecursos() throws AplicacaoException{
		
		System.out.println("RECURSOS = " + recursos);
	}
	
	@Test(groups="inclusao", dependsOnGroups="exclusao")
	public void incluirRecursos() throws AplicacaoException{
		
		Recurso recurso = new Recurso();
		Recurso recurso2 = new Recurso();
		Recurso recurso3 = new Recurso();
		Recurso recurso4 = new Recurso();
		
		recurso.setCodRecurso("1");
		recurso.setCustoUnit(1.5);
		recurso.setDescrRecurso("linha");
		recurso.setUM("m");
		
		recurso2.setCodRecurso("11");
		recurso2.setCustoUnit(1.9);
		recurso2.setDescrRecurso("botao");
		recurso2.setUM("m");
		
		recurso3.setCodRecurso("2");
		recurso3.setCustoUnit(9.1);
		recurso3.setDescrRecurso("ziper");
		recurso3.setUM("m");
		
		recurso4.setCodRecurso("11111");
		recurso4.setCustoUnit(8.4);
		recurso4.setDescrRecurso("copo");
		recurso4.setUM("km");
		
		recursoAppService.inclui(recurso);
		recursoAppService.inclui(recurso2);
		recursoAppService.inclui(recurso3);
		recursoAppService.inclui(recurso4);
	}
	
	@Test(groups="recuperador", dependsOnGroups="inclusao")
	public void recuperarRecursoPorCodigoLike(){
		
		recursos = recursoAppService.recuperaListaDeRecursosPeloCodigoLike("1");
	}
	
	@Test(groups="exclusao")
	public void excluiRecurso() throws AplicacaoException{
		
		recursos = recursoAppService.recuperaListaDeRecursos();
		
		for (Recurso recurso : recursos) {
			recursoAppService.exclui(recurso);
		}
	}
}
