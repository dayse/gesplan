 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.CadPlan;
import modelo.CapacDia;
import modelo.CapacRec;
import modelo.DeFamPer;
import modelo.DeModPer;
import modelo.Familia;
import modelo.HP;
import modelo.Modelo;
import modelo.Parametros;
import modelo.PerioPAP;
import modelo.PerioPM;
import modelo.Recurso;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.CadPlanDAO;
import DAO.CapacDiaDAO;
import DAO.CapacRecDAO;
import DAO.DeFamPerDAO;
import DAO.DeModPerDAO;
import DAO.FamiliaDAO;
import DAO.HPDAO;
import DAO.ModeloDAO;
import DAO.ParametrosDAO;
import DAO.PerioPAPDAO;
import DAO.PerioPMDAO;
import DAO.RecursoDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.CapacDiaDAOImpl;
import DAO.Impl.CapacRecDAOImpl;
import DAO.Impl.DeFamPerDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.FamiliaDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.RecursoDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class HPAppService {
	
	// DAOs
	private static HPDAO hpDAO;
	private static CadPlanDAO cadPlanDAO;
	private static ParametrosDAO parametrosDAO;
	private static DeModPerDAO deModPerDAO;
	private static DeFamPerDAO deFamPerDAO;
	private static ModeloDAO modeloDAO;
	private static FamiliaDAO familiaDAO;
	private static PerioPMDAO perioPMDAO;
	private static PerioPAPDAO perioPAPDAO;
	private static CapacDiaDAO capacDiaDAO;
	private static CapacRecDAO capacRecDAO;
	private static RecursoDAO recursoDAO;

	// Services
	private static ParametrosAppService parametrosService;
	private static CapacRecAppService capacRecService;
	private static CapacDiaAppService capacDiaService;
	private static DeModPerAppService deModPerService;
//	private static DeFamPerAppService deFamPerService;
	
	
	@SuppressWarnings("unchecked")
	public HPAppService() {
		
		try {
			
			// DAO
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			deFamPerDAO = FabricaDeDao.getDao(DeFamPerDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			familiaDAO = FabricaDeDao.getDao(FamiliaDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			perioPAPDAO = FabricaDeDao.getDao(PerioPAPDAOImpl.class);
			capacRecDAO = FabricaDeDao.getDao(CapacRecDAOImpl.class);
			capacDiaDAO = FabricaDeDao.getDao(CapacDiaDAOImpl.class);
			recursoDAO = FabricaDeDao.getDao(RecursoDAOImpl.class);
			
			// Service
			capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			capacRecService = FabricaDeAppService.getAppService(CapacRecAppService.class);
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			deModPerService = FabricaDeAppService.getAppService(DeModPerAppService.class);
//			deFamPerService = FabricaDeAppService.getAppService(DeFamPerAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Esta opcao executa uma serie de procedimentos que visam atualizar os
	 *  arquivos que são guiados pelos Periodos definidos no sistema, com os novos
	 *  periodos modificados pelo usuario. Sao atualizados os arquivos: DeModPer, 
	 *  DeFamPer, CapacDia e CapacRec, e sao excluidos os arquivos CadPlan, PlPerMod 
	 *  e PlPerFam porque ao iniciar um novo ciclo de planejamento nao tem sentido deixar 
	 *  os planos cadastrados usados para gerar os Planos Vigentes no ciclo de planejamento anterior.
	 *  Uma vez acionada esta opcao he alterado o flag InicPlanejamento do arquivo Parametro para Inicializado.
	 *   
	 * 
	 * @throws AplicacaoException
	 * @author felipe.arruda
	 */
	@Transacional
	public void iniciaPlanejamento() throws AplicacaoException {
		
		DeModPer demanda = null;
		// tirar do comentario quando fizer defamper
		DeFamPer deFamPer = null;
		List<PerioPAP> intervaloPeriodosDeFamPer = null;
		List<PerioPM> intervaloPeriodosDeModPer = null;
		List<Modelo> modelos = modeloDAO.recuperaListaDeModelos();
		List<Familia> familias = familiaDAO.recuperaListaDeFamilias();
		
		List<DeModPer> demandas = deModPerDAO.recuperaListaDeDemandaModeloPeriodo();
		//   FAZER!!!!!!!!!!!!!!!!!!!
//		List<DeFamPer> ListaDeDeFamPers = deFamPerDAO.recuperaListaDeDeFamPer();  //Fazer busca no DeFamPer  E TIRAR DE COMENTARIO
		
		List<CadPlan> planos = cadPlanDAO.recuperaListaDeCadPlan();
		List<CapacDia> listaCapacDia = capacDiaDAO.recuperaListaDeCapacDias();
		List<CapacRec> listaCapacRec = capacRecDAO.recuperaListaDeCapacRecs();
		List<Recurso> listaDeRecursos = recursoDAO.recuperaListaDeRecursos();
		
		// 0) Verificar se o Planejamento já se encontra Iniciado
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		
		if (parametro.isInicPlanejamento()){
			throw new AplicacaoException("hp.PLANEJAMENTO_JA_INICIADO");
		}
		
		// 1) Verificar se existem Períodos cadastrados no Sistema
		if (perioPMDAO.recuperaListaDePerioPMs().isEmpty()){
			throw new AplicacaoException("perioPM.PERIODOS_INEXISTENTES");
		}	
		
		// 2) Verificacao relativa a HP
		
		// a) Verificar se existe um HP cadastrado
		List<HP> hpBD = hpDAO.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
	
		// b) Verificar se existem intervalos correspondentes as Demandas (Familia e Modelo)
		HP hp = hpBD.get(0);
		
		if ((hp.getPerioPMInicDemMod() != null) && (hp.getPerioPMFinalDemMod() != null) &&
			(hp.getPerioPAPInicDemFam() != null) && (hp.getPerioPAPFinalDemFam() != null)){
			//monta lista com periopaps relativos ao intervalo do HP
			intervaloPeriodosDeFamPer = perioPAPDAO.recuperaIntervaloDePerioPAPs
				(hp.getPerioPAPInicDemFam().getPeriodoPAP(), hp.getPerioPAPFinalDemFam().getPeriodoPAP());
			
			intervaloPeriodosDeModPer = perioPMDAO.recuperaIntervaloDePerioPMs
				(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());
		} else {
			throw new AplicacaoException("hp.DEMANDAS_NAO_INICIALIZADAS");
		}
		
		//c) Verifica se o intervalo do PMP(Data Inicial do Primeiro Periodo, e Data Final do Ultimo Periodo) é igual ao intervalo do PAP
		//se não for impede o usuario de inicializar o HP
		if (hp.getPerioPMInicPMP().getDataFinal().equals(hp.getPerioPAPFinalPAP())){
			throw new AplicacaoException("hp.INTERVALO_PMP_INCOMPATIVEL_INTERVALO_PAP");
		}
		
		// 3) Setar o parametro 'InicPlanejamento' como TRUE, ou seja Inicializado
		parametro.setInicPlanejamento(true);
		parametrosDAO.altera(parametro);
		
		
		// 4) ATUALIZAR OS REGISTROS DE DeModPer 
		
		//percorre lista de perioPM e verifica se esta faltando
		//algum registro de DeModPer para cada periodo em questao,
		//e inclui um deModPer com valor zerado para o mesmo.
		for (PerioPM perioPM : intervaloPeriodosDeModPer) {
			for (Modelo modelo : modelos) {
				
				//tenta encontrar o deModPer relativo ao periodo e ao modelo em questao,
				//se ele nao existir cria um novo zerado.
				
				try {
					demanda = deModPerDAO.recuperaDeModPerPorPeriodoEModelo(perioPM, modelo);
				} catch (ObjetoNaoEncontradoException e) {
					demanda = new DeModPer(modelo, perioPM);
					deModPerService.inclui(demanda);
				}
			}
		}
		
		// 5) ATUALIZAR OS REGISTROS DE DeFamPer 
		DeFamPerAppService deFamPerService = null;
		try {
			deFamPerService = FabricaDeAppService 
					.getAppService(DeFamPerAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//percorre lista de perioPAP e verifica se esta faltando
		//algum registro de DeFamPer para cada periodo em questao,
		//e inclui um DeFamPer com valor zerado para o mesmo.
		for (PerioPAP perioPAP : intervaloPeriodosDeFamPer) {
			for (Familia familia : familias) {
				
				//tenta encontrar o deFamPer relativo ao periodo e a familia em questao,
				//se ele nao existir cria um novo zerado.
				
				try {
					deFamPer = deFamPerDAO.recuperaDeFamPerPorPerioPAPEFamilia(perioPAP, familia);
				} catch (ObjetoNaoEncontradoException e) {
					deFamPer = new DeFamPer(familia, perioPAP);
//					deFamPerService.inclui(deFamPer);
					//deve totalizar nesse motmento
					deFamPerService.incluiTotalizandoDeModPer(deFamPer);
				}
			}
		}

		
		

		
		// 6) Excluir todos os registros de CadPlan do sistema
		// Exclui em cascata os registros de planoModelo e PlPerMod relativos a cada plano.
		for (CadPlan plano : planos) {
			
			CadPlan cadPlanBD = null;
			
			try {
				 cadPlanBD = cadPlanDAO.getPorId(plano.getId());
			} catch (ObjetoNaoEncontradoException e) {
			}
			
			cadPlanDAO.exclui(cadPlanBD);
		}
		
		// 7) Excluir OS REGISTROS DE PlPerFam
		//
		//    ---------> Ainda nao implementado, pois aguarda a conclusao de PlPerFam <---------
		
		
		//Obtem lista com perioPMs dentro do HP cadastrado para o Plano Mestre
		List<PerioPM> listaPerioPMsHP = 
				perioPMDAO.recuperaIntervaloDePerioPMs(hp.getPerioPMInicPMP().getPeriodoPM(), hp.getPerioPMFinalPMP().getPeriodoPM());
		
		
		
		// 9) ATUALIZAR OS REGISTROS DE CapacRec 

		//percorre lista de periodoPM e verifica se esta faltando
		//algum registro de CapacRec para cada periodo em questao,
		//e inclui um CapacRec com valor zerado para o mesmo.
		CapacRec capacRec = null;
		for (PerioPM perioPM : listaPerioPMsHP) {
			for (Recurso recurso : listaDeRecursos ){
				
				//tenta encontrar o capacRec relativo ao periodo e ao recurso em questao,
				//se ele nao existir cria um novo zerado.
				try {
					capacRec = capacRecDAO.recuperaCapacRecPorRecursoEPerioPM(recurso, perioPM);
				} catch (ObjetoNaoEncontradoException e) {
					capacRec = new CapacRec();
					capacRec.setRecurso(recurso);
					capacRec.setPerioPM(perioPM);
					
					capacRecService.inclui(capacRec);
				}
			}
		}
		
		

		//			
		
		// 10) ATUALIZAR OS REGISTROS DE CapacDia

		//percorre lista de periodoPM e verifica se esta faltando
		//algum registro de CapacDia para cada periodo em questao,
		//e inclui um CapacDia com valor zerado para o mesmo.
		CapacDia capacDia = null;
		for (PerioPM perioPM : listaPerioPMsHP) {
				
				//tenta encontrar o capacDia relativo ao periodo em questao,
				//se ele nao existir cria um novo zerado.
				try {
					capacDia = capacDiaDAO.recuperaCapacDiaPorPerioPM(perioPM);
				} catch (ObjetoNaoEncontradoException e) {
					capacDia = new CapacDia();
					capacDia.setPerioPM(perioPM);
					
					capacDiaService.inclui(capacDia);
				}
		}
			
		
	}

	@Transacional
	public void inclui(HP hp) throws AplicacaoException {
		
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		parametro.setInicPlanejamento(false);
		parametrosDAO.altera(parametro);
		
		hpDAO.inclui(hp);
	}
	
	@Transacional
	public void altera(HP hp) {
		
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		parametro.setInicPlanejamento(false);
		parametrosDAO.altera(parametro);
		
		hpDAO.altera(hp);
	}

	@Transacional
	public void exclui(HP hp) throws AplicacaoException {
		
		HP hpBD = null;
		
		try {
			hpBD = hpDAO.getPorIdComLock(hp.getId());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		hpDAO.exclui(hpBD);
	}
	
	public List<HP> recuperaListaDeHP(){
		return hpDAO.recuperaListaDeHP();
	}
}
