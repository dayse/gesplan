 
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
 * As classes de servi�o � que possuem as regras de neg�cio. Fazem as cr�ticas
 * quando necess�rio e chamam as classes DAO para pegar as informa��es do BD.
 * 
 * S�o essas classes AppService que fazem o controle de transa�ao, ou seja quem
 * abre a transa�ao, "quem commita" atrav�s do interceptador de appservice. Aqui
 * defino se o metodo � transacional ou n�o e em fun�ao desta informa�ao o
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
			// O comando a seguir s� ser� usado caso haja a cria��o de um service.
			// Exemplo:
			// Um Service A tem dentro de si a chamada de um Service B, s� que o Service B tamb�m tem
			// uma chamada para o Service A, logo um service chamaria o outro sem parar causando assim um loop infinito.
			// Contudo, em termos de uso do sistemas esse erro n�o ocorreria de forma clara, 
			// pois a View seria carregada sem dados.
			// Para evitar que esse tipo de erro gere confus�es - como o usu�rio pensar que o banco foi perdido, por exemplo - 
			// utilizamos o comando System.exit(1) que interrompe a aplica��o, deixando explicita a ocorr�ncia do erro.
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
	
// A principio nao � para ter exclui ja que eh preciso ter sempre esse objeto la no BD	
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
