 
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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import comparator.PlPerModComparatorPorPerioPM;

import DAO.exception.ObjetoNaoEncontradoException;
import modelo.CadPlan;
import modelo.Modelo;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import br.blog.arruda.plot.Plot;
import service.CadPlanAppService;
import service.ModeloAppService;
import service.PlPerModAppService;
import service.PlanoModeloAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

public class PlPerModActions extends BaseActions implements Serializable{
	
	// Services
	private ModeloAppService modeloService;
	private PlanoModeloAppService planoModeloService;
	private PlPerModAppService plPerModService;
	private CadPlanAppService cadPlanService;
	
	private CadPlan cadPlanCorrente;
	private PlanoModelo planoModeloCorrente;
	private PlPerMod plPerModCorrente;
	
	private DataModel listaPlanoModelos;
	private DataModel listaPlPerMods;
	
	private int pagina = 1;
	private String opcaoEscolhidaAlteracao;
	
	// Paginas
	public final String PAGINA_LIST = "listPlPerMod";
	public final String PAGINA_EDIT = "editPlPerMod";
	
	public final String PAGINA_GRAFICO_PLPERMOD = "graficoPlPerMod";
	public final String PAGINA_GRAFICO_AVALIACAOFUZZY = "graficoPlPerModAvaliacaoFuzzy";
	
	private String LABEL_PAGINA_EDICAO = "";
	

	public Plot plotPlPerMod;
	
	public PlPerModActions() {
		
		try {
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			
		} catch (Exception ex){
		}
		
		cadPlanCorrente = ((CadPlanActions) getManagedBean("cadPlanActions")).getCadPlanCorrente();
		plotPlPerMod = null;
	}
	
	/**
	 * Este método é utilizado para atualizar a Lista de PlPerMods cada vez que ocorre uma mudança de página no Datascroller
	 */
	public void atualizarListaPlPerMods(){
		
		listaPlPerMods = null;
		
	}
	
	public String preparaAlteracao(){
		
		if(opcaoEscolhidaAlteracao.equals("vendasModel")){
			LABEL_PAGINA_EDICAO = "Vendas";
		}
		if(opcaoEscolhidaAlteracao.equals("coberturaModel")){
			LABEL_PAGINA_EDICAO = "Cobertura";
		}
		if(opcaoEscolhidaAlteracao.equals("pedidosModel")){
			LABEL_PAGINA_EDICAO = "Pedidos";
		}
		if(opcaoEscolhidaAlteracao.equals("dispProjModel")){
			LABEL_PAGINA_EDICAO = "Disponibilidade Projetada";
		}
		if(opcaoEscolhidaAlteracao.equals("prodLoteModel")){
			LABEL_PAGINA_EDICAO = "Produção em Lote";
		}
		if(opcaoEscolhidaAlteracao.equals("producaoModel")){
			LABEL_PAGINA_EDICAO = "Produção";
		}
		if(opcaoEscolhidaAlteracao.equals("prodDiariaLoteModel")){
			LABEL_PAGINA_EDICAO = "Produção Diária em Lote";
		}
		
		return PAGINA_EDIT;
	}
	
	public String cancelar() {
		
		listaPlanoModelos = null;
		listaPlPerMods = null;
		plotPlPerMod = null;
		
		return PAGINA_LIST;
	}
	
	/**
	 * Pegando apenas os PlPerMods do modelo selecionado.
	 * Grafico de DemandaMax , Producao E EstProj Por Periodo
	 * @return
	 */
	public String exibirGraficoDemandaMaxEProducaoEEstProjPorPeriodo(){
		listaPlPerMods=null;
		listaPlPerMods = getListaPlPerMods();
		
		plotPlPerMod = plPerModService.gerarPlotDemandaMaxEProducaoEEstProjPorPeriodo((List<PlPerMod>) listaPlPerMods.getWrappedData());
		
		return PAGINA_GRAFICO_PLPERMOD;
	}

	/**
	 * Pegando apenas os PlPerMods do modelo selecionado.
	 * Grafico com DemandaMax , Producao , EstProj e ESDesejado Por Periodo
	 * @return
	 */
	public String exibirGraficoDemandaMaxEProducaoEEstProjEESDesejadoPorPeriodo(){
		listaPlPerMods=null;
		listaPlPerMods = getListaPlPerMods();
		
		plotPlPerMod = plPerModService.gerarPlotDemandaMaxEProducaoEEstProjEESDesejadoPorPeriodo((List<PlPerMod>) listaPlPerMods.getWrappedData());
		return PAGINA_GRAFICO_AVALIACAOFUZZY;
	}
	
	public String altera(){
		
		try{
			plPerModService.altera(plPerModCorrente);
		} catch (Exception ex){
			error(ex.getMessage());
			return PAGINA_EDIT;
		}
		
		info("cadplan.SUCESSO_AJUSTE");

		//Fazer aqui a verificacao de qual foi o campo alterado para poder chamar metodo de recalcula
		if(opcaoEscolhidaAlteracao.equals("vendasModel")  || opcaoEscolhidaAlteracao.equals("pedidosModel") || opcaoEscolhidaAlteracao.equals("coberturaModel")){
			boolean edicaoCobertura = opcaoEscolhidaAlteracao.equals("coberturaModel");
			plPerModService.recalculaParametrosBaseadoCobertura(plPerModCorrente,edicaoCobertura);
		}
		//Fazer aqui a verificacao de qual foi o campo alterado para poder chamar metodo de recalcula
		if(opcaoEscolhidaAlteracao.equals("dispProjModel")){
			plPerModService.recalculaParametrosBaseadoDispProj(plPerModCorrente);
		}
		//Fazer aqui a verificacao de qual foi o campo alterado para poder chamar metodo de recalcula
		if(opcaoEscolhidaAlteracao.equals("prodLoteModel")){
			plPerModService.recalculaParametrosBaseadoProducao(plPerModCorrente);
		}
		//Fazer aqui a verificacao de qual foi o campo alterado para poder chamar metodo de recalcula
		if(opcaoEscolhidaAlteracao.equals("producaoModel")){
			plPerModService.recalculaParametrosBaseadoProdEmPecas(plPerModCorrente);
		}
		//Fazer aqui a verificacao de qual foi o campo alterado para poder chamar metodo de recalcula
		if(opcaoEscolhidaAlteracao.equals("prodDiariaLoteModel")){
			plPerModService.recalculaParametrosBaseadoProdDiaria(plPerModCorrente);
		}

		listaPlanoModelos = null;
		
		return PAGINA_LIST;
	}

	public String mudarStatusFlagProducao() {

		
		if (plPerModCorrente.getFlagProducaoModel()){
			plPerModCorrente.setFlagProducaoModel(false);
		} else {
			plPerModCorrente.setFlagProducaoModel(true);
		}

		try{
			plPerModService.altera(plPerModCorrente);
		} catch (Exception ex){
			error(ex.getMessage());
			return PAGINA_LIST;
		}
		
		info("plPerMod.SUCESSO_ALTERACAO_STATUS");
		
		return PAGINA_LIST;
	}
	
	public String mudarStatus() {
		
		planoModeloCorrente = (PlanoModelo) listaPlanoModelos.getRowData();
		
		if (planoModeloCorrente.isModeloPlanejado()){
			planoModeloCorrente.setModeloPlanejado(false);
		} else {
			planoModeloCorrente.setModeloPlanejado(true);
		}
		
		planoModeloService.altera(planoModeloCorrente);
		
		info("modelo.SUCESSO_ALTERACAO");
		
		return PAGINA_LIST;
	}
	
	public String mudarStatusGeral() {
		
		List<PlanoModelo> planosModelo = ((List<PlanoModelo>) listaPlanoModelos.getWrappedData());
		
		for (PlanoModelo planoModelo : planosModelo) {
			
			if (planoModelo.isModeloPlanejado()){
				planoModelo.setModeloPlanejado(false);
			} else {
				planoModelo.setModeloPlanejado(true);
			}
			
			planoModeloService.altera(planoModelo);
		}
		
		info("modelo.SUCESSO_ALTERACAO");
		
		return PAGINA_LIST;
	}
	
	public void imprimir(){
		
		try{
			CadPlan cadplan = cadPlanService.recuperaCadPlanApenasComPlanosModelo(this.getCadPlanCorrente().getCodPlan());
			
			if(cadplan.getPlanosModelo().size()==0 || cadplan==null){
				throw new AplicacaoException();	
			}
			
			plPerModService.gerarRelatorioAgregado(cadplan);    
			
		} catch (AplicacaoException re){
			error("cadplan.NAO_ENCONTRADO");//Nao existem planos associados a este cadastro de planos.
		}
				
	}
	
	
	// --------------------------- Métodos get() e set() --------------------------------
	
	public CadPlan getCadPlanCorrente() {
		return cadPlanCorrente;
	}

	public PlanoModelo getPlanoModeloCorrente() {
		return planoModeloCorrente;
	}

	public void setCadPlanCorrente(CadPlan cadPlanCorrente) {
		this.cadPlanCorrente = cadPlanCorrente;
	}

	public void setPlanoModeloCorrente(PlanoModelo planoModeloCorrente) {
		this.planoModeloCorrente = planoModeloCorrente;
	}

	public void setListaPlanoModelos(DataModel listaPlanoModelos) {
		this.listaPlanoModelos = listaPlanoModelos;
	}

	public DataModel getListaPlanoModelos() {
		
		if (listaPlanoModelos == null){
			
			List<PlanoModelo> planoModelos = new ArrayList(cadPlanCorrente.getPlanosModelo());
			Collections.sort(planoModelos);		// Ja deveria vir ordenado (devido a inserção), mas para garantir, ordeno novamente.
			
			for (PlanoModelo planoModelo : planoModelos) {
				
				try {
					
					// Trazendo a informação da Familia associada ao Modelo
					planoModelo.setModelo(modeloService.recuperaUmModeloComFamilia(planoModelo.getModelo()));
					
					// Trazendo os PlPerMods deste PlanoModelo
					List<PlPerMod> plPerMods = plPerModService.recuperaListaDePlPerModPorPlanoModelo(planoModelo);
					Collections.sort(plPerMods, new PlPerModComparatorPorPerioPM());
					planoModelo.setPlPerModsList(plPerMods);
					
				} catch (AplicacaoException e) {
				} 
			}
			
			listaPlanoModelos = new ListDataModel(planoModelos);
		}
		
		return listaPlanoModelos;
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
		
		planoModeloCorrente = ((List<PlanoModelo>) listaPlanoModelos.getWrappedData()).get(pagina - 1);
		
		List<PlPerMod> plPerMods = plPerModService.recuperaListaDePlPerModPorPlanoModelo(planoModeloCorrente);
		Collections.sort(plPerMods, new PlPerModComparatorPorPerioPM());
		
		// Sempre vai ser NULL pois o método atualizar() garante este estado
		if (listaPlPerMods == null){
			listaPlPerMods = new ListDataModel(plPerMods);
		}
		
		return listaPlPerMods;
	}

	public void setPlPerModCorrente(PlPerMod plPerModCorrente) {
		this.plPerModCorrente = plPerModCorrente;
	}

	public PlPerMod getPlPerModCorrente() {
		return plPerModCorrente;
	}

	public void setOpcaoEscolhidaAlteracao(String opcaoEscolhidaAlteracao) {
		this.opcaoEscolhidaAlteracao = opcaoEscolhidaAlteracao;
	}

	public String getOpcaoEscolhidaAlteracao() {
		return opcaoEscolhidaAlteracao;
	}

	public String getLABEL_PAGINA_EDICAO() {
		return LABEL_PAGINA_EDICAO;
	}

	public void setLABEL_PAGINA_EDICAO(String lABELPAGINAEDICAO) {
		LABEL_PAGINA_EDICAO = lABELPAGINAEDICAO;
	}


	public Plot getPlotPlPerMod() {
		return plotPlPerMod;
	}

	public void setPlotPlPerMod(Plot plotPlPerMod) {
		this.plotPlPerMod = plotPlPerMod;
	}
}
