 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.TipoUsuario;
import DAO.TipoUsuarioDAO;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles métodos que são específicos,
 * ou que ainda não foram generalizados
 * 
 * @author daysemou
 *
 */
public abstract class TipoUsuarioDAOImpl
       extends JPADaoGenerico<TipoUsuario, Long> implements TipoUsuarioDAO 
{   
    public TipoUsuarioDAOImpl()
    { 	super(TipoUsuario.class); 
    }
}
    
