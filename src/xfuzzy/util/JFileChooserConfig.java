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


package xfuzzy.util;

import java.awt.Container;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;

public class JFileChooserConfig extends FileView {

	static private void setJFileChooserColor(JFileChooser chooser) {
		// Change file pane background color
		Container c = (Container) chooser.getComponent(2);
		c = (Container) c.getComponent(0);
		c = (Container) c.getComponent(0);
		c = (Container) c.getComponent(0);
		c = (Container) c.getComponent(0);
		c.setBackground(XConstants.textbackground);

		// Change filename box background color
		c = (Container) chooser.getComponent(3);
		c = (Container) c.getComponent(0);
		c = (Container) c.getComponent(1);
		c.setBackground(XConstants.textbackground);
	}

	static private void setJFileChooserFileView(JFileChooser chooser) {
		// Change file icons view
		chooser.setFileView(new XfuzzyFileView());
	}
	
	static public void configure(JFileChooser chooser) {
		// Configure file chooser
		setJFileChooserColor(chooser);
		setJFileChooserFileView(chooser);
	}
}

class XfuzzyFileView extends FileView {

	public Icon getIcon(File f) {
		Icon icon = null;
		if (f.getName().endsWith(".xfl"))
			icon = XFileIcons.xfuzzy;
		if (f.getName().endsWith(".pkg"))
			icon = XFileIcons.pkg;
		if (f.getName().endsWith(".cfg"))
			icon = XFileIcons.xfconfig;
		if (f.getName().endsWith(".xml"))
			icon = XFileIcons.xfconfig;
		return icon;
	}
}
