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

package xfuzzy.xftsp;

import java.awt.*;
import java.awt.event.*; // ActionListener
import javax.swing.*;

import xfuzzy.util.*;
import xfuzzy.XfuzzyIcons;

/**
 * Dialog for configuring xftsp. @TODO
 * 
 * @author fedemp
 **/
public class XftspDialog extends JFrame implements ActionListener, WindowListener {

    private XCommandForm form;
    private XTextForm[] txt;

    /**
     * Basic and only constructor.
     **/
    public XftspDialog() { }

    private void build() {

	String menu[] = {"Load", "Save", "Generate models", "Exit" };
	this.form = new XCommandForm(menu, menu, this);
	this.form.setCommandWidth(200);
	this.form.block();

	this.txt = new XTextForm[5];
	this.txt[0] = new XTextForm("Training series", this);
	this.txt[0].setActionCommand("Training");
	this.txt[0].setEditable(false);
	this.txt[1] = new XTextForm("Test series");
	this.txt[1].setActionCommand("Test");
	this.txt[1].setEditable(false);
	this.txt[2] = new XTextForm("Identification algorithm");
	this.txt[2].setActionCommand("IdAlgorithm");
	this.txt[2].setEditable(false);
	this.txt[3] = new XTextForm("Optimization algorithm");
	this.txt[3].setActionCommand("OptAlgorithm");
	this.txt[3].setEditable(false);
	XTextForm.setWidth(txt);

	Box box = new Box(BoxLayout.Y_AXIS);
	box.add(txt[0]);
	box.add(txt[1]);
	box.add(txt[2]);
	box.add(txt[3]);    

	Container cont = getContentPane();
	cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));
	cont.add(new XLabel("xftsp"));
	cont.add(Box.createVerticalStrut(5));
	cont.add(box);
	cont.add(Box.createVerticalStrut(5));
	cont.add(form);

	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	setIconImage( XfuzzyIcons.xfuzzy.getImage() );
	addWindowListener(this);
	pack();
	//setLocation();
    }

    public void printMessage(String m) {  }

    public XftspConfig askConfig()
    {
	XftspConfig cfg = new XftspConfig();

	return cfg;
    }

    private JPopupMenu idAlgorithm() {
	String algs[] = { "Wang & Mendel", "Incremental Grid", "Subtractive Clustering", "Fixed Clustering" };
	String commands[] = {"WnM", "IG", "SC", "FC"};

	return null;
    }

    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if(command.equals("Close")) actionClose();
	else if(command.equals("Save")) actionSave();
	else if(command.equals("Apply")) actionApply();
	else if(command.equals("Reload")) actionReload();
	else if(command.equals("Run")) actionRun();
    }

    private void actionClose() {
    }
    private void actionSave() {
    }
    private void actionApply() {
    }
    private void actionReload() {
    }
    private void actionRun() {
    }

    /* Override some methods in the WindowListener interface */
    public void windowActivated(WindowEvent e) {} 
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowClosing(WindowEvent e) { actionClose(); }
    public void windowIconified(WindowEvent e) {}
}