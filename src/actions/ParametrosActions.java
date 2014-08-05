 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import modelo.Parametros;

import service.ParametrosAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.SelectOneDataModel;

/**
 * ATENCAO: Esta entidade nao tem tela propria.
 * Os outros Actions é que utilizariam o service dessa entidade.
 * */
public class ParametrosActions extends BaseActions implements Serializable {

	// Services
	private static ParametrosAppService parametrosService;
	
	// Paginas
	public final String PAGINA_MARGEM_SEG = "editMargemSeg";
	public final String PAGINA_PERCENT_PERDA  = "editPercentPerda";
	public final String PAGINA_INT_FIXO = "editIntFixo";
	public final String PAGINA_INIC_PLAN = "editInicPlan";
	
	// Variaveis de Tela
	private Parametros parametrosCorrente;
	
	
	public ParametrosActions() throws Exception {
		
		try {
			parametrosService = FabricaDeAppService.getAppService(ParametrosAppService.class);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	//É preciso ter um metodo de edicao e cancelamento pra cada parametro
	//pois cada um retorna para uma pagina diferente
	
	//alteracao da Margem de Seguranca
	public String preparaAlteracaoMargemSeg(){
		
		return PAGINA_MARGEM_SEG;
	}
	//Prepara a alteracao do Percentual de Perda
	public String preparaAlteracaoPercentPerda(){
		
		return PAGINA_PERCENT_PERDA;
	}
	//Prepara a alteracao do Numero de Intervalos Fixos
	public String preparaAlteracaoIntFixo(){
		
		return PAGINA_INT_FIXO;
	}

	
	//alteracao da Margem de Seguranca
	//retorna para a tela capacidade de producao diaria na matriz
	public String alteraMargemSeg() {		
		
		parametrosService.altera(parametrosCorrente);
		
		info("parametros.SUCESSO_ALTERACAO");
		
		return "listCapacDia";
	}

	//alteracao da Percentual de Perda
	//retorna para a tela list de Tecido
	public String alteraPercentPerda() {	
		
		
			
			parametrosService.altera(parametrosCorrente);
		
		
		
		info("parametros.SUCESSO_ALTERACAO");
		

		return "listTecido";
	}

	//alteracao da Percentual de Perda
	//retorna para a tela list de HP
	public String alteraIntFixo() {		
		
		parametrosService.altera(parametrosCorrente);
		
		info("parametros.SUCESSO_ALTERACAO");
		

		return "listHP";
	}

	//retorna para a tela capacidade de producao diaria na matriz
	public String cancelaMargemSeg() {		
		return "listCapacDia";
	}
	//retorna para a tela list de Tecido
	public String cancelaPercentPerda() {		
		return "listTecido";
	}
	//retorna para a tela list de HP
	public String cancelaIntFixo() {		
		return "listHP";
	}
	
	// ================================== Métodos get() e set() ================================== //


	public Parametros getParametrosCorrente() {
		if(parametrosCorrente == null)
		{
			parametrosCorrente = parametrosService.recuperaListaDeParametros().get(0);
		}
		return parametrosCorrente;
	}

	public void setParametrosCorrente(Parametros parametrosCorrente) {
		this.parametrosCorrente = parametrosCorrente;
	}
}
