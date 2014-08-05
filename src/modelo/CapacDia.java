 
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * Classe relativa ao Bean CapacDia que informa a capacidade diaria de minutos por periodos na matriz
 * 
 * 
 * @author daysemou/felipe
 *
 */

	@NamedQueries(
		{	
			@NamedQuery
			(	name = "CapacDia.recuperaCapacDia",
				query = "select c from CapacDia c where c.perioPM = ?"
			),
			@NamedQuery
			(	name = "CapacDia.recuperaListaDeCapacDias",
				query = "select c from CapacDia c order by c.perioPM"
			),
			@NamedQuery
			(	name = "CapacDia.recuperaCapacDiaPorPerioPM",
				query = "select c from CapacDia c where c.perioPM = ?"
			),
			@NamedQuery
			(	name = "CapacDia.recuperaListaPaginadaDeCapacDias",
				query = "select distinct c from CapacDia c order by c.perioPM"
			),
			@NamedQuery
			(	name = "CapacDia.recuperaListaPaginadaDeCapacDiasCount",
				query = "select count (distinct c) from CapacDia c"
			)
			
		}
	)
		

@Entity
@Table(name="CapacDia")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CAPACDIA",
		           allocationSize=1)
	
public class CapacDia implements Serializable, Comparable<CapacDia>   {

		private static final long serialVersionUID = 1L;

		/** identificador do periodo do plano agregado */		 
		private Long id;

		/** Capacidade de producao diaria no periodo na matriz em minutos */
		private double capacProdDiariaEmMin;
		
		/** Capacidade de producao diaria no periodo na matriz em minutos acrescido da margem de seguranca*/
		private transient double capacProdDiariaEmMinMg;
		
		/** Periodo relativo ao HP (é mostrado na tela com dois digitos) */
		private PerioPM perioPM;
				

		// ********* Construtores *********
		
		public CapacDia() {
			this.capacProdDiariaEmMin = 0;
		}

		

		// ********* Métodos do Tipo Get e Set *********
		@Id
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
		@Column(name="ID")
		public Long getId() {
			return id;
		}
		//Usa-se SupressWarnings porque o método é privado,
		// e daria um warning, pois ninguém usaria. Mas, a JPA usa através de reflexão.
		@SuppressWarnings("unused")
		public void setId(Long id) {
			this.id = id;
		}

		
		public double getCapacProdDiariaEmMin() {
			return capacProdDiariaEmMin;
		}



		public void setCapacProdDiariaEmMin(double capacProdDiariaEmMin) {
			this.capacProdDiariaEmMin = capacProdDiariaEmMin;
		}


		@Transient
		public double getCapacProdDiariaEmMinMg() {
			return capacProdDiariaEmMinMg;
		}



		public void setCapacProdDiariaEmMinMg(double capacProdDiariaEmMinMg) {
			this.capacProdDiariaEmMinMg = capacProdDiariaEmMinMg;
		}



		// ********* Métodos para Associações *********
		
		/** Quando recuperar um CapacDia, recupera automaticamente o perioPM ligado a ele. */
		@OneToOne(fetch=FetchType.EAGER)
		@JoinColumn(name="PERIOPM_ID",nullable=false)
		public PerioPM getPerioPM() {
			return perioPM;
		}

		public void setPerioPM(PerioPM perioPM) {
			this.perioPM = perioPM;
		}


		/**
		 *  Este método consiste em definir o critério de ordenação entre 2 objetos PerioPAP, que deve estar 
		 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'periodoPAP', que é do tipo 
		 *  nativo 'int', que sabe internamente se auto-ordenar, graças a implementação da API Java que o realiza.
		 *  
		 *  retorna -1 se for menor, 0 se for igual, e 1 se for maior
		 *  
		 * @author felipe
		 * @param CapacDia
		 * @return int
		 * 
		 */
		@Override
		public int compareTo(CapacDia o) {
			return this.perioPM.getPeriodoPM() < o.perioPM.getPeriodoPM() ? -1 : this.perioPM.getPeriodoPM() > o.perioPM.getPeriodoPM() ? 1 : 0;
		}
		
		public String toString(){
			return "Periodo = " + this.perioPM + " | Prod. Diaria = " + this.capacProdDiariaEmMin;
		}
}
