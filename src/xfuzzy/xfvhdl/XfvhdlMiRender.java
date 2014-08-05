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

package xfuzzy.xfvhdl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import xfuzzy.util.XConstants;
import xfuzzy.xfsg.XfsgIcons;

/**
 * Clase que define las características con las que se verá el árbol que
 * representa al sistema difuso
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfvhdlMiRender extends JPanel implements TreeCellRenderer {

	/**
	 * Serial uid para evitar warnings
	 */
	private static final long serialVersionUID = 7187242510053519089L;

	/** 
	 * Botón con el icono 
	 */
	private JButton botonIcono = new JButton();

	/**
	 * Botón para indicar estado ok / no_ok
	 */
	private JButton botonok = new JButton();

	/**
	 * TextArea que contiene el texto del nodo
	 */
	JTextArea jta = new JTextArea();

	/**
	 * Crea un nuevo objeto MiRender.
	 */
	public XfvhdlMiRender() {
		add(botonIcono);
		botonIcono.setBackground(XConstants.textbackground);
		botonIcono.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(jta);
		jta.setBackground(XConstants.textbackground);
		jta.setPreferredSize(new Dimension(90, 15));
		// jta.setVerticalTextPosition( SwingConstants.TOP );
		jta.setAlignmentY(TOP_ALIGNMENT);
		add(botonok);
		botonok.setBackground(XConstants.textbackground);
		botonok.setBorder(new EmptyBorder(0, 0, 0, 0));
		setOpaque(false);
	}

	/**
	 * Implementación del método de la interface TreeCellRenderer
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object nodeInfo = node.getUserObject();
		// Pone el icono adecuado
		if (leaf) {
			if (nodeInfo instanceof XfvhdlFLC) {
				botonIcono.setIcon(XfsgIcons.rulebase);
				XfvhdlFLC aux = (XfvhdlFLC) nodeInfo;
				boolean t = aux.gettodo_relleno();
				if (t)
					botonok.setIcon(XfsgIcons.ok);
				else
					botonok.setIcon(XfsgIcons.no_ok);
			} else if (nodeInfo instanceof XfvhdlCrisp) {
				botonIcono.setIcon(XfsgIcons.crispblock);
				XfvhdlCrisp aux = (XfvhdlCrisp) nodeInfo;
				boolean t = aux.gettodo_relleno();
				if (t)
					botonok.setIcon(XfsgIcons.ok);
				else
					botonok.setIcon(XfsgIcons.no_ok);
			}
			// jta.setFont(new Font( "Dialog",Font.ITALIC,12 ));
		} else if (expanded) {
			if (nodeInfo instanceof String) {
				botonIcono.setIcon(XfsgIcons.dbase);
				boolean t = XfvhdlProperties.activar_boton_GMF;
				if (t)
					botonok.setIcon(XfsgIcons.ok);
				else
					botonok.setIcon(XfsgIcons.no_ok);
			} else if (nodeInfo instanceof XfvhdlWindow.Rama) {
				botonIcono.setIcon(XfsgIcons.ofolder);
				XfvhdlWindow.Rama aux = (XfvhdlWindow.Rama) nodeInfo;
				boolean t = aux.getCompleta();
				if (t)
					botonok.setIcon(XfsgIcons.ok);
				else
					botonok.setIcon(XfsgIcons.no_ok);
			}
			// jta.setFont(new Font( "Dialog",Font.BOLD,12 ));
		} else {
			if (nodeInfo instanceof String) {
				botonIcono.setIcon(XfsgIcons.dbase_d);
				boolean t = XfvhdlProperties.activar_boton_GMF;
				if (t)
					botonok.setIcon(XfsgIcons.ok);
				else
					botonok.setIcon(XfsgIcons.no_ok);
			} else if (nodeInfo instanceof XfvhdlWindow.Rama) {
				botonIcono.setIcon(XfsgIcons.folder);
				XfvhdlWindow.Rama aux = (XfvhdlWindow.Rama) nodeInfo;
				boolean t = aux.getCompleta();
				if (t)
					botonok.setIcon(XfsgIcons.ok);
				else
					botonok.setIcon(XfsgIcons.no_ok);
			}
			// jta.setFont(new Font( "Dialog",Font.BOLD,12 ));
		}
		jta.setFont(new Font("Dialog", Font.BOLD, 12));
		// Y el texto.
		jta.setText("     "
				+ ((DefaultMutableTreeNode) value).getUserObject().toString());
		if (selected) {
			// setBorder(new CompoundBorder(
			// new EtchedBorder(),new LineBorder(Color.GREEN)));
			setBorder((Border) new BevelBorder(BevelBorder.RAISED));
		} else {
			setBorder(new EmptyBorder(1, 1, 1, 1));
		}

		return this;
	}

}
