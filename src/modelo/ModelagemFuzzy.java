 
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
 * Classe relativa ao Bean para o cadastro de Modelagem Fuzzy
 * 
 * 
 * @author felipe
 *
 */

@NamedQueries(
		{	

			@NamedQuery
			(	name = "ModelagemFuzzy.recuperaModelagemFuzzy",
				query = "select mf from ModelagemFuzzy mf where mf.id = ? "
			),
			@NamedQuery
			(	name = "ModelagemFuzzy.recuperaModelagemFuzzyPeloNome",
				query = "select mf from ModelagemFuzzy mf where mf.nomeModelagemFuzzy = ?"
			),				
			@NamedQuery
			(	name = "ModelagemFuzzy.recuperaListaDeModelagemFuzzys",
				query = "select mf "+
						"from ModelagemFuzzy mf "+
						"order by mf.nomeModelagemFuzzy asc"
			),
			@NamedQuery
			(	name = "ModelagemFuzzy.recuperaListaDeModelagemFuzzysPorFinalidade",
					query = "select mf "+
							"from ModelagemFuzzy mf "+
							"where mf.finalidadeModelagem = ? " +
							"order by mf.nomeModelagemFuzzy asc"
			),
			@NamedQuery
			(	name = "ModelagemFuzzy.recuperaListaPaginadaDeModelagemFuzzys",
				query = "select distinct mf from ModelagemFuzzy mf order by mf.nomeModelagemFuzzy asc"
			),
			@NamedQuery
			(	name = "ModelagemFuzzy.recuperaListaPaginadaDeModelagemFuzzysCount",
				query = "select count (distinct mf) from ModelagemFuzzy mf"
			)			
		})

				
@Entity
@Table(name="MODELAGEMFUZZY")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_MODELAGEMFUZZY",
		           allocationSize=1)
/**
 * ModelagemFuzzy - Essa classe representa uma modelagem fuzzy com seus dados que seram utilizados.
 * Como o nome do autor da modelagem, o arquivo da mesma, os cadPlans que a utilizam.
 * @author arruda
 *
 */		           
public class ModelagemFuzzy implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/** identificador da Modelagem Fuzzy */		 
	private Long id;

	/** nome que identifica a Modelagem Fuzzy*/		 
	private String nomeModelagemFuzzy;
	
	/** descricao da Modelagem Fuzzy*/		 
	private String descrModelagemFuzzy;	

	/** nome do usuario responsavel por criar esta Modelagem Fuzzy. */		 
	private String autor;
	
	/** Data em que foi feito upload dessa modelagem no sistema. */
	private Calendar dataCriacao;
	

	/** O nome do arquivo da modelagem usado. */	
	private String nomeArquivo;

	/** Indica qual a finalidade da modelagem(pode ser GERAR_PMP ou AVALIAR_PMP). */	
	private String finalidadeModelagem;
	
	/**
	 * Atributo que contém a lista de planos para um determinado usuário.
	 * 
	 */
	private List<CadPlan> cadPlans;
	
	/** Campo transiente para facilitar acesso ao conteudo do arquivo de modelagem */
	private transient Specification modelagem;
	
	public ModelagemFuzzy() {
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

	@Column(nullable = false,unique=true)
	public String getNomeModelagemFuzzy() {
		return nomeModelagemFuzzy;
	}

	public void setNomeModelagemFuzzy(String nomeModelagemFuzzy) {
		this.nomeModelagemFuzzy = nomeModelagemFuzzy;
	}



	@Temporal(value = TemporalType.DATE)
	public Calendar getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}



	/**
	 * @return the descrModelagemFuzzy
	 */
	public String getDescrModelagemFuzzy() {
		return descrModelagemFuzzy;
	}



	/**
	 * @param descrModelagemFuzzy the descrModelagemFuzzy to set
	 */
	public void setDescrModelagemFuzzy(String descrModelagemFuzzy) {
		this.descrModelagemFuzzy = descrModelagemFuzzy;
	}



	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}



	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}


	/**
	 * @return the finalidadeModelagem
	 */
	public String getFinalidadeModelagem() {
		return finalidadeModelagem;
	}



	/**
	 * @param finalidadeModelagem the finalidadeModelagem to set
	 */
	public void setFinalidadeModelagem(String finalidadeModelagem) {
		this.finalidadeModelagem = finalidadeModelagem;
	}



	/**
	 * @return the autor
	 */
	public String getAutor() {
		return autor;
	}



	/**
	 * @param autor the autor to set
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	// ********* Métodos para Associações *********
	

	@OneToMany(mappedBy="modelagemFuzzy")
	public List<CadPlan> getCadPlans() {
		return cadPlans;
	}

	public void setCadPlans(List<CadPlan> cadPlans) {
		this.cadPlans = cadPlans;
	}

	
	@Transient
	public Specification getModelagem() {
		return modelagem;
	}


	public void setModelagem(Specification modelagem) {
		this.modelagem = modelagem;
	}

	

	@Override
	public String toString() {
		return this.nomeArquivo  + " - " + this.descrModelagemFuzzy;
	}
	
}
