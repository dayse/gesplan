 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class AnaliseTecidoView {
	
	private Double necessidadeTotal;
	
	private Double disponibilidadeMaxDiaria;
	
	private Double comprometimentoKg;
	
	private Double comprometimentoPercentual;
	
	
	public AnaliseTecidoView(){
		
	}
	
	// ================================== Métodos get() e set() ================================== //
	
	public Double getNecessidadeTotal() {
		return necessidadeTotal;
	}

	public void setNecessidadeTotal(Double necessidadeTotal) {
		this.necessidadeTotal = necessidadeTotal;
	}

	public Double getDisponibilidadeMaxDiaria() {
		return disponibilidadeMaxDiaria;
	}

	public void setDisponibilidadeMaxDiaria(Double disponibilidadeMaxDiaria) {
		this.disponibilidadeMaxDiaria = disponibilidadeMaxDiaria;
	}

	public Double getComprometimentoKg() {
		return comprometimentoKg;
	}

	public void setComprometimentoKg(Double comprometimentoKg) {
		this.comprometimentoKg = comprometimentoKg;
	}

	public Double getComprometimentoPercentual() {
		return comprometimentoPercentual;
	}

	public void setComprometimentoPercentual(Double comprometimentoPercentual) {
		this.comprometimentoPercentual = comprometimentoPercentual;
	}
}
