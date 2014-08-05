 
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
 * Esta classe encapsula um objeto e o associa a uma variável booleana.
 */

public class ListaDeObjetosSelecao<T>
{	
	/*
	 * Este método recebe um List de um tipo genérico e para cada objeto da
	 * lista  será criado  um objeto  do tipo  ObjetoSelecao que  conterá o 
	 * objeto da lista mais uma variável boolean associada. O retorno desse 
	 * método é uma lista de ObjetoSelecao
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
	 * Este método retorna uma lista com os objetos que possuem a  variável
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
	 * Este método retorna uma lista com os objetos que possuem a  variável
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
