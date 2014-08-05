 
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
	
	@NamedQuery(name="HPVig.recuperaListaDeHPVig",
			query = "select hp from HPVig hp "
	)
  }
)


@Entity
@Table(name = "HPVIG")
@SequenceGenerator(name = "SEQUENCIA", sequenceName = "SEQ_HPVIG", allocationSize = 1)
public class HPVig implements Serializable {

	private static final long serialVersionUID = 1L;

	public HPVig() {
	}

	private Long id;

	private PerioPMVig perioPMInicPMP;
	
	private PerioPMVig perioPMFinalPMP;
	
	private PerioPMVig perioPMInicDemMod;
	
	private PerioPMVig perioPMFinalDemMod;
	
	private PerioPAPVig perioPAPInicPAP;
	
	private PerioPAPVig perioPAPFinalPAP;
	
	private PerioPAPVig perioPAPInicDemFam;
	
	private PerioPAPVig perioPAPFinalDemFam;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCIA")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPMVIG_ID1")
	public PerioPMVig getPerioPMInicPMP() {
		return perioPMInicPMP;
	}

	public void setPerioPMInicPMP(PerioPMVig perioPMInicPMP) {
		this.perioPMInicPMP = perioPMInicPMP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPMVIG_ID2")
	public PerioPMVig getPerioPMFinalPMP() {
		return perioPMFinalPMP;
	}

	public void setPerioPMFinalPMP(PerioPMVig perioPMFinalPMP) {
		this.perioPMFinalPMP = perioPMFinalPMP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPMVIG_ID3")
	public PerioPMVig getPerioPMInicDemMod() {
		return perioPMInicDemMod;
	}

	public void setPerioPMInicDemMod(PerioPMVig perioPMInicDemMod) {
		this.perioPMInicDemMod = perioPMInicDemMod;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPMVIG_ID4")
	public PerioPMVig getPerioPMFinalDemMod() {
		return perioPMFinalDemMod;
	}

	public void setPerioPMFinalDemMod(PerioPMVig perioPMFinalDemMod) {
		this.perioPMFinalDemMod = perioPMFinalDemMod;
	}
	
	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAPVIG_ID1")
	public PerioPAPVig getPerioPAPInicPAP() {
		return perioPAPInicPAP;
	}

	public void setPerioPAPInicPAP(PerioPAPVig perioPAPInicPAP) {
		this.perioPAPInicPAP = perioPAPInicPAP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAPVIG_ID2")
	public PerioPAPVig getPerioPAPFinalPAP() {
		return perioPAPFinalPAP;
	}

	public void setPerioPAPFinalPAP(PerioPAPVig perioPAPFinalPAP) {
		this.perioPAPFinalPAP = perioPAPFinalPAP;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAPVIG_ID3")
	public PerioPAPVig getPerioPAPInicDemFam() {
		return perioPAPInicDemFam;
	}

	public void setPerioPAPInicDemFam(PerioPAPVig perioPAPInicDemFam) {
		this.perioPAPInicDemFam = perioPAPInicDemFam;
	}

	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="PERIOPAPVIG_ID4")
	public PerioPAPVig getPerioPAPFinalDemFam() {
		return perioPAPFinalDemFam;
	}

	public void setPerioPAPFinalDemFam(PerioPAPVig perioPAPFinalDemFam) {
		this.perioPAPFinalDemFam = perioPAPFinalDemFam;
	}
}
