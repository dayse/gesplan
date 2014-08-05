 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package comparator;

import java.util.Comparator;

import modelo.PlPerMod;
import modelo.PlanoModelo;

public class PlPerModComparatorPorPerioPM implements Comparator<PlPerMod>{

	/**O PlPerMod esta sendo ordenado primeiramente pela ordem natural(CadPlan + Modelo), vide 
	 * compareTo da classe pojo.
	 * Aqui ele esta adicionando um novo criterio alem da ordem natural(PerioPM), ou seja a sequencia
	 * estara ordenada por PlanoModelo+PerioPM 
	 * 
	 * Sendo que PlanoModelo é ordenado naturalmente  por (CodPlan + CodModelo) e
	 * PerioPM é ordenado naturalmente por PeriodoPM.
	 * 
	 * Resultado Final da ordenação: CodPlan +CodModelo+ PeriodoPM
	 * 
	 * Atencao: O campo usado no compareTo nao pode estar NULL no momento da comparacao
	 * caso contrario dara nullpointer exception
	 * */
	@Override
	public int compare(PlPerMod o1, PlPerMod o2) {
		return o1.getPerioPM().compareTo(o2.getPerioPM());
	}
}
