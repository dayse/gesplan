 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
/**
 * package relativo a todas as classes de negocio.
 * Com as classes bean
 */
package modelo;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

@NamedQueries( 
		{
		@NamedQuery(name = "Modelo.recuperaListaDeModelos", 
					query = "select m from Modelo m " +
							"order by m.codModelo"
		),
		@NamedQuery(name = "Modelo.recuperaListaDeModelosComFamilias", 
					query = "select m from Modelo m " + 
							"left outer join fetch m.familia " + 
							"order by m.codModelo"
		),
		@NamedQuery(name = "Modelo.recuperaListaDeModelosComFamiliasEPeriodos", 
					query = "select m from Modelo m " + 
							"left outer join fetch m.familia " + 
							"left outer join fetch m.deModPers " +
							"order by m.codModelo "
		),
		@NamedQuery(name = "Modelo.recuperaUmModeloComFamilia", 
					query = "select m from Modelo m " + 
							"left outer join fetch m.familia " + 
							"where m = ?"
		),
		@NamedQuery(name = "Modelo.recuperaModeloComFamiliaEPeriodos", 
					query = "select m from Modelo m " + 
							"left outer join fetch m.familia " + 
							"left outer join fetch m.deModPers " +
							"where m = ?"
		),
		@NamedQuery(name = "Modelo.recuperaListaDeModelosPorFamilia", 
					query = "select m from Modelo m " + 
							"left outer join m.familia f " + 
							"where f = ?"
		),
		@NamedQuery(name = "Modelo.recuperaModeloPorCodigo", 
					query = "select m from Modelo m " + 
							"left outer join fetch m.familia " + 
							"where m.codModelo = ?"
		),
		@NamedQuery(name = "Modelo.recuperaModeloPorCodigoLike", 
					query = "select m from Modelo m " + 
							"left outer join fetch m.familia " + 
							"where m.codModelo like '%' || upper(?) || '%' "
		),
		@NamedQuery(name = "Modelo.recuperaModeloPorDescricao", 
					query = "select distinct(m) from Modelo m " + 
							"left outer join fetch m.familia " + 
							"where upper(m.descrModelo) like '%' || upper(?) || '%' " + 
							"order by m.codModelo "
		),
		@NamedQuery(name = "Modelo.recuperaListaPaginadaDeModelosComFamilias",
					query = "select m from Modelo m " +  
							"left outer join fetch m.familia " +  
							"order by m.codModelo "
		),			
		@NamedQuery(name = "Modelo.recuperaListaPaginadaDeModelosComFamiliasCount",
					query = "select count(distinct modelo) " +
							"from Modelo modelo "
		),
		@NamedQuery(name = "Modelo.recuperaListaDeModelosComFamiliasEPeriodosCount",
					query = "select count(distinct m) " +
							"from Modelo m " +
							"left outer join m.familia " + 
							"left outer join m.deModPers "
					),

		@NamedQuery
		(	name = "Modelo.recuperaListaPaginadaDeModelosComFamiliaComListaDePMPs",
			query = "select distinct m from Modelo m " +
					"left outer join fetch m.familia f " +
					"left outer join fetch m.pmps pmp " +
					"order by m.codModelo asc "
		),	
		@NamedQuery
		(	name = "Modelo.recuperaListaPaginadaDeModelosComFamiliaComListaDePMPsCount",
				query = "select count(distinct m) from Modelo m " +
						"left outer join m.pmps "
		)	
		
		}		
)

@Entity
@Table(name = "MODELO")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_MODELO", allocationSize = 1)
public class Modelo implements Serializable, Comparable<Modelo> {

	private static final long serialVersionUID = 1L;

	public static final String FLAG_LIVRE = "Livre";
	public static final String FLAG_FIXO = "Fixo";

	/** identificador de modelos */
	private Long id;

	/** codigo do modelo */
	private String codModelo;

	/** descricao do modelo */
	private String descrModelo;

	/** flag para identificar se modelo é livre ou fixo para alteração no algoritmo */
	private Boolean flagProducaoModel;

	/** verificar finalidade do campo e se deve realmente ser double */
	private double indiceCob;

	/** Tempo Unitario de Costura (em minutos) */
	private double tuc;

	/** tamanho do lote (em peças) */
	private double tamLote;

	/** Tempo de reposição do modelo (TR - em dias) */
	private double tr;

	/** cobertura percentual de estoques do modelo em relação ao próximo período do HP */
	private double cobertura;

	/** estoque inicial (em peças) para o modelo no início do HP */
	private double estqInicModel;

	/** O recebimento pendente entra na inclusão do plano somando com estoque inicial,
	 * pois é uma quantidade que já está sendo praticamente entregue no estoque,
	 * mas ainda está no chão de fabrica.
	 * 
	 * ATENCAO: quando O usuário for iniciar o novo ciclo de planejamento deve editar essa quantidade
	 * no campo recebimentopendente do modelo como sendo um feedback do chao de fabrica.
	 */
	private double recebimentoPendente;
	
	/**
	 * Corresponde a venda que não foi atendida no ciclo de planejamento anterior,
	 * ou seja, é a quantidade que ficou negativa na dispproj no plano
	 * vigente anterior e que estava indicando
	 * que tinha venda naquele período que não havia sido atendida. 
	 * Campo que pode ser editado a cada ciclo de planejamento, juntamente com estoque inicial,
	 * mas que será calculado automaticamente a partir da parcela negativa do dispProj do periodo inicial do
	 * ciclo de planejamento anterior.
	 * No campo estoque inicial do modelo que também é editado a cada ciclo essa quantidade "faltando" no estoque não vai ser
	 * levada em consideração pois não permite edição de estoque negativo.
	 */
	private double estqEmFalta;
	
	//Campos referentes aos dados de quando é gerado um PMP vigente.

	/** Tempo de reposição do modelo relativo ao momento em que foi implementado o PMP vigente. (TR - em dias) */
	private double trPMP;	

	/** tamanho do lote (em peças) relativo ao momento em que foi implementado o PMP vigente. */
	private double tamLotePMP;

	/** cobertura percentual de estoques do modelo em relação ao próximo período do HP.
	 * relativo ao momento em que foi implementado o PMP vigente. */
	private double coberturaPMP;

	/** estoque inicial (em peças) para o modelo no início do HP 
	 * relativo ao momento em que foi implementado o PMP vigente. */
	private double estqInicModelPMP;
	

	/** O recebimento pendente entra na inclusão do plano somando com estoque inicial,
	 * pois é uma quantidade que já está sendo praticamente entregue no estoque,
	 * mas ainda está no chão de fabrica.
	 * 
	 * ATENCAO: quando O usuário for iniciar o novo ciclo de planejamento deve editar essa quantidade
	 * no campo recebimentopendente do modelo como sendo um feedback do chao de fabrica.
	 * relativo ao momento em que foi implementado o PMP vigente. */
	private double recebimentoPendentePMP;

	/**
	 * Corresponde a venda que não foi atendida no ciclo de planejamento anterior,
	 * ou seja, é a quantidade que ficou negativa na dispproj no plano
	 * vigente anterior e que estava indicando
	 * que tinha venda naquele período que não havia sido atendida. 
	 * Campo que pode ser editado a cada ciclo de planejamento, juntamente com estoque inicial,
	 * mas que será calculado automaticamente a partir da parcela negativa do dispProj do periodo inicial do
	 * ciclo de planejamento anterior.
	 * No campo estoque inicial do modelo que também é editado a cada ciclo essa quantidade "faltando" no estoque não vai ser
	 * levada em consideração pois não permite edição de estoque negativo.
	 * relativo ao momento em que foi implementado o PMP vigente. 
	 */
	private double estqEmFaltaPMP;
	
	/** Um modelo pertence a uma única familia */
	private Familia familia;

	private List<RecModel> recModels = new ArrayList<RecModel>();
	
	private List<TecModel> tecmodels = new ArrayList<TecModel>();
	
	private List<DeModPer> deModPers;
	
	private Set<PlanoModelo> planosModelo;

	/** Lista de registros do Plano mestre Vigente relativos a esse modelo */
	private List<PMP> pmps;

	
	
	// ********* Construtores *********

	public Modelo() {
	}

	// ********* Métodos get/set *********
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 15, unique=true)
	public String getCodModelo() {
		return codModelo;
	}

	public void setCodModelo(String codModelo) {
		this.codModelo = codModelo;
	}

	@Column(length = 50)
	public String getDescrModelo() {
		return descrModelo;
	}

	public void setDescrModelo(String descrModelo) {
		this.descrModelo = descrModelo;
	}

	@Column(nullable = false)
	public Boolean getFlagProducaoModel() {
		return flagProducaoModel;
	}

	public void setFlagProducaoModel(Boolean flagProducaoModel) {
		this.flagProducaoModel = flagProducaoModel;
	}

	public double getIndiceCob() {
		return indiceCob;
	}

	public void setIndiceCob(double indiceCob) {
		this.indiceCob = indiceCob;
	}

	public double getTuc() {
		return tuc;
	}

	public void setTuc(double tuc) {
		this.tuc = tuc;
	}

	public double getTamLote() {
		return tamLote;
	}

	public void setTamLote(double tamLote) {
		this.tamLote = tamLote;
	}

	public double getTr() {
		return tr;
	}

	public void setTr(double tr) {
		this.tr = tr;
	}

	public double getCobertura() {
		return cobertura;
	}

	public void setCobertura(double cobertura) {
		this.cobertura = cobertura;
	}

	public double getEstqInicModel() {
		return estqInicModel;
	}

	public void setEstqInicModel(double estqInicModel) {
		this.estqInicModel = estqInicModel;
	}

	public double getRecebimentoPendente() {
		return recebimentoPendente;
	}

	public double getEstqEmFalta() {
		return estqEmFalta;
	}

	public void setEstqEmFalta(double estqEmFalta) {
		this.estqEmFalta = estqEmFalta;
	}

	@Column(nullable = true)
	public double getTrPMP() {
		return trPMP;
	}

	public void setTrPMP(double trPMP) {
		this.trPMP = trPMP;
	}

	@Column(nullable = true)
	public double getTamLotePMP() {
		return tamLotePMP;
	}

	public void setTamLotePMP(double tamLotePMP) {
		this.tamLotePMP = tamLotePMP;
	}

	@Column(nullable = true)
	public double getCoberturaPMP() {
		return coberturaPMP;
	}

	public void setCoberturaPMP(double coberturaPMP) {
		this.coberturaPMP = coberturaPMP;
	}

	@Column(nullable = true)
	public double getEstqInicModelPMP() {
		return estqInicModelPMP;
	}

	public void setEstqInicModelPMP(double estqInicModelPMP) {
		this.estqInicModelPMP = estqInicModelPMP;
	}

	@Column(nullable = true)
	public double getRecebimentoPendentePMP() {
		return recebimentoPendentePMP;
	}

	public void setRecebimentoPendentePMP(double recebimentoPendentePMP) {
		this.recebimentoPendentePMP = recebimentoPendentePMP;
	}

	@Column(nullable = true)
	public double getEstqEmFaltaPMP() {
		return estqEmFaltaPMP;
	}

	public void setEstqEmFaltaPMP(double estqEmFaltaPMP) {
		this.estqEmFaltaPMP = estqEmFaltaPMP;
	}

	public void setRecebimentoPendente(double recebimentoPendente) {
		this.recebimentoPendente = recebimentoPendente;
	}

	//ATENÇÃO: Quando excluir modelo esta excluindo em cascata os RecModels desse modelo
	//Isso por conta do uso da propriedade "cascade=CascadeType.REMOVE" na relação.
	
	//O atributo mappedBy faz referencia ao ATRIBUTO da classe RecModel, ou seja, 
	//o valor desse atributo deve ser o nome do atributo em RecModel
	@OneToMany(mappedBy = "modelo", cascade=CascadeType.REMOVE)
	public List<RecModel> getRecModels() {
		return recModels;
	}

	public void setRecModels(List<RecModel> recModels) {
		this.recModels = recModels;
	}

	// ********* Métodos para Associações *********
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FAMILIA_ID", nullable = false)
	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	@OneToMany(mappedBy = "modelo", cascade=CascadeType.REMOVE)
	public List<TecModel> getTecModels() {
		return tecmodels;
	}

	public void setTecModels(List<TecModel> tecmodels) {
		this.tecmodels = tecmodels;
	}

	public void setDeModPers(List<DeModPer> deModPers) {
		this.deModPers = deModPers;
	}

	// 	 O atributo 'mappedBy' do JPA faz referencia ao atributo "modelo" da classe DeModPer
	// e por default, esta lista é inicializada como LAZY.
	@OneToMany(mappedBy="modelo", cascade=CascadeType.REMOVE)
	public List<DeModPer> getDeModPers() {
		return deModPers;
	}
	
	public void setPlanosModelo(Set<PlanoModelo> planosModelo) {
		this.planosModelo = planosModelo;
	}

	@OneToMany(mappedBy="modelo", cascade=CascadeType.REMOVE)
	public Set<PlanoModelo> getPlanosModelo() {
		return planosModelo;
	}
	

	@OneToMany(mappedBy = "modelo", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	public List<PMP> getPmps() {
		return pmps;
	}

	/**
	 * @param pMPs the pMPs to set
	 */
	public void setPmps(List<PMP> pmps) {
		this.pmps = pmps;
	}

	@Override
	public String toString() {
		return this.codModelo  + " - " + this.descrModelo;
	}
	
	/**
	 * Este método poder ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método  "equals(Object obj)".
	 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 *  @return int
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codModelo == null) ? 0 : codModelo.hashCode());
		return result;
	}

	/**
	 * 
	 * Este método pode ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método 
	 * "hashCode()". Estes metodos são necessários para se determinar um critério principal
	 * de igualdade entre 2 objetos. 
	 * 
	 *  @param Object
	 *  @return boolean
	 */
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Modelo)) {
			return false;
		}
		Modelo other = (Modelo) obj;
		if (codModelo == null) {
			if (other.codModelo != null) {
				return false;
			}
		} else if (!codModelo.equals(other.codModelo)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 *  Este método consiste em definir o critério de ordenação entre 2 objetos Modelo, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'codModelo', que é do tipo 
	 *  nativo 'String', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
	 *  
	 * @author walanem.junior
	 * @param Modelo
	 * @return int
	 */
	 @Override
	 public int compareTo(Modelo o) {

		 int valor = codModelo.compareTo(o.codModelo);
	     return (valor != 0 ? valor : 1); 
	 }
}
