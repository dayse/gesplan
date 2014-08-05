 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modelo.TipoUsuario;
import modelo.Usuario;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.TipoUsuarioAppService;
import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.Constantes;
import util.JsonConfigLoader;

import com.google.gson.Gson;
/**
 * Sobre a Carga:
 * É uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o método executar().
 * Nesse método executar é que é chamado os outros métodos que são
 * as etápas dessa carga.
 * Portanto se é necessario rodar um método depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no método executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas é preciso retornar true.
 * Se houver algum problema(exceção) na execução de um das etapas, essa exceção deve ser lancada
 * 
 * Essa Carga:
 * Classe responsável pela inclusão de Tipos de Usuário e de Usuário.
 * É usada na carga do sistema e deve ser a primeira a ser executada.
 * Está criando um usuário para cada tipo. (dma)
 * 
 * @author felipe.arruda
 *
 */
public class CargaUsuario extends CargaBase{
  
	// Services
	public TipoUsuarioAppService tipoUsuarioService;
	public UsuarioAppService usuarioService;
	
	public CargaUsuario(){
		try {
			
			tipoUsuarioService = FabricaDeAppService.getAppService(TipoUsuarioAppService.class);
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executa a inclusao dos tipos de usuario
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirTiposDeUsuario();
		return true;
	}
	
	public List<Usuario> recuperaUsuariosDeArquivoConfigJson(){

		System.out.println(
				JsonConfigLoader.getJson(Constantes.CAMINHO_ABSOLUTO_ARQUIVO_USUARIOS_CARGA)
				);
		String json = JsonConfigLoader.getJson(Constantes.CAMINHO_ABSOLUTO_ARQUIVO_USUARIOS_CARGA);
		Gson gson = new Gson();
		Usuario[] arrayUsuarios = gson.fromJson(json, Usuario[].class);
		
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>(Arrays.asList(arrayUsuarios));
		return usuarios;
	}
	
	public void incluirTiposDeUsuario() throws AplicacaoException {

		
		TipoUsuario tipoUsuarioAdmin = new TipoUsuario();
		TipoUsuario tipoUsuarioAluno = new TipoUsuario();
		TipoUsuario tipoUsuarioGestor = new TipoUsuario();
		TipoUsuario tipoUsuarioEngenheiro = new TipoUsuario();
		
		tipoUsuarioAdmin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
		tipoUsuarioAdmin.setDescricao("O usuário ADMINISTRADOR pode realizar qualquer operação no Sistema.");
		
		tipoUsuarioAluno.setTipoUsuario(TipoUsuario.ALUNO);
		tipoUsuarioAluno.setDescricao("O usuário ALUNO pode realizar apenas consultas e impressão de relatórios nas telas " +
				                        "relativas ao Horizonte de Planejamento (HP,Periodo PMP, Periodo PAP) e não acessa " +
				                        "Administração e Eng. Conhecimento");
		
		tipoUsuarioGestor.setTipoUsuario(TipoUsuario.GESTOR);
		tipoUsuarioGestor.setDescricao("O usuário GESTOR pode realizar qualquer operação no Sistema, porém não possui acesso" +
				"as áreas de Administração e Engenharia de Conhecimento.");
		
		tipoUsuarioEngenheiro.setTipoUsuario(TipoUsuario.ENGENHEIRO_DE_CONHECIMENTO);
		tipoUsuarioEngenheiro.setDescricao("O usuário ENGENHEIRO pode realizar a parte de Logica Fuzzy (Engenharia de Conhecimento)" +
				"no Sistema. Porém, não possui acesso a área Administrativa.");
		
		tipoUsuarioService.inclui(tipoUsuarioAdmin);
		tipoUsuarioService.inclui(tipoUsuarioAluno);
		tipoUsuarioService.inclui(tipoUsuarioGestor);
		tipoUsuarioService.inclui(tipoUsuarioEngenheiro);
		

		List<Usuario> usuarios = recuperaUsuariosDeArquivoConfigJson();
		
		
		Usuario usuarioAdmin = usuarios.get(0);
		usuarioAdmin.setTipoUsuario(tipoUsuarioAdmin);
		
		Usuario usuarioAluno = usuarios.get(1);
		usuarioAluno.setTipoUsuario(tipoUsuarioAdmin);
		
		Usuario usuarioGestor = usuarios.get(2);
		usuarioGestor.setTipoUsuario(tipoUsuarioGestor);
		
		Usuario usuarioEngenheiro = usuarios.get(3);
		usuarioEngenheiro.setTipoUsuario(tipoUsuarioEngenheiro);

		
		
		
			usuarioService.inclui(usuarioAdmin, usuarioAdmin.getSenha());
			usuarioService.inclui(usuarioEngenheiro, usuarioEngenheiro.getSenha());
			usuarioService.inclui(usuarioGestor, usuarioGestor.getSenha());
			usuarioService.inclui(usuarioAluno, usuarioAluno.getSenha());
			
	}

}
