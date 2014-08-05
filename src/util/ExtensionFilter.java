 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;
import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilter implements FilenameFilter {
  private String extension;
  public ExtensionFilter( String extension ) {
    this.extension = extension;             
  }
  
  public boolean accept(File dir, String name) {
    return (name.endsWith(extension));
  }
}
