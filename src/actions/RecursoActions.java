 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.Modelo;
import modelo.Parametros;
import modelo.RecModel;
import modelo.Recurso;
import modelo.TecModel;
import modelo.Tecido;

import org.ajax4jsf.context.AjaxContext;
import org.jboss.util.NotImplementedException;

import service.ParametrosAppService;
import service.RecursoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class RecursoActions extends BaseActions {
	
	private static RecursoAppService recursoService;
	private static ParametrosAppService parametrosAppService;

	private Recurso recurso;
	private DataModel listaDeRecursos;
	private SelectOneDataModel<Recurso> listaSelecaoDeRecursos;
	
	private SelectOneDataModel<String> comboTiposDeBusca;

	private String campoDeBusca;
	private String tipoDeBuscaSelecionada;

	private boolean buscaEfetuada = false;
	
	private DataModel listaDeModelos;
	private int pagina;
	
	public final String BUSCA_POR_CODIGO = "Código";
	public final String BUSCA_POR_DESCRICAO = "Descrição";

	
	
	public RecursoActions() {
		try {
			recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
			parametrosAppService = FabricaDeAppService.getAppService(ParametrosAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author bruno.oliveira
	 * <br />
	 * Método que limpa a variável de lista e depois redireciona para página list da recurso.<br />
	 * É necessário apenas quando o cadastro tiver opção de busca (como no caso de familia e modelo), pois 
	 * depois de realizar a busca senão houver novo get na lista de recurso vai manter a lista resultante da busca.
	 * 
	 * @return String
	 */
	public String preparaListagem() {
		
		listaDeRecursos = null;
		buscaEfetuada = false;
		return "listRecurso";
	}

	/**
	 * Volta para a página de lista de Recurso
	 * 
	 * @return String
	 */
	public String cancela() {
		
		listaDeRecursos = null;
		buscaEfetuada = false;
		return "listRecurso";
	}

	/**
	 * Insere na requisicao um novo recurso
	 * Retorna "newRecurso"
	 * Prepara para utilizar a pagina de inclusao(new)
	 * @return
	 */
	public String preparaInclusao() {	
		
		Parametros parametro = parametrosAppService.recuperaListaDeParametros().get(0);
		
		if (!parametro.isInicPlanejamento()){
			error("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
			return "listRecurso";
		}
		
		recurso = new Recurso();
		
		return "newRecurso";
	}
	
	
	/**
	 * Executa a regra de negócio de inclusão de um recurso
	 * Atraves do recursoService
	 * 
	 * Atualiza a lista de Recurso e a insere no request
	 * 
	 * @return String
	 */
	public String inclui() {	
		
		try {
			recursoService.incluiComCriticas(recurso);
			listaDeRecursos = null;
			info("recurso.SUCESSO_INCLUSAO");
			return "listRecurso";
		} catch (AplicacaoException e) {
			
			error(e.getMessage());
			return "newRecurso";
		}	
	}
	

	/**
	 * Seleciona o objeto para edição e redireciona para a tela de edição
	 * 
	 * Insere o objeto no request
	 * 
	 * @return String
	 */
	public String preparaAlteracao() {
		
		recurso = (Recurso) listaDeRecursos.getRowData();
		
		try{
			recurso = recursoService.recuperaRecursoComListaDeCapacRecs(recurso);
		}catch(AplicacaoException e){
		}
		
		return "editRecurso";
	}

	/**
	 * Executa a regra de negócio de alteração de um recurso
	 * 
	 * Atualiza a lista de Recurso e a insere no request
	 * 
	 * @return String
	 */
	public String altera() {
		
		recursoService.altera(recurso);
		info("recurso.SUCESSO_ALTERACAO");
		
		listaDeRecursos = null;

		return "listRecurso";
		
	}
	
	/**
	 * Seleciona o objeto para exclusao 
	 * 
	 * Insere o objeto no request
	 * 
	 */
	public void preparaExclusao() {
		
		recurso = (Recurso) listaDeRecursos.getRowData();
		
	}

	
	/**
	 * Executa a regra de negócio de exclusão de um recurso
	 * 
	 * @return String
	 */
	public String exclui() {
		
		try {
			recursoService.exclui(recurso);
			listaDeRecursos = null;
			info("recurso.SUCESSO_EXCLUSAO");
		} catch (AplicacaoException e) {
			error(e.getMessage());
		}
		
		return "listRecurso";
	}


	/**
	 * Exibe um objeto Recurso
	 * 
	 * Redireciona para a página show
	 * 
	 * @return String
	 */
	public String mostra() {

		recurso = (Recurso) listaDeRecursos.getRowData();
		
		return "showRecurso";
	}
	
	

	/**
	 * Método que busca um Recurso por código/descrição aproximados
	 * redireciona para pagina list com a mensagem adequada:  encontrado/nao encontrado
	 * 
	 * @author walanem 
	 */
	public String buscaRecurso(){
		
		List<Recurso> recursosEncontrados = null;
		
		if (campoDeBusca.trim().isEmpty()){
			error("recurso.FORNECER_CAMPO_DE_BUSCA");
			return "listRecurso";
		} 

		else {	
			
			listaDeRecursos = null;
			
			if (comboTiposDeBusca.getObjetoSelecionado().equals(BUSCA_POR_CODIGO)){
				recursosEncontrados = new ArrayList<Recurso>(recursoService.recuperaListaDeRecursosPeloCodigoLike(campoDeBusca));
			} else {
				recursosEncontrados = new ArrayList<Recurso>(recursoService.recuperaListaDeRecursosPorDescricao(campoDeBusca));
			}
			
			if (recursosEncontrados.isEmpty()){
				error("recurso.NAO_ENCONTRADO");
				listaDeRecursos = null;
				return "listRecurso";
			}else{
				info("recurso.ENCONTRADOS");
			}
		}
		
		listaDeRecursos = new ListDataModel(recursosEncontrados);
		buscaEfetuada = true;
		
		return "listRecurso";
	}
	
	
	
	/**
	 * Ao voltar da tela "informa consumo de recurso por modelo"
	 * limpa a listaDeRecursos que estava sendo usada pois esta
	 * carregava todos os recmodels de cada recurso.
	 * Fazendo listaDeRecursos = null, quando a tela for renderizada 
	 * vai usar getListaDeRecursos que obtem entao a lista mais leve,
	 * sem os recmodels 
	 * 
	 * @return
	 */
	public String voltaLimpando() {
		
		listaDeRecursos = null;
		
		return "listRecurso";
	}
	
	public void imprimir(){
		
		try{
			recursoService.gerarRelatorio(recursoService.recuperaListaDeRecursos());

		} catch (AplicacaoException re){
				error("recurso.RECURSOS_INEXISTENTES");
		}
	}

	
	
	/**
	 * Obtem listaDeRecursos trazendo os RecModels de cada recurso para
	 * usar na tela de master detail  
	 * 
	 * Insere o objeto no request
	 * direciona para a pagina list de recModel
	 * 
	 */
	public String informaConsumoPorModelo() {
		recurso = (Recurso) listaDeRecursos.getRowData();
		int pag = ((List<Recurso>)listaDeRecursos.getWrappedData()).indexOf(recurso);
		pagina = pag +1;
		
		listaDeRecursos = new ListDataModel(recursoService.recuperaListaPaginadaDeRecursosComListaDeRecModels());

		return "listRecModel";
		
	}

	/**
	 * Obtem listaDeRecursos trazendo os CapacRecs de cada recurso para
	 * usar na tela de master detail  
	 * 
	 * Insere o objeto no request
	 * direciona para a pagina list de CapacRecs
	 * 
	 */
	public String informaCapacidadeDoRecurso() {
		recurso = (Recurso) listaDeRecursos.getRowData();
		int pag = ((List<Recurso>)listaDeRecursos.getWrappedData()).indexOf(recurso);
		pagina = pag +1;
	
		return "listCapacRec";
		
	}
	
	/* 
	 * ***********  Gets e Sets *****************
	 *
	 */
	

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
	 * Método que cria a ComboBox relativos as formas de busca de um Recurso
	 * 
	 * @author Felipe 
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

	public String getTipoDeBuscaSelecionada() {
		return tipoDeBuscaSelecionada;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	/**
	 * Obtem Data Model listaDeRecursos com lista paginada atraves de recursoAppService
	 * @return
	 */
	public DataModel getListaDeRecursos() {
		if (listaDeRecursos == null) {
			listaDeRecursos = new ListDataModel(recursoService.recuperaListaPaginadaDeRecursos());
		}
		return listaDeRecursos;
	}

	public void setListaDeRecursos(DataModel listaDeRecursos) {
		this.listaDeRecursos = listaDeRecursos;
	}

	public void setListaSelecaoDeRecursos(SelectOneDataModel<Recurso> listaSelecaoDeRecursos) {
		this.listaSelecaoDeRecursos = listaSelecaoDeRecursos;
	}

	public SelectOneDataModel<Recurso> getListaSelecaoDeRecursos() {
		return listaSelecaoDeRecursos;
	}

	public void setListaDeModelos(DataModel listaDeModelos) {
		this.listaDeModelos = listaDeModelos;
	}

	public DataModel getListaDeModelos() {
		return listaDeModelos;
	}
	
	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}


}
