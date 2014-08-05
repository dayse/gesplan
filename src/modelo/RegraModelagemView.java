 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class RegraModelagemView {
	
	private Double valor;
	
	private int indice;
	
	
	public RegraModelagemView(){
		
	}


	// ================================== Métodos get() e set() ================================== //

	public Double getValor() {
		return valor;
	}


	public void setValor(Double valor) {
		this.valor = valor;
	}



	public int getIndice() {
		return indice;
	}


	public void setIndice(int indice) {
		this.indice = indice;
	}


	@Override
	public String toString() {
		return "Regra: " + indice;
	}
	
	
}
