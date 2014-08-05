 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;


import modelo.DeFamPer;
import DAO.DeFamPerDAO;
import DAO.generico.JPADaoGenerico;

public abstract class DeFamPerDAOImpl 
       extends JPADaoGenerico<DeFamPer, Long> implements DeFamPerDAO
{
	public DeFamPerDAOImpl() 
	{	super(DeFamPer.class);		
	}
}
