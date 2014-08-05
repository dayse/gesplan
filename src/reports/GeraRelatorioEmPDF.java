 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package reports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class GeraRelatorioEmPDF extends HttpServlet
{
	private final static long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
      	throws ServletException, IOException
  	{
		String nomeDoRelatorio = (String)request.getAttribute("nomeDoRelatorio");
		List lista = (List)request.getAttribute("lista");
		HashMap<String, String> parametros = (HashMap<String, String>)request.getAttribute("parametros");
		
		ServletContext context = this.getServletConfig().getServletContext();
		
		String nomeDoArquivoCompilado = context.getRealPath(nomeDoRelatorio);
		File arquivo = new File(nomeDoArquivoCompilado);
		
		try
		{	JasperReport relatorioCompilado = (JasperReport) JRLoader.loadObject(arquivo);
            
		    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);

		    JasperPrint relatorioFinal = JasperFillManager.fillReport(relatorioCompilado, parametros, ds);

		    response.setContentType("application/pdf;charset=UTF-8");

		    // Adequado para se enviar dados binários para o browser.
		    ServletOutputStream out = response.getOutputStream();
		    
	      	JasperExportManager.exportReportToPdfStream(relatorioFinal, out);
	      	out.flush();
	    }
		catch(JRException e)
		{	response.setContentType("text/plain");
			PrintWriter printWriter = response.getWriter();
			e.printStackTrace(printWriter);
		} 
  	}
}
