 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.CadPlan;
import modelo.ModelagemFuzzy;
import modelo.Usuario;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaObjeto;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.DaoGenerico;

public interface CadPlanDAO extends DaoGenerico<CadPlan, Long>
{
	@RecuperaObjeto
	public CadPlan recuperaCadPlanPorCodigo(String codPlan) throws ObjetoNaoEncontradoException;

	@RecuperaObjeto
	public CadPlan recuperaCadPlanComPlanosModelo(CadPlan cadPlan);

	@RecuperaLista
	public List<CadPlan> recuperaListaDeCadPlan();
	
	@RecuperaLista
	public List<CadPlan> recuperaListaDeCadPlanPorUsuario(Usuario usuario);	

	@RecuperaLista
	public List<CadPlan> recuperaListaDeCadPlanPorModelagemFuzzy(ModelagemFuzzy modelagemFuzzy);	
	
	@RecuperaObjeto
	public CadPlan recuperaCadPlanComPlanosModelo(String codPlan) throws ObjetoNaoEncontradoException;	
	
	@RecuperaLista
	public List recuperaCadPlanComDependencias(CadPlan cadPlan);
	
	@RecuperaObjeto
	public CadPlan recuperaCadPlanApenasComPlanosModelo(String codPlan) throws ObjetoNaoEncontradoException;	 
}
