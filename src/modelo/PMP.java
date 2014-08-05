 
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

/**
 * Classe relativa ao bean PMP que informa o Plano Mestre de Produ��o Vigente por modelo, por per�odo
 * 
 *  Esta tabela � excluida e gerada novamente a partir do plpermod de um plano selecionado na fun��o
 *  "implementa plano vigente"
 *  
 * @author felipe.arruda/dayse.arruda
 *
 */
@NamedQueries( 
		{
			@NamedQuery(name = "PMP.recuperaPMPComPerioPMVig", 
					query = "select pmp from PMP pmp "
					+ "left outer join fetch pmp.perioPMVig " + "where pmp = ?"),
					
			@NamedQuery(name="PMP.recuperaPMPPorModeloEPerioPMVig",
					query = "select pmp from PMP pmp " +
							"left outer join pmp.modelo modelo " +
							"where modelo = ? " +
							"and pmp.perioPMVig = ? "
			),
			@NamedQuery(name="PMP.recuperaListaDePMPs",
					query = "select pmp from PMP pmp "
			),			
			@NamedQuery(name = "PMP.recuperaListaDePMPsComPerioPMVigs", 
					query = "select pmp from PMP pmp "
							+ "left outer join fetch pmp.perioPMVig "
			),
			@NamedQuery(name = "PMP.recuperaListaDePMPsPorModeloComPerioPMVigs", 
					query = "select pmp from PMP pmp "
							+ "left outer join fetch pmp.perioPMVig " +
							"left outer join pmp.modelo modelo " +
							"where modelo = ? "
			),
			@NamedQuery(name="PMP.recuperaIntervaloDePMPPorModeloEIntervaloDePerioPMVig",
					query = "select pmp from PMP pmp " +
							"left outer join pmp.modelo modelo " +	 
							"left outer join pmp.perioPMVig pmv " +
							"where modelo = ? " +
							"and pmv.periodoPM >= ? " +
							"and pmv.periodoPM <= ? "
			)					
		
		}		
)

@Entity
@Table(name = "PMP")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_PMP", allocationSize = 1)
public class PMP implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identificador  plano mestre vigente*/		 
	private Long id;
	
	/** modelo a que se refere o registro do plano mestre vigente */
	private Modelo modelo;

	/** Periodo do Plano Mestre Vigente relativo ao HP Vigente */ 
	private PerioPMVig perioPMVig;
	
	/**
	 * Vendas Projetadas do Modelo, em pe�as no per�odo do Plano Mestre Vigente 
	 */
	private Double vendasModel;

	/**
	 * Pedidos em carteira do Modelo, em pe�as no per�odo do Plano Mestre Vigente 
	 */
	private Double pedidosModel;
	
	/**
	 * Produ��o do Modelo, em pe�as no per�odo do Plano Mestre Vigente 
	 */
	private Double producaoModel;
	
	/**
	 * Produ��o do Modelo, em lotes no per�odo do Plano Mestre Vigente 
	 */
	private Double prodLoteModel;
	
	/**
	 * Produ��o di�ria do Modelo, em lotes no per�odo do Plano Mestre Vigente 
	 */
	private Double prodDiariaLoteModel;

	/**
	 * Per�odo no qual deve ser iniciada a produ��o, considerando o TR do modelo
	 */
	private int periodoPMInicioPMP;
	
	/**
	 * Disponibilidade de Entrega no periodo
	 */
	private Double disponibEntrega;

	/**
	 * Disponibilidade Projetado do Modelo no per�odo
	 */
	private Double dispProjModel;
	
	/**
	 * Cobertura % de estoque do Modelo no per�odo
	 * 
	 */
	private Double coberturaModel;
	
	/**
	 * Escore obtido nesse per�odo para a defini��o da produ��o para esse modelo
	 * 
	 * Herdado do plpermod
	 * 
	 */
	private Double escorePlanPerMod;

	/**
	 * varia��o percentual em rela��o ao estoque desejado.
	 * 
	 * Herdada do Plpermod
	 * 
	 */
	private Double varEstqPerc;
	
	/**
	 * varia��o percentual em rela��o a ProdDiariaMedia no HP.
	 * 
	 * Herdada do Plpermod
	 * 
	 */
	private Double varProdDiaPerc;
	
	
	// ********* Construtores *********

	public PMP(){
		
	}
	
	public PMP(Modelo modelo, PerioPMVig perioPMVig, Double vendasModel,
			Double pedidosModel, Double producaoModel, Double prodLoteModel,
			Double prodDiariaLoteModel, 
			int periodoPMInicioPMP, Double disponibEntrega,
			Double dispProjModel, Double coberturaModel,
			Double escorePlanPerMod, Double varEstqPerc, Double varProdDiaPerc) {
		super();
		this.modelo = modelo;
		this.perioPMVig = perioPMVig;
		this.vendasModel = vendasModel;
		this.pedidosModel = pedidosModel;
		this.producaoModel = producaoModel;
		this.prodLoteModel = prodLoteModel;
		this.prodDiariaLoteModel = prodDiariaLoteModel;
		this.periodoPMInicioPMP = periodoPMInicioPMP;
		this.disponibEntrega = disponibEntrega;
		this.dispProjModel = dispProjModel;
		this.coberturaModel = coberturaModel;
		this.escorePlanPerMod = escorePlanPerMod;
		this.varEstqPerc = varEstqPerc;
		this.varProdDiaPerc = varProdDiaPerc;
	}
	

	// ********* M�todos get() / set() *********
	



	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MODELO_ID", nullable = false)
	public Modelo getModelo() {
		return modelo;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PERIOPMVIG_ID")
	public PerioPMVig getPerioPMVig() {
		return perioPMVig;
	}

	public Double getVendasModel() {
		return vendasModel;
	}


	public Double getPedidosModel() {
		return pedidosModel;
	}
	
	public Double getDisponibEntrega() {
		return disponibEntrega;
	}


	public Double getDispProjModel() {
		return dispProjModel;
	}

	public Double getCoberturaModel() {
		return coberturaModel;
	}

	/**
	 * Decidimos implementar hashCode() e equals gradativamente em todas as entidades, para permitir compara��o
	 * ao usarmos set ao inv�s de list.  Sem isso algumas fun��es n�o funcionar�o corretamente, tais como recupera��o 
	 * do registro, ou at� mesmo a pagina��o master/detail.
	 * 
	 * Algumas entidades ainda n�o precisaram por serem entidades b�sicas ou n�o usarem set
	 * PMP por enquanto n�o demonstrou necessidade de uso, mas implementamos para padronizar
	 * 
	 * S�o colocados no hashCode os campos considerados �teis para identifica��o do registro.
	 * N�o � suficiente a coloca��o apenas do id que � a chave prim�ria, pois � poss�vel que o
	 * set seja usado num momento em que o registro ainda est� destacado, ou seja ainda n�o tem id.
	 * 
	 * Foram colocados os campos que correspondem a chave concatenada original na vers�o em delphi.
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime * result
				+ ((perioPMVig == null) ? 0 : perioPMVig.hashCode());
		return result;
	}


	/**
	 * Decidimos implementar hashCode() e equals gradativamente em todas as entidades, para permitir compara��o
	 * ao usarmos set ao inv�s de list.  Sem isso algumas fun��es n�o funcionar�o corretamente, tais como recupera��o 
	 * do registro, ou at� mesmo a pagina��o master/detail.
	 * 
	 * Algumas entidades ainda n�o precisaram por serem entidades b�sicas ou n�o usarem set
	 * PMP por enquanto n�o demonstrou necessidade de uso, mas implementamos para padronizar
	 * 
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
		if (!(obj instanceof PMP)) {
			return false;
		}
		PMP other = (PMP) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (modelo == null) {
			if (other.modelo != null) {
				return false;
			}
		} else if (!modelo.equals(other.modelo)) {
			return false;
		}
		if (perioPMVig == null) {
			if (other.perioPMVig != null) {
				return false;
			}
		} else if (!perioPMVig.equals(other.perioPMVig)) {
			return false;
		}
		return true;
	}


/* ****************** m�todos get  e set *******************/
	public Double getProducaoModel() {
		return producaoModel;
	}

	public Double getProdLoteModel() {
		return prodLoteModel;
	}

	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}


	public int getPeriodoPMInicioPMP() {
		return periodoPMInicioPMP;
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

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public void setPerioPMVig(PerioPMVig perioPMVig) {
		this.perioPMVig = perioPMVig;
	}

	public void setVendasModel(Double vendasModel) {
		this.vendasModel = vendasModel;
	}


	public void setPedidosModel(Double pedidosModel) {
		this.pedidosModel = pedidosModel;
	}

	public void setDisponibEntrega(Double disponibEntrega) {
		this.disponibEntrega = disponibEntrega;
	}
	
	public void setDispProjModel(Double dispProjModel) {
		this.dispProjModel = dispProjModel;
	}

	public void setCoberturaModel(Double coberturaModel) {
		this.coberturaModel = coberturaModel;
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


	public void setPeriodoPMInicioPMP(int periodoPMInicioPMP) {
		this.periodoPMInicioPMP = periodoPMInicioPMP;
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


}
