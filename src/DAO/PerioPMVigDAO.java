 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.PerioPAPVig;
import modelo.PerioPMVig;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface PerioPMVigDAO extends DaoGenerico<PerioPMVig, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public PerioPMVig recuperaPerioPMVig(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public PerioPMVig recuperaPerioPMVigPorPeriodoPM(int periodoPM)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<PerioPMVig> recuperaListaDePerioPMVigs();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<PerioPMVig> recuperaListaPaginadaDePerioPMVigs();
	
	/**
	 * Esta retornando uma lista ordenada por periodoPM relativos ao intervalo de
	 * periodoPMs definidos nos parametros
	 * @param periodoInicial
	 * @param periodoFinal
	 * @return
	 */
	@RecuperaLista
	public List<PerioPMVig> recuperaIntervaloDePerioPMVigs(int periodoInicial, int periodoFinal);
	
	/**
	 *  Atencao, o perioPAP precisa estar persistido no banco antes de usar isso.
	 * @param perioPAP
	 * @return
	 */
	@RecuperaLista
	public List<PerioPMVig> recuperaListaDePerioPMVigsPorPerioPAPVig(PerioPAPVig perioPAPVig);
	
}
