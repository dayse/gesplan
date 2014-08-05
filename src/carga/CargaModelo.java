 
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

import modelo.Modelo;
import service.FamiliaAppService;
import service.ModeloAppService;
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
 * Inclui modelos
 * 
 * @author felipe.arruda
 *
 */
public class CargaModelo extends CargaBase{
  
	private static ModeloAppService modeloService;
	private static FamiliaAppService familiaService;
	
	
	public CargaModelo(){
		
		try {
			
			modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Executa a inclusao de modelos.
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirModelos();
		return true;
	}
	
	
	public void incluirModelos() throws AplicacaoException{
		
		
		//-------------------- MODELO 1 ---------------------//
		Modelo modelo1 = new Modelo();
		modelo1.setCodModelo("121131");
		modelo1.setDescrModelo("cire bordado");
		modelo1.setFlagProducaoModel(true);
		modelo1.setIndiceCob(1);
		modelo1.setTuc(8.56);
		modelo1.setTamLote(48);
		modelo1.setTr(2);
		modelo1.setCobertura(60);
		modelo1.setEstqInicModel(1700);
		modelo1.setEstqEmFalta(0.0);
		modelo1.setRecebimentoPendente(0);
		modelo1.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("01"));

		//-------------------- MODELO 2 ---------------------//
		Modelo modelo2 = new Modelo();
		modelo2.setCodModelo("121451");
		modelo2.setDescrModelo("cetim bordado");
		modelo2.setFlagProducaoModel(true);
		modelo2.setIndiceCob(1);
		modelo2.setTuc(7.26);
		modelo2.setTamLote(48);
		modelo2.setTr(1);
		modelo2.setCobertura(60);
		modelo2.setEstqInicModel(1400);
		modelo2.setEstqEmFalta(0.0);
		modelo2.setRecebimentoPendente(0);
		modelo2.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("01"));

		//-------------------- MODELO 3 ---------------------//
		Modelo modelo3 = new Modelo();
		modelo3.setCodModelo("129508");
		modelo3.setDescrModelo("lyon lycra");
		modelo3.setFlagProducaoModel(true);
		modelo3.setIndiceCob(1);
		modelo3.setTuc(7.72);
		modelo3.setTamLote(48);
		modelo3.setTr(1);
		modelo3.setCobertura(65);
		modelo3.setEstqInicModel(2700);
		modelo3.setEstqEmFalta(0.0);
		modelo3.setRecebimentoPendente(0);
		modelo3.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("02"));

		//-------------------- MODELO 4 ---------------------//
		Modelo modelo4 = new Modelo();
		modelo4.setCodModelo("131121");
		modelo4.setDescrModelo("mondrian renda");
		modelo4.setFlagProducaoModel(true);
		modelo4.setIndiceCob(1);
		modelo4.setTuc(8.56);
		modelo4.setTamLote(48);
		modelo4.setTr(2);
		modelo4.setCobertura(60);
		modelo4.setEstqInicModel(3100);
		modelo4.setEstqEmFalta(0.0);
		modelo4.setRecebimentoPendente(0);
		modelo4.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("01"));
		
		//-------------------- MODELO 5 ---------------------//
		Modelo modelo5 = new Modelo();
		modelo5.setCodModelo("131216");
		modelo5.setDescrModelo("lycra lisa");
		modelo5.setFlagProducaoModel(true);
		modelo5.setIndiceCob(1);
		modelo5.setTuc(10.1);
		modelo5.setTamLote(48);
		modelo5.setTr(2);
		modelo5.setCobertura(60);
		modelo5.setEstqInicModel(2100);
		modelo5.setEstqEmFalta(0.0);
		modelo5.setRecebimentoPendente(0);
		modelo5.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("01"));

		//-------------------- MODELO 6 ---------------------//
		Modelo modelo6 = new Modelo();
		modelo6.setCodModelo("137206");
		modelo6.setDescrModelo("microfibra jap");
		modelo6.setFlagProducaoModel(true);
		modelo6.setIndiceCob(1);
		modelo6.setTuc(5.58);
		modelo6.setTamLote(48);
		modelo6.setTr(1);
		modelo6.setCobertura(65);
		modelo6.setEstqInicModel(700);
		modelo6.setEstqEmFalta(0.0);
		modelo6.setRecebimentoPendente(0);
		modelo6.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("02"));

		//-------------------- MODELO 7 ---------------------//
		Modelo modelo7 = new Modelo();
		modelo7.setCodModelo("149023");
		modelo7.setDescrModelo("gallatica renda");
		modelo7.setFlagProducaoModel(true);
		modelo7.setIndiceCob(1);
		modelo7.setTuc(3.74);
		modelo7.setTamLote(48);
		modelo7.setTr(1);
		modelo7.setCobertura(75);
		modelo7.setEstqInicModel(1190);
		modelo7.setEstqEmFalta(0.0);
		modelo7.setRecebimentoPendente(0);
		modelo7.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("03"));

		//-------------------- MODELO 8 ---------------------//
		Modelo modelo8 = new Modelo();
		modelo8.setCodModelo("149024");
		modelo8.setDescrModelo("savoy plisse");
		modelo8.setFlagProducaoModel(true);
		modelo8.setIndiceCob(1);
		modelo8.setTuc(5.3);
		modelo8.setTamLote(48);
		modelo8.setTr(1);
		modelo8.setCobertura(75);
		modelo8.setEstqInicModel(1300);
		modelo8.setEstqEmFalta(0.0);
		modelo8.setRecebimentoPendente(0);
		modelo8.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("03"));

		//-------------------- MODELO 9 ---------------------//
		Modelo modelo9 = new Modelo();
		modelo9.setCodModelo("154121");
		modelo9.setDescrModelo("cetim floral");
		modelo9.setFlagProducaoModel(true);
		modelo9.setIndiceCob(1);
		modelo9.setTuc(7.26);
		modelo9.setTamLote(48);
		modelo9.setTr(1);
		modelo9.setCobertura(60);
		modelo9.setEstqInicModel(1000);
		modelo9.setEstqEmFalta(0.0);
		modelo9.setRecebimentoPendente(0);
		modelo9.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("01"));

		//-------------------- MODELO 10 ---------------------//
		Modelo modelo10 = new Modelo();
		modelo10.setCodModelo("320941");
		modelo10.setDescrModelo("clear  gotícula");
		modelo10.setFlagProducaoModel(true);
		modelo10.setIndiceCob(1);
		modelo10.setTuc(3.74);
		modelo10.setTamLote(48);
		modelo10.setTr(1);
		modelo10.setCobertura(75);
		modelo10.setEstqInicModel(1100);
		modelo10.setEstqEmFalta(0.0);
		modelo10.setRecebimentoPendente(0);
		modelo10.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("03"));

		//-------------------- MODELO 11 ---------------------//
		Modelo modelo11 = new Modelo();
		modelo11.setCodModelo("420941");
		modelo11.setDescrModelo("savoy floral");
		modelo11.setFlagProducaoModel(true);
		modelo11.setIndiceCob(1);
		modelo11.setTuc(5.3);
		modelo11.setTamLote(48);
		modelo11.setTr(1);
		modelo11.setCobertura(75);
		modelo11.setEstqInicModel(1000);
		modelo11.setEstqEmFalta(0.0);
		modelo11.setRecebimentoPendente(0);
		modelo11.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("03"));

		//-------------------- MODELO 12 ---------------------//
		Modelo modelo12 = new Modelo();
		modelo12.setCodModelo("602731");
		modelo12.setDescrModelo("microfibra lisa");
		modelo12.setFlagProducaoModel(true);
		modelo12.setIndiceCob(1);
		modelo12.setTuc(5.58);
		modelo12.setTamLote(48);
		modelo12.setTr(1);
		modelo12.setCobertura(65);
		modelo12.setEstqInicModel(1500);
		modelo12.setEstqEmFalta(0.0);
		modelo12.setRecebimentoPendente(0);
		modelo12.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("02"));

		//-------------------- MODELO 13 ---------------------//
		Modelo modelo13 = new Modelo();
		modelo13.setCodModelo("612131");
		modelo13.setDescrModelo("lycra borboleta");
		modelo13.setFlagProducaoModel(true);
		modelo13.setIndiceCob(1);
		modelo13.setTuc(10.1);
		modelo13.setTamLote(48);
		modelo13.setTr(2);
		modelo13.setCobertura(60);
		modelo13.setEstqInicModel(1500);
		modelo13.setEstqEmFalta(0.0);
		modelo13.setRecebimentoPendente(0);
		modelo13.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("01"));

		//-------------------- MODELO 14 ---------------------//
		Modelo modelo14 = new Modelo();
		modelo14.setCodModelo("805921");
		modelo14.setDescrModelo("arco lycra");
		modelo14.setFlagProducaoModel(true);
		modelo14.setIndiceCob(1);
		modelo14.setTuc(7.72);
		modelo14.setTamLote(48);
		modelo14.setTr(1);
		modelo14.setCobertura(65);
		modelo14.setEstqInicModel(2700);
		modelo14.setEstqEmFalta(0.0);
		modelo14.setRecebimentoPendente(0);
		modelo14.setFamilia(familiaService.recuperaUmaFamiliaPeloCodigo("02"));
		
		// -------------- LISTA DE MODELOS -----------------// 
		List<Modelo> modelos = new ArrayList<Modelo>();
	
		modelos.add(modelo1);
		modelos.add(modelo2);
		modelos.add(modelo3);
		modelos.add(modelo4);
		modelos.add(modelo5);
		modelos.add(modelo6);
		modelos.add(modelo7);
		modelos.add(modelo8);
		modelos.add(modelo9);
		modelos.add(modelo10);
		modelos.add(modelo11);
		modelos.add(modelo12);
		modelos.add(modelo13);
		modelos.add(modelo14);
		
		//-------------- INCLUSAO DE FAMILIAS ---------------//
		for (Modelo modelo : modelos) {
			
			modeloService.inclui(modelo);
			
		}
		
		
	}
		
	
	
	
	
	
	
	
	
	
	
	
}
