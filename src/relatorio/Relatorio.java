 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio;

import java.util.List;
import java.util.Map;

import exception.relatorio.RelatorioException;

import util.Constantes;


public interface Relatorio {
	
	    //Tipos de Relatorios do sistema GESPLAN
		//cada relatorio deve receber um número diferente 
		//isto é colocado no hashmap da fabrica de relatorios (RelatorioFactory)
	    public final static int RELATORIO_LISTAGEM_DE_MODELOS = 0;
	    public final static int RELATORIO_LISTAGEM_DE_TECIDOS = 1;
	    public final static int RELATORIO_LISTAGEM_DE_RECURSOS = 2;
	    public final static int RELATORIO_LISTAGEM_DE_RECMODELS = 3;
	    public final static int RELATORIO_LISTAGEM_DE_FAMILIAS = 4;
	    public final static int RELATORIO_LISTAGEM_DE_FAMILIAS_COM_MODELOS = 5;
	    public final static int RELATORIO_ANALISE_MAQUINA = 6;
	    public final static int RELATORIO_ANALISE_TECIDO = 7;
	    public final static int RELATORIO_ANALISE_RECURSO = 8;
	    public final static int RELATORIO_LISTAGEM_DE_TECMODELS = 9;
	    public final static int RELATORIO_LISTAGEM_DE_PLANO_MESTRE_DE_PRODUCAO_POR_MODELO = 10;
	    public final static int RELATORIO_LISTAGEM_DE_USUARIOS = 11;
	    //Define os nomes dos arquivos  relativos as Imagens do Relatorio, concatenando com os caminhos definidos em constantes.java
	    public static final String LOGO_COPPE = Constantes.CAMINHO_LOGOTIPOS + "logoCoppe.png";
	    public static final String LOGO_INT = Constantes.CAMINHO_LOGOTIPOS + "logoINT2.jpg";
	    
	    //nome dos Arquivos de Relatorio (compilados Jaspers) que serão criados no momento da compilação (quando der preview no arquivo 
	    //*.jrxml que corresponde ao projeto) 
	    public static final String JASPER_LISTAGEM_DE_MODELOS = Constantes.CAMINHO_JASPERS + "relatorioListagemModelos.jasper";
	    public static final String JASPER_LISTAGEM_DE_TECIDOS = Constantes.CAMINHO_JASPERS + "relatorioListagemTecidos.jasper";
	    public static final String JASPER_LISTAGEM_DE_RECURSOS = Constantes.CAMINHO_JASPERS + "relatorioListagemRecursos.jasper";
	    public static final String JASPER_LISTAGEM_DE_RECMODELS = Constantes.CAMINHO_JASPERS + "relatorioListagemRecModels.jasper";
	    public static final String JASPER_LISTAGEM_DE_TECMODELS = Constantes.CAMINHO_JASPERS + "relatorioListagemTecModels.jasper";
	    public static final String JASPER_LISTAGEM_DE_FAMILIAS = Constantes.CAMINHO_JASPERS + "relatorioListagemFamilias.jasper";
	    public static final String JASPER_LISTAGEM_DE_FAMILIAS_COM_MODELOS = Constantes.CAMINHO_JASPERS + "relatorioListagemFamiliasComModelos.jasper";
	    public static final String JASPER_LISTAGEM_ANALISE_MAQUINA = Constantes.CAMINHO_JASPERS + "relatorioListagemAnaliseMaquina.jasper";
	    public static final String JASPER_LISTAGEM_ANALISE_TECIDO = Constantes.CAMINHO_JASPERS + "relatorioListagemAnaliseTecido.jasper";
	    public static final String JASPER_LISTAGEM_ANALISE_RECURSO = Constantes.CAMINHO_JASPERS + "relatorioListagemAnaliseRecurso.jasper";
	    public static final String JASPER_LISTAGEM_DE_PLANO_MESTRE_DE_PRODUCAO_POR_MODELO = Constantes.CAMINHO_JASPERS + "relatorioListagemPlanoMestreDeProducaoPorModelo.jasper";
	    public static final String JASPER_LISTAGEM_DE_USUARIOS = Constantes.CAMINHO_JASPERS + "relatorioListagemUsuarios.jasper";
	   
	    //Método que esta interface OBRIGA as subclasses a implementar, de acordo com seus requisitos
	    public void gerarRelatorio(List dados, Map<String, Object> parametros) throws RelatorioException;
	    
	}
