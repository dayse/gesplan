 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class TecidoRelatorio {
	
	//----------- Tecido -------------//
	private String codTecido;
	private String descrTecido;
	private String UM;
	private Double custoUnit;
	private Double leadTimeUnidade2;
	private Double fatorDeRendimento;
	private Double producaoDiariaMaxUnidade2;
	
	
	//----------- Modelo -------------//
	private String codModelo;
	private String descrModelo;
	
	
	//----------- TecModel -------------//
	private Double tempoDefasagemUsoTec;
	private Double consumoPorLoteMt;
	
	//----------- Parametros-------------//
	private Double percentualDePerda;
	
	
	public TecidoRelatorio(Tecido tecido, TecModel tecmodel, Modelo modelo, Parametros parametro){
		
		this.codTecido = tecido.getCodTecido();
		this.descrTecido = tecido.getDescrTecido();
		this.UM = tecido.getUM();
		this.custoUnit = tecido.getCustoUnit();
		this.leadTimeUnidade2 = tecido.getLeadTimeUnidade2();
		this.fatorDeRendimento = tecido.getFatorDeRendimento();
		this.producaoDiariaMaxUnidade2 = tecido.getProducaoDiariaMaxUnidade2();
	
	if(tecmodel != null){
		
		this.codModelo = modelo.getCodModelo();
		this.descrModelo = modelo.getDescrModelo();
		this.tempoDefasagemUsoTec = tecmodel.getTempoDefasagemUsoTec();
		this.consumoPorLoteMt = tecmodel.getConsumoPorLoteMt();
        
	} else {
		
		this.codModelo = null;
		this.descrModelo = null;
		this.tempoDefasagemUsoTec = null;
		this.consumoPorLoteMt = null;
		
	}
	
	if(parametro!=null)
		this.percentualDePerda = parametro.getPercentualDePerda();
	else{
		this.percentualDePerda = null;
	}

	
  }
	
	//----------------- metodos get e set -----------------------//

	public String getCodTecido() {
		return codTecido;
	}


	public void setCodTecido(String codTecido) {
		this.codTecido = codTecido;
	}


	public String getDescrTecido() {
		return descrTecido;
	}


	public void setDescrTecido(String descrTecido) {
		this.descrTecido = descrTecido;
	}


	public String getUM() {
		return UM;
	}


	public void setUM(String uM) {
		UM = uM;
	}


	public Double getCustoUnit() {
		return custoUnit;
	}


	public void setCustoUnit(Double custoUnit) {
		this.custoUnit = custoUnit;
	}


	public Double getLeadTimeUnidade2() {
		return leadTimeUnidade2;
	}


	public void setLeadTimeUnidade2(Double leadTimeUnidade2) {
		this.leadTimeUnidade2 = leadTimeUnidade2;
	}


	public Double getFatorDeRendimento() {
		return fatorDeRendimento;
	}


	public void setFatorDeRendimento(Double fatorDeRendimento) {
		this.fatorDeRendimento = fatorDeRendimento;
	}


	public Double getProducaoDiariaMaxUnidade2() {
		return producaoDiariaMaxUnidade2;
	}


	public void setProducaoDiariaMaxUnidade2(Double producaoDiariaMaxUnidade2) {
		this.producaoDiariaMaxUnidade2 = producaoDiariaMaxUnidade2;
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


	public Double getTempoDefasagemUsoTec() {
		return tempoDefasagemUsoTec;
	}


	public void setTempoDefasagemUsoTec(Double tempoDefasagemUsoTec) {
		this.tempoDefasagemUsoTec = tempoDefasagemUsoTec;
	}


	public Double getConsumoPorLoteMt() {
		return consumoPorLoteMt;
	}


	public void setConsumoPorLoteMt(Double consumoPorLoteMt) {
		this.consumoPorLoteMt = consumoPorLoteMt;
	}

	public Double getPercentualDePerda() {
		return percentualDePerda;
	}

	public void setPercentualDePerda(Double percentualDePerda) {
		this.percentualDePerda = percentualDePerda;
	}
	
}
