 
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

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.Parametros;
import modelo.TecModel;
import modelo.Tecido;
import service.CapacTecViewAppService;
import service.ParametrosAppService;
import service.TecidoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

/**
 * TecidoActions é uma classe relacionada à manipulação de tela, ou seja, a interação do ususário
 * de fato dar-se-á através de objetos do tipo TecidoActions quando na tela de Tecido.
 * Objetos do tipo "actions", nome aqui adotado, também são popularmente conhecidos como managedbeans
 * em outras palavras beans gerenciáveis.
 * 
 * @author marques.araujo
 *
 */

public class TecidoActions extends BaseActions {
	
	

	private static TecidoAppService tecidoService;
	private static CapacTecViewAppService capacTecViewService;
	private static ParametrosAppService parametrosService;
	
	private Tecido tecido;              //variavel global
	private DataModel listaDeTecidos;   //variavel global
	private Parametros parametrosCorrente;
	
	private SelectOneDataModel<Tecido> listaSelecaoDeTecidos;
	private SelectOneDataModel<String> comboTiposDeBusca;

	private String campoDeBusca;
	private String tipoDeBuscaSelecionada;

	private boolean buscaEfetuada = false;
		
	private DataModel listaDeModelos;
	private int pagina;
    
	public final String BUSCA_POR_CODIGO = "Código";
	public final String BUSCA_POR_DESCRICAO = "Descrição";
	
	
	
	public TecidoActions() throws Exception {
		try { 

			tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
			capacTecViewService = FabricaDeAppService.getAppService(CapacTecViewAppService.class);
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author bruno.oliveira
	 * <br />
	 * Método que limpa a variável de lista e depois redireciona para página list da tecidos.<br />
	 * É necessário apenas quando o cadastro tiver opção de busca (como no caso de familia e modelo), pois 
	 * depois de realizar a busca senão houver novo get na lista de tecidos vai manter a lista resultante da busca.
	 * 
	 * @return String
	 */
	public String preparaListagem() {
		
		listaDeTecidos = null;
		buscaEfetuada = false;
		return "listTecido";
	}	
	
	/**
	 * Volta para a página de lista de Recurso
	 * 
	 * @return String
	 */
	
	public String cancela() {
		
		listaDeTecidos = null;
		buscaEfetuada = false;
		return "listTecido";
	}
	
	public String cancelaModal() {
		
		parametrosCorrente = null;
		
		return "listTecido";
	}	
	
	/**
	 * Insere na requisicao um novo recurso
	 * Retorna "newTecido" e prepara para utilizar
	 * a página de inclusao(new)
	 * 
	 * @return String
	 */
	public String preparaInclusao() {		
		tecido = new Tecido();
		return "newTecido";
	}
	
	

	public String inclui() {	
		
		try {
					
			tecidoService.inclui(tecido);
			
			listaDeTecidos = null;
			
            info("tecido.SUCESSO_INCLUSAO");
            
			return "listTecido";
			
		} catch (AplicacaoException e) {
			
			error(e.getMessage());
			return "newTecido";
		}	
	}
	
	public String preparaAlteracao() {
		
		tecido = (Tecido) listaDeTecidos.getRowData();
		
		return "editTecido";
	}
	
	
	public boolean desabilitarBotao(){
		return (parametrosCorrente.getPercentualDePerda()<=0);
	}
	
	
	/**
	 * Este metodo substituiu o metodo alteracaoPercentualDePerdaDeParametros()
	 * uma vez que foram utilizados validadores padroes.
	 * @return String
	 * @author marques.araujo
	 */	
	public String alteracaoParametrosPercentualDePerda() {
		
		parametrosService.altera(parametrosCorrente);
			
		return "listTecido";
		
	}
	
	
	/**
	 * Este método foi utilizado para enviar mensagens de sucesso
	 * ou erro via servidor, porem após a criaçao de tags e classes
	 * proprias para validaçao este metodo nao foi mais utilizado.
	 * 
	 * @return String
	 * @author marques.araujo
	 */
	public String alteracaoPercentualDePerdaDeParametros() {
		
		  //Este try{}catch{} foi utilizado para teste de mensagens de erro via servidor
		  //A mensagem que obteremos com este try{} catch{} sera do tipo global mas o desejado
		  //era que a mensagem ocorresse dentro do ModalPanel.
	   try{  
			
						
			if(parametrosCorrente.getPercentualDePerda()>0){
				
				info("parametros.SUCESSO_ALTERACAO_PERCENTUAL");
		
				parametrosService.altera(parametrosCorrente);
				
			}else{
				
				error("tecido.FALHA_ALTERACAO");
				
			}
			
					
		}catch(Exception e){

			error(e.getMessage());
		}
		
			
		return "listTecido";
	}
	
	
	public String altera() {
		
		try{
	
			tecidoService.altera(tecido);
			
			info("tecido.SUCESSO_ALTERACAO");
			
		}catch(AplicacaoException e){

			error("tecido.FALHA_ALTERACAO");
	
		}
		
		listaDeTecidos = null;

		return "listTecido";
		
	}
	
	
  public void preparaExclusao() {
		
		tecido = (Tecido) listaDeTecidos.getRowData();
		
	}
	
	
  public String exclui() {
		
		try {
			tecidoService.exclui(tecido);
			listaDeTecidos = null;
			info("tecido.SUCESSO_EXCLUSAO");
		} catch (AplicacaoException e) {
			error(e.getMessage());
		}
		
		return "listTecido";
	}
  
  
	public String mostra() {

		tecido = (Tecido) listaDeTecidos.getRowData();

		return "showTecido";
	}
	
		
	
	
	/**
	 * Método que busca um Recurso por código/descrição aproximados
	 * redireciona para pagina list com a mensagem adequada:  encontrado/nao encontrado
	 * @return String
	 * @author walanem 
	 */
	public String buscaTecido(){
		
		List<Tecido> tecidosEncontrados = null;
		
		if (campoDeBusca.trim().isEmpty()){
			error("tecido.FORNECER_CAMPO_DE_BUSCA");
			return "listTecido";
		} 

		else {	
			
			listaDeTecidos = null;
			
			if (comboTiposDeBusca.getObjetoSelecionado().equals(BUSCA_POR_CODIGO)){
				tecidosEncontrados = new ArrayList<Tecido>(tecidoService.
						recuperaListaDeTecidosPeloCodigoLike(campoDeBusca));
			} else {
				tecidosEncontrados = new ArrayList<Tecido>(tecidoService.
						recuperaListaDeTecidosPorDescricao(campoDeBusca));
			}
			
			if (tecidosEncontrados.isEmpty()){
				error("tecido.NAO_ENCONTRADO");
				listaDeTecidos = null;
				return "listTecido";
			}else{
				info("tecido.ENCONTRADO");
			}
		}
		
		listaDeTecidos = new ListDataModel(tecidosEncontrados);
		buscaEfetuada = true;
		
		return "listTecido";
	}
	
	
	public void imprimir(){
		
		try{
			List<Tecido> listaDeTecidos = tecidoService.recuperaListaDeTecidos();			
			tecidoService.gerarRelatorio(listaDeTecidos);
			
		} catch (AplicacaoException re){
			error("tecido.TECIDOS_INEXISTENTES");
		}
				
	}
	
	

	/**
	 * Ao voltar da tela "mostrar disponibilidade maxima de tecido na matriz"
	 * limpa a listaDeTecidos que estava sendo usada pois esta
	 * carregava todos os capacTecViews de cada perioPM.
	 * Fazendo listaDeTecidos = null, quando a tela for renderizada 
	 * vai usar getListaDeTecidos que obtém ento a lista mais leve,
	 * sem os capacTecViews. 
	 * 
	 * @return String
	 */
	public String voltaLimpando() {
		
		listaDeTecidos = null;
		
		return "listTecido";
	}

	public String informaConsumoPorModelo() {
		
		
		
		tecido = (Tecido) listaDeTecidos.getRowData();
		int pag = ((List<Tecido>)listaDeTecidos.getWrappedData()).indexOf(tecido);
		pagina = pag +1;
		
		listaDeTecidos = new ListDataModel(tecidoService.
				recuperaListaPaginadaDeTecidosComListaDeTecModels());

		return "listTecModel";
		
	}

	
	/**
	 * Obtem listaDeTecidos trazendo os capacTecView de cada tecido para
	 * usar na tela de master detail.  
	 * 
	 * Insere o objeto no request direciona para a pagina list de capacTecView.
	 * 
	 * @return String
	 * 
	 */
	public String mostrarDisponibilidadeMaximaTecidoMatriz() {
		
		if(parametrosCorrente.isInicPlanejamento()){
			
			tecido = (Tecido) listaDeTecidos.getRowData();
			int pag = ((List<Tecido>)listaDeTecidos.getWrappedData()).indexOf(tecido);
			pagina = pag +1;
			
			try{
				listaDeTecidos = new ListDataModel(capacTecViewService.recuperaListaPaginadaDeTecidosComListaDeCapacTecViews());
			}
			catch(AplicacaoException e){
				error(e.getMessage());
				return "listTecido";
			}
			
			return "listCapacTecView";
		}else{
			error("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
			return "listTecido";
		}
	
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

	
	public SelectOneDataModel<String> getComboTiposDeBusca() {
		
		if (comboTiposDeBusca == null){
			
			List<String> tiposDeBusca = new ArrayList<String>(2);
			tiposDeBusca.add(BUSCA_POR_CODIGO);
			tiposDeBusca.add(BUSCA_POR_DESCRICAO);
			
			comboTiposDeBusca = SelectOneDataModel.
			     criaComObjetoSelecionadoSemTextoInicial(tiposDeBusca, BUSCA_POR_CODIGO);
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
	
	
	public Tecido getTecido() {
		return tecido;
	}

	public void setTecido(Tecido tecido) {
		this.tecido = tecido;
	}
	
	/**
	 * Obtem Data Model listaDeTecidos com lista paginada atraves de recursoAppService
	 * @return DataModel
	 */
	public DataModel getListaDeTecidos() {
		if (listaDeTecidos == null) {
			listaDeTecidos = new ListDataModel(tecidoService.recuperaListaPaginadaDeTecidos());
		}
		return listaDeTecidos;
	}

	public void setListaDeTecidos(DataModel listaDeTecidos) {
		this.listaDeTecidos = listaDeTecidos;
	}

	public void setListaSelecaoDeTecidos(SelectOneDataModel<Tecido> listaSelecaoDeTecidos) {
		this.listaSelecaoDeTecidos = listaSelecaoDeTecidos;
	}

	public SelectOneDataModel<Tecido> getListaSelecaoDeTecidos() {
		return listaSelecaoDeTecidos;
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
	
	public void setParametrosCorrente(Parametros parametrosCorrente) {
		
		this.parametrosCorrente = parametrosCorrente;
	}
	
	
	public Parametros getParametrosCorrente() {
				
		//Como temos apenas um registro no banco podemos usar o get(0)
		parametrosCorrente = parametrosService.recuperaListaDeParametros().get(0);
		return parametrosCorrente;
		
	}
	
}
