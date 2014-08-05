 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.HP;
import DAO.HPDAO;
import DAO.generico.JPADaoGenerico;

public abstract class HPDAOImpl extends JPADaoGenerico<HP, Long> implements HPDAO 
{   
    public HPDAOImpl() { 	
    	super(HP.class); 
    }
}
    
