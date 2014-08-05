 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.util.Date;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.CapacDia;
import modelo.Parametros;
import modelo.PerioPM;



import service.CapacDiaAppService;
import service.ParametrosAppService;
import service.controleTransacao.FabricaDeAppService;
import util.DataUtil;
import util.SelectOneDataModel;

public class CapacDiaActions extends BaseActions {

	// Paginas
	public final String PAGINA_LIST = "listCapacDia";
	public final String PAGINA_EDIT = "editCapacDia";
	public final String PAGINA_EDIT_CAPACPADRAO = "editCapacPadrao";
	
	
	// Services
	private static CapacDiaAppService capacDiaService;
	private static ParametrosAppService parametrosService;
	
	// Variaveis de Tela
	private CapacDia capacDiaCorrente;
	
	/** Campo existente apenas em tela  e que sera usado para
	 * propagar a mesma capacidade diaria para todos os periodos
	 */
	private double capacidadePadrao;
	
	// Componentes de Controle
	private DataModel listaCapacDia;
	private Parametros parametroCorrente;
	
	
	public CapacDiaActions(){
		
		try {
			capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			
		} catch (Exception e) {
		}
	}


	/**
	 * Usado na edição.
	 * É necessario fazer listaCapacDia e parametroCorrente como null para forçar
	 * o novo get dos mesmos.
	 * @return
	 */
	public String cancela() { 
		
		listaCapacDia = null;
		parametroCorrente = null;
		
		return PAGINA_LIST;
	}

	public String cancelaModal() {
		
		
		parametroCorrente = null;
		
		return PAGINA_LIST;
	}	
	
	/**
	 * Recebe a lista do bean e insere na requisição o CapacDia selecionado
	 *  na pagina "list"
	 *  Obtem o campo calculado capacProdDiariaEmMinMg desse CapacDia.
	 *  A partir do Parametros.MargemSeguranca.
	 *  Retorna "PAGINA_EDIT" e vai para a pagina "edit" onde é chamado o metodo altera()
	 *  para confirmar a edicao.
	 *  @return
	 */
	public String preparaAlteracao() {

		capacDiaCorrente = (CapacDia) listaCapacDia.getRowData();
        	
		
		//Atribui valores atuais para os campos calculados
		capacDiaCorrente.setCapacProdDiariaEmMinMg(capacDiaCorrente.getCapacProdDiariaEmMin()*(parametroCorrente.getMargemSeguranca()/100));
		
		return PAGINA_EDIT;
	}
	
	/**
	 * Atribui valor para o campo calculado: CapacProdDiariaEmMinMg.
	 * E altera  o campo calculado capacDiaCorrente.
	 * 
	 * @return
	 */
	public String altera() {
		
		capacDiaService.altera(capacDiaCorrente);
		
				
		info("capacDia.SUCESSO_ALTERACAO");
		listaCapacDia = null;
		parametroCorrente = null;
		return PAGINA_LIST;	
		
	}	

	/**
	 * Alteracao da Margem de Seguranca
	 * retorna para a tela capacidade de producao diaria na matriz
	 **/
	public String alteraMargemSeg(){
		
		parametrosService.altera(parametroCorrente);
		
		info("parametros.SUCESSO_ALTERACAO_MARGEM");
		listaCapacDia = null;
		parametroCorrente = null;
		
		return PAGINA_LIST;
	}

	public String PreparaAlteraCapacPadrao(){
		
		
		return PAGINA_EDIT_CAPACPADRAO;
	}
	/**
	 * Alteracao da capacidade de producao diaria em todos os registros a partir
	 * da variavel capacidadePadrao informada pelo usuario
	 * retorna para a tela capacidade de producao diaria na matriz
	 **/
	public String alteraCapacPadrao(){
		
		capacDiaService.alteraCapacPadrao(capacidadePadrao);
		
		info("capacDia.SUCESSO_ALTERACAO_CAPACPADRAO");
		listaCapacDia = null;
		parametroCorrente = null;
		
		return PAGINA_LIST;
	}

	
	// ================================== Métodos get() e set() ================================== //
	

	public double getCapacidadePadrao() {
		return capacidadePadrao;
	}
	
	public void setCapacidadePadrao(double capacidadePadrao) {
		this.capacidadePadrao = capacidadePadrao;
	}
	

	public CapacDia getCapacDiaCorrente() {
		return capacDiaCorrente;
	}
	
	public void setCapacDiaCorrente(CapacDia capacDiaCorrente) {
		this.capacDiaCorrente = capacDiaCorrente;
	}

	public DataModel getListaCapacDia() {

		if(parametroCorrente == null){
			parametroCorrente = new Parametros();
			parametroCorrente = parametrosService.recuperaListaDeParametros().get(0);			
		}
		
		if (listaCapacDia == null){
			listaCapacDia = new ListDataModel(capacDiaService.recuperaListaPaginadaDeCapacDias());
		}
		
		return listaCapacDia;
	}
	public void setListaCapacDia(DataModel listaCapacDia) {
		this.listaCapacDia = listaCapacDia;
	}
	public void setParametroCorrente(Parametros parametroCorrente) {
		this.parametroCorrente = parametroCorrente;
	}

	public Parametros getParametroCorrente() {
		
		if (parametroCorrente == null){
			parametroCorrente = new Parametros();
			parametroCorrente = parametrosService.recuperaListaDeParametros().get(0);
		}
		
		return parametroCorrente;
	}

}
