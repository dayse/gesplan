 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;

import java.util.ArrayList;
import java.util.List;

import modelo.Excecao;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.ExcecaoAppService;
import service.controleTransacao.FabricaDeAppService;
import service.exception.AplicacaoException;
import util.JPAUtil;

public class CargaExcecao {

	private static ExcecaoAppService excecaoService;

	@BeforeClass
	public void setupClass() {

		try {
			excecaoService = FabricaDeAppService
					.getAppService(ExcecaoAppService.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void incluirExcecoes() throws AplicacaoException {

		// -------------------- Excecao 1 ---------------------//
		Excecao excecao1 = new Excecao();
		excecao1.setDescrExcecao("Saldo Inicial Negativo no Inicio do HP.");
		excecao1.setTipoDeExcecao("1");
		excecao1.setStatusExcecao(true);		

		// -------------------- Excecao 2 ---------------------//
		Excecao excecao2 = new Excecao();
		excecao2.setDescrExcecao("Estoque Inicial Excessivo.");
		excecao2.setTipoDeExcecao("2");
		excecao2.setStatusExcecao(false);

		// -------------------- Excecao 3 ---------------------//
		Excecao excecao3 = new Excecao();
		excecao3.setDescrExcecao("Inicio PMP Antes do Primeiro Periodo do HP.");
		excecao3.setTipoDeExcecao("3");
		excecao3.setStatusExcecao(true);

		// -------------------- Excecao 4 ---------------------//
		Excecao excecao4 = new Excecao();
		excecao4.setDescrExcecao("Ordem do Plano Mestre Prestes a Ser Liberada.");
		excecao4.setTipoDeExcecao("4");
		excecao4.setStatusExcecao(false);

		
		// -------------------- Excecao 5 ---------------------//
		Excecao excecao5 = new Excecao();
		excecao5.setDescrExcecao("Recebimentos Pendentes Com Prazo Vencido.");
		excecao5.setTipoDeExcecao("5");
		excecao5.setStatusExcecao(true);

		// -------------------- Excecao 6 ---------------------//
		Excecao excecao6 = new Excecao();
		excecao6.setDescrExcecao("Antecipar Recebimento Plano Mestre.");
		excecao6.setTipoDeExcecao("6");
		excecao6.setStatusExcecao(false);

		// -------------------- Excecao 7 ---------------------//
		Excecao excecao7 = new Excecao();
		excecao7.setDescrExcecao("Postergar Recebimento Plano Mestre.");
		excecao7.setTipoDeExcecao("7");
		excecao7.setStatusExcecao(true);
		// -------------------- Excecao 8 ---------------------//
		Excecao excecao8 = new Excecao();
		excecao8.setDescrExcecao("Cancelar Recebimento Plano Mestre.");
		excecao8.setTipoDeExcecao("8");
		excecao8.setStatusExcecao(true);
		
		// -------------------- Excecao 9 ---------------------//
		Excecao excecao9 = new Excecao();
		excecao9.setDescrExcecao("Estoque Excessivo no Periodo.");
		excecao9.setTipoDeExcecao("9");
		excecao9.setStatusExcecao(true);
		
		// -------------- LISTA DE Excecoes -----------------//
		List<Excecao> excecoes = new ArrayList<Excecao>();

		excecoes.add(excecao1);
		excecoes.add(excecao2);
		excecoes.add(excecao3);
		excecoes.add(excecao4);
		excecoes.add(excecao5);
		excecoes.add(excecao6);
		excecoes.add(excecao7);
		excecoes.add(excecao8);
		excecoes.add(excecao9);

		// -------------- INCLUSAO DE EXCECOES ---------------//
		for (Excecao excecao : excecoes) {

			excecaoService.inclui(excecao);
//			System.out.println("tipo: " + excecao.getTipoDeExcecao() + " desc:"+excecao.getDescrExcecao());

		}

	}

}
