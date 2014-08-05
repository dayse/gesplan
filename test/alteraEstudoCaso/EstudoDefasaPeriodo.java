 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

import java.util.GregorianCalendar;
import java.util.Calendar;

import modelo.PerioPM;

import service.PerioPAPAppService;
import service.PerioPMAppService;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * 
 * @author bruno.oliveira
 * 
 */
public class EstudoDefasaPeriodo {
	
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	private static int OPCAO_EXCLUIR_PRIMEIRO=0;
	private PerioPM perioPM = new PerioPM();
	
	@BeforeClass
	  public void setupClass(){
		 try {
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			 perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		
	
		/**
		 * 
		 */		
		@Test
		public void excluiPrimeiroPeriodo(){
			if (perioPMService.recuperaListaDePerioPMs().isEmpty()){
				System.out.println("ERRO: Não existem planos cadastrados.");
			}
			try {
				perioPMService.excluirPrimeiroUltimoOuTodos(OPCAO_EXCLUIR_PRIMEIRO);
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 
		 */
		@Test(dependsOnMethods="excluiPrimeiroPeriodo")
		public void incluiUltimoPeriodo() throws AplicacaoException{
			Calendar dataInicial = new GregorianCalendar(2011,Calendar.DECEMBER,01);
			
			Calendar dataFinal = new GregorianCalendar(2011,Calendar.DECEMBER,15);
			
			perioPM.setPeriodoPM(10);
			perioPM.setDataInicial(dataInicial);
			perioPM.setDataFinal(dataFinal);
			perioPM.setNumDiasUteisMatriz(10);
			perioPM.setNumDiasUteisU2(10.5);
		
			perioPAPService.incluiComPerioPM(perioPM);
		}


	}

