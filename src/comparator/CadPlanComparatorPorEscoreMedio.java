 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package comparator;

import java.util.Comparator;

import modelo.CadPlan;

public class CadPlanComparatorPorEscoreMedio implements Comparator<CadPlan>{

	@Override
	public int compare(CadPlan o1, CadPlan o2) {
		if(o1.getEscoreMedio() > o2.getEscoreMedio())
			return -1;
		else if(o1.getEscoreMedio() == o2.getEscoreMedio())
			return 0;
		else
			return 1;		
	}

}
