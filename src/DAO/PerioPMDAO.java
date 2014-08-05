 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.PerioPAP;
import modelo.PerioPM;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface PerioPMDAO extends DaoGenerico<PerioPM, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public PerioPM recuperaPerioPM(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public PerioPM recuperaPerioPMPorPeriodoPM(int periodoPM)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<PerioPM> recuperaListaDePerioPMs();
	
	@RecuperaLista
	public List<PerioPM> recuperaListaDePerioPMsComPerioPAP();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<PerioPM> recuperaListaPaginadaDePerioPMs();
	
	@RecuperaObjeto
	public PerioPM recuperaPerioPMComDeModPers(PerioPM perioPM) throws ObjetoNaoEncontradoException;
	
	/**
	 * Esta retornando uma lista ordenada por periodoPM
	 * @param periodoInicial
	 * @param periodoFinal
	 * @return
	 */
	@RecuperaLista
	public List<PerioPM> recuperaIntervaloDePerioPMs(int periodoInicial, int periodoFinal);
	
	/**
	 *  Atencao, o perioPAP precisa estar persistido no banco antes de usar isso.
	 * @param perioPAP
	 * @return
	 */
	@RecuperaLista
	public List<PerioPM> recuperaListaDePerioPMsPorPerioPAP(PerioPAP perioPAP);
	
}
