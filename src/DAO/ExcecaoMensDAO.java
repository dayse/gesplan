 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.Excecao;
import modelo.ExcecaoMens;
import modelo.PerioPM;
import modelo.PlanoModelo;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

/**
 * DAO relativo a entidade excecaoMens
 * 
 * @author felipe.arruda
 * 
 */
public interface ExcecaoMensDAO extends DaoGenerico<ExcecaoMens, Long> {

	@RecuperaObjeto
	public ExcecaoMens recuperaExcecaoMens(long id)
			throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public ExcecaoMens recuperaExcecaoMensPorPlanoModeloEExcecaoEPeriodoOrigemEPeriodoDestino(PlanoModelo planoModelo, Excecao excecao,
																							PerioPM periodoOrigem, PerioPM periodoDestino)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<ExcecaoMens> recuperaListaDeExcecaoMens();	

	@RecuperaLista
	public List<ExcecaoMens> recuperaListaDeExcecaoMensPorExcecao(Excecao excecao);	

	@RecuperaLista
	public List<ExcecaoMens> recuperaListaDeExcecaoMensPorPlanoModelo(PlanoModelo planoModelo);	

	@RecuperaLista
	public List<ExcecaoMens> recuperaListaDeExcecaoMensPorPlanoModeloEExcecao(PlanoModelo planoModelo,Excecao excecao);	
	
	@RecuperaLista
	public List<ExcecaoMens> recuperaListaDeExcecaoMensPorPlanoModeloEExcecaoEPeriodoOrigem(PlanoModelo planoModelo, Excecao excecao,
			PerioPM periodoOrigem);
	
	
	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<ExcecaoMens> recuperaListaPaginadaDeExcecaoMens();
	
	@RecuperaLista
	public List<ExcecaoMens> recuperaListaPaginadaDeExcecaoMensCount();
	
	
	
}
