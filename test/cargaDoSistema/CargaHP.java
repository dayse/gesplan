 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import modelo.HP;
import modelo.PerioPAP;
import modelo.PerioPM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.HPAppService;
import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaHP {
  
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	private static HPAppService hpService;
	
	// A anotaçao @BeforeClass é necessária para criar os services
	// que serao responsaveis pela excuçao dos metodos das classes
	// AppService. Apos a excuçao do metodo setupClass que fora anotado
	// como @BeforeClass os demais metodos da classe de teste serao executados.
	
	@BeforeClass 	
	public void setupClass(){
		
		try {
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	// O parametro "groups" serve para estabeleler uma ordem de precedencia
	// ENTRE os metodos da classe de teste(CargaHP). Exemplo: @Test (groups="inclusao")
	
	@Test//(groups = "inclusao")
	public void incluirHP() throws AplicacaoException{

		//System.out.println(">>>>>>>>>>>>>>>>>> Passou no incluirHP 4");
		
		
		PerioPM perioPMInicial = null;
		PerioPM perioPMFinal = null;
		
		PerioPAP perioPAPInicial = null;
		PerioPAP perioPAPFinal = null;
		
		try {
			
			perioPMInicial = perioPMService.recuperaPerioPMPorPeriodoPM(1);
			perioPMFinal = perioPMService.recuperaPerioPMPorPeriodoPM(10);
			
			perioPAPInicial = perioPAPService.recuperaPerioPAPPorPeriodoPAP(perioPMInicial.getPeriodoPM());
			perioPAPFinal = perioPAPService.recuperaPerioPAPPorPeriodoPAP(perioPMFinal.getPeriodoPM());
			
		} catch (AplicacaoException e) {
			
		}
		 
		HP hp = new HP();
		
		// ---------------- PLANO MESTRE -----------------//
		
		hp.setPerioPMInicPMP(perioPMInicial);
		hp.setPerioPMFinalPMP(perioPMFinal);
		
		// ---------------- DEMANDA MODELO -----------------//
		
		hp.setPerioPMInicDemMod(perioPMInicial);
		hp.setPerioPMFinalDemMod(perioPMFinal);
		
		// ---------------- PLANO AGREGADO -----------------//
		
		hp.setPerioPAPInicPAP(perioPAPInicial);
		hp.setPerioPAPFinalPAP(perioPAPFinal);
		
		// ---------------- DEMANDA FAMILIA -----------------//
		
		hp.setPerioPAPInicDemFam(perioPAPInicial);
		hp.setPerioPAPFinalDemFam(perioPAPFinal);
		
		hpService.inclui(hp);
			
	}
	
	
	// A anotaçao dependsOnGroups obriga o metodo que possua esta anotaçao ser
	// executado apos axecuçao dos metodos que façam parte do grupo inclusao.
	// Exemplo: @Test (dependsOnGroups = "inclusao")
	
	@Test 
	public void inicializarHP() throws AplicacaoException{
		
		//System.out.println(">>>>>>>>>>>>>>>>>> Passou no incluirHP 5");
		
		hpService.iniciaPlanejamento();
	}
	
	
	// ATENÇAO: 
	// EMBORA A NOSSA SUITE DE TESTE POSSUA A PROPRIEDADE preserve-order="true"
	// O QUE A PRIORI DEFINIRIA A ORDEM DE EXECUÇAO COMO FOI DEFINIDA NO ARQUIVO
	// testng.xml O FATO DE ANOTARMOS ESTA CLASSE DE TESTE(CargaHP) COM GROUPS ISTO
	// FAZ COM QUE A CARGA DE TESTE CargaHP SEJA PRIORIZADA NA ORDEM DE EXCUÇAO DA 
	// NOSSA SUITE O QUE CERTAMENTE PROVOCARA UM ERRO. PARA EVITARMOS ESTE PROBLEMA
	// TIRAMOS AS ANOTAÇOES DE TODAS AS CLASSES DE TESTE DO PACKAGE CargaDoSistema.
	
	

}
