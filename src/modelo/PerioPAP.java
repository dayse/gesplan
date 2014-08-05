 
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
import java.util.Set;
import java.util.TreeSet;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Classe relativa ao Bean PerioPAP que informa os períodos do  Plano Agregado da Produção
 * 
 * 
 * @author daysemou/felipe
 *
 */

	@NamedQueries(
		{	
			@NamedQuery
			(	name = "PerioPAP.recuperaPerioPAP",
				query = "select p from PerioPAP p where p.id = ?"
			),	
			@NamedQuery
			(	name = "PerioPAP.recuperaPerioPAPPorPeriodoPAP",
				query = "select p from PerioPAP p where p.periodoPAP = ?"
			),
			@NamedQuery
			(	name = "PerioPAP.recuperaListaDePerioPAPs",
				query = "select p from PerioPAP p order by p.periodoPAP"
			),
			@NamedQuery
			(	name = "PerioPAP.recuperaListaPaginadaDePerioPAPs",
				query = "select distinct p from PerioPAP p order by p.periodoPAP"
			),
			@NamedQuery
			(	name = "PerioPAP.recuperaListaPaginadaDePerioPAPsCount",
				query = "select count (distinct p) from PerioPAP p"
			),
			@NamedQuery
			(	name = "PerioPAP.recuperaPerioPAPComPerioPMsPorPeriodoPAP",
				query = "select pap from PerioPAP pap left outer join fetch pap.perioPMs where pap.periodoPAP = ? "
			),
			@NamedQuery
			(	name = "PerioPAP.recuperaIntervaloDePerioPAPs",
					query = "select p from PerioPAP p " +
							"where p.periodoPAP >= ? " +
							"and p.periodoPAP <= ? "
			)
			
		}
	)
		

@Entity
@Table(name="PerioPAP")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PERIOPAP",
		           allocationSize=1)
		           
/**
 * precisa implementar Comparable e deve sobrescrever o metodo compareTo para que a lista possa 
 * se reordenar
 * 
 */
public class PerioPAP implements Serializable, Comparable<PerioPAP> {

		private static final long serialVersionUID = 1L;

		/** identificador do periodo do plano agregado */		 
		private Long id;
	
		/** numero do periodo do plano agredado */		 
		private int periodoPAP;

		private List<PerioPM> perioPMs;
		private List<DeFamPer> deFamPers;
		
		/**
		 * Campos Calculados obtidos a partir da lista de PerioPMs e da tabela PerioPM.
		 * Para que nao sejam criados os campos na tabela é preciso usar o modificador "transient" e
		 * a anotacao "@transient" nos gets dos respectivos atributos
		 * 
		 */

		/**  periodo PMP inicial que corresponde ao PeriodoPAP 
		 * 
		 * Obtido a partir do primeiro PerioPM da lista de PerioPMs que compoem este PerioPAP*/
		private transient int periodoPMInic;
		 
		/**  periodo PMP final que corresponde ao PeriodoPAP  
		 * Obtido a partir do ultimo  PerioPM da lista de PerioPMs que compoem este PerioPAP*/
		private transient int periodoPMFinal;
		
		/** data inicial relativa ao periodo do plano agregado. 
		 * Obtido a partir da dataIncial do primeiro PerioPM da lista de PerioPMs que compoem este PerioPAP*/		 
		private transient Calendar dataInicial;

		/** data final relativa ao periodo do plano agregado.
		 * Obtido a partir da dataFinal do ultimo PerioPM da lista de PerioPMs que compoem este PerioPAP */		 
		private transient Calendar dataFinal;
		
		/** Numero de dias uteis na matriz relativo ao periodo do plano agregado. 
		 * É calculado a partir da soma do numDiasUteisMatriz dos perioPMs que compoem esse perioPAP
		 * 
		 * */		
		private transient double numDiasUteis;

		

		// ********* Construtores *********
		
		public PerioPAP() {
		}

		
		public PerioPAP( int periodoPAP, List<PerioPM> perioPMs) {

			this.periodoPAP = periodoPAP;
			this.perioPMs = perioPMs;
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
		public int getPeriodoPAP() {
			return periodoPAP;
		}

		/**
		 * @param periodoPM the periodoPM to set
		 */
		public void setPeriodoPAP(int periodoPAP) {
			this.periodoPAP = periodoPAP;
		}
		


		public void setPerioPMs(List<PerioPM> perioPMs) {
			this.perioPMs = perioPMs;
		}


		@OneToMany(mappedBy="perioPAP", fetch=FetchType.EAGER)
		public List<PerioPM> getPerioPMs() {
			return perioPMs;
		}

		@OneToMany(mappedBy="perioPAP", fetch=FetchType.LAZY)
		public List<DeFamPer> getDeFamPers() {
			return deFamPers;
		}
		public void setDeFamPers(List<DeFamPer> deFamPers) {
			this.deFamPers = deFamPers;
		}
		
		@Transient
		public int getPeriodoPMInic() {
			return periodoPMInic;
		}


		public void setPeriodoPMInic(int periodoPMInic) {
			this.periodoPMInic = periodoPMInic;
		}

		@Transient
		public int getPeriodoPMFinal() {
			return periodoPMFinal;
		}


		public void setPeriodoPMFinal(int periodoPMFinal) {
			this.periodoPMFinal = periodoPMFinal;
		}

		@Transient
		@Temporal(value = TemporalType.DATE)
		public Calendar getDataInicial() {
			return dataInicial;
		}


		public void setDataInicial(Calendar dataInicial) {
			this.dataInicial = dataInicial;
		}

		@Transient
		@Temporal(value = TemporalType.DATE)
		public Calendar getDataFinal() {
			return dataFinal;
		}


		public void setDataFinal(Calendar dataFinal) {
			this.dataFinal = dataFinal;
		}

		@Transient
		public double getNumDiasUteis() {
			return numDiasUteis;
		}


		public void setNumDiasUteis(double numDiasUteis) {
			this.numDiasUteis = numDiasUteis;
		}


		/**
		 * @author felipe
		 * @return int
		 * 
		 *  	Este método consiste em definir o critério de ordenação entre 2 objetos PerioPAP, que deve estar 
		 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'periodoPAP', que é do tipo 
		 *  nativo 'int', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
		 *  
		 *  retorna -1 se for menor, 0 se for igual, e 1 se for maior
		 */
		@Override
		public int compareTo(PerioPAP o) {
			return this.periodoPAP < o.periodoPAP ? -1 : this.periodoPAP > o.periodoPAP ? 1 : 0;
		}
		
		@Override
		public String toString(){
			return "PerioPAP [" + this.periodoPAP + "]";
		}
}
