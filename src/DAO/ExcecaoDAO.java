 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.Excecao;

import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * DAO relativo a entidade excecao
 * 
 * @author felipe.arruda
 * 
 */
public interface ExcecaoDAO extends DaoGenerico<Excecao, Long> {

	@RecuperaObjeto
	public Excecao recuperaExcecao(long id) throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public Excecao recuperaExcecaoPeloTipoDeExcecao(String tipoDeExcecao) throws ObjetoNaoEncontradoException;
	
	
	@RecuperaLista
	public List<Excecao> recuperaListaDeExcecoes();	
	
	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<Excecao> recuperaListaPaginadaDeExcecoes();
	
	@RecuperaLista
	public List<Excecao> recuperaListaPaginadaDeExcecoesCount();
	
	
	
}
