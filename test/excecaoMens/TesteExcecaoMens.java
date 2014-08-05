 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package excecaoMens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Excecao;
import modelo.ExcecaoMens;
import modelo.PlPerMod;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.ExcecaoDAO;
import DAO.Impl.ExcecaoDAOImpl;
import DAO.controle.FabricaDeDao;

import service.ExcecaoMensAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class TesteExcecaoMens {
private static ExcecaoMensAppService excecaoMensService;
private static ExcecaoDAO excecaoDAO; 
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");

			excecaoDAO = FabricaDeDao.getDao(ExcecaoDAOImpl.class);
			excecaoMensService = FabricaDeAppService.getAppService(ExcecaoMensAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testeVetorGlobal(){

//		Map resultado = new HashMap();
//		resultado.put("listaAnaliseMaquinaViews" , listaAnaliseMaquinaViews);
//		List<PlPerMod> listaPlPerMods = (List<PlPerMod>)resultado.get("plPerMods");
		ArrayList<Map> vetorMap = new ArrayList<Map>();
		System.out.println("Inicio===");
		for(Map map : vetorMap){
			int periodo = (Integer)map.get("periodo");
			boolean recebimentoVerificado = (Boolean)map.get("recebimentoVerificado");
			int producao = (Integer)map.get("producao");
			System.out.println("periodo: " + periodo+ " recebimento: "+ recebimentoVerificado+ " producao: "+producao);			
		}
		vetorMap = criaVetorMap(vetorMap);
		System.out.println("Meio===");
		for(Map map : vetorMap){
			int periodo = (Integer)map.get("periodo");
			boolean recebimentoVerificado = (Boolean)map.get("recebimentoVerificado");
			int producao = (Integer)map.get("producao");
			System.out.println("periodo: " + periodo+ " recebimento: "+ recebimentoVerificado+ " producao: "+producao);			
		}
		vetorMap = alteraVetorMap(vetorMap);
		System.out.println("Final===");
		for(Map map : vetorMap){
			int periodo = (Integer)map.get("periodo");
			boolean recebimentoVerificado = (Boolean)map.get("recebimentoVerificado");
			int producao = (Integer)map.get("producao");
			System.out.println("periodo: " + periodo+ " recebimento: "+ recebimentoVerificado+ " producao: "+producao);			
		}
		alteraVetorMapSemRetorno(vetorMap);
		System.out.println("Final(sem retorno)===");
		for(Map map : vetorMap){
			int periodo = (Integer)map.get("periodo");
			boolean recebimentoVerificado = (Boolean)map.get("recebimentoVerificado");
			int producao = (Integer)map.get("producao");
			System.out.println("periodo: " + periodo+ " recebimento: "+ recebimentoVerificado+ " producao: "+producao);			
		}
		
		
	}

	public ArrayList<Map> criaVetorMap(ArrayList<Map> vetorMap){
		for(int i=0;i<10;i++){
			Map novoMap = new HashMap();
			novoMap.put("periodo", i);
			novoMap.put("recebimentoVerificado", false);
			novoMap.put("producao", i*10);
			vetorMap.add(novoMap);
		}
		return vetorMap;
	}

	public ArrayList<Map> alteraVetorMap(ArrayList<Map> vetorMap){
		

		for(Map map : vetorMap){
			Integer novaProd = (Integer)map.get("producao")*10;
			boolean novoRec= !(Boolean)map.get("recebimentoVerificado");
			map.put("producao", novaProd);
			map.put("recebimentoVerificado", novoRec);
		}
		return vetorMap;
	}
	public void alteraVetorMapSemRetorno(ArrayList<Map> vetorMap){
		

		for(Map map : vetorMap){
			Integer novaProd = (Integer)map.get("producao")*100;
			boolean novoRec= !(Boolean)map.get("recebimentoVerificado");
			map.put("producao", novaProd);
			map.put("recebimentoVerificado", novoRec);
		}
	}
	
//	@Test
	public void testandoVetorComecandoComNada(){
		List<Excecao> excecoes = excecaoDAO.recuperaListaDeExcecoes();
		
		List<Excecao> excecoes2 = new ArrayList<Excecao>();
		Excecao excecaoLixo = new Excecao();
		excecoes2.add(excecaoLixo);
		excecoes2.addAll(excecaoDAO.recuperaListaDeExcecoes());

		System.out.println("size normal: "+ excecoes.size());
		System.out.println("size outro: "+ excecoes2.size());	
		System.out.println("tipo1: "+ excecoes.get(1));
		System.out.println("tipo1: "+ excecoes2.get(1));		
	}
	
}
