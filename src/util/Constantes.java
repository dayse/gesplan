 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

/**
 * Classe com definicao das constantes usadas nas telas que informam
 * o comprimento do campo na tela
 * 
 * @author equipe
 *
 */
public class Constantes {
	
	public static final String PAGINA_LOGIN = "login";
	public static final String PAGINA_HOME = "home";
	
	public static String SENHA_CARGABD;
	
	//-------------------- CONSTANTES DE TAMANHO EM TELAS ------------------------
	
	// --------------------- CadPlan ---------------------
	public static final int L_CADPLAN_CODPLAN = 15; 
	public static final int L_CADPLAN_DESCRPLAN = 30;
	
	// --------------------- CapacDia ---------------------
	public static final int L_CAPACDIA_CAPACPRODIARIAEMMIN = 7; 
	public static final int L_CAPACDIA_CAPACPRODIARIAEMMINMG = 7;
	
	// --------------------- CapacRec ---------------------
	public static final int L_CAPACREC_CAPACDIARIA = 8; 
	
	// --------------------- Familia ---------------------
	public static final int L_FAMILIA_CODFAMILIA = 15; 
	public static final int L_FAMILIA_DESCRFAMILIA = 30;
	public static final int L_FAMILIA_COBERTURA = 6;
	public static final int L_FAMILIA_TMUC = 5;
	public static final int L_FAMILIA_ESTQINICFAM = 7;
	
	// --------------------- DeFamPer ---------------------
	public static final int L_DEFAMPER_VENDASPROJFAM = 7; 
	public static final int L_DEFAMPER_PEDIDOSFAM = 7; 
	
	// --------------------- Modelo ---------------------
	public static final int L_MODELO_CODMODELO = 15; 
	public static final int L_MODELO_DESCRMODELO = 30;
	public static final int L_MODELO_INDICECOBERTURA = 4;
	public static final int L_MODELO_TUC = 5;
	public static final int L_MODELO_TAMLOTE = 2;
	public static final int L_MODELO_TR  = 2;
	public static final int L_MODELO_COBERTURA  = 6;
	public static final int L_MODELO_ESTQINICIALMODEL  = 7;
	public static final int L_MODELO_RECEBIMENTOPENDENTE  = 7;
	public static final int L_MODELO_ESTQEMFALTA  = 7;

	// --------------------- ModelagemFuzzy ---------------------
	public static final int L_MODELAGEMFUZZY_NOMEMODELAGEMFUZZY = 15; 
	public static final int L_MODELAGEMFUZZY_DESCRMODELAGEMFUZZY = 30;
	public static final int L_MODELAGEMFUZZY_NOMEARQUIVO = 40;
	
	// --------------------- DeModPer ---------------------
	public static final int L_DEMODPER_VENDASPROJMODEL = 7; 
	public static final int L_DEMODPER_PEDIDOSMODEL = 7; 

	//------------------  Parametros ---------------------
	public static final int L_PARAMETROS_MARGEMSEGURANCA = 4;
	public static final int L_PARAMETROS_PERCENTUALPERDA = 4;
	public static final int L_PARAMETROS_NUMINTERVALOSFIXOS = 3;

	// --------------------- PerioPM ---------------------
	public static final int L_PERIOPM_PERIODOPM = 2; 
	public static final int L_PERIOPM_DATAINICIAL = 10;
	public static final int L_PERIOPM_DATAFINAL = 10;
	public static final int L_PERIOPM_NUMDIASUTEISMATRIZ = 4;
	public static final int L_PERIOPM_NUMDIASUTEISU2 = 4;
	
	// --------------------- PerioPAP ---------------------
	public static final int L_PERIOPAP_PERIODOPAP = 2; 
	public static final int L_PERIOPAP_NUMDIASUTEISMATRIZ = 6;
    	
	// --------------------- Recurso ---------------------
	public static final int L_RECURSO_CODRECURSO = 15; 
	public static final int L_RECURSO_DESCRRECURSO = 30;
	public static final int L_RECURSO_UM = 3;
	public static final int L_RECURSO_CUSTOUNIT = 6;
	
	// --------------------- RecModel ---------------------
	public static final int L_RECMODEL_CONSUMOUNIT = 8;
	
	// --------------------- Tecido ---------------------
	public static final int L_TECIDO_CODTECIDO = 15;
	public static final int L_TECIDO_DESCRTECIDO = 30;
	public static final int L_TECIDO_UM = 3;
	public static final int L_TECIDO_CUSTOUNIT = 6;
	public static final int L_TECIDO_LEADTIMEUNIDADE2 = 2; 		        
	public static final int L_TECIDO_FATORDERENDIMENTO = 3; 	        
	public static final int L_TECIDO_PRODUCAODIARIAMAXUNIDADE2 = 8; 	
	
	// --------------------- TecModel ---------------------
	public static final int L_TECMODEL_TEMPODEFASAGEMUSOTEC = 4;      
	public static final int L_TECMODEL_CONSUMOPORLOTEMT = 9; 	      
	public static final int L_TECMODEL_CONSUMOPORLOTEKG = 9;          
	
	// --------------------- Usuario ---------------------
	public static final int L_USUARIO_NOME = 25;      				
	public static final int L_USUARIO_LOGIN = 20;      				
	public static final int L_USUARIO_SENHA = 20;      				
	
	//----------------------- CONSTANTES DE RELATÓRIOS ---------------------------
	public final static String CAMINHO_JASPERS = "/WEB-INF/relatorios/";
	public static String CAMINHO_LOGOTIPOS = "/WEB-INF/imagensRelatorio/";
	public static String CAMINHO_RELATORIOS_GERADOS;
    public static String CAMINHO_ABSOLUTO_RELATORIOS;
    public static String CAMINHO_JASPERS_SUBRELATORIOS;
    public static String CAMINHO_SERVLET_LOGO_INT = CAMINHO_LOGOTIPOS + "logoINT2.jpg";
    public static String CAMINHO_SERVLET_LOGO_COPPE = CAMINHO_LOGOTIPOS + "logoCoppe.png";
	//----------------------------------------------------------------------------

	//----------------------- CONSTANTES DE FILEUPLOAD ---------------------------
  	public final static String CAMINHO_MODELAGEM_UPLOADFILE = "/WEB-INF/modelagens/";
	public static String CAMINHO_ABSOLUTO_MODELAGEM_UPLOADFILE;

	//----------------------- CONSTANTES DE CARGA ---------------------------
  	public final static String CAMINHO_ARQUIVOS_CARGA = "/WEB-INF/carga/";
	public static String CAMINHO_ABSOLUTO_ARQUIVOS_CARGA;
	public final static String CAMINHO_ARQUIVO_USUARIOS_CARGA = CAMINHO_ARQUIVOS_CARGA + "usuarios.json";
	public static String CAMINHO_ABSOLUTO_ARQUIVO_USUARIOS_CARGA;
	//----------------------------------------------------------------------------
	
	//----------------------- CONSTANTES DO PACKAGE XFUZZY ---------------------------
  	public final static String CAMINHO_PKGXFUZZY = "/WEB-INF/classes/";

	//----------------------------------------------------------------------------
	

    
    /* ------- CAPACDIA ------- */
    
    public int getlCapacdiaCapacproddiariaemmin() {
		return L_CAPACDIA_CAPACPRODIARIAEMMIN;
	}

	public int getlCapacdiaCapacproddiariaemminmg() {
		return L_CAPACDIA_CAPACPRODIARIAEMMINMG;
	}

    /* ------- CAPACREC ------- */
	
    public int getlCapacrecCapacdiaria() {
		return L_CAPACREC_CAPACDIARIA;
	}
    /* ------- FAMILIA ------- */
    

	public int getlFamiliaCodfamilia() {
		return L_FAMILIA_CODFAMILIA;
	}

	public int getlFamiliaDescrfamilia() {
		return L_FAMILIA_DESCRFAMILIA;
	}

	public int getlFamiliaCobertura() {
		return L_FAMILIA_COBERTURA;
	}
	
	public int getlFamiliaTmuc() {
		return L_FAMILIA_TMUC;
	}

	public int getlFamiliaEstqinicfam() {
		return L_FAMILIA_ESTQINICFAM;
	}
	
	/* ------- MODELO ------- */

	public int getlModeloCodmodelo() {
		return L_MODELO_CODMODELO;
	}

	public int getlModeloDescrmodelo() {
		return L_MODELO_DESCRMODELO;
	}

	public int getlModeloIndicecobertura() {
		return L_MODELO_INDICECOBERTURA;
	}

	public int getlModeloTuc() {
		return L_MODELO_TUC;
	}

	public int getlModeloTamlote() {
		return L_MODELO_TAMLOTE;
	}

	public int getlModeloTr() {
		return L_MODELO_TR;
	}

	public int getlModeloCobertura() {
		return L_MODELO_COBERTURA;
	}

	public int getlModeloEstqinicialmodel() {
		return L_MODELO_ESTQINICIALMODEL;
	}

	public int getlModeloEstqemfalta() {
		return L_MODELO_ESTQEMFALTA;
	}

	public int getlModeloRecebimentopendente() {
		return L_MODELO_RECEBIMENTOPENDENTE;
	}

	/* ------- MODELO ------- */

	public int getlModelagemfuzzyNomeModelagemFuzzy() {
		return L_MODELAGEMFUZZY_NOMEMODELAGEMFUZZY;
	}

	public int getlModelagemfuzzyDescrModelagemFuzzy() {
		return L_MODELAGEMFUZZY_DESCRMODELAGEMFUZZY;
	}
	
	public int getlModelagemfuzzyNomeArquivo() {
		return L_MODELAGEMFUZZY_NOMEARQUIVO;
	}
	/* ------- PARAMETROS ------- */

	public int getlParametrosMargemSeguranca() {
		return L_PARAMETROS_MARGEMSEGURANCA;
	}

	public int getlParametrosPercentualperda() {
		return L_PARAMETROS_PERCENTUALPERDA;
	}

	public int getlParametrosNumintervalosfixos() {
		return L_PARAMETROS_NUMINTERVALOSFIXOS;
	}

	/* ------- PERIOPM ------- */
	/**
	 * @return the lPeriopmPeriodopm
	 */
	public int getlPeriopmPeriodopm() {
		return L_PERIOPM_PERIODOPM;
	}

	/**
	 * @return the lPeriopmDatainicial
	 */
	public  int getlPeriopmDatainicial() {
		return L_PERIOPM_DATAINICIAL;
	}

	/**
	 * @return the lPeriopmDatafinal
	 */
	public  int getlPeriopmDatafinal() {
		return L_PERIOPM_DATAFINAL;
	}

	/**
	 * @return the lPeriopmNumdiasuteismatriz
	 */
	public  int getlPeriopmNumdiasuteismatriz() {
		return L_PERIOPM_NUMDIASUTEISMATRIZ;
	}

	/**
	 * @return the lPeriopmNumdiasuteisu2
	 */
	public  int getlPeriopmNumdiasuteisu2() {
		return L_PERIOPM_NUMDIASUTEISU2;
	}


	public int getlPeriopapPeriodopap() {
		return L_PERIOPAP_PERIODOPAP;
	}

	public int getlPeriopapNumdiasuteismatriz() {
		return L_PERIOPAP_NUMDIASUTEISMATRIZ;
	}

	/* ------- RECURSO ------- */

	public int getlRecursoCodRecurso() {
		return L_RECURSO_CODRECURSO;
	}

	public int getlRecursoDescrRecurso() {
		return L_RECURSO_DESCRRECURSO;
	}

	public int getlRecursoUM() {
		return L_RECURSO_UM;
	}
	
	public int getlRecursoCustoUnit() {
		return L_RECURSO_CUSTOUNIT;
	}
	
	/* ------- RECMODEL ------- */

	public int getlRecmodelConsumoUnit() {
		return L_RECMODEL_CONSUMOUNIT;
	}

	/* ------- TECIDO ------- */
	

	public int getlTecidoCodTecido(){
		return L_TECIDO_CODTECIDO;
	}
	
	public int getlTecidoDescrTecido(){
		return L_TECIDO_DESCRTECIDO;
		
	}
	
	public int getlTecidoUM() {
		return L_TECIDO_UM;
	}
	
	
	public int getlTecidoCustoUnit() {
		return L_TECIDO_CUSTOUNIT;
	}
	
	
	public int getlTecidoLeadTimeUnidade2(){
		return L_TECIDO_LEADTIMEUNIDADE2;
		
	}
	
	
    public int getlTecidoFatorDeRendimento(){
		
		return L_TECIDO_FATORDERENDIMENTO;
	}
	
	public int getlTecidoProducaoDiariaMaximaUnidade2(){
		return L_TECIDO_PRODUCAODIARIAMAXUNIDADE2;
		
	}
	
	/* ------- TECMODEL ------- */
	
	public int getlTecModelDefasagemUsoTec(){
		return L_TECMODEL_TEMPODEFASAGEMUSOTEC;
	}
	
	public int getlTecModelConsumoPortLoteMt(){
		return L_TECMODEL_CONSUMOPORLOTEMT;
		
	}
	
	public int getlTecModelConsumoPortLoteKg(){
		return L_TECMODEL_CONSUMOPORLOTEKG;
		
	}

	/* ------- CADPLAN ------- */
	public int getlCadplanCodplan() {
		return L_CADPLAN_CODPLAN;
	}

	public int getlCadplanDescrplan() {
		return L_CADPLAN_DESCRPLAN;
	}

	
	/* ------- USUARIO ------- */
	
	public int getlUsuarioNome() {
		return L_USUARIO_NOME;
	}

	public int getlUsuarioLogin() {
		return L_USUARIO_LOGIN;
	}

	public int getlUsuarioSenha() {
		return L_USUARIO_SENHA;
	}

	/* ------- DEFAMPER ------- */

	public static int getlDeFamPerVendasProjFam() {
		return L_DEFAMPER_VENDASPROJFAM;
	}
	
	public static int getlDeFamPerPedidosFam() {
		return L_DEFAMPER_PEDIDOSFAM;
	}

	/* ------- DEMODPER ------- */

	public static int getlDemodperVendasprojmodel() {
		return L_DEMODPER_VENDASPROJMODEL;
	}

	public static int getlDemodperPedidosmodel() {
		return L_DEMODPER_PEDIDOSMODEL;
	}

}
