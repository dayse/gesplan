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
 * <p> <b>Descripción:</b> clase de objetos que encuentran el cubrimiento
 * mínimo para un conjunto de mintérminos.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.0
 * @see XfspModel
 * @see XfspTabularSimplifier
 *
 */
public class XfspMinimalCoveringFinder {

  //almacena los mintérminos que ya han sido cubiertos
  private Set coveredMinterms;

  //almacena el cubrimiento mínimo para un conjunto de mintérminos asociado a
  //una conclusión
  private Map minimalCovering;

  /**
   * <p> <b>Descripción:</b> crea un objeto capaz de hallar el cubrimiento
   * mínimo necesario para un conjunto de mintérminos asociado a una conclusión.
   *
   */
  public XfspMinimalCoveringFinder() {
    minimalCovering = new HashMap();
  }

  /**
   * <p> <b>Descripción:</b> encuentra el conjunto de mintérminos que forman el
   * cubrimiento mínimo para un conjunto de mintérminos dado.
   * @param minterms Conjunto de mintérminos cuyo cubrimiento mínimo se quiere
   * encontrar.
   * @return Cubrimiento mínimo para el conjunto original.
   *
   */
  public Map findMinimalCovering(Map minterms) {
    //iterador para todas las claves del map que contiene los mintérminos y que
    //corresponde a conclusiones de la base de reglas
    Iterator it = minterms.keySet().iterator();
    //para todas las conclusiones cuyos mintérminos han sido encontrados en la
    //base de reglas...
    while (it.hasNext()) {
      //...obtiene la conclusión actual...
      Conclusion conc = (Conclusion) it.next();
      //...empieza asignándole como cubrimiento mínimo una lista vacía de
      //mintérminos...
      minimalCovering.put(conc, new HashSet());
      //...y obtiene su lista de mintérminos e implicantes
      List l = (List) minterms.get(conc);
      //calcula los mintérminos iniciales de la conclusión que se está tratando
      Set initialMinterms = calculateInitialMinterms( (Set) l.get(0));




      //TODO - eliminar esto
      //System.out.println("*** mintérminos a cubrir ***");
      //Iterator it3 = initialMinterms.iterator();
      //while (it3.hasNext()) {
        //System.out.println(it3.next());
      //}





      //conjunto de mintérminos cubiertos por el cubrimiento mínimo que se ha
      //encontrado
      coveredMinterms = new HashSet();
      //almacena si ya se han cubierto todos los mintérminos para la conclusión
      boolean allCovered = false;

      //nuevo
      //mientras no se hayan cubierto todos los mintérminos de para la
      //conclusión que se está tratando...
      while (!allCovered) {
        //...busca mintérminos o implicantes en la lista obtenida tras aplicar
        //el método de simplificación tabular, comenzando por aquellas
        //implicantes que agrupan más mintérminos...
        for (int i = (l.size() - 1); i >= 0 && !allCovered; i--) {



          //System.out.println("\nMiramos una iteración más para atrás...\n");





          //...obtiene el conjunto de mintérminos o implicantes de la iteración
          //actual de algoritmo
          Set s = (Set) l.get(i);
          //mintérmino o implicante elegido de entre los disponibles en la
          //iteración actual
          XfspMinterm selectedMinterm = null;
          //número de mintérminos cubiertos por el mintérmino o implicante
          //seleccionado
          int newCoveredMinterms = 0;
          //indica si se ha elegido un mintérmino o implicante de la iteración
          //que se está examinando (se inicializa por defecto a cierto para
          //poder entrar en el bucle siguiente)
          boolean added = true;
          //conjunto de mintérminos elegidos para ser añadidos al cubrimiento
          //mínimo de entre los disponibles en la iteración actual del
          //algoritmo de simplificación tabular
          Set addedMinterms = new HashSet();
          //mientras se sigan añadiendo mintérminos o implicantes al cubrimiento
          //mínimo y queden mintérminos por cubrir de entre los de la conclusión
          //que se está analizando...
          while (added && !allCovered) {
            //...indica que todavía no se ha añadido ningún mintérmino o
            //implicante en la iteración actual
            added = false;
            //iterador que permite recorrer las listas de mintérminos de la
            //iteración actual
            Iterator it1 = s.iterator();
            //mientras queden listas de mintérminos de la iteración actual por
            //ser procesados...
            while (it1.hasNext()) {
              //...obtiene una lista de mintérminos de la iteración actual
              List mintermsList = (List) it1.next();
              //para todos los conjuntos de mintérminos de la lista que se está
              //tratando...
              for (int j = 0; j < mintermsList.size() && !allCovered; j++) {
                //...obtiene un conjunto de mintérminos
                Set aux = (Set) mintermsList.get(j);
                //iterador que permite recorrer todos los mintérminos o
                //implicantes del conjunto de mintérminos actual
                Iterator it2 = aux.iterator();
                //para todos los mintérminos o implicantes del conjunto
                //actual...
                while (it2.hasNext()) {
                  //...obtiene un mintérmino o implicante del conjunto actual
                  XfspMinterm maux = (XfspMinterm) it2.next();
                  //obtiene el número de mintérminos de los cubiertos por el
                  //mintérmino o implicante actual que no han sido cubierto aún
                  //por ningún mintérmino o implicante de los seleccionados
                  //para el cubrimiento mínimo
                  int nonCovered = nonCoveredMinterms(maux);
                  //si el número de nuevos mintérminos cubiertos por el
                  //mintérmino o implicante actual es superior al mintérmino o
                  //implicante seleccionado temporalmente hasta el momento para
                  //ser añadido al cubrimiento mínimo y dicho mintérmino o
                  //implicante no ha sido añadido aún en la iteración actual...


                  //System.out.println("Implicante analizada " + maux);



                  if (nonCovered > newCoveredMinterms &&
                      !addedMinterms.contains(maux)) {



                    //System.out.println("Se elige provisionalmente para el cubrimiento porque es mejor que " + selectedMinterm);



                    //...selecciona de manera provisional el mintérmino o
                    //implicante actual...
                    selectedMinterm = maux;
                    //...indica que se ha elegido un mintérmino o implicante de
                    //entre los disponibles en la iteración actual...
                    added = true;
                    //...y almacena el número de mintérminos nuevos cubiertos
                    //por el seleccionado
                    newCoveredMinterms = nonCovered;
                  }
                  //si el número de nuevos mintérminos cubiertos por el
                  //mintérmino o implicante actual es igual al mintérmino o
                  //implicante seleccionado temporalmente hasta el momento para
                  //ser añadido al cubrimiento mínimo y dicho mintérmino o
                  //implicante no ha sido añadido aún en la iteración actual y
                  //ya se había seleccionado un mintérmino o implicante
                  //anteriormente (esto evita comportamientos extraños para el
                  //primer mintérmino o implicante tratado cuando aún no se ha
                  //seleccionado ninguno anteriormente)...
                  else if (nonCovered == newCoveredMinterms &&
                           !addedMinterms.contains(maux) && selectedMinterm != null) {
                    //...si el número de mintérminos cubiertos por el actual es
                    //mayor que el que cubría el mintérmino o implicante que
                    //había sido seleccionado anteriormente...
                    if (maux.getCoveredMinterms().size() >
                        selectedMinterm.getCoveredMinterms().size()) {
                      //...selecciona de manera provisional el mintérmino o
                      //implicante actual...
                      selectedMinterm = maux;
                      //...e indica que se ha elegido un mintérmino o implicante
                      //de entre los disponibles en la iteración actual
                      added = true;
                    }
                  }
                }
              }
            }
            //si se ha seleccionado algún mintérmino o implicante de la
            //iteración actual...
            if (added) {


              //System.out.println("se ha elegido definitivamente para el cubrimiento mínimo " + selectedMinterm);


              //...añade el mintérmino o implicante seleccionado al conjunto de
              //mintérminos o implicantes añadidos de los disponibles en la
              //iteración actual
              addedMinterms.add(selectedMinterm);
              //...y también lo añade al cubrimiento mínimo encontrado
              ( (Set) minimalCovering.get(conc)).add(selectedMinterm);
              //añade al los mintérminos cubiertos por el cubrimiento mínimo
              //actual los que cubre el mintérmino o implicante seleccionado
              coveredMinterms.addAll(selectedMinterm.getCoveredMinterms());


              //tratamiento para redundantes
              XfspMinterm redundantMinterm=findRedundantMinterms((Set) minimalCovering.get(conc));
              while(redundantMinterm!=null){
                System.out.println("El mintermino " + redundantMinterm + " es redundante!!!!!!");
                ((Set) minimalCovering.get(conc)).remove(redundantMinterm);
                redundantMinterm=findRedundantMinterms((Set) minimalCovering.get(conc));
              }


              //si el número de mintérminos cubiertos por el cubrimiento minímo
              //iguala a los mintérminos que tenía que cubrir para la conclusión
              //actual...
              if (coveredMinterms.size() == initialMinterms.size()) {
                //...termina con la búsqueda del cubrimiento mínimo
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
             //mientras no se hayan cubierto todos los mintérminos de la conclusión
             //que se está tratando...
             while (!allCovered) {
        //...examina todas las iteraciones de la conclusión actual, empezando
        //por la última, que es la que contiene implicantes mayores
        for (int i = (l.size() - 1); i >= 0 && !allCovered; i--) {
          //obtiene el conjunto de mintérminos correspondiente a la iteración
          //que se está examinando
          Set s = (Set) l.get(i);
          //examina todas las implicantes de la iteración
          Iterator it1 = s.iterator();
          while (it1.hasNext()) {
            //obtiene una lista de implicantes
            List mintermsList = (List) it1.next();
            //trata todos las implicantes de la lista mientras no hayan sido
            //cubiertos todos los mintérminos
            for (int j = 0; j < mintermsList.size() && !allCovered; j++) {
              //obtiene una lista de mintérminos
              Set saux = (Set) mintermsList.get(j);
              //itera sobre todos los mintérminos
              Iterator it2 = saux.iterator();
              while (it2.hasNext()) {
                //mintérmino actual
                XfspMinterm maux = (XfspMinterm) it2.next();
                //si no han sido cubiertos todos los mintérminos iniciales...
                if (!allMintermsCovered(maux.getCoveredMinterms())) {
                  //añade el mintérmino a la cubertura de la conclusión tratada
                  ( (Set) minimalCovering.get(conc)).add(maux);




                  System.out.println("se añade el mintermino " + maux);
                  System.out.println("porque cubre los minterminos");
                  it3 = maux.getCoveredMinterms().iterator();
                  while(it3.hasNext()){
                    System.out.println(it3.next());
                  }






                  //añade al conjunto de mintérminos cubiertos aquellos que
                  //cubre la implicante que se acaba de añadir
                  coveredMinterms.addAll(maux.getCoveredMinterms());
                  //si el conjunto de mitérminos cubiertos ha alcanzado en
                  //tamaño al de mintérminos iniciales para la conclusión
                  //tratada...
                  if (coveredMinterms.size() == initialMinterms.size()) {
                    //...se indica que ya se han cubierto todos los mintérminos
                    //de la conclusión tratada
                    allCovered = true;
                  }
                }
              }
            }
          }
        }
             }*/





      /*
            System.out.println("*** mintérminos cubiertos ***");
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

    //devuelve el cubrimiento mínimo encontrado
    return minimalCovering;
  }



  /**
   * <p> <b>Descripción:</b> busca en un conjunto de mintérminos e implicantes
   * uno que sea redundante, es decir, para el que todos los mintérminos
   * cubiertos por él ya sean cubiertos por el resto de los miembros del
   * conjunto.
   * @param minimalCovering Conjunto de mintérminos o implicantes.
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
   * <p> <b>Descripción:</b> calcula el conjunto de mintérminos cubiertos por
   * todos las implicantes de un conjunto dado.
   * @param mintermsSet Conjunto de mintérminos o implicantes.
   * @return Conjunto de mintérminos cubiertos por todas las implicantes del
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
   * <p> <b>Descripción:</b> calcula el conjunto de mintérminos iniciales para
   * una determinada conclusión.
   * @param minterms Conjunto de mintérminos asociados a una conclusión.
   * @return Conjunto inicial de mintérminos.
   *
   */
  private Set calculateInitialMinterms(Set init) {
    //almacena el conjunto de mintérminos iniciales asociados a una conclusión
    Set initialMinterms = new HashSet();
    //itera sobre el conjunto de listas de mintérminos de entrada
    Iterator it = init.iterator();
    while (it.hasNext()) {
      //obtiene una nueva lista de mintérminos del conjunto de entrada...
      List aux = (List) it.next();
      //...y añade todos los mintérminos de dicha lista al conjunto de
      //mintérminos iniciales
      for (int i = 0; i < aux.size(); i++) {
        initialMinterms.addAll( (Set) aux.get(i));
      }
    }
    return initialMinterms;
  }

  /**
   * <p> <b>Descripción:</b> calcula el número de mintérminos de los cubiertos
   * por un mintérmino o implicante que no ha sido cubierto aún por el
   * cubrimiento mínimo actual.
   * @param minterm Mintérmino o implicante para el que se calculará cuántos
   * mintérminos de los cubiertos por él no han sido cubiertos aún por el
   * cubrimiento mínimo actual.
   * @return Número de mintérminos cubiertos por un mintérmino o implicante que
   * no han sido cubiertos por el cubrimiento mínimo actual.
   *
   */
  private int nonCoveredMinterms(XfspMinterm minterm) {
    //número de mintérminos cubiertos por el mintérmino o implicante que no han
    //sido cubiertos aún por el cubrimiento mínimo actual (se inicializa a 0)
    int result = 0;
    //iterador que permite acceder a los mintérminos cubiertos por el mintérmino
    //o implicante analizado
    Iterator it = minterm.getCoveredMinterms().iterator();
    //para todos los mintérminos cubiertos por el mintérmino o implicante...
    while (it.hasNext()) {
      //...si el mintérmino actual aún no ha sido cubierto por el cubrimiento
      //mínimo actual...
      if (!coveredMinterms.contains(it.next())) {
        //...incrementa el número de mintérminos no cubiertos por el cubrimiento
        //mínimo que son cubiertos por el mintérmino o implicante analizado
        result++;
      }
    }
    return result;
  }
}
