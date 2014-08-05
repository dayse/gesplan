 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import modelo.Familia;
import modelo.Modelo;


public class FamiliaRelatorio {
	
	
		
	//----------- Familia -------------//
	private String codFamilia;
	private String descrFamilia;
	private Double familiaCobertura;
	private Double estqInicFam;
	private Double tmuc;

	
	//----------- Modelo -------------//
	private String codModelo;
	private String descrModelo;
	private boolean flagProducaoModel;
	private Double indiceCob;
	private Double tuc;
	private Double tamLote;
	private Double tr;
	private Double modeloCobertura;
	private Double estqInicModel;
	

	
	//----------- FamiliaRelatorio ------------//
	private String flagProducaoTexto;

	public FamiliaRelatorio(){
		
	}
	
	
	
	public FamiliaRelatorio(Familia familia, Modelo modelo){
		
			this.codFamilia = familia.getCodFamilia();
			this.descrFamilia = familia.getDescrFamilia();
			this.familiaCobertura =familia.getCobertura();
			this.estqInicFam = familia.getEstqInicFam();
			this.tmuc = familia.getTmuc();
		
		if(modelo != null){
			
			this.codModelo = modelo.getCodModelo();
			this.descrModelo = modelo.getDescrModelo();
			this.indiceCob = modelo.getIndiceCob();
			this.tuc = modelo.getTuc();
			this.tamLote = modelo.getTamLote();
			this.tr =	modelo.getTr();
			this.modeloCobertura = modelo.getCobertura();
		    this.estqInicModel = modelo.getEstqInicModel();
		    
		    if (modelo.getFlagProducaoModel()){
				this.flagProducaoTexto = Modelo.FLAG_LIVRE;
				
			} else {
				this.flagProducaoTexto = Modelo.FLAG_FIXO;
			}
	        
		} else {
			
			this.codModelo = null;
			this.descrModelo = null;
			this.indiceCob = null;
			this.tuc = null;
			this.tamLote = null;
			this.tr = null;
			this.modeloCobertura = null;
			this.estqInicModel = null;
			this.flagProducaoTexto = null;
		}
	}
		
	
	public String getCodFamilia() {
		return codFamilia;
	}


	public void setCodFamilia(String codFamilia) {
		this.codFamilia = codFamilia;
	}


	public String getDescrFamilia() {
		return descrFamilia;
	}


	public void setDescrFamilia(String descrFamilia) {
		this.descrFamilia = descrFamilia;
	}

	public Double getFamiliaCobertura() {
		return familiaCobertura;
	}

	public void setFamiliaCobertura(Double familiaCobertura) {
		this.familiaCobertura = familiaCobertura;
	}

	public Double getEstqInicFam() {
		return estqInicFam;
	}

	public void setEstqInicFam(Double estqInicFam) {
		this.estqInicFam = estqInicFam;
	}

	public Double getTmuc() {
		return tmuc;
	}

	public void setTmuc(Double tmuc) {
		this.tmuc = tmuc;
	}

	public boolean getFlagProducaoModel() {
		return flagProducaoModel;
	}

	public void setFlagProducaoModel(boolean flagProducaoModel) {
		this.flagProducaoModel = flagProducaoModel;
	}

	public Double getIndiceCob() {
		return indiceCob;
	}

	public void setIndiceCob(Double indiceCob) {
		this.indiceCob = indiceCob;
	}

	public Double getTuc() {
		return tuc;
	}

	public void setTuc(Double tuc) {
		this.tuc = tuc;
	}

	public Double getEstqInicModel() {
		return estqInicModel;
	}

	public void setEstqInicModel(Double estqInicModel) {
		this.estqInicModel = estqInicModel;
	}

	public String getCodModelo() {
		return codModelo;
	}

	public void setCodModelo(String codModelo) {
		this.codModelo = codModelo;
	}

	public String getDescrModelo() {
		return descrModelo;
	}

	public void setDescrModelo(String descrModelo) {
		this.descrModelo = descrModelo;
	}

	public Double getTamLote() {
		return tamLote;
	}

	public void setTamLote(Double tamLote) {
		this.tamLote = tamLote;
	}

	public Double getTr() {
		return tr;
	}

	public void setTr(Double tr) {
		this.tr = tr;
	}

	public String getFlagProducaoTexto() {
		return flagProducaoTexto;
	}

	public void setFlagProducaoTexto(String flagProducaoTexto) {
		this.flagProducaoTexto = flagProducaoTexto;
	}

	public Double getModeloCobertura() {
		return modeloCobertura;
	}
	
	public void setModeloCobertura(Double modeloCobertura) {
		this.modeloCobertura = modeloCobertura;
	}
}
