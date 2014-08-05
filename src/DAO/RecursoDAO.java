 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.Recurso;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface RecursoDAO extends DaoGenerico<Recurso, Long> {


	@RecuperaObjeto
	public Recurso recuperaRecurso(Long id)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public Recurso recuperaRecursoPeloCodigo(String codRecurso)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaLista
	public List<Recurso> recuperaListaDeRecursosPeloCodigoLike(String codRecurso);

	@RecuperaLista
	public List<Recurso> recuperaListaDeRecursosPorDescricao(String descricao);

	@RecuperaLista
	public List<Recurso> recuperaListaDeRecursos();

	@RecuperaLista
	public List<Recurso> recuperaListaDeRecursosComRecModels();
	
	@RecuperaLista
	public List<Recurso> recuperaListaDeRecursosQueTenhamApenasRecModels();
	
	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<Recurso> recuperaListaPaginadaDeRecursos();
	

	@RecuperaObjeto
	public Recurso recuperaRecursoComListaDeRecModels(Recurso recurso) throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public Recurso recuperaRecursoComListaDeCapacRecs(Recurso recurso) throws ObjetoNaoEncontradoException;

	@RecuperaListaPaginada(tamanhoPagina = 1)
	public List<Recurso> recuperaListaPaginadaDeRecursosComListaDeRecModels();

	@RecuperaListaPaginada(tamanhoPagina = 1)
	public List<Recurso> recuperaListaPaginadaDeRecursosComListaDeCapacRecs();
}
