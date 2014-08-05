 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package JPA;

import org.testng.annotations.Test;

import util.JPAUtil;

@Test
public class TesteJPA {
	  
	@Test
	public void startupJPA() {
		JPAUtil.JPAstartUp();
	}

}
