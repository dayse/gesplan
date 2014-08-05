 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import modelo.DeModPer;
import modelo.PerioPM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.DeModPerAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.PerioPMDAO;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

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
 * Prepara os periodos para as alteracoes; 
 * Altera as demandas do ultimo periodo para ficarem iguais das demandas do periodo anterior
 * 
 * @author felipe.arruda
 *
 */
public class CargaEstudoAlteraDemandaDePeriodo extends CargaBase{

	private static PerioPMDAO perioPMDAO;
	private static DeModPerAppService deModPerService;
	private PerioPM perioPM;
	private PerioPM perioPMAnterior;
	
	
	  public CargaEstudoAlteraDemandaDePeriodo() {
		 try {
			 perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			 deModPerService = FabricaDeAppService.getAppService(DeModPerAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			

		}


	/**
	 * Prepara os periodos para as alteracoes; 
	 * Altera as demandas do ultimo periodo para ficarem iguais das demandas do periodo anterior
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.preparaPeriodos();
		this.alterarDemandasUltimoPeriodo();
		return true;
	}
	
    public void preparaPeriodos() throws AplicacaoException{
    	try{
    		
		//pega o ultimo periodo da lista de periopms
		int periodoPMFinal = perioPMDAO.recuperaListaDePerioPMs().size();
		perioPM = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoPMFinal);
		
		//recupera o ultimo periodo junto com os demodpers.
		perioPM = perioPMDAO.recuperaPerioPMComDeModPers(perioPM);
		
		//recupera informacoes sobre o periodo anterior
		perioPMAnterior = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoPMFinal-1);
		perioPMAnterior = perioPMDAO.recuperaPerioPMComDeModPers(perioPMAnterior);
    	}catch (ObjetoNaoEncontradoException e) {
    		throw new AplicacaoException(e);
		}
    }
	/**
	 * Altera as demandas do ultimo periodo para ficarem iguais das demandas do periodo anterior
	 */
	public void alterarDemandasUltimoPeriodo(){
		//percorre a lista de demod
		for(int i=0;i<perioPM.getDeModPers().size();i++){
			DeModPer atual = perioPM.getDeModPers().get(i);
			DeModPer anterior = perioPMAnterior.getDeModPers().get(i);

			//coloca os pedidos do modelo atual como sendo do mesmo modelo, mas do periodo anterior.
			atual.setPedidosModelo(anterior.getPedidosModelo());
			
			//coloca os vendas do modelo atual como sendo do mesmo modelo, mas do periodo anterior.
			atual.setVendasProjetadasModelo(anterior.getVendasProjetadasModelo());
			
			deModPerService.altera(atual);
		}
	}
		
	

	}

