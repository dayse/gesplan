 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.ajax4jsf.context.AjaxContext;
import org.jboss.util.NotImplementedException;

import DAO.exception.ObjetoNaoEncontradoException;

import modelo.CadPlan;
import modelo.Familia;
import modelo.HP;
import modelo.ModelagemFuzzy;
import modelo.Modelo;
import modelo.Parametros;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import service.CadPlanAppService;
import service.ExcecaoMensAppService;
import service.HPAppService;
import service.ModelagemFuzzyAppService;
import service.ModeloAppService;
import service.PMPAppService;
import service.ParametrosAppService;
import service.PlPerModAppService;
import service.PlanoModeloAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class CadPlanActions extends BaseActions implements Serializable {
	
	// Services
	private static CadPlanAppService cadPlanService;
	private static ModeloAppService modeloService;
	private static ModelagemFuzzyAppService modelagemFuzzyService;
	private static PMPAppService pmpService;
	private static PlPerModAppService plPerModService;
	private static ExcecaoMensAppService excecaoMensService;
	private static PlanoModeloAppService planoModeloService;
	private static HPAppService hpService;
	private static ParametrosAppService parametrosService;

	// Paginas
	public final String PAGINA_LIST = "listCadPlan";
	public final String PAGINA_NEW  = "newCadPlan";
	public final String PAGINA_NEW_FUZZY  = "newCadPlanFuzzy";
	public final String PAGINA_SHOW = "showCadPlan";
	public final String PAGINA_EDIT = "editCadPlan";
	public final String PAGINA_LIST_PLPERMOD = "listPlPerMod";
	public final String PAGINA_LIST_EXCECAOMENS = "listExcecaoMens";
	public final String PAGINA_LIST_PMP = "listPMP";

	public final String PAGINA_LIST_AVALIACAOFUZZY = "listAvaliacaoFuzzy";
	public final String PAGINA_DETALHAR_AVALIACAOFUZZY = "detalharAvaliacaoFuzzy";
	public final String PAGINA_SELECMODEL_AVALIACAOFUZZY = "selecModelagemAvaliacaoFuzzy";
	
	// Componentes de Controle
	private DataModel listaCadPlan;
	private DataModel listaPlPerModAgregado;
	
	// Variaveis de Tela
	private CadPlan cadPlanCorrente;
	private CadPlan cadPlanCopiado;
	private boolean renderizarTotalizacao = false;
	private int pagina;
	private int opcaoEscolhida;
	private String codPlanCorrente;
	private String descrPlanCorrente;
	
	// Opções de Escolha
	final static int OPCAO_NOVO_CADPLAN = 0;
	final static int OPCAO_COPIA_CADPLAN = 1;
	final static int OPCAO_NOVO_CADPLAN_FUZZY = 2;
	final static int OPCAO_GERAR_EXCECAO = 3;
	final static int OPCAO_MOSTRAR_EXCECAO = 4;
	private static String FINALIDADE_MODELAGEM;
	
	//avaliacao fuzzy
	

	private SelectOneDataModel<ModelagemFuzzy> comboModelagensFuzzy;
	
	public CadPlanActions() throws Exception {
		
		try {
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			modelagemFuzzyService = FabricaDeAppService.getAppService(ModelagemFuzzyAppService.class);
			plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
			pmpService = FabricaDeAppService.getAppService(PMPAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			excecaoMensService =FabricaDeAppService.getAppService(ExcecaoMensAppService.class); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String copiarPlano() {
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		cadPlanCopiado = new CadPlan();
		
		opcaoEscolhida = OPCAO_COPIA_CADPLAN;
		
		return PAGINA_NEW;
	}

	public String ajustarPlano(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		return PAGINA_LIST_PLPERMOD;
	}

	public String gerarExcecaoMens(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}

		//executar service de excecaoMens para gerar as msgs de excecao
		try {
			excecaoMensService.gerarMensagensExcecao(cadPlanCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
		}
		opcaoEscolhida = OPCAO_GERAR_EXCECAO;
		return PAGINA_LIST_EXCECAOMENS;
	}
	
	public String mostrarExcecaoMens(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		opcaoEscolhida = OPCAO_MOSTRAR_EXCECAO;
		return PAGINA_LIST_EXCECAOMENS;
	}
	
	public String detalhar(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		//caso não seja um cadplan com escore calculado.
		if(!cadPlanCorrente.getEscoreCalculado()){
			error("cadplan.NAO_CALCULADO");	
			return PAGINA_LIST_AVALIACAOFUZZY;
		}
		
			
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		return PAGINA_DETALHAR_AVALIACAOFUZZY;
	}
	

	public String implementaPlanoMestre(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try{

			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlanCorrente.getCodPlan());
		}
		catch(ObjetoNaoEncontradoException e){
			error("cadPlan.CADPLAN_NAO_ENCONTRADO");
		}
		
		try{
			pmpService.implementaPlanoMestre(cadPlanCorrente);
		}
		catch(AplicacaoException e){
			error(e.getMessage());
			return PAGINA_LIST;
		}
		
		return PAGINA_LIST_PMP;
	}
	
	
	public String preparaInclusao() {

		List<HP> hpList = hpService.recuperaListaDeHP();
		
		if (hpList.isEmpty()){
			error("hp.NAO_CADASTRADO");
			return PAGINA_LIST;
		}
		
		Parametros parametro = parametrosService.recuperaListaDeParametros().get(0);		
		if (!parametro.isInicPlanejamento()){
			error("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
			return PAGINA_LIST;
		}
		
		try {
			modeloService.recuperaListaDeModelos();
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST;
		}
			
		cadPlanCorrente = new CadPlan();
		
		opcaoEscolhida = OPCAO_NOVO_CADPLAN;
		
		return PAGINA_NEW;
	}

	public String preparaCalcularEscores(){		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();		

		FINALIDADE_MODELAGEM = "Avaliar PMP";


		List<ModelagemFuzzy> modelagemFuzzys = modelagemFuzzyService.recuperaListaPaginadaDeModelagemFuzzysPorFinalidade(FINALIDADE_MODELAGEM);
		
		if (modelagemFuzzys.isEmpty()){
			error("avaliacaoFuzzy.MODELAGEM_NAO_CADASTRADA");
			return PAGINA_LIST_AVALIACAOFUZZY;
		}		
		comboModelagensFuzzy=null;
		
		return PAGINA_SELECMODEL_AVALIACAOFUZZY;
	}
	

	public String calcularEscores(){		

		
		ModelagemFuzzy modelagemFuzzyCorrente = comboModelagensFuzzy.getObjetoSelecionado();

		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModeloEPlPerMods(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		
		try {
			List<PlanoModelo> planosModelos = cadPlanService.converteSetPlanoModelosParaListaPlanoModelos(cadPlanCorrente);
			cadPlanService.calcularEscores(cadPlanCorrente,modelagemFuzzyCorrente.getNomeArquivo(),planosModelos);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST_AVALIACAOFUZZY;
		}
		listaCadPlan = null;
		
		
		return PAGINA_LIST_AVALIACAOFUZZY;
	}
	
	public String preparaInclusaoFuzzy() {

		List<HP> hpList = hpService.recuperaListaDeHP();

		if (hpList.isEmpty()){
			error("hp.NAO_CADASTRADO");
			return PAGINA_LIST;
		}	
		FINALIDADE_MODELAGEM = "Gerar PMP";

		List<ModelagemFuzzy> modelagemFuzzys = modelagemFuzzyService.recuperaListaPaginadaDeModelagemFuzzysPorFinalidade(FINALIDADE_MODELAGEM);
		
		if (modelagemFuzzys.isEmpty()){
			error("modelagemFuzzy.NAO_CADASTRADA");
			return PAGINA_LIST;
		}
		
		try {
			modeloService.recuperaListaDeModelos();
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST;
		}
			
		cadPlanCorrente = new CadPlan();
		
		
		opcaoEscolhida = OPCAO_NOVO_CADPLAN_FUZZY;
		
		comboModelagensFuzzy=null;
		
		return PAGINA_NEW_FUZZY;
	}
	
	public String inclui(){
		
		try {
			
			if (opcaoEscolhida == OPCAO_COPIA_CADPLAN){
				
				cadPlanCopiado.setCodPlan(codPlanCorrente);
				cadPlanCopiado.setDescrPlan(descrPlanCorrente);
				cadPlanCopiado.setUsuario(sessaoUsuarioCorrente.getUsuarioLogado());
				
				cadPlanService.copiarPlano(cadPlanCorrente, cadPlanCopiado);
				
			} else if(opcaoEscolhida == OPCAO_NOVO_CADPLAN){
				
				cadPlanCorrente.setCodPlan(codPlanCorrente);
				cadPlanCorrente.setDescrPlan(descrPlanCorrente);
				cadPlanCorrente.setUsuario(sessaoUsuarioCorrente.getUsuarioLogado());
				
				cadPlanService.inclui(cadPlanCorrente);
				
			}
			else if(opcaoEscolhida == OPCAO_NOVO_CADPLAN_FUZZY) {
				
				cadPlanCorrente.setUsuario(sessaoUsuarioCorrente.getUsuarioLogado());

				cadPlanCorrente.setModelagemFuzzy(comboModelagensFuzzy.getObjetoSelecionado());
				cadPlanService.incluiPMPFuzzy(cadPlanCorrente);
			}
			
		} catch (AplicacaoException e) {
			error(e.getMessage());

			if(opcaoEscolhida == OPCAO_NOVO_CADPLAN_FUZZY){
				
				return preparaInclusaoFuzzy();
				
			}else{
				
				return preparaInclusao();
				
			}
		}
		
		info("cadplan.SUCESSO_INCLUSAO");
		
		listaCadPlan = null;
		codPlanCorrente = null;
		descrPlanCorrente = null;
		
		return PAGINA_LIST;
	}
	
	public String preparaAlteracao(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		return PAGINA_EDIT;
	}
	
	public String altera() {
		
		cadPlanService.altera(cadPlanCorrente);
		
		info("cadplan.SUCESSO_ALTERACAO");
		
		return PAGINA_LIST;
	}
	
	public String mostra(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		return PAGINA_SHOW;
	}
	
	public void preparaExclusao(){
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
	}
	
	
	public String exclui(){
		
		try {
			cadPlanService.exclui(cadPlanCorrente);
		} catch (AplicacaoException ex) {
			error(ex.getMessage());
			return PAGINA_LIST;
		}
		
		info("cadplan.SUCESSO_EXCLUSAO");
		
		listaCadPlan = null;
		
		return PAGINA_LIST;
	}
	
	public String cancelar() {
		
		listaCadPlan = null;
		
		return PAGINA_LIST;
	}
	
	public String cancelarAvaliacaoFuzzy() {
		
		listaCadPlan = null;
		
		return PAGINA_LIST_AVALIACAOFUZZY;
	}
	
	
	public void imprimir(){

		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try{					
			CadPlan cadplan = cadPlanService.recuperaCadPlanApenasComPlanosModelo(cadPlanCorrente.getCodPlan());
			
			if(cadplan.getPlanosModelo().size()==0 || cadplan==null){
				throw new AplicacaoException();						 
			}
			
			//cadPlanService.
			plPerModService.gerarRelatorioAgregado(cadplan);    
			
		} catch (AplicacaoException re){
			error("cadplan.NAO_ENCONTRADO");//Nao existem planos associados a este cadastro de planos.
		}
				
	}
	
	
	@SuppressWarnings("unchecked")
	public String totalizarPlano(){
		
		cadPlanCorrente = (CadPlan) listaCadPlan.getRowData();
		
		try {
			cadPlanCorrente = cadPlanService.recuperaCadPlanComPlanosModeloEPlPerMods(cadPlanCorrente.getCodPlan());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		pagina = ((List<CadPlan>) listaCadPlan.getWrappedData()).indexOf(cadPlanCorrente) + 1;
		
		listaPlPerModAgregado = new ListDataModel(plPerModService.recuperaListaPlPerModAgregadoPorCadPlan(cadPlanCorrente));
		
		renderizarTotalizacao = true;
		
		return PAGINA_LIST;
	}
	
	public void atualizarPlanos(){
		renderizarTotalizacao = false;
	}
	
	public String actionAjax(ActionEvent ev){
		
		System.out.println("---------------------------------------------------------------------");
		
		Map atributosDoRequest = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
		Set<String> teste = atributosDoRequest.keySet();
		for (String chave : teste) {
			System.out.println("chave: "+chave+" | valor: "+atributosDoRequest.get(chave));
		}
		
		AjaxContext ajaxContext = (AjaxContext)atributosDoRequest.get("ajaxContext");
		Map atribAjax = ajaxContext.getResponseDataMap();
		
		System.out.println(" <<<<<<<< TESTE AJAX - Walanem >>>>>>>>> ");
		
		Object atributosAjax = ajaxContext.getResponseData();
		
		String areaSubmitada = ajaxContext.getSubmittedRegionClientId();
		System.out.println("AREA SUBMITADA VIA AJAX = " + areaSubmitada);
		
		System.out.println(" <<<<<<<< FIM - TESTE AJAX - Walanem >>>>>>>>> ");
		
		Set<String> key = atribAjax.keySet();
		System.out.println("ATRIBUTOS DO AJAX");
		
		for (String string : key) {
			System.out.println("chave: "+string+" | valor: "+atribAjax.get(string));
		}
		
		System.out.println("---------------------------------------------------------------------");
		
		
		return PAGINA_LIST;
	}

	public void geraRelatorioPdf(){
		throw new NotImplementedException();
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

	public void setListaPlPerModAgregado(DataModel listaPlPerModAgregado) {
		this.listaPlPerModAgregado = listaPlPerModAgregado;
	}

	public DataModel getListaPlPerModAgregado() {
		return listaPlPerModAgregado;
	}

	public void setRenderizarTotalizacao(boolean renderizarTotalizacao) {
		this.renderizarTotalizacao = renderizarTotalizacao;
	}

	public boolean isRenderizarTotalizacao() {
		return renderizarTotalizacao;
	}

	public void setOpcaoEscolhida(int opcaoEscolhida) {
		this.opcaoEscolhida = opcaoEscolhida;
	}

	public int getOpcaoEscolhida() {
		return opcaoEscolhida;
	}

	public String getCodPlanCorrente() {
		return codPlanCorrente;
	}

	public String getDescrPlanCorrente() {
		return descrPlanCorrente;
	}

	public void setCodPlanCorrente(String codPlanCorrente) {
		this.codPlanCorrente = codPlanCorrente;
	}

	public void setDescrPlanCorrente(String descrPlanCorrente) {
		this.descrPlanCorrente = descrPlanCorrente;
	}

	/**
	 * Método que cria a Combobox que lista as ModelagemFuzzy cadastradas no sistema.
	 * 
	 * @author felipe.arruda
	 */
	public SelectOneDataModel<ModelagemFuzzy> getComboModelagensFuzzy() {
		
		if (comboModelagensFuzzy == null){
			comboModelagensFuzzy = SelectOneDataModel.criaSemTextoInicial(modelagemFuzzyService.recuperaListaPaginadaDeModelagemFuzzysPorFinalidade(FINALIDADE_MODELAGEM));	
		}
		
		return comboModelagensFuzzy;
	}

	public void setComboModelagensFuzzy(SelectOneDataModel<ModelagemFuzzy> comboModelagensFuzzy) {
		this.comboModelagensFuzzy = comboModelagensFuzzy;
	}

	public static String getFINALIDADE_MODELAGEM() {
		return FINALIDADE_MODELAGEM;
	}

	public static void setFINALIDADE_MODELAGEM(String fINALIDADEMODELAGEM) {
		FINALIDADE_MODELAGEM = fINALIDADEMODELAGEM;
	}
	
}
