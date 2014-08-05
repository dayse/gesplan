 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.exception;

public class ObjetoNaoEncontradoException extends Exception
{	
	private final static long serialVersionUID = 1;
	
	public ObjetoNaoEncontradoException()
	{
	}

	public ObjetoNaoEncontradoException(String msg)
	{	super(msg);
	}
}	
