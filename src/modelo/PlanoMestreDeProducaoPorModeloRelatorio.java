 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.util.Date;
import java.util.Date;


/**
 * Classe utilizada para criar objetos que serao utilizados na geraçao de relatorios. Esta classe agrega outras
 * 6 classes, ou seja, um objeto criado a partir desta classe necessita de informaçoes , atributos, que estao em
 * outras classes. 
 * 
 * @author marques.araujo
 *
 */
public class PlanoMestreDeProducaoPorModeloRelatorio implements Comparable<PlanoMestreDeProducaoPorModeloRelatorio>{
	
	
	
	//-------------- CadPlan ---------------//
	
	private String codPlan;
	private String descrPlan;
	
	//-------------- Familia ----------------//
	
	private String descrFamilia;
	
	//-------------- Modelo ----------------//
	
	private String codModelo;
	private String descrModelo;
	private double indiceCob;
	private double tamLote;
	private double cobertura;
	private double estqInicModel;
	private String flagProducaoModel; 
	
	//-------------- PlanoModelo ------------//
	
	private Double trOriginal;
	private String flagModeloPlanejado;
	
	
	//-------------- PerioPM ---------------//
	
	private Integer periodoPM;    
	private Date dataInicial; 
	private Date dataFinal;   
		
	//-------------- PlPerMod ---------------//
	
	private Double vendasModel;
	private Double pedidosModel;
	private Integer periodoPMInicioPMP;
	private Double producaoModel;
	private Double dispProjModel;
	private Double coberturaModel;
	private Double prodLoteModel;
	private Double prodDiariaLoteModel;
	private String flagProducaoModelPlperMod; 
	
	
	public PlanoMestreDeProducaoPorModeloRelatorio(){
		
	}
	
	public PlanoMestreDeProducaoPorModeloRelatorio(CadPlan cadplan, PlanoModelo planoModelo, PlPerMod plpermod, Familia familia ){
		
		//CadPlan
		this.codPlan = cadplan.getCodPlan();
		this.descrPlan = cadplan.getDescrPlan();
		
		//Familia
		this.descrFamilia = familia.getDescrFamilia();
		
		//Modelo
		this.codModelo = planoModelo.getModelo().getCodModelo();
		this.descrModelo = planoModelo.getModelo().getDescrModelo();
		this.indiceCob = planoModelo.getModelo().getIndiceCob();
		this.tamLote = planoModelo.getModelo().getTamLote();
		this.cobertura = planoModelo.getModelo().getCobertura();
		this.estqInicModel = planoModelo.getModelo().getEstqInicModel();
		
		if ((planoModelo.getModelo()).getFlagProducaoModel()){
			
			this.flagProducaoModel = "Livre";
			
		} else {
			
			this.flagProducaoModel = "Fixo";
			
		}
		
		//PlanoModelo
		this.trOriginal = planoModelo.getTrOriginal();
		
		if(planoModelo.isModeloPlanejado()){
			
			this.flagModeloPlanejado = "Planejado";
			
		}else{
			
			this.flagModeloPlanejado = "Nao planejado";
			
		}
		
		if(plpermod==null){
		
		//PlPerMod
		this.vendasModel = plpermod.getVendasModel();
		this.pedidosModel = plpermod.getPedidosModel();
		this.periodoPMInicioPMP = plpermod.getPeriodoPMInicioPMP() ;
		this.producaoModel = plpermod.getProducaoModel();
		this.dispProjModel = plpermod.getDispProjModel();
		this.coberturaModel = plpermod.getCoberturaModel();
		this.prodLoteModel = plpermod.getProdLoteModel();
		this.prodDiariaLoteModel = plpermod.getProdDiariaLoteModel();
		this.flagProducaoModelPlperMod = null; 
		
		//PerioPM
		this.periodoPM = null;
		this.dataInicial = (plpermod.getPerioPM().getDataInicial()).getTime();
		this.dataFinal =   (plpermod.getPerioPM().getDataFinal()).getTime();
		
		}else{
			
			//PlPerMod
			this.vendasModel = plpermod.getVendasModel();
			this.pedidosModel = plpermod.getPedidosModel();
			this.periodoPMInicioPMP = plpermod.getPeriodoPMInicioPMP() ;
			this.producaoModel = plpermod.getProducaoModel();
			this.dispProjModel = plpermod.getDispProjModel();
			this.coberturaModel = plpermod.getCoberturaModel();
			this.prodLoteModel = plpermod.getProdLoteModel();
			this.prodDiariaLoteModel = plpermod.getProdDiariaLoteModel();
			
			if(plpermod.getFlagProducaoModel()){
				
				this.flagProducaoModelPlperMod = "Livre";
				
			}else{
				
				this.flagProducaoModelPlperMod = "Fixo";
				
			}
			
			//PerioPM
			this.periodoPM =  Integer.valueOf(plpermod.getPerioPM().getPeriodoPM());
			this.dataInicial = plpermod.getPerioPM().getDataInicial().getTime();
			this.dataFinal =   (plpermod.getPerioPM().getDataFinal()).getTime();

		}
		
	}
	
	
	//-------- métodos get e set --------//
	
	public String getCodPlan() {
		return codPlan;
	}
	public void setCodPlan(String codPlan) {
		this.codPlan = codPlan;
	}
	public String getDescrPlan() {
		return descrPlan;
	}
	public void setDescrPlan(String descrPlan) {
		this.descrPlan = descrPlan;
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
	public String getFlagProducaoModel() {
		return flagProducaoModel;
	}
	public void setFlagProducaoModel(String flagProducaoModel) {
		this.flagProducaoModel = flagProducaoModel;
	}
	public double getIndiceCob() {
		return indiceCob;
	}
	public void setIndiceCob(double indiceCob) {
		this.indiceCob = indiceCob;
	}
	public double getTamLote() {
		return tamLote;
	}
	public void setTamLote(double tamLote) {
		this.tamLote = tamLote;
	}
	public double getCobertura() {
		return cobertura;
	}
	public void setCobertura(double cobertura) {
		this.cobertura = cobertura;
	}
	public double getEstqInicModel() {
		return estqInicModel;
	}
	public void setEstqInicModel(double estqInicModel) {
		this.estqInicModel = estqInicModel;
	}
	public String getDescrFamilia() {
		return descrFamilia;
	}
	public void setDescrFamilia(String descrFamilia) {
		this.descrFamilia = descrFamilia;
	}
	public String getFlagModeloPlanejado() {
		return flagModeloPlanejado;
	}
	public void setFlagModeloPlanejado(String flagModeloPlanejado) {
		this.flagModeloPlanejado = flagModeloPlanejado;
	}
	public Double getTrOriginal() {
		return trOriginal;
	}
	public void setTrOriginal(Double trOriginal) {
		this.trOriginal = trOriginal;
	}
	public Integer getPeriodoPM() {
		return periodoPM;
	}
	public void setPeriodoPM(Integer periodoPM) {
		this.periodoPM = periodoPM;
	}
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public Double getVendasModel() {
		return vendasModel;
	}
	public void setVendasModel(Double vendasModel) {
		this.vendasModel = vendasModel;
	}
	public Double getPedidosModel() {
		return pedidosModel;
	}
	public void setPedidosModel(Double pedidosModel) {
		this.pedidosModel = pedidosModel;
	}
	public Integer getPeriodoPMInicioPMP() {
		return periodoPMInicioPMP;
	}
	public void setPeriodoPMInicioPMP(Integer periodoPMInicioPMP) {
		this.periodoPMInicioPMP = periodoPMInicioPMP;
	}
	public Double getProducaoModel() {
		return producaoModel;
	}
	public void setProducaoModel(Double producaoModel) {
		this.producaoModel = producaoModel;
	}
	public Double getDispProjModel() {
		return dispProjModel;
	}
	public void setDispProjModel(Double dispProjModel) {
		this.dispProjModel = dispProjModel;
	}
	public Double getCoberturaModel() {
		return coberturaModel;
	}
	public void setCoberturaModel(Double coberturaModel) {
		this.coberturaModel = coberturaModel;
	}
	public Double getProdLoteModel() {
		return prodLoteModel;
	}
	public void setProdLoteModel(Double prodLoteModel) {
		this.prodLoteModel = prodLoteModel;
	}
	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}
	public void setProdDiariaLoteModel(Double prodDiariaLoteModel) {
		this.prodDiariaLoteModel = prodDiariaLoteModel;
	}
	public String getFlagProducaoModelPlperMod() {
		return flagProducaoModelPlperMod;
	}
	public void setFlagProducaoModelPlperMod(String flagProducaoModelPlperMod) {
		this.flagProducaoModelPlperMod = flagProducaoModelPlperMod;
	}

	/**
	 * 
	 *  Este método consiste em definir o critério de ordenação entre 2 objetos PlanoMestreDeProducaoPorModeloRelatorio,
	 *  que deve estar associado a um de seus atributos. No nosso caso, os atributos em questão são o 'codPlan', 'codModelo',
	 *  e 'periodoPM' , estes atributos tambem sao objetos que possuem um criterio de comparaçao internamente e podem se auto-ordenar,
	 *  graças a implementação da API Java que o realiza.
	 *  
	 * @author marques.araujo
	 * @param TecModel
	 * @return int
	 */
	
	@Override
	public int compareTo(PlanoMestreDeProducaoPorModeloRelatorio plm) {
		
	        int valor = this.codPlan.compareTo(plm.getCodPlan());
    		if(valor==0){
				
				  valor = this.codModelo.compareTo(plm.codModelo);
				  if(valor==0){
				  	  
					  valor = this.periodoPM.compareTo(plm.getPeriodoPM());
					  
				  }
			}
		
			return valor;
     }
	
	
	
}
