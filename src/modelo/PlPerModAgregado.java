 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class PlPerModAgregado {
	
	private PlanoModelo planoModelo;

	private PerioPM perioPM;
	
	private Double vendasModel;
	
	private Double vendasAmortModel;
	
	private Double pedidosModel;
	
	private Double dispProjModel;
	
	private Double coberturaModel;
	
	private Double producaoModel;
	
	private Double prodLoteModel;
	
	private Double prodDiariaLoteModel;
	
	private Double inicioProdAntesHP;
	
	private Boolean flagProducaoModel;
	
	private Double escorePlanPerMod;
	
	private Double varEstqPerc;
	
	private Double varProdDiaPer;
	
	
	public PlPerModAgregado(){
		
		this.vendasModel = 0.0;
		this.pedidosModel = 0.0;
		this.producaoModel = 0.0;
		this.dispProjModel = 0.0;
		this.coberturaModel = 0.0;
	}
	
	public PlPerModAgregado(Double vendasModel, Double vendasAmortModel, Double pedidosModel, Double dispProjModel,
			Double coberturaModel, Double producaoModel, Double prodLoteModel, Double prodDiariaLoteModel, 
			Double inicioProdAntesHP, boolean flagProducaoModel, Double escorePlanPerMod, Double varEstqPerc, Double varProdDiaPer)
		{
		
		this.vendasModel = vendasModel;
		this.vendasAmortModel = vendasAmortModel;
		this.pedidosModel = pedidosModel;
		this.dispProjModel = dispProjModel;
		this.coberturaModel = coberturaModel;
		this.producaoModel = producaoModel;
		this.prodLoteModel = prodLoteModel;
		this.prodDiariaLoteModel = prodDiariaLoteModel;
		this.inicioProdAntesHP = inicioProdAntesHP;
		this.flagProducaoModel = flagProducaoModel;
		this.escorePlanPerMod = escorePlanPerMod;
		this.varEstqPerc = varEstqPerc;
		this.varProdDiaPer = varProdDiaPer;
	}
	
	// ================================== Métodos get() e set() ================================== //

	public PlanoModelo getPlanoModelo() {
		return planoModelo;
	}

	public PerioPM getPerioPM() {
		return perioPM;
	}

	public Double getVendasModel() {
		return vendasModel;
	}

	public Double getVendasAmortModel() {
		return vendasAmortModel;
	}

	public Double getPedidosModel() {
		return pedidosModel;
	}

	public Double getDispProjModel() {
		return dispProjModel;
	}

	public Double getCoberturaModel() {
		return coberturaModel;
	}

	public Double getProducaoModel() {
		return producaoModel;
	}

	public Double getProdLoteModel() {
		return prodLoteModel;
	}

	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}

	public Double getInicioProdAntesHP() {
		return inicioProdAntesHP;
	}

	public Boolean getFlagProducaoModel() {
		return flagProducaoModel;
	}

	public Double getEscorePlanPerMod() {
		return escorePlanPerMod;
	}

	public Double getVarEstqPerc() {
		return varEstqPerc;
	}

	public Double getVarProdDiaPer() {
		return varProdDiaPer;
	}

	public void setPlanoModelo(PlanoModelo planoModelo) {
		this.planoModelo = planoModelo;
	}

	public void setPerioPM(PerioPM periodoPM) {
		this.perioPM = periodoPM;
	}

	public void setVendasModel(Double vendasModel) {
		this.vendasModel = vendasModel;
	}

	public void setVendasAmortModel(Double vendasAmortModel) {
		this.vendasAmortModel = vendasAmortModel;
	}

	public void setPedidosModel(Double pedidosModel) {
		this.pedidosModel = pedidosModel;
	}

	public void setDispProjModel(Double dispProjModel) {
		this.dispProjModel = dispProjModel;
	}

	public void setCoberturaModel(Double coberturaModel) {
		this.coberturaModel = coberturaModel;
	}

	public void setProducaoModel(Double producaoModel) {
		this.producaoModel = producaoModel;
	}

	public void setProdLoteModel(Double prodLoteModel) {
		this.prodLoteModel = prodLoteModel;
	}

	public void setProdDiariaLoteModel(Double prodDiariaLoteModel) {
		this.prodDiariaLoteModel = prodDiariaLoteModel;
	}

	public void setInicioProdAntesHP(Double inicioProdAntesHP) {
		this.inicioProdAntesHP = inicioProdAntesHP;
	}

	public void setFlagProducaoModel(Boolean flagProducaoModel) {
		this.flagProducaoModel = flagProducaoModel;
	}

	public void setEscorePlanPerMod(Double escorePlanPerMod) {
		this.escorePlanPerMod = escorePlanPerMod;
	}

	public void setVarEstqPerc(Double varEstqPerc) {
		this.varEstqPerc = varEstqPerc;
	}

	public void setVarProdDiaPer(Double varProdDiaPer) {
		this.varProdDiaPer = varProdDiaPer;
	} 
}
