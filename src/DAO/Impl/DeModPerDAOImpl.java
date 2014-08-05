 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import DAO.DeModPerDAO;
import DAO.generico.JPADaoGenerico;
import modelo.DeModPer;

public abstract class DeModPerDAOImpl 
	extends JPADaoGenerico<DeModPer, Long> implements DeModPerDAO
{
	public DeModPerDAOImpl() 
	{	super(DeModPer.class);		
	}
}
