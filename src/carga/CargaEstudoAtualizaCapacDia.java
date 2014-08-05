 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.List;

import modelo.CapacDia;
import service.CapacDiaAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.exception.ObjetoNaoEncontradoException;

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
 * Atualiza capadia do periodo 10(Producao DIaria em min) para o mesmo valor do periodo anterior.
 * 
 * @author felipe.arruda
 * 
 */
public class CargaEstudoAtualizaCapacDia extends CargaBase{

	private static CapacDiaAppService capacDiaService;
	private CapacDia capacDiaAnterior;
	private CapacDia capacDiaCorrente;
	
	  public CargaEstudoAtualizaCapacDia() {
		 try {
				capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			} catch (Exception e) {	
				e.printStackTrace();
			}
			List<CapacDia> capacDias = capacDiaService.recuperaListaDeCapacDias();
			//encontra quem � o capacdia do periopm 9 e do 10
			for (CapacDia capacDia : capacDias){
				if(capacDia.getPerioPM().getPeriodoPM() == 9)
					capacDiaAnterior = capacDia;
				if(capacDia.getPerioPM().getPeriodoPM() == 10)
					capacDiaCorrente = capacDia;
			}
			
			
		}
	
		/**
		 * Altera o capacDiaCorrente
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.alteraProducaoDiariaMinPeriodo10();
			return true;
		}
		
		/**
		 * Altera o capacDiaCorrente(do periodo 10) para ficar com o memso valor de 
		 * producao diaria em min do periodo anterior(periodo 9)
		 */
		public void alteraProducaoDiariaMinPeriodo10(){
			double novaCapacDiaria = capacDiaAnterior.getCapacProdDiariaEmMin();
			capacDiaCorrente.setCapacProdDiariaEmMin(novaCapacDiaria);
			capacDiaService.altera(capacDiaCorrente);
		}
		
	

	}

