 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import modelo.RecModel;
import DAO.RecModelDAO;
import DAO.generico.JPADaoGenerico;


/**
 * As classes DAOImpl implementam aqueles métodos que são específicos daquela entidade,
 * ou que ainda não foram generalizados
 * 

 * @author Felipe
 *
 */
public abstract class RecModelDAOImpl
extends JPADaoGenerico<RecModel, Long> implements RecModelDAO {

    public RecModelDAOImpl()
    { 	super(RecModel.class); 
    }
}
