 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import modelo.PerioPM;
import service.PerioPAPAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

/**
 * Sobre a Carga:
 * É uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o método executar().
 * Nesse método executar é que é chamado os outros métodos que são
 * as etápas dessa carga.
 * Portanto se é necessario rodar um método depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no método executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas é preciso retornar true.
 * Se houver algum problema(exceção) na execução de um das etapas, essa exceção deve ser lancada
 * 
 * Essa Carga:
 * Inclui perioPMs, do 1 ao 10 junto com perioPAPs.
 * 
 * @author felipe.arruda
 *
 */
public class CargaPerioPM extends CargaBase{
	
	private static PerioPAPAppService perioPapAppService;
 
	public CargaPerioPM() {		
		try {
			perioPapAppService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	

	/**
	 * Inclui perioPMs, do 1 ao 10 junto com perioPAPs.
	 * Iniciando em 1º de julho ( Ou seja define periodos do primeiro ciclo de planejamento
	 * 
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirPerioPMs();
		return true;
	}
	
	
	public void incluirPerioPMs()throws AplicacaoException{
		
       // ---------------- PERIOPM 1 -----------------//
		Calendar dataInicial1 = new GregorianCalendar(2011,Calendar.JULY,1);
		
		Calendar dataFinal1 = new GregorianCalendar(2011,Calendar.JULY,16);
		
		PerioPM perioPM1 = new PerioPM();

		perioPM1.setPeriodoPM(1);
		perioPM1.setDataInicial(dataInicial1);
		perioPM1.setDataFinal(dataFinal1);
		perioPM1.setNumDiasUteisMatriz(10.5);
		perioPM1.setNumDiasUteisU2(13);

		
		// ---------------- PERIOPM 2 -----------------//
		Calendar dataInicial2 = new GregorianCalendar(2011,Calendar.JULY,17);
		
		Calendar dataFinal2 = new GregorianCalendar(2011,Calendar.JULY,31);
		
		PerioPM perioPM2 = new PerioPM();
				
		perioPM2.setPeriodoPM(2);
		perioPM2.setDataInicial(dataInicial2);
		perioPM2.setDataFinal(dataFinal2);
		perioPM2.setNumDiasUteisMatriz(10);
		perioPM2.setNumDiasUteisU2(12);
		
		 
		// ---------------- PERIOPM 3 -----------------//
		Calendar dataInicial3 = new GregorianCalendar(2011,Calendar.AUGUST,01);
		
		Calendar dataFinal3 = new GregorianCalendar(2011,Calendar.AUGUST,16);
				
		PerioPM perioPM3 = new PerioPM();
		
		perioPM3.setPeriodoPM(3);
		perioPM3.setDataInicial(dataInicial3);
		perioPM3.setDataFinal(dataFinal3);
		perioPM3.setNumDiasUteisMatriz(11.5);
		perioPM3.setNumDiasUteisU2(13);
		
		// ---------------- PERIOPM 4 -----------------//
		Calendar dataInicial4 = new GregorianCalendar(2011,Calendar.AUGUST,17);
		
		Calendar dataFinal4 = new GregorianCalendar(2011,Calendar.AUGUST,31);
		
		PerioPM perioPM4 = new PerioPM();
		
		perioPM4.setPeriodoPM(4);
		perioPM4.setDataInicial(dataInicial4);
		perioPM4.setDataFinal(dataFinal4);
		perioPM4.setNumDiasUteisMatriz(11);
		perioPM4.setNumDiasUteisU2(13);

		
		// ---------------- PERIOPM 5 -----------------//
		Calendar dataInicial5 = new GregorianCalendar(2011,Calendar.SEPTEMBER,01);
		
		Calendar dataFinal5 = new GregorianCalendar(2011,Calendar.SEPTEMBER,15);
		
		PerioPM perioPM5 = new PerioPM();
		
		perioPM5.setPeriodoPM(5);
		perioPM5.setDataInicial(dataInicial5);
		perioPM5.setDataFinal(dataFinal5);
		perioPM5.setNumDiasUteisMatriz(9.5);
		perioPM5.setNumDiasUteisU2(13);

		// ---------------- PERIOPM 6 -----------------//
		Calendar dataInicial6  = new GregorianCalendar(2011,Calendar.SEPTEMBER,16);
		
		Calendar dataFinal6 = new GregorianCalendar(2011,Calendar.SEPTEMBER,30);
		
		PerioPM perioPM6 = new PerioPM();
		
		perioPM6.setPeriodoPM(6);
		perioPM6.setDataInicial(dataInicial6);
		perioPM6.setDataFinal(dataFinal6);
		perioPM6.setNumDiasUteisMatriz(10);
		perioPM6.setNumDiasUteisU2(14);

		
		// ---------------- PERIOPM 7 -----------------//
		Calendar dataInicial7 = new GregorianCalendar(2011,Calendar.OCTOBER,01);
		
		Calendar dataFinal7 = new GregorianCalendar(2011,Calendar.OCTOBER,16);
		
		PerioPM perioPM7 = new PerioPM();
		
		perioPM7.setPeriodoPM(7);
		perioPM7.setDataInicial(dataInicial7);
		perioPM7.setDataFinal(dataFinal7);
		perioPM7.setNumDiasUteisMatriz(9.5);
		perioPM7.setNumDiasUteisU2(12);

		// ---------------- PERIOPM 8 -----------------//
		Calendar dataInicial8 = new GregorianCalendar(2011,Calendar.OCTOBER,17);
		
		Calendar dataFinal8 = new GregorianCalendar(2011,Calendar.OCTOBER,31);
		
		PerioPM perioPM8 = new PerioPM();
		
		perioPM8.setPeriodoPM(8);
		perioPM8.setDataInicial(dataInicial8);
		perioPM8.setDataFinal(dataFinal8);
		perioPM8.setNumDiasUteisMatriz(10);
		perioPM8.setNumDiasUteisU2(13);

		
		
		// ---------------- PERIOPM 9 -----------------//
		Calendar dataInicial9 = new GregorianCalendar(2011,Calendar.NOVEMBER,01);
		
		Calendar dataFinal9 = new GregorianCalendar(2011,Calendar.NOVEMBER,15);
		
		PerioPM perioPM9 = new PerioPM();
		
		perioPM9.setPeriodoPM(9);
		perioPM9.setDataInicial(dataInicial9);
		perioPM9.setDataFinal(dataFinal9);
		perioPM9.setNumDiasUteisMatriz(9.5);
		perioPM9.setNumDiasUteisU2(12);

		// ---------------- PERIOPM 10 -----------------//
		Calendar dataInicial10 = new GregorianCalendar(2011,Calendar.NOVEMBER,16);
		
		Calendar dataFinal10 = new GregorianCalendar(2011,Calendar.NOVEMBER,30);
		
		PerioPM perioPM10 = new PerioPM();
		
		perioPM10.setPeriodoPM(10);
		perioPM10.setDataInicial(dataInicial10);
		perioPM10.setDataFinal(dataFinal10);
		perioPM10.setNumDiasUteisMatriz(10);
		perioPM10.setNumDiasUteisU2(12);
		
				
		
		// -------------- Lista dos PerioPMs criados --------------// 
		List<PerioPM> perioPMs = new ArrayList<PerioPM>();
		
		perioPMs.add(perioPM1);
		perioPMs.add(perioPM2);
		perioPMs.add(perioPM3);
		perioPMs.add(perioPM4);
		perioPMs.add(perioPM5);
		perioPMs.add(perioPM6);
		perioPMs.add(perioPM7);
		perioPMs.add(perioPM8);
		perioPMs.add(perioPM9);
		perioPMs.add(perioPM10);
		
		//-------------- INCLUSAO DE PERIOPMS ---------------//
		for (PerioPM perioPM : perioPMs) {
			perioPapAppService.incluiComPerioPM(perioPM);
		}
					
	}


}
