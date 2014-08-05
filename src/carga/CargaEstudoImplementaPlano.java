 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.Set;

import modelo.CadPlan;
import modelo.Parametros;
import modelo.PlanoModelo;
import service.ParametrosAppService;
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
 * 
 * 
 * Essa Carga:
 * Seleciona o cadPlan de código "1" como sendo plano corrente.
 * Faz com que todos os modelos fiquem como planejados e implementa o plano.
 *
 * @author bruno.oliveira e felipe.arruda
 */
public class CargaEstudoImplementaPlano extends CargaBase{
	private static ParametrosAppService parametrosService;
	private static PlanoModeloAppService planoModeloService;
	private static CadPlanAppService cadPlanService;
	private static PMPAppService pmpService;	

	private CadPlan cadPlanCorrente;
	
	  public CargaEstudoImplementaPlano(){
		
		 try {
			 parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
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
		 * Planeja todos os modelos, depois implementa o plano
		 * 
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.planejarTodosModelos();
			this.implementaPlano();
			return true;
		}
	
		/**
		 * Planeja todos os modelos do cadPlanCorrente
		 */		
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
		}
		
		/**
		 * Implementa o cadPlanCorrente
		 */
		public void implementaPlano() throws AplicacaoException{
			
			try {
				cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo("1");
				pmpService.implementaPlanoMestre(cadPlanCorrente);
			} catch (ObjetoNaoEncontradoException e){
				throw new AplicacaoException(e);
			}
		}
		

	}
