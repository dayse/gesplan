 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import exception.relatorio.RelatorioException;

/**
 * implementa os metodos que recuperam o arquivo compilado Jasper.
 * Implementa a interface Relatorio.java
 * (não precisou implementar o metodo gerarRelatorio porque esta é uma classe abstrata)
 * @author walanem
 *
 */
public abstract class DeclaracaoAbstrata implements Relatorio {

    protected InputStream recuperaJasper(String nomeJasper) {
        
    	FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

        return servletContext.getResourceAsStream(nomeJasper);		//Recupera o arquivo com extensão .jasper
    }

    protected InputStream recuperaJasperLocalmente(String nomeJasper) throws FileNotFoundException {

        InputStream jasper = new FileInputStream(nomeJasper);

        return jasper;
    }
}
