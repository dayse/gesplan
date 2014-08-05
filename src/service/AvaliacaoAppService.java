 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import modelo.AnaliseMaquinaView;
import modelo.AnaliseRecursoView;
import modelo.AnaliseTecidoView;
import modelo.CadPlan;
import modelo.CapacDia;
import modelo.CapacRec;
import modelo.CapacTecView;
import modelo.DeModPer;
import modelo.HP;
import modelo.Parametros;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import modelo.RecModel;
import modelo.Recurso;
import modelo.TecModel;
import modelo.Tecido;
import modelo.relatorios.AnaliseMaquinaRelatorio;
import modelo.relatorios.AnaliseTecidoRelatorio;
import modelo.relatorios.AnaliseRecursoRelatorio;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.CapacRecDAO;
import DAO.Impl.CapacRecDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import br.blog.arruda.plot.Plot;
import br.blog.arruda.plot.data.PlotData;

import comparator.PlPerModComparatorPorPeriodoInicPMP;

import exception.relatorio.RelatorioException;

public class AvaliacaoAppService {
	//DAOs
	private static CapacRecDAO capacRecDAO;
	
	// Services
	private static HPAppService hpService;
	private static PerioPMAppService perioPMService;
	private static CapacDiaAppService capacDiaService;
	private static CapacTecViewAppService capacTecViewService;
	private static DadosGraficoViewAppService dadosGraficoViewService;
	private static TecModelAppService tecModelService;
	private static RecModelAppService recModelService;
	private static CapacRecAppService capacRecService;
	private static ParametrosAppService parametrosService;
	private static PlanoModeloAppService planoModeloService;
	
	@SuppressWarnings("unchecked")
	public AvaliacaoAppService() {
		
		try {
			// DAOs
			capacRecDAO = FabricaDeDao.getDao(CapacRecDAOImpl.class);
			
			// Services
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			capacTecViewService = FabricaDeAppService.getAppService(CapacTecViewAppService.class);
			dadosGraficoViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);
			tecModelService = FabricaDeAppService.getAppService(TecModelAppService.class);
			recModelService = FabricaDeAppService.getAppService(RecModelAppService.class);
			capacRecService = FabricaDeAppService.getAppService(CapacRecAppService.class);
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// ---------------------------- ANALISE DE MAQUINA ----------------------------
	
	/**
	 * Metodo que gera o relatorio de Avaliacao de Maquina.
	 * Cria um Relatorio do tipo AnaliseDeMaquina
	 * Converte os atributos de plPerMod para dados do relatorio
	 * Popula o relatorio com esses dados, e com parametros do cadPlan em questao.
	 * 
	 */
	public void gerarRelatorioAvaliacaoMaquina(CadPlan cadPlan, List<PlPerMod> plPerMods) throws AplicacaoException {

		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_ANALISE_MAQUINA);
		List<AnaliseMaquinaRelatorio> dados = this.converterAtributosParaAnaliseMaquina(plPerMods);
		
		Map parametros = new HashMap();
		
		parametros.put("codPlan", cadPlan.getCodPlan());
		parametros.put("descrPlan", cadPlan.getDescrPlan());
		
		try{
			relatorio.gerarRelatorio(dados, parametros);
		} catch (RelatorioException re){
			throw new AplicacaoException("erro.GERACAO_RELATORIO");
		}
	}
	/**
	 * Converte os atributos de plPerMod para relatorio de analiseMaquina
	 * Percorre uma lista de plPerMods e instancia um AnaliseMaquinaRelatorio com esses
	 * dados e os coloca numa lista de AnaliseMaquinaRelatorio.
	 * Retorna essa lista ao fim do processo.
	 * @param plPerMods
	 * @return
	 */
	public List<AnaliseMaquinaRelatorio> converterAtributosParaAnaliseMaquina(List<PlPerMod> plPerMods){
		
		// Buscando a lista de CapacDias do sistema
		List<CapacDia> capacDias = capacDiaService.recuperaListaDeCapacDias();
		
		AnaliseMaquinaRelatorio analiseMaquina = null;
		List<AnaliseMaquinaRelatorio> dados = new LinkedList<AnaliseMaquinaRelatorio>();
		
		// Ordeno os PlPerMods pelo atributo 'periodoInicPMP'
		Collections.sort(plPerMods, new PlPerModComparatorPorPeriodoInicPMP());
		
		for (PlPerMod plPerMod : plPerMods) {
			
			PerioPM perioPM = null;
			Integer periodo = plPerMod.getPeriodoPMInicioPMP(); 
			
			// Se o período é ZERO, ele não é válido, visto que representa informação de produção que não tem mais importância no momento
			if (periodo != 0){
				
				try {
					// O PlPerMod teve sua produção realizada em um Período válido.
					perioPM = perioPMService.recuperaPerioPMPorPeriodoPM(periodo);
					
				} catch (AplicacaoException e) {
				}
				
				analiseMaquina = new AnaliseMaquinaRelatorio(perioPM, plPerMod);
				analiseMaquina.setCapacDia(capacDias.get(perioPM.getPeriodoPM() - 1).getCapacProdDiariaEmMin());
				
				dados.add(analiseMaquina);
			}
		}
		
		return dados;
	}

	/**
	 * Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, relativa a ANALISE DE MAQUINA
	 * 
	 * @param plPerMods
	 * @param periodoSelecionado
	 * @return
	 */
	public List<PlPerMod> defasarProducaoPlPerModsNoPeriodoAnaliseMaquina(List<PlPerMod> plPerMods, PerioPM periodoSelecionado){
		
		List<PlPerMod> plPerModsDefasados = new LinkedList<PlPerMod>();
		
		for (PlPerMod plPerMod : plPerMods) {
			
			// Lista que guarda os PlPerMods com PeriodoInicioPMP com valor igual ao do Periodo selecionado. 
			if (plPerMod.getPeriodoPMInicioPMP().equals(periodoSelecionado.getPeriodoPM())){
				plPerModsDefasados.add(plPerMod);
			}
		}
		
			
		for (PlPerMod plPerMod : plPerModsDefasados) {
			
			// Cálculo do atributo transiente 'prodDiariaLoteMin'
			Double prodDiariaLoteMin = plPerMod.getProdDiariaLoteModel() * 					// [EXEMPLO-TESTE]
			plPerMod.getPlanoModelo().getModelo().getTuc() *						  		//	M1=2  e  M2=3
			plPerMod.getPlanoModelo().getModelo().getTamLote();								//	M1=5  e  M2=5
			
			plPerMod.setProdDiariaLoteMin(prodDiariaLoteMin);
			
		}
		
		return plPerModsDefasados;
	}


	/**
	 * Metodo que gera o relatorio de Avaliacao de Tecido.
	 * Cria um Relatorio do tipo AnaliseTecidoRelatorio
	 * Defasa a producao de plPerMods de todos de todos os periodos da Analise de Tecido.
	 * Converte os atributos de plPerMod para dados do relatorio
	 * Popula o relatorio com esses dados, e com parametros do cadPlan em questao.
	 * 
	 */
	public void gerarRelatorioAvaliacaoTecido(CadPlan cadPlan, List<PlPerMod> plPerMods,Tecido tecido) throws AplicacaoException {
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_ANALISE_TECIDO);
		
		defasarProducaoPlPerModsDeTodosPeriodosAnaliseTecido(plPerMods,tecido);		
		
		List<AnaliseTecidoRelatorio> dados = this.converterAtributosParaAnaliseTecido(plPerMods, tecido);
		
		Map parametros = new HashMap();
		
		parametros.put("codPlan", cadPlan.getCodPlan());
		parametros.put("descrPlan", cadPlan.getDescrPlan());
		
		try{
			relatorio.gerarRelatorio(dados, parametros);
		} catch (RelatorioException re){
			throw new AplicacaoException("erro.GERACAO_RELATORIO");
		}
	}
	
	/**
	 * Converte os atributos de plPerMod para relatorio de AnaliseTecido
	 * Recupera lista de CapacTecsViews pelo tecido.
	 * Percorre uma lista de plPerMods e instancia um AnaliseTecidoRelatorio com esses
	 * dados e os coloca numa lista de AnaliseTecidoRelatorio.
	 * Retorna essa lista ao fim do processo.
	 * @param plPerMods
	 * @return
	 */
	public List<AnaliseTecidoRelatorio> converterAtributosParaAnaliseTecido(List<PlPerMod> plPerMods, Tecido tecido){
		
		// Buscando a lista de CapacDias do sistema
		List<CapacTecView> capacTecs = new ArrayList<CapacTecView>();
		try {
			capacTecs = capacTecViewService.recuperaListaDeCapacTecViewsPorTecido(tecido);
		} catch (AplicacaoException e1) {
			e1.printStackTrace();
		}
		
		AnaliseTecidoRelatorio analiseTecido = null;
		List<AnaliseTecidoRelatorio> dados = new LinkedList<AnaliseTecidoRelatorio>();
		
		// Ordeno os PlPerMods pelo atributo 'periodoInicPMP'
		Collections.sort(plPerMods, new PlPerModComparatorPorPeriodoInicPMP());
		
		//double necessidadeTotalDoPeriodo = 0.0;
		for (PlPerMod plPerMod : plPerMods) {
			
			PerioPM perioPM = null;
			Integer periodo = plPerMod.getPeriodoPMInicioPMP(); 

			
			
			
			// Se o período é ZERO, ele não é válido, visto que representa informação de produção que não tem mais importância no momento
			if (periodo != 0){
				
				try {
					// O PlPerMod teve sua produção realizada em um Período válido.
					perioPM = perioPMService.recuperaPerioPMPorPeriodoPM(periodo);
					
				} catch (AplicacaoException e) {
				}
				
				
				analiseTecido = new AnaliseTecidoRelatorio(perioPM, plPerMod,tecido);
				analiseTecido.setConsumoMaxDiarioMatriz(capacTecs.get(perioPM.getPeriodoPM() -1).getConsumoMaxDiarioMatriz());
				
				
				dados.add(analiseTecido);
			}
		}
		
		return dados;
	}

	/**
	 * Rotina que calcula a produção defasada dos PlPerMods em todos os
	 * Períodos, relativa a ANALISE DE TECIDO
	 * 
	 * @param plPerMods
	 * @param periodoSelecionado
	 * @return
	 */
	public void defasarProducaoPlPerModsDeTodosPeriodosAnaliseTecido(List<PlPerMod> plPerMods, Tecido tecido){
		
		
		TecModel tecModel = null;
		Parametros parametroCorrente = parametrosService.recuperaListaDeParametros().get(0);
		
		for (PlPerMod plPerMod : plPerMods) {
			
			try {
				tecModel = tecModelService.recuperaTecModelPorTecidoEModelo(tecido, plPerMod.getPlanoModelo().getModelo());

				Double consumoPorLoteKg = (tecModel.getConsumoPorLoteMt() * (parametroCorrente.getPercentualDePerda()/100 + 1)) / tecido.getFatorDeRendimento();
				
				plPerMod.setConsumoLoteMt(tecModel.getConsumoPorLoteMt());
				plPerMod.setConsumoLoteKg(consumoPorLoteKg);
				plPerMod.setConsumoDiarioKg(plPerMod.getProdDiariaLoteModel() * consumoPorLoteKg);
			} catch (ObjetoNaoEncontradoException e) {
				plPerMod.setConsumoLoteMt(0.0);
				plPerMod.setConsumoLoteKg(0.0);
				plPerMod.setConsumoDiarioKg(0.0);

			}
			
		}
		
		return;
	}
	
	/**
	 * Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, relativa a ANALISE DE TECIDO
	 * 
	 * @param plPerMods
	 * @param periodoSelecionado
	 * @return
	 */
	public List<PlPerMod> defasarProducaoPlPerModsNoPeriodoAnaliseTecido(List<PlPerMod> plPerMods, PerioPM periodoSelecionado, Tecido tecido){
		
		List<PlPerMod> plPerModsDefasados = new LinkedList<PlPerMod>();
		
		for (PlPerMod plPerMod : plPerMods) {
			
			// Lista que guarda os PlPerMods com PeriodoInicioPMP com valor igual ao do Periodo selecionado. 
			if (plPerMod.getPeriodoPMInicioPMP().equals(periodoSelecionado.getPeriodoPM())){
				plPerModsDefasados.add(plPerMod);
			}
		}
		
		TecModel tecModel = null;
		Parametros parametroCorrente = parametrosService.recuperaListaDeParametros().get(0);
		//implementa o consumo do tecido no momento em que esta iniciando a producao(periodoPMInicioPMP)
		for (PlPerMod plPerMod : plPerModsDefasados) {
			
			try {
				tecModel = tecModelService.recuperaTecModelPorTecidoEModelo(tecido, plPerMod.getPlanoModelo().getModelo());
				Double consumoPorLoteKg = (tecModel.getConsumoPorLoteMt() * (parametroCorrente.getPercentualDePerda()/100 + 1)) / tecido.getFatorDeRendimento();
				
				plPerMod.setConsumoLoteMt(tecModel.getConsumoPorLoteMt());
				plPerMod.setConsumoLoteKg(consumoPorLoteKg);
				plPerMod.setConsumoDiarioKg(plPerMod.getProdDiariaLoteModel() * consumoPorLoteKg);
			} catch (ObjetoNaoEncontradoException e) {
				//caso nao exista um tecmodel para esse tecido e o modelo corrente
				//o valor de consumos vai ser 0.
				plPerMod.setConsumoLoteMt(0.0);
				plPerMod.setConsumoLoteKg(0.0);
				plPerMod.setConsumoDiarioKg(0.0);
			}
			
		}
		
		return plPerModsDefasados;
	}

	/**
	 * Metodo que gera o relatorio de Avaliacao de Recurso.
	 * Cria um Relatorio do tipo AnaliseRecursoRelatorio
	 * Defasa a producao de plPerMods de todos de todos os periodos da Analise de Recurso.
	 * Converte os atributos de plPerMod para dados do relatorio
	 * Popula o relatorio com esses dados, e com parametros do cadPlan em questao.
	 * 
	 */
	public void gerarRelatorioAvaliacaoRecurso(CadPlan cadPlan, List<PlPerMod> plPerMods,Recurso recurso) throws AplicacaoException {
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_ANALISE_RECURSO);
		
		defasarProducaoPlPerModsDeTodosPeriodosAnaliseRecurso(plPerMods,recurso);		
		
		List<AnaliseRecursoRelatorio> dados = this.converterAtributosParaAnaliseRecurso(plPerMods, recurso);
		
		Map parametros = new HashMap();
		
		parametros.put("codPlan", cadPlan.getCodPlan());
		parametros.put("descrPlan", cadPlan.getDescrPlan());
		
		try{
			relatorio.gerarRelatorio(dados, parametros);
		} catch (RelatorioException re){
			throw new AplicacaoException("erro.GERACAO_RELATORIO");
		}
	}

	/**
	 * Converte os atributos de plPerMod para relatorio de AnaliseTecido
	 * Recupera lista de CapacTecsViews pelo tecido.
	 * Percorre uma lista de plPerMods e instancia um AnaliseTecidoRelatorio com esses
	 * dados e os coloca numa lista de AnaliseTecidoRelatorio.
	 * Retorna essa lista ao fim do processo.
	 * @param plPerMods
	 * @return
	 */
	public List<AnaliseRecursoRelatorio> converterAtributosParaAnaliseRecurso(List<PlPerMod> plPerMods, Recurso recurso){
		
		// Buscando a lista de CapacRecs do sistema
		List<CapacRec> capacRecs = new ArrayList<CapacRec>();
		try {
		     capacRecs = capacRecService.recuperaListaDeCapacRecsPorRecurso(recurso);
		} catch (AplicacaoException e1){
			e1.printStackTrace();
		}
		

		AnaliseRecursoRelatorio analiseRecurso = null;
		List<AnaliseRecursoRelatorio> dados = new LinkedList<AnaliseRecursoRelatorio>();
		
		// Ordeno os PlPerMods pelo atributo 'periodoInicPMP'
		Collections.sort(plPerMods, new PlPerModComparatorPorPeriodoInicPMP());
		
		//double necessidadeTotalDoPeriodo = 0.0;
		for (PlPerMod plPerMod : plPerMods) {
			
			PerioPM perioPM = null;
			Integer periodo = plPerMod.getPeriodoPMInicioPMP(); 

	
			// Se o período é ZERO, ele não é válido, visto que representa informação de produção que não tem mais importância no momento
			if (periodo != 0){
				
				try {
					// O PlPerMod teve sua produção realizada em um Período válido.
					perioPM = perioPMService.recuperaPerioPMPorPeriodoPM(periodo);
					
				} catch (AplicacaoException e) {
				}
				
				
				analiseRecurso = new AnaliseRecursoRelatorio(perioPM, plPerMod,recurso);
				analiseRecurso.setCapacidade(capacRecs.get(perioPM.getPeriodoPM() -1).getCapacDiaria());  
								
				
				dados.add(analiseRecurso);
			}
		}
		
		return dados;
	}

	/**
	 * Rotina que calcula a produção defasada dos PlPerMods em todos os
	 * Períodos, relativa a ANALISE DE RECURSO
	 * 
	 * @param plPerMods
	 * @param periodoSelecionado
	 * @return
	 */
	public void defasarProducaoPlPerModsDeTodosPeriodosAnaliseRecurso(List<PlPerMod> plPerMods, Recurso recurso){
		
		
		RecModel recModel = null;
		Parametros parametroCorrente = parametrosService.recuperaListaDeParametros().get(0);
		

		for (PlPerMod plPerMod : plPerMods) {
			
			try {
				recModel = recModelService.recuperaRecModelPorRecursoEModelo(recurso, plPerMod.getPlanoModelo().getModelo());
				Double consumoDiario = plPerMod.getProdDiariaLoteModel() * plPerMod.getPlanoModelo().getModelo().getTamLote() * recModel.getConsumoUnit();
				
				plPerMod.setConsumoUnitario(recModel.getConsumoUnit());
				plPerMod.setConsumoDiario(consumoDiario);
				plPerMod.setCustoDiario(consumoDiario * recurso.getCustoUnit());

			} catch (ObjetoNaoEncontradoException e) {
				plPerMod.setConsumoUnitario(0.0);
				plPerMod.setConsumoDiario(0.0);
				plPerMod.setCustoDiario(0.0);
			}
			
		}
		
		return;
	}
	/**
	 * Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, relativa a ANALISE DE RECURSO
	 * 
	 * @param plPerMods
	 * @param periodoSelecionado
	 * @return
	 */
	public List<PlPerMod> defasarProducaoPlPerModsNoPeriodoAnaliseRecurso(List<PlPerMod> plPerMods, PerioPM periodoSelecionado, Recurso recurso){
		
		List<PlPerMod> plPerModsDefasados = new LinkedList<PlPerMod>();
		
		for (PlPerMod plPerMod : plPerMods) {
			
			// Lista que guarda os PlPerMods com PeriodoInicioPMP com valor igual ao do Periodo selecionado. 
			if (plPerMod.getPeriodoPMInicioPMP().equals(periodoSelecionado.getPeriodoPM())){
				plPerModsDefasados.add(plPerMod);
			}
		}
		
		RecModel recModel = null;
		
		for (PlPerMod plPerMod : plPerModsDefasados) {
			
			try {
				recModel = recModelService.recuperaRecModelPorRecursoEModelo(recurso, plPerMod.getPlanoModelo().getModelo());
				Double consumoDiario = plPerMod.getProdDiariaLoteModel() * plPerMod.getPlanoModelo().getModelo().getTamLote() * recModel.getConsumoUnit();
				
				plPerMod.setConsumoUnitario(recModel.getConsumoUnit());
				plPerMod.setConsumoDiario(consumoDiario);
				plPerMod.setCustoDiario(consumoDiario * recurso.getCustoUnit());
			} catch (ObjetoNaoEncontradoException e) {
				plPerMod.setConsumoUnitario(0.0);
				plPerMod.setConsumoDiario(0.0);
				plPerMod.setCustoDiario(0.0);
			}
			
		}
		
		return plPerModsDefasados;
	}


	/**
	 * Lista todos os plPerMods de Todos os periodos do HP para um determinado tecido
	 * calcula a disponibilidade maxima, necessidade total  e todas as outras informacoes de analiseTecidoView
	 * Retorna um map com a lista de plpermods do periodo, e com a lista de AnaliseTecidoView para cada periodo.
	 * @param cadPlan
	 * @param tecidoCorrente
	 * @return
	 * @throws AplicacaoException 
	 */
	public Map listarPlPerModsDeTodosOsPeriodosParaUmTecido(CadPlan cadPlan, Tecido tecidoCorrente) throws AplicacaoException{

		Map resultado = new HashMap();
		List<HP> hpBD = hpService.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		HP hp = hpBD.get(0);
		
		List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
		(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());

		List<PlPerMod> plPerMods = new LinkedList<PlPerMod>();
		ArrayList<AnaliseTecidoView> listaAnaliseTecidoViews = new ArrayList<AnaliseTecidoView>();
		
		
		List<PlPerMod> plPerModsDefasados = new ArrayList<PlPerMod>();
		
		
		try {
			for (PlanoModelo planoModelo : cadPlan.getPlanosModelo()) {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				plPerMods.addAll(planoModelo.getPlPerMods());
			}	
			Collections.sort(plPerMods);     // Lista ORDENADA de todos os PlPerMods relativos a um CadPlan específico.
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		for(PerioPM perioPM : perioPMsDoHP){
			AnaliseTecidoView analiseTecidoView = new AnaliseTecidoView();	
			
			//******************************* Calculo da disponibilidadeMaxDiaria *******************************
			// Calculo similiar ao realizado em CapacTecView
			Double disponibilidadeMaxDiaria = 
				(tecidoCorrente.getProducaoDiariaMaxUnidade2() * perioPM.getNumDiasUteisU2())/perioPM.getNumDiasUteisMatriz();
			//******************************* Calculo da disponibilidadeMaxDiaria *******************************

			
			//******************************* Calculo da necessidadeTotalDoPeriodo *******************************
			double necessidadeTotalDoPeriodo=0.0;
			// Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, para ANALISE DE TECIDO
			List<PlPerMod> plPerModsDoPeriodo = defasarProducaoPlPerModsNoPeriodoAnaliseTecido(plPerMods, perioPM, tecidoCorrente);
			if (!plPerModsDoPeriodo.isEmpty()){
				
				// A Necessidade Total do Periodo é a soma de todos os valores de 'consumoDiarioKg' dos PlPerMods
				for (PlPerMod plPerMod : plPerModsDoPeriodo) {
					necessidadeTotalDoPeriodo += plPerMod.getConsumoDiarioKg();
				}
				
				// Com isso, cada um dos PlPerMods possui sua participação percentual na Necessidade Total do Período
				for (PlPerMod plPerMod : plPerModsDoPeriodo) {
					plPerMod.setParticipacaoPercentual((plPerMod.getConsumoDiarioKg()/necessidadeTotalDoPeriodo)*100);
				}
				
			}
			//******************************* Calculo da necessidadeTotalDoPeriodo *******************************


			//******************************* Salvo infos Em AnaliseTecidoView *******************************
			analiseTecidoView.setNecessidadeTotal(necessidadeTotalDoPeriodo);
			analiseTecidoView.setDisponibilidadeMaxDiaria(disponibilidadeMaxDiaria);
			analiseTecidoView.setComprometimentoKg(disponibilidadeMaxDiaria - necessidadeTotalDoPeriodo);
			analiseTecidoView.setComprometimentoPercentual((necessidadeTotalDoPeriodo*100)/disponibilidadeMaxDiaria);
			//******************************* Salvo infos Em AnaliseTecidoView *******************************
			listaAnaliseTecidoViews.add(analiseTecidoView);
			plPerModsDefasados.addAll(plPerModsDoPeriodo); 
			
		}

		resultado.put("plPerMods" , plPerMods);
		resultado.put("listaAnaliseTecidoViews" , listaAnaliseTecidoViews);

		return resultado;
	}
	
	/** 
	 * Gera o grafico de AvaliacaoTecido, todo populado e pronto para ser utilizado.
	 * Esse metodo utiliza outros metodos para popular os dados do grafico.
	 * @param cadPlan
	 * @param tecido
	 * @return
	 * @throws AplicacaoException
	 */
	public Plot gerarDadosGraficoAvaliacaoTecido(CadPlan cadPlan, Tecido tecido) throws AplicacaoException{
		Plot grafico = new Plot();
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();

		
		List<HP> hpBD = hpService.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		HP hp = hpBD.get(0);
		
		List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
		(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());


		ArrayList<Double> listPeriodosPM = new ArrayList<Double>();
		Map resultado = listarPlPerModsDeTodosOsPeriodosParaUmTecido(cadPlan,tecido);
		List<PlPerMod> listaPlPerMods = (List<PlPerMod>)resultado.get("plPerMods");
		List<AnaliseTecidoView> listaAnaliseTecidoViews = (List<AnaliseTecidoView> )resultado.get("listaAnaliseTecidoViews");

		//popula lista de Periodos PMs
		for(PerioPM perioPM: perioPMsDoHP){
			listPeriodosPM.add(Double.valueOf(perioPM.getPeriodoPM()));			
		}
				
		listaDadosGrafico.addAll(gerarGraficosViewDeTecidoNecessidadeTotalEDisponibilidadeMaxPorPeriodo(listPeriodosPM,listaAnaliseTecidoViews));	

		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Periodo", "Pecas");
		
		//retorna o  grafico.
		return grafico;
	}
	
	

	/**
	 * Gera uma lista de DadosGraficoView com dados populados sobre o grafico de NecessidadeTotal e DisponibilidadeMax
	 * por Periodo para a Analise de Tecido.
	 * @param listPeriodosPM
	 * @param listaAnalise
	 * @return
	 */
	public ArrayList<PlotData> gerarGraficosViewDeTecidoNecessidadeTotalEDisponibilidadeMaxPorPeriodo(ArrayList<Double> listPeriodosPM, List<AnaliseTecidoView> listaAnalise){
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		ArrayList<Double> listNecessidadeTotais = new ArrayList<Double>();
		ArrayList<Double> listDisponibilidadesMax = new ArrayList<Double>();
		
		for(int i=0;i<listPeriodosPM.size();i++){
			AnaliseTecidoView analiseTecidoViewCorrente = listaAnalise.get(i);
			//adciona o valor de cada necessidadeTotal para cada periodo 
			//na lista de necessidadesTotais
			listNecessidadeTotais.add(analiseTecidoViewCorrente.getNecessidadeTotal());			
			//adciona o valor de cada DisponibilidadeMaxDiaria para cada periodo 
			//na lista de DisponibilidadeMaxDiaria
			listDisponibilidadesMax.add(analiseTecidoViewCorrente.getDisponibilidadeMaxDiaria());
		}

		//gera o Dado de grafico de necessidadeTotal por periodo
//		PlotData dadoNecessidadeTotalPeriodo = dadosGraficoViewService.gerarDadosGraficoView(listPeriodosPM, listNecessidadeTotais, "bars");
		PlotData dadoNecessidadeTotalPeriodo = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listNecessidadeTotais);		
		dadoNecessidadeTotalPeriodo.setLabel("Necessidade Total");
		listaDadosGrafico.add(dadoNecessidadeTotalPeriodo);
		
		//gera o Dado de grafico de DisponibilidadeMaxDiaria por periodo
		PlotData dadoDisponibilidadeMaxDiariaPeriodo = dadosGraficoViewService.gerarPlotData(listPeriodosPM, listDisponibilidadesMax);
		dadoDisponibilidadeMaxDiariaPeriodo.setLabel("Disponibilidade max");
		listaDadosGrafico.add(dadoDisponibilidadeMaxDiariaPeriodo);				
		return listaDadosGrafico;		
	}

	

	/**
	 * Lista todos os plPerMods de Todos os periodos do HP para um determinado recurso
	 * calcula a capacidade, necessidade total  e todas as outras informacoes de analiseRecursoView
	 * Retorna um map com a lista de plpermods do periodo, e com a lista de AnaliseRecursoView para cada periodo.
	 * @param cadPlan
	 * @param tecidoCorrente
	 * @return
	 * @throws AplicacaoException 
	 */
	public Map listarPlPerModsDeTodosOsPeriodosParaUmRecurso(CadPlan cadPlan, Recurso recursoCorrente) throws AplicacaoException{

		Map resultado = new HashMap();
		List<HP> hpBD = hpService.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		HP hp = hpBD.get(0);
		
		List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
		(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());

		List<PlPerMod> plPerMods = new LinkedList<PlPerMod>();
		ArrayList<AnaliseRecursoView> listaAnaliseRecursoViews = new ArrayList<AnaliseRecursoView>();
		
		
		List<PlPerMod> plPerModsDefasados = new ArrayList<PlPerMod>();
		
		
		try {
			for (PlanoModelo planoModelo : cadPlan.getPlanosModelo()) {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				plPerMods.addAll(planoModelo.getPlPerMods());
			}	
			Collections.sort(plPerMods);     // Lista ORDENADA de todos os PlPerMods relativos a um CadPlan específico.
		} catch (ObjetoNaoEncontradoException e) {
		}


		
		for(PerioPM perioPM : perioPMsDoHP){
			CapacRec capacRec = null;
			
			try {
				capacRec = capacRecDAO.recuperaCapacRecPorRecursoEPerioPM(recursoCorrente, perioPM);
			} catch (ObjetoNaoEncontradoException e) {
			}
			
			AnaliseRecursoView analiseRecursoView = new AnaliseRecursoView();	
			
			//******************************* Calculo da capacidade diaria *******************************
				Double capacidadeDiaria = capacRec.getCapacDiaria();
			//******************************* Calculo da capacidade *******************************

			
			//******************************* Calculo da necessidadeTotalDoPeriodo *******************************
			double necessidadeTotalDoPeriodo=0.0;
			Double custoTotal = 0.0;
			// Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, para ANALISE DE RECURSO
			List<PlPerMod> plPerModsDoPeriodo = defasarProducaoPlPerModsNoPeriodoAnaliseRecurso(plPerMods, perioPM, recursoCorrente);
			if (!plPerModsDoPeriodo.isEmpty()){

				// A Necessidade Total do Periodo é a soma de todos os valores de 'ConsumoDiario' dos PlPerMods
				for (PlPerMod plPerMod : plPerModsDoPeriodo) {
					custoTotal += plPerMod.getCustoDiario();
					necessidadeTotalDoPeriodo += plPerMod.getConsumoDiario();
				}
				
				// Com isso, cada um dos PlPerMods possui sua participação percentual na Necessidade Total do Período
				for (PlPerMod plPerMod : plPerModsDoPeriodo) {
					plPerMod.setParticipacaoPercentual((plPerMod.getConsumoDiario()/necessidadeTotalDoPeriodo)*100);
				}
				
			}
			//******************************* Calculo da necessidadeTotalDoPeriodo *******************************


			//******************************* Salvo infos Em AnaliseRecursoView *******************************
			analiseRecursoView.setNecessidadeTotal(necessidadeTotalDoPeriodo);
			analiseRecursoView.setCapacidade(capacidadeDiaria);
			analiseRecursoView.setComprometimento(capacidadeDiaria - necessidadeTotalDoPeriodo);
			analiseRecursoView.setComprometimentoPercentual(necessidadeTotalDoPeriodo*100 / capacidadeDiaria);
			analiseRecursoView.setCustoTotal(custoTotal);   
			//******************************* Salvo infos Em AnaliseRecursoView *******************************
			listaAnaliseRecursoViews.add(analiseRecursoView);
			plPerModsDefasados.addAll(plPerModsDoPeriodo); 
			
		}

		resultado.put("plPerMods" , plPerMods);
		resultado.put("listaAnaliseRecursoViews" , listaAnaliseRecursoViews);

		return resultado;
	}
	/**
	 * Gera o grafico de AvaliacaoRecurso, todo populado e pronto para ser utilizado.
	 * Esse metodo utiliza outros metodos para popular os dados do grafico.
	 * @param cadPlan
	 * @param recurso
	 * @return
	 * @throws AplicacaoException
	 */
	public Plot gerarDadosGraficoAvaliacaoRecurso(CadPlan cadPlan, Recurso recurso) throws AplicacaoException{
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		 Plot grafico = new Plot();

		
		List<HP> hpBD = hpService.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		HP hp = hpBD.get(0);
		
		List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
		(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());


		ArrayList<Double> listPeriodosPM = new ArrayList<Double>();
		Map resultado = listarPlPerModsDeTodosOsPeriodosParaUmRecurso(cadPlan,recurso);
		List<PlPerMod> listaPlPerMods = (List<PlPerMod>)resultado.get("plPerMods");
		List<AnaliseRecursoView> listaAnaliseRecursoViews = (List<AnaliseRecursoView> )resultado.get("listaAnaliseRecursoViews");

		//popula lista de Periodos PMs
		for(PerioPM perioPM: perioPMsDoHP){
			listPeriodosPM.add(Double.valueOf(perioPM.getPeriodoPM()));			
		}
				
		listaDadosGrafico.addAll(gerarGraficosViewDeRecursoNecessidadeTotalECapacidadePorPeriodo(listPeriodosPM,listaAnaliseRecursoViews));

		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Periodo", "Pecas");
		
		//retorna o  grafico.
		return grafico;
	}
	/**
	 * Retorna uma string correspondente ao grafico de AvaliacaoRecurso, todo populado e pronto para ser utilizado.
	 * Esse metodo utiliza outros metodos para popular os dados do grafico.
	 * @param cadPlan
	 * @param recurso
	 * @return
	 * @throws AplicacaoException
	 */
	public String imprimirDadosGraficoAvaliacaoRecurso(CadPlan cadPlan, Recurso recurso) throws AplicacaoException{
		 Plot grafico = gerarDadosGraficoAvaliacaoRecurso(cadPlan,recurso);
		return grafico.printData();
	}

	/**
	 * Gera uma lista de PlotData com dados populados sobre o grafico de NecessidadeTotal e Capacidade
	 * por Periodo para a Analise de Recurso.
	 * @param listPeriodosPM
	 * @param listaAnalise
	 * @return
	 */
	public ArrayList<PlotData> gerarGraficosViewDeRecursoNecessidadeTotalECapacidadePorPeriodo(ArrayList<Double> listPeriodosPM, List<AnaliseRecursoView> listaAnalise){
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		ArrayList<Double> listNecessidadeTotais = new ArrayList<Double>();
		ArrayList<Double> listCapacidades = new ArrayList<Double>();
		
		for(int i=0;i<listPeriodosPM.size();i++){
			AnaliseRecursoView analiseRecursoViewCorrente = listaAnalise.get(i);
			//adciona o valor de cada necessidadeTotal para cada periodo 
			//na lista de necessidadesTotais
			listNecessidadeTotais.add(analiseRecursoViewCorrente.getNecessidadeTotal());			
			//adciona o valor de cada Capacidade para cada periodo 
			//na lista de capacidade
			listCapacidades.add(analiseRecursoViewCorrente.getCapacidade());
		}

		//gera o Dado de grafico de necessidadeTotal por periodo
		PlotData dadoNecessidadeTotalPeriodo = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listNecessidadeTotais);
		dadoNecessidadeTotalPeriodo.setLabel("Necessidade Total");
		listaDadosGrafico.add(dadoNecessidadeTotalPeriodo);
		//gera o Dado de grafico de Capacidade por periodo
		PlotData dadoCapacidadePeriodo = dadosGraficoViewService.gerarPlotData(listPeriodosPM, listCapacidades);
		dadoCapacidadePeriodo.setLabel("Capacidade");
		listaDadosGrafico.add(dadoCapacidadePeriodo);				
		return listaDadosGrafico;		
	}

	/**
	 * Lista todos os plPerMods de Todos os periodos do HP
	 * calcula a capacidade, necessidade total  e todas as outras informacoes de analiseMaquinaView
	 * Retorna um map com a lista de plpermods do periodo, e com a lista de AnaliseMaquinaView para cada periodo.
	 * @param cadPlan
	 * @return
	 * @throws AplicacaoException 
	 */
	public Map listarPlPerModsDeTodosOsPeriodosParaAnaliseMaquina(CadPlan cadPlan) throws AplicacaoException{

		Map resultado = new HashMap();
		List<HP> hpBD = hpService.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		HP hp = hpBD.get(0);
		
		List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
		(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());

		List<PlPerMod> plPerMods = new LinkedList<PlPerMod>();
		ArrayList<AnaliseMaquinaView> listaAnaliseMaquinaViews = new ArrayList<AnaliseMaquinaView>();		
		
		List<PlPerMod> plPerModsDefasados = new ArrayList<PlPerMod>();

		List<CapacDia> capacDias = new ArrayList<CapacDia>(capacDiaService.recuperaListaDeCapacDias());
		
		try {
			for (PlanoModelo planoModelo : cadPlan.getPlanosModelo()) {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				plPerMods.addAll(planoModelo.getPlPerMods());
			}	
			Collections.sort(plPerMods);     // Lista ORDENADA de todos os PlPerMods relativos a um CadPlan específico.
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		for(PerioPM perioPM : perioPMsDoHP){
			AnaliseMaquinaView analiseMaquinaView = new AnaliseMaquinaView();	
			
			//******************************* Calculo da capacidade *******************************
			// Calculo similiar ao realizado em calcularAnaliseMaquina em AvaliacaoActions

			Double capacidade = capacDias.get(perioPM.getPeriodoPM() - 1).getCapacProdDiariaEmMin();
			//******************************* Calculo da capacidade *******************************

			
			//******************************* Calculo da necessidadeTotalDoPeriodo *******************************
			double necessidadeTotalDoPeriodo=0.0;
			// Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, para ANALISE DE MAQUINA
			List<PlPerMod> plPerModsDoPeriodo = defasarProducaoPlPerModsNoPeriodoAnaliseMaquina(plPerMods, perioPM);
			if (!plPerModsDoPeriodo.isEmpty()){
				
				// A Necessidade Total do Periodo é a soma de todos os valores de 'ProdDiariaLoteMin' dos PlPerMods
				for (PlPerMod plPerMod : plPerModsDoPeriodo) {
					necessidadeTotalDoPeriodo += plPerMod.getProdDiariaLoteMin();
				}
				
				// Com isso, cada um dos PlPerMods possui sua participação percentual na Necessidade Total do Período
				for (PlPerMod plPerMod : plPerModsDoPeriodo) {
					plPerMod.setParticipacaoPercentual((plPerMod.getProdDiariaLoteMin()/necessidadeTotalDoPeriodo)*100);
				}
				
			}
			//******************************* Calculo da necessidadeTotalDoPeriodo *******************************


			//******************************* Salvo infos Em AnaliseMaquinaView *******************************
			analiseMaquinaView.setNecessidadeTotal(necessidadeTotalDoPeriodo);
			analiseMaquinaView.setCapacidade(capacidade);
			analiseMaquinaView.setComprometimentoMinutos(capacidade - necessidadeTotalDoPeriodo);
			analiseMaquinaView.setComprometimentoPercentual((necessidadeTotalDoPeriodo*100)/capacidade);
			
			//******************************* Salvo infos Em AnaliseMaquinaView *******************************
			listaAnaliseMaquinaViews.add(analiseMaquinaView);
			plPerModsDefasados.addAll(plPerModsDoPeriodo); 
			
		}

		resultado.put("plPerMods" , plPerMods);
		resultado.put("listaAnaliseMaquinaViews" , listaAnaliseMaquinaViews);

		return resultado;
	}

	/**
	 * Gera o grafico de AvaliacaoMaquina, todo populado e pronto para ser utilizado.
	 * Esse metodo utiliza outros metodos para popular os dados do grafico.
	 * @param cadPlan
	 * @return
	 * @throws AplicacaoException
	 */
	public Plot gerarDadosGraficoAvaliacaoMaquina(CadPlan cadPlan) throws AplicacaoException{
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		 Plot grafico = new Plot();

		
		List<HP> hpBD = hpService.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		HP hp = hpBD.get(0);
		
		List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
		(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());


		ArrayList<Double> listPeriodosPM = new ArrayList<Double>();
		Map resultado = listarPlPerModsDeTodosOsPeriodosParaAnaliseMaquina(cadPlan);
		List<PlPerMod> listaPlPerMods = (List<PlPerMod>)resultado.get("plPerMods");
		List<AnaliseMaquinaView> listaAnaliseMaquinaViews = (List<AnaliseMaquinaView> )resultado.get("listaAnaliseMaquinaViews");

		//popula lista de Periodos PMs
		for(PerioPM perioPM: perioPMsDoHP){
			listPeriodosPM.add(Double.valueOf(perioPM.getPeriodoPM()));			
		}
				
		listaDadosGrafico.addAll(gerarGraficosViewDeMaquinaNecessidadeTotalECapacidadePorPeriodo(listPeriodosPM,listaAnaliseMaquinaViews));

		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Periodo", "Min");
		

		//retorna o  grafico.
		return grafico;
	}
	

	/**
	 * Gera uma lista de PlotData com dados populados sobre o grafico de NecessidadeTotal e Capacidade
	 * por Periodo para a Analise de Maquina.
	 * @param listPeriodosPM
	 * @param listaAnalise
	 * @return
	 */
	public ArrayList<PlotData> gerarGraficosViewDeMaquinaNecessidadeTotalECapacidadePorPeriodo(ArrayList<Double> listPeriodosPM, List<AnaliseMaquinaView> listaAnalise){
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		ArrayList<Double> listNecessidadeTotais = new ArrayList<Double>();
		ArrayList<Double> listCapacidades = new ArrayList<Double>();
		
		for(int i=0;i<listPeriodosPM.size();i++){
			AnaliseMaquinaView analiseMaquinaViewCorrente = listaAnalise.get(i);
			//adciona o valor de cada necessidadeTotal para cada periodo 
			//na lista de necessidadesTotais
			listNecessidadeTotais.add(analiseMaquinaViewCorrente.getNecessidadeTotal());			
			//adciona o valor de cada Capacidade para cada periodo 
			//na lista de Capacidades
			listCapacidades.add(analiseMaquinaViewCorrente.getCapacidade());
		}

		//gera o Dado de grafico de necessidadeTotal por periodo
		PlotData dadoNecessidadeTotalPeriodo = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listNecessidadeTotais);
		dadoNecessidadeTotalPeriodo.setLabel("Necessidade Total");
		listaDadosGrafico.add(dadoNecessidadeTotalPeriodo);
		//gera o Dado de grafico de Capacidade por periodo
		PlotData dadoCapacidadePeriodo = dadosGraficoViewService.gerarPlotData(listPeriodosPM, listCapacidades);
		dadoCapacidadePeriodo.setLabel("Capacidade");
		listaDadosGrafico.add(dadoCapacidadePeriodo);				
		return listaDadosGrafico;		
	}
	
}
