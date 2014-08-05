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

package xfuzzy.xfvhdl;

import xfuzzy.lang.*;
import xfuzzy.xfsg.XfsgError;

import java.io.*;
import java.util.ArrayList;

/**
* Clase que gestiona la estructura que contiene la información
* de la base de reglas de cada módulo de inferencia del sistema jerárquico.
* 
* @author Lidia Delgado Carretero.
* 
*/
public class XfvhdlRulesMemData {

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//		  ATRIBUTOS PRIVADOS DE LA CLASE 				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**Guarda el contenido de la memoria de reglas.
	 * Estructura de vector:
	 * vector[_][0] ==> Almacena el número de fila en binario.
	 * vector[_][1] ==> Almacena el valor de la mf que se activa en la salida.
	 * vector[_][2] ==> Almacena el peso (si se ha definido).
	 * vector[_][3] ==> Indica si la fila es válida o no.
	 * vector[_][4] ==> Almacena el nombre de la mf que se activa.*/
	private Object vector[][]; // Estructura de datos  
	
	/**Estructura para almacenar las constantes en Takagi-sugeno*/
	private Object tak_sug[][];
	
	/**Número de filas de la estructura.*/
	private int longitud;
	
	/**Número de bits con los que se codifica el valor de entrada.*/
	private int N;
	
	/**Número de bits con los que se codifica el peso.*/
	private int K;

	/**Variables de entrada.*/
	private Variable[] var_in; // Variables de entrada
	
	/**Variables de salida.*/
	private Variable[] var_output; // Variables de salida

	/**Funciones de pertenencia de las variables de entrada ordenadas.*/
	private XfvhdlInOrderParamMemFunc[] inOrderMfInput;
	
	/**Funciones de pertenencia de la variable de salida ordenadas.*/
	private XfvhdlInOrderParamMemFunc inOrderMfOutput;
	private XfvhdlInOrderParamMemFunc[] inOrderMfOutput2;
	
	/**Funciones de pertenencia usadas.*/
	private int mfUsed[][]; 
	private double val;
	private double[] val2;
	
	/**Lista con el nº de funciones de pertenencia de cada entrada.*/
	private int [] n_mf_var;
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//				  CONSTRUCTOR DE LA CLASE					   
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	 * Constructor de XFvhdlRulesMemData.
	 * Inicializa la estructura línea a línea a partir de los datos 
	 * obtenidos de la base de reglas.
	 * 
	 * NOTA: Cuando una línea de la estructura contiene datos la marca 
	 * como verdadero y en caso contrario la marca como falso. Esto  
	 * facilita la generación de código VHDL.
	 */
	
	public XfvhdlRulesMemData(int filas, int n, int kk, Specification spec, 
			String nombre_i, ArrayList<Integer> bits_var, boolean calculateWeights)
	
	throws IOException {

		
	int No=n;
	int W=kk;
	int regla_i=-1;
	// Calcula la longitud de la estructura
	longitud = (int) Math.pow((double) 2, (double) filas);

	// Inicializa la estructura
	vector = new Object[longitud][5];
	
	this.N = n;
	this.K = kk;
	
	var_in = spec.getRulebases()[0].getInputs();
	n_mf_var=new int[var_in.length];
	for(int i=0;i<n_mf_var.length;i++){
		n_mf_var[i]=var_in[i].getType().getAllMembershipFunctions().length;
	}
	

	// Obtiene las variables de entrada y salida de la base de reglas
	Rulebase[] rulebases = spec.getRulebases();
	for (int i=0;i<rulebases.length;i++){
		if(rulebases[i].getName().equalsIgnoreCase(nombre_i))
			regla_i=i;
	}
	// Ahora permitimos más de una base de reglas
	//if (rulebases.length > 1) {
	//	XfvhdlError err = new XfvhdlError();
	//	err.newWarning(9);
	//}
	//Como ahora tenemos más de una base de reglas, tenemos que pasar el 
	//índice de la base de reglas
	var_in = rulebases[regla_i].getInputs();
	
	//Ahora tenemos que tener una lista de enteros con en número de 
	//bits para cada entrada.
	//bits_var = filas / var_in.length;

	var_output = rulebases[regla_i].getOutputs();

	// PARA N VARIABLES DE ENTRADA:

	// PASO 1: Se crea la estructura inOrderMfInput[i] (donde el 
	//         índice i direcciona las distintas variables de entrada) 
	//         que contiene una lista con las funciones de pertenencia 
	//         ordenadas

	inOrderMfInput = new XfvhdlInOrderParamMemFunc[var_in.length];
	for (int i = 0; i < var_in.length; i++) {
		inOrderMfInput[i] = new XfvhdlInOrderParamMemFunc(var_in[i]);
		inOrderMfInput[i].sort();
	}

	// PASO 2: Guarda en inOrderMfOutput las funciones de pertenencia 
	//         ordenadas de la variable de salida
	//         SOLO VALIDO PARA 1 VARIABLE DE SALIDA

	inOrderMfOutput = new XfvhdlInOrderParamMemFunc(var_output[0]);
	inOrderMfOutput.sort();

	// PASO 3: Guarda las reglas del sistema XFL en cprule		 
	Rule[] cprule = rulebases[regla_i].getRules();		

	// PASO 4: Inicializa la estructura vector donde se guardará el 
	//         contenido de la memoria de reglas. Inicializa todas 
	//         sus filas a false
	//
	//         Estructura de vector:
	//         vector[_][0] ==> Almacena el número de fila en binario
	//         vector[_][1] ==> Almacena el valor de la mf que se activa 
	//                          en la salida
	//         vector[_][2] ==> Almacena el peso (si se ha definido)
	//         vector[_][3] ==> Indica si la fila es válida o no
	//		   vector[_][4] ==> Almacena el nombre de la mf que se activa

	XfvhdlBinaryDecimal converter = new XfvhdlBinaryDecimal();
	for (int i = 0; i < longitud; i++) {
		vector[i][0] = converter.toBinary(i, filas);
		vector[i][3] = new Boolean(false);
		vector[i][4] = new String ("0");
	}

	// PASO 5: Se van recorriendo las reglas y se guarda su información en 
	// vector
	mfUsed = new int[var_in.length][2];
	
	for (int k = 0; k < cprule.length; k++) {
		// Obtiene el consecuente
		Conclusion conc[] = cprule[k].getConclusions();
		if (conc.length == 0) {
			new XfvhdlError(13, "" + k);
		}

		// Inicializa mfUsed
		for (int i = 0; i < mfUsed.length; i++){
			mfUsed[i][0] = -1;
			mfUsed[i][1] = -1;
		}

		Relation left = null;
		Relation right = null;

		// Aseguramos que la regla es de tipo AND o IS
		Relation rl = cprule[k].getPremise();
		if (rl.getKind() != Relation.AND && rl.getKind() != Relation.IS
				&& rl.getKind()!=Relation.GREATER
				&& rl.getKind()!=Relation.GR_EQ && rl.getKind()!=Relation.SMALLER
				&& rl.getKind()!=Relation.SM_EQ && rl.getKind()!=Relation.ISNOT) {
			new XfvhdlError(10, "" + k);
			//err.show();
			return;
		} else if (rl.getKind() == Relation.IS
				|| rl.getKind()==Relation.GREATER
				|| rl.getKind()==Relation.GR_EQ || rl.getKind()==Relation.SMALLER
				|| rl.getKind()==Relation.SM_EQ || rl.getKind()==Relation.ISNOT) {
			left = rl;
		} else {
			// parte izquierda
			left = rl.getLeftRelation();

			// parte derecha
			right = rl.getRightRelation();
		}

		// calcula las mf usadas
		getMfUsed(left, right, k);

		// averigua la conclusion
		int pos_c = 0;
		ParamMemFunc c = null;
		try {
		  c = (ParamMemFunc) conc[0].getMembershipFunction();
		} catch(Exception ex) {
		 new XfvhdlError(31);
		}
		// averiguA de qué mf se trata dentro de la variable de salida 
		// Y LA GUARDO EN TMP
		boolean enc = false;
		ParamMemFunc tmp;
		for (kk = 0;
			kk < inOrderMfOutput.getInOrderParamMemFunc().size() && !enc;
			kk++) {
			tmp =
				(ParamMemFunc) inOrderMfOutput
					.getInOrderParamMemFunc()
					.get(
					kk);
			val = tmp.center();
			if (c != null && tmp.equals(c.getLabel())) {
				pos_c = kk;
				enc = true;
			}
		}

		// PASO 6: Guarda la información correspondiente en vector			
		XfvhdlVectorCount v =
			new XfvhdlVectorCount(
				var_in.length,
				n_mf_var,
				mfUsed);

		int tmp1, tmp2;
		String aux = new String();

		for (int tt = 0; tt < var_in.length; tt++) {
			tmp1 = v.get(tt);
			aux += converter.toBinary(tmp1, bits_var.get(tt));
		}
		tmp2 = converter.toDecimal(aux);

		ParamMemFunc p;
		double _min=0, _max=0;
		p = inOrderMfOutput.getParamMemFunc(pos_c);
		_min = p.universe().min();
		_max = p.universe().max();
		
		vector[tmp2][1] = converter.toBinaryInRange(val, _min, _max, No);
		vector[tmp2][3] = new Boolean(true);
		vector[tmp2][4] = p.getLabel(); 
		
		// calcula el peso
		if (calculateWeights == true) {
			vector[tmp2][2] =
				converter.toBinaryInRange(
					calculateWeight(pos_c),
					0.0,
					1.0,
					W);
		}

		while (v.sub()) {
			aux = "";
			for (int tt = 0; tt < var_in.length; tt++) {
				tmp1 = v.get(tt);
				aux += converter.toBinary(tmp1, bits_var.get(tt));
			}
			tmp2 = converter.toDecimal(aux);

//			vector[tmp2][1] = converter.toBinary(pos_c, N);
			vector[tmp2][1] = converter.toBinaryInRange(val, _min, _max, No);
			vector[tmp2][3] = new Boolean(true);	
			vector[tmp2][4] = p.getLabel();
			
			// calcula el peso
			if (calculateWeights == true) {
				vector[tmp2][2] =
					converter.toBinaryInRange(
						calculateWeight(pos_c),
						0.0,
						1.0,
						W);
			}
		}
	}
}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			MÉTO_DOS PÚBLICOS DE LA CLASE				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	* Método que modifica el valor de una línea dada. Si la línea 
	* estaba inactiva, la activa.
	* @param line Línea a la que se le actualizará el valor.
	* @param value Nuevo valor que se le asignará a la línea.
	*/
	public void setValue(int line, int value) {
		// Crea un objeto conversor de formatos
		XfvhdlBinaryDecimal converter = new XfvhdlBinaryDecimal();
		// Modifica el valor de la línea
		vector[line][1] = converter.toBinary(value, N);
		if (!isActive(line)) {
			vector[line][3] = new Boolean(true);
		}
	}

	/**
	* Método que modifica el peso de una línea dada. Si la línea estaba 
	* inactiva, la activa.
	*/
	public void setWeight(int line, int weight) {
		// Crea un objeto conversor de formatos
		XfvhdlBinaryDecimal converter = new XfvhdlBinaryDecimal();
		// Modifica el peso de la línea
		vector[line][2] = converter.toBinary(weight, K);
		if (!isActive(line)) {
			vector[line][3] = new Boolean(true);
		}
	}

	/**
	* Método que obtiene el valor de una línea dada.
	*/
	public String getValue(int line) {
		return (String) vector[line][1];
	}

	/**
	* Método que obtiene el peso de una línea dada.
	*/
	public String getWeight(int line) {
		return (String) vector[line][2];
	}

	/**
	* Método que obtiene la función de pertenencia de una linea dada.
	*/
	public String getMF(int line) {
		return (String) vector[line][4];
	}
	
	/**
	* Método que obtiene cualquier elemento de vector de una linea dada.
	*/
	public String getMF2(int line, int j) {
		return (String) vector[line][j];
	}
	

	/**
	* Método para obtener una línea de la estructura (sólo el nombre).
	*/
	public String getLine(int line) {
		String l = new String();

		l = getBinaryData(line);

		return l;
	}

	/**
	* Método para obtener una línea de la estructura (nombre,valor y peso).
	*/
	public String getLineComplete(int line) {
		String l = new String();

		l = getBinaryData(line) + getValue(line) + getWeight(line);

		return l;
	}

	/**
	* Método para obtener el estado de una línea.
	*/
	public boolean isActive(int line) {
		Object o;
		Boolean b;

		o = vector[line][3];
		b = (Boolean) o;

		return b.booleanValue();
	}
	
	public boolean isActive2(int line) {
		
		return ((Boolean)vector[line][1]).booleanValue();
	}

	/**
	* Método para imprimir por pantalla el contenido de la estructura.
	*/
	public void toScreen() {
		for (int i = 0; i < longitud; i++)
			System.out.println(
				vector[i][0]
					+ " "
					+ vector[i][1]
					+ " "
					+ vector[i][2]
					+ " "
					+ vector[i][3]
					+ " "
					+ vector[i][4]
					+ " ");
	}
	
	public void toScreen2() {
		for (int i = 0; i < longitud; i++){
			for(int j=0;j<vector[i].length;j++){
				System.out.print(vector[i][j]+" ");
			}
			System.out.println(" ");
		}
	}

	/**
	* Método para obtener el número de líneas de la estructura.
	*/
	public int getLength() {
		return longitud;
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			  MÉTO_DOS PRIVADOS DE LA CLASE				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	* Método privado para obtener el valor binario de una línea.
	*/
	private String getBinaryData(int line) {
		return (String) vector[line][0];
	}

	/**Le da valor a la variable mfUsed.*/
	
	private void getMfUsed(Relation left, Relation right, int nrule) {
		int min=0,max=0;
		if ((left.getKind() == Relation.IS || left.getKind() == Relation.GREATER ||
				left.getKind() == Relation.GR_EQ || left.getKind() == Relation.SMALLER ||
				left.getKind() == Relation.SM_EQ || left.getKind()==Relation.ISNOT) && (right == null || right.getKind() == Relation.IS ||
					right.getKind() == Relation.GREATER || right.getKind() == Relation.GR_EQ ||
					right.getKind() == Relation.SMALLER || right.getKind() == Relation.SM_EQ
					|| right.getKind()==Relation.ISNOT)) {
			// ********** Trata la parte izquierda ***********
			int pos_v = 0, pos_mf = 0;
			Variable v = left.getVariable();
			ParamMemFunc mf = null;
			FamiliarMemFunc mf2=null;
			try {
				if(left.getMembershipFunction() instanceof ParamMemFunc)
					mf = (ParamMemFunc) left.getMembershipFunction();
				else if (left.getMembershipFunction() instanceof FamiliarMemFunc)
					mf2 = (FamiliarMemFunc) left.getMembershipFunction();
			} catch(Exception ex) {
				new XfvhdlError(31);
			}

			// averigua de qué variable se trata
			boolean enc = false;
			for (int kk = 0; kk < var_in.length && !enc; kk++) {
				if (var_in[kk].equals(v.getName())) {
					pos_v = kk;
					enc = true;
				}
			}

			// averigua de qué mf se trata
			enc = false;
			ParamMemFunc tmp=null;
			FamiliarMemFunc tmp2=null;
			for (int kk = 0;
				kk < (inOrderMfInput[pos_v].getInOrderParamMemFunc().size()+
					inOrderMfInput[pos_v].getFamiliarMemFunc().length)
					&& !enc;
				kk++) {
				if(inOrderMfInput[pos_v].getInOrderParamMemFunc().size()!=0)
					tmp =
						(ParamMemFunc) inOrderMfInput[pos_v]
						                              .getInOrderParamMemFunc()
						                              .get(kk);
				if(inOrderMfInput[pos_v].getFamiliarMemFunc().length!=0)
					tmp2=inOrderMfInput[pos_v].getFamiliarMemFunc()[kk];
				if(tmp!=null){
					if (mf != null && tmp.equals(mf.getLabel())) {
						pos_mf = kk;
						enc = true;
					}
				}
				if(tmp2!=null){
					if (mf2 != null && 
							tmp2.equals(mf2.getLabel())) {
						pos_mf = kk;
						enc = true;
					}
				}
			}

			// guarda la información en mfUsed
			min = calculamin_max(pos_mf,v,"min", left.getKind());
			max = calculamin_max(pos_mf,v,"max", left.getKind());
			actuliza_min_max(min,max,pos_v,left.getKind());
			//mfUsed[pos_v] = pos_mf;

			// ****** Trata la parte derecha, si es distinta de null *****		
			if (right != null) {
				pos_v = 0;
				pos_mf = 0;
				v = right.getVariable();
				try {
					if(right.getMembershipFunction() instanceof ParamMemFunc)
						mf = (ParamMemFunc) right.getMembershipFunction();
					else if (right.getMembershipFunction() instanceof FamiliarMemFunc)
						mf2 = (FamiliarMemFunc) right.getMembershipFunction();
				} catch(Exception ex) {
				 new XfvhdlError(31);
				}
				
				// averigua de qué variable se trata
				enc = false;
				for (int kk = 0; kk < var_in.length && !enc; kk++) {
					if (var_in[kk].equals(v.getName())) {
						pos_v = kk;
						enc = true;
					}
				}

				// averigua de qué mf se trata
				enc = false;
				tmp=null;
				tmp2=null;
				for (int kk = 0;
					kk < (inOrderMfInput[pos_v].getInOrderParamMemFunc().size()+
						inOrderMfInput[pos_v].getFamiliarMemFunc().length)
						&& !enc;
					kk++) {
					if(inOrderMfInput[pos_v].getInOrderParamMemFunc().size()!=0)
						tmp =
							(ParamMemFunc) inOrderMfInput[pos_v]
							                              .getInOrderParamMemFunc()
							                              .get(kk);
					if(inOrderMfInput[pos_v].getFamiliarMemFunc().length!=0)
						tmp2=inOrderMfInput[pos_v].getFamiliarMemFunc()[kk];
					
					if(tmp!=null){
						if (mf != null && tmp.equals(mf.getLabel())) {
							pos_mf = kk;
							enc = true;
						}
					}
					if(tmp2!=null){
						if (mf2 != null && tmp2.equals(mf2.getLabel())) {
							pos_mf = kk;
							enc = true;
						}
					}
				}

				// guarda la información en mfUsed
				//mfUsed[pos_v] = pos_mf;
				min = calculamin_max(pos_mf,v,"min", right.getKind());
				max = calculamin_max(pos_mf,v,"max", right.getKind());
				actuliza_min_max(min,max,pos_v,right.getKind());
			}
		} else if (
				(left.getKind() == Relation.AND) && (right.getKind() == Relation.IS ||
						right.getKind() == Relation.GREATER || right.getKind() == Relation.GR_EQ ||
						right.getKind() == Relation.SMALLER || right.getKind() == Relation.SM_EQ
						|| right.getKind()==Relation.ISNOT)) {
			// ***** Trata la parte derecha que es de tipo IS, GREATER, GR_EQ, SMALLER, SM_EQ ó ISNOT *****
			int pos_v = 0, pos_mf = 0;
			Variable v = right.getVariable();
			ParamMemFunc mf = null;
			FamiliarMemFunc mf2 = null;
			try {
			 //mf = (ParamMemFunc) right.getMembershipFunction();
			 if(right.getMembershipFunction() instanceof ParamMemFunc)
					mf = (ParamMemFunc) right.getMembershipFunction();
				else if (right.getMembershipFunction() instanceof FamiliarMemFunc)
					mf2 = (FamiliarMemFunc) right.getMembershipFunction();
			} catch(Exception ex) {
			 new XfvhdlError(31);
			}

			// averigua de qué variable se trata
			boolean enc = false;
			for (int kk = 0; kk < var_in.length && !enc; kk++) {
				if (var_in[kk].equals(v.getName())) {
					pos_v = kk;
					enc = true;
				}
			}

			// averigua de qué mf se trata
			enc = false;
			ParamMemFunc tmp=null;
			FamiliarMemFunc tmp2=null;
			for (int kk = 0;
				kk < (inOrderMfInput[pos_v].getInOrderParamMemFunc().size()+
						inOrderMfInput[pos_v].getFamiliarMemFunc().length)
					&& !enc;
				kk++) {
				if(inOrderMfInput[pos_v].getInOrderParamMemFunc().size()!=0)
					tmp =
						(ParamMemFunc) inOrderMfInput[pos_v]
						                              .getInOrderParamMemFunc()
						                              .get(kk);
				if(inOrderMfInput[pos_v].getFamiliarMemFunc().length!=0)
					tmp2=inOrderMfInput[pos_v].getFamiliarMemFunc()[kk];
				
				
				if (mf != null && tmp.equals(mf.getLabel())) {
					pos_mf = kk;
					enc = true;
				}
				if(tmp2!=null){
					if (mf2 != null && tmp2.equals(mf2.getLabel())) {
						pos_mf = kk;
						enc = true;
					}
				}
			}

			// guarda la información en mfUsed
			//mfUsed[pos_v] = pos_mf;
			min = calculamin_max(pos_mf,v,"min", right.getKind());
			max = calculamin_max(pos_mf,v,"max", right.getKind());
			actuliza_min_max(min,max,pos_v,right.getKind());

			// ******* Llama recursivamente a esta función con **** 
			// ******* la parte izquierda                      ****
			// ******* es de tipo AND. ****************************
			Relation newLeft = left.getLeftRelation();
			Relation newRight = left.getRightRelation();
			getMfUsed(newLeft, newRight, nrule);
		} else {
			// Informa de que la regla nrule no es del tipo
			// A & B & C ... ==> Z
			new XfvhdlError(10, "Left: " + left.toXfl()+"  Right: "+right.toXfl());
			return;
		}

	}

	/**@return Peso asociado a una entrada dada.*/
	private double calculateWeight(int pos_c) {
		ParamMemFunc p;
		double universeRange = 0, peso = 0;
		p = inOrderMfOutput.getParamMemFunc(pos_c);
		universeRange = p.universe().range();

		try {
			peso = p.basis() / universeRange;
		} catch (Exception e) {
			XfvhdlError err = new XfvhdlError(0);
			err.show();
			return 0;
		}
		return peso;
	}
	
	private double calculateWeight2(int j, int pos_c) {
		ParamMemFunc p;
		double universeRange = 0, peso = 0;
		p = inOrderMfOutput2[j].getParamMemFunc(pos_c);
		universeRange = p.universe().range();

		try {
			peso = p.basis() / universeRange;
		} catch (Exception e) {
			XfvhdlError err = new XfvhdlError(0);
			err.show();
			return 0;
		}
		return peso;
	}
	
	private double calculateWeight2(LinguisticLabel p, String defuzzy, double gamma) {
		
		double universeRange = 0, peso = 0;
		
		universeRange = p.max()-p.min();

		try {
			if(defuzzy.equals("WeightedFuzzyMean")){
				peso = p.basis() / universeRange;
			}
			else if(defuzzy.equals("GammaQuality")){
				double w = Math.pow(p.basis(), gamma);
				peso=w/universeRange;
			}
			else if(defuzzy.equals("Quality")){
				peso=(1/ p.basis()) / universeRange;
			}
		} catch (Exception e) {
			new XfsgError(5, defuzzy);
			//err.show();
			return 0;
		}
		return peso;
	}
	
	/**Método que nos ayuda a completar cuando tenemos bares de reglas incompletas.*/
	private int calculamin_max(int valor, Variable var, String s, int tipo){
		int res=0;
		int nmf=var.getType().getAllMembershipFunctions().length;
		if(tipo==Relation.IS){
			if(s.equals("min")){
				res=valor;
			}else if(s.equals("max")){
				res=valor;
			}
		}else if(tipo==Relation.GREATER){
			if(s.equals("min")){
				res=valor+1;
			}else if(s.equals("max")){
				res=nmf-1;
			}
		}else if(tipo==Relation.GR_EQ){
			if(s.equals("min")){
				res=valor;
			}else if(s.equals("max")){
				res=nmf-1;
			}
		}else if(tipo==Relation.SMALLER){
			if(s.equals("min")){
				res=0;
			}else if(s.equals("max")){
				res=valor-1;
			}
		}else if(tipo==Relation.SM_EQ){
			if(s.equals("min")){
				res=0;
			}else if(s.equals("max")){
				res=valor;
			}
		}else if(tipo==Relation.ISNOT){
			if(s.equals("min")){
				res=-2;
			}else if(s.equals("max")){
				res=valor;
			}
		}
		return res;
	}
	

	/**Método que nos ayuda a completar cuando tenemos bares de reglas incompletas.*/
	private void actuliza_min_max(int min, int max, int pos_v, int tipo){
		if(mfUsed[pos_v][0]==-1 && mfUsed[pos_v][1]==-1){
			mfUsed[pos_v][0]=min;
			mfUsed[pos_v][1]=max;
		}
		else if(mfUsed[pos_v][0]!=-1 && mfUsed[pos_v][1]!=-1){
			if(tipo==Relation.GR_EQ | tipo==Relation.GREATER)
				mfUsed[pos_v][0]=min;
			else if(tipo==Relation.SM_EQ | tipo==Relation.SMALLER)
				mfUsed[pos_v][1]=max;
		}
		
	}
} // Fin de la clase