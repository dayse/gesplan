 
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
import modelo.PerioPMVig;
import modelo.PMP;

import service.PerioPMVigAppService;
import service.PMPAppService;
import service.ModeloAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class PMPActions extends BaseActions {
	
	// Services
	private static PMPAppService pmpService;
	private static ModeloAppService modeloService;
	private static PerioPMVigAppService perioPMVigService;
	

	// Paginas
	public final String PAGINA_LIST = "listPMP";
	public final String PAGINA_EDIT = "editPMP";
	
	// Variaveis de Tela
	private PMP pmpCorrente;
	private Modelo modeloCorrente;
	private DataModel listaDeModelos;
	
	
	private int pagina;
	
	
	public PMPActions() throws Exception {
		try {
			perioPMVigService = FabricaDeAppService.getAppService(PerioPMVigAppService.class);
			pmpService = FabricaDeAppService.getAppService(PMPAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
		} catch (Exception e) {
			throw e;
		}

		pagina = 1;
	}
	
	/**
	 * obtem do data Model (tabelaRecModels) o objeto recModelCorrente
	 * @return
	 */
	public String preparaAlteracao(){			
		
		return PAGINA_EDIT;
	}
	
	
	public String altera() {
			
		pmpService.altera(pmpCorrente);
		
		info("PMP.SUCESSO_ALTERACAO");		
		
		listaDeModelos = null;
		
		return PAGINA_LIST;
	}
	
	

	
	public String cancela() {
		
		listaDeModelos = null;
		
		return PAGINA_LIST;
	}
	
	
	
	// ================================== Métodos get() e set() ================================== //

	
	public PMP getPMPCorrente() {
		return pmpCorrente;
	}

	public void setPMPCorrente(PMP pmpCorrente) {
		this.pmpCorrente = pmpCorrente;
	}


	public void setModeloCorrente(Modelo modeloCorrente) {
		this.modeloCorrente = modeloCorrente;
	}

	public Modelo getModeloCorrente() {
		return modeloCorrente;
	}

	public DataModel getListaDeModelos() {
		
		if (listaDeModelos == null){
			
			listaDeModelos = new ListDataModel(modeloService.recuperaListaPaginadaDeModelosComFamiliaComListaDePMPs());

		}

		return listaDeModelos;
	}

	public void setListaDeModelos(DataModel listaDeModelos) {
		this.listaDeModelos = listaDeModelos;
	}




	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}


}
