 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class ModeloRelatorio {
	
	private String codModelo;
	private String descrModelo;
	private String codFamilia;
	private String descrFamilia;
	private String flagProducaoModel;
	private String indiceCob;
	private String tuc;
	private String tamLote;
	private String tr;
	private String cobertura;
	private String estqInicModel;
	
	public ModeloRelatorio(Modelo modelo){
		
		this.codModelo = modelo.getCodModelo();
		this.descrModelo = modelo.getDescrModelo();
		this.codFamilia = modelo.getFamilia().getCodFamilia();
		this.descrFamilia = modelo.getFamilia().getDescrFamilia();
		this.indiceCob = Double.toString(modelo.getIndiceCob());
		this.tuc = Double.toString(modelo.getTuc());
		this.tamLote = Double.toString(modelo.getTamLote());
		this.tr = Double.toString(modelo.getTr());
		this.cobertura = Double.toString(modelo.getCobertura());
		this.estqInicModel = Double.toString(modelo.getEstqInicModel());
		
		if (modelo.getFlagProducaoModel()){
			this.flagProducaoModel = Modelo.FLAG_LIVRE;
		} else {
			this.flagProducaoModel = Modelo.FLAG_FIXO;
		}
	}

	// ================================== Métodos get() e set() ================================== //

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

	public String getFlagProducaoModel() {
		return flagProducaoModel;
	}

	public void setFlagProducaoModel(String flagProducaoModel) {
		this.flagProducaoModel = flagProducaoModel;
	}

	public String getIndiceCob() {
		return indiceCob;
	}

	public void setIndiceCob(String indiceCob) {
		this.indiceCob = indiceCob;
	}

	public String getTuc() {
		return tuc;
	}

	public void setTuc(String tuc) {
		this.tuc = tuc;
	}

	public String getTamLote() {
		return tamLote;
	}

	public void setTamLote(String tamLote) {
		this.tamLote = tamLote;
	}

	public String getTr() {
		return tr;
	}

	public void setTr(String tr) {
		this.tr = tr;
	}

	public String getCobertura() {
		return cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public String getEstqInicModel() {
		return estqInicModel;
	}

	public void setEstqInicModel(String estqInicModel) {
		this.estqInicModel = estqInicModel;
	}

}
