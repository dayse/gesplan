 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package selecao;


import java.util.ArrayList;
import java.util.List;

/*
 * Esta classe encapsula um objeto e o associa a uma vari�vel booleana.
 */

public class ListaDeObjetosSelecao<T>
{	
	/*
	 * Este m�todo recebe um List de um tipo gen�rico e para cada objeto da
	 * lista  ser� criado  um objeto  do tipo  ObjetoSelecao que  conter� o 
	 * objeto da lista mais uma vari�vel boolean associada. O retorno desse 
	 * m�todo � uma lista de ObjetoSelecao
	 */
	public static <T> List<ObjetoSelecao<T>> getListaDeObjetoSelecao(
			List<T> lista)
	{
		List<ObjetoSelecao<T>> listaDeObjetoSelecao = new ArrayList<ObjetoSelecao<T>>(lista.size());
		for (T obj : lista)
		{	listaDeObjetoSelecao.add(new ObjetoSelecao<T>(obj));
		}
		return listaDeObjetoSelecao;
	}

	
	public static <T> List<ObjetoSelecao<T>> getListaDeObjetoSelecaoMesmoQueEstejaSelecionadoOuNao(
			List<T> lista)
	{
		List<ObjetoSelecao<T>> listaDeObjetoSelecao = new ArrayList<ObjetoSelecao<T>>(lista.size());
		for (T obj : lista)
		{	listaDeObjetoSelecao.add(new ObjetoSelecao<T>(obj));
		}
		return listaDeObjetoSelecao;
	}
	
	
	/*
	 * Este m�todo retorna uma lista com os objetos que possuem a  vari�vel
	 * selecionado igual a false.
	 */
	public static <T> List<ObjetoSelecao<T>> getListaDosNaoSelecionados(
			List<ObjetoSelecao<T>> listaSelecao)
	{
		List<ObjetoSelecao<T>> lista = new ArrayList<ObjetoSelecao<T>>();
		for (ObjetoSelecao<T> obj : listaSelecao)
		{	if (!obj.isSelecionado()) 
				lista.add(obj);
		}
		return lista;
	}
	
	
	/*
	 * Este m�todo retorna uma lista com os objetos que possuem a  vari�vel
	 * selecionado igual a true.
	 */
	public static <T> List<ObjetoSelecao<T>> getListaDosSelecionados(
			List<ObjetoSelecao<T>> listaSelecao)
	{
		List<ObjetoSelecao<T>> lista = new ArrayList<ObjetoSelecao<T>>();
		
		  for (ObjetoSelecao<T> obj : listaSelecao)
			  {	if (obj.isSelecionado() ){
				  lista.add(obj);
			    }
			
			}
							
		return lista;
	}
	
}
