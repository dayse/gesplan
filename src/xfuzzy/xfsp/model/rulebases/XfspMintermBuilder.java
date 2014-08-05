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

import xfuzzy.lang.Conclusion;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.Relation;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Variable;

/**
 * <p> <b>Descripción:</b> construye un conjunto de mintérminos obtenidos a
 * partir de las reglas de una base de regla que contiene solamente reglas con
 * conjunciones en su premisa.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.7
 * @see XfspModel
 * @see XfspTabularSimplifier
 * @see XfspMintermDirector
 *
 */
public class XfspMintermBuilder {

  //variables de entrada de la base de reglas a simplificar
  private Variable[] inputvars;

  //mintérminos que ya han sido procesados por el constructor
  private Map minterms;

  //almacena las variables utilizadas por la regla que se está procesando
  private Map usedVars;

  //almacena los mintérminos generados por la regla que se está procesando
  private Set currentMinterms;

  /**
   * <p> <b>Descripción:</b> crea un objeto capaz de construir el conjunto de
   * mintérminos que representan una base de reglas.
   *
   */
  public XfspMintermBuilder() {
    minterms = new HashMap();
    usedVars = new HashMap();
    currentMinterms = new HashSet();
  }

  /**
   * <p> <b>Descripción:</b> establece el conjunto de variables de entrada del
   * mintérmino que se está procesando.
   * @param inputvars Vector de variables correspondiente a las variables de
   * entrada del mintérmino procesado.
   *
   */
  public void setInputvars(Variable[] inputvars) {
    this.inputvars = inputvars;
  }

  /**
   * <p> <b>Descripción:</b> devuelve el conjunto de mintérminos que se ha ido
   * construyendo al tratar cada una de las reglas que se le han pasado.
   * @return Conjunto de mintérminos construido de forma incremental.
   *
   */
  public Map getMinterms() {
    return minterms;
  }

  /**
   * <p> <b>Descripción:</b> construye un conjunto de mintérminos que
   * representa a la regla que se le pasa como parámetro.
   * @param rule Regla para la que se quiere construir el conjunto de
   * mintérminos que la representa.
   *
   */
  public void buildRule(Rule rule) {

    //System.out.println("Número de variables de entrada del sistema " + inputvars.length);
    //System.out.println("Analizando la regla " + rule.toXfl());


    //conjunto de mintérminos que representan a la regla que se quiere tratar
    currentMinterms = new HashSet();
    //inicializam un nuevo mintérmino...
    XfspMinterm currentMinterm = new XfspMinterm();

    //currentMinterm.setVariables(new HashMap());





    //indicam que todavía no hemos utilizado ninguna de las variables de
    //entrada de la base de reglas
    for (int i = 0; i < inputvars.length; i++) {
      usedVars.put(inputvars[i], new Boolean(false));
    }
    //añade al conjunto de mintérminos de la regla actual el inicial
    currentMinterms.add(currentMinterm);
    //actualiza todos los mintérminos de la regla actual para que contemplen
    //la premisa de dicha regla...
    constructRelation(rule.getPremise());
    //...y las variables que no aparecen dicha regla
    addNonUsedVars();
    //obtiene las conclusiones de la regla actual
    Conclusion[] conc = rule.getConclusions();
    //añade a los mintérminos de la regla actual las conclusiones de la misma
    currentMinterms = addConclusions(currentMinterms, conc);
    //distrubuye los mintérminos generados por la regla actual entre los grupos
    //de mintérminos que ya se había preparado para las reglas anteriores
    sortMinterms();
    //para cada una de las conclusiones de la regla tratada...
    for (int i = 0; i < conc.length; i++) {
      //...obtiene la lista de iteraciones asociadas
      List l = (List) minterms.get(conc[i]);
      //si pudo obtener dicha lista...
      if (l != null) {
        //...toma el conjunto con los mintérminos agrupables para la primera
        //iteración...
        Set s = (Set) l.get(0);
        //...y fusiona aquellas listas de mintérminos agrupables que pueda
        this.mergeMintermsLists(s);
      }
    }


        //TODO - quitar esto
        //System.out.print("*******************************\nTratando la regla " + rule.toXfl());
        //System.out.println("mintérminos construidos:");
        //Iterator it = currentMinterms.iterator();
        //while(it.hasNext()){
          //System.out.println(it.next());
        //}


  }

  /**
   * <p> <b>Descripción:</b> añade conclusiones a un conjunto de mintérminos o
   * implicantes.
   * @param currentMinterms Conjunto de mintérminos o implicantes a los que se
   * quiere conclusiones.
   * @param conc Conjunto de conclusiones que se quieren añadir a los
   * mintérminos o implicantes seleccionados.
   * @return Conjunto de mintérminos o implicantes iguales a los originales
   * a los que se han añadido las conclusiones deseadas.
   *
   */
  private Set addConclusions(Set currentMinterms, Conclusion[] conc) {
    //resultado de añadir las conclusiones a los mintérminos o implicantes
    Set result = new HashSet();
    //para todas las conclusiones que se quieren añadir...
    for (int i = 0; i < conc.length; i++) {
      //...toma una conclusión
      Conclusion aux = conc[i];
      //obtiene un iterador para el conjunto de mintérminos o implicantes
      Iterator it = currentMinterms.iterator();
      //para todos los mintérminos o implicantes...
      while (it.hasNext()) {
        //...obtiene un nuevo mintérmino...
        XfspMinterm minterm = (XfspMinterm) it.next();
        //...le añade la conclusión actual...
        minterm.setConclusion(aux);
        //...y lo añade al conjunto de mintérminos que se devuelve como
        //resultado
        result.add(minterm);
      }
    }
    return result;
  }

  /**
   * <p> <b>Descripción:</b> añade conclusiones a un conjunto de mintérminos o
   * implicantes.
   * @param currentMinterms Conjunto de mintérminos o implicantes a los que se
   * quiere conclusiones.
   * @param conc Conjunto de conclusiones que se quieren añadir a los
   * mintérminos o implicantes seleccionados.
   * @return Conjunto de mintérminos o implicantes iguales a los originales
   * a los que se han añadido las conclusiones deseadas.
   *
   */
  private void addNonUsedVars() {
    //para todas las variables de entrada...
    for (int i = 0; i < inputvars.length; i++) {
      //...crea un nuevo conjunto
      Set newset = new HashSet();
      //si la variable de entrada actual no ha sido utilizada hasta el
      //momento...
      if (! ( (Boolean) usedVars.get(inputvars[i])).booleanValue()) {
        //...crea un iterador que permite recorrer el conjunto de mintérminos
        //actuales
        Iterator it = currentMinterms.iterator();
        //para todos los mintérminos...
        while (it.hasNext()) {
          //...obtiene un mintérmino del conjunto de mintérminos actuales
          XfspMinterm currentMinterm = (XfspMinterm) it.next();
          //para todas las funciones de pertenencia del tipo al que pertenence
          //la variable de entrada actual...
          for (int j = 0;
               j < inputvars[i].getType().getMembershipFunctions().length; j++) {
            //...crea un nuevo mintérmino
            XfspMinterm auxMinterm = new XfspMinterm();
            //obtiene un clon de las asignaciones de funciones de pertenencia
            //a las variables para el mintérmino actual
            Map aux = (Map) ( (HashMap) currentMinterm.getVariables()).clone();
            //crea un nuevo conjunto...
            Set s = new HashSet();
            //...y le añade la función de pertenencia actual
            s.add(new Integer(j));
            //asigna a la variable de entrada actual el conjunto de funciones de
            //pertenencia creado
            aux.put(inputvars[i], s);
            //añade al nuevo mintérmino la nueva asignación de funciones de
            //pertenencia creada
            auxMinterm.setVariables(aux);
            //añade al nuevo conjunto el nuevo mintérmino creado
            newset.add(auxMinterm);
          }
        }
        //establece el nuevo conjunto de mintérminos para la regla que se está
        //procesando
        currentMinterms = newset;
      }
    }
  }

  /**
   * <p> <b>Descripción:</b> busca una función de pertenencia en un vector de
   * funciones de pertenencia.
   * @param mfs Vector de funciones de pertenencia donde se buscará una función
   * de pertenencia.
   * @param mf Función de pertenencia que se buscará en un vector de funciones
   * de pertenencia.
   * @return Devuelve la posición que la función de pertenencia a buscar ocupa
   * en el vector de funciones de pertenencia o -1 si dicha función de
   * pertenencia no se encuentra en el vector.
   *
   */
  private int search(LinguisticLabel[] mfs, LinguisticLabel mf) {
    //posición ocupada por la función de pertenencia buscada (se inicializa a
    //-1)
    int pos = -1;
    //guarda que permite salir del bucle de búsqueda cuando se encuentre la
    //función de pertenencia buscada
    boolean found = false;
    //para todas las funciones de pertenencia del vector mientras no se haya
    //encontrado la función de pertenencia buscada...
    for (int i = 0; i < mfs.length && !found; i++) {
      //...compara la función de pertenencia actual con la buscada y si
      //coinciden...
      if (mfs[i].equals(mf)) {
        //...almacena la posición ocupada por dicha función de pertenencia en el
        //vector...
        pos = i;
        //...e indica que se debe salir del bucle
        found = true;
      }
    }
    return pos;
  }

  /**
   * <p> <b>Descripción:</b> construye los mintérminos adecuados según la
   * relación de la premisa de la regla tratada o de las relaciones incluidas
   * dentro de otra relación.
   * @param rel Relación de tipo IS o AND para la que se obtendrá un conjunto
   * de mintérminos para la regla que se está tratando.
   *
   */
  private void constructRelation(Relation rel) {
    //si la relación es de tipo IS...
    if (rel.getKind() == Relation.IS) {
      //...crea un iterador que permite recorrer el conjunto de mintérminos
      //actual
      Iterator it = currentMinterms.iterator();
      //para todos los mintérminos del conjunto actual...
      while (it.hasNext()) {
        //...crea un nuevo conjunto...
        Set s = new HashSet();
        //...y le un entero con la posición ocupada por la función de
        //pertenencia asignada en la relación IS dentro del conjunto de
        //funciones de pertenencia del tipo al que pertenece la variable de la
        //relación tratada
        s.add(new
              Integer(search(rel.getVariable().getType().getMembershipFunctions(),
                             rel.getMembershipFunction())));
        //añade al mintérmino actual una asignación para la variable de la
        //relación con la función de pertenencia de dicha relación
        ( (XfspMinterm) it.next()).getVariables().put(rel.getVariable(), s);
      }
      //indica que la variable de la relación ha sido utilizada
      usedVars.put(rel.getVariable(), new Boolean(true));
    }
    //si la relación es de tipo AND...
    else if (rel.getKind() == Relation.AND) {
      //...construye los mintérminos para el término izquierdo de la relación...
      constructRelation(rel.getLeftRelation());
      //...y hace lo mismo para el término derecho de la relación
      constructRelation(rel.getRightRelation());
    }
  }

  /**
   * <p> <b>Descripción:</b> ordena los mintérminos generados para una
   * determinada regla de forma que queden en la lista de mintérminos
   * agrupables que les corresponda.
   *
   */
  private void sortMinterms() {
    //para todos lo mintérminos que se han generado para la nueva regla...
    Iterator it = currentMinterms.iterator();
    while (it.hasNext()) {
      XfspMinterm minterm = (XfspMinterm) it.next();
      //...obtiene su conclusión...
      Conclusion conc = minterm.getConclusion();
      //...y para todos los mintérminos que ya han sido ordenados
      //previamente...
      Iterator it2 = minterms.keySet().iterator();
      Conclusion aux = null;
      boolean found = false;
      while (it2.hasNext() && !found) {
        aux = (Conclusion) it2.next();
        //...comprueba si alguno tiene la misma conclusión que el mintérmino
        //que se quiere añadir
        if (aux.getVariable().equals(conc.getVariable().getName()) &&
            aux.
            getMembershipFunction().equals(conc.getMembershipFunction().getLabel())) {
          found = true;
        }
      }
      //lista que servirá para almacenar los mintérminos que tienen la misma
      //conclusión que el que se quiere añadir
      List l = null;
      //si se ha encontrado que ya había mintérminos con la misma conclusión...
      if (found) { //l != null) {
        //...se obtiene la lista asociada a dicha conclusión...
        l = (List) minterms.get(aux);
        //...se toma el conjunto correspondiente a la primera iteración de los
        //mintérminos que tenían esa conclusión...
        Set oldminterms = (Set) l.get(0);
        //...si el mintérmino no estaba en dicho conjunto...
        if (!exists(minterm, oldminterms)) {
          //...se añade el mintérmino actual a dicha iteración
          addNewMinterm(oldminterms, minterm);
        }
      }
      //si no se encontró...
      else {
        //...crea un nuevo conjunto para mintérminos que sumen lo mismo...
        Set sameSum = new HashSet();
        //...y le añade el mintérmino actual
        sameSum.add(minterm);
        //se crea una nueva lista de mintérminos agrupables...
        List laux = new ArrayList();
        //...y se le añade el nuevo mintérmino
        laux.add(sameSum); //minterm);
        //se crea una nueva iteración...
        Set firstIteration = new HashSet();
        //...y se le añade la nueva lista de mintérminos agrupables (que sólo
        //contiene al nuevo)
        firstIteration.add(laux);
        //se inicializa la lista de los mintérminos con igual conclusión que
        //el nuevo...
        l = new ArrayList();
        //...con su primera iteración...
        l.add(firstIteration);
        //...se añade al conjunto de mintérminos ya ordenados para que conste
        //con su correspondiente conclusión
        minterms.put(conc, l);
      }
    }
  }

  /**
   * <p> <b>Descripción:</b> comprueba si un determinado mintérmino existe en
   * un conjunto de mintérminos.
   * @param minterm Mintérmino para el que  se quiere comprobar si está o no en
   * un conjunto.
   * @param minterms Conjunto de mintérminos donde se buscará el mintérmino.
   * @return Cierto si dicho mintérmino se encuentra en el conjunto y falso en
   * caso contrario.
   *
   */
  private boolean exists(XfspMinterm minterm, Set minterms) {
    //resultado de la búsqueda (por defecto, se pone a falso)
    boolean found = false;
    //iterador que permite recorrer todos los elementos del conjunto
    Iterator it1 = minterms.iterator();
    //mientras queden elementos del conjunto por examinar y aún no se haya
    //encontrado el mintérmino...
    while (it1.hasNext() && !found) {
      //...obtiene la siguiente lista de conjuntos de mintérminos que queda por
      //procesar
      List l = (List) it1.next();
      //para todos los conjuntos de la lista y mientras no se haya encontrado
      //aún el mintérmino...
      for (int i = 0; i < l.size() && !found; i++) {
        //...obtiene el siguiente conjunto de mintérminos
        Set s = (Set) l.get(i);
        //iterador que permite recorrer todos los mintérminos del conjunto
        Iterator it2 = s.iterator();
        //mientras queden mintérminos por recorrer y aún no se haya encontrado
        //el mintérmino buscado en el conjunto...
        while (it2.hasNext() && !found) {
          //...obtiene el siguiente mintérmino
          XfspMinterm aux = (XfspMinterm) it2.next();
          //si coincide con el mintérmino buscado...
          if (aux.equals(minterm)) {
            //...finaliza la búsqueda del mintérmino
            found = true;
          }
        }
      }
    }
    return found;
  }

  /**
   * <p> <b>Descripción:</b> añade un mintérmino a un conjunto cuyos elementos
   * son listas de conjuntos de mintérminos agrupables y lo hace dentro de la
   * agrupación adecuada según la suma de los índices de las funciones de
   * pertenencia asignadas a las variables del mintérmino.
   * @param minterms Conjunto cuyos elementos son listas cuyos elementos, a su
   * vez, son conjuntos de mintérminos en los que los índices de las funciones
   * de pertenencia asignadas a las variables del mintérmino suman lo mismo.
   * @param newMinterm Mintérmino que se añadirá de forma ordenada en el
   * conjunto.
   *
   */
  private void addNewMinterm(Set minterms, XfspMinterm newMinterm) {
    //calcula cuánto suman los índices de las funciones de pertenencia asignadas
    //a las variables del mintérmino que se quiere añadir al conjunto
    int sum = calculateSum(newMinterm);
    //indica si el mintérmino ha sido añadido al conjunto en la posición que le
    //corresponda (por defecto, falso)
    boolean added = false;
    //iterador que permite recorrer el conjunto de mintérminos
    Iterator it = minterms.iterator();
    //mientras queden elementos del conjunto por procesar y aún no se haya
    //añadido el mintérmino...
    while (it.hasNext() && !added) {
      //...obtiene la siguiente lista
      List l = (List) it.next();
      //para todos los elementos de la lista y mientras aún no se haya añadido
      //el mintérmino...
      for (int i = 0; i < l.size() && !added; i++) {
        //XfspMinterm aux = (XfspMinterm) l.get(i);
        //...obtiene el siguiente conjunto de la lista
        Set s = (Set) l.get(i);
        //iterador que permite recorrer todos los elementos del conjunto actual
        Iterator it2 = s.iterator();
        //obtiene un mintérmino del conjunto actual (sólo es necesario un
        //elemento ya que dentro de dicho conjunto todos los mintérminos tienen
        //la misma suma para los índices de las funciones de pertenencia
        //asignadas a sus variables)
        XfspMinterm aux = (XfspMinterm) it2.next();
        //si se está examinando el conjunto de mintérminos cuya suma es menor y
        //la suma del mintérmino que se quiere añadir es una unidad menos que la
        //del conjunto...
        if (i == 0 && sum == (calculateSum(aux) - 1)) {
          //...crea un nuevo conjunto de mintérminos que sumarán lo mismo...
          Set newSet = new HashSet();
          //...y le añade el mintérmino
          newSet.add(newMinterm);
          //añade dicho conjunto en la primera posición de la lista de
          //mintérminos agrupables (y dicho conjunto pasará a ser el conjunto de
          //mintérminos con un valor más bajo para la suma)
          l.add(0, newSet);
          //indica que ya se ha añadido el mintérmino
          added = true;
        }
        //si se está examinando el conjunto de mintérminos cuya suma es mayor y
        //la suma del mintérmino que se quiere añadir es una unidad más que la
        //del conjunto...
        else if (i == (l.size() - 1) && sum == (calculateSum(aux) + 1)) {
          //...crea un nuevo conjunto de mintérminos que sumarán lo mismo...
          Set newSet = new HashSet();
          //...y le añade el mintérmino
          newSet.add(newMinterm);
          //añade dicho conjunto en la última posición de la lista de
          //mintérminos agrupables (y dicho conjunto pasará a ser el conjunto de
          //mintérminos con un valor más alto para la suma)
          l.add(l.size(), newSet);
          //indica que ya se ha añadido el mintérmino
          added = true;
        }
        //si se está examinando un conjunto de mintérminos cuya suma es igual a
        //la del mintérmino que se quiere añadir...
        else if (sum == calculateSum(aux) && !newMinterm.equals(aux)) {
          //...añade el mintérmino a dicho conjunto
          s.add(newMinterm);
          //indica que ya se ha añadido el mintérmino
          added = true;
        }
      }
    }
    //si no se ha añadido el mintérmino en ninguna de las listas de mintérminos
    //agrupables que existían...
    if (!added) {
      //...crea una nueva lista de mintérminos agrupables...
      List newList = new ArrayList();
      //...y crea un nuevo conjunto de mintérminos que sumarán lo mismo
      Set s = new HashSet();
      //...y le añade el mintérmino
      s.add(newMinterm);
      //añade el nuevo conjunto a la nueva lista...
      newList.add(s);
      //...y la nueva lista al conjunto de listas de mintérminos agrupables
      minterms.add(newList);
    }
    //fusiona las listas de mintérminos agrupables que se puedan
    mergeMintermsLists(minterms);
  }

  /**
   * <p> <b>Descripción:</b> fusiona aquellas listas de mintérminos agrupables
   * para las que los mintérminos de los extremos la lista sumen lo mismo.
   * @param minterms Conjunto cuyos elementos son listas cuyos elementos, a su
   * vez, son conjuntos de mintérminos en los que los índices de las funciones
   * de pertenencia asignadas a las variables del mintérmino suman lo mismo.
   *
   */
  private void mergeMintermsLists(Set minterms) {
    //clona el conjunto original
    Set mintermsClone = (Set) ( (HashSet) minterms).clone();
    //iterador que permite recorrer todos los elementos del clon
    Iterator it1 = mintermsClone.iterator();
    //para todos los elementos del conjunto clonado...
    while (it1.hasNext()) {
      //...obtiene la siguiente lista de mintérminos agrupables
      List aux1 = (List) it1.next();
      //iterador que permite recorrer los elementos del conjunto que ocupa la
      //primera posición de la lista anterior (y que, por lo tanto, tienen las
      //funciones de pertenencia cuyos índices suman menos de toda la lista)
      Iterator sameSum1 = ( (Set) aux1.get(0)).iterator();
      //obtiene un elemento cualquiera del conjunto que ocupa la primera
      //posición de la lista (da igual cual sea, ya que todos suman lo mismo)
      XfspMinterm minterm1 = (XfspMinterm) sameSum1.next();
      //iterador que permite recorrer todos los elementos del clon
      Iterator it2 = mintermsClone.iterator();
      //para todos los elementos del conjunto clonado...
      while (it2.hasNext()) {
        //...obtiene la siguiente lista de mitérminos agrupables
        List aux2 = (List) it2.next();
        //iterador que permite recorrer los elementos del conjunto que ocupa la
        //última posición de la lista anterior (y que, por lo tanto, tiene las
        //funciones de pertenencia cuyos índices suman más de toda la lista)
        Iterator sameSum2 = ( (Set) aux2.get(aux2.size() - 1)).iterator();
        //obtiene un elemento cualquiera del conjunto que ocupa la última
        //posición de la lista (da igual cual sea, ya que todos suman lo mismo)
        XfspMinterm minterm2 = (XfspMinterm) sameSum2.next();
        //si las listas examinadas son distintas (con esto se evita que se
        //intente fusionar una lista con ella misma)...
        if (!aux1.equals(aux2)) {
          //...si la suma de los mintérminos que ocupan la primera posición en
          //la primera lista es una unidad mayor que la de los que ocupan la
          //última posición de la segunda lista...
          if (calculateSum(minterm1) == (calculateSum(minterm2) + 1)) {
            //...vacía la lista original...
            minterms.clear();
            //...y le añade las listas del clon excepto la primera que se puede
            //fusionar
            //iterador que permite recorrer los elementos del clon
            Iterator it3 = mintermsClone.iterator();
            //mientras queden elementos por tratar...
            while (it3.hasNext()) {
              //...obtiene la siguiente lista del conjunto clonado
              List aux3 = (List) it3.next();
              //si dicha lista no coincide con la primera de las listas que se
              //va a fusionar...
              if (!aux3.equals(aux1)) {
                //...la añade al nuevo conjunto de mintérminos (con esto, sí se
                //añade la segunda de las listas que se va a fusionar)
                minterms.add(aux3);
              }
            }
            //para la segunda lista que se va fusionar, se le añaden todos los
            //elementos que contenía la primera
            aux2.addAll(aux1);
          }
        }
      }
    }
  }

  /**
   * <p> <b>Descripción:</b> calcula cúanto suman los índices de las funciones
   * de pertenencia que están asignadas a las variables de un mintérmino.
   * @param minterm Mintérmino cuyas funciones de pertenencia serán examinadas
   * para calcular la suma de sus índices.
   * @return Valor correspondiente a la suma de los índices de las funciones de
   * pertenencia asignadas a las variables del mintérmino.
   *
   */
  private int calculateSum(XfspMinterm minterm) {
    //valor total de la suma de los índices
    int sum = 0;
    //iterador que permite recorrer todas las funciones de pertenencia asignadas
    //a variables para el mintérmino que se esta examinando
    Iterator it1 = minterm.getVariables().values().iterator();
    //mientras queden funciones de pertenencia asignadas a las variables del
    //mintérmino por procesar...
    while (it1.hasNext()) {
      //...obtiene el siguiente conjunto de funciones de pertenencia asignadas a
      //una variable
      Set s = (Set) it1.next();
      //iterador que permite recorrer todas las funciones de pertenencia de la
      //asignación actual
      Iterator it2 = s.iterator();
      //para todas las funciones de pertenencia de la asignación actual...
      while (it2.hasNext()) {
        //...suma su índice al valor que había acumulado
        sum += ( (Integer) it2.next()).intValue();
      }
    }
    return sum;
  }
}
