 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.TipoUsuario;
import service.anotacao.Transacional;
import service.exception.AplicacaoException;
import DAO.TipoUsuarioDAO;
import DAO.Impl.TipoUsuarioDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * 
 * TipoUsuarioAppService � uma classe de servi�o que possui as regras de neg�cio para manipular inicialmente
 * a entidade TipoUsuario. Estas manipula��es incluem quando necess�rio chamadas as interfaces DAOs e
 * acessos a informa��es do BD.
 * 
 * A classe TipoUsuarioAppService fora criada para atender ao Padr�o MVC, Model Vision Control , sendo a mesma uma
 * classe de servi�o que � capaz de efetuar: controle de transa��o, ou seja esta classe possui o recurso de
 * abrir transa�ao, commitar e fechar uma transa�ao atrav�s de um interceptador de servi�o.
 * Neste interceptador ser� definido se o m�todo � transacional ou n�o e em fun��o desta informa��o
 * o interceptador ir� usar ou n�o uma transa��o.
 * 
 * @author marques.araujo
 * 
 */

public class TipoUsuarioAppService {
	// DAOs
	private TipoUsuarioDAO tipoUsuarioDAO;

	public TipoUsuarioAppService() {
		try {
			// DAOs
			tipoUsuarioDAO = FabricaDeDao.getDao(TipoUsuarioDAOImpl.class);
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
	
	@Transacional
	public void inclui(TipoUsuario tipoUsuario) {
		try {
			tipoUsuarioDAO.recuperaTipoUsuarioPorTipo(tipoUsuario.getTipoUsuario());
		} catch (ObjetoNaoEncontradoException e) {
			tipoUsuarioDAO.inclui(tipoUsuario);
		}

	}
	
	public TipoUsuario recuperaTipoUsuarioAdministrador() throws AplicacaoException {
		try {
			return tipoUsuarioDAO.recuperaTipoUsuarioPorTipo(TipoUsuario.ADMINISTRADOR);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("tipoUsuario.NAO_ENCONTRADO");
		}
	}
	
	public List<TipoUsuario> recuperaListaDeTipoUsuario(){
		return tipoUsuarioDAO.recuperaListaDeTipoUsuario();
	}
}
