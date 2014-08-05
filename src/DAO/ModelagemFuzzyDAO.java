 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.ModelagemFuzzy;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface ModelagemFuzzyDAO extends DaoGenerico<ModelagemFuzzy, Long> {


	@RecuperaObjeto
	public ModelagemFuzzy recuperaModelagemFuzzy(Long id)
			throws ObjetoNaoEncontradoException;
	
	@RecuperaObjeto
	public ModelagemFuzzy recuperaModelagemFuzzyPeloNome(String nomeModelagemFuzzy)
			throws ObjetoNaoEncontradoException;

	@RecuperaLista
	public List<ModelagemFuzzy> recuperaListaDeModelagemFuzzys();
	
	@RecuperaLista
	public List<ModelagemFuzzy> recuperaListaDeModelagemFuzzysPorFinalidade(String finalidadeModelagem);
	

	@RecuperaListaPaginada(tamanhoPagina = 10)
	public List<ModelagemFuzzy> recuperaListaPaginadaDeModelagemFuzzys();
	

}
