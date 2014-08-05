 
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

import modelo.RecModel;
import service.ModeloAppService;
import service.RecModelAppService;
import service.RecursoAppService;
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
 * Inclui recmodels.
 * 
 * @author felipe.arruda
 *
 */
public class CargaRecModel extends CargaBase{
	
	private static RecursoAppService recursoService;
	private static ModeloAppService modeloService;
	private static RecModelAppService recModelService;
	
	public CargaRecModel(){
		
		 try {
			 recursoService = FabricaDeAppService.getAppService(RecursoAppService.class);
			 modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			 recModelService = FabricaDeAppService.getAppService(RecModelAppService.class);
			
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		

		/**
		 * Executa a inclusao de recmodels.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluirRecModels();
			return true;
		}
		
		public void incluirRecModels() throws AplicacaoException{
			
					
			//-------------------- RECMODEL 1 ---------------------//
			RecModel recModel1 = new RecModel();
			recModel1.setConsumoUnit(5.0);
			recModel1.setModelo(modeloService.recuperaModeloPorCodigo("121131"));
			recModel1.setRecurso(recursoService.recuperaRecursoPeloCodigo("01"));

			//-------------------- RECMODEL 2 ---------------------//
			RecModel recModel2 = new RecModel();
			recModel2.setConsumoUnit(7.0);
			recModel2.setModelo(modeloService.recuperaModeloPorCodigo("121451"));
			recModel2.setRecurso(recursoService.recuperaRecursoPeloCodigo("02"));
			
			//-------------------- RECMODEL 3 ---------------------//
			RecModel recModel3 = new RecModel();
			recModel3.setConsumoUnit(9.7);
			recModel3.setModelo(modeloService.recuperaModeloPorCodigo("129508"));
			recModel3.setRecurso(recursoService.recuperaRecursoPeloCodigo("03"));
			
			
			
			
			// -------------- LISTA DE RECMODELS -----------------// 
			List<RecModel> recmodels = new ArrayList<RecModel>();
		
			recmodels.add(recModel1);
			recmodels.add(recModel2);
			recmodels.add(recModel3);
			
			//-------------- INCLUSAO DE RECMODELS ---------------//
			for (RecModel recmodel : recmodels) {
				
				recModelService.inclui(recmodel);
				
			}
			
			
		}
		
		
		

	}
