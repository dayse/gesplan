 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;


import modelo.Tecido;
import DAO.TecidoDAO;
import DAO.generico.JPADaoGenerico;

public abstract class TecidoDAOImpl
extends JPADaoGenerico<Tecido, Long> implements TecidoDAO {
   
   	public TecidoDAOImpl()
    { 	super(Tecido.class); 
    }
}
