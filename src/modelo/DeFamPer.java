 
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

/**
 * Classe relativa ao bean correspondente a entidade DeFamPer, que informa a demanda por familia e periodo.
 * 
 * @author marques.araujo
 *
 */

@NamedQueries(
		{
		@NamedQuery(name="DeFamPer.recuperaListaDeDemandaFamiliaPerioPAP", 
					query="select deFamPer from DeFamPer deFamPer " +
						  "left outer join deFamPer.familia familia " +
						  "left outer join DeFamPer.perioPAP perioPAP " +
						  "order by familia,perioPAP "
		),
		@NamedQuery(name="DeFamPer.recuperaListaDeDeFamPerPorFamilia",
					query="select deFamPer from DeFamPer deFamPer " +
						  "where deFamPer.familia = ? "
		),			
		@NamedQuery(name="DeFamPer.recuperaDeFamPerPorPerioPAPEFamilia",
				    query="select deFamPer from DeFamPer deFamPer " +
				          "where deFamPer.perioPAP = ? " +
					      "and deFamPer.familia = ? " 
	    )	    
		
	}			
)

@Entity
@Table(name="DeFamPer")
@SequenceGenerator(name="SEQUENCIA", sequenceName="SEQ_DEFAMPER", allocationSize=1)

public class DeFamPer implements Serializable, Comparable<DeFamPer> {
	
private static final long serialVersionUID = 1L;

   /**
	* Identificador de DeFamPer.
	* 
	*/
	private Long idDeFamPer;
	
	/**
	 * Uma Familia possui muitos DeFamPer.
	 * 
	 */
	private Familia familia;
	
	/**
	 * Um PerioPAP possui muitos DeFamPer.
	 * 
	 */
	private PerioPAP perioPAP;

	/**
	 * Informa venda projetada em peças por familia e periodo.
	 * 
	 */
	private double vendasProjetadasFamilia;
	
	/**
	 * Informa pedidos cadastrados de todos as familias(peças).
	 * 
	 */
	private double pedidosFamilia;

	
	/**
	 * Construtor sem agumentos da classe DeFamPer.
	 * 
	 */
	public DeFamPer(){
	}
	
	/**
	 * Construtor com agumentos da classe DeFamPer.
	 * 
	 */
	public DeFamPer(Familia familia, PerioPAP perioPAP){
		
		this.familia = familia;
		this.perioPAP = perioPAP;
		this.vendasProjetadasFamilia = 0.0;
		this.pedidosFamilia = 0.0;
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getIdDeFamPer() {
		return idDeFamPer;
	}
	
	
	public void setIdDeFamPer(Long idDeFamPer) {
		this.idDeFamPer = idDeFamPer;
	}

	// Lembrar de deixar como EAGER, pois na tela SEMPRE se quer exibir a informação da Familia
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="FAMILIA_ID")
	public Familia getFamilia() {
		return familia;
	}
	
	
	// Lembrar de deixar como EAGER, pois na tela SEMPRE se quer exibir a informação do Periodo
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="PERIOPAP_ID")
	public PerioPAP getPerioPAP() {
		return perioPAP;
	}


	public void setFamilia(Familia familia) {
		this.familia = familia;
	}
		
	
	public void setPerioPAP(PerioPAP perioPAP) {
		this.perioPAP = perioPAP;
	}

	
	public double getVendasProjetadasFamilia() {
		return vendasProjetadasFamilia;
	}

	
	public void setVendasProjetadasFamilia(double vendasProjetadasFamilia) {
		this.vendasProjetadasFamilia = vendasProjetadasFamilia;
	}

	
	public double getPedidosFamilia() {
		return pedidosFamilia;
	}

	
	public void setPedidosFamilia(double pedidosFamilia) {
		this.pedidosFamilia = pedidosFamilia;
	}

	
	@Override
	public String toString(){
		return this.familia.getDescrFamilia() + " - " + this.getPerioPAP() + " - " + 
		this.vendasProjetadasFamilia + " - " + this.pedidosFamilia; 
	}

	
	/**
	 * 	Este método consiste em definir o critério de comparação entre 2 objetos DeFamPer, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, o atributo em questão é o 'periodo', representando a
	 *  classe PerioPM, que sabe internamente se auto-ordenar.
	 *  
	 *  @param DeFamPer
	 *  @return int
	 */
	@Override
	public int compareTo(DeFamPer o) {
		return this.perioPAP.compareTo(o.getPerioPAP());
	}
	
	


}
