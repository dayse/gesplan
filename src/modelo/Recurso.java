 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

/**
 * Classe relativa ao Bean para o cadastro de Recursos de producao
 * 
 * 
 * @author felipe
 *
 */

/* Definição das NamedQueries usadas 
* A NamedQuerie tem que ter exatamente o nome do método que a emprega
* Atencao Estamos usando order by codRecurso e nao por id.
*/
@NamedQueries(
		{	
			
			@NamedQuery
			(	name = "Recurso.recuperaRecurso",
				query = "select r from Recurso r where r.id = ? "
			),
			@NamedQuery
			(	name = "Recurso.recuperaRecursoPeloCodigo",
				query = "select r from Recurso r where r.codRecurso = ?"
			),

			@NamedQuery(name = "Recurso.recuperaListaDeRecursosPeloCodigoLike", 
						query = "select r from Recurso r " + 
								"where r.codRecurso like '%' || upper(?) || '%' "
			),
			@NamedQuery(name = "Recurso.recuperaListaDeRecursosPorDescricao", 
						query = "select distinct(r) from Recurso r " + 
								"where upper(r.descrRecurso) like '%' || upper(?) || '%' " + 
								"order by r.codRecurso "
			),
			@NamedQuery
			(	name = "Recurso.recuperaListaDeRecursos",
				query = "select r from Recurso r order by r.codRecurso asc"
			),
			@NamedQuery
			(	name = "Recurso.recuperaListaPaginadaDeRecursos",
				query = "select distinct r from Recurso r order by r.codRecurso asc"
			),
			@NamedQuery
			(	name = "Recurso.recuperaListaPaginadaDeRecursosCount",
				query = "select count (distinct r) from Recurso r"
			),
			@NamedQuery
			(	name = "Recurso.recuperaRecursoComListaDeRecModels",
				query = "select r from Recurso r " +
						"left outer join fetch r.recModels " +
						"where r = ?"
			),
			@NamedQuery
			(	name = "Recurso.recuperaRecursoComListaDeCapacRecs",
				query = "select r from Recurso r " +
						"left outer join fetch r.capacRecs " +
						"where r = ?"
			),
			@NamedQuery
			(	name = "Recurso.recuperaListaPaginadaDeRecursosComListaDeRecModels",
				query = "select distinct r from Recurso r " +
						"left outer join fetch r.recModels rm order by r.codRecurso asc"
			),
			@NamedQuery
			(	name = "Recurso.recuperaListaPaginadaDeRecursosComListaDeRecModelsCount",
				query = "select count(distinct r) from Recurso r " +
						"left outer join r.recModels "
			),
			@NamedQuery
			(	name = "Recurso.recuperaListaPaginadaDeRecursosComListaDeCapacRecs",
				query = "select distinct r from Recurso r " +
						"left outer join fetch r.capacRecs cr order by r.codRecurso asc"
			),	
			@NamedQuery
			(	name = "Recurso.recuperaListaPaginadaDeRecursosComListaDeCapacRecsCount",
					query = "select count(distinct r) from Recurso r " +
							"left outer join r.capacRecs "
			),	
				@NamedQuery
				(	name = "Recurso.recuperaListaDeRecursosComRecModels",
						query = "select distinct r from Recurso r " +
								"left outer join fetch r.recModels "
			),@NamedQuery
			(	name = "Recurso.recuperaListaDeRecursosQueTenhamApenasRecModels",
					query = "select distinct r from Recurso r " +
							"inner join r.recModels "
			)
		})

				
@Entity
@Table(name="RECURSO")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_RECURSO",
		           allocationSize=1)
public class Recurso implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/** identificador do recurso */		 
	private Long id;

		/** codigo do recurso */		 
	private String codRecurso;

	/** descricao do recurso */		 
	private String descrRecurso;

	/** UM do recurso */		 
	private String UM;
	
	/** custo unitario do recurso em R$ */
	private double custoUnit;

	private List<RecModel> recModels = new ArrayList<RecModel>();
	
	private List<CapacRec> capacRecs = new ArrayList<CapacRec>();
	
	public Recurso() {
	}



	// ********* Métodos do Tipo Get e Set *********
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}

	public String getDescrRecurso() {
		return descrRecurso;
	}

	public void setDescrRecurso(String descrRecurso) {
		this.descrRecurso = descrRecurso;
	}

	public String getUM() {
		return UM;
	}

	public void setUM(String UM) {
		this.UM = UM;
	}

	public double getCustoUnit() {
		return custoUnit;
	}

	public void setCustoUnit(double custoUnit) {
		this.custoUnit = custoUnit;
	}
	
	
	
	
	// ********* Métodos para Associações *********
	
	@OneToMany(mappedBy = "recurso", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	public List<RecModel> getRecModels() {
		return recModels;
	}


	/**
	 * @param recModels the recModels to set
	 */
	public void setRecModels(List<RecModel> recModels) {
		this.recModels = recModels;
	}

	@OneToMany(mappedBy = "recurso", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	public List<CapacRec> getCapacRecs() {

		if(!capacRecs.isEmpty()){
			Collections.sort(capacRecs);
		}
		return capacRecs;
	}


	public void setCapacRecs(List<CapacRec> capacRecs) {
		this.capacRecs = capacRecs;
	}

	public String toString() {
		return codRecurso + " - " + descrRecurso + " - " + UM;
		
	}

	
	
	
}
