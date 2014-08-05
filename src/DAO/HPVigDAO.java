 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO;

import java.util.List;

import modelo.HPVig;
import DAO.anotacao.RecuperaLista;
import DAO.generico.DaoGenerico;

public interface HPVigDAO extends DaoGenerico<HPVig, Long> {

@RecuperaLista
public List<HPVig> recuperaListaDeHPVig();

}
