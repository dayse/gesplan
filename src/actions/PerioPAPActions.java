 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.PerioPAP;
import modelo.PerioPM;



import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.DataUtil;
import util.SelectOneDataModel;

public class PerioPAPActions extends BaseActions implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	// Paginas
	public final String PAGINA_LIST = "listPerioPAP";
	public final String PAGINA_SHOW = "showPerioPAP";
	public final String PAGINA_EDIT = "editPerioPAP";
	public final String PAGINA_EDITAGREGADOR = "editAgregador";
	
	// Services
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	
	// Variaveis de Tela
	private int agregadorDePerioPMs;
	private int numPeriodosPM;
	private PerioPAP perioPAPCorrente;
	
	// Componentes de Controle
	private SelectOneDataModel<PerioPM> comboPerioPMsInicial;
	private SelectOneDataModel<PerioPM> comboPerioPMsFinal;
	private DataModel listaPerioPAP;
	
	
	public PerioPAPActions(){
		
		try {
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			
		} catch (Exception e) {
		}
		agregadorDePerioPMs = 1;
	}

	/**
	 * Usado na edição.
	 * É necessario atribuir a listaPerioPAP, comboPerioPMsInicial e comboPerioPMsFinal  null, para forçar
	 * o novo get dos mesmos.
	 * @return
	 */
	public String cancela() { 
		
		listaPerioPAP = null;
		comboPerioPMsInicial = null;
		comboPerioPMsFinal = null;
		
		return PAGINA_LIST;
	}
	
	/**
	 * Método usado na exibição de um periopap
	 * 
	 * @return String
	 */
	
	public String mostra() {
		
		perioPAPCorrente = (PerioPAP) listaPerioPAP.getRowData();
		
		return PAGINA_SHOW;
	}
	

	/**
	 * Recebe a lista do bean e insere na requisição o PerioPAP selecionado
	 *  na pagina "list"
	 *  Obtem os campos calculados periodoPM inicial e final desse perioPAP, dataInicial, dataFinal, numDiasUteis.
	 *  Todos a partir do perioPM.
	 *  Retorna "PAGINA_EDIT" e vai para a pagina "edit" onde é chamado o metodo altera()
	 *  para confirmar a edicao.
	 *  @return
	 */
	public String preparaAlteracao() {
		
		if (sessaoUsuarioCorrente.isAdministrador() || sessaoUsuarioCorrente.isGestor()){
			
			perioPAPCorrente = (PerioPAP) listaPerioPAP.getRowData();
			//Obtem PeriodoPM inicial e final desse PerioPAP
			PerioPM perioPMInicial = perioPAPService.obtemPrimeiroPerioPMdoPerioPAP(perioPAPCorrente);
			
			//Atribui valores atuais para os campos calculados
			perioPAPCorrente.setPeriodoPMInic(perioPMInicial.getPeriodoPM());
			perioPAPCorrente.setDataInicial(perioPMInicial.getDataInicial());
			
			PerioPM perioPMFinal = perioPAPService.obtemUltimoPerioPMdoPerioPAP(perioPAPCorrente);		
			perioPAPCorrente.setPeriodoPMFinal(perioPMFinal.getPeriodoPM());
			perioPAPCorrente.setDataFinal(perioPMFinal.getDataFinal());
			
			perioPAPCorrente.setNumDiasUteis(
					perioPMService.calculaTotalDiasUteisIntervalo(perioPMInicial, perioPMFinal));		
		} else {
			error("erro.FALTA_DE_PRIVILEGIO");
			return PAGINA_LIST;
		}
		
		return PAGINA_EDIT;
	}
	
	/**
	 * Recupera os perioPMs inicial e final, editados nas comboBoxs.
	 * Atribui valores para os campos calculados: dataInicial, dataFinal.
	 * Faz criticas para nao permitir dataFinal anterior a dataInicial, ou periodoPMfinal anterior ao periodoPMInicial
	 * E altera  o perioPAPCorrente.
	 * 
	 * @return
	 */
	public String altera() {
		//É preciso fazer essas atribuicoes apenas para essas 2 variaveis, pois elas sao as unicas alteradas na edicao, atraves das comboBoxs.
		//Os demais campos calculados ja sao atualizados pelos gets e sets na hora do request.				
		PerioPM perioPMInicial = comboPerioPMsInicial.getObjetoSelecionado();
		perioPAPCorrente.setPeriodoPMInic(perioPMInicial.getPeriodoPM());
		
		PerioPM perioPMFinal = comboPerioPMsFinal.getObjetoSelecionado();		
		perioPAPCorrente.setPeriodoPMFinal(perioPMFinal.getPeriodoPM());
		
		perioPAPCorrente.setDataInicial(perioPMInicial.getDataInicial());
		perioPAPCorrente.setDataFinal(perioPMFinal.getDataFinal());

		//conversao necessaria para utilizar o validador
		Date dataInicial = perioPAPCorrente.getDataInicial().getTime();  
		Date dataFinal = perioPAPCorrente.getDataFinal().getTime();
		
		//Faz validação de data verificando se datafinal é anterior a dataInicial
		if (DataUtil.validacaoDatasInicialEFinal(dataInicial, dataFinal) ){

			//Essa critica foi feita como medida de segurança.
			//pois a critica anterior impediria esta (se o perioPM estiver com as criticas corretas)
			if(perioPMInicial.getPeriodoPM() > perioPMFinal.getPeriodoPM()){
				error("perioPAP.PERIOPM_FINAL_ANTERIOR");
				return PAGINA_EDIT;
			}
			else
			{
				perioPAPService.altera(perioPAPCorrente);

				
				info("perioPAP.SUCESSO_ALTERACAO");
				listaPerioPAP = null;
				comboPerioPMsInicial = null;
				comboPerioPMsFinal = null;
				
				return PAGINA_LIST;
				
			}
		
		} else {
			error("perioPAP.DATA_FINAL_ANTERIOR");
			return PAGINA_EDIT;
		}
	}

	/**
	 * Reecalcula os perioPAPs em funcao dos perioPMs, fazendo 1x1 novamente.
	 * verifica se existem periopaps cadastrados
	 * verifica se usuario tem acesso a essa função - ou seja verifica se é 
	 * Admin ou gestor
	 * 
	 * @return
	 */
	public String reinicializaPerioPAPs(){
		List<PerioPAP>  ListaDePeriodosPAP = perioPAPService.recuperaListaDePerioPAPs();
		//verifica  se  tem perioPAPs cadastradosao 
		//nao precisa verificar se existe perioPM pois se existir periopap é pq existe periopm
		if (!ListaDePeriodosPAP.isEmpty()) {
			if (sessaoUsuarioCorrente.isAdministrador() || sessaoUsuarioCorrente.isGestor()){
				perioPAPService.reinicializaPerioPAPs();
				listaPerioPAP = null;
				return PAGINA_LIST;
			} else {
				error("erro.FALTA_DE_PRIVILEGIO");
				return PAGINA_LIST;
			}
		}else {
			error("perioPAP.PERIODOS_INEXISTENTES");
			return PAGINA_LIST;

		}
	}

	/**
	 * Verifica se tem periodos cadastrados
	 * Verifica se o usuario tem acesso a essa operação
	 * Direciona para a pagina de edicao do agregador
	 * 
	 * @return
	 */
	public String preparaAgrupaPerioPMs(){
		List<PerioPAP>  ListaDePeriodosPAP = perioPAPService.recuperaListaDePerioPAPs();
		//verifica se tem perioPAPs cadastrados 
		//nao precisa verificar se existe perioPM pois se existir periopap é pq existe periopm
		if (!ListaDePeriodosPAP.isEmpty()) {
			if (sessaoUsuarioCorrente.isAdministrador() || sessaoUsuarioCorrente.isGestor()){
				return PAGINA_EDITAGREGADOR;
			} else {
				error("erro.FALTA_DE_PRIVILEGIO");
				return PAGINA_LIST;
			} 			
		}else {
			error("perioPAP.PERIODOS_INEXISTENTES");
			return PAGINA_LIST;
		}
	}
	
	/**
	 * Torna editavel o numero de periodos PM que o usuario deseja agregar
	 * verifica se o nume é divisivel pelo total de periodos PM cadastrados
	 * e agrega os Periodos  PM usando o agregador editado
	 *
	 * @return
	 */
	public String agrupaPerioPMs(){
		
			int numPerioPMs = perioPMService.recuperaListaDePerioPMs().size();
			if(numPerioPMs % agregadorDePerioPMs != 0){
				error("perioPAP.AGREGADOR_INCORRETO");
				return PAGINA_EDITAGREGADOR;
			}
			perioPAPService.agrupaPerioPAPs(agregadorDePerioPMs);
			listaPerioPAP = null;
			return PAGINA_LIST;
	}

	/**
	 * Metodo que é executado no momento em que a comboBox de periodoPMFinal do perioPAP é alterado.
	 * Deve ser usado em paralelo com o valueChangeListener e o rerender dos campos calculados na view.
	 * 
	 * Recalcula a dataFinal e o NumDiasUteis que sao mostrados na tela.
	 * Essas alteracoes ainda nao sao feitas em arquivo, por isso nao adianta usar o get de 
	 * variaveis como perioPAPCorrente.
	 * 
	 * @param evento
	 */
	public void calculaDataFinalENumDiasUteis (ValueChangeEvent evento){
		
		//Recupera a label que esta selecionada na combobox e a poem na string novoPeriodoPM
		String novoPeriodoPM = (String)evento.getNewValue();
		
		//Substitui o trecho da string que contem "Periodo - " por vazio
		//deixando assim na string apenas o numero do periodoPM
		novoPeriodoPM = novoPeriodoPM.replace("Periodo - ", "");

		PerioPM novoPerioPM = null;
		try{
			novoPerioPM = perioPMService.recuperaPerioPMPorPeriodoPM(Integer.valueOf(novoPeriodoPM));
		}		
		catch(AplicacaoException e){			
		}
		
		//recupera um novo intervalo de perioPMs usando como periodo final o novo valor da combo box
		List<PerioPM> novoIntervaloDePerioPMs = perioPMService.recuperaIntervaloDePerioPMs(perioPAPCorrente.getPeriodoPMInic(), novoPerioPM.getPeriodoPM());
		
		//calcula o total de dias uteis apartir do novo intervalo de perioPMs
		Double numDiasUteis = perioPAPService.calculaTotalDiasUteis(novoIntervaloDePerioPMs);	
		
		perioPAPCorrente.setDataFinal(novoPerioPM.getDataFinal());
		perioPAPCorrente.setNumDiasUteis(numDiasUteis);

	}
	// ================================== Métodos get() e set() ================================== //
	


	public int getNumPeriodosPM() {
		 
		return perioPMService.recuperaListaDePerioPMs().size();
	}

	public void setNumPeriodosPM(int numPeriodosPM) {
		this.numPeriodosPM = numPeriodosPM;
	}

	public int getAgregadorDePerioPMs() {
		return agregadorDePerioPMs;
	}

	public void setAgregadorDePerioPMs(int agregadorDePerioPMs) {
		this.agregadorDePerioPMs = agregadorDePerioPMs;
	}
	
	/**
	 * Método que cria a Combobox de PerioPMInicial que lista os perioPMs cadastrados no sistema.
	 * 
	 * @author felipe 
	 */
	public SelectOneDataModel<PerioPM> getComboPerioPMsInicial() {
		PerioPM perioPMInic = null;
		
		
		if (comboPerioPMsInicial == null){

			//Verifica se existe perioPAP corrente,
			//se existir atribui valor da comboBox para o perioPMInic do mesmo.
			if(perioPAPCorrente == null){
				comboPerioPMsInicial = SelectOneDataModel.criaSemTextoInicial(perioPMService.recuperaListaPaginadaDePerioPMs());				
			}
			else{
				perioPMInic = perioPAPService.obtemPrimeiroPerioPMdoPerioPAP(perioPAPCorrente);
				
				comboPerioPMsInicial = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(perioPMService.recuperaListaPaginadaDePerioPMs(),perioPMInic);
				
			}
		}
		
		return comboPerioPMsInicial;
	}

	public void setComboPerioPMsInicial(SelectOneDataModel<PerioPM> comboPerioPMsInicial) {
		this.comboPerioPMsInicial = comboPerioPMsInicial;
	}
	
	/**
	 * Método que cria a Combobox de PerioPMFinal que lista os perioPMs cadastrados no sistema.
	 * 
	 * @author felipe 
	 */
	public SelectOneDataModel<PerioPM> getComboPerioPMsFinal() {
		PerioPM perioPMfinal=null;
		
		if (comboPerioPMsFinal == null){
			
			//Verifica se existe perioPAP corrente,
			//se existir atribui valor da comboBox para o perioPMfinal do mesmo.
			if(perioPAPCorrente == null){
				comboPerioPMsFinal = SelectOneDataModel.criaSemTextoInicial(perioPMService.recuperaListaPaginadaDePerioPMs());
			
			}
			else{
				perioPMfinal = perioPAPService.obtemUltimoPerioPMdoPerioPAP(perioPAPCorrente);
				
				comboPerioPMsFinal = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(perioPMService.recuperaListaPaginadaDePerioPMs(),perioPMfinal);
				
			}
		}
		
		return comboPerioPMsFinal;
	}

	public void setComboPerioPMsFInal(SelectOneDataModel<PerioPM> comboPerioPMsFinal) {
		this.comboPerioPMsFinal = comboPerioPMsFinal;
	}
	
	

	public PerioPAP getPerioPAPCorrente() {
		return perioPAPCorrente;
	}

	public DataModel getListaPerioPAP() {
		
		if (listaPerioPAP == null){
			listaPerioPAP = new ListDataModel(perioPAPService.recuperaListaPaginadaDePerioPAPs());
		}
		
		return listaPerioPAP;
	}

	public void setPerioPAPCorrente(PerioPAP perioPAPCorrente) {
		this.perioPAPCorrente = perioPAPCorrente;
	}

	public void setListaPerioPAP(DataModel listaPerioPAP) {
		this.listaPerioPAP = listaPerioPAP;
	}
}
