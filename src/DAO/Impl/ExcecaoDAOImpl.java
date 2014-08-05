 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.Excecao;
import DAO.ExcecaoDAO;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles m�todos que s�o espec�ficos,
 * ou que ainda n�o foram generalizados
 * 
 * @author felipe.arruda
 *
 */
public abstract class ExcecaoDAOImpl
       extends JPADaoGenerico<Excecao, Long> implements ExcecaoDAO 
{   
    public ExcecaoDAOImpl()
    { 	super(Excecao.class); 
    }
    
}
    
