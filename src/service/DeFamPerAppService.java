 
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

import modelo.DeFamPer;
import modelo.DeModPer;
import modelo.Familia;
import modelo.HP;
import modelo.Modelo;
import modelo.PerioPAP;
import modelo.PerioPM;
import br.blog.arruda.plot.Plot;
import br.blog.arruda.plot.data.PlotData;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.DeFamPerDAO;
import DAO.DeModPerDAO;
import DAO.FamiliaDAO;
import DAO.HPDAO;
import DAO.PerioPAPDAO;
import DAO.PerioPMDAO;
import DAO.Impl.DeFamPerDAOImpl;
import DAO.Impl.DeModPerDAOImpl;
import DAO.Impl.FamiliaDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPAPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * 
 * DeFamPerAppService é uma classe de serviço que possui as regras de negócio para manipular inicialmente
 * a entidade DeFamPer. Estas manipulações incluem quando necessário chamadas as interfaces DAOs,
 * outras classes de serviço e acessos a informações do BD.
 * 
 * A classe DeFamPerAppService fora criada para atender ao Padrão MVC, Model Vision Control, sendo a mesma uma
 * classe de serviço que é capaz de efetuar: controle de transação, ou seja esta classe possui o recurso de
 * abrir transaçao, commitar e fechar uma transaçao através de um interceptador de serviço.
 * Neste interceptador será definido se o método é transacional ou não e em função desta informação
 * o interceptador irá usar ou não uma transação. 
 * 
 * @author marques.araujo
 * 
 */

public class DeFamPerAppService {
	
	// DAOs
	private static FamiliaDAO familiaDAO;
	private static PerioPAPDAO perioPAPDAO; 
	private static DeFamPerDAO deFamPerDAO; 
	private static DeModPerDAO deModPerDAO; 
	private static HPDAO hpDAO;
	
	// Services
	private static DadosGraficoViewAppService dadosGraficoViewService;
	private static PerioPAPAppService perioPAPAppService ;
	
	@SuppressWarnings("unchecked")
	public DeFamPerAppService() {
		try {
			
			// DAOs
			familiaDAO = FabricaDeDao.getDao(FamiliaDAOImpl.class);
			perioPAPDAO = FabricaDeDao.getDao(PerioPAPDAOImpl.class);
			deFamPerDAO = FabricaDeDao.getDao(DeFamPerDAOImpl.class);
			deModPerDAO = FabricaDeDao.getDao(DeModPerDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			
			// Services
			perioPAPAppService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			dadosGraficoViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	
	@Transacional
	public void altera(DeFamPer deFamPer) {
		deFamPerDAO.altera(deFamPer);
	}
	

	@Transacional
	public void inclui(DeFamPer deFamPer){
		deFamPerDAO.inclui(deFamPer);
	}
	/**
	 * Inclui defamper totalizando os dados de modelo.
	 * @param deFamPer
	 */
	@Transacional
	public void incluiTotalizandoDeModPer(DeFamPer deFamPer){
		inclui(deFamPer);
		totalizaDeModPerParaDeFamPer(deFamPer);
	}

	/**
	 * Totaliza o DeFamPer em questao, percorrendo os demodpers relativos
	 * a ele, e totalizando ovenda e pedido.
	 * depois salva o defamper.
	 * @param deFamPer
	 */
	@Transacional
	public void totalizaDeModPerParaDeFamPer(DeFamPer deFamPer){
		//recupera a lista de demodpers desse defamper
		List<DeModPer> listaDeModPer = recuperaDeModPersDeUmDeFamPer(deFamPer);
		Double vendaTotalizada=0.0;
		Double pedidoTotalizado= 0.0;
		//percorre os demodpers totalizando seus valores
		for(DeModPer deModPer: listaDeModPer){
			vendaTotalizada+=deModPer.getVendasProjetadasModelo();
			pedidoTotalizado+=deModPer.getPedidosModelo();
		}
		//seta os valores totalizados em defamper.
		deFamPer.setVendasProjetadasFamilia(vendaTotalizada);
		deFamPer.setPedidosFamilia(pedidoTotalizado);
		//salva o defamper em banco com as alteracoes.
		altera(deFamPer);
	}
	
	/**
	 * Totaliza vendas e pedidos de todos os deFamPers
	 */
	@Transacional
	public void totalizaDeModPerParaTodosDeFamPers(){
		List<DeFamPer> listaDeFamPers = deFamPerDAO.recuperaListaDeDemandaFamiliaPerioPAP();
		for(DeFamPer deFamPer: listaDeFamPers){
			totalizaDeModPerParaDeFamPer(deFamPer);
		}
	}
	
	/**
	 * Recupera os DeModPer de um DeFamPer.
	 * @param deFamPer
	 * @return
	 */
	@Transacional
	public List<DeModPer> recuperaDeModPersDeUmDeFamPer(DeFamPer deFamPer){
		
		List<DeModPer> listaDeModPer = new ArrayList<DeModPer>();
		List<PerioPM> listaPerioPM = deFamPer.getPerioPAP().getPerioPMs();

		//recupera os modelos dessa familia desse defamper.
		Familia familia=null;
		try {
			familia = familiaDAO.recuperaUmaFamiliaEModelos(deFamPer.getFamilia().getId());
		} catch (ObjetoNaoEncontradoException e1) {
		}		
		List<Modelo> listaModelo = familia.getModelos();	
		
		//percorre os periosPM do perioPAP do defamper em questao.
		for(PerioPM perioPM: listaPerioPM){			
			//percorre os modelos da familia desse defamper.
			for(Modelo modelo : listaModelo){
				try {
					DeModPer deModPer = deModPerDAO.recuperaDeModPerPorPeriodoEModelo(perioPM, modelo);
					listaDeModPer.add(deModPer);
				} catch (ObjetoNaoEncontradoException e) {
				}				
			}
		}
		return listaDeModPer;
	}
	
	
	
	@Transacional
	public void exclui(DeFamPer deFamPer){
		
		DeFamPer deFamPerBD = null;
		
		try {
			deFamPerBD = deFamPerDAO.getPorIdComLock(deFamPer.getIdDeFamPer());
		} catch (ObjetoNaoEncontradoException e) {			
		}
		
		deFamPerDAO.exclui(deFamPerBD);
	}
	
	
    /**
     * Inclui defampers baseado no intervalo pap do hpCorrente.
     * @param familia
     * @throws AplicacaoException
     */
	@Transacional
	public void incluiDemandas(Familia familia) throws AplicacaoException {
		
		DeFamPer deFamPer = null;
		int periodoInicial;
		int periodoFinal;
		
		List<HP> hp = hpDAO.recuperaListaDeHP();
		
		if (hp.isEmpty()){
			throw new AplicacaoException("hp.NAO_CADASTRADO");
		} 
		
		HP hpCorrente = hp.get(0);
		
		try {
			
			periodoInicial = hpCorrente.getPerioPAPInicDemFam().getPeriodoPAP();     //.getPeriodoPM(); // .getPerioPMInicDemMod().getPeriodoPM(); 
			//periodoInicial = hpCorrente.getPerioPMInicDemMod().getPeriodoPM();
//			System.out.println("***************************"+periodoInicial);
			
			periodoFinal   = hpCorrente.getPerioPAPFinalDemFam().getPeriodoPAP();   //.getPeriodoPM();   // .getPerioPMFinalDemMod().getPeriodoPM(); 
			//periodoFinal   = hpCorrente.getPerioPMFinalDemMod().getPeriodoPM();
			
//			System.out.println("*********************"+periodoFinal);
			
			
		} catch (NullPointerException ex){
			throw new AplicacaoException("hp.PERIODO_DEMANDA_NAO_CADASTRADO");
		}
		
		//List<PerioPM> periodos = perioPMDAO.recuperaIntervaloDePerioPMs(periodoInicial, periodoFinal);
				
		List<PerioPAP> periodos = perioPAPDAO.recuperaIntervaloDePerioPAPs(periodoInicial, periodoFinal);
		
//		System.out.println("/////////////////"+periodos+"/////////////////////");
			
		
		for (PerioPAP perioPAP : periodos) {
			
			deFamPer = new DeFamPer(familia, perioPAP);
			
//			System.out.println(">>>>>>>>>>>>>>>"+deFamPer);
			
			deFamPerDAO.inclui(deFamPer);
		}
	}

	/**
	 * Retorna uma string que representa os dados do grafico de Demanda de uma familia.
	 * @param demandasModelo
	 * @return
	 */
	public Plot gerarDadosGrafico(List<DeFamPer> demandasFamilia) {
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		Plot grafico = new Plot();

		PlotData dado = new PlotData();
		ArrayList<Double> listVendasTotais = new ArrayList<Double>();
		ArrayList<Double> listPedidosTotais = new ArrayList<Double>();
		ArrayList<Double> listPeriodosPAP = new ArrayList<Double>();
		
		//popula os dados que irao para os eixos.
		for(DeFamPer deFamPer : demandasFamilia){
			listVendasTotais.add(deFamPer.getVendasProjetadasFamilia());
			listPedidosTotais.add(deFamPer.getPedidosFamilia());			
			listPeriodosPAP.add(Double.valueOf(deFamPer.getPerioPAP().getPeriodoPAP()));
		}
		
		//Cria os dados de grafico com os valores recuperados anteriormente.
		PlotData dadoVendasTotais = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPAP, listVendasTotais);
		dadoVendasTotais.setLabel("Vendas");
		listaDadosGrafico.add(dadoVendasTotais);
		PlotData dadoPedidosTotais = dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPAP, listPedidosTotais);
		dadoPedidosTotais.setLabel("Pedidos");
		listaDadosGrafico.add(dadoPedidosTotais);

		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Agregado", "Vendas/Pedidos");

		//retorna o  grafico.
		return grafico;
		
	}
	/**
	 * Retorna uma string que representa os dados do grafico de Demanda de uma familia.
	 * @param demandasModelo
	 * @return
	 */
	public String imprimirDadosGrafico(List<DeFamPer> demandasFamilia) {
		Plot grafico = gerarDadosGrafico(demandasFamilia);
		 return grafico.printData();
	 }
	
		
	/**
	 * Como a relacao é eager, ao recuperar a lista de perioPAPs vem junto as
	 * listas de PerioPMs Alem de recuperar lista paginada atribui os campos
	 * calculados todos.
	 * 
	 */
	public List<PerioPAP> recuperaListaPaginadaDeFamPerPerioPAPs() throws AplicacaoException{//public List<PerioPAP> recuperaListaPaginadaDePerioPAPs() {
		List<PerioPAP> listaDePerioPAP = perioPAPDAO.recuperaListaPaginadaDePerioPAPs();
		
		if(!listaDePerioPAP.isEmpty()){
			
//			System.out.println(">>>>>>>>>>>>>>>>>>>>> A lista de Pap nao esta vazia! <<<<<<<<<<<<<<<<<<<<<<<<<<");
//			System.out.println(">>>>>>>>>>>>>>>>>>listaDePerioPAP ="+listaDePerioPAP);
		}

//		System.out.println(">>>>>>>>>>>>> Tamanho = "+listaDePerioPAP.size());
		
		for (PerioPAP perioPAP : listaDePerioPAP) {


			// Obtem PeriodoPM inicial e final desse PerioPAP
			
			PerioPM perioPMInicial = perioPAPAppService.obtemPrimeiroPerioPMdoPerioPAP(perioPAP);
//			System.out.println("Pegou o perioPMInicial"+perioPMInicial);
			
			// se for null significa que o perioPAP atual deveria ter sido
			// deletado e por alguma razao n foi,
			// pois nao tem nenhum perioPM ligado a ele.

			// Atribui valores atuais para os campos calculados
			perioPAP.setPeriodoPMInic(perioPMInicial.getPeriodoPM());
			perioPAP.setDataInicial(perioPMInicial.getDataInicial());

			PerioPM perioPMFinal = perioPAPAppService.obtemUltimoPerioPMdoPerioPAP(perioPAP);
			perioPAP.setPeriodoPMFinal(perioPMFinal.getPeriodoPM());
			perioPAP.setDataFinal(perioPMFinal.getDataFinal());

			/*
			perioPAP.setNumDiasUteis(perioPMService
					.calculaTotalDiasUteisIntervalo(perioPMInicial,
							perioPMFinal));
			*/
		}
	

		return listaDePerioPAP;
	}
	
	public List<DeFamPer> recuperaListaDeDeFamPerPorFamilia(Familia familia){
		
		List<DeFamPer> listaDeFamper =  deFamPerDAO.recuperaListaDeDeFamPerPorFamilia(familia);
		
		
		
		return listaDeFamper;
	}
	
	
	
	
	
	
}
