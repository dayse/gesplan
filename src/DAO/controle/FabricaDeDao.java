 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.controle;

import net.sf.cglib.proxy.Enhancer;

public class FabricaDeDao
{
    
	@SuppressWarnings("unchecked")  
    public static <T> T getDao(Class classeDoDao) 
        throws Exception 
    {
		return (T)Enhancer.create (classeDoDao, new InterceptadorDeDAO());
    }
}
