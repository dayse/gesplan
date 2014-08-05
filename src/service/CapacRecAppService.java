 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import service.anotacao.Transacional;

import service.exception.AplicacaoException;

import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.CapacRecDAOImpl;
import DAO.Impl.RecursoDAOImpl;

import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import DAO.PerioPMDAO;
import DAO.CapacRecDAO;
import DAO.RecursoDAO;


import modelo.CapacDia;
import modelo.PerioPM;
import modelo.CapacRec;
import modelo.Recurso;


public class CapacRecAppService {
	// DAOs
	private static CapacRecDAO capacRecDAO;
	private static PerioPMDAO perioPMDAO;
	private static RecursoDAO recursoDAO;
	
	@SuppressWarnings("unchecked")
	public CapacRecAppService() {
		try {
			// DAOs
			capacRecDAO = FabricaDeDao.getDao(CapacRecDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			recursoDAO = FabricaDeDao.getDao(RecursoDAOImpl.class);
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

	@Transacional
	public long inclui(CapacRec capacRec) 
		throws AplicacaoException
	{
		long retorno = -1;
		CapacRec capacRecBD = null;
		// verifica se existe o recurso cadastrado
		try
		{	recursoDAO.getPorIdComLock(capacRec.getRecurso().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}
		
		// verifica se existe o perioPM cadastrado
		try
		{	perioPMDAO.getPorIdComLock(capacRec.getPerioPM().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("perioPM.NAO_ENCONTRADO");
		}
		
		// verifica se o recurso já esta relacionado com este perioPM 
		try {
			capacRecBD = capacRecDAO.recuperaCapacRecPorRecursoEPerioPM(capacRec.getRecurso(), capacRec.getPerioPM());
			throw new AplicacaoException("capacRec.ENCONTRADO_PERIOPM");
		} catch (ObjetoNaoEncontradoException ob) {
			//o recurso e o perioPM serao setados no iniciaPlanejamento do HPAppService, que é o responsavel pela inclusao
			retorno = capacRecDAO.inclui(capacRec).getId();

		}
		return retorno;
	}	

	
	@Transacional
	public void altera(CapacRec capacRec) {
		capacRecDAO.altera(capacRec);
	}

	/**
	 * Alteracao da capacidade em todos os registros a partir
	 * da variavel capacidadePadrao informada pelo usuario
	 **/
	@Transacional
	public void alteraCapacPadrao(Recurso recursoCorrente, double capacidadePadrao) {
		
		List<CapacRec> listaDeCapacRecs = capacRecDAO.recuperaListaDeCapacRecsPorRecurso(recursoCorrente);
		
		for(CapacRec capacRec : listaDeCapacRecs){
			capacRec.setCapacDiaria(capacidadePadrao);
			altera(capacRec);
		}
		
	}
	
	/**
	 * VERIFICAR SE ESTA FUNCIONAL
	 */
	@Transacional
	public void exclui(CapacRec umCapacRec) throws AplicacaoException {
		
		CapacRec capacRec = null;
		
		try {
			capacRec = capacRecDAO.getPorIdComLock((umCapacRec.getId()));
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("capacRec.NAO_ENCONTRADO");
		}

		capacRecDAO.exclui(capacRec);
	}

	/**
	 * Usa um método do DAO para recuperar um CapacRec juntamente com o seu perioPM
	 * 
	 * @author dayse.arruda
	 * @throws AplicacaoException
	 */
	public CapacRec recuperaCapacRecComPerioPM(CapacRec capacRec) throws AplicacaoException {
		
		try {
			return capacRecDAO.recuperaCapacRecComPerioPM(capacRec);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("capacRec.NAO_ENCONTRADO");
		}
	}
	
	public CapacRec recuperaCapacRecPorRecursoEPerioPM(Recurso recurso, PerioPM perioPM) throws AplicacaoException {

		try {
			return capacRecDAO.recuperaCapacRecPorRecursoEPerioPM(recurso, perioPM);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("capacRec.NAO_ENCONTRADO");
		}
	}
	
	public List<CapacRec> recuperaListaDeCapacRecsPorRecurso(Recurso recurso) throws AplicacaoException {

    	List<CapacRec> capacRecs = capacRecDAO.recuperaListaDeCapacRecsPorRecurso(recurso);
		
		if (capacRecs.size() == 0) {
			throw new AplicacaoException("capacRec.NAO_ENCONTRADO");
		} else {
			return capacRecs;
		}

	}
	
	
	
	/**
	 * 
	 * Usa CapacRecDAO para recuperar lista de todos os CapacRecs. Retorna um List
	 * de CapacRecs
	 * 
	 * @author felipe.arruda
	 * @throws AplicacaoException
	 */
	public List<CapacRec> recuperaListaDeCapacRecs() throws AplicacaoException {
		
		List<CapacRec> capacRecs = capacRecDAO.recuperaListaDeCapacRecs();
		
		if (capacRecs.size() == 0) {
			throw new AplicacaoException("capacRec.NAO_ENCONTRADO");
		} else {
			return capacRecs;
		}
	}



	public List<CapacRec> recuperaListaDeCapacRecsComPerioPMs () {
		return capacRecDAO.recuperaListaDeCapacRecsComPerioPMs();
	}


	public List<CapacRec> recuperaListaDeCapacRecsComRecursosEPerioPMs () {
		return capacRecDAO.recuperaListaDeCapacRecsComRecursosEPerioPMs();
	}

}
