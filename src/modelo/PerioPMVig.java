 
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Classe relativa ao Bean PerioPMVig que informa os períodos do  Plano Mestre da Produção Vigente
 * 
 * 
 * @author felipe
 *
 */

	@NamedQueries(
		{	

			@NamedQuery
			(	name = "PerioPMVig.recuperaPerioPMVig",
				query = "select p from PerioPMVig p where p.id = ?"
			),
			@NamedQuery
			(	name = "PerioPMVig.recuperaPerioPMVigPorPeriodoPM",
				query = "select p from PerioPMVig p where p.periodoPM = ? "
			),
			@NamedQuery
			(	name = "PerioPMVig.recuperaListaDePerioPMVigs",
				query = "select p from PerioPMVig p order by p.periodoPM"
			),
			@NamedQuery
			(	name = "PerioPMVig.recuperaListaPaginadaDePerioPMVigs",
				query = "select distinct p from PerioPMVig p order by p.periodoPM"
			),
			@NamedQuery
			(	name = "PerioPMVig.recuperaListaPaginadaDePerioPMVigsCount",
				query = "select count (distinct p) from PerioPMVig p"
			),

			@NamedQuery
			(	name = "PerioPMVig.recuperaIntervaloDePerioPMVigs",
					query = "select p from PerioPMVig p " +
							"where p.periodoPM >= ? " +
							"and p.periodoPM <= ? " +
							"order by p.periodoPM "
			),
			@NamedQuery
			(	name = "PerioPMVig.recuperaListaDePerioPMVigsPorPerioPAPVig",
					query = "select p from PerioPMVig p " +
							"where p.perioPAPVig = ? " +
							"order by p.periodoPM"
			)
		}
	)
		

@Entity
@Table(name="PerioPMVig")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PERIOPMVIG",
		           allocationSize=1)
		           

public class PerioPMVig implements Serializable, Comparable<PerioPMVig> {

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
		
		/** Relativo ao perioPAP do plano agregado vigente ao qual pertence esse periodo do plano mestre vigente */
		private PerioPAPVig perioPAPVig;
		
		/** Lista de registros do Plano mestre Vigente que utilizam esses periodo vigente */
		private List<PMP> pmps;

		// ********* Construtores *********
		
		public PerioPMVig() {
		}

		public PerioPMVig(int periodoPM, Calendar dataInicial,
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
		@Column(unique=true)
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
		// Usa-se lazy normalmente para evitar acabar levando todo o banco
		@JoinColumn(name = "PERIOPAPVIG_ID",nullable = false)
	
		public PerioPAPVig getPerioPAPVig() {
			return perioPAPVig;
		}

		public void setPerioPAPVig(PerioPAPVig perioPAPVig) {
			this.perioPAPVig = perioPAPVig;
		}

		@OneToMany(mappedBy = "perioPMVig", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
		public List<PMP> getPmps() {
			return pmps;
		}

		/**
		 * @param pMPs the pMPs to set
		 */
		public void setPmps(List<PMP> pmps) {
			this.pmps = pmps;
		}

		/**
		 * 
		 *  Este método consiste em definir o critério de ordenação entre 2 objetos PerioPM, que deve estar 
		 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'periodoPM', que é do tipo 
		 *  nativo 'int', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
		 *
		 * @author walanem.junior
		 * @param PerioPMVig
		 * @return int
		 * 
		 */
		@Override
		public int compareTo(PerioPMVig o) {
			return this.periodoPM < o.periodoPM ? -1 : this.periodoPM > o.periodoPM ? 1 : 0;
		}
		@Override
		public String toString(){
			return "Periodo - "+this.periodoPM;
		}
}
