 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service.anotacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface Transacional 
{}

/*
   Os três tipos de anotações existentes no Java 5 são:
   
   - Override
   - Deprecated 
   - SuppressWarnings
   
   A  palavra-chave  @interface  diz  ao   compilador  que 
   estamos definindo uma anotação em vez de uma  interface
   normal. 
   
   O  target  de uma  anotação  representa  o  elemento do 
   programa que está sendo anotado. Uma anotação pode  ser
   utilizada  para  anotar  um  package,  uma  classe, uma 
   interface, um método, um campo, etc. 
   
   A  retenção de uma  anotação significa por quanto tempo
   a informação  contida na  anotação será retida. Algumas 
   anotações são  descartadas  pelo compilador e  aparecem 
   apenas   no  código  fonte.  Outras  são  compiladas  e 
   armazenadas  no  arquivo .class.  Dentre estas, algumas 
   são ignoradas pela máquina virtual, e outras são  lidas
   pela  máquina  virtual  quando  a  classe  é carregada. 
   Anotações que são  carregadas pela máquina  virtual são 
   visíveis em tempo  de execução e  podem ser pesquisadas
   pela API de reflexão Java.  
   
   As três possibilidades de retenção são:
   
   RetentionPolicy.SOURCE
   RetentionPolicy.CLASS
   RetentionPolicy.RUNTIME
   
   Diferentemente     de     Override     e    Deprecated, 
   SuppressWarnings  não é uma  anotação de marcação.  Ela 
   possui  um  único membro  denominado value  cujo tipo é 
   String[].  Os  valores desse  membro são os  warnings a 
   serem   suprimidos   pelo   compilador.   A    anotação 
   SuppressWarnings não define quais nomes de warnings são
   permitidos:   esta    é   uma   responsabilidade    dos 
   implementadores  dos  compiladores.  Para  o compilador 
   javac os nomes são: "unchecked" e "fallthrough".
   
   Exemplo:
   
   @SuppressWarnings(value={"unchecked", "fallthrough"})
   
   
*/
