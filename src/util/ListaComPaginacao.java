 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DAO.generico.JPADaoGenerico;

public class ListaComPaginacao<T> extends AbstractList<T> {

    //Cache dos itens pesquisados anteriormente
    Map<Integer, T> cache;
    
    //Total de resultados
    long numResults;
    
    //Tamanho da pagina
    int pageSize;
    
    //jpql a ser executada
    String jpql;
    
    //Listagem com os parametros
    Object[] parametros;
    
    public ListaComPaginacao(String jpql, Object[] parametros, int pagesize, long numResults) {

    	this.jpql = jpql;
    	this.parametros = parametros;
    	this.pageSize = pagesize;
    	this.numResults = numResults;
        this.cache = new HashMap<Integer, T>();

    }

    @Override
    public final int size() {
        return (int) numResults;
    }

    @Override
    public final T get(int i) {
        if (!cache.containsKey(i)) {
            final List<T> results = JPADaoGenerico.executaQuery(jpql, parametros, i, pageSize);

            for (int j = 0; j < results.size(); j++) {
                T item = results.get(j);
                cache.put(i + j, item);
            }
        }
        return cache.get(i);
    }

    @Override
    public boolean remove(Object o) {
        final Iterator<Integer> it = cache.keySet().iterator();
        while (it.hasNext()) {
            final Integer chave = it.next();
            final T item = cache.get(chave);
            if (item.equals(o)) {
                cache.remove(chave);
                return true;
            }
        }
        return false;
    }
}
