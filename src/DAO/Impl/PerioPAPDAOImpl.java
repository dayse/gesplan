 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.PerioPAP;
import DAO.PerioPAPDAO;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles m�todos que s�o espec�ficos,
 * ou que ainda n�o foram generalizados
 * 
 * @author daysemou
 *
 */
public abstract class PerioPAPDAOImpl
       extends JPADaoGenerico<PerioPAP, Long> implements PerioPAPDAO 
{   
    public PerioPAPDAOImpl()
    { 	super(PerioPAP.class); 
    }
    
}
    
