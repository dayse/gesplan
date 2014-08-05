 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import modelo.PlanoModelo;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface PlanoModeloDAO extends DaoGenerico<PlanoModelo, Long>{

	@RecuperaObjeto
	public PlanoModelo recuperarPlPerModsPorPlanoModelo(PlanoModelo planoModelo) throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public PlanoModelo recuperarPlanoModeloPorCodigoModeloECodigoCadPlan(String codPlan, String codModelo) throws ObjetoNaoEncontradoException;
	
}
