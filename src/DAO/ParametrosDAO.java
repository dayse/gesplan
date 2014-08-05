 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;


import modelo.Parametros;
import DAO.anotacao.RecuperaObjeto;
import DAO.anotacao.RecuperaLista;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * 
 * 
 * @author felipe
 * 
 */
public interface ParametrosDAO extends DaoGenerico<Parametros, Long> {

	@RecuperaObjeto
	public Parametros recuperaParametrosPorId(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<Parametros> recuperaListaDeParametros();


}
