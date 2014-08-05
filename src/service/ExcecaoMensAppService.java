 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comparator.PlPerModComparatorPorPerioPM;

import modelo.CadPlan;
import modelo.Excecao;
import modelo.ExcecaoMens;
import modelo.HP;
import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.PlanoModelo;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.ExcecaoDAO;
import DAO.ExcecaoMensDAO;
import DAO.HPDAO;
import DAO.PerioPMDAO;
import DAO.PlPerModDAO;
import DAO.PlanoModeloDAO;
import DAO.Impl.ExcecaoDAOImpl;
import DAO.Impl.ExcecaoMensDAOImpl;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.Impl.PlPerModDAOImpl;
import DAO.Impl.PlanoModeloDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * classe responsavel pela geracao das mensagens de excecao por planoModelo.
 * @author arruda
 *
 */
public class ExcecaoMensAppService {

	// DAOs
	private static PerioPMDAO perioPMDAO; 
	private static ExcecaoDAO excecaoDAO; 
	private static HPDAO hpDAO; 
	private static PlPerModDAO plPerModDAO; 
	private static PlanoModeloDAO planoModeloDAO; 
	private static ExcecaoMensDAO excecaoMensDAO; 
	
	// Services
	private static ExcecaoAppService excecaoService;
	private static PlPerModAppService plPerModService;
	
	@SuppressWarnings("unchecked")
	public ExcecaoMensAppService() {
		try {
			
			// DAOs
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			excecaoDAO = FabricaDeDao.getDao(ExcecaoDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			plPerModDAO = FabricaDeDao.getDao(PlPerModDAOImpl.class);
			planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
			excecaoMensDAO = FabricaDeDao.getDao(ExcecaoMensDAOImpl.class);
			
			// Service
			excecaoService = FabricaDeAppService.getAppService(ExcecaoAppService.class);
			plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	
	/**
	 * Inclui uma excecaoMens
	 * Verifica se existe já uma com as mesmas chavez, se existir nao inclui.
	 * 
	 * Se o periodoOrigem for null, utiliza a query "recuperaListaDeExcecaoMensPorPlanoModeloEExcecao", para evitar buscar perioPM null.
	 * Caso não seja null, utiliza a query: "recuperaListaDeExcecaoMensPorPlanoModeloEExcecaoEPeriodoOrigem".
	 * 
	 * @param excecaoMens
	 * @return
	 * @throws AplicacaoException
	 */
	@Transacional
	public long inclui(ExcecaoMens excecaoMens) throws AplicacaoException {
		
		long retorno = -1;
		List<ExcecaoMens> listaExecaoMens=null;
		if(excecaoMens.getPeriodoOrigem() == null){
			listaExecaoMens = excecaoMensDAO.recuperaListaDeExcecaoMensPorPlanoModeloEExcecao
			(excecaoMens.getPlanoModelo(),excecaoMens.getExcecao());
			
		}
		else{
			listaExecaoMens = excecaoMensDAO.recuperaListaDeExcecaoMensPorPlanoModeloEExcecaoEPeriodoOrigem
			(excecaoMens.getPlanoModelo(),excecaoMens.getExcecao(),
						excecaoMens.getPeriodoOrigem());
			
		}		
		if(listaExecaoMens.size()!= 0){
			throw new AplicacaoException("excecaoMens.EXCECAOMENS_EXISTENTE");
		}
		retorno = excecaoMensDAO.inclui(excecaoMens).getId();
		
		
		return retorno;

	}

	/**
	 * Altera uma ExcecaoMens
	 * @param excecao
	 */
	@Transacional
	public void altera(ExcecaoMens excecaoMens) {
		excecaoMensDAO.altera(excecaoMens);
	}

	/**
	 * Exclui uma ExcecaoMens
	 * 
	 * @param excecao
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(ExcecaoMens excecaoMens) throws AplicacaoException {

		ExcecaoMens excecaoParaExcuir = null;

		try {
			excecaoParaExcuir = excecaoMensDAO.getPorIdComLock((excecaoMens.getId()));
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("excecaoMens.NAO_ENCONTRADA");
		}

		excecaoMensDAO.exclui(excecaoParaExcuir);
	}
	

	public void gerarMensagensExcecao(CadPlan cadPlan) throws AplicacaoException {		
		deletarExcecoesDoPlano(cadPlan);
		List<PlanoModelo>listaPlanoModeloCadPlan = new ArrayList(cadPlan.getPlanosModelo());
		Collections.sort(listaPlanoModeloCadPlan);		//ordenando
		for(PlanoModelo planoModelo : listaPlanoModeloCadPlan){
//			try {
//				planoModelo = planoModeloDAO.recuperarPlPerModsPorPlanoModelo(planoModelo);
//			} catch (ObjetoNaoEncontradoException e) {
//			}
			List<PlPerMod>listaDePlPerMods = plPerModDAO.recuperaListaDePlPerModPorPlanoModelo(planoModelo);
			Collections.sort(listaDePlPerMods, new PlPerModComparatorPorPerioPM());
			planoModelo.setPlPerModsList(listaDePlPerMods);
			gerarMensagemDeExcecaoPorModelo(planoModelo);
		}
	}
	
	@Transacional
	public void deletarExcecoesDoPlano(CadPlan cadPlan) throws AplicacaoException {
		List<ExcecaoMens> excecoesDoPlano = recuperaListaDeExcecaoMensPorCadPlan(cadPlan);
		for (ExcecaoMens excecaoMens: excecoesDoPlano){
				exclui(excecaoMens);
		}
	}
	
	public ArrayList<Map> inicializaVetorRecebimentos(ArrayList<Map> vetorRecebimentos,PlanoModelo planoModelo){
		//verificar se esta ordenado nesse momento.
		for(PlPerMod plPerMod : planoModelo.getPlPerModsList()){
			Map novoMap = new HashMap();
			novoMap.put("periodo", plPerMod.getPerioPM());
			novoMap.put("recebimentoVerificado", false);
			novoMap.put("producao", plPerMod.getProducaoModel());
			vetorRecebimentos.add(novoMap);
		}
		return vetorRecebimentos;
	}
	/**
	 * Cria uma mensagem de excecao usando os parametros passados.
	 * Caso o periodoDestino e o periodoOrigem não existam, entao cria usando null em ambos.
	 * 
	 * @param planoModelo
	 * @param periodoOrigem
	 * @param periodoDestino
	 * @param excecao
	 * @param quantidade
	 * @return
	 * @throws AplicacaoException 
	 */
	public ExcecaoMens criaMensagemExcecao(PlanoModelo planoModelo,
			int periodoOrigem,int periodoDestino,Excecao excecao,Double quantidade) throws AplicacaoException{		
		ExcecaoMens novaMensagem = new ExcecaoMens();
		novaMensagem.setPlanoModelo(planoModelo);
		novaMensagem.setExcecao(excecao);
		PerioPM perioOrigem=null;
		PerioPM perioDestino=null;
		try{
			perioOrigem = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoOrigem);
		}
		catch(ObjetoNaoEncontradoException e){	
		}
		try{
			perioDestino = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoDestino);
		}
		catch(ObjetoNaoEncontradoException e){	
		}
		novaMensagem.setPeriodoOrigem(perioOrigem);
		novaMensagem.setPeriodoDestino(perioDestino);
		novaMensagem.setQuantidade(quantidade);	
		inclui(novaMensagem);
		return novaMensagem;
	}
	
	public void gerarMensagemDeExcecaoPorModelo(PlanoModelo planoModelo) {
		ArrayList<Map> vetorRecebimentos = new ArrayList<Map>();
		vetorRecebimentos = inicializaVetorRecebimentos(vetorRecebimentos,planoModelo); 
		List<Excecao> excecoes = excecaoService.inicializaVetorExcecao();
		double demandaTotalDoHP = 0.0;
		double estoqueInicModelCalc = planoModelo.getModelo().getEstqInicModel()
										+ planoModelo.getModelo().getRecebimentoPendente()
										- planoModelo.getModelo().getEstqEmFalta();
		//representa a porcentagem permitida considerada toleravel no calculo do excesso da cobertura de estoque.
		double limiteSuperior = planoModelo.getModelo().getCobertura()*1.25;
		
		//msg sobre saldo inicial negativo
		if(excecoes.get(1).getStatusExcecao()){
			if(estoqueInicModelCalc < 0){				
				try {
					criaMensagemExcecao(planoModelo,0,0,excecoes.get(1),estoqueInicModelCalc);
				} catch (AplicacaoException e) {
				}
			}
		}

		//msg sobre recebimento pendente com prazo vencido
		if(excecoes.get(5).getStatusExcecao()){
			if(planoModelo.getModelo().getRecebimentoPendente() > 0){				
				try {
					criaMensagemExcecao(planoModelo,0,0,excecoes.get(5),planoModelo.getModelo().getRecebimentoPendente());
				} catch (AplicacaoException e) {
				}
			}
		}
		
		//msg sobre estoque em falta
		if(excecoes.get(4).getStatusExcecao()){
			if(planoModelo.getModelo().getEstqEmFalta() > 0){				
				try {
					criaMensagemExcecao(planoModelo,0,0,excecoes.get(4),planoModelo.getModelo().getEstqEmFalta());
				} catch (AplicacaoException e) {
				}
			}
		}
		//percorre todos os plpermods desse planoModelo
		for(PlPerMod plPerMod : planoModelo.getPlPerModsList()){
			//inicio do PMP antes do primeiro periodo do HP
			if(excecoes.get(3).getStatusExcecao()){
				if(plPerMod.getPeriodoPMInicioPMP() < 1){
					try {
						criaMensagemExcecao(planoModelo, plPerMod.getPerioPM().getPeriodoPM(), 0,
											excecoes.get(3), null);
					} catch (AplicacaoException e) {
					}
				}
			}
			if(plPerMod.getProducaoModel() != 0){

				//ordem prestes a ser liberada
				if(excecoes.get(4).getStatusExcecao()){
					if(plPerMod.getPeriodoPMInicioPMP() == 1){
						try {
							criaMensagemExcecao(planoModelo, plPerMod.getPerioPM().getPeriodoPM(), 0,
												excecoes.get(4), plPerMod.getProducaoModel());
						} catch (AplicacaoException e) {
						}
					}
				}
				
			}

			//mensagem : Estoque Excessivo no Periodo
			//se tiver estoque excessivo no período, além de 15% acima da cobertura de estoque desejada para o período
			//estou trabalhando com uma margem pois o proprio algoritmo de inclusão, como faz muitos arredondamentos para cima 
			// devido ao tam lote acaba sempre trabalhando com um valor um pouco acima do desejado
			if(excecoes.get(9).getStatusExcecao() && 
					plPerMod.getCoberturaModel()  > limiteSuperior){
				try {
					criaMensagemExcecao(planoModelo, plPerMod.getPerioPM().getPeriodoPM(), 0,
										excecoes.get(9), plPerMod.getDispProjModel());
				} catch (AplicacaoException e) {
				}
			}
			demandaTotalDoHP = demandaTotalDoHP + Math.max(plPerMod.getPedidosModel(), plPerMod.getVendasModel());				
		}

		//estoque inicial excessivo
		if(excecoes.get(2).getStatusExcecao()){
			if(estoqueInicModelCalc > demandaTotalDoHP){
				try {
					criaMensagemExcecao(planoModelo, 0, 0,excecoes.get(2),
										(estoqueInicModelCalc - demandaTotalDoHP));
				} catch (AplicacaoException e) {
				}
			}			
		}
		//antecipar, postergar e cancelar recebimento de producao
		if(excecoes.get(6).getStatusExcecao() ||
			excecoes.get(7).getStatusExcecao() ||
			excecoes.get(8).getStatusExcecao()){
			
			double disponibilidade = estoqueInicModelCalc;
			//para todos os periodos da lista fazer:
			//percorre todos os periodos do plper mod do plano e do modelo
			//até atingir a disponibilidade desejada
			//antecipa os recebimentos que ocorrem apos o momento necessario
			//e posterga aqueles que ocorrem antes do AuxPeriodo necessario.
			HP hp = hpDAO.recuperaListaDeHP().get(0);
			List<PerioPM> periodosHP = perioPMDAO.recuperaIntervaloDePerioPMs(hp.getPerioPMInicPMP().getPeriodoPM(),
					hp.getPerioPMFinalPMP().getPeriodoPM());
			
			for (PerioPM perioPM : periodosHP){
				PlPerMod plPerMod =null;
				try {
					plPerMod = plPerModDAO.recuperaPlPerModPorPlanoModeloEPerioPM(planoModelo, perioPM);
				} catch (ObjetoNaoEncontradoException e) {
				}
				disponibilidade = disponibilidade - Math.max(plPerMod.getPedidosModel(), plPerMod.getVendasModel());
				
				double demandaMaxProxPeriodo = plPerModService.obtemDemandaMaxProxPeriodo(plPerMod, hp);

				double disponibilidadeDesejada = demandaMaxProxPeriodo * planoModelo.getModelo().getCobertura() / 100;	
		
//				double disponibilidadeDesejada = Math.max(plPerMod.getPedidosModel(), plPerMod.getVendasModel())
//				* planoModelo.getModelo().getCobertura() / 100;	
				
				Map infosRecebimento = new HashMap();
				boolean localizouRecebimento = true;
				int numPeriodoRecebimento =0;//???
				double proxRecebimento =0;//????
				infosRecebimento.put("localizouRecebimento", localizouRecebimento);
				infosRecebimento.put("numPeriodoRecebimento", 0);//???
				infosRecebimento.put("proxRecebimento", proxRecebimento);
				while(disponibilidade < disponibilidadeDesejada && localizouRecebimento){
					//aparentemente vetorRecebimentos esta sendo alterado, mesmo sem retornar como parametro
					infosRecebimento = localizaProxRecebimentoPMP(infosRecebimento, vetorRecebimentos, hp);
					//recupera as infos do map
					localizouRecebimento = (Boolean)infosRecebimento.get("localizouRecebimento");
					numPeriodoRecebimento = (Integer)infosRecebimento.get("numPeriodoRecebimento");
					proxRecebimento = (Double)infosRecebimento.get("proxRecebimento");
					if(localizouRecebimento){
						disponibilidade = disponibilidade + proxRecebimento;
						//mensagem : antecipar recebimento
						//vai antecipar mesmo que com esse recebimento 
						//a disponibilidade fique um pouco maior que a desejada naquele periodo,
						//para evitar falta.
						//é uma postura conservadora.
						if(excecoes.get(6).getStatusExcecao() && numPeriodoRecebimento > perioPM.getPeriodoPM()){
							try {
								criaMensagemExcecao(planoModelo, numPeriodoRecebimento, perioPM.getPeriodoPM(),
													excecoes.get(6), proxRecebimento);
							} catch (AplicacaoException e) {
							}
						}
						//mensagem: postergar recebimento
						if(excecoes.get(7).getStatusExcecao() && numPeriodoRecebimento < perioPM.getPeriodoPM()){
							try {
								criaMensagemExcecao(planoModelo,numPeriodoRecebimento, perioPM.getPeriodoPM(),
													excecoes.get(7), proxRecebimento);
							} catch (AplicacaoException e) {
							}
						}
					}					
				}				
			}
			//mensagem de cancelamento de recebimento
			/*se ja atingiu a disponibilidade desejada e saiu do loop
			 * antes de chegar a verificar todos os periodos
			 * e portanto ainda tiver recebimentos programados nao
			 * verificados, entao esses devem ser cancelados pois estao excedendo a quantidade
			 * necessaria
			 */
			if(excecoes.get(8).getStatusExcecao()){
				Map infosRecebimento = new HashMap();
				boolean localizouRecebimento = true;
				int numPeriodoRecebimento =0;
				double proxRecebimento =0;
				infosRecebimento.put("localizouRecebimento", localizouRecebimento);
				infosRecebimento.put("numPeriodoRecebimento", 0);
				infosRecebimento.put("proxRecebimento", proxRecebimento);
				while(localizouRecebimento){
					//aparentemente vetorRecebimentos esta sendo alterado, mesmo sem retornar como parametro
					infosRecebimento = localizaProxRecebimentoPMP(infosRecebimento, vetorRecebimentos, hp);
					//recupera as infos do map
					localizouRecebimento = (Boolean)infosRecebimento.get("localizouRecebimento");
					numPeriodoRecebimento = (Integer)infosRecebimento.get("numPeriodoRecebimento");
					proxRecebimento = (Double)infosRecebimento.get("proxRecebimento");
					if(localizouRecebimento){
						try {
							criaMensagemExcecao(planoModelo, numPeriodoRecebimento, 0, excecoes.get(8),	proxRecebimento);
						} catch (AplicacaoException e) {
						}
					}
					
				}
			}
			
		}
		
	}
	
	public Map localizaProxRecebimentoPMP(Map parametrosEntrada,ArrayList<Map> vetorRecebimentos, HP hp){		
		boolean localizouRecebimento = (Boolean)parametrosEntrada.get("localizouRecebimento");
		int numPeriodoRecebimento = (Integer)parametrosEntrada.get("numPeriodoRecebimento");
		double proxRecebimento = (Double)parametrosEntrada.get("proxRecebimento");
		
		localizouRecebimento = false;
		parametrosEntrada.put("localizouRecebimento", localizouRecebimento);	
		int perio = 1;
		//percorre todos valores(maps) do vetor de recebimento(periodos basicamente)
		for(Map mapPeriodoCorrenteVetorReceb : vetorRecebimentos){
			//se localizar entao sai do loop
			if(localizouRecebimento)
				break;
			//recupera as infos do map atual do vetorRecebimentos (recebimentoVerificado do periodo sendo analisado)
			boolean recebimentoVerificado = (Boolean)mapPeriodoCorrenteVetorReceb.get("recebimentoVerificado");
			double producao = (Double)mapPeriodoCorrenteVetorReceb.get("producao");
			PerioPM periodo = (PerioPM)mapPeriodoCorrenteVetorReceb.get("periodo");
			
			if(!recebimentoVerificado && producao != 0){
				parametrosEntrada.put("localizouRecebimento", true);	
				parametrosEntrada.put("numPeriodoRecebimento", periodo.getPeriodoPM());	
				parametrosEntrada.put("proxRecebimento", producao);	
				mapPeriodoCorrenteVetorReceb.put("recebimentoVerificado", true);	
				
				localizouRecebimento=true;							
			}
		}
		
		return parametrosEntrada;
	}

	public List<ExcecaoMens> recuperaListaPaginadaDeExcecaoMens(){
		return excecaoMensDAO.recuperaListaPaginadaDeExcecaoMens();
	}
	
	public List<ExcecaoMens> recuperaListaDeExcecaoMensPorPlanoModelo(PlanoModelo planoModelo){
		return excecaoMensDAO.recuperaListaDeExcecaoMensPorPlanoModelo(planoModelo);
	}
	/**
	 * Percorre os planosModelos do cadPlan em questao e retorna a lista de 
	 * ExcecaoMens de todos os planos.
	 * @param cadPlanCorrente
	 * @return
	 */
	@Transacional
	public List<ExcecaoMens> recuperaListaDeExcecaoMensPorCadPlan(CadPlan cadPlanCorrente){
		List<ExcecaoMens> listaExcecaoMens = new ArrayList<ExcecaoMens>();
		for(PlanoModelo planoModelo : cadPlanCorrente.getPlanosModelo()){
			listaExcecaoMens.addAll(excecaoMensDAO.recuperaListaDeExcecaoMensPorPlanoModelo(planoModelo));
		}
		return listaExcecaoMens;
	}


}
