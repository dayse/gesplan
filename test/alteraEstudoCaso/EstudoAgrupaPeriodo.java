 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

import java.util.List;

import modelo.PerioPAP;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * 
 * @author felipe.arruda
 * 
 */
public class EstudoAgrupaPeriodo {
	
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	
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
		 * metodo de auxilio que retorna true ou false se a lista de periodos pap for vazia ou não
		 *	True se não for vazia e false se for vazia.
		 * @return
		 */
		@Test
		public void verificaPeriodosPAP() throws AplicacaoException{
			List<PerioPAP>  ListaDePeriodosPAP = perioPAPService.recuperaListaDePerioPAPs();
			if(ListaDePeriodosPAP.isEmpty()){
				throw new AplicacaoException("Periodos PAP inexistentes.");
			}
		}
	
		/**
		 * Agrupa periodo PAP de 2 em 2 perioPMs.
		 * Verifica antes de o numero de perioPMs é divisivel por 2.
		 * Se não, for joga exception 
		 * @throws AplicacaoException 
		 */
		@Test(dependsOnMethods="verificaPeriodosPAP")
		public void agruparPeriodosPAP2a2() throws AplicacaoException{
			
			int numPerioPMs = perioPMService.recuperaListaDePerioPMs().size();
			if(numPerioPMs % 2 != 0){
				throw new AplicacaoException("Numero de periodos PM não compativel com agregador.");
			}
			perioPAPService.agrupaPerioPAPs(2);
			
			System.out.println(">>>>agruparPeriodosPAP2a2");
		}
	


	}

