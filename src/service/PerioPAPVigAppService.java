 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.PerioPAP;
import modelo.PerioPAPVig;
import modelo.PerioPM;
import modelo.PerioPMVig;

import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

import DAO.PerioPAPDAO;
import DAO.PerioPAPVigDAO;
import DAO.PerioPMVigDAO;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.PerioPAPVigDAOImpl;
import DAO.Impl.PerioPMVigDAOImpl;
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
public class PerioPAPVigAppService {

	// DAOs
	private static PerioPAPVigDAO perioPAPVigDAO;
	private static PerioPAPDAO perioPAPDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	
	// Services
	private static PerioPMVigAppService perioPMVigService;

	public PerioPAPVigAppService() {
		try {
			
			// DAOs
			// O atributo PerioPMDAO em tempo de compilacao eh do tipo
			// PerioPMDAO e em runtime ele eh do tipo PerioPMDAOImpl
			perioPAPVigDAO = FabricaDeDao.getDao(PerioPAPVigDAOImpl.class);
			perioPAPDAO = FabricaDeDao.getDao(PerioPAPDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			
			// Services
			perioPMVigService = FabricaDeAppService.getAppService(PerioPMVigAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Inclui um objeto do tipo PerioPAPVig, mas antes verifica se ja existe um
	 * periodo com este numero.
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(PerioPAPVig perioPAPVig) throws AplicacaoException {

		try {
			perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(perioPAPVig.getPeriodoPAP());
			throw new AplicacaoException("perioPAPVig.PERIODO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {
		}
		perioPAPVigDAO.inclui(perioPAPVig);

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
	 */
	@Transacional
	public void incluiComPerioPMVig(PerioPMVig perioPMVig) throws AplicacaoException {

		PerioPAPVig perioPAPVig = new PerioPAPVig();

		perioPAPVig.setPeriodoPAP(perioPMVig.getPeriodoPM());
		perioPAPVig.setDataInicial(perioPMVig.getDataInicial());
		perioPAPVig.setDataFinal(perioPMVig.getDataFinal());
		perioPAPVig.setNumDiasUteis(perioPMVig.getNumDiasUteisMatriz());

		try {
			perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(perioPAPVig.getPeriodoPAP());
			throw new AplicacaoException("perioPAPVig.PERIODO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {
		}
		try {

			perioPAPVigDAO.inclui(perioPAPVig);

			// depois de incluir o perioPAP no DB ele vai chamar a inclusao do
			// perioPM
			perioPMVig.setPerioPAPVig(perioPAPVig);
			perioPMVigService.inclui(perioPMVig);
		} catch (AplicacaoException e) {

		}
	}

	@Transacional
	public void incluiCopiandoTodosPerioPAPs() throws AplicacaoException{
		List<PerioPAP> listaDePerioPAPs = perioPAPDAO.recuperaListaDePerioPAPs();
		
		for(PerioPAP perioPAP : listaDePerioPAPs){
			this.incluiCopiandoPerioPAP(perioPAP);
		}

	}
	
	@Transacional
	public void incluiCopiandoPerioPAP(PerioPAP perioPAP) 
	throws AplicacaoException
{
	PerioPAPVig perioPAPVig = new PerioPAPVig();

	perioPAPVig.setDataInicial(perioPAP.getDataInicial());
	perioPAPVig.setDataFinal(perioPAP.getDataFinal());
	
	perioPAPVig.setNumDiasUteis(perioPAP.getNumDiasUteis());
	
	perioPAPVig.setPeriodoPAP(perioPAP.getPeriodoPAP());
	
	//Atencao neste momento perioPMVig nao existe ainda
	//nao tem problema essa inicializacao pois os campos abaixos sao integer
	//e nao tem ligacao direta com perioPMVig.
	perioPAPVig.setPeriodoPMInic(perioPAP.getPeriodoPMInic());
	perioPAPVig.setPeriodoPMFinal(perioPAP.getPeriodoPMFinal());

	
	//Como nesse momento ainda n tenho nenhum perioPMVig, eu tenho q setar como null a lista de perioPMVig
	//de um perioPMVig
	perioPAPVig.setPerioPMVigs(null);
	
	
	this.inclui(perioPAPVig);
	
	return;
}	

	/**
	 * Nao existe opcao em tela para a exclusao de perioPAPVigs, esse metodo é chamado, a partir de regras de negocios,
	 *  como alteracoes e reinicializacao
	 *  
	 * @param perioPAPVig
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(PerioPAPVig perioPAPVig) throws AplicacaoException {

		PerioPAPVig perioPAPVigBD = null;

		try {
			perioPAPVigBD = perioPAPVigDAO.getPorIdComLock(perioPAPVig.getId());

		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("perioPAPVig.NAO_ENCONTRADO");
		}

		perioPAPVigDAO.exclui(perioPAPVigBD);
	}


	public List<PerioPAPVig> recuperaListaDePerioPAPVigs() {
		return perioPAPVigDAO.recuperaListaDePerioPAPVigs();
	}
	
	
	@Transacional
	public void apagaTodos() throws AplicacaoException{
		List<PerioPAPVig> listaDePerioPAPVig = perioPAPVigDAO.recuperaListaDePerioPAPVigs();
		
		for(PerioPAPVig perioPAPVig : listaDePerioPAPVig){
			this.exclui(perioPAPVig);
		}

	}
	
	/**
	 * Como a relacao é eager, ao recuperar a lista de perioPAPVigs vem junto as
	 * listas de PerioPMVigs Alem de recuperar lista paginada atribui os campos
	 * calculados todos.
	 * 
	 */
	public List<PerioPAPVig> recuperaListaPaginadaDePerioPAPVigs() {
		List<PerioPAPVig> listaDePerioPAPVig = perioPAPVigDAO
				.recuperaListaPaginadaDePerioPAPVigs();

		for (PerioPAPVig perioPAPVig : listaDePerioPAPVig) {


			// Obtem PeriodoPM inicial e final desse PerioPAP
			
			PerioPMVig perioPMVigInicial = obtemPrimeiroPerioPMVigdoPerioPAPVig(perioPAPVig);
			
			// se for null significa que o perioPAPVig atual deveria ter sido
			// deletado e por alguma razao n foi,
			// pois nao tem nenhum perioPM ligado a ele.

			// Atribui valores atuais para os campos calculados
			perioPAPVig.setPeriodoPMInic(perioPMVigInicial.getPeriodoPM());
			perioPAPVig.setDataInicial(perioPMVigInicial.getDataInicial());

			PerioPMVig perioPMVigFinal = obtemUltimoPerioPMVigdoPerioPAPVig(perioPAPVig);
			perioPAPVig.setPeriodoPMFinal(perioPMVigFinal.getPeriodoPM());
			perioPAPVig.setDataFinal(perioPMVigFinal.getDataFinal());

			perioPAPVig.setNumDiasUteis(perioPMVigService
					.calculaTotalDiasUteisIntervalo(perioPMVigInicial,
							perioPMVigFinal));
		}
	

		return listaDePerioPAPVig;
	}
	
	@Transacional
	public PerioPAPVig recuperaPerioPAPVigPorPeriodoPAP(int periodoPAP) {
	
		PerioPAPVig perioPAPVigDB = new PerioPAPVig();
		try {
			perioPAPVigDB = perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(periodoPAP);
		} catch (ObjetoNaoEncontradoException e) {
		}
	

		return perioPAPVigDB;
	}
	
	/**
	 * O numero de dias uteis do perioPAPVig é calculado 
	 * a partir do numero de dias uteis de cada perioPMVig que ele possui.
	 * @param periodos
	 * @return
	 */
	public double calculaTotalDiasUteis(List<PerioPMVig> periodos) {

		double total = 0.0;

		for (PerioPMVig perioPMVig : periodos) {
			total += perioPMVig.getNumDiasUteisMatriz();
		}

		return total;
	}

	/**
	 * Dado um objeto PerioPAPVig o metodo retorna o primeiro periopmVig de sua lista
	 * Retorna null caso a listaDePerioPMVigs seja vazia.
	 * @param perioPAPVig
	 * @return
	 */
	public PerioPMVig obtemPrimeiroPerioPMVigdoPerioPAPVig(PerioPAPVig perioPAPVig) {

		List<PerioPMVig> listaPerioPMVigs = perioPMVigDAO
				.recuperaListaDePerioPMVigsPorPerioPAPVig(perioPAPVig);

		
		PerioPMVig perioPMVig = null;
		

		if (!listaPerioPMVigs.isEmpty()) {
			perioPMVig = (PerioPMVig) listaPerioPMVigs.get(0);
		}

		return perioPMVig;

	}

	/**
	 * Dado um objeto PerioPAPVig o metodo retorna o ultimo periopmVig de sua lista
	 * Retorna null caso a listaDePerioPMVigs seja vazia.
	 * @param perioPAPVig
	 * @return
	 */
	public PerioPMVig obtemUltimoPerioPMVigdoPerioPAPVig(PerioPAPVig perioPAPVig) {

		List<PerioPMVig> listaPerioPMVigs = perioPMVigDAO
				.recuperaListaDePerioPMVigsPorPerioPAPVig(perioPAPVig);

		PerioPMVig perioPMVig = listaPerioPMVigs.get(listaPerioPMVigs.size() - 1);

		if (perioPMVig == null) {
			return null;
		}
		return perioPMVig;

	}
	
}
