 
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
import modelo.ModelagemFuzzy;
import modelo.Usuario;
import service.CadPlanAppService;
import service.UsuarioAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import DAO.ModelagemFuzzyDAO;
import DAO.Impl.ModelagemFuzzyDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;

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
 * Inclui novo plano que utiliza modelagem fuzzy.
 * Vai ser utilizado como modelagem a modelagem fuzzy
 * incluida anteriormente:"producao.xfl"
 * 
 * @author felipe.arruda
 */
public class CargaEstudoIncluiPlanoComFuzzy extends CargaBase{

	private static CadPlanAppService cadPlanService;
	private static ModelagemFuzzyDAO modelagemFuzzyDAO;
	private static UsuarioAppService	usuarioService;
	
	  public CargaEstudoIncluiPlanoComFuzzy(){
		
		 try {
			 cadPlanService = FabricaDeAppService.getAppService(CadPlanAppService.class);
			modelagemFuzzyDAO = FabricaDeDao.getDao(ModelagemFuzzyDAOImpl.class);
			usuarioService = FabricaDeAppService.getAppService(UsuarioAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
		}
		

		/**
		 * Inclui novo plano para o novo hp
		 * para que represente os dados originais defasados.
		 */
		@Override
		public boolean executar() throws AplicacaoException {
			this.incluiPlanoFuzzy();
			return true;
		}
		
		
		/**
		 * Inclui novo plano para o novo hp usando modelagem fuzzy
		 * 
		 * @throws AplicacaoException 
		 * 
		 */		
		public void incluiPlanoFuzzy() throws AplicacaoException{
			ModelagemFuzzy modelagem;
			try {
				modelagem = modelagemFuzzyDAO.recuperaModelagemFuzzyPeloNome("producao");
			} catch (ObjetoNaoEncontradoException e) {
				throw new AplicacaoException(e);
			}

			CargaUsuario cargaUsuario = new CargaUsuario();
			List<Usuario> usuarios = cargaUsuario.recuperaUsuariosDeArquivoConfigJson();
			Usuario usuarioAdmin = usuarios.get(0);
			
			CadPlan cadPlanFuzzy = new CadPlan();			
			cadPlanFuzzy.setCodPlan("2");
			cadPlanFuzzy.setDescrPlan("inclui fuzzy producao.xfl defasado");
			cadPlanFuzzy.setUsuario(usuarioService.recuperaPorLoginESenha(usuarioAdmin.getLogin(),usuarioAdmin.getSenha()));
			cadPlanFuzzy.setModelagemFuzzy(modelagem);

			cadPlanService.incluiPMPFuzzy(cadPlanFuzzy);
		}

		

	}
