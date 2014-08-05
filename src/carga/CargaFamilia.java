 
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
import service.FamiliaAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;

/**
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
 * Inicializa os parametros com valores default.
 * 
 * @author felipe.arruda
 *
 */
public class CargaFamilia extends CargaBase {
 
	private static FamiliaAppService familiaService;
	
	
	 public CargaFamilia(){
		
		try {
			
			familiaService = FabricaDeAppService.getAppService(FamiliaAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

		
	/**
	 * Executa a inclusao de familias
	 */
	@Override
	public boolean executar() throws AplicacaoException {
		this.incluirFamilias();
		return true;
	}
	
	public void incluirFamilias() throws AplicacaoException{
		
				
		//-------------------- FAMILIA 1 ---------------------//
		Familia familia1 = new Familia();
		familia1.setCodFamilia("01");
		familia1.setDescrFamilia("soutien");
		familia1.setCobertura(60);
		familia1.setTmuc(8.1);
		familia1.setEstqInicFam(10800);

		//-------------------- FAMILIA 2 ---------------------//
		Familia familia2 = new Familia();
		familia2.setCodFamilia("02");
		familia2.setDescrFamilia("cal�a/biquini");
		familia2.setCobertura(65);
		familia2.setTmuc(5.5);
		familia2.setEstqInicFam(7600);

		//-------------------- FAMILIA 3 ---------------------//
		Familia familia3 = new Familia();
		familia3.setCodFamilia("03");
		familia3.setDescrFamilia("infantil");
		familia3.setCobertura(75);
		familia3.setTmuc(3.8);
		familia3.setEstqInicFam(4590);
		
		// -------------- LISTA DE FAMILIAS -----------------// 
		List<Familia> familias = new ArrayList<Familia>();
	
		familias.add(familia1);
		familias.add(familia2);
		familias.add(familia3);
		
		//-------------- INCLUSAO DE FAMILIAS ---------------//
		for (Familia familia : familias) {
			
			familiaService.inclui(familia);
			
		}
		
		
	}
	
	
	

}
