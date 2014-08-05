 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

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
 * Sobre a Carga:
 * � uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o m�todo executar().
 * Nesse m�todo executar � que � chamado os outros m�todos que s�o
 * as et�pas dessa carga.
 * Portanto se � necessario rodar um m�todo depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no m�todo executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas � preciso retornar true.
 * Se houver algum problema(exce��o) na execu��o de um das etapas, essa exce��o deve ser lancada
 * 
 * Essa Carga:
 * Exclui o primeiro periodo, e depois inclui o ultimo.
 * Esse ultimo periodo � incluido com 10 dias uteis na matriz
 * e 12.5 dias uteis na u2
 * do dia 01/12/2011 - 15/12/2011
 * 
 * @author felipe.arruda e bruno.oliveira
 *
 */
public class CargaEstudoDefasaPeriodo extends CargaBase{
	
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	private static int OPCAO_EXCLUIR_PRIMEIRO=0;
	private PerioPM perioPM = new PerioPM();
	
	  public CargaEstudoDefasaPeriodo(){
		 try {
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			 perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		

		/**
		 * Executa a exclus�o do primeiro periodo e depois a inclusao
		 * do ultimo.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.excluiPrimeiroPeriodo();
			this.incluiUltimoPeriodo();
			return true;
		}
		
		/**
		 * exclui o primeiro periodo
		 */		
		public void excluiPrimeiroPeriodo(){
			if (perioPMService.recuperaListaDePerioPMs().isEmpty()){
				System.out.println("ERRO: N�o existem planos cadastrados.");
			}
			try {
				perioPMService.excluirPrimeiroUltimoOuTodos(OPCAO_EXCLUIR_PRIMEIRO);
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 
		 * Esse ultimo periodo � incluido com 10 dias uteis na matriz
		 * e 12.5 dias uteis na u2
		 * do dia 01/12/2011 - 15/12/2011
		 */
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

