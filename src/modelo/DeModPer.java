 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
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


@Entity
@Table(name="DeModPer")
@SequenceGenerator(name="SEQUENCIA", sequenceName="SEQ_DEMODPER", allocationSize=1)

@NamedQueries(
		{
		@NamedQuery(name="DeModPer.recuperaListaDeDemandaModeloPeriodo", 
					query="select deModPer from DeModPer deModPer " +
						  "left outer join deModPer.modelo modelo " +
						  "left outer join deModPer.periodo periodo " +
						  "order by modelo,periodo "
		),
		@NamedQuery(name="DeModPer.recuperaListaDeDeModPerPorModelo",
					query="select deModPer from DeModPer deModPer " +
						  "where deModPer.modelo = ? "
		),			
		@NamedQuery(name="DeModPer.recuperaDeModPerPorPeriodoEModelo",
					query="select deModPer from DeModPer deModPer " +
					      "where deModPer.periodo = ? " +
						  "and deModPer.modelo = ? " 
		)	
	}			
)

public class DeModPer implements Serializable, Comparable<DeModPer> {
	
	private static final long serialVersionUID = 1L;
	
	public DeModPer(){
	}
	
	public DeModPer(Modelo modelo, PerioPM periodo){
		
		this.modelo = modelo;
		this.periodo = periodo;
		this.vendasProjetadasModelo = 0.0;
		this.pedidosModelo = 0.0;
	}
	

	private Long idDeModPer;
	
	private Modelo modelo;
	
	private PerioPM periodo;
	
	private Double vendasProjetadasModelo;
	
	private Double pedidosModelo;

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getIdDeModPer() {
		return idDeModPer;
	}

	public void setIdDeModPer(Long idDemandaModeloPeriodo) {
		this.idDeModPer = idDemandaModeloPeriodo;
	}

	// Lembrar de deixar como EAGER, pois na tela SEMPRE se quer exibir a informação do Modelo
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="MODELO_ID")
	public Modelo getModelo() {
		return modelo;
	}

	// Lembrar de deixar como EAGER, pois na tela SEMPRE se quer exibir a informação do Periodo
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="PERIOPM_ID")
	public PerioPM getPeriodo() {
		return periodo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public void setPeriodo(PerioPM periodo) {
		this.periodo = periodo;
	}

	public Double getVendasProjetadasModelo() {
		return vendasProjetadasModelo;
	}

	public void setVendasProjetadasModelo(Double vendasProjetadasModelo) {
		this.vendasProjetadasModelo = vendasProjetadasModelo;
	}

	public Double getPedidosModelo() {
		return pedidosModelo;
	}

	public void setPedidosModelo(Double pedidosModelo) {
		this.pedidosModelo = pedidosModelo;
	}
	
	@Override
	public String toString(){
		return this.modelo.getDescrModelo() + " - " + this.periodo.getPeriodoPM() + " - " + 
		this.vendasProjetadasModelo + " - " + this.pedidosModelo; 
	}

	
	/**
	 *  Este método consiste em definir o critério de comparação entre 2 objetos DeModPer, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'periodo', representando a
	 *  classe PerioPM, que sabe internamente se auto-ordenar.
	 *  	 
	 * @author walanem.junior
	 * @param DeModPer
	 * @return int
	 * 
	 */
	@Override
	public int compareTo(DeModPer o) {
		return this.periodo.compareTo(o.getPeriodo());
	}
}
