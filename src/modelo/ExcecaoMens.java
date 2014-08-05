 
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
import java.util.Calendar;
import java.util.Collections;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import xfuzzy.lang.Specification;



/**
 * Classe relativa ao Bean para o cadastro de ExcecãoMens
 * Define as mensagens de Excecao para determinada combinacao PlanoModelo, tipo de Excecao e periodo de origem
 * a que esta se refere.
 * 
 * 
 * @author felipe
 *
 */

@NamedQueries(
		{	
			@NamedQuery
			(	name = "ExcecaoMens.recuperaExcecaoMens",
				query = "select ex from ExcecaoMens ex where ex.id = ? "
			),		
			@NamedQuery(name="ExcecaoMens.recuperaExcecaoMensPorPlanoModeloEExcecaoEPeriodoOrigemEPeriodoDestino",
					query = "select exm from ExcecaoMens exm " +
							"left outer join fetch exm.planoModelo pl " +
							"left outer join fetch exm.excecao ex " +
							"left outer join exm.periodoOrigem pm " +
							"left outer join exm.periodoDestino pd " +
							"where pl = ? " +
							"and ex = ? " +
							"and po = ? "+
							"and pd = ? "
			),
			@NamedQuery
			(	name = "ExcecaoMens.recuperaListaDeExcecaoMensPorExcecao",
					query = "select exm from ExcecaoMens exm " +
					"left outer join fetch exm.excecao ex " +
					"where ex = ? "
			),
			@NamedQuery
			(	name = "ExcecaoMens.recuperaListaDeExcecaoMensPorPlanoModelo",
					query = "select exm from ExcecaoMens exm " +
					"left outer join fetch exm.planoModelo pl " +
					"where pl = ? "
			),
			@NamedQuery
			(	name = "ExcecaoMens.recuperaListaDeExcecaoMensPorPlanoModeloEExcecao",
					query = "select exm from ExcecaoMens exm " +
					"left outer join fetch exm.planoModelo pl " +
					"left outer join fetch exm.excecao ex " +
					"where pl = ? "+
					"and ex = ? "
			),			
			@NamedQuery(name="ExcecaoMens.recuperaListaDeExcecaoMensPorPlanoModeloEExcecaoEPeriodoOrigem",
					query = "select exm from ExcecaoMens exm " +
							"left outer join fetch exm.planoModelo pl " +
							"left outer join fetch exm.excecao ex " +
							"left outer join exm.periodoOrigem pm " +
							"where pl = ? " +
							"and ex = ? " +
							"and pm = ? "
			),
			@NamedQuery
			(	name = "ExcecaoMens.recuperaListaDeExcecaoMens",
				query = "select exm "+
						"from ExcecaoMens exm "
			),
			@NamedQuery
			(	name = "ExcecaoMens.recuperaListaPaginadaDeExcecaoMens",
				query = "select distinct exm from ExcecaoMens exm"
			),
			@NamedQuery
			(	name = "ExcecaoMens.recuperaListaPaginadaDeExcecaoMensCount",
				query = "select count (distinct exm) from ExcecaoMens exm"
			),					
		})

				
@Entity
@Table(name="EXCECAOMENS")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_EXCECAOMENS",
		           allocationSize=1)
/**
 * ExcecaoMens - Armazena mensagem de exceção por planoModelo, por tipo de exceção e por periodo.
 * 
 * @author arruda
 *
 */		           
public class ExcecaoMens implements Serializable, Comparable<ExcecaoMens>{
	
	private static final long serialVersionUID = 1L;
	
	/** identificador da ExcecaoMens */		 
	private Long id;

	/** Quantidade de peças do modelo relativa a exceção */		 
	private Double quantidade;

	/**
	 * Atributo que contém a excecao a que essa mensagem se refere.
	 */
	private Excecao excecao;

	/**
	 * Atributo que indica o planoModelo a que essa mensagem se refere.
	 */
	private PlanoModelo planoModelo;
	
	/**
	 * Atributo que indica o periodoPM de origem a que essa mensagem se refere.
	 */
	private PerioPM periodoOrigem;
	
	/**
	 * Atributo que indica o periodoPM de destino a que essa mensagem se refere.
	 */
	private PerioPM periodoDestino;
	
	
	public ExcecaoMens() {
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
	public Double getQuantidade() {
		return quantidade;
	}



	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	
	// ********* Métodos para Associações *********
	




	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="EXCECAO_ID", nullable=false)
	public Excecao getExcecao() {
		return excecao;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PLANOMODELO_ID", nullable=false)
	public PlanoModelo getPlanoModelo() {
		return planoModelo;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PERIODOPM_ID1", nullable=true)
	public PerioPM getPeriodoOrigem() {
		return periodoOrigem;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PERIODOPM_ID2", nullable=true)
	public PerioPM getPeriodoDestino() {
		return periodoDestino;
	}


	
	public void setPeriodoDestino(PerioPM periodoDestino) {
		this.periodoDestino = periodoDestino;
	}
	
	public void setPeriodoOrigem(PerioPM periodoOrigem) {
		this.periodoOrigem = periodoOrigem;
	}

	
	public void setExcecao(Excecao excecao) {
		this.excecao = excecao;
	}



	public void setPlanoModelo(PlanoModelo planoModelo) {
		this.planoModelo = planoModelo;
	}

	@Override
	public String toString() {
		return this.planoModelo  + " - " + this.excecao + " (" +  this.periodoOrigem +  " / " + this.periodoDestino+ ")";
	}



	@Override
	public int compareTo(ExcecaoMens o) {
		return this.excecao.compareTo(o.getExcecao());
	}


	
}
