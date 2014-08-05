 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;
import modelo.CapacDia;
import modelo.Familia;
import modelo.PerioPM;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * @author felipe
 * 
 */

public interface CapacDiaDAO extends DaoGenerico<CapacDia, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public CapacDia recuperaCapacDiaPorPerioPM(PerioPM perioPM)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<CapacDia> recuperaListaDeCapacDias();
	
	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<CapacDia> recuperaListaPaginadaDeCapacDias();
	


}
