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
 * Ventana de selección de los parámetros de un operador
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditOpsetParamDialog extends JDialog implements ActionListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603027L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Marco del que depende la ventana
	 */
	private JFrame frame;
	
	/**
	 * Campo para mostrar el nombre del operador
	 */
	private XTextForm[] textform;
	
	/**
	 * Lista de campos para introducir los valores de los parámetros
	 */
	private XTextForm[] paramform;
	
	/**
	 * Lista de campos para introducir los valores de los parámetros
	 */
	private XTextForm[] listform;
	
	/**
	 * Panel que contiene los campos de los parámetros
	 */
	private Box listbox;
	
	/**
	 * Título de la ventana
	 */
	private String title;
	
	/**
	 * Operador a editar
	 */
	private FuzzyOperator fzop;
	
	/**
	 * Indicador de edición aceptada o cancelada
	 */
	private boolean selection;
	
	/**
	 * Indice del campo que permite crear un nuevo parámetro
	 */
	private int index;
	
	/**
	 * Mínimo número de campos de parámetros
	 */
	private int MINSIZE = 5;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditOpsetParamDialog(JFrame frame, FuzzyOperator fzop, String title) {
		super(frame,"Xfedit",true);
		this.frame = frame;
		this.fzop = fzop;
		this.title = title;
		this.index = 0;
		this.listbox = null;
		buildDialog();
		setData();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Presenta el diálogo
	 */
	public boolean showDialog() {
		selection = false;
		setVisible(true);
		return selection;
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("ParamSet")) {
			if(!getData()) return;
			selection = true;
			setVisible(false);
			return;
		}
		else if(command.equals("ParamCancel")) {
			selection = false;
			setVisible(false);
			return;
		}
		int i = Integer.parseInt(command);
		if(i > index ) return;
		if(i == index ) addParameter();
		else removeParameter(i);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Genera el contenido del diálogo
	 */
	private void buildDialog() {
		String label[] = { "Set", "Cancel" };
		String command[] = { "ParamSet", "ParamCancel" };
		XCommandForm form = new XCommandForm(label,command,this);
		form.setCommandWidth(130);

		textform = new XTextForm[3];
		for(int i=0; i<textform.length; i++) {
			textform[i] = new XTextForm("");
			textform[i].setEditable(false);
			textform[i].setLabelWidth(100);
			textform[i].setFieldWidth(200);
			textform[i].setFieldEnabled(false);
			textform[i].setAlignment(JLabel.CENTER);
		}
		textform[0].setLabel("Operator"); textform[0].setFieldEnabled(true);
		textform[1].setLabel("Package "); textform[1].setFieldEnabled(true);
		textform[2].setLabel("Function"); textform[2].setFieldEnabled(true);
		Box box1 = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<3; i++) box1.add(textform[i]);
		Box namebox = new Box(BoxLayout.X_AXIS);
		namebox.add(Box.createHorizontalStrut(5));
		namebox.add(box1);
		namebox.add(Box.createHorizontalStrut(5));

		paramform = new XTextForm[fzop.getSingleParameters().length];
		for(int i=0; i<paramform.length; i++) {
			paramform[i] = new XTextForm("");
			paramform[i].setEditable(false);
			paramform[i].setLabelWidth(100);
			paramform[i].setFieldWidth(200);
			paramform[i].setFieldEnabled(false);
			paramform[i].setAlignment(JLabel.CENTER);
		}

		Box box2 = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<paramform.length; i++) box2.add(paramform[i]);
		Box parambox = new Box(BoxLayout.X_AXIS);
		parambox.add(Box.createHorizontalStrut(5));
		parambox.add(box2);
		parambox.add(Box.createHorizontalStrut(5));

		String paramlistname = fzop.getParamListName();
		Box lastbox = null;
		if(paramlistname != null && paramlistname.length() >0) {
			listform = new XTextForm[MINSIZE];
			for(int i=0; i<listform.length; i++) {
				listform[i] = new XTextForm("",this);
				listform[i].setEditable(false);
				listform[i].setLabelWidth(100);
				listform[i].setFieldWidth(200);
				listform[i].setFieldEnabled(false);
				listform[i].setAlignment(JLabel.CENTER);
				listform[i].setActionCommand(""+i);
			}
			listbox = new Box(BoxLayout.Y_AXIS);
			for(int i=0; i<listform.length; i++) listbox.add(listform[i]);
			Box pbox = new Box(BoxLayout.X_AXIS);
			pbox.add(Box.createHorizontalStrut(5));
			pbox.add(listbox);
			pbox.add(Box.createHorizontalStrut(5));

			lastbox = new Box(BoxLayout.Y_AXIS);
			lastbox.add(new XLabel(paramlistname));
			lastbox.add(Box.createVerticalStrut(5));
			lastbox.add(new JScrollPane(pbox));
			lastbox.add(Box.createVerticalStrut(5));
		}

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Parameter Selection"));
		content.add(Box.createVerticalStrut(5));
		content.add(namebox);
		content.add(Box.createVerticalStrut(5));
		if(paramform.length >0) {
			content.add(new XLabel("Parameters"));
			content.add(Box.createVerticalStrut(5));
			content.add(parambox);
			content.add(Box.createVerticalStrut(5));
		}
		if(lastbox != null) content.add(lastbox);
		content.add(Box.createGlue());
		content.add(form);
		content.add(Box.createGlue());

		setFont(XConstants.font);
		setLocationRelativeTo(frame);
		pack();
	}

	/**
	 * Asigna el contenido de los campos
	 */
	private void setData() {
		textform[0].setText(title);
		textform[1].setText(fzop.getPackageName());
		textform[2].setText(fzop.getFunctionName());

		Parameter param[] = fzop.getSingleParameters();
		for(int i=0; i<param.length; i++) {
			paramform[i].setText(""+param[i].value);
			paramform[i].setEditable(true);
			paramform[i].setFieldEnabled(true);
			paramform[i].setLabel(param[i].getName());
		}

		if(listbox != null) {
			Parameter paramlist[] =  fzop.getParamList();
			index = paramlist.length;
			ensureSize();
			for(int i=0; i<index; i++) {
				listform[i].setText(""+paramlist[i].value);
				listform[i].setLabel("Remove");
				listform[i].setEditable(true);
				listform[i].setFieldEnabled(true);
			}
			for(int i=index; i<listform.length; i++) {
				listform[i].setText("");
				listform[i].setLabel("");
				listform[i].setEditable(false);
				listform[i].setFieldEnabled(false);
			}
			listform[index].setLabel("New");
		}
	}

	/**
	 * Lee el contenido de los campos
	 */
	private boolean getData() {
		Parameter param[] = fzop.getSingleParameters();

		double value[] = new double[paramform.length+index];

		for(int i=0; i<param.length; i++)
			try { value[i] = Double.parseDouble(paramform[i].getText()); }
		catch (NumberFormatException ex) {
			paramform[i].setText(" ");
			XDialog.showMessage(frame,"Not a numeric value");
			return false;
		}

		for(int i=0; i<index; i++)
			try { value[param.length+i] = Double.parseDouble(listform[i].getText()); }
		catch (NumberFormatException ex) {
			listform[i].setText(" ");
			XDialog.showMessage(frame,"Not a numeric value");
			return false;
		}

		try { fzop.set(value); } 
		catch (Exception ex) {
			XDialog.showMessage(frame,"Invalid parameters");
			return false;
		}

		if(!fzop.test()) {
			XDialog.showMessage(frame,"Invalid parameters");
			return false;
		}
		return true;
	}

	/**
	 * Redimensiona el panel de parámetros para asegurar que se
	 * puedan editar todos los parámetros del operador
	 */
	private void ensureSize() {
		while(index < listform.length-1 && listform.length > MINSIZE) {
			listbox.remove(listform[listform.length-1]);
			XTextForm at[] = new XTextForm[listform.length-1];
			System.arraycopy(listform,0,at,0,listform.length-1);
			listform = at;
		}
		for(int i=index-listform.length; i>=0; i--) {
			XTextForm nt = new XTextForm(" ",this);
			nt.setLabelWidth(100);
			nt.setFieldWidth(200);
			nt.setAlignment(JButton.CENTER);
			nt.setActionCommand(""+listform.length);
			listbox.add(nt);
			XTextForm at[] = new XTextForm[listform.length+1];
			System.arraycopy(listform,0,at,0,listform.length);
			at[listform.length] = nt;
			listform = at;
		}
	}

	/**
	 * Añade un campo para un nuevo parámetro
	 */
	private void addParameter() {
		String paramlistname = fzop.getParamListName();
		Parameter list[] = fzop.getParamList();
		Parameter np = new Parameter(paramlistname);
		Parameter p[] = new Parameter[list.length+1];
		System.arraycopy(list,0,p,0,list.length);
		p[list.length] = np;
		fzop.setParamList(p);
		index++;
		ensureSize();
		listform[index-1].setLabel("Remove");
		listform[index-1].setEditable(true);
		listform[index-1].setFieldEnabled(true);
		listform[index].setLabel("New");
		listform[index].setEditable(false);
		listform[index].setFieldEnabled(false);
	}

	/**
	 * Elimina un campo para introducir un parámetro
	 */
	private void removeParameter(int i) {
		Parameter list[] = fzop.getParamList();

		for(int j=i+1; j<=index; j++) {
			listform[j-1].setText(listform[j].getText());
			list[j-1].value = list[j].value;
		}
		Parameter ap[] = new Parameter[list.length-1];
		System.arraycopy(list,0,ap,0,list.length-1);
		fzop.setParamList(ap);
		index--;
		listform[index].setLabel("New");
		listform[index].setEditable(false);
		listform[index].setFieldEnabled(false);
		listform[index+1].setLabel("");
		ensureSize();
	}

}

