 
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

import DAO.exception.ObjetoNaoEncontradoException;

import comparator.PlPerModComparatorPorPerioPM;


import modelo.CadPlan;
import modelo.ExcecaoMens;
import modelo.PlPerMod;
import modelo.PlanoModelo;

import service.ExcecaoMensAppService;
import service.ModeloAppService;
import service.PlanoModeloAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;
/**
 * Implementa a view relativa a tela que mostra mensagem de excecao por planoModelo.
 * @author arruda
 *
 */
public class ExcecaoMensActions extends BaseActions {
	
	// Services
	private static ExcecaoMensAppService excecaoMensService;
	private ModeloAppService modeloService;
	private PlanoModeloAppService planoModeloService;
	

	// Paginas
	public final String PAGINA_LIST = "listExcecaoMens";
	public final String PAGINA_LIST_CADPLAN = "listCadPlan";
	
	// Variaveis de Tela
	private CadPlan cadPlanCorrente;
	private ExcecaoMens excecaoMensCorrente;
	private DataModel listaDeExcecoesMens;
	private DataModel listaPlanoModelos;
	

	public ExcecaoMensActions() throws Exception {
		try {
			excecaoMensService = FabricaDeAppService.getAppService(ExcecaoMensAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
		} catch (Exception e) {
			throw e;
		}
		cadPlanCorrente = ((CadPlanActions) getManagedBean("cadPlanActions")).getCadPlanCorrente();
	}

	public String cancelar() {
		
		listaPlanoModelos = null;
		listaDeExcecoesMens = null;
		cadPlanCorrente = null;
		
		return PAGINA_LIST_CADPLAN;
	}
	
	
	// ================================== Métodos get() e set() ================================== //


	
	public DataModel getListaDeExcecoesMens() {
		if (listaDeExcecoesMens == null){			
			listaDeExcecoesMens = new ListDataModel(excecaoMensService.recuperaListaDeExcecaoMensPorCadPlan(cadPlanCorrente));
		}
		return listaDeExcecoesMens;
	}


	public void setListaDeExcecoes(DataModel listaDeExcecoes) {
		this.listaDeExcecoesMens = listaDeExcecoes;
	}




	public ExcecaoMens getExcecaoMensCorrente() {
		return excecaoMensCorrente;
	}




	public void setExcecaoMensCorrente(ExcecaoMens excecaoMensCorrente) {
		this.excecaoMensCorrente = excecaoMensCorrente;
	}

	public CadPlan getCadPlanCorrente() {
		return cadPlanCorrente;
	}
	
	public void setCadPlanCorrente(CadPlan cadPlanCorrente) {
		this.cadPlanCorrente = cadPlanCorrente;
	}


	public void setListaPlanoModelos(DataModel listaPlanoModelos) {
		this.listaPlanoModelos = listaPlanoModelos;
	}

	public DataModel getListaPlanoModelos() {
		
		if (listaPlanoModelos == null){
			
			List<PlanoModelo> planoModelos = new ArrayList(cadPlanCorrente.getPlanosModelo());
			Collections.sort(planoModelos);	
			
			for (PlanoModelo planoModelo : planoModelos) {
				
				try {
					// Trazendo a informação da Familia associada ao Modelo
					planoModelo.setModelo(modeloService.recuperaUmModeloComFamilia(planoModelo.getModelo()));
					List<ExcecaoMens> excecaoMenss = excecaoMensService.recuperaListaDeExcecaoMensPorPlanoModelo(planoModelo);
					Collections.sort(excecaoMenss);
					planoModelo.setExcecaoMenss(excecaoMenss);
					
					
				} catch (AplicacaoException e) {
				} 
			}
			
			listaPlanoModelos = new ListDataModel(planoModelos);
		}
		
		return listaPlanoModelos;
	}




}
