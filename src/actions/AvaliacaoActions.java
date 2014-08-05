 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import modelo.AnaliseMaquinaView;
import modelo.AnaliseRecursoView;
import modelo.AnaliseTecidoView;
import modelo.CadPlan;
import modelo.CapacDia;
import modelo.CapacRec;
import modelo.HP;
import modelo.Modelo;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import modelo.Recurso;
import modelo.Tecido;

import br.blog.arruda.plot.Plot;

import org.jboss.util.NotImplementedException;

import service.AvaliacaoAppService;
import service.CadPlanAppService;
import service.CapacDiaAppService;
import service.CapacRecAppService;
import service.HPAppService;
import service.ModeloAppService;
import service.PerioPMAppService;
import service.PlPerModAppService;
import service.PlanoModeloAppService;
import service.RecursoAppService;
import service.TecidoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.exception.ObjetoNaoEncontradoException;

public class AvaliacaoActions extends BaseActions implements Serializable {
	
	// Services
	private static HPAppService hpService;
	private static CadPlanAppService cadPlanService;
	private static TecidoAppService tecidoService;
	private static RecursoAppService recursoService;
	private static PlanoModeloAppService planoModeloService;
	private static PerioPMAppService perioPMService;
	private static CapacDiaAppService capacDiaService;
	private static AvaliacaoAppService avaliacaoService;
	private static CapacRecAppService capacRecAppService;

	// Paginas
	public final String PAGINA_LIST_CADPLAN_AVALIACAO = "listCadPlanAvaliacao";
	public final String PAGINA_LIST_RECURSO = "listAvaliacaoRecurso";
	public final String PAGINA_LIST_TECIDO = "listAvaliacaoTecido";
	public final String PAGINA_LIST_PERIOPM_MAQUINA = "listPerioPMMaquina";
	public final String PAGINA_LIST_PERIOPM_TECIDO = "listPerioPMTecido";
	public final String PAGINA_LIST_PERIOPM_RECURSO = "listPerioPMRecurso";
	public final String PAGINA_LIST_RESULTADOS_AVALIACAO = "list";

	public final String PAGINA_GRAFICO_TECIDO = "graficoAvaliacaoTecido";
	public final String PAGINA_GRAFICO_RECURSO = "graficoAvaliacaoRecurso";
	public final String PAGINA_GRAFICO_MAQUINA = "graficoAvaliacaoMaquina";
	
	// Componentes de Controle
	private DataModel listaCadPlan;
	private DataModel listaTecido;
	private DataModel listaRecurso;
	private DataModel listaPerioPM;
	private DataModel listaPlPerMods;
	private DataModel listaAnaliseMaquina;
	private DataModel listaAnaliseTecido;
	private DataModel listaAnaliseRecurso;
	
	// Variaveis de Tela
	private CadPlan cadPlanCorrente;
	private PerioPM perioPMCorrente;
	private Tecido tecidoCorrente;
	private Recurso recursoCorrente;
	private HP hpCorrente;
	private Plot plotTecido;
	private Plot plotRecurso;
	private Plot plotMaquina;
	
	private int pagina = 1;
	private int paginaTecido = 1;
	private int paginaRecurso = 1;
	private int paginaPerioPM = 1;
	
	private boolean renderizarResultados;
	private Double necessidadeTotalDoPeriodo = 0.0;
	private String opcaoAnaliseCapacidade;
	
	private List<CapacDia> capacDias;
	private List<PlPerMod> plPerMods;
	private List<PlPerMod> plPerModsDoPeriodo;
	
	// Opcoes de Redirecionamento
	final static String OPCAO_ANALISE_MAQUINA = "maquina";
	final static String OPCAO_ANALISE_TECIDO = "tecido";
	final static String OPCAO_ANALISE_RECURSO = "recurso";
	
	//Opcoes de Impressoes de Relatorio
	private String opcaoRelatorioEscolhido = "tecido";
	
	
	
	public AvaliacaoActions() throws Exception {
		
		try {
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
			avaliacaoService = FabricaDeAppService.getAppService(AvaliacaoAppService.class);
			capacRecAppService = FabricaDeAppService.getAppService(CapacRecAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		hpCorrente = new HP();
	}
	
	public String analisarDisponibilidade(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		hpCorrente = hpService.recuperaListaDeHP().get(0);
		
		if (opcaoAnaliseCapacidade.equals(OPCAO_ANALISE_MAQUINA)){
			return PAGINA_LIST_PERIOPM_MAQUINA;
			
		} else if (opcaoAnaliseCapacidade.equals(OPCAO_ANALISE_TECIDO)){
			return PAGINA_LIST_TECIDO;
		
		} else {
			return PAGINA_LIST_RECURSO;
		}
	}
	
	public String analisarDisponibilidadeTecido(){
		
		tecidoCorrente = (Tecido) listaTecido.getRowData();
		
		return PAGINA_LIST_PERIOPM_TECIDO;
	}
	
	public String analisarDisponibilidadeRecurso(){
		
		recursoCorrente = (Recurso) listaRecurso.getRowData();
		
		return PAGINA_LIST_PERIOPM_RECURSO;
	}
	
	/**
	 * 	Este método é utilizado para atualizar os principais atributos inerentes à operação de Análise de Máquina 
	 * cada vez que o usuario deseja exibir uma nova listagem de resultados, para outros Periodos.
	 */
	public void resetarAtributos(){
		necessidadeTotalDoPeriodo = null;
		listaPlPerMods = null;
		listaAnaliseMaquina = null;
		listaAnaliseTecido = null;
		listaAnaliseRecurso = null;
		renderizarResultados = false;
		plPerModsDoPeriodo = null;
	}
	/**
	 * @author bruno.oliveira
	 * <br />
	 * Método que futuramente será implementado para o cálculo de rateio das avaliações de máquina, tecido e recurso
	 * <br />
	 * Atualmente o método aciona uma mensagem de aviso ao usário que o informa da não implementação do método.<br />
	 * O retorno é null, pois o mesmo método é chamado por máquina, tecido e recurso, impossibilitando o retorno de
	 * de uma página especifíca. O retorno null faz com que a página se mantenha.
	 * @return null
	 */
	public String calcularRateio(){
		error("erro.FUNCAO_NAO_IMPLEMENTADA");
		// Usamos return null para manter a página inalterada, o motivo se deve ao fato de que o método 
		// é chamador por tecido, máquina e recurso, o que impossibilita o retorno a uma página específica.
		return null;
	}
	
	/**
	 * Gera os relatorios Pdfs para Analise de maquina, tecido e recurso
	 * dependendo do valor da variavel global opcaoRelatorioEscolhido.
	 * 
	 * @return
	 */
	public String geraRelatorioPdf(){
		
		if(this.opcaoRelatorioEscolhido.equals(OPCAO_ANALISE_MAQUINA)){
			try {
				avaliacaoService.gerarRelatorioAvaliacaoMaquina(cadPlanCorrente, plPerMods);
			} catch (AplicacaoException e) {
				error(e.getMessage());
			}
		}
		else if(this.opcaoRelatorioEscolhido.equals(OPCAO_ANALISE_TECIDO)){
			try {
				avaliacaoService.gerarRelatorioAvaliacaoTecido(cadPlanCorrente, plPerMods, tecidoCorrente);
			} catch (AplicacaoException e) {
				error(e.getMessage());
			}
		}
		else if(this.opcaoRelatorioEscolhido.equals(OPCAO_ANALISE_RECURSO)){
			try {
				avaliacaoService.gerarRelatorioAvaliacaoRecurso(cadPlanCorrente, plPerMods, recursoCorrente);
			} catch (AplicacaoException e) {
				error(e.getMessage());
			}
		}
		
		return PAGINA_LIST_RESULTADOS_AVALIACAO;
	}
	
	/**
	 * Prepara a lista de plPerMods de um determinado periodo para apresentar os
	 * dados necessarios na tela de avaliacao de tecido.
	 * 
	 * @return
	 */
	public String preparaListagemAvaliacaoTecido(){
		
		perioPMCorrente = (PerioPM) listaPerioPM.getRowData();
		
		plPerMods = new LinkedList<PlPerMod>();
		necessidadeTotalDoPeriodo=0.0;
		
		try {
			for (PlanoModelo planoModelo : cadPlanCorrente.getPlanosModelo()) {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				plPerMods.addAll(planoModelo.getPlPerMods());
			}	
			Collections.sort(plPerMods);     // Lista ORDENADA de todos os PlPerMods relativos a um CadPlan específico.
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		// Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, para ANALISE DE TECIDO
		plPerModsDoPeriodo = avaliacaoService.defasarProducaoPlPerModsNoPeriodoAnaliseTecido(plPerMods, perioPMCorrente, tecidoCorrente);
		
		if (!plPerModsDoPeriodo.isEmpty()){
			
			// A Necessidade Total do Periodo é a soma de todos os valores de 'consumoDiarioKg' dos PlPerMods
			for (PlPerMod plPerMod : plPerModsDoPeriodo) {
				necessidadeTotalDoPeriodo += plPerMod.getConsumoDiarioKg();
			}
			
			// Com isso, cada um dos PlPerMods possui sua participação percentual na Necessidade Total do Período
			for (PlPerMod plPerMod : plPerModsDoPeriodo) {
				plPerMod.setParticipacaoPercentual((plPerMod.getConsumoDiarioKg()/necessidadeTotalDoPeriodo)*100);
			}
			
			renderizarResultados = true;
			
		} else {
			
			// Caso em que a defasagem buscou valores em um período em que não existe nenhum Modelo com Inicio de Produção planejado
			renderizarResultados = false;
			
			warn("avaliacao.MODELOS_NAO_ENCONTRADOS");
		}
		
		listaPlPerMods = null;		// Obrigando a listaPlPerMods a se atualizar
		
		return PAGINA_LIST_RESULTADOS_AVALIACAO;
	}
	
	/**
	 * Prepara a lista de plPerMods de um determinado período para apresentar os dados necessários 
	 * na tela de avaliação de recurso.
	 * (Alterado por: BBF/DMA)
	 *  
	 *  @return
	 */
	public String preparaListagemAvaliacaoRecurso(){
		
		perioPMCorrente = (PerioPM) listaPerioPM.getRowData();
		
		plPerMods = new LinkedList<PlPerMod>();
		
		necessidadeTotalDoPeriodo=0.0;
		try {
			for (PlanoModelo planoModelo : cadPlanCorrente.getPlanosModelo()) {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				plPerMods.addAll(planoModelo.getPlPerMods());
			}	
			Collections.sort(plPerMods);     // Lista ORDENADA de todos os PlPerMods relativos a um CadPlan específico.
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		// Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, para ANALISE DE RECURSO
		plPerModsDoPeriodo = avaliacaoService.defasarProducaoPlPerModsNoPeriodoAnaliseRecurso(plPerMods, perioPMCorrente, recursoCorrente);
		
		if (!plPerModsDoPeriodo.isEmpty()){
			
			// A Necessidade Total do Periodo é a soma de todos os valores de 'consumoDiario' dos PlPerMods
			for (PlPerMod plPerMod : plPerModsDoPeriodo) {
				necessidadeTotalDoPeriodo += plPerMod.getConsumoDiario();
			}
			
			// Com isso, cada um dos PlPerMods possui sua participação percentual na Necessidade Total do Período
			for (PlPerMod plPerMod : plPerModsDoPeriodo) {
				plPerMod.setParticipacaoPercentual((plPerMod.getConsumoDiario()/necessidadeTotalDoPeriodo)*100);
			}
			
			renderizarResultados = true;
			
		} else {
			
			// Caso em que a defasagem buscou valores em um período em que não existe nenhum Modelo com Inicio de Produção planejado
			renderizarResultados = false;
			
			warn("avaliacao.MODELOS_NAO_ENCONTRADOS");
		}
		
		listaPlPerMods = null;		// Obrigando a listaPlPerMods a se atualizar
		
		return PAGINA_LIST_RESULTADOS_AVALIACAO;
	}
	
	
	public String preparaListagemAvaliacaoMaquina(){
		
		perioPMCorrente = (PerioPM) listaPerioPM.getRowData();
		
		plPerMods = new LinkedList<PlPerMod>();
		capacDias = new ArrayList<CapacDia>(capacDiaService.recuperaListaDeCapacDias());
		necessidadeTotalDoPeriodo=0.0;
		try {
			for (PlanoModelo planoModelo : cadPlanCorrente.getPlanosModelo()) {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				plPerMods.addAll(planoModelo.getPlPerMods());
			}	
			Collections.sort(plPerMods);     // Lista ORDENADA de todos os PlPerMods relativos a um CadPlan específico.
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		// Rotina que calcula a produção defasada dos PlPerMods em um determinado Período, para ANALISE DE MAQUINA
		plPerModsDoPeriodo = avaliacaoService.defasarProducaoPlPerModsNoPeriodoAnaliseMaquina(plPerMods, perioPMCorrente);
		
		if (!plPerModsDoPeriodo.isEmpty()){
			
			// A Necessidade Total do Periodo é a soma de todos as 'prodDiariaLoteMin' dos PlPerMods
			for (PlPerMod plPerMod : plPerModsDoPeriodo) {
				necessidadeTotalDoPeriodo += plPerMod.getProdDiariaLoteMin();
			}
			
			// Com isso, cada um dos PlPerMods possui sua participação percentual na Necessidade Total do Período
			for (PlPerMod plPerMod : plPerModsDoPeriodo) {
				plPerMod.setParticipacaoPercentual((plPerMod.getProdDiariaLoteMin()/necessidadeTotalDoPeriodo)*100);
			}
			
			renderizarResultados = true;
			
		} else {
			
			// Caso em que a defasagem buscou valores em um período em que não existe nenhum Modelo com Inicio de Produção planejado
			renderizarResultados = false;
			
			warn("avaliacao.MODELOS_NAO_ENCONTRADOS");
		}
		
		listaPlPerMods = null;		// Obrigando a listaPlPerMods a se atualizar
		
		return PAGINA_LIST_RESULTADOS_AVALIACAO;
	}

	public String voltarPaginaListagemPerioPM(){
		
		resetarAtributos();
		
		if (opcaoAnaliseCapacidade.equals(OPCAO_ANALISE_MAQUINA)){
			return PAGINA_LIST_PERIOPM_MAQUINA;
			
		} else if (opcaoAnaliseCapacidade.equals(OPCAO_ANALISE_TECIDO)){
			return PAGINA_LIST_PERIOPM_TECIDO;
		
		} else {
			return PAGINA_LIST_PERIOPM_RECURSO;
		}
	}
	
	public String voltarPaginaListagemDeRecursoOuTecido(){
		
		resetarAtributos();
		
		if (opcaoAnaliseCapacidade.equals(OPCAO_ANALISE_TECIDO)){
			return PAGINA_LIST_TECIDO;
		
		} else {
			return PAGINA_LIST_RECURSO;
		}
	}
	
	public String voltarPaginaListagemCadPlan(){
		resetarAtributos();
		
		return PAGINA_LIST_CADPLAN_AVALIACAO;
	}
	
	public List<AnaliseMaquinaView> calcularAnaliseMaquina(){
		
		List<AnaliseMaquinaView> listAnaliseMaquina = new ArrayList<AnaliseMaquinaView>(1);
		AnaliseMaquinaView analiseMaquinaView = new AnaliseMaquinaView();
		
		Double capacidade = capacDias.get(perioPMCorrente.getPeriodoPM() - 1).getCapacProdDiariaEmMin();
		
		analiseMaquinaView.setNecessidadeTotal(necessidadeTotalDoPeriodo);
		analiseMaquinaView.setCapacidade(capacidade);
		analiseMaquinaView.setComprometimentoMinutos(capacidade-necessidadeTotalDoPeriodo);
		analiseMaquinaView.setComprometimentoPercentual((necessidadeTotalDoPeriodo*100)/capacidade);
		
		listAnaliseMaquina.add(analiseMaquinaView);
		
		return listAnaliseMaquina;
	}
	
	/**
	 * Popula os campos calculados de Analise de tecido.
	 * @return
	 */
	public List<AnaliseTecidoView> calcularAnaliseTecido(){
		
		List<AnaliseTecidoView> listAnaliseTecido = new ArrayList<AnaliseTecidoView>(1);
		AnaliseTecidoView analiseTecidoView = new AnaliseTecidoView();
		
		// Calculo similiar ao realizado em CapacTecView
		Double disponibilidadeMaxDiaria = 
			(tecidoCorrente.getProducaoDiariaMaxUnidade2() * perioPMCorrente.getNumDiasUteisU2())/perioPMCorrente.getNumDiasUteisMatriz();
		
		analiseTecidoView.setNecessidadeTotal(necessidadeTotalDoPeriodo);
		analiseTecidoView.setDisponibilidadeMaxDiaria(disponibilidadeMaxDiaria);
		analiseTecidoView.setComprometimentoKg(disponibilidadeMaxDiaria - necessidadeTotalDoPeriodo);
		analiseTecidoView.setComprometimentoPercentual((necessidadeTotalDoPeriodo*100)/disponibilidadeMaxDiaria);
		
		listAnaliseTecido.add(analiseTecidoView);
		
		return listAnaliseTecido;
	}
	
	public List<AnaliseRecursoView> calcularAnaliseRecurso(){
		
		List<AnaliseRecursoView> listAnaliseRecurso = new ArrayList<AnaliseRecursoView>(1);
		AnaliseRecursoView analiseRecursoView = new AnaliseRecursoView();
		
		CapacRec capacRec = null;
		
		try {
			capacRec = capacRecAppService.recuperaCapacRecPorRecursoEPerioPM(recursoCorrente, perioPMCorrente);
		} catch (AplicacaoException e) {
		}
		
		Double capacidadeDiaria = capacRec.getCapacDiaria();
		Double custoTotal = 0.0;
		
		for (PlPerMod plPerMod : plPerModsDoPeriodo) {
			custoTotal += plPerMod.getCustoDiario();
		}
		
		analiseRecursoView.setNecessidadeTotal(necessidadeTotalDoPeriodo);
		analiseRecursoView.setCapacidade(capacidadeDiaria);
		analiseRecursoView.setComprometimento(capacidadeDiaria - necessidadeTotalDoPeriodo);
		analiseRecursoView.setComprometimentoPercentual(necessidadeTotalDoPeriodo*100 / capacidadeDiaria);
		analiseRecursoView.setCustoTotal(custoTotal);   
		
		listAnaliseRecurso.add(analiseRecursoView);
		
		return listAnaliseRecurso;
	}	


	public String exibirGraficoTecido(){
		
		try {
			plotTecido = avaliacaoService.gerarDadosGraficoAvaliacaoTecido(cadPlanCorrente, tecidoCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST_PERIOPM_TECIDO;
		}
		return PAGINA_GRAFICO_TECIDO;
	}

	public String exibirGraficoRecurso(){
		
		try {
			
			plotRecurso =  avaliacaoService.gerarDadosGraficoAvaliacaoRecurso(cadPlanCorrente, recursoCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST_PERIOPM_RECURSO;
		}
		return PAGINA_GRAFICO_RECURSO;
	}
	
	public String exibirGraficoMaquina(){
		
		try {
			
			plotMaquina = avaliacaoService.gerarDadosGraficoAvaliacaoMaquina(cadPlanCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST_PERIOPM_MAQUINA;
		}
		return PAGINA_GRAFICO_MAQUINA;
	}
	
	// ================================== Métodos get() e set() ================================== //

	public DataModel getListaCadPlan() {
		
		if (listaCadPlan == null){
			listaCadPlan = new ListDataModel(cadPlanService.recuperaListaDeCadPlanPorUsuario(sessaoUsuarioCorrente.getUsuarioLogado()));
		}
		return listaCadPlan;
	}
	
	public CadPlan getCadPlanCorrente() {
		return cadPlanCorrente;
	}
	
	public void setListaCadPlan(DataModel listaCadPlan) {
		this.listaCadPlan = listaCadPlan;
	}
	
	public void setCadPlanCorrente(CadPlan cadPlanCorrente) {
		this.cadPlanCorrente = cadPlanCorrente;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public int getPagina() {
		return pagina;
	}

	public void setListaPlPerMods(DataModel listaPlPerMods) {
		this.listaPlPerMods = listaPlPerMods;
	}

	public DataModel getListaPlPerMods() {
		
		if (listaPlPerMods == null){
			listaPlPerMods = new ListDataModel(plPerModsDoPeriodo);
		}
		
		return listaPlPerMods;
	}

	public void setListaPerioPM(DataModel listaPerioPM) {
		this.listaPerioPM = listaPerioPM;
	}

	public DataModel getListaPerioPM() {
		
		if (listaPerioPM == null){
			
			List<PerioPM> perioPMsPMP = perioPMService.recuperaIntervaloDePerioPMs
				(hpCorrente.getPerioPMInicPMP().getPeriodoPM(), hpCorrente.getPerioPMFinalPMP().getPeriodoPM());
			
			listaPerioPM = new ListDataModel(perioPMsPMP);
		}
		
		return listaPerioPM;
	}

	public void setHpCorrente(HP hpCorrente) {
		this.hpCorrente = hpCorrente;
	}

	public HP getHpCorrente() {
		return hpCorrente;
	}

	public void setPlPerMods(List<PlPerMod> plPerMods) {
		this.plPerMods = plPerMods;
	}

	public List<PlPerMod> getPlPerMods() {
		return plPerMods;
	}

	public void setListaAnaliseMaquina(DataModel listaAnaliseMaquina) {
		this.listaAnaliseMaquina = listaAnaliseMaquina;
	}

	public void setCapacDias(List<CapacDia> capacDias) {
		this.capacDias = capacDias;
	}
	
	public List<CapacDia> getCapacDias() {
		return capacDias;
	}
	
	public DataModel getListaAnaliseMaquina() {
		
		if (listaAnaliseMaquina == null){
			listaAnaliseMaquina = new ListDataModel(calcularAnaliseMaquina());
		}
		
		return listaAnaliseMaquina;
	}

	public void setNecessidadeTotalDoPeriodo(Double necessidadeTotalDoPeriodo) {
		this.necessidadeTotalDoPeriodo = necessidadeTotalDoPeriodo;
	}

	public Double getNecessidadeTotalDoPeriodo() {
		if(necessidadeTotalDoPeriodo == null){
			necessidadeTotalDoPeriodo =0.0;
		}
		return necessidadeTotalDoPeriodo;
	}

	public void setPlPerModsDoPeriodo(List<PlPerMod> plPerModsDoPeriodo) {
		this.plPerModsDoPeriodo = plPerModsDoPeriodo;
	}

	public List<PlPerMod> getPlPerModsDoPeriodo() {
		return plPerModsDoPeriodo;
	}

	public void setPerioPMCorrente(PerioPM perioPMCorrente) {
		this.perioPMCorrente = perioPMCorrente;
	}

	public PerioPM getPerioPMCorrente() {
		return perioPMCorrente;
	}

	public void setRenderizarResultados(boolean renderizarResultados) {
		this.renderizarResultados = renderizarResultados;
	}

	public boolean isRenderizarResultados() {
		return renderizarResultados;
	}

	public void setPaginaPerioPM(int paginaPerioPM) {
		this.paginaPerioPM = paginaPerioPM;
	}

	public int getPaginaPerioPM() {
		return paginaPerioPM;
	}

	public void setListaTecido(DataModel listaTecido) {
		this.listaTecido = listaTecido;
	}

	public DataModel getListaTecido() {
		
		if (listaTecido == null){
			listaTecido = new ListDataModel(tecidoService.recuperaListaPaginadaDeTecidos());
		}
		
		return listaTecido;
	}

	public void setListaRecurso(DataModel listaRecurso) {
		this.listaRecurso = listaRecurso;
	}

	public DataModel getListaRecurso() {
		
		if (listaRecurso == null){
			listaRecurso = new ListDataModel(recursoService.recuperaListaPaginadaDeRecursos());
		}
		
		return listaRecurso;
	}

	public void setTecidoCorrente(Tecido tecidoCorrente) {
		this.tecidoCorrente = tecidoCorrente;
	}

	public Tecido getTecidoCorrente() {
		return tecidoCorrente;
	}

	public void setRecursoCorrente(Recurso recursoCorrente) {
		this.recursoCorrente = recursoCorrente;
	}

	public Recurso getRecursoCorrente() {
		return recursoCorrente;
	}

	public void setPaginaTecido(int paginaTecido) {
		this.paginaTecido = paginaTecido;
	}

	public int getPaginaTecido() {
		return paginaTecido;
	}

	public void setPaginaRecurso(int paginaRecurso) {
		this.paginaRecurso = paginaRecurso;
	}

	public int getPaginaRecurso() {
		return paginaRecurso;
	}

	public void setOpcaoAnaliseCapacidade(String opcaoAnaliseCapacidade) {
		this.opcaoAnaliseCapacidade = opcaoAnaliseCapacidade;
	}

	public String getOpcaoAnaliseCapacidade() {
		return opcaoAnaliseCapacidade;
	}

	public void setListaAnaliseTecido(DataModel listaAnaliseTecido) {
		this.listaAnaliseTecido = listaAnaliseTecido;
	}

	public DataModel getListaAnaliseTecido() {
		
		if (listaAnaliseTecido == null){
			listaAnaliseTecido = new ListDataModel(calcularAnaliseTecido());
		}
		
		return listaAnaliseTecido;
	}

	public void setListaAnaliseRecurso(DataModel listaAnaliseRecurso) {
		this.listaAnaliseRecurso = listaAnaliseRecurso;
	}

	public DataModel getListaAnaliseRecurso() {
		
		if (listaAnaliseRecurso == null){
			listaAnaliseRecurso = new ListDataModel(calcularAnaliseRecurso());
		}
		
		return listaAnaliseRecurso;
	}

	public String getOpcaoRelatorioEscolhido() {
		return opcaoRelatorioEscolhido;
	}

	public void setOpcaoRelatorioEscolhido(String opcaoRelatorioEscolhido) {
		this.opcaoRelatorioEscolhido = opcaoRelatorioEscolhido;
	}


	public Plot getPlotTecido() {
		return plotTecido;
	}

	public void setPlotTecido(Plot plotTecido) {
		this.plotTecido = plotTecido;
	}

	public Plot getPlotRecurso() {
		return plotRecurso;
	}

	public void setPlotRecurso(Plot plotRecurso) {
		this.plotRecurso = plotRecurso;
	}

	public Plot getPlotMaquina() {
		return plotMaquina;
	}

	public void setPlotMaquina(Plot plotMaquina) {
		this.plotMaquina = plotMaquina;
	}
}
