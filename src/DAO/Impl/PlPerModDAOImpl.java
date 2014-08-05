 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.CadPlan;
import modelo.PlPerMod;
import DAO.CadPlanDAO;
import DAO.PlPerModDAO;
import DAO.generico.JPADaoGenerico;

public abstract class PlPerModDAOImpl extends JPADaoGenerico<PlPerMod, Long> implements PlPerModDAO {
	
    public PlPerModDAOImpl(){ 	
    	super(PlPerMod.class); 
    }
}
    
