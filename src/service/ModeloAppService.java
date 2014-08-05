 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import exception.relatorio.RelatorioException;

import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import modelo.CadPlan;
import modelo.DeModPer;
import modelo.Familia;
import modelo.Modelo;
import modelo.ModeloDecorado;
import modelo.ModeloRelatorio;
import modelo.PMP;
import modelo.Parametros;
import modelo.PerioPMVig;
import modelo.Recurso;
import DAO.CadPlanDAO;
import DAO.FamiliaDAO;
import DAO.ModeloDAO;
import DAO.PMPDAO;
import DAO.ParametrosDAO;
import DAO.PerioPMVigDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.FamiliaDAOImpl;
import DAO.Impl.ModeloDAOImpl;
import DAO.Impl.PMPDAOImpl;
import DAO.Impl.ParametrosDAOImpl;
import DAO.Impl.PerioPMVigDAOImpl;

public class ModeloAppService {
	
	// DAOs
	private static ModeloDAO modeloDAO;
	private static FamiliaDAO familiaDAO;
	private static ParametrosDAO parametrosDAO;
	private static CadPlanDAO cadPlanDAO;
	private static PMPDAO pmpDAO;
	private static PerioPMVigDAO perioPMVigDAO;
	
	// Services
	private static DeModPerAppService deModPerService;
	private static FamiliaAppService familiaService;
	
	@SuppressWarnings("unchecked")
	public ModeloAppService() {
		try {
			
			// DAOs
			modeloDAO = FabricaDeDao.getDao(ModeloDAOImpl.class);
			familiaDAO = FabricaDeDao.getDao(FamiliaDAOImpl.class);
			parametrosDAO = FabricaDeDao.getDao(ParametrosDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			pmpDAO = FabricaDeDao.getDao(PMPDAOImpl.class);
			perioPMVigDAO = FabricaDeDao.getDao(PerioPMVigDAOImpl.class);
			
			// Services
			deModPerService = FabricaDeAppService.getAppService(DeModPerAppService.class);
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Método que tenta incluir um modelo, caso não haja nenhum cadastrado com o mesmo código.
	 * 
	 * @author Walanem
	 * @throws AplicacaoException
	 */
	@Transacional
	public void inclui(Modelo modelo) throws AplicacaoException {

		Parametros parametro = parametrosDAO.recuperaListaDeParametros().get(0);
		
		if (!parametro.isInicPlanejamento()){
			throw new AplicacaoException("parametros.PLANEJAMENTO_NAO_INCIALIZADO");
		}
		
		try {
			modeloDAO.recuperaModeloPorCodigo(modelo.getCodModelo());
			throw new AplicacaoException("modelo.CODIGO_EXISTENTE");
		} catch (ObjetoNaoEncontradoException ob) {
		}
		
		modeloDAO.inclui(modelo);
		
		this.recalcularAtributosDaFamilia(modelo.getFamilia());
		
		try {
			deModPerService.incluiDemandas(modelo);
		} catch (AplicacaoException ex) {
			throw new AplicacaoException(ex.getMessage());
		}
		
		// 	Módulo que inclui PlPerMods no BD, no intuito de sempre termos um número equivalente de PlPerMods para
		// CadPlans e Modelos.
		List<CadPlan> cadPlansCadastrados = cadPlanDAO.recuperaListaDeCadPlan();
		
		if (!cadPlansCadastrados.isEmpty()){
			
			PlanoModeloAppService planoModeloService;     // Inicialização cíclica
			
			try {
				planoModeloService = FabricaDeAppService.getAppService(PlanoModeloAppService.class);
				planoModeloService.incluirPlanoModeloParaNovoModelo(cadPlansCadastrados, modelo);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	/**
	 * Método padrão de alteração de Modelo.
	 * 
	 * @author Walanem
	 * @throws AplicacaoException 
	 */
	@Transacional
	public void altera(Modelo modelo) throws AplicacaoException {
		//verifica se tem cadplans cadastrados, se tiver nao pode alterar o modelo.
		if(cadPlanDAO.recuperaListaDeCadPlan().size()> 0){
			throw new AplicacaoException("modelo.CADPLAN_CADASTRADOS");
		}
		modeloDAO.altera(modelo);
		this.recalcularAtributosDaFamilia(modelo.getFamilia());
	}

	/**
	 * Método que atualiza os campos usados na implementacao do PMP de um modelo especifico
	 * para que assumam os respectivos valores usados na tabela de modelo  no momento da inclusao do PMP vigente
	 * Ex:
	 * modelo.trPMP = modelo.tr
	 * 
	 * @author felipe.arruda
	 */
	@Transacional
	public void atualizaCamposPMP(Modelo modelo) {
		modelo.setTrPMP(modelo.getTr());
		modelo.setTamLotePMP(modelo.getTamLote());
		modelo.setCoberturaPMP(modelo.getCobertura());
		modelo.setEstqInicModelPMP(modelo.getEstqInicModel());
		modelo.setRecebimentoPendentePMP(modelo.getRecebimentoPendente());
		modelo.setEstqEmFaltaPMP(modelo.getEstqEmFalta());
		modeloDAO.altera(modelo);
	}

	/**
	 * Verifica se existe um pmp, isso é se tem um plano implementado.
	 * Se tiver ele atualiza o valor do estoque em falta do modelo para ser a
	 * quantidade que aparece negativa do campo DispProj relativos
	 * ao primeiro periodo do PMP.
	 * @throws AplicacaoException 
	 */
	public Modelo atualizaEstoqueEmFaltaDeModeloPorPMP(Modelo modelo) throws AplicacaoException{
		//perioPMVig
		PerioPMVig perioPMVig=null;
		try {
			//tenta recuperar perioPMVig relativo ao periodo 1
			perioPMVig = perioPMVigDAO.recuperaPerioPMVigPorPeriodoPM(1);
			//tenta recuperar o pmp relativo a esse modelo e ao periodo 1
			PMP pmp = pmpDAO.recuperaPMPPorModeloEPerioPMVig(modelo, perioPMVig);
			if(pmp.getDispProjModel() < 0){
				modelo.setEstqEmFalta(pmp.getDispProjModel()*-1);				
			}
			
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("modelo.PMP_NAO_ENCONTRADO");
		}
		
		return modelo;
	}
	/**
	 * Método que exclui um Modelo, caso não hajam Demandas associadas a ele.
	 * 
	 * O método getPorIdComLock() é utilizado para garantir exclusão mútua, pois o método é crítico e requer isolamento.
	 * 
	 * 	
	 * Quando excluir Modelo, exclui em cascata os recModels associados,
	 * devido as regras da relação estabelecidas no Modelo.java  (Felipe) 
	 * 
	 * 
	 * @author Walanem
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(Modelo modelo) throws AplicacaoException {
		
		Modelo modeloBD = null;
		
		try {
			modeloBD = modeloDAO.getPorIdComLock(modelo.getId());
			
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}
		
		//this.recalcularAtributosDaFamilia(modelo.getFamilia());
		
		modeloDAO.exclui(modeloBD);
		this.recalcularAtributosDaFamilia(modelo.getFamilia());
	}
	
	@Transacional
	public void recalcularAtributosDaFamilia(Familia familia){
		
		List<Modelo> modelos = modeloDAO.recuperaListaDeModelosPorFamilia(familia);
		
		Double totalTmuc = 0.0;
		Double totalCobertura = 0.0;
		Double totalEstoqueInicial = 0.0;
		
		for (Modelo modeloCorrente : modelos) {
			
			totalTmuc += modeloCorrente.getTuc();
			totalCobertura += modeloCorrente.getCobertura();
			totalEstoqueInicial += modeloCorrente.getEstqInicModel();
		}
		
		if (!modelos.isEmpty()){
			
			familia.setTmuc(totalTmuc/modelos.size());
			familia.setCobertura(totalCobertura/modelos.size());
			familia.setEstqInicFam(totalEstoqueInicial);
			
			familiaService.altera(familia);
		}
	}
	
	public List<Modelo> recuperaListaDeModelosPorFamilia(Familia familia) {
		return modeloDAO.recuperaListaDeModelosPorFamilia(familia);
	}

	/**
	 * Usa um método do DAO para recuperar um modelo juntamente com a sua familia
	 * 
	 * @author Walanem
	 * @throws AplicacaoException
	 */
	public Modelo recuperaUmModeloComFamilia(Modelo modelo) throws AplicacaoException {
		
		try {
			return modeloDAO.recuperaUmModeloComFamilia(modelo);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}
	}

	/**
	 * 
	 * Usa modeloDAO para recuperar lista de todos os modelos. Retorna um List de Modelo
	 * 
	 * @author Walanem
	 * @throws AplicacaoException
	 */
	public List<Modelo> recuperaListaDeModelos() throws AplicacaoException {
		
		List<Modelo> modelos = modeloDAO.recuperaListaDeModelos();
		
		if (modelos.isEmpty()) {
			throw new AplicacaoException("modelo.MODELOS_INEXISTENTES");
		} else {
			return modelos;
		}
	}

	public List<Modelo> recuperaListaDeModelosComFamilias() {
		return modeloDAO.recuperaListaDeModelosComFamilias();
	}
	
	public List<Modelo> recuperaListaPaginadaDeModelosComFamilias(){
		return modeloDAO.recuperaListaPaginadaDeModelosComFamilias();
	}
	
	public List<Modelo> recuperaListaDeModelosComFamiliasEPeriodos(){
		return modeloDAO.recuperaListaDeModelosComFamiliasEPeriodos();
	}

	public Modelo recuperaModeloComFamiliaEPeriodos(Modelo modelo) throws AplicacaoException{
		
		Modelo modeloBD = null;
		
		try{
			modeloBD = modeloDAO.recuperaModeloComFamiliaEPeriodos(modelo);
		} catch (ObjetoNaoEncontradoException exc) {
			throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}
		
		return modeloBD;
	}

	public List<Modelo> recuperaListaPaginadaDeModelosComFamiliaComListaDePMPs() {
		
		return modeloDAO.recuperaListaPaginadaDeModelosComFamiliaComListaDePMPs();
	}
	
	public Modelo recuperaModeloPorCodigo(String codigoModelo) throws AplicacaoException {

		Modelo modeloBD = null;

		try {
			modeloBD = modeloDAO.recuperaModeloPorCodigo(codigoModelo);
		} catch (ObjetoNaoEncontradoException exc) {
			throw new AplicacaoException("modelo.NAO_ENCONTRADO");
		}

		return modeloBD;
	}
	
	/**
	 * 
	 * Recupera a lista de todos os modelos que possuam código aproximado com o passado por parametro. 
	 * 
	 * @author Walanem
	 */
	public List<Modelo> recuperaModeloPorCodigoLike(String codigoModelo){
		return modeloDAO.recuperaModeloPorCodigoLike(codigoModelo);
	}

	public List<Modelo> recuperaModeloPorDescricao(String descricaoModelo) {
		return modeloDAO.recuperaModeloPorDescricao(descricaoModelo);
	}
	
	@SuppressWarnings("unchecked")
	public void gerarRelatorio(List<Modelo> modelos) throws AplicacaoException {

		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_MODELOS);
		List<ModeloRelatorio> dados = this.converterModelosParaModelosRelatorio(modelos);
		
		try{
			relatorio.gerarRelatorio(dados, new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("modelo.RELATORIO_NAO_GERADO");
		}
	}

	/**
	 * Método auxiliar para conversão de ModelosDecorados em ModeloRelatorio, pois esta classe possui atributos
	 * oriundos de outras entidades.
	 * 
	 * @author Walanem
	 */
	public List<ModeloRelatorio> converterModelosParaModelosRelatorio(List<Modelo> modelos) {
		
		ModeloRelatorio modeloRelatorio = null;
		List<ModeloRelatorio> modelosRelatorio = new ArrayList<ModeloRelatorio>(modelos.size());

		for (Modelo modelo : modelos) {
			modeloRelatorio = new ModeloRelatorio(modelo);
			modelosRelatorio.add(modeloRelatorio);
		}
	  
		return modelosRelatorio;	
	}
}
