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
 * Ventana de edición de una función de pertenencia
 * 
 * @author Francisco José Moreno Velo
 */
public class XfeditMFEditor extends JDialog implements ActionListener,
ListCellRenderer, ItemListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603025L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de campos para introducir los valores de los parámetros
	 */
	private XTextForm[] paramform;
	
	/**
	 * Campo para introducir el nombre de la función
	 */
	private XTextForm labelform;
	
	/**
	 * Panel que contiene los campos de los parámetros
	 */
	private Box parambox;
	
	/**
	 * Menú desplegable para seleccionar la función
	 */
	private XComboBox functionform;
	
	/**
	 * Panel para representar gráficamente la función
	 */
	private XfeditTypeGraphPanel mfgraph;
	
	/**
	 * Editor de tipos que realiza la llamada a XfeditMFEditor
	 */
	private XfeditTypeEditor editor;
	
	/**
	 * Objeto original que describe la función de pertenencia a editar
	 */
	private LinguisticLabel mforig;
	
	/**
	 * Copia de trabajo sobre la que se realizan los cambios
	 * de edición de la función de pertenencia
	 */
	private LinguisticLabel mfcopy;
	
	/**
	 * Objeto Type al que pertenece la función
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
	public XfeditMFEditor(XfeditTypeEditor editor,LinguisticLabel function) {
		super(editor.getXfedit(),"Xfedit",true);
		this.editor = editor;
		this.type = editor.getWorkingCopy();
		this.mforig = function;
		if(function == null) {
			this.mfcopy = null; 
		} else if(function instanceof ParamMemFunc) {
			ParamMemFunc pff = (ParamMemFunc) function;
			ParamMemFunc pmf = (ParamMemFunc) pff.clone(type.getUniverse());
			pmf.setLabel(pff.getLabel());
			pmf.u = pff.u;
			this.mfcopy = pmf;
		} else if(function instanceof FamiliarMemFunc) {
			FamiliarMemFunc fmf = (FamiliarMemFunc) function;
			String label = fmf.getLabel();
			Family fam = fmf.getFamily();
			int index = fmf.getIndex();
			this.mfcopy = new FamiliarMemFunc(label,fam,index);
		}
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
		if(command.equals("MFSet")) { setMF(); return; }
		else if(command.equals("MFRefresh")) { if(getData()) setData(); return; }
		else if(command.equals("MFCancel")) { setVisible(false); return; }
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
		String label;
		if(listindex == -1 && mfcopy!=null) {
			if(mfcopy instanceof ParamMemFunc) {
				ParamMemFunc pmf = (ParamMemFunc) mfcopy;
				label = pmf.getPackageName()+"."+pmf.getFunctionName();
			} else {
				FamiliarMemFunc fmf = (FamiliarMemFunc) mfcopy;
				label = ""+fmf.getFamily();
			}
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

		Object item = functionform.getSelectedItem();
		if(item instanceof Family) {
			Family fam = (Family) item;
			if(mfcopy == null || mfcopy instanceof ParamMemFunc) {
				mfcopy = new FamiliarMemFunc(labelform.getText(), fam, 0);
				setData();
			} else {
				FamiliarMemFunc fmfc = (FamiliarMemFunc) mfcopy;
				if(fmfc.getFamily() != fam) {
					mfcopy = new FamiliarMemFunc(labelform.getText(), fam, 0);
					setData();
				}
			}
		} else {
			String def = (String) item;
			int dot = def.indexOf('.');
			String pkgname = def.substring(0, dot);
			String funcname = def.substring(dot+1);

			if(mfcopy == null || mfcopy instanceof FamiliarMemFunc) {
				XflPackageBank bank = editor.getXfedit().getXflPackageBank();
				ParamMemFunc pmf = (ParamMemFunc) bank.instantiate(pkgname, funcname, XflPackage.MFUNC);
				pmf.setLabel(labelform.getText());
				pmf.u = type.getUniverse();
				mfcopy = pmf;
				setData();
			} else {
				ParamMemFunc pmfc = (ParamMemFunc) mfcopy;
				if(!pkgname.equals(pmfc.getPackageName()) ||
						!funcname.equals(pmfc.getFunctionName()) ) {
					XflPackageBank bank = editor.getXfedit().getXflPackageBank();
					ParamMemFunc pmf = (ParamMemFunc) bank.instantiate(pkgname, funcname, XflPackage.MFUNC);
					pmf.setLabel(labelform.getText());
					pmf.u = type.getUniverse();
					mfcopy = pmf;
					setData();
				}
			}

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
		String command[] = { "MFSet", "MFRefresh", "MFCancel" };
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

		this.mfgraph = new XfeditTypeGraphPanel(type,300);
		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(leftbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(this.mfgraph);
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
		pack();
		setLocationRelativeTo(editor.getXfedit());
	}

	/**
	 * Actualiza la lista de funciones disponibles
	 */
	private void setList() {
		Vector available = new Vector();
		Family fam[] = type.getFamilies();
		for(int i=0; i<fam.length; i++) available.addElement(fam[i]);
		XflPackageBank bank = this.editor.getXfedit().getXflPackageBank();
		Vector mfavailable = bank.getFunctionNames(XflPackage.MFUNC);
		available.addAll(mfavailable);		
		functionform.setList(available);
	}

	/**
	 * Actualiza los datos de la función de pertenencia
	 */
	private void setData() {
		if(mfcopy == null) return;
		labelform.setText(mfcopy.getLabel());
		if(mfcopy instanceof ParamMemFunc) {
			ParamMemFunc pmfc = (ParamMemFunc) mfcopy;
			Parameter[] single = pmfc.getSingleParameters();
			Parameter[] list = pmfc.getParamList();
			String listname = pmfc.getParamListName();

			if(pmfc.hasParamList()) index = single.length + list.length;
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
			if(pmfc.hasParamList()) paramform[index].setLabel("New");
		} else {
			index = 1;
			ensureSize();
			paramform[0].setText(""+((FamiliarMemFunc) mfcopy).getIndex());
			paramform[0].setLabel("Index");
			paramform[0].setEditable(true);
			paramform[0].setFieldEnabled(true);
			for(int i=index; i<paramform.length; i++) {
				paramform[i].setText("");
				paramform[i].setLabel("");
				paramform[i].setEditable(false);
				paramform[i].setFieldEnabled(false);
			}
		}
		mfgraph.setExcluded(mforig);
		mfgraph.setSelection(mfcopy);
		mfgraph.repaint();
	}

	/**
	 * Obtiene los datos de la ventana
	 */
	private boolean getData() {
		if(mfcopy == null) return false;
		boolean numeric = true;
		mfcopy.setLabel(labelform.getText());
		if(mfcopy instanceof ParamMemFunc) {
			ParamMemFunc pmfc = (ParamMemFunc) mfcopy;
			Parameter[] single = pmfc.getSingleParameters();
			Parameter[] list = pmfc.getParamList();

			for(int i=0; i<single.length; i++) {
				try { single[i].value = Double.parseDouble(paramform[i].getText());}
				catch (NumberFormatException ex) {
					paramform[i].setText(""+single[i].value);
					numeric = false;
				}
			}
			if(pmfc.hasParamList()) for(int i=0; i<list.length; i++) {
				String text = paramform[i+single.length].getText();
				try { list[i].value = Double.parseDouble(text);}
				catch (NumberFormatException ex) {
					paramform[i+single.length].setText(""+list[i].value);
					numeric = false;
				}
			}
		} else {
			FamiliarMemFunc fmfc = (FamiliarMemFunc) mfcopy;
			try { fmfc.setIndex( Integer.parseInt(paramform[0].getText()) ); }
			catch (NumberFormatException ex) {
				paramform[0].setText(""+fmfc.getIndex());
				numeric = false;
			}
		}

		if(!numeric) {
			XDialog.showMessage(editor,"Not a numeric value");
			return false;
		}
		if(!mfcopy.test()) {
			XDialog.showMessage(editor,"Invalid parameters");
			return false;
		}
		return true;
	}

	/**
	 * Redimensiona el panel de parámetros para asegurar que se
	 * puedan editar todos los parámetros de la función de pertenencia
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
		if(mfcopy instanceof FamiliarMemFunc) return;
		ParamMemFunc pmfc = (ParamMemFunc) mfcopy;
		if(!pmfc.hasParamList()) return;
		String listname = pmfc.getParamListName();
		Parameter[] list = pmfc.getParamList();

		Parameter np = new Parameter(listname);
		Parameter p[] = new Parameter[list.length+1];
		System.arraycopy(list,0,p,0,list.length);
		p[list.length] = np;
		pmfc.setParamList(p);
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
		if(mfcopy instanceof FamiliarMemFunc) return;
		ParamMemFunc pmfc = (ParamMemFunc) mfcopy;
		if(!pmfc.hasParamList()) return;
		Parameter[] single = pmfc.getSingleParameters();
		Parameter[] list = pmfc.getParamList();
		if(i<single.length) return;

		for(int j=i+1; j<index; j++) {
			paramform[j-1].setText(paramform[j].getText());
			list[j-single.length-1].value = list[j-single.length].value;
		}
		Parameter ap[] = new Parameter[list.length-1];
		System.arraycopy(list,0,ap,0,list.length-1);
		pmfc.setParamList(ap);
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
	 * Actualiza la función de pertenencia y cierra la ventana
	 */
	private void setMF() {
		if(!getData()) return;
		if(!XConstants.isIdentifier( mfcopy.getLabel() )) {
			labelform.setText("");
			mfcopy.setLabel("");
			XDialog.showMessage(editor,"Invalid linguistic label");
			return;
		}
		LinguisticLabel[] pmf = type.getMembershipFunctions();
		for(int i=0; i<pmf.length; i++)
			if(pmf[i] != mforig && pmf[i].equals(mfcopy.getLabel())) {
				String msg = "Label "+mfcopy.getLabel()+" already exists";
				labelform.setText("");
				mfcopy.setLabel("");
				XDialog.showMessage(editor,msg);
				return;
			}
		if(mforig == null) editor.addMF(mfcopy);
		else editor.exchangeMF(mforig,mfcopy);
		setVisible(false);
	}
}

