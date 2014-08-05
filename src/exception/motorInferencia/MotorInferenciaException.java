 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package exception.motorInferencia;
/**
 * Exception levantada que indica algum erro no motor de inferencia.
 * Ex:
 * Arquivo de modelagem com variaveis erradas ou faltando,
 * Utilização de modelagens com finalidade incompativel com o que está sendo pedido
 * pelo sistema.
 * @author arruda
 *
 */
public class MotorInferenciaException extends Exception {

	private static final long serialVersionUID = 1L;

	public MotorInferenciaException() {
    }

    public MotorInferenciaException(String msg) {
        super(msg);
    }

    public MotorInferenciaException(Throwable t) {
        super(t);
    }

    public MotorInferenciaException(String msg, Throwable t) {
        super(msg, t);
    }
}
