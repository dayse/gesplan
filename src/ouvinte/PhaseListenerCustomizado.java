 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package ouvinte;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;

public class PhaseListenerCustomizado implements PhaseListener
{
	private final static long serialVersionUID = 1;
	
	public PhaseListenerCustomizado()
	{	System.out.println(">>>>>>>>>>>>>>>>>> Criou o PhaseListenerCustomizado");
	}
	
	public PhaseId getPhaseId()
	{	return PhaseId.ANY_PHASE;
	}
	
	public void beforePhase(PhaseEvent e)
	{	System.out.println("BEFORE " + e.getPhaseId());
	}
	
	public void afterPhase(PhaseEvent e)
	{	System.out.println("AFTER " + e.getPhaseId());
	}
}
