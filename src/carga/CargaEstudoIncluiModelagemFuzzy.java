 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import modelo.ModelagemFuzzy;

import org.richfaces.model.UploadItem;

import service.ModelagemFuzzyAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.Constantes;
import util.DataUtil;

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
 * Inclui  um arquivo de base de modelagem,
 * esse arquivo tem que ter um caminho pre-definido
 * no sistema, da mesma forma que tem os relatorios.
 * Esse caminho � representado pela constante:
 * Constantes.CAMINHO_ABSOLUTO_ARQUIVOS_CARGA
 * @author felipe.arruda
 */
public class CargaEstudoIncluiModelagemFuzzy extends CargaBase{

	private static ModelagemFuzzyAppService modelagemFuzzyService;
	private UploadItem itemParaEnvio;
	
	  public CargaEstudoIncluiModelagemFuzzy(){
		
		 try {
			 modelagemFuzzyService = FabricaDeAppService.getAppService(ModelagemFuzzyAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
		}


	/**
	 * envia um arquivo de modelagem para o sistema e cria uma modelagemfuzzy
	 * usando ele
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.IncluirModelagemFuzzy();
		return true;
	}
	 /**
	  * Prepara o UploadItem com o arquivo de producao.xlf
	  */
	 public void prepararArquivoParaEnviar(){
		 	String nome_arquivo = "producao.xfl";
		 	String caminho = Constantes.CAMINHO_ABSOLUTO_ARQUIVOS_CARGA + nome_arquivo;
			java.io.File file = new java.io.File(caminho);
			
			byte[] b = new byte[(int) file.length()];
			
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(b);
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found.");
				e.printStackTrace();
			} catch (IOException e1) {
				System.out.println("Error Reading The File.");
				e1.printStackTrace();
			}
			
			itemParaEnvio = new UploadItem(nome_arquivo,(int)file.length(),"xfl",b);
	 }
	 
	 
	 

	    /**
	     * Inclui um arquivo de modelagem
	     * pegando um arquivo do sistema e "enviando" usando o
	     * m�todo modelagemFuzzyService.incluiComUpload.
	     *
	     * Essa modelagem tem os dados:
	     * NomeModelagemFuzzy: "producao"
	     * descricao: "producao limites novos"
	     * NomeArquivo: "producao.xfl"
	     * Autor: "degep"
	     * Finalidade: "GERAR_PMP"
	     * @author felipe.arruda
	     * @throws AplicacaoException 
	     * 
	     */
	    public void IncluirModelagemFuzzy() throws AplicacaoException {

	    	
	    		prepararArquivoParaEnviar();
	        	
	        	        	

	    		Date dataCriacao = new Date();
	        	ModelagemFuzzy modelagemFuzzyCorrente = new ModelagemFuzzy();
	        	modelagemFuzzyCorrente.setNomeModelagemFuzzy("producao");
	        	modelagemFuzzyCorrente.setDescrModelagemFuzzy("producao limites novos");
	        	modelagemFuzzyCorrente.setDataCriacao(DataUtil.dateToCalendar(dataCriacao));
	        	modelagemFuzzyCorrente.setNomeArquivo(itemParaEnvio.getFileName());
	        	modelagemFuzzyCorrente.setAutor("degep");
	        	modelagemFuzzyCorrente.setFinalidadeModelagem("GERAR_PMP");
	        	
	        	modelagemFuzzyService.incluiComUpload(modelagemFuzzyCorrente, itemParaEnvio);	
	        	
	        	
	        	
			
	    }
		

	}
