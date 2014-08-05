 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
/**
 * 
 */
package conversores;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
//import javax.faces.component.UIViewRoot;
//import java.util.Locale;

import util.Utilitario;

/**
 * @author marques
 *
 */
public class ConversorDouble implements Converter {

	/**
	 * 
	 */
	public ConversorDouble() {
		
			System.out.println(">>>>>>>> Instanciou o ConversorDouble");
	}

	
	
	public Object getAsObject(FacesContext contexto, 
                              UIComponent componente, 
                              String novoValor) 
		throws ConverterException
	{
		System.out.println(">>>>>>>>>>>>>>>>>>> Executou getAsObject de ConversorDouble");

		try
		{	Double valor = null;
		
			if (novoValor != null && novoValor.trim().length() > 0)
			{	
			System.out.println(">>>>>>>>>>>>>>>>>>> Executou getAsObject de ConversorDouble <<<<<<<<<<<<<<<<<<<<<<");	
				valor = Utilitario.strToDouble(novoValor);
			}

			return valor;
		}
		catch(Exception e)
		{	
			//	recuperando o texto da mensagem a partir do message bundle
			FacesContext context = FacesContext.getCurrentInstance();

			Application app = context.getApplication();
			String appBundleName = app.getMessageBundle();

			ResourceBundle bundle = ResourceBundle.getBundle(appBundleName);
			String msg = bundle.getString("erroConversaoDouble");
			
			String campo = bundle.getString(componente.getId());
			campo = "'"+campo+"'";

			Object[] params = {campo};
			MessageFormat formatter = new MessageFormat(msg);
			String texto = formatter.format(params);
						
			FacesMessage mensagem = new FacesMessage(texto);

			throw new ConverterException(mensagem);
		}
		
  }

	public String getAsString(FacesContext contexto, 
			                  UIComponent componente, 
			                  Object valor) 
		throws ConverterException
	{	System.out.println(">>>>>>>>>>>>>>>>>>> Executou getAsString de ConversorDouble");

		return Utilitario.doubleToStr((Double)valor);	
	}
}
