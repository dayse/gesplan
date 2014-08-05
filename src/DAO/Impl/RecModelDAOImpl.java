 
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
 * As classes DAOImpl implementam aqueles m�todos que s�o espec�ficos daquela entidade,
 * ou que ainda n�o foram generalizados
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
