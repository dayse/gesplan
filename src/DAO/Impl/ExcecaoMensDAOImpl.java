 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.ExcecaoMens;
import DAO.ExcecaoMensDAO;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles métodos que são específicos,
 * ou que ainda não foram generalizados
 * 
 * @author felipe.arruda
 *
 */
public abstract class ExcecaoMensDAOImpl
       extends JPADaoGenerico<ExcecaoMens, Long> implements ExcecaoMensDAO 
{   
    public ExcecaoMensDAOImpl()
    { 	super(ExcecaoMens.class); 
    }
    
}
    
