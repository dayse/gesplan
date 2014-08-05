 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package reports;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Usuario;
import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import util.Constantes;


public class GeraRelatorioDeUsuarios extends HttpServlet 
{	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException 
    {		
		UsuarioAppService usuarioService = null;
    	List<Usuario> listaDeUsuarios = null;
    	String proxima="";

		try
		{	
			usuarioService = (UsuarioAppService) FabricaDeAppService
					.getAppService(UsuarioAppService.class);
			
			listaDeUsuarios = usuarioService.recuperaListaDeUsuarios();
		
		}
		catch (Exception e) 
		{	
			e.printStackTrace();
		}

		if (!listaDeUsuarios.isEmpty())
     	{	
     		
     		String opcaoDeRelatorio = (String)req.getAttribute("opcaoRelatorioEscolhido");
     		
     		req.setAttribute("lista", listaDeUsuarios);
     		
     		Map<String, String> parametros = new HashMap<String, String>();
     		parametros.put("LOGO_INT", Constantes.CAMINHO_SERVLET_LOGO_INT );
    		parametros.put("LOGO_COPPE", Constantes.CAMINHO_SERVLET_LOGO_COPPE);
     		
     		req.setAttribute("parametros", parametros);

     		
     		req.setAttribute("nomeDoRelatorio","/reports/relatorioListagemUsuarios.jasper");
     		
     		if("pdf".equals(opcaoDeRelatorio))
     		{	proxima = "/GeraRelatorioEmPDF";
     		}
     		else
     		{	proxima = "/GeraRelatorioEmHTML";
     		}
     		
     		RequestDispatcher dispatcher = req.getRequestDispatcher(proxima);
        	dispatcher.forward(req, res);
     		
     	}
     	else{
     		
     		final String URLLogin = "http://" + req.getServerName() + ":" + req.getLocalPort() + req.getContextPath() + "/login.faces";
     		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Url="+URLLogin);
     		res.sendRedirect(res.encodeRedirectURL(URLLogin));	
     		
     	  }
     }
 }
