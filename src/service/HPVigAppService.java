 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.HP;
import modelo.HPVig;
import modelo.PerioPAPVig;
import modelo.PerioPMVig;
import service.anotacao.Transacional;
import service.exception.AplicacaoException;
import DAO.HPDAO;
import DAO.HPVigDAO;
import DAO.PerioPAPVigDAO;
import DAO.PerioPMVigDAO;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.HPVigDAOImpl;
import DAO.Impl.PerioPAPVigDAOImpl;
import DAO.Impl.PerioPMVigDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class HPVigAppService {

	// DAOs
	private static HPVigDAO hpVigDAO;
	private static HPDAO hpDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	private static PerioPAPVigDAO perioPAPVigDAO;

	@SuppressWarnings("unchecked")
	public HPVigAppService() {

		try {

			// DAO
			hpVigDAO = FabricaDeDao.getDao(HPVigDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			perioPAPVigDAO = FabricaDeDao.getDao(PerioPAPVigDAOImpl.class);

		} catch (Exception e) {
			e.printStackTrace();
			// O comando a seguir só será usado caso haja a criação de um service.
			// Exemplo:
			// Um Service A tem dentro de si a chamada de um Service B, só que o Service B também tem
			// uma chamada para o Service A, logo um service chamaria o outro sem parar causando assim um loop infinito.
			// Contudo, em termos de uso do sistemas esse erro não ocorreria de forma clara, 
			// pois a View seria carregada sem dados.
			// Para evitar que esse tipo de erro gere confusões - como o usuário pensar que o banco foi perdido, por exemplo - 
			// utilizamos o comando System.exit(1) que interrompe a aplicação, deixando explicita a ocorrência do erro.
			//System.exit(1); 
		}
	}

	// Verificar se tem que mudar o valor de parametros.inicPlanejamento
	@Transacional
	public void inclui(HPVig hpVig) throws AplicacaoException {
		//		
		// Parametros parametro =
		// parametrosDAO.recuperaListaDeParametros().get(0);
		// parametro.setInicPlanejamento(false);
		// parametrosDAO.altera(parametro);

		hpVigDAO.inclui(hpVig);
	}

	@Transacional
	public void incluiCopiandoTodosHPs() throws AplicacaoException{
		List<HP> listaDeHPs = hpDAO.recuperaListaDeHP();
		
		for(HP hp : listaDeHPs){
			this.incluiCopiandoHP(hp);
		}

	}
	
	@Transacional
	public void incluiCopiandoHP(HP hp) throws AplicacaoException {
		HPVig hpVig = new HPVig();

		PerioPMVig perioPMVig = new PerioPMVig();
		PerioPAPVig perioPAPVig = new PerioPAPVig();
		
		try {
			perioPMVig = perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(hp
					.getPerioPMInicPMP().getPeriodoPM());
			hpVig.setPerioPMInicPMP(perioPMVig);

			perioPMVig = perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(hp
					.getPerioPMFinalPMP().getPeriodoPM());
			hpVig.setPerioPMFinalPMP(perioPMVig);

			perioPMVig = perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(hp
					.getPerioPMInicDemMod().getPeriodoPM());
			hpVig.setPerioPMInicDemMod(perioPMVig);

			perioPMVig = perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(hp
					.getPerioPMFinalDemMod().getPeriodoPM());
			hpVig.setPerioPMFinalDemMod(perioPMVig);

			perioPAPVig = perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(hp
					   .getPerioPAPInicPAP().getPeriodoPAP());
			hpVig.setPerioPAPInicPAP(perioPAPVig);

			perioPAPVig = perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(hp
					.getPerioPAPFinalPAP().getPeriodoPAP());
			hpVig.setPerioPAPFinalPAP(perioPAPVig);

			perioPAPVig = perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(hp
					.getPerioPAPInicDemFam().getPeriodoPAP());
			hpVig.setPerioPAPInicDemFam(perioPAPVig);

			perioPAPVig = perioPAPVigDAO.recuperaPerioPAPVigPorPeriodoPAP(hp
					.getPerioPAPFinalDemFam().getPeriodoPAP());
			hpVig.setPerioPAPFinalDemFam(perioPAPVig);

		} catch (Exception e) {
		}
		
		this.inclui(hpVig);

		return;
	}

	@Transacional
	public void exclui(HPVig hpVig) throws AplicacaoException {

		HPVig hpVigBD = null;

		try {
			hpVigBD = hpVigDAO.getPorIdComLock(hpVig.getId());
		} catch (ObjetoNaoEncontradoException e) {
		}

		hpVigDAO.exclui(hpVigBD);
	}

	@Transacional
	public void apagaTodos() throws AplicacaoException{
		List<HPVig> listaDeHPVig = hpVigDAO.recuperaListaDeHPVig();
		
		for(HPVig hpVig : listaDeHPVig){
			this.exclui(hpVig);
		}

	}
	
	
	public List<HPVig> recuperaListaDeHPVig() {
		return hpVigDAO.recuperaListaDeHPVig();
	}
}
