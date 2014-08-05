 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import modelo.CapacRec;
import modelo.HP;
import modelo.PerioPM;
import modelo.RecModel;
import modelo.Recurso;
import modelo.RecursoRelatorio;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.HPDAO;
import DAO.PerioPMDAO;
import DAO.RecursoDAO;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.RecursoDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import exception.relatorio.RelatorioException;

public class RecursoAppService {
	
	// DAOs
	private static RecursoDAO recursoDAO;
	private static PerioPMDAO perioPMDAO;
	private static HPDAO hpDAO;
	
	// Services
	private static CapacRecAppService capacRecService;
	private static RecModelAppService recModelService;
	
	public RecursoAppService() { 
		try {
			// DAOs
			recursoDAO = FabricaDeDao.getDao(RecursoDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			
			//Services
			recModelService = FabricaDeAppService.getAppService(RecModelAppService.class);
			capacRecService = FabricaDeAppService.getAppService(CapacRecAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	/**
	 * Inclui um objeto do tipo Recurso sem nenhuma critica.
	 * 
	 * @return long
	 * @throws AplicacaoException
	 */
	@Transacional
	public long inclui(Recurso recurso) throws AplicacaoException {
		
		long retorno = -1;
		
		try {
			recursoDAO.recuperaRecursoPeloCodigo(recurso.getCodRecurso());
			throw new AplicacaoException("recurso.CODIGO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {			
			retorno = recursoDAO.inclui(recurso).getId();
		}
		
		
		return retorno;

	}
	
	/**
	 * Inclui um objeto do tipo Recurso junto com suas criticas
	 * 
	 * @return long
	 * @throws AplicacaoException
	 */
	@Transacional
	public long incluiComCriticas(Recurso recurso) throws AplicacaoException {
		
		long retorno = -1;
		
		try {
			recursoDAO.recuperaRecursoPeloCodigo(recurso.getCodRecurso());
			throw new AplicacaoException("recurso.CODIGO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {			
			retorno = this.inclui(recurso);
		}
		
		
		//Regras de inclusao de capacRec
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);

		// Obtem lista com perioPMs dentro do HP cadastrado para o Plano Mestre
		List<PerioPM> listaPerioPMsHP = perioPMDAO.recuperaIntervaloDePerioPMs(
				hpBD.getPerioPMInicPMP().getPeriodoPM(), hpBD.getPerioPMFinalPMP()
						.getPeriodoPM());
		
		for (PerioPM periodo : listaPerioPMsHP) {
			CapacRec capacRec = new CapacRec();
			capacRec.setRecurso(recurso);
			capacRec.setPerioPM(periodo);

			capacRecService.inclui(capacRec);
		}

		return retorno;

	}
	@Transacional
	public void altera(Recurso umRecurso) {
		recursoDAO.altera(umRecurso);
	}

	/**
	 * Quando excluir Recurso, exclui em cascata os recModels associados,
	 * devido as regras da relação estabelecidas no Recurso.java 
	 * 
	 * @author felipe
	 */
	@Transacional
	public void exclui(Recurso umRecurso) 
		throws AplicacaoException 
	{
		Recurso recurso;
		try
		{	recurso = recursoDAO.getPorIdComLock((umRecurso.getId()));
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}

		recursoDAO.exclui(recurso);
	}

	public Recurso recuperaRecurso(long id)
			throws AplicacaoException {
		try {
			return recursoDAO.getPorId(id);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}
	}

	public Recurso recuperaRecursoPeloCodigo(String codRecurso)
																	
			throws AplicacaoException {
		try {
			return recursoDAO.recuperaRecursoPeloCodigo(codRecurso);
		} catch (ObjetoNaoEncontradoException e) {

			throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}

	}

	/**
	 * 
	 * Recupera a lista de todos os recursos que possuam código aproximado com o passado por parametro. 
	 * 
	 */
	public List<Recurso> recuperaListaDeRecursosPeloCodigoLike(String codRecurso){
		return recursoDAO.recuperaListaDeRecursosPeloCodigoLike(codRecurso);
	}
	
	public List<Recurso> recuperaListaDeRecursos() throws AplicacaoException 
	{	
		List<Recurso> recursos = recursoDAO.recuperaListaDeRecursos();
		if (recursos.size() == 0) 
		{	throw new AplicacaoException("recurso.RECURSOS_INEXISTENTES");
		} 
		else 
		{	return recursos;
		}
	}
	
	
	/**
	 * @author marques.araujo
	 * @return List<Recurso>
	 */
	public List<Recurso> recuperaListaDeRecursosQueTenhamApenasRecModels(){
		
		return recursoDAO.recuperaListaDeRecursosQueTenhamApenasRecModels();
		
	}
	
	/**
	 * @author Felipe
	 * @param String
	 * @return Uma lista de recursos com esta descricao
	 */
	public List<Recurso> recuperaListaDeRecursosPorDescricao(String descrRecurso) {
		return recursoDAO.recuperaListaDeRecursosPorDescricao(descrRecurso);
	}

	public List<Recurso> getRecursos(){
		return recursoDAO.recuperaListaDeRecursos();
	}

	public List<Recurso> recuperaListaPaginadaDeRecursos() {
		return recursoDAO.recuperaListaPaginadaDeRecursos();
	}
	
	public Recurso recuperaRecursoComListaDeRecModels(Recurso recurso) throws AplicacaoException {
		try {
			return recursoDAO.recuperaRecursoComListaDeRecModels(recurso);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}
		
	}

	public Recurso recuperaRecursoComListaDeCapacRecs(Recurso recurso) throws AplicacaoException {
		try {
			return recursoDAO.recuperaRecursoComListaDeCapacRecs(recurso);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("recurso.NAO_ENCONTRADO");
		}
		
	}

	public List<Recurso> recuperaListaPaginadaDeRecursosComListaDeRecModels() {
		
		return recursoDAO.recuperaListaPaginadaDeRecursosComListaDeRecModels();
	}

	public List<Recurso> recuperaListaPaginadaDeRecursosComListaDeCapacRecs() {
		
		return recursoDAO.recuperaListaPaginadaDeRecursosComListaDeCapacRecs();
	}
	
	
	/**
	 *  
	 * @author marques.araujoe
	 * @return List<Recurso>
	 *  
	 */
	public List<Recurso> recuperaListaDeRecursosComRecModels(){
		
		return recursoDAO.recuperaListaDeRecursosComRecModels();
		
	}
	
	/**
	 * Monta e Imprime Relatorio de Recursos em PDF a partir da listaDeRecursos informada.
	 * Chama o metodo gerarRelatorio de Relatorio.
	 * @author dayse
	 * 
	 * @param listaDeRecursos
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorio(List<Recurso> listaDeRecursos) throws AplicacaoException {
		
		System.out.println("Antes do metodo getRelatorio dentro de gerarRelatorio de RecursoAppService");

		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_RECURSOS);
				
		System.out.println("Depois do metodo getRelatorio dentro de gerarRelatorio  de RecursoAppService");
		
		try{
			relatorio.gerarRelatorio(listaDeRecursos, new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("recurso.RELATORIO_NAO_GERADO");
		}
	}
	
	/**
	 * 
	 * @author marques.araujo
	 * @param List<Recurso> 
	 * @return void
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorioAgregado(List<Recurso> listaDeRecursos) throws AplicacaoException {
	
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_RECMODELS);
			
		try{
			relatorio.gerarRelatorio(this.converterParaRecursoRelatorio(listaDeRecursos),new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("recurso.RELATORIO_NAO_GERADO");
		}
	}
	
	
	
	/**
	 * 
	 * @author marques.araujo
	 * @return List<TecidoRelatorio>
	 * @param List<Tecido> tecidos
	 * 
	 */
	public List<RecursoRelatorio> converterParaRecursoRelatorio(List<Recurso> recursos){
			
			List<RecursoRelatorio> recursosRelatorio = new LinkedList<RecursoRelatorio>();
						
			for (Recurso recurso : recursos) {
				
				if (recurso.getRecModels().isEmpty()){
					
				recursosRelatorio.add( new RecursoRelatorio(recurso,null,null));	
				
				}
							
				for (RecModel recmodel : recurso.getRecModels()) {
										
					RecursoRelatorio recurso_Relatorio = null;
					
					try{
						recmodel = recModelService.recuperaRecModelComModelo(recmodel);
						
					}catch(AplicacaoException re){
   				}
					recurso_Relatorio = new RecursoRelatorio(recurso,recmodel,recmodel.getModelo());
								
					recursosRelatorio.add(recurso_Relatorio);
				 }
				
			}
					
			return recursosRelatorio;
		}
	
}
