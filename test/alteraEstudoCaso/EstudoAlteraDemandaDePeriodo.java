 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.PerioPMDAO;
import DAO.Impl.HPDAOImpl;
import DAO.Impl.PerioPMDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import modelo.DeModPer;
import modelo.PerioPM;

import service.PerioPMAppService;
import service.DeModPerAppService;
import service.controleTransacao.FabricaDeAppService;
import util.JPAUtil;

/**
 * 
 * @author felipe.arruda
 * 
 */
public class EstudoAlteraDemandaDePeriodo {

	private static PerioPMDAO perioPMDAO;
	private static DeModPerAppService deModPerService;
	private PerioPM perioPM;
	private PerioPM perioPMAnterior;
	@BeforeClass
	  public void setupClass() throws ObjetoNaoEncontradoException{
		 try {
			 perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
			 deModPerService = FabricaDeAppService.getAppService(DeModPerAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			//pega o ultimo periodo da lista de periopms
			int periodoPMFinal = perioPMDAO.recuperaListaDePerioPMs().size();
			perioPM = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoPMFinal);
			
			//recupera o ultimo periodo junto com os demodpers.
			perioPM = perioPMDAO.recuperaPerioPMComDeModPers(perioPM);
			
			//recupera informacoes sobre o periodo anterior
			perioPMAnterior = perioPMDAO.recuperaPerioPMPorPeriodoPM(periodoPMFinal-1);
			perioPMAnterior = perioPMDAO.recuperaPerioPMComDeModPers(perioPMAnterior);
			
			
		}
	/**
	 * Altera as demandas do ultimo periodo para ficarem iguais das demandas do periodo anterior
	 */
	@Test
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
		System.out.println(">>>>alterarDemandasUltimoPeriodo");
	}
		
	

	}

