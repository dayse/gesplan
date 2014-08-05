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
 * Clase que permite almacenar los ficheros XFL de forma eficiente
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XflSaver {

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Almacena el sistema difuso en formato XFL3
	 */
	
	public static boolean save(Specification spec, File file) {
		try {
			spec.setFile(file);
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream stream = new PrintStream(fos);
			printSpecification(stream,spec);
			stream.close();
		} catch(IOException e) { return false; }
		return true;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Escribe sobre el flujo la descripcion XFL3 del sistema difuso
	 */
	private static void printSpecification(PrintStream stream, Specification spec) {
		Operatorset[] opset = spec.getOperatorsets();
		Type[] type = spec.getTypes();
		Rulebase[] rulebase = spec.getRulebases();
		CrispBlockSet crisp = spec.getCrispBlockSet();
		SystemModule system = spec.getSystemModule();
		
		for(int i=0; i<opset.length; i++) printOperatorSet(stream,opset[i]);
		for(int i=0; i<type.length; i++) printType(stream, type[i]);
		for(int i=0; i<rulebase.length; i++) printRulebase(stream, rulebase[i]);
		if(crisp != null) printCrispBlockSet(stream, crisp);
		if(system != null) printSystemModule(stream, system);		
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de un conjunto de operadores
	 * @param stream
	 * @param opset
	 */
	private static void printOperatorSet(PrintStream stream, Operatorset opset) {
		if(opset.isDefault()) return;
		stream.println("operatorset "+opset.getName()+" {");
		printFuzzyOperator(stream,opset.and,"and");
		printFuzzyOperator(stream,opset.or,"or");
		printFuzzyOperator(stream,opset.imp,"imp");
		printFuzzyOperator(stream,opset.also,"also");
		printFuzzyOperator(stream,opset.not,"not");
		printFuzzyOperator(stream,opset.very,"strongly");
		printFuzzyOperator(stream,opset.moreorless,"moreorless");
		printFuzzyOperator(stream,opset.slightly,"slightly");
		printFuzzyOperator(stream,opset.defuz,"defuz");
		stream.println("}");
		stream.println();
	}

	/**
	 * Escribe sobre el flujo la descripción XFL3 de un operador
	 * @param stream
	 * @param fzop
	 * @param label
	 */
	private static void printFuzzyOperator(PrintStream stream, FuzzyOperator fzop, String label) {
		if(!fzop.isDefault()) stream.println("   "+label+" "+fzop.getXflDescription());
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de un tipo de variable
	 * @param stream
	 * @param type
	 */
	private static void printType(PrintStream stream, Type type) {
		Family[] fam = type.getFamilies();
		LinguisticLabel[] mf = type.getMembershipFunctions();
		
		stream.print("type "+type.getName()+" ");
		if(type.getParent() == null) {
			Universe u = type.getUniverse();
			stream.print("["+u.min()+","+u.max()+";"+u.card()+"]");
		}
		else stream.print("extends "+type.getParent().getName());
		stream.println(" {");

		for(int i=0; i<fam.length; i++) printFamily(stream,fam[i]);
		for(int i=0; i<mf.length; i++) printLabel(stream,mf[i]);

		stream.println("}");
		stream.println();
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de una familia de MFs
	 * @param stream
	 * @param fam
	 */
	private static void printFamily(PrintStream stream, Family fam) {
		stream.println("  "+fam.getLabel()+"[] "+fam.getXflDescription());
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de una etiqueta lingüística
	 * @param stream
	 * @param mf
	 */
	private static void printLabel(PrintStream stream, LinguisticLabel mf) {
		if(mf instanceof FamiliarMemFunc) {
			FamiliarMemFunc fmf = (FamiliarMemFunc) mf;
			stream.print("  "+fmf.getLabel()+" "+fmf.getFamily().getLabel());
			stream.println("["+fmf.getIndex()+"];");
		} else {
			ParamMemFunc pmf = (ParamMemFunc) mf;
			stream.println("  "+pmf.getLabel()+" "+pmf.getXflDescription());
		}
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de una base de reglas
	 * @param stream
	 * @param base
	 */
	private static void printRulebase(PrintStream stream, Rulebase base) {
		Variable[] inputvar = base.getInputs();
		Variable[] outputvar = base.getOutputs();
		Operatorset opset = base.getOperatorset();
		Rule[] rule = base.getRules();
		
		stream.print("rulebase "+base.getName()+" (");
		for(int i=0; i<inputvar.length; i++) {
			if(i>0) stream.print(", ");
			stream.print(inputvar[i].getType().getName()+" "+inputvar[i].getName());
		}
		stream.print(" : ");
		for(int i=0; i<outputvar.length; i++)  {
			if(i>0) stream.print(", ");
			stream.print(outputvar[i].getType().getName()+" "+outputvar[i].getName());
		}
		stream.print(")");
		if(!opset.isDefault()) stream.print(" using "+opset.getName());
		stream.println(" {");
		for(int i=0; i<rule.length; i++) printRule(stream,rule[i]);
		stream.println("}");
		stream.println();
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de una regla
	 * @param stream
	 * @param rule
	 */
	private static void printRule(PrintStream stream, Rule rule) {
		double degree = rule.getDegree();
		Relation antecedent = rule.getPremise();
		Conclusion[] conclusion = rule.getConclusions();

		stream.print("  ");
		if(degree != 1.0) stream.print("["+degree+"] ");
		stream.print( "if(" );
		printRelation(stream,antecedent);
		stream.print(") -> ");
		printConclusion(stream,conclusion[0]);
		for(int i=1; i<conclusion.length; i++) {
			stream.print(", ");
			printConclusion(stream,conclusion[i]);
		}
		stream.println(";");
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de una proposición difusa
	 * @param stream
	 * @param rel
	 */
	private static void printRelation(PrintStream stream, Relation rel) {
		if(rel instanceof SingleRelation) {
			String var = rel.getVariable().getName();
			String label = rel.getMembershipFunction().getLabel();
			String op = "";
			switch(rel.getKind()) {
				case Relation.IS:      op = " == "; break;
				case Relation.GREATER: op = " > "; break;
				case Relation.GR_EQ:   op = " >= "; break;
				case Relation.SMALLER: op = " < "; break;
				case Relation.SM_EQ:   op = " <= "; break; 
				case Relation.APP_EQ:  op = " ~= "; break;
				case Relation.ISNOT:   op = " != "; break;
				case Relation.SL_EQ:   op = " %= "; break;
				case Relation.VERY_EQ: op = " += "; break;
			}
			stream.print(var+op+label);
		} else if(rel instanceof UnaryRelation) {
			Relation rr = rel.getRightRelation();
			String op = "";
			switch(rel.getKind()) {
				case Relation.NOT : op = "!"; break;
				case Relation.MoL : op = "~"; break;
				case Relation.VERY : op = "+"; break;
				case Relation.SLIGHTLY : op = "%"; break;
			}
			stream.print(op+"(");
			printRelation(stream,rr);
			stream.print(")");
		} else if(rel instanceof BinaryRelation) {
			Relation left = rel.getLeftRelation();
			Relation right = rel.getRightRelation();
			boolean lparen = (left instanceof BinaryRelation && left.getKind() != rel.getKind());
			boolean rparen = (right instanceof BinaryRelation && right.getKind() != rel.getKind());
			
			if(lparen) stream.print("(");
			printRelation(stream,left);
			if(lparen) stream.print(")");
			if(rel.getKind() == Relation.AND) stream.print(" & ");
			else if(rel.getKind() == Relation.OR) stream.print(" | ");
			if(rparen) stream.print("(");
			printRelation(stream,right);
			if(rparen) stream.print(")");
		}
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de una conclusión de una regla
	 * @param stream
	 * @param conc
	 */
	private static void printConclusion(PrintStream stream, Conclusion conc) {
		String var = conc.getVariable().getName();
		String label = conc.getMembershipFunction().getLabel();
		stream.print(var+" = "+label);
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de los bloques no difusos
	 * @param stream
	 * @param crisp
	 */
	private static void printCrispBlockSet(PrintStream stream, CrispBlockSet crisp) {
		CrispBlock[] block = crisp.getBlocks();
		if(block.length == 0) return;
		stream.println("crisp {");
		for(int i=0; i<block.length; i++) printCrispBlock(stream,block[i]);
		stream.println("}");
		stream.println();
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de un bloques no difuso
	 * @param stream
	 * @param block
	 */
	private static void printCrispBlock(PrintStream stream, CrispBlock block) {
		stream.println("  "+block.getLabel()+" "+block.getXflDescription());
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de la estructura modular
	 * @param stream
	 * @param system
	 */
	private static void printSystemModule(PrintStream stream, SystemModule system) {
		Variable input[] = system.getInputs();
		Variable output[] = system.getOutputs();
		ModuleCall[] call = system.getModuleCalls();
		stream.print("system (");
		for(int i=0; i<input.length; i++) {
			if(i>0) stream.print(", ");
			stream.print(input[i].getType().getName()+" "+input[i].getName());
		}
		stream.print(" : ");
		for(int i=0; i<output.length; i++) {
			if(i>0) stream.print(", ");
			stream.print(output[i].getType().getName()+" "+output[i].getName());
		}
		stream.println(") {");
		for(int i=0; i<call.length; i++) printModuleCall(stream,call[i]);
		stream.println(" }");
	}
	
	/**
	 * Escribe sobre el flujo la descripción XFL3 de la llamada a un módulo
	 * @param stream
	 * @param call
	 */
	private static void printModuleCall(PrintStream stream, ModuleCall call) {
		Variable[] inputvar = call.getInputVariables();
		Variable[] outputvar = call.getOutputVariables();
		stream.print("  "+call.getName()+"(");
		for(int i=0; i<inputvar.length; i++) {
			stream.print( (i==0? "": ", ") );
			stream.print((inputvar[i] == null? "null" : inputvar[i].getName()));
		}
		stream.print( " : " );
		for(int i=0; i<outputvar.length; i++) {
			stream.print( (i==0? "": ", ") );
			stream.print( (outputvar[i] == null? "null" : outputvar[i].getName()) );
		}
		stream.println(");");		
	}
}
