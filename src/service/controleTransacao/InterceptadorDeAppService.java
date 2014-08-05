 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service.controleTransacao;

import java.lang.reflect.Method;

import DAO.exception.InfraestruturaException;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import service.anotacao.Transacional;
import util.JPAUtil;

public class InterceptadorDeAppService implements MethodInterceptor 
{	
	/* Observe que o método intercept() só iniciará uma transação se
	 * o  método  original  contiver a  anotação "@Transacional".  O
	 * método  isTransacional  é  responsável   por  identificar  os 
	 * métodos que são transacionais, isto é, os métodos que  contêm 
	 * a anotação "@Transacional".
	 * 
	 * Observe também que o EntityManager é sempre fechado.
	 */
	
	/* Parametros:
	 * 
	 * objeto - "this", o objeto "enhanced", isto é, o proxy.
	 * 
	 * metodo - o método interceptado, isto é, um  método  da classe
	 *          ProdutoAppService ou LanceAppService. 
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
			                 MethodProxy metodoProxy) 
		throws Throwable 
    {
		try 
        {
			Object resultado = null;
			
			if (isTransacional(metodo)) 
	        {	JPAUtil.beginTransaction ();
	        
	        	resultado = metodoProxy.invokeSuper(objeto, args);
	        	
	        	JPAUtil.commitTransaction ();
			}
			else
			{	resultado = metodoProxy.invokeSuper(objeto, args);
			}			
			return resultado;
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{	
			}
			
		    throw e;
		}
		catch(Exception e)
		{	
			if (isTransacional(metodo))
			{	if(efetuarRollback(e))
				{	JPAUtil.rollbackTransaction();
				}
				else
				{	JPAUtil.commitTransaction();
				}
			}
			
		    throw e;
		}
		finally
		{	JPAUtil.closeEntityManager();
		}
	}
	/**
	 * método que é responsavel por verificar se o metodo passado como parametro é transacional ou nao 
	 * 
	 * @param metodo
	 * @return
	 * @throws Exception
	 */
	
	public boolean isTransacional(Method metodo) 
		throws Exception
	{	return metodo.isAnnotationPresent(Transacional.class);
	}

	public boolean efetuarRollback(Exception e) 
	{	if(e.getClass().isAnnotationPresent(ExcecaoDeAplicacao.class))
		{	return e.getClass().getAnnotation(ExcecaoDeAplicacao.class).rollback();
		}
		else
		{	return true;
		}
	}
}
