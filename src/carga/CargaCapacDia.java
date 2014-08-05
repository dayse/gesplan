 
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
import modelo.HP;
import modelo.PerioPM;


import DAO.HPDAO;
import DAO.ParametrosDAO;

import service.CapacDiaAppService;
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
 * Inclui lista de capacDias com VALOR_PADRAO_CAPACIDADE = 25000
 * 
 * @author felipe.arruda
 *
 */
public class CargaCapacDia extends CargaBase{
	
	private static CapacDiaAppService capacDiaService;
	private static PerioPMAppService perioPMService;
	private static HPDAO hpDAO;
	private static ParametrosDAO parametrosDAO;
	
	public CargaCapacDia(){
		
		 try {
			 capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);			
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}

		/**
		 * Inclui capacdias.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluirCapacDias();
			return true;
		}
		
		/**
		 * Nesse ponto os capacdias ja foram add pela inicializacao do hp
		 * entao apenas altera seus valores
		 */
		public void incluirCapacDias() throws AplicacaoException{
			double VALOR_PADRAO_CAPACIDADE = 25000;

			List<CapacDia> listaCapacDias = capacDiaService.recuperaListaDeCapacDias();
			
			for(CapacDia capacDia : listaCapacDias){
				capacDia.setCapacProdDiariaEmMin(VALOR_PADRAO_CAPACIDADE);
				capacDiaService.altera(capacDia);
			}

			
		}


		
		
		

	}
