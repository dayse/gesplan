 
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import modelo.Familia;
import modelo.Modelo;
import modelo.ModeloDecorado;
import modelo.Parametros;
import modelo.PerioPM;
import service.CadPlanAppService;
import service.FamiliaAppService;
import service.ModeloAppService;
import service.ParametrosAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.DataUtil;
import util.SelectOneDataModel;

public class ModeloActions extends BaseActions implements Serializable {
	
	// Services
	private static ModeloAppService modeloService;
	private static CadPlanAppService cadPlanService;
	private static FamiliaAppService familiaService;
	private static ParametrosAppService parametrosService;

	// Paginas
	public final String PAGINA_NEW  = "newModelo";
	public final String PAGINA_LIST = "listModelo";
	public final String PAGINA_SHOW = "showModelo";
	public final String PAGINA_EDIT = "editModelo";
	
	// Componentes de Controle
	private DataModel listaModelos;
	
	private SelectOneDataModel<Familia> comboFamilias;
	private SelectOneDataModel<String> comboTiposDeBusca;
	private SelectOneDataModel<String> radioFlags;
	
	// Variaveis de Tela
	private Modelo modeloCorrente;
	private List<ModeloDecorado> modelosDecorados;
	
	private String flagSelecionado;
	private String campoDeBusca;
	private String tipoDeBuscaSelecionada;
	private boolean buscaEfetuada = false;
	
	public final String BUSCA_POR_CODIGO = "Código";
	public final String BUSCA_POR_DESCRICAO = "Descrição";
	
	public List<String> tiposDeFlag = new ArrayList<String>(2);
	private Date dataEstqInic;
	private Parametros parametroCorrente;
	
	
	public ModeloActions() throws Exception {
		
		try {
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			parametrosService = FabricaDeAppService
			.getAppService(ParametrosAppService.class);
		} catch (Exception e) {
			throw e;
		}
		parametroCorrente = parametrosService.recuperaListaDeParametros().get(0);
		
		tiposDeFlag.add(Modelo.FLAG_FIXO);
		tiposDeFlag.add(Modelo.FLAG_LIVRE);
	}  
	
	public String preparaInclusao() {

		if (familiaService.recuperaListaDeFamilias().isEmpty()){
			error("familia.FAMILIAS_INEXISTENTES");
			return PAGINA_LIST;
		}
		
		modeloCorrente = new Modelo();
		
		return PAGINA_NEW;
	}
	
	/**
	 * @author bruno.oliveira
	 * <br />
	 * Método que limpa a variável de lista e depois redireciona para página list dos modelos.<br />
	 * É necessário apenas quando o cadastro tiver opção de busca (como no caso de familia e modelo), pois 
	 * depois de realizar a busca senão houver novo get na lista de modelos vai manter a lista resultante da busca.
	 * 
	 * @return String
	 */
	public String preparaListagem() {
		
		listaModelos = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
	}	
	
	public String inclui(){
		
		if (radioFlags.getObjetoSelecionado().equals(Modelo.FLAG_LIVRE)){
			modeloCorrente.setFlagProducaoModel(true);
		} else {
			modeloCorrente.setFlagProducaoModel(false);
		}
		
		modeloCorrente.setFamilia(comboFamilias.getObjetoSelecionado());
			
		try{
			modeloService.inclui(modeloCorrente);
		} catch (AplicacaoException ex) {
			error(ex.getMessage());
			return PAGINA_NEW;
		}
		
		info("modelo.SUCESSO_INCLUSAO");
		
		listaModelos = null;
		
		return PAGINA_LIST;
	}
	
	public String preparaAlteracao(){
		
		modeloCorrente = (Modelo) listaModelos.getRowData();
		//verifica se tem cadplans cadastrados, se tiver nao pode alterar o modelo.
		if(cadPlanService.recuperaListaDeCadPlan().size()> 0){
			error("modelo.CADPLAN_CADASTRADOS");
			return PAGINA_LIST;			
		}
			
		comboFamilias = SelectOneDataModel.criaComObjetoSelecionado(familiaService.recuperaListaPaginadaDeFamilias(), modeloCorrente.getFamilia());
		
		radioFlags = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tiposDeFlag, 
				modeloCorrente.getFlagProducaoModel() ? Modelo.FLAG_LIVRE : Modelo.FLAG_FIXO);
		
		return PAGINA_EDIT;
	}


	/**
	 * Altera o valor do Estoque em falta do modelo sendo editado para o
	 * valor relativo ao PMP vigente.
	 * (APENAS NA TELA DE EDICAO)
	 * Funciona por ajax.
	 * @param event
	 */
	public void alteracaoCalcularEstqEmFaltaPorPMP() {
		System.out.println("bla");
		
		try{
			modeloCorrente = modeloService.atualizaEstoqueEmFaltaDeModeloPorPMP(modeloCorrente);
		} catch (AplicacaoException ex){
		}		
		
	}
	
	public String altera() {

		if (radioFlags.getObjetoSelecionado().equals(Modelo.FLAG_LIVRE)){
			modeloCorrente.setFlagProducaoModel(true);
		} else {
			modeloCorrente.setFlagProducaoModel(false);
		}
		
		modeloCorrente.setFamilia(comboFamilias.getObjetoSelecionado());
		
		try {
			modeloService.altera(modeloCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST;			
		}
		
		info("modelo.SUCESSO_ALTERACAO");
		
		buscaEfetuada = false;
		listaModelos = null;

		return PAGINA_LIST;
	}
	

	public String alteraDataEstqInic() {
		parametroCorrente.setDataEstqInic(DataUtil.dateToCalendar(dataEstqInic));
		
		parametrosService.altera(parametroCorrente);
		info("parametros.SUCESSO_ALTERACAO_DATAESTQINIC");

		return PAGINA_LIST;
	}
	
	public String mostra(){
		
		modeloCorrente = (Modelo) listaModelos.getRowData();

				
		radioFlags = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tiposDeFlag, 
				modeloCorrente.getFlagProducaoModel() ? Modelo.FLAG_LIVRE : Modelo.FLAG_FIXO);
		
		return PAGINA_SHOW;
	}
	
	public void preparaExclusao(){
		modeloCorrente = (Modelo) listaModelos.getRowData();
	}
	
	
	public String exclui(){
		
		try {
			modeloService.exclui(modeloCorrente);
		} catch (AplicacaoException ex) {
			error(ex.getMessage());
			return PAGINA_LIST;
		}
		
		info("modelo.SUCESSO_EXCLUSAO");
		
		listaModelos = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
	}
	
	public String cancelar() {
		
		listaModelos = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
	}

	public void geraRelatorioPdf(){
		
		List<Modelo> listaModelos = null;
		try{
			
			listaModelos = modeloService.recuperaListaDeModelosComFamilias();
			
			if(listaModelos.isEmpty()){
				error("modelo.MODELOS_INEXISTENTES");
				
			}else{

				modeloService.gerarRelatorio(modeloService.recuperaListaDeModelosComFamilias());
			}
			
		} catch (AplicacaoException re){
			re.getMessage();
		}
	}
	
	/**
	 * Método que busca um Modelo por código/descrição aproximados
	 * redireciona para pagina list com a mensagem adequada:  encontrado/nao encontrado
	 * 
	 * @author walanem 
	 */
	public String buscaModelo(){
		
		List<Modelo> modelosEncontrados = null;
		
		if (campoDeBusca.trim().isEmpty()){
			error("modelo.FORNECER_CAMPO_DE_BUSCA");
			return PAGINA_LIST;
		} 

		else {	
			
			listaModelos = null;
			
			if (comboTiposDeBusca.getObjetoSelecionado().equals(BUSCA_POR_CODIGO)){
				modelosEncontrados = new ArrayList<Modelo>(modeloService.recuperaModeloPorCodigoLike(campoDeBusca));
			} else {
				modelosEncontrados = new ArrayList<Modelo>(modeloService.recuperaModeloPorDescricao(campoDeBusca));
			}
			
			if (modelosEncontrados.isEmpty()){
				error("modelo.NAO_ENCONTRADO");
				listaModelos = null;
				return PAGINA_LIST;
			}else{
				info("modelo.ENCONTRADOS");
			}
		}
		
		listaModelos = new ListDataModel(modelosEncontrados);
		buscaEfetuada = true;
		
		return PAGINA_LIST;
	}
	
	
	// ================================== Métodos get() e set() ================================== //

	public Modelo getModeloCorrente() {
		return modeloCorrente;
	}

	public void setModeloCorrente(Modelo modeloCorrente) {
		this.modeloCorrente = modeloCorrente;
	}

	public DataModel getListaModelos() {
		
		if (listaModelos == null){
			listaModelos = new ListDataModel(modeloService.recuperaListaPaginadaDeModelosComFamilias());
		}
		
		return listaModelos;
	}

	public void setListaModelos(DataModel listaModelos) {
		this.listaModelos = listaModelos;
	}

	/**
	 * Método que cria a Combobox que lista as famílias cadastradas no sistema.
	 * 
	 * @author walanem 
	 */
	public SelectOneDataModel<Familia> getComboFamilias() {
		
		if (comboFamilias == null){
			comboFamilias = SelectOneDataModel.criaSemTextoInicial(familiaService.recuperaListaPaginadaDeFamilias());
		}
		
		return comboFamilias;
	}

	public void setComboFamilias(SelectOneDataModel<Familia> comboFamilias) {
		this.comboFamilias = comboFamilias;
	}

	/**
	 * Método que cria os RadioButtons relativos ao Flag de Producao do Modelo
	 * 
	 * @author walanem 
	 */
	public SelectOneDataModel<String> getRadioFlags() {
		
		if (radioFlags == null){
			radioFlags = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tiposDeFlag, Modelo.FLAG_LIVRE);
		}
		
		return radioFlags;
	}

	public void setRadioFlags(SelectOneDataModel<String> radioFlags) {
		this.radioFlags = radioFlags;
	}

	public String getFlagSelecionado() {
		return flagSelecionado;
	}

	public void setFlagSelecionado(String flagSelecionado) {
		this.flagSelecionado = flagSelecionado;
	}

	public String getCampoDeBusca() {
		return campoDeBusca;
	}

	public void setCampoDeBusca(String campoDeBusca) {
		this.campoDeBusca = campoDeBusca;
	}

	public void setComboTiposDeBusca(SelectOneDataModel<String> comboTiposDeBusca) {
		this.comboTiposDeBusca = comboTiposDeBusca;
	}

	/**
	 * Método que cria a ComboBox relativos as formas de busca de um Modelo
	 * 
	 * @author walanem 
	 */
	public SelectOneDataModel<String> getComboTiposDeBusca() {
		
		if (comboTiposDeBusca == null){
			
			List<String> tiposDeBusca = new ArrayList<String>(2);
			tiposDeBusca.add(BUSCA_POR_CODIGO);
			tiposDeBusca.add(BUSCA_POR_DESCRICAO);
			
			comboTiposDeBusca = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tiposDeBusca, BUSCA_POR_CODIGO);
		}
		
		return comboTiposDeBusca;
	}

	public void setBuscaEfetuada(boolean buscaEfetuada) {
		this.buscaEfetuada = buscaEfetuada;
	}

	public boolean isBuscaEfetuada() {
		return buscaEfetuada;
	}

	public void setTipoDeBuscaSelecionada(String tipoDeBuscaSelecionada) {
		this.tipoDeBuscaSelecionada = tipoDeBuscaSelecionada;
	}

	public Parametros getParametroCorrente() {
		if (parametroCorrente == null) {
			parametroCorrente = new Parametros();
			parametroCorrente = parametrosService.recuperaListaDeParametros()
					.get(0);
		}

		return parametroCorrente;
	}

	public void setParametroCorrente(Parametros parametroCorrente) {
		this.parametroCorrente = parametroCorrente;
	}

	public Date getDataEstqInic() {
		if (dataEstqInic == null){
			if (parametroCorrente == null){
				dataEstqInic = new GregorianCalendar().getTime();
			}
			else{
				dataEstqInic = parametroCorrente.getDataEstqInic().getTime();				
			}
		}
		return dataEstqInic;
	}

	public void setDataEstqInic(Date dataEstqInic) {
		this.dataEstqInic = dataEstqInic;
	}

	public String getTipoDeBuscaSelecionada() {
		return tipoDeBuscaSelecionada;
	}

	public void setModelosDecorados(List<ModeloDecorado> modelosDecorados) {
		this.modelosDecorados = modelosDecorados;
	}

	public List<ModeloDecorado> getModelosDecorados() {
		return modelosDecorados;
	}
}
