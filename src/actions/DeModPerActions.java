 
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
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import modelo.DeModPer;
import modelo.Modelo;
import modelo.ModeloDecorado;
import br.blog.arruda.plot.Plot;
import service.DeModPerAppService;
import service.ModeloAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class DeModPerActions extends BaseActions implements Serializable{
	
	// Services
	private static DeModPerAppService demandaModeloPeriodoService;
	private static ModeloAppService modeloService;
	
	// Paginas
	public final String PAGINA_LIST = "listDeModPer";
	public final String PAGINA_GRAFICO = "graficoDeModPer";
	
	// Componentes de controle
	private DataModel listaDeModPer;
	private DataModel listaModelos;
	
	private SelectOneDataModel<String> comboTiposDeBusca;
	private List<DeModPer> demandasModelo;
	
	// Variaveis de Tela
	private DeModPer demanda;
	private ModeloDecorado modeloDecoradoCorrente;
	private Modelo modeloCorrente;
	
	private int pagina = 1;
	private boolean buscaEfetuada = false;
	private String campoDeBusca;
	
	public final String BUSCA_POR_CODIGO = "Código";
	public final String BUSCA_POR_DESCRICAO = "Descrição";

	public Plot plotDeModPer;
	
	public DeModPerActions(){
		
		try {
			demandaModeloPeriodoService = FabricaDeAppService.getAppService(DeModPerAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		plotDeModPer = null;
	}

	@SuppressWarnings("unchecked")
	public String exibirDemandas(){
		
		if (modeloService.recuperaListaDeModelosComFamilias().isEmpty()){
			error("modelo.MODELOS_NAO_CADASTRADOS");
			return null;
		} 
		
		return PAGINA_LIST;
	}

	@SuppressWarnings("unchecked")
	public String exibirGrafico(){

		modeloCorrente = (Modelo) listaModelos.getRowData();
		
		try {
			demandasModelo = modeloService.recuperaModeloComFamiliaEPeriodos
								(modeloCorrente).getDeModPers();
			
			// Aqui estamos obrigando a lista ficar ordenada, ja que definimos como a classe DeModPer deve se ordenar.
			Collections.sort(demandasModelo);			
		} catch (AplicacaoException e) {
		}
		
		plotDeModPer = demandaModeloPeriodoService.gerarDadosGrafico(demandasModelo);
		
		return PAGINA_GRAFICO;
	}
	
	
	/**
	 * Método que busca um Modelo por código/descrição aproximados
	 * 
	 * @author walanem 
	 */
	public String buscaModelo(){
		
		List<Modelo> modelosEncontrados = null;
		
		if (campoDeBusca.trim().isEmpty()){
			error("modelo.FORNECER_CAMPO_DE_BUSCA");
			return PAGINA_LIST;
			
		} else {	
			
			listaModelos = null;
			
			if (comboTiposDeBusca.getObjetoSelecionado().equals(BUSCA_POR_CODIGO)){
				modelosEncontrados = new ArrayList<Modelo>(modeloService.recuperaModeloPorCodigoLike(campoDeBusca));
			} else {
				modelosEncontrados = new ArrayList<Modelo>(modeloService.recuperaModeloPorDescricao(campoDeBusca));
			}
			
			if (modelosEncontrados.isEmpty()){
				
				error("modelo.NAO_ENCONTRADO");
				
			} else {
				
				info("modelo.ENCONTRADOS");
				
				listaModelos = new ListDataModel(modelosEncontrados);
				
				buscaEfetuada = true;
			}
		}
		
		return PAGINA_LIST;
	}
	
	
	@SuppressWarnings("unchecked")
	public String informarDemandasPorModeloPeriodo(){
		
		modeloDecoradoCorrente = (ModeloDecorado) listaModelos.getRowData();
		
		try {
			demandasModelo = modeloService.recuperaModeloComFamiliaEPeriodos
								(modeloDecoradoCorrente.getModelo()).getDeModPers();
			
			// Aqui estamos obrigando a lista ficar ordenada, ja que definimos como a classe DeModPer deve se ordenar.
			Collections.sort(demandasModelo);
			
		} catch (AplicacaoException e) {
		}
		
		if (demandasModelo.isEmpty()){
			error("modelo.NAO_EXISTEM_DEMODPERS_ASSOCIADAS");
			
		} else {
			
			pagina = ((List<Modelo>) listaModelos.getWrappedData()).indexOf(modeloDecoradoCorrente) + 1;
			
			listaDeModPer = new ListDataModel(demandasModelo);
		}
		
		return PAGINA_LIST;
	}
	
	public String altera(){
		
		demandaModeloPeriodoService.altera(demanda);
		
		info("demodper.SUCESSO_ALTERACAO");
		
		buscaEfetuada = false;
		
		listaDeModPer = null;
		listaModelos = null;
		modeloCorrente=null;
		plotDeModPer = null;
		return PAGINA_LIST;
	}
	
	public String cancelar(){
		
		buscaEfetuada = false;
		listaModelos = null;
		modeloCorrente=null;
		plotDeModPer = null;
		
		return PAGINA_LIST;
	}
	
	
	// ================================== Métodos get() e set() ================================== //
	
	public void setTabelaDemandaModeloPeriodo(DataModel tabelaDemandaModeloPeriodo) {
		this.listaDeModPer = tabelaDemandaModeloPeriodo;
	}

	public DataModel getListaDeModPer() {
		return listaDeModPer;
	}

	public void setModeloDecoradoCorrente(ModeloDecorado modeloDecoradoCorrente) {
		this.modeloDecoradoCorrente = modeloDecoradoCorrente;
	}

	public ModeloDecorado getModeloDecoradoCorrente() {
		return modeloDecoradoCorrente;
	}

	public void setListaModelos(DataModel listaModelos) {
		this.listaModelos = listaModelos;
	}

	public DataModel getListaModelos() {
		
		if (listaModelos == null){
			List<Modelo> modelos = modeloService.recuperaListaDeModelosComFamiliasEPeriodos();
			for (Modelo modelo : modelos) {
				Collections.sort(modelo.getDeModPers());
			}
			listaModelos = new ListDataModel(modelos);
		}
		
		return listaModelos;
	}

	public void setDemanda(DeModPer demanda) {
		this.demanda = demanda;
	}

	public DeModPer getDemanda() {
		return demanda;
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

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public int getPagina() {
		return pagina;
	}

	public void setDemandasModelo(List<DeModPer> demandasModelo) {
		this.demandasModelo = demandasModelo;
	}

	public List<DeModPer> getDemandasModelo() {
		return demandasModelo;
	}

	
	

	public String getPAGINA_GRAFICO() {
		return PAGINA_GRAFICO;
	}

	public Modelo getModeloCorrente() {
		return modeloCorrente;
	}

	public void setModeloCorrente(Modelo modeloCorrente) {
		this.modeloCorrente = modeloCorrente;
	}

	public Plot getPlotDeModPer() {
		return plotDeModPer;
	}

	public void setPlotDeModPer(Plot plotDeModPer) {
		this.plotDeModPer = plotDeModPer;
	}
}
