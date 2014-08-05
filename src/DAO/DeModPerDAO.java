 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.DeModPer;
import modelo.Modelo;
import modelo.PerioPM;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface DeModPerDAO extends DaoGenerico<DeModPer, Long>{

	@RecuperaObjeto
	public DeModPer recuperaDeModPerPorPeriodoEModelo(PerioPM periodo, Modelo modelo) throws ObjetoNaoEncontradoException; 
	
	@RecuperaLista
	public List<DeModPer> recuperaListaDeDemandaModeloPeriodo();

	@RecuperaLista
	public List<DeModPer> recuperaListaDeDeModPerPorModelo(Modelo modelo);
	
	@RecuperaLista
	public List<DeModPer> recuperaListaDeModelosComFamiliasEPeriodos();
}
