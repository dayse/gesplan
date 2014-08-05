 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.CadPlan;
import modelo.DeFamPer;
import modelo.DeModPer;
import modelo.Parametros;
import modelo.PerioPAP;
import modelo.PerioPM;
import modelo.HP;

import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

import DAO.PerioPAPDAO;

import DAO.CadPlanDAO;
import DAO.DeFamPerDAO;
import DAO.ParametrosDAO;
import DAO.PerioPMDAO;
import DAO.DeModPerDAO;
import DAO.HPDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.DeFamPerDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * 
 * As classes de serviço é que possuem as regras de negócio. Fazem as críticas
 * quando necessário e chamam as classes DAO para pegar as informações do BD.
 * 
 * São essas classes AppService que fazem o controle de transaçao, ou seja quem
 * abre a transaçao, "quem commita" através do interceptador de appservice. Aqui
 * defino se o metodo é transacional ou não e em funçao desta informaçao o
 * interceptador vai usar ou nao transacao.
 * 
 * 
 * 
 * @author felipe
 * 
 */
public class PerioPAPAppService {

	// DAOs
	private static ParametrosDAO parametrosDAO;
	private static PerioPAPDAO perioPAPDAO;
	private static PerioPMDAO perioPMDAO;
	private static HPDAO hpDAO;
	private static DeModPerDAO deModPerDAO;
	private static DeFamPerDAO deFamPerDAO;
	private static CadPlanDAO cadPlanDAO;
	
	// Services
	private static PerioPMAppService perioPMService;
	private static HPAppService hpService;

	public PerioPAPAppService() {
		try {
			
			// DAOs
			// O atributo PerioPMDAO em tempo de compilacao eh do tipo
			// PerioPMDAO e em runtime ele eh do tipo PerioPMDAOImpl
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			perioPAPDAO = FabricaDeDao.getDao(PerioPAPDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			deFamPerDAO = FabricaDeDao.getDao(DeFamPerDAOImpl.class);
			
			// Services
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Inclui um objeto do tipo PerioPM, mas antes verifica se ja existe um
	 * periodo com este numero.
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(PerioPAP perioPAP) throws AplicacaoException {

		try {
			perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(perioPAP.getPeriodoPAP());
			throw new AplicacaoException("perioPAP.PERIODO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {
		}
		perioPAPDAO.inclui(perioPAP);

	}

	/**
	 * Inclui um objeto do tipo PerioPAP equivalente ao PerioPM informado. Antes
	 * verifica se ja existe um periodoPAP com este numero.
	 * 
	 * Atualiza os campos calculados do PerioPAP com as informaçoes do PerioPM
	 * (datas inicial e final, Numero do PerioPM e NumDiasUteis) PeriodoPAP é
	 * feito inicialmente = PeriodoPM.
	 * Inclui PerioPM depois de
	 * perioPM.setPerioPAP.
	 * 
	 * É preciso incluir primeiro o PerioPAP apesar da inclusao ser feita no
	 * PerioPM pois o PerioPAP é o lado um da relacao 1 para muitos.
	 * 
	 * @throws AplicacaoException
	 * @author felipe.arruda
	 */
	@Transacional
	public void incluiComPerioPM(PerioPM perioPM) throws AplicacaoException {

		PerioPAP perioPAP = new PerioPAP();

		perioPAP.setPeriodoPAP(perioPM.getPeriodoPM());
		perioPAP.setDataInicial(perioPM.getDataInicial());
		perioPAP.setDataFinal(perioPM.getDataFinal());
		perioPAP.setNumDiasUteis(perioPM.getNumDiasUteisMatriz());

		try {
			perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(perioPAP.getPeriodoPAP());
			throw new AplicacaoException("perioPAP.PERIODO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {
		}
		try {

			perioPAPDAO.inclui(perioPAP);

			// depois de incluir o perioPAP no DB ele vai chamar a inclusao do
			// perioPM
			perioPM.setPerioPAP(perioPAP);
			perioPMService.inclui(perioPM);
		} catch (AplicacaoException e) {

		}
	}

	/**
	 * Altera os novos perioPMs para que estes fiquem apontando para o perioPAP sendo alterado.
	 * 
	 * Altera todos os periosPAP subsequentes a essa alteracao, fazendo-os 1x1
	 * 
	 * Etapas:
	 * -obtem lista dos perioPAPs subsequentes ao alterado, incluindo o  proprio.
	 * -Obtem o numero de perioPAPs restantes, incluindo o que esta sendo alterado.
	 * -obtem lista dos perioPAPs subsequentes ao alterado, a partir do perioPM inicial do perioPAP que esta sendo alterado.
	 * -Obtem o numero de perioPMs restantes, apartir do perioPM inicial do perioPAP que esta sendo alterado.
	 * -Obtem a nova lista de perioPMs do PerioPAP sendo alterado..
	 * -Seta o novo valor de periodoPAP em todos os perioPMs que vao utilizar agora o perioPAP sendo alterado.
	 * -Executa: alteraPerioPAPsSubsequentes()
	 * 	Passando como parametros: 
	 *	Primeiro: perioPAP subsequente a alteracao
	 *	Segundo: verifica o perioPM final daquele perioPAP sendo alterado, e soma 1 para obter o primeiro perioPM do proximo perioPAP
	 *	Terceiro: o numero de perioPAPs restantes, incluindo o que esta sendo alterado
	 *	Quarto: o numero de perioPMs restantes, apartir do perioPM inicial do perioPAP que esta sendo alterado
	 */
	@Transacional
	public void altera(PerioPAP perioPAP) {
		int numPeriodosRestantesPAP,numPeriodosRestantesPM; 
		

		// obtem lista dos perioPAPs subsequentes ao alterado, incluindo o
		// proprio
		List<PerioPAP> listaDePerioPAPsSubsequentes = perioPAPDAO.recuperaIntervaloDePerioPAPs(perioPAP.getPeriodoPAP(),
																								perioPAPDAO.recuperaListaDePerioPAPs().size());		
		//Obtem o numero de perioPAPs restantes, incluindo o que esta sendo alterado
		numPeriodosRestantesPAP = listaDePerioPAPsSubsequentes.size();
		
		
		
		// obtem lista dos perioPAPs subsequentes ao alterado, apartir do perioPM inicial do perioPAP que esta sendo alterado
		List<PerioPM> listaDePerioPMsSubsequentes = perioPMDAO.recuperaIntervaloDePerioPMs(perioPAP.getPeriodoPMInic(),
																								perioPMDAO.recuperaListaDePerioPMs().size());		
		//Obtem o numero de perioPMs restantes, apartir do perioPM inicial do perioPAP que esta sendo alterado
		numPeriodosRestantesPM = listaDePerioPMsSubsequentes.size();
		
		


		//Obtem a nova lista de perioPMs do PerioPAP sendo alterado.
		List<PerioPM> novaListaDePerioPMsdoPerioPAP = perioPMDAO.recuperaIntervaloDePerioPMs(perioPAP.getPeriodoPMInic(),
																								perioPAP.getPeriodoPMFinal());


		//Seta o novo valor de periodoPAP em todos os perioPMs que vao utilizar agora o perioPAP sendo alterado
		for (PerioPM perioPM : novaListaDePerioPMsdoPerioPAP) {
			perioPM.setPerioPAP(perioPAP);
			perioPMService.altera(perioPM);


		}
		
//		Passando como parametro: 
//		Primeiro: perioPAP subsequente a alteracao
//		Segundo: verifica o perioPM final daquele perioPAP sendo alterado, e soma 1 para obter o primeiro perioPM do proximo perioPAP
//		Terceiro: o numero de perioPAPs restantes, incluindo o que esta sendo alterado
//		Quarto: o numero de perioPMs restantes, apartir do perioPM inicial do perioPAP que esta sendo alterado
		alteraPerioPAPsSubsequentes(perioPAP.getPeriodoPAP() + 1, perioPAP.getPeriodoPMFinal() + 1,numPeriodosRestantesPAP,numPeriodosRestantesPM);

	}

	/**
	 * Nao existe opcao em tela para a exclusao de perioPAPs, esse metodo é chamado, a partir de regras de negocios,
	 *  como alteracoes e reinicializacao
	 *  
	 * @param perioPAP
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(PerioPAP perioPAP) throws AplicacaoException {

		PerioPAP perioPAPBD = null;

		try {
			perioPAPBD = perioPAPDAO.getPorIdComLock(perioPAP.getId());

		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("perioPAP.NAO_ENCONTRADO");
		}

		perioPAPDAO.exclui(perioPAPBD);
	}
	
	/**
	 * Metodo que exclui um perioPAP junto com suas dependencias, para evitar inconsistencia
	 * no banco no que diz respeito ao perioPAP.
	 * 
	 * Altera os campos de perioPAP no HP para que caso o perioPAP excluido seja o
	 * periodoPAP inicial do HP ou o final.
	 * Se for excluido o inicial, ele ira por no lugar o proximo perioPAP.
	 * Se for excluido o final, ele ira por no lugar o perioPAP anterior.
	 * 
	 * Se em qualquer um dos casos o novo candidato a periodo nao exitir,
	 * ele coloca ambos os campos do HP como null.
	 * 
	 * @param perioPAP
	 * @throws AplicacaoException
	 */
	@Transacional
	public void excluiComDependencias(PerioPAP perioPAP) throws AplicacaoException {

		PerioPAP perioPAPBD = null;

		try {
			perioPAPBD = perioPAPDAO.getPorIdComLock(perioPAP.getId());

		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("perioPAP.NAO_ENCONTRADO");
		}


		//Antes de deletar perioPAP tem que deletar/remover suas ocorrencias nas dependencias.
		//Alterar HP, remover Defarper, perioPM(a principio nao tem mais referencia)

		List<HP> hpList = hpDAO.recuperaListaDeHP();
		if(!hpList.isEmpty()){
			HP hp = hpList.get(0);
			//PAREI AKI!!!!!!!
			
			try{
				if ( hp.getPerioPAPInicPAP().getPeriodoPAP() == perioPAPBD.getPeriodoPAP() ){
					PerioPAP novoPerioPAP = null;
					try{
						novoPerioPAP = perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(perioPAPBD.getPeriodoPAP()+1);
					}catch(ObjetoNaoEncontradoException e1){						
					}
					if(novoPerioPAP == null){
						hp.setPerioPAPFinalPAP(null);
						hp.setPerioPAPFinalDemFam(null);
						
					}
					
					hp.setPerioPAPInicPAP(novoPerioPAP);
					hp.setPerioPAPInicDemFam(novoPerioPAP);
				}
				if ( hp.getPerioPAPFinalPAP().getPeriodoPAP() == perioPAPBD.getPeriodoPAP() ){
					PerioPAP novoPerioPAP = null;
					try{
						novoPerioPAP = perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(perioPAPBD.getPeriodoPAP()-1);
					}catch(ObjetoNaoEncontradoException e1){						
					}

					if(perioPAP == null){
						hp.setPerioPAPInicPAP(null);
						hp.setPerioPAPInicDemFam(null);
						
					}
					
					hp.setPerioPAPFinalPAP(novoPerioPAP);
					hp.setPerioPAPFinalDemFam(novoPerioPAP);
				}
			}catch(Exception e){
			}
			hpService.altera(hp);
		}
		
		//recupera lista de deFamPers
		List<DeFamPer> listaDeFamPers = deFamPerDAO.recuperaListaDeDemandaFamiliaPerioPAP();
		
		//Percorre lista de DeFamPers deletando
		//os registros que estao ligados ao perioPAP a ser deletado.
		for(DeFamPer deFamPer : listaDeFamPers){
			if(deFamPer.getPerioPAP().getPeriodoPAP() == perioPAPBD.getPeriodoPAP()){
				//Prepara o deFamPerService
				DeFamPerAppService deFamPerService = null;
				try {
					deFamPerService = FabricaDeAppService
							.getAppService(DeFamPerAppService.class);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				deFamPerService.exclui(deFamPer);
			}					
		}
		
		this.exclui(perioPAPBD);
	}


	public List<PerioPAP> recuperaListaDePerioPAPs() {
		return perioPAPDAO.recuperaListaDePerioPAPs();
	}

	
	
	public PerioPAP recuperaPerioPAPPorPeriodoPAP(int periodoPAP) throws AplicacaoException {
		
		try {
			PerioPAP perioPAP = perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(periodoPAP);

			PerioPM perioPMInicial = obtemPrimeiroPerioPMdoPerioPAP(perioPAP);
			
			// se for null significa que o perioPAP atual deveria ter sido
			// deletado e por alguma razao n foi,
			// pois nao tem nenhum perioPM ligado a ele.

			// Atribui valores atuais para os campos calculados
			perioPAP.setPeriodoPMInic(perioPMInicial.getPeriodoPM());
			perioPAP.setDataInicial(perioPMInicial.getDataInicial());

			PerioPM perioPMFinal = obtemUltimoPerioPMdoPerioPAP(perioPAP);
			perioPAP.setPeriodoPMFinal(perioPMFinal.getPeriodoPM());
			perioPAP.setDataFinal(perioPMFinal.getDataFinal());

			perioPAP.setNumDiasUteis(perioPMService
					.calculaTotalDiasUteisIntervalo(perioPMInicial,
							perioPMFinal));		
			return perioPAP;
			
			
			
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("perioPAP.NAO_ENCONTRADO");
		}
		
	}


	/**
	 * Como a relacao é eager, ao recuperar a lista de perioPAPs vem junto as
	 * listas de PerioPMs Alem de recuperar lista paginada atribui os campos
	 * calculados todos.
	 * 
	 */
	public List<PerioPAP> recuperaListaPaginadaDePerioPAPs() {
		List<PerioPAP> listaDePerioPAP = perioPAPDAO
				.recuperaListaPaginadaDePerioPAPs();

		for (PerioPAP perioPAP : listaDePerioPAP) {


			// Obtem PeriodoPM inicial e final desse PerioPAP
			
			PerioPM perioPMInicial = obtemPrimeiroPerioPMdoPerioPAP(perioPAP);
			
			// se for null significa que o perioPAP atual deveria ter sido
			// deletado e por alguma razao n foi,
			// pois nao tem nenhum perioPM ligado a ele.

			// Atribui valores atuais para os campos calculados
			perioPAP.setPeriodoPMInic(perioPMInicial.getPeriodoPM());
			perioPAP.setDataInicial(perioPMInicial.getDataInicial());

			PerioPM perioPMFinal = obtemUltimoPerioPMdoPerioPAP(perioPAP);
			perioPAP.setPeriodoPMFinal(perioPMFinal.getPeriodoPM());
			perioPAP.setDataFinal(perioPMFinal.getDataFinal());

			perioPAP.setNumDiasUteis(perioPMService
					.calculaTotalDiasUteisIntervalo(perioPMInicial,
							perioPMFinal));
		}
	

		return listaDePerioPAP;
	}
	/**
	 * O numero de dias uteis do perioPAP é calculado 
	 * a partir do numero de dias uteis de cada perioPM que ele possui.
	 * @param periodos
	 * @return
	 */
	public double calculaTotalDiasUteis(List<PerioPM> periodos) {

		double total = 0.0;

		for (PerioPM perioPM : periodos) {
			total += perioPM.getNumDiasUteisMatriz();
		}

		return total;
	}

	/**
	 * Dado um objeto PerioPAP o metodo retorna o primeiro periopm de sua lista
	 * Retorna null caso a listaDePerioPMs seja vazia.
	 * @param perioPAP
	 * @return
	 */
	public PerioPM obtemPrimeiroPerioPMdoPerioPAP(PerioPAP perioPAP) {

		List<PerioPM> listaPerioPMs = perioPMDAO
				.recuperaListaDePerioPMsPorPerioPAP(perioPAP);

		
		PerioPM perioPM = null;
		

		if (!listaPerioPMs.isEmpty()) {
			perioPM = (PerioPM) listaPerioPMs.get(0);
		}

		return perioPM;

	}

	/**
	 * Dado um objeto PerioPAP o metodo retorna o ultimo periopm de sua lista
	 * Retorna null caso a listaDePerioPMs seja vazia.
	 * @param perioPAP
	 * @return
	 */
	public PerioPM obtemUltimoPerioPMdoPerioPAP(PerioPAP perioPAP) {

		List<PerioPM> listaPerioPMs = perioPMDAO
				.recuperaListaDePerioPMsPorPerioPAP(perioPAP);

		PerioPM perioPM = listaPerioPMs.get(listaPerioPMs.size() - 1);

		if (perioPM == null) {
			return null;
		}
		return perioPM;

	}

	/**
	 * Verifica e inclui perioPAPs ao fim da lista quando necessario, ou seja quando reduzir a quantidade de perioPMs de um PerioPAP
	 * Altera perioPMs para ficarem 1 para 1 com os perioPAPs
	 * Verifica e exclui perioPAPs quando estes estao sobrando em relacao aos perioPMs
	 * @param periodoPAPInic = A partir dele que serão feitas as alterações em perioPAPs.
	 * @param periodoPMInic = o perioPM inicial do perioPAP subsequente ao alterado.
	 * @param numPeriodosRestantesPAP = Numero de periodosPAPs subsequentes, incluindo o que foi alterado.
	 * @param numPeriodosRestantesPM = Numero de periodosPMs subsequentes, incluindo o perioPM inicial que pertencia ao perioPAP que foi alterado.
	 */
	@Transacional
	public void alteraPerioPAPsSubsequentes(int periodoPAPInic,	int periodoPMInic, int numPeriodosRestantesPAP,int numPeriodosRestantesPM) {
		
		//antes de qualquer coisa neste metodo, ele precisa verificar se eh necessario adicionar perioPAPs
		
		//====Esse trecho que inclui os perioPAPs que estao faltando, quando necessario====
		
		//Se a diferenca nao for positiva, entao nao precisa incluir perioPAP
		if(numPeriodosRestantesPM - numPeriodosRestantesPAP > 0){
			
			//Isso faz com que ele va incluindo apartir do ultimo
			int novoPeriodoPAP = perioPAPDAO.recuperaListaDePerioPAPs().size();
			
			for (int i = 0; i < (numPeriodosRestantesPM - numPeriodosRestantesPAP); i++) {
				
				novoPeriodoPAP += 1;
				PerioPAP novoPerioPAP = new PerioPAP(novoPeriodoPAP, null);
				perioPAPDAO.inclui(novoPerioPAP);
			}
			
			
		}
		
		
		//====Esse trecho faz a relacao 1x1 de perioPAPs para perioPMs====
		
		// obtem o PerioPAP porem agora persistido (o que tinhamos ainda nao era persistido)
		// precisamos dele persistido pois usamos o perioPM.setPerioPAP
		PerioPAP perioPAP = null;
		try {
			perioPAP = perioPAPDAO
					.recuperaPerioPAPPorPeriodoPAP(periodoPAPInic);
		} catch (ObjetoNaoEncontradoException e) {

		}
		
		//Obtem a lista de perioPMs a partir do parametro periodoPMInic
		List<PerioPM> listaDePerioPMsSubsequentes = perioPMDAO.recuperaIntervaloDePerioPMs(periodoPMInic, 
																							perioPMDAO.recuperaListaDePerioPMs().size());
		//Percorre a listaDePerioPMsSubsequentes alterando os perioPMs para ficarem
		//com a relação 1x1 com perioPAP
		for (PerioPM perioPM : listaDePerioPMsSubsequentes) {
			try {
				perioPAP = perioPAPDAO
						.recuperaPerioPAPPorPeriodoPAP(periodoPAPInic);
			} catch (ObjetoNaoEncontradoException e) {
			}
			perioPM.setPerioPAP(perioPAP);
			perioPMService.altera(perioPM);

			periodoPAPInic = periodoPAPInic + 1;
		}
		
		
		//====Esse trecho faz a exclusao dos perioPAPs restantes====
		
		//Recupera a lista de perioPAPs restantes
		List<PerioPAP> listaDePerioPAPsRestantes = perioPAPDAO
				.recuperaIntervaloDePerioPAPs(periodoPAPInic, perioPAPDAO
						.recuperaListaDePerioPAPs().size());
		
		//Se a listaDePerioPAPsRestantes nao for vazia, ele percorre a mesma
		//e exclui cada um dos perioPAPs que estao sobrando
		for (PerioPAP perioPAPRestante : listaDePerioPAPsRestantes) {
			try {
				excluiComDependencias(perioPAPRestante);
			} catch (AplicacaoException e) {
			}
		}

	}
	

	/**
	 */
	@Transacional
	public void agrupaPerioPAPs(int agregador) {
		int contadorDoGrupoDePerioPMs=0;
		int iteradorPeriodoPAP;
		
		PerioPAP perioPAP = null;

		//====Esse trecho faz a relacao 1x1 de perioPAPs para perioPMs====
		reinicializaPerioPAPs();
		
		
		//====Esse trecho faz a relacao 1xAgregador de perioPAPs para perioPMs====
		
		//Obtem a lista de perioPMs 
		List<PerioPM> listaDePerioPMs = perioPMDAO.recuperaListaDePerioPMs();
		
		//Comeca sempre com o periodoPAP 1
		iteradorPeriodoPAP = 1;
		
		//Percorre a listaDePerioPMsSubsequentes alterando os perioPMs para ficarem
		//com a relação 1xAgregador com perioPAP
		for (PerioPM perioPM : listaDePerioPMs) {
			
			//Caso esteja dentro de um grupo de perioPMs ainda
			if(contadorDoGrupoDePerioPMs < agregador){
				try {
					perioPAP = perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(iteradorPeriodoPAP);
				} catch (ObjetoNaoEncontradoException e) {
				}
				
				perioPM.setPerioPAP(perioPAP);
				perioPMService.altera(perioPM);
				//vai para o proximo elemento do grupo de perioPMs
				contadorDoGrupoDePerioPMs++;	
			}
			
			if(contadorDoGrupoDePerioPMs >= agregador){
				//vai para o primeiro perioPM do proximo grupo
				contadorDoGrupoDePerioPMs=0;
				
				//vai para o periodoPAP do proximo grupo
				iteradorPeriodoPAP++;
			}
			

		}
		
		
		//====Esse trecho faz a exclusao dos perioPAPs restantes====
		
		//Recupera a lista de perioPAPs restantes
		List<PerioPAP> listaDePerioPAPsRestantes = perioPAPDAO.recuperaIntervaloDePerioPAPs(iteradorPeriodoPAP, 
																								perioPAPDAO.recuperaListaDePerioPAPs().size());
		
		//Se a listaDePerioPAPsRestantes nao for vazia, ele percorre a mesma
		//e exclui cada um dos perioPAPs que estao sobrando
		for (PerioPAP perioPAPRestante : listaDePerioPAPsRestantes) {
			try {
				excluiComDependencias(perioPAPRestante);
			} catch (AplicacaoException e) {
			}
		}

	}

	/**
	 * Reinicializa os perioPAPs em funcao dos periosPMs fazendo-os 1x1
	 * 
	 */
	@Transacional
	public void reinicializaPerioPAPs(){
		
		
		int quantidadePerioPMs, quantidadePerioPAPs;
		
		List<PerioPM> listaDePerioPMs = perioPMDAO.recuperaListaDePerioPMs();
		quantidadePerioPMs = listaDePerioPMs.size();
		
		quantidadePerioPAPs = perioPAPDAO.recuperaListaDePerioPAPs().size();
		
		//se for positivo, quer dizer que vai ter que criar mais perioPAPs
		int diferencaPerioPMcomPerioPAP = quantidadePerioPMs - quantidadePerioPAPs;

		//Atualiza o HP, bota como null os campos de perioPAP
		try{
			HP hpBD = hpDAO.recuperaListaDeHP().get(0);
			hpBD.setPerioPAPInicDemFam(null);
			hpBD.setPerioPAPInicPAP(null);
			hpBD.setPerioPAPFinalDemFam(null);
			hpBD.setPerioPAPFinalPAP(null);
			hpDAO.altera(hpBD);
			Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
			parametro.setInicPlanejamento(false);
			parametrosDAO.altera(parametro);
		}catch(Exception e){			
		}

		//Inclui a quantidade de perioPAPs necessaria que esta faltando
		if(diferencaPerioPMcomPerioPAP > 0){
			for(int i=1; i <= diferencaPerioPMcomPerioPAP; i++){
				
				PerioPAP novoPerioPAP = new PerioPAP();				
				novoPerioPAP.setPeriodoPAP(quantidadePerioPAPs+i);
					try {
						inclui(novoPerioPAP);
					} catch (AplicacaoException e) {
					}
				
			}
		}
		
		//Percorre lista de perioPM alterando o atributo perioPAP dele para ficar com relacao 1 para 1
		for(PerioPM perioPM : listaDePerioPMs){
			
			PerioPAP newPerioPAP = null;
			
			try {
				newPerioPAP = perioPAPDAO.recuperaPerioPAPPorPeriodoPAP(perioPM.getPeriodoPM());
			} catch (ObjetoNaoEncontradoException e) {
			}

			perioPM.setPerioPAP(newPerioPAP);
			perioPMService.altera(perioPM);
		}	

		
		//>>>> Zera DeFamPer

		//recupera lista de deFamPers
		List<DeFamPer> demandasFamilia = deFamPerDAO.recuperaListaDeDemandaFamiliaPerioPAP();
		
		//Zerar TODOS os registros de deFamPer pois fica trocado as informacoes
		//de perioPAP, logo perde a logica de negocio(apesar da logica do banco ser mantida)
		DeFamPerAppService deFamPerService = null;
		try {
			deFamPerService = FabricaDeAppService.getAppService(DeFamPerAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		for (DeFamPer deFamPer : demandasFamilia) {
				
				deFamPer.setPedidosFamilia(0.0);
				deFamPer.setVendasProjetadasFamilia(0.0);
				deFamPerService.altera(deFamPer);
		}
		
		

	}
	

	//AINDA NAO IMPLEMENTADO- fazer refatoracao antes
	@Transacional
	public void atualizarEntidadesDependentes() throws AplicacaoException {
		
		// 1) Setar o parametro 'InicPlanejamento' como FALSE
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		parametro.setInicPlanejamento(false);
		parametrosDAO.altera(parametro);
		
		
		// 2) EXCLUIR OS REGISTROS DE DeFamPer (PROCESSO ANALOGO AO FEITO EM DeModPer ABAIXO)
		//
		//    ---------> Ainda nao implementado, pois aguarda a conclusao de DeFamPer <---------
		
		
		// 3) EXCLUIR OS REGISTROS DE DeModPer 
		
			// a) Excluir todos os registros de DeModPer cadastrados
			List<DeModPer> demandas = deModPerDAO.recuperaListaDeDemandaModeloPeriodo();
			for (DeModPer deModPer : demandas) {
				deModPerDAO.exclui(deModPer);
			}
			
		// 4) Excluir o HP cadastrado no Sistema
			List<HP> hpBD = hpDAO.recuperaListaDeHP();
			if (!hpBD.isEmpty()){
				HP hp = hpBD.get(0);
				hpDAO.exclui(hp);
			}
			
		// 5) Excluir todos os registros de CadPlan do sistema //exclui em cascata os planoModelo e PlPerMod relativos a cada cadPlan
		List<CadPlan> planos = cadPlanDAO.recuperaListaDeCadPlan();
		for (CadPlan plano : planos) {
			cadPlanDAO.exclui(plano);
		}
	}
	
	
	
	
}
