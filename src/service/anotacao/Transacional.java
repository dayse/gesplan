 
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
   Os tr�s tipos de anota��es existentes no Java 5 s�o:
   
   - Override
   - Deprecated 
   - SuppressWarnings
   
   A  palavra-chave  @interface  diz  ao   compilador  que 
   estamos definindo uma anota��o em vez de uma  interface
   normal. 
   
   O  target  de uma  anota��o  representa  o  elemento do 
   programa que est� sendo anotado. Uma anota��o pode  ser
   utilizada  para  anotar  um  package,  uma  classe, uma 
   interface, um m�todo, um campo, etc. 
   
   A  reten��o de uma  anota��o significa por quanto tempo
   a informa��o  contida na  anota��o ser� retida. Algumas 
   anota��es s�o  descartadas  pelo compilador e  aparecem 
   apenas   no  c�digo  fonte.  Outras  s�o  compiladas  e 
   armazenadas  no  arquivo .class.  Dentre estas, algumas 
   s�o ignoradas pela m�quina virtual, e outras s�o  lidas
   pela  m�quina  virtual  quando  a  classe  � carregada. 
   Anota��es que s�o  carregadas pela m�quina  virtual s�o 
   vis�veis em tempo  de execu��o e  podem ser pesquisadas
   pela API de reflex�o Java.  
   
   As tr�s possibilidades de reten��o s�o:
   
   RetentionPolicy.SOURCE
   RetentionPolicy.CLASS
   RetentionPolicy.RUNTIME
   
   Diferentemente     de     Override     e    Deprecated, 
   SuppressWarnings  n�o � uma  anota��o de marca��o.  Ela 
   possui  um  �nico membro  denominado value  cujo tipo � 
   String[].  Os  valores desse  membro s�o os  warnings a 
   serem   suprimidos   pelo   compilador.   A    anota��o 
   SuppressWarnings n�o define quais nomes de warnings s�o
   permitidos:   esta    �   uma   responsabilidade    dos 
   implementadores  dos  compiladores.  Para  o compilador 
   javac os nomes s�o: "unchecked" e "fallthrough".
   
   Exemplo:
   
   @SuppressWarnings(value={"unchecked", "fallthrough"})
   
   
*/
