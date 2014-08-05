 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cadplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import modelo.CadPlan;
import modelo.HP;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlPerModAgregado;
import modelo.PlanoModelo;
import modelo.Usuario;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.CadPlanAppService;
import service.HPAppService;
import service.PerioPAPAppService;
import service.PerioPMAppService;
import service.PlPerModAppService;
import service.PlanoModeloAppService;
import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;
import DAO.PlPerModDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.PlPerModDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

public class TesteCadPlan {
	
	// Variaveis globais
	Usuario usuarioAdmin;
	
	// Services
	private static CadPlanAppService cadPlanService;
	private static UsuarioAppService usuarioService;
	private static PerioPMAppService perioPMService;
	private static PlPerModAppService plPerModService;
	private static PlanoModeloAppService planoModeloService;
	
	
	  @BeforeClass
	  public void setupClass(){
		
		 try {
			 System.out.println("-----------------------------> Startando a JPA...");
			 JPAUtil.JPAstartUp();
			 System.out.println("-----------------------------> JPA startada com sucesso!");
			 
			 cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			 usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			 perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			 plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
			 planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			 
		 } catch (Exception e) {
		 }
	  }
	
	  //@Test   
	  public void inclui() {
		  
		  CadPlan cadPlan = new CadPlan();
		  
		  cadPlan.setCodPlan("c1");
		  cadPlan.setDescrPlan("Plano1");
		  
		  try {
			usuarioAdmin = usuarioService.recuperaPorLoginESenha("admin", "admin");
			cadPlan.setUsuario(usuarioAdmin);
			
			cadPlanService.inclui(cadPlan);
		  } catch (AplicacaoException e) {
			  e.getMessage();
			  e.printStackTrace();
		  }
	  }
	  
	  
//	  @Test
	  public void totalizaPlano(){
		  
		  CadPlan cadPlan = null;
		  
		  try {
			  cadPlan = cadPlanService.recuperaCadPlanComPlanosModelo("c1");
		  	} catch (ObjetoNaoEncontradoException e) {
		  }
		  
		  // É preciso criar um Map, pois queremos a totalização de Valores por Modelo em um determinado período.
		  Map<Integer, Double[]> mapTotalizacao = new HashMap<Integer, Double[]>();
		  
		  for (PlanoModelo planoModelo : cadPlan.getPlanosModelo()) {
			
			  try {
				  planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
			  } catch (ObjetoNaoEncontradoException e) {
			  }
			  
			  for (PlPerMod plPerMod : planoModelo.getPlPerMods()) {
			  	
				  // CHAVE: PeriodoPM
				  Integer key = plPerMod.getPerioPM().getPeriodoPM();	
				  
				  // VALOR: Um array com os valores correspondentes do PlPerMod neste período
				  Double valorVendas = plPerMod.getVendasModel();
				  Double valorPedidos = plPerMod.getPedidosModel();
				  Double valorProducao = plPerMod.getProducaoModel();
				  Double valorDispProjetada = plPerMod.getDispProjModel();
				  Double valorCobertura = plPerMod.getCoberturaModel();
				  
				  // Se o período atual ainda não consta no Map, ele é adicionado, com os valores referentes do PlPerMod atual
				  if (!mapTotalizacao.containsKey(key)){
					  
					  Double[] valores = new Double[5];
					  
					  valores[0] = valorVendas;
					  valores[1] = valorPedidos;
					  valores[2] = valorProducao;
					  valores[3] = valorDispProjetada;
					  valores[4] = valorCobertura;
					  
					  mapTotalizacao.put(key, valores);
					  
				  } else {
					  
					  //    Se o período já consta no Map, então temos que adicionar os valores referentes do PlPerMod atual, 
					  // a fim de totalizar este valor.
					  Double[] valores = mapTotalizacao.get(key);
					  
					  valores[0] += valorVendas;
					  valores[1] += valorPedidos;
					  valores[2] += valorProducao;
					  valores[3] += valorDispProjetada;
					  valores[4] += valorCobertura;
				  }
			  }
		  }
		  
		  //   Precisamos agora converter este Map para um objeto List, pois a exibição na Tela (que é um ListDataModel) 
		  // exige este tipo de estrutura de dado.
		  List<PlPerModAgregado> plPerModsAgregados = new ArrayList<PlPerModAgregado>();
		  
		  for (int i = 1; i <= mapTotalizacao.size(); i++) {
			  
			  PlPerModAgregado plPerModAgregado = new PlPerModAgregado();
			  Double[] valores = mapTotalizacao.get(i);
			  
			  try {
				  plPerModAgregado.setPerioPM(perioPMService.recuperaPerioPMPorPeriodoPM(i));
				  plPerModAgregado.setVendasModel(valores[0]);
				  plPerModAgregado.setPedidosModel(valores[1]);
				  plPerModAgregado.setProducaoModel(valores[2]);
				  plPerModAgregado.setDispProjModel(valores[3]);
				  plPerModAgregado.setCoberturaModel(valores[4]);
			  } catch (AplicacaoException e) {
			  }
			  
			  plPerModsAgregados.add(plPerModAgregado);
		 }
		  
		  
		 // SÓ PARA TESTAR E EXIBIR MESMO OS RESULTADOS... =]
		  for (PlPerModAgregado plPerModAgregado : plPerModsAgregados) {
			  
			  System.out.println(plPerModAgregado.getPerioPM());
			  System.out.println("Total Vendas = " + plPerModAgregado.getVendasModel());
			  System.out.println("Total Pedidos = " + plPerModAgregado.getPedidosModel());
			  System.out.println("Total Produção = " + plPerModAgregado.getProducaoModel());
			  System.out.println("Total Disponibilidade = " + plPerModAgregado.getDispProjModel());
			  System.out.println("Total Cobertura = " + plPerModAgregado.getCoberturaModel());
			  System.out.println("");
		  }
		  
	  }
	  
	  @Test
	  public void testarOrdenacaoPlanoModelosEmCadPlan(){

		  CadPlan cadPlan = null;
		  
		  try {
			  cadPlan = cadPlanService.recuperaCadPlanComPlanosModelo("1");
		  	}
		  catch (ObjetoNaoEncontradoException e) {
		  }
		  for(PlanoModelo planoModelo : cadPlan.getPlanosModelo()){
			  System.out.println(planoModelo);
		  }
		  	
	  }
	  
}
