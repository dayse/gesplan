 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package alteraEstudoCaso;

import java.util.List;

import modelo.Modelo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.ModeloAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

/**
 * 
 * @author felipe.arruda
 * 
 */
public class EstudoAtualizaEstoque {
	
	private static ModeloAppService modeloService;
	private List <Modelo> modelos;
	@BeforeClass
	  public void setupClass() throws AplicacaoException{
		 try {
			 modeloService = FabricaDeAppService.getAppService(ModeloAppService.class);
			 
			} catch (Exception e) {	
				e.printStackTrace();
			}
			modelos = modeloService.recuperaListaDeModelosComFamiliasEPeriodos();
			
		}
	
	/**
	 * Atualiza o estoque e o recebimento pendente de todos os modelos
	 */
	@Test

	/**
	 * Atualiza o estoque e o recebimento pendente de todos os modelos
	 */
	public void atualizaEstoqueERecPendente() throws AplicacaoException{
		Modelo modelo =null;
		Double estq = 0.0;
		Double estqfalta = 0.0;
		Double rec = 0.0;
		
		//====== MODELO 1 =======
		modelo = modelos.get(0);
		estq=20.00;
		estqfalta=1373.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======


		//====== MODELO 2 =======
		modelo = modelos.get(1);
		estq=10.00;
		estqfalta=1549.00;
		rec=100.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		

		//====== MODELO 3 =======
		modelo = modelos.get(2);
		estq=30.00;
		estqfalta=00.00;
		rec=00.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======

		//====== MODELO 4 =======
		modelo = modelos.get(3);
		estq=50.00;
		estqfalta=29.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 5 =======
		modelo = modelos.get(4);
		estq=10.00;
		estqfalta=937.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 6 =======
		modelo = modelos.get(5);
		estq=40.00;
		estqfalta=1861.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 7 =======
		modelo = modelos.get(6);
		estq=50.00;
		estqfalta=2810.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 8 =======
		modelo = modelos.get(7);
		estq=20.00;
		estqfalta=390.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 9 =======
		modelo = modelos.get(8);
		estq=50.00;
		estqfalta=1949.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 10 =======
		modelo = modelos.get(9);
		estq=50.00;
		estqfalta=693.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 11 =======
		modelo = modelos.get(10);
		estq=20.00;
		estqfalta=537.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 12 =======
		modelo = modelos.get(11);
		estq=30.00;
		estqfalta=559.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======

		//====== MODELO 13 =======
		modelo = modelos.get(12);
		estq=50.00;
		estqfalta=1573.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		
		//====== MODELO 14 =======
		modelo = modelos.get(13);
		estq=50.00;
		estqfalta=223.00;
		rec=0.00;
			
		modelo.setEstqInicModel(estq);
		modelo.setEstqEmFalta(estqfalta);
		modelo.setRecebimentoPendente(rec);
		modeloService.altera(modelo);
		//======          =======
		System.out.println(">>>>atualizaEstoqueERecPendente");
	}


	}

