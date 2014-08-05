 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package plpermod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import modelo.CadPlan;
import modelo.Modelo;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PlPerModAppService;
import service.controleTransacao.FabricaDeAppService;
import util.JPAUtil;
import DAO.CadPlanDAO;
import DAO.ModeloDAO;
import DAO.PerioPMDAO;
import DAO.PlPerModDAO;
import DAO.PlanoModeloDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.PlPerModDAOImpl;
import DAO.Impl.PlanoModeloDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class TestePlPerMod {
	
	private static PerioPMDAO perioPMDAO;
	private static CadPlanDAO cadPlanDAO;
	private static ModeloDAO modeloDAO;
	private static PlanoModeloDAO planoModeloDAO;
	private static PlPerModDAO plPerModDAO;
	private static PlPerModAppService plPerModService;
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
			plPerModDAO = FabricaDeDao.getDao(PlPerModDAOImpl.class);
			plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
			
		} catch (Exception e) {
		}
	}
	
	//@Test
	public void recuperaPlPerModsDePlanoModelo() {
		
		CadPlan cadPlan1 = null;
		
		try {
			cadPlan1 = cadPlanDAO.recuperaCadPlanComPlanosModelo("c");
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		Set<PlanoModelo> planosModeloSet = new TreeSet<PlanoModelo>();
		Iterator it = cadPlan1.getPlanosModelo().iterator();
		while (it.hasNext()){
			PlanoModelo pm = (PlanoModelo) it.next();
			planosModeloSet.add(pm);
		}
		
		System.out.println("Tamanho do Set = " + planosModeloSet.size());
	}
	
	//@Test 
	public void compararPlPerMods(){
		
		PerioPM periodo1 = null;
		PerioPM periodo2 = null;
		PerioPM periodo3 = null;
		
		CadPlan cadPlan1 = null;
		
		try {
			periodo1 = perioPMDAO.recuperaPerioPMPorPeriodoPM(1);
			periodo2 = perioPMDAO.recuperaPerioPMPorPeriodoPM(2);
			periodo2 = perioPMDAO.recuperaPerioPMPorPeriodoPM(3);
			cadPlan1 = cadPlanDAO.recuperaCadPlanComPlanosModelo("2");
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		List<PlanoModelo> planosModeloList = new ArrayList<PlanoModelo>();
		Set<PlanoModelo> planosModeloSet = new TreeSet<PlanoModelo>();
		PlanoModelo planoModelo2 = new PlanoModelo();
		
		Iterator it = cadPlan1.getPlanosModelo().iterator();
		while (it.hasNext()){
			PlanoModelo pm = (PlanoModelo) it.next();
			planosModeloSet.add(pm);
		}
		
		for (PlanoModelo planoModelo : planosModeloSet) {
			System.out.println("PlanoModelo.getModelo() = " + planoModelo.getModelo());
		}
		
		System.out.println("Modelo do PlanoModelo2 = " + planoModelo2.getModelo());
		
		
		PlPerMod plPerMod1 = new PlPerMod();
		plPerMod1.setPlanoModelo(planoModelo2);
		plPerMod1.setPerioPM(periodo1);
		
		PlPerMod plPerMod2 = new PlPerMod();
		plPerMod2.setPlanoModelo(planoModelo2);
		plPerMod2.setPerioPM(periodo2);
		
		PlPerMod plPerMod3 = new PlPerMod();
		plPerMod2.setPlanoModelo(planoModelo2);
		plPerMod2.setPerioPM(periodo3);
		
		System.out.println("PlPerMod2 = PlPerMod3 ??? ---> " + plPerMod1.equals(plPerMod2) + "!");
		
		Set<PlPerMod> plPerMods = new TreeSet<PlPerMod>();
		System.out.println("Adicionou PlPerMod3 ? ->" + plPerMods.add(plPerMod3));
		System.out.println("Adicionou PlPerMod1 ? ->" + plPerMods.add(plPerMod1));
		System.out.println("Adicionou PlPerMod2 ? ->" + plPerMods.add(plPerMod2));
		System.out.println("");
		
		for (PlPerMod plPerMod : plPerMods) {
			System.out.println("PLPERMOD --> PlanoModelo = " + plPerMod.getPlanoModelo().getId());
			System.out.println("PLPERMOD --> PerioPM = " + plPerMod.getPerioPM().getId());
			System.out.println("");
		}
	}
	
	
	public void testarRetornoPorParametro(){
		ArrayList<Boolean> bools = new ArrayList<Boolean>(4);
//		bools.add(false);
//		bools.add(false);
//		bools.add(false);
//		bools.add(false);
		
		this.metodoQueDeveRetornarPorParametro(bools);
		for(Boolean b: bools){
			System.out.println(">>>"+b);
		}
	}
	
	public void metodoQueDeveRetornarPorParametro(ArrayList<Boolean> booleanos){
		booleanos.set(2,true);		
	}
	
	
	@Test
	public void testarRecuperaIntervaloDePlPerModPorPlanoModeloEIntervaloDePerioPM(){
		PlanoModelo planoModelo=null;
		try {
			planoModelo = (PlanoModelo) cadPlanDAO.recuperaCadPlanApenasComPlanosModelo("1").getPlanosModelo().iterator().next();
		} catch (ObjetoNaoEncontradoException e) {
			e.printStackTrace();
		}
		List<PlPerMod> intervalo = plPerModDAO.recuperaIntervaloDePlPerModPorPlanoModeloEIntervaloDePerioPM
			(planoModelo, 2, 4);

		for(PlPerMod plPerMod: intervalo){
			System.out.println("plpermod="+plPerMod.getPerioPM());
		}
	}

}
