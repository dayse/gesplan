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
 * <p> <b>Descripci�n:</b> construye un conjunto de mint�rminos obtenidos a
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

  //mint�rminos que ya han sido procesados por el constructor
  private Map minterms;

  //almacena las variables utilizadas por la regla que se est� procesando
  private Map usedVars;

  //almacena los mint�rminos generados por la regla que se est� procesando
  private Set currentMinterms;

  /**
   * <p> <b>Descripci�n:</b> crea un objeto capaz de construir el conjunto de
   * mint�rminos que representan una base de reglas.
   *
   */
  public XfspMintermBuilder() {
    minterms = new HashMap();
    usedVars = new HashMap();
    currentMinterms = new HashSet();
  }

  /**
   * <p> <b>Descripci�n:</b> establece el conjunto de variables de entrada del
   * mint�rmino que se est� procesando.
   * @param inputvars Vector de variables correspondiente a las variables de
   * entrada del mint�rmino procesado.
   *
   */
  public void setInputvars(Variable[] inputvars) {
    this.inputvars = inputvars;
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve el conjunto de mint�rminos que se ha ido
   * construyendo al tratar cada una de las reglas que se le han pasado.
   * @return Conjunto de mint�rminos construido de forma incremental.
   *
   */
  public Map getMinterms() {
    return minterms;
  }

  /**
   * <p> <b>Descripci�n:</b> construye un conjunto de mint�rminos que
   * representa a la regla que se le pasa como par�metro.
   * @param rule Regla para la que se quiere construir el conjunto de
   * mint�rminos que la representa.
   *
   */
  public void buildRule(Rule rule) {

    //System.out.println("N�mero de variables de entrada del sistema " + inputvars.length);
    //System.out.println("Analizando la regla " + rule.toXfl());


    //conjunto de mint�rminos que representan a la regla que se quiere tratar
    currentMinterms = new HashSet();
    //inicializam un nuevo mint�rmino...
    XfspMinterm currentMinterm = new XfspMinterm();

    //currentMinterm.setVariables(new HashMap());





    //indicam que todav�a no hemos utilizado ninguna de las variables de
    //entrada de la base de reglas
    for (int i = 0; i < inputvars.length; i++) {
      usedVars.put(inputvars[i], new Boolean(false));
    }
    //a�ade al conjunto de mint�rminos de la regla actual el inicial
    currentMinterms.add(currentMinterm);
    //actualiza todos los mint�rminos de la regla actual para que contemplen
    //la premisa de dicha regla...
    constructRelation(rule.getPremise());
    //...y las variables que no aparecen dicha regla
    addNonUsedVars();
    //obtiene las conclusiones de la regla actual
    Conclusion[] conc = rule.getConclusions();
    //a�ade a los mint�rminos de la regla actual las conclusiones de la misma
    currentMinterms = addConclusions(currentMinterms, conc);
    //distrubuye los mint�rminos generados por la regla actual entre los grupos
    //de mint�rminos que ya se hab�a preparado para las reglas anteriores
    sortMinterms();
    //para cada una de las conclusiones de la regla tratada...
    for (int i = 0; i < conc.length; i++) {
      //...obtiene la lista de iteraciones asociadas
      List l = (List) minterms.get(conc[i]);
      //si pudo obtener dicha lista...
      if (l != null) {
        //...toma el conjunto con los mint�rminos agrupables para la primera
        //iteraci�n...
        Set s = (Set) l.get(0);
        //...y fusiona aquellas listas de mint�rminos agrupables que pueda
        this.mergeMintermsLists(s);
      }
    }


        //TODO - quitar esto
        //System.out.print("*******************************\nTratando la regla " + rule.toXfl());
        //System.out.println("mint�rminos construidos:");
        //Iterator it = currentMinterms.iterator();
        //while(it.hasNext()){
          //System.out.println(it.next());
        //}


  }

  /**
   * <p> <b>Descripci�n:</b> a�ade conclusiones a un conjunto de mint�rminos o
   * implicantes.
   * @param currentMinterms Conjunto de mint�rminos o implicantes a los que se
   * quiere conclusiones.
   * @param conc Conjunto de conclusiones que se quieren a�adir a los
   * mint�rminos o implicantes seleccionados.
   * @return Conjunto de mint�rminos o implicantes iguales a los originales
   * a los que se han a�adido las conclusiones deseadas.
   *
   */
  private Set addConclusions(Set currentMinterms, Conclusion[] conc) {
    //resultado de a�adir las conclusiones a los mint�rminos o implicantes
    Set result = new HashSet();
    //para todas las conclusiones que se quieren a�adir...
    for (int i = 0; i < conc.length; i++) {
      //...toma una conclusi�n
      Conclusion aux = conc[i];
      //obtiene un iterador para el conjunto de mint�rminos o implicantes
      Iterator it = currentMinterms.iterator();
      //para todos los mint�rminos o implicantes...
      while (it.hasNext()) {
        //...obtiene un nuevo mint�rmino...
        XfspMinterm minterm = (XfspMinterm) it.next();
        //...le a�ade la conclusi�n actual...
        minterm.setConclusion(aux);
        //...y lo a�ade al conjunto de mint�rminos que se devuelve como
        //resultado
        result.add(minterm);
      }
    }
    return result;
  }

  /**
   * <p> <b>Descripci�n:</b> a�ade conclusiones a un conjunto de mint�rminos o
   * implicantes.
   * @param currentMinterms Conjunto de mint�rminos o implicantes a los que se
   * quiere conclusiones.
   * @param conc Conjunto de conclusiones que se quieren a�adir a los
   * mint�rminos o implicantes seleccionados.
   * @return Conjunto de mint�rminos o implicantes iguales a los originales
   * a los que se han a�adido las conclusiones deseadas.
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
        //...crea un iterador que permite recorrer el conjunto de mint�rminos
        //actuales
        Iterator it = currentMinterms.iterator();
        //para todos los mint�rminos...
        while (it.hasNext()) {
          //...obtiene un mint�rmino del conjunto de mint�rminos actuales
          XfspMinterm currentMinterm = (XfspMinterm) it.next();
          //para todas las funciones de pertenencia del tipo al que pertenence
          //la variable de entrada actual...
          for (int j = 0;
               j < inputvars[i].getType().getMembershipFunctions().length; j++) {
            //...crea un nuevo mint�rmino
            XfspMinterm auxMinterm = new XfspMinterm();
            //obtiene un clon de las asignaciones de funciones de pertenencia
            //a las variables para el mint�rmino actual
            Map aux = (Map) ( (HashMap) currentMinterm.getVariables()).clone();
            //crea un nuevo conjunto...
            Set s = new HashSet();
            //...y le a�ade la funci�n de pertenencia actual
            s.add(new Integer(j));
            //asigna a la variable de entrada actual el conjunto de funciones de
            //pertenencia creado
            aux.put(inputvars[i], s);
            //a�ade al nuevo mint�rmino la nueva asignaci�n de funciones de
            //pertenencia creada
            auxMinterm.setVariables(aux);
            //a�ade al nuevo conjunto el nuevo mint�rmino creado
            newset.add(auxMinterm);
          }
        }
        //establece el nuevo conjunto de mint�rminos para la regla que se est�
        //procesando
        currentMinterms = newset;
      }
    }
  }

  /**
   * <p> <b>Descripci�n:</b> busca una funci�n de pertenencia en un vector de
   * funciones de pertenencia.
   * @param mfs Vector de funciones de pertenencia donde se buscar� una funci�n
   * de pertenencia.
   * @param mf Funci�n de pertenencia que se buscar� en un vector de funciones
   * de pertenencia.
   * @return Devuelve la posici�n que la funci�n de pertenencia a buscar ocupa
   * en el vector de funciones de pertenencia o -1 si dicha funci�n de
   * pertenencia no se encuentra en el vector.
   *
   */
  private int search(LinguisticLabel[] mfs, LinguisticLabel mf) {
    //posici�n ocupada por la funci�n de pertenencia buscada (se inicializa a
    //-1)
    int pos = -1;
    //guarda que permite salir del bucle de b�squeda cuando se encuentre la
    //funci�n de pertenencia buscada
    boolean found = false;
    //para todas las funciones de pertenencia del vector mientras no se haya
    //encontrado la funci�n de pertenencia buscada...
    for (int i = 0; i < mfs.length && !found; i++) {
      //...compara la funci�n de pertenencia actual con la buscada y si
      //coinciden...
      if (mfs[i].equals(mf)) {
        //...almacena la posici�n ocupada por dicha funci�n de pertenencia en el
        //vector...
        pos = i;
        //...e indica que se debe salir del bucle
        found = true;
      }
    }
    return pos;
  }

  /**
   * <p> <b>Descripci�n:</b> construye los mint�rminos adecuados seg�n la
   * relaci�n de la premisa de la regla tratada o de las relaciones incluidas
   * dentro de otra relaci�n.
   * @param rel Relaci�n de tipo IS o AND para la que se obtendr� un conjunto
   * de mint�rminos para la regla que se est� tratando.
   *
   */
  private void constructRelation(Relation rel) {
    //si la relaci�n es de tipo IS...
    if (rel.getKind() == Relation.IS) {
      //...crea un iterador que permite recorrer el conjunto de mint�rminos
      //actual
      Iterator it = currentMinterms.iterator();
      //para todos los mint�rminos del conjunto actual...
      while (it.hasNext()) {
        //...crea un nuevo conjunto...
        Set s = new HashSet();
        //...y le un entero con la posici�n ocupada por la funci�n de
        //pertenencia asignada en la relaci�n IS dentro del conjunto de
        //funciones de pertenencia del tipo al que pertenece la variable de la
        //relaci�n tratada
        s.add(new
              Integer(search(rel.getVariable().getType().getMembershipFunctions(),
                             rel.getMembershipFunction())));
        //a�ade al mint�rmino actual una asignaci�n para la variable de la
        //relaci�n con la funci�n de pertenencia de dicha relaci�n
        ( (XfspMinterm) it.next()).getVariables().put(rel.getVariable(), s);
      }
      //indica que la variable de la relaci�n ha sido utilizada
      usedVars.put(rel.getVariable(), new Boolean(true));
    }
    //si la relaci�n es de tipo AND...
    else if (rel.getKind() == Relation.AND) {
      //...construye los mint�rminos para el t�rmino izquierdo de la relaci�n...
      constructRelation(rel.getLeftRelation());
      //...y hace lo mismo para el t�rmino derecho de la relaci�n
      constructRelation(rel.getRightRelation());
    }
  }

  /**
   * <p> <b>Descripci�n:</b> ordena los mint�rminos generados para una
   * determinada regla de forma que queden en la lista de mint�rminos
   * agrupables que les corresponda.
   *
   */
  private void sortMinterms() {
    //para todos lo mint�rminos que se han generado para la nueva regla...
    Iterator it = currentMinterms.iterator();
    while (it.hasNext()) {
      XfspMinterm minterm = (XfspMinterm) it.next();
      //...obtiene su conclusi�n...
      Conclusion conc = minterm.getConclusion();
      //...y para todos los mint�rminos que ya han sido ordenados
      //previamente...
      Iterator it2 = minterms.keySet().iterator();
      Conclusion aux = null;
      boolean found = false;
      while (it2.hasNext() && !found) {
        aux = (Conclusion) it2.next();
        //...comprueba si alguno tiene la misma conclusi�n que el mint�rmino
        //que se quiere a�adir
        if (aux.getVariable().equals(conc.getVariable().getName()) &&
            aux.
            getMembershipFunction().equals(conc.getMembershipFunction().getLabel())) {
          found = true;
        }
      }
      //lista que servir� para almacenar los mint�rminos que tienen la misma
      //conclusi�n que el que se quiere a�adir
      List l = null;
      //si se ha encontrado que ya hab�a mint�rminos con la misma conclusi�n...
      if (found) { //l != null) {
        //...se obtiene la lista asociada a dicha conclusi�n...
        l = (List) minterms.get(aux);
        //...se toma el conjunto correspondiente a la primera iteraci�n de los
        //mint�rminos que ten�an esa conclusi�n...
        Set oldminterms = (Set) l.get(0);
        //...si el mint�rmino no estaba en dicho conjunto...
        if (!exists(minterm, oldminterms)) {
          //...se a�ade el mint�rmino actual a dicha iteraci�n
          addNewMinterm(oldminterms, minterm);
        }
      }
      //si no se encontr�...
      else {
        //...crea un nuevo conjunto para mint�rminos que sumen lo mismo...
        Set sameSum = new HashSet();
        //...y le a�ade el mint�rmino actual
        sameSum.add(minterm);
        //se crea una nueva lista de mint�rminos agrupables...
        List laux = new ArrayList();
        //...y se le a�ade el nuevo mint�rmino
        laux.add(sameSum); //minterm);
        //se crea una nueva iteraci�n...
        Set firstIteration = new HashSet();
        //...y se le a�ade la nueva lista de mint�rminos agrupables (que s�lo
        //contiene al nuevo)
        firstIteration.add(laux);
        //se inicializa la lista de los mint�rminos con igual conclusi�n que
        //el nuevo...
        l = new ArrayList();
        //...con su primera iteraci�n...
        l.add(firstIteration);
        //...se a�ade al conjunto de mint�rminos ya ordenados para que conste
        //con su correspondiente conclusi�n
        minterms.put(conc, l);
      }
    }
  }

  /**
   * <p> <b>Descripci�n:</b> comprueba si un determinado mint�rmino existe en
   * un conjunto de mint�rminos.
   * @param minterm Mint�rmino para el que  se quiere comprobar si est� o no en
   * un conjunto.
   * @param minterms Conjunto de mint�rminos donde se buscar� el mint�rmino.
   * @return Cierto si dicho mint�rmino se encuentra en el conjunto y falso en
   * caso contrario.
   *
   */
  private boolean exists(XfspMinterm minterm, Set minterms) {
    //resultado de la b�squeda (por defecto, se pone a falso)
    boolean found = false;
    //iterador que permite recorrer todos los elementos del conjunto
    Iterator it1 = minterms.iterator();
    //mientras queden elementos del conjunto por examinar y a�n no se haya
    //encontrado el mint�rmino...
    while (it1.hasNext() && !found) {
      //...obtiene la siguiente lista de conjuntos de mint�rminos que queda por
      //procesar
      List l = (List) it1.next();
      //para todos los conjuntos de la lista y mientras no se haya encontrado
      //a�n el mint�rmino...
      for (int i = 0; i < l.size() && !found; i++) {
        //...obtiene el siguiente conjunto de mint�rminos
        Set s = (Set) l.get(i);
        //iterador que permite recorrer todos los mint�rminos del conjunto
        Iterator it2 = s.iterator();
        //mientras queden mint�rminos por recorrer y a�n no se haya encontrado
        //el mint�rmino buscado en el conjunto...
        while (it2.hasNext() && !found) {
          //...obtiene el siguiente mint�rmino
          XfspMinterm aux = (XfspMinterm) it2.next();
          //si coincide con el mint�rmino buscado...
          if (aux.equals(minterm)) {
            //...finaliza la b�squeda del mint�rmino
            found = true;
          }
        }
      }
    }
    return found;
  }

  /**
   * <p> <b>Descripci�n:</b> a�ade un mint�rmino a un conjunto cuyos elementos
   * son listas de conjuntos de mint�rminos agrupables y lo hace dentro de la
   * agrupaci�n adecuada seg�n la suma de los �ndices de las funciones de
   * pertenencia asignadas a las variables del mint�rmino.
   * @param minterms Conjunto cuyos elementos son listas cuyos elementos, a su
   * vez, son conjuntos de mint�rminos en los que los �ndices de las funciones
   * de pertenencia asignadas a las variables del mint�rmino suman lo mismo.
   * @param newMinterm Mint�rmino que se a�adir� de forma ordenada en el
   * conjunto.
   *
   */
  private void addNewMinterm(Set minterms, XfspMinterm newMinterm) {
    //calcula cu�nto suman los �ndices de las funciones de pertenencia asignadas
    //a las variables del mint�rmino que se quiere a�adir al conjunto
    int sum = calculateSum(newMinterm);
    //indica si el mint�rmino ha sido a�adido al conjunto en la posici�n que le
    //corresponda (por defecto, falso)
    boolean added = false;
    //iterador que permite recorrer el conjunto de mint�rminos
    Iterator it = minterms.iterator();
    //mientras queden elementos del conjunto por procesar y a�n no se haya
    //a�adido el mint�rmino...
    while (it.hasNext() && !added) {
      //...obtiene la siguiente lista
      List l = (List) it.next();
      //para todos los elementos de la lista y mientras a�n no se haya a�adido
      //el mint�rmino...
      for (int i = 0; i < l.size() && !added; i++) {
        //XfspMinterm aux = (XfspMinterm) l.get(i);
        //...obtiene el siguiente conjunto de la lista
        Set s = (Set) l.get(i);
        //iterador que permite recorrer todos los elementos del conjunto actual
        Iterator it2 = s.iterator();
        //obtiene un mint�rmino del conjunto actual (s�lo es necesario un
        //elemento ya que dentro de dicho conjunto todos los mint�rminos tienen
        //la misma suma para los �ndices de las funciones de pertenencia
        //asignadas a sus variables)
        XfspMinterm aux = (XfspMinterm) it2.next();
        //si se est� examinando el conjunto de mint�rminos cuya suma es menor y
        //la suma del mint�rmino que se quiere a�adir es una unidad menos que la
        //del conjunto...
        if (i == 0 && sum == (calculateSum(aux) - 1)) {
          //...crea un nuevo conjunto de mint�rminos que sumar�n lo mismo...
          Set newSet = new HashSet();
          //...y le a�ade el mint�rmino
          newSet.add(newMinterm);
          //a�ade dicho conjunto en la primera posici�n de la lista de
          //mint�rminos agrupables (y dicho conjunto pasar� a ser el conjunto de
          //mint�rminos con un valor m�s bajo para la suma)
          l.add(0, newSet);
          //indica que ya se ha a�adido el mint�rmino
          added = true;
        }
        //si se est� examinando el conjunto de mint�rminos cuya suma es mayor y
        //la suma del mint�rmino que se quiere a�adir es una unidad m�s que la
        //del conjunto...
        else if (i == (l.size() - 1) && sum == (calculateSum(aux) + 1)) {
          //...crea un nuevo conjunto de mint�rminos que sumar�n lo mismo...
          Set newSet = new HashSet();
          //...y le a�ade el mint�rmino
          newSet.add(newMinterm);
          //a�ade dicho conjunto en la �ltima posici�n de la lista de
          //mint�rminos agrupables (y dicho conjunto pasar� a ser el conjunto de
          //mint�rminos con un valor m�s alto para la suma)
          l.add(l.size(), newSet);
          //indica que ya se ha a�adido el mint�rmino
          added = true;
        }
        //si se est� examinando un conjunto de mint�rminos cuya suma es igual a
        //la del mint�rmino que se quiere a�adir...
        else if (sum == calculateSum(aux) && !newMinterm.equals(aux)) {
          //...a�ade el mint�rmino a dicho conjunto
          s.add(newMinterm);
          //indica que ya se ha a�adido el mint�rmino
          added = true;
        }
      }
    }
    //si no se ha a�adido el mint�rmino en ninguna de las listas de mint�rminos
    //agrupables que exist�an...
    if (!added) {
      //...crea una nueva lista de mint�rminos agrupables...
      List newList = new ArrayList();
      //...y crea un nuevo conjunto de mint�rminos que sumar�n lo mismo
      Set s = new HashSet();
      //...y le a�ade el mint�rmino
      s.add(newMinterm);
      //a�ade el nuevo conjunto a la nueva lista...
      newList.add(s);
      //...y la nueva lista al conjunto de listas de mint�rminos agrupables
      minterms.add(newList);
    }
    //fusiona las listas de mint�rminos agrupables que se puedan
    mergeMintermsLists(minterms);
  }

  /**
   * <p> <b>Descripci�n:</b> fusiona aquellas listas de mint�rminos agrupables
   * para las que los mint�rminos de los extremos la lista sumen lo mismo.
   * @param minterms Conjunto cuyos elementos son listas cuyos elementos, a su
   * vez, son conjuntos de mint�rminos en los que los �ndices de las funciones
   * de pertenencia asignadas a las variables del mint�rmino suman lo mismo.
   *
   */
  private void mergeMintermsLists(Set minterms) {
    //clona el conjunto original
    Set mintermsClone = (Set) ( (HashSet) minterms).clone();
    //iterador que permite recorrer todos los elementos del clon
    Iterator it1 = mintermsClone.iterator();
    //para todos los elementos del conjunto clonado...
    while (it1.hasNext()) {
      //...obtiene la siguiente lista de mint�rminos agrupables
      List aux1 = (List) it1.next();
      //iterador que permite recorrer los elementos del conjunto que ocupa la
      //primera posici�n de la lista anterior (y que, por lo tanto, tienen las
      //funciones de pertenencia cuyos �ndices suman menos de toda la lista)
      Iterator sameSum1 = ( (Set) aux1.get(0)).iterator();
      //obtiene un elemento cualquiera del conjunto que ocupa la primera
      //posici�n de la lista (da igual cual sea, ya que todos suman lo mismo)
      XfspMinterm minterm1 = (XfspMinterm) sameSum1.next();
      //iterador que permite recorrer todos los elementos del clon
      Iterator it2 = mintermsClone.iterator();
      //para todos los elementos del conjunto clonado...
      while (it2.hasNext()) {
        //...obtiene la siguiente lista de mit�rminos agrupables
        List aux2 = (List) it2.next();
        //iterador que permite recorrer los elementos del conjunto que ocupa la
        //�ltima posici�n de la lista anterior (y que, por lo tanto, tiene las
        //funciones de pertenencia cuyos �ndices suman m�s de toda la lista)
        Iterator sameSum2 = ( (Set) aux2.get(aux2.size() - 1)).iterator();
        //obtiene un elemento cualquiera del conjunto que ocupa la �ltima
        //posici�n de la lista (da igual cual sea, ya que todos suman lo mismo)
        XfspMinterm minterm2 = (XfspMinterm) sameSum2.next();
        //si las listas examinadas son distintas (con esto se evita que se
        //intente fusionar una lista con ella misma)...
        if (!aux1.equals(aux2)) {
          //...si la suma de los mint�rminos que ocupan la primera posici�n en
          //la primera lista es una unidad mayor que la de los que ocupan la
          //�ltima posici�n de la segunda lista...
          if (calculateSum(minterm1) == (calculateSum(minterm2) + 1)) {
            //...vac�a la lista original...
            minterms.clear();
            //...y le a�ade las listas del clon excepto la primera que se puede
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
                //...la a�ade al nuevo conjunto de mint�rminos (con esto, s� se
                //a�ade la segunda de las listas que se va a fusionar)
                minterms.add(aux3);
              }
            }
            //para la segunda lista que se va fusionar, se le a�aden todos los
            //elementos que conten�a la primera
            aux2.addAll(aux1);
          }
        }
      }
    }
  }

  /**
   * <p> <b>Descripci�n:</b> calcula c�anto suman los �ndices de las funciones
   * de pertenencia que est�n asignadas a las variables de un mint�rmino.
   * @param minterm Mint�rmino cuyas funciones de pertenencia ser�n examinadas
   * para calcular la suma de sus �ndices.
   * @return Valor correspondiente a la suma de los �ndices de las funciones de
   * pertenencia asignadas a las variables del mint�rmino.
   *
   */
  private int calculateSum(XfspMinterm minterm) {
    //valor total de la suma de los �ndices
    int sum = 0;
    //iterador que permite recorrer todas las funciones de pertenencia asignadas
    //a variables para el mint�rmino que se esta examinando
    Iterator it1 = minterm.getVariables().values().iterator();
    //mientras queden funciones de pertenencia asignadas a las variables del
    //mint�rmino por procesar...
    while (it1.hasNext()) {
      //...obtiene el siguiente conjunto de funciones de pertenencia asignadas a
      //una variable
      Set s = (Set) it1.next();
      //iterador que permite recorrer todas las funciones de pertenencia de la
      //asignaci�n actual
      Iterator it2 = s.iterator();
      //para todas las funciones de pertenencia de la asignaci�n actual...
      while (it2.hasNext()) {
        //...suma su �ndice al valor que hab�a acumulado
        sum += ( (Integer) it2.next()).intValue();
      }
    }
    return sum;
  }
}
