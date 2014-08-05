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
import java.awt.*;
import java.awt.event.*;

/**
 * Panel que desarrolla la edición en formato libre de una base de reglas
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditRulebaseFreePanel extends JPanel implements ActionListener,
ItemListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603033L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Modelo asociado a la tabla de representación de las reglas
	 */
	private XfeditRulebaseFreeModel model;
	
	/**
	 * Tabla que muestra la representación de la base de reglas
	 */
	private XfeditRulebaseFreeForm table;
	
	/**
	 * Menú desplegable que permite seleccionar la variable a asignar en
	 * la cláusula seleccionada
	 */
	private JComboBox varcombo;
	
	/**
	 * Menú desplegable que permite seleccionar la función de pertenencia
	 * a asignar en la cláusula seleccionada
	 */
	private JComboBox mfcombo;
	
	/**
	 * Base de reglas a editar
	 */
	private Rulebase copy;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditRulebaseFreePanel() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Actualiza el contenido del panel
	 */
	public void refresh(Rulebase copy) {
		removeAll();
		this.copy = copy;
		model = new XfeditRulebaseFreeModel(this,copy);
		table = new XfeditRulebaseFreeForm(model);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createHorizontalStrut(10));
		box.add( new JScrollPane(table) );
		box.add(Box.createHorizontalStrut(10));

		varcombo = new JComboBox();
		varcombo.setBackground(XConstants.textbackground);
		varcombo.setFont(XConstants.textfont);
		varcombo.setEditable(false);
		varcombo.setEnabled(false);
		varcombo.addItemListener(this);
		Dimension minsize = varcombo.getMinimumSize();
		Dimension maxsize = varcombo.getMaximumSize();
		Dimension prefsize = varcombo.getPreferredSize();
		int width = (prefsize.width > 100 ? prefsize.width : 100);
		varcombo.setPreferredSize(new Dimension(width,prefsize.height));
		varcombo.setMinimumSize(new Dimension(width,minsize.height));
		varcombo.setMaximumSize(new Dimension(width,maxsize.height));

		mfcombo = new JComboBox();
		mfcombo.setBackground(XConstants.textbackground);
		mfcombo.setFont(XConstants.textfont);
		mfcombo.setEditable(false);
		mfcombo.setEnabled(false);
		mfcombo.addItemListener(this);
		mfcombo.setPreferredSize(new Dimension(width,prefsize.height));
		mfcombo.setMinimumSize(new Dimension(width,minsize.height));
		mfcombo.setMaximumSize(new Dimension(width,maxsize.height));

		String btlabel[] = { "&", "|", "!", "%", "~", "+", "==", "!=", "<=", ">=",
				"<", ">", "~=", "+=", "->", "%=", ">..<" };
		String btcommand[] = { "AND", "OR", "NOT", "SLIGHTLY", "MoL", "VERY", "IS", 
				"ISNOT", "SM_EQ", "GR_EQ", "SMALLER", "GREATER", "APP_EQ", "VERY_EQ", 
				"THEN", "SL_EQ", "PRUNE" };
		String bttip[] = { "and", "or", "not", "slightly", "more or less", "strongly",
				"is equal to", "is not equal to", "is less or equal to",
				"is greater or equal to", "is less than", "is greater than",
				"is approximately equal to", "is strongly equal to", "then",
				"is slightly equal to", "prune" };

		XButton button[] = new XButton[btlabel.length];
		for(int i=0; i<button.length; i++) {
			button[i] = new XButton(btlabel[i], btcommand[i], this);
			button[i].setToolTipText(bttip[i]);
		}

		JPanel bpanel = new JPanel();
		bpanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridwidth = 1; c.gridheight = 1;
		c.gridx = 0; c.gridy = 0; bpanel.add(button[0],c);
		c.gridx = 0; c.gridy = 1; bpanel.add(button[1],c);
		c.gridx = 1; c.gridy = 0; bpanel.add(button[2],c);
		c.gridx = 1; c.gridy = 1; bpanel.add(button[3],c);
		c.gridx = 2; c.gridy = 0; bpanel.add(button[4],c);
		c.gridx = 2; c.gridy = 1; bpanel.add(button[5],c);
		c.gridx = 3; c.gridy = 0; bpanel.add(button[6],c);
		c.gridx = 3; c.gridy = 1; bpanel.add(button[7],c);
		c.gridx = 4; c.gridy = 0; bpanel.add(button[8],c);
		c.gridx = 4; c.gridy = 1; bpanel.add(button[9],c);
		c.gridx = 5; c.gridy = 0; bpanel.add(button[10],c);
		c.gridx = 5; c.gridy = 1; bpanel.add(button[11],c);
		c.gridx = 6; c.gridy = 0; bpanel.add(button[12],c);
		c.gridx = 6; c.gridy = 1; bpanel.add(button[13],c);
		c.gridx = 7; c.gridy = 0; bpanel.add(button[14],c);
		c.gridx = 7; c.gridy = 1; bpanel.add(button[15],c);
		c.gridheight = 2;
		c.gridx = 8; c.gridy = 0; bpanel.add(button[16],c);
		c.gridheight = 1;
		c.gridx = 9; c.gridy = 0; bpanel.add(new XLabel("Variable"),c);
		c.gridx = 9; c.gridy = 1; bpanel.add(new XLabel("M.F."),c);
		c.gridx = 10; c.gridy = 0; bpanel.add(varcombo,c);
		c.gridx = 10; c.gridy = 1; bpanel.add(mfcombo,c);

		Box cbox = new Box(BoxLayout.X_AXIS);
		cbox.add(Box.createHorizontalStrut(10));
		cbox.add(bpanel);
		cbox.add(Box.createHorizontalStrut(10));

		JPanel form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.add(Box.createVerticalStrut(5));
		form.add(cbox);
		form.add(Box.createVerticalStrut(5));

		maxsize = form.getMaximumSize();
		prefsize = form.getPreferredSize();
		form.setMaximumSize(new Dimension(maxsize.width,prefsize.height));

		add(Box.createVerticalGlue());
		add(Box.createVerticalStrut(10));
		add(box);
		add(Box.createVerticalStrut(10));
		add(Box.createVerticalGlue());
		add(form);
		add(Box.createVerticalStrut(10));
		add(Box.createVerticalGlue());
	}

	/**
	 * Obtiene el contenido del panel
	 */
	public boolean getRules(Rulebase rulebase) {
		return model.getRules(rulebase);
	}

	/**
	 * Deselecciona la fila de la tabla
	 */
	public void clearSelection() {
		table.clearSelection();
	}

	/**
	 * Asigna el contenido de los menus desplegables
	 */
	public void setComboBoxes(Variable selvar, LinguisticLabel selmf, boolean input) {
		if(selvar == null) {
			varcombo.removeAllItems();
			varcombo.setEnabled(false);
			mfcombo.removeAllItems();
			mfcombo.setEnabled(false);
			return;
		}
		varcombo.setEnabled(true);
		mfcombo.setEnabled(true);
		Variable var[] = (input ? copy.getInputs() : copy.getOutputs());
		varcombo.removeAllItems();
		for(int i=0; i<var.length; i++)
			if(var[i].getType().getAllMembershipFunctions().length >0)
				varcombo.addItem(var[i]);
		varcombo.setSelectedItem(selvar);
		mfcombo.removeAllItems();
		LinguisticLabel pmf[] = selvar.getType().getAllMembershipFunctions();
		for(int i=0; i<pmf.length; i++) mfcombo.addItem(pmf[i]);
		mfcombo.setSelectedItem(selmf);
	}

	/**
	 * Crea una nueva conclusion con la primera variable de salida
	 * y función de pertenencia disponible
	 */
	public Conclusion createConclusion() {
		Variable[] var = copy.getOutputs();
		if(var.length == 0) return null;
		int i=0;
		LinguisticLabel pmf[] = var[0].getType().getAllMembershipFunctions();
		while(i<var.length && pmf.length == 0)
		{ i++; pmf = var[i].getType().getAllMembershipFunctions(); }
		if(i == var.length) return null;
		return new Conclusion(var[i],pmf[0],copy);
	}

	/**
	 * Crea una nueva relacion simple o modifica una existente
	 */
	public Relation createSingleRelation(Relation rel, int kind) {
		if(rel == null) {
			Variable[] var=copy.getInputs();
			if(var.length == 0) return null;
			int i=0;
			LinguisticLabel pmf[] = var[0].getType().getAllMembershipFunctions();
			while(i<var.length && pmf.length == 0)
			{ i++; pmf = var[i].getType().getAllMembershipFunctions(); }
			if(i == var.length) return null;
			return Relation.create(kind,null,null,var[i],pmf[0],copy);
		} else {
			Variable var = rel.getVariable();
			LinguisticLabel pmf = rel.getMembershipFunction();
			return Relation.create(kind,null,null,var,pmf,copy);
		}
	}

	/**
	 * Crea una nueva relacion unaria
	 */
	public Relation createCompoundRelation(Relation rel, int kind) {
		return Relation.create(kind,rel,null,null,null,copy);
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("AND")) table.tableAction(Relation.AND);
		else if(command.equals("OR")) table.tableAction(Relation.OR);
		else if(command.equals("NOT")) table.tableAction(Relation.NOT);
		else if(command.equals("MoL")) table.tableAction(Relation.MoL);
		else if(command.equals("SLIGHTLY")) table.tableAction(Relation.SLIGHTLY);
		else if(command.equals("VERY")) table.tableAction(Relation.VERY);
		else if(command.equals("IS")) table.tableAction(Relation.IS);
		else if(command.equals("SM_EQ")) table.tableAction(Relation.SM_EQ);
		else if(command.equals("SMALLER")) table.tableAction(Relation.SMALLER);
		else if(command.equals("APP_EQ")) table.tableAction(Relation.APP_EQ);
		else if(command.equals("ISNOT")) table.tableAction(Relation.ISNOT);
		else if(command.equals("GR_EQ")) table.tableAction(Relation.GR_EQ);
		else if(command.equals("GREATER")) table.tableAction(Relation.GREATER);
		else if(command.equals("VERY_EQ")) table.tableAction(Relation.VERY_EQ);
		else if(command.equals("SL_EQ")) table.tableAction(Relation.SL_EQ);
		else if(command.equals("PRUNE")) table.tableAction(-1);
		else if(command.equals("THEN")) table.tableAction(-2);
		else if(command.equals("VAR")) changeVarCombo();
		else if(command.equals("MF")) changeMFCombo();
	}

	/**
	 * Interfaz ItemListener
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() != ItemEvent.SELECTED) return;
		if(e.getItem() instanceof Variable) changeVarCombo();
		else if(e.getItem() instanceof LinguisticLabel) changeMFCombo();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Acción de repuesta a un cambio de seleccion de variable
	 */
	private void changeVarCombo() {
		Variable var = (Variable) varcombo.getSelectedItem();
		varcombo.setSelectedItem(var);
		mfcombo.removeAllItems();
		LinguisticLabel pmf[] = var.getType().getAllMembershipFunctions();
		for(int i=0; i<pmf.length; i++) mfcombo.addItem(pmf[i]);
		mfcombo.setSelectedItem(pmf[0]);
		table.tableAction(var,pmf[0]);
	}

	/**
	 * Acción de repuesta a un cambio de seleccion de MF
	 */
	private void changeMFCombo() {
		Variable var = (Variable) varcombo.getSelectedItem();
		LinguisticLabel pmf = (LinguisticLabel) mfcombo.getSelectedItem();
		table.tableAction(var,pmf);
	}
}

