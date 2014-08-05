 
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.PerioPAP;
import modelo.PerioPM;
import modelo.TipoUsuario;

import org.jboss.util.NotImplementedException;

import actions.controle.SessaoDoUsuario;


import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.DataUtil;

public class PerioPMActions extends BaseActions implements Serializable {

	// Paginas
	public final String PAGINA_LIST = "listPerioPM";
	public final String PAGINA_NEW  = "newPerioPM";
	public final String PAGINA_SHOW = "showPerioPM";
	public final String PAGINA_EDIT = "editPerioPM";
	
	// Services
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	
	// Variaveis de Tela
	private PerioPM perioPMCorrente;
	private Date dataInicial;
	private Date dataFinal;
	private int periodoPMAtual;
	private boolean dataInicialHabilitada = false;
	private int opcaoExclusao;
	
	
	
	// Componentes de Controle
	private DataModel listaPerioPM;
	
	
	public PerioPMActions(){
		
		try {
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
		} catch (Exception e) {
		}
	}
	
	public String preparaInclusao() {
		
		if (sessaoUsuarioCorrente.isAdministrador() || sessaoUsuarioCorrente.isGestor()){
			
			perioPMCorrente = new PerioPM();
			
			List<PerioPM> periodos = perioPMService.recuperaListaDePerioPMs();
			
			if (!periodos.isEmpty()){
				
				PerioPM ultimoPeriodo = periodos.get(periodos.size() - 1);
				
				Calendar ultimaData = ultimoPeriodo.getDataFinal();
				ultimaData.add(Calendar.DAY_OF_MONTH, 1);
				dataInicial = ultimaData.getTime();
				
				periodoPMAtual = ultimoPeriodo.getPeriodoPM() + 1;
				
				dataInicialHabilitada = true;
				
			} else {
				periodoPMAtual = 1;
				dataInicial = null;
			}
			
			dataFinal = null;
			
			return PAGINA_NEW;

		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
			return PAGINA_LIST;
		}
	}

	/**
	 *  inclui PerioPAP e imediatamente Inclui PerioPM equivalente usando o  PeriodoPM = a PeriodoPAP
	 *	é preciso incluir primeiro o PerioPAP apesar da inclusao ser feita no PerioPM pois
	 *	o PerioPAP é o lado um da relacao 1 para muitos.
	 *	
	 *	Verifica antes se datas Iniciais e finais do perioPM 
	 * @return
	 */
	public String inclui() {	
		
		if (!DataUtil.validacaoDatasInicialEFinal(dataInicial, dataFinal)){
			error("perioPM.DATA_FINAL_ANTERIOR");
			return PAGINA_NEW;
		} 
		
		perioPMCorrente.setDataInicial(DataUtil.dateToCalendar(dataInicial));
		perioPMCorrente.setDataFinal(DataUtil.dateToCalendar(dataFinal));
		
		perioPMCorrente.setPeriodoPM(periodoPMAtual);
		
		try {
			//Incluindo usando o service do PAP, que por sua vez ira incluir o perioPAP.
			//Depois ele ira chamar o service do perioPM que ira incluir o perio PM
			perioPAPService.incluiComPerioPM(perioPMCorrente);
			
		} catch (AplicacaoException e) {
			
			error(e.getMessage());
			return PAGINA_NEW;
		}
		
		info("perioPM.SUCESSO_INCLUSAO");
		listaPerioPM = null;
		
		return PAGINA_LIST;
	}


	/**
	 * Recebe a lista do bean e insere na secao o PerioPM selecionado
	 *  na pagina "list"
	 *  Retorna "editPerioPM" e vai para a pagina "edit" onde é chamado o metodo altera()
	 *  para confirmar a edicao.
	 *  @return
	 */
	public String preparaAlteracao() {
		
		
		if (sessaoUsuarioCorrente.isAdministrador() || sessaoUsuarioCorrente.isGestor()){
			perioPMCorrente = (PerioPM) listaPerioPM.getRowData();
			dataInicial = perioPMCorrente.getDataInicial().getTime();
			dataFinal = perioPMCorrente.getDataFinal().getTime();
			dataInicial = perioPMCorrente.getDataInicial().getTime();
	    	dataFinal = perioPMCorrente.getDataFinal().getTime();

			return PAGINA_EDIT;
			
		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
			return PAGINA_LIST;
		}
	}
	
	public String altera() {
		
		perioPMCorrente.setDataInicial(DataUtil.dateToCalendar(dataInicial));
		perioPMCorrente.setDataFinal(DataUtil.dateToCalendar(dataFinal));
		
		if (DataUtil.validacaoDatasInicialEFinal(dataInicial, dataFinal)){
			perioPMService.altera(perioPMCorrente);
			
			info("perioPM.SUCESSO_ALTERACAO");
			listaPerioPM = null;
			
			return PAGINA_LIST;
		
		} else {
			error("perioPM.DATA_FINAL_ANTERIOR");
			return PAGINA_EDIT;
		}
	}
	
	public String exclui() {
		
		if (sessaoUsuarioCorrente.isAdministrador() || sessaoUsuarioCorrente.isGestor()){
			
			if (perioPMService.recuperaListaDePerioPMs().isEmpty()){
				error("perioPM.PERIODOS_INEXISTENTES");
				return PAGINA_LIST;
			}
			
			try {
				perioPMService.excluirPrimeiroUltimoOuTodos(opcaoExclusao);
			} catch (AplicacaoException e) {
				error(e.getMessage());
				return PAGINA_LIST;
			}
			
			info("perioPM.SUCESSO_EXCLUSAO");
			listaPerioPM = null;
			
		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
		}
		
		return PAGINA_LIST;
	}

	public String cancelar() {
		
		listaPerioPM = null;
		
		return PAGINA_LIST;
	}

	public String mostra() {
		
		perioPMCorrente = (PerioPM) listaPerioPM.getRowData();
		
		return PAGINA_SHOW;
	}
	
	public String geraRelatorioPdf() {
		throw new NotImplementedException("metodo imprimir");
	}
	
	// ================================== Métodos get() e set() ================================== //
	
	public Date getDataInicial() {
		return dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public PerioPM getPerioPMCorrente() {
		return perioPMCorrente;
	}

	public DataModel getListaPerioPM() {
		
		if (listaPerioPM == null){
			listaPerioPM = new ListDataModel(perioPMService.recuperaListaPaginadaDePerioPMs());
		}
		
		return listaPerioPM;
	}

	public void setPerioPMCorrente(PerioPM perioPMCorrente) {
		this.perioPMCorrente = perioPMCorrente;
	}

	public void setListaPerioPM(DataModel listaPerioPM) {
		this.listaPerioPM = listaPerioPM;
	}

	public void setDataInicialHabilitada(boolean dataInicialHabilitada) {
		this.dataInicialHabilitada = dataInicialHabilitada;
	}

	public boolean isDataInicialHabilitada() {
		return dataInicialHabilitada;
	}

	public void setPeriodoPMAtual(int periodoPMAtual) {
		this.periodoPMAtual = periodoPMAtual;
	}

	public int getPeriodoPMAtual() {
		return periodoPMAtual;
	}

	public void setOpcaoExclusao(int opcaoExclusao) {
		this.opcaoExclusao = opcaoExclusao;
	}

	public int getOpcaoExclusao() {
		return opcaoExclusao;
	}
}
