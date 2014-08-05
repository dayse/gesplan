 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import modelo.CadPlan;
import modelo.DeModPer;
import modelo.Familia;
import modelo.HP;
import modelo.HPVig;
import modelo.Modelo;
import modelo.PMP;
import modelo.PerioPM;
import modelo.PerioPMVig;
import modelo.PlPerMod;
import modelo.PlPerModAgregado;
import modelo.PlanoMestreDeProducaoPorModeloRelatorio;
import modelo.PlanoModelo;

import br.blog.arruda.plot.Plot;
import br.blog.arruda.plot.data.PlotData;
import motorInferencia.MotorInferencia;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.Numeric;
import DAO.HPDAO;
import DAO.HPVigDAO;
import DAO.PMPDAO;
import DAO.PerioPMDAO;
import DAO.PerioPMVigDAO;
import DAO.PlPerModDAO;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.HPVigDAOImpl;
import DAO.Impl.PMPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.PerioPMVigDAOImpl;
import DAO.Impl.PlPerModDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import comparator.PlPerModComparatorPorPerioPM;

import exception.motorInferencia.MotorInferenciaException;
import exception.relatorio.RelatorioException;

/**
 * 
 * As classes de serviço é que possuem as regras de negócio. Fazem as críticas
 * quando necessário e chamam as classes DAO para pegar as informações do BD.
 * 
 * São essas classes AppService que fazem o controle de transaçao, ou seja quem
 * abre a transaçao, "quem commita" através do interceptador de appservice. Aqui
 * defino se o metodo é transacional ou não e em funçao desta informaçao o
 * interceptador vai usar ou nao transacao.
 * 
 * Classe responsavel pela implementação do ajuste do plano mestre.
 * Contem os algoritimos relativos a equação de conservação de estoque.
 * 
 * @author felipe.arruda
 *
 */
public class PlPerModAppService {
	
	// DAOs
	private static PlPerModDAO plPerModDAO;
	private static HPVigDAO hpVigDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	private static PerioPMDAO perioPMDAO;
	private static PMPDAO pmpDAO;
	private static HPDAO hpDAO;
	
	// Services
	private static UsuarioAppService usuarioService;
	private static PerioPMAppService perioPMService;
	private static PlanoModeloAppService planoModeloService;
	private static DadosGraficoViewAppService dadosGraficoViewService;
	private static ModeloAppService modeloService;
	
	public PlPerModAppService() {
		
		try {
			
			// DAOs
			hpVigDAO = FabricaDeDao.getDao(HPVigDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			pmpDAO = FabricaDeDao.getDao(PMPDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			plPerModDAO = FabricaDeDao.getDao(PlPerModDAOImpl.class);
			
			// Services
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			perioPMService = FabricaDeAppService.getAppService(PerioPMAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			dadosGraficoViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Inclui um PlPErMod
	 * @param plPerMod
	 */
	@Transacional
	public void inclui(PlPerMod plPerMod){
		plPerModDAO.inclui(plPerMod);
	}
	
	/**
	 * Altera um plPerMod
	 * @param plPerMod
	 */
	@Transacional
	public void altera(PlPerMod plPerMod){
		plPerModDAO.altera(plPerMod);
	}
	
	/**
	 * Exclui um plPerMod com lock
	 * @param plPerMod
	 */
	@Transacional
	public void exclui(PlPerMod plPerMod) {
		
		PlPerMod plPerModBD = null;
		
		try {
			plPerModBD = plPerModDAO.getPorIdComLock(plPerMod.getId());
		} catch (ObjetoNaoEncontradoException ex){
			
		}
		
		plPerModDAO.exclui(plPerModBD);
	}
	
	/**
	 * Gera o Grafico de DemandaMax, Producao e EstProj Por Periodo de PlPerMod
	 * @param listaPlPerMods
	 * @return
	 */
	public Plot gerarPlotDemandaMaxEProducaoEEstProjPorPeriodo(List<PlPerMod> listaPlPerMods) {
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		Plot grafico = new Plot(); 
		

		PlotData dado = new PlotData();
		ArrayList<Double> listDemandaMax = new ArrayList<Double>();
		ArrayList<Double> listProducao = new ArrayList<Double>();
		ArrayList<Double> listDispProj = new ArrayList<Double>();
//		ArrayList<Double> listESDesejado = new ArrayList<Double>();
		
		ArrayList<Double> listPeriodosPM = new ArrayList<Double>();
		
		int periodoAnterior = 0;
		//popula os dados que irao para os eixos.
		for(PlPerMod plPerMod: listaPlPerMods){
			double demandaMax = Math.max(plPerMod.getVendasModel(),plPerMod.getPedidosModel());
			
			listDemandaMax.add(demandaMax);
			listProducao.add(plPerMod.getProducaoModel());
			listDispProj.add(plPerMod.getDispProjModel());
			
			//so insere periodos que nao sao repetidos
			if(plPerMod.getPerioPM().getPeriodoPM() != periodoAnterior){
				listPeriodosPM.add(Double.valueOf(plPerMod.getPerioPM().getPeriodoPM()));
				periodoAnterior = plPerMod.getPerioPM().getPeriodoPM();
			}
		}
		
		//Cria os dados de grafico com os valores recuperados anteriormente.
		PlotData dadoDemandaMax = 
			dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listDemandaMax);
		dadoDemandaMax.setLabel("DemandaMax");
		listaDadosGrafico.add(dadoDemandaMax);
		
		PlotData dadoProducao =
			dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listProducao);
		dadoProducao.setLabel("Producao");
		listaDadosGrafico.add(dadoProducao);
		
		PlotData dadoDispProj =
			dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listDispProj);
		dadoDispProj.setLabel("estq. projetado");
		listaDadosGrafico.add(dadoDispProj);
		
		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Periodo", "Unidades");
		//retorna o  grafico.
		return grafico;
		
	}	
	/**
	 * Imprime dados do Grafico de DemandaMax, Producao e EstProj Por Periodo de PlPerMod
	 * @param listaPlPerMods
	 * @return
	 */
	public String imprimirDadosGraficoDemandaMaxEProducaoEEstProjPorPeriodo(List<PlPerMod> listaPlPerMods) {
		Plot grafico = gerarPlotDemandaMaxEProducaoEEstProjPorPeriodo(listaPlPerMods); 
		return grafico.printData();
	}

	/**
	 * Gera o Grafico de DemandaMax, Producao e EstProj Por Periodo de PlPerMod
	 * @param listaPlPerMods
	 * @return
	 */
	public Plot gerarPlotDemandaMaxEProducaoEEstProjEESDesejadoPorPeriodo(List<PlPerMod> listaPlPerMods) {
		ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		Plot grafico= new Plot();

		PlotData dado = new PlotData();
		ArrayList<Double> listDemandaMax = new ArrayList<Double>();
		ArrayList<Double> listProducao = new ArrayList<Double>();
		ArrayList<Double> listDispProj = new ArrayList<Double>();
		ArrayList<Double> listESDesejado = new ArrayList<Double>();
		
		ArrayList<Double> listPeriodosPM = new ArrayList<Double>();
		
		int periodoAnterior = 0;
		//popula os dados que irao para os eixos.
		for(PlPerMod plPerMod: listaPlPerMods){
			double demandaMax = Math.max(plPerMod.getVendasModel(),plPerMod.getPedidosModel());
			
			listDemandaMax.add(demandaMax);
			listProducao.add(plPerMod.getProducaoModel());
			listDispProj.add(plPerMod.getDispProjModel());
			listESDesejado.add(plPerMod.getEstqSegDesejado());
			
			//so insere periodos que nao sao repetidos
			if(plPerMod.getPerioPM().getPeriodoPM() != periodoAnterior){
				listPeriodosPM.add(Double.valueOf(plPerMod.getPerioPM().getPeriodoPM()));
				periodoAnterior = plPerMod.getPerioPM().getPeriodoPM();
			}
		}
		
		//Cria os dados de grafico com os valores recuperados anteriormente.
		PlotData dadoDemandaMax = 
			dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listDemandaMax);
		dadoDemandaMax.setLabel("DemandaMax");
		listaDadosGrafico.add(dadoDemandaMax);
		
		PlotData dadoProducao =
			dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listProducao);
		dadoProducao.setLabel("Producao");
		listaDadosGrafico.add(dadoProducao);

		PlotData dadoDispProj =
			dadosGraficoViewService.gerarPlotDataEmBarras(listPeriodosPM, listDispProj);
		dadoDispProj.setLabel("estq. projetado");
		listaDadosGrafico.add(dadoDispProj);
		
		PlotData dadoESdesejado =
			dadosGraficoViewService.gerarPlotData(listPeriodosPM, listESDesejado);
		dadoESdesejado.setLabel("Est. Seg. Desejado");
		listaDadosGrafico.add(dadoESdesejado);

		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Periodo", "Unidades");
		
		//retorna o grafico.
		return grafico;
		
	}	

	/**
	 * Recupera a lista de PlPerMod
	 * @return
	 */
	public List<PlPerMod> recuperaListaDePlPerMod(){
		return plPerModDAO.recuperaListaDePlPerMod();
	}
	
	/**
	 * Recupera uma lista de PlPerMod por um planoModelo.
	 * 
	 * @param planoModelo
	 * @return
	 */
	public List<PlPerMod> recuperaListaDePlPerModPorPlanoModelo(PlanoModelo planoModelo){
		return plPerModDAO.recuperaListaDePlPerModPorPlanoModelo(planoModelo);
	}
	
	/**
	 * Recupera uma lista de PlPerMod para um determinado CadPlan
	 *  
	 * É utilizado na tela de totalização dos plPerMods para um determinado CadPlan.
	 * 
	 * Caminho para a tela:
	 * cadastro de planos->totalizar 
	 * 
	 * Ele constroi um Map (mapTotalizacao) com os valores dos campos do plpermod totalizados  
	 * por periodo(com excecao da cobertura, que é media) considerando todos os modelos
	 * 
	 * Uma vez construido o Map fazemos uma copia para a lista plPerModsAgregados, pois precisamos de um
	 * list como input do datamodel para ser mostrado na tela.
	 * 
	 * 
	 * @param cadPlan
	 * @return
	 */
	public List<PlPerModAgregado> recuperaListaPlPerModAgregadoPorCadPlan(CadPlan cadPlan) {
		
		try {
			  // Foi utilizada a instanciacao neste ponto p/ evitar o problema de Inicializacao Ciclica!	
			  CadPlanAppService cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			  cadPlan = cadPlanService.recuperaCadPlanComPlanosModelo(cadPlan.getCodPlan());
		   } catch (ObjetoNaoEncontradoException e) {
		   } catch (Exception e) {
		}
		  
		// É preciso criar um Map, pois queremos a totalização de Valores por Modelo em um determinado período.
		  Map<Integer, Double[]> mapTotalizacao = new HashMap<Integer, Double[]>();
		  
		  for (PlanoModelo planoModelo : cadPlan.getPlanosModelo()) {
			
			  //Metodo que recupera um planoModelo com seus respectivos PlPerMods.
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

		  HP hp = hpDAO.recuperaListaDeHP().get(0);
			
		  List<PerioPM> perioPMsPMP = perioPMDAO.recuperaIntervaloDePerioPMs
						(hp.getPerioPMInicPMP().getPeriodoPM(), hp.getPerioPMFinalPMP().getPeriodoPM());
		  //faz a media do campo cobertura
		  for(PerioPM perioPM : perioPMsPMP){
			  // CHAVE: PeriodoPM
			  Integer key = perioPM.getPeriodoPM();	
			  //recupera os valores para esse periodo
			  Double[] valores = mapTotalizacao.get(key);
			  //acha a cobertura media no periodo, dividindo a soma de cada cobertura desse periodo e para todos os modelos, pelo num de modelos
			  Double valorCobertura = valores[4] / cadPlan.getPlanosModelo().size();
			  //Altera o valor da cobertura para o correto(verificar se esta alterando)
			  //mapTotalizacao.put(key, valores);
			  valores[4] = valorCobertura;
			  
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
		  
		return plPerModsAgregados;
	}
	
	/**
	 * Inclui os registros de PlPerMod relativos a um novo modelo,
	 * Inicializando os campos com 0(exceto o campo de flagProducao que é posto como livre
	 * e o Tuc que vem do Modelo)
	 * 
	 * Felipe.Arruda
	 * @param planoModelo
	 */
	public void incluirPlPerModParaNovoModelo(PlanoModelo planoModelo){
		
		HP hp = hpDAO.recuperaListaDeHP().get(0);
		
		List<PerioPM> perioPMsPMP = perioPMDAO.recuperaIntervaloDePerioPMs
					(hp.getPerioPMInicPMP().getPeriodoPM(), hp.getPerioPMFinalPMP().getPeriodoPM());
		
		for (PerioPM perioPM : perioPMsPMP) {
			
			PlPerMod plPerMod = new PlPerMod();
			
			plPerMod.setPlanoModelo(planoModelo);
			plPerMod.setPerioPM(perioPM);
			plPerMod.setDispProjModel(0.0);
			plPerMod.setVendasModel(0.0);
			plPerMod.setPedidosModel(0.0);
			plPerMod.setCoberturaModel(planoModelo.getModelo().getCobertura());
			plPerMod.setTuc(planoModelo.getModelo().getTuc());
			plPerMod.setFlagProducaoModel(planoModelo.getModelo().getFlagProducaoModel()); //Livre
			plPerMod.setProducaoModel(0.0);
			plPerMod.setProdLoteModel(0.0);
			plPerMod.setProdDiariaLoteModel(0.0);
			plPerMod.setInicioProdAntesHP(0.0);
			plPerMod.setPeriodoPMInicioPMP(0);
			plPerMod.setEscorePlanPerMod(0.0);
			plPerMod.setVarEstqPerc(0.0);
			plPerMod.setVarProdDiaPerc(0.0);
			
			this.inclui(plPerMod);
		}
	}

	
	/**
	 * É usado no momento da inclusao do plano mestre.
	 * Quando tem PMPVigente, que vai servir de subsidio para o novo plano incluido. 
	 * É utilizado quando o campo modelo.FlagProducaoModel é fixo, ou quando está dentro do intervalo
	 * fixo definido no arquivo de parametros.
	 * 
	 * No caso do modelo considerado fixo ou do periodo considerado fixo, aproveita a producaoModel 
	 * do PlanoVigente, mas para isto precisa saber em qual periodo do plano vigente cai a dataInicial
	 * do Plano atual.
	 * Esta procedure atribui ao capmpo producaoModel o valor da producaoModel do 
	 * periodo equivalente no plano vigente.
	 * No caso de nao ter PMPVigente devolve 0 para o campo ProducaoModel.
	 * 
	 * @author felipe.arruda
	 * @param dataInicial
	 * @param numDiasUteisMatriz
	 * @param plPerModCorrente
	 * @return
	 */
	@Transacional
	public PlPerMod obtemProducaoModelDoPMPVigente(Calendar dataInicial,Double numDiasUteisMatriz,
																			PlPerMod plPerModCorrente){
		
		//Recupera o HPVig
		HPVig hpVig = null;
		try{
			hpVig = hpVigDAO.recuperaListaDeHPVig().get(0);
		}catch(IndexOutOfBoundsException e){
			plPerModCorrente.setProducaoModel(0.0);
			return plPerModCorrente;
		}
		//Recupera o intervalo de perioPMVigs do HPVig
		List<PerioPMVig> perioPMVigs = perioPMVigDAO.recuperaIntervaloDePerioPMVigs
				(hpVig.getPerioPMInicPMP().getPeriodoPM(), hpVig.getPerioPMFinalPMP().getPeriodoPM());
		
		plPerModCorrente.setProducaoModel(0.0);
		boolean encontrouPerio = false;
		
		
		for(PerioPMVig perioPMvig : perioPMVigs){
			
			//Caso a dataInicial esteja dentro do intervalo do perioPMVig corrente
			if((perioPMvig.getDataInicial().compareTo(dataInicial) <= 0) &&
				(dataInicial.compareTo(perioPMvig.getDataFinal()) <= 0) ){
				PMP pmpCorrente = null;
				try {
					pmpCorrente = pmpDAO.recuperaPMPPorModeloEPerioPMVig
												(plPerModCorrente.getPlanoModelo().getModelo(),perioPMvig);
				} catch (ObjetoNaoEncontradoException e) {
				}
				
				//verificar oq fazer caso o numero de dias uteis na matriz do perioPMVig seja 0
				if(perioPMvig.getNumDiasUteisMatriz()==0){
					
				}
				else{
					Double prodDiariaPMPVig = pmpCorrente.getProducaoModel()/perioPMvig.getNumDiasUteisMatriz();
					plPerModCorrente.setProducaoModel(prodDiariaPMPVig*numDiasUteisMatriz);
				}
				//caso tenha encontrado o periodo ao qual pertence a datainicial, ele sai do for
				//e para de procurar outros periodos
				encontrouPerio = true;
				break;
				
			}
			
		}

	//Se houver perioPMVigs cadastrados
	if(!perioPMVigs.isEmpty()){
		//Se a data inicial do periodo do plano sendo incluido nao estiver contida no HP do plano vigente
		//entao usa a producao do ultimo periodo do HP vigente
		if(!encontrouPerio){
			
			PMP pmpCorrente = null;
			//Ultimo perioPMVig da lista de perioPMVigs
			PerioPMVig ultimoPerioPMvig = perioPMVigs.get(perioPMVigs.size()-1);
			
			try {
				//Recupera PMP usando o modelo do plPerModCorrente e o ultimoPerioPMVig
				pmpCorrente = pmpDAO.recuperaPMPPorModeloEPerioPMVig(plPerModCorrente.getPlanoModelo().getModelo(),
						ultimoPerioPMvig);
				//Calcula o valor da ProducaoModel utilizando o numDiasUteisMatriz do ultimo perioPMVig
				Double prodDiariaPMPVig = pmpCorrente.getProducaoModel()/ultimoPerioPMvig.getNumDiasUteisMatriz();
				plPerModCorrente.setProducaoModel(prodDiariaPMPVig*numDiasUteisMatriz);
				
			} catch (ObjetoNaoEncontradoException e) {
				//Caso nao existe um pmp com aquele modelo e o ultimo periodoPMVig
				//Seta o valor de producaoModel de plPerModCorrente para 0
				plPerModCorrente.setProducaoModel(0.0);
			}
			
		}
			
	}
		
		
		return plPerModCorrente;
	}

	/**
	 * Para um dado plano, recalcula valores para Disponibilidade Projetada, Produção ,
	 * Cobertura e Periodo Inicial para todos os modelos, 
	 * Para cada modelo, para cada periodo executa calculaDispProjProducaoECoberturaModel.
	 * E grava esse recalculo em arquivo(PlPerMod)
	 * 
	 * 
	 * @author felipe.arruda
	 * @param planoModelos
	 */
	@Transacional
	public void calculaDisProjProducaoECoberturaTodosModelos(List<PlanoModelo> planoModelos){
		
		//ordena por medida de segurança
		Collections.sort(planoModelos);	
		
		Double dispProjAnterior = new Double(0.0);
		
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		
		//Percorre a lista de planomodelos que possui neste momento a lista de modelos de um determinado plano
		//Usamos isso para acessar a lista de todos os modelos
		
		//Para cada modelo, para cada periodo, recupera um plPermod correspondente a esse par
		for (PlanoModelo planoModelo : planoModelos){
			//No calculo da primeira dispProj deve levar em consideração o estoque em falta, ou seja aquelas pecas que
			//já estavam previstas para chegar no período quando foi feito o plano vigente anterior mas que não poderiam porque o TR
			// exigiria que ele fosse fabricado antes para ser recebido naquele momento.
			// EstoqueEmFalta= a quantidade que ficou negativa na dispproj no plano vigente anterior e que estava indicando
			// que tinha venda para ser atendida naquele período que não havia sido atendida. 
			//OBS: Estoque em falta entrara subtraindo do estoque inicial.
			//
			//No calculo da primeira dispProj também deve levar em consideração o recebimento pendente, ou seja
			//O recebimento pendente entra na inclusão do plano somando com estoque inicial,
			//pois é uma quantidade que já está sendo praticamente entregue no estoque,
			//mas ainda está no chão de fabrica.
			//ATENÇAO: quando flagproducao é livre não vai aproveitar o que foi planejado no plano vigente e vai considerar que 
			// nao consegue receber producao no primeiro periodo (isso para tr >0 e <numdiasperiodo)
			dispProjAnterior = planoModelo.getModelo().getEstqInicModel()
								+ planoModelo.getModelo().getRecebimentoPendente()
								- planoModelo.getModelo().getEstqEmFalta();
			
//			if(dispProjAnterior < 0 ){
//				dispProjAnterior = 0.0;
//			}			
			
			//Recupera um intervalo que vai do periodo 1 ao periodo final do HP do Plano Mestre
			List<PerioPM> intervaloPerioPM = perioPMDAO.recuperaIntervaloDePerioPMs(1, hpBD.getPerioPMFinalPMP().getPeriodoPM());
			
			//Para cada periodo
			for(PerioPM perioPM : intervaloPerioPM){
				
				//Recupera um plPermod correspondente a esse par
				//E recalcula a dispProjAnterior usando o metodo calculaDispProjProducaoECoberturaModel
				//e grava em arquivo.
				PlPerMod plPerModCorrente = null;
				try {
					plPerModCorrente = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM(planoModelo, perioPM);
					
//					System.out.println("PERIOPM ATUAL = " + perioPM);
//					System.out.println("DISPPROJ-ANTES = " + dispProjAnterior);
					
					dispProjAnterior = calculaDispProjProducaoECoberturaModel(planoModelo, plPerModCorrente, dispProjAnterior, hpBD);
					
//					System.out.println("DISPPROJ-DEPOIS = " + dispProjAnterior);
					this.altera(plPerModCorrente);
				} catch (ObjetoNaoEncontradoException e) {
				}
			}
			
		}
		
	}

	/**
	 * Para um dado plano, recalcula valores para Disponibilidade Projetada, Produção ,
	 * Cobertura e Periodo Inicial para todos os modelos, empregando para isso um mecanismo de inferência fuzzy
	 * que gera inicialmente o valor da ProducaoModel
	 * <br>
	 * Para cada modelo, para cada periodo executa calculaFuzzyDispProjProducaoECoberturaModel.
	 * E grava esse recalculo em arquivo(PlPerMod)
	 * 
	 * 
	 * @author felipe.arruda
	 * @param planoModelos
	 * @throws AplicacaoException 
	 */
	@Transacional
	public void calculaFuzzyDisProjProducaoECoberturaTodosModelos(List<PlanoModelo> planoModelos) throws AplicacaoException{

		//ordena por medida de segurança
		Collections.sort(planoModelos);	
		
		Double dispProjAnterior = new Double(0.0);
		Double varEstqPerc = new Double(0.0);
		
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		
		//Percorre a lista de planomodelos que possui neste momento a lista de modelos de um determinado plano
		//Usamos isso para acessar a lista de todos os modelos
		
		//instancia aqui o motor de inferencia, pois ele será igual para cada plano modelo
		//do cadplan sendo utilizado, portanto fica fora da lista de planoModelos,
		//para evitar uma quantidade maior de vezes que o mesmo é instanciado.
		MotorInferencia motorInferencia= new MotorInferencia();
		//Para cada modelo, para cada periodo, recupera um plPermod correspondente a esse par
		// e obtem valores de estoque inicial do cadastro de modelos para inicializar a variavel dispProjAnterior
		// ATENÇÃO: USA TAMBEM O RECEBIMENTOPENDENTE
		for (PlanoModelo planoModelo : planoModelos){
			dispProjAnterior = planoModelo.getModelo().getEstqInicModel() 
								+ planoModelo.getModelo().getRecebimentoPendente()
								- planoModelo.getModelo().getEstqEmFalta();
			//if(dispProjAnterior < 0 ){
			//	dispProjAnterior = 0.0;
			//}			
			//Recupera lista de periodos em um intervalo que vai do periodo 1 ao periodo final do HP do Plano Mestre			
			List<PerioPM> intervaloPerioPM = perioPMDAO.recuperaIntervaloDePerioPMs(1, hpBD.getPerioPMFinalPMP().getPeriodoPM());
			
			//cria vetores com variaveis relativas a estoque a partir do indice 0 para corresponder aos valores iniciais relativos a t0
			double [] dispProjModels = new double[intervaloPerioPM.size()+1];
			
			double [] varEstqPercs = new double[intervaloPerioPM.size()+1];
			
			
			Map<String, double[]> variaveisDeEstoqueDefasadas = new HashMap<String, double[]>();
			variaveisDeEstoqueDefasadas.put("dispProjModels" , dispProjModels);
			variaveisDeEstoqueDefasadas.put("varEstqPercs" , varEstqPercs);

			//======calculando informacoes de base(relativas a t0) para o periodo 1			
			//PlPerMod do periodo 1 para fazer calculo de ESInicDesejado
			PlPerMod plPerModCorrente = null;
			try {
				plPerModCorrente = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM(planoModelo, intervaloPerioPM.get(0));			
			} catch (ObjetoNaoEncontradoException e) {
			}			
			dispProjModels[0] = dispProjAnterior;
			// calcula ESInicDesejado - Estoque de Segurança Inicial Desejado
			double ESInicDesejado = (obtemDemandaMaxProxPeriodo(plPerModCorrente, hpBD, true) * planoModelo.getModelo().getCobertura())/100;
			if( ESInicDesejado == 0){
				varEstqPercs[0] = 100;
			}
			//Calcula diferença percentual do estoque inicial projetado em relação ao Estoque de Segurança Inicial Desejado
			varEstqPercs[0] = ((planoModelo.getModelo().getEstqInicModel() - ESInicDesejado)/ESInicDesejado)*100;
			

			//Para cada periodo de 1 até n
			for(PerioPM perioPM : intervaloPerioPM){
				
				//Recupera um plPermod correspondente a esse par
				//E recalcula a dispProjAnterior usando o metodo calculaDispProjProducaoECoberturaModel
				//e grava em arquivo.
				try {
					plPerModCorrente = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM(planoModelo, perioPM);
					
//					System.out.println("PERIOPM ATUAL = " + perioPM);
//					System.out.println("DISPPROJ-ANTES = " + dispProjAnterior);
					 
					calculaFuzzyDispProjProducaoECoberturaModel(planoModelo, plPerModCorrente, variaveisDeEstoqueDefasadas, hpBD,motorInferencia);
					
//					System.out.println("DISPPROJ-DEPOIS = " + dispProjAnterior);
					this.altera(plPerModCorrente);
				} catch (ObjetoNaoEncontradoException e) {
				}
			}
			
		}	
		
	}
	
	/**
	 * IMPORTANTE: Não pega demanda do demodper
	 * 
	 * 
	 * 
	 * Tambem usado no momento da inclusao
	 * 
	 * Esta retornando o DispProj do periodo sendo calculado, que sera usado como
	 * DispProjAnterior na proxima rodada
	 * 
	 * */
	@Transacional
	public Double calculaDispProjProducaoECoberturaModel(PlanoModelo planoModelo, PlPerMod plPerModCorrente, Double dispProjAnterior, HP hpBD){
		//inicializa o valor do campo periodoPMInicioPMP com 0
		//ou seja comeca dizendo que a produção deveria ter sido realizada no periodo
		//anterior ao inicio do hp.
		plPerModCorrente.setPeriodoPMInicioPMP(0);
		
		//Obtem demanda maxima no proximo periodo.
		//sem acessar o DeModPer. Acessa os valores do proprio PlPerMod
		//que foram inicializados no momento da inclusao do plano.
		Double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente,hpBD);

		//variavel temporaria usada para facilitar a clareza da formula
		Double novoDispProjModel = 0.0;
		
		Boolean inicioPMPAntesInicioHP = false;
		
		Integer periodoDefasado = 0;
		
		//se flagProducao é "livre"
		//se for true, eh livre:
		if(plPerModCorrente.getFlagProducaoModel()){
			
			//calcula dispproj a partir da cobertura de estoque e da demandaMaxProxPeriodo
			//quando a cobertura do plano no periodo for negativa, usa a cobertura do modelo.
//			if(plPerModCorrente.getCoberturaModel() < 0){
				plPerModCorrente.setDispProjModel(demandaMaxProxPeriodo * planoModelo.getModelo().getCobertura()/100);
//			}
//			else{
//				plPerModCorrente.setDispProjModel(demandaMaxProxPeriodo * plPerModCorrente.getCoberturaModel()/100);
//			}
			

			//calcula produção a partir da dispProj e o max entre pedidos ou vendas do periodo
			plPerModCorrente.setProducaoModel(plPerModCorrente.getDispProjModel() - 
													dispProjAnterior +
													Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel()));
			
			
			//se a producao for negativa, atribui 0 pois não pode produzir qtd negativa.
			if(plPerModCorrente.getProducaoModel() < 0){
				plPerModCorrente.setProducaoModel(0.0);
				
				novoDispProjModel= plPerModCorrente.getProducaoModel() + dispProjAnterior - 
				Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
				
				plPerModCorrente.setDispProjModel(novoDispProjModel);
			}			
			
		}//if livre
		
		//se for fixo:
		//aproveita a producao ja estabelecida anteriormente e calcula a dispProj em funcao disso.
		else{ 
			novoDispProjModel = plPerModCorrente.getProducaoModel() + dispProjAnterior - 
															Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
			
			plPerModCorrente.setDispProjModel(novoDispProjModel);
		}

//		System.out.println("PERIODO DEFASADO (ANTES) = " + periodoDefasado);
//		System.out.println("INICIO PMP (ANTES) = " + inicioPMPAntesInicioHP);
		
		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());
		
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);
		
		
//		System.out.println("INICIO PMP (DEPOIS) = " + inicioPMPAntesInicioHP);
//		System.out.println("PERIODO DEFASADO (DEPOIS) = " + periodoDefasado);
		
		//so devera fazer isso se for flag livre!
		if(plPerModCorrente.getProducaoModel() > 0){
			
			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP  && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);

				novoDispProjModel = plPerModCorrente.getProducaoModel() + dispProjAnterior - 
																Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
				
				plPerModCorrente.setDispProjModel(novoDispProjModel);
				
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
			
		}
		
		//Calcula producao em lotes e prodDiariaLoteModel em funcao da producao em pcs ja calculada.
		if((plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() > 0)
				&& (planoModelo.getModelo().getTamLote() > 0)){
			

			//Versao antiga que nao utiliza aproximacao
			//int novaProdDiariaLoteModel = (int) (plPerModCorrente.getProducaoModel() / (planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz()));
			
			int novaProdDiariaLoteModel= Numeric.resultadoDivisaoInteira(plPerModCorrente.getProducaoModel(),(planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz()));
			plPerModCorrente.setProdDiariaLoteModel(new Double(novaProdDiariaLoteModel));
	


			//Versao antiga que nao utiliza aproximacao
			//int novaProdLoteModel = (int) (plPerModCorrente.getProdDiariaLoteModel() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz());
			
			//(resultadoMultiplicacaoInteira)Usada para evitar prodLoteModel fracionario.
			int novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente.getPerioPM().getNumDiasUteisMatriz());
			plPerModCorrente.setProdLoteModel(new Double(novaProdLoteModel));
					
			
		}
		else{
			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
		}		
		
		//Recalcula producao em pecas considerando a producao diaria em lotes,
		//devido ao arredondamento ocorrido no calculo da prodDiariaLoteModel
		if(plPerModCorrente.getProdLoteModel() < 0){
			plPerModCorrente.setProducaoModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProdDiariaLoteModel(0.0);			
		}
		else{
			Double novaProducaoModel = plPerModCorrente.getProdDiariaLoteModel() *
											planoModelo.getModelo().getTamLote() *
											plPerModCorrente.getPerioPM().getNumDiasUteisMatriz();
			
			plPerModCorrente.setProducaoModel(novaProducaoModel);
		}
		
		//recalcula dispProjModel com a nova producaoModel
		novoDispProjModel = plPerModCorrente.getProducaoModel() + dispProjAnterior - 
														Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
		plPerModCorrente.setDispProjModel(novoDispProjModel);
		
		
		//recalcula cobertura do modelo a partir da nova disponibilidade
		//quando a venda eh igual a zero a cobertura eh feita igual a do modelo
		if(demandaMaxProxPeriodo != 0){
			Double novaCoberturaModel = (plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo) * 100;
			plPerModCorrente.setCoberturaModel(novaCoberturaModel);
		}
		else
		{
			plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());	
		}
		
								
		return (dispProjAnterior = plPerModCorrente.getDispProjModel());
	}

	/**
	 * IMPORTANTE: Não pega demanda do demodper
	 * 
	 * 
	 * 
	 * Usado apenas na edição da cobertura pois permite manter a cobertura nova editada.  Na edição dos demais campos sempre tentará
	 * usar a cobertura do modelo do cadastro de modelos e não a do plpermod corrente.
	 * 
	 * Esta retornando o DispProj do periodo sendo calculado, que sera usado como
	 * DispProjAnterior na proxima rodada
	 * (metodo inexistente na versao em delphi)
	 * 
	 * */
	@Transacional
	public Double calculaDispProjProducaoPartindoCoberturaModel(PlanoModelo planoModelo, PlPerMod plPerModCorrente,
																PlPerMod plPerModAlterado, Double dispProjAnterior, HP hpBD){
		//inicializa o valor do campo periodoPMInicioPMP com 0
		//ou seja comeca dizendo que a produção deveria ter sido realizada no periodo
		//anterior ao inicio do hp.
		plPerModCorrente.setPeriodoPMInicioPMP(0);
		
		//Obtem demanda maxima no proximo periodo.
		//sem acessar o DeModPer. Acessa os valores do proprio PlPerMod
		//que foram inicializados no momento da inclusao do plano.
		Double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente,hpBD);

		//variavel temporaria usada para facilitar a clareza da formula
		Double novoDispProjModel = 0.0;
		
		Boolean inicioPMPAntesInicioHP = false;
		
		Integer periodoDefasado = 0;
		
		//se flagProducao é "livre"
		//se for true, eh livre:
		if(plPerModCorrente.getFlagProducaoModel()){
			
			//calcula dispproj a partir da cobertura de estoque e da demandaMaxProxPeriodo
			//usa a cobertura do modelo.
//			if(plPerModCorrente.getCoberturaModel() < 0){
			if(plPerModAlterado.getPerioPM().getPeriodoPM() == plPerModCorrente.getPerioPM().getPeriodoPM()){
				plPerModCorrente.setDispProjModel(demandaMaxProxPeriodo * plPerModCorrente.getCoberturaModel()/100);
			}else{
				plPerModCorrente.setDispProjModel(demandaMaxProxPeriodo * planoModelo.getModelo().getCobertura()/100);
			}
//			}
//			else{
//				plPerModCorrente.setDispProjModel(demandaMaxProxPeriodo * plPerModCorrente.getCoberturaModel()/100);
//			}
			

			//calcula produção a partir da dispProj e o max entre pedidos ou vendas do periodo
			plPerModCorrente.setProducaoModel(plPerModCorrente.getDispProjModel() - 
													dispProjAnterior +
													Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel()));
			
			
			//se a producao for negativa, atribui 0 pois não pode produzir qtd negativa.
			if(plPerModCorrente.getProducaoModel() < 0){
				plPerModCorrente.setProducaoModel(0.0);
				
				novoDispProjModel= plPerModCorrente.getProducaoModel() + dispProjAnterior - 
				Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
				
				plPerModCorrente.setDispProjModel(novoDispProjModel);
			}			
			
		}//if livre
		
		//se for fixo:
		//aproveita a producao ja estabelecida anteriormente e calcula a dispProj em funcao disso.
		else{ 
			novoDispProjModel = plPerModCorrente.getProducaoModel() + dispProjAnterior - 
															Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
			
			plPerModCorrente.setDispProjModel(novoDispProjModel);
		}

//		System.out.println("PERIODO DEFASADO (ANTES) = " + periodoDefasado);
//		System.out.println("INICIO PMP (ANTES) = " + inicioPMPAntesInicioHP);
		
		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());
		
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);
		
		
//		System.out.println("INICIO PMP (DEPOIS) = " + inicioPMPAntesInicioHP);
//		System.out.println("PERIODO DEFASADO (DEPOIS) = " + periodoDefasado);
		
		//so devera fazer isso se for flag livre!
		if(plPerModCorrente.getProducaoModel() > 0){
			
			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP  && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);

				novoDispProjModel = plPerModCorrente.getProducaoModel() + dispProjAnterior - 
																Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
				
				plPerModCorrente.setDispProjModel(novoDispProjModel);
				
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
			
		}
		
		//Calcula producao em lotes e prodDiariaLoteModel em funcao da producao em pcs ja calculada.
		if((plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() > 0)
				&& (planoModelo.getModelo().getTamLote() > 0)){
			

			//Versao antiga que nao utiliza aproximacao
			//int novaProdDiariaLoteModel = (int) (plPerModCorrente.getProducaoModel() / (planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz()));
			
			int novaProdDiariaLoteModel= Numeric.resultadoDivisaoInteira(plPerModCorrente.getProducaoModel(),(planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz()));
			plPerModCorrente.setProdDiariaLoteModel(new Double(novaProdDiariaLoteModel));
	


			//Versao antiga que nao utiliza aproximacao
			//int novaProdLoteModel = (int) (plPerModCorrente.getProdDiariaLoteModel() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz());
			
			//(resultadoMultiplicacaoInteira)Usada para evitar prodLoteModel fracionario.
			int novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente.getPerioPM().getNumDiasUteisMatriz());
			plPerModCorrente.setProdLoteModel(new Double(novaProdLoteModel));
					
			
		}
		else{
			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
		}		
		
		//Recalcula producao em pecas considerando a producao diaria em lotes,
		//devido ao arredondamento ocorrido no calculo da prodDiariaLoteModel
		if(plPerModCorrente.getProdLoteModel() < 0){
			plPerModCorrente.setProducaoModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProdDiariaLoteModel(0.0);			
		}
		else{
			Double novaProducaoModel = plPerModCorrente.getProdDiariaLoteModel() *
											planoModelo.getModelo().getTamLote() *
											plPerModCorrente.getPerioPM().getNumDiasUteisMatriz();
			
			plPerModCorrente.setProducaoModel(novaProducaoModel);
		}
		

		
		
		//recalcula dispProjModel com a nova producaoModel
		novoDispProjModel = plPerModCorrente.getProducaoModel() + dispProjAnterior - 
														Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
		plPerModCorrente.setDispProjModel(novoDispProjModel);
		
		
		//recalcula cobertura do modelo a partir da nova disponibilidade
		//quando a venda eh igual a zero a cobertura eh feita igual a do modelo
		if(demandaMaxProxPeriodo != 0){
			Double novaCoberturaModel = (plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo) * 100;
			plPerModCorrente.setCoberturaModel(novaCoberturaModel);
		}
		else
		{
			plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());	
		}
		
								
		return (dispProjAnterior = plPerModCorrente.getDispProjModel());
	}
	/**
	 * método criado tendo como base a versão feita no simulador 2 da tese.  main.m   (procedure calculaProducao)
	 * 
	 *  
	 * 
	 * 
	 * IMPORTANTE: Não pega demanda do demodper. Usa informação já inicializada no Plpermod
	 * 
	 * 
	 * 
	 * Usado no momento da inclusao do plano empregando um mecanismo de inferência fuzzy
	 * @throws AplicacaoException 
	 * 
	 * 
	 * 
	 * */
	@Transacional
	public void calculaFuzzyDispProjProducaoECoberturaModel(PlanoModelo planoModelo, PlPerMod plPerModCorrente, 
			                                                Map<String, double[]> variaveisDeEstoqueDefasadas, HP hpBD,
			                                    			MotorInferencia motorInferencia) throws AplicacaoException{
		
		//inicializa o valor do campo periodoPMPInicioPMP com 0
		//ou seja comeca dizendo que a producao deveria ter 
		//sido realizada no periodo anterior ao inicio do hp.
		plPerModCorrente.setPeriodoPMInicioPMP(0);
		
		boolean inicioPMPAntesInicioHP = false;
		Integer periodoDefasado = 0;
		
		//Demanda maxima do periodo corrente.
		double demandaMaxT = Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());		

		//Obtem demanda maxima no proximo periodo.
		//sem acessar o DeModPer. Acessa os valores do proprio PlPerMod
		//que foram inicializados no momento da inclusao do plano.
		double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente, hpBD);		
		
		
		//Obtem valores de varEstqPerc e DispProjModels dos periodos anteriores.
		double [] varEstqPercs =  variaveisDeEstoqueDefasadas.get("varEstqPercs");
		double [] dispProjModels =  variaveisDeEstoqueDefasadas.get("dispProjModels");
		
		//se flag producao for "livre"
		//se for true, é livre
		//entao calcula nova producao.
		// se não for livre aproveita a producaoModel do plpermodcorrente
		if(plPerModCorrente.getFlagProducaoModel()){
			
			//calcula producao model do periodo corrente em funcao da demanda maxima
			//no periodo corrente e varEstqPercs(t-1. A varEstqPercs foi calculada na chamada anterior em funcao dispProjModel do
			//periodo anterior e do estoque de seguranca desejado).
			
			String nomeArquivoModelagem = planoModelo.getCadPlan().getModelagemFuzzy().getNomeArquivo();
			
			
			double [] variaveisDeEntrada = {demandaMaxT, varEstqPercs[plPerModCorrente.getPerioPM().getPeriodoPM()-1]};
			double [] variaveisDeSaida = null;
			try {
				variaveisDeSaida = motorInferencia.executaMotorDeInferencia(variaveisDeEntrada,nomeArquivoModelagem);
			} catch (MotorInferenciaException e) {
				throw new AplicacaoException(e.getMessage());
			}
			//primeira posicao do vetor da VariaveisDeSaida = producaoModel
			double fuzzyProducaoModel = variaveisDeSaida[0];
			plPerModCorrente.setProducaoModel(fuzzyProducaoModel);
//			System.out.println
//			("valor fuzzy da producaoModel: " + fuzzyProducaoModel + "  no periodo: " + plPerModCorrente.getPerioPM().getPeriodoPM());
			
		}
		
		
		
		//Calcula dispProj
		double novaDispProjModel =  dispProjModels[plPerModCorrente.getPerioPM().getPeriodoPM()-1] + 
		                                            plPerModCorrente.getProducaoModel() - demandaMaxT;
		plPerModCorrente.setDispProjModel(novaDispProjModel);

		

		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());

		//recupera informacoes do map, que representa os retornos da funcao: verificaPeriodoInicioPMP()
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);

		
		if(plPerModCorrente.getProducaoModel() > 0 ){
			
			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);

				novaDispProjModel = dispProjModels[plPerModCorrente.getPerioPM().getPeriodoPM()-1] + 
                								plPerModCorrente.getProducaoModel() - demandaMaxT;								
				plPerModCorrente.setDispProjModel(novaDispProjModel);
				
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
						
		}
		

		//Calcula producao em lotes e prodDiariaLoteModel em funcao da producao em pcs ja calculada.
		if((plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() > 0)
				&& (planoModelo.getModelo().getTamLote() > 0)){
			

			//calcula prodDiariaLoteModel
			int novaProdDiariaLoteModel= Numeric.resultadoDivisaoInteira(plPerModCorrente.getProducaoModel(),
					                                  (planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz()));
			plPerModCorrente.setProdDiariaLoteModel(new Double(novaProdDiariaLoteModel));
			

			int novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente.getPerioPM().getNumDiasUteisMatriz());
			plPerModCorrente.setProdLoteModel(new Double(novaProdLoteModel));
					
			
		}
		else{
			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
		}		
		
		

		//Recalcula producao em pecas considerando a producao diaria em lotes,
		//devido ao arredondamento ocorrido no calculo da prodDiariaLoteModel
		if(plPerModCorrente.getProdLoteModel() < 0){
			plPerModCorrente.setProducaoModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProdDiariaLoteModel(0.0);			
		}
		else{			
			//recalcula producao em funcao do arredondamento em prodDiariaLoteModel
			double novaProducaoModel = 
				plPerModCorrente.getProdDiariaLoteModel() * planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz();
			plPerModCorrente.setProducaoModel(novaProducaoModel);
		}
		
		
		
		//recalcula dispProj em funcao do arredondamento em prodDiariaLote, eh necessario isso?!
		novaDispProjModel =  dispProjModels[plPerModCorrente.getPerioPM().getPeriodoPM()-1] + plPerModCorrente.getProducaoModel() - demandaMaxT;
		plPerModCorrente.setDispProjModel(novaDispProjModel);
		
		//Calcula cobertura resultante do produto no periodo
		//se for o ultimo periodo usa a demanda do proprio periodo como estimativa da demanda do proximo periodo.
		if (plPerModCorrente.getPerioPM().getPeriodoPM() == hpBD.getPerioPMFinalPMP().getPeriodoPM()){
			//caso demandaMaxT seja zero, utiliza a cobertura do modelo ao inves de calcular a cobertura do plPerMod.
			//para evitar divisao por zero
			if(demandaMaxT == 0){
				plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());
			}
			else{
				plPerModCorrente.setCoberturaModel(
						(plPerModCorrente.getDispProjModel() / demandaMaxT)*100
						);
			}
			
		}
		//caso não seja o ultimo periodo do HP
		else{
			//caso demanda maxima do proximo periodo seja zero, utiliza a cobertura do modelo como ao inves de calcular a cobertura do plPerMod.
			//para evitar divisao por zero
			if(demandaMaxProxPeriodo == 0){
				plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());
			}
			else{
				plPerModCorrente.setCoberturaModel(
						(plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo)*100
						);
			}
			
		}
		
		//Calcula o estoque desejado ao fim do periodo em funcao da
		//cobertura do produto e da demanda max no periodo t+1 e a variacao
		//percentual dispprojetada(t) em relacao ao estoque desejado ES(t)
		double ESDesejado;
		//caso seja o ultimo periodo do HP.
		if (plPerModCorrente.getPerioPM().getPeriodoPM() == hpBD.getPerioPMFinalPMP().getPeriodoPM()){
			
			ESDesejado = (demandaMaxT * planoModelo.getModelo().getCobertura())/100;
			
			
		}
		else{
			ESDesejado = (demandaMaxProxPeriodo * planoModelo.getModelo().getCobertura())/100;						
		}

		if( ESDesejado == 0){
			plPerModCorrente.setVarEstqPerc(100.00);
		}

		plPerModCorrente.setVarEstqPerc(
				((plPerModCorrente.getDispProjModel() - ESDesejado)/ESDesejado)*100
				);
		//como a variacao do estoque percentual, varia de -100 a 400 no simulador fuzzy, valores que
		//ultrapassam isso sao setados para os limites.
		//caso contrario daria erro no simulador.
		//ATENCAO: NAO ESTAMOS RECALCULANDO O DispProj PARA ESSES VALORES FORA DA RANGE
		//E PORTANTO O DispProj MOSTRADO AINDA É O ORIGINAL.
		//COMO A VARIACAO DE ESTOQUE FOI ALTERADA A PRODUCAO CALCULADA NAO TENTARA COMPENSAR
		//A DISPONIBILIDADE TAO AFASTADA DO DESEJADO.
		if(plPerModCorrente.getVarEstqPerc() > 400){
			plPerModCorrente.setVarEstqPerc(400.00);
		}
		else if(plPerModCorrente.getVarEstqPerc() < -100){
			
			plPerModCorrente.setVarEstqPerc(-100.00);
		}
		
		
		//Seta as informacoes de varEstqPerc e dispProjModel no vetor na posicao correspondente a esse periodo:
		varEstqPercs[plPerModCorrente.getPerioPM().getPeriodoPM()] = plPerModCorrente.getVarEstqPerc();
		dispProjModels[plPerModCorrente.getPerioPM().getPeriodoPM()] = plPerModCorrente.getDispProjModel();
		
		
		
		
		return;
	}
	
	@Transacional
	public double calculaEstqSegDesejado(PlPerMod plPerModCorrente, HP hpBD){
		double demandaMaxProPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente, hpBD);
		
		//calculo do estoque de seguranca desejado em funcao da cobertura do modelo desejado.
		double estqSegDesejado = demandaMaxProPeriodo * plPerModCorrente.getPlanoModelo().getModelo().getCobertura() / 100;
		
		plPerModCorrente.setEstqSegDesejado(estqSegDesejado);
		
		return estqSegDesejado;
	}

	@Transacional
	public double calculaVarEstqPerc(PlPerMod plPerModCorrente){
		

		//calculo da difPercentual entre estoque desejado e estoque projetado
		double varEstqPerc = (plPerModCorrente.getDispProjModel() - plPerModCorrente.getEstqSegDesejado())
									/ plPerModCorrente.getEstqSegDesejado() * 100;
		
		plPerModCorrente.setVarEstqPerc(varEstqPerc);
		
		return varEstqPerc;
	}

	@Transacional
	public double calculaVarProdDiaPerc(PlPerMod plPerModCorrente){
		

		//calculo da variacao percentual entre proddiaria em lotes de cada periodo e a proddiaria media do plano.
		//para avaliar o plano quanto ao aspecto do nivelamento da producao.
		double varProdDiaPerc = (plPerModCorrente.getProdDiariaLoteModel() 
									- plPerModCorrente.getPlanoModelo().getProdDiariaMediaPlanoModelo())
									/ plPerModCorrente.getPlanoModelo().getProdDiariaMediaPlanoModelo() * 100;
		
		plPerModCorrente.setVarProdDiaPerc(varProdDiaPerc);
		
		return varProdDiaPerc;
	}
	
	

	
	@Transacional
	public double calculaEscorePlanPerMod(PlPerMod plPerModCorrente,String nomeArquivoModelagem, MotorInferencia motorInferencia) throws AplicacaoException{
		

		double [] variaveisDeEntrada = {plPerModCorrente.getVarEstqPerc()};
		double [] variaveisDeSaida = null;
		
		
		try{
			variaveisDeSaida = motorInferencia.executaMotorDeInferencia(variaveisDeEntrada,nomeArquivoModelagem);
				
		}catch (MotorInferenciaException e) {
			throw new AplicacaoException(e.getMessage());
		}

		//primeira posicao do vetor da VariaveisDeSaida = escorePlanPerMod
		//executa o motorInferencia para calcular o escore a partir da variacao VarEstqPerc
		double escorePlanPerMod = variaveisDeSaida[0];
		
//		double escorePlanPerMod = 50.00;
		plPerModCorrente.setEscorePlanPerMod(escorePlanPerMod);
		
		return escorePlanPerMod;

	}
	
	/**
	* Obtem demanda maxima no proximo periodo. Considera o maximo entre a venda e o pedido
	* do proximo periodo.
	* No caso de estar no ultimo periodo, usa como estimativa para a venda/pedido do proximo
	* periodo a venda/pedido do proprio periodo.
	* 
	* sem acessar o DeModPer. Acessa os valores do proprio PlPerMod
	* que foram inicializados no momento da inclusao do plano.
	* @author felipe.arruda
	*/
	@Transacional
	public Double obtemDemandaMaxProxPeriodo(PlPerMod plPerModCorrente, HP hpBD) {
		
		Double demandaMaxProxPeriodo = 0.0;
		
		
		//Verifica se o periodo do plPerModCorrente é anterior ao periodo final HP
		//calcula a demandaMaxProxPeriodo fazendo o maximo entre venda e pedido do proximo periodo.
		if(plPerModCorrente.getPerioPM().getPeriodoPM() < hpBD.getPerioPMFinalPMP().getPeriodoPM()){
			
			PerioPM perioPMProx=null;
			try {
				perioPMProx = perioPMDAO.recuperaPerioPMPorPeriodoPM(plPerModCorrente.getPerioPM().getPeriodoPM()+1);
			} catch (ObjetoNaoEncontradoException e) {
			}
			PlPerMod plPerModProxPeriodo=null;
			try {
				plPerModProxPeriodo = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
												(plPerModCorrente.getPlanoModelo(), perioPMProx);
				demandaMaxProxPeriodo = Math.max(plPerModProxPeriodo.getVendasModel(),plPerModProxPeriodo.getPedidosModel());
			} catch (ObjetoNaoEncontradoException e) {
			}
			
			
		}
		//se estiver fora do HP
		//usa como estimativa para a venda/pedido do proximo periodo a venda/pedido do proprio periodo. 
		else{
			demandaMaxProxPeriodo = Math.max(plPerModCorrente.getVendasModel(),plPerModCorrente.getPedidosModel());
		}
			
		return demandaMaxProxPeriodo;		
	}
	
	/**
	* Obtem demanda maxima no proximo periodo. Considera o maximo entre a venda e o pedido
	* do proximo periodo.
	* No caso de estar no ultimo periodo, usa como estimativa para a venda/pedido do proximo
	* periodo a venda/pedido do proprio periodo.
	* 
	* Passando o parametro isPeriodoZero como True, o metodo considera que o plPerModCorrente é o proximo
	* E calcula as informacoes baseadas nesse plPerMod.
	* 
	* sem acessar o DeModPer. Acessa os valores do proprio PlPerMod
	* que foram inicializados no momento da inclusao do plano.
	* @author felipe.arruda
	*/
	@Transacional
	public Double obtemDemandaMaxProxPeriodo(PlPerMod plPerModCorrente, HP hpBD, boolean isPeriodoZero) {
		
		Double demandaMaxProxPeriodo = 0.0;
		
		//se for periodoZero, considera que o plPerModCorrente ja é o do proximo periodo.
		if(isPeriodoZero){
			
			demandaMaxProxPeriodo = Math.max(plPerModCorrente.getVendasModel(),plPerModCorrente.getPedidosModel());
			
		}else{
			//Verifica se o periodo do plPerModCorrente é anterior ao periodo final HP
			//calcula a demandaMaxProxPeriodo fazendo o maximo entre venda e pedido do proximo periodo.
			if(plPerModCorrente.getPerioPM().getPeriodoPM() < hpBD.getPerioPMFinalPMP().getPeriodoPM()){
				
				PerioPM perioPMProx=null;
				try {
					perioPMProx = perioPMDAO.recuperaPerioPMPorPeriodoPM(plPerModCorrente.getPerioPM().getPeriodoPM()+1);
				} catch (ObjetoNaoEncontradoException e) {
				}
				PlPerMod plPerModProxPeriodo=null;
				try {
					plPerModProxPeriodo = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
													(plPerModCorrente.getPlanoModelo(), perioPMProx);
					demandaMaxProxPeriodo = Math.max(plPerModProxPeriodo.getVendasModel(),plPerModProxPeriodo.getPedidosModel());
				} catch (ObjetoNaoEncontradoException e) {
				}
				
				
			}
			//se estiver fora do HP
			//usa como estimativa para a venda/pedido do proximo periodo a venda/pedido do proprio periodo. 
			else{
				demandaMaxProxPeriodo = Math.max(plPerModCorrente.getVendasModel(),plPerModCorrente.getPedidosModel());
			}			
		}
		
			
		return demandaMaxProxPeriodo;		
	}

	/**
	 * Metodo chamado no altera de PlPerMod, no momento em que
	 * é editada a cobertura ou vendas ou pedidos.
	 * Recalcula para todos os periodos a partir da cobertura
	 * informada todos os outros parametros(dispProj, Producao, PeriodoInicioPMP)
	 * No caso de edição da cobertura chama um metodo que vai permitir alterar a cobertura pois
	 * o metodo usado para edição de vendas e pedidos vai sempre tentar usar a cobertura do cadastro de modelo.
	 * 
	 * @author felipe.arruda
	 * @param plPerModAlterado
	 */
	@Transacional
	public void recalculaParametrosBaseadoCobertura(PlPerMod plPerModAlterado, boolean edicaoCobertura) {
		//so precisa ser recalculado a partir do periodo em que foi feita
		//a alteracao, estou fazendo em todos os periodos apenas para
		//o algoritimo ficar mais simples.
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		List<PlPerMod>listaDePlPerMods = plPerModDAO.recuperaListaDePlPerModPorPlanoModelo(plPerModAlterado.getPlanoModelo());
		Collections.sort(listaDePlPerMods, new PlPerModComparatorPorPerioPM());
		//calcula o estoque inicial relativo ao primeiro periodo levando em consideração
		// estoque em falta e recebimento pendente
		Double dispProjAnterior = plPerModAlterado.getPlanoModelo().getModelo().getEstqInicModel()
												+ plPerModAlterado.getPlanoModelo().getModelo().getRecebimentoPendente()
												- plPerModAlterado.getPlanoModelo().getModelo().getEstqEmFalta();

		for(PlPerMod plPerMod : listaDePlPerMods){
			//se estiver editando a cobertura
			if(edicaoCobertura){
				//usando a mesma funcao que a inclusao chama, ou seja usa a demanda do proprio plpermod.
				dispProjAnterior = calculaDispProjProducaoPartindoCoberturaModel(plPerMod.getPlanoModelo(), plPerMod,
																					plPerModAlterado,dispProjAnterior, hpBD);
				
			}
			//caso esteja editando pedido ou vendas
			else{
				//usando a mesma funcao que a inclusao chama, ou seja usa a demanda do proprio plpermod.
				dispProjAnterior = calculaDispProjProducaoECoberturaModel(plPerMod.getPlanoModelo(), plPerMod,dispProjAnterior, hpBD);				
			}
				
			//dispProjAnterior = calcDispProjProdECoberturaModel(dispProjAnterior,plPerMod, hpBD);
			
			//salva o plpermod atual para que ele fique com todas as informacoes gravadas em banco
			this.altera(plPerMod);
			
		}
		
		
		
	}
	
	/**
	 * Recalcula para todos os periodos, a partir da DisProjModel
	 * informada todos os outros parametros (producao, cobertura, periodoInicioPMP)
	 * 
	 * Utilizada no metodo altera de plPerModAppService, no momento em que edita
	 * a dispProjModel
	 * VERIFICAR RECEBIMENTOPENDENTE
	 * 
	 * @author felipe.arruda
	 * @param plPerModCorrente
	 */
	@Transacional
	public void recalculaParametrosBaseadoDispProj(PlPerMod plPerModCorrente) {
		//so precisa recalcular a partir do periodo em que foi feito a alteracao
		//vai para o periodo alterado do modelo selecionado e percorre todos os
		//demais periodos do HP do PMP.
		
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		
		
		//Pegando a lista de plpermod apartir do proximo periodo depois do plpermodCorrenteS
		List<PlPerMod>listaDePlPerMods = plPerModDAO.recuperaListaDePlPerModPorPlanoModeloEPerioPMApartirDePerioPM(plPerModCorrente.getPlanoModelo(),plPerModCorrente.getPerioPM().getPeriodoPM()+1);
		Collections.sort(listaDePlPerMods, new PlPerModComparatorPorPerioPM());
		
		Double dispProjAnterior = null;
		
		//se a alteracao foi feita no primeiro periodo, entao usa o EstqInicModel e recebimentopendente, caso contrario,
		//pega a dispProj do periodo anterior.
		if(plPerModCorrente.getPerioPM().getPeriodoPM() == 1){
			dispProjAnterior = plPerModCorrente.getPlanoModelo().getModelo().getEstqInicModel() 
								+ plPerModCorrente.getPlanoModelo().getModelo().getRecebimentoPendente()
								- plPerModCorrente.getPlanoModelo().getModelo().getEstqEmFalta();
			
		}
		else{
			//obtem disProj do periodo anterior
			
			PerioPM perioPMPAnt=null;
			//pega o perioPM anterior ao do plpermodcorrente
			try {
				perioPMPAnt = perioPMDAO.recuperaPerioPMPorPeriodoPM(plPerModCorrente.getPerioPM().getPeriodoPM()-1);
			} catch (ObjetoNaoEncontradoException e) {
				//nao entra aqui pois a principio se for o primeiro nem entra nesse else
			}

			//obtem disProj do periodo anterior
			PlPerMod plPerModPeriodoAnterior=null;
			try {
				plPerModPeriodoAnterior = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
												(plPerModCorrente.getPlanoModelo(), perioPMPAnt);
				dispProjAnterior = plPerModPeriodoAnterior.getDispProjModel();
				
			} catch (ObjetoNaoEncontradoException e) {
				//a principio nao deve acontecer essa excecao
			}
			
			
			
		}
		
		
		//Recalcula apenas para o plPerMod que esta sendo alterado.
		recalculaProdECobModelPartindoDispProj(plPerModCorrente.getPlanoModelo(),plPerModCorrente, dispProjAnterior,hpBD);
		this.altera(plPerModCorrente);
		
		for (PlPerMod plPerMod : listaDePlPerMods){
			
			//nesse caso parte da cobertura para recalcular os demais parametros
			dispProjAnterior = calculaDispProjProducaoECoberturaModel(plPerMod.getPlanoModelo(), plPerMod,dispProjAnterior, hpBD);
			
			//salva o plpermod atual para que ele fique com todas as informacoes gravadas em banco
			this.altera(plPerMod);
		}
		
	}

	/**
	 * Recalcula para um dado periodo em que houve alteracao na dispProj
	 * todos os outros parametros,(producao,cobertura,periodoInicPMP).
	 * A partir da producao model recalculada devido a edicao da dispProj,
	 * recalcula os demais campos, e portanto desconsidera o flag producao = Fixo,
	 * pois foi iniciativa do usuario a edicao do valor. 
	 * 
	 * Retorna o dispProjAnterior e altera os valores do plPerModCorrente
	 * @author felipe.arruda
	 * @param planoModelo
	 * @param plPerModCorrente
	 * @param dispProjAnterior
	 * @param hpBD
	 * @return
	 */
	@Transacional
	public double recalculaProdECobModelPartindoDispProj(PlanoModelo planoModelo, PlPerMod plPerModCorrente,Double dispProjAnterior, HP hpBD) {

		plPerModCorrente.setPeriodoPMInicioPMP(0);

		//determina a demanda maxima do proximo periodo para o calculo da cobertura.
		Double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente,hpBD);

		//variavel temporaria usada para facilitar a clareza da formula
		Double novoDispProjModel = 0.0;
		
		Boolean inicioPMPAntesInicioHP = false;
		
		Integer periodoDefasado = 0;
		
		
		//calcula nova prodModel a partir da nova DispProj
		//se for negativa torna = 0
		if(plPerModCorrente.getDispProjModel() < 0){
			plPerModCorrente.setDispProjModel(0.0);
		}
		else{
			plPerModCorrente.setProducaoModel(plPerModCorrente.getDispProjModel() -
												dispProjAnterior +
									Math.max(plPerModCorrente.getVendasModel(),plPerModCorrente.getPedidosModel()));
			
			
		}
		//Calcula nova prodLotModel a partir da ProducaoModel em pecas
		//calcula producao em lotes e diaria
		if(plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() > 0 
				&& planoModelo.getModelo().getTamLote() > 0){
			
			double novaProdDiariaLoteModel = Numeric.resultadoDivisaoInteira(plPerModCorrente.getProducaoModel(),
					(planoModelo.getModelo().getTamLote() * plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() ) );
			
			plPerModCorrente.setProdDiariaLoteModel(novaProdDiariaLoteModel);
			

			 double novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(plPerModCorrente.getProdDiariaLoteModel(),
					plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() );
			
			plPerModCorrente.setProdLoteModel(novaProdLoteModel);
		}
		else{
			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProducaoModel(0.0);
		}
		
		//recalcula producao em pecas, considerando a nova producao em lotes e diaria
		//devido ao arredondamento ocorrido no calculo da prodLotModel
		if(plPerModCorrente.getProdLoteModel() < 0){
			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProducaoModel(0.0);
			
		}
		else{

			 double novaProducaoModel = plPerModCorrente.getProdDiariaLoteModel() *
			 							planoModelo.getModelo().getTamLote() *
			 							plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() ;
			
			plPerModCorrente.setProducaoModel(novaProducaoModel);
		}
		

		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());
		
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);
		
		
//		System.out.println("INICIO PMP (DEPOIS) = " + inicioPMPAntesInicioHP);
//		System.out.println("PERIODO DEFASADO (DEPOIS) = " + periodoDefasado);
		
		if(plPerModCorrente.getProducaoModel() > 0){			
						
			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP  && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
			
			
		}
		
		//recalcula disProj com a nova producaoModel
		novoDispProjModel = plPerModCorrente.getProducaoModel() +
							dispProjAnterior - 
							Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
		
		plPerModCorrente.setDispProjModel(novoDispProjModel);
		
		//Recalcula cobertura do modelo a partir da nova disponibilidade
		if(demandaMaxProxPeriodo != 0){
			double novaCobModel = (plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo) * 100;
			
			plPerModCorrente.setCoberturaModel(novaCobModel);
		
		
		}
		else{

			plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());
		
		}
		dispProjAnterior = plPerModCorrente.getDispProjModel();
		return dispProjAnterior;
	}
	

	
	/**
	 * É usado pelo plPerModService.altera no momento da edicao da producao em lote.
	 * 
	 * Recalcula para todos os peridos a partir da producaoEmLoteModel
	 * informada todos os outros parametros(DispProj, cobertura, periodoInicPMP)
	 *  
	 * @author felipe.arruda
	 * @param plPerModCorrente
	 */
	@Transacional
	public void recalculaParametrosBaseadoProducao(PlPerMod plPerModCorrente) {
		//so precisa recalcular a partir do periodo em que foi feito alteracao.
		//vai para o proximo periodo depois do alterado e percorre
		//todos os demais periodos do HP do PMP
		
		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		
		//Pegando a lista de plpermod a partir do proximo periodo depois do plpermodCorrenteS
		List<PlPerMod>listaDePlPerMods = plPerModDAO.recuperaListaDePlPerModPorPlanoModeloEPerioPMApartirDePerioPM
													(plPerModCorrente.getPlanoModelo(),
													plPerModCorrente.getPerioPM().getPeriodoPM()+1);
		Collections.sort(listaDePlPerMods, new PlPerModComparatorPorPerioPM());
		
		Double dispProjAnterior = null;
		
		//se a edicao for no primeiro periodo do HP
		//obtem a dispProjAnterior a partir do EstqInicModel
		if(plPerModCorrente.getPerioPM().getPeriodoPM() == 1){
			dispProjAnterior = plPerModCorrente.getPlanoModelo().getModelo().getEstqInicModel() 
								+ plPerModCorrente.getPlanoModelo().getModelo().getRecebimentoPendente()
								- plPerModCorrente.getPlanoModelo().getModelo().getEstqEmFalta();
			
		}
		else{

			PerioPM perioPMPAnt=null;
			//pega o perioPM anterior ao do plpermodcorrente
			try {
				perioPMPAnt = perioPMDAO.recuperaPerioPMPorPeriodoPM(plPerModCorrente.getPerioPM().getPeriodoPM()-1);
			} catch (ObjetoNaoEncontradoException e) {
				//nao entra aqui pois a principio se for o primeiro nem entra nesse else
			}
			
			PlPerMod plPerModPeriodoAnterior=null;
			try {
				plPerModPeriodoAnterior = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
												(plPerModCorrente.getPlanoModelo(), perioPMPAnt);
				dispProjAnterior = plPerModPeriodoAnterior.getDispProjModel();
				
			} catch (ObjetoNaoEncontradoException e) {
				//a principio nao deve acontecer essa excecao
			}
			
			
			
		}
		
		
		dispProjAnterior = recalculaDispProjECoberturaModel(plPerModCorrente.getPlanoModelo(),
															plPerModCorrente,dispProjAnterior , hpBD);
		
		this.altera(plPerModCorrente);
		
		//Lista que comeca no proximo periodo depois do periodo do PlPerModCorrente
		for (PlPerMod plPerMod : listaDePlPerMods){
			
			//nesse caso parte da cobertura para recalcular os demais parametros
			dispProjAnterior = calculaDispProjProducaoECoberturaModel(plPerMod.getPlanoModelo(),
																	plPerMod,dispProjAnterior, hpBD);
			
			//salva o plpermod atual para que ele fique com todas as informacoes gravadas em banco
			this.altera(plPerMod);
		}
		
	}

	/**
	 * Recalcula para um dado periodo em que houve alteracao na producaoEmLote,
	 * todos os outros parametros (DispProj, Cobertura, PeriodoInicioPMP), a partir da producaoLoteModel informada.
	 * Parte da producao e calcula os demais campos e portanto desconsidera o flagProducao = fixo,
	 * pois foi iniciativa do usuario a edicao do valor.  
	 * 
	 * Metodo usado em recalculaParametrosBaseadoProducao
	 * @author felipe.arruda
	 */
	public Double recalculaDispProjECoberturaModel(PlanoModelo planoModelo,
			PlPerMod plPerModCorrente, Double dispProjAnterior, HP hpBD) {

		plPerModCorrente.setPeriodoPMInicioPMP(0);
		//Determina demanda maxima do proximo periodo para calcular a cobertura.
		Double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente,hpBD);

		//variavel temporaria usada para facilitar a clareza da formula
		Double novoDispProjModel = 0.0;
		
		Boolean inicioPMPAntesInicioHP = false;
		//Inicializa periodo defasado com 0, significando que a producao 
		//comeca no periodo anterior ao HP
		Integer periodoDefasado = 0;

		//Calcula nova producaoModel em pecas, a partir da ProdLoteModel editada
		//Para evitar divisao por 0, tem a verificacao do numeroDiasUteisMatriz ser == 0.
		if(plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() == 0){

			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProducaoModel(0.0);
		}
		else {
			double novaProdDiariaLoteModel = Numeric.resultadoDivisaoInteira(
					plPerModCorrente.getProdLoteModel(), plPerModCorrente
							.getPerioPM().getNumDiasUteisMatriz());
			plPerModCorrente.setProdDiariaLoteModel(novaProdDiariaLoteModel);

			double novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(
					plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente
							.getPerioPM().getNumDiasUteisMatriz());

			plPerModCorrente.setProdLoteModel(novaProdLoteModel);

			//Recalcula producao model depois da edicao e recalculo de prodLoteModel
			plPerModCorrente.setProducaoModel(plPerModCorrente
					.getProdLoteModel()
					* planoModelo.getModelo().getTamLote());

		}
		
		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());
		
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);
		
		
//		System.out.println("INICIO PMP (DEPOIS) = " + inicioPMPAntesInicioHP);
//		System.out.println("PERIODO DEFASADO (DEPOIS) = " + periodoDefasado);
		
		if(plPerModCorrente.getProducaoModel() > 0){
			

			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP  && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
			
		}
		
		//Recalcula dispProjModel com a nova producaoModel.
		novoDispProjModel = plPerModCorrente.getProducaoModel() +
							dispProjAnterior - 
							Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
		
		plPerModCorrente.setDispProjModel(novoDispProjModel);

		//Recalcula Cobertura do Modelo a partir da nova disponibilidade
		if(demandaMaxProxPeriodo != 0){
			double novaCobModel = (plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo) * 100;
			
			plPerModCorrente.setCoberturaModel(novaCobModel);
		
		
		}
		else{

			plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());
		
		}
		
		dispProjAnterior = plPerModCorrente.getDispProjModel();
		return dispProjAnterior;
	}


	/**
	 * Recalcula para todos os periodos, a partir da ProducaoModel em peças informada todos os 
	 * outros parametros (DispProj, Cobertura, PeriodoInicioPMP)
	 * @param plPerModCorrente
	 * @author dayse.arruda
	 */
	@Transacional
	public void recalculaParametrosBaseadoProdEmPecas(PlPerMod plPerModCorrente) {

		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		//Só precisa recalcular a partir do periodo em que foi feita a alteração
		//vai para o periodo alterado e percorre os demais periodos do HP do PMP
		
		//Pegando a lista de plpermod a partir do proximo periodo referente ao plpermodCorrenteS
		List<PlPerMod>listaDePlPerMods = plPerModDAO.recuperaListaDePlPerModPorPlanoModeloEPerioPMApartirDePerioPM(plPerModCorrente.getPlanoModelo(),plPerModCorrente.getPerioPM().getPeriodoPM()+1);
		Collections.sort(listaDePlPerMods, new PlPerModComparatorPorPerioPM());
		
		Double dispProjAnterior = null;
		//se a edicao for no primeiro periodo do HP
		//obtem a dispProjAnterior a partir do EstqInicModel
		if(plPerModCorrente.getPerioPM().getPeriodoPM() == 1){
			dispProjAnterior = plPerModCorrente.getPlanoModelo().getModelo().getEstqInicModel()  
								+ plPerModCorrente.getPlanoModelo().getModelo().getRecebimentoPendente()
								- plPerModCorrente.getPlanoModelo().getModelo().getEstqEmFalta();
			
		}
		else{
			//obtem dispProj do periodo anterior
			PerioPM perioPMPAnt=null;
			//pega o perioPM anterior ao do plpermodcorrente
			try {
				perioPMPAnt = perioPMDAO.recuperaPerioPMPorPeriodoPM(plPerModCorrente.getPerioPM().getPeriodoPM()-1);
			} catch (ObjetoNaoEncontradoException e) {
				//nao entra aqui pois a principio se for o primeiro nem entra nesse else
			}
			
			PlPerMod plPerModPeriodoAnterior=null;
			try {
				plPerModPeriodoAnterior = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
												(plPerModCorrente.getPlanoModelo(), perioPMPAnt);
				dispProjAnterior = plPerModPeriodoAnterior.getDispProjModel();
				
			} catch (ObjetoNaoEncontradoException e) {
				//a principio nao deve acontecer essa excecao
			}
			
			
			
		}
		
		//recalcula demais parametros para o periodo em que foi alterada a producao
		dispProjAnterior = recalculaDispProjECobModelDaProdModel(plPerModCorrente.getPlanoModelo(),
															plPerModCorrente,dispProjAnterior , hpBD);
		//salva recalculo no periodo alterado em arquivo
		this.altera(plPerModCorrente);
		
		//Lista que comeca no proximo periodo depois do periodo do PlPerModCorrente
		//recalcula demais parametros para os periodos restantes do HP e salva em arquivo
		for (PlPerMod plPerMod : listaDePlPerMods){
			
			//nesse caso parte da cobertura para recalcular os demais parametros
			dispProjAnterior = calculaDispProjProducaoECoberturaModel(plPerMod.getPlanoModelo(),
																	plPerMod,dispProjAnterior, hpBD);
			
			//salva o plpermod atual para que ele fique com todas as informacoes gravadas em banco
			this.altera(plPerMod);
		}
		
	}

	/**
	 * Recalcula para um dado periodo em que houve alteracao na producao em pecas,
	 * todos os outros parametros (DispProj, Cobertura, PeriodoInicioOMP), a partir da producaoModel informada.
	 * Parte da producao e calcula os demais campos e portanto desconsidera o flagPRoducao = Fixo,
	 * pois foi iniciativa do usuario a edicao do valor.
	 * 
	 * Esta retornando o DispProj do periodo sendo calculado, que sera usado como
	 * DispProjAnterior na proxima rodada 
	 * 
	 * @param planoModelo
	 * @param plPerModCorrente
	 * @param dispProjAnterior
	 * @param hpBD
	 * @return
	 * @author felipe.arruda
	 */
	public Double recalculaDispProjECobModelDaProdModel(PlanoModelo planoModelo,
			PlPerMod plPerModCorrente, Double dispProjAnterior, HP hpBD) {

		plPerModCorrente.setPeriodoPMInicioPMP(0);
		//determina a demanda maxima no proximo periodo para calculo da cobertura
		Double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente,hpBD);

		//variavel temporaria usada para facilitar a clareza da formula
		Double novoDispProjModel = 0.0;
		
		Boolean inicioPMPAntesInicioHP = false;
		
		Integer periodoDefasado = 0;
		
		//Calcula nova prodLotModel e prodDiariaLote a partir da producaoModel em pecas editada
		//Calcula producao em lotes e producao diaria em lotes
		if(plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() == 0 ||
				planoModelo.getModelo().getTamLote() == 0){

			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProducaoModel(0.0);
		}
		else {
			double novaProdDiariaLoteModel = Numeric.resultadoDivisaoInteira(
					plPerModCorrente.getProducaoModel(), 
					plPerModCorrente.getPerioPM().getNumDiasUteisMatriz() * 
					planoModelo.getModelo().getTamLote());

			
			plPerModCorrente.setProdDiariaLoteModel(novaProdDiariaLoteModel);

			double novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(
					plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente
							.getPerioPM().getNumDiasUteisMatriz());

			plPerModCorrente.setProdLoteModel(novaProdLoteModel);

		}
		
		//Recalcula producao em pecas, considerando a producao em lotes e a
		//producao diaria em lote, devido ao arredondamento ocorrido no calculo
		//da prodLoteModel e na prodDiariaModel
		if(plPerModCorrente.getProdLoteModel() < 0){

			plPerModCorrente.setProdDiariaLoteModel(0.0);
			plPerModCorrente.setProdLoteModel(0.0);
			plPerModCorrente.setProducaoModel(0.0);
		}
		else{
			plPerModCorrente.setProducaoModel(plPerModCorrente
					.getProdDiariaLoteModel() *
					 planoModelo.getModelo().getTamLote() *
					 plPerModCorrente.getPerioPM().getNumDiasUteisMatriz());
		}
		
		
		
		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());
		
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);
		
		
//		System.out.println("INICIO PMP (DEPOIS) = " + inicioPMPAntesInicioHP);
//		System.out.println("PERIODO DEFASADO (DEPOIS) = " + periodoDefasado);
		
		if(plPerModCorrente.getProducaoModel() > 0){
			
			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP  && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
			
		}
		
		//Recalcula dispProj com a nova producaoModel
		novoDispProjModel = plPerModCorrente.getProducaoModel() +
							dispProjAnterior - 
							Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
		
		plPerModCorrente.setDispProjModel(novoDispProjModel);
		
		//Recalcula cobertura do modelo a partir da nova disponibilidade
		if(demandaMaxProxPeriodo != 0){
			double novaCobModel = (plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo) * 100;
			
			plPerModCorrente.setCoberturaModel(novaCobModel);
		
		
		}
		else{

			plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());
		
		}
		
		dispProjAnterior = plPerModCorrente.getDispProjModel();
		return dispProjAnterior;
	}


	/**
	 * Recalcula para todos os periodos, a partir da edição da ProdDiariaLoteModel
	 * informando todos os outros parametros(DispProj, Cobertura, PeriodoInicioPMP).
	 * 
	 * @author felipe.arruda
	 * @param plPerModCorrente
	 */
	@Transacional
	public void recalculaParametrosBaseadoProdDiaria(PlPerMod plPerModCorrente) {

		HP hpBD = hpDAO.recuperaListaDeHP().get(0);
		
		//Pegando a lista de plpermod apartir do proximo periodo referente ao plpermodCorrenteS
		List<PlPerMod>listaDePlPerMods = plPerModDAO.recuperaListaDePlPerModPorPlanoModeloEPerioPMApartirDePerioPM(plPerModCorrente.getPlanoModelo(),plPerModCorrente.getPerioPM().getPeriodoPM()+1);
		Collections.sort(listaDePlPerMods, new PlPerModComparatorPorPerioPM());
		
		Double dispProjAnterior = null;
		
		//se a alteracao foi feita no 1º periodo entao usa o esqInicModel, caso contrario, pega a dispProj
		//do periodo anterior
		if(plPerModCorrente.getPerioPM().getPeriodoPM() == 1){
			//obtem dispProj do periodo anterior
			dispProjAnterior = plPerModCorrente.getPlanoModelo().getModelo().getEstqInicModel()  
								+ plPerModCorrente.getPlanoModelo().getModelo().getRecebimentoPendente()
								- plPerModCorrente.getPlanoModelo().getModelo().getEstqEmFalta();
			
		}
		else{
			// tratar melhor essa excecao
			PerioPM perioPMPAnt=null;
			//pega o perioPM anterior ao do plpermodcorrente
			try {
				perioPMPAnt = perioPMDAO.recuperaPerioPMPorPeriodoPM(plPerModCorrente.getPerioPM().getPeriodoPM()-1);
			} catch (ObjetoNaoEncontradoException e) {
				//nao entra aqui pois a principio se for o primeiro nem entra nesse else
			}
			
			PlPerMod plPerModPeriodoAnterior=null;
			try {
				plPerModPeriodoAnterior = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM
												(plPerModCorrente.getPlanoModelo(), perioPMPAnt);
				dispProjAnterior = plPerModPeriodoAnterior.getDispProjModel();
				
			} catch (ObjetoNaoEncontradoException e) {
				//a principio nao deve acontecer essa excecao
			}
			
			
			
		}
		
		//Recalcula demais parametros para o periodo em que foi alterada producao
		//nesse caso parte da nova producao em lote para calcular os outros campos
		dispProjAnterior = recalculaDispProjECobModelDaProdDiaria(plPerModCorrente.getPlanoModelo(),
															plPerModCorrente,dispProjAnterior , hpBD);
		//Salva o recalculo em arquivo
		this.altera(plPerModCorrente);
		
		//Lista que comeca no proximo periodo depois do periodo do PlPerModCorrente
		//recalcula demais parametros para os periodos restantes do HP
		for (PlPerMod plPerMod : listaDePlPerMods){
			
			//nesse caso parte da cobertura para recalcular os demais parametros
			dispProjAnterior = calculaDispProjProducaoECoberturaModel(plPerMod.getPlanoModelo(),
																	plPerMod,dispProjAnterior, hpBD);
			
			//salva o plpermod atual para que ele fique com todas as informacoes gravadas em banco
			this.altera(plPerMod);
		}
		
	}

	/**
	 * Recalcula para um dado periodo em que houve alteracao na prodDiariaLote,
	 * todos os outros parametros (DispProj, Cobertura, PeriodoInicioPMP), a partir da prodDiariaLote informada.
	 * Calcula os demais campos e portanto desconsidera o flagPRoducao=Fixo, pois foi iniciativa
	 * do usuario a edicao do valor.
	 * 
	 * Esta retornando o DispProj do periodo sendo calculado, que sera usado como
	 * DispProjAnterior na proxima rodada 
	 * 
	 * @param planoModelo
	 * @param plPerModCorrente
	 * @param dispProjAnterior
	 * @param hpBD
	 * @return
	 * @author felipe.arruda
	 */
	public Double recalculaDispProjECobModelDaProdDiaria(PlanoModelo planoModelo,
			PlPerMod plPerModCorrente, Double dispProjAnterior, HP hpBD) {

		plPerModCorrente.setPeriodoPMInicioPMP(0);
		//determina demanda maxima no proximo period para calcular cobertura.
		Double demandaMaxProxPeriodo = obtemDemandaMaxProxPeriodo(plPerModCorrente,hpBD);

		//variavel temporaria usada para facilitar a clareza da formula
		Double novoDispProjModel = 0.0;
		
		Boolean inicioPMPAntesInicioHP = false;
		
		Integer periodoDefasado = 0;
		

		//calcula nova producaoModel e ProdLoteModel em pecas
		//a partir da ProdDiariaLoteModel editada.
		//para evitar prodLote fraciario usa a funcao resultadoMultiplicacaoInteira.
		double novaProdLoteModel = Numeric.resultadoMultiplicacaoInteira(
				plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente
						.getPerioPM().getNumDiasUteisMatriz());

		plPerModCorrente.setProdLoteModel(novaProdLoteModel);
		//reacalcula a producaoModel depois da edicao e recalculo da prodLoteModel
		double novaProducaoModel = Numeric.resultadoMultiplicacaoInteira(
				plPerModCorrente.getProdDiariaLoteModel(), plPerModCorrente
						.getPerioPM().getNumDiasUteisMatriz() *
						planoModelo.getModelo().getTamLote());

		plPerModCorrente.setProducaoModel(novaProducaoModel);

		
		
		
		Map resultado = perioPMService.verificaPeriodoInicioPMP(inicioPMPAntesInicioHP,
												plPerModCorrente.getPerioPM().getPeriodoPM(),
												periodoDefasado, planoModelo.getModelo().getTr());
		
		inicioPMPAntesInicioHP = (Boolean) resultado.get("inicioPMPAntesInicioHP");
		periodoDefasado = (Integer) resultado.get("periodoDefasado");
		plPerModCorrente.setPeriodoPMInicioPMP(periodoDefasado);
		
		
//		System.out.println("INICIO PMP (DEPOIS) = " + inicioPMPAntesInicioHP);
//		System.out.println("PERIODO DEFASADO (DEPOIS) = " + periodoDefasado);
		
		if(plPerModCorrente.getProducaoModel() > 0){
			
			//ocorrencia de excecao: necessidade de producao antes do inicio do HP
			if(inicioPMPAntesInicioHP  && plPerModCorrente.getFlagProducaoModel()){
				plPerModCorrente.setProducaoModel(0.0);
				plPerModCorrente.setProdDiariaLoteModel(0.0);
				plPerModCorrente.setProdLoteModel(0.0);
			}
			//verificar se para flagproducao = fixo tb deveria fazer InicioProdAntesHP =0
			if(inicioPMPAntesInicioHP){				
				plPerModCorrente.setInicioProdAntesHP(plPerModCorrente.getProducaoModel());
			}
			else{
				plPerModCorrente.setInicioProdAntesHP(0.0);
			}
		}
		
		
		//Recalcula dispProjModel com nova ProducaoModel
		novoDispProjModel = plPerModCorrente.getProducaoModel() +
							dispProjAnterior - 
							Math.max(plPerModCorrente.getVendasModel(), plPerModCorrente.getPedidosModel());
		
		plPerModCorrente.setDispProjModel(novoDispProjModel);
		//Recalcula cobertura do modelo a partir da nova disponibilidade
		if(demandaMaxProxPeriodo != 0){
			double novaCobModel = (plPerModCorrente.getDispProjModel() / demandaMaxProxPeriodo) * 100;
			
			plPerModCorrente.setCoberturaModel(novaCobModel);
		
		
		}
		else{

			plPerModCorrente.setCoberturaModel(planoModelo.getModelo().getCobertura());
		
		}
		
		dispProjAnterior = plPerModCorrente.getDispProjModel();
		return dispProjAnterior;
	}

	
	
	
//-------------------------------- Marques ----------------------------------------------//
	
	/**
	 * Este método é responsável por gerar um relatório ..... 
	 * @author marques.araujo
	 * @param List<> 
	 * @return void
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorioAgregado(CadPlan cadplan) throws AplicacaoException {
		
	
		System.out.println(">>>>>>>>>>>>>> Antes da variavel relatorio em gerarRelatorioAgregado>>>>>>");
		
		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_PLANO_MESTRE_DE_PRODUCAO_POR_MODELO);
			
		System.out.println(">>>>>>>>>>>>>> Depois da variavel relatorio em gerarRelatorioAgregado>>>>>>");
		
		if(relatorio!=null){
			System.out.println("A variavel relatorio eh diferente de null");
		}else{
			System.out.println("A variavel relatorio tem valor null");
		}
		
		try{
			
			relatorio.gerarRelatorio(this.converterParaPlanoMestreDeProducaoPorModeloRelatorio(cadplan),new HashMap());
			
			System.out.println(">>>>>>>>>>>>>> Depois da variavel relatorio em gerarRelatorioAgregado executa converter>>>>>>");
			
		} catch (RelatorioException re){
			throw new AplicacaoException("cadplan.RELATORIO_NAO_GERADO");
		}
	}
	
	/**
	 * Este método é responsável por gerar objetos do tipo PlanoMestreDeProducaoPorModeloRelatorio a partir de objetos do tipo CadPlan,
	 * PlanoModelo, PlPerMode, PerioPm, Familia e Modelo.
	 * @author marques.araujo
	 * @param CadPlan cadplan
	 * @return List<PlanoMestreDeProducaoPorModeloRelatorio>
	 * 
	 */
	
	public List<PlanoMestreDeProducaoPorModeloRelatorio> converterParaPlanoMestreDeProducaoPorModeloRelatorio(CadPlan cadplan)throws AplicacaoException{
			
			List<PlanoMestreDeProducaoPorModeloRelatorio> planoMestreRelatorio = new LinkedList<PlanoMestreDeProducaoPorModeloRelatorio>();
			
			  for (PlanoModelo planoModelo : cadplan.getPlanosModelo()) {
				  
				  List<PlPerMod> plpermods = this.recuperaListaDePlPerModPorPlanoModelo(planoModelo); 
				  Modelo modelo = modeloService.recuperaUmModeloComFamilia(planoModelo.getModelo());
				  Familia familia = modelo.getFamilia();
				  
				  if(plpermods.isEmpty()){//Caso o PlanoModelo nao tenha plpermods.
					  planoMestreRelatorio.add(new PlanoMestreDeProducaoPorModeloRelatorio(cadplan,planoModelo,null,familia));
				  }
				  
				  //Perceba que o planomodelo que nao tiver plpermods nao passara no segundo for e tambem nao dara erro.
				  for (PlPerMod plpermod : plpermods) {
					  
					  planoMestreRelatorio.add(new PlanoMestreDeProducaoPorModeloRelatorio(cadplan, planoModelo, plpermod, familia));
				}
				
			}
			  
			  Collections.sort(planoMestreRelatorio);//Ordenando para ficar de acordo com a tela list do plpermod
			  
		return planoMestreRelatorio;
    }
		
	
	
}
