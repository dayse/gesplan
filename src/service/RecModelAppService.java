 
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

import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.RecModelDAOImpl;
import DAO.Impl.RecursoDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import DAO.ModeloDAO;
import DAO.RecModelDAO;
import DAO.RecursoDAO;


import modelo.Modelo;
import modelo.RecModel;
import modelo.Recurso;


public class RecModelAppService {
	
	// DAOs
	private static RecModelDAO recModelDAO;
	private static ModeloDAO modeloDAO;
	private static RecursoDAO recursoDAO;
	
	@SuppressWarnings("unchecked")
	public RecModelAppService() {
		try {
			//DAOs
			recModelDAO = FabricaDeDao.getDao(RecModelDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
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

	/**
	 * Inclui recmodel, ou seja, inclui modelo na lista de modelos que usam
	 * aquele recurso em particular
	 * Verifica antes se este modelo já havia sido incluido na lista de 
	 * recmodel deste recurso
	 * 
	 * @param recModel
	 * @throws AplicacaoException
	 */
/*	
	@Transacional
	public long inclui(RecModel recModel) throws AplicacaoException {
		long retorno = -1;
		RecModel recModelBD = null;
		try {
			recModelBD = recModelDAO.recuperaRecModelPorCodModelo(recModel.getModelo().getCodModelo());
			throw new AplicacaoException("recModel.CODIGO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException ob) {
			
			retorno = recModelDAO.inclui(recModel).getId();

		}
		return retorno;
	}
*/
    /**
     *  
	 * Inclui recmodel, ou seja, inclui modelo na lista de modelos que usam
	 * aquele recurso em particular
	 * Verifica antes se este modelo já havia sido incluido na lista de 
	 * recmodel deste recurso
     *  
     * - alterei bastante em relacao ao jpa8 original- verificar se está certo
     * Acho que talvez nao precise do lock
     * @param umModelo
     * @param umaFamilia
     * @return
     * @throws AplicacaoException
     */	
//VERSAO DAYSE INTERMEDIARIA ERRADO
//	@Transacional
//	public long inclui(RecModel recModel, Modelo modelo, Recurso recurso) 
//		throws AplicacaoException
//	{
//		long retorno = -1;
//		RecModel recModelBD = null;
//		// verifica se existe o recurso
//		try
//		{	recursoDAO.getPorIdComLock(recurso.getId());
//		}
//		catch(ObjetoNaoEncontradoException e)
//		{	throw new AplicacaoException("recurso.NAO_ENCONTRADO");
//		}
//		
//		// verifica se existe o modelo
//		try
//		{	modeloDAO.getPorIdComLock(modelo.getId());
//		}
//		catch(ObjetoNaoEncontradoException e)
//		{	throw new AplicacaoException("modelo.NAO_ENCONTRADO");
//		}
//		
//		// verifica se o recurso já é usado neste modelo 
//		try {
//			recModelBD = recModelDAO.recuperaRecModelPorRecursoEModelo(recurso, modelo);
//			throw new AplicacaoException("recModel.ENCONTRADO_MODELO");
//		} catch (ObjetoNaoEncontradoException ob) {
//			recModel.setModelo(modelo);
//			recModel.setRecurso(recurso);
//			retorno = recModelDAO.inclui(recModel).getId();
//
//		}
//		return retorno;
//	}	


	@Transacional
	public long inclui(RecModel recModel) 
		throws AplicacaoException
	{
		long retorno = -1;
		RecModel recModelBD = null;
		// verifica se existe o recurso
		try
		{	recursoDAO.getPorIdComLock(recModel.getRecurso().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}
		
		// verifica se existe o modelo
		try
		{	modeloDAO.getPorIdComLock(recModel.getModelo().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}
		
		// verifica se o recurso já é usado neste modelo 
		try {
			recModelBD = recModelDAO.recuperaRecModelPorRecursoEModelo(recModel.getRecurso(), recModel.getModelo());
			throw new AplicacaoException("recModel.ENCONTRADO_MODELO");
		} catch (ObjetoNaoEncontradoException ob) {
			//Recurso e modelo foram setados em RecModelActions no Inclui
			retorno = recModelDAO.inclui(recModel).getId();

		}
		return retorno;
	}	

	
	@Transacional
	public void altera(RecModel recModel) {
		recModelDAO.altera(recModel);
	}
	/**
	 * baseado em modelo - usei lock - verificar se é para ser desse jeito
	 * @param umRecModel
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(RecModel umRecModel) throws AplicacaoException {
		
		RecModel recModel = null;
		
		try {
			recModel = recModelDAO.getPorIdComLock((umRecModel.getId()));
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("recModel.NAO_ENCONTRADO");
		}

		recModelDAO.exclui(recModel);
	}

	/**
	 * Usa um método do DAO para recuperar um recmodel juntamente com o seu modelo
	 * 
	 * @author dayse.arruda
	 * @throws AplicacaoException
	 */
	public RecModel recuperaRecModelComModelo(RecModel recModel) throws AplicacaoException {
		
		try {
			return recModelDAO.recuperaRecModelComModelo(recModel);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("recModel.NAO_ENCONTRADO");
		}
	}

	public List<RecModel> recuperaListaDeRecModelsComRecursoComModelos() throws AplicacaoException{
		
		List<RecModel> recModels = recModelDAO.recuperaListaDeRecModelsComRecursoComModelos();

		if (recModels.size() == 0) {
			throw new AplicacaoException("Não foram encontrados Modelos que usam o recurso ");
		} else {
			return recModels;
		}
	}
	
	/**
	 * 
	 * Usa recModelDAO para recuperar lista de todos os recmodels. Retorna um List
	 * de recModels
	 * 
	 * @author dayse.arruda
	 * @throws AplicacaoException
	 */
	public List<RecModel> recuperaListaDeRecModels() throws AplicacaoException {
		
		List<RecModel> recModels = recModelDAO.recuperaListaDeRecModels();
		
		if (recModels.size() == 0) {
			throw new AplicacaoException("Não foram encontrados Modelos que usam o recurso ");
		} else {
			return recModels;
		}
	}
	
	/**
	 * Executa uma busca por todos os recmodels paginando o resultado
	 * baseado em familia
	 * 
	 * @return List<RecModel>
	 */
	public List<RecModel> recuperaListaPaginadaDeRecModels() {
		return recModelDAO.recuperaListaPaginadaDeRecModels();
	}
	
	/**
	 * Executa uma busca pelos recmodels de um determinado recurso,
	 * paginando o resultado
	 *
	 * 
	 * @return List<RecModel>
	 */
	public List<RecModel> recuperaListaPaginadaDeRecModelsPorRecurso(Recurso recurso) {
		return recModelDAO.recuperaListaPaginadaDeRecModelsPorRecurso();
	}


	public List<RecModel> recuperaListaDeRecModelsComModelos () {
		return recModelDAO.recuperaListaDeRecModelsComModelos();
	}


	public List<RecModel> recuperaListaDeRecModelsPorCodModeloLike(String codModelo) {
		return recModelDAO.recuperaListaDeRecModelsPorCodModeloLike(codModelo);
	}
	
	public RecModel recuperaRecModelPorCodModelo(String codModelo) throws AplicacaoException {

		RecModel recModelBD = null;

		try {
			recModelBD = recModelDAO.recuperaRecModelPorCodModelo(codModelo);
		} catch (ObjetoNaoEncontradoException exc) {
			throw new AplicacaoException("recmodel.NAO_ENCONTRADO");
		}

		return recModelBD;
	}
	
	public RecModel recuperaRecModelPorRecursoEModelo(Recurso  recurso, Modelo modelo) throws ObjetoNaoEncontradoException {
		return recModelDAO.recuperaRecModelPorRecursoEModelo(recurso, modelo);
	}

	public List<RecModel> recuperaListaDeRecModelsPorDescrModelo(String descrModelo) {
		return recModelDAO.recuperaListaDeRecModelsPorDescrModelo(descrModelo);
	}

	public List<RecModel> recuperaRecModelosPorRecurso(Recurso recurso){
		return recModelDAO.recuperaRecModelosPorRecurso(recurso);
	}
}
