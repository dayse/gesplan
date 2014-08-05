 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import xfuzzy.lang.Specification;



/**
 * Classe relativa ao Bean para o cadastro de Excecão
 * 
 * 
 * @author felipe
 *
 */
@NamedQueries(
		{	

			@NamedQuery
			(	name = "Excecao.recuperaExcecao",
				query = "select ex from Excecao ex where ex.id = ? "
			),
			@NamedQuery
			(	name = "Excecao.recuperaExcecaoPeloTipoDeExcecao",
				query = "select ex from Excecao ex where ex.tipoDeExcecao = ?"
			),				
			@NamedQuery
			(	name = "Excecao.recuperaListaDeExcecoes",
				query = "select ex "+
						"from Excecao ex "+
						"order by ex.tipoDeExcecao asc"
			),
			@NamedQuery
			(	name = "Excecao.recuperaListaPaginadaDeExcecoes",
				query = "select distinct ex from Excecao ex order by ex.tipoDeExcecao asc"
			),
			@NamedQuery
			(	name = "Excecao.recuperaListaPaginadaDeExcecoesCount",
				query = "select count (distinct ex) from Excecao ex"
			)					
		})

				
@Entity
@Table(name="EXCECAO")      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_EXCECAO",
		           allocationSize=1)
/**
 * Excecao - Essa classe representa as mensagens de exceção.
 * 
 * @author arruda
 *
 */		           
public class Excecao implements Serializable, Comparable<Excecao>{
	
	private static final long serialVersionUID = 1L;
	
	/** identificador da Excecao */		 
	private Long id;

	/** codigo/tipo do identificador da Excecao*/		 
	private String tipoDeExcecao;
	
	/** descricao da Excecao */		 
	private String descrExcecao;	

	/** Informa o status da exceção (pode ser "ativa" ou "desativa") */		 
	private boolean statusExcecao;

	
	/**
	 * Atributo que contém a lista de planos para um determinado usuário.
	 * 
	 */
	private List<ExcecaoMens> excecaoMenss;
	
	public Excecao() {
	}



	// ********* Métodos do Tipo Get e Set *********
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length=15, unique=true)
	public String getTipoDeExcecao() {
		return tipoDeExcecao;
	}



	public void setTipoDeExcecao(String tipoDeExcecao) {
		this.tipoDeExcecao = tipoDeExcecao;
	}



	public String getDescrExcecao() {
		return descrExcecao;
	}



	public void setDescrExcecao(String descrExcecao) {
		this.descrExcecao = descrExcecao;
	}



	public boolean getStatusExcecao() {
		return statusExcecao;
	}



	public void setStatusExcecao(boolean statusExcecao) {
		this.statusExcecao = statusExcecao;
	}



	
	// ********* Métodos para Associações *********
	

	@OneToMany(mappedBy="excecao")
	public List<ExcecaoMens> getExcecaoMenss() {
		return excecaoMenss;
	}

	public void setExcecaoMenss(List<ExcecaoMens> excecaoMenss) {
		this.excecaoMenss = excecaoMenss;
	}

	
	@Override
	public String toString() {
		return this.tipoDeExcecao  + " - " + this.descrExcecao;
	}



	@Override
	public int compareTo(Excecao o) {
		 int valor = tipoDeExcecao.compareTo(o.tipoDeExcecao);
	     return (valor != 0 ? valor : 1); 
	}

	
}
