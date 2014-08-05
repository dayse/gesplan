 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import util.JPAUtil;
import listener.JPAStartUpListener;

/**
 * Inicializa a JPA, dessa forma os proximos testes não precisam startar ela novamente.
 * @author felipe.arruda
 *
 */
public class CargaStartarJPA {
	// A anotaçao @BeforeClass é necessária para criar os services
	// que serao responsaveis pela excuçao dos metodos das classes
	// AppService. Apos a excuçao do metodo setupClass que fora anotado
	// como @BeforeClass os demais metodos da classe de teste serao executados.
	
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
	public void startando(){
		System.out.println(">>>>Terminado de Startar JPA");
	}

}
