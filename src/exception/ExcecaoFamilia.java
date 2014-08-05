 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package exception;
public class ExcecaoFamilia extends Exception
{	
	private static final long serialVersionUID = 1L;
	int codigo;

	public ExcecaoFamilia (String msg)
	{	super(msg);
	}

	public ExcecaoFamilia (int codigo, String msg)
	{	super(msg);
		this.codigo = codigo;
	}
	
	public ExcecaoFamilia ()
	{ }

	public int getCodigoDeErro()
	{	return codigo;
	}
}
