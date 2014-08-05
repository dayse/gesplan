 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import modelo.HP;
import modelo.PerioPAP;
import modelo.PerioPM;
import service.HPAppService;
import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

/**
 * Carga para ser usada como base, pois possui o method executar.
 * 
 * Nesse método executar é que é chamado os outros métodos que são
 * as etápas dessa carga.
 * Portanto se é necessario rodar um método depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no método executar() eles devem ser chamados nessa ordem.
 * 
 * Essa Carga:
 * inclui e inicializa o hp.
 * 
 */
public class CargaHP extends CargaBase{
  
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	private static HPAppService hpService;
	
	public CargaHP(){
		
		try {
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Executa a inclusao de hp
	 * e inicializa o mesmo depois.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirHP();
		this.inicializarHP();
		return true;
	}
	
	public void incluirHP() throws AplicacaoException{

		PerioPM perioPMInicial = null;
		PerioPM perioPMFinal = null;
		
		PerioPAP perioPAPInicial = null;
		PerioPAP perioPAPFinal = null;
		
			
		perioPMInicial = perioPMService.recuperaPerioPMPorPeriodoPM(1);
		perioPMFinal = perioPMService.recuperaPerioPMPorPeriodoPM(10);
		
		perioPAPInicial = perioPAPService.recuperaPerioPAPPorPeriodoPAP(perioPMInicial.getPeriodoPM());
		perioPAPFinal = perioPAPService.recuperaPerioPAPPorPeriodoPAP(perioPMFinal.getPeriodoPM());
			
		 
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
	
	
	
	public void inicializarHP() throws AplicacaoException{
		hpService.iniciaPlanejamento();
	}


	
	
	

}
