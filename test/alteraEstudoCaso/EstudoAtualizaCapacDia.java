 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

import java.util.List;

import modelo.CapacDia;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.CapacDiaAppService;
import service.controleTransacao.FabricaDeAppService;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * Atualiza capadia do periodo 10(Producao DIaria em min) para o mesmo valor do periodo anterior.
 * 
 * @author felipe.arruda
 * 
 */
public class EstudoAtualizaCapacDia {

	private static CapacDiaAppService capacDiaService;
	private CapacDia capacDiaAnterior;
	private CapacDia capacDiaCorrente;
	
	@BeforeClass
	  public void setupClass() throws ObjetoNaoEncontradoException{
		 try {
				capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			} catch (Exception e) {	
				e.printStackTrace();
			}
			List<CapacDia> capacDias = capacDiaService.recuperaListaDeCapacDias();
			//encontra quem é o capacdia do periopm 9 e do 10
			for (CapacDia capacDia : capacDias){
				if(capacDia.getPerioPM().getPeriodoPM() == 9)
					capacDiaAnterior = capacDia;
				if(capacDia.getPerioPM().getPeriodoPM() == 10)
					capacDiaCorrente = capacDia;
			}
			
			
		}
	
	/**
	 * Altera o capacDiaCorrente(do periodo 10) para ficar com o memso valor de 
	 * producao diaria em min do periodo anterior(periodo 9)
	 */
	@Test
	public void alteraProducaoDiariaMinPeriodo10(){
		double novaCapacDiaria = capacDiaAnterior.getCapacProdDiariaEmMin();
		capacDiaCorrente.setCapacProdDiariaEmMin(novaCapacDiaria);
		capacDiaService.altera(capacDiaCorrente);
		System.out.println(">>>>alteraProducaoDiariaMinPeriodo10");
	}
		
	

	}

