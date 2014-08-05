 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.util.ArrayList;
import java.util.List;

import modelo.Familia;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.FamiliaAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaFamilia {
 
	private static FamiliaAppService familiaService;
	
	
	@BeforeClass	
	public void setupClass(){
		
		try {
			
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Test//(groups="inclusao")
	public void incluirFamilias() throws AplicacaoException{
		
				
		//-------------------- FAMILIA 1 ---------------------//
		Familia familia1 = new Familia();
		familia1.setCodFamilia("01");
		familia1.setDescrFamilia("soutien");
		familia1.setCobertura(60);
		familia1.setTmuc(8.1);
		familia1.setEstqInicFam(10800);

		//-------------------- FAMILIA 2 ---------------------//
		Familia familia2 = new Familia();
		familia2.setCodFamilia("02");
		familia2.setDescrFamilia("calça/biquini");
		familia2.setCobertura(65);
		familia2.setTmuc(5.5);
		familia2.setEstqInicFam(7600);

		//-------------------- FAMILIA 3 ---------------------//
		Familia familia3 = new Familia();
		familia3.setCodFamilia("03");
		familia3.setDescrFamilia("infantil");
		familia3.setCobertura(75);
		familia3.setTmuc(3.8);
		familia3.setEstqInicFam(4590);
		
		// -------------- LISTA DE FAMILIAS -----------------// 
		List<Familia> familias = new ArrayList<Familia>();
	
		familias.add(familia1);
		familias.add(familia2);
		familias.add(familia3);
		
		//-------------- INCLUSAO DE FAMILIAS ---------------//
		for (Familia familia : familias) {
			
			familiaService.inclui(familia);
			
		}
		
		
	}
	
	
	

}
