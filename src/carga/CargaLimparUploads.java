 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.io.File;

import service.exception.AplicacaoException;
import util.Constantes;
import util.ExtensionFilter;
import util.JPAUtil;


/**
 * 
 * Sobre a Carga:
 * � uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o m�todo executar().
 * Nesse m�todo executar � que � chamado os outros m�todos que s�o
 * as et�pas dessa carga.
 * Portanto se � necessario rodar um m�todo depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no m�todo executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas � preciso retornar true.
 * Se houver algum problema(exce��o) na execu��o de um das etapas, essa exce��o deve ser lancada
 * 
 * Essa Carga:
 * Percorre a pasta de uploads e deleta todos os arquivos .xfl que encontrar la
 * 
 * @author felipe.arruda
 *
 */
public class CargaLimparUploads extends CargaBase{
	
	public CargaLimparUploads(){
		
	}
	/**
	 * Executa a delecao.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.limparPastaDeUploads();
		return true;
	}


	public void limparPastaDeUploads(){
		System.out.println(">>>>>>CAMINHO:"+Constantes.CAMINHO_ABSOLUTO_MODELAGEM_UPLOADFILE);
	    deleteFiles(Constantes.CAMINHO_ABSOLUTO_MODELAGEM_UPLOADFILE, ".xfl");
		
	}
	
	public static void deleteFiles(String directory, String extension) {
		ExtensionFilter filter = new ExtensionFilter(extension);
		File dir = new File(directory);

		String[] list = dir.list(filter);
		File file;
		if (list.length == 0)
			return;

		for (int i = 0; i < list.length; i++) {
			file = new File(directory, list[i]);
			System.out.println(file + "  deleted : " + file.delete());
		}
	}
	
}
