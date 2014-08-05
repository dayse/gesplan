 
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
 * Atualiza o valor da data referente ao estoque inicial.
 * 
 * @author felipe.arruda
 *
 */
public class CargaDataEstqInic extends CargaBase{
	
	// Services
	private static ParametrosAppService parametroService;
	private GregorianCalendar dataParaAlterar;
	public CargaDataEstqInic(){

		try {
			parametroService = FabricaDeAppService.getAppService(ParametrosAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public CargaDataEstqInic(GregorianCalendar dataParaAlterar){
		this();
		this.dataParaAlterar = dataParaAlterar;
	}
	
	/**
	 * Executa a inclusao de parametros.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.alteraData();
		return true;
	}
	
	/**
	 * Inclusao dos valores default para os campos da tabela de parametros
	 * @throws AplicacaoException
	 */
	public void alteraData() throws AplicacaoException{
		
		Parametros parametro = parametroService.recuperaListaDeParametros().get(0);
		
		parametro.setDataEstqInic(this.dataParaAlterar);
		
		parametroService.altera(parametro);
	}


}
