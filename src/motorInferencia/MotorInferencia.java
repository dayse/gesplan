 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package motorInferencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;



import exception.motorInferencia.MotorInferenciaException;

import service.anotacao.Transacional;
import util.Constantes;
import xfuzzy.lang.AggregateMemFunc;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.MemFunc;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;
import xfuzzy.lang.RulebaseCall;
import xfuzzy.lang.Specification;
import xfuzzy.lang.Type;
import xfuzzy.lang.Variable;
import xfuzzy.lang.XflParser;

/**
 * Classe responsável pela interface entre a aplicacao e a biblioteca XFuzzy
 * 
 * 
 * @author felipe.arruda
 * 
 */
public class MotorInferencia {
	public static final int NUM_VARIAVEIS_ENTRADA = 2;
	public static final String PATH_MODELAGEM_DEFAULT = "C:\\gesplan2010WorkComFuzzy\\xFuzzyGesplanConsole\\src\\xfl\\gesplan.xfl";
	/**
	 * Representa uma modelagem já carregada pelo sistema, no caso a ultima que foi carregada 
	 * por essa instancia do motor de inferencia.
	 */
	private Specification modelagemCarregada;

	public MotorInferencia() {
	}

	/**
	 * A partir da informação do nome do arquivo que contem a modelagem (*.xfl),
	 * abre o arquivo .xfl indicado. <br/>
	 * instancia a classe parser que vai armazenar a modelagem usando o método
	 * parser.parse.
	 * 
	 * Coloca a modelagemCarregada como sendo essa modelagem recem carregada.
	 * atualizando assim a variavel para ser a ultima modelagem carregada com sucesso.
	 * devolve como retorno o objeto da classe Specification que
	 * contem a modelagem.
	 * 
	 * 
	 * @param nomeArqModelagem
	 * @return
	 * @throws MotorInferenciaException
	 */
	public Specification carregarModelagem(String nomeArqModelagem)
			throws MotorInferenciaException {

		String caminhoDoArquivo = Constantes.CAMINHO_ABSOLUTO_MODELAGEM_UPLOADFILE;
		caminhoDoArquivo = caminhoDoArquivo + nomeArqModelagem;

		File file = new File(caminhoDoArquivo);
		XflParser parser = new XflParser();
		// converte o arquivo XFL para uma Specification
		Specification modelagem = parser.parse(file.getAbsolutePath());

		if (modelagem == null) {
			modelagem =null;
			file=null;
			throw new MotorInferenciaException(
					"motorInferencia.INCONSISTENCIA_MODELAGEM");
		}

		//Coloca a modelagemCarregada como sendo essa modelagem recem carregada.
		//atualizando assim a variavel para ser a ultima modelagem carregada com sucesso.
		this.modelagemCarregada = modelagem;
		
		return modelagem;
	}

	/**
	 * Verifica se já possui possui uma modelagem carregada e se o parametro de
	 * usar modelagem carregada é verdadeiro, se for o caso então utiliza a modelagemCarregada
	 * como a modelagem a ser executada.
	 * Caso não seja verdadeiro entao carrega o arquivo de modelagem fuzzy (*.xfl)
	 * Depois, independente da modelagem que foi carregada:
	 * executa o motor de
	 * inferência considerando o vetor de double fornecido como input
	 * (inputValue) </br> - obtem o conjunto fuzzy resultante </br> - executa a
	 * defuzzificação para obter o valor crisp resultante do motor de inferência
	 * </br> e devolve um vetor de double como saida </br>
	 * 
	 * @param inputValue
	 * @return
	 * @throws MotorInferenciaException
	 */
	public double[] executaMotorDeInferencia(double[] inputValue,
			String nomeArqModelagem) throws MotorInferenciaException {

		Specification modelagem;
		//se tiver alguma modelagem já carregada nesse motor de inferencia
		//e se a variavel de usarModelagemCarregada for verdadeira, e se tem o mesmo nome de arquivo.
		//usa a mesma para fazer a execucao.
		if(this.modelagemCarregada != null && 
				(this.modelagemCarregada.getFile().getName().equals(nomeArqModelagem))){
				modelagem = this.modelagemCarregada;
		}
		//caso contrario carrega arquivo de modelagem .xfl
		else{
			modelagem = carregarModelagem(nomeArqModelagem);
		}

		// executa o motor de inferência e retorna o resultado num vetor
		// result[]da classe MemFunc
		MemFunc result[] = null;
		try {
			result = modelagem.getSystemModule().fuzzyInference(inputValue);
		} catch (NullPointerException npe) {
			throw new MotorInferenciaException(
					"motorInferencia.INCONSISTENCIA_MODELAGEM");
		}
		// cria um vetor de double com o número de variáveis correspondentes ao
		// length do vetor result
		double[] saida = new double[result.length];
		// percorre o vetor de saida fazendo typecast de double para
		// AggregateMemFunc
		for (int i = 0; i < saida.length; i++) {
			double val = 0;
			// obtem o valor defuzzificado que representa essa função de
			// pertinencia
			// no caso de ser singleton devolve a propria abcissa como sendo o
			// valor defuzzificado
			if (result[i] instanceof pkg.xfl.mfunc.singleton) {
				val = ((pkg.xfl.mfunc.singleton) result[i]).get()[0];

			} else {// Obtem a funcao de pertinencia resultante da agregacao de
					// todas as regras
				// usa o metodo defuzzify para obter o valor defuzzificado desse
				// conjunto resultante
				// faz typecast de result para o tipo AggregateMemFunc
				AggregateMemFunc amf = (AggregateMemFunc) result[i];
				val = amf.defuzzify();
			}

			saida[i] = val;
		}

		return saida;
	}
	
	/**
	 * Salva um arquivo de modelagem(.xfl) no caminhoDoArquivo como path.
	 * Se já existir um arquivo de modelagem com esse nome ele joga uma exception.
	 * Se der erro na hora de salvar ele tambem joga exception.
	 * 
	 * Esse metodo é static pois nao precisa ter uma instancia de motor de inferencia para salvar um arquivo.
	 * @param caminhoDoArquivo
	 * @param data
	 * @throws MotorInferenciaException
	 */
	@Transacional
	public static void salvarArquivoModelagem(String caminhoDoArquivo, byte [] data) throws MotorInferenciaException {
    	//verifica se ja existe um arquivo com esse nome
		try {
	    	FileInputStream input;
			input = new FileInputStream(caminhoDoArquivo);
			//caso ja exista joga uma excecao ai
			throw new MotorInferenciaException("modelagem.NOME_ARQUIVO_MODELAGEM_EXISTENTE");
		} catch (FileNotFoundException e) {
			//isso quer dizer que ele pode salvar o arquivo sem sobreescrever outro.
		}
    	
		//pode salvar sem problemas, pois ainda não existe arquivo com esse nome.
    	OutputStream out;
		try {
			out = new FileOutputStream(caminhoDoArquivo);
			
			//salva, escrevendo os dados no arquivo.
    		out.write(data);
    		out.close();
		} catch (FileNotFoundException e) {
			//caso a pasta onde esta tentando salvar nao exista ele entra aqui.
			throw new MotorInferenciaException("modelagem.ERRO_CAMINHO_INCORRETO_ARQUIVO_MODELAGEM");
		} catch(IOException error){    		
			throw new MotorInferenciaException("modelagem.ERRO_ESCRITA_ARQUIVO_MODELAGEM");
    	}
	}

	/**
	 * Remove um arquivo de modelagem no caminho do arquivo passado como parametro
	 * Se nao deletar ou se o arquivo nao existir joga exception.
	 * 
	 * Esse metodo é static pois nao precisa ter uma instancia de motor de inferencia para remover um arquivo.
	 * @param caminhoDoArquivo
	 * @throws MotorInferenciaException
	 */
	@Transacional
	public static void removerArquivoModelagem(String caminhoDoArquivo) throws MotorInferenciaException {		
	    //um arquivo de modelagem que representa o suposto arquivo que sera deletado
	    File arquivoModelagem = new File(caminhoDoArquivo);

	    // ter certeza que o arquivo existe.
	    if (!arquivoModelagem.exists())
	      throw new MotorInferenciaException("modelagemFuzzy.ARQUIVO_NAO_ENCONTRADO");
	          
	    
	    // tenta deletar o arquivo
	    boolean deletou = arquivoModelagem.delete();

	    if (!deletou)
	      throw new MotorInferenciaException("modelagemFuzzy.ERRO_DELECAO_ARQUIVO");
	    System.out.println("Deletou arquivo: " + caminhoDoArquivo);
	}
	

	public Specification getModelagemCarregada() {
		return modelagemCarregada;
	}

	public void setModelagemCarregada(Specification modelagemCarregada) {
		this.modelagemCarregada = modelagemCarregada;
	}

	/**
	 * Retorna uma string que representa o arquivo de xfl 
	 * @param modelagem
	 * @return
	 */
	public static String imprimeModelagem(Specification modelagem) {
		String conteudo = modelagem.toXfl();
		return conteudo;
	}
	/**
	 * Retorna uma string que representa o as regras da modelagem 
	 * @param modelagem
	 * @return
	 */
	public static String imprimeRegras(Specification modelagem) {

		String conteudo = "";

		Rulebase[] basesDeRegras = modelagem.getRulebases();

		// para cada base de regras imprime o conjunto de suas regras
		// no nosso caso so temos uma base de regras entao basesDeRegras.length
		// = 1
		for (int i = 0; i < basesDeRegras.length; i++) {
			conteudo += "NOME DA BASE DE REGRAS: ";
			conteudo += basesDeRegras[i].getName() + "\n";
			// obtem o vetor de regras desta base de regras
			// regras = conjunto de todas as regras de uma basesDeRegras
			Rule[] regras = basesDeRegras[i].getRules();
			conteudo += "\n**** REGRAS **** \n";
			// //no nosso caso temos aproximadamente 80 regras entao
			// regras.length = 80
			for (int j = 0; j < regras.length; j++) {
				conteudo += "\t" + regras[j].toXfl() + "\n";
			}
		}
		return conteudo;
	}

	/**
	 * Retorna uma string que representa os operadores do xfl 
	 * @param modelagem
	 * @return
	 */
	public static String imprimeOperadores(Specification modelagem) {

		  String conteudo = "";
		
		  Rulebase[] basesDeRegras = modelagem.getRulebases();
		  
		  //para cada base de regras imprime o conjunto de suas regras
		  //no nosso caso so temos uma base de regras entao basesDeRegras.length = 1 
		  for(int i =0; i < basesDeRegras.length;i++){
			  conteudo += "NOME DA BASE DE REGRAS: "; 
			  conteudo += basesDeRegras[i].getName()+ "\n";
			  conteudo += "\n**** OPERADORES **** \n";
			  conteudo += basesDeRegras[i].operation.toXfl();
			  
		  }
		  return conteudo;
	}

	/**
	 * Retorna uma string que representa os limites do xfl 
	 * @param modelagem
	 * @return
	 */
	public static String imprimeLimites(Specification modelagem) {
			String conteudo = "";
		  //IMPRESSAO DO UNIVERSO DO DISCURSO DE TODAS AS VARIAVEIS LINGUISTICAS
		  Type[] variaveisLinguisticas = modelagem.getTypes();
			 
		  //executa o loop para todas as variaveis linguisticas
		  for(int varLing=0; varLing < variaveisLinguisticas.length;varLing++){
			  //pega as informacoes do universo do discurso da variavel linguistica corrente.
			  double min = variaveisLinguisticas[varLing].getUniverse().min();
			  double max = variaveisLinguisticas[varLing].getUniverse().max();
			  double card = variaveisLinguisticas[varLing].getUniverse().card();
			  
			  conteudo += "VARIÁVEL LINGUÍSTICA: "; 
			  conteudo += variaveisLinguisticas[varLing].getName();
			  conteudo += "\n**** UNIVERSO DO DISCURSO ****\n";
			  conteudo += "\t"+"- Mínimo: " + min+"\n";
			  conteudo += "\t"+"- Máximo: " + max+"\n";
			  conteudo += "\t"+"- Cardinalidade: " + card+"\n\n\n";
			  
		  }
		  return conteudo;
	}

	/**
	 * Retorna uma string que representa as funcoes de pertinencia do xfl  
	 * @param modelagem
	 * @return
	 */
	public static String imprimeMF(Specification modelagem) {

		String conteudo = "";
		  //IMPRESSAO DAS FUNCOES DE PERTINENCIA DE TODAS AS VARIAVEIS LINGUISTICAS
		  Type[] variaveisLinguisticas = modelagem.getTypes();

		  //executa o loop para todas as variaveis linguisticas
		  for(int varLing=0; varLing < variaveisLinguisticas.length;varLing++){
			  double min = variaveisLinguisticas[varLing].getUniverse().min();
			  double max = variaveisLinguisticas[varLing].getUniverse().max();
			  double card = variaveisLinguisticas[varLing].getUniverse().card();
			  LinguisticLabel[] mf = variaveisLinguisticas[varLing].getAllMembershipFunctions();
			  conteudo += "**** VARIÁVEL LINGUÍSTICA ==> "; 
			  conteudo += variaveisLinguisticas[varLing].getName()+" ****";

			  conteudo += "\n\t**** TERMOS LINGUISTICOS ****"; 
			  //mf.length devolve o número de termos linguísticos desta variável linguística
			  //type.length devolve o número de variáveis linguisticas
			  //na variável produção por exemplo teremos mf.length = 11 (ou seja, teremos 11 termos linguísticos)		  
			  for(int termosLing=0; termosLing<mf.length; termosLing++) {
				  conteudo += "\n";
				  conteudo += "\t - " + mf[termosLing].toXfl();
			  	}
			  conteudo += "\n\n\n";
			  }	 	  
		  return conteudo;
		
	}
	
	/**
	 * Retorna uma lista de Regras de uma modelagem
	 * Por padrao esta pegando sempre e unicamente a base de regras(RuleBase) 0.
	 * Isso é: Pegando apenas a primeira base de regras do sistema.
	 * 
	 * @param modelagem
	 * @return
	 */
	public static ArrayList<Rule> recuperaListaRegras(Specification modelagem){
		ArrayList<Rule> regras = new ArrayList<Rule>();
		for (Rule rule : modelagem.getSystemModule().getRulebaseCalls()[0].getRulebase().getRules()){
			regras.add(rule);
		}
		return regras;
	}
	
	/**
	 * Retorna uma lista de Degree da modelagem carregada
	 * Por padrao esta pegando sempre e unicamente a base de regras(RuleBase) 0.
	 * Isso é: Pegando apenas a primeira base de regras do sistema.
	 * 
	 * @param modelagem
	 * @return
	 */
	public ArrayList<Double> recuperaListaDegree(){
		ArrayList<Double> degree = new ArrayList<Double>();
		if(this.modelagemCarregada==null)
			return null;
		
		double [] degreesEmVec = this.modelagemCarregada.getSystemModule().getRulebaseCalls()[0].getDegree();
		if(degreesEmVec== null){
			return null;
		}
		for (Double valor : degreesEmVec){
			degree.add(valor);
		}
		return degree;
	}

	/**
	 * Retorna uma lista de Variables de input de uma modelagem
	 * @param modelagem
	 * @return
	 */
	public static ArrayList<Variable> recuperaListaVariaveisInput(Specification modelagem){
		ArrayList<Variable> variaveis = new ArrayList<Variable>();
		for (Variable variable : modelagem.getSystemModule().getInputs()){
			variaveis.add(variable);
		}
		return variaveis;
	}
	
	/**
	 * Retorna uma lista de Variables de output de uma modelagem
	 * @param modelagem
	 * @return
	 */
	public static ArrayList<Variable> recuperaListaVariaveisOutput(Specification modelagem){
		ArrayList<Variable> variaveis = new ArrayList<Variable>();
		for (Variable variable : modelagem.getSystemModule().getOutputs()){
			variaveis.add(variable);
		}
		return variaveis;
	}
	
	/**
	 * Retorna uma variavel(Variable) ou null(caso nao encontre) que tenha o mesmo nome da string nomeVariavel, dada uma modelagem
	 * @param modelagem
	 * @return
	 */
	public static Variable recuperaVariavelPorNome(String nomeVariavel,Specification modelagem){
		
		for (Variable variable : modelagem.getSystemModule().getVariables()){
			if(variable.getName().equals(nomeVariavel))
				return variable;
		}
		return null;
	}

	/**
	 * Retorna o indice de uma variavel(Variable) ou -1(caso nao encontre) que tenha o mesmo nome da string nomeVariavel, dada uma modelagem
	 * Na lista de variaveis de input
	 * @param modelagem
	 * @return
	 */
	public static int recuperaIndiceVariavelInputPorNome(String nomeVariavel,Specification modelagem){
		
		for (int i=0;i< modelagem.getSystemModule().getInputs().length;i++){
			Variable variable = modelagem.getSystemModule().getInputs()[i];
			if(variable.getName().equals(nomeVariavel))
				return i;
		}
		return -1;
	}
	/**
	 * Retorna o indice de uma variavel(Variable) ou -1(caso nao encontre) que tenha o mesmo nome da string nomeVariavel, dada uma modelagem
	 * Na lista de variaveis de output
	 * @param modelagem
	 * @return
	 */
	public static int recuperaIndiceVariavelOutputPorNome(String nomeVariavel,Specification modelagem){
		
		for (int i=0;i< modelagem.getSystemModule().getOutputs().length;i++){
			Variable variable = modelagem.getSystemModule().getOutputs()[i];
			if(variable.getName().equals(nomeVariavel))
				return i;
		}
		return -1;
	}
	
	/**
	 * recupera a primeira rulebasecall da modelagem carregada
	 * @return
	 */
	public RulebaseCall recuperaPrimeiraRuleBaseCall(){
		
		if(this.modelagemCarregada==null)
			return null;
		
		return this.modelagemCarregada.getSystemModule().getRulebaseCalls()[0];
	}
}
