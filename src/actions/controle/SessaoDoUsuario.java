 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions.controle;

import java.io.Serializable;

import modelo.TipoUsuario;
import modelo.Usuario;

public class SessaoDoUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Usuario usuarioLogado;
	
	private boolean aluno;
	private boolean gestor;
	private boolean engenheiroConhecimento;
	private boolean administrador;

	// ================================== Métodos get() e set() ================================== //
	

	public boolean isAluno() {
		return usuarioLogado.getTipoUsuario().equals(TipoUsuario.ALUNO);
	}

	public boolean isGestor() {
		return usuarioLogado.getTipoUsuario().getTipoUsuario().equals(TipoUsuario.GESTOR);
	}

	public boolean isEngenheiroConhecimento() {
		return usuarioLogado.getTipoUsuario().getTipoUsuario().equals(TipoUsuario.ENGENHEIRO_DE_CONHECIMENTO);
	}
	
	public boolean isAdministrador() {
		return usuarioLogado.getTipoUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR);
	}
	
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
}
