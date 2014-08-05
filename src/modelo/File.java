 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package modelo;

public class File {

    private String Name;
    private String mime;
    private long length;
    private byte[] data;
   
    public File(){
    	
    	
    }
   
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            String extension = name.substring(extDot +1);
            if("bmp".equals(extension)){
                mime="image/bmp";
            } else if("jpg".equals(extension)){
                mime="image/jpeg";
            } else if("gif".equals(extension)){
                mime="image/gif";
            } else if("png".equals(extension)){
                mime="image/png";
            }else if("xfl".equals(extension)){
                mime="fuzzy/xfl";
            } else {
                mime = "image/unknown";
            }
        }
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    
    public String getMime(){
        return mime;
    }
}
