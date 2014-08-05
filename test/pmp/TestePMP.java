 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package pmp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import modelo.CadPlan;
import modelo.Modelo;
import modelo.PMP;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import util.JPAUtil;
import DAO.ModeloDAO;
import DAO.PMPDAO;
import DAO.PerioPMVigDAO;
import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.PMPDAOImpl;
import DAO.Impl.PerioPMVigDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class TestePMP {
	
	private static PMPDAO pmpDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	private static ModeloDAO modeloDAO;
	private double d;
	private Double d2;
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			pmpDAO = FabricaDeDao.getDao(PMPDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			
		} catch (Exception e) {
		}
	}
	
	
	//@Test
	public void testarRecuperaListaDePMPsPorModeloComPerioPMVigs(){
		Modelo modelo=null;
		try {
			modelo = modeloDAO.recuperaModeloPorCodigo("121131");
		} catch (ObjetoNaoEncontradoException e) {
			e.printStackTrace();
		}
		List<PMP> pmps = pmpDAO.recuperaListaDePMPsPorModeloComPerioPMVigs(modelo);
		
		for(PMP pmp: pmps){
			System.out.println("PMP="+pmp.getPerioPMVig());
		}
	}

//	@Test
	public void testaDouble(){
		System.out.println("d="+this.d);
		System.out.println("d2="+this.d2);
		
	}
	@Test
	public void testarRecuperaIntervaloDePMPPorModeloEIntervaloDePerioPMVig(){
		Modelo modelo=null;
		try {
			modelo = modeloDAO.recuperaModeloPorCodigo("121131");
		} catch (ObjetoNaoEncontradoException e) {
			e.printStackTrace();
		}
		List<PMP> pmps = pmpDAO.recuperaIntervaloDePMPPorModeloEIntervaloDePerioPMVig
											(modelo,2,5);
		
		for(PMP pmp: pmps){
			System.out.println("PMP="+pmp.getPerioPMVig());
		}
	}

}
