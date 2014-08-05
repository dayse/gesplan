 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.exception.AplicacaoException;
import util.Constantes;
import util.JPAUtil;

/**
 * Classe responsável pela criação do diretorio de modelagem fuzzy
 * Classe obsoleta.  Não é mais usada.
 * 
 * @author marques
 * doc dayse.arruda
 *
 */
public class CargaDiretorio {
	
	@BeforeClass	
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void criarDiretorio() throws AplicacaoException{
		
		        String destinoDaPasta = Constantes.CAMINHO_MODELAGEM_UPLOADFILE;
		        
		        File diretorio = new File(destinoDaPasta);
		        
		        if(diretorio.exists()){
		        	//System.out.println("O diretorio já existe no sistema.");
		        	//throw new AplicacaoException("O diretorio já existe no sistema.");
		         }else{
		        	
		        	if(diretorio.mkdir()){
				          //System.out.println("Diretorio criado com sucesso");
		        	}else{
				         //System.out.println("Nao foi possivel criar o diretorio.");
				         throw new AplicacaoException("Nao foi possível criar o diretório.");
				     }
		        }
	     }
	
 }
