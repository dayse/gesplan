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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import xfuzzy.lang.Conclusion;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.Relation;
import xfuzzy.lang.Rule;
import xfuzzy.lang.Rulebase;
import xfuzzy.lang.Variable;

/**
 * <p> <b>Descripci�n:</b> construye un conjunto de reglas para una determinada
 * conclusi�n a partir del cubrimiento m�nimo de sus mint�rminos.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 2.2
 * @see XfspModel
 * @see XfspTabularSimplifier
 * @see XfspRulebaseTabularDirector
 *
 */
public class XfspRulebaseTabularBuilder {

  //nueva base de reglas que se va a construir
  private Rulebase newRulebase;

  //indica si se deben de mantener las funciones de pertenencia situadas en los
  //extremos del universo de discurso de las variables
  private boolean maintainExtremeMF;

  /**
   * <p> <b>Descripci�n:</b> crea un constructor de reglas a partir de un
   * cubrimiento m�nimo de los mint�rminos asociados a una conclusi�n.
   * @param maintainExtremeMF Si vale cierto se mantienen las funciones de
   * pertenencia en los extremos de cada tipo y si vale falso no lo hace.
   * @param rulebase Base de reglas original para la que se construir� una
   * nueva base de reglas equivalente.
   *
   */
  public XfspRulebaseTabularBuilder(boolean maintainExtremeMF, Rulebase rulebase) {
    //indica si se deben mantener las funciones de pertenencia que est�n en los
    //extremos del universo de discurso de las variables o, por el contrario, si
    //pueden ser eliminadas
    this.maintainExtremeMF = maintainExtremeMF;
    //inicializa la nueva base de reglas clonando la original...


    newRulebase = new Rulebase(rulebase.getName());
    newRulebase.setOperatorset(rulebase.getOperatorset());
    for(int i=0;i<rulebase.getInputs().length;i++){
      newRulebase.addInputVariable(rulebase.getInputs()[i]);
    }
    for(int i=0;i<rulebase.getOutputs().length;i++){
      newRulebase.addOutputVariable(rulebase.getOutputs()[i]);
    }



    //newRulebase = (Rulebase) rulebase.clone();
    //...y le quita todas las reglas que contiene
    //newRulebase.removeAllRules();
  }

  /**
   * <p> <b>Descripci�n:</b> devuelve la base de reglas que se ha ido
   * construyendo al tratar cada uno de los mint�rminos que se le han pasado.
   * @return Base de reglas construida de forma incremental.
   *
   */
  public Rulebase getRulebase() {
    return newRulebase;
  }

  /**
   * <p> <b>Descripci�n:</b> construye un conjunto de reglas para una
   * conclusi�n a partir de los mint�rminos que forman el cubrimiento m�nimo.
   * @param conc Conclusi�n de las reglas que se van a crear.
   * @param minterms Mint�rminos que forman el cubrimiento para la conclusi�n
   * anterior.
   *
   */
  public void buildRule(Conclusion conc, Set minterms) {



    System.out.println("Para la conclusi�n " + conc.toXfl() + " el cubrimiento m�nimo encontrado es:");
    Iterator it1 = minterms.iterator();
    while(it1.hasNext()){
      System.out.println("" + (XfspMinterm) it1.next());
    }




    //premisa de la regla que se est� creando en cada momento
    Relation premise = null;
    //cuenta el n�mero de mint�rminos que se han tratado
    int numMinterms = 0;
    //iterador que permite recorrer todos los mint�rminos que forman el
    //cubrimiento m�nimo para una conclusi�n
    Iterator it = minterms.iterator();
    //mientras quedan mint�rminos por ser tratados...
    while (it.hasNext()) {
      //...obtiene un nuevo mint�rmino
      XfspMinterm minterm = (XfspMinterm) it.next();
      //obtiene el conjunto la relaci�n asociada al mint�rmino anterior
      Relation mintermRel = buildMinterm(minterm);
      //si se ha tratado alg�n mint�rmino anteriormente...
      if (numMinterms > 0) {
        //...la premisa la formar� la disyunci�n de la premisa anterior y la
        //relaci�n asociada al mint�rmino actual
        premise = Relation.create(Relation.OR, premise, mintermRel, null, null,
                                  newRulebase);
      }
      //en caso de que sea el primer mint�rmino tratado...
      else if (numMinterms == 0) {
        //...la premisa la formar� la relaci�n asociada al mint�rmino actual
        premise = mintermRel;
      }
      //incrementa el n�mero de mint�rminos tratados
      numMinterms++;
    }
    //crea una nueva regla que tiene como premisa la que se ha creado a partir
    //del conjunto de mint�rminos tratado...
    Rule rule = new Rule(premise);
    //...y le a�ade la conclusi�n
    rule.add(conc);
    //a�ade la regla creada a la base de reglas que se est� creando
    newRulebase.addRule(rule);







    //TODO - eliminar esto
    //System.out.println("Para la conclusi�n " + conc.toXfl() + " el cubrimiento m�nimo encontrado es:");
    //Iterator it1 = minterms.iterator();
    //while(it1.hasNext()){
      //System.out.println("" + (XfspMinterm) it1.next());
    //}






  }

  /**
   * <p> <b>Descripci�n:</b> construye una relaci�n a partir de un mint�rmino.
   * @param minterm Mint�rmino a partir del cual se construir� la nueva
   * relaci�n.
   * @return Relaci�n que expresa los valores que pueden tomar todas las
   * variables que aparecen en el mint�rmino tratado.
   *
   */
  private Relation buildMinterm(XfspMinterm minterm) {
    //map cuya clave son las variables del mint�rmino y cuyos valores son las
    //funciones que toman dichas variables en el mint�rmino
    Map variables = minterm.getVariables();
    //relaci�n obtenida a partir del mint�rmino
    Relation result = null;
    //cuenta el n�mero de variables que se han tratado
    int numVars = 0;
    //iterador que permite obtener todas las variables del mint�rmino
    Iterator it1 = variables.keySet().iterator();
    //para todas las variables del mint�rmino que se est� tratando...
    while (it1.hasNext()) {
      //...obtiene una variable...
      Variable var = (Variable) it1.next();
      //...y el conjunto de funciones de pertenencia que toma dicha variable
      Set s = (Set) variables.get(var);




      //TODO - quitar esto
      System.out.println("Construyendo premisa para el mint�rmino " + minterm.toString() + " *****************");





      //construye una relaci�n para la variable que se est� tratando a partir
      //del conjunto de funciones de pertenencia que puede tomar
      Relation rel = buildVariable(var, s);
      //si no se ha tratado ninguna variable anteriormente...
      if (numVars == 0) {
        //...la relaci�n resultado ser� la que se acaba de construir
        result = rel;
      }
      //si se ha tratado alguna variable anteriormente pero no se ha contruido
      //ninguna relaci�n...
      else if (numVars > 0 && result == null) {
        //...la relaci�n resultado ser� la que se acaba de construir
        result = rel;
      }
      //si se ha tratado alguna variable anteriormente y se ha podido contruir
      //alguna relaci�n...
      else if (numVars > 0 && rel != null) {
        //...la nueva relaci�n ser� igual a la conjunci�n de la relaci�n
        //antigua y la relaci�n que se acaba de construir
        result = Relation.create(Relation.AND, result, rel, null, null,
                                 newRulebase);
      }
      //incremente el n�mero de variables que se han tratado
      numVars++;
    }

    if(result==null){
      maintainExtremeMF = true;
      result = buildMinterm(minterm);
      maintainExtremeMF = false;
    }

    System.out.println("la premisa construida es " + result.toXfl());


    //devuelve la relaci�n creada
    return result;
  }

  /**
   * <p> <b>Descripci�n:</b> construye una relaci�n a partir de una variable y
   * el conjunto de funciones de pertenencia que dicha variable puede tomar.
   * @param var Variable para la que se crear� la relaci�n.
   * @param MFs Funciones de pertenenica que puede tomar la variable.
   * @return Relaci�n que expresa las funciones de pertenencia que puede tomar
   * la variable como valor.
   *
   */
  private Relation buildVariable(Variable var, Set MFs) {
    //relaci�n que se construir� a partir de la variable y el conjunto de
    //funciones de pertenencia que puede tomar
    Relation result = null;
    //si la variable tiene asignadas todas las funciones de pertenencia de su
    //tipo menos una y, adem�s, no se deben mantener las funciones de pertenecia
    //de los extremos del tipo...
    if (!maintainExtremeMF && MFs.size() == (var.getType().getMembershipFunctions().length - 1)) {
      //indica si se ha encontrado la �nica funci�n de pertenencia que no se
      //asignado a la variable
      boolean found = false;
      //guardar� la �nica funci�n de pertenencia no asignada a la variable
      LinguisticLabel mf = null;
      //funciones de pertenencia del tipo al que pertenece la variable
      LinguisticLabel [] mfs = var.getType().getMembershipFunctions();
      //para todas las funciones de pertenencia del tipo de la variable y
      //mientras que no se haya encontrado a�n la �nica funci�n de pertenencia
      //no asignada a la variable...
      for (int i = 0; i < mfs.length && !found; i++) {
        //...obtiene la siguiente funci�n de pertenencia del tipo
        mf = mfs[i];
        //si el conjunto de funciones de pertenencia asignadas a la variable no
        //contiene la funci�n de pertenencia actual...
        if (!MFs.contains(new Integer(i))) {
          //...ha encontrado la �nica funci�n de pertenencia no asignada a la
          //variable y termina la b�squeda
          found = true;
        }
      }
      //la relaci�n correspondiente a la asignaci�n de la variable analizada es
      //de tipo ISNOT con la funci�n de pertenencia que se ha encontrado
      result = Relation.create(Relation.ISNOT, null, null, var, mf, newRulebase);
    }
    else if (maintainExtremeMF || (MFs.size() < (var.getType().getMembershipFunctions().length - 1) && !maintainExtremeMF)) {

      //System.out.println("variable " + var + " con funciones de pertenencia " + MFs);

      TreeSet sortedMFs = new TreeSet(MFs);

      //Iterator it = sortedMFs.iterator();
      //Integer lower = null;
      //Integer upper = null;
      Relation newRel = null;



//System.out.println("Para la variable " + var + " la menor funci�n de pertenencia es " + sortedMFs.first() + " y la mayor es " + sortedMFs.last());

      if (((Integer)sortedMFs.first()).intValue() == ((Integer)sortedMFs.last()).intValue()) {
         newRel = Relation.create(Relation.IS, null, null, var, var.getType().getMembershipFunctions()[((Integer)sortedMFs.first()).intValue()], newRulebase);
      }
      else if(((Integer)sortedMFs.last()).intValue() == (var.getType().getMembershipFunctions().length-1) && !maintainExtremeMF){
        newRel = Relation.create(Relation.GR_EQ, null, null, var, var.getType().getMembershipFunctions()[((Integer)sortedMFs.first()).intValue()], newRulebase);
      }
      else if(((Integer)sortedMFs.first()).intValue() == 0 && !maintainExtremeMF){
        newRel = Relation.create(Relation.SM_EQ, null, null, var, var.getType().getMembershipFunctions()[((Integer)sortedMFs.last()).intValue()], newRulebase);
      }
      else if(((Integer)sortedMFs.first()).intValue() == 0 && ((Integer)sortedMFs.last()).intValue() == var.getType().getMembershipFunctions().length && maintainExtremeMF){
        Relation lowerRel = Relation.create(Relation.GR_EQ, null, null, var,
                                            var.getType().
                                            getMembershipFunctions()[((Integer)sortedMFs.first()).intValue()], newRulebase);
        Relation upperRel = Relation.create(Relation.SM_EQ, null, null, var,
                                            var.getType().
                                            getMembershipFunctions()[((Integer)sortedMFs.last()).intValue()], newRulebase);
        newRel = Relation.create(Relation.AND, lowerRel, upperRel, null, null,
                                 newRulebase);
      }
      else { //if(((Integer)sortedMFs.first()).intValue() < ((Integer)sortedMFs.last()).intValue() && maintainExtremeMF) {
        Relation lowerRel = Relation.create(Relation.GR_EQ, null, null, var,
                                            var.getType().
                                            getMembershipFunctions()[((Integer)sortedMFs.first()).intValue()], newRulebase);
        Relation upperRel = Relation.create(Relation.SM_EQ, null, null, var,
                                            var.getType().
                                            getMembershipFunctions()[((Integer)sortedMFs.last()).intValue()], newRulebase);
        newRel = Relation.create(Relation.AND, lowerRel, upperRel, null, null,
                                 newRulebase);
      }
      if (result != null) {
        result = Relation.create(Relation.OR, result, newRel, null, null,
                                 newRulebase);
      }
      else {
        result = newRel;
      }
    }










/*
      if(it.hasNext()){
        lower = (Integer) it.next();
        upper = lower;
      }

      while (it.hasNext()) {
        Integer aux = (Integer) it.next();

        if (aux.intValue() == (upper.intValue() + 1)) {
          //System.out.println("seguimos mirando funciones de pertenencia...");
          upper = aux;
          //System.out.println("ahora upper vale " + upper.intValue() + " y lower vale " + lower.intValue());
        }
        else{
          System.out.println("no hemos entrado por la primera rama!!! revisar!!!");
        }

        //estas opciones se toman cuando ya no se pueden seguir agrupando
        //funciones de pertenencia
        if (aux.intValue() > (upper.intValue() + 1) &&
            upper.intValue() > lower.intValue()) {

          //System.out.println("hemos terminado por aki..");



          Relation lowerRel = Relation.create(Relation.GR_EQ, null, null, var,
                                              var.getType().
                                              getMembershipFunctions()[
                                              lower.intValue()], newRulebase);
          Relation upperRel = Relation.create(Relation.SM_EQ, null, null, var,
                                              var.getType().
                                              getMembershipFunctions()[
                                              upper.intValue()], newRulebase);
          newRel = Relation.create(Relation.AND, lowerRel, upperRel, null, null,
                                   newRulebase);





          //System.out.println("regla creada en la primera opci�n " + newRel.toXfl());

          if (result != null) {
            //result = Relation.create(Relation.AND, result, newRel, null, null,
            //                         newRulebase);

            result = Relation.create(Relation.OR, result, newRel, null, null,
                                     newRulebase);
          }
          else {
            result = newRel;
          }
          lower = aux;
          upper = aux;
        }
        else if (aux.intValue() > (upper.intValue() + 1) &&
                 upper.intValue() == lower.intValue()) {
          newRel = Relation.create(Relation.IS, null, null, var,
                                   var.getType().getMembershipFunctions()[lower.
                                   intValue()], newRulebase);

          //System.out.println("regla creada en la quinta opcion " + newRel.toXfl());

          if (result != null) {
            result = Relation.create(Relation.OR, result, newRel, null, null,
                                     newRulebase);
          }
          else {
            result = newRel;
          }
          lower = aux;
          upper = aux;
        }
      }

      if (!it.hasNext() &&
          upper.intValue() == var.getType().getMembershipFunctions().length &&
          upper.intValue() > lower.intValue()) {

        //System.out.println("ya hemos terminado por ahora...");

        newRel = Relation.create(Relation.GR_EQ, null, null, var,
                                 var.getType().getMembershipFunctions()[
                                 lower.intValue()], newRulebase);



        //System.out.println("regla creada al final en la primera opcion " + newRel.toXfl());




        if (result != null) {
          result = Relation.create(Relation.OR, result, newRel, null, null,
                                   newRulebase);
        }
        else {
          result = newRel;
        }
      }
      if (!it.hasNext() &&
          upper.intValue() < var.getType().getMembershipFunctions().length &&
          upper.intValue() > lower.intValue()) {

        Relation lowerRel = Relation.create(Relation.GR_EQ, null, null, var,
                                            var.getType().
                                            getMembershipFunctions()[
                                            lower.intValue()], newRulebase);
        Relation upperRel = Relation.create(Relation.SM_EQ, null, null, var,
                                            var.getType().
                                            getMembershipFunctions()[
                                            upper.intValue()], newRulebase);
        newRel = Relation.create(Relation.AND, lowerRel, upperRel, null, null,
                                 newRulebase);

        //System.out.println("regla creada al final en la segunda opcion " + newRel.toXfl());

        if (result != null) {
          result = Relation.create(Relation.OR, result, newRel, null, null,
                                   newRulebase);
        }
        else {
          result = newRel;
        }
      }

      else if (!it.hasNext() && upper.intValue() == lower.intValue()) {
        newRel = Relation.create(Relation.IS, null, null, var,
                                 var.getType().getMembershipFunctions()[lower.
                                 intValue()], newRulebase);




        //System.out.println("regla creada al final en la tercera opcion " + newRel.toXfl());




        if (result != null) {
          result = Relation.create(Relation.OR, result, newRel, null, null,
                                   newRulebase);
        }
        else {
          result = newRel;
        }
      }

    }
*/



    //parte antigua
    /*
           Relation newRel;
           int numMFs = 0;
           while (it2.hasNext()) {
      Integer i = (Integer) it2.next();
      LinguisticLabel mf = var.getType().getMembershipFunctions()[i.intValue()];
      newRel = Relation.create(Relation.IS, null, null, var, mf, newRulebase);
      if (numMFs > 0) {
        result = Relation.create(Relation.OR, result, newRel, null, null,
                                 newRulebase);
      }
      else if (numMFs == 0) {
        result = newRel;
      }
      numMFs++;
           }*/

    return result;
  }
}
