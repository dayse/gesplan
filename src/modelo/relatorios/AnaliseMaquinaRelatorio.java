 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo.relatorios;

import java.util.Calendar;
import java.util.Date;

import modelo.AnaliseMaquinaView;
import modelo.CadPlan;
import modelo.PerioPM;
import modelo.PlPerMod;

public class AnaliseMaquinaRelatorio {
	
	//----------------- CadPlan ------------------//
	private String codPlan;
	private String descrPlan;
	
	//----------------- PerioPM ------------------//
	private Integer periodo;
	private Date dataInicial;
	private Date dataFinal;
	
	//----------- AnaliseMaquinaView -------------//
	private Double necessidadeTotal;					// Calculado no relatório
	private Double comprometimentoMinutos;				// Calculado no relatório
	private Double comprometimentoPercentual;			// Calculado no relatório
	
	//----------- CapacDia ------------------//
	private Double capacDia;
	
	//---------------- PlPerMod ------------------//
	private String codModelo;
	private String descrModelo;
	private Double prodDiariaLoteModel;
	private Double prodDiariaLoteMin;				 
	private Double participacaoPercentual;							// Calculado no relatório
	
	public AnaliseMaquinaRelatorio(){
		
	}
	
	public AnaliseMaquinaRelatorio(PerioPM perioPM, PlPerMod plPerMod){
		
		// OK
		this.periodo = perioPM.getPeriodoPM();
		this.dataInicial = perioPM.getDataInicial().getTime();
		this.dataFinal = perioPM.getDataFinal().getTime();
		
		// OK
		this.codModelo = plPerMod.getPlanoModelo().getModelo().getCodModelo();
		this.descrModelo = plPerMod.getPlanoModelo().getModelo().getDescrModelo();
		this.prodDiariaLoteModel = plPerMod.getProdDiariaLoteModel();
		this.prodDiariaLoteMin = plPerMod.getProdDiariaLoteModel() * plPerMod.getPlanoModelo().getModelo().getTuc() * plPerMod.getPlanoModelo().getModelo().getTamLote();
		this.participacaoPercentual = plPerMod.getParticipacaoPercentual();
	}
	
	// ================================== Métodos get() e set() ================================== //
	
	public Integer getPeriodo() {
		return periodo;
	}
	public Date getDataInicial() {
		return dataInicial;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public Double getNecessidadeTotal() {
		return necessidadeTotal;
	}
	public Double getComprometimentoMinutos() {
		return comprometimentoMinutos;
	}
	public Double getComprometimentoPercentual() {
		return comprometimentoPercentual;
	}
	public String getCodModelo() {
		return codModelo;
	}
	public String getDescrModelo() {
		return descrModelo;
	}
	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}
	public Double getProdDiariaLoteMin() {
		return prodDiariaLoteMin;
	}
	public Double getParticipacaoPercentual() {
		return participacaoPercentual;
	}
	public Double getCapacDia() {
		return capacDia;
	}
	public String getCodPlan() {
		return codPlan;
	}
	public String getDescrPlan() {
		return descrPlan;
	}
	
	public void setPeriodoPM(Integer periodo) {
		this.periodo = periodo;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public void setNecessidadeTotal(Double necessidadeTotal) {
		this.necessidadeTotal = necessidadeTotal;
	}
	public void setComprometimentoMinutos(Double comprometimentoMinutos) {
		this.comprometimentoMinutos = comprometimentoMinutos;
	}
	public void setComprometimentoPercentual(Double comprometimentoPercentual) {
		this.comprometimentoPercentual = comprometimentoPercentual;
	}
	public void setCodModelo(String codModelo) {
		this.codModelo = codModelo;
	}
	public void setDescrModelo(String descrModelo) {
		this.descrModelo = descrModelo;
	}
	public void setCapacDia(Double capacDia) {
		this.capacDia = capacDia;
	}
	public void setProdDiariaLoteModel(Double prodDiariaLoteModel) {
		this.prodDiariaLoteModel = prodDiariaLoteModel;
	}
	public void setProdDiariaLoteMin(Double prodDiariaLoteMin) {
		this.prodDiariaLoteMin = prodDiariaLoteMin;
	}
	public void setParticipacaoPercentual(Double participacaoPercentual) {
		this.participacaoPercentual = participacaoPercentual;
	}
	public void setCodPlan(String codPlan) {
		this.codPlan = codPlan;
	}
	public void setDescrPlan(String descrPlan) {
		this.descrPlan = descrPlan;
	}
}
