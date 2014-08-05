 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.Usuario;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface UsuarioDAO extends DaoGenerico<Usuario, Long>
{

	@RecuperaObjeto
	public Usuario recuperaPorLoginESenha(String login, String senha) throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public Usuario recuperaPorLogin(String login) throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<Usuario> recuperaListaDeUsuarios();
	
	@RecuperaLista
	public List<Usuario> recuperaListaDeUsuariosComTipo();

	@RecuperaListaPaginada
	public List<Usuario> recuperaListaPaginadaPorNome(String nome);

	@RecuperaObjeto
	public Usuario recuperaComPlanos(Usuario usuario) throws ObjetoNaoEncontradoException;	
}
