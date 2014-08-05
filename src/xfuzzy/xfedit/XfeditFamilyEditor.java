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
import java.util.Vector;

/**
 * Ventana de edición de una familia de funciones de pertenencia
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditFamilyEditor extends JDialog implements ActionListener,
ListCellRenderer, ItemListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603022L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de campos para introducir los valores de los parámetros
	 */
	private XTextForm[] paramform;
	
	/**
	 * Campo para introducir el nombre de la familia
	 */
	private XTextForm labelform;
	
	/**
	 * Panel que contiene los campos de los parámetros
	 */
	private Box parambox;
	
	/**
	 * Menú desplegable para seleccionar la familia
	 */
	private XComboBox functionform;
	
	/**
	 * Panel para representar gráficamente las MF de la familia
	 */
	private XfeditFamilyGraphPanel graph;
	
	/**
	 * Editor de tipos que realiza la llamada a XfeditFamilyEditor
	 */
	private XfeditTypeEditor editor;
	
	/**
	 * Objeto original que describe la familia a editar
	 */
	private Family famorig;
	
	/**
	 * Copia de trabajo sobre la que se realizan los cambios
	 * de edición de la familia
	 */
	private Family famcopy;
	
	/**
	 * Objeto Type al que pertenece la familia
	 */
	private Type type;
	
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
	public XfeditFamilyEditor(XfeditTypeEditor editor, Family family) {
		super(editor.getXfedit(),"Xfedit",true);
		this.editor = editor;
		this.type = editor.getWorkingCopy();
		this.famorig = family;
		this.famcopy = (family == null? null : (Family) family.clone(type) );
		build();
		setList();
		setData();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Set")) { set(); return; }
		else if(command.equals("Refresh")) { if(getData()) setData(); return; }
		else if(command.equals("Cancel")) { setVisible(false); return; }
		int i = Integer.parseInt(command);
		if(i > index ) return;
		if(i == index ) addParameter();
		else removeParameter(i);
	}

	/**
	 * Interfaz ListCellRenderer
	 */
	public Component getListCellRendererComponent(
			JList list, Object value, int listindex,
			boolean isSelected, boolean cellHasFocus) {
		String label = "";
		if(listindex == -1 && famcopy!=null) {
			label = famcopy.getPackageName()+"."+famcopy.getFunctionName();
		}
		else if(listindex == -1) label = " ";
		else label = value.toString();

		JLabel renderer = new JLabel(label);
		renderer.setForeground(Color.black);
		renderer.setBackground(XConstants.textbackground);
		renderer.setOpaque(true);
		renderer.setFont(XConstants.textfont);
		return renderer;
	}

	/**
	 * Interfaz ItemListener
	 */
	public void itemStateChanged(ItemEvent e) {
		if(functionform.getSelectedItem() == null) return;
		if(e.getStateChange() != ItemEvent.SELECTED) return;

		String def = (String) functionform.getSelectedItem();
		int dot = def.indexOf('.');
		String pkgname = def.substring(0, dot);
		String funcname = def.substring(dot+1);
		
		if(famcopy == null ||
				!pkgname.equals(famcopy.getPackageName()) ||
				!funcname.equals(famcopy.getFunctionName()) ) {
			XflPackageBank bank = editor.getXfedit().getXflPackageBank();
			famcopy = (Family) bank.instantiate(pkgname, funcname, XflPackage.FAMILY);
			famcopy.set(labelform.getText(),type);
			setData();
		}
		functionform.setSelectedItem(null);
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Construye la ventana
	 */
	private void build() {
		String label[] = { "Set", "Refresh", "Cancel" };
		String command[] = { "Set", "Refresh", "Cancel" };
		XCommandForm form = new XCommandForm(label, command, this);
		form.setCommandWidth(130);

		labelform = new XTextForm("Label");
		labelform.setLabelWidth(100);
		labelform.setFieldWidth(200);
		labelform.setAlignment(JLabel.CENTER);

		functionform = new XComboBox("Function");
		functionform.setWidth(100,200);
		functionform.setRenderer(this);
		functionform.addItemListener(this);

		Box box1 = new Box(BoxLayout.Y_AXIS);
		box1.add(labelform);
		box1.add(functionform);
		Box labelbox = new Box(BoxLayout.X_AXIS);
		labelbox.add(Box.createHorizontalStrut(5));
		labelbox.add(box1);
		labelbox.add(Box.createHorizontalStrut(5));

		paramform = new XTextForm[MINSIZE];
		for(int i=0; i<paramform.length; i++) {
			paramform[i] = new XTextForm("",this);
			paramform[i].setEditable(false);
			paramform[i].setLabelWidth(100);
			paramform[i].setFieldWidth(200);
			paramform[i].setFieldEnabled(false);
			paramform[i].setAlignment(JLabel.CENTER);
			paramform[i].setActionCommand(""+i);
		}
		parambox = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<paramform.length; i++) parambox.add(paramform[i]);
		Box pbox = new Box(BoxLayout.X_AXIS);
		pbox.add(Box.createHorizontalStrut(5));
		pbox.add(parambox);
		pbox.add(Box.createHorizontalStrut(5));

		Box leftbox = new Box(BoxLayout.Y_AXIS);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(labelbox);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(new XLabel("Parameters"));
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(new JScrollPane(pbox));
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(Box.createGlue());

		this.graph = new XfeditFamilyGraphPanel(famcopy,300);
		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(leftbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(this.graph);
		hbox.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Parameter Selection"));
		content.add(Box.createVerticalStrut(5));
		content.add(hbox);
		content.add(Box.createVerticalStrut(5));
		content.add(Box.createGlue());
		content.add(form);
		content.add(Box.createGlue());

		setFont(XConstants.font);
		setLocationRelativeTo(editor.getXfedit());
		pack();
	}

	/**
	 * Actualiza la lista de funciones disponibles
	 */
	private void setList() {
		XflPackageBank bank = this.editor.getXfedit().getXflPackageBank();
		Vector available = bank.getFunctionNames(XflPackage.FAMILY);
		functionform.setList(available);
	}

	/**
	 * Actualiza los datos de la familia
	 */
	private void setData() {
		if(famcopy == null) return;
		labelform.setText(famcopy.getLabel());
		Parameter[] single = famcopy.getSingleParameters();
		Parameter[] list = famcopy.getParamList();
		String listname = famcopy.getParamListName();

		if(famcopy.hasParamList()) index = single.length + list.length;
		else index = single.length;
		ensureSize();

		for(int i=0; i<single.length; i++) {
			paramform[i].setText(""+single[i].value);
			paramform[i].setLabel(single[i].getName());
			paramform[i].setEditable(true);
			paramform[i].setFieldEnabled(true);
		}
		for(int i=single.length; i<index; i++) {
			String paramname = listname+"["+(i-single.length)+"]";
			paramform[i].setText(""+list[i-single.length].value);
			paramform[i].setLabel(paramname);
			paramform[i].setEditable(true);
			paramform[i].setFieldEnabled(true);
		}
		for(int i=index; i<paramform.length; i++) {
			paramform[i].setText("");
			paramform[i].setLabel("");
			paramform[i].setEditable(false);
			paramform[i].setFieldEnabled(false);
		}
		if(famcopy.hasParamList()) paramform[index].setLabel("New");

		graph.setFamily(famcopy);
		graph.repaint();
	}

	/**
	 * Obtiene los datos de la ventana
	 */
	private boolean getData() {
		if(famcopy == null) return false;
		boolean numeric = true;
		famcopy.setLabel(labelform.getText());

		Parameter[] single = famcopy.getSingleParameters();
		Parameter[] list = famcopy.getParamList();

		for(int i=0; i<single.length; i++) {
			try { single[i].value = Double.parseDouble(paramform[i].getText());}
			catch (NumberFormatException ex) {
				paramform[i].setText(""+single[i].value);
				numeric = false;
			}
		}
		if(famcopy.hasParamList()) for(int i=0; i<list.length; i++) {
			String text = paramform[i+single.length].getText();
			try { list[i].value = Double.parseDouble(text);}
			catch (NumberFormatException ex) {
				paramform[i+single.length].setText(""+list[i].value);
				numeric = false;
			}
		}

		if(!numeric) {
			XDialog.showMessage(editor,"Not a numeric value");
			return false;
		}
		if(!famcopy.test()) {
			XDialog.showMessage(editor,"Invalid parameters");
			return false;
		}

		boolean wrong = false;
		int members = famcopy.members();
		FamiliarMemFunc[] fmf = type.getFamiliarMembershipFunctions();
		for(int i=0; i<fmf.length; i++) {
			if(fmf[i].getFamily() == famorig && fmf[i].getIndex()>=members) wrong = true;
		}
		if(wrong) {
			XDialog.showMessage(editor,"Insuficient members for defined MFs");
			return false;
		}

		return true;
	}

	/**
	 * Redimensiona el panel de parámetros para asegurar que se
	 * puedan editar todos los parámetros de la familia
	 */
	private void ensureSize() {
		while(index < paramform.length-1 && paramform.length > MINSIZE) {
			parambox.remove(paramform[paramform.length-1]);
			XTextForm at[] = new XTextForm[paramform.length-1];
			System.arraycopy(paramform,0,at,0,paramform.length-1);
			paramform = at;
		}
		for(int i=index-paramform.length; i>=0; i--) {
			XTextForm nt = new XTextForm(" ",this);
			nt.setLabelWidth(100);
			nt.setFieldWidth(200);
			nt.setAlignment(JButton.CENTER);
			nt.setActionCommand(""+paramform.length);
			parambox.add(nt);
			XTextForm at[] = new XTextForm[paramform.length+1];
			System.arraycopy(paramform,0,at,0,paramform.length);
			at[paramform.length] = nt;
			paramform = at;
		}
	}

	/**
	 * Añade un campo para un nuevo parámetro
	 */
	private void addParameter() {
		if(!famcopy.hasParamList()) return;
		String listname = famcopy.getParamListName();
		Parameter[] list = famcopy.getParamList();

		Parameter np = new Parameter(listname);
		Parameter p[] = new Parameter[list.length+1];
		System.arraycopy(list,0,p,0,list.length);
		p[list.length] = np;
		famcopy.setParamList(p);
		index++;
		ensureSize();
		String paramname = listname+"["+(index-1)+"]";
		paramform[index-1].setLabel(paramname);
		paramform[index-1].setEditable(true);
		paramform[index-1].setFieldEnabled(true);
		paramform[index].setLabel("New");
		paramform[index].setEditable(false);
		paramform[index].setFieldEnabled(false);
	}

	/**
	 * Elimina un campo para introducir un parámetro
	 */
	private void removeParameter(int i) {
		if(!famcopy.hasParamList()) return;
		Parameter[] single = famcopy.getSingleParameters();
		Parameter[] list = famcopy.getParamList();
		if(i<single.length) return;

		for(int j=i+1; j<index; j++) {
			paramform[j-1].setText(paramform[j].getText());
			list[j-single.length-1].value = list[j-single.length].value;
		}
		Parameter ap[] = new Parameter[list.length-1];
		System.arraycopy(list,0,ap,0,list.length-1);
		famcopy.setParamList(ap);
		index--;
		paramform[index].setLabel("New");
		paramform[index].setText("");
		paramform[index].setEditable(false);
		paramform[index].setFieldEnabled(false);
		paramform[index+1].setLabel("");
		paramform[index+1].setText("");
		ensureSize();
	}

	/**
	 * Actualiza la familia y cierra la ventana
	 */
	private void set() {
		if(!getData()) return;
		if(!XConstants.isIdentifier( famcopy.getLabel() )) {
			labelform.setText("");
			famcopy.setLabel("");
			XDialog.showMessage(editor,"Invalid label");
			return;
		}
		Family[] pmf = type.getFamilies();
		for(int i=0; i<pmf.length; i++)
			if(pmf[i] != famorig && pmf[i].equals(famcopy.getLabel())) {
				String msg = "Family "+famcopy.getLabel()+" already exists";
				labelform.setText("");
				famcopy.setLabel("");
				XDialog.showMessage(editor,msg);
				return;
			}
		if(famorig == null) editor.addFamily(famcopy);
		else editor.exchangeFamily(famorig,famcopy);
		setVisible(false);
	}
}

