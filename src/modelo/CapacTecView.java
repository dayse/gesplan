 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.util.List;

/**
 * Classe relativa a entidade que informa para cada TECIDO o consumo m�ximo di�rio poss�vel na Matriz
 * em Kg para cada PERIOPM( ou seja a  disponibilidade di�ria de tecido na Matriz).
 * 
 * Seria uma entidade equivalente ao CapacRec por�m � apenas vis�o.  N�o � persistido em
 * arquivo como o CapacRec
 * 
 * 
 * @author daysemou/Felipe
 *
 */
public class CapacTecView {
	
	/**
	 * Disponibilidade Diaria de Tecido na Matriz (em Kg)
	 * 
	 * Calculada para cada periodo i da seguinte forma:
	 * 
	 * Tecido.ProdDiariaMaxU2 * PerioPM.NumDiasUteisU2(i)/PerioPM.NumDiasUteisMatriz(i);
	 * 
	 * ou seja, como o numero de dias uteis na matriz por per�odo � diferente do numero de dias uteis na 
	 * Unidade 2, faz uma convers�o
	 * 
	 */
	private Double consumoMaxDiarioMatriz;

	private Tecido tecido;
	
	private PerioPM perioPM;
	
	
	
	public CapacTecView(){
		
	}
	
	// ================================== M�todos get() e set() ================================== //

	public Double getConsumoMaxDiarioMatriz() {
		return consumoMaxDiarioMatriz;
	}



	public void setConsumoMaxDiarioMatriz(Double consumoMaxDiarioMatriz) {
		this.consumoMaxDiarioMatriz = consumoMaxDiarioMatriz;
	}



	public Tecido getTecido() {
		return tecido;
	}



	public void setTecido(Tecido tecido) {
		this.tecido = tecido;
	}



	public PerioPM getPerioPM() {
		return perioPM;
	}



	public void setPerioPM(PerioPM perioPM) {
		this.perioPM = perioPM;
	}
}
