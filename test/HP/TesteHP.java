 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package HP;

import java.util.Calendar;
import java.util.List;

import modelo.HP;
import modelo.PerioPM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.HPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class TesteHP {
  
	// Services
	private static HPAppService hpService;
	private static PerioPMAppService periodoService;
	
	
	@BeforeClass
	public void setupClass() {
		
		try {

			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			hpService = FabricaDeAppService.getAppService(HPAppService.class);
			periodoService = FabricaDeAppService.getAppService(PerioPMAppService.class);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void incluirHP() throws AplicacaoException{
		
		List<PerioPM> periodos = periodoService.recuperaListaDePerioPMs();
		
		String dia;
		String mes;
		String ano;
		
		for (PerioPM perioPM : periodos) {
			
			dia = Integer.toString(perioPM.getDataInicial().get(Calendar.DAY_OF_MONTH));
			mes = Integer.toString(perioPM.getDataInicial().get(Calendar.MONTH) + 1);
			ano = Integer.toString(perioPM.getDataInicial().get(Calendar.YEAR));
			
			if (dia.length() < 2){
				dia = "0" + dia;
			}
			
			if (mes.length() < 2){
				mes = "0" + mes;
			}
			
			String dataString =  dia + "/" + mes + "/" + ano; 
			System.out.println("DATA-INICIAL = " + dataString);
		}
		
		
		HP hp = new HP();
		
		// Delimitando o intervalo de periodos do PMP
		hp.setPerioPMInicPMP(periodos.get(0));
		hp.setPerioPMFinalPMP(periodos.get(2));
		
		// Delimitando o intervalo de periodos de DEMODPER
		hp.setPerioPMInicDemMod(periodos.get(3));
		hp.setPerioPMFinalDemMod(periodos.get(5));
		
		//hpService.inclui(hp);
	}

}
