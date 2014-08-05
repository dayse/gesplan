 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package cargaDoSistema;


import java.util.List;

import modelo.DeModPer;
import modelo.Modelo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.DeModPerAppService;
import service.controleTransacao.FabricaDeAppService;
import util.JPAUtil;

public class CargaDeModPer {
	
	private static DeModPerAppService deModPerAppService;
 	
    @BeforeClass	
	public void setupClass(){
		
		try {
			
			deModPerAppService = FabricaDeAppService.getAppService(DeModPerAppService.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
 
    
  @Test
  public void alteraVendaPedidoDeModPer(){
	  
	  List<DeModPer> listaDeModPer = deModPerAppService.recuperaListaDeDemandaModeloPeriodo();
	  
	  
	  DeModPer deModPer1 = listaDeModPer.get(0);					//----------- INICIO DO DEMODPER 1 -----------//
	           deModPer1.setPedidosModelo(3073.0);
	           deModPer1.setVendasProjetadasModelo(2500.0);
	           
	           listaDeModPer.add(deModPer1);
	           
	           
	  DeModPer deModPer2 = listaDeModPer.get(1);
	           deModPer2.setPedidosModelo(2927.0);
	           deModPer2.setVendasProjetadasModelo(2400.0);
	           
	           listaDeModPer.add(deModPer2);


	  DeModPer deModPer3 = listaDeModPer.get(2);
	           deModPer3.setPedidosModelo(4089.0);
	           deModPer3.setVendasProjetadasModelo(4000.0);
	           
	           listaDeModPer.add(deModPer3);
	           
	  DeModPer deModPer4 = listaDeModPer.get(3);
	           deModPer4.setPedidosModelo(3911.0);
	           deModPer4.setVendasProjetadasModelo(3500.0);
	           
	           listaDeModPer.add(deModPer4);
	           
	           
	  DeModPer deModPer5 = listaDeModPer.get(4);
	           deModPer5.setPedidosModelo(4141.0);
	           deModPer5.setVendasProjetadasModelo(4000.0);
	           
	           listaDeModPer.add(deModPer5);
	           
	           
	  DeModPer deModPer6 = listaDeModPer.get(5);
	           deModPer6.setPedidosModelo(4000.0);
	           deModPer6.setVendasProjetadasModelo(4359.0);
	           
	           listaDeModPer.add(deModPer6);
	           
	           
	  DeModPer deModPer7 = listaDeModPer.get(6);
	           deModPer7.setPedidosModelo(2000.0);
	           deModPer7.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer7);
	           
	           
	  DeModPer deModPer8 = listaDeModPer.get(7);
	           deModPer8.setPedidosModelo(2000.0);
	           deModPer8.setVendasProjetadasModelo(3077.0);
	           
	           listaDeModPer.add(deModPer8);
	           
	           
	  DeModPer deModPer9 = listaDeModPer.get(8);
	           deModPer9.setPedidosModelo(1500.0);
	           deModPer9.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer9);
	           
	  DeModPer deModPer10 = listaDeModPer.get(9);					//----------- FIM DO DEMODPER 1 -----------//				
	           deModPer10.setPedidosModelo(1000.0);
	           deModPer10.setVendasProjetadasModelo(3077.0);
	           
	           listaDeModPer.add(deModPer10);
	           
	           
	  DeModPer deModPer11 = listaDeModPer.get(10);					//----------- INICIO DO DEMODPER 2 -----------//
	           deModPer11.setPedidosModelo(2949.0);
	           deModPer11.setVendasProjetadasModelo(1600.0);
	           
	           listaDeModPer.add(deModPer11);
	           
	           
	  DeModPer deModPer12 = listaDeModPer.get(11);
	           deModPer12.setPedidosModelo(1951.0);
	           deModPer12.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer12);
	           
	           
	  DeModPer deModPer13 = listaDeModPer.get(12);
	           deModPer13.setPedidosModelo(2044.0);
	           deModPer13.setVendasProjetadasModelo(1600.0);
	           
	           listaDeModPer.add(deModPer13);
	           
	  DeModPer deModPer14 = listaDeModPer.get(13);
	           deModPer14.setPedidosModelo(1956.0);
	           deModPer14.setVendasProjetadasModelo(1900.0);
	           
	           listaDeModPer.add(deModPer14);
	           
	           
	  DeModPer deModPer15 = listaDeModPer.get(14);
	           deModPer15.setPedidosModelo(1000.0);
	           deModPer15.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer15);
	           
	           
	  DeModPer deModPer16 = listaDeModPer.get(15);
	           deModPer16.setPedidosModelo(1000.0);
	           deModPer16.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer16);
	           
	           
	  DeModPer deModPer17 = listaDeModPer.get(16);
	           deModPer17.setPedidosModelo(1500.0);
	           deModPer17.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer17);
	           
	           
	  DeModPer deModPer18 = listaDeModPer.get(17);
	           deModPer18.setPedidosModelo(2000.0);
	           deModPer18.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer18);
	           
	           
	  DeModPer deModPer19 = listaDeModPer.get(18);
	           deModPer19.setPedidosModelo(1800.0);
	           deModPer19.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer19);
	           
	  DeModPer deModPer20 = listaDeModPer.get(19);					//----------- FIM DO DEMODPER 2 -----------//				
	           deModPer20.setPedidosModelo(2000.0);
	           deModPer20.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer20);
	           
	           
	  DeModPer deModPer21 = listaDeModPer.get(20);					//----------- INICIO DO DEMODPER 3 -----------//
	           deModPer21.setPedidosModelo(2561.0);
	           deModPer21.setVendasProjetadasModelo(2000.0);
	           
	           listaDeModPer.add(deModPer21);
	           
	           
	  DeModPer deModPer22 = listaDeModPer.get(21);
	           deModPer22.setPedidosModelo(2439.0);
	           deModPer22.setVendasProjetadasModelo(2300.0);
	           
	           listaDeModPer.add(deModPer22);
	           
	           
	  DeModPer deModPer23 = listaDeModPer.get(22);
	           deModPer23.setPedidosModelo(2556.0);
	           deModPer23.setVendasProjetadasModelo(2000.0);
	           
	           listaDeModPer.add(deModPer23);
	           
	  DeModPer deModPer24 = listaDeModPer.get(23);
	           deModPer24.setPedidosModelo(2444.0);
	           deModPer24.setVendasProjetadasModelo(2391.0);
	           
	           listaDeModPer.add(deModPer24);
	           
	           
	  DeModPer deModPer25 = listaDeModPer.get(24);
	           deModPer25.setPedidosModelo(2110.0);
	           deModPer25.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer25);
	           
	           
	  DeModPer deModPer26 = listaDeModPer.get(25);
	           deModPer26.setPedidosModelo(1800.0);
	           deModPer26.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer26);
	           
	           
	  DeModPer deModPer27 = listaDeModPer.get(26);
	           deModPer27.setPedidosModelo(1800.0);
	           deModPer27.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer27);
	           
	           
	  DeModPer deModPer28 = listaDeModPer.get(27);
	           deModPer28.setPedidosModelo(1500.0);
	           deModPer28.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer28);
	           
	           
	  DeModPer deModPer29 = listaDeModPer.get(28);
	           deModPer29.setPedidosModelo(1000.0);
	           deModPer29.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer29);
	           
	  DeModPer deModPer30 = listaDeModPer.get(29);					//----------- FIM DO DEMODPER 3 -----------//				
	           deModPer30.setPedidosModelo(2800.0);
	           deModPer30.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer30);
	           
	           
	  DeModPer deModPer31 = listaDeModPer.get(30);					//----------- INICIO DO DEMODPER 4 -----------//
	           deModPer31.setPedidosModelo(3329.0);
	           deModPer31.setVendasProjetadasModelo(2800.0);
	           
	           listaDeModPer.add(deModPer31);
	           
	           
	  DeModPer deModPer32 = listaDeModPer.get(31);
	           deModPer32.setPedidosModelo(3171.0);
	           deModPer32.setVendasProjetadasModelo(2500.0);
	           
	           listaDeModPer.add(deModPer32);
	           
	           
	  DeModPer deModPer33 = listaDeModPer.get(32);
	           deModPer33.setPedidosModelo(4089.0);
	           deModPer33.setVendasProjetadasModelo(4000.0);
	           
	           listaDeModPer.add(deModPer33);
	           
	  DeModPer deModPer34 = listaDeModPer.get(33);
	           deModPer34.setPedidosModelo(3911.0);
	           deModPer34.setVendasProjetadasModelo(3500.0);
	           
	           listaDeModPer.add(deModPer34);
	           
	           
	  DeModPer deModPer35 = listaDeModPer.get(34);
	           deModPer35.setPedidosModelo(3410.0);
	           deModPer35.setVendasProjetadasModelo(3400.0);
	           
	           listaDeModPer.add(deModPer35);
	           
	           
	  DeModPer deModPer36 = listaDeModPer.get(35);
	           deModPer36.setPedidosModelo(3500.0);
	           deModPer36.setVendasProjetadasModelo(3590.0);
	           
	           listaDeModPer.add(deModPer36);
	           
	           
	  DeModPer deModPer37 = listaDeModPer.get(36);
	           deModPer37.setPedidosModelo(3000.0);
	           deModPer37.setVendasProjetadasModelo(3654.0);
	           
	           listaDeModPer.add(deModPer37);
	           
	           
	  DeModPer deModPer38 = listaDeModPer.get(37);
	           deModPer38.setPedidosModelo(2000.0);
	           deModPer38.setVendasProjetadasModelo(3846.0);
	           
	           listaDeModPer.add(deModPer38);
	           
	           
	  DeModPer deModPer39 = listaDeModPer.get(38);
	           deModPer39.setPedidosModelo(1800.0);
	           deModPer39.setVendasProjetadasModelo(3897.0);
	           
	           listaDeModPer.add(deModPer39);
	           
	  DeModPer deModPer40 = listaDeModPer.get(39);					//----------- FIM DO DEMODPER 4 -----------//				
	           deModPer40.setPedidosModelo(1500.0);
	           deModPer40.setVendasProjetadasModelo(4103.0);
	           
	           listaDeModPer.add(deModPer40);
	         
	  DeModPer deModPer41 = listaDeModPer.get(40);					//----------- INICIO DO DEMODPER 5 -----------//
	           deModPer41.setPedidosModelo(3037.0);
	           deModPer41.setVendasProjetadasModelo(2800.0);
	           
	           listaDeModPer.add(deModPer41);
	           
	           
	  DeModPer deModPer42 = listaDeModPer.get(41);
	           deModPer42.setPedidosModelo(2927.0);
	           deModPer42.setVendasProjetadasModelo(2500.0);
	           
	           listaDeModPer.add(deModPer42);
	           
	           
	  DeModPer deModPer43 = listaDeModPer.get(42);
	           deModPer43.setPedidosModelo(3578.0);
	           deModPer43.setVendasProjetadasModelo(3200.0);
	           
	           listaDeModPer.add(deModPer43);
	           
	  DeModPer deModPer44 = listaDeModPer.get(43);
	           deModPer44.setPedidosModelo(3422.0);
	           deModPer44.setVendasProjetadasModelo(3300.0);
	           
	           listaDeModPer.add(deModPer44);
	           
	           
	  DeModPer deModPer45 = listaDeModPer.get(44);
	           deModPer45.setPedidosModelo(2923.0);
	           deModPer45.setVendasProjetadasModelo(2500.0);
	           
	           listaDeModPer.add(deModPer45);
	           
	           
	  DeModPer deModPer46 = listaDeModPer.get(45);
	           deModPer46.setPedidosModelo(2000.0);
	           deModPer46.setVendasProjetadasModelo(3077.0);
	           
	           listaDeModPer.add(deModPer46);
	           
	           
	  DeModPer deModPer47 = listaDeModPer.get(46);
	           deModPer47.setPedidosModelo(1000.0);
	           deModPer47.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer47);
	           
	           
	  DeModPer deModPer48 = listaDeModPer.get(47);
	           deModPer48.setPedidosModelo(800.0);
	           deModPer48.setVendasProjetadasModelo(3077.0);
	           
	           listaDeModPer.add(deModPer48);
	           
	           
	  DeModPer deModPer49 = listaDeModPer.get(48);
	           deModPer49.setPedidosModelo(700.0);
	           deModPer49.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer49);
	           
	  DeModPer deModPer50 = listaDeModPer.get(49);					//----------- FIM DO DEMODPER 5 -----------//				
	           deModPer50.setPedidosModelo(500.0);
	           deModPer50.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer50);
	           
	           
	  DeModPer deModPer51 = listaDeModPer.get(50);					//----------- INICIO DO DEMODPER 6 -----------//
	           deModPer51.setPedidosModelo(2561.0);
	           deModPer51.setVendasProjetadasModelo(2350.0);
	           
	           listaDeModPer.add(deModPer51);
	           
	           
	  DeModPer deModPer52 = listaDeModPer.get(51);
	           deModPer52.setPedidosModelo(2439.0);
	           deModPer52.setVendasProjetadasModelo(2300.0);
	           
	           listaDeModPer.add(deModPer52);
	           
	           
	  DeModPer deModPer53 = listaDeModPer.get(52);
	           deModPer53.setPedidosModelo(2556.0);
	           deModPer53.setVendasProjetadasModelo(2200.0);
	           
	           listaDeModPer.add(deModPer53);
	           
	  DeModPer deModPer54 = listaDeModPer.get(53);
	           deModPer54.setPedidosModelo(2444.0);
	           deModPer54.setVendasProjetadasModelo(2300.0);
	           
	           listaDeModPer.add(deModPer54);
	           
	           
	  DeModPer deModPer55 = listaDeModPer.get(54);
	           deModPer55.setPedidosModelo(2425.0);
	           deModPer55.setVendasProjetadasModelo(1800.0);
	           
	           listaDeModPer.add(deModPer55);
	           
	           
	  DeModPer deModPer56 = listaDeModPer.get(55);
	           deModPer56.setPedidosModelo(2000.0);
	           deModPer56.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer56);
	           
	           
	  DeModPer deModPer57 = listaDeModPer.get(56);
	           deModPer57.setPedidosModelo(1500.0);
	           deModPer57.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer57);
	           
	           
	  DeModPer deModPer58 = listaDeModPer.get(57);
	           deModPer58.setPedidosModelo(1000.0);
	           deModPer58.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer58);
	           
	           
	  DeModPer deModPer59 = listaDeModPer.get(58);
	           deModPer59.setPedidosModelo(800.0);
	           deModPer59.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer59);
	           
	  DeModPer deModPer60 = listaDeModPer.get(59);					//----------- FIM DO DEMODPER 6 -----------//				
	           deModPer60.setPedidosModelo(500.0);
	           deModPer60.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer60);
	           
	           
	  DeModPer deModPer61 = listaDeModPer.get(60);					//----------- INICIO DO DEMODPER 7 -----------//
	           deModPer61.setPedidosModelo(1588.0);
	           deModPer61.setVendasProjetadasModelo(4000.0);
	           
	           listaDeModPer.add(deModPer61);
	           
	           
	  DeModPer deModPer62 = listaDeModPer.get(61);
	           deModPer62.setPedidosModelo(1512.0);
	           deModPer62.setVendasProjetadasModelo(1400.0);
	           
	           listaDeModPer.add(deModPer62);
	           
	           
	  DeModPer deModPer63 = listaDeModPer.get(62);
	           deModPer63.setPedidosModelo(1533.0);
	           deModPer63.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer63);
	           
	  DeModPer deModPer64 = listaDeModPer.get(63);
	           deModPer64.setPedidosModelo(1300.0);
	           deModPer64.setVendasProjetadasModelo(1300.0);
	           
	           listaDeModPer.add(deModPer64);
	           
	           
	  DeModPer deModPer65 = listaDeModPer.get(64);
	           deModPer65.setPedidosModelo(1462.0);
	           deModPer65.setVendasProjetadasModelo(1350.0);
	           
	           listaDeModPer.add(deModPer65);
	           
	           
	  DeModPer deModPer66 = listaDeModPer.get(65);
	           deModPer66.setPedidosModelo(1300.0);
	           deModPer66.setVendasProjetadasModelo(1350.0);
	           
	           listaDeModPer.add(deModPer66);
	           
	           
	  DeModPer deModPer67 = listaDeModPer.get(66);
	           deModPer67.setPedidosModelo(1462.0);
	           deModPer67.setVendasProjetadasModelo(1462.0);
	           
	           listaDeModPer.add(deModPer67);
	           
	           
	  DeModPer deModPer68 = listaDeModPer.get(67);
	           deModPer68.setPedidosModelo(1000.0);
	           deModPer68.setVendasProjetadasModelo(1538.0);
	           
	           listaDeModPer.add(deModPer68);
	           
	           
	  DeModPer deModPer69 = listaDeModPer.get(68);
	           deModPer69.setPedidosModelo(900.0);
	           deModPer69.setVendasProjetadasModelo(1510.0);
	           
	           listaDeModPer.add(deModPer69);
	           
	  DeModPer deModPer70 = listaDeModPer.get(69);					//----------- FIM DO DEMODPER 7 -----------//				
	           deModPer70.setPedidosModelo(500.0);
	           deModPer70.setVendasProjetadasModelo(1590.0);
	           
	           listaDeModPer.add(deModPer70);
	           
	           
	  DeModPer deModPer71 = listaDeModPer.get(70);					//----------- INICIO DO DEMODPER 8 -----------//
	           deModPer71.setPedidosModelo(1690.0);
	           deModPer71.setVendasProjetadasModelo(1400.0);
	           
	           listaDeModPer.add(deModPer71);
	           
	           
	  DeModPer deModPer72 = listaDeModPer.get(71);
	           deModPer72.setPedidosModelo(1610.0);
	           deModPer72.setVendasProjetadasModelo(1300.0);
	           
	           listaDeModPer.add(deModPer72);
	           
	           
	  DeModPer deModPer73 = listaDeModPer.get(72);
	           deModPer73.setPedidosModelo(1533.0);
	           deModPer73.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer73);
	           
	  DeModPer deModPer74 = listaDeModPer.get(73);
	           deModPer74.setPedidosModelo(1300.0);
	           deModPer74.setVendasProjetadasModelo(1300.0);
	           
	           listaDeModPer.add(deModPer74);
	           
	           
	  DeModPer deModPer75 = listaDeModPer.get(74);
	           deModPer75.setPedidosModelo(1364.0);
	           deModPer75.setVendasProjetadasModelo(1300.0);
	           
	           listaDeModPer.add(deModPer75);
	           
	           
	  DeModPer deModPer76 = listaDeModPer.get(75);
	           deModPer76.setPedidosModelo(1000.0);
	           deModPer76.setVendasProjetadasModelo(1463.0);
	           
	           listaDeModPer.add(deModPer76);
	           
	           
	  DeModPer deModPer77 = listaDeModPer.get(76);
	           deModPer77.setPedidosModelo(1500.0);
	           deModPer77.setVendasProjetadasModelo(1364.0);
	           
	           listaDeModPer.add(deModPer77);
	           
	           
	  DeModPer deModPer78 = listaDeModPer.get(77);
	           deModPer78.setPedidosModelo(800.0);
	           deModPer78.setVendasProjetadasModelo(1436.0);
	           
	           listaDeModPer.add(deModPer78);
	           
	           
	  DeModPer deModPer79 = listaDeModPer.get(78);
	           deModPer79.setPedidosModelo(800.0);
	           deModPer79.setVendasProjetadasModelo(1608.0);
	           
	           listaDeModPer.add(deModPer79);
	           
	  DeModPer deModPer80 = listaDeModPer.get(79);					//----------- FIM DO DEMODPER 8 -----------//				
	           deModPer80.setPedidosModelo(600.0);
	           deModPer80.setVendasProjetadasModelo(1692.0);
	           
	           listaDeModPer.add(deModPer80);
	           
	                
	  DeModPer deModPer81 = listaDeModPer.get(80);					//----------- INICIO DO DEMODPER 9 -----------//
	           deModPer81.setPedidosModelo(2949.0);
	           deModPer81.setVendasProjetadasModelo(1000.0);
	           
	           listaDeModPer.add(deModPer81);
	           
	 DeModPer  deModPer82 = listaDeModPer.get(81);					
	           deModPer82.setPedidosModelo(1951.0);
	           deModPer82.setVendasProjetadasModelo(1200.0);
	           
	           listaDeModPer.add(deModPer82);
	           
	           
	  DeModPer deModPer83 = listaDeModPer.get(82);
	           deModPer83.setPedidosModelo(2300.0);
	           deModPer83.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer83);


	  DeModPer deModPer84 = listaDeModPer.get(83);
	           deModPer84.setPedidosModelo(2200.0);
	           deModPer84.setVendasProjetadasModelo(1600.0);
	           
	           listaDeModPer.add(deModPer84);
	           
	  DeModPer deModPer85 = listaDeModPer.get(84);
	           deModPer85.setPedidosModelo(2436.0);
	           deModPer85.setVendasProjetadasModelo(2000.0);
	           
	           listaDeModPer.add(deModPer85);
	           
	           
	  DeModPer deModPer86 = listaDeModPer.get(85);
	           deModPer86.setPedidosModelo(1200.0);
	           deModPer86.setVendasProjetadasModelo(2300.0);
	           
	           listaDeModPer.add(deModPer86);
	           
	           
	  DeModPer deModPer87 = listaDeModPer.get(86);
	           deModPer87.setPedidosModelo(1200.0);
	           deModPer87.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer87);
	           
	           
	  DeModPer deModPer88 = listaDeModPer.get(87);
	           deModPer88.setPedidosModelo(1000.0);
	           deModPer88.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer88);
	           
	           
	  DeModPer deModPer89 = listaDeModPer.get(88);
	           deModPer89.setPedidosModelo(1600.0);
	           deModPer89.setVendasProjetadasModelo(1705.0);
	           
	           listaDeModPer.add(deModPer89);
	           
	           
	  DeModPer deModPer90 = listaDeModPer.get(89);
	           deModPer90.setPedidosModelo(1500.0);
	           deModPer90.setVendasProjetadasModelo(1650.0);
	           
	           listaDeModPer.add(deModPer90);
	           
	  DeModPer deModPer91 = listaDeModPer.get(90);					//----------- FIM DO DEMODPER 9 -----------//				
	           deModPer91.setPedidosModelo(1793.0);
	           deModPer91.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer91);
	           
	           
	  DeModPer deModPer92 = listaDeModPer.get(91);					//----------- INICIO DO DEMODPER 10 -----------//
	           deModPer92.setPedidosModelo(1707.0);
	           deModPer92.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer92);
	           
	           
	  DeModPer deModPer93 = listaDeModPer.get(92);
	           deModPer93.setPedidosModelo(1400.0);
	           deModPer93.setVendasProjetadasModelo(1430.0);
	           
	           listaDeModPer.add(deModPer93);
	           
	           
	  DeModPer deModPer94 = listaDeModPer.get(93);
	           deModPer94.setPedidosModelo(1260.0);
	           deModPer94.setVendasProjetadasModelo(1450.0);
	           
	           listaDeModPer.add(deModPer94);
	           
	  DeModPer deModPer95 = listaDeModPer.get(94);
	           deModPer95.setPedidosModelo(1413.0);
	           deModPer95.setVendasProjetadasModelo(1400.0);
	           
	           listaDeModPer.add(deModPer95);
	           
	           
	  DeModPer deModPer96 = listaDeModPer.get(95);
	           deModPer96.setPedidosModelo(1100.0);
	           deModPer96.setVendasProjetadasModelo(1487.0);
	           
	           listaDeModPer.add(deModPer96);
	           
	           
	  DeModPer deModPer97 = listaDeModPer.get(96);
	           deModPer97.setPedidosModelo(1000.0);
	           deModPer97.setVendasProjetadasModelo(1462.0);
	           
	           listaDeModPer.add(deModPer97);
	           
	           
	  DeModPer deModPer98 = listaDeModPer.get(97);
	           deModPer98.setPedidosModelo(500.0);
	           deModPer98.setVendasProjetadasModelo(1180.0);
	           
	           listaDeModPer.add(deModPer98);
	           
	           
	  DeModPer deModPer99 = listaDeModPer.get(98);
	           deModPer99.setPedidosModelo(400.0);
	           deModPer99.setVendasProjetadasModelo(1200.0);
	           
	           listaDeModPer.add(deModPer99);
	           
	           
	  DeModPer deModPer100 = listaDeModPer.get(99);
	           deModPer100.setPedidosModelo(200.0);
	           deModPer100.setVendasProjetadasModelo(1005.0);
	           
	           listaDeModPer.add(deModPer100);
	           
	  DeModPer deModPer101  = listaDeModPer.get(100);					//----------- FIM DO DEMODPER 10 -----------//				
	           deModPer101.setPedidosModelo(1537.0);
	           deModPer101.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer101);
	           
	           
	  DeModPer deModPer102 = listaDeModPer.get(101);					//----------- INICIO DO DEMODPER 11 -----------//
	           deModPer102.setPedidosModelo(1463.0);
	           deModPer102.setVendasProjetadasModelo(1400.0);
	           
	           listaDeModPer.add(deModPer102);
	           
	           
	  DeModPer deModPer103 = listaDeModPer.get(102);
	           deModPer103.setPedidosModelo(1960.0);
	           deModPer103.setVendasProjetadasModelo(1930.0);
	           
	           listaDeModPer.add(deModPer103);
	           
	           
	  DeModPer deModPer104 = listaDeModPer.get(103);
	           deModPer104.setPedidosModelo(1830.0);
	           deModPer104.setVendasProjetadasModelo(1850.0);
	           
	           listaDeModPer.add(deModPer104);
	           
	  DeModPer deModPer105 = listaDeModPer.get(104);
	           deModPer105.setPedidosModelo(1462.0);
	           deModPer105.setVendasProjetadasModelo(1400.0);
	           
	           listaDeModPer.add(deModPer105);
	           
	           
	  DeModPer deModPer106 = listaDeModPer.get(105);
	           deModPer106.setPedidosModelo(1300.0);
	           deModPer106.setVendasProjetadasModelo(1538.0);
	           
	           listaDeModPer.add(deModPer106);
	           
	           
	  DeModPer deModPer107 = listaDeModPer.get(106);
	           deModPer107.setPedidosModelo(900.0);
	           deModPer107.setVendasProjetadasModelo(1330.0);
	           
	           listaDeModPer.add(deModPer107);
	           
	           
	  DeModPer deModPer108 = listaDeModPer.get(107);
	           deModPer108.setPedidosModelo(200.0);
	           deModPer108.setVendasProjetadasModelo(1035.0);
	           
	           listaDeModPer.add(deModPer108);
	           
	           
	  DeModPer deModPer109 = listaDeModPer.get(108);
	           deModPer109.setPedidosModelo(100.0);
	           deModPer109.setVendasProjetadasModelo(1026.0);
	           
	           listaDeModPer.add(deModPer109);
	           
	           
	  DeModPer deModPer110 = listaDeModPer.get(109);
	           deModPer110.setPedidosModelo(100.0);
	           deModPer110.setVendasProjetadasModelo(1026.0);
	           
	           listaDeModPer.add(deModPer110);
	           
	  DeModPer deModPer111  = listaDeModPer.get(110);					//----------- FIM DO DEMODPER 11 -----------//				
	           deModPer111.setPedidosModelo(2049.0);
	           deModPer111.setVendasProjetadasModelo(2000.0);
	           
	           listaDeModPer.add(deModPer111);
	           
	           
	  DeModPer deModPer112 = listaDeModPer.get(111);					//----------- INICIO DO DEMODPER 12 -----------//
	           deModPer112.setPedidosModelo(1951.0);
	           deModPer112.setVendasProjetadasModelo(1900.0);
	           
	           listaDeModPer.add(deModPer112);
	           
	           
	  DeModPer deModPer113 = listaDeModPer.get(112);
	           deModPer113.setPedidosModelo(2300.0);
	           deModPer113.setVendasProjetadasModelo(2300.0);
	           
	           listaDeModPer.add(deModPer113);
	           
	           
	  DeModPer deModPer114 = listaDeModPer.get(113);
	           deModPer114.setPedidosModelo(2000.0);
	           deModPer114.setVendasProjetadasModelo(2000.0);
	           
	           listaDeModPer.add(deModPer114);
	           
	  DeModPer deModPer115 = listaDeModPer.get(114);
	           deModPer115.setPedidosModelo(2130.0);
	           deModPer115.setVendasProjetadasModelo(2000.0);
	           
	           listaDeModPer.add(deModPer115);
	           
	           
	  DeModPer deModPer116 = listaDeModPer.get(115);
	           deModPer116.setPedidosModelo(1830.0);
	           deModPer116.setVendasProjetadasModelo(1900.0);
	           
	           listaDeModPer.add(deModPer116);
	           
	           
	  DeModPer deModPer117 = listaDeModPer.get(116);
	           deModPer117.setPedidosModelo(1000.0);
	           deModPer117.setVendasProjetadasModelo(1705.0);
	           
	           listaDeModPer.add(deModPer117);
	           
	           
	  DeModPer deModPer118 = listaDeModPer.get(117);
	           deModPer118.setPedidosModelo(1000.0);
	           deModPer118.setVendasProjetadasModelo(1795.0);
	           
	           listaDeModPer.add(deModPer118);
	           
	           
	  DeModPer deModPer119 = listaDeModPer.get(118);
	           deModPer119.setPedidosModelo(500.0);
	           deModPer119.setVendasProjetadasModelo(2192.0);
	           
	           listaDeModPer.add(deModPer119);
	           
	           
	  DeModPer deModPer120 = listaDeModPer.get(119);
	           deModPer120.setPedidosModelo(400.0);
	           deModPer120.setVendasProjetadasModelo(2308.0);
	           
	           listaDeModPer.add(deModPer120);
	           
	  DeModPer deModPer121  = listaDeModPer.get(120);					//----------- FIM DO DEMODPER 12 -----------//				
	           deModPer121.setPedidosModelo(3073.0);
	           deModPer121.setVendasProjetadasModelo(3000.0);
	           
	           listaDeModPer.add(deModPer121);
	           
	           
	  DeModPer deModPer122 = listaDeModPer.get(121);					//----------- INICIO DO DEMODPER 13 -----------//
	           deModPer122.setPedidosModelo(2927.0);
	           deModPer122.setVendasProjetadasModelo(2900.0);
	           
	           listaDeModPer.add(deModPer122);
	           
	           
	  DeModPer deModPer123 = listaDeModPer.get(122);
	           deModPer123.setPedidosModelo(3578.0);
	           deModPer123.setVendasProjetadasModelo(3500.0);
	           
	           listaDeModPer.add(deModPer123);
	           
	           
	  DeModPer deModPer124 = listaDeModPer.get(123);
	           deModPer124.setPedidosModelo(3422.0);
	           deModPer124.setVendasProjetadasModelo(3400.0);
	           
	           listaDeModPer.add(deModPer124);
	           
	  DeModPer deModPer125 = listaDeModPer.get(124);
	           deModPer125.setPedidosModelo(2923.0);
	           deModPer125.setVendasProjetadasModelo(2900.0);
	           
	           listaDeModPer.add(deModPer125);
	           
	           
	  DeModPer deModPer126 = listaDeModPer.get(125);
	           deModPer126.setPedidosModelo(3000.0);
	           deModPer126.setVendasProjetadasModelo(3077.0);
	           
	           listaDeModPer.add(deModPer126);
	           
	           
	  DeModPer deModPer127 = listaDeModPer.get(126);
	           deModPer127.setPedidosModelo(2000.0);
	           deModPer127.setVendasProjetadasModelo(3410.0);
	           
	           listaDeModPer.add(deModPer127);
	           
	           
	  DeModPer deModPer128 = listaDeModPer.get(127);
	           deModPer128.setPedidosModelo(2000.0);
	           deModPer128.setVendasProjetadasModelo(3590.0);
	           
	           listaDeModPer.add(deModPer128);
	           
	           
	  DeModPer deModPer129 = listaDeModPer.get(128);
	           deModPer129.setPedidosModelo(1000.0);
	           deModPer129.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer129);
	           
	           
	  DeModPer deModPer130 = listaDeModPer.get(129);
	           deModPer130.setPedidosModelo(500.0);
	           deModPer130.setVendasProjetadasModelo(3590.0);
	           
	           listaDeModPer.add(deModPer130);
	           
	  DeModPer deModPer131  = listaDeModPer.get(130);					//----------- FIM DO DEMODPER 13 -----------//				
	           deModPer131.setPedidosModelo(1000.0);
	           deModPer131.setVendasProjetadasModelo(2923.0);
	           
	           listaDeModPer.add(deModPer131);
	           
	           
	  DeModPer deModPer132 = listaDeModPer.get(131);					//----------- INICIO DO DEMODPER 14 -----------//
	           deModPer132.setPedidosModelo(2439.0);
	           deModPer132.setVendasProjetadasModelo(2200.0);
	           
	           listaDeModPer.add(deModPer132);
	           
	           
	  DeModPer deModPer133 = listaDeModPer.get(132);
	           deModPer133.setPedidosModelo(2300.0);
	           deModPer133.setVendasProjetadasModelo(2200.0);
	           
	           listaDeModPer.add(deModPer133);
	           
	           
	  DeModPer deModPer134 = listaDeModPer.get(133);
	           deModPer134.setPedidosModelo(2100.0);
	           deModPer134.setVendasProjetadasModelo(2130.0);
	           
	           listaDeModPer.add(deModPer134);
	           
	  DeModPer deModPer135 = listaDeModPer.get(134);
	           deModPer135.setPedidosModelo(1949.0);
	           deModPer135.setVendasProjetadasModelo(1500.0);
	           
	           listaDeModPer.add(deModPer135);
	           
	           
	  DeModPer deModPer136 = listaDeModPer.get(135);
	           deModPer136.setPedidosModelo(1300.0);
	           deModPer136.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer136);
	           
	           
	  DeModPer deModPer137 = listaDeModPer.get(136);
	           deModPer137.setPedidosModelo(1200.0);
	           deModPer137.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer137);
	           
	           
	  DeModPer deModPer138 = listaDeModPer.get(137);
	           deModPer138.setPedidosModelo(1100.0);
	           deModPer138.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer138);
	           
	           
	  DeModPer deModPer139 = listaDeModPer.get(138);
	           deModPer139.setPedidosModelo(1000.0);
	           deModPer139.setVendasProjetadasModelo(1949.0);
	           
	           listaDeModPer.add(deModPer129);
	           
	           
	  DeModPer deModPer140 = listaDeModPer.get(139);					//----------- FIM DO DEMODPER 14 -----------//	
	           deModPer140.setPedidosModelo(1000.0);
	           deModPer140.setVendasProjetadasModelo(2051.0);
	           
	           listaDeModPer.add(deModPer140);

	           
	            // ----------- ALTERAÇAO DEMODPER ------------- //-
	           	for (DeModPer deModPer : listaDeModPer) {
		  
	           		deModPerAppService.altera(deModPer);
	           	}         
	           
  		}
  
}

