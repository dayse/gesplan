 
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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import comparator.PlPerModComparatorPorPerioPM;

import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;

import service.exception.AplicacaoException;

import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMVigDAOImpl;
import DAO.Impl.PMPDAOImpl;
import DAO.Impl.ModeloDAOImpl;

import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import DAO.HPDAO;
import DAO.PerioPMVigDAO;
import DAO.PMPDAO;
import DAO.ModeloDAO;


import modelo.CadPlan;
import modelo.HP;
import modelo.HPVig;
import modelo.PerioPM;
import modelo.PerioPMVig;
import modelo.PMP;
import modelo.Modelo;
import modelo.PlPerMod;
import modelo.PlanoModelo;

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
 * 
 * 
 * 
 * @author felipe/daysemou
 * 
 */
public class PMPAppService {
	
	// DAOs
	private static PMPDAO pmpDAO;
	private static HPDAO hpDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	private static ModeloDAO modeloDAO;
	
	// Services
	private static PerioPMVigAppService perioPMVigService;
	private static ModeloAppService modeloService;
	private static PlanoModeloAppService planoModeloService;
	private static PlPerModAppService plPerModService;
	private static PerioPAPVigAppService perioPAPVigService;
	private static HPVigAppService hpVigService;
	
	@SuppressWarnings("unchecked")
	public PMPAppService() {
		try {
			
			// DAOs
			pmpDAO = FabricaDeDao.getDao(PMPDAOImpl.class);
			hpDAO = FabricaDeDao.getDao(HPDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			
			// Services
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			plPerModService = FabricaDeAppService.getAppService(PlPerModAppService.class);
			planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
			perioPMVigService = FabricaDeAppService.getAppService(PerioPMVigAppService.class);
			perioPAPVigService = FabricaDeAppService.getAppService(PerioPAPVigAppService.class);
			hpVigService = FabricaDeAppService.getAppService(HPVigAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * A inclusão do PMP vigente é feita a partir da opção implementaPlanoMestre no cadastro de Planos
	 * 
	 * 
	 * 
	 * @param pmp
	 * @return
	 * @throws AplicacaoException
	 */
	@Transacional
	public long inclui(PMP pmp) 
		throws AplicacaoException
	{
		long retorno = -1;
		PMP pmpBD = null;
		// verifica se existe o modelo  a que se refere o registro PMP
		try
		{	modeloDAO.getPorIdComLock(pmp.getModelo().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}
		
		// verifica se existe o perioPMVig a que se refere o registro PMP
		try
		{	perioPMVigDAO.getPorIdComLock(pmp.getPerioPMVig().getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("perioPMVig.NAO_ENCONTRADO");
		}
		
		// verifica se o modelo já esta relacionado com este perioPMVig 
		try {
			pmpBD = pmpDAO.recuperaPMPPorModeloEPerioPMVig(pmp.getModelo(), pmp.getPerioPMVig());
			throw new AplicacaoException("capacRec.ENCONTRADO_PERIOPM");
		} catch (ObjetoNaoEncontradoException ob) {
			//o modelo e o perioPMVig serao setados no implementa plano do plpermod, que é o responsavel pela inclusao
			retorno = pmpDAO.inclui(pmp).getId();

		}
		return retorno;
	}	


	
	@Transacional
	public void implementaPlanoMestre(CadPlan cadPlan) 
		throws AplicacaoException
	{
		//===Verifica se todos os modelos desse plano foram planejados
		
		SortedSet<PlanoModelo> setDePlanoModelos =  new TreeSet<PlanoModelo>(cadPlan.getPlanosModelo());
		
		for(PlanoModelo planoModelo : setDePlanoModelos){
			if(!planoModelo.isModeloPlanejado()){
				throw new AplicacaoException("PMP.MODELO_NAO_PLANEJADO");
			}
		}
		
		
		//===Apaga todos os registros de plano mestre vigente atual
		//===Apaga plano mestre vigente atual
		List<PMP> listaDePMPs = pmpDAO.recuperaListaDePMPs();
		
		for(PMP pmp : listaDePMPs){
			this.exclui(pmp);
		}
		

		//===Apaga todos os registros de PerioPMVig, PerioPAPVig, HPVig
		hpVigService.apagaTodos();
		perioPMVigService.apagaTodos();
		perioPAPVigService.apagaTodos();
		
		
		//===Inclui copiando todos os PerioPMVig, PerioPAPVig, HPVig
		perioPAPVigService.incluiCopiandoTodosPerioPAPs();
		perioPMVigService.incluiCopiandoTodosPerioPMs();
		hpVigService.incluiCopiandoTodosHPs();
		HP hp = hpDAO.recuperaListaDeHP().get(0);
		for(PlanoModelo planoModelo : setDePlanoModelos){
//			System.out.println("plano modelo: "+planoModelo.getCadPlan());
			try {
				planoModelo = planoModeloService.recuperarPlPerModsPorPlanoModelo(planoModelo);
				SortedSet<PlPerMod> setDePlPerMods = new TreeSet<PlPerMod>(new PlPerModComparatorPorPerioPM());
				
				setDePlPerMods.addAll(planoModelo.getPlPerMods());
				
//				ArrayList<Double> dispEntregas = plPerModService.calculaDisponibilidadeDeEntregaDiscreta(planoModelo,hp);
				for(PlPerMod plPerMod : setDePlPerMods){
//					System.out.println("plpermod: "+plPerMod.getPerioPM());
					this.incluiCopiandoPlPerMod(plPerMod);
				}
			//depois de copiar os plpermods, altera o modelo referente a esse plano modelo para que os valores
			//dos campos relativos a pmp sejam atualizados
			modeloService.atualizaCamposPMP(planoModelo.getModelo());
			this.calculaDisponibilidadeDeEntregaDiscreta(planoModelo.getModelo());
			} catch (ObjetoNaoEncontradoException e) {
				e.printStackTrace();
			}
			
		}
		
		//Apaga o planoAgregadoVigente e copia o plano selecionado para o Plano Agregado Vigente,
		//ou seja copia o conteudo do arquivo plperfam referente a este cadPlan para o arquivo PAP.
	
		
		//calcaula e grava o parametro disponibilidade de entrega para o plano mestre vigente
		//(Ver procedure CalculaDisponibilidadeDeEntrega)
		

		
		return;
	}	
	
	@Transacional
	public void incluiCopiandoPlPerMod(PlPerMod plPerMod) 
		throws AplicacaoException
	{
		PMP pmp = new PMP();
		
		
		pmp.setCoberturaModel(plPerMod.getCoberturaModel());
//		pmp.setDisponibEntrega(dispEntrega);
		pmp.setDispProjModel(plPerMod.getDispProjModel());
		pmp.setEscorePlanPerMod(plPerMod.getEscorePlanPerMod());
		pmp.setModelo(plPerMod.getPlanoModelo().getModelo());
		pmp.setPedidosModel(plPerMod.getPedidosModel());
		pmp.setPeriodoPMInicioPMP(plPerMod.getPeriodoPMInicioPMP());
		
		PerioPMVig perioPMVig = new PerioPMVig();
		try {
			perioPMVig = perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(plPerMod.getPerioPM().getPeriodoPM());
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		pmp.setPerioPMVig(perioPMVig);
		pmp.setProdDiariaLoteModel(plPerMod.getProdDiariaLoteModel());
		pmp.setProdLoteModel(plPerMod.getProdLoteModel());
		pmp.setProducaoModel(plPerMod.getProducaoModel());
		pmp.setVarEstqPerc(plPerMod.getVarEstqPerc());
		pmp.setVarProdDiaPerc(plPerMod.getVarProdDiaPerc());
		pmp.setVendasModel(plPerMod.getVendasModel());
		
		this.inclui(pmp);
		
		return;
	}	

	
	/**
	 * Retorna o pmp que contem o proximo recebimento, dado:
	 * o periodo em que deve comecar a procurar;
	 * a lista de PMPs que deve percorrer;
	 * o vetor de recebimentos que foram verificados;
	 * 
	 * @param periodo
	 * @param plPerModList
	 * @param vetorRecebimentoVerificados
	 * @return
	 */
	@Transacional
	public PMP localizaProximoRecebimentoSimples(int periodo, List<PMP> pmpList){
		
		 for(int i=periodo-1;i < pmpList.size(); i++){			 
			 PMP pmpCorrente = pmpList.get(i);
			 
			 //se tiver recebimento nesse periodo entao marca esse periodo como verificado e retorna o mesmo plpermod.
			 if(pmpCorrente.getProducaoModel()!= 0){
				 return pmpCorrente;
			 }
		 }
		 //se chegar nesse ponto quer dizer que nao encontrou nenhum recebimento e retorna null
		 return null;
	}

	/**
	 * Retorna o sum dos pedidos acumulados num intervalo de periodo para um dado modelo.
	 * quando o periodo final for menor que o periodo inicial, considera que está querendo analizar apenas o periodo
	 * inicial em particular.
	 * 
	 * @param modelo
	 * @param periodoPMInicial
	 * @param periodoPMFinal
	 * @return
	 */
	@Transacional
	public double calculaPedidosAcumuladosNoIntervaloParaModelo(Modelo modelo, int periodoPMInicial, int periodoPMFinal){
		double pedidosAcumulados = 0.0;
		//entra nesse caso quando temos algo do tipo:
		//periodo inicial = 2
		//periodo final = 1
		//causado pelo fato de que o priprio periodo 2 teve o recebimento, e o algoritimo tenta fazer
		//para n -1
		//mas nesse caso deve considerar apenas o priprio periodo 2.
		if(periodoPMFinal < periodoPMInicial){
			return pedidosAcumulados;
		}
		List<PMP> intervaloPMPs = pmpDAO.recuperaIntervaloDePMPPorModeloEIntervaloDePerioPMVig
															(modelo, periodoPMInicial, periodoPMFinal);
		
		for(PMP pmp: intervaloPMPs){
			pedidosAcumulados += pmp.getPedidosModel();
		}
		return pedidosAcumulados;
	}
	
	/**
	 * Implementando algoritmo para o calculo discreto da Disponibilidade de Entrega
	 * 
	 * @param modelo
	 */
	@Transacional
	public void calculaDisponibilidadeDeEntregaDiscreta(Modelo modelo){
		
		//calcula o estoque total desse modelo:
		double estqInicCalculado = modelo.getEstqInicModelPMP() 
							+ modelo.getRecebimentoPendentePMP()
							- modelo.getEstqEmFaltaPMP();

		List<PMP> listaDePMPDoModelo = pmpDAO.recuperaListaDePMPsPorModeloComPerioPMVigs(modelo);		
		
		//Para cada periodo de 1 até o final do hp vigente
		for(PMP pmpCorrente : listaDePMPDoModelo){
			double dispEntrega= 0.0;
			//Recupera um pmp correspondente a cada periodo para esse modelo 
			//calcula a dispEntrega para ele.

			System.out.println("localizando para:"+pmpCorrente.getPerioPMVig());
			//localiza o proximo recebimento, considerando que deve comecar a procurar desconsiderando o proprio periodo
			//que esta sendo calculado no momento.
			PMP pmpProxRecebimento = this.localizaProximoRecebimentoSimples(pmpCorrente.getPerioPMVig().getPeriodoPM()+1, listaDePMPDoModelo);
			
			//já considera o pedido do proprio periodo, logo em seguida é somado o pedido dos proximos periodos,
			//até o pedido do periodo n-1
			double pedidosAcumulados = pmpCorrente.getPedidosModel();
			
			Integer numPeriodoProxRecebimento;			
			//se for null quer dizer que não existe recebimento nos periodos seguintes a esse.
			//logo deve considerar que vai acumular os pedidos desde o periodo corrente até o ultimo
			//periodo do hp.
			if(pmpProxRecebimento!= null){
				numPeriodoProxRecebimento = pmpProxRecebimento.getPerioPMVig().getPeriodoPM();
			}
			else{
				PMP ultimoPMP = listaDePMPDoModelo.get(listaDePMPDoModelo.size()-1);
				numPeriodoProxRecebimento = ultimoPMP.getPerioPMVig().getPeriodoPM()+1;				
			}
			
			
			//calcula os pedidos acumulados para o intervalo do periodo seguinte ao que está sendo analisado até o 
			//periodo do proximo recebimento -1
			//soma esses valores acumulados de pedidos ao valor que já havia antes(isso é, o do proprio periodo)
			pedidosAcumulados += this.calculaPedidosAcumuladosNoIntervaloParaModelo
												(modelo,pmpCorrente.getPerioPMVig().getPeriodoPM()+1,
														numPeriodoProxRecebimento-1);	
			
			//se for o primeiro periodo faz um calculo diferente
			if(pmpCorrente.getPerioPMVig().getPeriodoPM()==1){
				//calcula a disponibilidade de entrega para o primeiro periodo
				dispEntrega = estqInicCalculado + pmpCorrente.getProducaoModel()
								- pedidosAcumulados;					
			}
			else{
				//se recebimento de producao for igual a 0 a DispEntrega é zero
				if(pmpCorrente.getProducaoModel() == 0){
					dispEntrega= 0.0;
				}
				else{
					dispEntrega = pmpCorrente.getProducaoModel()
								- pedidosAcumulados;	
				}
			}
			if(dispEntrega<0){
				dispEntrega=0.0;
			}
			
			//altera o valor da disponibilidade de entrega do plPermod corrente para a calculada
			pmpCorrente.setDisponibEntrega(dispEntrega);
			this.altera(pmpCorrente);
		}
		
		return;
	}
/**	A principio nao tera alteracao de um pmp
*/	
	@Transacional
	public void altera(PMP pmp) {
		pmpDAO.altera(pmp);
	}
	
	@Transacional
	public void exclui(PMP umPMP) throws AplicacaoException {
		
		PMP pmp = null;
		
		try {
			pmp = pmpDAO.getPorIdComLock((umPMP.getId()));
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("PMP.NAO_ENCONTRADO");
		}

		pmpDAO.exclui(pmp);
	}

	/**
	 * Usa um método do DAO para recuperar um PMP juntamente com o seu perioPMVig
	 * 
	 * @author dayse.arruda
	 * @throws AplicacaoException
	 */
	public PMP recuperaPMPComPerioPMVig(PMP pmp) throws AplicacaoException {
		
		try {
			return pmpDAO.recuperaPMPComPerioPMVig(pmp);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("PMP.NAO_ENCONTRADO");
		}
	}
	
	public PMP recuperaPMPPorModeloEPerioPMVig(Modelo modelo, PerioPMVig perioPMVig) throws AplicacaoException {

		try {
			return pmpDAO.recuperaPMPPorModeloEPerioPMVig(modelo, perioPMVig);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("PMP.NAO_ENCONTRADO");
		}
	}
	
	/**
	 * 
	 * Usa PMPDAO para recuperar lista de todos os PMPs. Retorna um List
	 * de PMPs
	 * 
	 * @author felipe.arruda
	 * @throws AplicacaoException
	 */
	public List<PMP> recuperaListaDePMPs() throws AplicacaoException {
		
		List<PMP> pmps = pmpDAO.recuperaListaDePMPs();
		
		if (pmps.size() == 0) {
			throw new AplicacaoException("PMP.NAO_ENCONTRADO");
		} else {
			return pmps;
		}
	}
	


	public List<PMP> recuperaListaDePMPsComPerioPMVigs () {
		return pmpDAO.recuperaListaDePMPsComPerioPMVigs();
	}

}
