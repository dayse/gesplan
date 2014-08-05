 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.util.List;

import modelo.CapacDia;
import modelo.HP;
import modelo.PerioPM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.HPDAO;
import DAO.ParametrosDAO;

import service.CapacDiaAppService;
import service.PerioPMAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaCapacDia {
	
	private static CapacDiaAppService capacDiaService;
	private static PerioPMAppService perioPMService;
	private static HPDAO hpDAO;
	private static ParametrosDAO parametrosDAO;
	
	@BeforeClass
	  public void setupClass(){
		
		 try {
			 capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);			
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		
		
		@Test
		/**
		 * Nesse ponto os capacdias ja foram add pela inicializacao do hp
		 * entao apenas altera seus valores
		 */
		public void incluirCapacDias() throws AplicacaoException{
			double VALOR_PADRAO_CAPACIDADE = 25000;

			List<CapacDia> listaCapacDias = capacDiaService.recuperaListaDeCapacDias();
			
			for(CapacDia capacDia : listaCapacDias){
				capacDia.setCapacProdDiariaEmMin(VALOR_PADRAO_CAPACIDADE);
				capacDiaService.altera(capacDia);
			}

			
		}
		
		
		

	}
