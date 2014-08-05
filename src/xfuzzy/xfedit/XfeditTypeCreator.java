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
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana de creación de tipos predefinidos
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditTypeCreator extends JDialog 
implements ActionListener, ChangeListener, WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603040L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia a la aplicación Xfedit que abre la ventana de creación de tipos
	 */
	private Xfedit xfeditor;
	
	/**
	 * Sistema difuso a editar
	 */
	private Specification spec;
	
	/**
	 * Lista de componentes de texto para introducir los datos
	 */
	private XTextForm dataform[];
	
	/**
	 * Menú desplegable para seleccionar el ancestro de un tipo heredado
	 */
	private XComboBox parentform;
	
	/**
	 * Lista de bootones de selección entre tipos predefinidos
	 */
	private JToggleButton button[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditTypeCreator(Xfedit xfeditor){
		super(xfeditor,"Xfedit",false);
		this.xfeditor = xfeditor;
		this.spec = xfeditor.getSpecification();
		build();
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Abre una ventana de creación de un tipo de variable
	 */
	static void showTypeCreator(Xfedit xfeditor) {
		XfeditTypeCreator editor = new XfeditTypeCreator(xfeditor);
		editor.setVisible(true);
		editor.repaint();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Create")) {
			Type tp = create();
			if(tp == null) return;
			setVisible(false);
			XfeditTypeEditor.showTypeEditor(xfeditor,tp);
		}
		else if(command.equals("Cancel")) setVisible(false);
	}

	/**
	 * Interfaz ChangeListener
	 */
	public void stateChanged(ChangeEvent e) {
		AbstractButton button = (AbstractButton) e.getSource();
		if(button.isSelected()) {
			button.setBorder(BorderFactory.createLoweredBevelBorder());
			setDataEnabled();
		}
		else button.setBorder(BorderFactory.createRaisedBevelBorder());
	}

	/**
	 * Interfaz WindowListener. Acción al abrir la ventana
	 */
	public void windowOpened(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al cerrar la ventana
	 */
	public void windowClosing(WindowEvent e) {
		setVisible(false);
	}
	
	/**
	 * Interfaz WindowListener. Acción al terminar de cerrar la ventana
	 */
	public void windowClosed(WindowEvent e) {	
	}
	
	/**
	 * Interfaz WindowListener. Acción al minimizar la ventana
	 */
	public void windowIconified(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al maximizar la ventana
	 */
	public void windowDeiconified(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al activar la ventana
	 */
	public void windowActivated(WindowEvent e) {
	}
	
	/**
	 * Interfaz WindowListener. Acción al desactivar la ventana
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Creación de la ventana
	 */
	private void build() {
		String lb[] = {"Create","Cancel"};
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(120);
		form.block();

		String datalabel[] = { "Name","Minimum","Maximum","Cardinality","No. MFs"};
		dataform = new XTextForm[5];
		for(int i=0; i<dataform.length; i++) {
			dataform[i] = new XTextForm(datalabel[i]);
			dataform[i].setLabelWidth(100);
			dataform[i].setFieldWidth(150);
			dataform[i].setAlignment(JLabel.CENTER);
		}

		parentform = new XComboBox("Parent");
		parentform.setWidth(100,150);
		parentform.setList(spec.getTypes());

		Box leftbox = new Box(BoxLayout.Y_AXIS);
		leftbox.add(dataform[0]);
		leftbox.add(parentform);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(new XLabel("Universe of discourse"));
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(dataform[1]);
		leftbox.add(dataform[2]);
		leftbox.add(dataform[3]);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(new XLabel("Membership functions"));
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(dataform[4]);

		button = new JToggleButton[12];
		JPanel rightbox = new JPanel();
		rightbox.setLayout(new GridLayout(4,3));
		ButtonGroup rbg = new ButtonGroup();
		ImageIcon icon[] = { XfeditIcons.typeF1, XfeditIcons.typeF2,
				XfeditIcons.type1, XfeditIcons.type2, XfeditIcons.type3,
				XfeditIcons.type4, XfeditIcons.type5, XfeditIcons.type6, 
				XfeditIcons.type7, XfeditIcons.type8, XfeditIcons.type9};
		for(int i=0; i<button.length; i++) {
			if(i<button.length-1) {
				button[i] = new JToggleButton(icon[i]);
			}
			else {
				button[i] = new JToggleButton("extends");
				button[i].setFont(XConstants.textfont);
				button[i].setForeground(Color.black);
			}
			button[i].setBorder(BorderFactory.createRaisedBevelBorder());
			button[i].setFocusPainted(false);
			button[i].setContentAreaFilled(false);
			button[i].addChangeListener(this);
			rbg.add(button[i]);
			rightbox.add(button[i]);
		}

		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(leftbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(rightbox);
		hbox.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Type Creation"));
		content.add(Box.createVerticalStrut(5));
		content.add(hbox);
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(xfeditor);
		pack();
	}

	/**
	 *  Devuelve el indice del tipo predefinido seleccionado
	 */
	private int getSelection() {
		for(int i=0; i<button.length; i++) if(button[i].isSelected()) return i;
		return -1;
	}

	/**
	 * Activa los campos de la ventana en función de la selección
	 */
	private void setDataEnabled() {
		int sel = getSelection();
		if(sel == 10) /* BLANK */ {
			for(int i=1; i<4; i++) dataform[i].setFieldEnabled(true);
			for(int i=1; i<4; i++) dataform[i].setEditable(true);
			parentform.setEnabled(false);
			parentform.setSelectedItem(null);
			dataform[4].setFieldEnabled(false);
			dataform[4].setEditable(false);
			dataform[4].setText("");
		}
		else if(sel == 11) /* EXTENDS */ {
			for(int i=1; i<5; i++) dataform[i].setFieldEnabled(false);
			for(int i=1; i<5; i++) dataform[i].setEditable(false);
			for(int i=1; i<5; i++) dataform[i].setText("");
			parentform.setEnabled(true);
		}
		else /* ANY OTHER */ {
			for(int i=1; i<5; i++) dataform[i].setFieldEnabled(true);
			for(int i=1; i<5; i++) dataform[i].setEditable(true);
			parentform.setEnabled(false);
			parentform.setSelectedItem(null);
		}
	}

	/**
	 * Lee el identificador del tipo y verifica que es correcto
	 */
	private String getTypeName() {
		String name = dataform[0].getText();
		if(!XConstants.isIdentifier(name)) {
			dataform[0].setText("");
			XDialog.showMessage(dataform[0],"Invalid Name");
			return null;
		}
		if(spec.searchType(name) != null) {
			dataform[0].setText("");
			XDialog.showMessage(dataform[0],"Invalid Name: Type already exists");
			return null;
		}
		return name;
	}

	/**
	 *  Lee los campos del universo y verifica que son correctos
	 */
	private Universe getUniverse() {
		Universe u = null;
		boolean numeric = true;
		double min=0, max=0;
		int card=256;
		try { min = Double.parseDouble(dataform[1].getText()); }
		catch(NumberFormatException ex) { dataform[1].setText(""); numeric = false; }
		try { max = Double.parseDouble(dataform[2].getText()); }
		catch(NumberFormatException ex) { dataform[2].setText(""); numeric = false; }
		try {
			String cardinality = dataform[3].getText();
			if(cardinality.length() >0 ) card = Integer.parseInt(cardinality);
		}
		catch(NumberFormatException ex) { dataform[3].setText(""); numeric = false; }
		if(!numeric) {
			XDialog.showMessage(dataform[3],"Not a numeric value");
			return null;
		}
		try { u = new Universe(min,max,card); }
		catch(XflException ex) {
			XDialog.showMessage(dataform[3],"Invalid universe data");
			return null;
		}
		return u;
	}

	/**
	 * Lee el número de etiquetas y verifica que es correcto
	 */
	private int getNumberOfMFs() {
		int sel = getSelection();
		int limit = 0;
		if(sel == 0) limit = 2;
		else if(sel == 1) limit = 4;
		else if(sel <= 7) limit = 2;
		else limit = 1;
		int mfs = -1;
		try { mfs = Integer.parseInt(dataform[4].getText()); }
		catch(NumberFormatException ex) { 
			dataform[4].setText("");
			XDialog.showMessage(dataform[3],"Not a numeric value");
			return -1;
		}
		if(mfs < limit) {
			dataform[4].setText("");
			XDialog.showMessage(dataform[3],"Not a valid value");
			return -1;
		}
		return mfs;
	}

	//----------------------------------------------------------------------------//
	// Métodos que generan el tipo                                                //
	//----------------------------------------------------------------------------//

	/**
	 * Crea un nuevo tipo de la familia seleccionada
	 */
	private Type create() {
		String name = getTypeName();
		if(name == null) return null;
		Type parent = (Type) parentform.getSelectedItem();
		Universe u = null;
		int mfs = 0;
		if(parent == null) { u = getUniverse(); if(u == null) return null; }
		int sel = getSelection();
		if(sel == -1) {
			XDialog.showMessage(dataform[3],"A predefined selection is needed");
			return null;
		}
		else if(dataform[4].isEditable())
		{ mfs = getNumberOfMFs(); if(mfs<0) return null; }

		Type tp;
		if(parent == null) tp = new Type(name,u);
		else tp = new Type(name,parent);
		createMemFuncs(tp, sel, mfs);
		spec.addType(tp);
		spec.setModified(true);
		return tp;
	}

	/**
	 * Genera un conjunto de funciones de pertenencia predefinidas
	 */
	private void createMemFuncs(Type type, int sel, int mfs) {
		if(sel<0 || sel>9) return;
		String prefix = "mf";
		boolean exists = true;
		while(exists) {
			exists = false;
			for(int i=0;i<mfs;i++) if(type.search(prefix+i) != null) exists = true;
			if(exists) prefix += "_";
		}

		switch(sel) {
			case 0: createTypeF1(type,mfs,prefix); break;
			case 1: createTypeF2(type,mfs,prefix); break;
			case 2: createType0(type,mfs,prefix); break;
			case 3: createType1(type,mfs,prefix); break;
			case 4: createType2(type,mfs,prefix); break;
			case 5: createType3(type,mfs,prefix); break;
			case 6: createType4(type,mfs,prefix); break;
			case 7: createType5(type,mfs,prefix); break;
			case 8: createType6(type,mfs,prefix); break;
			case 9: createType7(type,mfs,prefix); break;
		}
	}

	/**
	 * Crea una familia de triangulos equiespaciados
	 */
	private void createTypeF1(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs-1);
		double param[] = new double[mfs-2];
		for(int i=0; i<mfs-2; i++) param[i] = min+(i+1)*r;

		String famname = "fam";
		int index = 0;
		while(type.searchFamily(famname) != null) famname = "fam"+index;

		Family fam = new pkg.xfl.family.triangular();
		fam.set(famname,type);
		try { 
			fam.set(param);
			type.addFamily(fam);
		} catch(Exception ex) {}

		for(int i=0; i<mfs; i++) {
			FamiliarMemFunc fmf = new FamiliarMemFunc(prefix+i,fam,i);
			try { type.add(fmf); } catch(XflException e) {}
		}
	}

	/**
	 * Crea una familia de splines equiespaciados
	 */
	private void createTypeF2(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs-2);
		double param[] = new double[mfs-3];
		for(int i=0; i<mfs-3; i++) param[i] = min+(i+1)*r;

		String famname = "fam";
		int index = 0;
		while(type.searchFamily(famname) != null) famname = "fam"+index;

		Family fam = new pkg.xfl.family.spline();
		fam.set(famname,type);
		try { 
			fam.set(param);
			type.addFamily(fam);
		} catch(Exception ex) {}

		for(int i=0; i<mfs; i++) {
			FamiliarMemFunc fmf = new FamiliarMemFunc(prefix+i,fam,i);
			try { type.add(fmf); } catch(XflException e) {}
		}
	}

	/**
	 * Crea un conjunto de triangulos equiespaciados
	 */
	private void createType0(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs-1);
		double param[] = new double[3];
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		param[0] = min - r;
		param[1] = min;
		param[2] = min + r;
		pmf[0] = new pkg.xfl.mfunc.triangle();
		pmf[0].set(prefix+"0",u);
		try { pmf[0].set(param); }
		catch(XflException ex) { System.out.println(""+ex); }

		for(int i=1; i<mfs-1; i++) {
			param[0] = min + (i-1)*r;
			param[1] = min + i*r;
			param[2] = min + (i+1)*r;
			pmf[i] = new pkg.xfl.mfunc.triangle();
			pmf[i].set(prefix+i,u);
			try { pmf[i].set(param); } catch(XflException ex) {}
		}

		param[0] = max - r;
		param[1] = max;
		param[2] = max + r;
		pmf[mfs-1] = new pkg.xfl.mfunc.triangle();
		pmf[mfs-1].set(prefix+(mfs-1),u);
		try { pmf[mfs-1].set(param); } catch(XflException ex) {}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de triangulos y trapecios
	 */
	private void createType1(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs+1);
		double param4[] = new double[4];
		double param3[] = new double[3];
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		param4[0] = min-r;
		param4[1] = min;
		param4[2] = min+r;
		param4[3] = min+2*r;
		pmf[0] = new pkg.xfl.mfunc.trapezoid();
		pmf[0].set(prefix+"0",u);
		try { pmf[0].set(param4); } catch(XflException ex) {}

		for(int i=1; i<mfs-1; i++) {
			param3[0] = min + i*r;
			param3[1] = min + (i+1)*r;
			param3[2] = min + (i+2)*r;
			pmf[i] = new pkg.xfl.mfunc.triangle();
			pmf[i].set(prefix+i,u);
			try { pmf[i].set(param3); } catch(XflException ex) {}
		}

		param4[0] = max-2*r;
		param4[1] = max-r;
		param4[2] = max;
		param4[3] = max+r;
		pmf[mfs-1] = new pkg.xfl.mfunc.trapezoid();
		pmf[mfs-1].set(prefix+(mfs-1), u);
		try { pmf[mfs-1].set(param4); } catch(XflException ex) {}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de campanas equiespaciadas
	 */
	private void createType2(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs-1);
		double param[] = new double[2];
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		param[0] = min;
		param[1] = 0.6*r;
		pmf[0] = new pkg.xfl.mfunc.bell();
		pmf[0].set(prefix+"0", u);
		try { pmf[0].set(param); } catch(XflException ex) {}

		for(int i=1; i<mfs-1; i++) {
			param[0] = min + i*r;
			pmf[i] = new pkg.xfl.mfunc.bell();
			pmf[i].set(prefix+i, u);
			try { pmf[i].set(param); } catch(XflException ex) {}
		}

		param[0] = max;
		pmf[mfs-1] = new pkg.xfl.mfunc.bell();
		pmf[mfs-1].set(prefix+(mfs-1), u);
		try { pmf[mfs-1].set(param); } catch(XflException ex) {}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de campanas y sigmoides
	 */
	private void createType3(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs+1);
		double param[] = new double[2];
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		param[0] = min + 1.5*r;
		param[1] = -0.17*r;
		pmf[0] = new pkg.xfl.mfunc.sigma();
		pmf[0].set(prefix+"0", u);
		try { pmf[0].set(param); } catch(XflException ex) {}

		for(int i=1; i<mfs-1; i++) {
			param[0] = min + (i+1)*r;
			param[1] = 0.6*r;
			pmf[i] = new pkg.xfl.mfunc.bell();
			pmf[i].set(prefix+i, u);
			try { pmf[i].set(param); } catch(XflException ex) {}
		}

		param[0] = max - 1.5*r;
		param[1] = 0.17*r;
		pmf[mfs-1] = new pkg.xfl.mfunc.sigma();
		pmf[mfs-1].set(prefix+(mfs-1), u);
		try { pmf[mfs-1].set(param); } catch(XflException ex) {}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de trapecios equiespaciados
	 */
	private void createType4(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(3*mfs-1);
		double param[] = new double[4];
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		param[0] = min - r;
		param[1] = min;
		param[2] = min + 2*r;
		param[3] = min + 3*r;
		pmf[0] = new pkg.xfl.mfunc.trapezoid();
		pmf[0].set(prefix+"0", u);
		try { pmf[0].set(param); } catch(XflException ex) {}

		for(int i=1; i<mfs-1; i++) {
			param[0] = min + (3*i-1)*r;
			param[1] = min + 3*i*r;
			param[2] = min + (3*i+2)*r;
			param[3] = min + (3*i+3)*r;
			pmf[i] = new pkg.xfl.mfunc.trapezoid();
			pmf[i].set(prefix+i, u);
			try { pmf[i].set(param); } catch(XflException ex) {}
		}

		param[0] = max - 3*r;
		param[1] = max - 2*r;
		param[2] = max;
		param[3] = max + r;
		pmf[mfs-1] = new pkg.xfl.mfunc.trapezoid();
		pmf[mfs-1].set(prefix+(mfs-1), u);
		try { pmf[mfs-1].set(param); } catch(XflException ex) {}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de singularidades equiespaciadas
	 */
	private void createType5(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double r = (max-min)/(mfs-1);
		double param;
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		param = min;
		pmf[0] = new pkg.xfl.mfunc.singleton();
		pmf[0].set(prefix+"0", u);
		pmf[0].set(param);

		for(int i=1; i<mfs-1; i++) {
			param = min + i*r;
			pmf[i] = new pkg.xfl.mfunc.singleton();
			pmf[i].set(prefix+i, u);
			pmf[i].set(param);
		}

		param = max;
		pmf[mfs-1] = new pkg.xfl.mfunc.singleton();
		pmf[mfs-1].set(prefix+(mfs-1), u);
		pmf[mfs-1].set(param);

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de campanas centradas
	 */
	private void createType6(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double param[] = new double[2];
		param[0] = (min + max)/2;
		param[1] = (max - min)/8;
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		for(int i=0; i<mfs; i++) {
			pmf[i] = new pkg.xfl.mfunc.bell();
			pmf[i].set(prefix+i, u);
			try { pmf[i].set(param); } catch(XflException ex) {}
		}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

	/**
	 * Crea un conjunto de singularidades centradas
	 */
	private void createType7(Type type, int mfs, String prefix) {
		Universe u = type.getUniverse();
		double min = u.min();
		double max = u.max();
		double middle = (min + max)/2 ;
		ParamMemFunc pmf[] = new ParamMemFunc[mfs];

		for(int i=0; i<mfs; i++) {
			pmf[i] = new pkg.xfl.mfunc.singleton();
			pmf[i].set(prefix+i, u);
			pmf[i].set(middle);
		}

		try { for(int i=0; i<mfs; i++) type.add(pmf[i]); }
		catch(XflException e) {}
	}

}

