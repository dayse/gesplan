 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package exception.relatorio;

public class RelatorioException extends Exception {

	private static final long serialVersionUID = 1L;

	public RelatorioException() {
    }

    public RelatorioException(String msg) {
        super(msg);
    }

    public RelatorioException(Throwable t) {
        super(t);
    }

    public RelatorioException(String msg, Throwable t) {
        super(msg, t);
    }
}
