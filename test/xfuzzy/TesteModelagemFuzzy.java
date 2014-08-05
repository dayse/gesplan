package xfuzzy;

import java.io.File;
import java.util.List;

import modelo.ModelagemFuzzy;
import modelo.Modelo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import DAO.CadPlanDAO;
import DAO.ModelagemFuzzyDAO;
import DAO.Impl.CadPlanDAOImpl;
import DAO.Impl.ModelagemFuzzyDAOImpl;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;


import service.ModeloAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * Classe para testes dos metodos necessários ao funcionamento da biblioteca XFuzzy
 * @author dayse.arruda
 *
 */
public class TesteModelagemFuzzy {

	private static ModelagemFuzzyDAO modelagemFuzzyDAO;
	private static CadPlanDAO cadPlanDAO;

	@BeforeClass
	public void setupClass(){
		try {
			System.out.println("-----------------------------> Startando a JPA...");
			JPAUtil.JPAstartUp();
			System.out.println("-----------------------------> JPA startada com sucesso!");
			
			modelagemFuzzyDAO = FabricaDeDao.getDao(ModelagemFuzzyDAOImpl.class);
			cadPlanDAO = FabricaDeDao.getDao(CadPlanDAOImpl.class);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	

	@Test
	public void testeModelagemFuzzyDAO() {
		ModelagemFuzzy modelagemFuzzy = new ModelagemFuzzy();
		modelagemFuzzy.setNomeModelagemFuzzy("teste");
		System.out.println("aqui");
		try {
			modelagemFuzzyDAO.recuperaModelagemFuzzyPeloNome(modelagemFuzzy.getNomeModelagemFuzzy());
			//throw new AplicacaoException("modelagem.NOME_MODELAGEM_EXISTENTE");
		} catch (ObjetoNaoEncontradoException e) {	
			e.printStackTrace();
				//retorno = inclui(modelagemFuzzy);
				//salvarArquivoModelagemFuzzy(item);
		}		

	}
	
}
