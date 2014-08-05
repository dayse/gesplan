 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.Calendar;
import java.util.GregorianCalendar;

import modelo.Parametros;

import service.ParametrosAppService;
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
 * Inicializa os parametros com valores default.
 * Detalhe ao fato de que ele faz o intervalo de tempo congelado = 1
 * 
 * @author felipe.arruda
 *
 */
public class CargaParametros extends CargaBase{
	
	// Services
	private static ParametrosAppService parametroService;
	
	public CargaParametros(){

		try {
			parametroService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executa a inclusao de parametros.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirParametros();
		return true;
	}
	
	/**
	 * Inclusao dos valores default para os campos da tabela de parametros
	 * @throws AplicacaoException
	 */
	public void incluirParametros() throws AplicacaoException{
		
		Parametros parametro = new Parametros();
		
		parametro.setInicPlanejamento(false);
		parametro.setMargemSeguranca(97.0);
		parametro.setNumIntervalosFixos(1);
		parametro.setPercentualDePerda(5);
		parametro.setDataEstqInic(new GregorianCalendar());
		
		parametroService.inclui(parametro);
	}


}
