 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package comparator;

import java.util.Comparator;

import modelo.PlanoModelo;

public class PlanoModeloComparatorPorCadPlan implements Comparator<PlanoModelo>{

	@Override
	public int compare(PlanoModelo o1, PlanoModelo o2) {
		return o1.getCadPlan().compareTo(o2.getCadPlan());
	}

}
