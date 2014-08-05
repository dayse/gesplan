 
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe relativa ao bean que informa a capacidade de recurso cr�tico por per�odo do plano mestre
 * (CAPACREC)
 * 
 * @author felipe.arruda
 * 
 * 
 */

@NamedQueries( {
		@NamedQuery(name = "CapacRec.recuperaCapacRecComPerioPM", 
					query = "select cr from CapacRec cr "
					+ "left outer join fetch cr.perioPM " + "where cr = ?"),
					
		@NamedQuery(name = "CapacRec.recuperaListaDeCapacRecs", 
					query = "select cr from CapacRec cr "
							+ "order by cr.id"),
							
		@NamedQuery(name = "CapacRec.recuperaListaDeCapacRecsPorRecurso", 
				query = "select cr from CapacRec cr " +
						"left outer join fetch cr.recurso " + 
						"where cr.recurso = ?" +
						"order by cr.id"),
							
		@NamedQuery(name = "CapacRec.recuperaListaDeCapacRecsComPerioPMs", 
					query = "select cr from CapacRec cr "
							+ "left outer join fetch cr.perioPM " + "order by cr.id"),

		@NamedQuery(name = "CapacRec.recuperaListaDeCapacRecsComRecursosEPerioPMs", 
				query = "select cr from CapacRec cr "
						+ "left outer join fetch cr.recurso " 
						+ "left outer join fetch cr.perioPM " 
						+ "order by cr.id"),							

		@NamedQuery(name = "CapacRec.recuperaCapacRecPorRecursoEPerioPM", 
					query = "select cr from CapacRec cr " + 
							"left outer join fetch cr.recurso " + 
							"left outer join fetch cr.perioPM " +
							"where cr.recurso = ? and cr.perioPM = ?"
					)
})
@Entity
@Table(name = "CAPACREC")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_CAPACREC", allocationSize = 1)
public class CapacRec implements Serializable, Comparable<CapacRec>{

	private static final long serialVersionUID = 1L;

	/** identificador de recModel */
	private Long id;

	/** Informa a capacidade de cada recurso critico por periodo */
	private double capacDiaria;

	/** Um CapacRec � relativo a um unico recurso */
	private Recurso recurso;

	/** Periodo do Plano Mestre relativo ao HP  - Um CapacRec � relativo a um unico periodo*/
	private PerioPM perioPM;
	

	public CapacRec() {

	}



	// ********* M�todos get/set *********
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCapacDiaria(double capacDiaria) {
		this.capacDiaria = capacDiaria;
	}

	public double getCapacDiaria() {
		return capacDiaria;
	}

	// ********* M�todos para Associa��es *********

	/*
	 * O atributo fetch indica se a associa��o deve ser carregada atrav�s da
	 * utiliza��o de proxy (de forma "lazy") ou se ela deve ser carregada
	 * antecipadamente. Neste caso, o default � EAGER (recupera��o antecipada).
	 * 
	 * O elemento @JoinColumn tamb�m � opcional. Se o nome da FK n�o for declarada a
	 * JPA automaticamente utiliza a combina��o: nome da entidade alvo (PRODUTO,
	 * neste caso) concatenado ao nome da propriedade identificadora no banco de
	 * dados (ID, neste caso).
	 */
	// lazy significa que nao vai trazer automaticamente o outro elemento da
	// associa��o

	// com a anota�ao Manytoone permite que estando no capacRec consiga
	// recuperar o recurso
	// sem esta anota�ao nao adianta ter o metodo
	@ManyToOne(fetch = FetchType.LAZY)
	// usar lazy normalmente para evitar acabar levando todo o banco
	@JoinColumn(name = "RECURSO_ID", nullable = false)
	// name define o nome da chave estrangeira na tabela
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	@ManyToOne
	/** n�o estou usando lazy pois quero os dados do perioPM sempre que mostrar
	 capacRec */
	@JoinColumn(name = "PERIOPM_ID", nullable = false)
	// name define o nome da chave estrangeira na tabela 
	@OrderBy("periodoPM")
	public PerioPM getPerioPM() {
		return perioPM;
	}

	public void setPerioPM(PerioPM perioPM) {
		this.perioPM = perioPM;
	}
	
	/**
	 * 
	 *  Este m�todo consiste em definir o crit�rio de compara��o entre 2 objetos DeModPer, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, o atributo em quest�o � o 'periodo', representando a
	 *  classe PerioPM, que sabe internamente se auto-ordenar.
	 *  
	 * @author walanem.junior
	 * @param CapacRec 
	 * @return int
	 *  
	 */
	@Override
	public int compareTo(CapacRec o) {
		return this.perioPM.compareTo(o.getPerioPM());
	}

}
