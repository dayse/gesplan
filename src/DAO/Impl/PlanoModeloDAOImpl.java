 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.PlanoModelo;
import DAO.PlanoModeloDAO;
import DAO.generico.JPADaoGenerico;


public abstract class PlanoModeloDAOImpl extends JPADaoGenerico<PlanoModelo, Long> implements PlanoModeloDAO
{
	public PlanoModeloDAOImpl() {	
		super(PlanoModelo.class);		
	}
}
