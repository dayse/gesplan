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
//		DIALOGO PARA CREAR UNA NUEVA SALIDA		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsim;

import xfuzzy.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo para crear una salida (plot o log) de un proceso de simulación
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimOutputDialog extends JDialog implements ActionListener {
	
	//----------------------------------------------------------------------------//
	//                            COSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603060L;
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfsimOutputDialog(Xfsim xfsim) {
		super(xfsim,"Xfsim",true);
		this.xfsim = xfsim;
		
		String lb[] = { "Insert new log file", "Insert new plot" };
		String cm[] = { "Logfile", "Plot" };
		XCommandForm form = new XCommandForm(lb,cm,this);
		form.setCommandWidth(200);
		
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(new XLabel("Select output to insert"));
		content.add(form);
		
		Point loc = xfsim.getLocationOnScreen();
		loc.x += 40; loc.y += 200;
		this.setLocation(loc);
		pack();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Interfaz ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Logfile")) insertLogfile();
		else if(command.equals("Plot")) insertPlot();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Crea un nuevo fichero de almacenamiento histórico
	 */
	private void insertLogfile() {
		setVisible(false);
		XfsimLog logfile = new XfsimLog(xfsim);
		XfsimLogDialog dialog = new XfsimLogDialog(xfsim,logfile);
		dialog.setVisible(true);
		if(dialog.getResult()) {
			xfsim.getConfiguration().output.add(logfile);
			xfsim.refreshConfig();
		}
	}
	
	/**
	 * Crea una nueva representación gráfica
	 */
	private void insertPlot() {
		setVisible(false);
		XfsimPlot plot = new XfsimPlot(xfsim);
		XfsimPlotDialog dialog = new XfsimPlotDialog(xfsim,plot);
		dialog.setVisible(true);
		if(dialog.getResult()) {
			xfsim.getConfiguration().output.add(plot);
			xfsim.refreshConfig();
		}
	}
}
