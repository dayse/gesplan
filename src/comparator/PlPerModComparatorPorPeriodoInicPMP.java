 
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

public class PlPerModComparatorPorPeriodoInicPMP implements Comparator<PlPerMod>{

	@Override
	public int compare(PlPerMod o1, PlPerMod o2) {
		return o1.getPeriodoPMInicioPMP() < o2.getPeriodoPMInicioPMP() ? -1 : 
			   o1.getPeriodoPMInicioPMP() > o2.getPeriodoPMInicioPMP() ? 1  : 0;
	}
}
