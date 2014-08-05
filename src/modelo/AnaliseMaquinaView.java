 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class AnaliseMaquinaView {
	
	private Double necessidadeTotal;
	
	private Double capacidade;
	
	private Double comprometimentoMinutos;
	
	private Double comprometimentoPercentual;
	
		
	
	public AnaliseMaquinaView(){
		
	}
	
	// ================================== Métodos get() e set() ================================== //

	public Double getNecessidadeTotal() {
		return necessidadeTotal;
	}


	public Double getCapacidade() {
		return capacidade;
	}


	public Double getComprometimentoMinutos() {
		return comprometimentoMinutos;
	}


	public Double getComprometimentoPercentual() {
		return comprometimentoPercentual;
	}


	public void setNecessidadeTotal(Double necessidadeTotal) {
		this.necessidadeTotal = necessidadeTotal;
	}


	public void setCapacidade(Double capacidade) {
		this.capacidade = capacidade;
	}

	public void setComprometimentoMinutos(Double comprometimentoMinutos) {
		this.comprometimentoMinutos = comprometimentoMinutos;
	}


	public void setComprometimentoPercentual(Double comprometimentoPercentual) {
		this.comprometimentoPercentual = comprometimentoPercentual;
	}
	
}
