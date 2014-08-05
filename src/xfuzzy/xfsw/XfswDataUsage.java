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

package xfuzzy.xfsw;

import xfuzzy.lang.*;

/**
 * Clase que estudia las funciones que realmente se utilizan en un
 * sistema difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfswDataUsage {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Sistema difuso a estudiar
	 */
	private Specification spec;
	
	/**
	 * Funciones de pertenencia utilizadas en el sistema
	 */
	private ParamMemFunc[] mflist;
	
	/**
	 * Lista de los nombres de las funciones de pertenencia
	 * utilizadas en el sistema
	 */
	private String[] mfnames;
	
	/**
	 * Marcadores para indicar la utilización del modificador
	 * mayor_o_igual sobre una función de pertenencia
	 */
	private boolean[] mfgreq;
	
	/**
	 * Marcadores para indicar la utilización del modificador
	 * menor_o_igual sobre una función de pertenencia
	 */
	private boolean[] mfsmeq;
	
	/**
	 * Marcadores para indicar la utilización del método center
	 * sobre una función de pertenencia
	 */
	private boolean[] mfcenter;
	
	/**
	 * Marcadores para indicar la utilización del método basis
	 * sobre una función de pertenencia
	 */
	private boolean[] mfbasis;
	
	/**
	 * Marcadores para indicar la utilización del método param
	 * sobre una función de pertenencia
	 */
	private boolean[] mfparam;
	
	/**
	 * Familias de funciones de pertenencia utilizadas en el sistema
	 */
	private Family[] famlist;
	
	/**
	 * Lista de los nombres de las familias de funciones de pertenencia
	 * utilizadas en el sistema
	 */
	private String[] famnames;
	
	/**
	 * Marcadores para indicar la utilización del modificador
	 * mayor_o_igual sobre una familia de funciones de pertenencia
	 */
	private boolean[] famgreq;
	
	/**
	 * Marcadores para indicar la utilización del modificador
	 * menor_o_igual sobre una familia de funciones de pertenencia
	 */
	private boolean[] famsmeq;
	
	/**
	 * Marcadores para indicar la utilización del método center
	 * sobre una familia de funciones de pertenencia
	 */
	private boolean[] famcenter;
	
	/**
	 * Marcadores para indicar la utilización del método basis
	 * sobre una familia de funciones de pertenencia
	 */
	private boolean[] fambasis;
	
	/**
	 * Marcadores para indicar la utilización del método param
	 * sobre una familia de funciones de pertenencia
	 */
	private boolean[] famparam;
	

	/**
	 * Lista de tipos definidos en el sistema
	 */
	private Type[] type;
	
	/**
	 * Marcadores para indicar si un tipo se utiliza en variables
	 * de entrada de las bases de reglas
	 */
	private boolean[] inputtype;
	
	/**
	 * Marcadores para indicar si un tipo se utiliza en variables
	 * de salida de las bases de reglas
	 */
	private boolean[] outputtype;
	
	/**
	 * Marcadores de los conjuntos de operadores
	 */
	private boolean[][] opsetflags;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	public XfswDataUsage(Specification spec) {
		this.spec = spec;
		
		Operatorset[] opset = spec.getOperatorsets();
		this.opsetflags = new boolean[opset.length][7];
		
		// Funciones de pertenencia
		createMFList();
		int mfsize = mflist.length;
		this.mfgreq = new boolean[mfsize];
		this.mfsmeq = new boolean[mfsize];
		this.mfcenter = new boolean[mfsize];
		this.mfbasis = new boolean[mfsize];
		this.mfparam = new boolean[mfsize];
		
		// Familias de funciones de pertenencia
		createFamilyList();
		int famsize = famlist.length;
		this.famgreq = new boolean[famsize];
		this.famsmeq = new boolean[famsize];
		this.famcenter = new boolean[famsize];
		this.fambasis = new boolean[famsize];
		this.famparam = new boolean[famsize];
		
		// Usos de las familias y funciones de pertenencia
		setUses();
	
		// Usos de los tipos
		this.type = spec.getTypes();
		this.inputtype = new boolean[type.length];
		this.outputtype = new boolean[type.length];
		setTypeUses();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Verifica si los métodos de concreción utilizados hacen uso
	 * del método "compute", referido al cálculo del grado de pertenencia
	 * de una salida como agregación de implicaciones.
	 * Esto lo usan los métodos de concreción tipo "CenterOfArea"
	 */
	public boolean isComputeUsed() {
		Operatorset[] opset = spec.getOperatorsets();
		for(int i=0; i<opset.length; i++) {
			if(isComputeUsed(opset[i].defuz)) return true;
		}
		return false;
	}
	
	/**
	 * Obtiene los marcadores de un determinado conjunto de operadores
	 * @param os
	 * @return
	 */
	public boolean[] getOpsetUsages(Operatorset os) {
		int index = getIndex(os);
		return opsetflags[index];
	}
	
	//----------------------------------------------------------------------------//
	// Métodos referidos a los usos de las funciones de pertenencia               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la lista de funciones de pertenencia utilizadas
	 */
	public ParamMemFunc[] getMFList() {
		return this.mflist;
	}
	
	/**
	 * Obtiene el nombre utilizado para codificar la función de 
	 * pertenencia
	 * @param mf
	 * @return
	 */
	public String getMFname(ParamMemFunc mf) {
		return "MF_"+mf.getPackageName()+"_"+mf.getFunctionName();
	}
	
	/**
	 * Verifica si se utiliza el modificador mayor_o_igual sobre
	 * la función de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isMFGreqUsed(String name) {
		int index = getIndex(mfnames, name);
		return this.mfgreq[index];
	}
	
	/**
	 * Verifica si se utiliza el modificador menor_o_igual sobre
	 * la función de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isMFSmeqUsed(String name) {
		int index = getIndex(mfnames, name);
		return this.mfsmeq[index];
	}
	
	/**
	 * Verifica si se utiliza el método center sobre
	 * la función de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isMFCenterUsed(String name) {
		int index = getIndex(mfnames, name);
		return this.mfcenter[index];
	}
	
	/**
	 * Verifica si se utiliza el método basis sobre
	 * la función de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isMFBasisUsed(String name) {
		int index = getIndex(mfnames, name);
		return this.mfbasis[index];
	}
	
	/**
	 * Verifica si se utiliza el método param sobre
	 * la función de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isMFParamUsed(String name) {
		int index = getIndex(mfnames, name);
		return this.mfparam[index];
	}
	
	//----------------------------------------------------------------------------//
	// Métodos referidos a los usos de las familias de funciones de pertenencia   //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la lista de familias de funciones de pertenencia utilizadas
	 */
	public Family[] getFamilyList() {
		return this.famlist;
	}
	
	/**
	 * Obtiene el nombre utilizado para codificar la familia de
	 * funciones de pertenencia
	 * @param fam
	 * @return
	 */
	public String getFamilyName(Family fam) {
		return "FMF_"+fam.getPackageName()+"_"+fam.getFunctionName();
	}
	
	/**
	 * Verifica si se utiliza el modificador mayor_o_igual sobre
	 * la familia de funciones de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isFamilyGreqUsed(String name) {
		int index = getIndex(famnames, name);
		return this.famgreq[index];
	}
	
	/**
	 * Verifica si se utiliza el modificador menor_o_igual sobre
	 * la familia de funciones de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isFamilySmeqUsed(String name) {
		int index = getIndex(famnames, name);
		return this.famsmeq[index];
	}
	
	/**
	 * Verifica si se utiliza el método center sobre
	 * la familia de funciones de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isFamilyCenterUsed(String name) {
		int index = getIndex(famnames, name);
		return this.famcenter[index];
	}
	
	/**
	 * Verifica si se utiliza el método basis sobre
	 * la familia de funciones de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isFamilyBasisUsed(String name) {
		int index = getIndex(famnames, name);
		return this.fambasis[index];
	}
	
	/**
	 * Verifica si se utiliza el método param sobre
	 * la familia de funciones de pertenencia
	 * @param mf
	 * @return
	 */
	public boolean isFamilyParamUsed(String name) {
		int index = getIndex(famnames, name);
		return this.famparam[index];
	}
	
	//----------------------------------------------------------------------------//
	// Métodos relacionados con los conjuntos de operadores
	//----------------------------------------------------------------------------//

	/**
	 * Verifica si el método de concreción utiliza la función compute
	 */
	public boolean isComputeUsed(DefuzMethod defuz)  {
		String code = defuz.getCCode();
		return code.contains("compute");
	}
	
	/**
	 * Verifica si el método de concreción utiliza la función center
	 * @param defuz
	 * @return
	 */
	public boolean isCenterUsed(DefuzMethod defuz)  {
		String code = defuz.getCCode();
		return code.contains("center");
	}
	
	/**
	 * Verifica si el método de concreción utiliza la función basis
	 * @param defuz
	 * @return
	 */
	public boolean isBasisUsed(DefuzMethod defuz)  {
		String code = defuz.getCCode();
		return code.contains("basis");
	}
	
	/**
	 * Verifica si el método de concreción utiliza la función param
	 * @param defuz
	 * @return
	 */
	public boolean isParamUsed(DefuzMethod defuz)  {
		String code = defuz.getCCode();
		return code.contains("param");
	}
	
	/**
	 * Verifica si el método de concreción utiliza los valores de las entradas
	 * @param defuz
	 * @return
	 */
	public boolean isInputUsed(DefuzMethod defuz)  {
		String code = defuz.getCCode();
		return code.contains("input");
	}
	
	//----------------------------------------------------------------------------//
	// Métodos relacionados con los tipos de variables difusas                    //
	//----------------------------------------------------------------------------//

	/**
	 * Verifica si un tipo de variable se utiliza como entrada
	 */
	public boolean isUsedAsInput(Type t) {
		for(int i=0; i<type.length; i++) if(type[i] == t) return inputtype[i];
		return false;
	}
	
	/**
	 * Verifica si un tipo de variable se utiliza como salida
	 * @param t
	 * @return
	 */
	public boolean isUsedAsOutput(Type t) {
		for(int i=0; i<type.length; i++) if(type[i] == t) return outputtype[i];
		return false;
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Busca las funciones de pertenencia incluidas
	 */
	private void createMFList() {
		this.mfnames = new String[0];
		this.mflist = new ParamMemFunc[0];
		
		Type[] type = spec.getTypes();

		for(int i=0; i<type.length; i++) {
			ParamMemFunc[] mf = type[i].getParamMembershipFunctions();
			for(int j=0; j<mf.length; j++) {
				String mfname = getMFname(mf[j]);
				if(!contains(mfnames,mfname)) {
					mfnames = addString(mfnames,mfname);
					mflist = addMF(mflist,mf[j]);
				}
			}
		}
	}
	
	/**
	 * Busca las familias de funciones de pertenencia incluidas
	 */
	private void createFamilyList() {
		this.famnames = new String[0];
		this.famlist = new Family[0];
		Type[] type = spec.getTypes();

		for(int i=0; i<type.length; i++) {
			Family[] fam = type[i].getFamilies();
			for(int j=0; j<fam.length; j++) {
				String famname = getFamilyName(fam[j]);
				if(!contains(famnames, famname)) {
					famnames = addString(famnames, famname);
					famlist = addFamily(famlist,fam[j]);
				}
			}
		}
	}

	/**
	 * Añade una familia a una lista de familias
	 * @param list
	 * @param added
	 * @return
	 */
	private Family[] addFamily(Family[] list, Family added) {
		Family[] aux = new Family[list.length+1];
		System.arraycopy(list, 0, aux, 0, list.length);
		aux[list.length] = added;
		return aux;
	}
	
	/**
	 * Añade una función de pertenencia a una lista de funciones de pertenencia
	 * @param list
	 * @param added
	 * @return
	 */
	private ParamMemFunc[] addMF(ParamMemFunc[] list, ParamMemFunc added) {
		ParamMemFunc[] aux = new ParamMemFunc[list.length+1];
		System.arraycopy(list, 0, aux, 0, list.length);
		aux[list.length] = added;
		return aux;
	}
	
	/**
	 * Añade una cadena a una lista de cadenas
	 * @param list
	 * @param added
	 * @return
	 */
	private String[] addString(String[] list, String added) {
		String[] aux = new String[list.length+1];
		System.arraycopy(list, 0, aux, 0, list.length);
		aux[list.length] = added;
		return aux;
	}
			
	/**
	 * Estudia si una cadena está incluida en una lista 
	 * @param list
	 * @param element
	 * @return
	 */
	private boolean contains(String[] list, String element) {
		for(int i=0; i<list.length; i++) if(list[i].equals(element)) return true;
		return false;
	}
	
	/**
	 * Obtiene la posición de un nombre en una lista de nombres
	 * @param mf
	 * @return
	 */
	private int getIndex(String[] list, String name) {
		for(int i=0; i<list.length; i++) {
			if(list[i].equals(name)) return i;
		}
		return 0;
	}
	
	/**
	 * Obtiene la posición de un conjunto de operadores
	 * @param os
	 * @return
	 */
	private int getIndex(Operatorset os) {
		Operatorset[] opsetlist = spec.getOperatorsets();
		for(int i=0; i<opsetlist.length; i++) {
			if(opsetlist[i] == os) return i;
		}
		return 0;
	}
	
	/**
	 * Calcula el valor de los marcadores de uso realizando un recorrido
	 * sobre las bases de reglas
	 *
	 */
	private void setUses() {
		Rulebase[] rulebase = this.spec.getRulebases();
		for(int i=0; i<rulebase.length; i++) {
			Rule[] rule = rulebase[i].getRules();
			Operatorset opset = rulebase[i].getOperatorset();
			int opsetindex = getIndex(opset);
			opsetflags[opsetindex][6] = isComputeUsed(opset.defuz);
			boolean usesCenter = isCenterUsed(opset.defuz);
			boolean usesBasis = isBasisUsed(opset.defuz);
			boolean usesParam = isParamUsed(opset.defuz);
			for(int j=0; j<rule.length; j++) {
				setUses(rule[j].getPremise(),opsetindex);
				Conclusion[] conc = rule[j].getConclusions();
				for(int k=0; k<conc.length; k++) setUses(conc[k],usesCenter,usesBasis, usesParam);
			}
		}
	}
	
	/**
	 * Recorre el antecedente de una regla para calcular los marcadores
	 * de uso
	 * @param rel
	 */
	private void setUses(Relation rel, int osi) {
		int kind = rel.getKind();
		LinguisticLabel label;
		switch(kind) {
			case Relation.AND:
				opsetflags[osi][0] = true;
				setUses(rel.getLeftRelation(),osi);
				setUses(rel.getRightRelation(),osi);
				break;
			case Relation.OR:
				opsetflags[osi][1] = true;
				setUses(rel.getLeftRelation(),osi);
				setUses(rel.getRightRelation(),osi);
				break;
			case Relation.NOT:
				opsetflags[osi][2] = true;
				setUses(rel.getLeftRelation(),osi);
				break;
			case Relation.MoL:
				opsetflags[osi][3] = true;
				setUses(rel.getLeftRelation(),osi);
				break;
			case Relation.SLIGHTLY:
				opsetflags[osi][4] = true;
				setUses(rel.getLeftRelation(),osi);
				break;
			case Relation.VERY:
				opsetflags[osi][5] = true;
				setUses(rel.getLeftRelation(),osi);
				break;
			case Relation.GR_EQ:
			case Relation.SMALLER:
				label = rel.getMembershipFunction();
				if(label instanceof ParamMemFunc) {
					int index = getIndex(mfnames,getMFname( (ParamMemFunc) label));
					mfgreq[index] = true;
				} else {
					Family fam = ((FamiliarMemFunc) label).getFamily();
					int index = getIndex(famnames, getFamilyName(fam));
					famgreq[index] = true;
				}
				break;
			case Relation.SM_EQ:
			case Relation.GREATER:
				label = rel.getMembershipFunction();
				if(label instanceof ParamMemFunc) {
					int index = getIndex(mfnames,getMFname( (ParamMemFunc) label));
					mfsmeq[index] = true;
				} else {
					Family fam = ((FamiliarMemFunc) label).getFamily();
					int index = getIndex(famnames, getFamilyName(fam));
					famsmeq[index] = true;
				}
				break;
		}
	}
	
	/**
	 * Asigna los marcadores asociados a los consecuentes de las reglas
	 * @param conc
	 * @param center
	 * @param basis
	 * @param param
	 */
	private void setUses(Conclusion conc, boolean center, boolean basis, boolean param) {
		LinguisticLabel label = conc.getMembershipFunction();
		if(label instanceof ParamMemFunc) {
			int index = getIndex(mfnames,getMFname( (ParamMemFunc) label));
			if(center) mfcenter[index] = true;
			if(basis) mfbasis[index] = true;
			if(param) mfparam[index] = true;
		} else {
			Family fam = ((FamiliarMemFunc) label).getFamily();
			int index = getIndex(famnames, getFamilyName(fam));
			if(center) famcenter[index] = true;
			if(basis) fambasis[index] = true;
			if(param) famparam[index] = true;
		}
	}
	
	/**
	 * Asigna los marcadores que indican si un tipo se utiliza como
	 * entrada o como salida
	 *
	 */
	private void setTypeUses() {
		Rulebase[] base = spec.getRulebases();
		for(int i=0; i<base.length; i++) {
			Variable[] input = base[i].getInputs();
			for(int j=0; j<input.length; j++) {
				Type itype = input[j].getType();
				for(int k=0; k<type.length; k++) {
					if(type[k] == itype) inputtype[k] = true;					
				}
			}
			
			Variable[] output = base[i].getOutputs();
			for(int j=0; j<output.length; j++) {
				Type otype = output[j].getType();
				for(int k=0; k<type.length; k++) {
					if(type[k] == otype) outputtype[k] = true;
				}
			}
		}
	}
}
