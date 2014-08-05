 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package avaliacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import modelo.CadPlan;
import modelo.CapacDia;
import modelo.CapacTecView;
import modelo.HP;
import modelo.Parametros;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import modelo.RecModel;
import modelo.Recurso;
import modelo.TecModel;
import modelo.Tecido;
import modelo.Usuario;
import modelo.relatorios.AnaliseMaquinaRelatorio;
import modelo.relatorios.AnaliseTecidoRelatorio;
import modelo.relatorios.AnaliseRecursoRelatorio;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.AvaliacaoAppService;
import service.CadPlanAppService;
import service.CapacDiaAppService;
import service.CapacTecViewAppService;
import service.HPAppService;
import service.ParametrosAppService;
import service.PerioPMAppService;
import service.PlanoModeloAppService;
import service.RecModelAppService;
import service.TecModelAppService;
import service.TecidoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.exception.ObjetoNaoEncontradoException;

import comparator.PlPerModComparatorPorPeriodoInicPMP;

import exception.relatorio.RelatorioException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.JPAUtil;
	
public class TesteAvaliacao {
	

	// Services
	private static CadPlanAppService cadPlanService;
	// Services
	private static AvaliacaoAppService avaliacaoService;
	// Services
	private static HPAppService hpService;
	private static PerioPMAppService perioPMService;
	private static CapacDiaAppService capacDiaService;
	private static CapacTecViewAppService capacTecViewService;
	private static TecidoAppService tecidoService;
	private static TecModelAppService tecModelService;
	private static RecModelAppService recModelService;
	private static ParametrosAppService parametrosService;
	private static PlanoModeloAppService planoModeloService;
	
	
	  @BeforeClass
	  public void setupClass(){
		
		 try {
			 System.out.println("-----------------------------> Startando a JPA...");
			 JPAUtil.JPAstartUp();
			 System.out.println("-----------------------------> JPA startada com sucesso!");

			 avaliacaoService = FabricaDeAppService.getAppService(AvaliacaoAppService.class);
				hpService = FabricaDeAppService.getAppService(HPAppService.class);
				perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
				capacDiaService = FabricaDeAppService.getAppService(CapacDiaAppService.class);
				capacTecViewService = FabricaDeAppService.getAppService(CapacTecViewAppService.class);
				cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
				tecidoService = FabricaDeAppService.getAppService(TecidoAppService.class);
				tecModelService = FabricaDeAppService.getAppService(TecModelAppService.class);
				recModelService = FabricaDeAppService.getAppService(RecModelAppService.class);
				parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);

				planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			 
		 } catch (Exception e) {
		 }
	  }

//	  @Test
//	  public void testarListarPlPerModsDeTodosOsPeriodosParaUmTecido(){
//		  try {
//			CadPlan cadPlan = cadPlanService.recuperaCadPlanPorCodigo("01");
//			try {
//				cadPlan = cadPlanService.recuperaCadPlanComPlanosModeloEPlPerMods(cadPlan.getCodPlan());
//			} catch (ObjetoNaoEncontradoException e) {
//				e.printStackTrace();
//			}
//			
//			Tecido tecido = tecidoService.recuperaTecidoPorCodigo("01");
//			
//			Map resultado = avaliacaoService.listarPlPerModsDeTodosOsPeriodosParaUmTecido(cadPlan, tecido);
//
//			List<PlPerMod> plPerMods = (List<PlPerMod>) resultado.get("plPerMods");
//			List<Double> necessidadesTotais = (List<Double>) resultado.get("necessidadesTotais");
//
//
//			List<HP> hpBD = hpService.recuperaListaDeHP();
//			if (hpBD.isEmpty()){
//				throw new AplicacaoException("hp.NAO_CADASTRADO");
//			}
//			HP hp = hpBD.get(0);
//			
//			List<PerioPM> perioPMsDoHP = perioPMService.recuperaIntervaloDePerioPMs
//			(hp.getPerioPMInicDemMod().getPeriodoPM(), hp.getPerioPMFinalDemMod().getPeriodoPM());
//			
//			for(PerioPM perioPM: perioPMsDoHP){
//				System.out.println("== periodo : " + perioPM.getPeriodoPM());
//				System.out.println("===== Necessidade total  : " + necessidadesTotais.get(perioPM.getPeriodoPM()-1));
//				
//			}
//			
//			
//		} catch (AplicacaoException e) {
//			e.printStackTrace();
//		}
//	  }
	  @Test
	  public void testarGraficoNecDispPerio(){
		  try {
			CadPlan cadPlan = cadPlanService.recuperaCadPlanPorCodigo("01");
			try {
				cadPlan = cadPlanService.recuperaCadPlanComPlanosModeloEPlPerMods(cadPlan.getCodPlan());
			} catch (ObjetoNaoEncontradoException e) {
				e.printStackTrace();
			}
			
			Tecido tecido = tecidoService.recuperaTecidoPorCodigo("01");
			
			
		//	String str = avaliacaoService.imprimirDadosGraficoAvaliacaoTecido(cadPlan,tecido);					

		//	System.out.println(str);
			
		} catch (AplicacaoException e) {
			e.printStackTrace();
		}
	  }
}
