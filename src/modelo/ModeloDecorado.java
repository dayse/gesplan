 
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


public class ModeloDecorado {

	private Modelo modelo;
	
	private String flagProducaoTexto;
	
	public ModeloDecorado(Modelo modelo, boolean flagProducao){
		
		this.modelo = modelo;
		
		if (flagProducao){
			this.flagProducaoTexto = Modelo.FLAG_LIVRE;
		} else {
			this.flagProducaoTexto = Modelo.FLAG_FIXO;
		}
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setFlagProducaoTexto(String flagProducaoTexto) {
		this.flagProducaoTexto = flagProducaoTexto;
	}

	public String getFlagProducaoTexto() {
		return flagProducaoTexto;
	}

}
