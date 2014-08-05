 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

import java.util.ArrayList;
import java.util.List;

import modelo.CadPlan;
import modelo.Usuario;
import service.CadPlanAppService;
import service.UsuarioAppService;
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

 * Classe respons�vel pela carga do arquivo cadplan e seus descendentes PlanoModelo e PlPerMod (em cascata)
 * vers�o atual incluindo apenas 2 planos (plano 1 que vai representar os dados originais da planilha da empresa 
 * e o plano 9 que corresponde a inclus�o usando o algoritmo de conserva��o de estoques ou seja, o pr�prio algoritmo
 * usado na inclus�o de plano
 * 
 * @author felipe.arruda
 *
 */
public class CargaCadPlan extends CargaBase{
 
	private static CadPlanAppService cadPlanService;
	private static UsuarioAppService	usuarioService;
	
	
	public CargaCadPlan(){
		
		try {
			
			cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Executa a inclusao de CadPlans.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirCadPlans();
		return true;
	}
	
	public void incluirCadPlans() throws AplicacaoException{


		CargaUsuario cargaUsuario = new CargaUsuario();
		List<Usuario> usuarios = cargaUsuario.recuperaUsuariosDeArquivoConfigJson();
		Usuario usuarioAdmin = usuarios.get(0);
		
		
		//-------------------- CADPLAN 1 ---------------------//
		CadPlan cadPlan1 = new CadPlan();
		cadPlan1.setCodPlan("1");
		cadPlan1.setDescrPlan("dados originais");
		cadPlan1.setEscoreMin(0.0);
		cadPlan1.setEscoreMedio(0.0);
		cadPlan1.setVarEstqPer(0.0);
		cadPlan1.setVarProdDiaPer(0.0);
		cadPlan1.setRanking(0.0);
		cadPlan1.setUsuario(usuarioService.recuperaPorLoginESenha(usuarioAdmin.getLogin(),usuarioAdmin.getSenha()));

		//-------------------- CADPLAN 9 ---------------------//
		CadPlan cadPlan9 = new CadPlan();
		cadPlan9.setCodPlan("9");
		cadPlan9.setDescrPlan("exemplo");
		cadPlan9.setEscoreMin(0.0);
		cadPlan9.setEscoreMedio(0.0);
		cadPlan9.setVarEstqPer(0.0);
		cadPlan9.setVarProdDiaPer(0.0);
		cadPlan9.setRanking(0.0);
		cadPlan9.setUsuario(usuarioService.recuperaPorLoginESenha(usuarioAdmin.getLogin(),usuarioAdmin.getSenha()));
		
		
		//-------------------- LISTA DE CADPLANS ---------------------//
		List<CadPlan> cadPlans = new ArrayList<CadPlan>();
		
		cadPlans.add(cadPlan1);

		cadPlans.add(cadPlan9);
		
		//-------------- INCLUSAO DE CADPLANS ---------------//
		for (CadPlan cadplan : cadPlans) {
			
			cadPlanService.inclui(cadplan);
			
		}
		
		
		
		
	}
		
	
	
	
	
	
	
	
}
