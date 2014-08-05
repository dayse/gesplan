 
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

public class GeraRelatorioEmHTML extends HttpServlet
{
	private final static long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
      	throws ServletException, IOException
  	{
		String nomeDoRelatorio = (String)request.getAttribute("nomeDoRelatorio");
		
		System.out.println(">>>>>>>>>>>>>>>Nome do relatorio="+nomeDoRelatorio);
		
		List lista = (List)request.getAttribute("lista");
		
		HashMap<String, String> parametros = (HashMap<String, String>)request.getAttribute("parametros");
		
		ServletContext context = this.getServletContext();
		
		String nomeDoArquivoCompilado = context.getRealPath(nomeDoRelatorio);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> nomeDoArquivoCompilado="+nomeDoArquivoCompilado);
		File arquivo = new File(nomeDoArquivoCompilado);
		PrintWriter printWriter = response.getWriter();
		
		try
		{	JasperReport relatorioCompilado = (JasperReport) JRLoader.loadObject(arquivo);
            
		    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);

		    JasperPrint impressao = JasperFillManager.fillReport(relatorioCompilado, parametros, ds);

			JRHtmlExporter htmlExporter = new JRHtmlExporter();
			
			response.setContentType("text/html");
			
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, impressao);
			htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, impressao);
			htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER, printWriter);
			htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/gesplan/image?image=");
			htmlExporter.exportReport();
			
		}
		catch(JRException e)
		{	e.printStackTrace(printWriter);
		} 
  	}
}
