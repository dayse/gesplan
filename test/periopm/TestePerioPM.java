 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package periopm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.CadPlan;
import modelo.DeModPer;
import modelo.HP;
import modelo.Parametros;
import modelo.PerioPAP;
import modelo.PerioPM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.CadPlanDAO;
import DAO.DeModPerDAO;
import DAO.HPDAO;
import DAO.ParametrosDAO;
import DAO.PerioPAPDAO;
import DAO.PerioPMDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import service.HPAppService;
import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class TestePerioPM {
	
	// Services
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	private static HPAppService hpService;
	
	// DAOs
	private static ParametrosDAO parametrosDAO;
	private static CadPlanDAO cadPlanDAO;
	private static DeModPerDAO deModPerDAO;
	private static HPDAO hpDAO;
	private static PerioPMDAO perioPMDAO;
	private static PerioPAPDAO perioPAPDAO;
	
	// Constantes
	final static int EXCLUSAO_PRIMEIRO_PERIODO = 0;
	final static int EXCLUSAO_ULTIMO_PERIODO = 1;
	
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			perioPAPDAO = FabricaDeDao.getDao(PerioPAPDAOImpl.class);
			
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
		} catch (Exception e) {
		}
	}
	
	//@Test
	public void listarPeriodosOrdenadamente(){
		
		List<PerioPM> periodos = perioPMService.recuperaListaDePerioPMs();
		
		// Metodo da classe Collections que ordena uma lista baseado no metodo compareTo() do Objeto.
		Collections.sort(periodos);	 	
		
		for (PerioPM perioPM : periodos) {
			System.out.println("PERIODO[" + perioPM.getPeriodoPM() + "] - Num. Dias U2 = " + perioPM.getNumDiasUteisU2());
		}
	}
	
	//  ESTE METODO NAO SERA NECESSARIO NO SERVICE DE PERIOPM. USADO APENAS AQUI NO TESTE PARA EXPLICITAR 
	// QUAL O TIPO DE EXCLUSAO ESTA SENDO UTILIZADA.
	@Test 
	public void excluirPeriodo() throws AplicacaoException{
		excluirPrimeiroOuUltimo(EXCLUSAO_ULTIMO_PERIODO);
	}	
	
	public void excluirPrimeiroOuUltimo(int tipoExclusao) throws AplicacaoException{
		
		if (!cadPlanDAO.recuperaListaDeCadPlan().isEmpty()){
			throw new AplicacaoException("cadplan.PLANOS_CADASTRADOS");
		}
		
		List<PerioPM> periodos = perioPMService.recuperaListaDePerioPMs();
		
		int indiceInicial;
		int indiceFinal;
		
		if (tipoExclusao == EXCLUSAO_PRIMEIRO_PERIODO){
			
			PerioPM primeiroPeriodo = periodos.get(0);
			
			indiceInicial = periodos.indexOf(primeiroPeriodo) + 1;
			indiceFinal = periodos.size() - 1; 
			
			if (indiceInicial > indiceFinal){
				indiceInicial = 0;	// Significa que a lista de Periodos possui apenas 1 elemento
			}
			
			List<PerioPM> periodosSubsequentes = perioPMService.recuperaIntervaloDePerioPMs
				(periodos.get(indiceInicial).getPeriodoPM(), periodos.get(indiceFinal).getPeriodoPM());
			
			perioPMService.exclui(primeiroPeriodo);
			
			atualizarPeriodosRestantes(periodosSubsequentes, EXCLUSAO_PRIMEIRO_PERIODO);
			
		} else {
			
			indiceInicial = periodos.indexOf(periodos.get(0));
			indiceFinal = periodos.indexOf(periodos.get(periodos.size() - 1)) - 1;	// Indice anterior ao indice do ultimo periodo da lista
			
			PerioPM ultimoPeriodo = periodos.get(periodos.size() - 1);
			
			if (indiceFinal < 0){
				indiceFinal = 0;  // Significa que a lista de Periodos possui apenas 1 elemento
			}	
			
			perioPMService.exclui(ultimoPeriodo);
			
			List<PerioPM> periodosRestantes = perioPMService.recuperaIntervaloDePerioPMs
				(periodos.get(indiceInicial).getPeriodoPM(), periodos.get(indiceFinal).getPeriodoPM());
			
			atualizarPeriodosRestantes(periodosRestantes, EXCLUSAO_ULTIMO_PERIODO);
		}
		
		atualizarEntidadesDependentes();
	}
	
	public void atualizarPeriodosRestantes(List<PerioPM> periodosRestantes, int tipoExclusao){
		
		List<PerioPAP> perioPAPAntigos = perioPAPService.recuperaListaDePerioPAPs();
		
		for (int i = 1; i <= periodosRestantes.size(); i++) {
			
			PerioPM perioPMAtual = null;
			
			try {
				perioPMAtual = perioPMDAO.getPorId(((periodosRestantes.get(i - 1).getId())));
			} catch (ObjetoNaoEncontradoException ex) {
			}
			
			if (tipoExclusao == EXCLUSAO_PRIMEIRO_PERIODO){
				
				// RENUMERACAO DOS REGISTROS DE PerioPM 
				perioPMAtual.setPeriodoPM(perioPMAtual.getPeriodoPM() - 1);
			}
			
			if (i <= perioPAPAntigos.size()){
				
				PerioPAP perioPAPAtual = null;
				
				try {
					perioPAPAtual = perioPAPDAO.getPorId((perioPAPAntigos.get(i - 1).getId()));
				} catch (ObjetoNaoEncontradoException e) {
				}
				
				// OS PerioPAPs ANTIGOS VAO SENDO LIGADOS DE FORMA 1 <--> 1 COM OS PerioPMs
				perioPMAtual.setPerioPAP(perioPAPAtual);
				
			} else {
				
				// MODULO QUE ADICIONA NOVOS PerioPAPs PARA CASAR COM OS PerioPMs RESTANTES  
				PerioPAP novoPerioPAP = new PerioPAP();
				novoPerioPAP.setPeriodoPAP(perioPMAtual.getPeriodoPM());
				
				try {
					perioPAPService.inclui(novoPerioPAP);
				} catch (AplicacaoException e) {
				}
				
				perioPMAtual.setPerioPAP(novoPerioPAP);
			}
			
			perioPMService.altera(perioPMAtual);
		}
	}
	
	public void atualizarEntidadesDependentes() throws AplicacaoException {
		
		// 1) Setar o parametro 'InicPlanejamento' como FALSE
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		parametro.setInicPlanejamento(false);
		parametrosDAO.altera(parametro);
		
		// 2) ATUALIZAR OS REGISTROS DE DeFamPer (PROCESSO ANALOGO AO FEITO EM DeModPer ABAIXO)
		//
		//    ---------> Ainda nao implementado, pois aguarda a conclusao de DeFamPer <---------
		
		
		// 3) ATUALIZAR OS REGISTROS DE DeModPer 
		
			// a) Excluir todos os registros de DeModPer cadastrados
			List<DeModPer> demandas = deModPerDAO.recuperaListaDeDemandaModeloPeriodo();
			for (DeModPer deModPer : demandas) {
				deModPerDAO.exclui(deModPer);
			}
			
		// 4) Excluir o HP cadastrado no Sistema
			
			List<HP> hpBD = hpDAO.recuperaListaDeHP();
			if (!hpBD.isEmpty()){
				HP hp = hpBD.get(0);
				hpService.exclui(hp);
			}
			
		
		// 5) Excluir todos os registros de CadPlan do sistema
		List<CadPlan> planos = cadPlanDAO.recuperaListaDeCadPlan();
		for (CadPlan plano : planos) {
			cadPlanDAO.exclui(plano);
		}
	}
}
