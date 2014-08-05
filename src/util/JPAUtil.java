 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import DAO.exception.InfraestruturaException;

public class JPAUtil 
{	private static EntityManagerFactory emf = null;
	private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
	private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

	/**
	 * cria o entity manager factory
	 */
	public static void JPAstartUp() 
	{
		try
		{	emf = Persistence.createEntityManagerFactory("gesplan");
		}
		catch(Throwable e)
		{	
			e.printStackTrace();
		}
	}

	public static EntityManager getEntityManager() 
	{	
	
		EntityManager s = threadEntityManager.get();
		// Abre uma nova Sessão, se a thread ainda não possui uma.
		try 
		{	if (s == null) 
			{	s = emf.createEntityManager();
				threadEntityManager.set(s);
			}
		} 
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		}
		return s;
	}

	public static void closeEntityManager() 
	{	

		try 
		{	EntityManager s = threadEntityManager.get();
			threadEntityManager.set(null);
			if (s != null && s.isOpen())
			{	s.close();
				
			}

			EntityTransaction tx = threadTransaction.get();
			if ( tx != null && tx.isActive())
			{	rollbackTransaction();
				throw new RuntimeException("EntityManager sendo fechado " +
						                   "com transação ativa.");
			}
		} 	
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		}
	}

	public static void beginTransaction() 
	{	

		EntityTransaction tx = threadTransaction.get();
		try 
		{   //Existe alguma transacao aberta 
			//na execucao dessa requisicao?
			if (tx == null) 
			{	tx = getEntityManager().getTransaction();
				tx.begin();
				threadTransaction.set(tx);
			}
			else
			{	
			}
		} 
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		}
	}

	public static void commitTransaction() 
	{	EntityTransaction tx = threadTransaction.get();
		try 
		{	if ( tx != null && tx.isActive())
			{	tx.commit();
		
			}
			threadTransaction.set(null);
		} 
		catch (RuntimeException ex) 
		{	try
			{	rollbackTransaction();
			}
			catch(RuntimeException e)
			{}
			
			throw new InfraestruturaException(ex);
		}
	}

	public static void rollbackTransaction() 
	{	
	
		EntityTransaction tx = threadTransaction.get();
		try 
		{	threadTransaction.set(null);
			if ( tx != null && tx.isActive()) 
			{	tx.rollback();
			}
		} 
		catch (RuntimeException ex)
		{	
			throw new InfraestruturaException(ex);
		} 
		finally 
		{	closeEntityManager();
		}
	}
}
