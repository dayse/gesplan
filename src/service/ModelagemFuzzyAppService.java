 
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
import java.util.StringTokenizer;

import org.richfaces.model.UploadItem;

import com.google.gson.Gson;

import exception.motorInferencia.MotorInferenciaException;


import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;

import service.exception.AplicacaoException;

import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

import DAO.Impl.ModelagemFuzzyDAOImpl;
import DAO.Impl.CadPlanDAOImpl;

import DAO.ModelagemFuzzyDAO;
import DAO.CadPlanDAO;

import modelo.ModelagemFuzzy;
import modelo.CadPlan;
import modelo.RegraModelagemView;
import modelo.VariavelModelagemView;

import br.blog.arruda.plot.Plot;
import br.blog.arruda.plot.data.PlotData;
import br.blog.arruda.plot.opt.tipo.PlotLines;
import br.blog.arruda.plot.opt.tipo.PlotPoints;
import motorInferencia.MotorInferencia;

import util.Constantes;
import xfuzzy.lang.AggregateMemFunc;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.MemFunc;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Rule;
import xfuzzy.lang.RulebaseCall;
import xfuzzy.lang.Specification;
import xfuzzy.lang.Type;
import xfuzzy.lang.Variable;



public class ModelagemFuzzyAppService {
	
	// DAOs
	private static ModelagemFuzzyDAO modelagemFuzzyDAO;
	private static CadPlanDAO cadPlanDAO;

	// Services
	private static DadosGraficoViewAppService dadosGraficoViewService;
	
	@SuppressWarnings("unchecked")
	public ModelagemFuzzyAppService() {
		try {
			
			// DAOs
			modelagemFuzzyDAO = FabricaDeDao.getDao(ModelagemFuzzyDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);
			
			// Service
			dadosGraficoViewService = FabricaDeAppService.getAppService(DadosGraficoViewAppService.class);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	
	/**
	 * Inclui uma modelagem fuzzy sozinha, isso é sem o arquivo de modelagem.
	 * @param modelagemFuzzy
	 * @return
	 * @throws AplicacaoException
	 */
	@Transacional
	public long inclui(ModelagemFuzzy modelagemFuzzy) throws AplicacaoException {
		
		long retorno = -1;
		
		try {
			modelagemFuzzyDAO.recuperaModelagemFuzzyPeloNome(modelagemFuzzy.getNomeModelagemFuzzy());
			throw new AplicacaoException("modelagem.NOME_MODELAGEM_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {			
			retorno = modelagemFuzzyDAO.inclui(modelagemFuzzy).getId();
		}
		
		
		return retorno;

	}

	
	/**
	 * Metodo resposavel por salvar os dados de um arquivo de modelagem fuzzy(.xfl).
	 * Chama o motor de inferencia e utiliza o metodo salvarArquivoModelagem passando o caminho do arquivo
	 * e os dados a serem incluidos.
	 * Se o metodo anterior jogar uma motorInferencia exception ele a converte para uma applicationexception e joga para cima.
	 * Com isso a parte que lida com a gravacao dos dados em arquivo fica ligada apenas ao motorinferencia.
	 * 
	 */
	@Transacional
	public void salvarArquivoModelagemFuzzy(UploadItem item) throws AplicacaoException {

    	String caminhoDoArquivo = Constantes.CAMINHO_ABSOLUTO_MODELAGEM_UPLOADFILE;

    	System.out.println("Destino do arquivo="+caminhoDoArquivo);
    	System.out.println("original="+item.getFileName());
    	
    	String filePathName = item.getFileName();
    	String fileName = "";
    	StringTokenizer st = new StringTokenizer(filePathName,"\\");    	
    	while(st.hasMoreElements()){
    		fileName = st.nextToken();
    	}
    	
    	
    	caminhoDoArquivo = caminhoDoArquivo  + fileName;
    	
    	try {
			MotorInferencia.salvarArquivoModelagem(caminhoDoArquivo, item.getData());
		} catch (MotorInferenciaException e) {
			throw new AplicacaoException(e.getMessage());
		}
	}
	
	
	/**
	 * Inclui uma modelagem fuzzy junto com o arquivo que foi feito upload, dessa forma ambos os passos ficam ligados.
	 * Apois incluir a modelagem fuzzy desejada no BD, ele tenta salvar o arquivo em si no sistema.
	 * Se um dos passos falhar ele volta sem deixar lixo.
	 * @param modelagemFuzzy
	 * @param item
	 * @return
	 * @throws AplicacaoException
	 */
	@Transacional
	public long incluiComUpload(ModelagemFuzzy modelagemFuzzy, UploadItem item) throws AplicacaoException {
		
		
		long retorno = -1;
		
		try {
			modelagemFuzzyDAO.recuperaModelagemFuzzyPeloNome(modelagemFuzzy.getNomeModelagemFuzzy());
			throw new AplicacaoException("modelagem.CODIGO_MODELAGEM_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {		
				retorno = modelagemFuzzyDAO.inclui(modelagemFuzzy).getId();		
				salvarArquivoModelagemFuzzy(item);
		}		

		return retorno;
	}


	/**
	 * Altera uma modelagem fuzzy
	 * @param modelagemFuzzy
	 */
	@Transacional
	public void altera(ModelagemFuzzy modelagemFuzzy) {
		modelagemFuzzyDAO.altera(modelagemFuzzy);
	}
	
	/**
	 * Exclui uma modelagemFuzzy
	 * logo em seguida tenta excluir o arquivo de modelagem ligado a essa modelagem Fuzzy.
	 * @param modelagemFuzzy
	 * @throws AplicacaoException
	 */
	@Transacional
	public void exclui(ModelagemFuzzy modelagemFuzzy) throws AplicacaoException {
		
		List<CadPlan> listaDeCadPlans = cadPlanDAO.recuperaListaDeCadPlanPorModelagemFuzzy(modelagemFuzzy);
		if (listaDeCadPlans.isEmpty()){
			// se não tem cadPlans associados a essa modelagem então pode excluir a modelagem
			
			ModelagemFuzzy umaModelagemFuzzy = null;
			
			try {
				umaModelagemFuzzy = modelagemFuzzyDAO.getPorIdComLock((modelagemFuzzy.getId()));
			} catch (ObjetoNaoEncontradoException e) {
				throw new AplicacaoException("modelagemFuzzy.NAO_ENCONTRADO");
			}
			

			modelagemFuzzyDAO.exclui(umaModelagemFuzzy);
			
			//======= PONTO CRITICO===== Exclui o arquivo da modelagem!
			String caminhoDoArquivo = Constantes.CAMINHO_ABSOLUTO_MODELAGEM_UPLOADFILE + modelagemFuzzy.getNomeArquivo();
			
			//chama o motor de inferencia para remover o arquivo fisico.
			try {
				MotorInferencia.removerArquivoModelagem(caminhoDoArquivo);
			} catch (MotorInferenciaException e) {
				throw new AplicacaoException(e.getMessage());
			}
		    //======= PONTO CRITICO=====
			    
		}
		else{
			throw new AplicacaoException("modelagemFuzzy.USADA_EM_CADPLAN");
		}
		
	}
	
	/**
	 * Gera o grafico das funcoes de pertinencia de uma dada variavel de uma
	 * dada modelagem fuzzy.
	 * ATENCAO: FUNCIONA APENAS PARA FUNÇÕES DE PERTINENCIA TRIANGULAR, TRAPEZOIDAL E SINGLETON
	 * OS VALORES DAS FUNCOES DE PERTINENCIA(NO EIXO Y) SAO ESTABELECIDOS MANUALMENTE ENTRE 0 e 1
	 * @param variavelGraficoMF
	 * @param modelagemFuzzy
	 * @return
	 */
	public Plot gerarDadosGraficoMF(String variavelGraficoMF, ModelagemFuzzy modelagemFuzzy) {
		  ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
		  Plot grafico = new Plot();
		  
		  //IMPRESSAO DAS FUNCOES DE PERTINENCIA DE TODAS AS VARIAVEIS LINGUISTICAS
		  Type[] variaveisLinguisticas = modelagemFuzzy.getModelagem().getTypes();
		  //Atribuindo manualmente os valores para o eixo y(grau de pertinencia)
		  ArrayList<Double> triangular = new ArrayList<Double>();
		  triangular.add(0.0);
		  triangular.add(1.0);
		  triangular.add(0.0);

		  ArrayList<Double> trapezoidal = new ArrayList<Double>();
		  trapezoidal.add(0.0);
		  trapezoidal.add(1.0);
		  trapezoidal.add(1.0);
		  trapezoidal.add(0.0);

		  ArrayList<Double> singleton = new ArrayList<Double>();
		  singleton.add(0.0);
		  singleton.add(1.0);
		  
		  //executa o loop para todas as variaveis linguisticas
		  for(int varLing=0; varLing < variaveisLinguisticas.length;varLing++){
			  //se for a variavel escolhida, ele pega os dados dela.
			  if(variavelGraficoMF.equals(variaveisLinguisticas[varLing].getName())){
				  
				  LinguisticLabel[] mf = variaveisLinguisticas[varLing].getAllMembershipFunctions();
				  //mf.length devolve o número de termos linguísticos desta variável linguística
				  //type.length devolve o número de variáveis linguisticas
				  //na variável produção por exemplo teremos mf.length = 11 (ou seja, teremos 11 termos linguísticos)		  
				  for(int termosLing=0; termosLing<mf.length; termosLing++) {
					  ArrayList<Double> listaDados = new ArrayList<Double>();

					  // busca todos os resultados de uma só vez: usando o toXfl - para isso nao precisaria do "for" abaixo
					  // System.out.print("      - " + mf[numTermosLing].toXfl() + "\n");	
					  //executa o loop para todas os parâmetros de cada termo linguístico
					  
					  for(int param=0; param<mf[termosLing].get().length; param++) {
//							//Cria os dados de grafico com os valores das funcoes de pertinencia da atual iteracao do loop.
						    listaDados.add(mf[termosLing].get()[param]);
						    //se for singleton
						    if(mf[termosLing] instanceof pkg.xfl.mfunc.singleton){
						    	listaDados.add(mf[termosLing].get()[param]);
							}		
					  	}	  
					  
					  PlotData dadoGrafico = new PlotData();
					  //se for triangular
					  if(mf[termosLing] instanceof pkg.xfl.mfunc.triangle){

							dadoGrafico = dadosGraficoViewService.gerarPlotData(listaDados, triangular);	
					  }
					  //se for trapezoidal
					  else if(mf[termosLing] instanceof pkg.xfl.mfunc.trapezoid){
						  dadoGrafico = dadosGraficoViewService.gerarPlotData(listaDados, trapezoidal);
					  }		//se for trapezoidal
					  else if(mf[termosLing] instanceof pkg.xfl.mfunc.singleton){
						  dadoGrafico = dadosGraficoViewService.gerarPlotData(listaDados, singleton);
					  }					  
					  	  
						dadoGrafico.setLabel(mf[termosLing].getLabel());
						//revesa o pontilhamento dos graficos
						if(termosLing % 2 != 0){
							PlotPoints points = new PlotPoints();
							dadoGrafico.setPoints(points);						
						}
						//tem que por forcado os lines, já que colocando pontos ele vai considerar que sao apenas pontos pelo
						//padrao do Flot, por isso tem que explicitar que tem tambem linhas.
						PlotLines lines = new PlotLines();
						dadoGrafico.setLines(lines);						
						listaDadosGrafico.add(dadoGrafico);
					  
				  	}
				  	//USAR BREAK?! ou dar return?
					//retorna os dados do grafico em formato compativel com o JS na forma de uma string.
					break;
				  }	 	  
			  }


			//====seta as opcoes basicas do grafico
			grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, variavelGraficoMF, "Grau de Pertinencia");
		
		//retorna o  grafico.
		return grafico;
		
	}	

	
	/**
	 * Retorna a string que representa os dados do grafico das funcoes de pertinencia de uma dada variavel de uma
	 * dada modelagem fuzzy.
	 * @param variavelGraficoMF
	 * @param modelagemFuzzy
	 * @return
	 */
	public String imprimirDadosGraficoMF(String variavelGraficoMF, ModelagemFuzzy modelagemFuzzy) {
		  Plot grafico = gerarDadosGraficoMF(variavelGraficoMF, modelagemFuzzy);
		  return grafico.printData();		
	}
	
	public ArrayList<PlotData> gerarPlotDatasGraficoConjuntoFuzzyResultante(
							Variable output, MemFunc mf, Double valor) {

		  	ArrayList<PlotData> listaDadosGrafico = new ArrayList<PlotData>();
			double min = output.getType().getUniverse().min();
			double max = output.getType().getUniverse().max();
			//numero de amostras para realizar o grafico
			//se for uma mf agregada não discreta(continua).
		   int num_samples=186;
		   //nao faz nada se a mf for nula
		   if(mf != null){			   
			   if(mf instanceof pkg.xfl.mfunc.singleton) {
				    double val = ((ParamMemFunc) mf).get()[0];
				    //cria os dados para o eixo Y(0 - 1)
				    ArrayList<Double> yAxis =new ArrayList<Double>();
				    yAxis.add(0.0);
				    yAxis.add(1.0);

				    //prepara o eixo x do singleton
				    ArrayList<Double> xAxis =new ArrayList<Double>();
				    xAxis.add(val);
				    xAxis.add(val);
				    
				    //gera o plotData com essa informacao
					PlotData dataGrafico = dadosGraficoViewService.gerarPlotData(xAxis, yAxis);
					dataGrafico.setLabel("Conjunto Fuzzy Resultante");
					//adciona na lista de dados do grafico
					listaDadosGrafico.add(dataGrafico);
			   }
			   
			   else if((mf instanceof AggregateMemFunc) &&
			      ((AggregateMemFunc) mf).isDiscrete() ) {
				   
				    double[][] val = ((AggregateMemFunc) mf).getDiscreteValues();
				    ArrayList<Double> xAxis =new ArrayList<Double>();
				    ArrayList<Double> yAxis =new ArrayList<Double>();
				    for(int i=0; i<val.length; i++){
				    	//verificar esses aqui se deve ainda sim diminuir de min e dividir pelo max-min
						double xpos = val[i][0];
						double ypos = val[i][1];
//						int xpos = (int) ((val[i][0]-min)*num_samples/(max-min));
//						int ypos = (int) (val[i][1]*num_samples);

						//add os valores de x e y
					    xAxis.add(xpos);
					    yAxis.add(ypos);
				    }
				    //gera o plotData com essa informacao
					PlotData dataGrafico = dadosGraficoViewService.gerarPlotData(xAxis, yAxis);
					dataGrafico.setLabel("Conjunto Fuzzy Resultante");
					//adciona na lista de dados do grafico
					listaDadosGrafico.add(dataGrafico);
				    
				    				    
			   }
			   
			   else{
				   double yval = mf.compute(0);
				   ArrayList<Double> xAxis =new ArrayList<Double>();
				   ArrayList<Double> yAxis =new ArrayList<Double>();
				   int i =1;
				   for(i=1; i<num_samples; i++) {
					   double xval =  min + (max-min)*i /num_samples;
					   yval = mf.compute( xval );
					   
					   //adciona os valores de x e y do ponto atual
					   xAxis.add(xval);
					   yAxis.add(yval);
					   
					   
				   } 
				   //adciona os ultimos pontos, para i+1 e npos final
				   //simulando o mesmo que era feito no drawline:
					//	   				x0		y0	  x1	 y1
					//	   gmf.drawLine(x0+i, ypos, x0+i+1, npos);				   
				   xAxis.add(Double.valueOf( min + (max-min)*(i+1) /num_samples));
				   yAxis.add(yval);
				   
				    //gera o plotData com essa informacao
					PlotData dataGrafico = dadosGraficoViewService.gerarPlotData(xAxis, yAxis);
					dataGrafico.setLabel("Conjunto Fuzzy Resultante");
					//adciona na lista de dados do grafico
					listaDadosGrafico.add(dataGrafico);
				   
			   }			  
			   
		   }
		   
		   //se tiver valor desenha tambem o ponto dado
		   if(valor!=null){
				   ArrayList<Double> xAxis =new ArrayList<Double>();
				   ArrayList<Double> yAxis =new ArrayList<Double>();
				   //(x0=valor, y0=0)
				   xAxis.add(valor);
				   yAxis.add(0.0);

				   // (x1=valor, y1=1)
				   xAxis.add(valor);
				   yAxis.add(1.0);
				   
				   
				    //gera outro plotData com essa informacao
					PlotData dataGrafico = dadosGraficoViewService.gerarPlotData(xAxis, yAxis);
					dataGrafico.setLabel("Valor Defuzzyficado");
					//adciona na lista de dados do grafico
					listaDadosGrafico.add(dataGrafico);
				   
		   }
		   
		return listaDadosGrafico;
	}

	public Plot gerarDadosGraficoConjuntoFuzzyResultante(MotorInferencia motorInferencia) {
		Plot grafico = new Plot();
		ArrayList<PlotData> listaDadosGrafico=null;
		RulebaseCall call = motorInferencia.getModelagemCarregada().getSystemModule().getRulebaseCalls()[0];
		
		MemFunc result[] = call.getFuzzyValues();
		MemFunc trueresult[] = call.getTrueValues();
		
	   Variable output[] = call.getRulebase().getOutputs();
	   
		 for(int i=0; i<result.length; i++) {			   
			 if(trueresult[i] instanceof pkg.xfl.mfunc.triangle) {
				 double val = ((pkg.xfl.mfunc.triangle) trueresult[i]).get()[0];
				listaDadosGrafico=  this.gerarPlotDatasGraficoConjuntoFuzzyResultante(output[i], result[i], val);
			 }
			 else{
				listaDadosGrafico= this.gerarPlotDatasGraficoConjuntoFuzzyResultante(output[i], result[i], null);
			 }
		 }

		//====seta as opcoes basicas do grafico
		grafico = dadosGraficoViewService.gerarPlotComLabels(listaDadosGrafico, "Variavel de Output", "Grau de Pertinencia");
		
		return grafico;
	}

	
	/**
	 * Metodo que retorna uma modelagem(uma specification do xFuzzy) de uma modelagemFuzzy
	 * Esse metodo chama o metodo carregamodelagem do motor de inferencia.
	 * @param modelagemFuzzy
	 * @return
	 * @throws AplicacaoException
	 */
	public Specification recuperaArquivoDaModelagem(ModelagemFuzzy modelagemFuzzy) throws AplicacaoException{

    	MotorInferencia motorInferencia = new MotorInferencia();    	
		Specification modelagem;
		try {
			modelagem = motorInferencia.carregarModelagem(modelagemFuzzy.getNomeArquivo());
		} catch (MotorInferenciaException e) { 
			throw new AplicacaoException(e.getMessage());
		}		
		
		return modelagem;
		
	}
	
	
	/**
	 * Retorna uma modelagem fuzzy com seu arquivo de modelagem junto, dado uma modelagem fuzzy.
	 * @param modelagemFuzzy
	 * @return
	 * @throws AplicacaoException
	 */
	public ModelagemFuzzy recuperaModelagemFuzzyComSpecification(ModelagemFuzzy modelagemFuzzy) throws AplicacaoException{		
	
		modelagemFuzzy.setModelagem(recuperaArquivoDaModelagem(modelagemFuzzy));
		
		return modelagemFuzzy;
	}
	/**
	 * Retorna a lista de todas as modelagens fuzzy
	 * @return
	 */
	public List<ModelagemFuzzy> recuperaListaPaginadaDeModelagemFuzzys(){		
		
		return modelagemFuzzyDAO.recuperaListaPaginadaDeModelagemFuzzys();		
	}

	/**
	 * Retorna a lista de todas as modelagens fuzzy de uma determinada finalidade.
	 * @param finalidade
	 * @return
	 */
	public List<ModelagemFuzzy> recuperaListaPaginadaDeModelagemFuzzysPorFinalidade(String finalidade){		
		
		return modelagemFuzzyDAO.recuperaListaDeModelagemFuzzysPorFinalidade(finalidade);		
	}
	
	/**
	 * Retorna uma string que representa o arquivo de xfl 
	 * @param modelagemFuzzy
	 * @return
	 */
	public String imprimeModelagem(ModelagemFuzzy modelagemFuzzy){
		return MotorInferencia.imprimeModelagem(modelagemFuzzy.getModelagem());			
	}

	/**
	 * Retorna uma string que representa o as regras da modelagem 
	 * @param modelagemFuzzy
	 * @return
	 */
	public String imprimeRegras(ModelagemFuzzy modelagemFuzzy){
		return MotorInferencia.imprimeRegras(modelagemFuzzy.getModelagem());			
	}

	/**
	 * Retorna uma string que representa os operadores do xfl 
	 * @param modelagemFuzzy
	 * @return
	 */
	public String imprimeOperadores(ModelagemFuzzy modelagemFuzzy){
		return MotorInferencia.imprimeOperadores(modelagemFuzzy.getModelagem());			
	}

	/**
	 * Retorna uma string que representa os limites do xfl 
	 * @param modelagemFuzzy
	 * @return
	 */
	public String imprimeLimites(ModelagemFuzzy modelagemFuzzy){
		return MotorInferencia.imprimeLimites(modelagemFuzzy.getModelagem());			
	}

	/**
	 * Retorna uma string que representa as funcoes de pertinencia do xfl 
	 * @param modelagemFuzzy
	 * @return
	 */
	public String imprimeMF(ModelagemFuzzy modelagemFuzzy){
		return MotorInferencia.imprimeMF(modelagemFuzzy.getModelagem());			
	}

	/**
	 * Retorna uma lista de RegraModelagemView populada a partir de uma modelagem.
	 * @param modelagemFuzzy
	 * @return
	 */
	public ArrayList<RegraModelagemView> recuperaListaRegraModelagemViewPorModelagemPorMotorInferencia
											(ModelagemFuzzy modelagemFuzzy,MotorInferencia motorInferenciaCorrente){
		
		ArrayList<RegraModelagemView> regras = new ArrayList<RegraModelagemView>();
		ArrayList<Rule>rules  = MotorInferencia.recuperaListaRegras(modelagemFuzzy.getModelagem());
		ArrayList<Double> degree  = motorInferenciaCorrente.recuperaListaDegree();

		for (int i=0;i< rules.size();i++){
			Rule rule = rules.get(i);
			RegraModelagemView novaRegra = new RegraModelagemView();
			//coloca o indice como sendo o indice da regra
			novaRegra.setIndice(i);
			//se for null quer dizer que nao tem ainda grau de ativacao, logo coloca valor de 0 para o mesmo. So para 
			if(degree==null){
				novaRegra.setValor(null);
				
			}
			else{
				//coloca o valor como sendo o grau de ativacao da regra
				novaRegra.setValor(degree.get(i));
			}
			regras.add(novaRegra);
		}
			
		return regras;
	}
	
	/**
	 * Retorna uma lista de VariavelModelagemView populada a partir das variaveis de input de uma modelagem.
	 * @param modelagemFuzzy
	 * @return
	 */
	public ArrayList<VariavelModelagemView> recuperaListaVariavelModelagemViewDeInputPorModelagem(ModelagemFuzzy modelagemFuzzy){
		
		ArrayList<VariavelModelagemView> variaveis = new ArrayList<VariavelModelagemView>();
		for (Variable variable : MotorInferencia.recuperaListaVariaveisInput(modelagemFuzzy.getModelagem())){
			VariavelModelagemView novaVariavel = new VariavelModelagemView();
			novaVariavel.setNome(variable.getName());
			novaVariavel.setValor(variable.point(0.5));
			variaveis.add(novaVariavel);
		}
		return variaveis;
	}

	/**
	 * Retorna uma lista de VariavelModelagemView populada a partir das variaveis de output de uma modelagem.
	 * @param modelagemFuzzy
	 * @return
	 */
	public ArrayList<VariavelModelagemView> recuperaListaVariavelModelagemViewDeOutputPorModelagem(ModelagemFuzzy modelagemFuzzy){
		
		ArrayList<VariavelModelagemView> variaveis = new ArrayList<VariavelModelagemView>();
		for (Variable variable : MotorInferencia.recuperaListaVariaveisOutput(modelagemFuzzy.getModelagem())){
			VariavelModelagemView novaVariavel = new VariavelModelagemView();
			novaVariavel.setNome(variable.getName());
			variaveis.add(novaVariavel);
		}
		return variaveis;
	}

	
	//TODO: jogar excecao aki caso valores estejam vazios?
	public double[] recuperaValoresDeVariaveisInput
						(ArrayList<VariavelModelagemView> variaveisViewInput, Specification modelagem){
		double [] valores= new double[modelagem.getSystemModule().getInputs().length];
		
		//percorre todas as variaveis de input, e percorre as views, quando achar as que batem o nome, altera o valor da variavel
		//para o valor da variavel view.
		for(int i=0;i<modelagem.getSystemModule().getInputs().length;i++){
			Variable variavel = modelagem.getSystemModule().getInputs()[i];
			//Apenas a variavel selecionada para o eixoY tera seu valor como 0.0
			//esse valor depois será alterado
			Double valor=0.0;
			for(VariavelModelagemView varView : variaveisViewInput){
				if(variavel.getName().equals(varView.getNome())){
					valor = varView.getValor();
					//remove a variavel achada da lista, para evitar ficar muito lento
//					variaveisViewInput.remove(varView);
				}
			}
			valores[i]=valor;
		}
		return valores;
	}

	/**
	 * Gera um grafico do simulador 2D usando logica baseada no XFuzzy.
	 * @param eixoX
	 * @param eixoY
	 * @param variaveisInputQueSobraram
	 * @param variaveisOutputQueSobraram
	 * @param modelagemFuzzyCorrente
	 * @return
	 * @throws MotorInferenciaException
	 */
	public Plot gerarPlotSimulador2D(VariavelModelagemView eixoX,
										VariavelModelagemView eixoY,
										ArrayList<VariavelModelagemView> variaveisInputQueSobraram,
										ArrayList<VariavelModelagemView> variaveisOutputQueSobraram,
										ModelagemFuzzy modelagemFuzzyCorrente) throws MotorInferenciaException {
		//numero de amostras do grafico
		int numAmostras = 101;
		//recupera variavel de output e de input dos eixos
		Variable inputVar  = MotorInferencia.recuperaVariavelPorNome(eixoX.getNome(),modelagemFuzzyCorrente.getModelagem());
		int xindex = MotorInferencia.recuperaIndiceVariavelInputPorNome(eixoX.getNome(), modelagemFuzzyCorrente.getModelagem()); 
		
		Variable outputVar = MotorInferencia.recuperaVariavelPorNome(eixoY.getNome(),modelagemFuzzyCorrente.getModelagem());
		int yindex = MotorInferencia.recuperaIndiceVariavelOutputPorNome(eixoY.getNome(), modelagemFuzzyCorrente.getModelagem()); 
		
		
		
		//min e max 
		double ymin = outputVar.point(0.0);
		double ymax = outputVar.point(1.0);
		
		//lista de valores das variaveis de input
		double valoresInput[] = recuperaValoresDeVariaveisInput(variaveisInputQueSobraram,modelagemFuzzyCorrente.getModelagem());

		MotorInferencia motorInferencia = new MotorInferencia();

		ArrayList<Double> valoresDoEixoX = new ArrayList<Double>();
		ArrayList<Double> valoresDoEixoY = new ArrayList<Double>();
		//para cada amostra do numero de amostras, indo de 0-numAmostras-1.
		for(int i=0; i<numAmostras; i++) {
			//coloca para cada amostra o valor da variavel selecionada no eixo x
			valoresInput[xindex] = inputVar.point(i*1.0/(numAmostras-1));
			valoresDoEixoX.add(valoresInput[xindex]);
			//executa o motor de inferencia para cada valor de amostra
			//e salva no arrayList do eixo Y
			valoresDoEixoY.add(motorInferencia.executaMotorDeInferencia(valoresInput, modelagemFuzzyCorrente.getNomeArquivo())[yindex]); 
		}
		//cria os dados de pontos para serem utilizados no grafico(Plot) pelo JayFlot.
		ArrayList<PlotData> plotDatas = new ArrayList<PlotData>();	
		PlotData dataSimulador = Plot.generatePlotData(valoresDoEixoX, valoresDoEixoY);
		dataSimulador.setLabel("Simulação");
		plotDatas.add(dataSimulador);
		//cria um grafico(Plot) do JayFlot usando os dados populados anteriormente, 
		//e fazendo a label do eixo X ser o nome da variavel de input
		//e fazendo a label do eixo Y ser o nome da variavel de output
		Plot grafico = Plot.generatePlot(plotDatas, inputVar.getName(), outputVar.getName());
		return grafico;
	}
	
	public String gerarDadosGrafico3D(ModelagemFuzzy modelagemFuzzyCorrente) throws MotorInferenciaException{
		Variable inputvar[] = modelagemFuzzyCorrente.getModelagem().getSystemModule().getInputs();	
		String jsonData = "";
		int z=0;
		Variable outputvar[] = modelagemFuzzyCorrente.getModelagem().getSystemModule().getOutputs();
		double zmin = outputvar[z].point(0.0);
		double zmax = outputvar[z].point(1.0);
		MotorInferencia motorInferencia = new MotorInferencia();
		
		int samples = 40;
	  	double data[][] = new double[samples][samples];
	  	//como sao 2 variaveis de input faz um vetor de 2 variaveis
	  	double dadosInput[] = new double[2];
		System.out.println("len = "+inputvar.length);
		System.out.println("len2 = "+dadosInput.length);
	  	double output = 0.0;
	  	for(int i=0;i<samples;i++){
	  		for(int j=0;j<samples;j++){
	  			dadosInput[0] = inputvar[0].point(i*1.0/(samples-1));
	  			dadosInput[1] = inputvar[1].point(j*1.0/(samples-1));
	  			
	  			output = motorInferencia.executaMotorDeInferencia(dadosInput, modelagemFuzzyCorrente.getNomeArquivo())[z];//modelagem.getModelagem().getSystemModule().crispInference(dadosInput)[z];
	  			data[i][j] = (output - zmin)/(zmax - zmin);
	  		}
	  	}	  	
	  	//converte para gson
		Gson gson = new Gson();
		String temp = gson.toJson(data,data.getClass());
		jsonData = temp;
		return jsonData;		
	}
	
	
}
