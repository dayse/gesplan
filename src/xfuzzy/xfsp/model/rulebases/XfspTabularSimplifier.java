//--------------------------------------------------------------------------------//
//                               COPYRIGHT NOTICE                                 //
//--------------------------------------------------------------------------------//
// Copyright (c) 2012, Instituto de Microelectronica de Sevilla (IMSE-CNM)        //
//                                                                                //
// All rights reserved.                                                           //
//                                                                                //
// Redistribution and use in source and binary forms, with or without             //
// modification, are permitted provided that the following  conditions are met:   //
//                                                                                //
//     * Redistributions of source code must retain the above copyright notice,   //
//       this list of conditions and the following disclaimer.                    // 
//                                                                                //
//     * Redistributions in binary form must reproduce the above copyright        // 
//       notice, this list of conditions and the following disclaimer in the      //
//       documentation and/or other materials provided with the distribution.     //
//                                                                                //
//     * Neither the name of the IMSE-CNM nor the names of its contributors may   //
//       be used to endorse or promote products derived from this software        //
//       without specific prior written permission.                               //
//                                                                                //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"    //
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE      //
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE // 
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE  //
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL     //
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR     //
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER     //
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  //
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  //
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.           //
//--------------------------------------------------------------------------------//

package xfuzzy.xfsp.model.rulebases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xfuzzy.lang.Rulebase;
import xfuzzy.lang.Specification;
import xfuzzy.lang.Variable;

/**
 * <p> <b>Descripción:</b> simplificador de bases de reglas mediante un método
 * tabular similar al de Quine-McCluskey.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 5.0
 * @see XfspModel
 * @see IXfspRulebaseSimplifier
 *
 */
public class XfspTabularSimplifier
    implements IXfspRulebaseSimplifier {

  //indica si se deben mantener las funciones de pertenencia situadas en los
  //extremos del universo de discurso de cada variable
  private boolean maintainExtremeMF;

  /**
   * <p> <b>Descripción:</b> crea un objeto que simplifica bases de reglas
   * mediante un método tabular basado en el método de Quine-McCluskey para
   * simplificación de funciones de conmutación del álgebra booleana.
   * @param maintainExtremeMF Si vale cierto se mantendrán las funciones de
   * pertenencia situadas en los extremos del universo de discurso de cada tipo
   * tras realizar la simplificación de bases de reglas y, en caso contrario,
   * no se mantendrán.
   *
   */
  public XfspTabularSimplifier(boolean maintainExtremeMF){
    //establece si se deben mantener las funciones de pertenencia situadas en
    //los extremos de los tipos
    this.maintainExtremeMF = maintainExtremeMF;
  }

  /**
   * <p> <b>Descripción:</b> simplifica una base de reglas de un sistem difuso
   * mediante métodos tabulares.
   * @param rulebase Base de reglas que se quiere simplificar.
   * @param spec Especificación del sistema cuya base de reglas se va a
   * simplificar.
   *
   */
  public void simplify(Rulebase rulebase, Specification spec) {


    //System.out.println("Base de reglas original");
    //System.out.println(rulebase.toXfl());



    //crea un objeto capaz de transformar una base de reglas en un conjunto de
    //mintérminos
    XfspMintermDirector director = new XfspMintermDirector();
    //crea un conjunto de mintérminos correspondiente a la base de reglas que
    //se quiere simplificar
    Map minterms = director.build(rulebase);



    /*
    System.out.println("Mintérminos iniciales:");
    Iterator itaux = minterms.values().iterator();
    while(itaux.hasNext()){
      List l = (List) itaux.next();
      Set s = (Set) l.get(0);
      Iterator itaux2 = s.iterator();
      while(itaux2.hasNext()){
        List l2 = (List) itaux2.next();
        Iterator itaux3 = l2.iterator();
        while(itaux3.hasNext()){
          Set s2 =  (Set) itaux3.next();
          Iterator itaux4 = s2.iterator();
          while(itaux4.hasNext()){
            System.out.println((XfspMinterm)itaux4.next());
          }
        }
      }
    }*/





    //iterador para el conjunto de mintérminos equivalente a la base de reglas
    Iterator it = minterms.values().iterator();
    //mientras queden mintérminos para una determinada conclusión por
    //procesar...
    while (it.hasNext()) {
      //...obtiene la lista de iteraciones para una conclusión
      List l = (List) it.next();
      boolean end = false;

      while (!end) {
        end = true;
        //obtiene la última iteración que se ha creado
        Set currentIteration = (Set) l.get(l.size() - 1);
        //inicializa la siguiente iteración como un conjunto vacío
        Set nextIteration = new HashSet();
        //iterador para la iteración actual
        Iterator it1 = currentIteration.iterator();
        //mientras queden mintérminos agrupables en la iteración actual...

        while (it1.hasNext()) {
          //...obtiene la lista de mintérminos agrupables de la iteración actual
          List aux = (List) it1.next();
          //reduce la lista de mintérminos agrupables de la iteración actual
          //(si es posible)
          List newList = reduce(aux, rulebase.getInputs());
          //si se ha podido reducir la lista de mintérminos agrupables de la
          //iteración actual...
          if (!newList.isEmpty()) {
            //...continúa iterando
            end = false;
          }
          //añade a la siguiente iteración el resultado de reducir la iteración
          //actual
          nextIteration.add(newList);
        }
        //si se ha añadido alguna lista reducida no vacía a la siguiente
        //iteración...
        if (!end) {
          //...añade la siguiente iteración a la lista de iteraciones
          l.add(nextIteration);
        }
      }
    }
    //crea un objeto capaz de encontrar el cubrimiento mínimo para un conjunto
    //de mintérminos asociados a una conclusión
    XfspMinimalCoveringFinder finder = new XfspMinimalCoveringFinder();
    //busca el cubrimiento mínimo para el conjunto de mintérminos
    Map minimalCovering = finder.findMinimalCovering(minterms);
    //crea un objeto que construye bases de reglas a partir de un cubrimiento
    //mínimo de un conjunto de mintérminos
    XfspRulebaseTabularDirector td = new XfspRulebaseTabularDirector();



    //nuevo
    td.setMaintainExtremeMF(this.maintainExtremeMF);




    //crea la nueva base de reglas a partir del cubrimiento mínimo
    Rulebase newRulebase = td.build(minimalCovering, rulebase);
    //cambia en la especificación del sistema la base de reglas original por la
    //nueva
    spec.exchangeRulebase(rulebase, newRulebase);
    //quita todas las reglas de la base de reglas original...
    rulebase.removeAllRules();
    //...y le añade todas las reglas de la nueva base de reglas
    for (int i = 0; i < newRulebase.getRules().length; i++) {
      rulebase.addRule(newRulebase.getRules()[i]);
    }
  }

  /**
   * <p> <b>Descripción:</b> reduce una lista cuyos elementos son conjuntos de
   * mintérminos produciendo una nueva lista cuyos elementos son conjuntos de
   * tal forma que cada conjunto de la nueva lista es resultado de las uniones
   * de mintérminos de conjuntos adyacentes en la lista original.
   * @param oldMinterms Lista de conjuntos de mintérminos de tal forma que dos
   * conjuntos adyacentes en la lista contiene mintérminos posiblemente
   * agrupables.
   * @param vars Variables de entrada del sistema al que pertenecen los
   * mintérminos de la lista original.
   *
   */
  private List reduce(List oldMinterms, Variable[] vars) {
    //almacenan mintérminos consecutivos de la lista de mintérminos que se
    //quiere simplificar
    XfspMinterm mt1, mt2;
    //nueva lista resultado de reducir la lista de mintérminos original
    List result = new ArrayList();
    //para toda pareja de conjuntos de mintérminos agrupables (consecutivos) de
    //la lista original...
    for (int i = 0; i < (oldMinterms.size() - 1); i++) {
      //...toma una nueva pareja de conjuntos de mintérminos agrupables
      Set s1 = (Set) oldMinterms.get(i);
      Set s2 = (Set) oldMinterms.get(i + 1);
      //conjunto resultado de la fusión de aquellas parejas de mintérminos (uno
      //de cada conjunto) que se puedan fusionar
      Set merged = new HashSet();
      //iterador que permite recorrer todos los mintérminos del primer conjunto
      Iterator it1 = s1.iterator();
      //para todos los mintérminos del primer conjunto...
      while (it1.hasNext()) {
        //...obtiene un nuevo mintérmino del primer conjunto
        mt1 = (XfspMinterm) it1.next();
        //iterador que permite recorre todos los mintérminos del segundo
        //conjunto
        Iterator it2 = s2.iterator();
        //para todos los mintérminos del segundo conjunto...
        while (it2.hasNext()) {
          //...obtiene un nuevo mintérmino del segundo conjunto
          mt2 = (XfspMinterm) it2.next();
          //examina los dos mintérminos y determina si se pueden agrupar en
          //relación a alguna de sus variables
          Variable var = canBeMerged(mt1, mt2, vars);
          //si los mintérminos pueden ser fusionados...
          if (var != null) {
            //...comprueba que el mintérmino resultante de la fusión de los dos
            //anteriores no estaba ya en el conjunto y, si es el caso, lo añade
            //al mismo (esto se hace porque el conjunto "merged" no comprueba
            //correctamente que los elementos nuevos a añadir no estuvieran
            //antes en dicho conjunto, cosa que debería hacer)
            boolean found = false;
            Iterator it = merged.iterator();
            while (it.hasNext()) {
              XfspMinterm aux = (XfspMinterm) it.next();
              if (aux.equals(merge(mt1, mt2, var))) {
                found = true;
              }
            }
            //si el resultado de la fusión no se encontraba ya en el conjunto
            //de mintérminos fusionados...
            if (!found) {
              //...lo añade a dicho conjunto
              merged.add(merge(mt1, mt2, var));
            }
          }
        }
        //si se ha podido fusionar alguna pareja de mintérminos...
        if (!merged.isEmpty()) {
          //...se añade el conjunto de mintérminos fusionados a la lista que
          //corresponde a la reducción de la lista original
          result.add(merged);
        }
      }
    }
    //devuelve el resultado de reducir la lista de mintérminos originales
    return result;
  }

  /**
   * <p> <b>Descripción:</b> comprueba si dos mintérminos o implicantes pueden
   * ser reducidas en un nuevo implicante.
   * @param oldMinterms Lista de conjuntos de mintérminos de tal forma que dos
   * conjuntos adyacentes en la lista contiene mintérminos posiblemente
   * agrupables.
   * @param vars Variables de entrada del sistema al que pertenecen los
   * mintérminos de la lista original.
   *
   */
  private Variable canBeMerged(XfspMinterm mt1, XfspMinterm mt2,
                               Variable[] vars) {
    //map cuyas claves son las variables del primer mintérmino y cuyos datos
    //son las funciones de pertenencia que pueden tomar como valor dichas
    //variables
    Map mp1 = mt1.getVariables();
    //map cuyas claves son las variables del segundo mintérmino y cuyos datos
    //son las funciones de pertenencia que pueden tomar como valor dichas
    //variables
    Map mp2 = mt2.getVariables();
    //indica si se han encontrado dos conjuntos de funciones de pertenencia que
    //se diferencien sólo en un elemento
    boolean found = false;



    boolean end = false;



    Variable var = null;








    //versión nueva
    //para todas las variables de entrada del sistema...
    for(int i=0;i<vars.length & !end;i++){
      //...obtiene las funciones de pertenencia asignadas a dicha variable en
      //el primer mintermino...
      Set s1 = (Set) mp1.get(vars[i]);
      //...y en el segundo mintérmino
      Set s2 = (Set) mp2.get(vars[i]);





/*
      System.out.println("Comparamos los conjuntos:");
      System.out.println(s1);
      System.out.println(s2);
*/






      //si la variable actual tiene asignado un conjunto de funciones de
      //pertenencia distinto en ambos mintérminos y todavía no se ha encontrado
      //ninguna variable con asignaciones distintas hasta ahora...
      if(!s1.equals(s2) & !found){
        //...comprueba que las asignaciones en ambos mintérminos sólo se
        //diferencien en un elemento...
        if (differenceOne( (Set) mp1.get(vars[i]), (Set) mp2.get(vars[i]))) {
          //...y en ese caso ya se ha encontrado la variable por la que se
          //pueden fusionar ambos mintérminos
          found = true;
          var = vars[i];
        }



        //nuevo!!!!!
        else{
          var = null;
          end = true;
        }



      }
      //si la variable actual tiene asignado un conjunto de funciones de
      //pertenencia distinto en ambos mintérminos y pero ya se ha encontrado
      //alguna variable con asignaciones distintas...
      else if(!s1.equals(s2) & found){
        //...los dos mintérminos no se podrán fusionar por diferenciarse en
        //más de una variable
        var =  null;
      }
    }







    //versión antigua
    //para todas las variables de entrada del sistema...
    /*
    for (int i = 0; i < vars.length && !found; i++) {
      Variable aux = vars[i];
      Set s1 = new HashSet();
      Set s2 = new HashSet();
      //iterador que permite recorrer todas las variables del primer map
      Iterator it = mp1.keySet().iterator();
      //para todas las variables del primer map...
      while (it.hasNext()) {
        //...obtiene una variable
        Variable current = (Variable) it.next();
        //si dicha variable no coincide con la variable de entrada que se está
        //examinando...
        if (!current.equals(aux.getName())) {
          //...la añade al primer conjunto
          s1.add(mp1.get(current));
        }
      }
      //iterador que permite recorrer todas las variables del segundo map
      it = mp2.keySet().iterator();
      //para todas las variables del segundo map...
      while (it.hasNext()) {
        //...obtiene una variable
        Variable current = (Variable) it.next();
        //si dicha variable no coincide con la variable de entrada que se está
        //examinando...
        if (!current.equals(aux.getName())) {
          //...la añade al segundo conjunto
          s2.add(mp2.get(current));
        }
      }
      //si ambos conjuntos contienen las mismas variables (esta comprobación es
      //trivial)...
      if (s1.equals(s2)) {
        //...comprueba que el conjunto de funciones de pertenencia que pueden
        //tomar las variables de ambos conjuntos se diferencian en sólo una
        //variable...
        if (differenceOne( (Set) mp1.get(aux), (Set) mp2.get(aux))) { //true){
          //...y en ese caso ya se ha encontrado la variable por la que se
          //pueden fusionar ambos mintérminos
          found = true;
          var = aux;
        }
      }
    }
    */










    if(var!=null){



      boolean prueba = differenceOneBis( (Set) mp1.get(var), (Set) mp2.get(var))==differenceOne( (Set) mp1.get(var), (Set) mp2.get(var));
      if(prueba == false){
        System.out.println("****************************** OJO ***********************************");
      System.out.println("Se pueden fusionar" + mt1 + " y " + mt2 + " en " + var);
        System.out.println("y el método difference one para " + var + " devuelve " + prueba);
      System.out.println("**********************************************************************");
      }

    }/*
    else{
      System.out.println("No se pueden fusionar" + mt1 + " y " + mt2);
    }
    */






    //devuelve la variable de entrada por la que se pueden fusionar ambos
    //mintérminos
    return var;
  }







//quitar (temporal)
  private boolean differenceOneBis(Set s1, Set s2) {
    //clona el primer conjunto
    Set s1Clone = (Set) ( (HashSet) s1).clone();
    //clona el segundo conjunto
    Set s2Clone = (Set) ( (HashSet) s2).clone();
    //elimina todos los elementos del segundo conjunto
    s1Clone.retainAll(s2);
    //elimina todos los elementos del primer conjunto
    s2Clone.retainAll(s1);
    //la diferencia entre los dos conjuntos es uno si, y sólo si, tras quitar
    //los elementos del conjunto contrario, quedan los mismos elementos que
    //tenía al principio excepto uno



    boolean result = false;

    if(s1.size()>1 && s2.size()>1){
      result = (s1Clone.size() == (s1.size() - 1)) &&
          (s2Clone.size() == (s2.size() - 1));
    }else{
      Iterator it = s1.iterator();
      Integer i1 = (Integer) it.next();
      it = s2.iterator();
      Integer  i2 = (Integer) it.next();
      result = i1.equals(new Integer(i2.intValue()-1)) || i2.equals(new Integer(i1.intValue()-1));
    }
    return result;
  }




















  /**
   * <p> <b>Descripción:</b> comprueba si los conjuntos de funciones de
   * pertenencia asociados a una variable de dos mintérmino se diferencia en
   * sólo un elemento.
   * @param s1 Primer conjunto de enteros que representa las funciones de
   * pertenencia asociadas a una variable en un mintérmino.
   * @param s2 Segundo conjunto de enteros que representa las funciones de
   * pertenencia asociadas a una variable en un mintérmino.
   *
   */
  private boolean differenceOne(Set s1, Set s2) {
    //clona el primer conjunto
    Set s1Clone = (Set) ( (HashSet) s1).clone();
    //clona el segundo conjunto
    Set s2Clone = (Set) ( (HashSet) s2).clone();
    //elimina todos los elementos del segundo conjunto
    s1Clone.retainAll(s2);
    //elimina todos los elementos del primer conjunto
    s2Clone.retainAll(s1);
    //la diferencia entre los dos conjuntos es uno si, y sólo si, tras quitar
    //los elementos del conjunto contrario, quedan los mismos elementos que
    //tenía al principio excepto uno




    //obtiene los elementos extremos de ambos conjuntos
    int m1Min = findMin(s1);
    int m1Max = findMax(s1);
    int m2Min = findMin(s2);
    int m2Max = findMax(s2);
    //comprueba que los extremos de ambos conjuntos sólo se diferencian en una
    //unidad
    boolean extremeCond = Math.abs(m1Min-m2Min) == 1 && Math.abs(m1Max-m2Max) == 1;
        //((m1Min==m2Min-1)&&(m1Max==m2Max-1)) || (m1Min==m2Min+1 && m1Max==m2Max+1));


    return (s1Clone.size() == (s1.size() - 1)) &&
        (s2Clone.size() == (s2.size() - 1)) && extremeCond;
  }

  /**
   * <p> <b>Descripción:</b> mezcla dos mintérminos uniendo los conjuntos de
   * funciones de pertenencia asociados a una determinada variable.
   * @param mt1 Primer mintérmino que se fusionará.
   * @param mt2 Segundo mintérmino que se fusionará.
   * @param var Variable cuyas funciones de pertenencia asociadas en los dos
   * mintérminos anteriores serán unidas en el nuevo mintérmino.
   * @return Devuelve un nuevo mintérmino similar a los dos anteriores excepto
   * para la variable indicada, en cuyo caso se le asignará la unión de las
   * funciones de pertenencia asociadas a dicha variable en los dos mintérminos
   * originales.
   *
   */
  private XfspMinterm merge(XfspMinterm mt1, XfspMinterm mt2, Variable var) {
    //resultado de la fusión de ambos mintérminos
    XfspMinterm result = new XfspMinterm(false);
    //map con las variables del primer mintérmino y las funciones de
    //pertenencia que dichas variables pueden tomar
    Map var1 = (Map) ( (HashMap) mt1.getVariables()).clone();
    //map con las variables del segundo mintérmino y las funciones de
    //pertenencia que dichas variables pueden tomar
    Map var2 = (Map) ( (HashMap) mt2.getVariables()).clone();
    //conjunto que contendrá las funciones de pertenencia que puede tomar la
    //variable que se pasa como parámetros en el primer mintérmino
    Set s1 = null;
    //iterador que permite recorrer todas las variables del primer mintérmino
    Iterator it = var1.keySet().iterator();
    //añade a s1 las funciones de pertenencia que puede tomar la variable var
    //en el primer mintérmino
    while (it.hasNext()) {
      Variable aux = (Variable) it.next();
      if (aux.equals(var.getName())) {
        s1 = (Set) var1.get(aux);
      }
    }
    //conjunto que contendrá las funciones de pertenencia que puede tomar la
    //variable que se pasa como parámetros en el segundo mintérmino
    Set s2 = null;
    //iterador que permite recorrer todas las variables del segundo mintérmino
    it = var2.keySet().iterator();
    //añade a s2 las funciones de pertenencia que puede tomar la variable var
    //en el segundo mintérmino
    boolean found = false;
    while (it.hasNext() && !found) {
      Variable aux = (Variable) it.next();
      if (aux.equals(var.getName())) {
        s2 = (Set) var2.get(aux);
        found = true;
      }
    }
    //establece la conclusión del mintermino resultado de fusionar los dos
    //mintérminos originales
    result.setConclusion(mt1.getConclusion());
    //establece las variables del resultado de la fusión y les da como
    //asignación inicial la misma que tenían en el primer mintérmino
    result.setVariables( (Map) ( (HashMap) mt1.getVariables()).clone());
    //conjunto que almacenará las funciones de pertenencia que puede tomar la
    //variable var tanto en el primer como en el segundo mintérmino
    Set s3 = new HashSet();
    s3.addAll(s1);
    s3.addAll(s2);
    //iterador que permite recorrer las variables del resultado de la fusión
    it = result.getVariables().keySet().iterator();
    //busca en el resultado de la fusión...
    found = false;
    while (it.hasNext() && !found) {
      Variable aux = (Variable) it.next();
      //...hasta que encuentra la variable por la que se tienen que fusionar
      //los dos mintérminos...
      if (aux.equals(var.getName())) {
        //...en cuyo caso sustituye las funciones de pertenencia que puede
        //tomar por la unión de las funciones de pertenencia que puede tomar en
        //ambos mintérminos
        result.getVariables().put(aux, s3);
        found = true;
      }
    }
    //el resultado de la fusión cubrirá los mintérminos que cubría el primero...
    result.getCoveredMinterms().addAll(mt1.getCoveredMinterms());
    //...así como los que cubría el segundo
    result.getCoveredMinterms().addAll(mt2.getCoveredMinterms());








/*
    System.out.println("El resultado de la fusión de " + mt1 + " y " + mt2);
    System.out.println("es " + result);
    System.out.println("y cubre los mintérminos:");
    Iterator it1 = result.getCoveredMinterms().iterator();
    while(it1.hasNext()){
      XfspMinterm aux = (XfspMinterm) it1.next();
      System.out.println(aux);
    }
*/






    //devuelve la fusión de ambos mintérminos
    return result;
  }

  /**
   * <p> <b>Descripción:</b> busca el elemento mínimo de un conjunto de enteros.
   * @param s Conjunto no vacío de objetos de tipo Integer.
   * @return Menor elemento contenido en el conjunto s.
   *
   */
  private int findMin(Set s){
    Iterator it = s.iterator();
    int result = ((Integer) it.next()).intValue();
    while(it.hasNext()){
      int aux = ((Integer) it.next()).intValue();
      if(aux<result){
        result = aux;
      }
    }
    return result;
  }

  /**
   * <p> <b>Descripción:</b> busca el elemento máximo de un conjunto de enteros.
   * @param s Conjunto no vacío de objetos de tipo Integer.
   * @return Mayor elemento contenido en el conjunto s.
   *
   */
  private int findMax(Set s){
    Iterator it = s.iterator();
    int result = ((Integer) it.next()).intValue();
    while(it.hasNext()){
      int aux = ((Integer) it.next()).intValue();
      if(aux>result){
        result = aux;
      }
    }
    return result;
  }
}
