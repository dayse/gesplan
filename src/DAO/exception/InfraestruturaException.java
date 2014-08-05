 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.exception;

public class InfraestruturaException extends RuntimeException
{	
	private final static long serialVersionUID = 1L;
	
	public InfraestruturaException(Exception e)
	{	super(e);
	}

	public InfraestruturaException(String msg)
	{	super(msg);
	}
}	
