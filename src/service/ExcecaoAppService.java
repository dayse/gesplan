 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.List;

import modelo.CadPlan;
import modelo.Excecao;
import modelo.ExcecaoMens;
import modelo.ModelagemFuzzy;
import modelo.PerioPM;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.Constantes;
import DAO.ExcecaoDAO;
import DAO.ExcecaoMensDAO;
import DAO.PerioPMDAO;
import DAO.Impl.ExcecaoDAOImpl;
import DAO.Impl.ExcecaoMensDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;


public class ExcecaoAppService {

	// DAOs
	private static PerioPMDAO perioPMDAO; 
	private static ExcecaoDAO excecaoDAO; 
	private static ExcecaoMensDAO excecaoMensDAO; 
	
	// Services
//	private static DadosGraficoViewAppService dadosGraficoViewService;
	
	@SuppressWarnings("unchecked")
	public ExcecaoAppService() {
		try {
			
			// DAOs
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			excecaoDAO = FabricaDeDao.getDao(ExcecaoDAOImpl.class);
			excecaoMensDAO = FabricaDeDao.getDao(ExcecaoMensDAOImpl.class);
			
			// Service
//			dadosGraficoViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			// O comando a seguir só será usado caso haja a criação de um service.
			// Exemplo:
			// Um Service A tem dentro de si a chamada de um Service B, só que o Service B também tem
			// uma chamada para o Service A, logo um service chamaria o outro sem parar causando assim um loop infinito.
			// Contudo, em termos de uso do sistemas esse erro não ocorreria de forma clara, 
			// pois a View seria carregada sem dados.
			// Para evitar que esse tipo de erro gere confusões - como o usuário pensar que o banco foi perdido, por exemplo - 
			// utilizamos o comando System.exit(1) que interrompe a aplicação, deixando explicita a ocorrência do erro.
			//System.exit(1); 
		}
	}

	
	/**
	 * Inclui uma excecao
	 * @param excecao
	 * @return
	 * @throws AplicacaoException
	 */
	@Transacional
	public long inclui(Excecao excecao) throws AplicacaoException {
		
		long retorno = -1;
		
		try {
			excecaoDAO.recuperaExcecaoPeloTipoDeExcecao(excecao.getTipoDeExcecao());
			throw new AplicacaoException("excecao.TIPO_EXCECAO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {			
			retorno = excecaoDAO.inclui(excecao).getId();
		}
		
		
		return retorno;

	}

	/**
	 * Altera uma Excecao
	 * @param excecao
	 */
	@Transacional
	public void altera(Excecao excecao) {
		excecaoDAO.altera(excecao);
	}

	/**
	 * Exclui uma Excecao
	 * @param excecao
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(Excecao excecao) throws AplicacaoException {
		
		List<ExcecaoMens> listaDeExcecaoMenss = excecaoMensDAO.recuperaListaDeExcecaoMensPorExcecao(excecao);
		if (listaDeExcecaoMenss.isEmpty()){
			// se não tem execaoMens associados a essa execao então pode exclui-la
			
			Excecao excecaoParaExcuir= null;
			
			try {
				excecaoParaExcuir = excecaoDAO.getPorIdComLock((excecao.getId()));
			} catch (ObjetoNaoEncontradoException e) {
				throw new AplicacaoException("excecao.NAO_ENCONTRADA");
			}
			

			excecaoDAO.exclui(excecaoParaExcuir);			
			    
		}
		else{
			throw new AplicacaoException("excecao.USADA_EM_EXCECAOMENS");
		}
		
	}

	/**
	 * Retorna a lista de todas as excecoes
	 * @return
	 */
	public List<Excecao> recuperaListaPaginadaDeExcecoes(){		
		
		return excecaoDAO.recuperaListaPaginadaDeExcecoes();
	}

	/**
	 * Retorna uma lista com as excecoes, chamando o metodo excecaoDAO.recuperaListaDeExcecoes
	 * O primeiro valor da lista é uma excecao nula, que não deve ser utilizado.
	 * 
	 * @return
	 */
	public List<Excecao> inicializaVetorExcecao(){
		List<Excecao> excecoes = new ArrayList<Excecao>();
		Excecao excecaoLIXO = new Excecao();
		excecoes.add(excecaoLIXO);
		excecoes.addAll(excecaoDAO.recuperaListaDeExcecoes());		
		return excecoes;
	}
}
