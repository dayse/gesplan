 
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

import modelo.Parametros;
import modelo.TecModel;
import modelo.Tecido;
import modelo.TecidoRelatorio;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.ParametrosDAO;
import DAO.TecidoDAO;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.TecidoDAOImpl;
import DAO.anotacao.RecuperaLista;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import exception.relatorio.RelatorioException;

/**
 * 
 * TecidoAppService é uma classe de serviço que possui as regras de negócio para manipular inicialmente
 * a entidade Tecido. Estas manipulações incluem quando necessário chamadas as interfaces DAOs,
 * outras classes de serviço e acessos a informações do BD.
 * 
 * A classe TecidoAppService fora criada para atender ao Padrão MVC, Model Vision Control, sendo a mesma uma
 * classe de serviço que é capaz de efetuar: controle de transação, ou seja esta classe possui o recurso de
 * abrir transaçao, commitar e fechar uma transaçao através de um interceptador de serviço.
 * Neste interceptador será definido se o método é transacional ou não e em função desta informação
 * o interceptador irá usar ou não uma transação. 
 * 
 * @author marques.araujo
 * 
 */

public class TecidoAppService {
	// DAOs
	private static TecidoDAO tecidoDAO;
	private static ParametrosDAO parametrosDAO;
    
	// Services 
	private static ParametrosAppService parametrosService;
    
	public TecidoAppService() { 
		try {
			// DAOs
			tecidoDAO = FabricaDeDao.getDao(TecidoDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
		
			// Services
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Transacional
	public void inclui(Tecido tecido) throws AplicacaoException {
		
		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		
		if (!parametro.isInicPlanejamento()){
			throw new AplicacaoException("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
		}
		
		try {
			tecidoDAO.recuperaTecidoPorCodigo(tecido.getCodTecido());
			throw new AplicacaoException("tecido.CODIGO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {			
		}
		
		tecidoDAO.inclui(tecido).getId();
	}

	
	/*
	 * Uma outra soluçao para validar o campo fatorDeRendimento
	 * seria a criaçao dos validadores atraves de uma tag converter
	 * como fora ensinado no curso do Carlos Ribeiro.
	 *  
	 */
	@Transacional
	public void altera(Tecido umTecido)
		throws AplicacaoException {
		
		if(umTecido.getFatorDeRendimento() > 0.0){
			
			tecidoDAO.altera(umTecido);
			
		}else{
			
			throw new AplicacaoException("O valor de cada campo deve ser maior que zero.");
			
		}
		
	}

	
	/** verificar se o exclui esta correto **/
	@Transacional
	public void exclui(Tecido umTecido) 
		throws AplicacaoException 
	{
		Tecido tecido;
		try
		{	
//			System.out.println("<<<<<<<<<<< Passou pelo exclui de TecidoAppService. >>>>>>>>>>>");
			
			tecido = tecidoDAO.getPorIdComLock((umTecido.getId()));
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Tecido não encontrado.");
		}

		tecidoDAO.exclui(tecido);
	}
	
	

	public Tecido recuperaTecido(long id)
			throws AplicacaoException {
		try {
			return tecidoDAO.getPorId(id);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("Tecido não encontrado");
		}
	}

	
	// Verificar se realmente este metodo sera necessario
	public Tecido recuperaTecidoPorCodigo(String codTecido)
																	
			throws AplicacaoException {
		try {
			return tecidoDAO.recuperaTecidoPorCodigo(codTecido);
		} catch (ObjetoNaoEncontradoException e) {

//			System.out
//					.println("O metodo recuperaUmTecidoPeloCodigo de TecidoAppService retornou uma excecao isto signifca que Tecido nao foi encontrada no bnco");
			throw new AplicacaoException("Tecido não encontrado");
		}

	}
	
	public List<Tecido> recuperaListaDeTecidos() throws AplicacaoException 
	{	
		List<Tecido> tecidos = tecidoDAO.recuperaListaDeTecidos();
		if (tecidos.size() == 0) 
		{	throw new AplicacaoException("Tecidos não encontrados");
		} 
		else 
		{	return tecidos;
		}
	}
	
	public List<Tecido> recuperaListaDeTecidosPorDescricao(String descrTecido) {
		return tecidoDAO.recuperaListaDeTecidosPorDescricao(descrTecido);
	}
	
	/**
	 * 
	 * Recupera a lista de todos os recursos que possuam código
	 * aproximado com o passado por parametro. 
	 * @param  String
	 * @return List<Tecido>
	 * 
	 */
	public List<Tecido> recuperaListaDeTecidosPeloCodigoLike(String codTecido){
		return tecidoDAO.recuperaListaDeTecidosPeloCodigoLike(codTecido);
	}
	
	
	public List<Tecido> recuperaListaPaginadaDeTecidos() {
		return tecidoDAO.recuperaListaPaginadaDeTecidos();
	}
	
	
	/**
	 * Este método é responsável por recuperar uma lista de Tecido com seus respectios TecModels.
	 * @author marques.araujo
	 * @param  Tecido
	 * @return Tecido
	 * @throws AplicacaoException
	 * 
	 */
	public Tecido recuperaTecidoComListaDeTecModels(Tecido tecido) throws AplicacaoException {
		try {
			return tecidoDAO.recuperaTecidoComListaDeTecModels(tecido);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("tecido.NAO_ENCONTRADO");
		}
		
	}
	
	public List<Tecido> recuperaListaDeTecidosQueTenhamApenasTecModels(){
		
		return tecidoDAO.recuperaListaDeTecidosQueTenhamApenasTecModels();
		
	}
	
	
	/**
	 * Este método retorna uma lista de tecidos já tendo efetuado o cálculo do consumoPorLoteKg
	 * para cada um dos tecidos da lista. 
	 * @author marques.araujo
	 * @return List<Tecido>
	 * 
	 */
	public List<Tecido> recuperaListaPaginadaDeTecidosComListaDeTecModels() {
		
		List<Tecido> listaTecidos = tecidoDAO.recuperaListaPaginadaDeTecidosComListaDeTecModels();
		
		List<Parametros> listaParametro = parametrosService.recuperaListaDeParametros();
		
		
		/*
		 *
		 * Como o parametro é um arquivo de registro único basta pegar o primeiro elemento da Classe Parametro.
		 * 
		 */
		Double percentualDePerda = listaParametro.get(0).getPercentualDePerda();
		
		for (Tecido tecido : listaTecidos) {
			
			for (TecModel tecModel : tecido.getTecModels()) {
								
				Double consumoPorLoteKg = (tecModel.getConsumoPorLoteMt()*(percentualDePerda/100 + 1))/tecido.getFatorDeRendimento();
							
				tecModel.setConsumoPorLoteKg(consumoPorLoteKg);
			}
			
		}
		
		
		return listaTecidos;
			
	}
	
	
	/**
	 * Este método é responsável or gerar relatório simples. 
	 * @param List<Tecido> 
	 * @return void
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorio(List<Tecido> listaDeTecidos) throws AplicacaoException {
		
//		System.out.println("Antes do metodo getRelatorio dentro de gerarRelatorio de TecidoAppService");

		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_TECIDOS);
				
		if(relatorio != null)
			System.out.println("A variavel do tipo Relatorio e difente de null em TecdioAppService");
		
//		System.out.println("Depois do metodo getRelatorio dentro de gerarRelatorio  de TecidoAppService");
		
		try{
			relatorio.gerarRelatorio(listaDeTecidos, new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("tecido.RELATORIO_NAO_GERADO");
		}
	}
	
	
	
	/**
	 * Este método é responsável por gerar um relatório a partir das classes Tecido e Modelo. 
	 * @author marques.araujo
	 * @param List<Tecido> 
	 * @return void
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorioAgregado(List<Tecido> listaDeTecidos) throws AplicacaoException {
		
		//RECUPERANDO A LISTA DE PARAMETROS COM O OBJETIVO DE PEGAR O PRIMEIRA OCORRENCIA
		List<Parametros> listaDeParametros = parametrosService.recuperaListaDeParametros();
	
		System.out.println(">>>>>>>>>>>>>> Antes da variavel relatorio em gerarRelatorioAgregado>>>>>>");
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_TECMODELS);
			
		System.out.println(">>>>>>>>>>>>>> Depois da variavel relatorio em gerarRelatorioAgregado>>>>>>");
		
		if(relatorio!=null){
			System.out.println("A variavel relatorio eh diferente de null");
		}else{
			System.out.println("A variavel relatorio tem valor null");
		}
		
		try{
			
			relatorio.gerarRelatorio(this.converterParaTecidoRelatorio(listaDeTecidos,listaDeParametros),new HashMap());
			
			System.out.println(">>>>>>>>>>>>>> Depois da variavel relatorio em gerarRelatorioAgregado executando converterParaTecidoRelatorio>>>>>>");
			
		} catch (RelatorioException re){
			throw new AplicacaoException("tecido.RELATORIO_NAO_GERADO");
		}
	}
	
	
	/**
	 * Este método é responsável por recuperar uma lista de Tecidos com seus respectivos TecModels com o consumoPorLoteKg já calculado. 
	 * @author marques.araujo
	 * @return List<Tecido> 
	 * 
	 */
	public List<Tecido> recuperaListaDeTecidosComTecModels(){
		
		List<Tecido> listaTecidos = tecidoDAO.recuperaListaDeTecidosComTecModels();
		
		List<Parametros> listaParametro = parametrosService.recuperaListaDeParametros();
		
//		System.out.println("Tamanho da lista de tecidos="+listaTecidos);
		/*
		 *
		 * Como o parametro eh um arquivo de registro unico basta pegar o primeiro elemento da lista.
		 * 
		 */
		Double percentualDePerda = listaParametro.get(0).getPercentualDePerda();
		
		for (Tecido tecido : listaTecidos) {
			
			for (TecModel tecModel : tecido.getTecModels()) {
								
				Double consumoPorLoteKg = (tecModel.getConsumoPorLoteMt()*(percentualDePerda/100 + 1))/tecido.getFatorDeRendimento();
							
				tecModel.setConsumoPorLoteKg(consumoPorLoteKg);
			}
			
		}
		
		
		return listaTecidos;
		
				
	}
	
	/*
	
	// Para usar o metodo abaixo fique ciente que sera necessario
	// fazer o calculo para o atributo consumoPorLoteKg. Este atributo
	// deve ser calculado seguindo a formula abaixo:
	// Double consumoPorLoteKg = (tecModel.getConsumoPorLoteMt()*(percentualDePerda/100 + 1))/tecido.getFatorDeRendimento();
	// O metodo abaixo foi criado partindo do presuposto que no relatorioListagemTecModels.jrxml
	// seria realizado o calculo do atributo consumoPorLoteKg.  
		 
	public List<Tecido> recuperaListaDeTecidosComTecModels(){
		
		return tecidoDAO.recuperaListaDeTecidosComTecModels();
		
	}
	*/
	
	
	
	
	/**
	 * Este método é responsável por gerar objetos do tipo TecidoRelatorio a partir de objetos do tipo Tecido e Modelo.
	 * @author marques.araujo
	 * @param List<Tecido> tecidos, List<Parametros> parametros
	 * @return List<TecidoRelatorio>
	 * 
	 */
	public List<TecidoRelatorio> converterParaTecidoRelatorio(List<Tecido> tecidos,List<Parametros> parametros){
			
			List<TecidoRelatorio> tecidosRelatorio = new LinkedList<TecidoRelatorio>();
			
			
			System.out.println(">>>>>>>>>>>>>> Rodou o metodo converterParaTecidoRelatorio >>>>>>>>");
			System.out.println("Quantidade de tecidos="+tecidos.size());
			
			for (Tecido tecido : tecidos) {
			
			System.out.println(">>>>>>>>>>>>>> Entrou no for");
			
				if (tecido.getTecModels().isEmpty()){
					System.out.println(">>>>>>>>>>>>>> UM tecido nao possui tecmodels");
					
				tecidosRelatorio.add(new TecidoRelatorio(tecido,null,null,null));	
				
				}
								
				//Perceba que o tecido que nao tiver tecmodels nao entrara no segundo for e tambem nao dara erro.
				for (TecModel tecmodel : tecido.getTecModels()) {
					
					// O ATRIBUTO setConsumoPorLoteKg ESTA SENDO CALCULADO NO PROPRIO RELATORIO
				    // tecmodel.setConsumoPorLoteKg(1000*tecmodel.getConsumoPorLoteMt());//VERIFICAR A FORMULA CORRETA
					
					TecidoRelatorio tecido_Relatorio = new TecidoRelatorio(tecido,tecmodel,tecmodel.getModelo(),parametros.get(0));
								
					tecidosRelatorio.add(tecido_Relatorio);
				 }
				
			}
			
			System.out.println(">>>>>>>>>>>>>> Rodou e chegou no final do metodo converterParaTecidoRelatorio >>>>>>>>");
			
			return tecidosRelatorio;
		}
		
	
}
