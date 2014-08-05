 
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
import javax.persistence.Transient;

@NamedQueries(
	{
		@NamedQuery(name="PlanoModelo.recuperarPlPerModsPorPlanoModelo",
					query = "select pl " +
						  	"from PlanoModelo pl " +
						  	"left outer join fetch pl.plPerMods " +
						  	"where pl = ? "	
		),
		@NamedQuery(name="PlanoModelo.recuperarPlanoModeloPorCodigoModeloECodigoCadPlan",
					query = "select pl " +
						  	"from PlanoModelo pl " +
						  	"left outer join fetch pl.cadPlan cp " +
						  	"left outer join fetch pl.modelo m " +
						  	"where cp.codPlan = ? "	+
						  	"and m.codModelo = ? "
		)
	}
)	


@Entity
@Table(name = "PLANOMODELO")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_PLANOMODELO", allocationSize = 1)
public class PlanoModelo implements Serializable, Comparable<PlanoModelo> {

	private static final long serialVersionUID = 1L;
	
	
	public PlanoModelo(){
		 this.escorePlanPerModMin=0.0;
		 this.varEstqPercEscMin =0.0;
		 this.varProdDiaPercEscMin=0.0;
		 this.prodDiariaMediaPlanoModelo=0.0;
		 this.escore=0.0;
	}
	
	public PlanoModelo(CadPlan cadPlan, Modelo modelo){
		
		this.cadPlan = cadPlan;
		this.modelo = modelo;
	}

	/**
	 * identificador do registro planoModelo
	 * 
	 */
	private Long id;
	
	/**
	 * Define o escore resultante da avaliação do plano para um determinado modelo.
	 * corresponde a media dos escores de cada periodo do modelo(plPerMods).
	 */
	private Double escore;
	
	/**
	 * Informa se o modelo foi planejado ou não, caso tenha sido permite a implementação do plano.
	 * Funciona como um controle para o planejador, permitindo identificar os planos
	 * de que modelos ja foram analisados.
	 */
	private boolean modeloPlanejado;
	
	/**
	 * Objeto que representa a classe Modelo de um determinado plano.
	 */
	private Modelo modelo;
	
	/**
	 * Objeto que representa o cadPlan desse planoModelo
	 */
	private CadPlan cadPlan;

	/**
	 * booleano que indica se o campo produção pode ser recalculado pelo algoritmo ("livre')
	 * ou se é "fixo", não pode ser recalculado automaticamente pelo algoritmo.
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	 */
	private Boolean flagProducaoModelOriginal;
	
	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	 */
	private Double indiceCobOriginal;
	

	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	 */
	private Double tamLoteOriginal;

	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	 */
	private Double trOriginal;

	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	 */
	private Double coberturaOriginal;

	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	 */
	private Double estqInicModelOriginal;


	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	*
     */
	private Double estqEmFaltaOriginal;

	/**
	 * Este campo armazena o valor original da tabela modelo no momento em que foi gerado o plano.
	 * É util pois esse campo pode ser alterado na tabela de modelo mas nao sera alterado nos PlPerMod
	 * gerados anteriormente a essa alteração.
	*
     */
	private Double recebimentoPendenteOriginal;
	
	
	/**
	 * Menor escore dentre os escores obtidos para todos os periodos desse modelo nesse plano(plPerMod).
	 */
	private Double escorePlanPerModMin;
	


	/**
	 * Variacao do estoque percentual relativa ao periodo de escore minimo, para esse plano x modelo.
	 */
	private Double varEstqPercEscMin;
	
	/**
	 * Variacao percentual na producao diaria em lote relativa ao periodo de escore minimo, para esse plano x modelo.
	 */
	private Double varProdDiaPercEscMin;
	
	private Double prodDiariaMediaPlanoModelo;
	
	
	/**
	 * Objeto que representa os PlPerMods relacionado a um planoModelo.
	 */
	private Set<PlPerMod> plPerMods;
	
	/**
	 *   Variável criada unica e exclusivamente para fins de exibicao em tela da coleção de PlPerMods associada a
	 * entidade.  ATENÇÃO: Esta variável é exatamente uma CÓPIA do Set declarado acima, por isso, este atributo não
	 * é persistido, ou seja, é declarado como transiente.
	 * 
	 *@author wallanem figueiredo
	 *
	 */
	private transient List<PlPerMod> plPerModsList;

	/**
	 * Objeto que representa os PlPerMods relacionado a um planoModelo.
	 */
	private List<ExcecaoMens> excecaoMenss;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getEscore() {
		return escore;
	}

	public boolean isModeloPlanejado() {
		return modeloPlanejado;
	}

	public void setEscore(Double escore) {
		this.escore = escore;
	}

	public void setModeloPlanejado(boolean modeloPlanejado) {
		this.modeloPlanejado = modeloPlanejado;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	@ManyToOne
	@JoinColumn(name="MODELO_ID", nullable = false)
	public Modelo getModelo() {
		return modelo;
	}

	public void setCadPlan(CadPlan cadPlan) {
		this.cadPlan = cadPlan;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CADPLAN_ID", nullable = false)
	public CadPlan getCadPlan() {
		return cadPlan;
	}
	
	public Boolean getFlagProducaoModelOriginal() {
		return flagProducaoModelOriginal;
	}

	public Double getIndiceCobOriginal() {
		return indiceCobOriginal;
	}

	public Double getTamLoteOriginal() {
		return tamLoteOriginal;
	}

	public Double getTrOriginal() {
		return trOriginal;
	}

	public Double getCoberturaOriginal() {
		return coberturaOriginal;
	}

	public Double getEstqInicModelOriginal() {
		return estqInicModelOriginal;
	}

	public Double getRecebimentoPendenteOriginal() {
		return recebimentoPendenteOriginal;
	}

	public void setRecebimentoPendenteOriginal(Double recebimentoPendenteOriginal) {
		this.recebimentoPendenteOriginal = recebimentoPendenteOriginal;
	}

	public Double getEstqEmFaltaOriginal() {
		return estqEmFaltaOriginal;
	}

	public void setEstqEmFaltaOriginal(Double estqEmFaltaOriginal) {
		this.estqEmFaltaOriginal = estqEmFaltaOriginal;
	}

	public Double getEscorePlanPerModMin() {
		return escorePlanPerModMin;
	}

	public void setEscorePlanPerModMin(Double escorePlanPerModMin) {
		this.escorePlanPerModMin = escorePlanPerModMin;
	}

	
	public Double getVarEstqPercEscMin() {
		return varEstqPercEscMin;
	}

	public void setVarEstqPercEscMin(Double varEstqPercEscMin) {
		this.varEstqPercEscMin = varEstqPercEscMin;
	}

	
	public Double getVarProdDiaPercEscMin() {
		return varProdDiaPercEscMin;
	}

	public void setVarProdDiaPercEscMin(Double varProdDiaPercEscMin) {
		this.varProdDiaPercEscMin = varProdDiaPercEscMin;
	}

	public void setFlagProducaoModelOriginal(Boolean flagProducaoModelOriginal) {
		this.flagProducaoModelOriginal = flagProducaoModelOriginal;
	}

	public void setIndiceCobOriginal(Double indiceCobOriginal) {
		this.indiceCobOriginal = indiceCobOriginal;
	}

	public void setTamLoteOriginal(Double tamLoteOriginal) {
		this.tamLoteOriginal = tamLoteOriginal;
	}

	public void setTrOriginal(Double trOriginal) {
		this.trOriginal = trOriginal;
	}

	public void setCoberturaOriginal(Double coberturaOriginal) {
		this.coberturaOriginal = coberturaOriginal;
	}

	public void setEstqInicModelOriginal(Double estqInicModelOriginal) {
		this.estqInicModelOriginal = estqInicModelOriginal;
	}

	public void setPlPerMods(Set<PlPerMod> plPerMods) {
		this.plPerMods = plPerMods;
	}

	@OneToMany(mappedBy="planoModelo", cascade={CascadeType.REMOVE, CascadeType.PERSIST})
	public Set<PlPerMod> getPlPerMods() {
		return plPerMods;
	}

	@OneToMany(mappedBy="planoModelo")
	public List<ExcecaoMens> getExcecaoMenss() {
		return excecaoMenss;
	}

	public void setExcecaoMenss(List<ExcecaoMens> excecaoMenss) {
		this.excecaoMenss = excecaoMenss;
	}
	
	//  ------------------------ Métodos get() e set() dos atributos transientes ------------------------
	
	public void setPlPerModsList(List<PlPerMod> plPerModsList) {
		this.plPerModsList = plPerModsList;
	}

	@Transient
	public List<PlPerMod> getPlPerModsList() {
		return plPerModsList;
	}

	

	
	public Double getProdDiariaMediaPlanoModelo() {
		return prodDiariaMediaPlanoModelo;
	}

	public void setProdDiariaMediaPlanoModelo(Double prodDiariaMediaPlanoModelo) {
		this.prodDiariaMediaPlanoModelo = prodDiariaMediaPlanoModelo;
	}

	/**
	 * 	Aqui, estamos dizendo que a Ordem Natural de Objetos, ou seja a ordem que sao inseridos em uma lista,
	 *  do tipo 'PlanoModelo' deve se basear em como é implementada a ordem natural na classe 'Modelo' neste
	 *  caso por codigo do modelo.
	 *  
	 *  @param PlanoModelo
	 *  @return int
	 */
	@Override
	public int compareTo(PlanoModelo o) {
		return this.modelo.compareTo(o.getModelo());
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cadPlan == null) ? 0 : cadPlan.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		return result;
	}

	/**
	 * 
	 * Este método é gerado AUTOMATICAMENTE pelo Java , juntamente com o método 
	 * "hashCode()". Eles São necessários pois como o critério principal de igualdade entre 2 objetos PlanoModelo é o campo 
	 * 'cadPlan' exige que haja a implementação deste método.
	 *  Caso contrário, fica impossível diferenciar 2 objetos do tipo PlanoModelo, e consequentemente temos a impressão
	 * de que 'sumiram' registros, entre outros possiveis problemas. 
	 * 
	 * OBS: É PRIMORDIAL DAR ATENÇÃO PARA ESTE DETALHE (PRINCIPALMENTE QUANDO TRABALHARMOS COM ESTRUTURAS COMO 'Set')
	 * 
	 * @author felipe.arruda
	 * @param Object 
	 * @return int
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
		if (!(obj instanceof PlanoModelo)) {
			return false;
		}
		PlanoModelo other = (PlanoModelo) obj;
		if (cadPlan == null) {
			if (other.cadPlan != null) {
				return false;
			}
		} else if (!cadPlan.equals(other.cadPlan)) {
			return false;
		}
		if (modelo == null) {
			if (other.modelo != null) {
				return false;
			}
		} else if (!modelo.equals(other.modelo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PlanoModelo [cadPlan=" + cadPlan + ", modelo=" + modelo + "]";
	}
	
}
