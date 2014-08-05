 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
/**
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Classe relativa ao bean correspondente a entidade TecModel, que relaciona Tecido e Modelo.
 * Informa o consumo de Tecido/Renda para cada Modelo.
 * 
 * @author marques.araujo
 *
 */



@NamedQueries( {
	
	@NamedQuery(name = "TecModel.recuperaListaDeTecModelsComTecidoComModelos", 
			query = "select tm from TecModel tm " + 
					"left outer join fetch tm.tecido " + 
					"left outer join fetch tm.modelo " 
	),
	@NamedQuery(name = "TecModel.recuperaTecModelPorTecidoEModelo", 
			query = "select tm from TecModel tm " + 
					"left outer join fetch tm.tecido " + 
					"left outer join fetch tm.modelo " +
					"where tm.tecido = ? and tm.modelo = ?"
	)
	

})





@Entity
@Table(name = "TECMODEL")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_TECMODEL", allocationSize = 1)
public class TecModel  implements Serializable, Comparable<TecModel> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Identificador de TecModel.
	 * 
	 */
	private Long id;
	
	/**
	 * Tempo de defasagem no uso do Tecido (em dias).
	 * 
	 */
	private Double tempoDefasagemUsoTec;
	
	/**
	 * Consumo do Tecido pelo modelo, por lote, em metros.
	 * 
	 */
	private Double consumoPorLoteMt;
	
	/**
	 * Um Tecido pertence a muitos TecModels.
	 * 
	 */
	
	private Tecido tecido;
	
	/**
	 * Um Modelo pertence a muitos TecModels.
	 * 
	 */
	private Modelo modelo;	
	
	/**
	 * Este modificador esta sendo usado, pois este eh um
	 * campo calculado e nao deve existir no banco de dados.
	 * Vide tambem a Anotação Transiente feita no get do referido
	 * campo neste caso o campo consumoPorLoteKg.
	 * Campo calculado a partir do campo ConsumoPorLoteMt, conforme o seguinte calculo:
	 * consumoPorLoteKg = (ConsumoPorLoteMt * (PercentualDePerda/100 + 1))/ FatorRendimento
	 *  
	 */
	private transient Double consumoPorLoteKg;
	
	public TecModel() {
		
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


	public Double getTempoDefasagemUsoTec() {
		return tempoDefasagemUsoTec;
	}


	public void setTempoDefasagemUsoTec(Double tempoDefasagemUsoTec) {
		this.tempoDefasagemUsoTec = tempoDefasagemUsoTec;
	}


	public Double getConsumoPorLoteMt() {
		return consumoPorLoteMt;
	}


	public void setConsumoPorLoteMt(Double consumoPorLoteMt) {
		this.consumoPorLoteMt = consumoPorLoteMt;
	}

	
	// ********* Métodos para Associações *********

	/*
	 * O atributo fetch indica se a associação deve ser carregada através da
	 * utilização de proxy (de fotecma "lazy") ou se ela deve ser carregada
	 * antecipadamente. Neste caso, o default é EAGER (recuperação antecipada).
	 * 
	 * O elemento @JoinColumn tb é opcional. Se o nome da FK não for declarada a
	 * JPA automaticamente utiliza a combinação: nome da entidade alvo (PRODUTO,
	 * neste caso) concatenado ao nome da propriedade identificadora no banco de
	 * dados (ID, neste caso).
	 */
	// lazy significa que nao vou trazer automaticamente o outro elemento da
	// associação

	// com a anotaçao Manytoone é possivel que estando no tecmodel consiga
	// recuperar o tecido
	// sem esta anotaçao nao adianta ter o metodo
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TECIDO_ID", nullable = false)
	// name define o nome da chave estrangeira na tabela
	@OrderBy("codTecido")
	public Tecido getTecido() {
		return tecido;
	}


	public void setTecido(Tecido tecido) {
		this.tecido = tecido;
  	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MODELO_ID", nullable = false)
	// name define o nome da chave estrangeira na tabela
	@OrderBy("codModelo")
	public Modelo getModelo() {
		return modelo;
	}


	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

    @Transient
	public Double getConsumoPorLoteKg() {
		return consumoPorLoteKg;
	}


	public void setConsumoPorLoteKg(Double consumoPorLoteKg) {
		this.consumoPorLoteKg = consumoPorLoteKg;
	}
	
	public String toString(){
		return " Id do TecModel="+this.id+" tempoDefasagemUsoTec="+this.tempoDefasagemUsoTec+" consumoPorLoteMt="+this.consumoPorLoteMt+" Tecido="+this.getTecido().getCodTecido();
	}

	
	/**
	 * 
	 *  Este método consiste em definir o critério de ordenação entre 2 objetos TecModels, que deve estar 
	 *  associado a um de seus atributos. No nosso caso, os atributos em questão são o 'tecido' e o 'modelo', 
	 *  estes objetos tambem possuem um criterio de comparaçao internamente e podem se auto-ordenar, graças
	 *  a implementação da API Java que o realiza.
	 *  
	 * @author marques.araujo
	 * @param TecModel
	 * @return int
	 */

	@Override
	public int compareTo(TecModel outro) {

		
		int valor = tecido.compareTo(outro.tecido);
        if(valor==0){
        	valor = modelo.compareTo(outro.modelo);
		 }
	     return (valor != 0 ? valor : 1); 

	}

	/**
	 * Este método poder ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método  "equals(Object obj)".
	 * Eles São necessários para determinarmos um criterio de igualdade entre 2 objetos.
	 * 
	 * Obs.: É primoridal dar atenção para este detalhe, principalmente quando trabalhamos com Estruturas
	 * 		 de Dados como Set.
	 * 
	 *  @return int
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((consumoPorLoteMt == null) ? 0 : consumoPorLoteMt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime * result + ((tecido == null) ? 0 : tecido.hashCode());
		result = prime
				* result
				+ ((tempoDefasagemUsoTec == null) ? 0 : tempoDefasagemUsoTec
						.hashCode());
		return result;
	}

	
	/**
	 * 
	 * Este método pode ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método 
	 * "hashCode()". Estes metodos são necessários para se determinar um critério principal
	 * de igualdade entre 2 objetos. 
	 * 
	 *  @param Object
	 *  @return boolean
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TecModel other = (TecModel) obj;
		if (consumoPorLoteMt == null) {
			if (other.consumoPorLoteMt != null)
				return false;
		} else if (!consumoPorLoteMt.equals(other.consumoPorLoteMt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		if (tecido == null) {
			if (other.tecido != null)
				return false;
		} else if (!tecido.equals(other.tecido))
			return false;
		if (tempoDefasagemUsoTec == null) {
			if (other.tempoDefasagemUsoTec != null)
				return false;
		} else if (!tempoDefasagemUsoTec.equals(other.tempoDefasagemUsoTec))
			return false;
		return true;
	}
	
		
}
