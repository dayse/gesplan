 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.model.ListDataModel;

import comparator.CadPlanComparatorPorEscoreMedio;
import comparator.PlPerModComparatorPorPerioPM;
import comparator.PlPerModComparatorPorPeriodoInicPMP;
import exception.relatorio.RelatorioException;

import modelo.CadPlan;
import modelo.DeModPer;
import modelo.Familia;
import modelo.HP;
import modelo.Modelo;
import modelo.Parametros;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlPerModAgregado;
import modelo.PlanoMestreDeProducaoPorModeloRelatorio;
import modelo.PlanoModelo;
import modelo.Usuario;
import motorInferencia.MotorInferencia;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.CadPlanDAO;
import DAO.DeModPerDAO;
import DAO.HPDAO;
import DAO.ModeloDAO;
import DAO.ParametrosDAO;
import DAO.PerioPMDAO;
import DAO.PlanoModeloDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.PlanoModeloDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class CadPlanAppService {
	
	// DAOs
	private static CadPlanDAO cadPlanDAO;
	private static ParametrosDAO parametrosDAO;
	private static ModeloDAO modeloDAO;
	private static PerioPMDAO perioPMDAO;
	private static PlanoModeloDAO planoModeloDAO;
	private static DeModPerDAO deModPerDAO;
	private static HPDAO hpDAO;
	
	// Services
	private static PlanoModeloAppService planoModeloService;
	private static ModeloAppService modeloService;
	private static PlPerModAppService plPerModService;
	
	@SuppressWarnings("unchecked")
	public CadPlanAppService() {
		
		try {
			// DAOs
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			
			// Services
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	/**
	 * Inclui cadplan.
	 * Inclui dependencias - PlanoModelos
	 * Inclui PlPerMod com valores iniciais da tabela de Modelo(TUC/Cobertura,estoque inicial)
	 * e a demanda informada em DeModPer.
	 * A partir dos valores iniciais de plpermod executa a equação de conservação de estoques
	 * e atualiza em arquivo os registros de plpermod já recalculados
	 * 
	 * <br/><br/>
	 * 
	 * Metodo equivalente ao DMPlPerMod.incluiPlPerModBaseadoEquacaoConserv da versao em delphi.
	 * 
	 * 
	 * 
	 * @author felipe.arruda
	 * @param cadPlan
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(CadPlan cadPlan) throws AplicacaoException {
		
		List<PlanoModelo> planoModelos = null;
		// verifica se o plano ja esta cadastrado,
		// e se for o caso, inclui cadplan e inclui dependencias relativas a planomodelo 
		
			planoModelos = this.inicioOperacaoInclusao(cadPlan);
			Collections.sort(planoModelos);		// Só para garantir que os PlanoModelo novos vão estar ordenados.

		// 3) Incluir dependencias referentes a tabela PlPerMod
		
		int numeroIntervalosCongelados = parametrosDAO.recuperaListaDeParametros().get(0).getNumIntervalosFixos();
		
		// 	É preciso recuperar o intervalo de HP-PMP, pois é neste intervalo que será baseada a criação de novos
		// registros na tabela PlPerMod
		HP hp = hpDAO.recuperaListaDeHP().get(0);
		
		List<PerioPM> perioPMs = perioPMDAO.recuperaIntervaloDePerioPMs
				(hp.getPerioPMInicPMP().getPeriodoPM(), hp.getPerioPMFinalPMP().getPeriodoPM());
		
		PlPerMod plPerMod = null;
		PerioPM perioPMAtual = null;
		DeModPer deModPer = null;
		boolean flagProducao = false; 
		SortedSet<PlPerMod> plPerMods = null;
		Parametros parametros = parametrosDAO.recuperaListaDeParametros().get(0);

		//Equivalente a percorrer o arquivo de modelo
		//ja que esta ordenado da mesma forma que Modelo
		//Percorrendo todos os modelos de um determinado plano
		for (PlanoModelo planoModelo : planoModelos) {

			//cria um plpermod p/ cada modelo
			plPerMods = new TreeSet<PlPerMod>();

			///Percorre todos os periodos
			for (PerioPM perioPM : perioPMs) {
				
				try {
					perioPMAtual = perioPMDAO.getPorId(perioPM.getId());
				} catch (ObjetoNaoEncontradoException ex){
				}
				
				try {
					deModPer = deModPerDAO.recuperaDeModPerPorPeriodoEModelo(perioPMAtual, planoModelo.getModelo());
				} catch (ObjetoNaoEncontradoException e) {
				}
				
				//Inicializando todos os valores para o plPerMod com os dados de
				//demanda do arquivo deModPer e com os dados de modelo
				plPerMod = new PlPerMod();
				
				plPerMod.setPlanoModelo(planoModelo);
				plPerMod.setPerioPM(perioPMAtual);
				plPerMod.setDispProjModel(0.0);
				plPerMod.setVendasModel(deModPer.getVendasProjetadasModelo());
				plPerMod.setPedidosModel(deModPer.getPedidosModelo());
				plPerMod.setCoberturaModel(planoModelo.getModelo().getCobertura());
				plPerMod.setTuc(planoModelo.getModelo().getTuc());
				plPerMod.setFlagProducaoModel(true); //Livre
				plPerMod.setProducaoModel(0.0);
				plPerMod.setProdLoteModel(0.0);
				plPerMod.setProdDiariaLoteModel(0.0);
				plPerMod.setInicioProdAntesHP(0.0);
				plPerMod.setPeriodoPMInicioPMP(0);
				plPerMod.setEscorePlanPerMod(0.0);
				plPerMod.setVarEstqPerc(0.0);
				plPerMod.setVarProdDiaPerc(0.0);
				
				//Se o periodoPM estiver dentro do intervalo fixo ou se o modelo for fixo
				//entao aproveita valores do pmp vigente
				if((perioPM.getPeriodoPM() <= parametros.getNumIntervalosFixos()) ||
					(!planoModelo.getModelo().getFlagProducaoModel())){
					plPerMod = plPerModService.obtemProducaoModelDoPMPVigente(perioPM.getDataInicial(), perioPM.getNumDiasUteisMatriz(), plPerMod);
					plPerMod.setFlagProducaoModel(false); // fixo
					
				}
				
				
				plPerMods.add(plPerMod);
//				System.out.println("plpermod: "+plPerMod.getPlanoModelo()+" "+plPerMod.getPerioPM());
				
			}
			
			planoModelo.setPlPerMods(plPerMods);
			//Inclui em cascata os plPerMods
			planoModeloService.inclui(planoModelo);
		}
		//Na versão atual nao esta sendo usado como TreeSet, agora o metodo inicioOperacaoInclusao ja 
		// esta retornando PlanoModelos como um list
		// deixamos este trecho apenas como referencia para possivel reutilização
		//addAll coloca a lista com todos os planoModelos para forma de set.
		//Na versão anterior o metodo inicioOperacaoInclusao estava devolvendo um set, tinhamos que
		//converter para lista para fazer a manipulação e depois tornávamos a reconverter para set
		//Set<PlanoModelo> setPlanoModelos = new TreeSet<PlanoModelo>();
		//setPlanoModelos.addAll(planoModelos);
		
		
		//Para um dado plano, recalcula valores de disponibilidade projetada, producao, cobertura e periodoInicial
		//para todos modelos e grava esse recalculo em arquivo(PlPerMod)
		plPerModService.calculaDisProjProducaoECoberturaTodosModelos(planoModelos);
		
	}
	
	

	/**
	 * Inclui cadplan.
	 * Inclui dependencias - PlanoModelos
	 * Inclui PlPerMod com valores iniciais da tabela de Modelo(TUC/Cobertura,estoque inicial)
	 * e a demanda informada em DeModPer.
	 * 
	 * @author felipe.arruda
	 * @param cadPlan
	 * @throws AplicacaoException
	 */
	@Transacional
	public List<PlanoModelo> incluiCadPlanEDependenciasComValoresInic(CadPlan cadPlan) throws AplicacaoException {
		
		List<PlanoModelo> planoModelos = null;
		// verifica se o plano ja esta cadastrado,
		// e se for o caso, inclui cadplan e inclui dependencias relativas a planomodelo 
		try {
			planoModelos = this.inicioOperacaoInclusao(cadPlan);
			Collections.sort(planoModelos);		// Só para garantir que os PlanoModelo novos vão estar ordenados.
		} catch (AplicacaoException e) {
		}

		// 3) Incluir dependencias referentes a tabela PlPerMod
		
		int numeroIntervalosCongelados = parametrosDAO.recuperaListaDeParametros().get(0).getNumIntervalosFixos();
		
		// 	É preciso recuperar o intervalo de HP-PMP, pois é neste intervalo que será baseada a criação de novos
		// registros na tabela PlPerMod
		HP hp = hpDAO.recuperaListaDeHP().get(0);
		
		List<PerioPM> perioPMs = perioPMDAO.recuperaIntervaloDePerioPMs
				(hp.getPerioPMInicPMP().getPeriodoPM(), hp.getPerioPMFinalPMP().getPeriodoPM());
		
		PlPerMod plPerMod = null;
		PerioPM perioPMAtual = null;
		DeModPer deModPer = null;
		boolean flagProducao = false; 
		SortedSet<PlPerMod> plPerMods = null;
		Parametros parametros = parametrosDAO.recuperaListaDeParametros().get(0);

		//Equivalente a percorrer o arquivo de modelo
		//ja que esta ordenado da mesma forma que Modelo
		//Percorrendo todos os modelos de um determinado plano
		for (PlanoModelo planoModelo : planoModelos) {

			//cria um plpermod p/ cada modelo
			plPerMods = new TreeSet<PlPerMod>();

			///Percorre todos os periodos
			for (PerioPM perioPM : perioPMs) {
				
				try {
					perioPMAtual = perioPMDAO.getPorId(perioPM.getId());
				} catch (ObjetoNaoEncontradoException ex){
				}
				
				try {
					deModPer = deModPerDAO.recuperaDeModPerPorPeriodoEModelo(perioPMAtual, planoModelo.getModelo());
				} catch (ObjetoNaoEncontradoException e) {
				}
				
				//Inicializando todos os valores para o plPerMod com os dados de
				//demanda do arquivo deModPer e com os dados de modelo
				plPerMod = new PlPerMod();
				
				plPerMod.setPlanoModelo(planoModelo);
				plPerMod.setPerioPM(perioPMAtual);
				plPerMod.setDispProjModel(0.0);
				plPerMod.setVendasModel(deModPer.getVendasProjetadasModelo());
				plPerMod.setPedidosModel(deModPer.getPedidosModelo());
				plPerMod.setCoberturaModel(planoModelo.getModelo().getCobertura());
				plPerMod.setTuc(planoModelo.getModelo().getTuc());
				plPerMod.setFlagProducaoModel(true); //Livre
				plPerMod.setProducaoModel(0.0);
				plPerMod.setProdLoteModel(0.0);
				plPerMod.setProdDiariaLoteModel(0.0);
				plPerMod.setInicioProdAntesHP(0.0);
				plPerMod.setPeriodoPMInicioPMP(0);
				plPerMod.setEscorePlanPerMod(0.0);
				plPerMod.setVarEstqPerc(0.0);
				plPerMod.setVarProdDiaPerc(0.0);
				
				//Se o periodoPM estiver dentro do intervalo fixo ou se o modelo for fixo
				//entao aproveita valores do pmp vigente
				if((perioPM.getPeriodoPM() <= parametros.getNumIntervalosFixos()) ||
					(!planoModelo.getModelo().getFlagProducaoModel())){
					plPerMod = plPerModService.obtemProducaoModelDoPMPVigente(perioPM.getDataInicial(), perioPM.getNumDiasUteisMatriz(), plPerMod);
					plPerMod.setFlagProducaoModel(false); // fixo
					
				}
				
				
				plPerMods.add(plPerMod);
//				System.out.println("plpermod: "+plPerMod.getPlanoModelo()+" "+plPerMod.getPerioPM());
				
			}
			
			planoModelo.setPlPerMods(plPerMods);
			//Inclui em cascata os plPerMods
			planoModeloService.inclui(planoModelo);
		}
		//Na versão atual nao esta sendo usado como TreeSet, agora o metodo inicioOperacaoInclusao ja 
		// esta retornando PlanoModelos como um list
		// deixamos este trecho apenas como referencia para possivel reutilização
		//addAll coloca a lista com todos os planoModelos para forma de set.
		//Na versão anterior o metodo inicioOperacaoInclusao estava devolvendo um set, tinhamos que
		//converter para lista para fazer a manipulação e depois tornávamos a reconverter para set
		//Set<PlanoModelo> setPlanoModelos = new TreeSet<PlanoModelo>();
		//setPlanoModelos.addAll(planoModelos);
		return planoModelos;
	}
	
	/**
	 * Verifica se o planejamento foi inicializado,
	 * Verifica se ja existe esse cadPlan  no banco
	 * Caso nao exista. inclui esse plano no cadplan
	 * Incluir dependencias referentes a tabela PlanoModelo, ou seja:
	 * Para cada modelo inclui um planoModelo referente ao modelo em questao e a esse plano
	 * 
	 * Retorna essa lista de planoModelos para um determinado plano.
	 *  */
	@Transacional 
	public List<PlanoModelo> inicioOperacaoInclusao(CadPlan cadPlan) throws AplicacaoException {
		
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		
		// 1) Verificar se o Parametro 'InicPlanejamento' está habilitado
		
		if (!parametro.isInicPlanejamento()){
			throw new AplicacaoException("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
		}
		
		try {
			cadPlanDAO.recuperaCadPlanPorCodigo(cadPlan.getCodPlan());
			throw new AplicacaoException("cadplan.CODIGO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException ob) {
		}
		
		cadPlanDAO.inclui(cadPlan);
		
		// 2) Incluir dependencias referentes a tabela PlanoModelo
		
		PlanoModelo planoModelo = null;
		List<Modelo> modelos = modeloDAO.recuperaListaDeModelos();
		Collections.sort(modelos);		// Só para garantir que os registros serao inseridos ordenados por Modelo.
		
		// atenção só posso usar list aqui porque não estou incluindo a lista, caso contrario daria erro
		// pois o hibernate estaria esperando set
		List<PlanoModelo> planoModelos = new ArrayList<PlanoModelo>(0);

		for (Modelo modelo : modelos) {
			
			planoModelo = new PlanoModelo(cadPlan, modelo);
			
			planoModelo.setFlagProducaoModelOriginal(modelo.getFlagProducaoModel());
			planoModelo.setIndiceCobOriginal(modelo.getIndiceCob());
			planoModelo.setTamLoteOriginal(modelo.getTamLote());
			planoModelo.setTrOriginal(modelo.getTr());
			planoModelo.setCoberturaOriginal(modelo.getCobertura());
			planoModelo.setEstqInicModelOriginal(modelo.getEstqInicModel());
			planoModelo.setEstqEmFaltaOriginal(modelo.getEstqEmFalta());
			planoModelo.setRecebimentoPendenteOriginal(modelo.getRecebimentoPendente());
			
			planoModelos.add(planoModelo);
		}
		
		return planoModelos;
	}
	
	
	/**
	 * Calcula os escores de um cadplan.
	 * 
	 * @param cadPlan
	 * @throws AplicacaoException 
	 * @throws AplicacaoException
	 */
	@Transacional
	public double calcularEscores(CadPlan cadPlan, String nomeArquivoModelagem, List<PlanoModelo> listaPlanoModelos) throws AplicacaoException{
//		
//		try {
//			cadPlan = recuperaCadPlanComPlanosModeloEPlPerMods(cadPlan.getCodPlan());
//		} catch (ObjetoNaoEncontradoException e) {
//			//throw new AplicacaoException(e.getMessage());
//		}
		

		// 	É preciso recuperar o intervalo de HP-PMP, pois é neste intervalo que será baseada a criação de novos
		// registros na tabela PlPerMod
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		double escMinPlano=0.0;
		double escMedioPlano = 0.0;
		double escMinGeral =0.0;
		int numInteracoesPlanoModelosDoCadPlan=0;
		PlPerMod plPerModMinGeral=null;
		
		//Instancia aqui o motor de inferencia para evitar que este fique carregando varias vezes o mesmo arquivo
		//que é o que aconteceria se fosse chamado dentro do metodo escorePlanPerMod
		//ja que cada vez que passa num plPerMod iria ser chamado.
		//pode ser chamado foro do plano modelo já que o nome do arquivo de modelagem nesse ponto não vai mudar,
		//portanto é a mesma modelagem.
		MotorInferencia motorInferencia = new MotorInferencia();
		for (PlanoModelo planoModelo : listaPlanoModelos) { 
			try {
				//usar o DAO!
				planoModelo = planoModeloDAO.recuperarPlPerModsPorPlanoModelo(planoModelo);
			} catch (ObjetoNaoEncontradoException e) {
			}
			//popula a list de plPerMods usando o set de plpermods
			ArrayList<PlPerMod> listaPlPerModsPlanoModelo = new ArrayList<PlPerMod>();
			listaPlPerModsPlanoModelo.addAll(planoModelo.getPlPerMods());
			planoModelo.setPlPerModsList(listaPlPerModsPlanoModelo);
			
			planoModeloService.calculaProdDiariaMediaDoPlanoModelo(planoModelo);
			

			int numIteracoesPlPerModsDoPlanoModelo=0;
			PlPerMod plPerModMin = null;
			double escMin=0.0;
			

			//verificar se esta vindo com plPerMods
			for (PlPerMod plPerMod : planoModelo.getPlPerModsList()){
				
				plPerModService.calculaEstqSegDesejado(plPerMod,hpBD);
				plPerModService.calculaVarEstqPerc(plPerMod);
				

				plPerModService.calculaVarProdDiaPerc(plPerMod);
				
				//usa motor de inferencia
				plPerModService.calculaEscorePlanPerMod(plPerMod, nomeArquivoModelagem,motorInferencia);	
				plPerModService.altera(plPerMod);

				//se for o primeiro plPerMod do planoModelo corrente
				//coloca o valor inicial do escore minimo(escMin) como sendo o escore do primeiro plPerMod(o atual)
				if(numIteracoesPlPerModsDoPlanoModelo==0){
					escMin = plPerMod.getEscorePlanPerMod();
					plPerModMin=plPerMod;
					//se for o primeiro plPerMod do primeiro plano modelo
					//coloca esse valor de escore como sendo o valor inicial do escore minimo geral como sendo
					//o valor do escore desse plPermod.
					if(numInteracoesPlanoModelosDoCadPlan ==0){
						escMinGeral=escMin;
						plPerModMinGeral=plPerMod;
						
					}

					numIteracoesPlPerModsDoPlanoModelo++;
				}

				//se o escore do plPermod atual for menor que o escore minimo, entao
				//ele se torna o escore minimo dos plPermods
				if(plPerMod.getEscorePlanPerMod() < escMin){
					escMin = plPerMod.getEscorePlanPerMod();
					plPerModMin=plPerMod;
				}
				//se o escore minimo dos plPermods for menor do que o escore minimo geral, entao
				//ele se torna o escore minimo geral.
				if(escMin < escMinGeral){
					escMinGeral = escMin;
					plPerModMinGeral=plPerMod;
				}
			}

			//seta o valor do escore minimo do plano modelo como sendo o menor escore de todos os periodos.
			planoModelo.setEscorePlanPerModMin(escMin);
			planoModelo.setVarEstqPercEscMin(plPerModMin.getVarEstqPerc());
			planoModelo.setVarProdDiaPercEscMin(plPerModMin.getVarProdDiaPerc());
			
			//calculando escore medio do modelo considerando a media dos escores de todos os periodos
			planoModeloService.calculaEscore(planoModelo);			
			
			
			planoModeloService.altera(planoModelo);
			
			//a cada modelo soma o valor ao escore medio plano
			escMedioPlano +=  planoModelo.getEscore();
			
			//se for o primeiro planoModelo do cadPlan
			//inicializa o valor da variavel escMinPlano como sendo o escore do primeiro plano(atual).
			if(numInteracoesPlanoModelosDoCadPlan==0){
				escMinPlano = planoModelo.getEscore();
				numInteracoesPlanoModelosDoCadPlan++;
			}

			//se o escore do plano modelo atual for menor que o escore minimo dos planos modelos, entao
			//ele se torna o escore minimo dos plonos modelos
			if(planoModelo.getEscore() < escMinPlano){
				escMinPlano = planoModelo.getEscore();
			}
		}
		//calcula escore medio do plano
		escMedioPlano = escMedioPlano / listaPlanoModelos.size();
		cadPlan.setEscoreMedio(escMedioPlano);
		
		cadPlan.setEscoreModeloEscMinimo(escMinPlano);
		
		cadPlan.setEscoreMin(escMinGeral);

		cadPlan.setVarEstqPer(plPerModMinGeral.getVarEstqPerc());		

		cadPlan.setVarProdDiaPer(plPerModMinGeral.getVarProdDiaPerc());
		
		cadPlan.setEscoreCalculado(true);
		
		altera(cadPlan);
		//atualiza o rank
		atualizaRankCadPlansDeUmUsuario(cadPlan.getUsuario());
		
		return escMedioPlano;
	}  
	
	@Transacional
	public void atualizaRankCadPlansDeUmUsuario(Usuario usuario){
		List<CadPlan> listaDeCadPlans = cadPlanDAO.recuperaListaDeCadPlanPorUsuario(usuario);
		// Ordeno os cadPlans pelo atributo 'escoreMedio'
		Collections.sort(listaDeCadPlans, new CadPlanComparatorPorEscoreMedio());
		
		//para cada cadplan na lista
		for (int i = 0; i < listaDeCadPlans.size(); i++) {
			//pega o cadplan da posicao i da listaDeCadPlans
			CadPlan cadPlan= listaDeCadPlans.get(i);
			
			//se i escore medio for 0 quer dizer que ele n foi avaliado,
			//portanto seu rank==0
			if(cadPlan.getEscoreMedio()==0){
				cadPlan.setRanking(0.0);				
			}else{
				//caso tenha sido avaliado ele coloca o rank como sendo
				//a posição do mesmo na lista +1.
				cadPlan.setRanking(new Double(i+1));	
			}
			
			altera(cadPlan);
		}
	}
	
	/**
	 * Inclui cadplan.
	 * Inclui dependencias - PlanoModelos
	 * Inclui PlPerMod com valores iniciais da tabela de Modelo(TUC/Cobertura,estoque inicial)
	 * e a demanda informada em DeModPer.
	 * 
	 * A partir dos valores iniciais de plpermod executa mecanismo de inferencia
	 * e atualiza em arquivo os registros de plpermod já recalculados
	 * 
	 * <br/><br/>
	 * 
	 * Metodo equivalente ao DMPlPerMod.incluiPlPerModBaseadoEquacaoConserv da versao em delphi apenas
	 * usando o mecanismo de inferencia fuzzy.
	 * 
	 * 
	 * 
	 * @author felipe.arruda
	 * @param cadPlan
	 * @throws AplicacaoException
	 */
	@Transacional
	public void incluiPMPFuzzy(CadPlan cadPlan) throws AplicacaoException {
		//Inclui cadPlan e suas depencencias com valores iniciais(PlPerMod, PlanoModelo)
		List<PlanoModelo> planoModelos = incluiCadPlanEDependenciasComValoresInic(cadPlan);
		
		//Para um dado plano, recalcula valores de disponibilidade projetada, producao, cobertura e periodoInicial
		//para todos modelos e grava esse recalculo em arquivo(PlPerMod)
		plPerModService.calculaFuzzyDisProjProducaoECoberturaTodosModelos(planoModelos);
		
	}  
	
	@Transacional
	public void copiarPlano(CadPlan planoOriginal, CadPlan novoPlano){
		
		// A primeira coisa a se fazer é copiar as informações gerais do CADPLAN Original para o novo Plano
		novoPlano.setEscoreMedio(planoOriginal.getEscoreMedio());
		novoPlano.setEscoreMin(planoOriginal.getEscoreMin());
		novoPlano.setRanking(planoOriginal.getRanking());
		novoPlano.setVarEstqPer(planoOriginal.getVarEstqPer());
		novoPlano.setVarProdDiaPer(planoOriginal.getVarProdDiaPer());
		
		List<PlanoModelo> planoModelosNovos = null;
		
		try {
			//   Aqui, o novo Plano é incluído, ja com as informações copiadas do Plano Original. Este método ja retornou
			// os registros de 'PlanoModelo' deste novo plano, mas que ainda NAO ESTAO em Banco de dados, ou seja, TRANSIENTES. 
			planoModelosNovos = this.inicioOperacaoInclusao(novoPlano);  
			Collections.sort(planoModelosNovos);		// Só para garantir que os PlanoModelo novos vão estar ordenados.
		} catch (AplicacaoException e) {
		}
		
		SortedSet<PlPerMod> plPerModsCopiados = null;	// Lista de PlPerMods que serão copiados do Plano Original.
		
		// A estrutura 'SortedSet' ordena seus elementos baseada na Ordem Natural definida p/ os mesmos (vide 'Comparable')
		SortedSet<PlanoModelo> planosModeloOriginais = new TreeSet<PlanoModelo>(planoOriginal.getPlanosModelo()); 
		int i = 0;
		
		
		// 	  Agora, estas repetições vão varrer todos os PlPerMods do Plano ORIGINAL, que vão ter seus valores copiados
		// para os novos PlPerMods, que ao fim, são adicionados a um Set.
		
		for (PlanoModelo planoModeloOriginal : planosModeloOriginais) {
			
			try {
				// Executamos esta busca para buscarmos os PlPerMods do Plano Original.
				planoModeloOriginal = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModeloOriginal);
			} catch (ObjetoNaoEncontradoException e) {
			}
			
			PlanoModelo planoModeloNovo = planoModelosNovos.get(i);
			
			plPerModsCopiados = new TreeSet<PlPerMod>(new PlPerModComparatorPorPerioPM());  // Inicializando a estrutura que guardará os novos PlPerMods
			
			for (PlPerMod plPerMod : planoModeloOriginal.getPlPerMods()) {
				
				PlPerMod plPerModCopiado = new PlPerMod();
				
				// Copiando todas as informações do PlPerMod corrente do Plano Original
				plPerModCopiado.setCoberturaModel(plPerMod.getCoberturaModel());
				plPerModCopiado.setTuc(planoModeloNovo.getModelo().getTuc());
				plPerModCopiado.setDispProjModel(plPerMod.getDispProjModel());
				plPerModCopiado.setEscorePlanPerMod(plPerMod.getEscorePlanPerMod());
				plPerModCopiado.setFlagProducaoModel(plPerMod.getFlagProducaoModel());
				plPerModCopiado.setInicioProdAntesHP(plPerMod.getInicioProdAntesHP());
				plPerModCopiado.setPedidosModel(plPerMod.getPedidosModel());
				plPerModCopiado.setProdDiariaLoteModel(plPerMod.getProdDiariaLoteModel());
				plPerModCopiado.setProdLoteModel(plPerMod.getProdLoteModel());
				plPerModCopiado.setProducaoModel(plPerMod.getProducaoModel());
				plPerModCopiado.setVarEstqPerc(plPerMod.getVarEstqPerc());
				plPerModCopiado.setVarProdDiaPerc(plPerMod.getVarProdDiaPerc());
				plPerModCopiado.setVendasAmortModel(plPerMod.getVendasAmortModel());
				plPerModCopiado.setVendasModel(plPerMod.getVendasModel());
				plPerModCopiado.setPerioPM(plPerMod.getPerioPM());
				plPerModCopiado.setPlanoModelo(planoModeloNovo);
				plPerModCopiado.setPeriodoPMInicioPMP(plPerMod.getPeriodoPMInicioPMP());
				
				plPerModsCopiados.add(plPerModCopiado);		// Ao fim, o PlPerMod copiado é adicionado no Set
				
			}
			
			// Por fim, o Set de PlPerMods copiados é associado ao novo registro de PlanoModelo que...
			planoModeloNovo.setPlPerMods(plPerModsCopiados);
			
			// ... é persistido no BD, persistindo seus respectivos registros de PlPerMod em cascata (definido na Entidade).
			planoModeloService.inclui(planoModeloNovo);
			
			i++;	// A próxima iteração será para o registro seguinte de PlanoModelo.
		}
	}
	
	@Transacional
	public void altera(CadPlan cadPlan) {
		cadPlanDAO.altera(cadPlan);
	}

	/**
	 * Atenção: o mapeamento realizado no modelo da Classe (via JPA) já está excluindo as 
	 * dependências <b>em cascata</b>!
	 * 
	 * ENTIDADES: PlanoModelo, PlPerMod
	 * 
	 * @param cadPlan
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(CadPlan cadPlan) throws AplicacaoException {
		
		CadPlan cadPlanBD = null;
		
		try {
			cadPlanBD = cadPlanDAO.getPorIdComLock(cadPlan.getId());
			
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("cadplan.NAO_ENCONTRADO");
		}
		
		cadPlanDAO.exclui(cadPlanBD);
	}

	public CadPlan recuperaCadPlanComPlanosModelo(String codPlan) throws ObjetoNaoEncontradoException {
		return cadPlanDAO.recuperaCadPlanComPlanosModelo(codPlan);
	}

	public List<CadPlan> recuperaListaDeCadPlan(){
		return cadPlanDAO.recuperaListaDeCadPlan();
	}
	
	public List<CadPlan> recuperaListaDeCadPlanPorUsuario(Usuario usuario){
		return cadPlanDAO.recuperaListaDeCadPlanPorUsuario(usuario);
	}

	public CadPlan recuperaCadPlanPorCodigo(String codPlan) throws AplicacaoException {

		CadPlan cadPlanBD = null;

		try {
			cadPlanBD = cadPlanDAO.recuperaCadPlanPorCodigo(codPlan);
		} catch (ObjetoNaoEncontradoException exc) {
			throw new AplicacaoException("cadPlan.NAO_ENCONTRADO");
		}

		return cadPlanBD;
	}

	public CadPlan recuperaCadPlanComPlanosModeloEPlPerMods(String codPlan) throws ObjetoNaoEncontradoException {
		return cadPlanDAO.recuperaCadPlanComPlanosModelo(codPlan);
	}
	
	public List recuperaCadPlanComDependencias(CadPlan cadPlan){
		return cadPlanDAO.recuperaCadPlanComDependencias(cadPlan);
	}
	
	/**
	 * Este metodo retorna um objeto do tipo CadPlan.
	 * @param codPlan
	 * @return
	 * @throws AplicacaoException
	 * 
	 * @author marques.araujo
	 * 
	 */
	public CadPlan recuperaCadPlanApenasComPlanosModelo(String codPlan)throws AplicacaoException {
		
		CadPlan cadplan = null;
		
		try{
			cadplan = cadPlanDAO.recuperaCadPlanApenasComPlanosModelo(codPlan);
		}catch(ObjetoNaoEncontradoException obj){
			throw new AplicacaoException();
		}
		return cadplan;
	}
	
	/**
	 * Este método é responsável por gerar um relatório ..... 
	 * @author marques.araujo
	 * @param List<> 
	 * @return void
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorioAgregado(CadPlan cadplan) throws AplicacaoException {
		
	
		System.out.println(">>>>>>>>>>>>>> Antes da variavel relatorio em gerarRelatorioAgregado>>>>>>");
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_PLANO_MESTRE_DE_PRODUCAO_POR_MODELO);
			
		System.out.println(">>>>>>>>>>>>>> Depois da variavel relatorio em gerarRelatorioAgregado>>>>>>");
		
		if(relatorio!=null){
			System.out.println("A variavel relatorio eh diferente de null");
		}else{
			System.out.println("A variavel relatorio tem valor null");
		}
		
		try{
			
			relatorio.gerarRelatorio(this.converterParaPlanoMestreDeProducaoPorModeloRelatorio(cadplan),new HashMap());
			
			System.out.println(">>>>>>>>>>>>>> Depois da variavel relatorio em gerarRelatorioAgregado executa converter>>>>>>");
			
		} catch (RelatorioException re){
			throw new AplicacaoException("cadplan.RELATORIO_NAO_GERADO");
		}
	}
	
	/**
	 * Este método é responsável por gerar objetos do tipo PlanoMestreDeProducaoPorModeloRelatorio a partir de objetos do tipo CadPlan,
	 * PlanoModelo, PlPerMode, PerioPm, Familia e Modelo.
	 * @author marques.araujo
	 * @param CadPlan cadplan
	 * @return List<PlanoMestreDeProducaoPorModeloRelatorio>
	 * 
	 */
	
	public List<PlanoMestreDeProducaoPorModeloRelatorio> converterParaPlanoMestreDeProducaoPorModeloRelatorio(CadPlan cadplan)throws AplicacaoException{
			
			List<PlanoMestreDeProducaoPorModeloRelatorio> planoMestreRelatorio = new LinkedList<PlanoMestreDeProducaoPorModeloRelatorio>();
			
			  for (PlanoModelo planoModelo : cadplan.getPlanosModelo()) {
				  
				  List<PlPerMod> plpermods = plPerModService.recuperaListaDePlPerModPorPlanoModelo(planoModelo); 
				  Modelo modelo = modeloService.recuperaUmModeloComFamilia(planoModelo.getModelo());
				  Familia familia = modelo.getFamilia();
				  
				  if(plpermods.isEmpty()){//Caso o PlanoModelo nao tenha plpermods.
					  planoMestreRelatorio.add(new PlanoMestreDeProducaoPorModeloRelatorio(cadplan,planoModelo,null,familia));
				  }
				  
				  //Perceba que o planomodelo que nao tiver plpermods nao passara no segundo for e tambem nao dara erro.
				  for (PlPerMod plpermod : plpermods) {
					  
					  planoMestreRelatorio.add(new PlanoMestreDeProducaoPorModeloRelatorio(cadplan, planoModelo, plpermod, familia));
				}
				
			}
			  
			  Collections.sort(planoMestreRelatorio);//Ordenando para ficar de acordo com a tela list do plpermod
			  
		return planoMestreRelatorio;
    }
		
	
	public List<PlanoModelo> converteSetPlanoModelosParaListaPlanoModelos(CadPlan cadPlanCorrente) {
		
		List<PlanoModelo> listaDePlanoModelos= new ArrayList<PlanoModelo>(cadPlanCorrente.getPlanosModelo());
			
			Collections.sort(listaDePlanoModelos);		// Ja deveria vir ordenado (devido a inserção), mas para garantir, ordeno novamente.
			
			for (PlanoModelo planoModelo : listaDePlanoModelos) {
				
				try {
					
					// Trazendo a informação da Familia associada ao Modelo
					planoModelo.setModelo(modeloService.recuperaUmModeloComFamilia(planoModelo.getModelo()));
					
					// Trazendo os PlPerMods deste PlanoModelo
//					planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
					
					// <<<<<<<<<<<<<<<<<<<<< NOVO TRECHO DE CÓDIGO >>>>>>>>>>>>>>>>>>>>>>>>
					List<PlPerMod> plPerMods = plPerModService.recuperaListaDePlPerModPorPlanoModelo(planoModelo);
					Collections.sort(plPerMods, new PlPerModComparatorPorPerioPM());
					planoModelo.setPlPerModsList(plPerMods);
					
				} catch (AplicacaoException e) {
				} 
			}
			
		
		return listaDePlanoModelos;
	}
	
}
