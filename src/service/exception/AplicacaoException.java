 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service.exception;


@ExcecaoDeAplicacao
public class AplicacaoException extends Exception
{	

	private final static long serialVersionUID = 1;

	private int codigo;

	public AplicacaoException(Exception e) {
		super(e);
	}

	public AplicacaoException() {
	}

	public AplicacaoException(String msg) {
		super(msg);
	}

	public AplicacaoException(int codigo, String msg) {
		super(msg);
		this.codigo = codigo;
	}

	public int getCodigoDeErro() {
		return codigo;
	}

}	
