 
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

import modelo.CadPlan;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.CadPlanAppService;
import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * 
 * @author marques
 * doc: dayse.arruda
 * 
 * Classe responsável pela carga do arquivo cadplan e seus descendentes PlanoModelo e PlPerMod (em cascata)
 * versão atual incluindo apenas 2 planos (plano 1 que vai representar os dados originais da planilha da empresa 
 * e o plano 9 que corresponde a inclusão usando o algoritmo de conservação de estoques ou seja, o próprio algoritmo
 * usado na inclusão de plano
 *
 */
public class CargaCadPlan {
 
	private static CadPlanAppService cadPlanService;
	private static UsuarioAppService	usuarioService;
	
	
	@BeforeClass	
	public void setupClass(){
		
		try {
			
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Test//(groups="inclusao2")
	public void incluirCadPlans() throws AplicacaoException{
		
		
		//-------------------- CADPLAN 1 ---------------------//
		CadPlan cadPlan1 = new CadPlan();
		cadPlan1.setCodPlan("1");
		cadPlan1.setDescrPlan("dados originais defasados");
		cadPlan1.setEscoreMin(0.0);
		cadPlan1.setEscoreMedio(0.0);
		cadPlan1.setVarEstqPer(0.0);
		cadPlan1.setVarProdDiaPer(0.0);
		cadPlan1.setRanking(0.0);
		cadPlan1.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
/**		
		//-------------------- CADPLAN 2 ---------------------//
		CadPlan cadPlan2 = new CadPlan();
		cadPlan2.setCodPlan("2");
		cadPlan2.setDescrPlan("teste inclusão com NumDiasUteis novo");
		cadPlan2.setEscoreMin(0.0);
		cadPlan2.setEscoreMedio(0.0);
		cadPlan2.setVarEstqPer(0.0);
		cadPlan2.setVarProdDiaPer(0.0);
		cadPlan2.setRanking(0.0);
		cadPlan2.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		//-------------------- CADPLAN 3 ---------------------//
		CadPlan cadPlan3 = new CadPlan();
		cadPlan3.setCodPlan("3");
		cadPlan3.setDescrPlan("teste 2 inclusão plano com numdiasuteis fracionari");
		cadPlan3.setEscoreMin(0.0);
		cadPlan3.setEscoreMedio(0.0);
		cadPlan3.setVarEstqPer(0.0);
		cadPlan3.setVarProdDiaPer(0.0);
		cadPlan3.setRanking(0.0);
		cadPlan3.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		//-------------------- CADPLAN 4 ---------------------//
		CadPlan cadPlan4 = new CadPlan();
		cadPlan4.setCodPlan("4");
		cadPlan4.setDescrPlan("conferência inclusão com numdiasuteis fracionario");
		cadPlan4.setEscoreMin(0.0);
		cadPlan4.setEscoreMedio(0.0);
		cadPlan4.setVarEstqPer(0.0);
		cadPlan4.setVarProdDiaPer(0.0);
		cadPlan4.setRanking(0.0);
		cadPlan4.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		//-------------------- CADPLAN 5 ---------------------//
		CadPlan cadPlan5 = new CadPlan();
		cadPlan5.setCodPlan("5");
		cadPlan5.setDescrPlan("teste inclusao com novas coberturas");
		cadPlan5.setEscoreMin(0.0);
		cadPlan5.setEscoreMedio(0.0);
		cadPlan5.setVarEstqPer(0.0);
		cadPlan5.setVarProdDiaPer(0.0);
		cadPlan5.setRanking(0.0);
		cadPlan5.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		//-------------------- CADPLAN 6 ---------------------//
		CadPlan cadPlan6 = new CadPlan();
		cadPlan6.setCodPlan("6");
		cadPlan6.setDescrPlan("inclusão para teste de alteração prodlotemodel");
		cadPlan6.setEscoreMin(0.0);
		cadPlan6.setEscoreMedio(0.0);
		cadPlan6.setVarEstqPer(0.0);
		cadPlan6.setVarProdDiaPer(0.0);
		cadPlan6.setRanking(0.0);
		cadPlan6.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		//-------------------- CADPLAN 7 ---------------------//
		CadPlan cadPlan7 = new CadPlan();
		cadPlan7.setCodPlan("7");
		cadPlan7.setDescrPlan("teste recalculo prodlote");
		cadPlan7.setEscoreMin(0.0);
		cadPlan7.setEscoreMedio(0.0);
		cadPlan7.setVarEstqPer(0.0);
		cadPlan7.setVarProdDiaPer(0.0);
		cadPlan7.setRanking(0.0);
		cadPlan7.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		//-------------------- CADPLAN 8 ---------------------//
		CadPlan cadPlan8 = new CadPlan();
		cadPlan8.setCodPlan("8");
		cadPlan8.setDescrPlan("teste alteração prod em peças");
		cadPlan8.setEscoreMin(0.0);
		cadPlan8.setEscoreMedio(0.0);
		cadPlan8.setVarEstqPer(0.0);
		cadPlan8.setVarProdDiaPer(0.0);
		cadPlan8.setRanking(0.0);
		cadPlan8.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
**/		
		//-------------------- CADPLAN 9 ---------------------//
		CadPlan cadPlan9 = new CadPlan();
		cadPlan9.setCodPlan("9");
		cadPlan9.setDescrPlan("exemplo");
		cadPlan9.setEscoreMin(0.0);
		cadPlan9.setEscoreMedio(0.0);
		cadPlan9.setVarEstqPer(0.0);
		cadPlan9.setVarProdDiaPer(0.0);
		cadPlan9.setRanking(0.0);
		cadPlan9.setUsuario(usuarioService.recuperaPorLoginESenha("dgep","admgesplan2@@8"));
		
		
		//-------------------- LISTA DE CADPLANS ---------------------//
		List<CadPlan> cadPlans = new ArrayList<CadPlan>();
		
		cadPlans.add(cadPlan1);
		/**
		cadPlans.add(cadPlan2);
		cadPlans.add(cadPlan3);
		cadPlans.add(cadPlan4);
		cadPlans.add(cadPlan5);
		cadPlans.add(cadPlan6);
		cadPlans.add(cadPlan7);
		cadPlans.add(cadPlan8);
		**/
		cadPlans.add(cadPlan9);
		
		//-------------- INCLUSAO DE CADPLANS ---------------//
		for (CadPlan cadplan : cadPlans) {
			
			cadPlanService.inclui(cadplan);
			
		}
		
		
		
		
	}
		
	
	
	
	
	
	
	
}
