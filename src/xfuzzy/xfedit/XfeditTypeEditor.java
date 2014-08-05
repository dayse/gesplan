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
 * Ventana de edición de un tipo de variable lingüística
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfeditTypeEditor extends JDialog implements ActionListener, 
MouseListener, KeyListener, ListSelectionListener, ListCellRenderer,
WindowListener {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603041L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Referencia a la aplicación Xfedit que realiza la llamada al objeto
	 */
	private Xfedit xfeditor;
	
	/**
	 * Sistema difuso a editar
	 */
	private Specification spec;
	
	/**
	 * Referencia al tipo original a editar
	 */
	private Type original;
	
	/**
	 * Copia de trabajo del tipo original a editar
	 */
	private Type copy;
	
	/**
	 * Correpondencia entre el tipo original y la copia de trabajo 
	 */
	private XfeditTypeMapping mapping[];
	
	/**
	 * Campos de texto para introducir datos
	 */
	private XTextForm dataform[];
	
	/**
	 * Lista de funciones de pertenencia del tipo
	 */
	private XList mflist;
	
	/**
	 * Lista de familias de funciones del tipo
	 */
	private XList famlist;
	
	/**
	 * Menú desplegable asociado a las funciones de pertenencia
	 */
	private JPopupMenu mfpopup;
	
	/**
	 * Menú desplegable asociado a las familias
	 */
	private JPopupMenu fampopup;
	
	/**
	 * Representación gráfica del tipo en edición
	 */
	private XfeditTypeGraphPanel graph;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfeditTypeEditor(Xfedit xfeditor, Type type){
		super(xfeditor,"Xfedit",false);
		this.xfeditor = xfeditor;
		this.spec = xfeditor.getSpecification();
		this.original = type;
		this.original.setEditing(true);
		getCopy();
		build();
		refresh();
	}

	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ESTÁTICOS                          //
	//----------------------------------------------------------------------------//

	/**
	 * Abre una ventana de edición de tipos
	 */
	static void showTypeEditor(Xfedit xfeditor, Type type) {
		XfeditTypeEditor editor = new XfeditTypeEditor(xfeditor,type);
		editor.setVisible(true);
		editor.repaint();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene la referencia de la ventana principal
	 */
	public Xfedit getXfedit() {
		return this.xfeditor;
	}

	/**
	 * Obtiene la copia de trabajo del tipo editado
	 */
	public Type getWorkingCopy() {
		return this.copy;
	}

	/**
	 * Añade una MF en la copia de trabajo del tipo
	 */
	public void addMF(LinguisticLabel newmf) {
		LinguisticLabel mf[] = copy.getMembershipFunctions();
		LinguisticLabel amf[] = new LinguisticLabel[mf.length+1];
		System.arraycopy(mf,0,amf,0,mf.length);
		amf[mf.length] = newmf;
		copy.setMembershipFunctions(amf);

		XfeditTypeMapping amp[] = new XfeditTypeMapping[mapping.length+1];
		System.arraycopy(mapping,0,amp,0,mapping.length);
		amp[mapping.length] = new XfeditTypeMapping(null,newmf);
		this.mapping = amp;

		setMFList();
		refresh();
	}

	/**
	 * Sustituye una MF en la copia de trabajo del tipo
	 */
	public void exchangeMF(LinguisticLabel oldmf, LinguisticLabel newmf) {
		LinguisticLabel mf[] = copy.getMembershipFunctions();
		for(int i=0; i<mf.length; i++) if(mf[i] == oldmf) mf[i] = newmf;
		for(int i=0; i<mapping.length; i++) 
			if(mapping[i].copy == oldmf) mapping[i].copy = newmf;
		copy.setMembershipFunctions(mf);
		setMFList();
		refresh();
	}

	/**
	 * Añade una familia en la copia de trabajo del tipo
	 */
	public void addFamily(Family newfam) {
		Family fam[] = copy.getFamilies();
		Family af[] = new Family[fam.length+1];
		System.arraycopy(fam,0,af,0,fam.length);
		af[fam.length] = newfam;
		copy.setFamilies(af);
		setFamilyList();
	}

	/**
	 * Sustituye una familia en la copia de trabajo del tipo
	 */
	public void exchangeFamily(Family oldfam, Family newfam) {
		Family fam[] = copy.getFamilies();
		for(int i=0; i<fam.length; i++) if(fam[i] == oldfam) fam[i] = newfam;
		copy.setFamilies(fam);
		FamiliarMemFunc fmf[] = copy.getFamiliarMembershipFunctions();
		for(int i=0; i<fmf.length; i++) {
			if(fmf[i].getFamily()==oldfam) fmf[i].setFamily(newfam);
		}
		setFamilyList();
	}

	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Ok")) { if(apply()) cancel(); }
		else if(command.equals("Apply")) apply();
		else if(command.equals("Reload")) { getCopy(); refresh(); }
		else if(command.equals("Cancel")) cancel();
		else if(command.equals("Name")) textAction(0);
		else if(command.equals("Minimum")) textAction(1);
		else if(command.equals("Maximum")) textAction(2);
		else if(command.equals("Cardinality")) textAction(3);
		else if(command.equals("RemoveFamily")) removeFamily();
		else if(command.equals("NewFamily")) newFamily();
		else if(command.equals("EditFamily")) editFamily();
		else if(command.equals("RemoveMF")) removeMF();
		else if(command.equals("NewMF")) newMF();
		else if(command.equals("EditMF")) editMF();
	}

	/**
	 * Interfaz KeyListener	- Acción de soltar una tecla
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getComponent() == famlist.getList()) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE) { e.consume(); removeFamily(); }
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { e.consume(); removeFamily(); }
			if(e.getKeyCode() == KeyEvent.VK_INSERT) { e.consume(); newFamily(); }
			if(e.getKeyCode() == KeyEvent.VK_ENTER) { e.consume(); editFamily(); }
			if(e.getKeyCode() == 226) { e.consume(); editFamily(); }
		} else {
			if(e.getKeyCode() == KeyEvent.VK_DELETE) { e.consume(); removeMF(); }
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { e.consume(); removeMF(); }
			if(e.getKeyCode() == KeyEvent.VK_INSERT) { e.consume(); newMF(); }
			if(e.getKeyCode() == KeyEvent.VK_ENTER) { e.consume(); editMF(); }
			if(e.getKeyCode() == 226) { e.consume(); editMF(); }
		}
	}
	
	/**
	 * Interfaz KeyListener	- Acción de presionar una tecla
	 */
	public void keyPressed(KeyEvent e) {
	}
	
	/**
	 * Interfaz KeyListener	- Acción de pulsar una tecla
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Interfaz MouseListener - Acción de pulsar un botón del ratón
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getComponent() == famlist.getList()) {
			if(e.getClickCount() == 2) { editFamily(); }
		} else {
			if(e.getClickCount() == 2) { editMF(); }
		}
	}

	/**
	 * Interfaz MouseListener - Acción de presionar un botón del ratón
	 */
	public void mousePressed(MouseEvent e) {
		if(!e.isPopupTrigger()) return;
		if(e.getComponent() == famlist.getList()) 
			fampopup.show((Component) e.getSource(), e.getX(), e.getY());
		else mfpopup.show((Component) e.getSource(), e.getX(), e.getY());
	}

	/**
	 * Interfaz MouseListener - Acción de soltar un botón del ratón
	 */
	public void mouseReleased(MouseEvent e) { 
		if(!e.isPopupTrigger()) return;
		if(e.getComponent() == famlist.getList()) 
			fampopup.show((Component) e.getSource(), e.getX(), e.getY());
		else mfpopup.show((Component) e.getSource(), e.getX(), e.getY());
	}

	/**
	 * Interfaz MouseListener - Acción de entrar con el ratón en el componente
	 */
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	 * Interfaz MouseListener - Acción de salir con el ratón del componente
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Interfaz ListSelectionListener
	 */
	public void valueChanged(ListSelectionEvent e) { 
		selectMF();
	}

	/**
	 * Interfaz ListCellRenderer
	 */
	public Component getListCellRendererComponent(
			JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		Color blocked = dataform[0].getBackground();

		int local;
		if(famlist.getList() == list) {
			local = copy.getFamilies().length;
		} else {
			local = copy.getMembershipFunctions().length;
		}

		JLabel renderer = new JLabel(value.toString());
		renderer.setForeground(Color.black);
		if(isSelected) renderer.setBackground(XConstants.textselectedbg);
		else if(index >= local) renderer.setBackground(blocked);
		else renderer.setBackground(XConstants.textbackground);
		renderer.setOpaque(true);
		renderer.setFont(XConstants.textfont);
		return renderer;
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
		cancel();
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
	 * Crea de la ventana
	 */
	private void build() {
		String lb[] = {"Ok","Apply","Reload","Cancel"};
		XCommandForm form = new XCommandForm(lb,lb,this);
		form.setCommandWidth(120);
		form.block();

		mflist = new XList("Membership Functions");
		mflist.addListSelectionListener(this);
		mflist.setCellRenderer(this);
		mflist.addMouseListener(this);
		mflist.addKeyListener(this);

		famlist = new XList("M.F. Families");
		famlist.getList().setVisibleRowCount(3);
		famlist.setCellRenderer(this);
		famlist.addMouseListener(this);
		famlist.addKeyListener(this);

		String datalabel[] = { "Name","Minimum","Maximum","Cardinality","Parent"};
		dataform = new XTextForm[(copy.hasParent()? 5:4)];
		for(int i=0; i<dataform.length; i++) {
			dataform[i] = new XTextForm(datalabel[i]);
			dataform[i].setLabelWidth(100);
			dataform[i].setFieldWidth(150);
			dataform[i].setAlignment(JLabel.CENTER);
			dataform[i].addFieldActionListener(this);
			dataform[i].setFieldActionCommand(datalabel[i]);
		}

		JPanel leftbox = new JPanel();
		leftbox.setLayout(new BoxLayout(leftbox,BoxLayout.Y_AXIS));
		leftbox.add(dataform[0]);
		if(dataform.length == 5) leftbox.add(dataform[4]);
		if(dataform.length == 5) dataform[4].setEditable(false);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(new XLabel("Universe of discourse"));
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(dataform[1]);
		leftbox.add(dataform[2]);
		leftbox.add(dataform[3]);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(famlist);
		leftbox.add(Box.createVerticalStrut(5));
		leftbox.add(mflist);
		Dimension pref = leftbox.getPreferredSize();
		Dimension maxd = leftbox.getMaximumSize();
		leftbox.setMaximumSize(new Dimension(pref.width, maxd.height));

		graph = new XfeditTypeGraphPanel(this.copy,450);
		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(leftbox);
		hbox.add(Box.createHorizontalStrut(5));
		hbox.add(graph);
		hbox.add(Box.createHorizontalStrut(5));

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Type Edition"));
		content.add(Box.createVerticalStrut(5));
		content.add(hbox);
		content.add(form);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setFont(XConstants.font);
		setLocationRelativeTo(xfeditor);
		pack();
		createPopups();
	}

	/**
	 * Crea los menús desplegables
	 */
	private void createPopups() {
		String famlabel[]= { "New MF family","Edit MF family", "Remove MF family" };
		String famcommand[] = { "NewFamily", "EditFamily", "RemoveFamily" };
		fampopup = new JPopupMenu();
		JMenuItem famitem[] = new JMenuItem[famcommand.length];
		for(int i=0; i<famcommand.length; i++) {
			famitem[i] = new JMenuItem(famlabel[i]);
			famitem[i].setActionCommand(famcommand[i]);
			famitem[i].addActionListener(this);
			famitem[i].setFont(XConstants.font);
			fampopup.add(famitem[i]);
		}

		String mflabel[]= { "New membership function","Edit membership function",
		"Remove membership function" };
		String mfcommand[] = { "NewMF", "EditMF", "RemoveMF" };
		mfpopup = new JPopupMenu();
		JMenuItem mfitem[] = new JMenuItem[mfcommand.length];
		for(int i=0; i<mfcommand.length; i++) {
			mfitem[i] = new JMenuItem(mflabel[i]);
			mfitem[i].setActionCommand(mfcommand[i]);
			mfitem[i].addActionListener(this);
			mfitem[i].setFont(XConstants.font);
			mfpopup.add(mfitem[i]);
		}
	}

	/**
	 * Actualiza los campos de la ventana
	 */
	private void refresh() {
		dataform[0].setText(copy.getName());
		dataform[1].setText(""+copy.getUniverse().min());
		dataform[2].setText(""+copy.getUniverse().max());
		dataform[3].setText(""+copy.getUniverse().card());
		if(copy.hasParent()) {
			dataform[1].setFieldEnabled(false);
			dataform[1].setEditable(false);
			dataform[2].setFieldEnabled(false);
			dataform[2].setEditable(false);
			dataform[3].setFieldEnabled(false);
			dataform[3].setEditable(false);
			dataform[4].setText(copy.getParent().getName());
			dataform[4].setFieldEnabled(false);
			dataform[4].setEditable(false);
		}
		setFamilyList();
		setMFList();
		graph.repaint();
	}

	/**
	 * Actualiza la lista de familias de funciones de pertenencia
	 */
	private void setFamilyList() {
		famlist.setListData(copy.getAllFamilies());
	}

	/**
	 * Actualiza la lista de etiquetas lingüísticas
	 */
	private void setMFList() {
		mflist.setListData(copy.getAllMembershipFunctions());
	}

	/**
	 * Lee y verifica el identificador del tipo
	 */
	private boolean getTypeName() {
		String name = dataform[0].getText();
		if(!XConstants.isIdentifier(name)) {
			dataform[0].setText("");
			XDialog.showMessage(dataform[0],"Invalid Name");
			return false;
		}
		Type search = spec.searchType(name);
		if(search != null && search != original) {
			dataform[0].setText("");
			XDialog.showMessage(dataform[0],"Invalid Name: Type already exists");
			return false;
		}
		copy.setName(name);
		return true;
	}

	/**
	 * Lee y verifica los valores del universo de discurso
	 */
	private boolean getUniverse() {
		if(copy.hasParent()) return true;
		boolean numeric = true;
		double min=0, max=0;
		int card=0;

		try { min = Double.parseDouble(dataform[1].getText()); }
		catch(NumberFormatException ex) { dataform[1].setText(""); numeric = false; }
		try { max = Double.parseDouble(dataform[2].getText()); }
		catch(NumberFormatException ex) { dataform[2].setText(""); numeric = false; }
		try { card = Integer.parseInt(dataform[3].getText()); }
		catch(NumberFormatException ex) { dataform[3].setText(""); numeric = false; }

		if(!numeric) {
			XDialog.showMessage(dataform[3],"Not a numeric value");
			return false;
		}

		if(min>=max || card < 2) {
			XDialog.showMessage(dataform[3],"Invalid universe data");
			return false;
		}

		if(!copy.testUniverse(min,max,card)) {
			XDialog.showMessage(dataform[3],"Universe conflicts with defined MFs");
			return false;
		}

		try { copy.getUniverse().set(min,max,card); } catch( XflException ex) {}
		graph.repaint();
		return true;
	}

	/**
	 * Obtiene una copia a partir del tipo original	
	 */
	private void getCopy() {
		this.copy = (Type) this.original.clone();
		LinguisticLabel omf[] = this.original.getMembershipFunctions();
		LinguisticLabel cmf[] = this.copy.getMembershipFunctions();
		this.mapping = new XfeditTypeMapping[omf.length];
		for(int i=0; i<omf.length; i++)
			this.mapping[i] = new XfeditTypeMapping(omf[i],cmf[i]);
		if(this.graph != null) this.graph.setType(copy);
	}

	/**
	 * Cierra la ventana
	 */
	private void cancel() {
		original.setEditing(false);
		xfeditor.refresh();
		setVisible(false);
	}

	/**
	 * Actualiza la gráfica con la función seleccionada
	 */
	private void selectMF() {
		LinguisticLabel selected = null;
		try { selected = (LinguisticLabel) mflist.getSelectedValue(); }
		catch(Exception ex) {}
		graph.setExcluded( selected );
		graph.setSelection( selected );
		graph.repaint();
	}

	/**
	 * Crea una nueva función de pertenencia
	 */
	private void newMF() {
		XfeditMFEditor editor = new XfeditMFEditor(this,null);
		editor.setVisible(true);
	}

	/**
	 * Edita la función de pertenencia seleccionada
	 */
	private void editMF() {
		LinguisticLabel selection = (LinguisticLabel) mflist.getSelectedValue();
		if(selection == null) return;
		int local = copy.getMembershipFunctions().length;
		if(mflist.getSelectedIndex() >= local) return;
		XfeditMFEditor editor = new XfeditMFEditor(this,selection);
		editor.setVisible(true);
	}

	/**
	 *  Elimina la función de pertenencia seleccionada
	 */
	private void removeMF() {
		LinguisticLabel selection = (LinguisticLabel) mflist.getSelectedValue();
		if(selection == null) return;
		int local = copy.getMembershipFunctions().length;
		if(mflist.getSelectedIndex() >= local) return;
		LinguisticLabel origmf = null;
		for(int i=0; i<mapping.length; i++)
			if(mapping[i].copy == selection) origmf = mapping[i].orig;
		if(origmf != null && origmf.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove membership function "+selection+".";
			msg[1] = "There are rulebases using this function.";
			XDialog.showMessage(xfeditor,msg);
			return;
		}

		copy.remove(selection);
		for(int i=0; i<mapping.length; i++)
			if(mapping[i].copy == selection) mapping[i].copy = null;
		setMFList();
	}

	/**
	 * Crea una nueva familia de funciones de pertenencia
	 */
	private void newFamily() {
		XfeditFamilyEditor editor = new XfeditFamilyEditor(this,null);
		editor.setVisible(true);
	}

	/**
	 * Edita la familia de funciones de pertenencia seleccionada
	 */
	private void editFamily() {
		Family selection = (Family) famlist.getSelectedValue();
		if(selection == null) return;
		int local = copy.getFamilies().length;
		if(famlist.getSelectedIndex() >= local) return;
		XfeditFamilyEditor editor = new XfeditFamilyEditor(this,selection);
		editor.setVisible(true);
	}

	/**
	 * Elimina la familia de funciones de pertenencia seleccionada
	 */
	private void removeFamily() {
		Family selection = (Family) famlist.getSelectedValue();
		if(selection == null) return;
		int local = copy.getFamilies().length;
		if(famlist.getSelectedIndex() >= local) return;
		if(selection.isLinked()) {
			String msg[] = new String[2];
			msg[0] = "Cannot remove family "+selection+".";
			msg[1] = "There are membership functions using it.";
			XDialog.showMessage(xfeditor,msg);
			return;
		}

		copy.removeFamily(selection);
		setFamilyList();
	}

	/**
	 * Actualiza los cambios sobre el tipo original
	 */
	private boolean apply() {
		if(!getTypeName()) return false;
		if(!getUniverse()) return false;
		Universe cu = copy.getUniverse();
		Universe ou = original.getUniverse();
		original.setName(copy.getName());
		try { ou.set(cu.min(),cu.max(),cu.card()); } catch(Exception e) {}
		LinguisticLabel cmf[] = copy.getMembershipFunctions();
		for(int i=0; i<cmf.length; i++) {
			if(cmf[i] instanceof ParamMemFunc) ((ParamMemFunc) cmf[i]).u = ou;
		}
		original.setMembershipFunctions(cmf);
		original.setFamilies(copy.getFamilies());

		for(int i=0; i<mapping.length; i++) 
			if(mapping[i].orig != null) spec.exchange(mapping[i].orig,mapping[i].copy);

		spec.setModified(true);
		getCopy();
		setFamilyList();
		setMFList();
		return true;
	}

	/**
	 * Acción a realizar al introducir datos sobre los campos de texto
	 */
	private void textAction(int index) {
		switch(index) {
		case 0: if(getTypeName()) dataform[1].requestFieldFocus(); return;
		case 1: dataform[2].requestFieldFocus(); return;
		case 2: dataform[3].requestFieldFocus(); return;
		case 3: if(getUniverse()) dataform[0].requestFieldFocus(); return;
		}
	}
}

