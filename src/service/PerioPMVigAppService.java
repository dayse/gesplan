 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.List;

import modelo.HPVig;
import modelo.PMP;
import modelo.PerioPAPVig;
import modelo.PerioPM;
import modelo.PerioPMVig;
import modelo.PlPerMod;

import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;


import DAO.HPVigDAO;
import DAO.PerioPAPVigDAO;
import DAO.PerioPMDAO;
import DAO.PerioPMVigDAO;
import DAO.Impl.HPVigDAOImpl;
import DAO.Impl.PerioPAPVigDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
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
 * Esta classe é originaria de jpa8 - DAO Generico familia e adaptada com
 * tratamento de excecao feito em EmpregadoAppService de jsf4b para o metodo que
 * recupera todas as familias
 * 
 * @author daysemou
 * 
 */
public class PerioPMVigAppService {
	
	// DAOs
	private static PerioPMDAO perioPMDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	private static HPVigDAO hpVigDAO;
	private static PerioPAPVigDAO perioPAPVigDAO;
	
	// Services
	private static HPVigAppService hpVigService;
	
	public PerioPMVigAppService() { 
		
		try {
			
			// DAOs
			hpVigDAO = FabricaDeDao.getDao(HPVigDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			perioPAPVigDAO = FabricaDeDao.getDao(PerioPAPVigDAOImpl.class);
			
			// Services
			hpVigService = FabricaDeAppService.getAppService(HPVigAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Inclui um objeto do tipo PerioPMVig, mas antes verifica se ja existe um periodo com este numero.
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(PerioPMVig perioPMVig) throws AplicacaoException {
		
		try {			
			// Se encontrou um PerioPMVig, é porque já existia alguem cadastrado c/ esse codigo.
			//
			perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(perioPMVig.getPeriodoPM());
			
			throw new AplicacaoException("perioPMVig.PERIODO_EXISTENTE");
			
		} catch (ObjetoNaoEncontradoException e) {			
		}
		
		perioPMVigDAO.inclui(perioPMVig);
	}

	

	@Transacional
	public void incluiCopiandoPerioPM(PerioPM perioPM) 
		throws AplicacaoException
	{
		PerioPMVig perioPMVig = new PerioPMVig();

		perioPMVig.setDataInicial(perioPM.getDataInicial());
		perioPMVig.setDataFinal(perioPM.getDataFinal());
		perioPMVig.setNumDiasUteisMatriz(perioPM.getNumDiasUteisMatriz());
		perioPMVig.setNumDiasUteisU2(perioPM.getNumDiasUteisU2());
		perioPMVig.setPeriodoPM(perioPM.getPeriodoPM());
		

		PerioPAPVig perioPAPVig = new PerioPAPVig();

		try {
			  // Foi utilizada a instanciacao neste ponto p/ evitar o problema de Inicializacao Ciclica!	
				PerioPAPVigAppService perioPAPVigAppService = FabricaDeAppService.getAppService(PerioPAPVigAppService.class);
				perioPAPVig = perioPAPVigAppService.recuperaPerioPAPVigPorPeriodoPAP(perioPM.getPerioPAP().getPeriodoPAP());
		 } catch (ObjetoNaoEncontradoException e) {
		 } catch (Exception e) {
			 e.printStackTrace();
			 System.exit(1);
		 }
		 
		
		perioPMVig.setPerioPAPVig(perioPAPVig);
		
		//Como nesse momento ainda n tenho nenhum PMP, eu tenho q setar como null a lista de pmps
		//de um perioPMVig
		perioPMVig.setPmps(null);
		
		
		this.inclui(perioPMVig);
		
		return;
	}
	
	
	@Transacional
	public void incluiCopiandoTodosPerioPMs() throws AplicacaoException{
		List<PerioPM> listaDePerioPM = perioPMDAO.recuperaListaDePerioPMsComPerioPAP();
		
		for(PerioPM perioPM : listaDePerioPM){
			this.incluiCopiandoPerioPM(perioPM);
		}

	}
	
	/**	A principio nao tera alteracao de um perioPMVig
	*/	
	@Transacional
	public void altera(PerioPMVig umPerioPMVig) {
		perioPMVigDAO.altera(umPerioPMVig);
	}

	@Transacional
	public void exclui(PerioPMVig perioPMVig) throws AplicacaoException {
		
		PerioPMVig perioPMVigBD = null;
		
		try {
			perioPMVigBD = perioPMVigDAO.getPorIdComLock(perioPMVig.getId());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		perioPMVigDAO.exclui(perioPMVigBD);
	}
	
	@Transacional
	public void apagaTodos() throws AplicacaoException{
		List<PerioPMVig> listaDePerioPMVig = perioPMVigDAO.recuperaListaDePerioPMVigs();
		
		for(PerioPMVig perioPMVig : listaDePerioPMVig){
			this.exclui(perioPMVig);
		}

	}

	public PerioPMVig recuperaPerioPMVigPorPeriodoPM(int periodoPM) throws AplicacaoException {
		
		try {
			return perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(periodoPM);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("perioPMVig.NAO_ENCONTRADO");
		}
	}

	public List<PerioPMVig> recuperaListaDePerioPMVigs() {
		return perioPMVigDAO.recuperaListaDePerioPMVigs();
	}
	

	public List<PerioPMVig> recuperaListaPaginadaDePerioPMVigs() {
		return perioPMVigDAO.recuperaListaPaginadaDePerioPMVigs();
	}
	
	/**
	 * Calcula o numero de dias uteis no perioPM dados os perioPM Inicial e final
	 * 
	 * @param periodoInicial
	 * @param periodoFinal
	 * @return double
	 */
	public double calculaTotalDiasUteisIntervalo(PerioPMVig periodoInicial, PerioPMVig periodoFinal){
		
		double total = 0.0;
		
		List<PerioPMVig> periodos = this.recuperaListaDePerioPMVigs();
		
		//lembrando que sublist utiliza como parametros( int arg, int arg2 -1) entao quando for chamar
		//utilizar no formato( periodoInicial-1, periodofinal)
		List<PerioPMVig> intervalo = periodos.subList((periodoInicial.getPeriodoPM()-1), (periodoFinal.getPeriodoPM()));
		
		for (PerioPMVig perioPMVig : intervalo) {
			total += perioPMVig.getNumDiasUteisMatriz();
		}
		return total;
	}
	
	
	public List<PerioPMVig> recuperaIntervaloDePerioPMVigs(int periodoInicial, int periodoFinal){
		return perioPMVigDAO.recuperaIntervaloDePerioPMVigs(periodoInicial, periodoFinal);
	}
}
