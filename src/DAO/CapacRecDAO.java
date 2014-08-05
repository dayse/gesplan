 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;
import modelo.CapacRec;
import modelo.PerioPM;
import modelo.Recurso;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * @author felipe
 * 
 */

public interface CapacRecDAO extends DaoGenerico<CapacRec, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public CapacRec recuperaCapacRecComPerioPM(CapacRec CapacRec)
			throws ObjetoNaoEncontradoException;
	
	
	@RecuperaObjeto
	public CapacRec recuperaCapacRecPorRecursoEPerioPM(Recurso recurso, PerioPM perioPM)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<CapacRec> recuperaListaDeCapacRecs();

	@RecuperaLista
	public List<CapacRec> recuperaListaDeCapacRecsComPerioPMs();

	@RecuperaLista
	public List<CapacRec> recuperaListaDeCapacRecsComRecursosEPerioPMs();
	
	
	@RecuperaLista
	public List<CapacRec> recuperaListaDeCapacRecsPorRecurso(Recurso recurso);
	
	
	
}
