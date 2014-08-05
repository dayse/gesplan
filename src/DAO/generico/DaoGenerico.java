 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.generico;

import java.io.Serializable;

import DAO.exception.ObjetoNaoEncontradoException;


/**
 * A interface GenericDao b�sica com os m�todos CRUD. Os m�todos
 * de busca s�o adicionados por heran�a de interface.
 * 
 * Nesta classe sao definidos os metodos completamente genericos
 */
public interface DaoGenerico<T, PK extends Serializable>
{
    T inclui(T obj);

    T getPorId(PK id) throws ObjetoNaoEncontradoException;

    T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException;

    void altera(T obj);

    void exclui(T obj);
}
