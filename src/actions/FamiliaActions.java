 
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
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.Familia;
import modelo.Modelo;
import modelo.Parametros;
import service.FamiliaAppService;
import service.ParametrosAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;
/**
 * @author samuelbandeira
 *
 */
public class FamiliaActions extends BaseActions implements Serializable{

	// Services
	private static FamiliaAppService familiaService;
	private static ParametrosAppService parametrosService;
	
	private Familia familia;
	private DataModel listaDeFamilias;
	
	// Paginas
	public final String PAGINA_LIST = "listFamilia";
	
	
	private String campoDeBusca;//
	private boolean buscaEfetuada = false;//
	private boolean renderizarDemandas = false;//
	
	private SelectOneDataModel<String> comboTiposDeBusca;//
	public final String BUSCA_POR_CODIGO = "Código";//
	public final String BUSCA_POR_DESCRICAO = "Descrição";//
	
	private int opcaoRelatorioEscolhido;
	private int OPCAO_RELATORIO_FAMILIAS = 4;
	private int OPCAO_RELATORIO_FAMILIAS_COM_MODELOS = 5;

	public FamiliaActions() {
		try {
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String preparaInclusao() {
		
		Parametros parametro = parametrosService.recuperaListaDeParametros().get(0);
		
		if (!parametro.isInicPlanejamento()){
			error("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
			return PAGINA_LIST;
		}
		
		familia = new Familia();
		
		return "newFamilia";
	}
	
	/**
	 * @author bruno.oliveira
	 * <br />
	 * Método que limpa a variável de lista e depois redireciona para página list da família.<br />
	 * É necessário apenas quando o cadastro tiver opção de busca (como no caso de familia e modelo), pois 
	 * depois de realizar a busca senão houver novo get na lista de familias vai manter a lista resultante da busca.
	 * 
	 * @return String
	 */
	public String preparaListagem() {
		
		listaDeFamilias = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
	}	
	
	/**
	 * Executa a regra de negócio de inclusão de uma família
	 * 
	 * Atualiza a lista de Família e a insere no request
	 * 
	 * @return String
	 */
	public String inclui() {	
		
		try {
			familiaService.inclui(familia);
			listaDeFamilias = null;
			info("familia.SUCESSO_INCLUSAO");
			return PAGINA_LIST;
		} catch (AplicacaoException e) {
			
			error(e.getMessage());
			return "newFamilia";
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
		
		familia = (Familia) listaDeFamilias.getRowData();
		
		return "editFamilia";
	}

	/**
	 * Executa a regra de negócio de alteração de uma família
	 * 
	 * Atualiza a lista de Família e a insere no request
	 * 
	 * @return String
	 */
	public String altera() {
		
		familiaService.altera(familia);
		info("familia.SUCESSO_ALTERACAO");
		
		listaDeFamilias = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
		
	}
	
	/**
	 * Exibe um objeto Família
	 * 
	 * Redireciona para a página show
	 * 
	 * @return String
	 */
	public String mostra() {

		familia = (Familia) listaDeFamilias.getRowData();

		return "showFamilia";
	}
	
	public void prepararExclusao() {
		familia = (Familia)listaDeFamilias.getRowData();
	}
	
	/**
	 * Executa a regra de negócio de exclusão de uma família
	 * 
	 * @return String
	 */
	public String exclui() {
		
		try {
			familiaService.exclui(familia);
			
			listaDeFamilias = null;
			buscaEfetuada = false;
		
			info("familia.SUCESSO_EXCLUSAO");
		} catch (AplicacaoException e) {
			error(e.getMessage());
		}
		
		return PAGINA_LIST;
	}

	/**
	 * Volta para a página de lista de Família
	 * 
	 * @return String
	 */
	public String cancela() {
		
		listaDeFamilias = null;
		buscaEfetuada = false;
		
		return PAGINA_LIST;
	}
	
	public void imprimir(){
		
		List<Familia> listaFamilia = null;
        listaFamilia = familiaService.recuperaListaDeFamilias();		
		
		try{
			if(listaFamilia.isEmpty()){
				
				error("familia.FAMILIAS_INEXISTENTES");
				
			}else{
				
				if( opcaoRelatorioEscolhido == OPCAO_RELATORIO_FAMILIAS ){
					
					familiaService.gerarRelatorio(familiaService.recuperaListaDeFamilias());
				}else{
					
					familiaService.gerarRelatorioAgregado(familiaService.recuperaListaDeFamiliasComModelos());
				}
			}
			
			
		} catch (AplicacaoException re){
			re.getMessage();
		}
	}
	
	
	/**
	 * Método que busca uma Familia por código/descrição aproximados
	 * redireciona para pagina list com a mensagem adequada:  encontrado/nao encontrado
	 * 
	 * @author walanem 
	 */
	public String buscaFamilia(){
		
		List<Familia> familiasEncontradas = null;
		
		if (campoDeBusca.trim().isEmpty()){
			error("familia.FORNECER_CAMPO_DE_BUSCA");
			return PAGINA_LIST;
		} 

		else {	
			
			listaDeFamilias = null;
			
			if (comboTiposDeBusca.getObjetoSelecionado().equals(BUSCA_POR_CODIGO)){
				familiasEncontradas = new ArrayList<Familia>(familiaService.
						recuperaListaDeFamiliasPeloCodigoLike(campoDeBusca));
			} else {
				familiasEncontradas = new ArrayList<Familia>(familiaService.
						recuperaListaDeFamiliasPorDescricao(campoDeBusca));
			}
			
			if (familiasEncontradas.isEmpty()){
				error("familia.NAO_ENCONTRADA");
				listaDeFamilias = null;
				return PAGINA_LIST;
			}else{
				info("familia.ENCONTRADAS");
			}
		}
		
		listaDeFamilias = new ListDataModel(familiasEncontradas);
		buscaEfetuada = true;
		
		return PAGINA_LIST;
	}
	
	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public DataModel getListaDeFamilias() {
		if (listaDeFamilias == null) {
			listaDeFamilias = new ListDataModel(familiaService.recuperaListaPaginadaDeFamilias());
		}
		return listaDeFamilias;
	}

	public void setListaDeFamilias(DataModel listaDeFamilias) {
		this.listaDeFamilias = listaDeFamilias;
	}

	public int getOpcaoRelatorioEscolhido() {
		return opcaoRelatorioEscolhido;
	}

	public void setOpcaoRelatorioEscolhido(int opcaoRelatorioEscolhido) {
		this.opcaoRelatorioEscolhido = opcaoRelatorioEscolhido;
	}

	public String getCampoDeBusca() {
		return campoDeBusca;
	}

	public boolean isBuscaEfetuada() {
		return buscaEfetuada;
	}

	public void setCampoDeBusca(String campoDeBusca) {
		this.campoDeBusca = campoDeBusca;
	}

	public void setBuscaEfetuada(boolean buscaEfetuada) {
		this.buscaEfetuada = buscaEfetuada;
	}
	
	public void setComboTiposDeBusca(SelectOneDataModel<String> comboTiposDeBusca) {
		this.comboTiposDeBusca = comboTiposDeBusca;
	}

	public SelectOneDataModel<String> getComboTiposDeBusca() {
		
		if (comboTiposDeBusca == null){
			
			List<String> tiposDeBusca = new ArrayList<String>(2);
			tiposDeBusca.add(BUSCA_POR_CODIGO);
			tiposDeBusca.add(BUSCA_POR_DESCRICAO);
			
			comboTiposDeBusca = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tiposDeBusca, BUSCA_POR_CODIGO);
		}
		
		return comboTiposDeBusca;
	}
	
}
