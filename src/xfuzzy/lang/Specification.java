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

package xfuzzy.lang;

import java.io.*;

/**
 * Descripción de la estructura de un sistema difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Specification {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre del sistema difuso
	 */
	private String name;
	
	/**
	 * Fichero donde se encuentra almacenada la descripción
	 */
	private File file;
	
	/**
	 * Lista de conjuntos de operadores definidas en el sistema difuso
	 */
	private Operatorset operation[];
	
	/**
	 * Lista de bloques no difusos definida en el sistema difuso
	 */
	private CrispBlockSet crisp;
	
	/**
	 * Lista de tipos de variables definidas en el sistma difuso
	 */
	private Type type[];
	
	/**
	 * Lista de bases de reglas definidas en el sistema difuso
	 */
	private Rulebase rulebase[];
	
	/**
	 * Estructura jerárquica del sistema difuso
	 */
	private SystemModule system;
	
	/**
	 * Lista de parámetros del sistema que se desean ajustar en un proceso
	 * de aprendizaje automático
	 */
	private Parameter[] adjustable;
	
	/**
	 * Marcador para indicar que el sistema ha sufrido modificaciones no
	 * almacenadas en su correspondiente archivo
	 */
	private boolean modified;
	
	/**
	 * Marcador para indicar que el sistema está siendo editado directamente
	 * por un objeto XfeditFileEditor
	 */
	private boolean fileediting;
	
	/**
	 * Referencia a la ventana de edición que está editando el sistema
	 */
	private Object editor;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor a partir de un fichero
	 */
	public Specification(File file) {
		String filename = file.getName();
		int index = filename.toLowerCase().lastIndexOf(".xfl");
		if(index <= 0) this.name = new String(filename);
		else this.name = filename.substring(0,index);
		this.file = file;
		this.operation = new Operatorset[0];
		this.type = new Type[0];
		this.rulebase = new Rulebase[0];
		this.crisp = new CrispBlockSet();
		this.system = new SystemModule();
		this.modified = false;
		this.editor = null;
		this.fileediting = false;
	}

	/**
	 * Constructor a partir de un nombre
	 */
	public Specification(String name) {
		this.name = name;
		this.file = null;
		this.operation = new Operatorset[0];
		this.type = new Type[0];
		this.rulebase = new Rulebase[0];
		this.crisp = new CrispBlockSet();
		this.system = new SystemModule();
		this.modified = false;
		this.editor = null;
		this.fileediting = false;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos de acceso a los campos                                             //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre del sistema difuso
	 */
	public String getName() {
		return new String(this.name);
	}

	/**
	 * Obtiene el fichero de almacenamiento del sistema difuso
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Obtiene la lista de conjuntos de operadores del sistema
	 */
	public Operatorset [] getOperatorsets() {
		return this.operation;
	}

	/**
	 * Obtiene la lista de tipos del sistema
	 */
	public Type [] getTypes() {
		return this.type;
	}

	/**
	 * Obtiene la lista de bases de reglas del sistema
	 */
	public Rulebase [] getRulebases() {
		return this.rulebase;
	}

	/**
	 * Obtiene la lista de bloques no difusos
	 */
	public CrispBlockSet getCrispBlockSet() {
		return this.crisp;
	}

	/**
	 * Obtiene la estructura modular del sistema difuso
	 */
	public SystemModule getSystemModule() {
		return this.system;
	}

	/**
	 * Obtiene la referencia a la ventana de edición del sistema
	 */
	public Object getEditor() {
		return this.editor;
	}

	/**
	 * Verifica si existen cambios no almacenados en el sistema
	 */
	public boolean isModified() {
		return this.modified;
	}

	/**
	 * Verifica si el sistema difuso está siendo editado
	 */
	public boolean isEditing() {
		return (this.editor!=null);
	}

	/**
	 * Verifica si la descripción XFL3 está siendo editada
	 */
	public boolean isFileEditing() {
		return this.fileediting;
	}

	//----------------------------------------------------------------------------//
	// Métodos para asignar los valores de los campos                             //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el fichero de almacenamiento del sistema
	 */
	public void setFile(File file) {
		String filename = file.getName();
		int index = filename.lastIndexOf(".xfl");
		if(index == -1) this.name = new String(filename);
		else this.name = filename.substring(0,index);
		this.file = file;
	}

	/**
	 * Asigna la lista de conjuntos de operadores del sistema
	 */
	public void setOperatorsets(Operatorset op[]) {
		this.operation = op;
	}

	/**
	 * Asigna la lista de tipos del sistema
	 */
	public void setTypes(Type type[]) {
		this.type = type;
	}

	/**
	 * Asigna la lista de bases de reglas del sistema
	 */
	public void setRulebases(Rulebase rulebase[]) {
		this.rulebase = rulebase;
	}

	/**
	 * Asigna la lista de bloques no difusos
	 */
	public void setCrispBlockSet(CrispBlockSet cbset) {
		this.crisp = cbset;
	}

	/**
	 * Asigna la estructura modular del sistema	
	 */
	public void setSystemModule(SystemModule system) {
		this.system = system;
	}

	/**
	 *  Asigna el marcador de modificado del sistema
	 */
	public void setModified(boolean modif) {
		this.modified = modif;
	}

	/**
	 * Asigna la referencia al editor del sistema
	 */
	public void setEditor(Object editor) {
		this.editor = editor;
	}

	/**
	 * Asigna el marcador de edición del código XFL3 del sistema
	 */
	public void setFileEditing(boolean editing) {
		this.fileediting = editing;
	}

	//----------------------------------------------------------------------------//
	// Métodos para añadir y eliminar componentes                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Añade un conjunto de operadores
	 */
	public void addOperatorset(Operatorset op) { 
		Operatorset aop[] = new Operatorset[this.operation.length+1];
		System.arraycopy(this.operation,0,aop,0,this.operation.length);
		aop[this.operation.length] = op;
		this.operation = aop;
	}

	/**
	 * Añade un tipo de variable
	 */
	public void addType(Type tp) {
		Type atp[] = new Type[this.type.length+1];
		System.arraycopy(this.type,0,atp,0,this.type.length);
		atp[this.type.length] = tp;
		this.type = atp;
	}

	/**
	 * Añade un base de reglas
	 */
	public void addRulebase(Rulebase rb) {
		Rulebase arb[] = new Rulebase[this.rulebase.length+1];
		System.arraycopy(this.rulebase,0,arb,0,this.rulebase.length);
		arb[this.rulebase.length] = rb;
		this.rulebase = arb;
	}

	/**
	 * Elimina un conjunto de operadores
	 */
	public void removeOperatorset(Operatorset op) {
		if(op.isLinked()) return;
		int i;
		for(i=0; i<this.operation.length ; i++) if(this.operation[i]==op) break;
		if(i == this.operation.length) return;
		Operatorset aop[] = new Operatorset[this.operation.length-1];
		System.arraycopy(this.operation,0,aop,0,i);
		System.arraycopy(this.operation,i+1,aop,i,this.operation.length-i-1);
		this.operation = aop;
	}

	/**
	 * Elimina un tipo de variable
	 */
	public void removeType(Type tp) {
		if(tp.isLinked()) return;
		int i;
		for(i=0; i<this.type.length ; i++) if(this.type[i]==tp) break;
		if(i == this.type.length) return;
		Type atp[] = new Type[this.type.length-1];
		System.arraycopy(this.type,0,atp,0,i);
		System.arraycopy(this.type,i+1,atp,i,this.type.length-i-1);
		this.type = atp;
	}

	/**
	 * Elimina un base de reglas
	 */
	public void removeRulebase(Rulebase rb) {
		if(rb.isLinked()) return;
		int i;
		for(i=0; i<this.rulebase.length ; i++) if(this.rulebase[i]==rb) break;
		if(i == this.rulebase.length) return;
		Rulebase arb[] = new Rulebase[this.rulebase.length-1];
		System.arraycopy(this.rulebase,0,arb,0,i);
		System.arraycopy(this.rulebase,i+1,arb,i,this.rulebase.length-i-1);
		this.rulebase = arb;
		rb.dispose();
	}

	//----------------------------------------------------------------------------//
	// Métodos para buscar e intercambiar componentes                             //
	//----------------------------------------------------------------------------//

	/**
	 * Intercambia dos bases de reglas
	 */
	public void exchangeRulebase(Rulebase oldrb, Rulebase newrb) {
		for(int i=0; i<this.rulebase.length ; i++)
			if(this.rulebase[i]==oldrb)  { this.rulebase[i] = newrb; oldrb.dispose(); }
		system.exchangeRulebase(oldrb, newrb);
	}

	/**
	 * Intercambia dos funciones de pertenencia
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		for(int i=0; i<this.rulebase.length; i++) 
			if(oldmf != null) rulebase[i].exchange(oldmf,newmf);
	}

	/**
	 * Busca un conjunto de operadores
	 */
	public Operatorset searchOperatorset(String opname) {
		for(int i=0; i<this.operation.length ; i++)
			if(this.operation[i].equals(opname))
				return this.operation[i];
		return null;
	}

	/**
	 * Busca un tipo de variable
	 */
	public Type searchType(String typename) {
		for(int i=0; i<this.type.length ; i++)
			if(this.type[i].equals(typename))
				return this.type[i];
		return null;
	}

	/**
	 * Busca una base de reglas
	 */
	public Rulebase searchRulebase(String rbname) {
		for(int i=0; i<this.rulebase.length ; i++)
			if(this.rulebase[i].equals(rbname))
				return this.rulebase[i];
		return null;
	}

	//----------------------------------------------------------------------------//
	// Métodos genéricos                                                          //
	//----------------------------------------------------------------------------//

	/**
	 * Compara una cadena con el nombre del sistema
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}

	/**
	 * Obtiene el nombre del sistema
	 */
	public String toString() {
		return new String(this.name);
	}

	/**
	 * Genera la descripción XFL3 del sistema
	 */
	public String toXfl() {
		String code = "";
		for(int i=0; i<this.operation.length; i++) code += this.operation[i].toXfl();
		for(int i=0; i<this.type.length; i++) code += this.type[i].toXfl();
		for(int i=0; i<this.rulebase.length; i++) code += this.rulebase[i].toXfl();
		if(this.crisp != null) code += this.crisp.toXfl();
		if(this.system != null) code += this.system.toXfl();
		return code;
	}

	/**
	 * Almacena la descripción XFL3 del sistema en su fichero
	 */
	
	public boolean save() {
		return XflSaver.save(this, this.file);
	}
    
	
	/**
	 * Almacena la descripción XFL3 del sistema en un fichero dado
	 */
	public boolean save_as(File file) {
		setFile(file);
		return save();
	}

	//----------------------------------------------------------------------------//
	// Métodos utilizados por el aprendizaje                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Actualiza los parámetros de los tipos de variables
	 */
	public void update() {
		for(int i=0; i<this.type.length; i++) this.type[i].update();
	}

	/**
	 * Actualiza el marcador de ajustable de los componentes
	 */
	public void setAdjustable() {
		for(int i=0; i<this.type.length; i++) this.type[i].setAdjustable();
		for(int i=0; i<this.rulebase.length; i++) this.rulebase[i].setAdjustable();
		int count = 0;
		int outputs = system.getOutputs().length;
		for(int i=0; i<type.length; i++) {
			Family fam[] = type[i].getFamilies();
			for(int j=0; j<fam.length; j++)  {
				Parameter param[] = fam[j].getParameters();
				for(int k=0; k<param.length; k++) if(param[k].isAdjustable()) count++;
			}

			ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
			for(int j=0; j<mf.length; j++) {
				Parameter param[] = mf[j].getParameters();
				for(int k=0; k<param.length; k++) if(param[k].isAdjustable()) count++;
			}
		}

		adjustable = new Parameter[count];
		for(int i=0,p=0; i<type.length; i++) {
			Family fam[] = type[i].getFamilies();
			for(int j=0; j<fam.length; j++) {
				Parameter param[] = fam[j].getParameters();
				for(int k=0; k<param.length; k++) if(param[k].isAdjustable()) {
					param[k].oderiv = new double[outputs];
					adjustable[p] = param[k];
					p++;
				}
			}

			ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
			for(int j=0; j<mf.length; j++) {
				Parameter param[] = mf[j].getParameters();
				for(int k=0; k<param.length; k++) if(param[k].isAdjustable()) {
					param[k].oderiv = new double[outputs];
					adjustable[p] = param[k];
					p++;
				}
			}
		}
	}

	/**
	 * Obtiene la lista de parámetros ajustables
	 */
	public Parameter[] getAdjustable() {
		return adjustable;
	}

	/**
	 * Estima la derivada de los parámetros mediante la tangente
	 */
	public double[] estimate_derivatives(double[] input, double perturb) {
		double[] output1 = system.crispInference(input);
		for(int i=0; i<type.length; i++) {
			Family fam[] = type[i].getFamilies();
			for(int j=0; j<fam.length; j++) {
				Parameter param[] = fam[j].getParameters();
				for(int k=0; k<param.length; k++) {
					if(!param[k].isAdjustable()) continue;
					param[k].oderiv = new double[output1.length];
					double prev = param[k].value;
					double sign = (Math.random() <0.5? 1.0 : -1.0);
					param[k].setDesp(sign*perturb);
					fam[j].update();
					double[] output2 = system.crispInference(input);
					if((param[k].value - prev)/(sign*perturb) >0.001 )
						for(int o=0; o<output1.length; o++)
							param[k].oderiv[o] = (output2[o] - output1[o])/(param[k].value - prev);
					param[k].value = prev;
				}
			}

			ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
			for(int j=0; j<mf.length; j++) {
				Parameter param[] = mf[j].getParameters();
				for(int k=0; k<param.length; k++) {
					if(!param[k].isAdjustable()) continue;
					param[k].oderiv = new double[output1.length];
					double prev = param[k].value;
					double sign = (Math.random() <0.5? 1.0 : -1.0);
					param[k].setDesp(sign*perturb);
					mf[j].update();
					double[] output2 = system.crispInference(input);
					if((param[k].value - prev)/(sign*perturb) >0.001 )
						for(int o=0; o<output1.length; o++)
							param[k].oderiv[o] = (output2[o] - output1[o])/(param[k].value - prev);
					param[k].value = prev;
				}
			}
		}
		return output1;
	}

	/**
	 * Estima la derivada de los parámetros de forma grosera
	 */
	public double[] stochastic_derivatives(double[] input, double perturb) {
		double prev[] = new double[adjustable.length];
		double val1[] = new double[adjustable.length];
		double sign[] = new double[adjustable.length];
		for(int i=0; i<adjustable.length; i++) {
			prev[i] = adjustable[i].value;
			sign[i] = (Math.random() <0.5? 1.0 : -1.0);
			adjustable[i].setDesp(sign[i]*perturb);
		}
		update();
		double[] output1 = system.crispInference(input);

		for(int i=0; i<adjustable.length; i++) {
			val1[i] = adjustable[i].value;
			adjustable[i].value = prev[i];
			adjustable[i].setDesp(-sign[i]*perturb);
		}
		update();
		double[] output2 = system.crispInference(input);

		for(int i=0; i<adjustable.length; i++) {
			double val2 = adjustable[i].value;
			adjustable[i].value = prev[i];
			if( (val1[i]-val2)/(sign[i]*perturb) >0.001 )
				for(int o=0; o<output1.length; o++)
					adjustable[i].oderiv[o] = (output2[o] - output1[o])/(val2 - val1[i]);
		}
		return system.crispInference(input);
	}

	/**
	 * Calcula las derivadas del sistema
	 */
	public double[] compute_derivatives(double[] input) throws XflException {
		double[] output = system.crispInference(input);
		for(int i=0; i<output.length; i++) {
			double[] deriv = new double[output.length];
			deriv[i] = 1;
			system.derivative(deriv);
			for(int p=0; p<adjustable.length; p++) {
				adjustable[p].oderiv[i] = adjustable[p].getDeriv();
				adjustable[p].setDeriv(0);
			}
		}
		return output;
	}
}
