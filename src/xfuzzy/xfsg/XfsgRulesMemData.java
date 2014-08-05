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

package xfuzzy.xfsg;

import xfuzzy.lang.*;
import java.io.*;

/**
* Clase que gestiona la estructura que contiene la información
* de la base de reglas
* 
* @author Jesús Izquierdo Tena
*/
public class XfsgRulesMemData {

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//		  ATRIBUTOS PRIVADOS DE LA CLASE 				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	
	private Object vector[][]; // Estructura de datos  
	private Object tak_sug[][]; // Estructura para almacenar las constantes en Takagi-sugeno 
	private int longitud; // Número de filas de la estructura
	private int bits_var; // Número de bits con los que se codifica una variable de entrada
	private Variable[] var_in; // Variables de entrada
	private Variable[] var_output; // Variables de salida
	// Funciones de pertenencia de las variables de entrada ordenadas
	private XfsgInOrderMemFunc[] inOrderMfInput;
	// Funciones de pertenencia de la variable de salida ordenadas
	//private XfsgInOrderParamMemFunc[] inOrderMfOutput2;
	private int mfUsed[][]; // Funciones de pertenencia usadas 
	private int [] n_mf_var;
	private boolean calcularpesos=false;
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//				  CONSTRUCTOR DE LA CLASE					   
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	/**
	 * Constructor de <code>XfsgdlRulesMemData</code>
	 * Inicializa la estructura línea a línea a partir de los datos 
	 * obtenidos de la base de reglas
	 * 
	 * NOTA: Cuando una línea de la estructura contiene datos la marca 
	 * como verdadero y en caso contrario la marca como falso.
	 */
	public XfsgRulesMemData(int filas, Rulebase rulebase, String defuzzy) throws IOException {

	// Calcula la longitud de la estructura
	longitud = (int) Math.pow((double) 2, (double) filas);
	
	// Inicializa la estructura
	var_in = rulebase.getInputs();

	n_mf_var=new int[var_in.length];
	
	for(int i=0;i<n_mf_var.length;i++){
		n_mf_var[i]=var_in[i].getType().getAllMembershipFunctions().length;
		//System.out.println("entradas "+n_mf_var[i]);
	}
	
	bits_var = filas / var_in.length;

	var_output = rulebase.getOutputs();
	
	if(defuzzy.equals("GammaQuality") || defuzzy.equals("Quality") || defuzzy.equals("WeightedFuzzyMean"))
		calcularpesos=true;

	double gamma=0;
	
	if (defuzzy.equals("GammaQuality")) 
			gamma= rulebase.getOperatorset().defuz.getSingleParameters()[0].value;
	
	if(defuzzy.equals("TakagiSugeno")){
		tak_sug=new Object[longitud][1+var_output.length*3];
	}
	//System.out.println("Gamma: "+ gamma);
	// PARA N VARIABLES DE ENTRADA:

	// PASO 1: Se crea la estructura inOrderMfInput[i] (donde el 
	//         índice i direcciona las distintas variables de entrada) 
	//         que contiene una lista con las funciones de pertenencia 
	//         ordenadas

	inOrderMfInput = new XfsgInOrderMemFunc[var_in.length];
	for (int i = 0; i < var_in.length; i++) {
		inOrderMfInput[i] = new XfsgInOrderMemFunc(var_in[i]);
		inOrderMfInput[i].sort();
	}


	// PASO 2: Guarda las reglas del sistema XFL en cprule		 
	Rule[] cprule = rulebase.getRules();

	// PASO 3: Inicializa la estructura vector donde se guardará el 
	//         contenido de la memoria de reglas. Inicializa todas 
	//         sus filas a false
	//
	//         Estructura de vector:
	//         vector[_][0] ==> Almacena el número de fila en binario
	//         vector[_][1] ==> Indica si la fila es válida o no
	//		   vector[_][2] ==> Almacena el grado de certeza de la regla
	//                         
	// Los siguientes dos campos se repiten por cada variable de salida.
	//         vector[_][3] ==> Almacena el peso de la regla
	//		   vector[_][4] ==> Almacena el nombre de la mf que se activa

	vector = new Object[longitud][2+3*var_output.length];
	XfsgBinaryDecimal converter = new XfsgBinaryDecimal();
	for (int i = 0; i < longitud; i++) {
		vector[i][0] = converter.toBinary(i, filas);
		vector[i][1] = new Boolean(false);
		vector[i][2]=0.0;
		for(int j=4;j<vector[i].length;j=j+2)
			vector[i][j] = new String ("NR");
		for(int j=3;j<vector[i].length;j=j+2)
			vector[i][j] = 0.0;
	}
	
	if(defuzzy.equals("TakagiSugeno")){
		for (int i = 0; i < longitud; i++) {
			tak_sug[i][0] = converter.toBinary(i, filas);
			for(int j=1;j<tak_sug[i].length;j++){
				tak_sug[i][j]=0.0;
			}
		}
	}

	// PASO 4: Se van recorriendo las reglas y se guarda su información en 
	// vector
	mfUsed = new int[var_in.length][2];
	for (int k = 0; k < cprule.length; k++) {
		// Obtiene el consecuente
		Conclusion conc[] = cprule[k].getConclusions();
		
		//System.out.println(conc.length);
		// Inicializa mfUsed
		for (int i = 0; i < mfUsed.length; i++){
			mfUsed[i][0] = -1;
			mfUsed[i][1] = -1;
		}

		Relation left = null;
		Relation right = null;

		// Aseguramos que la regla es de tipo correcto
		Relation rl = cprule[k].getPremise();
		if (rl.getKind() != Relation.AND && rl.getKind() != Relation.IS && rl.getKind()!=Relation.GREATER
				&& rl.getKind()!=Relation.GR_EQ && rl.getKind()!=Relation.SMALLER
				&& rl.getKind()!=Relation.SM_EQ && rl.getKind()!=Relation.ISNOT) {
				new XfsgError(4, "" + cprule[k].toXfl());
			//err.show();
			return;
		} else if (rl.getKind() == Relation.IS || rl.getKind()==Relation.GREATER
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


		// PASO 5: Guarda la información correspondiente en vector			
		XfsgVectorCount v =new XfsgVectorCount(var_in.length, n_mf_var, mfUsed);
		if(!v.Error()){
			int tmp1, tmp2;
			String aux = new String();
	
			for (int tt = 0; tt < var_in.length; tt++) {
				tmp1 = v.get(tt);
				aux += converter.toBinary(tmp1, bits_var);
			}
			tmp2 = converter.toDecimal(aux);
			vector[tmp2][1] = new Boolean(true);
			vector[tmp2][2]= cprule[k].getDegree();
			
			
			
			for(int j=0;j<conc.length;j++){
				Variable var=conc[j].getVariable();
				//System.out.println(var);
				int v_aux=0;
				boolean enc=false;
				for(int ii=0;ii<var_output.length && !enc;ii++){
					if(var_output[ii].equals(var)){
						enc=true;
						v_aux=ii;
					}
				}
				LinguisticLabel p = conc[j].getMembershipFunction();
				
				vector[tmp2][4+v_aux*2] = p.getLabel(); 
				v.reset(0);
				if(defuzzy.equals("TakagiSugeno")){
					Parameter [] params =((ParamMemFunc)p).getParameters();
					tak_sug[tmp2][1+v_aux*3]=params[0].value;
					tak_sug[tmp2][2+v_aux*3]=params[1].value;
					tak_sug[tmp2][3+v_aux*3]=params[2].value;
				}
				// calcula el peso
				if (calcularpesos == true) {
					String clase=p.getClass().toString();
					if(clase.contains("singleton")){
						//XfsgError err=new XfsgError();
						//err.newWarning(10, "Defuzzification method -> "+defuzzy +" .Membership function -> "+ p.getClass()+"..");
					}
					else
						vector[tmp2][3+v_aux*2] =calculateWeight2(p, defuzzy, gamma);
				}
				
				while (v.sub()) {
					aux = "";
					for (int tt = 0; tt < var_in.length; tt++) {
						tmp1 = v.get(tt);
						aux += converter.toBinary(tmp1, bits_var);
					}
					int tmp3 = converter.toDecimal(aux);
					//tmp2= converter.toDecimal(aux);
					vector[tmp3][1] = new Boolean(true);	
					vector[tmp3][4+v_aux*2] = p.getLabel();
					vector[tmp3][2]= cprule[k].getDegree();
					
					if(defuzzy.equals("TakagiSugeno")){
						Parameter [] params =((ParamMemFunc)p).getSingleParameters();
						tak_sug[tmp3][1+v_aux*3]=params[0].value;
						tak_sug[tmp3][2+v_aux*3]=params[1].value;
						tak_sug[tmp3][3+v_aux*3]=params[2].value;
					}
					
					// calcula el peso
					if (calcularpesos == true) {
						vector[tmp3][3+v_aux*2] = calculateWeight2(p,defuzzy, gamma);
								
					}
					
				}
			}
		}
	}
}
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			MÉTO_DOS PÚBLICOS DE LA CLASE				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	
	/**
	* Método que obtiene la mf de una linea dada
	*/
	public String getMF2(int line, int j) {
		return (String) vector[line][j];
	}
	
	/**
	* Método que obtiene el peso de una linea (line) dada en la posicion j
	*/
	public Double getPeso(int line, int j) {
		return (Double) vector[line][j];
	}
	
	/**
	* Método que obtiene el grado de certeza de una linea (line) dada en la posición j
	*/
	public Double getGrado(int line, int j) {
		return (Double) vector[line][j];
	}
	
	
	public Double getConstante(int line, int j) {
		return (Double) tak_sug[line][j];
	}
	
	
	/**
	 * 
	 * @param line numero de linea de 
	 * @return si la linea indicada esta activa
	 */
	public boolean isActive2(int line) {
		return ((Boolean)vector[line][1]).booleanValue();
	}
	

	/**
	* Método para obtener el número de líneas de la estructura
	*/
	public int getLength() {
		return longitud;
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	//			  MÉTO_DOS PRIVADOS DE LA CLASE				        
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	private void getMfUsed(Relation left, Relation right, int nrule) {
		//System.out.println(left.toString()+" ");
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
			LinguisticLabel mf = null;
			try {
			 mf =  left.getMembershipFunction();
			} catch(Exception ex) {
				new XfsgError(7, ex.getMessage());
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
			LinguisticLabel tmp;
			for (int kk = 0;
				kk < inOrderMfInput[pos_v].getInOrderMemFunc().size()&& !enc; kk++) {
				tmp =inOrderMfInput[pos_v].getInOrderMemFunc().get(kk);
				if (mf != null && tmp.equals(mf.getLabel())) {
					pos_mf = kk;
					enc = true;
				}
			}

			// guarda la información en mfUsed
			min = calculamin_max(pos_mf,v,"min", left.getKind());
			max = calculamin_max(pos_mf,v,"max", left.getKind());
			actuliza_min_max(min,max,pos_v,left.getKind());
			//mfUsed[pos_v][0] = calculamin_max(pos_mf,v,"min", left.getKind());
			//mfUsed[pos_v][1] = calculamin_max(pos_mf,v,"max", left.getKind());
			
			// ****** Trata la parte derecha, si es distinta de null *****		
			if (right != null) {
				pos_v = 0;
				pos_mf = 0;
				v = right.getVariable();
				try {
				 mf = right.getMembershipFunction();
				} catch(Exception ex) {
					new XfsgError(7, ex.getMessage());
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
				for (int kk = 0;
					kk < inOrderMfInput[pos_v].getInOrderMemFunc().size()&& !enc; kk++) {
					tmp =inOrderMfInput[pos_v].getInOrderMemFunc().get(kk);
					if (mf != null && tmp.equals(mf.getLabel())) {
						pos_mf = kk;
						enc = true;
					}
				}

				// guarda la información en mfUsed
				min = calculamin_max(pos_mf,v,"min", right.getKind());
				max = calculamin_max(pos_mf,v,"max", right.getKind());
				actuliza_min_max(min,max,pos_v,right.getKind());
				//mfUsed[pos_v][0] = calculamin_max(pos_mf,v,"min", right.getKind());
				//mfUsed[pos_v][1] = calculamin_max(pos_mf,v,"max", right.getKind());
			}
		} else if (
			(left.getKind() == Relation.AND) && (right.getKind() == Relation.IS ||
					right.getKind() == Relation.GREATER || right.getKind() == Relation.GR_EQ ||
					right.getKind() == Relation.SMALLER || right.getKind() == Relation.SM_EQ
					|| right.getKind()==Relation.ISNOT)) {
			// ***** Trata la parte derecha que es de tipo IS, GREATER, GR_EQ, SMALLER, SM_EQ ó ISNOT *****
			int pos_v = 0, pos_mf = 0;
			Variable v = right.getVariable();
			LinguisticLabel mf = null;
			try {
				mf =  right.getMembershipFunction();
			} catch(Exception ex) {
				new XfsgError(7, ex.getMessage());
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
			LinguisticLabel tmp;
			for (int kk = 0;
				kk < inOrderMfInput[pos_v].getInOrderMemFunc().size()&& !enc; kk++) {
				tmp = inOrderMfInput[pos_v].getInOrderMemFunc().get(kk);
				if (mf != null && tmp.equals(mf.getLabel())) {
					pos_mf = kk;
					enc = true;
				}
			}

			// guarda la información en mfUsed
			min = calculamin_max(pos_mf,v,"min", right.getKind());
			max = calculamin_max(pos_mf,v,"max", right.getKind());
			actuliza_min_max(min,max,pos_v,right.getKind());
			//mfUsed[pos_v][0] = calculamin_max(pos_mf,v,"min", right.getKind());
			//mfUsed[pos_v][1] = calculamin_max(pos_mf,v,"max", right.getKind());
			
			// ******* Llama recursivamente a esta función con **** 
			// ******* la parte izquierda                      ****
			// ******* es de tipo AND. ****************************
			Relation newLeft = left.getLeftRelation();
			Relation newRight = left.getRightRelation();
			getMfUsed(newLeft, newRight, nrule);
		} else {
			// Informa de que la regla nrule no es del tipo
			// A & B & C ... ==> Z
			new XfsgError(4, "Left: " + left.toXfl()+"  Right: "+right.toXfl());
			return;
		}

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
		//System.out.println(s+res);
		return res;
	}
	
	
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

