 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.List;
import java.util.Set;

import modelo.CadPlan;
import modelo.HP;
import modelo.Parametros;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import service.HPAppService;
import service.ParametrosAppService;
import service.PerioPMAppService;
import service.PlPerModAppService;
import service.PlanoModeloAppService;
import service.CadPlanAppService;
import service.PMPAppService;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.PlPerModDAO;
import DAO.PlanoModeloDAO;
import DAO.Impl.PlPerModDAOImpl;
import DAO.Impl.PlanoModeloDAOImpl;
import DAO.controle.FabricaDeDao;
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
 * Essa Carga:
 * Seleciona o cadPlan de código "1" como sendo plano corrente.
 * Altera alguns periodos especificos para que o recebimento de producao(Producao Model)
 * destes seja igual 0, apenas do primeiro modelo.
 *
 * @author felipe.arruda
 */
public class CargaEstudoAlteraRecebimentoProducao extends CargaBase{
	private static PlanoModeloDAO planoModeloDAO;
	private static PlPerModDAO plPerModDAO;
	private static PlanoModeloAppService planoModeloService;
	private PlPerModAppService plPerModService;
	private static PerioPMAppService perioPMService;
	private static HPAppService hpService;

	
	  public CargaEstudoAlteraRecebimentoProducao(){
		
		 try {
			 plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
			 planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
				
			 planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
				plPerModDAO = FabricaDeDao.getDao(PlPerModDAOImpl.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
		}

		@Override
		public boolean executar() throws AplicacaoException {
			this.alteraRecebimentos();
			return true;
		}

		/**
		 * Recupera o plano modelo referente ao primeiro modelo do cadplan 1
		 * em seguida percorre todos os periodos do hp e recupera os plPerMods
		 * referentes ao periodo e ao planoModelo.
		 * Em seguida chama o método para alterar o plPerMod em questao.
		 * 
		 * @throws ObjetoNaoEncontradoException 
		 */
		public void alteraRecebimentos() throws AplicacaoException{

			String codPlan = "1";
			String codModelo = "121131";
			try{
				PlanoModelo planoModelo = planoModeloDAO.
										recuperarPlanoModeloPorCodigoModeloECodigoCadPlan(codPlan, codModelo);
				
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				HP hp = hpService.recuperaListaDeHP().get(0);
				List<PerioPM> intervaloDePeriodoDoHP = perioPMService.recuperaIntervaloDePerioPMs
																(1, hp.getPerioPMFinalPMP().getPeriodoPM());
				for(PerioPM perioPM : intervaloDePeriodoDoHP){
					//altera  apenas recebimento dos periodos 3, 7, 8 e 10
					int periodo =perioPM.getPeriodoPM();
					if(periodo == 3 || periodo == 7 || periodo == 8 || periodo == 10 ){
						PlPerMod plPerModCorrente = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
																	(planoModelo, perioPM);
						alteraRecebimentoEmPlPerMod(plPerModCorrente);
					}
				}
			}
			catch(ObjetoNaoEncontradoException e){
				throw new AplicacaoException(e);
			}
			
		}
		
		/**
		 * Altera o recebimento para se igual a 0 no dado plPerMod
		 * E em seguida recalcula parametros baseado na producao em pecas.
		 * 
		 * @param plPerMod
		 */
		public void alteraRecebimentoEmPlPerMod(PlPerMod plPerMod){
			plPerMod.setProducaoModel(0.0);
			plPerModService.altera(plPerMod);
			
			//depois de alterar o plpermod devemos chamar o mesmo metodo que é chamado
			//na tela quando fazemos essa operacao.
			plPerModService.recalculaParametrosBaseadoProdEmPecas(plPerMod);			
		}
		

	}
