 
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

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import modelo.Modelo;
import modelo.Parametros;
import modelo.TecModel;
import modelo.Tecido;
import service.ModeloAppService;
import service.ParametrosAppService;
import service.TecModelAppService;
import service.TecidoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

/**
 * TecModelActions é uma classe relacionada à manipulação de tela, ou seja, a interação do ususário
 * de fato dar-se-á através de objetos do tipo TecModelActions quando na tela de TecModel.
 * Objetos do tipo "actions", nome aqui adotado, também são popularmente conhecidos como managebeans
 * em outras palavras beans gerenciáveis.
 * 
 * @author marques.araujo
 *
 */

public class TecModelActions extends BaseActions implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		// Services
		private static TecModelAppService tecModelService;
		private static TecidoAppService tecidoService;
		private static ModeloAppService modeloService;
		private static ParametrosAppService parametrosService;
		
		// Paginas
		public final String PAGINA_LIST = "listTecModel";
		public final String PAGINA_NEW  = "newTecModel";
		public final String PAGINA_SHOW = "showTecModel";
		public final String PAGINA_EDIT = "editTecModel";
		
		// Variaveis de Tela
		private TecModel tecModelCorrente;
		private Tecido tecidoCorrente;
		private Parametros parametrosCorrente;
		private DataModel listaDeTecidos;
		
		private Double aux = 0.0;
		
		private SelectOneDataModel<Modelo> comboModelos;
		private SelectOneDataModel<String> comboTiposDeBusca;
		private String campoDeBusca;
		private boolean buscaEfetuada = false;
		
		public final String BUSCA_POR_CODIGO = "Código";
		public final String BUSCA_POR_DESCRICAO = "Descrição";
		
		private int pagina;
		
		
		public TecModelActions() throws Exception {
			
			try {
				modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
				tecModelService = FabricaDeAppService.getAppService(TecModelAppService.class);
				tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
				parametrosService =  FabricaDeAppService.getAppService(ParametrosAppService.class);
			} catch (Exception e) {
				throw e;
			}
			
			pagina = ((TecidoActions) getManagedBean("tecidoActions")).getPagina();
		}
		
		/**
		 * Antes de incluir RecModel, verifica se tem modelos cadastrados e recursos
		 * cadastrados, e mostra as mensagens de erro se elas existirem
		 * e instancia RecModel se nao houver excecao
		 * retorna para a pagina new em caso de sucesso, caso contrario retorna para pagina list.
		 * @return String
		 * 
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
				tecidoService.recuperaListaDeTecidos();			
			}
			catch(AplicacaoException ae){
				error(ae.getMessage());
				return PAGINA_LIST;
			}
			
			tecModelCorrente = new TecModel();
			tecidoCorrente = (Tecido) listaDeTecidos.getRowData();
			tecModelCorrente.setTecido(tecidoCorrente);	
			
			return PAGINA_NEW;
		}
		
		public void setaTRDefaultDoModeloSelecionado(ValueChangeEvent event){
			Modelo modeloCorrente = recuperaModeloDoCombodeModelo(event.getNewValue().toString());
			tecModelCorrente.setTempoDefasagemUsoTec(modeloCorrente.getTr());
		}
		
		public String inclui(){
			
			tecModelCorrente.setModelo(comboModelos.getObjetoSelecionado());
			
			try{
				tecModelService.inclui(tecModelCorrente);
			} catch (AplicacaoException ex) {
				error(ex.getMessage());
				return PAGINA_NEW;
			}
			
			info("tecModel.SUCESSO_INCLUSAO");
			listaDeTecidos = null;
			
			return PAGINA_LIST;
		}
		
		public String preparaAlteracao(){			
			
			return PAGINA_EDIT;
		}
		
		public String altera() {
				
			tecModelService.altera(tecModelCorrente);
			
			info("tecModel.SUCESSO_ALTERACAO");		
			
			buscaEfetuada = false;
			listaDeTecidos = null;
			
			return PAGINA_LIST;
		}
		
		public String mostra(){
			
			tecModelCorrente = (TecModel) listaDeTecidos.getRowData();		
			return PAGINA_SHOW;
		}
		
		public String exclui(){
			
			try {
				tecModelService.exclui(tecModelCorrente);
			} catch (AplicacaoException e) {
			}
			
			info("tecModel.SUCESSO_EXCLUSAO");		
			buscaEfetuada = false;
			listaDeTecidos = null;
			
			return PAGINA_LIST;
		}
		
		public String cancela() {
			
			listaDeTecidos = null;
			buscaEfetuada = false;
			
			return PAGINA_LIST;
		}
		/**
		 * Metodo que percorre a lista de modelos do combo de modelos e 
		 * verifica qual deles tem o valor igual ao valorSelecionado.
		 * Entao retorna o modelo do combo que bateu com o resultado.
		 * @param valorSelecionado
		 * @return
		 */
		public Modelo recuperaModeloDoCombodeModelo(String valorSelecionado){
			for(SelectItem item : comboModelos.getListaSelecao()){
	            if (item.getLabel().equals(valorSelecionado)) {
	                return (Modelo) item.getValue();
	            }
			}
			return null;
		}
		
		// ============ Métodos get() e set() ================= //

		public TecModel getTecModelCorrente() {
			
			System.out.println(tecModelCorrente);
			
			return tecModelCorrente;
		}
		
		public void setTecModelCorrente(TecModel tecModelCorrente) {
			this.tecModelCorrente = tecModelCorrente;
		}

		public void setTecidoCorrente(Tecido tecidoCorrente) {
			this.tecidoCorrente = tecidoCorrente;
		}
		
		
		public Tecido getTecidoCorrente() {
			return tecidoCorrente;
		}

		public DataModel getListaDeTecidos() {
			
			if (listaDeTecidos == null){
				
				listaDeTecidos = new ListDataModel(tecidoService.
						recuperaListaPaginadaDeTecidosComListaDeTecModels());
			}
				
					
			return listaDeTecidos;
		}
		

		public void setListaDeTecidos(DataModel listaDeTecidos) {
			this.listaDeTecidos = listaDeTecidos;
		}
		
		/**
		 * Este método é responsável por retornar uma lista de Modelos.
		 * @return SelectOneDataModel<Modelo>
		 * 
		 */		
		public SelectOneDataModel<Modelo> getComboModelos() {
			
			if (comboModelos == null){

				try{
					comboModelos = SelectOneDataModel.
					   criaSemTextoInicial(modeloService.recuperaListaDeModelos());
								
				}
				catch(AplicacaoException ae){
					error(ae.getMessage());
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

        
		/**
		 * Exatamente igual ao metodo no cadastro de modelos
		 * talvez seja interessante refatorar para colocar num lugar só
		 * para utilizaçao de todos que usem combobox.
		 * 
		 */
		public void setBuscaEfetuada(boolean buscaEfetuada) {
			this.buscaEfetuada = buscaEfetuada;
		}

		/**
		 * Exatamente igual ao metodo no cadastro de modelos
		 * talvez seja interessante refatorar para colocar num lugar só
		 * para utilizaçao de todos que usem combobox
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
		 * Este metodo é responsável pelo cálculo do consumo por lote em quilograma.
		 * @author marques.araujo
		 * @param ValueChangeEvent evento  
		 * @return void
		 * 
		 */
		public void calculaConsumoPorLoteKgListener (ValueChangeEvent evento){

			Double newValue = (Double)evento.getNewValue();

			Double percentual = parametrosService.recuperaListaDeParametros().get(0).getPercentualDePerda();
			
			Double consumoMt = tecModelCorrente.getConsumoPorLoteMt();
	
			Double fatorDeRendimento = tecModelCorrente.getTecido().getFatorDeRendimento();//  tecidoCorrente.getFatorDeRendimento();
	
			if(!tecModelCorrente.getConsumoPorLoteMt().equals((Double)evento.getNewValue())){
	
				tecModelCorrente.setConsumoPorLoteKg(((Double)evento.getNewValue()*(percentual/100 + 1))/tecModelCorrente.getTecido().getFatorDeRendimento());
		
			}

		}

		public Double getAux() {
			return aux;
		}

		public void setAux(Double aux) {
			this.aux = aux;
		}
		
		
		/**
		 * Este método é usado para impressão, caso não haja modelos associados
		 * a tecidos uma mensagem de alerta sera enviada ao usuário.
		 * @author marques.araujo
		 * @return void
		 */
		public void imprimir(){
			
			try{
				
				if(tecidoService.recuperaListaDeTecidosQueTenhamApenasTecModels().isEmpty()){
					throw new AplicacaoException("tecido.TECIDOS_SEM_MODELOS");
				}
			      
					tecidoService.gerarRelatorioAgregado(tecidoService.recuperaListaDeTecidosComTecModels());
								
			} catch (AplicacaoException re){
				
				if(re.getMessage().equals("tecido.TECIDOS_SEM_MODELOS")){
					  error("tecido.TECIDOS_SEM_MODELOS");
				}else{
				      error("tecido.TECIDOS_INEXISTENTES");
				}
			}
	
	   }
    
}		
