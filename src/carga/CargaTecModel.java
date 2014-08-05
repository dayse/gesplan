 
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

import modelo.Familia;
import modelo.Modelo;
import modelo.TecModel;
import modelo.Tecido;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.ModeloAppService;
import service.TecModelAppService;
import service.TecidoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

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
 * Teste de carga que cria tecmodels com base nos modelos e tecidos
 * usando o seguinte raciocinio:
 * PCS / MT:
 *  Calcinhas - 3
 *  Infantil - 5
 *  Sutian - 4
 * 
 * Defasagem : TR do modelo 
 * 
 * @author felipe.arruda
 *
 */
public class CargaTecModel extends CargaBase{

	private static TecidoAppService tecidoService;
	private static ModeloAppService modeloService;
	private static TecModelAppService tecModelService;

	public CargaTecModel() {

		try {

			tecidoService = FabricaDeAppService
					.getAppService(TecidoAppService.class);
			modeloService = FabricaDeAppService
					.getAppService(ModeloAppService.class);
			tecModelService = FabricaDeAppService
					.getAppService(TecModelAppService.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executa a inclusao de tecmodels.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirTecModelsCoerentes();
		return true;
	}
	

	/**
	 * Teste de carga que cria tecmodels com base nos modelos e tecidos
	 * usando o seguinte raciocinio:
	 * PCS / MT:
	 *  Calcinhas - 3
	 *  Infantil - 5
	 *  Sutian - 4
	 * 
	 * Defasagem : TR do modelo 
	 */
	public void incluirTecModelsCoerentes() throws AplicacaoException {
		List<Modelo> listaModelos = modeloService
				.recuperaListaDeModelosComFamilias();
		List<Tecido> listaTecidos = tecidoService.recuperaListaDeTecidos();

		// percorre a lista de modelos e vai adicionando para cada um deles um
		// tecmodel correspondente.
		for (int i = 0; i < listaModelos.size(); i++) {
			// Modelo corrente é o modelo da iteracao atual
			Modelo modeloCorrente = listaModelos.get(i);

			/*
			 * tecido vai variando e repetindo sempre que chegar no ultimo, ex:
			 * (14 modelos e 4 tecidos):
			 *  modelo 0 - tecido 0
			 *  modelo 1 - tecido 1
			 *  modelo 2 - tecido 2
			 *  modelo 3 - tecido 3
			 *  modelo 4 - tecido 0 
			 *  modelo 5 - tecido 1 
			 *  ....
			 *  modelo 12 - tecido 0 
			 *  modelo 13 - tecido 1 
			 */
			int numDoTecido = (i % listaTecidos.size());
			
			Tecido tecidoCorrente = listaTecidos.get(numDoTecido);
			
			//cria um tecmodel usando o tecidocorrente e o modelo corrente
			TecModel tecModel = gerarTecModel(tecidoCorrente,modeloCorrente);
			// inclui o tecmodel corrente
			tecModelService.inclui(tecModel);
		}

		// para finalizar inclui outros 2 tecmodels que sao correspondentes a 2
		// modelos onde cada um usam 2 tecidos.
		
		//tecmodel usando o primeiro modelo da lista de modelos e um tecido diferente do que foi usado anteriormente
		Modelo modeloComMaisUmTecido1 = listaModelos.get(0);
		Tecido tecidoMaisUm1 = listaTecidos.get((0+1) % listaTecidos.size()); 
		TecModel tecModelMaisUm1 = gerarTecModel(tecidoMaisUm1,modeloComMaisUmTecido1);

		tecModelService.inclui(tecModelMaisUm1);
		
		
		//tecmodel usando o modelo do meio da lista de modelos e um tecido diferente do que foi usado anteriormente
		int numModeloMeioDaLista = (listaModelos.size() / 2);
		Modelo modeloComMaisUmTecido2 = listaModelos.get((listaModelos.size() / 2));		
		Tecido tecidoMaisUm2 = listaTecidos.get((numModeloMeioDaLista+1) % listaTecidos.size()); 
		TecModel tecModelMaisUm2 = gerarTecModel(tecidoMaisUm2,modeloComMaisUmTecido2);

		tecModelService.inclui(tecModelMaisUm2);

	}
	
	/**
	 * retorna um tecmodel usando os dados de tecido e modelo passados como parametro.
	 * os dados do tecmodel foram calculados a partir dos parametros de entrada, para ficarem coerentes.
	 * 
	 * @param tecido
	 * @param modelo
	 * @return
	 */
	public TecModel gerarTecModel(Tecido tecido, Modelo modelo){
		TecModel tecModel = new TecModel();
		
		// o modelo do tecmodel é o modelo
		tecModel.setModelo(modelo);
		// o tecido do tecmodel é o tecido
		tecModel.setTecido(tecido);

		// tempo de defasagem do tecido é igual a do modelo.
		tecModel.setTempoDefasagemUsoTec(modelo.getTr());

		// numero de pecs por metro do modelo atual
		int numPecasPorMt = numPecasPorMtPorFamilia(modelo.getFamilia());

		Double consumoLoteMt = modelo.getTamLote() * (1.0 / (double)numPecasPorMt);
		tecModel.setConsumoPorLoteMt(consumoLoteMt);

		return tecModel;
	}
	
	/**
	 * Retorna o numero de pecas por mt por familia
	 * isso é cada familia tem um numero de pecas por metro.
	 * 
	 * @param familia
	 * @return
	 */
	public int numPecasPorMtPorFamilia(Familia familia) {
		String descFam = familia.getDescrFamilia();

		// se for calcinha entao o numero de pcs por metro é de 3
		if (descFam.equals("calça/biquini")) {
			return 2;
		}
		// se for sutian entao o numero de pcs por metro é de 4
		else if (descFam.equals("soutien")) {
			return 3;
		}
		// se for infantil entao o numero de pcs por metro é de 5
		else if (descFam.equals("infantil")) {
			return 4;
		}
		// se não for nenhum deles retorna 1
		return 1;
	}
}
