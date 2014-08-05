 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.util.List;

import modelo.CapacRec;
import modelo.HP;
import modelo.PerioPM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.HPDAO;
import DAO.ParametrosDAO;

import service.CapacRecAppService;
import service.PerioPMAppService;

import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaCapacRec {
	
	private static CapacRecAppService capacRecService;
	private static PerioPMAppService perioPMService;
	private static HPDAO hpDAO;
	private static ParametrosDAO parametrosDAO;
	
	@BeforeClass
	  public void setupClass(){
		
		 try {
			 
			 capacRecService = FabricaDeAppService.getAppService(CapacRecAppService.class);
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);			
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		
		
		@Test
		/**
		 * Nesse ponto os capacrecs ja foram add pela inicializacao do hp
		 * entao apenas altera seus valores
		 */
		public void incluirCapacRecs() throws AplicacaoException{

			List<CapacRec> listaCapacRecs = capacRecService.recuperaListaDeCapacRecsComRecursosEPerioPMs();
			
			for(CapacRec capacRec : listaCapacRecs){
				if(capacRec.getRecurso().getCodRecurso().equals("01")){

					capacRec.setCapacDiaria(1800.0);
				}
				else if(capacRec.getRecurso().getCodRecurso().equals("02")){

					capacRec.setCapacDiaria(1700.0);
				}
				else if(capacRec.getRecurso().getCodRecurso().equals("03")){

					capacRec.setCapacDiaria(2900.0);
				}
				
				capacRecService.altera(capacRec);
			}

			
		}
		
		
		

	}
