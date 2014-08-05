 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.Modelo;
import modelo.RecModel;
import modelo.Recurso;

import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * verificar se tem busca faltando ou sobrando
 *
 * @author felipe
 * 
 */
public interface RecModelDAO extends DaoGenerico<RecModel, Long> {


	
	@RecuperaObjeto
	public RecModel recuperaRecModelComModelo(RecModel recModel) throws ObjetoNaoEncontradoException;


	@RecuperaObjeto
	public RecModel recuperaRecModel(Long id)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public RecModel recuperaRecModelPorCodModelo(String codModelo)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public RecModel recuperaRecModelPorRecursoEModelo(Recurso  recurso, Modelo modelo) throws ObjetoNaoEncontradoException;
	
	@RecuperaLista
	public List<RecModel> recuperaListaDeRecModelsPorCodModeloLike(String codModelo);
	
	
	@RecuperaLista
	public List<RecModel> recuperaListaDeRecModelsComModelos();
	
	@RecuperaLista
	public List<RecModel> recuperaListaDeRecModelsComRecursoComModelos();

	@RecuperaLista
	public List<RecModel> recuperaListaDeRecModelsPorDescrModelo(String descrModelo);

	@RecuperaLista
	public List<RecModel> recuperaListaDeRecModels();
	
	@RecuperaLista
	public List<RecModel> recuperaRecModelosPorRecurso(Recurso recurso);


	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<RecModel> recuperaListaPaginadaDeRecModels();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<RecModel> recuperaListaPaginadaDeRecModelsPorRecurso();

}
