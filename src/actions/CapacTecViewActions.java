 
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

import modelo.CapacTecView;
import modelo.PerioPM;
import modelo.Tecido;

import service.PerioPMAppService;
import service.CapacTecViewAppService;
import service.TecidoAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class CapacTecViewActions extends BaseActions {
	
	// Services
	private static CapacTecViewAppService capacTecViewService;
	private static TecidoAppService tecidoService;
	private static PerioPMAppService perioPMService;
	

	// Paginas
	public final String PAGINA_LIST = "listCapacTec";
	
	// Variaveis de Tela
	private CapacTecView capacTecCorrente;
	private Tecido tecidoCorrente;
	private DataModel listaDeTecidos;
	
	
	private int pagina;
	
	
	public CapacTecViewActions() throws Exception {
		try {
			capacTecViewService = FabricaDeAppService.getAppService(CapacTecViewAppService.class);
			tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
		} catch (Exception e) {
			throw e;
		}
		//resgata a pagina do managed bean "tecidoActions" que foi mantido na requisao através do keepAlive
		
		pagina = ((TecidoActions) getManagedBean("tecidoActions")).getPagina();
	}
	
	
	
	// ================================== Métodos get() e set() ================================== //

	
	public CapacTecView getCapacTecCorrente() {
		return capacTecCorrente;
	}

	public void setCapacTecCorrente(CapacTecView capacTecCorrente) {
		this.capacTecCorrente = capacTecCorrente;
	}


	public void setTecidoCorrente(Tecido tecidoCorrente) {
		this.tecidoCorrente = tecidoCorrente;
	}

	public Tecido getTecidoCorrente() {
		return tecidoCorrente;
	}

	public DataModel getListaDeTecidos() {
		
		if (listaDeTecidos == null){
			try{
				listaDeTecidos = new ListDataModel(capacTecViewService.recuperaListaPaginadaDeTecidosComListaDeCapacTecViews());
			}
			catch(AplicacaoException e){
				error(e.getMessage());
			}

		}

		return listaDeTecidos;
	}

	public void setListaDeTecidos(DataModel listaDeTecidos) {
		this.listaDeTecidos = listaDeTecidos;
	}




	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}


}
