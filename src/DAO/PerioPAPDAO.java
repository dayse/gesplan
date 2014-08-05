 
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

/**
 * @author felipe
 * 
 */
public interface PerioPAPDAO extends DaoGenerico<PerioPAP, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public PerioPAP recuperaPerioPAP(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public PerioPAP recuperaPerioPAPPorPeriodoPAP(int periodoPAP)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public PerioPAP recuperaPerioPAPComPerioPMsPorPeriodoPAP(int periodoPAP)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<PerioPAP> recuperaListaDePerioPAPs();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<PerioPAP> recuperaListaPaginadaDePerioPAPs();
	
	@RecuperaLista
	public List<PerioPAP> recuperaIntervaloDePerioPAPs(int periodoInicial, int periodoFinal);

}
