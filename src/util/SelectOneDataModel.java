 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     

package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 * Cria lista de objetos para selecao (SelectItem) que podem ser usados em combo box e outros componentes
 * que usem lista de item (radio buttons, etc)
 * retorna uma lista de objetos SelectOneDataModel que encapsulam os SelectItem
 * 
 * coloca o item selecionado no atributo  "selecao"
 * e a lista dos itens (selectItem) no atributo "listaSelecao"

 * @author Heliomar
 *
 * @param <T>
 */
public class SelectOneDataModel<T> {

    public static final String TEXTO_SELECIONE_AQUI = "Selecione aqui";
    private String selecao;
    private List<SelectItem> listaSelecao;

    /**
     * Cria lista de objetos para selecao (SelectItem) que podem ser usados em combo box e outros componentes
     * que usem lista de item (radio buttons, etc)
     * Cria ista de objetos para selecao (SelectItem)sem texto para opção default
     * coloca o item selecionado no atributo  "selecao"
     * e a lista dos itens (selectItem) no atributo "listaSelecao"
     * Mostra inicialmente o primeiro item da lista na combo
     * @param <T>
     * @param itens
     * @return
     */
    public static <T> SelectOneDataModel<T> criaSemTextoInicial(final Collection<T> itens) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size());

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }

        select.setListaSelecao(listaSelecao);

        return select;
    }

    /**
       Cria lista de objetos para selecao (SelectItem) que podem ser usados em combo box e outros componentes
     * que usem lista de item (radio buttons, etc)

     * O texto default no comboBox Ã©: Selecione aqui
     * @param <T>
     * @param itens
     * @return
     */
    public static <T> SelectOneDataModel<T> criaComTextoInicialDefault(final Collection<T> itens) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }

        select.setSelecao(TEXTO_SELECIONE_AQUI);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public static <T> SelectOneDataModel<T> criaComTextoInicialPersonalizado(Collection<T> itens, final String LabelPrimeiroElemento) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        listaSelecao.add(new SelectItem(null, LabelPrimeiroElemento));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
        String selecao = LabelPrimeiroElemento;

        select.setSelecao(selecao);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public static <T> SelectOneDataModel<T> criaComObjetoSelecionado(Collection<T> itens, T objetoSelecionado) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
        String selecao = objetoSelecionado.toString();

        select.setSelecao(selecao);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    /**
     * Cria lista de objetos para selecao (SelectItem) que podem ser usados em radio button, ou check box
     * @param <T>
     * @param itens
     * @param objetoSelecionado
     * @return
     */
    public static <T> SelectOneDataModel<T> criaComObjetoSelecionadoSemTextoInicial(Collection<T> itens, T objetoSelecionado) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
        String selecao = objetoSelecionado.toString();

        select.setSelecao(selecao);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public SelectOneDataModel() {
        listaSelecao = new ArrayList<SelectItem>(1);
        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));
        selecao = TEXTO_SELECIONE_AQUI;
    }

    public SelectOneDataModel(Collection<T> itens) {
        if (itens != null) {
            listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        } else {
            listaSelecao = new ArrayList<SelectItem>(1);
        }

        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
    }

    public String getSelecao() {
        return selecao;
    }

    public void setSelecao(String selecao) {
        this.selecao = selecao;
    }

    public List<SelectItem> getListaSelecao() {
        return listaSelecao;
    }

    public void setListaSelecao(List<SelectItem> listaSelecao) {
        this.listaSelecao = listaSelecao;
    }

    public T getObjetoSelecionado() {
        if (selecao == null) {
            return null;
        }
        for (SelectItem item : listaSelecao) {
            if (item.getLabel().equals(selecao)) {
                return (T) item.getValue();
            }
        }
        return null;
    }

    public int getQuantidadeElementos() {
        int qtd = listaSelecao.size() - 1;
        return (qtd > 0) ? qtd : 0;
    }

    public boolean isTextoInicialSelecionado() {
        if (selecao == null) {
            return true;
        }
        return false;
    }

}
