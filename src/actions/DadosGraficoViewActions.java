 
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

import br.blog.arruda.plot.Plot;

import service.DadosGraficoViewAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class DadosGraficoViewActions extends BaseActions {
	
	// Services
	private static DadosGraficoViewAppService dadosGraficosViewService;
	

	// Paginas
	public final String PAGINA_LIST = "listCapacTec";
	
	// Variaveis de Tela
	private String dadosGrafico;
	
	
	
	public DadosGraficoViewActions() throws Exception {
		try {
			dadosGraficosViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	// ================================== Métodos get() e set() ================================== //

	public static DadosGraficoViewAppService getDadosGraficosViewService() {
		return dadosGraficosViewService;
	}



	public String getDadosGrafico() {
		return dadosGrafico;
	}



	public static void setDadosGraficosViewService(
			DadosGraficoViewAppService dadosGraficosViewService) {
		DadosGraficoViewActions.dadosGraficosViewService = dadosGraficosViewService;
	}



	public void setDadosGrafico(String dadosGrafico) {
		this.dadosGrafico = dadosGrafico;
	}





}
