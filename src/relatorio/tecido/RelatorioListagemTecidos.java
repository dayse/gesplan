 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio.tecido;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import relatorio.DeclaracaoAbstrata;
import relatorio.Relatorio;
import relatorio.RelatorioPdf;
import util.Constantes;
import exception.relatorio.RelatorioException;

/**
 * É obrigada a implementar o metodo gerarRelatorio pois extende
 * DeclaracaoAbstrata que implementa a interface Relatorio.java
 * 
 * Informa nome do arquivo pdf que será gerado. Recupera stream com o arquivo
 * jasper compilado. Coloca logos do relatorio. Preenche arquivo com layout
 * usando dados e parametros adequados.
 * 
 * @author marques
 * 
 */
public class RelatorioListagemTecidos extends DeclaracaoAbstrata {

	/**
	 * É obrigada a implementar o metodo gerarRelatorio pois extende
	 * DeclaracaoAbstrata que implementa a interface Relatorio.java
	 * 
	 * Informa nome do arquivo pdf que será gerado. 
	 * Recupera stream do  arquivo jasper compilado. 
	 * Coloca logos do relatorio. Preenche arquivo com
	 * layout usando dados e parametros adequados.
	 * 
	 * @author marques
	 * 
	 */
	@Override
	public void gerarRelatorio(List dados, Map<String, Object> parametros)
			throws RelatorioException {
		// define nome do arquivo pdf que vai ser gerado
		String nomeArquivo = "RelatorioListagemTecidos.pdf";
		// recupera do stream o arquivo jasper compilado usando o caminho
		// completo como parametro
		InputStream jasper = super
				.recuperaJasper(Relatorio.JASPER_LISTAGEM_DE_TECIDOS);

		parametros.put("LOGO_COPPE", Relatorio.LOGO_COPPE);
		parametros.put("LOGO_INT", Relatorio.LOGO_INT);

		RelatorioPdf pdf = new RelatorioPdf(
				Constantes.CAMINHO_RELATORIOS_GERADOS);
		// preenchimento do relatorio com os dados
		pdf.download(nomeArquivo, jasper, parametros, dados);

	}
}
