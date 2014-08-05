 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.PerioPAPVig;
import DAO.PerioPAPVigDAO;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles m�todos que s�o espec�ficos,
 * ou que ainda n�o foram generalizados
 * 
 * @author daysemou
 *
 */
public abstract class PerioPAPVigDAOImpl
       extends JPADaoGenerico<PerioPAPVig, Long> implements PerioPAPVigDAO 
{   
    public PerioPAPVigDAOImpl()
    { 	super(PerioPAPVig.class); 
    }
    
}
    
