 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.HashMap;
import java.util.List;

import exception.relatorio.RelatorioException;

import modelo.Tecido;
import modelo.TipoUsuario;
import modelo.Usuario;
import relatorio.Relatorio;
import relatorio.RelatorioFactory;
import service.anotacao.Transacional;
import service.exception.AplicacaoException;
import util.Digesto;
import DAO.UsuarioDAO;
import DAO.Impl.UsuarioDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

/**
 * 
 * UsuarioAppService é uma classe de serviço que possui as regras de negócio para manipular inicalmente
 * entidade Usuario. Estas manipulações incluem quando necessário chamadas as interfaces DAOs,
 * outras classes de serviço e acessos a informações do BD.
 * 
 * A classe UsuarioAppService fora criada para atender ao Padrão MVC, Model Vision Control , sendo a mesma uma
 * classe de serviço que é capaz de efetuar: controle de transação, ou seja esta classe possui o recurso de
 * abrir transaçao, commitar e fechar uma transaçao através de um interceptador de serviço.
 * Neste interceptador será definido se o método é transacional ou não e em função desta informação
 * o interceptador irá usar ou não uma transação.
 * 
 * @author marques.araujo
 * 
 */

public class UsuarioAppService {
	// DAOs
	private UsuarioDAO usuarioDAO;

	public UsuarioAppService() {
		try {
			// DAOs
			usuarioDAO = FabricaDeDao.getDao(UsuarioDAOImpl.class);
		} catch (Exception e) {
			e.printStackTrace();
			// O comando a seguir só será usado caso haja a criação de um service.
			// Exemplo:
			// Um Service A tem dentro de si a chamada de um Service B, só que o Service B também tem
			// uma chamada para o Service A, logo um service chamaria o outro sem parar causando assim um loop infinito.
			// Contudo, em termos de uso do sistemas esse erro não ocorreria de forma clara, 
			// pois a View seria carregada sem dados.
			// Para evitar que esse tipo de erro gere confusões - como o usuário pensar que o banco foi perdido, por exemplo - 
			// utilizamos o comando System.exit(1) que interrompe a aplicação, deixando explicita a ocorrência do erro.
			//System.exit(1); 
		}
	}
	
	public Usuario recuperaPorLoginESenha(String login, String senha) throws AplicacaoException {
		Usuario usuario;
		try {
			
			senha = Digesto.gerarDigestoMD5Correto(senha);
			
			usuario = usuarioDAO.recuperaPorLoginESenha(login, senha);
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException("usuario.ERRO_LOGIN_SENHA_INEXISTENTES");
		}
		
		return usuario;
	}
	
	@Transacional
	public void inclui(Usuario usuario, String confirmacaoSenha) throws AplicacaoException {
		
//		if (!usuarioCadastrador.getTipoUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
//			throw new AplicacaoException("usuario.NAO_POSSUI_PERMISSAO");
//		}
		
		try {
			usuarioDAO.recuperaPorLogin(usuario.getLogin());
			throw new AplicacaoException("usuario.LOGIN_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		if (usuario.getSenha().equals(confirmacaoSenha)) {
			String senha = Digesto.gerarDigestoMD5Correto(usuario.getSenha());
			usuario.setSenha(senha);
			
			usuario.setNome(usuario.getNome().toUpperCase());
			
			usuarioDAO.inclui(usuario);
		} else {
			throw new AplicacaoException("usuario.SENHA_CONFIRMACAO_ERRADA");
		}
	}
	
	@Transacional
	public void altera(Usuario usuario, Usuario usuarioAlterador, String confirmacaoSenha) throws AplicacaoException {
		
		if (!usuarioAlterador.getTipoUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
			throw new AplicacaoException(2,"usuario.NAO_POSSUI_PERMISSAO");
		}
		
		try {
			// Faz uma busca utilizando o ID do usuário que está sendo editado e o salva na variável usuarioExistente.
			Usuario usuarioExistente = usuarioDAO.getPorIdComLock(usuario.getId());
			
			/* O if verifica se o usuario que foi encontrado no banco possui um login diferente
			 		 do usuário que está sendo editado neste momento.
				Uma fez que o método equals retorne false - indicando que os logins são diferentes - o operador
					de negação (!) o transforma em true fazendo que as linhas dentro do if sejam executadas.
				A primeira linha de código dentro do if() faz uma busca por login, caso ele encontre algum resultado
					uma exceção é gerada. Isso se deve ao fato de que, se existe um login igual no banco, ele pertence
					a um usuário diferente.
			*/
			if(!usuarioExistente.getLogin().equals(usuario.getLogin())){
				usuarioDAO.recuperaPorLogin(usuario.getLogin());
				throw new AplicacaoException("usuario.LOGIN_EXISTENTE");
			}			
		} catch (ObjetoNaoEncontradoException e) {
		}
		
		if (usuario.getSenha().equals(confirmacaoSenha)) {
			try {
				usuarioDAO.getPorIdComLock(usuario.getId());
				
				String senha = Digesto.gerarDigestoMD5Correto(usuario.getSenha());
				usuario.setSenha(senha);
				
				usuario.setNome(usuario.getNome().toUpperCase());
				usuarioDAO.altera(usuario);
				
			} catch (ObjetoNaoEncontradoException e) {
				throw new AplicacaoException(0, "usuario.NAO_ENCONTRADO");
			}
		}else{
			throw new AplicacaoException("usuario.SENHA_CONFIRMACAO_ERRADA");
		}
	}

/*
  O método alteraSenha foi posto entre comentários, pois foi descoberto que ele não está sendo
  utilizado em nenhum momento do sistema.
  @Bruno.oliveira
 */
/*	@Transacional
	public void alteraSenha(String senhaAtual, Usuario usuario, String confirmacaoSenha) throws AplicacaoException {
		
		try {
			Usuario usuarioBD = usuarioDAO.getPorIdComLock(usuario.getId());
			
			senhaAtual = Digesto.gerarDigestoMD5Correto(senhaAtual);
			String senhaUsuario = usuarioBD.getSenha();
			
			if (!senhaAtual.equals(senhaUsuario)) {
				usuario.setSenha(senhaUsuario);
				throw new AplicacaoException(0,"usuario.SENHA_ATUAL_ERRADA");
			}
			
			String novaSenha = usuario.getSenha();
			
			if (!novaSenha.equals(confirmacaoSenha)) {
				usuario.setSenha(senhaUsuario);
				throw new AplicacaoException(1,"usuario.SENHA_CONFIRMACAO_ERRADA");
			}
			
			novaSenha = Digesto.gerarDigestoMD5Correto(novaSenha);
			
			usuarioBD.setSenha(novaSenha);
			
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException();
		}
	} */
	
	@Transacional
	public void exclui(Usuario usuario) throws AplicacaoException {
		
		Usuario usuarioBD = null;
		
		try {
			usuarioBD = usuarioDAO.getPorIdComLock(usuario.getId());
					
			usuarioDAO.exclui(usuarioBD);
			
		} catch (ObjetoNaoEncontradoException e) {
			throw new AplicacaoException();
		}
	}
	
	public List<Usuario> recuperaUsuarios() {
		return usuarioDAO.recuperaListaDeUsuarios();
	}
	
	public List<Usuario> recuperaUsuariosComTipo() {
		return usuarioDAO.recuperaListaDeUsuariosComTipo();
	}

	public List<Usuario> recuperaPorNome(String nome) {
		return usuarioDAO.recuperaListaPaginadaPorNome(nome);
	}

	public Usuario recuperaComPlanos(Usuario usuario) throws ObjetoNaoEncontradoException {
		return usuarioDAO.recuperaComPlanos(usuario);
	}	
	
	public List<Usuario> recuperaListaDeUsuarios() throws AplicacaoException 
	{	
		List<Usuario> usuarios = usuarioDAO.recuperaListaDeUsuarios();
		if (usuarios.size() == 0) 
		{	throw new AplicacaoException("Usuários não encontrados.");
		} 
		else 
		{	return usuarios;
		}
	}
	
	/**
	 * Este método é responsável or gerar relatório simples. 
	 * @param List<Tecido> 
	 * @return void
	 * @throws AplicacaoException
	 */
	@SuppressWarnings("unchecked")
	public void gerarRelatorio(List<Usuario> listaDeUsuarios) throws AplicacaoException {
		
		System.out.println("Antes do metodo getRelatorio dentro de gerarRelatorio de UsuarioAppService");

		Relatorio relatorio = RelatorioFactory.getRelatorio(Relatorio.RELATORIO_LISTAGEM_DE_USUARIOS);
				
		if(relatorio != null)
			System.out.println("A variavel do tipo Relatorio e difente de null em UsuarioAppService");
		
		System.out.println("Depois do metodo getRelatorio dentro de gerarRelatorio  de UsuarioAppService");
		
		try{
			relatorio.gerarRelatorio(listaDeUsuarios, new HashMap());
		} catch (RelatorioException re){
			throw new AplicacaoException("Usuario.RELATORIO_NAO_GERADO");
		}
	}
	
	
	
		
}
