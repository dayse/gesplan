 
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

public interface FamiliaDAO extends DaoGenerico<Familia, Long> {

	@RecuperaObjeto
	public Familia recuperaUmaFamiliaEModelos(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public Familia recuperaUmaFamiliaPeloCodigo(String codFamilia)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public Familia recuperaUmaFamiliaApartirDoModelo(Modelo modelo)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaLista
	public List<Familia> recuperaListaDeFamiliasPeloCodigoLike(String codFamilia);
	
	@RecuperaLista
	public List<Familia> recuperaListaDeFamiliasPorDescricao(String descricao);
			
	@RecuperaLista
	public List<Familia> recuperaListaDeFamilias();
	
	@RecuperaLista
	public List<Familia> recuperaListaDeFamiliasComModelos();
	
	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<Familia> recuperaListaPaginadaDeFamilias();
	
	@RecuperaLista
	public List<Familia> recuperaListaDeFamiliasComDeFamPers();
	
	
	
}
