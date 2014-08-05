 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.HPVig;
import DAO.HPVigDAO;
import DAO.generico.JPADaoGenerico;

public abstract class HPVigDAOImpl extends JPADaoGenerico<HPVig, Long> implements HPVigDAO 
{   
    public HPVigDAOImpl() { 	
    	super(HPVig.class); 
    }
}
    
