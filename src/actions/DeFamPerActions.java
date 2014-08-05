 
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

import modelo.DeFamPer;
import modelo.DeModPer;
import modelo.Familia;
import modelo.Modelo;
import modelo.PerioPAP;
import modelo.PerioPM;
import br.blog.arruda.plot.Plot;
import service.DeFamPerAppService;
import service.FamiliaAppService;
import service.PerioPAPAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

/**
 * DeFamPerActions é uma classe relacionada à manipulação de tela, ou seja, a interação do ususário
 * de fato dar-se-á através de objetos do tipo DeFamPerActions quando na tela de DeFamPer.
 * Objetos do tipo "actions", nome aqui adotado, também são popularmente conhecidos como managebeans
 * em outras palavras beans gerenciáveis.
 * 
 * @author marques.araujo
 *
 */

public class DeFamPerActions extends BaseActions implements Serializable {
	// Services
	private static DeFamPerAppService demandaFamiliaPeriodoService;
	private static FamiliaAppService familiaService;
	private static PerioPAPAppService perioPAPAppService;
	
	// Paginas
	public final String PAGINA_LIST = "listDeFamPer";
	public final String PAGINA_GRAFICO = "graficoDeFamPer";
	
	// Componentes de controle
	private DataModel listaDeFamPer;
	private DataModel listaDeFamilias;
	
	private SelectOneDataModel<String> comboTiposDeBusca;
	private List<DeModPer> listaDeModPer;
	private List<DeFamPer> demandasFamilia;
	
	// Variaveis de Tela
	private DeFamPer demanda;
	private Familia familiaCorrente;
	private DeFamPer deFamPerCorrente;
	
	private String campoDeBusca;
	private boolean buscaEfetuada = false;
	private boolean renderizarDemandas = false;
	
	public final String BUSCA_POR_CODIGO = "Código";
	public final String BUSCA_POR_DESCRICAO = "Descrição";
	private int pagina;
	private Plot plotDeFamPer;
	
	
	public DeFamPerActions(){
		
		try {
			demandaFamiliaPeriodoService = FabricaDeAppService.getAppService(DeFamPerAppService.class);
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
			perioPAPAppService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		plotDeFamPer = null;
	}
	
	/**
	 * Exibe a tela do grafico de DeFamPer
	 * @return
	 */
	public String exibirGrafico(){
		familiaCorrente = (Familia) listaDeFamilias.getRowData();
		
			demandasFamilia = familiaCorrente.getDeFamPers();
			
			// Aqui estamos obrigando a lista ficar ordenada, ja que definimos como a classe DeModPer deve se ordenar.
			Collections.sort(demandasFamilia);			
			
		plotDeFamPer = demandaFamiliaPeriodoService.gerarDadosGrafico(demandasFamilia);
		return PAGINA_GRAFICO;
	}
	
	
	/**
	 * Este metodo esta sendo usado no template principal
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String exibirDemandas(){
		
		List<Familia> familiasCadastradas = familiaService.recuperaListaDeFamilias();
		
		if (familiasCadastradas.isEmpty()){
			error("familia.FAMILIAS_NAO_CADASTRADAS");
		} 
		
		return PAGINA_LIST;
	}
	
	/**
	 * Totaliza todos os DeFamPers do sistema com base nos dados dos seus
	 * respectivos DeModPers.
	 * @return
	 */
	public String totalizarTodosDeFamPers(){
		//totaliza nesse ponto
		demandaFamiliaPeriodoService.totalizaDeModPerParaTodosDeFamPers();
		//Captura novamente a lista de familia, para vir atualizada.
		listaDeFamilias = null;
		return PAGINA_LIST;
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
	
	
	
	
	public String altera(){
		
		demandaFamiliaPeriodoService .altera(demanda);
		
		info("defamper.SUCESSO_ALTERACAO");
		
		buscaEfetuada = false;
		
		listaDeFamPer = null;
		listaDeFamilias = null;
		plotDeFamPer = null;
		
		return PAGINA_LIST;
	}
	
	
	
		
	public String cancelar(){
		
		buscaEfetuada = false;
		listaDeFamilias = null;
		plotDeFamPer = null;
		
		return PAGINA_LIST;
	}
	
	
	
	
	
	// ================================== Métodos get() e set() ================================== //
	
	
	public void setDemanda(DeFamPer demanda) {
		this.demanda = demanda;
	}

	public DeFamPer getDemanda() {
		return demanda;
	}	
	
	public void desabilitarDemandas(){
		renderizarDemandas = false;
	}
	
	public void setRenderizarDemandas(boolean renderizarDemandas) {
		this.renderizarDemandas = renderizarDemandas;
	}

	public boolean isRenderizarDemandas() {
		return renderizarDemandas;
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
	
	
	public void setListaDeFamilias(DataModel listaDeFamilias) {
		this.listaDeFamilias = listaDeFamilias;
	}
	
	
	
	public DataModel getListaDeFamilias() {
				
		if (listaDeFamilias == null){
			
		    List<Familia> familias = familiaService.recuperaListaDeFamiliasComDeFamPers();
			
			for (Familia familia : familias) {
			  
				for (DeFamPer deFamper : familia.getDeFamPers()) {
					
					  PerioPM perioPMInicial = perioPAPAppService.obtemPrimeiroPerioPMdoPerioPAP(deFamper.getPerioPAP());
					  PerioPM perioPMFinal = perioPAPAppService.obtemUltimoPerioPMdoPerioPAP(deFamper.getPerioPAP());
								  
					  PerioPAP perioPAP =  deFamper.getPerioPAP();
									 
					  perioPAP.setDataInicial(perioPMInicial.getDataInicial());
					  perioPAP.setDataFinal(perioPMFinal.getDataFinal());
				}
				
			  }
			
					
			for (Familia familia : familias) {
				Collections.sort(familia.getDeFamPers());
			}
			
			listaDeFamilias = new ListDataModel(familias);
		}
		
		return listaDeFamilias;
	}

	
	
	public Familia getFamiliaCorrente() {
		return familiaCorrente;
	}


	public void setFamiliaCorrente(Familia familiaCorrente) {
		this.familiaCorrente = familiaCorrente;
	}


	public void setListaDeFamPer(DataModel listaDeFamPer) {
		this.listaDeFamPer = listaDeFamPer;
	}
	
	
	public DataModel getListaDeFamPer() {
		return listaDeFamPer;
	}


	public Plot getPlotDeFamPer() {
		return plotDeFamPer;
	}

	public void setPlotDeFamPer(Plot plotDeFamPer) {
		this.plotDeFamPer = plotDeFamPer;
	}
	
	
	
}
