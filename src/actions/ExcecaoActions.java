 
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


import modelo.Excecao;

import service.ExcecaoAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

public class ExcecaoActions extends BaseActions {
	
	// Services
	private static ExcecaoAppService excecaoService;
	

	// Paginas
	public final String PAGINA_LIST = "listExcecao";
	
	// Variaveis de Tela
	private Excecao excecaoCorrente;
	private DataModel listaDeExcecoes;
	

	public ExcecaoActions() throws Exception {
		try {
			excecaoService = FabricaDeAppService.getAppService(ExcecaoAppService.class);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * Altera o status da excecao corrente selecionada.
	 * Se for True(Ativa) vira False(Inativa), se for False(Inativa) vira True(Ativa)
	 * retorna para tela list
	 * @return
	 */
	public String mudarStatusExcecao() {
		excecaoCorrente = (Excecao) listaDeExcecoes.getRowData();
		
		if (excecaoCorrente.getStatusExcecao()){
			excecaoCorrente.setStatusExcecao(false);
		} else {
			excecaoCorrente.setStatusExcecao(true);
		}

		excecaoService.altera(excecaoCorrente);		
		info("excecao.SUCESSO_ALTERACAO_STATUS");		
		return PAGINA_LIST;
	}

	/**
	 * Altera o status de todas as excecoes
	 * Se for True(Ativa) vira False(Inativa), se for False(Inativa) vira True(Ativa)
	 * retorna para tela list
	 * @return
	 */
	public String mudarStatusGeral() {
		
		List<Excecao> excecoes = ((List<Excecao>) listaDeExcecoes.getWrappedData());
		
		for (Excecao excecao : excecoes) {

			if (excecao.getStatusExcecao()){
				excecao.setStatusExcecao(false);
			} else {
				excecao.setStatusExcecao(true);
			}

			excecaoService.altera(excecao);	
		}

		info("excecao.SUCESSO_ALTERACAO_STATUS");
		
		return PAGINA_LIST;
	}
	
	// ================================== Métodos get() e set() ================================== //


	
	public DataModel getListaDeExcecoes() {
		if (listaDeExcecoes == null){
			
			listaDeExcecoes = new ListDataModel(excecaoService.recuperaListaPaginadaDeExcecoes());
		}
		return listaDeExcecoes;
	}


	public void setListaDeExcecoes(DataModel listaDeExcecoes) {
		this.listaDeExcecoes = listaDeExcecoes;
	}




	public Excecao getExcecaoCorrente() {
		return excecaoCorrente;
	}




	public void setExcecaoCorrente(Excecao excecaoCorrente) {
		this.excecaoCorrente = excecaoCorrente;
	}





}
