 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.List;

import modelo.PerioPAP;
import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

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
 * Executa a verificacao se os periodos paps existem, e agrupa-os de 2 em 2, para periodo pm.
 * 
 * @author felipe.arruda
 *
 */
public class CargaEstudoAgrupaPeriodo extends CargaBase{
	
	private static PerioPMAppService perioPMService;
	private static PerioPAPAppService perioPAPService;
	
	
	  public CargaEstudoAgrupaPeriodo(){
		 try {
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			 perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}

		/**
		 * Executa a verificacao se os periodos paps existem, e agrupa-os de 2 em 2, para periodo pm.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.verificaPeriodosPAP();
			this.agruparPeriodosPAP2a2();
			return true;
		}
		
		/**
		 * 
		 * metodo de auxilio que retorna true ou false se a lista de periodos pap for vazia ou n�o
		 *	True se n�o for vazia e false se for vazia.
		 * @return
		 */
		public void verificaPeriodosPAP() throws AplicacaoException{
			List<PerioPAP>  ListaDePeriodosPAP = perioPAPService.recuperaListaDePerioPAPs();
			if(ListaDePeriodosPAP.isEmpty()){
				throw new AplicacaoException("Periodos PAP inexistentes.");
			}
		}
	
		/**
		 * Agrupa periodo PAP de 2 em 2 perioPMs.
		 * Verifica antes de o numero de perioPMs � divisivel por 2.
		 * Se n�o, for joga exception 
		 * @throws AplicacaoException 
		 */
		public void agruparPeriodosPAP2a2() throws AplicacaoException{
			
			int numPerioPMs = perioPMService.recuperaListaDePerioPMs().size();
			if(numPerioPMs % 2 != 0){
				throw new AplicacaoException("Numero de periodos PM n�o compativel com agregador.");
			}
			perioPAPService.agrupaPerioPAPs(2);
			
		}
	


	}

