 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class RecursoRelatorio {
	
	//-------------- Recurso -----------------//
	
	private String codRecurso;
	private String descrRecurso;
	private String UM;
	private Double custoUnit;
	
	//-------------- Modelo ------------------//
	
	private String codModelo;
	private String descrModelo;
	
	//------------- RecModel -----------------//
	
	private Double consumoUnit;
	
	
	
	public RecursoRelatorio(Recurso recurso, RecModel recmodel, Modelo modelo){
		
		this.codRecurso = recurso.getCodRecurso();
		this.descrRecurso = recurso.getDescrRecurso();
		this.UM = recurso.getUM();
		this.custoUnit = recurso.getCustoUnit();
		
		if(recmodel != null){
			
			this.codModelo = modelo.getCodModelo();
			this.descrModelo = modelo.getDescrModelo();
			this.consumoUnit = recmodel.getConsumoUnit();
			
		} else {
			
			this.codModelo = null;
			this.descrModelo = null;
			this.consumoUnit = null;
			
		}
		
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

	public Double getConsumoUnit() {
		return consumoUnit;
	}

	public void setConsumoUnit(Double consumoUnit) {
		this.consumoUnit = consumoUnit;
	}
	

}
