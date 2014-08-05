 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package filtro;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import actions.UsuarioActions;
import actions.controle.SessaoDoUsuario;

public class Filtro implements Filter {
	
	public void init(FilterConfig filterConfig) {
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {		
        
		// 	  Recupera a sessao HTTP. Esta classe (HTTPSession) prove meios de identificar um usuario em varias requisicoes 
		// de paginas realizadas no sistema. 
		//    O responsavel por criar a sessao entre o cliente e o servidor HTTP é o servlet, padrao do sistema 
		// (no nosso caso, o Faces Servlet). 
		//    Esta Sessao persiste por um tempo limitado por um Timeout e geralmente está associada a 1 usuário que pode
		// visitar o site varias vezes. 
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpSession sessao = request.getSession();
		
		//Recupera o Managed Bean com escopo de SESSAO, 'SessaoDoUsuario'
		UsuarioActions usuarioActions = (UsuarioActions)sessao.getAttribute("usuarioActions");   
		SessaoDoUsuario sessaoDoUsuario = (SessaoDoUsuario)sessao.getAttribute("sessaoDoUsuario");   
	
		// Se o ManagedBean ainda está na Sessão e o usuário é um Usuário existente e cadastrado...
		
		if(sessaoDoUsuario != null && sessaoDoUsuario.getUsuarioLogado().getId() != null) {
			
			chain.doFilter(req, res);		// ... A filtragem do usuario é realizada com sucesso!
			
		} else {
			
			HttpServletRequest requisicao = (HttpServletRequest)req;
			HttpServletResponse response = (HttpServletResponse)res;
			
			// Caso contrário, o usuário é redirecionado para a Tela de Login.
			final String URLLogin = "http://" + requisicao.getServerName() + ":" + requisicao.getLocalPort() + "/" + requisicao.getContextPath() + "/login.faces";
			response.sendRedirect(response.encodeRedirectURL(URLLogin));
		}
	}

	public void destroy() {
		
	}
}
