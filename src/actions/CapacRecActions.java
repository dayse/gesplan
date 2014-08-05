 
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
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.ajax4jsf.context.AjaxContext;

import exception.relatorio.RelatorioException;

import modelo.Modelo;
import modelo.PerioPM;
import modelo.CapacRec;
import modelo.PlanoModelo;
import modelo.Recurso;

import service.PerioPMAppService;
import service.CapacRecAppService;
import service.RecursoAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class CapacRecActions extends BaseActions {
	
	// Services
	private static CapacRecAppService capacRecService;
	private static RecursoAppService recursoService;
	private static PerioPMAppService perioPMService;

	// Paginas
	public final String PAGINA_LIST = "listCapacRec";
	public final String PAGINA_EDIT = "editCapacRec";
	public final String PAGINA_EDIT_CAPACPADRAO = "editCapacPadrao";
	
	// Variaveis de Tela
	private CapacRec capacRecCorrente;
	private Recurso recursoCorrente;
	private DataModel listaDeRecursos;
	
	/**
	 *  Campo existente apenas em tela  e que sera usado para
	 * propagar a mesma capacidade para todos os periodos
	 */
	private double capacidadePadrao;
	
	
	private int pagina;
	
	
	public CapacRecActions() throws Exception {
		try {
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			capacRecService = FabricaDeAppService.getAppService(CapacRecAppService.class);
			recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
		} catch (Exception e) {
			throw e;
		}
		
		// Resgata a pagina do managed bean "recursoActions" que foi mantido na requisao através do keepAlive
		pagina = ((RecursoActions) getManagedBean("recursoActions")).getPagina();
	}
	
	/**
	 * obtem do data Model (tabelaRecModels) o objeto recModelCorrente
	 * @return
	 */
	public String preparaAlteracao(){			
		
		return PAGINA_EDIT;
	}
	

	public String PreparaAlteraCapacPadrao(){
		
		
		return PAGINA_EDIT_CAPACPADRAO;
	}
	
	public String altera() {
			
		capacRecService.altera(capacRecCorrente);
		
		info("capacRec.SUCESSO_ALTERACAO");		
		
		listaDeRecursos = null;
		
		return PAGINA_LIST;
	}

	/**
	 * Alteracao da capacidade de todos os registros a partir
	 * da variavel capacidadePadrao informada pelo usuario
	 * retorna para a tela list de capacrec.
	 **/
	public String alteraCapacPadrao(){

		recursoCorrente = ((List<Recurso>) listaDeRecursos.getWrappedData()).get(pagina - 1);
		capacRecService.alteraCapacPadrao(recursoCorrente, capacidadePadrao);
		
		info("capacRec.SUCESSO_ALTERACAO_CAPACPADRAO");
		listaDeRecursos = null;
		
		return PAGINA_LIST;
	}
	

	
	public String cancela() {
		
		listaDeRecursos = null;
		
		return PAGINA_LIST;
	}
	
	
	
	// ================================== Métodos get() e set() ================================== //

	
	public CapacRec getCapacRecCorrente() {
		return capacRecCorrente;
	}

	public void setCapacRecCorrente(CapacRec capacRecCorrente) {
		this.capacRecCorrente = capacRecCorrente;
	}


	public void setRecursoCorrente(Recurso recursoCorrente) {
		this.recursoCorrente = recursoCorrente;
	}

	public Recurso getRecursoCorrente() {
		return recursoCorrente;
	}

	public DataModel getListaDeRecursos() {
		
		if (listaDeRecursos == null){
			
			listaDeRecursos = new ListDataModel(recursoService.recuperaListaPaginadaDeRecursosComListaDeCapacRecs());
			
		}

		return listaDeRecursos;
	}

	public void setListaDeRecursos(DataModel listaDeRecursos) {
		this.listaDeRecursos = listaDeRecursos;
	}




	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public double getCapacidadePadrao() {
		return capacidadePadrao;
	}

	public void setCapacidadePadrao(double capacidadePadrao) {
		this.capacidadePadrao = capacidadePadrao;
	}


}
