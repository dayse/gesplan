 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package actions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import modelo.TipoUsuario;

import actions.controle.SessaoDoUsuario;


public class BaseActions {

	private static final String CAMINHO_BUNDLE_MENSAGENS = "mensagens";
	
	protected SessaoDoUsuario sessaoUsuarioCorrente; 
	
	public BaseActions() {
		sessaoUsuarioCorrente = (SessaoDoUsuario) this.getManagedBean("sessaoDoUsuario");
	}

	protected void inserirObjetoNaSessao(String chave, Object valor) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(chave, valor);
	}

	public Object recuperarObjetoDaSessao(String chave) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(chave);
	}
	
	public void inserirObjetoNaRequest(String chave, Object valor){
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(chave, valor);
	}
	
	public Object recuperarObjetoDaRequest(String chave){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(chave);
	}

	protected void info(String messageKey) {
		
		FacesMessage mensagemSucesso = new FacesMessage();
		mensagemSucesso.setSummary(getMensagemBundled(messageKey));
		mensagemSucesso.setSeverity(FacesMessage.SEVERITY_INFO);
		
		FacesContext.getCurrentInstance().addMessage(null, mensagemSucesso);
	}

	 protected void error(String messageKey) {
		 
		 FacesMessage mensagemErro = new FacesMessage();
		 mensagemErro.setSummary(getMensagemBundled(messageKey));
		 mensagemErro.setSeverity(FacesMessage.SEVERITY_ERROR);
		 
		 FacesContext.getCurrentInstance().addMessage(null, mensagemErro);
	 }
	 
	 protected void warn(String messageKey) {
		 
		 FacesMessage mensagemWarning = new FacesMessage();
		 mensagemWarning.setSummary(getMensagemBundled(messageKey));
		 mensagemWarning.setSeverity(FacesMessage.SEVERITY_WARN);
		 
		 FacesContext.getCurrentInstance().addMessage(null, mensagemWarning);
	 }
	/**
	 * obtem um ManagedBean já instanciado em outro ManagedBean
	 * 
	 * @author  Walanem
	 * 
	 * @param nomeManagedBean  - string com o nome do managedBean definido no facesConfig
	 * @return
	 */
	protected Object getManagedBean(String nomeManagedBean){
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		return context.getELContext().getELResolver().getValue(context.getELContext(), null, nomeManagedBean);
	}
	
	
	protected String getMensagemBundled(String key) {
		return this.getMessageResourceString(CAMINHO_BUNDLE_MENSAGENS, key, null, new Locale("pt-BR"));
	}

	public String getMessageResourceString(String bundleName, String key, Object params[], Locale locale) {

		String text = null;

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "?? mensagem \"" + key + "\" não encontrada ??";
		}

		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}
	
	protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }
}
