 
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

import modelo.Parametros;
import DAO.ParametrosDAO;
import DAO.Impl.ParametrosDAOImpl;
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
public class ParametrosAppService {
	
	// DAOs
	private static ParametrosDAO parametrosDAO;

	public ParametrosAppService() {
		try {
			
			// DAOs
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			
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
	 * cria um objeto do tipo Parametros
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(Parametros parametros) {
		parametrosDAO.inclui(parametros);
	}
	
	/**
	 * Altera um objeto do tipo Parametros
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void altera(Parametros parametros) {
		parametrosDAO.altera(parametros);
	}
	
// A principio nao é para ter exclui ja que eh preciso ter sempre esse objeto la no BD	
//
//	/**
//	 * Exclui um objeto do tipo Parametros
//	 * 
//	 * @throws AplicacaoException
//	 */
//	@Transacional
//	public void exclui(Parametros parametros) throws AplicacaoException {
//
//		try {
//			parametros = parametrosDAO.recuperaParametrosPorId(parametros.getId());
//		} catch (ObjetoNaoEncontradoException e) {
//			throw new AplicacaoException("parametros.NAO_ENCONTRADO");
//		}
//
//		parametrosDAO.exclui(parametros);
//	}



	
	/**
	 * Executa uma busca por um unico conjunto de parametros pelo Id
	 * 
	 * @return Parametros
	 */
	public Parametros recuperaParametrosPorId(long id) {
		Parametros parametros = null;
		try{
			parametros = parametrosDAO.recuperaParametrosPorId(id);
		}catch(ObjetoNaoEncontradoException e){
			
		}
		return parametros;
	}

	/**
	 * Executa uma busca por todos os parametros
	 * 
	 * @return List<Parametros>
	 */
	public List<Parametros> recuperaListaDeParametros() {
		return parametrosDAO.recuperaListaDeParametros();
	}

}
