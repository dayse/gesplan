 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;


import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import modelo.File;


/**
 * ATENCAO: Esta entidade nao tem tela propria.
 * Os outros Actions é que utilizariam o service dessa entidade.
 * Possivelmente este action nao sera utilizado. 
 * */
public class TesteFileActions extends BaseActions {

	// Services
	
	// Paginas
	public final String PAGINA_MARGEM_SEG = "editMargemSeg";
	public final String PAGINA_PERCENT_PERDA  = "editPercentPerda";
	public final String PAGINA_INT_FIXO = "editIntFixo";
	public final String PAGINA_INIC_PLAN = "editInicPlan";
	

	//vars de arquivos
    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 5;
    private boolean autoUpload = false;
    private boolean useFlash = false;
	
    
	public TesteFileActions() throws Exception {
		
	}
    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getFiles().get((Integer)object).getData());
    }

    /**
     * Este método é responsável pelo upload de arquivos, perceba que este
     * metodo esta ligado a um fileUploadListener, ou seja, a um listener de aplicaçao,
     * por isso nao foi necessario passar um atributo do tipo File, pois o proprio
     * atributo fileUploadListener encarrega-se de receber um objeto do tipo File. 
     * 
     * @author marques.araujo
     * @param evento
     * 
     */
    public void enviandoArquivo(UploadEvent evento)throws FileNotFoundException{

    	
    	UploadItem item = evento.getUploadItem();
        File file = new File();
        file.setLength(item.getData().length);
        file.setName(item.getFileName());
        file.setData(item.getData());
        files.add(file);
        uploadsAvailable--;

    	String destinoDoArquivo = "C:\\FileUpLoad\\testeUpload\\";

    	System.out.println("Destino do arquivo="+destinoDoArquivo);
    	System.out.println("original="+item.getFile());
    	
    	String filePathName = item.getFileName();
    	String fileName = "";
    	StringTokenizer st = new StringTokenizer(filePathName,"\\");
    	
    	while(st.hasMoreElements()){
    		fileName = st.nextToken();
    	}
    	
    	
    	destinoDoArquivo = destinoDoArquivo  + fileName;
    	OutputStream out = new FileOutputStream(destinoDoArquivo);
    	
    	try{
    		out.write(item.getData());
    		out.close();
    		
    	}catch(IOException error){
    		
    		System.out.println("Ocorreu um erro ao enviar o arquivo");

    	}
    	
    	
    }
    
    
    
    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(5);
        return null;
    }
    
    
	// ================================== Métodos get() e set() ================================== //

    
    public int getSize() {
        if (getFiles().size()>0){
            return getFiles().size();
        }else 
        {
            return 0;
        }
    }
    
    public long getTimeStamp(){
        return System.currentTimeMillis();
    }
    
    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) { 
        this.files = files;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

}
