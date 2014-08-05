 
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

import service.anotacao.Transacional;

import service.exception.AplicacaoException;

import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.TecidoDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import DAO.HPDAO;
import DAO.PerioPMDAO;
import DAO.TecidoDAO;


import modelo.CapacTecView;
import modelo.HP;
import modelo.PerioPM;
import modelo.Tecido;


public class CapacTecViewAppService {

	// DAOs
	private static HPDAO hpDAO;
	private static PerioPMDAO perioPMDAO;
	private static TecidoDAO tecidoDAO;
	
	@SuppressWarnings("unchecked")
	public CapacTecViewAppService() {
		try {
			// DAOs
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			tecidoDAO = FabricaDeDao.getDao(TecidoDAOImpl.class);
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
	 * Constroi em memoria a lista de capacTecViews a partir da combinacao de Tecido e PerioPM
	 * Calcula o campo ConsumoMaximoDiario
	 * Seta a lista de tecidos com suas respectivas listas de capacTecViews.
	 * Retorna a lista de Tecido populada com as respectivas listas de CapacTecViews
	 * 
	 * @return List<Tecido>
	 * @throws AplicacaoException 
	 */
	public List<Tecido> recuperaListaPaginadaDeTecidosComListaDeCapacTecViews() throws AplicacaoException {

		// a) Verificar se existe um HP cadastrado
		List<HP> hpBD = hpDAO.recuperaListaDeHP();
		if (hpBD.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		}
		//Recupera o unico hp do bd. isso eh: O elemento 0 da lista retornada na busca
		HP hp = hpBD.get(0);
		
		
		//Lista de todos os tecidos
		List<Tecido> listaDeTecidos = tecidoDAO.recuperaListaPaginadaDeTecidos();
		
		//Lista de periodos no intervalo do HP
		List<PerioPM> listaPerioPMsHP = 
			perioPMDAO.recuperaIntervaloDePerioPMs(hp.getPerioPMInicPMP().getPeriodoPM(), hp.getPerioPMFinalPMP().getPeriodoPM());
		
		//Lista de capacTecViews
		List<CapacTecView> listaDeCapacTecViews;
		
		//para cada tecido da lista de tecidos
		for(Tecido tecido : listaDeTecidos){
			
			//Cria uma nova lista zerada de capacTecViews do tecido atual
			listaDeCapacTecViews = new ArrayList<CapacTecView>();
			
			//Para cada perioPM de cada tecido, e Popula a lista de capacTecView para cada perioPM
			for(PerioPM perioPM : listaPerioPMsHP){
				CapacTecView capacTecView = new CapacTecView();
				capacTecView.setTecido(tecido);
				capacTecView.setPerioPM(perioPM);

				double prodTotalU2NoPeriodo = tecido.getProducaoDiariaMaxUnidade2()*perioPM.getNumDiasUteisU2();
				
				double formulaConsumoMaxDiario = 0;
				// no caso da divisao por zero, como os valores são double não ocorre erro, apenas 
				// será mostrado o valor infinito na tela
				//o try catch foi colocoda como medida de segurança para rastrear algum outro tipo de erro apenas
				try{
					formulaConsumoMaxDiario = prodTotalU2NoPeriodo / perioPM.getNumDiasUteisMatriz();
				}
				catch(Exception e){	
					e.printStackTrace();
				}
				capacTecView.setConsumoMaxDiarioMatriz( formulaConsumoMaxDiario );
				
				
				//Inclui um capacTecView referente a combinacao atual de Tecido X PerioPM				
				listaDeCapacTecViews.add(capacTecView);
			}
			
			//Seta a lista do tecido atual com a sua respectiva lista de CapacTecViews
			tecido.setCapacTecViews(listaDeCapacTecViews);
		}
		
		return listaDeTecidos;
	}
	
	public List<CapacTecView> recuperaListaDeCapacTecViewsPorTecido(Tecido tecido) throws AplicacaoException{
		
		List<Tecido> listaDeTecidos = this.recuperaListaPaginadaDeTecidosComListaDeCapacTecViews();
		List<CapacTecView> listaDeCapacTecViewsDoTecido =  new ArrayList<CapacTecView>();
		
		for (Tecido tecidoCorrente : listaDeTecidos){
			if(tecidoCorrente.getCodTecido().equals(tecido.getCodTecido())){
				listaDeCapacTecViewsDoTecido = tecidoCorrente.getCapacTecViews();
			}
		}
		
		return listaDeCapacTecViewsDoTecido;
	}
	
}
