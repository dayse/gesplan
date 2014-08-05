 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package carga;

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
 * É uma Carga do sistema, portanto deve herdar de CargaBase e
 * implementar o método executar().
 * Nesse método executar é que é chamado os outros métodos que são
 * as etápas dessa carga.
 * Portanto se é necessario rodar um método depois do outro, eles devem ser chamados
 * na ordem correta. Ex:
 * incluiHP() vem antes de inicializaHP(), portanto no método executar() eles devem ser chamados nessa ordem.
 * 
 * Terminado de executar todas as etapas é preciso retornar true.
 * Se houver algum problema(exceção) na execução de um das etapas, essa exceção deve ser lancada
 * 
 * Essa Carga:
 * Inclui novo plano para o novo hp
 * para que represente os dados originais defasados.
 * 
 * @author felipe.arruda
 */
public class CargaEstudoIncluiPlanoComNovoHP extends CargaBase{
	
	private static CadPlanAppService cadPlanService;
	private static UsuarioAppService	usuarioService;
	
	  public CargaEstudoIncluiPlanoComNovoHP(){
		
		 try {
			 cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
		}
		

		/**
		 * Inclui novo plano para o novo hp
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluiPlanoDefasado();
			return true;
		}
		
		/**
		 * Inclui novo plano para o novo hp
		 * para que represente os dados originais defasados.
		 * 
		 * @throws AplicacaoException 
		 * 
		 */		
		public void incluiPlanoDefasado() throws AplicacaoException{

			CargaUsuario cargaUsuario = new CargaUsuario();
			List<Usuario> usuarios = cargaUsuario.recuperaUsuariosDeArquivoConfigJson();
			Usuario usuarioAdmin = usuarios.get(0);
			
			CadPlan cadPlanDefasado = new CadPlan();			
			cadPlanDefasado.setCodPlan("1");
			cadPlanDefasado.setDescrPlan("dados defasados 1 periodo");
			cadPlanDefasado.setUsuario(usuarioService.recuperaPorLoginESenha(usuarioAdmin.getLogin(),usuarioAdmin.getSenha()));

			cadPlanService.inclui(cadPlanDefasado);
		}
		
		

	}
