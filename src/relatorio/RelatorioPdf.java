 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package relatorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import exception.relatorio.RelatorioException;

/**
 * classe que implementa o
 *
 * metodo responsavel pelo preenchimento do relatorio, a partir do arquivo jasper compilado, do nome do arquivo pdf, 
 * considerando os dados sob a forma de List e os parâmetros 
*/
public class RelatorioPdf {
	
	private String caminhoRelatorioPDF;

    public RelatorioPdf() {
    }

    public RelatorioPdf(String pathRelatorioPDF) {
        this.caminhoRelatorioPDF = pathRelatorioPDF;
    }
    
    /**
     * metodo responsavel pelo preenchimento do relatorio, a partir do arquivo jasper compilado, do nome do arquivo pdf, 
     * e considerando os dados sob a forma de List e os parâmetros (dayse)
     * 
     * @param nomeArquivo
     * @param streamRelatorio
     * @param parametros
     * @param dados
     * @throws RelatorioException
     */
    public void download(String nomeArquivo, InputStream streamRelatorio, Map parametros, List dados) throws RelatorioException {

        String caminhoRelatorio = caminhoRelatorioPDF + nomeArquivo;
        System.out.println("CAMINHO RELATÓRIO: " + caminhoRelatorio);

        File relatorio = new File(caminhoRelatorio);
        byte[] buffer = null;

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + nomeArquivo + "\"");

            if (parametros == null) {
                parametros = new HashMap();
            }
            
            //Neste ponto, a nossa lista preenchida com o modelo é entregue ao Jasper, que cria um DataSource com ela
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);

            //Aqui é onde ocorre de fato o preenchimento do relatório, com os parâmetros e DataSource passados
            JasperPrint impressao = JasperFillManager.fillReport(streamRelatorio, parametros, dataSource);

            //Ponto onde o relatório com os dados montados é exportado para o formato PDF
            JasperExportManager.exportReportToPdfFile(impressao, caminhoRelatorio);

            FileInputStream fis = new FileInputStream(relatorio);
            OutputStream os = response.getOutputStream();

            int read = 0;
            buffer = new byte[1024];

            while ((read = fis.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            
            os.flush();
            os.close();
            fis.close();

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new RelatorioException(ex);	
        } finally {
            buffer = null; 
        }
    }

}
