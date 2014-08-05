 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

//import java.util.ArrayList;
//import java.util.List;

import java.util.List;

import modelo.PerioPAP;
import modelo.PerioPM;
import modelo.HP;

import service.HPAppService;
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

public class EstudoDefineNovoHP {
	
	private static PerioPAPAppService perioPAPService;
	private static PerioPMAppService perioPMService;
	private static HPAppService hpService;
	
	private PerioPM perioPMInicial;
	private PerioPM perioPMFinal;
	private PerioPAP perioPAPInicial;
	private PerioPAP perioPAPFinal;
	private HP hp = new HP();
	
	@BeforeClass
	  public void setupClass(){
		
		 try {
			 perioPMService= FabricaDeAppService.getAppService(PerioPMAppService.class);
			 perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			 hpService = FabricaDeAppService.getAppService(HPAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
			int periodoPAPFinal = perioPAPService.recuperaListaDePerioPAPs().size();
			try {
				//recupera os paps e depois os pms deles
				perioPAPInicial = perioPAPService.recuperaPerioPAPPorPeriodoPAP(1);
				perioPAPFinal = perioPAPService.recuperaPerioPAPPorPeriodoPAP(periodoPAPFinal);
				
				perioPMInicial = perioPAPService.obtemPrimeiroPerioPMdoPerioPAP(perioPAPInicial);
				perioPMFinal = perioPAPService.obtemUltimoPerioPMdoPerioPAP(perioPAPFinal);
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}
			List<HP> lista = hpService.recuperaListaDeHP();
			if (!lista.isEmpty()) {
				hp = lista.get(0);
			}
		}
	
	@Test
	public void defineNovoHP(){
		hp.setPerioPMInicPMP(perioPMInicial);
		hp.setPerioPMFinalPMP(perioPMFinal);
		hp.setPerioPMInicDemMod(perioPMInicial);
		hp.setPerioPMFinalDemMod(perioPMFinal);
		
		hp.setPerioPAPInicPAP(perioPAPInicial);
		hp.setPerioPAPFinalPAP(perioPAPFinal);
		hp.setPerioPAPInicDemFam(perioPAPInicial);
		hp.setPerioPAPFinalDemFam(perioPAPFinal);
			try {
				if (hp.getId() == null) {
					hpService.inclui(hp);
				} else {
					hpService.altera(hp);
				}
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}
		System.out.println(">>>>defineNovoHP");
	}
	
	@Test(dependsOnMethods="defineNovoHP")
	public void inicializaPlanejamento(){

		try {
			hpService.iniciaPlanejamento();
		} catch (AplicacaoException e) {
			e.printStackTrace();
		}
		
		System.out.println(">>>>inicializaPlanejamento");
	}
	
}
