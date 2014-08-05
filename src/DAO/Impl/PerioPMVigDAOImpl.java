 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.PerioPMVig;
import DAO.PerioPMVigDAO;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles m�todos que s�o espec�ficos,
 * ou que ainda n�o foram generalizados
 * 
 * @author daysemou
 *
 */
public abstract class PerioPMVigDAOImpl
       extends JPADaoGenerico<PerioPMVig, Long> implements PerioPMVigDAO 
{   
    public PerioPMVigDAOImpl()
    { 	super(PerioPMVig.class); 
    }
    
}
    
