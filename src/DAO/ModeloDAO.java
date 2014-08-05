 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;



import modelo.Familia;
import modelo.Modelo;

import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface ModeloDAO extends DaoGenerico<Modelo, Long>
{	
	@RecuperaLista
	public List<Modelo> recuperaListaDeModelos();

	@RecuperaObjeto
	public Modelo recuperaUmModeloComFamilia(Modelo modelo) throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public Modelo recuperaModeloPorCodigo(String codigoModelo) throws ObjetoNaoEncontradoException;
	
	@RecuperaLista
	public List<Modelo> recuperaModeloPorDescricao(String descricaoModelo);

	@RecuperaLista
	public List<Modelo> recuperaListaDeModelosComFamilias();
	
	@RecuperaLista
	public List<Modelo> recuperaListaDeModelosPorFamilia(Familia familia);

	@RecuperaLista
	public List<Modelo> recuperaModeloPorCodigoLike(String codigoModelo);

	@RecuperaListaPaginada(tamanhoPagina=10)
	public List<Modelo> recuperaListaPaginadaDeModelosComFamilias();

	@RecuperaListaPaginada(tamanhoPagina=10)
	public List<Modelo> recuperaListaDeModelosComFamiliasEPeriodos();
	
	@RecuperaListaPaginada(tamanhoPagina=10)
	public List<Modelo> recuperaListaPaginadaDeModelosComFamiliaComListaDePMPs();

	@RecuperaObjeto
	public Modelo recuperaModeloComFamiliaEPeriodos(Modelo modelo) throws ObjetoNaoEncontradoException;
}
