 
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
 * Classe relativa ao bean que informa o consumo de recurso por modelo
 * (RECMODEL)
 * 
 * @author dayse.arruda
 * 
 * 
 */

@NamedQueries( {
		@NamedQuery(name = "RecModel.recuperaListaDeRecModels", 
					query = "select rm from RecModel rm "
							+ "order by rm.id"),
		@NamedQuery(name = "RecModel.recuperaListaDeRecModelsComModelos", 
					query = "select rm from RecModel rm "
							+ "left outer join fetch rm.modelo " + "order by rm.id"),

		@NamedQuery(name = "RecModel.recuperaListaDeRecModelsComRecursoComModelos", 
					query = "select rm from RecModel rm "
							+ "left outer join fetch rm.recurso " + "order by rm.modelo.codModelo"),
							
		@NamedQuery(name = "RecModel.recuperaRecModelComModelo", 
					query = "select rm from RecModel rm "
							+ "left outer join fetch rm.modelo " + "where rm = ?"),
		
		@NamedQuery(name = "RecModel.recuperaRecModelComRecurso", 
					query = "select rm from RecModel rm "
							+ "left outer join fetch rm.recurso " + "where rm = ?"),	
							
		@NamedQuery(name = "RecModel.RecuperaListaDeRecModelsPorCodModeloLike", 
					query = "select distinct(rm) from RecModel rm "
							+ "left outer join fetch rm.modelo modelo "
							+ "where upper(modelo.codModelo) like '%' || upper(?) || '%' "
							+ "order by modelo.codModelo "),

		@NamedQuery(name = "RecModel.recuperaListaDeRecModelsPorDescrModelo", 
					query = "select distinct(rm) from RecModel rm " + 
							"left outer join fetch rm.modelo modelo " + 
							"where upper(modelo.descrModelo) like '%' || upper(?) || '%' " + 
							"order by modelo.codModelo "
		),					
		@NamedQuery(name = "RecModel.RecuperaRecModelPorCodModelo", 
				  query = "select distinct(rm) from RecModel rm "
						+ "left outer join fetch rm.modelo modelo "
						+ "where modelo.codModelo = ? "
						+ "order by modelo.codModelo "),


		@NamedQuery(name = "RecModel.recuperaRecModelPorRecursoEModelo", 
				query = "select rm from RecModel rm " + 
						"left outer join fetch rm.recurso " + 
						"left outer join fetch rm.modelo " +
						"where rm.recurso = ? and rm.modelo = ?"
		),

		@NamedQuery
		(	name = "RecModel.recuperaListaPaginadaDeRecModels",
			query = "select distinct (rm) from RecModel rm left outer join fetch rm.modelo modelo order by modelo.codModelo"
		),
		@NamedQuery
		(	name = "RecModel.recuperaListaPaginadaDeRecModelsCount",
			query = "select count (distinct rm) from RecModel rm"
		),
							
		@NamedQuery
		(	name = "RecModel.recuperaListaPaginadaDeRecModelsPorRecurso",
			query = "select distinct (rm) from RecModel rm " 
				   +" left outer join fetch rm.modelo modelo "
				   + "left outer join fetch rm.recurso recurso "
				   + "where recurso = ? "
		),
		@NamedQuery
		(	name = "RecModel.recuperaListaPaginadaDeRecModelsPorRecursoCount",
			query = "select count(distinct rm)" +
					"from RecModel rm " 
				   + "left outer join  rm.recurso recurso "
		)


})
@Entity
@Table(name = "RECMODEL")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_RECMODEL", allocationSize = 1)
public class RecModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identificador de recModel */
	private Long id;

	/** Consumo Unitario do Modelo para esse recurso */
	private double consumoUnit;

	// Um recModel é relativo a um unico recurso
	private Recurso recurso;

	// Um recModel é relativo a um unico modelo
	private Modelo modelo;
	

	public RecModel() {

	}

	public RecModel(double consumoUnit, Recurso recurso, Modelo modelo) {
		this.consumoUnit = consumoUnit;
		this.recurso = recurso;
		this.modelo = modelo;
	}


	// ********* Métodos get/set *********
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setConsumoUnit(double consumoUnit) {
		this.consumoUnit = consumoUnit;
	}

	public double getConsumoUnit() {
		return consumoUnit;
	}

	// ********* Métodos para Associações *********

	@ManyToOne(fetch = FetchType.LAZY)
	// Usa-se lazy normalmente para evitar acabar levando todo o banco
	@JoinColumn(name = "RECURSO_ID", nullable = false)
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	@ManyToOne
	@JoinColumn(name = "MODELO_ID", nullable = false)
	@OrderBy("codModelo")
	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

}
