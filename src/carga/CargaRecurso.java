 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.ArrayList;
import java.util.List;

import modelo.Recurso;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.RecursoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

/**
 * 
 * Sobre a Carga:
 * É uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o método executar().
 * Nesse método executar é que é chamado os outros métodos que são
 * as etápas dessa carga.
 * Portanto se é necessario rodar um método depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no método executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas é preciso retornar true.
 * Se houver algum problema(exceção) na execução de um das etapas, essa exceção deve ser lancada
 * 
 * Essa Carga:
 * Inclui modelos
 * 
 * @author felipe.arruda
 *
 */
public class CargaRecurso extends CargaBase{
  

	private static RecursoAppService recursoService;
	
	
	public CargaRecurso(){
		
		try{
			
			
			recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
		}catch(Exception e){
			
			e.printStackTrace();
			
		 }
			
	  }
		
		/**
		 * Executa a inclusao de Recursos.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluirRecursos();
			return true;
		}
		
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
