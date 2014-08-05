 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries(
	{
	
	@NamedQuery(name="HP.recuperaListaDeHP",
			query = "select hp from HP hp "
	)
  }
)


@Entity
@Table(name = "HP")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_HP", allocationSize = 1)
public class HP implements Serializable {

	private static final long serialVersionUID = 1L;

	public HP() {
	}

	private Long id;

	private PerioPM perioPMInicPMP;
	
	private PerioPM perioPMFinalPMP;
	
	private PerioPM perioPMInicDemMod;
	
	private PerioPM perioPMFinalDemMod;
	
	private PerioPAP perioPAPInicPAP;
	
	private PerioPAP perioPAPFinalPAP;
	
	private PerioPAP perioPAPInicDemFam;
	
	private PerioPAP perioPAPFinalDemFam;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPM_ID1")
	public PerioPM getPerioPMInicPMP() {
		return perioPMInicPMP;
	}

	public void setPerioPMInicPMP(PerioPM periodoInicPMP) {
		this.perioPMInicPMP = periodoInicPMP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPM_ID2")
	public PerioPM getPerioPMFinalPMP() {
		return perioPMFinalPMP;
	}

	public void setPerioPMFinalPMP(PerioPM periodoFinalPMP) {
		this.perioPMFinalPMP = periodoFinalPMP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPM_ID3")
	public PerioPM getPerioPMInicDemMod() {
		return perioPMInicDemMod;
	}

	public void setPerioPMInicDemMod(PerioPM periodoInicDemMod) {
		this.perioPMInicDemMod = periodoInicDemMod;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPM_ID4")
	public PerioPM getPerioPMFinalDemMod() {
		return perioPMFinalDemMod;
	}

	public void setPerioPMFinalDemMod(PerioPM periodoFinalDemMod) {
		this.perioPMFinalDemMod = periodoFinalDemMod;
	}
	
	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAP_ID1")
	public PerioPAP getPerioPAPInicPAP() {
		return perioPAPInicPAP;
	}

	public void setPerioPAPInicPAP(PerioPAP periodoInicPAP) {
		this.perioPAPInicPAP = periodoInicPAP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAP_ID2")
	public PerioPAP getPerioPAPFinalPAP() {
		return perioPAPFinalPAP;
	}

	public void setPerioPAPFinalPAP(PerioPAP periodoFinalPAP) {
		this.perioPAPFinalPAP = periodoFinalPAP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAP_ID3")
	public PerioPAP getPerioPAPInicDemFam() {
		return perioPAPInicDemFam;
	}

	public void setPerioPAPInicDemFam(PerioPAP periodoInicDemFam) {
		this.perioPAPInicDemFam = periodoInicDemFam;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAP_ID4")
	public PerioPAP getPerioPAPFinalDemFam() {
		return perioPAPFinalDemFam;
	}

	public void setPerioPAPFinalDemFam(PerioPAP periodoFinalDemFam) {
		this.perioPAPFinalDemFam = periodoFinalDemFam;
	}
}
