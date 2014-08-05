 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.Modelo;
import modelo.TecModel;
import modelo.Tecido;
import service.anotacao.Transacional;
import service.exception.AplicacaoException;
import DAO.ModeloDAO;
import DAO.TecModelDAO;
import DAO.TecidoDAO;
import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.TecModelDAOImpl;
import DAO.Impl.TecidoDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * 
 * TecModelAppService é uma classe de serviço que possui as regras de negócio para manipular inicialmente
 * a entidade TecModel. Estas manipulações incluem quando necessário chamadas as interfaces DAOs,
 * outras classes de serviço e acessos a informações do BD.
 * 
 * A classe TecModelAppService fora criada para atender ao Padrão MVC, Model Vision Control , sendo a mesma uma
 * classe de serviço que é capaz de efetuar: controle de transação, ou seja esta classe possui o recurso de
 * abrir transaçao, commitar e fechar uma transaçao através de um interceptador de serviço.
 * Neste interceptador será definido se o método é transacional ou não e em função desta informação
 * o interceptador irá usar ou não uma transação.
 * 
 * @author marques.araujo
 * 
 */


public class TecModelAppService {
	
	// DAOs
	private static TecModelDAO tecModelDAO;
	private static ModeloDAO modeloDAO;
	private static TecidoDAO tecidoDAO;

	@SuppressWarnings("unchecked")
	public TecModelAppService() {
		try {
			// DAOs
			tecModelDAO = FabricaDeDao.getDao(TecModelDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			tecidoDAO = FabricaDeDao.getDao(TecidoDAOImpl.class);
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
	 * Inclui TecModel, ou seja, inclui modelo na lista de modelos que usam
	 * aquele recurso em particular
	 * Verifica antes se este modelo já havia sido incluido na lista de 
	 * TecModel deste recurso
	 * 
	 * @param TecModel
	 * @throws AplicacaoException
	 */
	@Transacional
	public long inclui(TecModel tecModel) 
		throws AplicacaoException
	{
		long retorno = -1;
		TecModel tecModelBD = null;
		// verifica se existe o tecido
		try
		{	tecidoDAO.getPorIdComLock(tecModel.getTecido().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("tecido.NAO_ENCONTRADO");
		}
		
		// verifica se existe o modelo
		try
		{	modeloDAO.getPorIdComLock(tecModel.getModelo().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}
		
		// verifica se o tecido já é usado neste modelo 
		try {
			 tecModelBD = tecModelDAO.recuperaTecModelPorTecidoEModelo(tecModel.getTecido(), tecModel.getModelo());
			throw new AplicacaoException("recModel.ENCONTRADO_MODELO");
		} catch (ObjetoNaoEncontradoException ob) {
			//Recurso e modelo foram setados em RecModelActions no Inclui
			retorno = tecModelDAO.inclui(tecModel).getId();

		}
		return retorno;
	}	
	

	@Transacional
	public void altera(TecModel tecModel) {
		tecModelDAO.altera(tecModel);
	}
	/**
	 * baseado em modelo - usei lock - verificar se é para ser desse jeito
	 * @param umTecModel
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(TecModel umTecModel) throws AplicacaoException {
		
		TecModel tecModel = null;
		
		try {
			tecModel = tecModelDAO.getPorIdComLock((umTecModel.getId()));
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("tecModel.NAO_ENCONTRADO");
		}

		tecModelDAO.exclui(tecModel);
	}

	/**
	 * Usa um método do DAO para recuperar um TecModel juntamente com o seu modelo
	 * @author marques.araujo
	 * @param TecModel
	 * @return TecModel
	 * @throws AplicacaoException
	 */
	public TecModel recuperaTecModelComModelo(TecModel tecModel) throws AplicacaoException {
		
		try {
			return tecModelDAO.recuperaTecModelComModelo(tecModel);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("TecModel.NAO_ENCONTRADO");
		}
	}
	
	public List<TecModel> recuperaListaDeTecModelsComRecursoComModelos() throws AplicacaoException{
		
		List<TecModel> tecModels = tecModelDAO.recuperaListaDeTecModelsComTecidoComModelos();

		if (tecModels.size() == 0) {
			throw new AplicacaoException("Não foram encontrados Modelos que usam o Tecido ");
		} else {
			return tecModels;
		}
	}
	
	/**
	 * 
	 * Usa TecModelDAO para recuperar lista de todos os TecModels. Retorna um List
	 * de TecModels
	 * @author marques.araujo
	 * @return List<TecModel>
	 * @throws AplicacaoException
	 */
	public List<TecModel> recuperaListaDeTecModels() throws AplicacaoException {
		
		List<TecModel> tecModels = tecModelDAO.recuperaListaDeTecModels();
		
		if (tecModels.size() == 0) {
			throw new AplicacaoException("Não foram encontrados Modelos que usam o Tecido ");
		} else {
			return tecModels;
		}
	}
	
	/**
	 * Executa uma busca por todos os TecModels paginando o resultado
	 * baseado em familia
	 * 
	 * @return List<TecModel>
	 */
	public List<TecModel> recuperaListaPaginadaDeTecModels() {
		return tecModelDAO.recuperaListaPaginadaDeTecModels();
	}
	
	/**
	 * Executa uma busca pelos TecModels de um determinado Tecido,
	 * paginando o resultado
	 *
	 * 
	 * @return List<TecModel>
	 */
	public List<TecModel> recuperaListaPaginadaDeTecModelsPorTecido() {
//		System.out.println("==============metodo recuperaListaPaginadaDeTecModelsPorTecido()=========");
		return tecModelDAO.recuperaListaPaginadaDeTecModelsPorTecido();
	}


	public List<TecModel> recuperaListaDeTecModelsComModelos () {
		return tecModelDAO.recuperaListaDeTecModelsComModelos();
	}

	
	/**
	 * Este método é responsável por retornar um TecModel a partir do seu respectivo Modelo.
	 * @author marques.araujo
	 * @param  String
	 * @return TecModel
	 * @throws AplicacaoException
	 * 
	 */	
	public TecModel recuperaTecModelPorCodModelo(String codModelo) throws AplicacaoException {

		TecModel tecModelBD = null;

		try {
			tecModelBD = tecModelDAO.recuperaTecModelPorCodModelo(codModelo);
		} catch (ObjetoNaoEncontradoException exc) {
			throw new AplicacaoException("TecModel.NAO_ENCONTRADO");
		}

		return tecModelBD;
	}

	public List<TecModel> recuperaTecModelPorDescrModelo(String descrModelo) {
		return tecModelDAO.recuperaTecModelPorDescrModelo(descrModelo);
	}


	public TecModel recuperaTecModelPorTecidoEModelo(Tecido  tecido, Modelo modelo) throws ObjetoNaoEncontradoException{
		return tecModelDAO.recuperaTecModelPorTecidoEModelo(tecido, modelo);
	}
	

}
