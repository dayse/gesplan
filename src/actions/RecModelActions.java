 
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
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.ajax4jsf.context.AjaxContext;

import exception.relatorio.RelatorioException;

import modelo.Familia;
import modelo.Modelo;
import modelo.ModeloDecorado;
import modelo.PerioPM;
import modelo.RecModel;
import modelo.Recurso;
import service.FamiliaAppService;
import service.ModeloAppService;
import service.PerioPMAppService;
import service.RecModelAppService;
import service.RecursoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class RecModelActions extends BaseActions {
	
	// Services
	private static RecModelAppService recModelService;
	private static RecursoAppService recursoService;
	private static ModeloAppService modeloService;
	

	// Paginas
	public final String PAGINA_LIST = "listRecModel";
	public final String PAGINA_NEW  = "newRecModel";
	public final String PAGINA_SHOW = "showRecModel";
	public final String PAGINA_EDIT = "editRecModel";
	
	// Variaveis de Tela
	private RecModel recModelCorrente;
	private Recurso recursoCorrente;
	private DataModel listaDeRecursos;
	
	
	private SelectOneDataModel<Modelo> comboModelos;
	private SelectOneDataModel<String> comboTiposDeBusca;
	
	
	private String campoDeBusca;
	private boolean buscaEfetuada = false;
	
	public final String BUSCA_POR_CODIGO = "C�digo";
	public final String BUSCA_POR_DESCRICAO = "Descri��o";
	
	private int pagina;
	
	
	public RecModelActions() throws Exception {
		try {
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			recModelService = FabricaDeAppService.getAppService(RecModelAppService.class);
			recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
		} catch (Exception e) {
			throw e;
		}
		//resgata o managed bean "recursoActions" que foi mantido na requisao atrav�s do keepAlive
		pagina = ((RecursoActions) getManagedBean("recursoActions")).getPagina();
	}
	
	/**
	 * Antes de incluir RecModel, verifica se tem modelos cadastrados e recursos
	 * cadastrados, e mostra as mensagens de erro se elas existirem.
	 * Resgata o managed bean "recursoActions" que foi mantido na requisao atrav�s do keepAlive
	 * e instancia RecModel se nao houver excecao
	 * retorna para a pagina new em caso de sucesso, caso contrario retorna para pagina list
	 */
	public String preparaInclusao() {
		
		try{
			 modeloService.recuperaListaDeModelos();			
		}
		catch(AplicacaoException ae){
			error(ae.getMessage());
			return PAGINA_LIST;
		}
		
		try{
			recursoService.recuperaListaDeRecursos();			
		}
		catch(AplicacaoException ae){
			error(ae.getMessage());
			return PAGINA_LIST;
		}
		
		recModelCorrente = new RecModel();
		
		recursoCorrente = (Recurso) listaDeRecursos.getRowData();
		recModelCorrente.setRecurso(recursoCorrente);	
		
		return PAGINA_NEW;
	}
	
	public String inclui(){


		//atribui a recModel.modelo o objeto modelo relativo ao 
		//modelo selecionado na combobox	
		recModelCorrente.setModelo(comboModelos.getObjetoSelecionado());
		
		try{
			recModelService.inclui(recModelCorrente);
		} catch (AplicacaoException ex) {
			error(ex.getMessage());
			return PAGINA_NEW;
		}
		
		info("recModel.SUCESSO_INCLUSAO");
		listaDeRecursos = null;
	
		return PAGINA_LIST;
	}
	
	/**
	 * obtem do data Model (tabelaRecModels) o objeto recModelCorrente
	 * @return
	 */
	public String preparaAlteracao(){			
		
		return PAGINA_EDIT;
	}
	
	public String altera() {
			
		recModelService.altera(recModelCorrente);
		
		info("recModel.SUCESSO_ALTERACAO");		
		
		buscaEfetuada = false;
		listaDeRecursos = null;
		
		return PAGINA_LIST;
	}
	
	public String mostra(){
		
		recModelCorrente = (RecModel) listaDeRecursos.getRowData();		
		return PAGINA_SHOW;
	}
	
	
	/**
	 * obtem o objeto recModelCorrente, que ficar� no escopo de requisi��o (vide faces-config)
	 */
	public void preparaExclusao(){		
		
//		System.out.println("************************ Passou pelo metodo preparaExclusao de RecModelActions *********************************** ");
//		Recurso recurso;
//		recurso = (Recurso)recursoActions.getListaDeRecursos().getRowData();
//		
//		listaDeRecursos = new ListDataModel(recurso.getRecModels());
//		recModelCorrente = (RecModel) listaDeRecursos.getRowData();
	}
	
	public String exclui(){
		
		try {
			recModelService.exclui(recModelCorrente);
		} catch (AplicacaoException e) {
		}
		
		info("recModel.SUCESSO_EXCLUSAO");		
		buscaEfetuada = false;
		listaDeRecursos = null;
		
		return PAGINA_LIST;
	}
	
	public String cancela() {
		
		listaDeRecursos = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
	}
	

	
	// ================================== M�todos get() e set() ================================== //

	
	public RecModel getRecModelCorrente() {
		return recModelCorrente;
	}

	public void setRecModelCorrente(RecModel recModelCorrente) {
		this.recModelCorrente = recModelCorrente;
	}


	public void setRecursoCorrente(Recurso recursoCorrente) {
		this.recursoCorrente = recursoCorrente;
	}

	public Recurso getRecursoCorrente() {
		return recursoCorrente;
	}

	public DataModel getListaDeRecursos() {
		
		if (listaDeRecursos == null){
			
			listaDeRecursos = new ListDataModel(recursoService.recuperaListaPaginadaDeRecursosComListaDeRecModels());
		}

		return listaDeRecursos;
	}

	public void setListaDeRecursos(DataModel listaDeRecursos) {
		this.listaDeRecursos = listaDeRecursos;
	}

	/**
	 * M�todo que cria a Combobox que lista os modelos cadastrados no sistema.
	 * 
	 * @author dayse 
	 */
	public SelectOneDataModel<Modelo> getComboModelos() {
		
		if (comboModelos == null){
			try{

				comboModelos = SelectOneDataModel.criaSemTextoInicial(modeloService.recuperaListaDeModelos());
			}
			catch(AplicacaoException e){
				
			}
		}
		
		return comboModelos;
	}

	public void setComboModelos(SelectOneDataModel<Modelo> comboModelos) {
		this.comboModelos = comboModelos;
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
	 * M�todo que cria a ComboBox relativos as formas de busca de um Modelo
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

	/**
	 * Exatamente igual ao metodo no cadastro de modelos
	 * talvez seja interessante refatorar para colocar num lugar s�
	 * para utiliza�ao de todos que usem combobox
	 * 
	 */

	public void setBuscaEfetuada(boolean buscaEfetuada) {
		this.buscaEfetuada = buscaEfetuada;
	}

	/**
	 * Exatamente igual ao metodo no cadastro de modelos
	 * talvez seja interessante refatorar para colocar num lugar s�
	 * para utiliza�ao de todos que usem combobox
	 * 
	 */
	public boolean isBuscaEfetuada() {
		return buscaEfetuada;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	

	
	/**
	 * M�todo usado para impressao,caso n�o haja modelos associados
	 * a recursos uma mensagem de alerta sera enviada.
	 * 
	 * @author marques.araujo
	 * @return void
	 */
	public void imprimir(){
		
		try{
			
			if(recursoService.recuperaListaDeRecursosQueTenhamApenasRecModels().isEmpty()){
				throw new AplicacaoException("recurso.RECURSOS_SEM_MODELOS");
			}
		      
			recursoService.gerarRelatorioAgregado(recursoService.recuperaListaDeRecursosComRecModels());
							
		} catch (AplicacaoException re){
			
			if(re.getMessage().equals("recurso.RECURSOS_SEM_MODELOS")){
				  error("recurso.RECURSOS_SEM_MODELOS");
			}else{
			      error("recurso.RECURSOS_INEXISTENTES");
			}
		}
		
	}

}
