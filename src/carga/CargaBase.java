 
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
 * Nesse método executar é que é chamado os outros métodos que são
 * as etápas dessa carga.
 * Portanto se é necessario rodar um método depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no método executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas é preciso retornar true.
 * Se houver algum problema(exceção) na execução de um das etapas, essa exceção deve ser lancada
 * 
 * @author felipe.arruda
 *
 */
public abstract class CargaBase {
	/**
	 * Executa métodos dessa carga.
	 * @return
	 * @throws AplicacaoException
	 */
	public abstract boolean executar() throws AplicacaoException;
}
