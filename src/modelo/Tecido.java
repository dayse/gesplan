 
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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

/**
 * Classe relativa ao bean correspondente a entidade Tecido para o cadastro de Tecidos/Renda.
 * 
 * @author marques.araujo
 *
 */
 
@NamedQueries(
		{	
			@NamedQuery
			(	name = "Tecido.recuperaTecido",
					query = "select t from Tecido t where t.id = ?"
			),
			@NamedQuery
			(	name = "Tecido.recuperaTecidoPorCodigo",
				query = "select t from Tecido t where t.codTecido = ?"
			),
			@NamedQuery
			(   name = "Tecido.recuperaListaDeTecidosPeloCodigoLike",
					query = "select t from Tecido t " +
					        "where t.codTecido like '%' || upper(?) || '%' "  
 			),
 			@NamedQuery
 			(	name = "Tecido.recuperaListaDeTecidosPorDescricao", 
					query = "select distinct(t) from Tecido t " + 
							"where upper(t.descrTecido) like '%' || upper(?) || '%' " + 
							"order by t.codTecido "
		    ),
			@NamedQuery
			(	name = "Tecido.recuperaListaDeTecidos",
				query = "select t from Tecido t order by t.codTecido asc"
			),
			@NamedQuery
			(	name = "Tecido.paginaTecido",
				query = "select distinct t from Tecido t order by t.codTecido asc"
			),
			@NamedQuery
			(	name = "Tecido.recuperaListaPaginadaDeTecidos",
				query = "select distinct t from Tecido t order by t.codTecido asc"
			),
			@NamedQuery
			(	name = "Tecido.paginaTecidoCount",
					query = "select count (distinct t) from Tecido t order by t.codTecido asc"
			),
			@NamedQuery
			(	name = "Tecido.recuperaListaPaginadaDeTecidosCount",
					query ="select count (distinct t) from Tecido t"
			),
			@NamedQuery
			(    name = "Tecido.recuperaListaPaginadaDeTecidosComListaDeTecModels",
					 query = "select distinct t from Tecido t " + 
					         "left outer join fetch t.tecModels tm " +
					         "order by t.codTecido asc"
			),
			@NamedQuery
			(	name = "Tecido.recuperaListaPaginadaDeTecidosComListaDeTecModelsCount",
				query = "select count(distinct t) from Tecido t " +
						"left outer join t.tecModels "
			),
			@NamedQuery
			(	name = "Tecido.recuperaListaDeTecidosComTecModels",
				query = "select distinct t from Tecido t " +
						"left outer join fetch t.tecModels " +
						"order by t.codTecido "
			),
			@NamedQuery
			(	name = "Tecido.recuperaListaDeTecidosQueTenhamApenasTecModels",
				query = "select distinct t from Tecido t " +
						"inner join t.tecModels "
			)
								
		} )
		
@Entity		
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_TECIDO",
		           allocationSize=1)

public class Tecido implements Serializable, Comparable<Tecido>{

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador do Tecido/Renda 
	 *   
	 */
	private long id;
	
	/** 
	 * Código do Tecido/Renda
	 * 
	 */
	private String codTecido;
	
	/** 
	 * Descrição do Tecido/Renda
	 * 
	 */
	private String descrTecido;
	
	/** 
	 * Unidade de Medida do Tecido/Renda
	 * 
	 */
	private String UM;
	
	/** 
	 * Custo unitário do Tecido/Renda por Kg ou por m
	 * 
	 */
	private Double custoUnit;
	
	/** 
	 * Lead Time do Tecido na Unidade 2
	 * 
	 */
	private Double leadTimeUnidade2;
	
	/** 
	 * Fator de Rendimento de cada Tecido
	 * 
	 */
	private Double fatorDeRendimento;
	
	/** 
	 * Produção diária máxima do Tecido na Unidade 2
	 * 
	 */
	private Double producaoDiariaMaxUnidade2;
	
	/** 
	 * Lista de TecModels que usam o Tecido.
	 * 
	 */
	private List<TecModel> tecModels = new ArrayList<TecModel>(); 
	
	/**
	 * Lista da entidade visão que informa a capacidade diária por período disponível do tecido 
	 * na matriz, calculada em função da producaoDiariaMaxUnidade2.
	 */
	private transient List<CapacTecView> capacTecViews = new ArrayList<CapacTecView>(); 
	
	/**
	 * Construtor da classe Tecido. 
	 * 
	 */
	public Tecido() {
	}
	
	
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public void setDescrTecido(String descrTecido) {
		this.descrTecido = descrTecido;
	}


	public String getCodTecido() {
		return codTecido;
	}


	public void setCodTecido(String codTecido) {
		this.codTecido = codTecido;
	}


	public String getDescrTecido() {
		return descrTecido;
	}


	public void setDescTecido(String descrTecido) {
		this.descrTecido = descrTecido;
	}


	public String getUM() {
		return UM;
	}


	public void setUM(String UM) {
		this.UM = UM;
	}


	public Double getCustoUnit() {
		return custoUnit;
	}


	public void setCustoUnit(Double custoUnit) {
		this.custoUnit = custoUnit;
	}


	public Double getLeadTimeUnidade2() {
		return leadTimeUnidade2;
	}
	
	
	public void setLeadTimeUnidade2(Double leadTimeUnidade2) {
		this.leadTimeUnidade2 = leadTimeUnidade2;
	}
	
	
	public Double getFatorDeRendimento() {
		return fatorDeRendimento;
	}
	
	
	public void setFatorDeRendimento(Double fatorDeRendimento) {
		
		this.fatorDeRendimento = fatorDeRendimento;
			
	}
	
	
	public Double getProducaoDiariaMaxUnidade2() {
		return producaoDiariaMaxUnidade2;
	}
	
	
	public void setProducaoDiariaMaxUnidade2(Double producaoDiariaMaxUnidade2) {
		this.producaoDiariaMaxUnidade2 = producaoDiariaMaxUnidade2;
	}


	
	@OneToMany(mappedBy = "tecido", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	public List<TecModel> getTecModels() {
		return tecModels;
	}


	public void setTecModels(List<TecModel> tecmodels) {
		this.tecModels = tecmodels;
	}
	
	

    @Transient
	public List<CapacTecView> getCapacTecViews() {
		return capacTecViews;
	}

	public void setCapacTecViews(List<CapacTecView> capacTecViews) {
		this.capacTecViews = capacTecViews;
	}
	

	public String toString() {
		return codTecido + " - " + descrTecido + " - " + UM;
		
	}
	
	/**
	 * 
	 *  Este método consiste em definir o critério de ordenação entre 2 objetos Tecidos, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'codTecido', que é do tipo 
	 *  nativo 'String', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
	 *  
	 * @author marques.araujo
	 * @param Tecido
	 * @return int
	 * 
	 */
	
	@Override
	public int compareTo(Tecido o) {
		
		int valor = codTecido.compareTo(o.codTecido);
		return (valor != 0 ? valor : 1); 
		
	}

	/**
	 * Este método poder ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método  "equals(Object obj)".
	 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 * @author marques.araujo
	 * @return int
	 * 
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UM == null) ? 0 : UM.hashCode());
		result = prime * result
				+ ((codTecido == null) ? 0 : codTecido.hashCode());
		result = prime * result
				+ ((custoUnit == null) ? 0 : custoUnit.hashCode());
		result = prime * result
				+ ((descrTecido == null) ? 0 : descrTecido.hashCode());
		result = prime
				* result
				+ ((fatorDeRendimento == null) ? 0 : fatorDeRendimento
						.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime
				* result
				+ ((leadTimeUnidade2 == null) ? 0 : leadTimeUnidade2.hashCode());
		result = prime
				* result
				+ ((producaoDiariaMaxUnidade2 == null) ? 0
						: producaoDiariaMaxUnidade2.hashCode());
		result = prime * result
				+ ((tecModels == null) ? 0 : tecModels.hashCode());
		return result;
	}

	
	/**
	 * 
	 * Este método pode ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método 
	 * "hashCode()". Estes metodos são necessários para se determinar um critério principal
	 * de igualdade entre 2 objetos. 
	 * 
	 * @author marques.araujo
	 * @param Object
	 * @return boolean
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tecido other = (Tecido) obj;
		if (UM == null) {
			if (other.UM != null)
				return false;
		} else if (!UM.equals(other.UM))
			return false;
		if (codTecido == null) {
			if (other.codTecido != null)
				return false;
		} else if (!codTecido.equals(other.codTecido))
			return false;
		if (custoUnit == null) {
			if (other.custoUnit != null)
				return false;
		} else if (!custoUnit.equals(other.custoUnit))
			return false;
		if (descrTecido == null) {
			if (other.descrTecido != null)
				return false;
		} else if (!descrTecido.equals(other.descrTecido))
			return false;
		if (fatorDeRendimento == null) {
			if (other.fatorDeRendimento != null)
				return false;
		} else if (!fatorDeRendimento.equals(other.fatorDeRendimento))
			return false;
		if (id != other.id)
			return false;
		if (leadTimeUnidade2 == null) {
			if (other.leadTimeUnidade2 != null)
				return false;
		} else if (!leadTimeUnidade2.equals(other.leadTimeUnidade2))
			return false;
		if (producaoDiariaMaxUnidade2 == null) {
			if (other.producaoDiariaMaxUnidade2 != null)
				return false;
		} else if (!producaoDiariaMaxUnidade2
				.equals(other.producaoDiariaMaxUnidade2))
			return false;
		if (tecModels == null) {
			if (other.tecModels != null)
				return false;
		} else if (!tecModels.equals(other.tecModels))
			return false;
		return true;
	}
	
	
	
	
	
	
}
