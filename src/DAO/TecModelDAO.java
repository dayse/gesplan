 
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
import modelo.TecModel;
import modelo.Tecido;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface TecModelDAO extends DaoGenerico<TecModel, Long> {
	
	@RecuperaObjeto
	public TecModel recuperaTecModel(Long id) throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public TecModel recuperaTecModelPorCodModelo(String codModelo) throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	TecModel recuperaTecModelComModelo(TecModel TecModel) throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<TecModel> recuperaListaDeTecModelsComModelos();
	
	@RecuperaLista
	public List<TecModel> recuperaListaDeTecModelsComTecidoComModelos();

	@RecuperaLista
	public List<TecModel> recuperaTecModelPorDescrModelo(String descrModelo);

	@RecuperaLista
	public List<TecModel> recuperaListaDeTecModels();

	@RecuperaObjeto
	public TecModel recuperaTecModelPorTecidoEModelo(Tecido  tecido, Modelo modelo) throws ObjetoNaoEncontradoException;
	
	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<TecModel> recuperaListaPaginadaDeTecModels();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<TecModel> recuperaListaPaginadaDeTecModelsPorTecido();

}
