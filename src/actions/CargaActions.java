 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;



import service.CargaAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.Constantes;
import util.SelectOneDataModel;



/**
 * Action responsavel por realizar a carga do sistema,
 * Atenção ao fato de que esse action não requer autenticação para seus metodos!
 * Ou seja pode ser acessada sem passar pelo filtro de login.
 *  
 * */
public class CargaActions extends BaseActions {

	// Services
	private static CargaAppService cargaService;
	

	private static String PAGINA_CARGA = "cargabd";
	
	private static String SENHA_CARGA = Constantes.SENHA_CARGABD;

	private static String OPCAO_BASICA = "Basica"; 
	private static String OPCAO_ESTUDO = "Estudo de Caso"; 
	private static String OPCAO_ALTERACAO_ESTUDO = "Alteracoes no Estudo de Caso"; 

	/** Opcoes possiveis para o combobox do campo de Carga do Sistema
	 *  */
	public List<String> tiposDeCarga = new ArrayList<String>();
	private SelectOneDataModel<String> comboTiposDeCarga;
	
	//variaveis de tela
	private String senha;
	private String descCarga;
	
	
	public CargaActions() throws Exception {
		
		try {
			cargaService = FabricaDeAppService.getAppService(CargaAppService.class);
		} catch (Exception e) {
			throw e;
		}

		//add a opcao de alteracao no estudo de caso nos tipos de carga
		tiposDeCarga.add(OPCAO_ALTERACAO_ESTUDO);
		//add a opcao do estudo de caso nos tipos de carga
		tiposDeCarga.add(OPCAO_ESTUDO);
		//adciona a opcao basica nos tipos de carga.
		tiposDeCarga.add(OPCAO_BASICA);
		descCarga=null;
		comboTiposDeCarga=null;
	}
	/**
	 * Metodo chamado por ajax, para alterar a descrição sempre que alterar a combobox
	 */
	public void alteraDescAjax(ValueChangeEvent event){
		
		String opcao = event.getNewValue().toString();
		alteraDesc(opcao);
	}

	/**
	 * Metodo chamado para alterar a descrição
	 */
	public void alteraDesc(String opcao){		
		
		if(opcao == null || opcao.equals(OPCAO_ALTERACAO_ESTUDO)){
			descCarga = "Insere tipos de usuarios, usuario administrador e parametros." +
						"<br />Insere o estudo de casos." +
						"<br /> Realiza as alterações posteriores.";
		}
		else if(opcao.equals(OPCAO_BASICA)){
			descCarga = "Insere tipos de usuarios, usuario administrador e parametros.";
		}
		else if(opcao.equals(OPCAO_ESTUDO)){
			descCarga = "Insere tipos de usuarios, usuario administrador e parametros." +
					"<br />E o estudo de casos.";
		}
	}
	/**
	 * Carrega a carga do sistema se a senha for igual a SENHA_CARGA
	 * 
	 * @return
	 */
	public String executarCarga(){	
		//caso a senha não bata com a SENHA_CARGA, da mensagem de erro e não faz a carga
		if(!senha.equals(SENHA_CARGA)){
			error("usuario.ERRO_LOGIN_SENHA_INEXISTENTES");
			return PAGINA_CARGA;
		}
		String opcao = comboTiposDeCarga.getObjetoSelecionado();
		System.out.println(">>>"+opcao);
		try {
			if(opcao.equals(OPCAO_BASICA)){
				cargaService.executarCargaBasica();
			}
			else if(opcao.equals(OPCAO_ESTUDO)){
				cargaService.executarCargaEstudoDeCaso();
			}
			else if(opcao.equals(OPCAO_ALTERACAO_ESTUDO)){
				cargaService.executarCargaAlteraEstudoDeCaso();
			}
			info("carga.SUCESSO_CARGA");
		} catch (AplicacaoException ex){
			error(ex.getMessage());
		}
		descCarga=null;
		return PAGINA_CARGA;
	}
	
	// ================================== Métodos get() e set() ================================== //


	public String getSenha() {
		return senha;
	}

	public String getDescCarga() {
		if(descCarga == null){
			if(comboTiposDeCarga==null){
				//supostamente so passa aqui na primeira vez que roda o sistema.
				descCarga = "Carga básica do sistema.";
			}
			else{
				String opcao = comboTiposDeCarga.getObjetoSelecionado();
				alteraDesc(opcao);
			}
			
		}
		return descCarga;
	}

	public void setDescCarga(String descCarga) {
		this.descCarga = descCarga;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public static String getSENHA_CARGA() {
		return SENHA_CARGA;
	}


	public static void setSENHA_CARGA(String sENHACARGA) {
		SENHA_CARGA = sENHACARGA;
	}

	/**
	 * Método que cria a Combobox que lista os tipos de Carga no sistema
	 * @author felipe.arruda 
	 */
	public SelectOneDataModel<String> getComboTiposDeCarga() {
		
		if (comboTiposDeCarga == null){
				
			comboTiposDeCarga = SelectOneDataModel.criaSemTextoInicial(tiposDeCarga);
		}
		return comboTiposDeCarga;
	}

	public void setComboTiposDeCarga(SelectOneDataModel<String> comboTiposDeCarga) {
		this.comboTiposDeCarga = comboTiposDeCarga;
	}

}
