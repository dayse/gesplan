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
 * en formato libre
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseFreeModel extends AbstractTableModel
implements TableCellRenderer {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603032L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Etiqueta para representar las celdas de las columnas 0, 2 y 5
	 */
	private XLabel label;
	
	/**
	 * Etiqueta para representar las celdas de las columnas 1, 3, y 6
	 */
	private JLabel text;
	
	/**
	 * Valor del grado de certeza de cada regla
	 */
	private double[] degree;
	
	/**
	 * Editor del antecedente de cada regla
	 */
	private XfeditRulebaseFreeAntec[] premise;
	
	/**
	 * Editor del consecuente de cada regla
	 */
	private XfeditRulebaseFreeConseq[] conclusion;
	
	/**
	 * Panel de edición de la base de reglas en formato libre
	 */
	private XfeditRulebaseFreePanel panel;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseFreeModel(XfeditRulebaseFreePanel panel, Rulebase copy) {
		super();
		this.panel = panel;
		initialize(copy);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el número de columnas de la tabla
	 */
	public int getColumnCount() {
		return 6;
	}

	/**
	 * Obtiene el número de filas (reglas) de la tabla
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
		if(column == 3) return premise[row].toString();
		if(column == 4) return "->";
		if(column == 5) return conclusion[row];
		return "";
	}

	/**
	 * Asigna el valor a un elemento de la tabla
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
		fireTableCellUpdated(row, column);
	}

	/**
	 * Obtiene las reglas representadas en la tabla
	 */
	public boolean getRules(Rulebase copy) {
		copy.removeAllRules();

		for(int i=0; i<degree.length; i++) {
			if(premise[i].isIncomplete()) return false;
			if(conclusion[i].isIncomplete()) return false;
		}

		for(int i=0; i<degree.length; i++) {
			Rule newrule = new Rule(premise[i].getAntecedent(),degree[i]);
			Conclusion conc[] = conclusion[i].getConsequents();
			for(int j=0; j<conc.length; j++) newrule.add(conc[j]);
			copy.addRule(newrule);
		}
		return true;
	}

	/**
	 * Obtiene los títulos de las columnas de la tabla
	 */
	public String getColumnName(int column) {
		if(column == 0) return "Rule";
		if(column == 1) return "";
		if(column == 2) return "";
		if(column == 3) return "Premise";
		if(column == 4) return "";
		if(column == 5) return "Conclusion";
		return "";
	}

	/**
	 * Obtiene el editor de una celda de la tabla
	 */
	public TableCellEditor getEditor(int row, int column) {
		if(column == 3 && row != degree.length) return premise[row];
		if(column == 5 && row != degree.length) return conclusion[row];
		return null;
	}

	/**
	 * Estudia si la celda es editable
	 */
	public boolean isCellEditable(int row, int column) {
		return (column == 1 || column == 3 || column == 5);
	}

	/**
	 * Obtiene el componente que representa cada celda
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(column == 0 || column == 2 || column == 4)
		{ label.setText(value.toString()); return label; }
		if(isSelected) {
			text.setForeground(table.getSelectionForeground());
			text.setBackground(XConstants.textselectedbg);
		} else {
			text.setForeground(Color.black);
			text.setBackground(XConstants.textbackground);
		}
		text.setText(value.toString());
		return text;
	}

	/**
	 * Ejecuta una accion sobre el modelo
	 */
	public void modelAction(int row,int kind) {
		if(kind == -2) conclusion[row].conclusionAdd();
		else premise[row].action(kind);
	}

	/**
	 * Modifica la variable y MF del antecedente seleccionado
	 */
	public void changePremise(int row, Variable var, LinguisticLabel pmf) {
		premise[row].setVariable(var);
		premise[row].setMembershipFunction(pmf);
	}

	/**
	 * Modifica la variable y MF del consecuente seleccionado
	 */
	public void changeConclusion(int row, Variable var, LinguisticLabel pmf) {
		conclusion[row].setVariable(var);
		conclusion[row].setMembershipFunction(pmf);
	}

	/**
	 * Elimina una fila (regla) de la tabla
	 */
	public void removeRow(int row) {
		XfeditRulebaseFreeAntec auxpm[];
		auxpm = new XfeditRulebaseFreeAntec[degree.length-1];
		System.arraycopy(premise,0,auxpm,0,row);
		System.arraycopy(premise,row+1,auxpm,row,degree.length-row-1);
		premise = auxpm;

		XfeditRulebaseFreeConseq auxcn[];
		auxcn = new XfeditRulebaseFreeConseq[degree.length-1];
		System.arraycopy(conclusion,0,auxcn,0,row);
		System.arraycopy(conclusion,row+1,auxcn,row,degree.length-row-1);
		conclusion = auxcn;

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
		Rule[] rule = copy.getRules();
		degree = new double[rule.length];
		premise = new XfeditRulebaseFreeAntec[rule.length];
		conclusion = new XfeditRulebaseFreeConseq[rule.length];
		for(int i=0; i<rule.length; i++) {
			Rule ruleclone = (Rule) rule[i].clone(copy);
			Conclusion[] conclone = ruleclone.getConclusions();
			degree[i] = ruleclone.getDegree();
			premise[i] = new XfeditRulebaseFreeAntec(panel,ruleclone.getPremise());
			conclusion[i] = new XfeditRulebaseFreeConseq(panel,conclone);
		}
	}

	/**
	 * Añade una fila (regla) a la tabla
	 */
	private void addRow() {
		XfeditRulebaseFreeAntec auxpm[];
		auxpm = new XfeditRulebaseFreeAntec[degree.length+1];
		System.arraycopy(premise,0,auxpm,0,degree.length);
		auxpm[degree.length] = new XfeditRulebaseFreeAntec(panel,null);
		premise = auxpm;

		XfeditRulebaseFreeConseq auxcn[];
		auxcn = new XfeditRulebaseFreeConseq[degree.length+1];
		System.arraycopy(conclusion,0,auxcn,0,degree.length);
		auxcn[degree.length] = new XfeditRulebaseFreeConseq(panel,new Conclusion[0]);
		conclusion = auxcn;

		double auxdeg[] = new double[degree.length + 1];
		System.arraycopy(degree,0,auxdeg,0,degree.length);
		auxdeg[degree.length] = 1.0;
		degree = auxdeg;

		fireTableChanged(new TableModelEvent(this, degree.length, degree.length,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

}

