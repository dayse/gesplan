 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

//import java.util.ArrayList;
//import java.util.List;

import java.util.List;

import modelo.HP;
import modelo.PerioPAP;
import modelo.PerioPM;
import service.HPAppService;
import service.PerioPAPAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;



/**
 * 
 * Sobre a Carga:
 * � uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o m�todo executar().
 * Nesse m�todo executar � que � chamado os outros m�todos que s�o
 * as et�pas dessa carga.
 * Portanto se � necessario rodar um m�todo depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no m�todo executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas � preciso retornar true.
 * Se houver algum problema(exce��o) na execu��o de um das etapas, essa exce��o deve ser lancada
 * 
 * Essa Carga:
 * Define um novo HP, visto que o periodo foi defasado.
 * recupera os primeiros e os outimos perioPMs e perioPAPs
 *  e define o novo hp para eles.
 *  Depois inicializa esse hp
 * 
 * @author felipe.arruda e bruno.oliveira
 *
 */
public class CargaEstudoDefineNovoHP extends CargaBase{
	
	private static PerioPAPAppService perioPAPService;
	private static HPAppService hpService;
	
	private PerioPM perioPMInicial;
	private PerioPM perioPMFinal;
	private PerioPAP perioPAPInicial;
	private PerioPAP perioPAPFinal;
	private HP hp = new HP();
	
	  public CargaEstudoDefineNovoHP(){
		
		 try {
			 perioPAPService = FabricaDeAppService.getAppService(PerioPAPAppService.class);
			 hpService = FabricaDeAppService.getAppService(HPAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
			List<HP> lista = hpService.recuperaListaDeHP();
			if (!lista.isEmpty()) {
				hp = lista.get(0);
			}
		}
	  

	/**
	 * recupera os primeiros e os outimos perioPMs e perioPAPs
	 *  e define o novo hp para eles.
	 *  Depois inicializa esse hp
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.preparaPeriodos();
		this.defineNovoHP();
		this.inicializaPlanejamento();
		return true;
	}
	
	/**
	 * 
	 * recupera os primeiros e os outimos perioPMs e perioPAPs
	 * @throws AplicacaoException
	 */
	public void preparaPeriodos() throws AplicacaoException {
		int periodoPAPFinal = perioPAPService.recuperaListaDePerioPAPs().size();
		//recupera os paps e depois os pms deles
		perioPAPInicial = perioPAPService.recuperaPerioPAPPorPeriodoPAP(1);
		perioPAPFinal = perioPAPService.recuperaPerioPAPPorPeriodoPAP(periodoPAPFinal);
		
		perioPMInicial = perioPAPService.obtemPrimeiroPerioPMdoPerioPAP(perioPAPInicial);
		perioPMFinal = perioPAPService.obtemUltimoPerioPMdoPerioPAP(perioPAPFinal);
	}
	
	/**
	 * define um novo hp para os peridos recuperados
	 * @throws AplicacaoException
	 */
	public void defineNovoHP() throws AplicacaoException {
		hp.setPerioPMInicPMP(perioPMInicial);
		hp.setPerioPMFinalPMP(perioPMFinal);
		hp.setPerioPMInicDemMod(perioPMInicial);
		hp.setPerioPMFinalDemMod(perioPMFinal);
		
		hp.setPerioPAPInicPAP(perioPAPInicial);
		hp.setPerioPAPFinalPAP(perioPAPFinal);
		hp.setPerioPAPInicDemFam(perioPAPInicial);
		hp.setPerioPAPFinalDemFam(perioPAPFinal);
			if (hp.getId() == null) {
				hpService.inclui(hp);
			} else {
				hpService.altera(hp);
			}
	}
	
	/**
	 * inicializa o planejamento
	 * @throws AplicacaoException
	 */
	public void inicializaPlanejamento() throws AplicacaoException{

		hpService.iniciaPlanejamento();
	}
	
}
