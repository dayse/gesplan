 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

import java.util.Set;

import modelo.CadPlan;
import modelo.PlanoModelo;
import service.PlanoModeloAppService;
import service.CadPlanAppService;
import service.PMPAppService;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.exception.ObjetoNaoEncontradoException;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * 
 * @author bruno.oliveira
 * Seleciona o cadPlan de código "1" como sendo plano corrente.
 * Faz com que todos os modelos fiquem como planejados e implementa o plano.
 *
 */
public class EstudoImplementaPlano {
	
	private static PlanoModeloAppService planoModeloService;
	private static CadPlanAppService cadPlanService;
	private static PMPAppService pmpService;	

	private CadPlan cadPlanCorrente;
	@BeforeClass
	  public void setupClass(){
		
		 try {
			 planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			 cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			 pmpService = FabricaDeAppService.getAppService(PMPAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
			try {
				cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo("1");
			} catch (ObjetoNaoEncontradoException e) {
				e.printStackTrace();
			}
		}
		
	
		/**
		 * Planeja todos os modelos do cadPlanCorrente
		 */		
		@Test
		public void planejarTodosModelos(){

			Set<PlanoModelo> planosModelo = cadPlanCorrente.getPlanosModelo();
			
			for (PlanoModelo planoModelo : planosModelo) {
				
				if (planoModelo.isModeloPlanejado()){
					planoModelo.setModeloPlanejado(false);
				} else {
					planoModelo.setModeloPlanejado(true);
				}
				
				planoModeloService.altera(planoModelo);
			}			
			System.out.println(">>>>planejarTodosModelos");
		}
		
		/**
		 * Implementa o cadPlanCorrente
		 */
		@Test(dependsOnMethods="planejarTodosModelos")
		public void implementaPlano(){
			
			try {
				cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo("1");
				pmpService.implementaPlanoMestre(cadPlanCorrente);
			} catch (AplicacaoException e) {
				e.printStackTrace();
			} catch (ObjetoNaoEncontradoException e){
				e.printStackTrace();
			}
			System.out.println(">>>>implementaPlano");
		}
		

	}
