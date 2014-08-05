 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.Modelo;
import DAO.ModeloDAO;
import DAO.generico.JPADaoGenerico;


public abstract class ModeloDAOImpl 
       extends JPADaoGenerico<Modelo, Long> implements ModeloDAO
{
	public ModeloDAOImpl() 
	{	super(Modelo.class);		
	}
			
}
