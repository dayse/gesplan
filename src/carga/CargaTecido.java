 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.ArrayList;
import java.util.List;

import modelo.Tecido;

import service.TecidoAppService;
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
 * Inclui tecidos
 * 
 * @author felipe.arruda
 *
 */
public class CargaTecido extends CargaBase{
  
	private static TecidoAppService tecidoService;
		
	public CargaTecido(){
		
		 try {
			 
			 tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
			
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}

		/**
		 * Executa a inclusao de tecidos.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluirTecidos();
			return true;
		}
	
		
		public void incluirTecidos() throws AplicacaoException{
			
					
			//-------------------- TECIDO 1 ---------------------//
			Tecido tecido1 = new Tecido();
			tecido1.setCodTecido("01");
			tecido1.setDescrTecido("algodao");
			tecido1.setUM("m");
			tecido1.setLeadTimeUnidade2(5.0);
			tecido1.setFatorDeRendimento(10.0);
			tecido1.setProducaoDiariaMaxUnidade2(40.0);
			tecido1.setCustoUnit(30.0);

			//-------------------- TECIDO 2 ---------------------//
			Tecido tecido2 = new Tecido();
			tecido2.setCodTecido("02");
			tecido2.setDescrTecido("linho");
			tecido2.setUM("m");
			tecido2.setLeadTimeUnidade2(11.0);
			tecido2.setFatorDeRendimento(20.0);
			tecido2.setProducaoDiariaMaxUnidade2(20.0);
			tecido2.setCustoUnit(40.0);

			//-------------------- TECIDO 3 ---------------------//
			Tecido tecido3 = new Tecido();
			tecido3.setCodTecido("03");
			tecido3.setDescrTecido("seda");
			tecido3.setUM("m");
			tecido3.setLeadTimeUnidade2(10.0);
			tecido3.setFatorDeRendimento(30.0);
			tecido3.setProducaoDiariaMaxUnidade2(8.0);
			tecido3.setCustoUnit(100.0);

			//-------------------- TECIDO 4 ---------------------//
			Tecido tecido4 = new Tecido();
			tecido4.setCodTecido("04");
			tecido4.setDescrTecido("nylon");
			tecido4.setUM("m");
			tecido4.setLeadTimeUnidade2(4.0);
			tecido4.setFatorDeRendimento(4.0);
			tecido4.setProducaoDiariaMaxUnidade2(50.0);
			tecido4.setCustoUnit(10.0);
			

			
			
			// -------------- LISTA DE TECIDOS -----------------// 
			List<Tecido> tecidos = new ArrayList<Tecido>();
		
			tecidos.add(tecido1);
			tecidos.add(tecido2);
			tecidos.add(tecido3);
			tecidos.add(tecido4);
			
			//-------------- INCLUSAO DE TECIDOS ---------------//
			for (Tecido tecido : tecidos) {
				
				tecidoService.inclui(tecido);
				
			}
			
			
		}
		
		
		

	}
