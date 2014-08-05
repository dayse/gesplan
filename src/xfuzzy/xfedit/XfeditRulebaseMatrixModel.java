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
//MODELO PARA LA REPRESENTACION MATRICIAL DE LA BASE DE REGLAS	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfedit;

import xfuzzy.lang.*;
import xfuzzy.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Modelo de la tabla para representar la bese de reglas en formato
 * matricial
 *  
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseMatrixModel extends AbstractTableModel 
implements TableCellRenderer {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603034L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Componente para mostrar las celdas de los nombres de las MF de las
	 * variables de entrada
	 */
	private XLabel label;
	
	/**
	 * Componente para mostrar la selección de una celda
	 */
	private JLabel text;
	
	/**
	 * Descripción de las variables de entrada de la base de reglas
	 */
	private LinguisticLabel[] input0mf,input1mf;
	
	/**
	 * Contenido de la base de reglas, es decir, matriz de MF de salida
	 */
	private LinguisticLabel[][] pmf;
	
	/**
	 * Editor del contenido de las celdas (menú desplegable con las MF de salida)
	 */
	private DefaultCellEditor editor;
	
	/**
	 * Marca para verificar si la base de reglas puede mostrarse en formato matricial
	 */
	private boolean valid;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseMatrixModel(Rulebase copy) {
		super();
		initialize(copy);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Verifica que la base de reglas cumpla las restricciones de la forma matricial
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Obtiene el número de columnas de la matriz
	 */
	public int getColumnCount() {
		return (input1mf.length+1);
	}

	/**
	 * Obtiene el número de filas de la matriz
	 */
	public int getRowCount() {
		return input0mf.length;
	}

	/**
	 *  Obtiene un elemento de la matriz
	 */
	public Object getValueAt(int row, int column) {
		if(column == 0) return input0mf[row].toString();
		if(pmf[row][column-1] == null) return "";
		return pmf[row][column-1];
	}

	/**
	 * Asigna un elemento de la matriz
	 */
	public void setValueAt(Object value, int row, int column) {
		if(column == 0) return;
		try { pmf[row][column-1] = (LinguisticLabel) value; }
		catch(Exception ex) {  pmf[row][column-1] = null; }
		fireTableCellUpdated(row, column);
	}

	/**
	 * Obtiene el título de una columna de la matriz
	 */
	public String getColumnName(int column) {
		if(column == 0) return "";
		return input1mf[column-1].toString();
	}

	/**
	 * Obtiene el editor de las celdas de la matriz
	 */
	public TableCellEditor getEditor() {
		return editor;
	}

	/**
	 * Obtiene la clase de un elemento de la matriz
	 */
	public Class getColumnClass(int column) {
		return Object.class;
	}

	/**
	 * Verifica que una celda sea editable
	 */
	public boolean isCellEditable(int row, int column) {
		if(column == 0) return false;
		return true;
	}

	/**
	 * Obtiene el componente que representa cada celda
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(column == 0) { label.setText(value.toString()); return label; }
		text.setText(value.toString());
		return text;
	}

	/**
	 * Obtiene las reglas representadas en la matriz
	 */
	public boolean getRules(Rulebase copy) {
		copy.removeAllRules();
		Variable inputs[] = copy.getInputs();
		Variable outputs[] = copy.getOutputs();

		for(int i=0; i<input0mf.length; i++)
			for(int j=0; j<input1mf.length; j++) {
				if(pmf[i][j] == null) continue;
				Relation rel0,rel1,premise;
				rel0 = Relation.create(Relation.IS,null,null,inputs[0],input0mf[i],null);
				rel1 = Relation.create(Relation.IS,null,null,inputs[1],input1mf[j],null);
				premise = Relation.create(Relation.AND,rel0,rel1,null,null,copy);
				Rule newrl = new Rule(premise);
				newrl.add(new Conclusion(outputs[0],pmf[i][j],copy));
				copy.addRule(newrl);
			}
		return true;
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Inicializa el contenido de la matriz	
	 */
	private void initialize(Rulebase copy) {
		label = new XLabel("");
		label.setLabelFont(XConstants.textfont);
		text = new JLabel("");
		text.setFont(XConstants.textfont);
		text.setOpaque(true);
		text.setForeground(Color.black);
		text.setBackground(XConstants.textbackground);

		Variable inputs[] = copy.getInputs();
		Variable outputs[] = copy.getOutputs();
		if(inputs.length != 2 || outputs.length != 1) { valid = false; return; }
		input0mf = inputs[0].getType().getAllMembershipFunctions();
		input1mf = inputs[1].getType().getAllMembershipFunctions();
		LinguisticLabel outputmf[] = outputs[0].getType().getAllMembershipFunctions();

		JComboBox combo = new JComboBox();
		combo.setBackground(XConstants.textbackground);
		combo.setFont(XConstants.textfont);
		combo.setEditable(false);
		combo.addItem("");
		for(int k=0; k<outputmf.length; k++) combo.addItem(outputmf[k]);
		editor = new DefaultCellEditor(combo);

		pmf = new LinguisticLabel[input0mf.length][input1mf.length];

		Rule[] cprule = copy.getRules();
		for(int k=0; k<cprule.length; k++) {
			Conclusion conc[] = cprule[k].getConclusions();
			if(conc.length != 1) { valid = false; return; }
			Relation rl = cprule[k].getPremise();
			if(rl.getKind() != Relation.AND) { valid = false; return; }
			Relation left = rl.getLeftRelation();
			Relation right = rl.getRightRelation();
			if(left.getKind() != Relation.IS) { valid = false; return; }
			if(right.getKind() != Relation.IS) { valid = false; return; }
			Variable lvar = left.getVariable();
			Variable rvar = right.getVariable();
			if(lvar == rvar) { valid = false; return; }
			LinguisticLabel mf0, mf1;
			if(lvar == inputs[0]) {
				mf0 = left.getMembershipFunction();
				mf1 = right.getMembershipFunction();
			}
			else {
				mf1 = left.getMembershipFunction();
				mf0 = right.getMembershipFunction();
			}
			int index0 = 0,index1 = 0;
			for(int i=0; i<input0mf.length; i++) if(mf0 == input0mf[i]) index0 = i;
			for(int i=0; i<input1mf.length; i++) if(mf1 == input1mf[i]) index1 = i;
			if(pmf[index0][index1] != null) { valid = false; return; }
			pmf[index0][index1] = conc[0].getMembershipFunction();
		}
		valid = true;
	}
}

