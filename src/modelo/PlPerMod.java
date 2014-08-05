 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
/**
 * package relativo a todas as classes de negocio.
 * relativo as classes bean
 * 
 */
package modelo;

import java.io.Serializable;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@NamedQueries(
	{
	@NamedQuery(name="PlPerMod.recuperaPlPerModPorPlanoModeloEPerioPM",
				query = "select plpermod from PlPerMod plpermod " +
						"left outer join fetch plpermod.planoModelo pl " +
						"left outer join plpermod.perioPM pm " +
						"where pl = ? " +
						"and pm = ? "
		),
	@NamedQuery(name="PlPerMod.recuperaListaDePlPerMod",
				query = "select pl from PlPerMod pl "
		),
	@NamedQuery(name="PlPerMod.recuperaListaDePlPerModPorPlanoModelo",
				query = "select pl from PlPerMod pl " +
						"left outer join pl.planoModelo planmod " +	 
						"where planmod = ? " 
		),
	@NamedQuery(name="PlPerMod.recuperaListaDePlPerModPorPlanoModeloEPerioPMApartirDePerioPM",
				query = "select pl from PlPerMod pl " +
						"left outer join pl.planoModelo planmod " +	 
						"left outer join pl.perioPM pm " +
						"where planmod = ? " +
						"and pm.periodoPM >= ? " 
		),					
	@NamedQuery(name="PlPerMod.recuperaIntervaloDePlPerModPorPlanoModeloEIntervaloDePerioPM",
				query = "select pl from PlPerMod pl " +
						"left outer join pl.planoModelo planmod " +	 
						"left outer join pl.perioPM pm " +
						"where planmod = ? " +
						"and pm.periodoPM >= ? " +
						"and pm.periodoPM <= ? "
		)					
		
	}		
)
/**
 * Classe respons�vel pela defini��o dos planos mestre de produ��o
 * cada registro se refere a combina��o de plano, por modelo, por periodo
 * 
 */
@Entity
@Table(name = "PLPERMOD")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_PLPERMOD", allocationSize = 1)
public class PlPerMod implements Serializable, Comparable<PlPerMod> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * identificador do registro plpermod
	 * 
	 */
	private Long id;
	
	/**
	 * Objeto PerioPM que indica o periodo de recebimento do Plano Mestre
	 * 
	 */
	private PerioPM perioPM;
	
	/**
	 * Vendas projetadas do modelo, em pe�as, no per�odo.
	 * Informa��o originaria do DeModPer, mas passivel de ser alterada
	 * aqui de forma independente.
	 * 
	 */
	private Double vendasModel;

	private Double vendasAmortModel;
	
	/**
	 * Pedidos cadastrados do modelo, em pe�as no per�odo.
	 * Informa��o originaria do DeModPer, mas passivel de ser alterada
	 * aqui de forma independente.
	 * 
	 */
	private Double pedidosModel;
	
	/**
	 * disponibilidade projetada do modelo, em pe�as, no per�odo
	 * 
	 */
	private Double dispProjModel;
	
	/**
	 * cobertura % de estoque desejada do modelo em rela��o �s vendas projetadas
	 * no per�odo
	 */
	private Double coberturaModel;
	
	/**
	 * Tempo Unitario de Costura por pe�a do modelo.
	 * Proveniente do arquivo modelo.
	 */
	private Double tuc;
	
	/**
	 * produ��o do modelo, em pe�as, no per�odo
	 * 
	 */
	private Double producaoModel;
	
	/**
	 * produ��o do modelo, em lotes, no per�odo
	 * 
	 */
	private Double prodLoteModel;
	
	/**
	 * produ��o di�ria do modelo, em lotes, no per�odo
	 */
	private Double prodDiariaLoteModel;

	/**
	 * Quantidade que deveria ter sido produzida antes do in�cio do HP
	 * 
	 */
	private Double inicioProdAntesHP;
	
	/**
	 * booleano que indica se o campo produ��o pode ser recalculado pelo algoritmo ("livre')
	 * ou se � "fixo", e n�o pode ser recalculado automaticamente pelo algoritmo
	 */
	private Boolean flagProducaoModel;

	/**
	 * Escore obtido nesse periodo para a defini��o da produ��o para esse modelo nesse plano.
	 * Gerado na parte fuzzy do sistema
	 * 
	 */
	private Double escorePlanPerMod;
	
	/**
	 * Varia��o percentual em rela��o ao estoque desejado.
	 * Corresponde a diferen�a percentual entre o estoque de seguran�a desejado e a disponibilidade projetada. 
	 * Vari�vel usada como input na parte fuzzy do sistema
	 * Calculada como:  varEstqPerc(t) =(  (dispProjModel(t) - EstqSeguran�aDesejado(t) )/EstqSeguran�aDesejado(t) ) * 100
	 * 
	 */
	private Double varEstqPerc;
	
	/**
	 * Varia��o percentual em rela��o a ProdDiariaMedia no HP.
	 * Variacao percentual entre a producao diaria em lotes do periodo e a producao diaria media do plano para todos
	 * os periodos desse modelo(plPerMods).
	 * Vari�vel usada na parte fuzzy do sistema
	 */
	private Double varProdDiaPerc;
	
	/**
	 * Objeto PlanoModelo relativo a este plpermod
	 */
	private PlanoModelo planoModelo;
	
	/**
	 * Per�odo de In�cio da Produ��o, defasado em rela��o ao PerioPM que indica o periodo
	 *  de recebimento do Plano Mestre, a partir do TR do modelo.
	 */
	private Integer periodoPMInicioPMP;
	
	
	// ---------------------------- Atributos Transientes ----------------------------

	/**
	 *informa para cada periodo qual o estoque de seguranca desejado.
	 *� calculado a partir da demanda maxima do proximo periodo e da cobertura do modelo.
	 * Vari�vel usada na parte fuzzy do sistema
	 */
	private Double estqSegDesejado; 
	
	/**
	 * campo transiente comum a todas as telas de analise de capacidade
	 * (Analise de capacidade de maquina, de tecido e de recursos)
	 * Participa��o % do modelo na necessidade total do recurso/tecido/maquina sendo analisado.
	 */
	private transient Double participacaoPercentual;	// Comum a TODOS os tipos de Analise!
	

	// a) Analise de Maquina
	/**
	 * Produ��o diaria do modelo no periodo, em lote convertidas para minutos.
	 */
	private transient Double prodDiariaLoteMin;
	
	private transient Double producaoDiariaDefasada;
	
	// b) Analise de Tecido

	/**
	 * Consumo do Tecido pelo modelo, por lote, em metros.
	 * Proveniente da tabela TecModel
	 */
	private transient Double consumoLoteMt;
	
	/**
	 * Campo calculado que informa o consumo por lote em Kg do Tecido para um modelo
	 * a partir do consumoPorLoteMt, PercentualDePerda e FatorRendimento. 
	 */
	private transient Double consumoLoteKg;
	
	/**
	 * � o consumo diario em Kg para esse Tecido de um determinado Modelo
	 * calculado a partir da formula:
	 * setConsumoDiarioKg(plPerMod.getProdDiariaLoteModel() * consumoPorLoteKg)
	 */
	private transient Double consumoDiarioKg;
	
	// c) Analise de Recurso
	/**
	 * Campo transient proveniente da tabela RecModel(ConsumoUnit)
	 * Consumo Unitario do Modelo para um recurso 
	 */
	private transient Double consumoUnitario;
	
	/**
	 * Consumo diario em pecas de um recurso, em um periodo para um modelo.
	 * 
	 * consumoDiario = plPerMod.getProdDiariaLoteModel() *
	 *  				plPerMod.getPlanoModelo().getModelo().getTamLote() *
	 *  				 recModel.getConsumoUnit();
	 */
	private transient Double consumoDiario;
	
	/**
	 * � o custo diario de um recurso para um determinado modelo num periodo.
	 * 
	 * setCustoDiario(consumoDiario * recurso.getCustoUnit())
	 */
	private transient Double custoDiario;
	
	
	// ********* Construtores *********

	public PlPerMod(){
		this.vendasModel = 0.0;
		this.vendasAmortModel = 0.0;
		this.pedidosModel = 0.0;
		this.dispProjModel = 0.0;
		this.coberturaModel = 0.0;
		this.tuc = 0.0;
		this.producaoModel = 0.0;
		this.prodLoteModel = 0.0;
		this.prodDiariaLoteModel = 0.0;
		this.inicioProdAntesHP = 0.0;
		this.flagProducaoModel = false;
		this.escorePlanPerMod = 0.0;
		this.varEstqPerc = 0.0;
		this.periodoPMInicioPMP = 0;
		this.varProdDiaPerc = 0.0;
		this.estqSegDesejado = 0.0;
		
	}
	
	public PlPerMod(Double vendasModelo, Double pedidosModelo, boolean flagModelo) {
		
		this.vendasModel = vendasModelo;
		this.vendasAmortModel = 0.0;
		this.pedidosModel = pedidosModelo;
		this.dispProjModel = 0.0;
		this.coberturaModel = 0.0;
		this.tuc = 0.0;
		this.producaoModel = 0.0;
		this.prodLoteModel = 0.0;
		this.prodDiariaLoteModel = 0.0;
		this.inicioProdAntesHP = 0.0;
		this.flagProducaoModel = flagModelo;
		this.escorePlanPerMod = 0.0;
		this.varEstqPerc = 0.0;
		this.periodoPMInicioPMP = 0;
		this.varProdDiaPerc = 0.0;
		this.estqSegDesejado = 0.0;
	}

	// ********* M�todos get() / set() *********
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name="PLANOMODELO_ID")
	public PlanoModelo getPlanoModelo() {
		return planoModelo;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PERIOPM_ID")
	public PerioPM getPerioPM() {
		return perioPM;
	}

	public Double getVendasModel() {
		return vendasModel;
	}

	public Double getVendasAmortModel() {
		return vendasAmortModel;
	}

	public Double getPedidosModel() {
		return pedidosModel;
	}

	public Double getDispProjModel() {
		return dispProjModel;
	}

	public Double getCoberturaModel() {
		return coberturaModel;
	}

	public Double getTuc() {
		return tuc;
	}


	public Double getProducaoModel() {
		return producaoModel;
	}

	public Double getProdLoteModel() {
		return prodLoteModel;
	}

	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}

	public Double getInicioProdAntesHP() {
		return inicioProdAntesHP;
	}

	public Boolean getFlagProducaoModel() {
		return flagProducaoModel;
	}

	public Double getEscorePlanPerMod() {
		return escorePlanPerMod;
	}

	public Double getVarEstqPerc() {
		return varEstqPerc;
	}
	

	public Double getVarProdDiaPerc() {
		return varProdDiaPerc;
	}

	public Integer getPeriodoPMInicioPMP() {
		return periodoPMInicioPMP;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setPlanoModelo(PlanoModelo planoModelo) {
		this.planoModelo = planoModelo;
	}

	public void setPerioPM(PerioPM perioPM) {
		this.perioPM = perioPM;
	}

	public void setVendasModel(Double vendasModel) {
		this.vendasModel = vendasModel;
	}

	public void setVendasAmortModel(Double vendasAmortModel) {
		this.vendasAmortModel = vendasAmortModel;
	}

	public void setPedidosModel(Double pedidosModel) {
		this.pedidosModel = pedidosModel;
	}

	public void setDispProjModel(Double dispProjModel) {
		this.dispProjModel = dispProjModel;
	}

	public void setCoberturaModel(Double coberturaModel) {
		this.coberturaModel = coberturaModel;
	}
	
	public void setTuc(Double tuc) {
		this.tuc = tuc;
	}

	public void setProducaoModel(Double producaoModel) {
		this.producaoModel = producaoModel;
	}

	public void setProdLoteModel(Double prodLoteModel) {
		this.prodLoteModel = prodLoteModel;
	}

	public void setProdDiariaLoteModel(Double prodDiariaLoteModel) {
		this.prodDiariaLoteModel = prodDiariaLoteModel;
	}

	public void setInicioProdAntesHP(Double inicioProdAntesHP) {
		this.inicioProdAntesHP = inicioProdAntesHP;
	}

	public void setFlagProducaoModel(Boolean flagProducaoModel) {
		this.flagProducaoModel = flagProducaoModel;
	}

	public void setEscorePlanPerMod(Double escorePlanPerMod) {
		this.escorePlanPerMod = escorePlanPerMod;
	}

	public void setVarEstqPerc(Double varEstqPerc) {
		this.varEstqPerc = varEstqPerc;
	}
	
	public void setVarProdDiaPerc(Double varProdDiaPerc) {
		this.varProdDiaPerc = varProdDiaPerc;
	}

	public void setPeriodoPMInicioPMP(Integer periodoPMInicioPMP) {
		this.periodoPMInicioPMP = periodoPMInicioPMP;
	}
	
	public void setProdDiariaLoteMin(Double prodDiariaLoteMin) {
		this.prodDiariaLoteMin = prodDiariaLoteMin;
	}

	public Double getEstqSegDesejado() {
		return estqSegDesejado;
	}

	public void setEstqSegDesejado(Double estqSegDesejado) {
		this.estqSegDesejado = estqSegDesejado;
	}
	@Transient
	public Double getProdDiariaLoteMin() {
		return prodDiariaLoteMin;
	}
	
	public void setParticipacaoPercentual(Double participacaoPercentual) {
		this.participacaoPercentual = participacaoPercentual;
	}

	@Transient
	public Double getParticipacaoPercentual() {
		return participacaoPercentual;
	}
	
	public void setProducaoDiariaDefasada(Double producaoDiariaDefasada) {
		this.producaoDiariaDefasada = producaoDiariaDefasada;
	}

	@Transient
	public Double getProducaoDiariaDefasada() {
		return producaoDiariaDefasada;
	}
	
	@Transient
	public Double getConsumoLoteMt() {
		return consumoLoteMt;
	}

	public void setConsumoLoteMt(Double consumoLoteMt) {
		this.consumoLoteMt = consumoLoteMt;
	}

	@Transient
	public Double getConsumoLoteKg() {
		return consumoLoteKg;
	}

	public void setConsumoLoteKg(Double consumoLoteKg) {
		this.consumoLoteKg = consumoLoteKg;
	}

	@Transient
	public Double getConsumoDiarioKg() {
		return consumoDiarioKg;
	}

	public void setConsumoDiarioKg(Double consumoDiarioKg) {
		this.consumoDiarioKg = consumoDiarioKg;
	}

	@Transient
	public Double getConsumoUnitario() {
		return consumoUnitario;
	}

	public void setConsumoUnitario(Double consumoUnitario) {
		this.consumoUnitario = consumoUnitario;
	}

	@Transient
	public Double getConsumoDiario() {
		return consumoDiario;
	}

	public void setConsumoDiario(Double consumoDiario) {
		this.consumoDiario = consumoDiario;
	}

	@Transient
	public Double getCustoDiario() {
		return custoDiario;
	}

	public void setCustoDiario(Double custoDiario) {
		this.custoDiario = custoDiario;
	}

	/**
	 * Este m�todo consiste em definir o crit�rio de ordena��o entre 2 objetos PlPerMod, que deve estar 
	 *  associado a um de seus atributos. No caso � PlanoModelo.
	 * Aqui estamos dizendo que a Ordem Natural de Objetos do tipo 'PlPerMod' 
	 * deve se basear em como � implementada
	 * a ordem natural na classe 'PlanoModelo'
	 * 
	 * @param PlPerMod
	 * @return int
	 */
	@Override
	public int compareTo(PlPerMod o) {
		return this.planoModelo.compareTo(o.getPlanoModelo());
	}

	/**
	 * Este m�todo poder ser gerado AUTOMATICAMENTE pelo Java , juntamente com o m�todo  "equals(Object obj)".
	 * Eles S�o necess�rios para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: � primoridal dar aten��o para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 * @return int
	 */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((perioPM == null) ? 0 : perioPM.hashCode());
		result = prime * result + ((planoModelo == null) ? 0 : planoModelo.hashCode());
		return result;
	}
	
	/**
	 * 
	 * Este m�todo � gerado AUTOMATICAMENTE pelo Java , juntamente com o m�todo 
	 * "hashCode()". Eles S�o necess�rios pois como o crit�rio principal de igualdade entre 2 objetos PlPerMod � o campo 
	 * 'planoModelo' exige que haja a implementa��o deste m�todo.
	 *  Caso contr�rio, fica imposs�vel diferenciar 2 objetos do tipo PlPerMod, e consequentemente temos a impress�o
	 * de que 'sumiram' registros, entre outros possiveis problemas. 
	 * 
	 * OBS: � PRIMORDIAL DAR ATEN��O PARA ESTE DETALHE (PRINCIPALMENTE QUANDO TRABALHARMOS COM ESTRUTURAS COMO 'Set')
	 * 
	 * @author felipe.arruda
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
		if (!(obj instanceof PlPerMod)) {
			return false;
		}
		PlPerMod other = (PlPerMod) obj;
		if (perioPM == null) {
			if (other.perioPM != null) {
				return false;
			}
		} else if (!perioPM.equals(other.perioPM)) {
			return false;
		}
		if (planoModelo == null) {
			if (other.planoModelo != null) {
				return false;
			}
		} else if (!planoModelo.equals(other.planoModelo)) {
			return false;
		}
		return true;
	}
}
