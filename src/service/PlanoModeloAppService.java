 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import comparator.PlPerModComparatorPorPerioPM;

import modelo.CadPlan;
import modelo.Modelo;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.HPDAO;
import DAO.ModeloDAO;
import DAO.PerioPMDAO;
import DAO.PlanoModeloDAO;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.PlanoModeloDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class PlanoModeloAppService {
	
	// DAOs
	private static PlanoModeloDAO planoModeloDAO;

	public PlanoModeloAppService() {
		
		try {
			
			// DAOs
			planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
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
	 * Inclui em cascata o set de PlPerMods do planoModelo
	 *  */
	@Transacional
	public void inclui(PlanoModelo planoModelo) {
		planoModeloDAO.inclui(planoModelo);
	}
	
	@Transacional
	public void altera(PlanoModelo planoModelo) {
		planoModeloDAO.altera(planoModelo);
	}
	
	public PlanoModelo recuperarPlPerModsPorPlanoModelo(PlanoModelo planoModelo) throws ObjetoNaoEncontradoException {
		return planoModeloDAO.recuperarPlPerModsPorPlanoModelo(planoModelo);
	}
	
	@Transacional
	public void exclui(PlanoModelo planoModelo) {
		
		PlanoModelo planoModeloBD = null;
		
		try {
			planoModeloBD = planoModeloDAO.getPorIdComLock(planoModelo.getId());
		} catch (ObjetoNaoEncontradoException ex){
		}
		
		planoModeloDAO.exclui(planoModeloBD);
	}


	@Transacional
	public double calculaProdDiariaMediaDoPlanoModelo(PlanoModelo planoModelo){
		double prodDiariaMedia = 0.0;
		
		for(PlPerMod plPerMod : planoModelo.getPlPerModsList()){
			prodDiariaMedia += plPerMod.getProdDiariaLoteModel();
		}
		prodDiariaMedia = prodDiariaMedia / planoModelo.getPlPerModsList().size();
		planoModelo.setProdDiariaMediaPlanoModelo(prodDiariaMedia);
		
		return prodDiariaMedia;
	}
	
	@Transacional
	public double calculaEscore(PlanoModelo planoModelo){
		double escore = 0.0;
		
		for(PlPerMod plPerMod : planoModelo.getPlPerModsList()){
			escore += plPerMod.getEscorePlanPerMod();
		}
		escore = escore / planoModelo.getPlPerModsList().size();
		planoModelo.setEscore(escore);
		
		return escore;
	}
	
	/**
	 * Inclui os registros de PlanoModelo relativos a um novo modelo.
	 * É usado no momento da inclusao de um modelo.
	 * 
	 * Felipe.Arruda
	 * @param planosCadastrados
	 * @param modelo
	 */
	@Transacional
	public void incluirPlanoModeloParaNovoModelo(List<CadPlan> planosCadastrados, Modelo modelo){
		
		for (CadPlan cadPlan : planosCadastrados) {
			
			PlanoModelo planoModelo = new PlanoModelo(cadPlan, modelo);
			this.inclui(planoModelo);
			
			PlPerModAppService plPerModService;
			
			try {
				plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);	// Inicialização cíclica
				plPerModService.incluirPlPerModParaNovoModelo(planoModelo);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	

}
