 
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

/**
 * @author felipe
 * 
 */
public interface PerioPAPVigDAO extends DaoGenerico<PerioPAPVig, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public PerioPAPVig recuperaPerioPAPVig(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public PerioPAPVig recuperaPerioPAPVigPorPeriodoPAP(int periodoPAP)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public PerioPAPVig recuperaPerioPAPVigComPerioPMVigsPorPeriodoPAP(int periodoPAP)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<PerioPAPVig> recuperaListaDePerioPAPVigs();

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<PerioPAPVig> recuperaListaPaginadaDePerioPAPVigs();
	
	@RecuperaLista
	public List<PerioPAPVig> recuperaIntervaloDePerioPAPVigs(int periodoInicial, int periodoFinal);

}
