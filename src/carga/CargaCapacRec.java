 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.List;

import modelo.CapacRec;
import service.CapacRecAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

/**
 * 
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
 * Nesse ponto os capacrecs ja foram add pela inicializacao do hp
 * entao apenas altera seus valores
 * 
 * @author felipe.arruda
 *
 */
public class CargaCapacRec extends CargaBase{
	
	private static CapacRecAppService capacRecService;
	
	public CargaCapacRec(){
		
		 try {
			 
			 capacRecService = FabricaDeAppService.getAppService(CapacRecAppService.class);		
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		
		

		/**
		 * Executa a inclusao de capacRecs.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluirCapacRecs();
			return true;
		}
		
		/**
		 * Nesse ponto os capacrecs ja foram add pela inicializacao do hp
		 * entao apenas altera seus valores
		 */
		public void incluirCapacRecs() throws AplicacaoException{

			List<CapacRec> listaCapacRecs = capacRecService.recuperaListaDeCapacRecsComRecursosEPerioPMs();
			
			for(CapacRec capacRec : listaCapacRecs){
				if(capacRec.getRecurso().getCodRecurso().equals("01")){

					capacRec.setCapacDiaria(1800.0);
				}
				else if(capacRec.getRecurso().getCodRecurso().equals("02")){

					capacRec.setCapacDiaria(1700.0);
				}
				else if(capacRec.getRecurso().getCodRecurso().equals("03")){

					capacRec.setCapacDiaria(2900.0);
				}
				
				capacRecService.altera(capacRec);
			}

			
		}
		
		
		

	}
