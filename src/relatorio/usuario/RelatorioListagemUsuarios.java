 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio.usuario;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import relatorio.DeclaracaoAbstrata;
import relatorio.Relatorio;
import relatorio.RelatorioPdf;
import util.Constantes;
import exception.relatorio.RelatorioException;

public class RelatorioListagemUsuarios extends DeclaracaoAbstrata {

	@Override
	public void gerarRelatorio(List dados, Map<String, Object> parametros) throws RelatorioException {
		// define nome do arquivo pdf que vai ser gerado
		String nomeArquivo = "RelatorioListagemUsuarios.pdf";
		
		// recupera do stream o arquivo jasper compilado usando o caminho
		// completo como parametro	
		InputStream jasper = super.recuperaJasper(Relatorio.JASPER_LISTAGEM_DE_USUARIOS);

		parametros.put("LOGO_COPPE", Relatorio.LOGO_COPPE);
		parametros.put("LOGO_INT", Relatorio.LOGO_INT);
		
		//RelatorioHtml html = new RelatorioHtml(Constantes.CAMINHO_RELATORIOS_GERADOS);
		RelatorioPdf pdf = new RelatorioPdf(Constantes.CAMINHO_RELATORIOS_GERADOS);
		
		// preenchimento do relatorio com os dados	
		pdf.download(nomeArquivo, jasper, parametros, dados);
	}
	
}
