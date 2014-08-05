 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import modelo.File;
import modelo.ModelagemFuzzy;
import modelo.Modelo;
import modelo.RegraModelagemView;
import modelo.VariavelModelagemView;
import motorInferencia.MotorInferencia;
import br.blog.arruda.plot.Plot;

import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import exception.motorInferencia.MotorInferenciaException;

import service.ModelagemFuzzyAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.DataUtil;
import xfuzzy.lang.ParamMemFunc;
import util.SelectOneDataModel;
import xfuzzy.lang.MemFunc;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;
import xfuzzy.lang.SystemModule;
import xfuzzy.lang.Type;
import xfuzzy.lang.Variable;

public class ModelagemFuzzyActions extends BaseActions {
	
	private static ModelagemFuzzyAppService modelagemFuzzyService;

	private ModelagemFuzzy modelagemFuzzyCorrente;
	private DataModel listaDeModelagemFuzzys;

	// Paginas
	public final String PAGINA_GRAFICO_MF = "graficoMFModelagem";
	public final String PAGINA_SIMULADOR_2D_PREPARA = "preparaSimulador2DModelagem";
	public final String PAGINA_SIMULADOR_2D_MOSTRA = "mostraSimulador2DModelagem";
	public final String PAGINA_MONITOR = "monitorModelagem";
	public final String PAGINA_GRAFICO_3D_MOSTRA = "showGrafico3D";
	
	//vars de arquivos
    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 1;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    private boolean messenger = false;
    private String valorDaMensagem="";
    
	private Date dataCriacao;
	
	private final static String OPCAO_MODELAGEM = "Conteudo do Arquivo de Modelagem";
	private final static String OPCAO_REGRAS = "Regras do Arquivo de Modelagem";
	private final static String OPCAO_OPERADORES = "Operadores do Arquivo de Modelagem";
	private final static String OPCAO_MF = "Funcões de Pertinência do Arquivo de Modelagem";
	private final static String OPCAO_GRAFICO_MF = "Grafico das Funcões de Pertinência das Variáveis Linguísticas";
	private final static String OPCAO_SIMULADOR_2D = "Simulador 2D";
	private final static String OPCAO_MONITOR = "Monitor";
	private final static String OPCAO_LIMITES = "Universo do Discurso de Cada Variavel";
	private final static String OPCAO_SUPERFICIE = "Superficie Gerada Pela Modelagem";	
	private final static String OPCAO_GRAFICO3D = "Grafico 3D";	
	
	
	private String opcaoDescricaoSelecionada = "Detalhar Arquivo";
	private String opcaoMostraModelagem = "Informações Extras";
    
	private String conteudoTextArea = "Nenhuma opção selecionada.";
	
	private boolean isTexto = true;

	private SelectOneDataModel<String> comboModelagemFuzzy;
	/** Opcoes possiveis para o combobox do campo Finalidade.	 */
	public List<String> tiposDeFlag = new ArrayList<String>(2);
	
	
	// Variaveis de grafico
	//variaveis possiveis da modelagem
	private SelectOneDataModel<Type> comboVariaveisGraficoMF;
	private boolean exibindoGrafico = false;
	
	
	//representa os datasets(conjunto de funcoes de um grafico) do grafico de MF.
	private Plot plotMF;
	private String variavelGraficoMF = "DemandaMaxT";
	//=====
	
	//representa o grafico do simulador 2d.
	//IMPORTANTE: SEMPRE GERAR GETS E SETS DO PLOT
	private Plot plotSimulador2D;
	private SelectOneDataModel<VariavelModelagemView> comboVariaveisInput;
	private SelectOneDataModel<VariavelModelagemView> comboVariaveisOutput;
	private VariavelModelagemView variavelInputCorrente;
	private VariavelModelagemView variavelOutputCorrente;
	private boolean simuladorExecutado=false;

	//variaveis de input e de output
	private String paginaAtual;
	private DataModel variaveisInput;
	private DataModel variaveisOutput;
	
	//variaveis de Monitor
	//IMPORTANTE: SEMPRE GERAR GETS E SETS DO PLOT
	private Plot plotMonitor;
	private DataModel regrasViewModelagem;
	private MotorInferencia motorInferenciaMonitor;
	//=====
	
	//variaveis do grafico 3d
	private VariavelModelagemView variavelInput1CorrenteGrafico3D;
	private VariavelModelagemView variavelInput2CorrenteGrafico3D;
	private String dataSimulador3D;
	private int simulador3DNumSamples=40;
	
	public boolean extensao=false;
	
	public ModelagemFuzzyActions() {
		try {
			modelagemFuzzyService = FabricaDeAppService.getAppService(ModelagemFuzzyAppService.class);
			
			tiposDeFlag.add("Gerar PMP");
			tiposDeFlag.add("Avaliar PMP");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		isTexto = true;
		paginaAtual=null;
	}

	/**
	 * Volta para a página de lista de ModelagemFuzzy
	 * 
	 * @return String
	 */
	public String cancela() {
		
		listaDeModelagemFuzzys = null;
		opcaoDescricaoSelecionada = "Detalhar Arquivo";
		opcaoMostraModelagem = "Informações Extras";
		conteudoTextArea = "Nenhuma opção selecionada.";
		isTexto = true;
		  comboModelagemFuzzy =null;
		  variavelGraficoMF =null;
		  plotMF=null;
		  exibindoGrafico=false;
		
		comboVariaveisInput=null;  
		comboVariaveisOutput=null;
		variaveisInput=null;
		variaveisOutput=null;
		variavelInputCorrente=null;
		variavelOutputCorrente=null;
		variavelInput1CorrenteGrafico3D=null;
		variavelInput2CorrenteGrafico3D=null;
		paginaAtual=null;
		simuladorExecutado=false;
		dataSimulador3D=null;
		motorInferenciaMonitor=null;
		return "listModelagemFuzzy";
	}

	/**
	 * Insere na requisicao um novo ModelagemFuzzy
	 * Retorna "newModelagemFuzzy"
	 * Prepara para utilizar a pagina de inclusao(new)
	 * @return
	 */
	public String preparaInclusao() {	
		
		modelagemFuzzyCorrente = new ModelagemFuzzy();
		comboModelagemFuzzy = null;
		return "newModelagemFuzzy";
		
	}

    /**
     * Este método é responsável pelo upload de arquivos, perceba que este
     * metodo esta ligado a um fileUploadListener, ou seja, a um listener de aplicaçao,
     * por isso nao foi necessario passar um atributo do tipo File, pois o proprio
     * atributo fileUploadListener encarrega-se de receber um objeto do tipo File. 
     * 
     * chama o metodo incluiComUpload() de modelagemFuzzyService.
     * 
     * @author felipe.arruda
     * @param evento
     * 
     */
    public void enviarArquivo(UploadEvent evento) {

    	
    	UploadItem item = evento.getUploadItem();
    	
        try {        	
        	dataCriacao = new Date();
        	modelagemFuzzyCorrente.setDataCriacao(DataUtil.dateToCalendar(dataCriacao));
        	modelagemFuzzyCorrente.setNomeArquivo(item.getFileName());
        	modelagemFuzzyCorrente.setAutor(sessaoUsuarioCorrente.getUsuarioLogado().getNome());
        	modelagemFuzzyCorrente.setFinalidadeModelagem(comboModelagemFuzzy.getObjetoSelecionado());
        	modelagemFuzzyService.incluiComUpload(modelagemFuzzyCorrente, item);			
        	
			File file = new File();
	        file.setLength(item.getData().length);
	        file.setName(item.getFileName());
	        file.setData(item.getData());
	        files.add(file);
	        
	        uploadsAvailable--;
			listaDeModelagemFuzzys = null;
			messenger = true;
        	
		} catch (AplicacaoException e) {
			
			valorDaMensagem=e.getMessage();
			messenger = false;
		}
		
    }
    
    /**
     * Este metodo direciona para a pagina listModelagemFuzzy, apos a inclusao de modelagemFuzzyCorrente no
     * metodo enviarArquivo ou caso ja exista um arquivo com determinado nome.
     * 
     * @return String
     */
    public String mensagemUpload(){
    	this.clearUploadData();
    	if(messenger){
    		info("modelagemFuzzy.SUCESSO_UPLOAD");
    	}else{
    		
    		if(valorDaMensagem.indexOf("CODIGO_MODELAGEM_EXISTENTE")!=-1){
    			error("modelagemFuzzy.CODIGO_MODELAGEM_EXISTENTE");
    			return "newModelagemFuzzy";
    		}else{
    			error("modelagemFuzzy.NOME_ARQUIVO_MODELAGEM_EXISTENTE");
    			modelagemFuzzyCorrente.setId(null);
    			return "uploadModelagemFuzzy";
    		}
    	}
    	
    	return "listModelagemFuzzy";
    }

    
    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getFiles().get((Integer)object).getData());
    }
    
    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(1);
        return null;
    }
    
    /**
     * Este metodo é uma variaçao do do original para atender a necessidade de nao mostrar a imagem do arquivo 
     * quando o usuario voltar a pagina new para efetuar um novo upload
     * 
     * @return
     */
    public void clearUploadDataModificado() {
        files.clear();
        setUploadsAvailable(1);
      
    }

	/**
	 * Seleciona o objeto para edição e redireciona para a tela de edição
	 * 
	 * Insere o objeto no request
	 * 
	 * @return String
	 */
	public String preparaAlteracao() {
		
		modelagemFuzzyCorrente = (ModelagemFuzzy) listaDeModelagemFuzzys.getRowData();
		comboModelagemFuzzy = null;

		comboModelagemFuzzy = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tiposDeFlag, modelagemFuzzyCorrente.getFinalidadeModelagem());
		
		
		return "editModelagemFuzzy";
	}

	/**
	 * Executa a regra de negócio de alteração de um ModelagemFuzzy
	 * 
	 * Atualiza a lista de ModelagemFuzzy e a insere no request
	 * 
	 * @return String
	 */
	public String altera() {
		
		modelagemFuzzyService.altera(modelagemFuzzyCorrente);
		info("modelagemFuzzy.SUCESSO_ALTERACAO");
		
		listaDeModelagemFuzzys = null;

		return "listModelagemFuzzy";
		
	}
	
	
	/**
	 * Seleciona o objeto para exclusao 
	 * 
	 * Insere o objeto no request
	 * 
	 */
	public void preparaExclusao() {
		
		modelagemFuzzyCorrente = (ModelagemFuzzy) listaDeModelagemFuzzys.getRowData();
	
	}

	
	/**
	 * Executa a regra de negócio de exclusão de um recurso
	 * 
	 * @return String
	 */
	public String exclui() {
		
		try {
			modelagemFuzzyService.exclui(modelagemFuzzyCorrente);
			listaDeModelagemFuzzys = null;
			info("modelagemFuzzy.SUCESSO_EXCLUSAO");
		} catch (AplicacaoException e) {
			error(e.getMessage());
		}
		
		return "listModelagemFuzzy";
	}


	/**
	 * Exibe um objeto modelagemFuzzy, e leva para a tela onde tem as opcoes de 
	 * Mostrar informacoes diversas sobre a modelagem.
	 * 
	 * Redireciona para a página show
	 * 
	 * @return String
	 */
	public String mostra() {

		modelagemFuzzyCorrente = (ModelagemFuzzy) listaDeModelagemFuzzys.getRowData();
		
		try {
			modelagemFuzzyCorrente = modelagemFuzzyService.recuperaModelagemFuzzyComSpecification(modelagemFuzzyCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return "listModelagemFuzzy";
		}
		
		opcaoMostraModelagem = "Informações Extras";
		conteudoTextArea = "Nenhuma opção selecionada.";
		isTexto = true;
		
		return "showModelagemFuzzy";
	}
	
	/**
	 * Exibe informacoes em um TextArea da pagina show.
	 * Dependendo do valor de opcaoMostraModelagem ele mostra um valor diferente no TextArea, e se 
	 * for igual OPCAO_MF troca o TextArea pelo grafico.
	 * @return String
	 * @author felipe.arruda
	 */
	public String mostraDados() {
		
		if(opcaoMostraModelagem.equals(OPCAO_MODELAGEM)){
			opcaoDescricaoSelecionada = "Detalhar Modelagem";
			conteudoTextArea = modelagemFuzzyService.imprimeModelagem(modelagemFuzzyCorrente);
			isTexto = true;
			
		}
		if(opcaoMostraModelagem.equals(OPCAO_REGRAS)){
			opcaoDescricaoSelecionada = "Detalhar Regras";
			conteudoTextArea = modelagemFuzzyService.imprimeRegras(modelagemFuzzyCorrente);
			isTexto = true;
			
		}
		if(opcaoMostraModelagem.equals(OPCAO_OPERADORES)){
			opcaoDescricaoSelecionada = "Detalhar Operadores";
			conteudoTextArea = modelagemFuzzyService.imprimeOperadores(modelagemFuzzyCorrente);
			isTexto = true;
			
		}
		if(opcaoMostraModelagem.equals(OPCAO_LIMITES)){
			opcaoDescricaoSelecionada = "Detalhar Limites";
			conteudoTextArea = modelagemFuzzyService.imprimeLimites(modelagemFuzzyCorrente);			  
			isTexto = true;
			
		}
		if(opcaoMostraModelagem.equals(OPCAO_MF)){
			opcaoDescricaoSelecionada = "Detalhar MF";
			conteudoTextArea = modelagemFuzzyService.imprimeMF(modelagemFuzzyCorrente);
			isTexto = true;
			
		}
		if(opcaoMostraModelagem.equals(OPCAO_GRAFICO_MF)){
			  conteudoTextArea = "";
			  			  
			  isTexto = false;
			  comboModelagemFuzzy =null;
			  variavelGraficoMF = modelagemFuzzyCorrente.getModelagem().getTypes()[0].getName();
			  plotMF = modelagemFuzzyService.gerarDadosGraficoMF(variavelGraficoMF, modelagemFuzzyCorrente);
			  
			  return PAGINA_GRAFICO_MF;
		}
		if(opcaoMostraModelagem.equals(OPCAO_SIMULADOR_2D)){
			  conteudoTextArea = "";
			  			  
			  isTexto = false;
			  	comboVariaveisInput=null;  
				comboVariaveisOutput=null;
				variaveisInput=null;
				variaveisOutput=null;
				variavelInputCorrente=null;
				variavelOutputCorrente=null;
				paginaAtual=PAGINA_SIMULADOR_2D_PREPARA;
				simuladorExecutado=false;
			  
			  return PAGINA_SIMULADOR_2D_PREPARA;
		}
		if(opcaoMostraModelagem.equals(OPCAO_MONITOR)){
			  conteudoTextArea = "";
			  			  
			  isTexto = false;
				variaveisInput=null;
				variaveisOutput=null;
				motorInferenciaMonitor=null;
				regrasViewModelagem=null;
				plotMonitor=null;
				exibindoGrafico = false;
				paginaAtual=PAGINA_MONITOR;
				return PAGINA_MONITOR;
		}
		if(opcaoMostraModelagem.equals(OPCAO_GRAFICO3D)){
				//verifica se tem exatas 2 variaveis de input, se nao tiver nao permite ao usuario entrar nessa opcao.
				//a principio estou obrigando a x e y serem,respectivamente, variaveis de entrada 1 e 2 da modelagem corrente.
				ArrayList<VariavelModelagemView> listaVariaveisInput = 
				modelagemFuzzyService.recuperaListaVariavelModelagemViewDeInputPorModelagem(modelagemFuzzyCorrente);
				if (modelagemFuzzyCorrente.getModelagem().getSystemModule().getInputs().length != 2){
					error("modelagemFuzzy.VARIAVEIS_INPUT_INCORRETAS");	
					return "showModelagemFuzzy";
				}
			  conteudoTextArea = "";
			  			  
			  isTexto = false;
				exibindoGrafico = false;
				motorInferenciaMonitor=null;
				dataSimulador3D = null;

				variavelInput1CorrenteGrafico3D=listaVariaveisInput.get(0);
				variavelInput2CorrenteGrafico3D=listaVariaveisInput.get(1);
				variavelOutputCorrente=
						modelagemFuzzyService.recuperaListaVariavelModelagemViewDeOutputPorModelagem(modelagemFuzzyCorrente).get(0);
				paginaAtual=PAGINA_GRAFICO_3D_MOSTRA;
				return PAGINA_GRAFICO_3D_MOSTRA;
		}
		return "showModelagemFuzzy";
	}
	/**
	 * Exibe grafico das funcoes de pertinencia na tela.
	 * Funciona por ajax.
	 * @param event
	 */
	public void mostrarGraficoMF(ValueChangeEvent event){
		variavelGraficoMF = event.getNewValue().toString();

		plotMF = modelagemFuzzyService.gerarDadosGraficoMF(event.getNewValue().toString(), modelagemFuzzyCorrente);
		exibindoGrafico=true;
	}
	
	/**
	 * Metodo chamado quando clicado no botao "Proximo" que redireciona para a pagina em que será realizado o simulador
	 * 
	 * @return
	 */
	public String preparaSimulador2D(){
		//obriga a pegar valores novos e atualizar o datamodel de variaveis para ficarem corretos, isso é
		//retirando o que foi selecionado pelas comboboxes.
		variaveisOutput = null;
		variaveisInput = null;
		//variavel selecionada anteriormente para eixo X
		variavelInputCorrente = comboVariaveisInput.getObjetoSelecionado();
		//variavel selecionada anteriormente para eixo Y
		variavelOutputCorrente = comboVariaveisOutput.getObjetoSelecionado();
		
		return PAGINA_SIMULADOR_2D_MOSTRA;
	}
	
	/**
	 * Metodo chamado quando clicado no botao "Executar Simulador"
	 * Chamado depois que o usuario colocou valores fixos para as variaveis que nao estao sendo analizadas.
	 * Chama o metodo do service responsavel por criar um grafico(Plot) com os dados de tela passados como parametro.
	 * 
	 * @return
	 */
	public String mostraSimulador2D(){
		//variavel selecionada anteriormente para eixo X
		VariavelModelagemView eixoX = comboVariaveisInput.getObjetoSelecionado();
		//variavel selecionada anteriormente para eixo Y
		VariavelModelagemView eixoY = comboVariaveisOutput.getObjetoSelecionado();
		
		//Lista das varaiveis que sobraram depois da selecao, e tiveram seus que ter seus valores fixos.
		ArrayList<VariavelModelagemView> variaveisInputQueSobraram = ((ArrayList<VariavelModelagemView>) variaveisInput.getWrappedData());
		ArrayList<VariavelModelagemView> variaveisOutputQueSobraram = ((ArrayList<VariavelModelagemView>) variaveisOutput.getWrappedData());
		//chama o simulador passando os variaveis de input que sobraram, e as de output que sobraram, junto com as selecionadas		
		try {
			plotSimulador2D = 
				modelagemFuzzyService.gerarPlotSimulador2D(eixoX,eixoY,variaveisInputQueSobraram,variaveisOutputQueSobraram,modelagemFuzzyCorrente);
		} catch (MotorInferenciaException e) {
			error(e.getMessage());
		}
		simuladorExecutado=true;
		return PAGINA_SIMULADOR_2D_MOSTRA;
	}
	

	/**
	 * Metodo que faz com que retorne para a pagina de prepara Simulador,
	 * caso o usuario queira selecionar outra variavel para ficar nos eixos.
	 * 
	 * @return
	 */
	public String voltarSimulador2D(){
		variaveisInput=null;
		variaveisOutput=null;
		variavelInputCorrente=null;
		variavelOutputCorrente=null;
		paginaAtual=PAGINA_SIMULADOR_2D_PREPARA;
		simuladorExecutado=false;
		return PAGINA_SIMULADOR_2D_PREPARA;
	}
	
	/**
	 * Criar um grafico 3d,
	 * Atualmente não está usando as variaveis de input, está selecionando na mao, portanto funciona apenas para o nosso sistema.
	 * @return
	 */
	public String mostraGrafico3D(){

		
		//variavel selecionada para eixo X
		VariavelModelagemView eixoX = variavelInput1CorrenteGrafico3D;
		//variavel selecionada para eixo Y
		VariavelModelagemView eixoY = variavelInput2CorrenteGrafico3D;
		
		try {
			dataSimulador3D = modelagemFuzzyService.gerarDadosGrafico3D(modelagemFuzzyCorrente);	

			exibindoGrafico = true;
		} catch (MotorInferenciaException e) {
			exibindoGrafico = false;
			error(e.getMessage());
		}
		return PAGINA_GRAFICO_3D_MOSTRA;
	}

	/**
	 * Metodo chamado quando clicado no botao "Mostrar Monitor"
	 * Chamado depois que o usuario colocou valores para as variaveis de input.
	 * 
	 * @return
	 */
	public String mostraMonitor(){
		
		motorInferenciaMonitor = new MotorInferencia();
		//popula os novos valores das variaveis de entrada.
		double[] inputs= new double[((ArrayList<VariavelModelagemView>) variaveisInput.getWrappedData()).size()];
		for (int i = 0; i < ((ArrayList<VariavelModelagemView>) variaveisInput.getWrappedData()).size(); i++) {
			VariavelModelagemView variavelIn = ((ArrayList<VariavelModelagemView>) variaveisInput.getWrappedData()).get(i);
			inputs[i] = variavelIn.getValor();
		}
		//popula os novos valores das outputs pegando os valores da execucao do motor de inferencia para as entradas acima.
		double[] outputs=null;
		try {
			outputs = motorInferenciaMonitor.executaMotorDeInferencia(inputs, modelagemFuzzyCorrente.getNomeArquivo());
		} catch (MotorInferenciaException e) {
			error(e.getMessage());
			return PAGINA_MONITOR;
		}

		//pega a lista das variaveis de output que serao atualizadas(todas).
		ArrayList<VariavelModelagemView> variaveisOutputParaAtualizar = ((ArrayList<VariavelModelagemView>) variaveisOutput.getWrappedData());
		
		//atualiza o arraylist variaveisOutputParaAtualizar com os valores do motor de inferencia
		for (int i = 0; i < outputs.length; i++) {
			//pega a variavel de output do datamodel que pelo indice i
			VariavelModelagemView variavelOut = variaveisOutputParaAtualizar.get(i);
			//altera o valor da mesma para o da execucao do motor de inferencia.
			variavelOut.setValor(outputs[i]);
		}
		//atualiza o datamodel variaveisOutput para ficar com os valores atualizados, pegando a nova lista de outputs
		variaveisOutput = new ListDataModel(variaveisOutputParaAtualizar);

		//gera o grafico do conjunto fuzzy resultante
		plotMonitor = 
			modelagemFuzzyService.gerarDadosGraficoConjuntoFuzzyResultante(motorInferenciaMonitor);

		exibindoGrafico = true;
		//coloca como null para forcar a atualizacao da mesma.
		regrasViewModelagem=null;

		return PAGINA_MONITOR;
	}
	
	/**
	 * Método que cria a Combobox que lista as Modelagens Fuzzy cadastradas no sistema.
	 * 
	 * @author walanem 
	 */
	public SelectOneDataModel<String> getComboModelagemFuzzy() {
		
		if (comboModelagemFuzzy == null){
				
				comboModelagemFuzzy = SelectOneDataModel.criaSemTextoInicial(tiposDeFlag);
		}
		return comboModelagemFuzzy;
	}

	public void setComboModelagemFuzzy(SelectOneDataModel<String> comboModelagemFuzzy) {
		this.comboModelagemFuzzy = comboModelagemFuzzy;
	}

	/**
	 * Método que cria a Combobox que lista as variaveis(Type) da Modelagens Fuzzy selecionada.
	 * 
	 * @author felipe.arruda 
	 */
	public SelectOneDataModel<Type> getComboVariaveisGraficoMF() {
		
		if (comboModelagemFuzzy == null){
			ArrayList<Type> tipos = new ArrayList<Type>();
			for (Type tipo : modelagemFuzzyCorrente.getModelagem().getTypes()){
				tipos.add(tipo);
			}
			comboVariaveisGraficoMF = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tipos, tipos.get(0));
			variavelGraficoMF = comboVariaveisGraficoMF.getObjetoSelecionado().toString();
			plotMF = modelagemFuzzyService.gerarDadosGraficoMF(variavelGraficoMF, modelagemFuzzyCorrente);
		}
		return comboVariaveisGraficoMF;
	}

	public void setComboVariaveisGraficoMF(SelectOneDataModel<Type> comboVariaveisGraficoMF) {
		this.comboVariaveisGraficoMF = comboVariaveisGraficoMF;
	}

	/**
	 * Método que cria a combobox que lista as variaveis(VariavelModelagemView) de input da modelagem Fuzzy selecionada.
	 * @return
	 */
    public SelectOneDataModel<VariavelModelagemView> getComboVariaveisInput() {
		if (comboVariaveisInput == null){

			ArrayList<VariavelModelagemView> inputvars = 
				modelagemFuzzyService.recuperaListaVariavelModelagemViewDeInputPorModelagem(modelagemFuzzyCorrente);			
			
			comboVariaveisInput = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(inputvars, inputvars.get(0));			
		}
		return comboVariaveisInput;
	}

	public void setComboVariaveisInput(
			SelectOneDataModel<VariavelModelagemView> comboVariaveisInput) {
		this.comboVariaveisInput = comboVariaveisInput;
	}

	/**
	 * Método que cria a combobox que lista as variaveis(VariavelModelagemView) de output da modelagem Fuzzy selecionada.
	 * @return
	 */
	public SelectOneDataModel<VariavelModelagemView> getComboVariaveisOutput() {
		if (comboVariaveisOutput == null){

			ArrayList<VariavelModelagemView> outputvars = 
				modelagemFuzzyService.recuperaListaVariavelModelagemViewDeOutputPorModelagem(modelagemFuzzyCorrente);	
			
			comboVariaveisOutput = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(outputvars, outputvars.get(0));	
		}
		return comboVariaveisOutput;
	}

	public void setComboVariaveisOutput(
			SelectOneDataModel<VariavelModelagemView> comboVariaveisOutput) {
		this.comboVariaveisOutput = comboVariaveisOutput;
	}
	/* 
	 * ***********  Gets e Sets *****************
	 *
	 */
	


	public int getSize() {
        if (getFiles().size()>0){
            return getFiles().size();
        }else 
        {
            return 0;
        }
    }
    
    public long getTimeStamp(){
        return System.currentTimeMillis();
    }

	
	public ModelagemFuzzy getModelagemFuzzyCorrente() {
		return modelagemFuzzyCorrente;
	}

	public void setModelagemFuzzyCorrente(ModelagemFuzzy modelagemFuzzyCorrente) {
		this.modelagemFuzzyCorrente = modelagemFuzzyCorrente;
	}
	
	/**
	 * Obtem Data Model listaDeModelagemFuzzys com lista atraves de ModelagemFuzzyAppService
	 * @return
	 */
	public DataModel getListaDeModelagemFuzzys() {
		if (listaDeModelagemFuzzys == null) {
			listaDeModelagemFuzzys = new ListDataModel(modelagemFuzzyService.recuperaListaPaginadaDeModelagemFuzzys());
		}
		return listaDeModelagemFuzzys;
	}

	public void setListaDeModelagemFuzzys(DataModel listaDeModelagemFuzzys) {
		this.listaDeModelagemFuzzys = listaDeModelagemFuzzys;
	}



	/**
	 * @return the files
	 */
	public ArrayList<File> getFiles() {
		return files;
	}


	/**
	 * @param files the files to set
	 */
	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}


	/**
	 * @return the uploadsAvailable
	 */
	public int getUploadsAvailable() {
		return uploadsAvailable;
	}


	/**
	 * @param uploadsAvailable the uploadsAvailable to set
	 */
	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}


	/**
	 * @return the autoUpload
	 */
	public boolean isAutoUpload() {
		return autoUpload;
	}


	/**
	 * @param autoUpload the autoUpload to set
	 */
	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}


	/**
	 * @return the useFlash
	 */
	public boolean isUseFlash() {
		return useFlash;
	}


	/**
	 * @param useFlash the useFlash to set
	 */
	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the modelagemFuzzyService
	 */
	public static ModelagemFuzzyAppService getModelagemFuzzyService() {
		return modelagemFuzzyService;
	}

	/**
	 * @param modelagemFuzzyService the modelagemFuzzyService to set
	 */
	public static void setModelagemFuzzyService(
			ModelagemFuzzyAppService modelagemFuzzyService) {
		ModelagemFuzzyActions.modelagemFuzzyService = modelagemFuzzyService;
	}

	/**
	 * @return the messenger
	 */
	public boolean isMessenger() {
		return messenger;
	}

	/**
	 * @param messenger the messenger to set
	 */
	public void setMessenger(boolean messenger) {
		this.messenger = messenger;
	}


	/**
	 * @return the opcaoMostraModelagem
	 */
	public String getOpcaoMostraModelagem() {
		return opcaoMostraModelagem;
	}

	/**
	 * @param opcaoMostraModelagem the opcaoMostraModelagem to set
	 */
	public void setOpcaoMostraModelagem(String opcaoMostraModelagem) {
		this.opcaoMostraModelagem = opcaoMostraModelagem;
	}

	/**
	 * @return the opcaoModelagem
	 */
	public String getOpcaoModelagem() {
		return OPCAO_MODELAGEM;
	}

	/**
	 * @return the opcaoRegras
	 */
	public String getOpcaoRegras() {
		return OPCAO_REGRAS;
	}

	/**
	 * @return the opcaoOperadores
	 */
	public String getOpcaoOperadores() {
		return OPCAO_OPERADORES;
	}

	/**
	 * @return the opcaoMf
	 */
	public String getOpcaoMf() {
		return OPCAO_MF;
	}
	/**
	 * @return the OpcaoGraficoMf
	 */
	public String getOpcaoGraficoMf() {
		return OPCAO_GRAFICO_MF;
	}

	/**
	 * @return the opcaoLimites
	 */
	public String getOpcaoLimites() {
		return OPCAO_LIMITES;
	}

	/**
	 * @return the opcaoSuperficie
	 */
	public String getOpcaoSuperficie() {
		return OPCAO_SUPERFICIE;
	}

	/**
	 * @return the conteudoTextArea
	 */
	public String getConteudoTextArea() {
		return conteudoTextArea;
	}

	/**
	 * @param conteudoTextArea the conteudoTextArea to set
	 */
	public void setConteudoTextArea(String conteudoTextArea) {
		this.conteudoTextArea = conteudoTextArea;
	}

	/**
	 * @return the getIsTexto
	 */
	public boolean getIsTexto() {
		return isTexto;
	}

	/**
	 * @param isTexto the isTexto to set
	 */
	public void setIsTexto(boolean isTexto) {
		this.isTexto = isTexto;
	}
	
	public boolean getExibindoGrafico() {
		return exibindoGrafico;
	}

	public void setExibindoGrafico(boolean exibindoGrafico) {
		this.exibindoGrafico = exibindoGrafico;
	}


	public String getVariavelGraficoMF() {
		return variavelGraficoMF;
	}

	public void setVariavelGraficoMF(String variavelGraficoMF) {
		this.variavelGraficoMF = variavelGraficoMF;
	}

	public Plot getPlotMF() {
		return plotMF;
	}

	public void setPlotMF(Plot plotMF) {
		this.plotMF = plotMF;
	}

	public Plot getPlotSimulador2D() {
		return plotSimulador2D;
	}

	public void setPlotSimulador2D(Plot plotSimulador2D) {
		this.plotSimulador2D = plotSimulador2D;
	}


	public Plot getPlotMonitor() {
		return plotMonitor;
	}

	public void setPlotMonitor(Plot plotMonitor) {
		this.plotMonitor = plotMonitor;
	}

	public DataModel getVariaveisInput() {

		if (variaveisInput == null && modelagemFuzzyCorrente!=null) {
			
			ArrayList<VariavelModelagemView> listaVariaveisInput = 
				modelagemFuzzyService.recuperaListaVariavelModelagemViewDeInputPorModelagem(modelagemFuzzyCorrente);
			
			if(paginaAtual==PAGINA_SIMULADOR_2D_PREPARA){
				VariavelModelagemView variavelInputSelecionada = comboVariaveisInput.getObjetoSelecionado();	
				ArrayList<Integer> indicesParaDelecao=new ArrayList<Integer>();
				for(int i=0;i<listaVariaveisInput.size();i++){
					VariavelModelagemView var = listaVariaveisInput.get(i);
					
					if(var.getNome().equals(variavelInputSelecionada.getNome()))
						indicesParaDelecao.add(i);
				}
				for(int i=0;i<indicesParaDelecao.size();i++){
					int temp = indicesParaDelecao.get(i);
					listaVariaveisInput.remove(temp); 				
				}
				
			}
			
			variaveisInput = new ListDataModel(listaVariaveisInput);
		}
		return variaveisInput;
	}

	public void setVariaveisInput(DataModel variaveisInput) {
		this.variaveisInput = variaveisInput;
	}

	public DataModel getVariaveisOutput() {
		if (variaveisOutput == null && modelagemFuzzyCorrente!=null) {
			ArrayList<VariavelModelagemView> listaVariaveisOutput = 
				modelagemFuzzyService.recuperaListaVariavelModelagemViewDeOutputPorModelagem(modelagemFuzzyCorrente);	

			if(paginaAtual==PAGINA_SIMULADOR_2D_PREPARA){

				VariavelModelagemView variavelOutputSelecionada = comboVariaveisOutput.getObjetoSelecionado();	
				
				ArrayList<Integer> indicesParaDelecao=new ArrayList<Integer>();
				for(int i=0;i<listaVariaveisOutput.size();i++){
					VariavelModelagemView var = listaVariaveisOutput.get(i);
					
					if(var.getNome().equals(variavelOutputSelecionada.getNome()))
						indicesParaDelecao.add(i);
				}
				for(int i=0;i<indicesParaDelecao.size();i++){
					int temp = indicesParaDelecao.get(i);
					listaVariaveisOutput.remove(temp); 
				}
			}
			variaveisOutput = new ListDataModel(listaVariaveisOutput);
		}
		return variaveisOutput;
	}

	public void setVariaveisOutput(DataModel variaveisOutput) {
		this.variaveisOutput = variaveisOutput;
	}
	
	/**
	 * Recupera as informacoes de grau de ativacao de cada regra dada uma modelagem.
	 * @return
	 */
	public DataModel getRegrasViewModelagem() {
		if (regrasViewModelagem == null && modelagemFuzzyCorrente!=null && motorInferenciaMonitor!=null) {

			ArrayList<RegraModelagemView> listaRegrasViewModelagem = 
				modelagemFuzzyService.recuperaListaRegraModelagemViewPorModelagemPorMotorInferencia(modelagemFuzzyCorrente,motorInferenciaMonitor);	
			
			regrasViewModelagem = new ListDataModel(listaRegrasViewModelagem);
		}
		return regrasViewModelagem;
	}

	public void setRegrasViewModelagem(DataModel regrasViewModelagem) {
		this.regrasViewModelagem = regrasViewModelagem;
	}

	public VariavelModelagemView getVariavelInputCorrente() {
		return variavelInputCorrente;
	}

	public void setVariavelInputCorrente(VariavelModelagemView variavelInputCorrente) {
		this.variavelInputCorrente = variavelInputCorrente;
	}

	public VariavelModelagemView getVariavelOutputCorrente() {
		return variavelOutputCorrente;
	}

	public void setVariavelOutputCorrente(
			VariavelModelagemView variavelOutputCorrente) {
		this.variavelOutputCorrente = variavelOutputCorrente;
	}

	public boolean isSimuladorExecutado() {
		return simuladorExecutado;
	}

	public void setSimuladorExecutado(boolean simuladorExecutado) {
		this.simuladorExecutado = simuladorExecutado;
	}

	public  String getOpcaoSimulador2d() {
		return OPCAO_SIMULADOR_2D;
	}

	public String getOpcaoMonitor() {
		return OPCAO_MONITOR;
	}

	public String getDataSimulador3D() {
		return dataSimulador3D;
	}

	public void setDataSimulador3D(String dataSimulador3D) {
		this.dataSimulador3D = dataSimulador3D;
	}

	public int getSimulador3DNumSamples() {
		return simulador3DNumSamples;
	}

	public void setSimulador3DNumSamples(int simulador3dNumSamples) {
		simulador3DNumSamples = simulador3dNumSamples;
	}

	public String getOpcaoGrafico3d() {
		return OPCAO_GRAFICO3D;
	}

	public VariavelModelagemView getVariavelInput1CorrenteGrafico3D() {
		return variavelInput1CorrenteGrafico3D;
	}

	public void setVariavelInput1CorrenteGrafico3D(
			VariavelModelagemView variavelInput1CorrenteGrafico3D) {
		this.variavelInput1CorrenteGrafico3D = variavelInput1CorrenteGrafico3D;
	}

	public VariavelModelagemView getVariavelInput2CorrenteGrafico3D() {
		return variavelInput2CorrenteGrafico3D;
	}

	public void setVariavelInput2CorrenteGrafico3D(
			VariavelModelagemView variavelInput2CorrenteGrafico3D) {
		this.variavelInput2CorrenteGrafico3D = variavelInput2CorrenteGrafico3D;
	}

	public void setOpcaoDescricaoSelecionada(String opcaoDescricaoSelecionada) {
		this.opcaoDescricaoSelecionada = opcaoDescricaoSelecionada;
	}

	public String getOpcaoDescricaoSelecionada() {
		return opcaoDescricaoSelecionada;
	}
	
}
