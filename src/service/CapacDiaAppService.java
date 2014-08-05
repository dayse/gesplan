 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.List;

import modelo.CapacDia;
import modelo.Parametros;
import modelo.PerioPM;

import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

import DAO.CapacDiaDAO;
import DAO.PerioPMDAO;
import DAO.ParametrosDAO;
import DAO.Impl.CapacDiaDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

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
 * 
 * @author felipe
 * 
 */
public class CapacDiaAppService {
	
	// DAOs
	private static CapacDiaDAO capacDiaDAO;
	private static ParametrosDAO parametrosDAO;
//	private static PerioPMDAO perioPMDAO;
	
	// Services
//	private static PerioPMAppService perioPMService;

	public CapacDiaAppService() {
		try {
			
			// DAOs
			// O atributo PerioPMDAO em tempo de compilacao eh do tipo
			// PerioPMDAO e em runtime ele eh do tipo PerioPMDAOImpl
			capacDiaDAO = FabricaDeDao.getDao(CapacDiaDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
//			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			
			// Services
//			perioPMService = FabricaDeAppService
//					.getAppService(PerioPMAppService.class);
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
	 * Inclui um objeto do tipo CapacDia, mas antes verifica se ja existe um
	 * CapacDia com este perioPM.
	 * 
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(CapacDia capacDia) throws AplicacaoException {

		try {
			capacDiaDAO.recuperaCapacDiaPorPerioPM(capacDia.getPerioPM());
			throw new AplicacaoException("capacDia.CAPACDIA_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {
		}
		capacDiaDAO.inclui(capacDia);

	}


	@Transacional
	public void altera(CapacDia capacDia) {
		// Obtem Parametros para pegar o valor da margem de seguranca
		Parametros parametros = null;
		
		parametros = parametrosDAO.recuperaListaDeParametros().get(0);

		// Atribui valores atuais para os campos calculados
		capacDia.setCapacProdDiariaEmMinMg(capacDia.getCapacProdDiariaEmMin()*(parametros.getMargemSeguranca()/100));
		
		capacDiaDAO.altera(capacDia);
		
	}

	/**
	 * Alteracao da capacidade de producao diaria em todos os registros a partir
	 * da variavel capacidadePadrao informada pelo usuario
	 **/
	@Transacional
	public void alteraCapacPadrao(double capacidadePadrao) {
		
		List<CapacDia> listaDeCapacDiaria = capacDiaDAO.recuperaListaDeCapacDias();
		
		for(CapacDia capacDiaAtual : listaDeCapacDiaria){
			capacDiaAtual.setCapacProdDiariaEmMin(capacidadePadrao);
			altera(capacDiaAtual);
		}
		
	}

	/**
	 * Nao existe opcao em tela para a exclusao de perioPAPs, esse metodo é chamado, a partir de regras de negocios,
	 *  como alteracoes e reinicializacao
	 *  
	 * @param perioPAP
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(CapacDia capacDia) throws AplicacaoException {

		CapacDia capacDiaBD = null;

		try {
			capacDiaBD = capacDiaDAO.getPorIdComLock(capacDia.getId());

		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("capacDia.NAO_ENCONTRADO");
		}

		capacDiaDAO.exclui(capacDiaBD);
	}


	public List<CapacDia> recuperaListaDeCapacDias() {
		return capacDiaDAO.recuperaListaDeCapacDias();
	}

	/**
	 * Como a relacao é eager, ao recuperar a lista de CapacDias vem junto as
	 * listas de PerioPMs Alem de recuperar lista paginada atribui os campos
	 * calculados todos.
	 * 
	 */
	public List<CapacDia> recuperaListaPaginadaDeCapacDias() {
		List<CapacDia> listaDeCapacDia = capacDiaDAO.recuperaListaPaginadaDeCapacDias();
		// Obtem Parametros para pegar o valor da margem de seguranca
		Parametros parametros = null;
		
		parametros = parametrosDAO.recuperaListaDeParametros().get(0);

		for (CapacDia capacDia : listaDeCapacDia) {
			

			// Atribui valores atuais para os campos calculados
			capacDia.setCapacProdDiariaEmMinMg(capacDia.getCapacProdDiariaEmMin()*(parametros.getMargemSeguranca()/100));
			
		}
	

		return listaDeCapacDia;
	}
		
	
}
