 
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
import modelo.PerioPM;
import modelo.Recurso;
import service.CapacRecAppService;
import service.PerioPMAppService;
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
 * Atualiza CapacRec do periodo 10(Capacidade diaria) para o mesmo valor do
 * periodo anterior, para cada recurso.
 * 
 * @author felipe.arruda
 * 
 */
public class CargaEstudoAtualizaCapacRec extends CargaBase{

	private static CapacRecAppService capacRecService;
	private static RecursoAppService recursoService;
	private static PerioPMAppService perioPMService;

	private static List<Recurso> recursos;
	private static PerioPM penultimoPeriodo;
	private static PerioPM ultimoPeriodo ;
	
	public  CargaEstudoAtualizaCapacRec() throws AplicacaoException {
		try {
			capacRecService = FabricaDeAppService
					.getAppService(CapacRecAppService.class);
			recursoService = FabricaDeAppService
					.getAppService(RecursoAppService.class);
			perioPMService = FabricaDeAppService
					.getAppService(PerioPMAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		recursos = recursoService.recuperaListaDeRecursos();
		penultimoPeriodo = perioPMService
				.recuperaPerioPMPorPeriodoPM(9);
		ultimoPeriodo = perioPMService.recuperaPerioPMPorPeriodoPM(10);


	}


	/**
	 * altera a Producao Diaria Do Recurso Do Periodo 10
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.alteraProducaoDiariaDoRecursoDoPeriodo10();
		return true;
	}
	
	/**
	 * prepara a lista de CapacRecs do penultimo e do ultimo periodo, para
	 * cada recurso
	 * depois altera o capacRec do ultimo para que seu valor de Capacidade
	 * Diaria fique
	 * igual ao do penultimo.
	 * @throws AplicacaoException 
	 */
	public void alteraProducaoDiariaDoRecursoDoPeriodo10() throws AplicacaoException{

		for (Recurso recurso : recursos) {
			CapacRec capacRecAnterior = capacRecService
					.recuperaCapacRecPorRecursoEPerioPM(recurso,
							penultimoPeriodo);
			
			CapacRec capacRecCorrente = capacRecService
					.recuperaCapacRecPorRecursoEPerioPM(recurso, ultimoPeriodo);
			
			// altera o valor do ultimo para ficar igual ao do penultimo
			capacRecCorrente.setCapacDiaria(capacRecAnterior.getCapacDiaria());

			// salva no banco
			capacRecService.altera(capacRecCorrente);

		}
	}


}
