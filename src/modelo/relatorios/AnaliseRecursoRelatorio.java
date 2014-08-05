 
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
import modelo.Recurso;

public class AnaliseRecursoRelatorio {
	
	//----------------- CadPlan ------------------//
	private String codPlan;
	private String descrPlan;
	
	//----------------- PerioPM ------------------//
	private Integer periodo;
	private Date dataInicial;
	private Date dataFinal;

	//----------------- Recurso ------------------//
	private String codRecurso;
	private String descrRecurso;
	
	
	
	//----------- AnaliseRecursoView -------------//

	

	private Double necessidadeTotal; 			// Calculado no relatório	
	private Double capacidade;					// Calculado no relatório
	private Double comprometimento;				// Calculado no relatório
	private Double comprometimentoPercentual;	// Calculado no relatório
	private Double custoTotal;					// Calculado no relatório
	
	
	//---------------- PlPerMod ------------------//
	private String codModelo;
	private String descrModelo;
	private Double prodDiariaLoteModel;
	private Double consumoUnitario;					// Calculado no relatório
	private Double consumoDiario;					// Calculado no relatório
	private Integer periodoPMInicioPMP;
	private Double custoDiario;					// Calculado no relatório
	
	public AnaliseRecursoRelatorio(){
		
	}
	
	public AnaliseRecursoRelatorio(PerioPM perioPM, PlPerMod plPerMod, Recurso recurso){

		// OK
		this.periodo = perioPM.getPeriodoPM();
		this.dataInicial = perioPM.getDataInicial().getTime();
		this.dataFinal = perioPM.getDataFinal().getTime();
		
		
		// OK
		this.codModelo = plPerMod.getPlanoModelo().getModelo().getCodModelo();
		this.descrModelo = plPerMod.getPlanoModelo().getModelo().getDescrModelo();
		this.prodDiariaLoteModel = plPerMod.getProdDiariaLoteModel();
		this.consumoUnitario = plPerMod.getConsumoUnitario();
		this.consumoDiario = plPerMod.getConsumoDiario();
		this.periodoPMInicioPMP = plPerMod.getPeriodoPMInicioPMP();
		this.custoDiario = plPerMod.getCustoDiario();
		
		this.codRecurso=recurso.getCodRecurso();
		this.descrRecurso=recurso.getDescrRecurso();
		
		
	}
	
	// ================================== Métodos get() e set() ================================== //

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

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
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

	public String getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}

	public String getDescrRecurso() {
		return descrRecurso;
	}

	public void setDescrRecurso(String descrRecurso) {
		this.descrRecurso = descrRecurso;
	}

	public Double getNecessidadeTotal() {
		return necessidadeTotal;
	}

	public void setNecessidadeTotal(Double necessidadeTotal) {
		this.necessidadeTotal = necessidadeTotal;
	}

	public Double getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(Double capacidade) {
		this.capacidade = capacidade;
	}

	public Double getComprometimento() {
		return comprometimento;
	}

	public void setComprometimento(Double comprometimento) {
		this.comprometimento = comprometimento;
	}

	public Double getComprometimentoPercentual() {
		return comprometimentoPercentual;
	}

	public void setComprometimentoPercentual(Double comprometimentoPercentual) {
		this.comprometimentoPercentual = comprometimentoPercentual;
	}

	public Double getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(Double custoTotal) {
		this.custoTotal = custoTotal;
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

	public Double getProdDiariaLoteModel() {
		return prodDiariaLoteModel;
	}

	public void setProdDiariaLoteModel(Double prodDiariaLoteModel) {
		this.prodDiariaLoteModel = prodDiariaLoteModel;
	}

	public Double getConsumoUnitario() {
		return consumoUnitario;
	}

	public void setConsumoUnitario(Double consumoUnitario) {
		this.consumoUnitario = consumoUnitario;
	}

	public Double getConsumoDiario() {
		return consumoDiario;
	}

	public void setConsumoDiario(Double consumoDiario) {
		this.consumoDiario = consumoDiario;
	}

	public Integer getPeriodoPMInicioPMP() {
		return periodoPMInicioPMP;
	}

	public void setPeriodoPMInicioPMP(Integer periodoPMInicioPMP) {
		this.periodoPMInicioPMP = periodoPMInicioPMP;
	}

	public Double getCustoDiario() {
		return custoDiario;
	}

	public void setCustoDiario(Double custoDiario) {
		this.custoDiario = custoDiario;
	}

	

}
