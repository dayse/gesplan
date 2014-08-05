 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

@NamedQueries(
	{
	@NamedQuery(name="CadPlan.recuperaCadPlanPorCodigo",
				query = "select cadplan " +
					  	"from CadPlan cadPlan " +
					  	"where cadPlan.codPlan = ? "	
	),
	@NamedQuery(name="CadPlan.recuperaListaDeCadPlan",
				query = "select cadPlan " +
						"from CadPlan cadPlan " +
						"order by cadPlan.codPlan "
	),
	@NamedQuery(name="CadPlan.recuperaListaDeCadPlanPorUsuario",
				query = "select cadPlan " +
						"from CadPlan cadPlan " +
						"where cadPlan.usuario = ? " +
						"order by cadPlan.codPlan "
	),
	@NamedQuery(name="CadPlan.recuperaListaDeCadPlanPorModelagemFuzzy",
			query = "select cadPlan " +
					"from CadPlan cadPlan " +
					"where cadPlan.modelagemFuzzy = ? "
	),

	@NamedQuery(name="CadPlan.recuperaCadPlanComPlanosModelo",
				query = "select c from CadPlan c " +
						"left outer join fetch c.planosModelo pl " +
						"where c.codPlan = ? "
	),
	@NamedQuery(name="CadPlan.recuperaCadPlanApenasComPlanosModelo",
				query = "select c from CadPlan c " +
						"left outer join fetch c.planosModelo pl " +
						"where c.codPlan = ? "
	)
  }
)




@Entity
@Table(name = "CadPlan")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_CADPLAN", allocationSize = 1)
public class CadPlan implements Serializable, Comparable<CadPlan> {

	private static final long serialVersionUID = 1L;

	public CadPlan() {
		
		this.escoreMin = 0.0;
		this.escoreModeloEscMinimo = 0.0;
		this.escoreMedio = 0.0;
		this.varEstqPer = 0.0;
		this.varProdDiaPer = 0.0;
		this.ranking = 0.0;
		this.escoreCalculado=false;
	}

	/**
	 * Identificador do cadastro de planos
	 */
	private Long id;
	
	/**
	 * Codigo do Plano Mestre de Producao
	 */
	private String codPlan;

	/**
	 * Descrição do PMP
	 */
	private String descrPlan;
	
	/**
	 * Valor do Escore minimo obtido pelo plano para um determinado 
	 * modelo em algum periodo de seu HP
	 */
	private Double escoreMin;
	
	/**
	 * Valor do escore medio obtido pelo plano, considerando 
	 * todos seus modelos
	 */
	private Double escoreMedio;

	/**
	 * Valor do escore do modelo de escore minimo.
	 * (menor nota de todos os modelos)
	 */
	private Double escoreModeloEscMinimo;
	
	/**
	 * Variacao percentual em relacao ao Estoque Desejado para
	 * um determinado modelo x periodo relativa ao EscoreMin
	 */
	private Double varEstqPer;

	/**
	 * Variacao percentual em relacao a ProdDiariaMedia daquela 
	 * combinacao modelo x plano
	 */
	private Double varProdDiaPer;

	/**
	 * Ranking obtido pelo plano.
	 * Varia no intervalo [1...numeroDePlanos]
	 */
	private Double ranking;

	/**
	 * Se o escore foi calculado para esse plano.
	 */
	private boolean escoreCalculado;
	
	/**
	 * Usuario responsavel por este Plano.
	 */
	private Usuario usuario;

	/**
	 * Modelagem fuzzy sendo usada neste plano.
	 */
	private ModelagemFuzzy modelagemFuzzy;

	/**
	 * Objeto que referencia a classe que faz a relação entre o Plano e o Modelo
	 */
	private Set<PlanoModelo> planosModelo;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique=true)
	public String getCodPlan() {
		return codPlan;
	}

	public void setCodPlan(String codPlan) {
		this.codPlan = codPlan;
	}

	@ManyToOne
	@JoinColumn(name="USUARIO_ID", nullable=false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne
	@JoinColumn(name="MODELAGEMFUZZY_ID", nullable=true)
	public ModelagemFuzzy getModelagemFuzzy() {
		return modelagemFuzzy;
	}

	public void setModelagemFuzzy(ModelagemFuzzy modelagemFuzzy) {
		this.modelagemFuzzy = modelagemFuzzy;
	}

	public String getDescrPlan() {
		return descrPlan;
	}

	public Double getEscoreMin() {
		return escoreMin;
	}

	public Double getEscoreMedio() {
		return escoreMedio;
	}

	public Double getEscoreModeloEscMinimo() {
		return escoreModeloEscMinimo;
	}


	public Double getVarEstqPer() {
		return varEstqPer;
	}

	public Double getVarProdDiaPer() {
		return varProdDiaPer;
	}

	public Double getRanking() {
		return ranking;
	}

	@OneToMany(mappedBy="cadPlan", cascade={CascadeType.REMOVE, CascadeType.PERSIST})
	public Set<PlanoModelo> getPlanosModelo() {
		return planosModelo;
	}

	public void setDescrPlan(String descrPlan) {
		this.descrPlan = descrPlan;
	}

	public void setEscoreMin(Double escoreMin) {
		this.escoreMin = escoreMin;
	}

	public void setEscoreMedio(Double escoreMedio) {
		this.escoreMedio = escoreMedio;
	}

	public void setEscoreModeloEscMinimo(Double escoreModeloEscMinimo) {
		this.escoreModeloEscMinimo = escoreModeloEscMinimo;
	}
	public void setVarEstqPer(Double varEstqPer) {
		this.varEstqPer = varEstqPer;
	}

	public void setVarProdDiaPer(Double varProdDiaPer) {
		this.varProdDiaPer = varProdDiaPer;
	}

	public void setRanking(Double ranking) {
		this.ranking = ranking;
	}

	public void setPlanosModelo(Set<PlanoModelo> planosModelo) {
		this.planosModelo = planosModelo;
	}
	
	public boolean getEscoreCalculado() {
		return escoreCalculado;
	}

	public void setEscoreCalculado(boolean escoreCalculado) {
		this.escoreCalculado = escoreCalculado;
	}

	/**
	 * 
	 *  Este método consiste em definir o critério de ordenação entre 2 objetos CadPlan, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'codPlan', que é do tipo 
	 *  nativo 'String', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
	 *  
	 * @author walanem.junior
	 * @param CadPlan
	 * @return int
	 * 
	 */
	 @Override
	 public int compareTo(CadPlan o) {
		return Collator.getInstance().compare(codPlan, o.codPlan);  
	 }
	
	public String toString() {
		return this.codPlan + " - " + this.descrPlan;
	}

	/**
	 * Este método poder ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método  "equals(Object obj)".
	 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 * @return int
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((codPlan == null) ? 0 : codPlan.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	/**
	 * 
	 * Como o critério principal de igualdade entre 2 objetos CadPlan é o campo 
	 * 'codPlan' que, por ser do tipo String, também representa uma classe, exige que haja a implementação deste método.
	 *  Caso contrário, fica impossível diferenciar 2 objetos do tipo CadPlan, e consequentemente temos a impressão
	 * de que 'sumiram' registros, entre outros possiveis problemas. 
	 * 
	 * OBS: É PRIMORDIAL DAR ATENÇÃO PARA ESTE DETALHE (PRINCIPALMENTE QUANDO TRABALHARMOS COM ESTRUTURAS COMO 'Set')!!!
	 * 
	 * @author walanem.junior
	 * @param Object
	 * @return boolean
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CadPlan)) {
			return false;
		}
		CadPlan other = (CadPlan) obj;
		if (codPlan == null) {
			if (other.codPlan != null) {
				return false;
			}
		} else if (!codPlan.equals(other.codPlan)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		return true;
	}

}
