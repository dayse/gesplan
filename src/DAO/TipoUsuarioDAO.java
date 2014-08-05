 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.TipoUsuario;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface TipoUsuarioDAO extends DaoGenerico<TipoUsuario, Long>
{

	@RecuperaObjeto
	public TipoUsuario recuperaTipoUsuarioPorTipo(String tipoUsuario) throws ObjetoNaoEncontradoException;
	
	@RecuperaLista
	public List<TipoUsuario> recuperaListaDeTipoUsuario();
}
