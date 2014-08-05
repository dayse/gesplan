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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xfuzzy.lang.Conclusion;

/**
 * <p> <b>Descripci�n:</b> clase de objetos que encuentran el cubrimiento
 * m�nimo para un conjunto de mint�rminos.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.0
 * @see XfspModel
 * @see XfspTabularSimplifier
 *
 */
public class XfspMinimalCoveringFinder {

  //almacena los mint�rminos que ya han sido cubiertos
  private Set coveredMinterms;

  //almacena el cubrimiento m�nimo para un conjunto de mint�rminos asociado a
  //una conclusi�n
  private Map minimalCovering;

  /**
   * <p> <b>Descripci�n:</b> crea un objeto capaz de hallar el cubrimiento
   * m�nimo necesario para un conjunto de mint�rminos asociado a una conclusi�n.
   *
   */
  public XfspMinimalCoveringFinder() {
    minimalCovering = new HashMap();
  }

  /**
   * <p> <b>Descripci�n:</b> encuentra el conjunto de mint�rminos que forman el
   * cubrimiento m�nimo para un conjunto de mint�rminos dado.
   * @param minterms Conjunto de mint�rminos cuyo cubrimiento m�nimo se quiere
   * encontrar.
   * @return Cubrimiento m�nimo para el conjunto original.
   *
   */
  public Map findMinimalCovering(Map minterms) {
    //iterador para todas las claves del map que contiene los mint�rminos y que
    //corresponde a conclusiones de la base de reglas
    Iterator it = minterms.keySet().iterator();
    //para todas las conclusiones cuyos mint�rminos han sido encontrados en la
    //base de reglas...
    while (it.hasNext()) {
      //...obtiene la conclusi�n actual...
      Conclusion conc = (Conclusion) it.next();
      //...empieza asign�ndole como cubrimiento m�nimo una lista vac�a de
      //mint�rminos...
      minimalCovering.put(conc, new HashSet());
      //...y obtiene su lista de mint�rminos e implicantes
      List l = (List) minterms.get(conc);
      //calcula los mint�rminos iniciales de la conclusi�n que se est� tratando
      Set initialMinterms = calculateInitialMinterms( (Set) l.get(0));




      //TODO - eliminar esto
      //System.out.println("*** mint�rminos a cubrir ***");
      //Iterator it3 = initialMinterms.iterator();
      //while (it3.hasNext()) {
        //System.out.println(it3.next());
      //}





      //conjunto de mint�rminos cubiertos por el cubrimiento m�nimo que se ha
      //encontrado
      coveredMinterms = new HashSet();
      //almacena si ya se han cubierto todos los mint�rminos para la conclusi�n
      boolean allCovered = false;

      //nuevo
      //mientras no se hayan cubierto todos los mint�rminos de para la
      //conclusi�n que se est� tratando...
      while (!allCovered) {
        //...busca mint�rminos o implicantes en la lista obtenida tras aplicar
        //el m�todo de simplificaci�n tabular, comenzando por aquellas
        //implicantes que agrupan m�s mint�rminos...
        for (int i = (l.size() - 1); i >= 0 && !allCovered; i--) {



          //System.out.println("\nMiramos una iteraci�n m�s para atr�s...\n");





          //...obtiene el conjunto de mint�rminos o implicantes de la iteraci�n
          //actual de algoritmo
          Set s = (Set) l.get(i);
          //mint�rmino o implicante elegido de entre los disponibles en la
          //iteraci�n actual
          XfspMinterm selectedMinterm = null;
          //n�mero de mint�rminos cubiertos por el mint�rmino o implicante
          //seleccionado
          int newCoveredMinterms = 0;
          //indica si se ha elegido un mint�rmino o implicante de la iteraci�n
          //que se est� examinando (se inicializa por defecto a cierto para
          //poder entrar en el bucle siguiente)
          boolean added = true;
          //conjunto de mint�rminos elegidos para ser a�adidos al cubrimiento
          //m�nimo de entre los disponibles en la iteraci�n actual del
          //algoritmo de simplificaci�n tabular
          Set addedMinterms = new HashSet();
          //mientras se sigan a�adiendo mint�rminos o implicantes al cubrimiento
          //m�nimo y queden mint�rminos por cubrir de entre los de la conclusi�n
          //que se est� analizando...
          while (added && !allCovered) {
            //...indica que todav�a no se ha a�adido ning�n mint�rmino o
            //implicante en la iteraci�n actual
            added = false;
            //iterador que permite recorrer las listas de mint�rminos de la
            //iteraci�n actual
            Iterator it1 = s.iterator();
            //mientras queden listas de mint�rminos de la iteraci�n actual por
            //ser procesados...
            while (it1.hasNext()) {
              //...obtiene una lista de mint�rminos de la iteraci�n actual
              List mintermsList = (List) it1.next();
              //para todos los conjuntos de mint�rminos de la lista que se est�
              //tratando...
              for (int j = 0; j < mintermsList.size() && !allCovered; j++) {
                //...obtiene un conjunto de mint�rminos
                Set aux = (Set) mintermsList.get(j);
                //iterador que permite recorrer todos los mint�rminos o
                //implicantes del conjunto de mint�rminos actual
                Iterator it2 = aux.iterator();
                //para todos los mint�rminos o implicantes del conjunto
                //actual...
                while (it2.hasNext()) {
                  //...obtiene un mint�rmino o implicante del conjunto actual
                  XfspMinterm maux = (XfspMinterm) it2.next();
                  //obtiene el n�mero de mint�rminos de los cubiertos por el
                  //mint�rmino o implicante actual que no han sido cubierto a�n
                  //por ning�n mint�rmino o implicante de los seleccionados
                  //para el cubrimiento m�nimo
                  int nonCovered = nonCoveredMinterms(maux);
                  //si el n�mero de nuevos mint�rminos cubiertos por el
                  //mint�rmino o implicante actual es superior al mint�rmino o
                  //implicante seleccionado temporalmente hasta el momento para
                  //ser a�adido al cubrimiento m�nimo y dicho mint�rmino o
                  //implicante no ha sido a�adido a�n en la iteraci�n actual...


                  //System.out.println("Implicante analizada " + maux);



                  if (nonCovered > newCoveredMinterms &&
                      !addedMinterms.contains(maux)) {



                    //System.out.println("Se elige provisionalmente para el cubrimiento porque es mejor que " + selectedMinterm);



                    //...selecciona de manera provisional el mint�rmino o
                    //implicante actual...
                    selectedMinterm = maux;
                    //...indica que se ha elegido un mint�rmino o implicante de
                    //entre los disponibles en la iteraci�n actual...
                    added = true;
                    //...y almacena el n�mero de mint�rminos nuevos cubiertos
                    //por el seleccionado
                    newCoveredMinterms = nonCovered;
                  }
                  //si el n�mero de nuevos mint�rminos cubiertos por el
                  //mint�rmino o implicante actual es igual al mint�rmino o
                  //implicante seleccionado temporalmente hasta el momento para
                  //ser a�adido al cubrimiento m�nimo y dicho mint�rmino o
                  //implicante no ha sido a�adido a�n en la iteraci�n actual y
                  //ya se hab�a seleccionado un mint�rmino o implicante
                  //anteriormente (esto evita comportamientos extra�os para el
                  //primer mint�rmino o implicante tratado cuando a�n no se ha
                  //seleccionado ninguno anteriormente)...
                  else if (nonCovered == newCoveredMinterms &&
                           !addedMinterms.contains(maux) && selectedMinterm != null) {
                    //...si el n�mero de mint�rminos cubiertos por el actual es
                    //mayor que el que cubr�a el mint�rmino o implicante que
                    //hab�a sido seleccionado anteriormente...
                    if (maux.getCoveredMinterms().size() >
                        selectedMinterm.getCoveredMinterms().size()) {
                      //...selecciona de manera provisional el mint�rmino o
                      //implicante actual...
                      selectedMinterm = maux;
                      //...e indica que se ha elegido un mint�rmino o implicante
                      //de entre los disponibles en la iteraci�n actual
                      added = true;
                    }
                  }
                }
              }
            }
            //si se ha seleccionado alg�n mint�rmino o implicante de la
            //iteraci�n actual...
            if (added) {


              //System.out.println("se ha elegido definitivamente para el cubrimiento m�nimo " + selectedMinterm);


              //...a�ade el mint�rmino o implicante seleccionado al conjunto de
              //mint�rminos o implicantes a�adidos de los disponibles en la
              //iteraci�n actual
              addedMinterms.add(selectedMinterm);
              //...y tambi�n lo a�ade al cubrimiento m�nimo encontrado
              ( (Set) minimalCovering.get(conc)).add(selectedMinterm);
              //a�ade al los mint�rminos cubiertos por el cubrimiento m�nimo
              //actual los que cubre el mint�rmino o implicante seleccionado
              coveredMinterms.addAll(selectedMinterm.getCoveredMinterms());


              //tratamiento para redundantes
              XfspMinterm redundantMinterm=findRedundantMinterms((Set) minimalCovering.get(conc));
              while(redundantMinterm!=null){
                System.out.println("El mintermino " + redundantMinterm + " es redundante!!!!!!");
                ((Set) minimalCovering.get(conc)).remove(redundantMinterm);
                redundantMinterm=findRedundantMinterms((Set) minimalCovering.get(conc));
              }


              //si el n�mero de mint�rminos cubiertos por el cubrimiento min�mo
              //iguala a los mint�rminos que ten�a que cubrir para la conclusi�n
              //actual...
              if (coveredMinterms.size() == initialMinterms.size()) {
                //...termina con la b�squeda del cubrimiento m�nimo
                allCovered = true;
              }



              //creo que esto lo puede resolver todo
              newCoveredMinterms = 0;
              selectedMinterm = null;



            }
          }




          //if (allCovered) {
            //System.out.println("Todos los minterminos fueron cubiertos!!!!!!");
          //}




        }
      }

      //antiguo
      /*
             //mientras no se hayan cubierto todos los mint�rminos de la conclusi�n
             //que se est� tratando...
             while (!allCovered) {
        //...examina todas las iteraciones de la conclusi�n actual, empezando
        //por la �ltima, que es la que contiene implicantes mayores
        for (int i = (l.size() - 1); i >= 0 && !allCovered; i--) {
          //obtiene el conjunto de mint�rminos correspondiente a la iteraci�n
          //que se est� examinando
          Set s = (Set) l.get(i);
          //examina todas las implicantes de la iteraci�n
          Iterator it1 = s.iterator();
          while (it1.hasNext()) {
            //obtiene una lista de implicantes
            List mintermsList = (List) it1.next();
            //trata todos las implicantes de la lista mientras no hayan sido
            //cubiertos todos los mint�rminos
            for (int j = 0; j < mintermsList.size() && !allCovered; j++) {
              //obtiene una lista de mint�rminos
              Set saux = (Set) mintermsList.get(j);
              //itera sobre todos los mint�rminos
              Iterator it2 = saux.iterator();
              while (it2.hasNext()) {
                //mint�rmino actual
                XfspMinterm maux = (XfspMinterm) it2.next();
                //si no han sido cubiertos todos los mint�rminos iniciales...
                if (!allMintermsCovered(maux.getCoveredMinterms())) {
                  //a�ade el mint�rmino a la cubertura de la conclusi�n tratada
                  ( (Set) minimalCovering.get(conc)).add(maux);




                  System.out.println("se a�ade el mintermino " + maux);
                  System.out.println("porque cubre los minterminos");
                  it3 = maux.getCoveredMinterms().iterator();
                  while(it3.hasNext()){
                    System.out.println(it3.next());
                  }






                  //a�ade al conjunto de mint�rminos cubiertos aquellos que
                  //cubre la implicante que se acaba de a�adir
                  coveredMinterms.addAll(maux.getCoveredMinterms());
                  //si el conjunto de mit�rminos cubiertos ha alcanzado en
                  //tama�o al de mint�rminos iniciales para la conclusi�n
                  //tratada...
                  if (coveredMinterms.size() == initialMinterms.size()) {
                    //...se indica que ya se han cubierto todos los mint�rminos
                    //de la conclusi�n tratada
                    allCovered = true;
                  }
                }
              }
            }
          }
        }
             }*/





      /*
            System.out.println("*** mint�rminos cubiertos ***");
            it3 =  coveredMinterms.iterator();
            while(it3.hasNext()){
              System.out.println(it3.next());
            }
       */

    }

    //System.out.println("cubrimiento minimo encontrado");
    //Iterator it3 = minimalCovering.values().iterator();
    //while (it3.hasNext()) {
      //System.out.println(it3.next());
    //}

    //devuelve el cubrimiento m�nimo encontrado
    return minimalCovering;
  }



  /**
   * <p> <b>Descripci�n:</b> busca en un conjunto de mint�rminos e implicantes
   * uno que sea redundante, es decir, para el que todos los mint�rminos
   * cubiertos por �l ya sean cubiertos por el resto de los miembros del
   * conjunto.
   * @param minimalCovering Conjunto de mint�rminos o implicantes.
   * @return Devuelve una implicante redundante si la hay en el conjunto o null
   * en caso contrario.
   *
   */
  private XfspMinterm findRedundantMinterms(Set minimalCovering){
    Iterator it = minimalCovering.iterator();
    Set clonedSet = (Set) ((HashSet) minimalCovering).clone();
    boolean found = false;
    XfspMinterm redundantMinterm = null;

    while(it.hasNext() && !found){
      XfspMinterm aux = (XfspMinterm) it.next();
      clonedSet.remove(aux);
//      System.out.println("conjunto original\n" + minimalCovering);
//      System.out.println("se examina " + aux);
//      System.out.println("Conjunto clonado tras quitarlo\n" + clonedSet);
      Set clonedSetCoveredMinterms = getCoveredMinterms(clonedSet);
//      System.out.println("Los que quedan cubren\n" + clonedSetCoveredMinterms);
      if(clonedSetCoveredMinterms.containsAll(aux.getCoveredMinterms())){
        redundantMinterm = aux;
        found = true;
      }
      else{
        clonedSet.add(aux);
      }
    }
    return redundantMinterm;
  }

  /**
   * <p> <b>Descripci�n:</b> calcula el conjunto de mint�rminos cubiertos por
   * todos las implicantes de un conjunto dado.
   * @param mintermsSet Conjunto de mint�rminos o implicantes.
   * @return Conjunto de mint�rminos cubiertos por todas las implicantes del
   * conjunto.
   *
   */
  private Set getCoveredMinterms(Set mintermsSet){
    Set result = new HashSet();
    Iterator it = mintermsSet.iterator();
    while(it.hasNext()){
      XfspMinterm aux = (XfspMinterm) it.next();
      result.addAll(aux.getCoveredMinterms());
    }
    return result;
  }




  /**
   * <p> <b>Descripci�n:</b> calcula el conjunto de mint�rminos iniciales para
   * una determinada conclusi�n.
   * @param minterms Conjunto de mint�rminos asociados a una conclusi�n.
   * @return Conjunto inicial de mint�rminos.
   *
   */
  private Set calculateInitialMinterms(Set init) {
    //almacena el conjunto de mint�rminos iniciales asociados a una conclusi�n
    Set initialMinterms = new HashSet();
    //itera sobre el conjunto de listas de mint�rminos de entrada
    Iterator it = init.iterator();
    while (it.hasNext()) {
      //obtiene una nueva lista de mint�rminos del conjunto de entrada...
      List aux = (List) it.next();
      //...y a�ade todos los mint�rminos de dicha lista al conjunto de
      //mint�rminos iniciales
      for (int i = 0; i < aux.size(); i++) {
        initialMinterms.addAll( (Set) aux.get(i));
      }
    }
    return initialMinterms;
  }

  /**
   * <p> <b>Descripci�n:</b> calcula el n�mero de mint�rminos de los cubiertos
   * por un mint�rmino o implicante que no ha sido cubierto a�n por el
   * cubrimiento m�nimo actual.
   * @param minterm Mint�rmino o implicante para el que se calcular� cu�ntos
   * mint�rminos de los cubiertos por �l no han sido cubiertos a�n por el
   * cubrimiento m�nimo actual.
   * @return N�mero de mint�rminos cubiertos por un mint�rmino o implicante que
   * no han sido cubiertos por el cubrimiento m�nimo actual.
   *
   */
  private int nonCoveredMinterms(XfspMinterm minterm) {
    //n�mero de mint�rminos cubiertos por el mint�rmino o implicante que no han
    //sido cubiertos a�n por el cubrimiento m�nimo actual (se inicializa a 0)
    int result = 0;
    //iterador que permite acceder a los mint�rminos cubiertos por el mint�rmino
    //o implicante analizado
    Iterator it = minterm.getCoveredMinterms().iterator();
    //para todos los mint�rminos cubiertos por el mint�rmino o implicante...
    while (it.hasNext()) {
      //...si el mint�rmino actual a�n no ha sido cubierto por el cubrimiento
      //m�nimo actual...
      if (!coveredMinterms.contains(it.next())) {
        //...incrementa el n�mero de mint�rminos no cubiertos por el cubrimiento
        //m�nimo que son cubiertos por el mint�rmino o implicante analizado
        result++;
      }
    }
    return result;
  }
}
