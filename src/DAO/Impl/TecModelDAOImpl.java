 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;


import modelo.TecModel;
import DAO.TecModelDAO;
import DAO.generico.JPADaoGenerico;

public abstract class TecModelDAOImpl
extends JPADaoGenerico<TecModel, Long> implements TecModelDAO {

    public TecModelDAOImpl()
    { 	super(TecModel.class); 
    }
}
