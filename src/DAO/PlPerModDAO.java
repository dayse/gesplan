 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;
import java.util.Set;

import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface PlPerModDAO extends DaoGenerico<PlPerMod, Long> {
	
	@RecuperaObjeto
	public PlPerMod recuperaPlPerModPorPlanoModeloEPerioPM(PlanoModelo planoModelo, PerioPM perioPM) throws ObjetoNaoEncontradoException;
	
	
	@RecuperaLista
	public List<PlPerMod> recuperaListaDePlPerMod();
	
	@RecuperaLista
	public List<PlPerMod> recuperaListaDePlPerModPorPlanoModelo(PlanoModelo planoModelo);

	@RecuperaLista
	public List<PlPerMod> recuperaListaDePlPerModPorPlanoModeloEPerioPMApartirDePerioPM(PlanoModelo planoModelo, int periodoPM);
	
	@RecuperaLista
	public List<PlPerMod> recuperaIntervaloDePlPerModPorPlanoModeloEIntervaloDePerioPM(PlanoModelo planoModelo, int periodoPMInicial, int periodoPMFinal);
}
