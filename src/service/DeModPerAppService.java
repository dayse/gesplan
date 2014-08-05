 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.List;

import modelo.DeModPer;
import modelo.HP;
import modelo.Modelo;
import modelo.PerioPM;
import br.blog.arruda.plot.Plot;
import br.blog.arruda.plot.data.PlotData;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.DeModPerDAO;
import DAO.HPDAO;
import DAO.PerioPMDAO;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import java.lang.Math;

public class DeModPerAppService {
	
	//DAOs
	private static DeModPerDAO deModPerDAO; 
	private static PerioPMDAO perioPMDAO; 
	private static HPDAO hpDAO;
	
	// Services
	private static DadosGraficoViewAppService dadosGraficoViewService;
	
	@SuppressWarnings("unchecked")
	public DeModPerAppService() {
		try {
			//DAOs
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			
			// Service
			dadosGraficoViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Transacional
	public void incluiDemandas(Modelo modelo) throws AplicacaoException {
		
		DeModPer demanda = null;
		int periodoInicial;
		int periodoFinal;
		
		List<HP> hp = hpDAO.recuperaListaDeHP();
		
		if (hp.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		} 
		
		HP hpCorrente = hp.get(0);
		
		try {
			periodoInicial = hpCorrente.getPerioPMInicDemMod().getPeriodoPM();
			periodoFinal = hpCorrente.getPerioPMFinalDemMod().getPeriodoPM();
		
		} catch (NullPointerException ex){
			throw new AplicacaoException("hp.PERIODO_DEMANDA_NAO_CADASTRADO");
		}
		
		List<PerioPM> periodos = perioPMDAO.recuperaIntervaloDePerioPMs(periodoInicial, periodoFinal);
		
		for (PerioPM perioPM : periodos) {
			
			demanda = new DeModPer(modelo, perioPM);
			
			deModPerDAO.inclui(demanda);
		}
	}

	/**
	 * Devolve o valor maximo para demanda a partir da tabela deModPer.
	 * Atencao: Se nao estiver usando para inclusao, deve usar o metodo alternativo que
	 * esta em PlPerMod que devolve a demanda a partir do proprio PlPerMod.
	 * @param modelo
	 * @param perioPM
	 * @param hpDB
	 * @return
	 */
	@Transacional
	public Double obtemDemandaMaxProxPeriodo(Modelo modelo, PerioPM perioPM, HP hpDB) {
		
		Double demandaMaxProxPeriodo = 0.0;
		
		if (perioPM.getPeriodoPM() >= hpDB.getPerioPMFinalPMP().getPeriodoPM()){
			perioPM = hpDB.getPerioPMFinalPMP();
		}
		else{
			//Recupera o proximo perioPM
			try {
				perioPM = perioPMDAO.recuperaPerioPMPorPeriodoPM(perioPM.getPeriodoPM()+1);
			} catch (ObjetoNaoEncontradoException e) {
			}
		}
		
		
		try {
			DeModPer deModPerCorrente = deModPerDAO.recuperaDeModPerPorPeriodoEModelo(perioPM, modelo);
			demandaMaxProxPeriodo = Math.max(deModPerCorrente.getVendasProjetadasModelo(),deModPerCorrente.getPedidosModelo());
			
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		return demandaMaxProxPeriodo;
		
	}	
		
	public Plot gerarDadosGrafico(List<DeModPer> demandasModelo) {
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		Plot grafico = new Plot();

		PlotData dado = new PlotData();
		ArrayList<Double> listVendasModelo = new ArrayList<Double>();
		ArrayList<Double> listPedidosModelo = new ArrayList<Double>();
		ArrayList<Double> listPeriodosPM = new ArrayList<Double>();
		
		//popula os dados que irao para os eixos.
		for(DeModPer deModPer : demandasModelo){
			listVendasModelo.add(deModPer.getVendasProjetadasModelo());
			listPedidosModelo.add(deModPer.getPedidosModelo());			
			listPeriodosPM.add(Double.valueOf(deModPer.getPeriodo().getPeriodoPM()));
		}
		
		//Cria os dados de grafico com os valores recuperados anteriormente.
		PlotData dadoVendasModelo = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listVendasModelo);
		dadoVendasModelo.setLabel("Vendas");
		listaDadosGrafico.add(dadoVendasModelo);
		PlotData dadoPedidosModelo = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listPedidosModelo);
		dadoPedidosModelo.setLabel("Pedidos");
		listaDadosGrafico.add(dadoPedidosModelo);
		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Periodo", "Vendas/Pedidos");


		//retorna o  grafico.
		return grafico;
		
	}	
		

	public String imprimirDadosGrafico(List<DeModPer> demandasModelo) {
		Plot grafico = gerarDadosGrafico(demandasModelo);
		return grafico.printData();		
	}
	
	
	
	@Transacional
	public void inclui(DeModPer deModPer){
		deModPerDAO.inclui(deModPer);
	}


	@Transacional
	public void altera(DeModPer deModPer) {
		deModPerDAO.altera(deModPer);
	}
	
	
	@Transacional
	public void exclui(DeModPer deModPer){
		
		DeModPer deModPerBD = null;
		
		try {
			deModPerBD = deModPerDAO.getPorIdComLock(deModPer.getIdDeModPer());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		deModPerDAO.exclui(deModPerBD);
	}
	
	public List<DeModPer> recuperaListaDeDemandaModeloPeriodo() {
		return deModPerDAO.recuperaListaDeDemandaModeloPeriodo();
	}

	public List<DeModPer> recuperaListaDeDeModPerPorModelo(Modelo modelo) {
		return deModPerDAO.recuperaListaDeDeModPerPorModelo(modelo);  
	}
}
