 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe relativa ao bean correspondente a entidade TipoUusario.
 * 
 * Através deste bean é possível identificar o tipo de usuário que encontra-se logado.
 * 
 * @author marques.araujo
 *
 */

@NamedQueries(
		{	
			@NamedQuery
			(	name = "TipoUsuario.recuperaTipoUsuarioPorTipo",
				query = "select t from TipoUsuario t " +
						"where t.tipoUsuario = ? "
			),
			@NamedQuery
			(	name = "TipoUsuario.recuperaListaDeTipoUsuario",
					query = "select t from TipoUsuario t " +
							"order by t.tipoUsuario "
			)
		})

@Entity
@Table(name="TIPO_USUARIO")
@SequenceGenerator(name="SEQUENCIA", sequenceName="SEQ_TIPO_USUARIO", allocationSize=1)
public class TipoUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String ALUNO = "Aluno";
	public static final String ADMINISTRADOR = "Administrador";
	public static final String GESTOR = "Gestor";
	public static final String ENGENHEIRO_DE_CONHECIMENTO = "Engenheiro de Conhecimento";
	
	
	/**
	 * Identificador do tipo do usuario.
	 * 
	 */
	private Long id;
	
	/**
	 * Atributo que informa o tipo do usuario.
	 * 
	 */
	private String tipoUsuario;
	
	/**
	 * Descrisção do tipo de usuário.	 
	 * 
	 */
	private String descricao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTipoUsuario() {
		return tipoUsuario;
	}
	
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String toString(){
		return this.tipoUsuario;
	}
}
