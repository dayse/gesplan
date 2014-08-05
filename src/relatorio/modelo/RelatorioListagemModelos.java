 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio.modelo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import relatorio.DeclaracaoAbstrata;
import relatorio.Relatorio;
import relatorio.RelatorioPdf;
import util.Constantes;
import exception.relatorio.RelatorioException;

public class RelatorioListagemModelos extends DeclaracaoAbstrata {

	@Override
	public void gerarRelatorio(List dados, Map<String, Object> parametros) throws RelatorioException {

		String nomeArquivo = "RelatorioListagemModelos.pdf";

		InputStream jasper = super.recuperaJasper(Relatorio.JASPER_LISTAGEM_DE_MODELOS);

		parametros.put("LOGO_COPPE", Relatorio.LOGO_COPPE);
		parametros.put("LOGO_INT", Relatorio.LOGO_INT);
		
		RelatorioPdf pdf = new RelatorioPdf(Constantes.CAMINHO_RELATORIOS_GERADOS);
		pdf.download(nomeArquivo, jasper, parametros, dados);

	}
}
