 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package validadores;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValorMaiorQueZero implements Validator, Serializable {
	private final static long serialVersionUID = 1;
	
	
	
	public void validate(FacesContext facesContext, 
	        UIComponent componente, Object object) throws ValidatorException {
	
	Double valor = (Double)object;
	
	
	
	
	if (valor == null) {
		System.out.println("%%%%%%%%%%%%%%%%%%%% Um valor nulo foi enviado pelo browser. %%%%%%%%%%%%%%%%%%%%");
	}
		
	   //recuperando o texto da mensagem a partir do message bundle
	   facesContext = FacesContext.getCurrentInstance();
	  
	   UIViewRoot viewRoot = facesContext.getViewRoot();
	   Locale locale = viewRoot.getLocale();
     
	   Application app = facesContext.getApplication();
	   String appBundleName = app.getMessageBundle();
	   ResourceBundle bundle = ResourceBundle.getBundle(appBundleName, locale);
	  
	  String msg = null; 

	  FacesMessage mensagem = new FacesMessage();
	  
		try {

			if (valor <= Double.parseDouble("0.0")){
	
				msg = bundle.getString("erro.maiorQueZero");
					
				mensagem = new FacesMessage(msg);
				
				throw new ValidatorException(mensagem);
				
			}else if(valor > Double.MAX_VALUE){ 
				
				msg = bundle.getString("erro.valorMaiorQuePermetido");
				
				mensagem = new FacesMessage(msg);
				
				throw new ValidatorException(mensagem);
				
			}
		} catch (NumberFormatException e) {

			System.out.println("<<<<<<<<<<<<<<<<<<< Nao farei nada!!! >>>>>>>>>>>>>>>>>>>>>>>>");

		}

	}
}
