 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.generico;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import DAO.exception.InfraestruturaException;
import DAO.exception.ObjetoNaoEncontradoException;

import util.JPAUtil;
import util.ListaComPaginacao;

/**
 * Faz a implementação de um DAO genérico para a JPA. É uma classe inteiramente
 * genérica que pode ser usada exatamente como está em qualquer aplicação.
 * 
 * Faz uma implementação "typesafe" dos métodos CRUD e dos métodos de busca.
 * 
 * Ser "typesafe" significa que o compilador consegue identificar o tipo do
 * objeto
 */
public class JPADaoGenerico<T, PK extends Serializable> implements
		DaoGenerico<T, PK> {
	private Class<T> clazz;// tipo == Familia.class

	public JPADaoGenerico(Class<T> tipo) {
		this.clazz = tipo;
	}

	@SuppressWarnings("unchecked")
	public final T inclui(T o) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.persist(o);
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}

		return o;
	}

	public final void altera(T o) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.merge(o);
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	/**
	 * Este método exige que o objeto passado seja persistente senao gera um erro!
	 */
	public final void exclui(T o) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.remove(o);
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	public final T getPorId(PK id) throws ObjetoNaoEncontradoException {
		EntityManager em = JPAUtil.getEntityManager();
		T t = null;
		try {
			t = em.find(clazz, id);

			if (t == null) {
				throw new ObjetoNaoEncontradoException();
			}
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
		return t;
	}

	// implementacao de lock pessimista
	// deve ter cuidado pois deve ser usado no inicio do processo de acesso ao
	// recurso critico
	// para que nao haja risco de alteracoes simultaneas
	public final T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException {
		EntityManager em = JPAUtil.getEntityManager();
		T t = null;
		try {
			t = em.find(clazz, id);

			if (t != null) {
				em.lock(t, LockModeType.READ); // read: define o tipo de lock
				// como sendo de leitura
				em.refresh(t);
			} else {
				throw new ObjetoNaoEncontradoException();
			}
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}

		return t;
	}

	// daqui para baixo são os metodos relativos ao "ExecutorDeBuscas" (os
	// metodos acima são efetivamente genericos
	// enquanto que os de busca são genericos porem tem querys especificas.
	@SuppressWarnings("unchecked")
	public final T busca(Method metodo, Object[] argumentos)
			throws ObjetoNaoEncontradoException {
		EntityManager em = JPAUtil.getEntityManager();
		T t = null;
		try {
			String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo); // ex:
			// Familia.recuperaUmaFamiliaEModelos
			Query namedQuery = em.createNamedQuery(nomeDaBusca);

			preencheParametros(argumentos, namedQuery);
			t = (T) namedQuery.getSingleResult();

			return t;
		} catch (NoResultException e)// caso nao seja encontrado o objeto sera
		// retornada esta excecao
		{
			throw new ObjetoNaoEncontradoException();
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public final T buscaUltimoOuPrimeiro(Method metodo, Object[] argumentos)
			throws ObjetoNaoEncontradoException {
		EntityManager em = JPAUtil.getEntityManager();
		T t = null;
		try {
			List lista;
			String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
			Query namedQuery = em.createNamedQuery(nomeDaBusca);

			preencheParametros(argumentos, namedQuery);
			lista = namedQuery.getResultList();

			t = (lista.size() == 0) ? null : (T) lista.get(0);

			if (t == null) {
				throw new ObjetoNaoEncontradoException();
			}

			return t;
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public final List<T> buscaLista(Method metodo, Object[] argumentos) {
		EntityManager em = JPAUtil.getEntityManager();

		try {
			String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
			Query namedQuery = em.createNamedQuery(nomeDaBusca);

			preencheParametros(argumentos, namedQuery);
			return (List<T>) namedQuery.getResultList();
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public final Set<T> buscaConjunto(Method metodo, Object[] argumentos) {
		EntityManager em = JPAUtil.getEntityManager();

		try {
			String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
			Query namedQuery = em.createNamedQuery(nomeDaBusca);

			preencheParametros(argumentos, namedQuery);

			List<T> lista = namedQuery.getResultList();

			return new LinkedHashSet(lista);
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public final List<T> buscaListaPaginada(Method metodo, Object[] argumentos,
			int pageSize) {
		EntityManager em = JPAUtil.getEntityManager();

		String nomeDaBusca = null;

		try {

			nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
			String nomeDaBuscaCount = nomeDaBusca + "Count";

			Query namedQueryCount = em.createNamedQuery(nomeDaBuscaCount);

			preencheParametros(argumentos, namedQueryCount);

			long tamanhoTotal = 0;
			try {
				tamanhoTotal = (Long) namedQueryCount.getSingleResult();
			} catch (NoResultException e) {
			}


			return new ListaComPaginacao<T>(nomeDaBusca, argumentos, pageSize,
					tamanhoTotal);

		} catch (PersistenceException pe) {
			pe.printStackTrace();
			throw new InfraestruturaException(
					"A NAMED QUERY DO COUNT DEVE SER CRIADA! O SEU NOME DEVE SER: "
							+ nomeDaBusca + "Count");
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> executaQuery(String hql, Object[] argumentos,
			int inicio, int quantidade) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			Query query = em.createNamedQuery(hql);

			preencheParametros(argumentos, query);
			// define o primeiro elemento a ser recuperado e
			// a quantidade de elementos a serem recuperados
			return query.setFirstResult(inicio).setMaxResults(quantidade)
					.getResultList();

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			JPAUtil.closeEntityManager();
		}
		return null;
	}

	// preenche os parametros posicionalmente, ou seja na ordem em que os
	// parametros são declarados na query
	private static void preencheParametros(Object[] argumentos, Query namedQuery) {
		if (argumentos != null) {
			for (int i = 0; i < argumentos.length; i++) {
				Object arg = argumentos[i];
				namedQuery.setParameter(i + 1, arg);// Parâmetros de buscas
				// são 1-based.
			}
		}
	}

	// devolve uma String com o nome da classe + nome do metodo.
	private String getNomeDaBuscaPeloMetodo(Method metodo) {
		return clazz.getSimpleName() + "." + metodo.getName(); // ex:
		// Familia.recuperaPorDescricao
	}
}
