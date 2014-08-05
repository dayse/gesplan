 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
/**
 * package relativo as classes de negocio
 * Com as classes bean
 * 
 */
package modelo;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Classe relativa ao Bean PerioPM que informa os períodos do  Plano Mestre da Produção
 * 
 * 
 * @author daysemou
 *
 */

	@NamedQueries(
		{	
			@NamedQuery
			(	name = "PerioPM.recuperaPerioPM",
				query = "select p from PerioPM p where p.id = ?"
			),
			@NamedQuery
			(	name = "PerioPM.recuperaPerioPMPorPeriodoPM",
				query = "select p from PerioPM p where p.periodoPM = ? "
			),
			@NamedQuery
			(	name = "PerioPM.recuperaListaDePerioPMs",
				query = "select p from PerioPM p order by p.periodoPM"
			),
			@NamedQuery
			(	name = "PerioPM.recuperaListaDePerioPMsComPerioPAP",
				query = "select p from PerioPM p "+
						"left outer join fetch p.perioPAP pap " +
						"order by p.periodoPM"
			),
			@NamedQuery
			(	name = "PerioPM.recuperaListaPaginadaDePerioPMs",
				query = "select distinct p from PerioPM p order by p.periodoPM"
			),
			@NamedQuery
			(	name = "PerioPM.recuperaListaPaginadaDePerioPMsCount",
				query = "select count (distinct p) from PerioPM p"
			),
			@NamedQuery
			(	name = "PerioPM.recuperaPerioPMComDeModPers",
					query = "select p from PerioPM p " +
						    "left outer join fetch p.deModPers " +
							"where p = ? "
			),
			@NamedQuery
			(	name = "PerioPM.recuperaIntervaloDePerioPMs",
					query = "select p from PerioPM p " +
							"where p.periodoPM >= ? " +
							"and p.periodoPM <= ? " +
							"order by p.periodoPM "
			),
			@NamedQuery
			(	name = "PerioPM.recuperaListaDePerioPMsPorPerioPAP",
					query = "select p from PerioPM p " +
							"where p.perioPAP = ? " +
							"order by p.periodoPM"
			)
		}
	)
		

@Entity
@Table(name="PerioPM")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PERIOPM",
		           allocationSize=1)
		           

public class PerioPM implements Serializable, Comparable<PerioPM> {

		private static final long serialVersionUID = 1L;

		/** identificador do periodo do plano mestre */		 
		private Long id;
	
		/** numero do periodo do plano mestre */		 
		private int periodoPM;

		/** data inicial relativa ao periodo do plano mestre */		 
		private Calendar dataInicial;

		/** data final relativa ao periodo do plano mestre */		 
		private Calendar dataFinal;

		/** Numero de dias uteis na matriz no periodo do plano mestre */		 
		private double numDiasUteisMatriz;
		
		/** Numero de dias uteis na unidade 2 no periodo do plano mestre */		 
		private double numDiasUteisU2;
		
		private PerioPAP perioPAP;
		
		private List<DeModPer> deModPers;
		
		private List<CapacRec> capacRecs;

		/**
		 * Lista da entidade visão que informa a capacidade diária por período disponível do tecido 
		 * na matriz, calculada em função da producaoDiariaMaxUnidade2
		 */
		private transient List<CapacTecView> capacTecViews = new ArrayList<CapacTecView>(); 
		

		// ********* Construtores *********
		
		public PerioPM() {
		}

		public PerioPM(int periodoPM, Calendar dataInicial,
				Calendar dataFinal, double numDiasUteisMatriz, double numDiasUteisU2) {
			this.periodoPM = periodoPM;
			this.dataInicial = dataInicial;
			this.dataFinal = dataFinal;
			this.numDiasUteisMatriz = numDiasUteisMatriz;
			this.numDiasUteisU2 = numDiasUteisU2;
		}


		// ********* Métodos do Tipo Get e Set *********
		@Id
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
		@Column(name="ID")
		public Long getId() {
			return id;
		}
		
		@SuppressWarnings("unused")
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * @return the periodoPM
		 */
		@Column(nullable=false, unique=true)
		public int getPeriodoPM() {
			return periodoPM;
		}

		/**
		 * @param periodoPM the periodoPM to set
		 */
		public void setPeriodoPM(int periodoPM) {
			this.periodoPM = periodoPM;
		}

		/**
		 * @return the dataInicial
		 */
		@Temporal(value = TemporalType.DATE)
		public Calendar getDataInicial() {
			return dataInicial;
		}

		/**
		 * @param dataInicial the dataInicial to set
		 */
		public void setDataInicial(Calendar dataInicial) {
			this.dataInicial = dataInicial;
		}

		/**
		 * @return the dataFinal
		 */
		@Temporal(value = TemporalType.DATE)
		public Calendar getDataFinal() {
			return dataFinal;
		}

		/**
		 * @param dataFinal the dataFinal to set
		 */
		public void setDataFinal(Calendar dataFinal) {
			this.dataFinal = dataFinal;
		}

		/**
		 * @return the numDiasUteisMatriz
		 */
		public double getNumDiasUteisMatriz() {
			return numDiasUteisMatriz;
		}

		/**
		 * @param numDiasUteisMatriz the numDiasUteisMatriz to set
		 */
		public void setNumDiasUteisMatriz(double numDiasUteisMatriz) {
			this.numDiasUteisMatriz = numDiasUteisMatriz;
		}

		/**
		 * @return the numDiasUteisU2
		 */
		public double getNumDiasUteisU2() {
			return numDiasUteisU2;
		}

		/**
		 * @param numDiasUteisU2 the numDiasUteisU2 to set
		 */
		public void setNumDiasUteisU2(double numDiasUteisU2) {
			this.numDiasUteisU2 = numDiasUteisU2;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
		// usar lazy normalmente para evitar acabar levando todo o bd
		@JoinColumn(name = "PERIOPAP_ID",nullable = false)
		
		public PerioPAP getPerioPAP() {
			return perioPAP;
		}

		public void setPerioPAP(PerioPAP perioPAP) {
			this.perioPAP = perioPAP;
		}

		public void setDeModPers(List<DeModPer> deModPers) {
			this.deModPers = deModPers;
		}

		@OneToMany(mappedBy="periodo")
		public List<DeModPer> getDeModPers() {
			return deModPers;
		}

		public void setCapacRecs(List<CapacRec> capacRecs) {
			this.capacRecs = capacRecs;
		}
		/** mappedBy usa o nome do atributo que faz a ligação no lado one */
		@OneToMany(mappedBy="perioPM")
		public List<CapacRec> getCapacRecs() {
			return capacRecs;
		}


	    @Transient
		public List<CapacTecView> getCapacTecViews() {
			return capacTecViews;
		}

		public void setCapacTecViews(List<CapacTecView> capacTecViews) {
			this.capacTecViews = capacTecViews;
		}
		

		/**
		 * 
		 *  Este método consiste em definir o critério de ordenação entre 2 objetos PerioPM, que deve estar 
		 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'periodoPM', que é do tipo 
		 *  nativo 'int', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
		 *
		 * @author walanem.junior
		 * @param PerioPM
		 * @return int
		 * 
		 */
		@Override
		public int compareTo(PerioPM o) {
			return this.periodoPM < o.periodoPM ? -1 : this.periodoPM > o.periodoPM ? 1 : 0;
		}
		@Override
		public String toString(){
			return "Periodo - "+this.periodoPM;
		}

		
		/**
		 * Este método poder ser gerado AUTOMATICAMENTE pelo Java, juntamente com o método "equals(Object obj)".
		 * Eles São necessários para determinarmos um critério de igualdade entre 2 objetos.
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
			result = prime * result + periodoPM;
			return result;
		}
		
		/**
		 * Este método poder ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método  "hashCode()".
		 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
		 * 
		 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
		 * 		 de Dados como Set.
		 *
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
			if (!(obj instanceof PerioPM)) {
				return false;
			}
			PerioPM other = (PerioPM) obj;
			if (periodoPM != other.periodoPM) {
				return false;
			}
			return true;
		}
}
