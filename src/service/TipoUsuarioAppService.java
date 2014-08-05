 
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
 * TipoUsuarioAppService é uma classe de serviço que possui as regras de negócio para manipular inicialmente
 * a entidade TipoUsuario. Estas manipulações incluem quando necessário chamadas as interfaces DAOs e
 * acessos a informações do BD.
 * 
 * A classe TipoUsuarioAppService fora criada para atender ao Padrão MVC, Model Vision Control , sendo a mesma uma
 * classe de serviço que é capaz de efetuar: controle de transação, ou seja esta classe possui o recurso de
 * abrir transaçao, commitar e fechar uma transaçao através de um interceptador de serviço.
 * Neste interceptador será definido se o método é transacional ou não e em função desta informação
 * o interceptador irá usar ou não uma transação.
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
