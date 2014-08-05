 
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.jboss.util.NotImplementedException;

import modelo.CadPlan;
import modelo.HP;
import modelo.Modelo;
import modelo.Parametros;
import modelo.PerioPAP;
import modelo.PerioPM;
import service.CadPlanAppService;
import service.HPAppService;
import service.ModeloAppService;
import service.ParametrosAppService;
import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.DataUtil;
import util.SelectOneDataModel;

public class HPActions extends BaseActions implements Serializable {

	// Services
	private static HPAppService hpService;
	private static PerioPMAppService periodoService;
	private static PerioPAPAppService perioPAPService;

	private static ParametrosAppService parametrosService;

	// Paginas
	public final String PAGINA_LIST = "listHP";
	public final String PAGINA_EDIT = "editHP";

	public final String ALTERA_PMP = "Alteração de HP PMP";
	public final String ALTERA_PAP = "Alteração de HP PAP";
	public final String ALTERA_PMP_DEMOD = "Alteração de HP Demanda Modelo";
	public final String ALTERA_PAP_DEFAM = "Alteração de HP Demanda Familia";
	
	// Componentes de Controle
	private SelectOneDataModel<String> comboPeriodoInicial;
	private SelectOneDataModel<String> comboPeriodoFinal;
	private SelectOneDataModel<String> comboPeriodoFinalPerioPAP;
	private SelectOneDataModel<String> comboPeriodoInicialPerioPAP;

	// Variaveis de Tela
	private HP hpCorrente;
	private PerioPM periodoInicialCorrente;
	private PerioPM periodoFinalCorrente;
	private PerioPAP periodoPAPInicialCorrente;
	private PerioPAP periodoPAPFinalCorrente;

	private Parametros parametroCorrente;
	private String statusPlanejamento;
	private int opcaoEscolhida;
	private String paginaCorrente;

	final static int OPCAO_ALTERACAO_PMP = 0;
	final static int OPCAO_ALTERACAO_PAP = 1;
	final static int OPCAO_ALTERACAO_DEMODPER = 2;
	final static int OPCAO_ALTERACAO_DEFAMPER = 3;

	public HPActions() throws Exception {

		try {
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			periodoService = FabricaDeAppService
					.getAppService(PerioPMAppService.class);
			perioPAPService = FabricaDeAppService
					.getAppService(PerioPAPAppService.class);

			parametrosService = FabricaDeAppService
					.getAppService(ParametrosAppService.class);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Verifica se usuario tem status para executar operação (é administrador ou
	 * gestor) e chama o metodo do service
	 * 
	 * @return
	 */
	public String iniciaPlanejamento() {

		if (sessaoUsuarioCorrente.isAdministrador()
				|| sessaoUsuarioCorrente.isGestor()) {

			hpCorrente = getHpCorrente();

			try {
				hpService.iniciaPlanejamento();
			} catch (AplicacaoException e) {
				error(e.getMessage());
				return PAGINA_LIST;
			}

			info("hp.SUCESSO_INICIO_PLANEJAMENTO");

			statusPlanejamento = null;
			parametroCorrente = null;

			return PAGINA_LIST;

		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
			return PAGINA_LIST;
		}
	}

	public String preparaAlteracaoPerioPM() {

		if (sessaoUsuarioCorrente.isAdministrador()
				|| sessaoUsuarioCorrente.isGestor()) {

			if (periodoService.recuperaListaDePerioPMs().isEmpty()) {
				error("perioPM.PERIODOS_INEXISTENTES");
				return PAGINA_LIST;
			}

			return PAGINA_EDIT;

		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
			return PAGINA_LIST;
		}
	}

	public String preparaAlteracaoPerioPAP() {

		if (sessaoUsuarioCorrente.isAdministrador()
				|| sessaoUsuarioCorrente.isGestor()) {

			if (perioPAPService.recuperaListaDePerioPAPs().isEmpty()) {
				error("perioPAP.PERIODOS_INEXISTENTES");
				return PAGINA_LIST;
			}

			return PAGINA_EDIT;

		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
			return PAGINA_LIST;
		}
	}

	public String salvar() {

		if ((paginaCorrente == ALTERA_PMP || paginaCorrente == ALTERA_PMP_DEMOD) && 
				!validacaoSelecaoPeriodos()) {
			error("perioPM.DATA_FINAL_ANTERIOR");
			return PAGINA_EDIT;
		}
		if ((paginaCorrente == ALTERA_PAP || paginaCorrente == ALTERA_PAP_DEFAM) && 
				!validacaoSelecaoPeriodosPAP()) {
			error("perioPAP.DATA_FINAL_ANTERIOR");
			return PAGINA_EDIT;
		}

		if (opcaoEscolhida == OPCAO_ALTERACAO_PMP
				|| opcaoEscolhida == OPCAO_ALTERACAO_DEMODPER) {

			hpCorrente.setPerioPMInicPMP(periodoInicialCorrente);
			hpCorrente.setPerioPMFinalPMP(periodoFinalCorrente);

			hpCorrente.setPerioPMInicDemMod(periodoInicialCorrente);
			hpCorrente.setPerioPMFinalDemMod(periodoFinalCorrente);

		} else {

			hpCorrente.setPerioPAPInicPAP(periodoPAPInicialCorrente);
			hpCorrente.setPerioPAPFinalPAP(periodoPAPFinalCorrente);

			hpCorrente.setPerioPAPInicDemFam(periodoPAPInicialCorrente);
			hpCorrente.setPerioPAPFinalDemFam(periodoPAPFinalCorrente);
		}

		try {

			if (hpCorrente.getId() == null) {
				hpService.inclui(hpCorrente);
			} else {
				hpService.altera(hpCorrente);
			}

		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_EDIT;
		}

		info("hp.SUCESSO_ALTERACAO");

		comboPeriodoInicial = null;
		comboPeriodoFinal = null;
		comboPeriodoInicialPerioPAP = null;
		comboPeriodoFinalPerioPAP = null;
		hpCorrente = null;

		return PAGINA_LIST;
	}

	public String cancelar() {
		return PAGINA_LIST;
	}

	public void geraRelatorioPdf() {
		throw new NotImplementedException();
	}

	public String alteraIntervalos() {

		List<PerioPM> periodos = periodoService.recuperaListaDePerioPMs();

		if (periodos.isEmpty()) {
			error("periodos.PERIODOS_INEXISTENTES");
		} else {
			if (parametroCorrente.getNumIntervalosFixos() > periodos.size()) {
				error("parametros.INTERVALO_FIXO_INVALIDO");
				return PAGINA_LIST;
			}
			parametrosService.altera(parametroCorrente);
			info("parametros.SUCESSO_ALTERACAO_INTERVALO");
		}

		return PAGINA_LIST;
	}

	/**
	 * Constroi e retorna uma lista com periodo+dataInicial+dataFinal para
	 * mostrar no combobox da alteração de HP
	 * 
	 * @param periodos
	 * @return
	 */
	public List<String> converterExibicaoPerioPM(List<PerioPM> periodos) {

		List<String> datasConvertidas = new ArrayList<String>();

		String diaInicial;
		String mesInicial;
		String anoInicial;

		String diaFinal;
		String mesFinal;
		String anoFinal;

		for (PerioPM perioPM : periodos) {

			String periodoPM = Integer.toString(perioPM.getPeriodoPM());

			diaInicial = Integer.toString(perioPM.getDataInicial().get(
					Calendar.DAY_OF_MONTH));
			mesInicial = Integer.toString(perioPM.getDataInicial().get(
					Calendar.MONTH) + 1);
			anoInicial = Integer.toString(perioPM.getDataInicial().get(
					Calendar.YEAR));

			diaFinal = Integer.toString(perioPM.getDataFinal().get(
					Calendar.DAY_OF_MONTH));
			mesFinal = Integer.toString(perioPM.getDataFinal().get(
					Calendar.MONTH) + 1);
			anoFinal = Integer.toString(perioPM.getDataFinal().get(
					Calendar.YEAR));

			if (diaInicial.length() < 2) {
				diaInicial = "0" + diaInicial;
			}

			if (diaFinal.length() < 2) {
				diaFinal = "0" + diaFinal;
			}

			if (mesInicial.length() < 2) {
				mesInicial = "0" + mesInicial;
			}

			if (mesFinal.length() < 2) {
				mesFinal = "0" + mesFinal;
			}

			String dataString = periodoPM + " - " + diaInicial + "/"
					+ mesInicial + "/" + anoInicial + " a " + diaFinal + "/"
					+ mesFinal + "/" + anoFinal;

			datasConvertidas.add(dataString);
		}

		return datasConvertidas;
	}

	/**
	 * Constroi e retorna uma lista com periodo+dataInicial+dataFinal para
	 * mostrar no combobox da alteração de HP para PERIOPAP
	 * 
	 * @param periodos
	 * @return
	 */
	public List<String> converterExibicaoPerioPAP(List<PerioPAP> periodos) {

		List<String> datasConvertidas = new ArrayList<String>();

		String diaInicial;
		String mesInicial;
		String anoInicial;

		String diaFinal;
		String mesFinal;
		String anoFinal;

		for (PerioPAP perioPAP : periodos) {

			try {
				perioPAP = perioPAPService.recuperaPerioPAPPorPeriodoPAP(perioPAP.getPeriodoPAP());
			} catch (AplicacaoException e) {
			}
			
			String periodoPAP = Integer.toString(perioPAP.getPeriodoPAP());

			diaInicial = Integer.toString(perioPAP.getDataInicial().get(
					Calendar.DAY_OF_MONTH));
			mesInicial = Integer.toString(perioPAP.getDataInicial().get(
					Calendar.MONTH) + 1);
			anoInicial = Integer.toString(perioPAP.getDataInicial().get(
					Calendar.YEAR));

			diaFinal = Integer.toString(perioPAP.getDataFinal().get(
					Calendar.DAY_OF_MONTH));
			mesFinal = Integer.toString(perioPAP.getDataFinal().get(
					Calendar.MONTH) + 1);
			anoFinal = Integer.toString(perioPAP.getDataFinal().get(
					Calendar.YEAR));

			if (diaInicial.length() < 2) {
				diaInicial = "0" + diaInicial;
			}

			if (diaFinal.length() < 2) {
				diaFinal = "0" + diaFinal;
			}

			if (mesInicial.length() < 2) {
				mesInicial = "0" + mesInicial;
			}

			if (mesFinal.length() < 2) {
				mesFinal = "0" + mesFinal;
			}

			String dataString = periodoPAP + " - " + diaInicial + "/"
					+ mesInicial + "/" + anoInicial + " a " + diaFinal + "/"
					+ mesFinal + "/" + anoFinal;

			datasConvertidas.add(dataString);
		}

		return datasConvertidas;
	}

	public int periodoPMEPAPDePerioPMStr(String perioPMStr){
		String []strsDivididasEspaco = perioPMStr.split(" ");
		String periodo = strsDivididasEspaco[0];
		
		return Integer.parseInt(periodo);
	}
	public boolean validacaoSelecaoPeriodos() {

		String selecao2 = comboPeriodoFinal.getObjetoSelecionado();

		try {
			periodoInicialCorrente = periodoService
					.recuperaPerioPMPorPeriodoPM(1); 
			periodoFinalCorrente = periodoService
					.recuperaPerioPMPorPeriodoPM(periodoPMEPAPDePerioPMStr(selecao2));
			
		} catch (AplicacaoException e) {
		}

		if (periodoInicialCorrente.getPeriodoPM() >= periodoFinalCorrente
				.getPeriodoPM()) {
			return false;
		}

		return true;
	}

	public boolean validacaoSelecaoPeriodosPAP() {

		String selecao2 = comboPeriodoFinal.getObjetoSelecionado();

		try {
			periodoPAPInicialCorrente = perioPAPService
					.recuperaPerioPAPPorPeriodoPAP(1); 
			periodoPAPFinalCorrente = perioPAPService
					.recuperaPerioPAPPorPeriodoPAP(periodoPMEPAPDePerioPMStr(selecao2));
		} catch (AplicacaoException e) {
		}

		if (periodoPAPInicialCorrente.getPeriodoPAP() >= periodoPAPFinalCorrente.getPeriodoPAP()) {
			return false;
		}

		return true;
	}

	// ================================== Métodos get() e set()
	// ================================== //

	public void setHpCorrente(HP hpCorrente) {
		this.hpCorrente = hpCorrente;
	}

	public HP getHpCorrente() {

		List<HP> lista = hpService.recuperaListaDeHP();

		if (lista.isEmpty()) {

			return hpCorrente = new HP();
		}
		hpCorrente = lista.get(0);
		//Recupera informacao dos campos transientes e
		//seta a informacao nos perioPAPs inicial e final.
		if (hpCorrente.getPerioPAPInicPAP() != null) {
			
			try {
				periodoPAPInicialCorrente = perioPAPService
						.recuperaPerioPAPPorPeriodoPAP(hpCorrente.getPerioPAPInicPAP()
								.getPeriodoPAP());
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}
			
			hpCorrente.setPerioPAPInicPAP(periodoPAPInicialCorrente);
			hpCorrente.setPerioPAPInicDemFam(periodoPAPInicialCorrente);
			
		}
		if (hpCorrente.getPerioPAPFinalPAP() != null) {
			try {
				periodoPAPFinalCorrente = perioPAPService
						.recuperaPerioPAPPorPeriodoPAP(hpCorrente.getPerioPAPFinalPAP()
								.getPeriodoPAP());
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}

			hpCorrente.setPerioPAPFinalPAP(periodoPAPFinalCorrente);
			hpCorrente.setPerioPAPFinalDemFam(periodoPAPFinalCorrente);
			
		}

		return hpCorrente;
	}

	public PerioPM getPeriodoInicialCorrente() {

		if (periodoInicialCorrente == null) {
			periodoInicialCorrente = new PerioPM();
		}

		return periodoInicialCorrente;
	}

	public void setPeriodoInicialCorrente(PerioPM periodoInicialCorrente) {
		this.periodoInicialCorrente = periodoInicialCorrente;
	}

	public PerioPM getPeriodoFinalCorrente() {

		if (periodoFinalCorrente == null) {
			periodoFinalCorrente = new PerioPM();
		}

		return periodoFinalCorrente;
	}

	public void setPeriodoFinalCorrente(PerioPM periodoFinalCorrente) {
		this.periodoFinalCorrente = periodoFinalCorrente;
	}

	/**
	 * @return the periodoPAPInicialCorrente
	 */
	public PerioPAP getPeriodoPAPInicialCorrente() {
		return periodoPAPInicialCorrente;
	}

	/**
	 * @param periodoPAPInicialCorrente
	 *            the periodoPAPInicialCorrente to set
	 */
	public void setPeriodoPAPInicialCorrente(PerioPAP periodoPAPInicialCorrente) {
		this.periodoPAPInicialCorrente = periodoPAPInicialCorrente;
	}

	/**
	 * @return the periodoPAPFinalCorrente
	 */
	public PerioPAP getPeriodoPAPFinalCorrente() {
		return periodoPAPFinalCorrente;
	}

	/**
	 * @param periodoPAPFinalCorrente
	 *            the periodoPAPFinalCorrente to set
	 */
	public void setPeriodoPAPFinalCorrente(PerioPAP periodoPAPFinalCorrente) {
		this.periodoPAPFinalCorrente = periodoPAPFinalCorrente;
	}

	public void setParametroCorrente(Parametros parametroCorrente) {
		this.parametroCorrente = parametroCorrente;
	}

	public Parametros getParametroCorrente() {

		if (parametroCorrente == null) {
			parametroCorrente = new Parametros();
			parametroCorrente = parametrosService.recuperaListaDeParametros()
					.get(0);
		}

		return parametroCorrente;
	}

	public void setOpcaoEscolhida(int opcaEscolhida) {
		this.opcaoEscolhida = opcaEscolhida;
	}

	public int getOpcaoEscolhida() {
		return opcaoEscolhida;
	}

	public SelectOneDataModel<String> getComboPeriodoInicial() {

		if (comboPeriodoInicial == null) {
			if(paginaCorrente == ALTERA_PAP || paginaCorrente == ALTERA_PAP_DEFAM){
				comboPeriodoInicial = SelectOneDataModel
				.criaSemTextoInicial(this
						.converterExibicaoPerioPAP(perioPAPService
								.recuperaListaDePerioPAPs()));
				
			}
			else{
				comboPeriodoInicial = SelectOneDataModel.criaSemTextoInicial(this
						.converterExibicaoPerioPM(periodoService
								.recuperaListaDePerioPMs()));				
			}
		}

		return comboPeriodoInicial;
	}

	public SelectOneDataModel<String> getComboPeriodoFinal() {

		if (comboPeriodoFinal == null) {
			if (paginaCorrente == ALTERA_PAP || paginaCorrente == ALTERA_PAP_DEFAM) {

				comboPeriodoFinal = SelectOneDataModel.criaSemTextoInicial(this
						.converterExibicaoPerioPAP(perioPAPService
								.recuperaListaDePerioPAPs()));
			} else {
				comboPeriodoFinal = SelectOneDataModel.criaSemTextoInicial(this
						.converterExibicaoPerioPM(periodoService
								.recuperaListaDePerioPMs()));
			}
		}

		return comboPeriodoFinal;
	}

	public SelectOneDataModel<String> getComboPeriodoInicialPerioPAP() {

		if (comboPeriodoInicialPerioPAP == null) {
			comboPeriodoInicialPerioPAP = SelectOneDataModel
					.criaSemTextoInicial(this
							.converterExibicaoPerioPAP(perioPAPService
									.recuperaListaDePerioPAPs()));
		}

		return comboPeriodoInicialPerioPAP;
	}

	public SelectOneDataModel<String> getComboPeriodoFinalPerioPAP() {

		if (comboPeriodoFinalPerioPAP == null) {
			comboPeriodoFinalPerioPAP = SelectOneDataModel
					.criaSemTextoInicial(this
							.converterExibicaoPerioPAP(perioPAPService
									.recuperaListaDePerioPAPs()));
		}

		return comboPeriodoFinalPerioPAP;
	}

	public void setComboPeriodoInicial(
			SelectOneDataModel<String> comboPeriodoInicial) {
		this.comboPeriodoInicial = comboPeriodoInicial;
	}

	public void setComboPeriodoFinal(
			SelectOneDataModel<String> comboPeriodoFinal) {
		this.comboPeriodoFinal = comboPeriodoFinal;
	}

	public void setPaginaCorrente(String paginaCorrente) {
		this.paginaCorrente = paginaCorrente;
	}

	public String getPaginaCorrente() {

		switch (opcaoEscolhida) {

		case 0:
			paginaCorrente = "Alteração de HP PMP";
			break;

		case 1:
			paginaCorrente = "Alteração de HP PAP";
			break;

		case 2:
			paginaCorrente = "Alteração de HP Demanda Modelo";
			break;

		case 3:
			paginaCorrente = "Alteração de HP Demanda Familia";
			break;

		default:
			break;
		}

		return paginaCorrente;
	}

	public void setStatusPlanejamento(String statusPlanejamento) {
		this.statusPlanejamento = statusPlanejamento;
	}

	public String getStatusPlanejamento() {

		if (statusPlanejamento == null) {
			statusPlanejamento = (parametroCorrente.isInicPlanejamento() ? "Planejamento inicializado"
					: "Planejamento não inicializado");
		}

		return statusPlanejamento;
	}

	/**
	 * @return the aLTERA_PAP
	 */
	public String getALTERA_PAP() {
		return ALTERA_PAP;
	}
}
