 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package selecao;

/*
 * Esta classe encapsula um objeto e o associa a uma variável booleana.
 */

public class ObjetoSelecao<T>
{	private T objeto;
	private boolean selecionado = false;

	public ObjetoSelecao(T obj)
	{	objeto = obj;
	}

	public boolean isSelecionado()
	{	return selecionado;
	}

	public void setSelecionado(boolean selecionado)
	{	this.selecionado = selecionado;
	}

	public T getObjeto()
	{	return objeto;
	}

	public void setObjeto(T objeto)
	{	this.objeto = objeto;
	}
}
