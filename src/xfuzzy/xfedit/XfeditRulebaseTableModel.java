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


package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Modelo de la tabla para la representación de una base de reglas
 * en formato tabular
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseTableModel extends AbstractTableModel
implements TableCellRenderer {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603037L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Etiqueta para representar las celdas no editables
	 */
	private XLabel label;
	
	/**
	 * Etiqueta para representar las celdas editables
	 */
	private JLabel text;
	
	/**
	 * Lista de variables de entrada de la base de reglas
	 */
	private Variable[] input;
	
	/**
	 * Lista de variables de salida de la base de reglas
	 */
	private Variable[] output;
	
	/**
	 * Grados de activación de cada regla
	 */
	private double[] degree;
	
	/**
	 * Matriz de las funciones de pertenencia de los antecedentes de
	 * cada regla
	 */
	private LinguisticLabel[][] ipmf;
	
	/**
	 * Matriz de las funciones de pertenencia de los consecuentes de
	 * cada regla
	 */
	private LinguisticLabel[][] opmf;
	
	/**
	 * Editor de las celdas de las variables de entrada
	 */
	private DefaultCellEditor[] ieditor;
	
	/**
	 * Editor de las celdas de las variables de salida
	 */
	private DefaultCellEditor[] oeditor;
	
	/**
	 * Marcador para indicar si la base de reglas se puede
	 * representar en formato tabular
	 */
	private boolean valid;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseTableModel(Rulebase copy) {
		super();
		initialize(copy);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la referencia a la variable de entrada i-ésima
	 */
	public Variable getInput(int i) {
		return this.input[i];
	}

	/**
	 * Obtiene la referencia a la variable de salida i-ésima
	 */
	public Variable getOutput(int i) {
		return this.output[i];
	}

	/**
	 * Obtiene el número de variables de entrada del modelo
	 */
	public int getInputLength() {
		return this.input.length;
	}

	/**
	 * Obtiene el número de variables de salida del modelo
	 */
	public int getOutputLength() {
		return this.output.length;
	}

	/**
	 * Verifica que la base de reglas cumpla las restricciones de la forma tabular
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Obtiene el número de columnas de la tabla
	 */
	public int getColumnCount() {
		return (input.length*2+3+output.length);
	}

	/**
	 * Obtiene el número de filas de la tabla
	 */
	public int getRowCount() {
		return degree.length+1;
	}

	/**
	 * Obtiene un elemento de la tabla
	 */
	public Object getValueAt(int row, int column) { 
		if(column == 0 && row == degree.length) return "*";
		if(row == degree.length) return "";
		if(column == 0) return ""+row;
		if(column == 1) return ""+degree[row];
		if(column == 2) return "if";
		if(column == (input.length*2+2) ) return "->";
		if(isInputIndex(column)) return ipmf[row][getInputIndex(column)];
		if(isOutputIndex(column)) return opmf[row][getOutputIndex(column)];
		return "&";
	}

	/**
	 * Asigna un elemento de la tabla
	 */
	public void setValueAt(Object value, int row, int column) {
		if(row == degree.length) addRow(); 
		if(column == 1) {
			double deg;
			try { deg = Double.parseDouble((String) value); }
			catch(Exception ex) { deg = -1; }
			if(deg <0.0 || deg > 1.0) Toolkit.getDefaultToolkit().beep(); 
			else degree[row] = deg;
		}
		if(isInputIndex(column)) {
			LinguisticLabel pmf;
			try { pmf = (LinguisticLabel) value; } catch (Exception ex) { pmf = null; }
			ipmf[row][getInputIndex(column)] = pmf;
		}
		if(isOutputIndex(column)) {
			LinguisticLabel pmf;
			try { pmf = (LinguisticLabel) value; } catch (Exception ex) { pmf = null; }
			opmf[row][getOutputIndex(column)] = pmf;
		}
		fireTableCellUpdated(row, column);
	}

	/**
	 * Obtiene las reglas representadas en la tabla
	 */
	public boolean getRules(Rulebase copy) {
		copy.removeAllRules();
		for(int i=0; i<ipmf.length; i++) {
			boolean flag = false;
			for(int j=0; j<ipmf[i].length; j++) if(ipmf[i][j] != null) flag = true;
			if(!flag) return false;
		}
		for(int i=0; i<opmf.length; i++) {
			boolean flag = false;
			for(int j=0; j<opmf[i].length; j++) if(opmf[i][j] != null) flag = true;
			if(!flag) return false;
		}
		for(int i=0; i<degree.length; i++) {
			Relation premise = null;
			for(int j=input.length-1; j>=0; j--) if(ipmf[i][j] != null)  {
				Relation rel = Relation.create(Relation.IS,null,null,input[j],
						ipmf[i][j],null);
				if(premise == null) premise = rel;
				else premise=Relation.create(Relation.AND,rel,premise,null,null,copy);
			}
			Rule newrule = new Rule(premise,degree[i]);
			for(int j=0; j<opmf[i].length; j++) if(opmf[i][j] != null)
				newrule.add(new Conclusion(output[j],opmf[i][j],copy));
			copy.addRule(newrule);
		}
		return true;
	}

	/**
	 * Obtiene el título de una columna de la tabla
	 */
	public String getColumnName(int column) {
		if(column == 0) return "Rule";
		if(column == 1 || column == 2) return "";
		if(isInputIndex(column)) return input[getInputIndex(column)].getName();
		if(isOutputIndex(column)) return output[getOutputIndex(column)].getName();
		return "";
	}

	/**
	 * Obtiene el editor de las celdas de la tabla
	 */
	public TableCellEditor getEditor(int row, int column) {
		if(isInputIndex(column)) return ieditor[getInputIndex(column)];
		if(isOutputIndex(column)) return oeditor[getOutputIndex(column)];
		return null; 
	}

	/**
	 * Obtiene la clase de un elemento de la tabla
	 */
	public Class getColumnClass(int column) {
		if(column == 1) return Object.class;
		if(isInputIndex(column) || isOutputIndex(column)) return JComboBox.class;
		return XLabel.class;
	}

	/**
	 * Verifica que una celda sea editable
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 1) return true;
		if(isInputIndex(columnIndex) || isOutputIndex(columnIndex)) return true;
		return false;
	}

	/**
	 * Obtiene el componente que representa cada celda
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(column != 1 && !isInputIndex(column) && !isOutputIndex(column) )
		{ label.setText(value.toString()); return label; }
		if(isSelected) {
			text.setForeground(table.getSelectionForeground());
			text.setBackground(XConstants.textselectedbg);
		} else {
			text.setForeground(Color.black);
			text.setBackground(XConstants.textbackground);
		}
		if(row == degree.length) { text.setText(""); return text; }
		if(column == 1) { text.setText(value.toString()); return text; }
		if(isInputIndex(column)) {
			int index = getInputIndex(column);
			if(ipmf[row][index] == null) text.setText("");
			else text.setText(input[index]+" == "+ipmf[row][index]);
		}
		if(isOutputIndex(column)) {
			int index = getOutputIndex(column);
			if(opmf[row][index] == null) text.setText("");
			else text.setText(output[index]+" = "+opmf[row][index]);
		}
		return text;
	}

	/**
	 * Añade una fila (regla) a la tabla
	 */
	public void addRow() {
		LinguisticLabel auxipmf[][] = new LinguisticLabel[degree.length+1][input.length];
		System.arraycopy(ipmf,0,auxipmf,0,degree.length);
		auxipmf[degree.length] = new LinguisticLabel[input.length];
		ipmf = auxipmf;

		LinguisticLabel auxopmf[][] = new LinguisticLabel[degree.length+1][output.length];
		System.arraycopy(opmf,0,auxopmf,0,degree.length);
		auxopmf[degree.length] = new LinguisticLabel[output.length];
		opmf = auxopmf;

		double auxdeg[] = new double[degree.length + 1];
		System.arraycopy(degree,0,auxdeg,0,degree.length);
		auxdeg[degree.length] = 1.0;
		degree = auxdeg;

		fireTableChanged(new TableModelEvent(this, degree.length, degree.length,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	/**
	 * Elimina una fila (regla) de la tabla
	 */
	public void removeRow(int row) {
		LinguisticLabel auxipmf[][] = new LinguisticLabel[degree.length-1][input.length];
		System.arraycopy(ipmf,0,auxipmf,0,row);
		System.arraycopy(ipmf,row+1,auxipmf,row,degree.length-row-1);
		ipmf = auxipmf;

		LinguisticLabel auxopmf[][] = new LinguisticLabel[degree.length-1][output.length];
		System.arraycopy(opmf,0,auxopmf,0,row);
		System.arraycopy(opmf,row+1,auxopmf,row,degree.length-row-1);
		opmf = auxopmf;

		double auxdeg[] = new double[degree.length - 1];
		System.arraycopy(degree,0,auxdeg,0,row);
		System.arraycopy(degree,row+1,auxdeg,row,degree.length-row-1);
		degree = auxdeg;

		fireTableChanged(new TableModelEvent(this, row, row,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Inicializa el contenido de la tabla
	 */
	private void initialize(Rulebase copy) {
		label = new XLabel("");
		label.setLabelFont(XConstants.textfont);
		text = new JLabel("");
		text.setFont(XConstants.textfont);
		text.setOpaque(true);

		input = copy.getInputs();
		output = copy.getOutputs();

		initializeInputCombo();
		initializeOutputCombo();
		valid = initializeRules(copy);
	}

	/**
	 * Crea los menús desplegables de las variables de entrada
	 */
	private void initializeInputCombo() {
		ieditor = new DefaultCellEditor[input.length];
		for(int i=0; i<input.length; i++) {
			JComboBox newcombo = new JComboBox();
			newcombo.setBackground(XConstants.textbackground);
			newcombo.setFont(XConstants.textfont);
			newcombo.setEditable(false);
			newcombo.setRenderer(new XfeditRulebaseTableRenderer(this,i,true));
			newcombo.addItem("");
			LinguisticLabel[] pmf = input[i].getType().getAllMembershipFunctions();
			for(int k=0; k<pmf.length; k++) newcombo.addItem(pmf[k]);
			ieditor[i] = new DefaultCellEditor(newcombo);
		}
	}

	/**
	 * Crea los menús desplegables de las variables de salida
	 */
	private void initializeOutputCombo() {
		oeditor = new DefaultCellEditor[output.length];
		for(int i=0; i<output.length; i++) {
			JComboBox newcombo = new JComboBox();
			newcombo.setBackground(XConstants.textbackground);
			newcombo.setFont(XConstants.textfont);
			newcombo.setEditable(false);
			newcombo.setRenderer(new XfeditRulebaseTableRenderer(this,i,false));
			newcombo.addItem("");
			LinguisticLabel[] pmf = output[i].getType().getAllMembershipFunctions();
			for(int k=0; k<pmf.length; k++) newcombo.addItem(pmf[k]);
			oeditor[i] = new DefaultCellEditor(newcombo);
		}
	}


	/**
	 * Genera el contenido de las celdas (proposiciones)
	 */
	private boolean initializeRules(Rulebase copy) {
		Rule[] rule = copy.getRules();
		degree = new double[rule.length];
		ipmf = new LinguisticLabel[rule.length][input.length];
		opmf = new LinguisticLabel[rule.length][output.length];

		for(int i=0; i<rule.length; i++) {
			Relation rel[] = crumblePremise(rule[i]);
			Conclusion conc[] = crumbleConclusion(rule[i]);
			if(rel == null || conc == null) return false;
			degree[i] = rule[i].getDegree();
			for(int j=0; j<input.length; j++)
				if(rel[j] != null) ipmf[i][j] = rel[j].getMembershipFunction();
			for(int j=0; j<output.length; j++)
				if(conc[j] != null) opmf[i][j] = conc[j].getMembershipFunction();
		}
		return true;
	}

	/**
	 * Disgrega el antecedente de una regla formado por conjunción de 
	 * proposiciones de igualdad
	 */
	private Relation[] crumblePremise(Rule rl) {
		Relation crumble[] = new Relation[input.length];
		Relation disorder[] = crumbleRelation(rl.getPremise());
		if(disorder == null) return null;
		for(int i=0; i<disorder.length; i++) {
			int index = -1;
			Variable var = disorder[i].getVariable();
			for(int j=0; j<input.length; j++) if(input[j] == var) index = j;
			if(crumble[index] != null) return null;
			crumble[index] = disorder[i];
		}
		return crumble;
	}

	/**
	 * Disgrega el conjunto de conclusiones de una regla
	 */
	private Conclusion[] crumbleConclusion(Rule rl) {
		Conclusion disorder[] = rl.getConclusions();
		Conclusion crumble[] = new Conclusion[output.length];
		for(int i=0; i<disorder.length; i++) {
			int index = -1;
			Variable var = disorder[i].getVariable();
			for(int j=0; j<output.length; j++) if(output[j] == var) index = j;
			if(crumble[index] != null) return null;
			crumble[index] = disorder[i];
		}
		return crumble;
	}

	/**
	 * Disgrega una proposición compuesta
	 */
	private Relation[] crumbleRelation(Relation rel) {
		switch(rel.getKind()) {
			case Relation.IS:
				Relation[] out = new Relation[1]; out[0] = rel;
				return out;
			case Relation.AND:
				Relation[] left = crumbleRelation(rel.getLeftRelation());
				Relation[] right = crumbleRelation(rel.getRightRelation());
				if(left == null || right == null) return null;
				Relation[] crumble = new Relation[left.length + right.length];
				for(int i=0; i<left.length; i++) crumble[i] = left[i];
				for(int i=0; i<right.length; i++) crumble[left.length + i] = right[i];
				return crumble;
			default: return null;
		}
	}

	/**
	 * Estudia si el índice corresponde a una columna de entrada
	 */
	private boolean isInputIndex(int column) {
		if(column>2 && column<input.length*2+2 && (column-3)%2==0) return true;
		return false;
	}

	/**
	 * Estudia si el índice corresponde a una columna de salida
	 */
	private boolean isOutputIndex(int column) {
		if(column>input.length*2+2 && column<input.length*2+3+output.length)
			return true;
		return false;
	}

	/**
	 * Obtiene el indice de la variable de entrada de una columna
	 */
	private int getInputIndex(int column) {
		if(column>2 && column<input.length*2+2 && (column-3)%2==0)
			return (column-3)/2;
		return -1;
	}

	/**
	 * Obtiene el índice de la variable de salida de una columna
	 */
	private int getOutputIndex(int column) {
		if(column > input.length*2+2 && column < input.length*2+3+output.length)
			return column-input.length*2-3;
		return -1;
	}
}
