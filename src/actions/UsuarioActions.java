 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.TipoUsuario;
import modelo.Usuario;
import service.TipoUsuarioAppService;
import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * UsuarioActions é uma classe relacionada à manipulação de tela, ou seja, a interação do ususário
 * de fato dar-se-á através de objetos do tipo UsuarioActions quando na tela de Usuario.
 * Objetos do tipo "actions" nome aqui adotado também são popularmente conhecidos como managebeans
 * em outras palavras beans gerenciáveis.
 * 
 * @author marques.araujo
 *
 */

public class UsuarioActions extends BaseActions{
	
	// Componentes de Controle
	private DataModel listaUsuarios;
	private SelectOneDataModel<TipoUsuario> comboTiposUsuario;
	
	// Variaveis de Tela
	private Usuario usuarioCorrente;
	private String confirmacaoSenha;
	private String opcaoRelatorioEscolhido;
	private boolean exclusaoUsuarioLogado;
	private boolean permitirAlterarStatus;
	
	
	// Services
	private UsuarioAppService usuarioService;
	private TipoUsuarioAppService tipoUsuarioService;
	
	
	// Paginas
	public final String PAGINA_LIST = "listUsuario";
	public final String PAGINA_NEW  = "newUsuario";
	public final String PAGINA_SHOW = "showUsuario";
	public final String PAGINA_EDIT = "editUsuario";
	

	public UsuarioActions(){
		
		try {
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			tipoUsuarioService = FabricaDeAppService.getAppService(TipoUsuarioAppService.class);
		} catch (Exception e) {
		}
		
	}
	
	public String preparaInclusao(){
		
		usuarioCorrente = new Usuario();
		
		return PAGINA_NEW;
	}
	
	public String inclui(){
		
		usuarioCorrente.setTipoUsuario(comboTiposUsuario.getObjetoSelecionado());
		
		try {
			usuarioService.inclui(usuarioCorrente, confirmacaoSenha);
		} catch (AplicacaoException ex) {
			error(ex.getMessage());
			return PAGINA_NEW;
		}
		
		info("usuario.SUCESSO_INCLUSAO");
		listaUsuarios = null;
		
		return PAGINA_LIST;
	}
	
	public String mostra(){
		
		usuarioCorrente = (Usuario) listaUsuarios.getRowData();
		comboTiposUsuario = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tipoUsuarioService.recuperaListaDeTipoUsuario(), usuarioCorrente.getTipoUsuario());
		
		try {
			usuarioCorrente = usuarioService.recuperaComPlanos(usuarioCorrente);
		} catch (ObjetoNaoEncontradoException ex){
		}
		
		return PAGINA_SHOW;
	}
	
	
	public String preparaAlteracao(){
		
		usuarioCorrente = (Usuario) listaUsuarios.getRowData();
		comboTiposUsuario = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(tipoUsuarioService.recuperaListaDeTipoUsuario(), usuarioCorrente.getTipoUsuario());
		
		permitirAlterarStatus=true;
				
		return PAGINA_EDIT;
	}
	
	
	public String altera(){
		
		try{
			usuarioCorrente.setTipoUsuario(comboTiposUsuario.getObjetoSelecionado());
			
			
			usuarioService.altera(usuarioCorrente, sessaoUsuarioCorrente.getUsuarioLogado(), confirmacaoSenha);
		} catch (AplicacaoException ex){
			error(ex.getMessage());
			listaUsuarios = null;
			return PAGINA_EDIT;
		}
		
		info("usuario.SUCESSO_ALTERACAO");
		listaUsuarios = null;
		
		return PAGINA_LIST;
	}
	
	
	public String cancelar(){
		return PAGINA_LIST;
	}
	
	
	public void preparaExclusao(){
		usuarioCorrente = (Usuario) listaUsuarios.getRowData();
	}
	
	public String exclui(){
		
		try {
			usuarioService.exclui(usuarioCorrente);
		} catch (AplicacaoException e) {
			error(e.getMessage());
			return PAGINA_LIST;
		}
		
		info("usuario.SUCESSO_EXCLUSAO");
		listaUsuarios = null;
		
		return PAGINA_LIST;
	}

	
	public void imprimirEmPdfOuHthml(){   
		 
		String url = "/GeraRelatorioDeUsuarios";   
		 
		FacesContext context = FacesContext.getCurrentInstance(); 
		List<Usuario> listaDeUsuarios = null;
		
		try { 
			
			listaDeUsuarios = usuarioService.recuperaListaDeUsuarios();
			
			if(listaDeUsuarios.isEmpty()){
					throw new AplicacaoException();
			}
			
		    ServletContext sc = (ServletContext) context.getExternalContext().getContext();   
			
		    RequestDispatcher rd = sc.getRequestDispatcher(url);   
		             
		    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();   
		    HttpServletResponse response=(HttpServletResponse)context.getExternalContext().getResponse();   
			           
		    request.setAttribute("opcaoRelatorioEscolhido",opcaoRelatorioEscolhido);
			            
		    rd.forward(request, response);   
			               
		}catch (Exception e) {   
			e.printStackTrace();
		}   
		finally{   
		    context.responseComplete();  
		}   
	}   
	 
	public void imprimir(){
		
		try{
			List<Usuario> listaDeUsuarios =  usuarioService.recuperaListaDeUsuarios();		
			
			usuarioService.gerarRelatorio(listaDeUsuarios);
			
		} catch (AplicacaoException re){
			error("usuario.USUARIOS_INEXISTENTES");
		}
				
	}
	
	// ================================== Métodos get() e set() ================================== //

	
	/**
	 * Este método retorna uma lista de ususarios.
	 * @return DataModel
	 * 
	 */
	public DataModel getListaUsuarios() {
		
		if (listaUsuarios == null){
			listaUsuarios = new ListDataModel(usuarioService.recuperaUsuariosComTipo());
		}
		
		return listaUsuarios;
	}


	public Usuario getUsuarioCorrente() {
		return usuarioCorrente;
	}

	/**
	 * Este metodo retorna os tipos de usuarios disponíveis.
	 * @return SelectOneDataModel<TipoUsuario>
	 */
	public SelectOneDataModel<TipoUsuario> getComboTiposUsuario() {
		
		if (comboTiposUsuario == null){
			comboTiposUsuario = SelectOneDataModel.criaSemTextoInicial(tipoUsuarioService.recuperaListaDeTipoUsuario());
		}
		
		return comboTiposUsuario;
	}
	
	public void setListaUsuarios(DataModel listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}


	public void setUsuarioCorrente(Usuario usuarioCorrente) {
		this.usuarioCorrente = usuarioCorrente;
	}

	public void setComboTiposUsuario(SelectOneDataModel<TipoUsuario> comboTiposUsuario) {
		this.comboTiposUsuario = comboTiposUsuario;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setExclusaoUsuarioLogado(boolean exclusaoUsuarioLogado) {
		this.exclusaoUsuarioLogado = exclusaoUsuarioLogado;
	}

	/**
	 * Este metodo identifica se o usuario esta logado a partir do metodo equals.
	 * @return boolean
	 * 
	 */
	public boolean isExclusaoUsuarioLogado() {
		return sessaoUsuarioCorrente.getUsuarioLogado().equals((Usuario) listaUsuarios.getRowData());
	}
	
	public boolean isPermitirAlterarStatus() {
		return permitirAlterarStatus;
	}

	public void setPermitirAlterarStatus(boolean permitirAlterarStatus) {
		this.permitirAlterarStatus = permitirAlterarStatus;
	}

	public String getOpcaoRelatorioEscolhido() {
		return opcaoRelatorioEscolhido;
	}

	public void setOpcaoRelatorioEscolhido(String opcaoRelatorioEscolhido) {
		this.opcaoRelatorioEscolhido = opcaoRelatorioEscolhido;
	}
	
}
