 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.jdbc2.EscapedFunctions;

import modelo.CadPlan;
import modelo.CapacDia;
import modelo.CapacRec;
import modelo.DeFamPer;
import modelo.DeModPer;
import modelo.HP;
import modelo.Parametros;
import modelo.PerioPAP;
import modelo.PerioPM;

import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;


import DAO.CadPlanDAO;
import DAO.CapacDiaDAO;
import DAO.CapacRecDAO;
import DAO.DeFamPerDAO;
import DAO.DeModPerDAO;
import DAO.HPDAO;
import DAO.ParametrosDAO;
import DAO.PerioPAPDAO;
import DAO.PerioPMDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.CapacDiaDAOImpl;
import DAO.Impl.CapacRecDAOImpl;
import DAO.Impl.DeFamPerDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * 
 * As classes de serviço é que possuem as regras de negócio. Fazem as críticas
 * quando necessário e chamam as classes DAO para pegar as informações do BD.
 * 
 * São essas classes AppService que fazem o controle de transaçao, ou seja quem
 * abre a transaçao, "quem commita" através do interceptador de appservice. Aqui
 * defino se o metodo é transacional ou não e em funçao desta informaçao o
 * interceptador vai usar ou nao transacao.
 * 
 * 
 * Esta classe é originaria de jpa8 - DAO Generico familia e adaptada com
 * tratamento de excecao feito em EmpregadoAppService de jsf4b para o metodo que
 * recupera todas as familias
 * 
 * @author daysemou
 * 
 */
public class PerioPMAppService {
	
	// DAOs
	private static PerioPMDAO perioPMDAO;
	private static HPDAO hpDAO;
	private static ParametrosDAO parametrosDAO;
	private static PerioPAPDAO perioPAPDAO;
	private static DeModPerDAO deModPerDAO;
	private static DeFamPerDAO deFamPerDAO;
	private static CadPlanDAO cadPlanDAO;
	private static CapacDiaDAO capacDiaDAO;
	private static CapacRecDAO capacRecDAO;
	
	// Services
	private static HPAppService hpService;
	
	// Constantes
	final static int EXCLUSAO_PRIMEIRO_PERIODO = 0;
	final static int EXCLUSAO_ULTIMO_PERIODO = 1;
	final static int EXCLUSAO_TODOS_PERIODOS = 2;
	
	public PerioPMAppService() { 
		
		try {
			
			// DAOs
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			capacDiaDAO = FabricaDeDao.getDao(CapacDiaDAOImpl.class);
			capacRecDAO = FabricaDeDao.getDao(CapacRecDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			perioPAPDAO = FabricaDeDao.getDao(PerioPAPDAOImpl.class);
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			deFamPerDAO = FabricaDeDao.getDao(DeFamPerDAOImpl.class);
			
			// Services
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Inclui um objeto do tipo PerioPM, mas antes verifica se ja existe um periodo com este numero.
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(PerioPM perioPM) throws AplicacaoException {
		
		try {
			
			// Setar o parametro 'InicPlanejamento' como FALSE
			Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
			parametro.setInicPlanejamento(false);
			parametrosDAO.altera(parametro);
			
			// Se encontrou um PerioPM, é porque já existia alguem cadastrado c/ esse codigo.
			//
			perioPMDAO.recuperaPerioPMPorPeriodoPM(perioPM.getPeriodoPM());
			
			throw new AplicacaoException("perioPM.PERIODO_EXISTENTE");
			
		} catch (ObjetoNaoEncontradoException e) {			
		}
		
		perioPMDAO.inclui(perioPM);
	}

	@Transacional
	public void altera(PerioPM umPerioPM) {
		perioPMDAO.altera(umPerioPM);
	}

	@Transacional
	public void exclui(PerioPM perioPM) throws AplicacaoException {
		
		PerioPM perioPMBD = null;
		
		try {
			perioPMBD = perioPMDAO.getPorIdComLock(perioPM.getId());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		perioPMDAO.exclui(perioPMBD);
	}
	/**
	 * Regras gerais que diferenciam os 3 tipos de exclusao de um perioPM:
	 * Excluir Primeiro
	 * Excluir Ultimo
	 * Excluir Todos
	 * Para cada um dos casos existe regras expecificas de exclusao, entretanto, ambos os casos
	 * chamam o metodo atualizarEntidadesDependentes
	 * @param tipoExclusao
	 * @throws AplicacaoException
	 * @author walanem e felipe.arruda
	 */
	@Transacional
	public void excluirPrimeiroUltimoOuTodos(int tipoExclusao) throws AplicacaoException{
		/*
		if (!cadPlanDAO.recuperaListaDeCadPlan().isEmpty()){
			throw new AplicacaoException("cadplan.PLANOS_CADASTRADOS");
		}
		*/
		List<PerioPM> periodos = perioPMDAO.recuperaListaDePerioPMs();
		
		int indiceInicial;
		int indiceFinal;
		
		if (tipoExclusao == EXCLUSAO_PRIMEIRO_PERIODO){
			
			PerioPM primeiroPeriodo = periodos.get(0);
			
			indiceInicial = 1;
			indiceFinal = periodos.size() - 1; 
			
			if (indiceInicial == periodos.size()){
				indiceInicial = 0;	// Significa que a lista de Periodos possui apenas 1 elemento
			}
			
			List<PerioPM> periodosSubsequentes = perioPMDAO.recuperaIntervaloDePerioPMs
				(periodos.get(indiceInicial).getPeriodoPM(), periodos.get(indiceFinal).getPeriodoPM());
			
			this.atualizarEntidadesDependentes(EXCLUSAO_PRIMEIRO_PERIODO);
			
			this.exclui(primeiroPeriodo);
			
			if (indiceInicial != 0) {	// Só rearruma os periodos subsequentes se o Período excluído não era o unico da lista
				atualizarPeriodosRestantes(periodosSubsequentes, EXCLUSAO_PRIMEIRO_PERIODO);
			}
			
			
			
		} else if (tipoExclusao == EXCLUSAO_ULTIMO_PERIODO){
			
			indiceInicial = 0;
			indiceFinal = periodos.indexOf(periodos.get(periodos.size() - 1)) - 1;	// Indice anterior ao indice do ultimo periodo da lista
			
			PerioPM ultimoPeriodo = periodos.get(periodos.size() - 1);
			
			if (indiceFinal < 0){
				indiceFinal = 0;  // Significa que a lista de Periodos possui apenas 1 elemento
			}	
			
			this.atualizarEntidadesDependentes(EXCLUSAO_ULTIMO_PERIODO);
			
			this.exclui(ultimoPeriodo);
			
			List<PerioPM> periodosRestantes = perioPMDAO.recuperaIntervaloDePerioPMs
				(periodos.get(indiceInicial).getPeriodoPM(), periodos.get(indiceFinal).getPeriodoPM());
			
			this.atualizarPeriodosRestantes(periodosRestantes, EXCLUSAO_ULTIMO_PERIODO);
		
		} else {
			
			// Se nao é nenhuma das opções anteriores, então trata-se da opção de exclusao de TODOS os periodos.
			
				//a) Atualizamos todas as entidades dependentes
				this.atualizarEntidadesDependentes(EXCLUSAO_TODOS_PERIODOS);
				
				// b) Excluimos TODOS os PerioPMs
				for (PerioPM perioPM : periodos) {
					this.exclui(perioPM);
				}
				
				// c) Excluimos TODOS os PerioPAPs
				List<PerioPAP> periodosPAP = perioPAPDAO.recuperaListaDePerioPAPs();
				
				try {
					PerioPAPAppService perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
					
					for (PerioPAP perioPAP : periodosPAP){
						perioPAPService.excluiComDependencias(perioPAP);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
		}
		

	}
	/**
	 * Metodo responsavel por reorganizar uma lista de periodos restantes
	 * utiliza o tipo de exclusao que foi feito para poder organizar de 
	 * maneira eficiente(usa diferentes metodos para cada caso de exclusao)
	 * a relacao de PerioPM com PerioPAP, para ficar de 1x1.
	 * Se for o caso de ja existir uma relacao 1x1 de PerioPM com PerioPAP no momento da exclusao
	 * do perioPM(tanto primeiro quando o ultimo) sera excluido o ultimo perioPAP, enquanto
	 * os outros sao reorganizados para ficar novamente 1x1 com os perioPMs
	 * Por exemplo:
	 * Considerando uma exclusao de primeiro perioPM, de 10 periodos
	 * O perioPM 10 iria ficar ligado ao perioPAP 9, o perioPM 9 iria ficar ligado ao perioPAP 8...
	 * ateh o perioPM 2 aportar para o perioPAP 1, entao o perioPAP 10 que ficaria sobrando,
	 * sem nenhum perioPM ligado a ele, seria excluido.
	 * @param periodosRestantes
	 * @param tipoExclusao
	 * @author walanem e felipe.arruda
	 */
	@Transacional
	public void atualizarPeriodosRestantes(List<PerioPM> periodosRestantes, int tipoExclusao){
		
		List<PerioPAP> perioPAPAntigos = perioPAPDAO.recuperaListaDePerioPAPs();
		int tamanhoListaPeriodosRestantes = periodosRestantes.size();
		
		for (int i = 1; i <= tamanhoListaPeriodosRestantes; i++) {
			
			PerioPM perioPMAtual = null;
			
			try {
				perioPMAtual = perioPMDAO.getPorId(((periodosRestantes.get(i - 1).getId())));
			} catch (ObjetoNaoEncontradoException ex) {
			}
			
			if (tipoExclusao == EXCLUSAO_PRIMEIRO_PERIODO){
				
				// RENUMERACAO DOS REGISTROS de PerioPM 
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
				
				perioPAPDAO.inclui(novoPerioPAP);
				
				perioPMAtual.setPerioPAP(novoPerioPAP);
			}
			
			this.altera(perioPMAtual);
		}
		
		// 	  Se executar este IF, estamos tratando o caso em que os PerioPMs e os PerioPAPs já estavam originalmente ligados
		// de forma 1 <--> 1. Assim, como já excluimos um PerioPM, consequentemente um PerioPAP também deve ser excluído, e
		// este sempre será o último da lista.
		if (tamanhoListaPeriodosRestantes < perioPAPAntigos.size()){
			
			
			
			try {

				
				//   Esta classe está instanciada localmente neste ponto, pois se esta variavel fosse Global, cairimos
				// num problema de "Inicialização Cíclica", já que no construtor desta classe é feita uma chamada para
				// uma instancia de PerioPAPService, que por sua vez, faz uma chamada para instanciar o PerioPMService
				// e assim, cairimos em Deadlock. Logo, por ser uma variavel local, assim que este metodo terminar sua
				// execucao, esta instancia será perdida.
				PerioPAPAppService perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
				
				PerioPAP ultimoPerioPAP = perioPAPDAO.getPorId((perioPAPAntigos.get(perioPAPAntigos.size() - 1)).getId());
				

				perioPAPService.excluiComDependencias(ultimoPerioPAP);
				
			} catch (ObjetoNaoEncontradoException e) {
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	/**
	 * Atualiza as entidades dependentes de perioPM para que o banco de dados
	 * nao fique inconsistente ao excluir um perioPM.
	 * No caso da inclusao de um perioPM nao eh feito nada, pois isso eh tratado no inicializaHP.
	 * Exitem 3 casos distintos, que sao relativos aos 3 tipos de exclusao de perioPM
	 * Caso exclua o primeiro periodo, o ultimo ou todos.
	 * Alem disso tambem tem algumas regras que sao independentes de qualquer caso.
	 * (Ex: Zerar os registros de DeFamPer)
	 * @param tipoExclusao
	 * @throws AplicacaoException
	 */
	@Transacional
	public void atualizarEntidadesDependentes(int tipoExclusao) throws AplicacaoException {
		
		// 1) Setar o parametro 'InicPlanejamento' como FALSE
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		parametro.setInicPlanejamento(false);
		parametrosDAO.altera(parametro);
		
		
		
		//>>>>>>>>>>>>nova versao da exclusao

		
		List<PerioPM> periodos = perioPMDAO.recuperaListaDePerioPMs();
		List<HP> hpBD = hpDAO.recuperaListaDeHP();
		HP hp = null;
		if(!hpBD.isEmpty()){
			hp = hpBD.get(0);
		}
		
		//Prepara o capacRecService
		CapacRecAppService capacRecService = null;
		try {
			capacRecService = FabricaDeAppService
					.getAppService(CapacRecAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Prepara o capacDiaService
		CapacDiaAppService capacDiaService = null;
		try {
			capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Prepara o demodperservice
		DeModPerAppService deModPerService = null;
		try {
			deModPerService = FabricaDeAppService.getAppService(DeModPerAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		

		//recupera lista de deModPers
		List<DeModPer> demandas = deModPerDAO.recuperaListaDeDemandaModeloPeriodo();
		//Recupera lista de capacRecs
		List<CapacRec> listaCapacRec = capacRecDAO.recuperaListaDeCapacRecs();	
		//recupera a lista de capacDias
		List<CapacDia> listaCapacDia = capacDiaDAO.recuperaListaDeCapacDias();
		
		//>>>CASO1:
		//exclui dependencias que estavam relacionados ao primeiro periodo
		if(tipoExclusao == EXCLUSAO_PRIMEIRO_PERIODO){
			PerioPM novoPrimeiroPerioPM = null;
			try {
				novoPrimeiroPerioPM = perioPMDAO.recuperaPerioPMPorPeriodoPM(2);
			} catch (ObjetoNaoEncontradoException e1) {
			}
			
			//>>>>Exclui CapacRecs
			
			//Percorre a lista de capacRecs e exclui todos que estao relacionados ao periodo 1
			//ja que esse foi excluido.
			for(CapacRec capacRec : listaCapacRec){
				if(capacRec.getPerioPM().getPeriodoPM() == 1){
					try {
						capacRecService.exclui(capacRec);
					} catch (Exception e) {
						System.out.println("ERRO NA EXCLUSAO DE CAPACRECS!!!!");
					}
				}
			}


			//>>>>Exclui CapacDia
					
			
			//Percorre a lista de capacDias e exclui todos que estao relacionados ao periodo 1
			//ja que esse foi excluido.
			for(CapacDia capacDia : listaCapacDia){
				if(capacDia.getPerioPM().getPeriodoPM() == 1){
					capacDiaService.exclui(capacDia);
				}
			}

			//>>>>Exclui  DeModPer 
			for (DeModPer deModPer : demandas) {
				if(deModPer.getPeriodo().getPeriodoPM() == 1){
					//Verificar se eh melhor usar o DAO ou o service aqui
					//tenho quase certeza que o correto seria usar o Service!
					deModPerService.exclui(deModPer);
				}
			}


			//DELETAR PlanoFamilia e PlPerFam
			

			
			//>>>> Altera informacoes do HP
			//verificar se nesse momento o primeiro perioPm da lista de
			//perioPMs eh realmente o primeiro ou se eh o que foi deletado.
			
			//se novoPrimeiroPerioPM nao for null e
			//se ele é o perioPMInic do HP foi esse mesmo perioPM entao
			//altera o HP para usar o proximo perioPM
			try{

				if (hp.getPerioPMInicDemMod().getPeriodoPM() == 1) {
					if (novoPrimeiroPerioPM == null){
						hp.setPerioPMFinalDemMod(null);
						hp.setPerioPMFinalPMP(null);						
					}
					hp.setPerioPMInicDemMod(novoPrimeiroPerioPM);
					hp.setPerioPMInicPMP(novoPrimeiroPerioPM);
					
					hpService.altera(hp);
				}
				
			}catch(Exception e){
				
			}
				
			
		}
		
		//>>>CASO2:
		//exclui dependencias que estavam relacionados ao ultimo periodo
		else if(tipoExclusao == EXCLUSAO_ULTIMO_PERIODO){
			PerioPM ultimoPerioPM = null;
			try {
				ultimoPerioPM = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodos.size());
			} catch (ObjetoNaoEncontradoException e1) {
			}
			
			//>>>>Exclui  DeModPer 
			for (DeModPer deModPer : demandas) {
				if(deModPer.getPeriodo().getPeriodoPM() == ultimoPerioPM.getPeriodoPM()){
					//Verificar se eh melhor usar o DAO ou o service aqui
					//tenho quase certeza que o correto seria usar o Service!
					deModPerService.exclui(deModPer);
				}
			}

			//>>>>Exclui CapacRecs
			
			//Percorre a lista de capacRecs e exclui todos que estao relacionados ao periodo 1
			//ja que esse foi excluido.
			for(CapacRec capacRec : listaCapacRec){
				if(capacRec.getPerioPM().getPeriodoPM() == ultimoPerioPM.getPeriodoPM()){
					try {
						capacRecService.exclui(capacRec);
					} catch (Exception e) {
						System.out.println("ERRO NA EXCLUSAO DE CAPACRECS!!!!");
					}
				}
			}

			

			//>>>>Exclui CapacDia
									
			//Percorre a lista de capacDias e exclui todos que estao relacionados ao periodo 1
			//ja que esse foi excluido.
			for(CapacDia capacDia : listaCapacDia){
				if(capacDia.getPerioPM().getPeriodoPM() == ultimoPerioPM.getPeriodoPM()){
					capacDiaService.exclui(capacDia);
				}
			}

			
			

			
			try{
				// verifica se o perioPM excluido eh o periodoFinal do HP
				if (ultimoPerioPM.getPeriodoPM() == hp.getPerioPMFinalPMP().getPeriodoPM()) {

					// >>>> Altera informacoes do HP
					// verificar se nesse momento o ultimo perioPm da lista de
					// perioPMs eh realmente o ultimo ou se eh o que foi
					// deletado.
					PerioPM novoUltimoPerioPM = null;
					try {
						novoUltimoPerioPM = perioPMDAO
								.recuperaPerioPMPorPeriodoPM(periodos.size() - 1);
					} catch (ObjetoNaoEncontradoException e1) {
					}

					if (novoUltimoPerioPM == null){
						hp.setPerioPMInicDemMod(null);
						hp.setPerioPMInicPMP(null);					
					}
					hp.setPerioPMFinalDemMod(novoUltimoPerioPM);
					hp.setPerioPMFinalPMP(novoUltimoPerioPM);

					hpService.altera(hp);
				}
			}catch(Exception e){
				
			}


			
		}
		//>>>>CASO3:
		//atualiza as entidades levando em conta que todos os perioPMs foram excluidos
		else{
			
			//>>>>Exclui CapacRecs
			//Percorre a lista de capacRecs e exclui todos
			for(CapacRec capacRec : listaCapacRec){
				capacRecService.exclui(capacRec);
			}

			//>>>>Exclui CapacDia
			//Percorre a lista de capacDias e exclui todos
			for(CapacDia capacDia : listaCapacDia){
				capacDiaService.exclui(capacDia);
			}
			
			//>>>>Exclui  DeModPer 
			//Percorre a lista de deModPers e exclui todos
			for (DeModPer deModPer : demandas) {
					//Verificar se eh melhor usar o DAO ou o service aqui
					//tenho quase certeza que o correto seria usar o Service!
					deModPerService.exclui(deModPer);
			}

			//>>>>Exclui  HP
			for(HP hpdel : hpBD){
				hpService.exclui(hpdel);
			}
			

			
		}
		
		
		//>>>> Zera DeFamPer

		//recupera lista de deFamPers
		List<DeFamPer> demandasFamilia = deFamPerDAO.recuperaListaDeDemandaFamiliaPerioPAP();
		
		//Zerar TODOS os registros de deFamPer pois fica trocado as informacoes
		//de perioPAP, logo perde a logica de negocio(apesar da logica do banco ser mantida)
		DeFamPerAppService deFamPerService = null;
		try {
			deFamPerService = FabricaDeAppService.getAppService(DeFamPerAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		for (DeFamPer deFamPer : demandasFamilia) {
				
				deFamPer.setPedidosFamilia(0.0);
				deFamPer.setVendasProjetadasFamilia(0.0);
				deFamPerService.altera(deFamPer);
		}
		

		//>>>> Exclui todos os cadPlan e seus respectivos PlanoModelo e PlPerMod
		CadPlanAppService cadPlanService = null;
		try {
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
//		System.out.println("antes do list");
		// 5) Excluir todos os registros de CadPlan do sistema
		//exclui em cascata os planoModelo e PlPerMod relativos a cada cadPlan
		List<CadPlan> planos = cadPlanDAO.recuperaListaDeCadPlan();
//		System.out.println("depois do list");
		for (CadPlan plano : planos) {
//			System.out.println("entrou no for list cadplan");
			cadPlanService.exclui(plano);
//			System.out.println("depois de excluir");
		}
//		System.out.println("depois do for");
		
		//DELETAR PlanoFamilia e PlPerFam

		
			
	}
	
	public PerioPM recuperaPerioPMPorPeriodoPM(int periodoPM) throws AplicacaoException {

		try {
			return perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoPM);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("perioPM.NAO_ENCONTRADO");
		}
	}

	public List<PerioPM> recuperaListaDePerioPMs() {
		return perioPMDAO.recuperaListaDePerioPMs();
	}
	

	public List<PerioPM> recuperaListaPaginadaDePerioPMs() {
		return perioPMDAO.recuperaListaPaginadaDePerioPMs();
	}
	
	/**
	 * Calcula o numero de dias uteis no perioPM dados os perioPM Inicial e final
	 * 
	 * @param periodoInicial
	 * @param periodoFinal
	 * @return double
	 */
	public double calculaTotalDiasUteisIntervalo(PerioPM periodoInicial, PerioPM periodoFinal){
		
		double total = 0.0;
		
		List<PerioPM> periodos = this.recuperaListaDePerioPMs();
		
		//lembrando que sublist utiliza como parametros( int arg, int arg2 -1) entao quando for chamar
		//utilizar no formato( periodoInicial-1, periodofinal)
		List<PerioPM> intervalo = periodos.subList((periodoInicial.getPeriodoPM()-1), (periodoFinal.getPeriodoPM()));
		
		for (PerioPM perioPM : intervalo) {
			total += perioPM.getNumDiasUteisMatriz();
		}
		return total;
	}
	
	
	/**
	 *  Este metodo devolve o Periodo onde deve ser iniciada a produção,
	 *  usando o tempo de reposição para fazer a defasagem.
	 * 
	 *  A lista de retorno não possui tipo explicito, pois são retornados
	 *  <b>vários objetos de tipos distintos</b> na mesma.
	 *  
	 *  <br/>
	 *  Esse recurso foi utilizado pois precisavamos ter 2 parametros como
	 *  retorno da função, entao foi usado um map.
	 *  <br/>
	 *  
	 *  
	 *  <b>Parâmetros de Retorno:</b>
	 *  
	 *  <br/><br/>
	 *  
	 *  0) inicioPMPAntesInicioHP - variavel booleana que indica se o periodo no qual deveria 
	 *  iniciar a produção ocorre antes do inicio do HP (true) <br/>
	 *  1) periodoDefasado    - corresponde ao período de inicio da produção. Quando é zero 
	 *  significa que a produção deveria ter sido feita antes do início do HP   <br/>
	 *  
	 * @param inicioPMPAntesInicioHP
	 * @param periodoRecebimentoPMP
	 * @param periodoDefasado
	 * @param tempoReposicao
	 * @return List
	 */
	public Map verificaPeriodoInicioPMP(boolean inicioPMPAntesInicioHP, int periodoRecebimentoPMP,
														int periodoDefasado, Double tempoReposicao){
		
		Map resultado = new HashMap();
		
		inicioPMPAntesInicioHP = false;
		
		periodoDefasado = periodoDefasado(periodoRecebimentoPMP, tempoReposicao);
		if(periodoDefasado < 1){
			inicioPMPAntesInicioHP = true;
		}
		
		resultado.put("inicioPMPAntesInicioHP" , inicioPMPAntesInicioHP);
		resultado.put("periodoDefasado" , periodoDefasado);
		
		return resultado;
	}

	/** considera que o modelo deve ser entregue pronto no 1º dia do Periodo de Recebimento
	 * ou seja, se TR= 1,2,3,4 ou 5   ; NumDiasUteis = 5 e PeriodoRecebimento = 2 entao
	 * PeriodoInic = 1  (acho que está errado.  Deveria ser PeriodoInic = 2 ou seja, poderia ser
	 * feito no mesmo periodo em que seria recebido pois TR <= NumDiasUteisDoPeriodo
	 * @param periodoRecebimentoPMP
	 * @param tempoReposicao
	 * @return
	 */
	public int periodoDefasado(int periodoRecebimentoPMP, Double tempoReposicao){
		
		int periodoDefasado = 0;
		
		if(tempoReposicao == 0){
			periodoDefasado = periodoRecebimentoPMP;
		}
		else{
//			System.out.println("Entrou no else do periodoDefasado");
			Double numDiasUteisAcumulado = 0.0;
			
			PerioPM perioPM = null;
			
			//pega perioPM anterior ao atual
			try {
				perioPM = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoRecebimentoPMP-1);
			} catch (ObjetoNaoEncontradoException e) {
			//se o periodo atual for =1, entao o anterior nao existe, logo retorna periodoDefasado = 0	
				return periodoDefasado = 0;
			}
			

//			System.out.println("passou pelo try, perioPM="+perioPM.getPeriodoPM());
			
			List<PerioPM> intervaloPerioPMs = recuperaIntervaloDePerioPMs(1, perioPM.getPeriodoPM());
			
			Collections.reverse(intervaloPerioPMs);

//			System.out.println("vai entrar no for");
			for(PerioPM perioPMCorrente : intervaloPerioPMs){
//				System.out.println("perioPM = "+perioPMCorrente.getPeriodoPM());
				
				numDiasUteisAcumulado += perioPMCorrente.getNumDiasUteisMatriz();

//				System.out.println("numdiasAcumulado = "+numDiasUteisAcumulado+" - tempoReposicoa"+tempoReposicao);
				
				if (numDiasUteisAcumulado >= tempoReposicao){
					periodoDefasado = perioPMCorrente.getPeriodoPM();
//					System.out.println("periodoDefasado: "+periodoDefasado);
					break;
					
				}
				
			}			
			
		}

//		System.out.println("retorno de periodoDefasado="+periodoDefasado);
		return periodoDefasado;
	}
	
	/**
	 * Calcula o numero de dias uteis no perioPM dados os perioPM Inicial e final
	 * 
	 * @param periodoInicial
	 * @param periodoFinal
	 * @return double
	 */
	public double TesteSubList(){
		
		double total = 0.0;
		Double a = 5.0;
		Double b = 6.0;
		Double c = 7.0;
		Double d = 8.0;
		
		List<Double> lista=new ArrayList<Double>();
		lista.add(a);
		lista.add(b);
		lista.add(c);
		lista.add(d);
		
		System.out.println(lista.size());
		List<Double> intervalo = lista.subList(3,lista.size());
		
		for (Double dd : intervalo) {
			System.out.println("==="+dd);
			total += dd;
		}
		return total;
	}
	
	public List<PerioPM> recuperaIntervaloDePerioPMs(int periodoInicial, int periodoFinal){
		return perioPMDAO.recuperaIntervaloDePerioPMs(periodoInicial, periodoFinal);
	}
}
