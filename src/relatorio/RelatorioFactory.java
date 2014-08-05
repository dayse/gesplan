 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio;

import java.util.HashMap;

import relatorio.analise.RelatorioListagemAnaliseMaquina;
import relatorio.analise.RelatorioListagemAnaliseRecurso;
import relatorio.analise.RelatorioListagemAnaliseTecido;
import relatorio.familia.RelatorioListagemFamilias;
import relatorio.familia.RelatorioListagemFamiliasComModelos;
import relatorio.modelo.RelatorioListagemModelos;
import relatorio.planoMestreDeProducaoPorModelo.RelatorioListagemPlanoMestreDeProducaoPorModelo;
import relatorio.recModel.RelatorioListagemRecModels;
import relatorio.recurso.RelatorioListagemRecursos;
import relatorio.tecModel.RelatorioListagemTecModels;
import relatorio.tecido.RelatorioListagemTecidos;
import relatorio.usuario.RelatorioListagemUsuarios;

/**
 * @author Walanem
 * 
 * Esta classe implementa o padrão SINGLETON, cuja característica-chave de identificação é a presença de um
 * construtor privado, possuindo o intuito de retornar uma nova instancia de uma classe caso ela não tenha
 * sido instanciada ainda. Caso contrário, retorna uma referencia para a propria classe. 
 */
public class RelatorioFactory {

    private final static HashMap<Integer, Relatorio> relatorios = new HashMap();
    
    private final static RelatorioFactory factory;
    
    //Este bloco é executado uma unica vez, que é quando a classe é instanciada pela primeira vez.
    static {
        factory = new RelatorioFactory();
    }
    
    //No construtor, o Map de Relatórios é preenchido com todos os relatórios existentes na aplicação
    private RelatorioFactory() {
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_MODELOS, new RelatorioListagemModelos());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_TECIDOS, new RelatorioListagemTecidos());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_RECURSOS, new RelatorioListagemRecursos());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_RECMODELS, new RelatorioListagemRecModels());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_TECMODELS, new RelatorioListagemTecModels());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_FAMILIAS, new RelatorioListagemFamilias());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_FAMILIAS_COM_MODELOS, new RelatorioListagemFamiliasComModelos());
        relatorios.put(Relatorio.RELATORIO_ANALISE_MAQUINA, new RelatorioListagemAnaliseMaquina());
        relatorios.put(Relatorio.RELATORIO_ANALISE_TECIDO, new RelatorioListagemAnaliseTecido());
        relatorios.put(Relatorio.RELATORIO_ANALISE_RECURSO, new RelatorioListagemAnaliseRecurso());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_PLANO_MESTRE_DE_PRODUCAO_POR_MODELO, new RelatorioListagemPlanoMestreDeProducaoPorModelo());
        relatorios.put(Relatorio.RELATORIO_LISTAGEM_DE_USUARIOS, new RelatorioListagemUsuarios());
    }
    
    public static RelatorioFactory getInstance() {
        return factory;
    }

    public static Relatorio getRelatorio(int tipoRelatorio) {
        return relatorios.get(tipoRelatorio);
    }
}
