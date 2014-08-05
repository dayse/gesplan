 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries(
		{	
			@NamedQuery
			(	name = "Parametros.recuperaListaDeParametros",
				query = "select p from Parametros p"
			),
			@NamedQuery
			(	name = "Parametros.recuperaParametrosPorId",
				query = "select p from Parametros p where p.id = ?"
			)
		})

@Entity
@Table(name="PARAMETROS")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PARAMETROS",
		           allocationSize=1)
public class Parametros implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** identificador da familia de modelos */		 
	private Long id;

	private Double margemSeguranca;
	
	private Double percentualDePerda;
	
	private int numIntervalosFixos;
	
	private boolean inicPlanejamento;

	/**
	 * Data a que se refere o valor de estoque inicial dos modelos. 
	 * esse dado é obtido normalmente a partir de um relatorio do sistema de controle
	 * de estoque.
	 * */		 
	private Calendar dataEstqInic;
	


	public Parametros(){
		
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
	


	public Calendar getDataEstqInic() {
		return dataEstqInic;
	}


	public void setDataEstqInic(Calendar dataEstqInic) {
		this.dataEstqInic = dataEstqInic;
	}
	
	@Column(length=3)
	public double getMargemSeguranca() {
		return margemSeguranca;
	}

	public void setMargemSeguranca(double margemSeguranca) {
		this.margemSeguranca = margemSeguranca;
	}

	@Column(length=3)
	public double getPercentualDePerda() {
		return percentualDePerda;
	}

	public void setPercentualDePerda(double percentualDePerda) {
		this.percentualDePerda = percentualDePerda;
	}

	@Column(length=2)
	public int getNumIntervalosFixos() {
		return numIntervalosFixos;
	}

	public void setNumIntervalosFixos(int numIntervalosFixos) {
		this.numIntervalosFixos = numIntervalosFixos;
	}

	public boolean isInicPlanejamento() {
		return inicPlanejamento;
	}

	public void setInicPlanejamento(boolean inicPlanejamento) {
		this.inicPlanejamento = inicPlanejamento;
	}
}
