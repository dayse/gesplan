 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe relativa ao bean correspondente a entidade Uusario.
 * Através deste bean é possível identificar se um usuáio esta logado.
 * 
 * @author marques.araujo
 *
 */

@NamedQueries(
		{	
			@NamedQuery
			(	name = "Usuario.recuperaPorLoginESenha",
				query = "select u from Usuario u " +
						"left outer join fetch u.tipoUsuario tu " +
						"where u.login = ? and u.senha = ? "
			),
			@NamedQuery
			(	name = "Usuario.recuperaPorLogin",
				query = "select u from Usuario u " +
					"where u.login = ?"
			),
			@NamedQuery
			(	name = "Usuario.recuperaListaDeUsuarios",
				query = "select u from Usuario u order by u.nome"
			),
			@NamedQuery
			(	name = "Usuario.recuperaListaDeUsuariosComTipo",
					query = "select u from Usuario u " +
							"left outer join fetch u.tipoUsuario tu " +
							"order by u.nome " 
			),
			@NamedQuery
			(	name = "Usuario.recuperaComPlanos",
					query = "select u from Usuario u " +
							"left outer join fetch u.planos " +
							"where u = ? "
			),
			@NamedQuery
			(	name = "Usuario.recuperaListaPaginadaPorNome",
				query = "select u from Usuario u " +
						"where upper(u.nome) like '%' || upper(?) || '%'  "
			),
			@NamedQuery
			(	name = "Usuario.recuperaListaPaginadaPorNomeCount",
				query = "select count(u) from Usuario u " +
						"where upper(u.nome) like '%' || upper(?) || '%'  "
			)
		})


@Entity
@Table(name="Usuario")
@SequenceGenerator(name="SEQUENCIA", sequenceName="SEQ_USUARIO", allocationSize=1)
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Usuario() {
	}
	
	
	/**
	 * Identificador do usuário.
	 * 
	 */
	private Long id;
	
	
	/**
	 * Nome do usuário.
	 * 
	 */
	private String nome;
	
	
	/**
	 * Login do usuario
	 * 
	 */
	private String login;
	
	/**
	 * Senha do usuário.
	 * 
	 */
	private String senha;
	
	/**
	 * Atributo que indica o tipo do usuário.
	 * 
	 */
	private TipoUsuario tipoUsuario;
	
	/**
	 * Atributo que contém a lista de planos para um determinado usuário.
	 * 
	 */
	private List<CadPlan> planos;

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@ManyToOne
	@JoinColumn(name="TIPO_USUARIO_ID", nullable=false)
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@OneToMany(mappedBy="usuario", cascade=CascadeType.REMOVE)
	public List<CadPlan> getPlanos() {
		return planos;
	}

	public void setPlanos(List<CadPlan> planos) {
		this.planos = planos;
	}
	
	/**
	 * Este método poder ser gerado AUTOMATICAMENTE pelo Java, juntamente com o método  "equals(Object obj)".
	 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 * @return int
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}

	
	/**
	 * Este método poder ser gerado AUTOMATICAMENTE pelo Java, juntamente com o método  "hashCode()".
	 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 * @param Object  
	 * @return boolean
	 */
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		if (senha == null) {
			if (other.senha != null) {
				return false;
			}
		} else if (!senha.equals(other.senha)) {
			return false;
		}
		return true;
	}
}
