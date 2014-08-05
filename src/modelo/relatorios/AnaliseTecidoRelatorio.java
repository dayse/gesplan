 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo.relatorios;

import java.util.Date;

import modelo.PerioPM;
import modelo.PlPerMod;
import modelo.Tecido;

public class AnaliseTecidoRelatorio {
	
	//----------------- CadPlan ------------------//
	private String codPlan;
	private String descrPlan;
	
	//----------------- PerioPM ------------------//
	private Integer periodo;
	private Date dataInicial;
	private Date dataFinal;

	//----------------- Tecido ------------------//
	private String codTecido;
	private String descrTecido;
	
	
	
	//----------- AnaliseTecidoView -------------//
	private Double necessidadeTotal; 			// Calculado no relatório	
	private Double disponibilidadeMaxDiaria;	// Calculado no relatório	
	private Double comprometimentoKg;			// Calculado no relatório	
	private Double comprometimentoPercentual;	// Calculado no relatório
	
	
	//----------- CapacTecView ------------------//
	private Double consumoMaxDiarioMatriz;

	//---------------- PlPerMod ------------------//
	private String codModelo;
	private String descrModelo;
	private Double prodDiariaLoteModel;
	private Integer periodoPMInicioPMP;
	private Double consumoLoteMt;
	private Double consumoLoteKg;
	private Double consumoDiarioKg;		 
	private Double participacaoPercentual;						// Calculado no relatório
	
	public AnaliseTecidoRelatorio(){
		
	}
	
	public AnaliseTecidoRelatorio(PerioPM perioPM, PlPerMod plPerMod, Tecido tecido){

		// OK
		this.periodo = perioPM.getPeriodoPM();
		this.dataInicial = perioPM.getDataInicial().getTime();
		this.dataFinal = perioPM.getDataFinal().getTime();
		
		//OK
		this.codTecido = tecido.getCodTecido();
		this.descrTecido = tecido.getDescrTecido();
		
		// OK
		this.codModelo = plPerMod.getPlanoModelo().getModelo().getCodModelo();
		this.descrModelo = plPerMod.getPlanoModelo().getModelo().getDescrModelo();
		this.consumoLoteMt = plPerMod.getConsumoLoteMt();
		this.consumoLoteKg = plPerMod.getConsumoLoteKg();
		this.consumoDiarioKg = plPerMod.getConsumoDiarioKg();
		this.prodDiariaLoteModel = plPerMod.getProdDiariaLoteModel();
		this.periodoPMInicioPMP = plPerMod.getPeriodoPMInicioPMP();
		this.participacaoPercentual = plPerMod.getParticipacaoPercentual();
		
	}
	
	// ================================== Métodos get() e set() ================================== //

	public String getCodPlan() {
		return codPlan;
	}

	public String getDescrPlan() {
		return descrPlan;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public String getCodTecido() {
		return codTecido;
	}

	public String getDescrTecido() {
		return descrTecido;
	}

	public Double getNecessidadeTotal() {
		return necessidadeTotal;
	}

	public Double getDisponibilidadeMaxDiaria() {
		return disponibilidadeMaxDiaria;
	}

	public Double getComprometimentoKg() {
		return comprometimentoKg;
	}

	public Double getComprometimentoPercentual() {
		return comprometimentoPercentual;
	}

	public Double getConsumoMaxDiarioMatriz() {
		return consumoMaxDiarioMatriz;
	}

	public String getCodModelo() {
		return codModelo;
	}

	public String getDescrModelo() {
		return descrModelo;
	}

	public Double getConsumoLoteMt() {
		return consumoLoteMt;
	}

	public Double getConsumoLoteKg() {
		return consumoLoteKg;
	}

	public Double getConsumoDiarioKg() {
		return consumoDiarioKg;
	}

	public Double getParticipacaoPercentual() {
		return participacaoPercentual;
	}

	public void setCodPlan(String codPlan) {
		this.codPlan = codPlan;
	}

	public void setDescrPlan(String descrPlan) {
		this.descrPlan = descrPlan;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void setCodTecido(String codTecido) {
		this.codTecido = codTecido;
	}

	public void setDescrTecido(String descrTecido) {
		this.descrTecido = descrTecido;
	}

	public void setNecessidadeTotal(Double necessidadeTotal) {
		this.necessidadeTotal = necessidadeTotal;
	}

	public void setDisponibilidadeMaxDiaria(Double disponibilidadeMaxDiaria) {
		this.disponibilidadeMaxDiaria = disponibilidadeMaxDiaria;
	}

	public void setComprometimentoKg(Double comprometimentoKg) {
		this.comprometimentoKg = comprometimentoKg;
	}

	public void setComprometimentoPercentual(Double comprometimentoPercentual) {
		this.comprometimentoPercentual = comprometimentoPercentual;
	}

	public void setConsumoMaxDiarioMatriz(Double consumoMaxDiarioMatriz) {
		this.consumoMaxDiarioMatriz = consumoMaxDiarioMatriz;
	}

	public void setCodModelo(String codModelo) {
		this.codModelo = codModelo;
	}

	public void setDescrModelo(String descrModelo) {
		this.descrModelo = descrModelo;
	}

	public void setConsumoLoteMt(Double consumoLoteMt) {
		this.consumoLoteMt = consumoLoteMt;
	}

	public void setConsumoLoteKg(Double consumoLoteKg) {
		this.consumoLoteKg = consumoLoteKg;
	}

	public void setConsumoDiarioKg(Double consumoDiarioKg) {
		this.consumoDiarioKg = consumoDiarioKg;
	}

	public void setParticipacaoPercentual(Double participacaoPercentual) {
		this.participacaoPercentual = participacaoPercentual;
	}

	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}

	public Integer getPeriodoPMInicioPMP() {
		return periodoPMInicioPMP;
	}

	public void setProdDiariaLoteModel(Double prodDiariaLoteModel) {
		this.prodDiariaLoteModel = prodDiariaLoteModel;
	}

	public void setPeriodoPMInicioPMP(Integer periodoPMInicioPMP) {
		this.periodoPMInicioPMP = periodoPMInicioPMP;
	}
	
	

}
