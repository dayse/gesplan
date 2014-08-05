 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service.controleTransacao;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;

public class FabricaDeAppService 
{	
	@SuppressWarnings("unchecked")
	private static Map<Class, Object> map = new HashMap<Class, Object>();
	
	@SuppressWarnings("unchecked")
	public static <T> T getAppService(Class classeDoService) 
		throws Exception 
    {

		T appService = (T)map.get(classeDoService);
		
		if(appService == null)
		{	
			// O valor de T ser� designado ap�s a execu��o do m�todo Enhancer.create()
			appService = (T)Enhancer.create (classeDoService, new InterceptadorDeAppService());
			map.put(classeDoService, appService);
		}

		return appService;

		/*
			A  classe  Enhancer  gera  subclasses  din�micas para habilitar a 
			intercepta��o de m�todos.  
			  
			A subclasse  gerada dinamicamente  faz o override dos m�todos n�o 
			"finais"   da  superclasse  (ProdutoAppService,  por exemplo)   e
			insere  chamadas que  executam  implementa��es de interceptadores 
			definidos pelo usu�rio. 

			O original e mais geral  tipo de callback � o  MethodInterceptor. 
			MethodInterceptor   �   uma  interface   que   possui  um  m�todo 
			denominado 
                        
                 intercept(java.lang.Object obj,
                           java.lang.reflect.Method method,
                           java.lang.Object[] args,
                           MethodProxy proxy)

            Durante   a   execu��o   do  m�todo   intercept()  acima  pode-se
            invocar  c�digo  customizado  antes  e  ap�s  a chamada do m�todo 
            "super".  Al�m  disso,  �  poss�vel  modificar  os  valores   dos 
            argumentos  antes  de  chamar o m�todo  "super", ou at� mesmo n�o 
            cham�-lo.
            
            Embora a  inteface  MethodInterceptor  seja gen�rica o suficiente 
            para   fazer  o   que  �  necess�rio   em  qualquer  situa��o  de 
            intercepta��o, ela  frequentemente � exagerada.  Por simplicidade
            e  desempenho,  outros  tipos de callback  especializados (como o 
            LazyLoader)  tamb�m est�o  dispon�veis.  Frequentemente  um �nico
            callback ser�  utilizado  por classe  "enhanced", mas �  poss�vel 
            controlar qual  callback  est�  sendo  utilizado  (utilizando  um 
            CallbackFilter) a cada execu��o de m�todo.  
		*/    
    }
}
