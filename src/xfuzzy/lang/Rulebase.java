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

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//BASE DE REGLAS			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.lang;

/**
 * Clase que describe una base de reglas difusas
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Rulebase implements Cloneable {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Conjunto de operadores utilizados en la base de reglas
	 */
	public Operatorset operation;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre de la base de reglas
	 */
	private String name;
	
	/**
	 * Lista de variables de entrada de la base de reglas
	 */
	private Variable inputvar[];
	
	/**
	 * Lista de variables de salida de la base de reglas
	 */
	private Variable outputvar[];
	
	/**
	 * Lista de reglas que forman la base de reglas
	 */
	private Rule rule[];
	
	/**
	 * Contador de enlaces (usos) de la base de reglas
	 */
	private int link;
	
	/**
	 * Indicador de que en un determinado momento, la base de reglas se está
	 * editando
	 */
	private boolean editing = false;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public Rulebase(String name) {
		this.name = name;
		this.operation = null;
		this.inputvar = new Variable[0];
		this.outputvar = new Variable[0];
		this.rule = new Rule[0];
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el identificador de la base de reglas
	 */
	public String toString() {
		return new String(this.name);
	}

	/**
	 * Compara un nombre con el identificador de la base de reglas
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		Rulebase clone = new Rulebase(this.name);
		clone.setOperatorset(this.operation);
		Variable cloneinputvar[] = new Variable[inputvar.length];
		Variable cloneoutputvar[] = new Variable[outputvar.length];
		for(int i=0; i<inputvar.length; i++) {
			String varname = inputvar[i].getName();
			Type vartype = inputvar[i].getType();
			cloneinputvar[i] = new Variable(varname,vartype, Variable.INPUT);
			clone.addInputVariable(cloneinputvar[i]);
		}
		for(int i=0; i<outputvar.length; i++) {
			String varname = outputvar[i].getName();
			Type vartype = outputvar[i].getType();
			cloneoutputvar[i] = new Variable(varname,vartype, clone);
			clone.addOutputVariable(cloneoutputvar[i]);
		}
		for(int i=0; i<rule.length; i++) clone.addRule( (Rule) rule[i].clone(clone));
		for(int i=0; i<inputvar.length; i++)
			clone.exchange(inputvar[i], cloneinputvar[i]);
		for(int i=0; i<outputvar.length; i++)
			clone.exchange(outputvar[i], cloneoutputvar[i]);
		return clone;
	}

	/**
	 * Elimina los enlaces para poder eliminar la base de reglas
	 */
	public void dispose() {
		while(rule.length>0) remove(rule[0]);
		while(inputvar.length>0) removeInputVar(inputvar[0]);
		while(outputvar.length>0) removeOutputVar(outputvar[0]);
		setOperatorset(null);
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a los campos                                             //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre de la base de reglas
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Obtiene el conjunto de operadores utilizado en la base
	 */
	public Operatorset getOperatorset() {
		return this.operation;
	}

	/**
	 * Obtiene la lista de variables de entrada
	 */
	public Variable[] getInputs() {
		return this.inputvar;
	}

	/**
	 * Obtiene la lista de variables de salida	
	 */
	public Variable[] getOutputs() {
		return this.outputvar;
	}

	/**
	 *  Obtiene la lista de reglas
	 */
	public Rule[] getRules() {
		return this.rule;
	}

	/**
	 * Verifica si la base de reglas esta siendo utilizada
	 */
	public boolean isLinked() {
		return (this.link>0);
	}

	/**
	 * Verifica si la base de reglas esta siendo editada
	 */
	public boolean isEditing() {
		return this.editing;
	}

	//----------------------------------------------------------------------------//
	// Métodos de asignación de los campos                                        //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el conjunto de operadores utilizado
	 */
	public void setOperatorset(Operatorset op) {
		if(this.operation != null) this.operation.unlink();
		this.operation = op;
		if(this.operation != null) this.operation.link();
	}

	/**
	 * Incrementa el contador de enlaces (usos) del objeto
	 */
	public void link() {
		this.link++;
	}

	/**
	 * Decrementa el contador de enlaces (usos) del objeto
	 */
	public void unlink() {
		this.link--;
	}

	/**
	 * Asigna el contenido del campo de edicion
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	/**
	 * Asigna el nombre de la base de reglas
	 */
	public void setName(String name) {
		this.name = name;
	}

	//----------------------------------------------------------------------------//
	// Métodos para añadir valores                                                //
	//----------------------------------------------------------------------------//

	/**
	 * Añade una variable de entrada
	 */
	public void addInputVariable(Variable var) {
		Variable av[] = new Variable[this.inputvar.length+1];
		System.arraycopy(this.inputvar,0,av,0,this.inputvar.length);
		av[this.inputvar.length] = var;
		this.inputvar = av;
	}

	/**
	 * Añade una variable de salida
	 */
	public void addOutputVariable(Variable var) {
		Variable av[] = new Variable[this.outputvar.length+1];
		System.arraycopy(this.outputvar,0,av,0,this.outputvar.length);
		av[this.outputvar.length] = var;
		this.outputvar = av;
	}

	/**
	 *  Añade una regla	
	 */
	public void addRule(Rule rule) {
		Rule arl[] = new Rule[this.rule.length+1];
		System.arraycopy(this.rule,0,arl,0,this.rule.length);
		arl[this.rule.length] = rule;
		this.rule = arl;
	}

	//----------------------------------------------------------------------------//
	// Métodos para eliminar valores                                              //
	//----------------------------------------------------------------------------//

	/**
	 * Elimina una regla
	 */
	public void remove(Rule rl) {
		int index = -1;
		for(int i=0; i<rule.length; i++) if(rule[i] == rl) index = i;
		if(index == -1) return;
		Rule[] aux = new Rule[rule.length-1];
		System.arraycopy(rule,0,aux,0,index);
		System.arraycopy(rule,index+1,aux,index,aux.length-index);
		rl.dispose();
		rule = aux;
	}

	/**
	 * Elimina todas las reglas	
	 */
	public void removeAllRules() {
		for(int i=0; i<this.rule.length; i++) this.rule[i].dispose();
		this.rule = new Rule[0];
	}

	/**
	 * Elimina una variable de entrada	
	 */
	public void removeInputVar(Variable var) {
		boolean contains = false;
		if(inputvar.length == 0) return;
		Variable[] aux = new Variable[inputvar.length-1];
		for(int i=0,j=0; i<inputvar.length; i++)
			if(inputvar[i] != var) { aux[j]=inputvar[i]; j++; } else contains = true;
		if(contains) { var.getType().unlink(); inputvar = aux; }
	}

	/**
	 * Elimina una variable de salida
	 */
	public void removeOutputVar(Variable var) {
		boolean contains = false;
		if(outputvar.length == 0) return;
		Variable[] aux = new Variable[outputvar.length-1];
		for(int i=0,j=0; i<outputvar.length; i++)
			if(outputvar[i] != var) { aux[j]=outputvar[i]; j++; } else contains = true;
		if(contains) { var.getType().unlink(); outputvar = aux; }
	}

	//----------------------------------------------------------------------------//
	// Métodos de búsqueda e intercambio                                          //
	//----------------------------------------------------------------------------//
	
	/**
	 * Busca una variable
	 */
	public Variable searchVariable(String varname) {
		for(int i=0; i<this.inputvar.length; i++)
			if( this.inputvar[i].equals(varname) ) return this.inputvar[i];
		for(int i=0; i<this.outputvar.length; i++)
			if( this.outputvar[i].equals(varname) ) return this.outputvar[i];
		return null;
	}

	/**
	 * Calcula el numero de veces que la i-esima variable de salida aparece en el
	 * consecuente de las reglas
	 */
	public int computeOutputSize(int i) {
		int counter=0;
		for(int j=0; j<rule.length; j++) {
			Conclusion conc[] = rule[j].getConclusions();
			for(int k=0; k<conc.length; k++)
				if(conc[k].getVariable() == outputvar[i]) counter++;
		}
		return counter;
	}

	/**
	 * Intercambia dos funciones de pertenencia
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		for(int i=0; i<rule.length; i++) rule[i].exchange(oldmf,newmf);
	}

	/**
	 * Intercambia dos variables
	 */
	public void exchange(Variable oldvar, Variable newvar) {
		for(int i=0; i<rule.length; i++) rule[i].exchange(oldvar,newvar);
	}

	//----------------------------------------------------------------------------//
	// Métodos que genaran código                                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripción XFL3 de la base de reglas
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		String code = "rulebase "+this.name+" (";
		for(int i=0; i<inputvar.length; i++)
			code += (i==0?"" :", ")+inputvar[i].toXfl();
		code += " : ";
		for(int i=0; i<outputvar.length; i++)
			code += (i==0?"" :", ")+outputvar[i].toXfl();
		code += ") ";
		if(!this.operation.isDefault()) code += "using "+this.operation.getName();
		code += " {"+eol;
		for(int i=0; i<this.rule.length; i++) code += this.rule[i].toXfl();
		code += " }"+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	// Métodos de cálculo del proceso de inferencia                               //
	//----------------------------------------------------------------------------//

	/**
	 * Realiza la inferencia con los valores de las variables de entrada y 
	 * almacena los valores de las variables de salida,	el grado de activacion 
	 * de cada regla y la funcion de pertenencia agregada de cada salida		
	 */
	public void compute(Variable[] ivar, Variable[] ovar, double[] degree,
			MemFunc[] value) {
		double ival[] = new double[ivar.length];
		for(int i=0; i<this.inputvar.length; i++)
			try{ ival[i] = ivar[i].getCrispValue(); } catch(Exception ex) { ival[i]=0; }

			for(int i=0; i<this.outputvar.length; i++) this.outputvar[i].reset();
			for(int i=0; i<this.inputvar.length; i++)
				this.inputvar[i].set(ivar[i].getValue());

			try { for(int i=0; i<this.rule.length;i++) degree[i]=this.rule[i].compute(); }
			catch(XflException e) {}

			for(int i=0; i<this.outputvar.length; i++)
				((AggregateMemFunc) outputvar[i].getValue()).input = ival;

			if(this.operation.defuz.isDefault())
				for(int i=0; i<this.outputvar.length; i++)
					ovar[i].set(outputvar[i].getValue());
			else
				for(int i=0; i<this.outputvar.length; i++)
					ovar[i].set( ((AggregateMemFunc) outputvar[i].getValue()).defuzzify() );

			for(int i=0; i<this.outputvar.length; i++) value[i] = outputvar[i].getValue();
	}

	/**
	 * Calcula la derivada del proceso de inferencia
	 */
	public void derivative(double[] derror) throws XflException {
		for(int i=0; i<outputvar.length; i++) outputvar[i].derivative(derror[i]);
		for(int i=0; i<rule.length; i++) rule[i].derivative();
	}

	/**
	 * Calcula la derivada del proceso de inferencia
	 */
	public void derivative() throws XflException {
		for(int i=0; i<rule.length; i++) rule[i].derivative();
	}

	/**
	 * Actualiza el campo de ajustabilidad de las reglas
	 */
	public void setAdjustable() {
		for(int i=0; i<rule.length; i++) rule[i].setAdjustable();
	}

	/**
	 * Inicializa el grado de activación máxima de las reglas
	 */
	public void resetMaxActivation() {
		for(int i=0; i<rule.length; i++) rule[i].resetMaxActivation();
	}
}
