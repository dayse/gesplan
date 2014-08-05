 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class AnaliseRecursoView {
	
	private Double necessidadeTotal;
	
	private Double capacidade;
	
	private Double comprometimento;
	
	private Double comprometimentoPercentual;
	
	private Double custoTotal;
	
	
	public AnaliseRecursoView(){
		
	}
	
	// ================================== Métodos get() e set() ================================== //
	
	public Double getNecessidadeTotal() {
		return necessidadeTotal;
	}


	public Double getCapacidade() {
		return capacidade;
	}


	public Double getComprometimento() {
		return comprometimento;
	}


	public Double getComprometimentoPercentual() {
		return comprometimentoPercentual;
	}


	public Double getCustoTotal() {
		return custoTotal;
	}


	public void setNecessidadeTotal(Double necessidadeTotal) {
		this.necessidadeTotal = necessidadeTotal;
	}


	public void setCapacidade(Double capacidade) {
		this.capacidade = capacidade;
	}


	public void setComprometimento(Double comprometimento) {
		this.comprometimento = comprometimento;
	}


	public void setComprometimentoPercentual(Double comprometimentoPercentual) {
		this.comprometimentoPercentual = comprometimentoPercentual;
	}


	public void setCustoTotal(Double custoTotal) {
		this.custoTotal = custoTotal;
	}
}
