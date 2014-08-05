 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.Tecido;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;


public interface TecidoDAO extends DaoGenerico<Tecido, Long> {

	
	@RecuperaObjeto
	public Tecido recuperaTecido(Long id)   //Subtende-se que a recuperaçao sera pelo id
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public Tecido recuperaTecidoPorCodigo(String codRecurso)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public Tecido recuperaTecidoComListaDeTecModels(Tecido tecido)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaLista
	public List<Tecido> recuperaListaDeTecidosPeloCodigoLike(String codTecido);
	
	@RecuperaLista
	public List<Tecido> recuperaListaDeTecidosPorDescricao(String descricao);
	
	@RecuperaLista
	public List<Tecido> recuperaListaDeTecidos();
	
	@RecuperaLista
	public List<Tecido> recuperaListaDeTecidosComTecModels();
	
	@RecuperaLista
	public List<Tecido> recuperaListaDeTecidosQueTenhamApenasTecModels();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<Tecido> recuperaListaPaginadaDeTecidos();
	
	@RecuperaListaPaginada(tamanhoPagina = 1)
	public List<Tecido> recuperaListaPaginadaDeTecidosComListaDeTecModels();

}
