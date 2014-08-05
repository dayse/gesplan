 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;


import service.exception.AplicacaoException;

/**
 * Carga para ser usada como base, pois possui o method executar.
 * 
 * Nesse m�todo executar � que � chamado os outros m�todos que s�o
 * as et�pas dessa carga.
 * Portanto se � necessario rodar um m�todo depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no m�todo executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas � preciso retornar true.
 * Se houver algum problema(exce��o) na execu��o de um das etapas, essa exce��o deve ser lancada
 * 
 * @author felipe.arruda
 *
 */
public abstract class CargaBase {
	/**
	 * Executa m�todos dessa carga.
	 * @return
	 * @throws AplicacaoException
	 */
	public abstract boolean executar() throws AplicacaoException;
}
