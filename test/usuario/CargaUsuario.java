 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package usuario;

import modelo.TipoUsuario;
import modelo.Usuario;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.TipoUsuarioAppService;
import service.UsuarioAppService;
import service.anotacao.Transacional;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * Classe usada para TESTE de tipo de usuario e usuario
 * 
 * N�O � A Classe usada na carga do sistema.(dma)
 * 
 * @author marques
 *
 */
public class CargaUsuario {
  
	// Services
	public TipoUsuarioAppService tipoUsuarioService;
	public UsuarioAppService usuarioService;
	
	
	@BeforeClass
	public void setupClass(){
		
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			tipoUsuarioService = FabricaDeAppService.getAppService(TipoUsuarioAppService.class);
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test()
	public void incluirTiposDeUsuario() {
		
		TipoUsuario tipoUsuarioAdmin = new TipoUsuario();
		TipoUsuario tipoUsuarioAluno = new TipoUsuario();
		TipoUsuario tipoUsuarioGestor = new TipoUsuario();
		TipoUsuario tipoUsuarioEngenheiro = new TipoUsuario();
		
		tipoUsuarioAdmin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
		tipoUsuarioAdmin.setDescricao("O usu�rio ADMINISTRADOR pode realizar qualquer opera��o no Sistema.");
		
		tipoUsuarioAluno.setTipoUsuario(TipoUsuario.ALUNO);
		tipoUsuarioAluno.setDescricao("O usu�rio ALUNO pode realizar apenas consultas e impress�o de relat�rios nas telas " +
				                        "relativas ao Horizonte de Planejamento (HP,Periodo PMP, Periodo PAP) e n�o acessa " +
				                        "Administra��o e Eng. Conhecimento");
		
		tipoUsuarioGestor.setTipoUsuario(TipoUsuario.GESTOR);
		tipoUsuarioGestor.setDescricao("O usu�rio GESTOR pode realizar qualquer opera��o no Sistema, por�m n�o possui acesso" +
				"as �reas de Administra��o e Engenharia de Conhecimento.");
		
		tipoUsuarioEngenheiro.setTipoUsuario(TipoUsuario.ENGENHEIRO_DE_CONHECIMENTO);
		tipoUsuarioEngenheiro.setDescricao("O usu�rio ENGENHEIRO pode realizar a parte de Logica Fuzzy (Engenharia de Conhecimento)" +
				"no Sistema. Por�m, n�o possui acesso a �rea Administrativa.");
		
		tipoUsuarioService.inclui(tipoUsuarioAdmin);
		tipoUsuarioService.inclui(tipoUsuarioAluno);
		tipoUsuarioService.inclui(tipoUsuarioGestor);
		tipoUsuarioService.inclui(tipoUsuarioEngenheiro);
		
		
		Usuario usuarioAdmin = new Usuario();
		Usuario usuarioAluno = new Usuario();
		Usuario usuarioGestor = new Usuario();
		Usuario usuarioEngenheiro = new Usuario();
		
		usuarioAdmin.setNome("Administrador");
		usuarioAdmin.setLogin("admin");
		usuarioAdmin.setSenha("123456");
		usuarioAdmin.setTipoUsuario(tipoUsuarioAdmin);
		
		usuarioAluno.setNome("Felipe Arruda");
		usuarioAluno.setLogin("felipe");
		usuarioAluno.setSenha("felipe");
		usuarioAluno.setTipoUsuario(tipoUsuarioAluno);
		
		usuarioEngenheiro.setNome("Gabriel Souza");
		usuarioEngenheiro.setLogin("gabriel");
		usuarioEngenheiro.setSenha("gabriel");
		usuarioEngenheiro.setTipoUsuario(tipoUsuarioEngenheiro);
		
		usuarioGestor.setNome("Marcos da Silva");
		usuarioGestor.setLogin("marcos");
		usuarioGestor.setSenha("marcos");
		usuarioGestor.setTipoUsuario(tipoUsuarioGestor);
		
		try {
			usuarioService.inclui(usuarioAdmin, usuarioAdmin.getSenha());
			usuarioService.inclui(usuarioEngenheiro, usuarioEngenheiro.getSenha());
			usuarioService.inclui(usuarioGestor, usuarioGestor.getSenha());
			usuarioService.inclui(usuarioAluno, usuarioAluno.getSenha());
		} catch (AplicacaoException e) {
			System.out.println("Erro na inclusao do usuario: "+ e.getMessage());
		}
	}
}
