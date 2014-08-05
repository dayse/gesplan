 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions.controle;

import java.util.HashSet;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import modelo.Usuario;

import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.Constantes;
import actions.BaseActions;

public class AutenticadorActions extends BaseActions {
	
	// Services
	private UsuarioAppService usuarioAppService;
	
	// Variaveis de tela
	private String login;
	private String senha;
	
	public AutenticadorActions() {
		
		try {
			usuarioAppService = FabricaDeAppService.getAppService(UsuarioAppService.class);
		} catch (Exception e) {
		}
	}
	
	public String autenticar(){
		
		Usuario usuario;
		
		try {
			usuario = usuarioAppService.recuperaPorLoginESenha(login, senha);
			
			System.out.println(" >>>> USUARIO CADASTRADO = " + usuario.getNome());
			
		} catch (AplicacaoException ex){
			error(ex.getMessage());
			return Constantes.PAGINA_LOGIN;
		}
		
		sessaoUsuarioCorrente.setUsuarioLogado(usuario);
		
		return Constantes.PAGINA_HOME;
	}
	
	public String logout(){
		
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		 
		sessao.removeAttribute("sessaoDoUsuario");
		
		if (sessao != null) {						// Ou seja, se ela ainda existe... 
		    sessao.invalidate();					// ... é invalidada!
		}
		
		return Constantes.PAGINA_LOGIN;
	}

	
	
	// ================================== Métodos get() e set() ================================== //

	public String getLogin() {
		return login;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
