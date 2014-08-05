 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package planomodelo;

import modelo.PlanoModelo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import util.JPAUtil;
import DAO.PlanoModeloDAO;
import DAO.Impl.PlanoModeloDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class TestePlanoModelo {
	

	private static PlanoModeloDAO planoModeloDAO;
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
			
		} catch (Exception e) {
		}
	}
	
	@Test
	public void recuperaPlPerModsDePlanoModelo() throws ObjetoNaoEncontradoException{

		String codPlan = "1";
		String codModelo = "121131";
		
		PlanoModelo planoModelo = planoModeloDAO.
								recuperarPlanoModeloPorCodigoModeloECodigoCadPlan(codPlan, codModelo);
		
		System.out.println("Plano Modelo = " + planoModelo.getModelo() + " - "+ planoModelo.getCadPlan());
	}
	
}
