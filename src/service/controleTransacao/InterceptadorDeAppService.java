 
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
	/* Observe que o m�todo intercept() s� iniciar� uma transa��o se
	 * o  m�todo  original  contiver a  anota��o "@Transacional".  O
	 * m�todo  isTransacional  �  respons�vel   por  identificar  os 
	 * m�todos que s�o transacionais, isto �, os m�todos que  cont�m 
	 * a anota��o "@Transacional".
	 * 
	 * Observe tamb�m que o EntityManager � sempre fechado.
	 */
	
	/* Parametros:
	 * 
	 * objeto - "this", o objeto "enhanced", isto �, o proxy.
	 * 
	 * metodo - o m�todo interceptado, isto �, um  m�todo  da classe
	 *          ProdutoAppService ou LanceAppService. 
	 * 
	 * args - um  array  de args; tipos  primitivos s�o empacotados.
	 *        Cont�m   os   argumentos  que  o  m�todo  interceptado 
	 *        recebeu.
	 * 
	 * metodoProxy - utilizado para executar um m�todo super. Veja o
	 *               coment�rio abaixo.
	 * 
	 * MethodProxy  -  Classes  geradas pela  classe Enhancer passam 
	 * este objeto para o objeto MethodInterceptor registrado quando
	 * um m�todo  interceptado �  executado.  Ele pode ser utilizado
	 * para  invocar o  m�todo  original,  ou  chamar o mesmo m�todo
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
	 * m�todo que � responsavel por verificar se o metodo passado como parametro � transacional ou nao 
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
