 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.Recurso;
import DAO.RecursoDAO;
import DAO.generico.JPADaoGenerico;


/**
 * As classes DAOImpl implementam aqueles métodos que são específicos daquela entidade,
 * ou que ainda não foram generalizados
 * 

 * @author Felipe
 *
 */
public abstract class RecursoDAOImpl
extends JPADaoGenerico<Recurso, Long> implements RecursoDAO {

    public RecursoDAOImpl()
    { 	super(Recurso.class); 
    }
}
