 
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

import modelo.DeFamPer;
import modelo.Familia;
import modelo.FamiliaRelatorio;
import modelo.Modelo;
import modelo.PerioPAP;
import modelo.PerioPM;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.FamiliaDAO;
import DAO.ParametrosDAO;
import DAO.Impl.FamiliaDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import exception.relatorio.RelatorioException;

/**
 * 
 * As classes de serviço é que possuem as regras de negócio. Fazem as críticas
 * quando necessário e chamam as classes DAO para pegar as informações do BD.
 * 
 * São essas classes AppService que fazem o controle de transaçao, ou seja quem
 * abre a transaçao, "quem commita" através do interceptador de appservice. Aqui
 * defino se o metodo é transacional ou não e em funçao desta informaçao o
 * interceptador vai usar ou nao transacao.
 * 
 * 
 * Esta classe é originaria de jpa8 - DAO Generico familia e adaptada com
 * tratamento de excecao feito em EmpregadoAppService de jsf4b para o metodo que
 * recupera todas as familias
 * 
 * @author daysemou
 * 
 */
public class FamiliaAppService {
	
	// DAOs
	private static FamiliaDAO familiaDAO;
	private static ParametrosDAO parametrosDAO;
	private static DeFamPerAppService deFamPerService;
	private static PerioPAPAppService perioPAPAppService;

	public FamiliaAppService() {
		
		try {
			
			// DAOs
			familiaDAO = FabricaDeDao.getDao(FamiliaDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			deFamPerService = FabricaDeAppService.getAppService(DeFamPerAppService.class);
			
			// Service
			perioPAPAppService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Inclui um objeto do tipo Familia
	 * 
	 * Verifica se o código da família é único
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
		public void inclui(Familia familia) throws AplicacaoException {
		
		try {
			
			/*
			 * Se o metodo nao recuperaUmaFamiliaPeloCodigo nao encontrar a familia
			 * cujo codigo foi passado sera retornada uma exceçao a qual sera capturada
			 * pelo catch abaixo permitindo assim incluir uma nova familia.	
			 * 	
			 */
			
			familiaDAO.recuperaUmaFamiliaPeloCodigo(familia.getCodFamilia());
			
			throw new AplicacaoException("familia.CODIGO_EXISTENTE");
			
		} catch (ObjetoNaoEncontradoException e) {			
		}
		
		familiaDAO.inclui(familia);
		
		
		try {
			deFamPerService.incluiDemandas(familia);
		} catch (AplicacaoException ex) {
			throw new AplicacaoException(ex.getMessage());
		}
		
	}
	
	/**
	 * Altera um objeto do tipo Familia
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void altera(Familia umaFamilia) {
		familiaDAO.altera(umaFamilia);
	}

	/**
	 * Exclui um objeto do tipo Familia
	 * 
	 * Verifica se a família possui modelos
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(Familia familia) throws AplicacaoException {

		try {
			familia = familiaDAO.recuperaUmaFamiliaEModelos(familia.getId());
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("familia.NAO_ENCONTRADA");
		}

		if (familia.getModelos().size() > 0) {
			throw new AplicacaoException("familia.POSSUI_MODELOS");
		}

		familiaDAO.exclui(familia);
	}


	@SuppressWarnings("unchecked")
	public void gerarRelatorioFamilia(List<FamiliaRelatorio> familiasRelatorios) throws AplicacaoException {
       
		System.out.println("##################### 1 - Passou pelo metodo gerarRelatorioFamilia de FamiliaAppService ##################");
		
		System.out.println("################## Passo 3 gerarRelatorioFamilia######################");
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_FAMILIAS_COM_MODELOS);
					
		try{
			System.out.println("##################### 2 - Passou pelo metodo gerarRelatorioFamilia de FamiliaAppService ##################");
			relatorio.gerarRelatorio(familiasRelatorios, new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("familia.RELATORIO_NAO_GERADO");
		}
	}
	
		
	
	@SuppressWarnings("unchecked")
	public void gerarRelatorioAgregado(List<Familia> familias) throws AplicacaoException {
       
		System.out.println("##################### 1 - Passou pelo metodo gerarRelatorio de FamiliaAppService ##################");
		
		System.out.println("################## Passo 3 ######################");
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_FAMILIAS_COM_MODELOS);
		
		
		try{
			System.out.println("##################### 2 - Passou pelo metodo gerarRelatorio de FamiliaAppService ##################");
			
			relatorio.gerarRelatorio(this.converterParaFamiliaRelatorio(familias), new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("familia.RELATORIO_NAO_GERADO");
		}
	}
	
	
	/**
	 * Este metodo é responsável por transformar uma lista de Familias com Modelos
	 * em uma lista de FamiliaRelatorio. A classe FamiliaRelatorio é um agregado de
	 * atributos das classes Familia e Modelo sendo utilizado para impressao de um
	 * relatorio com Familias e Modelos.
	 * 
	 * @author marques.araujo 
	 * 
	 */
	public List<FamiliaRelatorio> converterParaFamiliaRelatorio(List<Familia> familias){
		
		List<FamiliaRelatorio> familiasRelatorio = new LinkedList<FamiliaRelatorio>();
		
		for (Familia familia : familias) {
			
			if (familia.getModelos().isEmpty()){
				
			familiasRelatorio.add( new FamiliaRelatorio(familia,null));	
			
			}
			
			for (Modelo modelo : familia.getModelos() ) {
				
				FamiliaRelatorio familiaRelatorio = new FamiliaRelatorio(familia,modelo);
							
				familiasRelatorio.add(familiaRelatorio);
			 }
			
		}
		
		
		
		return familiasRelatorio;
	}
	
	
	 //Este metodo eh o metodo original para gerar relatorios simples.
	@SuppressWarnings("unchecked")
	public void gerarRelatorio(List<Familia> familias) throws AplicacaoException {
       
		System.out.println("*************** 1 - Passou pelo metodo gerarRelatorio de FamiliaAppService ************");
		
		System.out.println("*************** Passo 3 ****************");
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_FAMILIAS);
		
		try{
			System.out.println("*************** 2 - Passou pelo metodo gerarRelatorio de FamiliaAppService ************");
			relatorio.gerarRelatorio(familias, new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("familia.RELATORIO_NAO_GERADO");
		}
	}
	
	/**
	 * Executa uma busca por todas as famílias paginando o resultado
	 * 
	 * @return List<Familia>
	 */
	public List<Familia> recuperaListaPaginadaDeFamilias() {
		return familiaDAO.recuperaListaPaginadaDeFamilias();
	}
	
	/**
	 * Executa uma busca por todas as famílias paginando o resultado
	 * 
	 * @return List<Familia>
	 */
	public List<Familia> recuperaListaDeFamilias() {
		
		return familiaDAO.recuperaListaDeFamilias();
	}
	
	/**
	 * Executa uma busca por todas as famílias com ou sem modelos
	 * @author marques.araujo
	 * @return List<Familia>
	 */
	public List<Familia> recuperaListaDeFamiliasComModelos() {
	
		return familiaDAO.recuperaListaDeFamiliasComModelos();
	}
	
	/** 
	 * @author marques.araujo
	 * Recupera a lista de todos as familias que possuam código aproximado com o passado por parametro. 
	 */
	public List<Familia> recuperaListaDeFamiliasPeloCodigoLike(String codFamilia){
		return familiaDAO.recuperaListaDeFamiliasPeloCodigoLike(codFamilia);
	}
	
	/**
	 * @author marques.araujo
	 * @return Uma lista de familias com esta descricao
	 */
	public List<Familia> recuperaListaDeFamiliasPorDescricao(String descrFamilia) {
		return familiaDAO.recuperaListaDeFamiliasPorDescricao(descrFamilia);
	}
	
		
	public Familia recuperaUmaFamiliaPeloCodigo(String codFamilia)throws AplicacaoException{
		
		Familia familia = null;
		
		try{
			
			familia = familiaDAO.recuperaUmaFamiliaPeloCodigo(codFamilia);
		
		}catch(ObjetoNaoEncontradoException e){
			
			throw new AplicacaoException("familia.NAO_ENCONTRADA");
			
		}
		
		return familia;
	}
	
	 /** 
	  * @author marques.araujo
	  * 
	  * Este metodo esta sendo utilizado me DeFamPerActions
	  * para a tela list de DeFamPer. 
	  */
	
   public List<Familia> recuperaListaDeFamiliasComDeFamPers(){
	   
	   List<Familia> familias = familiaDAO.recuperaListaDeFamiliasComDeFamPers();
	   
	   return familias;
    }
   
   public Familia recuperaUmaFamiliaApartirDoModelo(Modelo modelo)throws AplicacaoException {
	   
	   Familia familia = null;
	   
	   try{
		 familia = familiaDAO.recuperaUmaFamiliaApartirDoModelo(modelo);
		   
	   }catch(ObjetoNaoEncontradoException obj){
		   throw new AplicacaoException();
	   }
	   return familia;
   }
   
   
}


