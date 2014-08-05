 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;
import modelo.PMP;
import modelo.PerioPMVig;
import modelo.Modelo;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * @author felipe
 * 
 */

public interface PMPDAO extends DaoGenerico<PMP, Long> {

	/*     ****** Métodos Genéricos ******* */

	@RecuperaObjeto
	public PMP recuperaPMPComPerioPMVig(PMP pMP)
			throws ObjetoNaoEncontradoException;
	
	
	@RecuperaObjeto
	public PMP recuperaPMPPorModeloEPerioPMVig(Modelo modelo, PerioPMVig perioPMVig)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<PMP> recuperaListaDePMPs();
	
	@RecuperaLista
	public List<PMP> recuperaListaDePMPsComPerioPMVigs();

	@RecuperaLista
	public List<PMP> recuperaListaDePMPsPorModeloComPerioPMVigs(Modelo modelo);	
	

	@RecuperaLista
	public List<PMP> recuperaIntervaloDePMPPorModeloEIntervaloDePerioPMVig(Modelo modelo,int periodoPMInic, int periodoPMFinal);
	
	
	
}
