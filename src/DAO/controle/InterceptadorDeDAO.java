 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.controle;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import DAO.anotacao.RecuperaConjunto;
import DAO.anotacao.RecuperaLista;
import DAO.anotacao.RecuperaListaPaginada;
import DAO.anotacao.RecuperaObjeto;
import DAO.anotacao.RecuperaUltimoOuPrimeiro;
import DAO.exception.InfraestruturaException;
import DAO.generico.JPADaoGenerico;

public class InterceptadorDeDAO implements MethodInterceptor 
{
	/* Parametros:
	 * 
	 * objeto - "this", o objeto "enhanced", isto é, o proxy.
	 * 
	 * metodo - o  método   interceptado,  isto  é,  um   método  da 
	 *          interface ProdutoDAO, LanceDAO, etc. 
	 * 
	 * args - um  array  de args; tipos  primitivos são empacotados.
	 *        Contém   os   argumentos  que  o  método  interceptado 
	 *        recebeu.
	 * 
	 * metodoProxy - utilizado para executar um método super. Veja o
	 *               comentário abaixo.
	 * 
	 * MethodProxy  -  Classes  geradas pela  classe Enhancer passam 
	 * este objeto para o objeto MethodInterceptor registrado quando
	 * um método  interceptado é  executado.  Ele pode ser utilizado
	 * para  invocar o  método  original,  ou  chamar o mesmo método
	 * sobre um objeto diferente do mesmo tipo.
	 * 
	 */
	
	public Object intercept (Object objeto, 
    		                 Method metodo, 
    		                 Object[] args, 
                             MethodProxy metodoDoProxy) 
    	throws Throwable 
    {
		// O símbolo ? representa um tipo desconhecido.
        JPADaoGenerico<?,?> jpaDaoGenerico = (JPADaoGenerico<?, ?>) objeto;

        if(metodo.isAnnotationPresent(RecuperaObjeto.class))
        {	// O método busca() retorna um Objeto (Entidade)
        	return jpaDaoGenerico.busca(metodo, args);
        }
        else if(metodo.isAnnotationPresent(RecuperaLista.class))
		{	// O método buscaLista() retorna um List
        	return jpaDaoGenerico.buscaLista(metodo, args);
        }
        else if(metodo.isAnnotationPresent(RecuperaConjunto.class))
        {	// O método buscaConjunto() retorna um Set
        	return jpaDaoGenerico.buscaConjunto(metodo, args);
        }
        else if(metodo.isAnnotationPresent(RecuperaUltimoOuPrimeiro.class))
        {	// O método buscaUltimoOuPrimeiro() retorna um Objeto (Entidade)
        	return jpaDaoGenerico.buscaUltimoOuPrimeiro(metodo, args);
        }
        else if(metodo.isAnnotationPresent(RecuperaListaPaginada.class))
        {	// O método busca() retorna um Objeto (Entidade)
        	return jpaDaoGenerico.buscaListaPaginada(metodo, args, metodo.getAnnotation(RecuperaListaPaginada.class).tamanhoPagina());
        }
        else 
        {  	
        	throw new InfraestruturaException("Um método não final deixou de ser anotado");
        }
    }
}
