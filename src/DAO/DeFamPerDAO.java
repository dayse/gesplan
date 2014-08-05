 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.DeFamPer;
import modelo.Familia;
import modelo.PerioPAP;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;


public interface DeFamPerDAO extends DaoGenerico<DeFamPer, Long>{
	
	@RecuperaObjeto
	DeFamPer recuperaDeFamPerPorPerioPAPEFamilia(PerioPAP perioPAP, Familia familia) throws ObjetoNaoEncontradoException; 
	
	@RecuperaLista
	List<DeFamPer> recuperaListaDeDemandaFamiliaPerioPAP();

	@RecuperaLista
	List<DeFamPer> recuperaListaDeDeFamPerPorFamilia(Familia familia);
	
	@RecuperaLista
	List<DeFamPer> recuperaListaDeFamiliasComModelosEPeriodos();
		
}
